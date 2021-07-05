package com.xxxx.crm.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.CusDevPlanMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.vo.CusDevPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Autowired(required = false)
    private CusDevPlanMapper cusDevPlanMapper;


    public Map<String,Object>  queryCusDvePlanById(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> maps=new HashMap<>();
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        List<CusDevPlan> list = cusDevPlanMapper.selectByParams(cusDevPlanQuery);
        PageInfo<CusDevPlan> plist=new PageInfo(list);
        maps.put("code",0);
        maps.put("msg","");
        maps.put("count",plist.getTotal());
        maps.put("data",plist.getList());
        return maps;
    }

}
