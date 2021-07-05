package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.ModuleMapper;
import com.xxxx.crm.mapper.PermissionMapper;
import com.xxxx.crm.mapper.RoleMapper;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class RoleService extends BaseService<Role,Integer> {

    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    //用户(User)管理表使用该方法
    //查询所有角色role列表, (只需要id与roleName,所以使用Map<String,Object>, 当然也可以使用Role)
    public List<Map<String,Object>> queryAllRoles(){
        return roleMapper.selectAllRoles();
    }

    // 用户表弹出层的"角色Role"下拉栏能够实现回显功能,因为传入userId参数,
    // 且SQL语句中进行联表查询, 同时SQL语句增加了selected字段,返回List<Map<>>中包含select键值对
    public List<Map<String,Object>> queryAllRoles2(Integer userId){
        return roleMapper.selectAllRoles2(userId);
    }


    //初始化角色管理表内容, 查询所有信息
    public Map<String,Object> queryRole(RoleQuery query){
        //实例化map
        Map<String,Object> map=new HashMap<String,Object>();
        //开始分页
        PageHelper.startPage(query.getPage(),query.getLimit());
        //查询所有的数据
        List<Role> rlist = roleMapper.selectByParams(query);
        //实例化pageInfo
        PageInfo<Role> pageInfo=new PageInfo<>(rlist);
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }


    public void addRole(Role role){
        //验证，非空，角色不能重复
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色不能为空");
        Role temp=roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp!=null,"角色已经存在");
        //默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //添加是否成功
        AssertUtil.isTrue(insertSelective(role)<1,"添加角色失败");
    }


    public void updateRole(Role role){
        //验证，非空，角色不能重复
        AssertUtil.isTrue(role.getId()==null || roleMapper.selectByPrimaryKey(role.getId())==null,"待修改角色不存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名称");
        //角色名称不能重复
        Role temp = roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp!=null &&!(temp.getId().equals(role.getId())),"角色已经存在");
        //默认值
        role.setUpdateDate(new Date());
        //添加是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"修改角色失败");
    }


    /**
     * 添加授权操作,增删改授权id
     * @param moduleIds 资源id集合
     * @param roleId
     */
    public void addGrant(Integer[] moduleIds,Integer roleId){
        //角色role验证
        Role temp = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(roleId==null || temp==null,"待授权的角色不存在!!!");
        //统计一下角色的权限id数量
        int count=permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId)!=count,"删除影响行数与统计授权角色数不等,权限分配失败!!!");
        }
        //收集所有新选中的权限数据
        if(moduleIds!=null && moduleIds.length>0){
            //准备存储权限的集合
            List<Permission> permissions=new ArrayList<Permission>();
            for (Integer mid: moduleIds) {
                //实例化对象Permission
                Permission permission=new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                //默认参数
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //寻找当前module信息，根据mid
                Module module=moduleMapper.selectByPrimaryKey(mid);
                //赋值
                permission.setAclValue(module.getOptValue());
                //存储到集合
                permissions.add(permission);
            }
            //批量添加验证
            AssertUtil.isTrue(permissionMapper.insertBatch(permissions)!=permissions.size(),"授权失败!!!");
        }
    }




}
