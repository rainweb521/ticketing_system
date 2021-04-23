laydate.render({
    elem: '#adddate'
});
/**
 * 通知管理初始化
 */
var TableVar = {
    id: "Table",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
TableVar.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '发车时间', field: 'departuretime', visible: true, align: 'center', valign: 'middle'},
        {title: '出发站点', field: 'onsite', visible: true, align: 'center', valign: 'middle'},
        {title: '达到站点', field: 'arrivedsite', visible: true, align: 'center', valign: 'middle'},
        {title: '车型', field: 'models', visible: true, align: 'center', valign: 'middle'},
        {title: '余票', field: 'moreticket', visible: true, align: 'center', valign: 'middle'},
        {title: '票价', field: 'fares', visible: true, align: 'center', valign: 'middle'},
        {title: '车牌号', field: 'licenseplate', visible: true, align: 'center', valign: 'middle'},
        {title: '班次号', field: 'flightno', visible: true, align: 'center', valign: 'middle'},
        {title: '里程', field: 'mileage', visible: true, align: 'center', valign: 'middle'},
        {title: '座位类型', field: 'seatyype', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
TableVar.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        TableVar.seItem = selected[0];
        return true;
    }
};

/**
 * 点击
 */
TableVar.openAddHouse = function () {
    if (this.check()) {

        var index = layer.open({
            type: 2,
            title: '确认买票信息',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/trains/buyuser/'+TableVar.seItem.id+"?moreticket="+TableVar.seItem.moreticket
                                +"&trainsdate="+$("#adddate").val()
        });
        this.layerIndex = index;
    }
};


/**
 * 查询列表
 */
TableVar.search = function () {
    var date = new Date();
    var nowMonth = date.getMonth() + 1;
    var strDate = date.getDate();
    var seperator = "-";
    if (nowMonth >= 1 && nowMonth <= 9) {
        nowMonth = "0" + nowMonth;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var nowDate = date.getFullYear() + seperator + nowMonth + seperator + strDate;
    if (nowDate>$("#adddate").val()){
        Feng.info("请选择今天及以后的车次！");
        return false;
    }

    var queryData = {};
    queryData['onsite'] = $("#onsite").val();
    queryData['arrivedsite'] = $("#arrivedsite").val();
    queryData['adddate'] = $("#adddate").val();
    // alert($("#condition").val());
    TableVar.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = TableVar.initColumn();
    var table = new BSTable(TableVar.id, "", defaultColunms);
    var table = new BSTable(TableVar.id, "/trains/buylist", defaultColunms);
    table.setPaginationType("client");
    TableVar.table = table.init();


});