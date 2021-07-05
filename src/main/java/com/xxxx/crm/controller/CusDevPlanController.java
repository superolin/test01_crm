package com.xxxx.crm.controller;


import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;


@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Autowired
    private CusDevPlanService cusDevPlanService;

    @Autowired
    private SaleChanceService saleChanceService;

//    data-tab="cus_dev_plan/index"转发至客户开发计划表
    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }


    /**
     * 通过行内工具栏的"开发"和"详情"跳转至弹出层, 营销机会开发表弹出层||营销机会维护弹出层
     * @param model
     * @param sid
     * @return
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String sayList(Model model, Integer sid) {
        //根据选中的id, 查询一条营销机会的记录
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        //存储该记录到作用域, 用于回显填充. "开发"和"维护"弹出层都需要达到回显效果.
        model.addAttribute("saleChance", saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }


    /**
     * 返回计划项数据展示
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> saylistChild(CusDevPlanQuery cusDevPlanQuery) {
        Map<String, Object> map = cusDevPlanService.queryCusDvePlanById(cusDevPlanQuery);
        return map;
    }


}
