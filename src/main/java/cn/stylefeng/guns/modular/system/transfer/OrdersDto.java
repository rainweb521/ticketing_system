package cn.stylefeng.guns.modular.system.transfer;

import cn.stylefeng.guns.modular.system.model.Orders;
import cn.stylefeng.guns.modular.system.model.Trains;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: wcy
 * @Date: 2020/2/15
 */
public class OrdersDto extends Orders {

    private String name;

    /**
     * 总价
     */
    private Double money;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date adddate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
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

}
