package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.UserMapper;
import com.xxxx.crm.mapper.UserRoleMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;


@Service
public class UserService extends BaseService<User, Integer> {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    //登录验证
    @Transactional(propagation = Propagation.REQUIRED)
    public UserModel userLogin(String userName, String userPwd) {
        //1. 验证参数
        checkLoginParams(userName, userPwd);
        //2. 根据⽤户名，查询⽤户对象
        User user = userMapper.selectUserByName(userName);
        //3. 判断⽤户是否存在 (⽤户对象为空，记录不存在，⽅法结束)
        AssertUtil.isTrue(user == null, "该用户不存在,记录为空!(⽤户不存在或已注销！)");
        checkLoginPwd(userPwd, user.getUserPwd());
        return buildUserInfo(user);
    }

    //验证用户登录参数
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "密码不能为空");
    }

    //验证登录密码
    private void checkLoginPwd(String userPwd, String uPwd) {
        // 数据库中的密码是经过加密的，将前台传递的密码先加密，再与数据库中的密码作⽐较
        userPwd = Md5Util.encode(userPwd);
        // ⽐较密码
        AssertUtil.isTrue(!userPwd.equals(uPwd), "用户名或密码错误!!!!");
    }

    //构建返回的用户信息
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * ⽤户密码修改
     * 1. 参数校验
     * ⽤户ID： userId ⾮空 ⽤户对象必须存在
     * 原始密码： oldPassword ⾮空 与数据库中密⽂密码保持⼀致
     * 新密码： newPassword ⾮空 与原始密码不能相同
     * 确认密码： confirmPassword ⾮空 与新密码保持⼀致
     * 2. 设置⽤户新密码
     * 新密码进⾏加密处理
     * 3. 执⾏更新操作
     * 受影响的⾏数⼩于1，则表示修改失败
     * 注：在对应的更新⽅法上，添加事务控制
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        // 通过userId获取⽤户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 1. 参数校验
        checkPasswordParams(user, oldPassword, newPassword, confirmPassword);
        // 2. 设置⽤户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        // 3. 执⾏更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "密码修改失败!");
    }

    //密码校验
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(user == null, "用户未登录或不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "原始密码为空!");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "新密码为空!");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword), "确认密码为空!");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))), "原始密码错误!");
        AssertUtil.isTrue(newPassword.equals(oldPassword), "新密码不得与原始密码相同!");
        AssertUtil.isTrue(!newPassword.equals(confirmPassword), "确认密码与新密码必须相同!");
    }


    //查询所有的销售人员信息
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.selectAllSales();
    }


    public Map<String, Object> selectByParams(UserQuery userQuery) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(userQuery.getPage(), userQuery.getLimit());
        List<User> users = userMapper.selectByParams(userQuery);
        PageInfo<User> plist = new PageInfo<>(users);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", plist.getTotal());
        map.put("data", plist.getList());
        return map;
    }


    //添加用户
    public void addUser(User user) {
        //校验
        checkUserParam(user.getUserName(), user.getEmail(), user.getPhone());
        //默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认密码,同时加密
        user.setUserPwd(Md5Util.encode("123"));
//        AssertUtil.isTrue(userMapper.insertSelective(user) < 1, "用户添加失败!!!");
        AssertUtil.isTrue(userMapper.insertUserByReturnKey(user) < 1, "用户添加失败!!!");
        // "角色"属性name="roleIds" xm-select="selectId"
        //前端获得userId,roleIds;其中roleIds获取到的为字符串,需要使用split方法以逗号分割
        //批量添加用户和角色的关系表数据
        relaionUserRole(user.getId(), user.getRoleIds());
    }


    //添加用户: 参数校验
    private void checkUserParam(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空!!!");
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp != null, "用户已经存在!!!");
        AssertUtil.isTrue(StringUtils.isBlank(email), "邮箱不能为空!!!");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "电话不能为空!!!");
        AssertUtil.isTrue(PhoneUtil.isMobile(phone), "电话格式有误!!!!");
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        // 1. 参数校验
        // 验证参数
        checkUserParam2(user.getUserName(), user.getEmail(), user.getPhone());
        // 通过id查询用户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断对象是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        // 2. 设置默认参数
        user.setUpdateDate(new Date());
        // 3. 执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新失败！");
        //用户更新中间表
        Integer userId = userMapper.selectUserByName(user.getUserName()).getId();
        relaionUserRole(userId, user.getRoleIds());
    }

    //修改用户:参数校验
    private void checkUserParam2(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空!!!");
        AssertUtil.isTrue(StringUtils.isBlank(email), "邮箱不能为空!!!");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "电话不能为空!!!");
//        AssertUtil.isTrue(!(PhoneUtil.isMobile(phone)), "电话格式有误!!!!");
    }


    public void removeUserByIds(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0, "未选择目标数据!!!");
        AssertUtil.isTrue(userMapper.deleteBatch(ids) < 1, "删除用户失败!!!");
    }


    private void relaionUserRole(Integer useId, String roleIds) {
/**
 * ⽤户⻆⾊分配: 如何进⾏⻆⾊分配???
 * 如果⽤户原始⻆⾊存在 ⾸先清空原始所有⻆⾊ 添加新的⻆⾊记录到⽤户⻆⾊表
 */
        // "角色"属性name="roleIds" xm-select="selectId"
        //前端获得userId,roleIds;其中roleIds获取到的为字符串,需要使用split方法以逗号分割
        //批量添加用户和角色的关系表数据
        //统计角色
        int count = userRoleMapper.countUserRoleByUserId(useId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(useId) !=
                    count, "⽤户⻆⾊分配失败!");
        }
        if (StringUtils.isNotBlank(roleIds)) {
            //重新添加新的⻆⾊//准备一个List
            List<UserRole> userRoles = new ArrayList<UserRole>();
            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(useId);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                //添加容器
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) != userRoles.size(), "角色分配失败了");
        }
    }



}
