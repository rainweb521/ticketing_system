/**
 * 初始化车票购买详情对话框
 */
var TrainsInfoDlg = {
    trainsInfoData : {}
};

/**
 * 清除数据
 */
TrainsInfoDlg.clearData = function() {
    this.trainsInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TrainsInfoDlg.set = function(key, val) {
    this.trainsInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TrainsInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
TrainsInfoDlg.close = function() {
    parent.layer.close(window.parent.Trains.layerIndex);
}

/**
 * 收集数据
 */
TrainsInfoDlg.collectData = function() {
    this
    .set('id')
    .set('departuretime')
    .set('onsite')
    .set('arrivedsite')
    .set('models')
    .set('moreticket')
    .set('fares')
    .set('licenseplate')
    .set('flightno')
    .set('mileage')
    .set('seatyype')
    .set('status')
    .set('adddate');
}

/**
 * 提交添加
 */
TrainsInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/trains/add", function(data){
        Feng.success("添加成功!");
        window.parent.Trains.table.refresh();
        TrainsInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.trainsInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
TrainsInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/trains/update", function(data){
        Feng.success("修改成功!");
        window.parent.Trains.table.refresh();
        TrainsInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.trainsInfoData);
    ajax.start();
}

$(function() {

});
