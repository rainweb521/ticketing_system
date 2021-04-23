/**
 * 订单管理初始化
 */
var Orders = {
    id: "OrdersTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Orders.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '订单号', field: 'ordernumber', visible: true, align: 'center', valign: 'middle'},
            {title: '班次号', field: 'flightno', visible: true, align: 'center', valign: 'middle'},
            {title: '出发地', field: 'onsite', visible: true, align: 'center', valign: 'middle'},
            {title: '目的地', field: 'arrivedsite', visible: true, align: 'center', valign: 'middle'},
            {title: '票价', field: 'fares', visible: true, align: 'center', valign: 'middle'},
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
Orders.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Orders.seItem = selected[0];
        // 这里加入检查能否改签和退票的判断
        // 1 已经改签的不可以再改签
        // 2 已经退票的不可以再退票
        // 3 已经超过出发日期的，不可以改签和退票
        // 4 未审核通过的禁止操作

        if (Orders.seItem.sreviewstatusStr=="待审核"){
            Feng.info("未审核通过的禁止操作！");
            return false;
        }
        if (Orders.seItem.statusStr=="已退票"){
            Feng.info("已经退票的不可以再操作！");
            return false;
        }



        // 获取当前日期
        var date = new Date();
        // 获取当前月份
        var nowMonth = date.getMonth() + 1;
        // 获取当前是几号
        var strDate = date.getDate();
        // 添加分隔符“-”
        var seperator = "-";
        // 对月份进行处理，1-9月在前面添加一个“0”
        if (nowMonth >= 1 && nowMonth <= 9) {
            nowMonth = "0" + nowMonth;
        }
        // 对月份进行处理，1-9号在前面添加一个“0”
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        // 最后拼接字符串，得到一个格式为(yyyy-MM-dd)的日期
        var nowDate = date.getFullYear() + seperator + nowMonth + seperator + strDate;
        if (nowDate>Orders.seItem.trainsdate){
            Feng.info("已经超过出发日期的，不可以改签和退票！");
            return false;
        }

        return true;
    }
};

/**
 * 点击添加订单
 */
Orders.openAddOrders = function () {
    var index = layer.open({
        type: 2,
        title: '添加订单',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/orders/orders_add'
    });
    this.layerIndex = index;
};

/**
 * 改签
 */
Orders.openOrdersDetail = function () {
    if (this.check()) {
        if (Orders.seItem.statusStr=="已改签"){
            Feng.info("已经改签的不可以再改签！");
            return ;
        }
        var index = layer.open({
            type: 2,
            title: '订单详情',
            area: ['800px', '620px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/orders/orders_endorse/' + Orders.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 退票订单
 */
Orders.delete = function () {
    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/orders/delete", function (data) {
                Feng.success("退票成功!");
                Orders.table.refresh();
            }, function (data) {
                Feng.error("退票失败!" + data.responseJSON.message + "!");
            });
            ajax.set("ordersId",Orders.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否退票" + "?",operation);
    }
};

/**
 * 查询订单列表
 */
Orders.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Orders.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Orders.initColumn();
    var table = new BSTable(Orders.id, "/orders/list", defaultColunms);
    table.setPaginationType("client");
    Orders.table = table.init();
});
