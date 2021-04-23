package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author wcy
 * @since 2020-02-15
 */
@TableName("sys_orders")
public class Orders extends Model<Orders> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单号
     */
    private String ordernumber;
    /**
     * 车次id
     */
    private Integer trainsid;
    /**
     * 订单状态str
     */
    @TableField("status_str")
    private String statusStr;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 用户id
     */
    private Integer userid;
    /**
     * 添加日期
     */
    private Date adddate;
    /**
     * 修改日期
     */
    private Date updatedate;
    /**
     * 审核状态
     */
    private Integer sreviewstatus;
    /**
     * 票数
     */
    private Integer votes;
    /**
     * 审核状态str
     */
    @TableField("sreviewstatus_str")
    private String sreviewstatusStr;

    /**
     * 购买车次日期
     */
    private String trainsdate;

    public String getTrainsdate() {
        return trainsdate;
    }

    public void setTrainsdate(String trainsdate) {
        this.trainsdate = trainsdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public Integer getTrainsid() {
        return trainsid;
    }

    public void setTrainsid(Integer trainsid) {
        this.trainsid = trainsid;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getAdddate() {
        return adddate;
    }

    public void setAdddate(Date adddate) {
        this.adddate = adddate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Integer getSreviewstatus() {
        return sreviewstatus;
    }

    public void setSreviewstatus(Integer sreviewstatus) {
        this.sreviewstatus = sreviewstatus;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getSreviewstatusStr() {
        return sreviewstatusStr;
    }

    public void setSreviewstatusStr(String sreviewstatusStr) {
        this.sreviewstatusStr = sreviewstatusStr;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Orders{" +
        ", id=" + id +
        ", ordernumber=" + ordernumber +
        ", trainsid=" + trainsid +
        ", statusStr=" + statusStr +
        ", status=" + status +
        ", userid=" + userid +
        ", adddate=" + adddate +
        ", updatedate=" + updatedate +
        ", sreviewstatus=" + sreviewstatus +
        ", votes=" + votes +
        ", sreviewstatusStr=" + sreviewstatusStr +
        "}";
    }
}
