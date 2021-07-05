package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {

    private String customerName;
    private String createMan;
    private String state;

    //查询开发计划表, 补充字段: 进现实本用户的营销机会使用字段assignMan
    private Integer assignMan;
    //查询开发计划表: 补充查询字段: 多条件查询:下拉栏"开发状态"选择
    private Integer devResult;

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public SaleChanceQuery() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", createMan='" + createMan + '\'' +
                ", state='" + state + '\'' +
                ", assignMan=" + assignMan +
                "} " + super.toString();
    }
}
