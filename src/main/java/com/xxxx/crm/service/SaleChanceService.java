package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.SaleChanceMapper;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Autowired(required = false)
    private SaleChanceMapper saleChanceMapper;


    /**
     * 多条件分页查询营销机会
     * @param saleChanceQuery
     * @return querySaleChanceByParam
     */
    public Map<String, Object> querySaleChanceByParam(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        List<SaleChance> list = saleChanceMapper.selectByParams(saleChanceQuery);
        PageInfo<SaleChance> plist = new PageInfo<SaleChance>(list);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", plist.getTotal());
        map.put("data", plist.getList());
        return map;
    }

    /**
     * 营销机会数据添加
     *   1.参数校验
     *      customerName:非空
     *      linkMan:非空
     *      linkPhone:非空 11位手机号
     *   2.设置相关参数默认值
     *      state:默认未分配  如果选择分配人  state 为已分配
     *      assignTime:如果  如果选择分配人   时间为当前系统时间
     *      devResult:默认未开发 如果选择分配人devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
     *      isValid:默认有效数据(1-有效  0-无效)
     *      createDate updateDate:默认当前系统时间
     *   3.执行添加 判断结果
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance) {
        checkParmas(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            saleChance.setState(0);
            saleChance.setAssignTime(null);
            saleChance.setDevResult(0);
        }
        if (StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        }
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) < 1, "插入失败!!!");
    }

    private void checkParmas(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名不能为空!!!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空!!!");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "手机不能为空!!!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "请输入合法的手机号!!!");
    }


    /**
     * 营销机会数据更新
     *  1.参数校验
     *      id:记录必须存在
     *      customerName:非空
     *      linkMan:非空
     *      linkPhone:非空，11位手机号
     *  2. 设置相关参数值
     *      updateDate:系统当前时间
     *         原始记录 未分配 修改后改为已分配(由分配人决定)
     *            state 0->1
     *            assginTime 系统当前时间
     *            devResult 0-->1
     *         原始记录  已分配  修改后 为未分配
     *            state  1-->0
     *            assignTime  待定  null
     *            devResult 1-->0
     *  3.执行更新 判断结果
     */
    /**
     * userId
     *
     * @param saleChance
     */
    public void changeSaleChance(SaleChance saleChance) {
        //判断id是否存在,id在前端为隐藏域存在.
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp == null, "修改记录不存在!!!");
        //参数验证
        checkParmas(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        if (StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())) {
            //分配状态
            saleChance.setState(1);
            //开发状态
            saleChance.setDevResult(1);
            //分配时间
            saleChance.setAssignTime(new Date());
        } else if (StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())) {
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
        }
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1, "更新修改失败!!!!");

    }


    /**
     * 批量删除||单个删除
     * @param array
     */
    public void removeSaleChance(Integer[] array) {
        AssertUtil.isTrue(array == null || array.length == 0, "请选择要删除的数据!!!");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(array) < 1, "批量删除失败了!!!");
    }


}
