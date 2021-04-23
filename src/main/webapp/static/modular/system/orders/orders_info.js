/**
 * 初始化订单详情对话框
 */
var OrdersInfoDlg = {
    ordersInfoData : {}
};

/**
 * 清除数据
 */
OrdersInfoDlg.clearData = function() {
    this.ordersInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrdersInfoDlg.set = function(key, val) {
    this.ordersInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrdersInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
OrdersInfoDlg.close = function() {
    parent.layer.close(window.parent.Orders.layerIndex);
}

/**
 * 收集数据
 */
OrdersInfoDlg.collectData = function() {
    this
    .set('id')
    .set('ordernumber')
    .set('trainsid')
    .set('statusStr')
    .set('status')
    .set('userid')
    .set('adddate')
    .set('updatedate')
    .set('sreviewstatus')
    .set('votes')
    .set('sreviewstatusStr');
}

/**
 * 提交添加
 */
OrdersInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/orders/add", function(data){
        Feng.success("添加成功!");
        window.parent.Orders.table.refresh();
        OrdersInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ordersInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
OrdersInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/orders/update", function(data){
        Feng.success("修改成功!");
        window.parent.Orders.table.refresh();
        OrdersInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ordersInfoData);
    ajax.start();
}

$(function() {

});
