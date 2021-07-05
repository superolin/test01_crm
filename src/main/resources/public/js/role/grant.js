var zTreeObj;
$(function () {
    loadModuleInfo();
});


function loadModuleInfo() {
    //发送ajax
    $.ajax({
        type: "post",
        url: ctx + "/module/queryModule",
        dataType: "json",
        //无回显作用
        // data: {},
        //选中role角色时, 传回roleId参数, 访问url, 查询该role拥有的权限, 然后使得checked=true, 实现回显效果.
        // (先查询所有资源treeDto(zTree要求返回格式),通过getId得到权限资源id, 然后查询选中的role所拥有的资源id集合,经过比对, contains比对.)
        data: {"roleId":$("#roleId").val()},
        success: function (data) {
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                view: {
                    showLine: false
                    // showIcon: false
                },
                check: {
                    enable: true,
                    chkboxType: {"Y": "ps", "N": "ps"}
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            var zNodes = data;
            zTreeObj = $.fn.zTree.init($("#test1"), setting, zNodes);
        }
    });
}


/**
 * 当点击ztree的复选框，触发的函数
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnCheck(event, treeId, treeNode) {
    //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
    var nodes = zTreeObj.getCheckedNodes(true);
    //console.log(nodes);
    //nodes==>>mid,roleId,name,checked
    //nodes是treeModel的集合
    var roleId = $("#roleId").val();
    //获取mid
    var mIds = "mIds=";//点击ztree的复选框,新选中的node得到的资源id集合
    //遍历
    for (var x in nodes) {
        if (x < nodes.length - 1) {
            mIds = mIds + nodes[x].id + "&mIds=";
        } else {
            mIds += nodes[x].id;
        }
    }
    console.log(mIds);
    //发送ajax批量添加
    $.ajax({
        type: "post",
        data: mIds + "&roleId=" + roleId,
        url: ctx + "/role/addGrant",
        dataType: "json",
        success: function (data) {
            console.log(data);
            alert(data.msg);
        }

    });
};
