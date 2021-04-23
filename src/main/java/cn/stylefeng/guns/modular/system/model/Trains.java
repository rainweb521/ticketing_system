package cn.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 车次表
 * </p>
 *
 * @author wcy
 * @since 2020-02-15
 */
@TableName("sys_trains")
public class Trains extends Model<Trains> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 发车时间
     */
    private String departuretime;
    /**
     * 出发站点
     */
    private String onsite;
    /**
     * 达到站点
     */
    private String arrivedsite;
    /**
     * 车型
     */
    private String models;
    /**
     * 余票
     */
    private Integer moreticket;
    /**
     * 票价
     */
    private Double fares;
    /**
     * 车牌号
     */
    private String licenseplate;
    /**
     * 班次号
     */
    private String flightno;
    /**
     * 里程
     */
    private String mileage;
    /**
     * 座位类型
     */
    private String seatyype;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 添加日期
     */
    private Date adddate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeparturetime() {
        return departuretime;
    }

    public void setDeparturetime(String departuretime) {
        this.departuretime = departuretime;
    }

    public String getOnsite() {
        return onsite;
    }

    public void setOnsite(String onsite) {
        this.onsite = onsite;
    }

    public String getArrivedsite() {
        return arrivedsite;
    }

    public void setArrivedsite(String arrivedsite) {
        this.arrivedsite = arrivedsite;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public Integer getMoreticket() {
        return moreticket;
    }

    public void setMoreticket(Integer moreticket) {
        this.moreticket = moreticket;
    }

    public Double getFares() {
        return fares;
    }

    public void setFares(Double fares) {
        this.fares = fares;
    }

    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public String getFlightno() {
        return flightno;
    }

    public void setFlightno(String flightno) {
        this.flightno = flightno;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getSeatyype() {
        return seatyype;
    }

    public void setSeatyype(String seatyype) {
        this.seatyype = seatyype;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAdddate() {
        return adddate;
    }

    public void setAdddate(Date adddate) {
        this.adddate = adddate;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Trains{" +
        ", id=" + id +
        ", departuretime=" + departuretime +
        ", onsite=" + onsite +
        ", arrivedsite=" + arrivedsite +
        ", models=" + models +
        ", moreticket=" + moreticket +
        ", fares=" + fares +
        ", licenseplate=" + licenseplate +
        ", flightno=" + flightno +
        ", mileage=" + mileage +
        ", seatyype=" + seatyype +
        ", status=" + status +
        ", adddate=" + adddate +
        "}";
    }
}
