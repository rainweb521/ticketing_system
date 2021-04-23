/**
 * 车次管理初始化
 */
var Trains = {
    id: "TrainsTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Trains.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '发车时间', field: 'departuretime', visible: true, align: 'center', valign: 'middle'},
            {title: '出发站点', field: 'onsite', visible: true, align: 'center', valign: 'middle'},
            {title: '达到站点', field: 'arrivedsite', visible: true, align: 'center', valign: 'middle'},
            {title: '车型', field: 'models', visible: true, align: 'center', valign: 'middle'},
            {title: '余票', field: 'moreticket', visible: true, align: 'center', valign: 'middle'},
            {title: '票价', field: 'fares', visible: true, align: 'center', valign: 'middle'},
            {title: '车牌号', field: 'licenseplate', visible: true, align: 'center', valign: 'middle'},
            {title: '班次号', field: 'flightno', visible: true, align: 'center', valign: 'middle'},
            {title: '里程', field: 'mileage', visible: true, align: 'center', valign: 'middle'},
            {title: '座位类型', field: 'seatyype', visible: true, align: 'center', valign: 'middle'},
            {title: '添加日期', field: 'adddate', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Trains.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Trains.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加车次
 */
Trains.openAddTrains = function () {
    var index = layer.open({
        type: 2,
        title: '添加车次',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/trains/trains_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看车次详情
 */
Trains.openTrainsDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '车次详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/trains/trains_update/' + Trains.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除车次
 */
Trains.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/trains/delete", function (data) {
            Feng.success("删除成功!");
            Trains.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("trainsId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询车次列表
 */
Trains.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Trains.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Trains.initColumn();
    var table = new BSTable(Trains.id, "/trains/list", defaultColunms);
    table.setPaginationType("client");
    Trains.table = table.init();
});
