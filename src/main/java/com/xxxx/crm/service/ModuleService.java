package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dto.TreeModel;
import com.xxxx.crm.mapper.ModuleMapper;
import com.xxxx.crm.mapper.PermissionMapper;
import com.xxxx.crm.vo.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    @Autowired(required = false)
    private PermissionMapper permissionMapper;


    //菜单管理模块, 实现list表格显示
    public Map<String, Object> queryAllModules() {
        Map<String, Object> map = new HashMap<>();
        //查询所有的数据
        List<Module> mlist = moduleMapper.selectAllModules();
        //准备数据
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", mlist.size());
        map.put("data", mlist);
        //返回目标map
        return map;
    }


    //角色管理表加载zTree弹出层, 展示弹出层的权限资源tree列表
    public List<TreeModel> queryAllModule() {
        return moduleMapper.selectAllModule();
    }


    //角色管理表加载zTree弹出层, 展示弹出层的权限资源tree列表,同时实现回显, 填充复选框.
    public List<TreeModel> queryAllModule(Integer roleId) {
        //查询所有的权限资源的treeModel, 然后通过getId方法, 得到资源id.
        List<TreeModel> treeModels = moduleMapper.selectAllModule();
        //根据⻆⾊roleId查询⻆⾊role拥有的权限资源id,返回权限资源id的一个集合
        List<Integer> roleHasModuleIds = permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);
        //循环遍历，判断角色拥有那些资源，checked=true,checked=false;
        for (TreeModel treeModel : treeModels) {
            if (roleHasModuleIds.contains(treeModel.getId())) {
                treeModel.setChecked(true);
            }
        }
        return treeModels;
    }


}
