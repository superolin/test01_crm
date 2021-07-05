package com.xxxx.crm.controller;

import com.xxxx.crm.annotation.RequiredPermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private UserService userService;

    //    main页面左侧导航: "营销机会管理"按钮 data-tab="sale_chance/index"
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }

    /**
     *  1. 多条件分页查询营销机会
     *  2. 这里展示开发计划项数据为已分配给当前登录用户的营销机会数据后续对客户进行下一步的开发操作，所以在数据展示上查询的机会数据分配状态为已分配状态。
     *  因此传入参数saleChanceQuery.setAssignMan(userId);
     * @param saleChanceQuery
     * @return
     */
//    @RequestMapping("list")
//    @ResponseBody
//    public Map<String, Object> sayList(SaleChanceQuery saleChanceQuery) {
//        Map<String, Object> map = saleChanceService.querySaleChanceByParam(saleChanceQuery);
//        return map;
//    }
    @RequestMapping("list")
    @ResponseBody
    @RequiredPermission(code = "101001")
    public Map<String, Object> sayList(SaleChanceQuery saleChanceQuery, Integer flag, HttpServletRequest request) {
        //此处flag != null && flag == 1中间必须为&&, 为何???
        //查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配人参数
        if (flag != null && flag == 1) {
            //获取当前用户的userId
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //设置查询条件分配人
            saleChanceQuery.setAssignMan(userId);
        }
        Map<String, Object> map = saleChanceService.querySaleChanceByParam(saleChanceQuery);
        return map;
    }


    //头部工具栏"添加"按钮转发到/addOrUpdateSaleChancePage, 弹出填充层
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdate(Integer id, Model model) {
        //修改和添加主要的区别是表单中是否有ID,有id修改操作，否则添加
        if (id != null) {
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }


    //save插入一个新的saleChance
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo sayAdd(SaleChance saleChance, HttpServletRequest request) {
//        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//        User user = userService.selectByPrimaryKey(userId);
//        String trueName = user.getTrueName();

        //老师的方法: 添加创建人createMan
        String trueName = CookieUtil.getCookieValue(request, "trueName");
        saleChance.setCreateMan(trueName);
        saleChance.setCreateDate(new Date());
        saleChanceService.saveSaleChance(saleChance);
        return success("添加营销机会成功!!!");
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo sayUpdate(SaleChance saleChance) {
        //修改
        saleChanceService.changeSaleChance(saleChance);
        return success("修改营销机会成功!!!!");
    }


    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo sayDeletes(Integer[] ids) {
        System.out.println("===>>>" + Arrays.toString(ids));
        saleChanceService.removeSaleChance(ids);
        return success("删除营销机会记录成功!!!");
    }


    //下拉栏显示所有销售
    //三标联表, 查询所有销售人员
    @RequestMapping("querySales")
    @ResponseBody
    public List<Map<String, Object>> querySales() {
        List<Map<String, Object>> listMap = userService.queryAllSales();
        listMap.forEach(System.out::println);
        return listMap;
    }


}
