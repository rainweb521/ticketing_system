package cn.stylefeng.guns.modular.system.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.common.constant.state.Order;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.model.Orders;
import cn.stylefeng.guns.modular.system.model.User;
import cn.stylefeng.guns.modular.system.service.IOrdersService;
import cn.stylefeng.guns.modular.system.service.IUserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import cn.stylefeng.guns.modular.system.model.Trains;
import cn.stylefeng.guns.modular.system.service.ITrainsService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 车次管理控制器
 *
 * @author fengshuonan
 * @Date 2020-02-15 01:32:38
 */
@Controller
@RequestMapping("/trains")
public class TrainsController extends BaseController {

    private String PREFIX = "/system/trains/";

    @Autowired
    private ITrainsService trainsService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IUserService userService;

    /**
     * 跳转到车次管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "trains.html";
    }
    /**
     * 跳转到车票购买页
     */
    @RequestMapping("/buy")
    public String buy() {
        return PREFIX + "buytrains.html";
    }

    /**
     * 跳转到添加车次管理
     */
    @RequestMapping("/trains_add")
    public String trainsAdd() {
        return PREFIX + "trains_add.html";
    }

    /**
     * 跳转到修改车次管理
     */
    @RequestMapping("/trains_update/{trainsId}")
    public String trainsUpdate(@PathVariable Integer trainsId, Model model) {
        Trains trains = trainsService.selectById(trainsId);
        model.addAttribute("item",trains);
        LogObjectHolder.me().set(trains);
        return PREFIX + "trains_edit.html";
    }
    /**
     * 图表显示
     */
    @RequestMapping("/bar")
    public String bar(Model model) {
        List<Trains> trainsList = new ArrayList<>();

        trainsService.selectList(null).forEach(item->{
            Trains trains = new Trains();
            trains.setFlightno(item.getFlightno());
            EntityWrapper<Orders> ordersWrapper = new EntityWrapper<>();
            ordersWrapper.eq("trainsid",item.getId());
            trains.setId(ordersService.selectList(ordersWrapper).stream().mapToInt(Orders::getVotes).sum());
            trainsList.add(trains);
        });
        List<Trains> thread = trainsList.stream().sorted((p1, p2) -> p1.getId().compareTo(p2.getId())).limit(3).collect(Collectors.toList());
        Map<String,Object> monthmap = new HashMap<>();
        monthmap.put("month1",thread.get(0).getFlightno());
        monthmap.put("month2",thread.get(1).getFlightno());
        monthmap.put("month3",thread.get(2).getFlightno());
        monthmap.put("data1",thread.get(0).getId());
        monthmap.put("data2",thread.get(1).getId());
        monthmap.put("data3",thread.get(2).getId());

        model.addAttribute("map",monthmap);
        return PREFIX + "bar.html";
    }

    /**
     * 获取车次管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return trainsService.selectList(null);
    }
    /**
     * 获取单独车次
     */
    @RequestMapping(value = "/view")
    @ResponseBody
    public Object view(String flightno,String trainsdate) {
        EntityWrapper<Trains> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("flightno",flightno);
        Trains trains = trainsService.selectOne(entityWrapper);
        if (trains==null){
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        EntityWrapper<Orders> ordersWrapper = new EntityWrapper<>();
        ordersWrapper.eq("trainsid",trains.getId());
        ordersWrapper.eq("trainsdate",trainsdate);
            trains.setMoreticket(trains.getMoreticket()-ordersService.selectList(ordersWrapper).stream().mapToInt(Orders::getVotes).sum());
        return trains;
    }
    /**
     * 获取要购买车次列表
     */
    @RequestMapping(value = "/buylist")
    @ResponseBody
    public Object list(String adddate,String arrivedsite,String onsite) {
        if (StrUtil.isEmpty(adddate)||StrUtil.isEmpty(arrivedsite)||StrUtil.isEmpty(onsite)){
            return new ArrayList<>();
        }
        EntityWrapper<Trains> entityWrapper = new EntityWrapper<>();
        entityWrapper.like("onsite",onsite);
        entityWrapper.like("arrivedsite",arrivedsite);
        List<Trains> list = trainsService.selectList(entityWrapper);
        EntityWrapper<Orders> ordersWrapper = new EntityWrapper<>();
        list.forEach(item->{
            ordersWrapper.eq("trainsid",item.getId());
            ordersWrapper.eq("trainsdate",adddate);
            item.setMoreticket(item.getMoreticket()-ordersService.selectList(ordersWrapper).stream().mapToInt(Orders::getVotes).sum());
        });
        return list;
    }

    /**
     * 跳转到确认购买车票
     */
    @RequestMapping(value = "/buyuser/{trainsId}",method = RequestMethod.GET)
    public String buyUser(@PathVariable Integer trainsId,Integer moreticket, String trainsdate,Model model) {
        Trains trains = trainsService.selectById(trainsId);
        trains.setMoreticket(moreticket);
        model.addAttribute("item",trains);
        LogObjectHolder.me().set(trains);
        Integer userId = ShiroKit.getUser().getId();
        model.addAttribute("user",userService.selectById(userId));
        model.addAttribute("trainsdate",trainsdate);
        return PREFIX + "buyuser.html";
    }

    /**
     * 确认购买车票
     */
    @RequestMapping(value = "/buyuser",method = RequestMethod.POST)
    @ResponseBody
    public Object buyUser(Integer id,Integer votes,Integer moreticket,String trainsdate) {
        Trains trains = trainsService.selectById(id);
        Double price = trains.getFares() * votes;
        Integer userId = ShiroKit.getUser().getId();
        User user = userService.selectById(userId);
        //判断余额
        if(user.getBalance()==null){
            user.setBalance(0.0);
        }
        if (user.getBalance()<price){
            throw new ServiceException(BizExceptionEnum.REQUEST_BALANCE);
        }
        //判断余票
        if (votes>moreticket){
            throw new ServiceException(BizExceptionEnum.REQUEST_VOTES);
        }
        user.setBalance(user.getBalance()-price);
        userService.updateById(user);
        //添加订单信息
        Orders orders = new Orders();
        orders.setTrainsid(id);
        orders.setAdddate(new Date());
        orders.setOrdernumber(String.valueOf(System.currentTimeMillis()));
        orders.setUpdatedate(new Date());
        orders.setTrainsdate(trainsdate);
        orders.setVotes(votes);
        orders.setUserid(userId);
        orders.setStatus(1);
        orders.setStatusStr("已购买");
        orders.setSreviewstatus(1);
        orders.setSreviewstatusStr("待审核");
        ordersService.insert(orders);
        return SUCCESS_TIP;
    }
    /**
     * 确认改签车票
     * @param id 车次的id
     * @param votes 购买票数
     * @param moreticket 余票数
     * @param trainsdate 车次日期
     * @param orderid 订单ID
     * @return
     */
    @RequestMapping(value = "/endorseuser",method = RequestMethod.POST)
    @ResponseBody
    public Object endorseUser(Integer id,Integer votes,Integer moreticket,String trainsdate,Integer orderid) {

        Orders old_Orders = ordersService.selectById(orderid);
        Trains old_trains = trainsService.selectById(old_Orders.getTrainsid());
        Integer userId = ShiroKit.getUser().getId();
        User user = userService.selectById(userId);
        //先将之前的钱还给用户
        Double old_money = old_Orders.getVotes() * old_trains.getFares();
        user.setBalance(user.getBalance()+old_money);
        /**
         * 因为票数的统计是根据订单来计算的，暂时不用还票数，所以改签最好不要改签本车次，这样就无法计算，毕竟本次车次
         * 再继续累加票数是没有意义的。
         */
        Trains trains = trainsService.selectById(id);
        Double price = trains.getFares() * votes;
        //判断余额
        if(user.getBalance()==null){
            user.setBalance(0.0);
        }
        if (user.getBalance()<price){
            throw new ServiceException(BizExceptionEnum.REQUEST_BALANCE);
        }
        //判断余票
        if (votes>moreticket){
            throw new ServiceException(BizExceptionEnum.REQUEST_VOTES);
        }
        user.setBalance(user.getBalance()-price);
        userService.updateById(user);
        //修改订单信息
//        Orders orders = new Orders();
        old_Orders.setTrainsid(id);
//        old_Orders.setAdddate(new Date());
//        old_Orders.setOrdernumber(String.valueOf(System.currentTimeMillis()));
        old_Orders.setUpdatedate(new Date());
        old_Orders.setTrainsdate(trainsdate);
        old_Orders.setVotes(votes);
//        orders.setUserid(userId);
        old_Orders.setStatus(2);
        old_Orders.setStatusStr("已改签");
        old_Orders.setSreviewstatus(1);
        old_Orders.setSreviewstatusStr("待审核");
        ordersService.updateById(old_Orders);

        return SUCCESS_TIP;
    }

    /**
     * 新增车次管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Trains trains) {
        trains.setAdddate(new Date());
        trains.setStatus(1);
        trainsService.insert(trains);
        return SUCCESS_TIP;
    }

    /**
     * 删除车次管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer trainsId) {
        trainsService.deleteById(trainsId);
        return SUCCESS_TIP;
    }

    /**
     * 修改车次管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Trains trains) {
        trainsService.updateById(trains);
        return SUCCESS_TIP;
    }

    /**
     * 车次管理详情
     */
    @RequestMapping(value = "/detail/{trainsId}")
    @ResponseBody
    public Object detail(@PathVariable("trainsId") Integer trainsId) {
        return trainsService.selectById(trainsId);
    }
}
