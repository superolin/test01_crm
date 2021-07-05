layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    //对"添加"或"编辑"弹出层的"确认"按钮触发进行绑定事件
    form.on("submit(addOrUpdateRole)", function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //弹出loading
        var url=ctx + "/role/save";         //如果从页面未获取到id, 则表示为"新增"操作.url访问"/role/save"
        if($("input[name='id']").val()){
            url=ctx + "/role/update";       //如果从页面获得了id, 则表示为"新增"操作.url访问"/role/update"
        }
        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(
                    res.msg, {
                        icon: 5
                    }
                );
            }
        });
        //阻止提交
        return false;
    });

    /**
     * "取消"按钮绑定事件
     */
    $("#closeBtn").click(function (){
        //假设这是iframe页
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });


});