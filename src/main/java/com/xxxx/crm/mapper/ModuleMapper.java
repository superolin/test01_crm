package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.dto.TreeModel;
import com.xxxx.crm.vo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    //菜单管理模块, 实现list表格显示
    List<Module> selectAllModules();


    //角色管理表加载zTree弹出层, 展示所有权限资源的tree
    //查询所有的资源，roleId
    //id,name,pid;
    public List<TreeModel> selectAllModule();



}