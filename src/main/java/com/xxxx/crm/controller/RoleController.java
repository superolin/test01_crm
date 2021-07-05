package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.dto.TreeModel;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.ModuleService;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;


    //用户(User)管理表使用该方法
    //查询所有角色role列表, (只需要id与roleName,所以使用Map<String,Object>, 当然也可以使用Role)
    @RequestMapping("queryRoles")
    @ResponseBody
    public List<Map<String,Object>> sayListRole(){
        List<Map<String, Object>> maps = roleService.queryAllRoles();
        return maps;
    }


    //formselects插件返回List<>
    // 用户表弹出层的"角色Role"下拉栏能够实现回显功能,因为传入userId参数,
    // 且SQL语句中进行联表查询, 同时SQL语句增加了selected字段,返回List<Map<>>中包含select键值对
    //访问localhost:8082/crm/role/queryRoles2?userId=10, 返回如下:
    //[{"roleName":"系统管理员","id":1,"selected":"selected"},{"roleName":"销售","id":2,"selected":"selected"},
    // {"roleName":"客户经理","id":3,"selected":""},{"roleName":"技术经理","id":14,"selected":""},{"roleName":"人事","id":17,"selected":""},
    // {"roleName":"研发","id":18,"selected":""},{"roleName":"财务","id":21,"selected":""}]
    @RequestMapping("queryRoles2")
    @ResponseBody
    public List<Map<String,Object>> sayListRole2(Integer userId){
        List<Map<String, Object>> maps = roleService.queryAllRoles2(userId);
        return maps;
    }


    //转发至"角色管理"页面==>>return "role/role"
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }


    //初始化角色管理表内容, 查询所有信息
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> userList(RoleQuery query){
        return  roleService.queryRole(query);
    }


    //"添加"或"编辑"操作, 用于跳转至弹出层页面.
    //添加不传回id, 编辑传回id,用于弹出层页面回显.
    @RequestMapping("toAddOrUpdatePage")
    public String toAddOrUpdatePage(Integer rid, Model model){
        if(rid!=null){
            Role role=roleService.selectByPrimaryKey(rid);
            model.addAttribute("role",role);
        }
        return "role/add_update";
    }


    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.addRole(role);
        return success("添加角色成功!!!!");
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("修改角色成功!!!");
    }


    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer rid){
        roleService.deleteByPrimaryKey(rid);
        return success("删除角色成功了!!!");
    }


    //将roleId存储到作用域中(弹出层),hidden属性.
    @RequestMapping("toAddGrandPage")
    public String toAddGrandPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

//    addGrant
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mIds,Integer roleId){
        roleService.addGrant(mIds,roleId);
        return success("角色授权成功!");
    }











}
