package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    //消除try catch
    //账号,密码登录
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo sayLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);
        return resultInfo;
    }


    //视图转发: 修改密码页面
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }


    //消除try catch
    //修改密码操作
    @RequestMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword) {
        ResultInfo resultInfo = new ResultInfo();
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUserPassword(userId, oldPassword, newPassword, confirmPassword);
        return resultInfo;
    }


    //基本资料:转发视图
    @RequestMapping("toSettingPage")
    public String toSettingPage(Model model, HttpServletRequest req) {
        //重当前Cookie中获取对象userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //根据userId查询用户信息
        User user = userService.selectByPrimaryKey(userId);
        //存储
        model.addAttribute("user", user);
//        req.setAttribute("user", user);
        //转发
        return "user/setting";
    }


    //修改基本资料
//    @RequestMapping("update")
//    @ResponseBody
//    public ResultInfo updateUser(User user) {
//        userService.updateByPrimaryKeySelective(user);
//        return success("保存信息成功!!!");
//    }

    //修改基本资料
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("保存信息成功!!!");
    }



    //转发视图至"用户管理"主界面
    @RequestMapping("index")
    public String index() {
        return "user/user";
    }


    //根据条件查询用户
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> sayList(UserQuery userQuery) {
        Map<String, Object> map = userService.selectByParams(userQuery);
        return map;
    }


    @RequestMapping("addOrUpdatePage")
    public String addOrUpdatePage(HttpServletRequest request, Integer id) {
        if (id != null) {
            //查询用户信息
            request.setAttribute("user", userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }


    //添加用户
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.addUser(user);
        return success("添加用户成功!!!!");
    }


    //删除用户 update字段is_valid为0
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUsers(Integer[] ids) {
        userService.removeUserByIds(ids);
        return success("用户删除成功!!!");
    }


}
