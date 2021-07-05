layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    // 引入 formSelects 模块
    formSelects = layui.formSelects;


    /**
     * 触发表单 submit
     */
    // <button class="layui-btn layui-btn-lg" lay-submit="" lay-filter="addOrUpdateUser">确认</button>
    form.on("submit(addOrUpdateUser)",function(data){
        //弹出层
       var index= layer.msg("数据加载中",{time:false,shade:0.8,icon:16});
        //构造title
        var url=ctx+"/user/save";
        //判断是否更新，还是添加
        if($("input[name='id']").val()){
            url=ctx+"/user/update"
        }
        //发送ajax
        $.post(url,data.field,function(obj){
            if(obj.code== 200){
                //添加成功了
                layer.msg(obj.msg, {icon: 6});
                //关闭加载层
                layer.close(index);
                //关闭iframe;
                layer.closeAll("iframe");
                //刷新父目录
                parent.location.reload();
            }else{
                //添加失败了
                layer.msg(obj.msg,{icon:5 });
            }
        },"json");
        //阻止
        return false;
    });


    /**
     * 取消
     */
    $("#closeBtn").click(function(){
        //假设这是iframe页
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });


    /**
     * 加载角色role下拉栏
     * 配置远程搜索
     * formSelects.config(ID,options,isJson)
     * ID: xm_selectet值
     * options配置项
     * isJson是否传输json数据
     */
        //无回显功能, 仅仅实现下拉栏.
    // formSelects.config('selectId',{
    //     type:"post",
    //     searchUrl:ctx + "/role/queryRoles2",
    //     //自定义返回数据中name的key, 默认 name
    //     keyName: 'roleName',//下拉框中的文本内容
    //     //自定义返回数据中value的key, 默认 value
    //     //查到的返回数据会自动填充到下拉框中
    //     keyVal: 'id'
    // },true);

        // 用户表弹出层的"角色Role"下拉栏能够实现回显功能,因为传入userId参数,
        // 且SQL语句中进行联表查询, 同时SQL语句增加了selected字段,返回List<Map<>>中包含select键值对
    var userId=$("input[name=id]").val();
    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx + "/role/queryRoles2?userId="+userId,
        //自定义返回数据中name的key, 默认 name
        keyName: 'roleName',//下拉框中的文本内容
        keyVal: 'id'
    },true);


});