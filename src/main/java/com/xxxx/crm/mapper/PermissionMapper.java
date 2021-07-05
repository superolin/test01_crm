package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;


public interface PermissionMapper extends BaseMapper<Permission,Integer> {


    /*根据角色id,查询资源的数量集合*/
    Integer countPermissionByRoleId(Integer roleId);

    /*根据roleId删除其所有的权限信息*/
    Integer deletePermissionByRoleId(Integer roleId);

    //查询某选中的角色拥有的资源, 用于回显选中复选框
    List<Integer> queryRoleHasAllModuleIdsByRoleId(Integer roleId);


    //首页查询指定该用户的权
    // 根据userId查询该用户user的权限资源id集合,查询用户的权限码acl_value
    List<String> queryPermissionByRoleByUserId(Integer userId);



}