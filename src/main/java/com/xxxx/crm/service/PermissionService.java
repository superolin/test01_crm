package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.PermissionMapper;
import com.xxxx.crm.vo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Autowired(required = false)
    private PermissionMapper permissionMapper;


    //根据userId查询该用户user的权限资源id集合,查询用户的权限码acl_value
    public List<String> queryPermissionByRoleByUserId(Integer userId){
        return permissionMapper.queryPermissionByRoleByUserId(userId);

    }
}
