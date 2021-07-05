package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.dto.TreeModel;
import com.xxxx.crm.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;


    @RequestMapping("index")
    public String index() {
        return "module/module";
    }

    //菜单管理模块, 实现list表格显示
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> sayList() {
        return moduleService.queryAllModules();
    }


    //角色管理表加载zTree弹出层, 展示弹出层的权限资源tree列表
//    @RequestMapping("queryModule")
//    @ResponseBody
//    public List<TreeModel> queryModule(){
//        List<TreeModel> treeModels = moduleService.queryAllModule();
//        return treeModels;
//    }


    //角色管理表加载zTree弹出层, 展示弹出层的权限资源tree列表,同时实现回显, 填充复选框.treeModel添加selected属性
//    @RequestMapping("queryModule")
//    @ResponseBody
//    public List<TreeModel> queryModule(Integer roleId) {
//        List<TreeModel> treeModels = moduleService.queryAllModule(roleId);
//        return treeModels;
//    }


    @RequestMapping("queryModule")
    @ResponseBody
    public List<TreeModel> queryModule(Integer roleId){
        List<TreeModel> treeModels=moduleService.queryAllModule(roleId);
        return treeModels;
    }

}
