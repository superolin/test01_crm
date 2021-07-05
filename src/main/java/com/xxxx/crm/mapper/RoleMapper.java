package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Role;
import com.xxxx.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    public List<Map<String,Object>> selectAllRoles();

    /**
     * 查询所有的角色
     */
    // 用户表弹出层的"角色Role"下拉栏能够实现回显功能,因为传入userId参数,
    // 且SQL语句中进行联表查询, 同时SQL语句增加了selected字段,返回List<Map<>>中包含select键值对
    public List<Map<String,Object>> selectAllRoles2(Integer userId);

    Role selectRoleByName(String roleName);

    //插入用户信息，有当前对象的userId
    public int insertUserByReturnKey(User user);
}