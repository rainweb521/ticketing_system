/**
 * 订单管理初始化
 */
var OrdersAudit = {
    id: "AuditTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
OrdersAudit.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '订单号', field: 'ordernumber', visible: true, align: 'center', valign: 'middle'},
            {title: '班次号', field: 'flightno', visible: true, align: 'center', valign: 'middle'},
            {title: '用户名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '出发地', field: 'onsite', visible: true, align: 'center', valign: 'middle'},
            {title: '目的地', field: 'arrivedsite', visible: true, align: 'center', valign: 'middle'},
            // {title: '票价', field: 'fares', visible: true, align: 'center', valign: 'middle'},
            {title: '票数', field: 'votes', visible: true, align: 'center', valign: 'middle'},
            {title: '总价', field: 'money', visible: true, align: 'center', valign: 'middle'},
            {title: '出发日期', field: 'trainsdate', visible: true, align: 'center', valign: 'middle'},
            {title: '订单状态', field: 'statusStr', visible: true, align: 'center', valign: 'middle'},
            {title: '添加日期', field: 'adddate', visible: true, align: 'center', valign: 'middle'},
            {title: '审核状态', field: 'sreviewstatusStr', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
OrdersAudit.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{

        return true;
    }
};




/**
 * 审核订单
 */
OrdersAudit.audit = function () {
    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/orders/delete/audit", function (data) {
                Feng.success("通过成功!");
                OrdersAudit.table.refresh();
            }, function (data) {
                Feng.error("通过失败!" + data.responseJSON.message + "!");
            });
            ajax.set("ordersId",OrdersAudit.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否审核通过" + "?",operation);
    }
};

/**
 * 查询订单列表
 */
OrdersAudit.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    OrdersAudit.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = OrdersAudit.initColumn();
    var table = new BSTable(OrdersAudit.id, "/orders/list/audit", defaultColunms);
    table.setPaginationType("client");
    OrdersAudit.table = table.init();
});
