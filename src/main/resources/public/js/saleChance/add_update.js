layui.use(['form', 'jquery', 'jquery_cookie', 'table'], function () {
    var form = layui.form,
        layer = layui.layer,
        table = layui.table,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
     * 触发表单
     */
    //营销机会添加操作管理表中, 确认button标签的lay-filter="addOrUpdateSaleChance"
    //submit(addOrUpdateSaleChance)
    form.on("submit(addOrUpdateSaleChance)", function (obj) {
        var dataField = obj.field;
        console.log(obj.field + "<<<<");

        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...", {
                icon: 16, // 图标
                time: false, // 不关闭
                shade: 0.8 // 设置遮罩的透明度
            });
        var url = ctx + "/sale_chance/save";

        //获取隐藏域信息，决定添加或者是修改
        //该id为营销机会id
        //存在即为真,不存在id表示新增操作, 存在id表示为修改操作
        if ($("input[name=id]").val()) {
            url = ctx + "/sale_chance/update";
        }
        /**
         * 发送ajax
         */
        $.ajax({
            type: "post",
            url: url,
            data: obj.field,
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    //添加成功了
                    layer.msg("添加成功了", {icon: 6});
                    //关闭加载层
                    layer.close(index);
                    //关闭iframe;
                    layer.closeAll("iframe");
                    //刷新父目录
                    parent.location.reload();
                } else {
                    //添加失败
                    layer.msg(data.msg, {icon: 5});//将老师的ms修改为了msg
                }
            }
        });
        //阻止表单提交
        return false;
    });


    /**
     * 营销机会添加表取消按钮
     */
    //营销机会添加表最下方的取消按钮
    //<button class="layui-btn layui-btn-lg layui-btn-normal" id="closeBtn">取消</button>
    //button的取消按钮,通过id选择器绑定click事件
    $("#closeBtn").click(function () {
        //假设这是iframe页
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });


    /*动态的追加下拉框*/
    $(function(){
        //获取当前对象的指派人(销售人员)的id
        var assignMan=$("input[name=assMan]").val();
        $.ajax({
            type:"post",
            url:ctx+"/sale_chance/querySales",
            dataType:"json",
            success:function (data){
                //遍历
                for( var  x in data){
                    if(assignMan==data[x].id){
                        $("#assignMan").append(" <option selected value='"+data[x].id+"'>"+data[x].uname+"</option>");
                    }else{
                        $("#assignMan").append(" <option value='"+data[x].id+"'>"+data[x].uname+"</option>");
                    }
                }
                //重新加载下拉框
                layui.form.render("select");
            }
        });
    });


});