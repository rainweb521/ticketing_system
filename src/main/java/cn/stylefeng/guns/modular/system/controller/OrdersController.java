package cn.stylefeng.guns.modular.system.controller;

import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.model.Trains;
import cn.stylefeng.guns.modular.system.model.User;
import cn.stylefeng.guns.modular.system.service.ITrainsService;
import cn.stylefeng.guns.modular.system.service.IUserService;
import cn.stylefeng.guns.modular.system.transfer.OrdersDto;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import cn.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import cn.stylefeng.guns.modular.system.model.Orders;
import cn.stylefeng.guns.modular.system.service.IOrdersService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单控制器
 *
 * @author fengshuonan
 * @Date 2020-02-15 02:43:38
 */
@Controller
@RequestMapping("/orders")
public class OrdersController extends BaseController {

    private String PREFIX = "/system/orders/";

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private ITrainsService trainsService;

    @Autowired
    private IUserService userService;

    /**
     * 跳转到订单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "orders.html";
    }
    /**
     * 跳转到订单首页
     */
    @RequestMapping("/audit")
    public String audit() {
        return PREFIX + "audit.html";
    }
    /**
     * 跳转到添加订单
     */
    @RequestMapping("/orders_add")
    public String ordersAdd() {
        return PREFIX + "orders_add.html";
    }

    /**
     * 跳转到修改订单
     */
    @RequestMapping("/orders_update/{ordersId}")
    public String ordersUpdate(@PathVariable Integer ordersId, Model model) {
        Orders orders = ordersService.selectById(ordersId);
        model.addAttribute("item",orders);
        LogObjectHolder.me().set(orders);
        return PREFIX + "orders_edit.html";
    }
    /**
     * 跳转到改签订单
     */
    @RequestMapping("/orders_endorse/{ordersId}")
    public String orders_endorse(@PathVariable Integer ordersId, Model model) {
        Orders orders = ordersService.selectById(ordersId);
        Trains trains = trainsService.selectById(orders.getTrainsid());
        EntityWrapper<Orders> ordersWrapper = new EntityWrapper<>();
        ordersWrapper.eq("trainsid",trains.getId());
        ordersWrapper.eq("trainsdate",orders.getTrainsdate());
        trains.setMoreticket(trains.getMoreticket()-ordersService.selectList(ordersWrapper).stream().mapToInt(Orders::getVotes).sum());
        model.addAttribute("item",trains);
        Integer userId = ShiroKit.getUser().getId();
        model.addAttribute("user",userService.selectById(userId));
        model.addAttribute("orderid",ordersId);
        LogObjectHolder.me().set(orders);
        return PREFIX + "endorse.html";
    }

    /**
     * 获取订单列表
     * 凭借用户订单
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        //添加对用户的过滤
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        EntityWrapper<Orders> ordersWrapper = new EntityWrapper<>();
        ordersWrapper.like("ordernumber",condition);
        ordersWrapper.eq("userid",ShiroKit.getUser().getId());
        ordersService.selectList(ordersWrapper).forEach(item->{
            OrdersDto ordersdto = new OrdersDto();
            Trains trains = trainsService.selectById(item.getTrainsid());
            ordersdto.setId(item.getId());
            ordersdto.setName(userService.selectById(item.getUserid()).getName());
            ordersdto.setOrdernumber(item.getOrdernumber());
            ordersdto.setFlightno(trains.getFlightno());
            ordersdto.setOnsite(trains.getOnsite());
            ordersdto.setArrivedsite(trains.getArrivedsite());
            ordersdto.setFares(trains.getFares());
            ordersdto.setVotes(item.getVotes());
            ordersdto.setMoney(trains.getFares() * item.getVotes());
            ordersdto.setTrainsdate(item.getTrainsdate());
            ordersdto.setStatusStr(item.getStatusStr());
            ordersdto.setAdddate(item.getAdddate());
            ordersdto.setUpdatedate(item.getUpdatedate());
            ordersdto.setSreviewstatusStr(item.getSreviewstatusStr());
            ordersdto.setSreviewstatus(item.getSreviewstatus());

            ordersDtoList.add(ordersdto);

        });
        return ordersDtoList;
    }
    /**
     * 审核订单列表
     */
    @RequestMapping(value = "/list/audit")
    @ResponseBody
    public Object listaudit(String condition) {
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        EntityWrapper<Orders> ordersWrapper = new EntityWrapper<>();
        ordersWrapper.like("ordernumber",condition);
        ordersService.selectList(ordersWrapper).forEach(item->{
            OrdersDto ordersdto = new OrdersDto();
            Trains trains = trainsService.selectById(item.getTrainsid());
            ordersdto.setId(item.getId());
            ordersdto.setName(userService.selectById(item.getUserid()).getName());
            ordersdto.setOrdernumber(item.getOrdernumber());
            ordersdto.setFlightno(trains.getFlightno());
            ordersdto.setOnsite(trains.getOnsite());
            ordersdto.setArrivedsite(trains.getArrivedsite());
            ordersdto.setFares(trains.getFares());
            ordersdto.setVotes(item.getVotes());
            ordersdto.setMoney(trains.getFares() * item.getVotes());
            ordersdto.setTrainsdate(item.getTrainsdate());
            ordersdto.setStatusStr(item.getStatusStr());
            ordersdto.setAdddate(item.getAdddate());
            ordersdto.setUpdatedate(item.getUpdatedate());
            ordersdto.setSreviewstatusStr(item.getSreviewstatusStr());
            ordersdto.setSreviewstatus(item.getSreviewstatus());

            ordersDtoList.add(ordersdto);

        });
        return ordersDtoList.stream().filter(item->item.getSreviewstatus()==1).collect(Collectors.toList());
    }

    /**
     * 新增订单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Orders orders) {
        ordersService.insert(orders);
        return SUCCESS_TIP;
    }

    /**
     * 退票订单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer ordersId) {
        Orders orders = ordersService.selectById(ordersId);
        orders.setStatus(3);
        orders.setStatusStr("已退票");
        //归还用户金额
        Double money = orders.getVotes() * trainsService.selectById(orders.getTrainsid()).getFares();
        Integer userId = ShiroKit.getUser().getId();
        User user = userService.selectById(userId);
        user.setBalance(user.getBalance() + money);
        userService.updateById(user);
        //归还票数
        orders.setVotes(0);
        orders.setSreviewstatus(1);
        orders.setSreviewstatusStr("待审核");
        ordersService.updateById(orders);
//        ordersService.deleteById(ordersId);
        return SUCCESS_TIP;
    }
    /**
     * 审核通过订单
     */
    @RequestMapping(value = "/delete/audit")
    @ResponseBody
    public Object deleteaudit(@RequestParam Integer ordersId) {
        Orders orders = ordersService.selectById(ordersId);
        orders.setSreviewstatus(0);
        orders.setSreviewstatusStr("已审核");
        ordersService.updateById(orders);

        return SUCCESS_TIP;
    }

    /**
     * 修改订单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Orders orders) {
        ordersService.updateById(orders);
        return SUCCESS_TIP;
    }

    /**
     * 订单详情
     */
    @RequestMapping(value = "/detail/{ordersId}")
    @ResponseBody
    public Object detail(@PathVariable("ordersId") Integer ordersId) {
        return ordersService.selectById(ordersId);
    }
}
