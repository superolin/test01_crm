package com.xxxx.crm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SaleChance {
    private Integer id;

    private String uname;
    //额外增加属性: 指派人由interger类型的assignMan(其实对应t_user表中的销售人员id), 变成String格式的指派人姓名user_name
//    select s.*,u.user_name as uname
//    from t_sale_chance as s
//    left join
//    t_user as u
//            on
//    u.id=s.assign_man

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    private String chanceSource;

    private String customerName;

    private Integer cgjl;

    private String overview;

    private String linkMan;

    private String linkPhone;

    private String description;

    private String createMan;

    private String assignMan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date assignTime;

//     * 格式化分配状态
//     * 0 - 未分配
//     * 1 - 已分配
    private Integer state;

//    * 格式化开发状态
//* 0 - 未开发
//* 1 - 开发中
//* 2 - 开发成功
//* 3 - 开发失败
//         *   2.设置相关参数默认值
//     *      state:默认未分配  如果选择分配人  state 为已分配
//     *      assignTime:如果  如果选择分配人   时间为当前系统时间
//     *      devResult:默认未开发 如果选择分配人devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
//     *      isValid:默认有效数据(1-有效  0-无效)
//     *      createDate updateDate:默认当前系统时间
    private Integer devResult;

    private Integer isValid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChanceSource() {
        return chanceSource;
    }

    public void setChanceSource(String chanceSource) {
        this.chanceSource = chanceSource == null ? null : chanceSource.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public Integer getCgjl() {
        return cgjl;
    }

    public void setCgjl(Integer cgjl) {
        this.cgjl = cgjl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview == null ? null : overview.trim();
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan == null ? null : linkMan.trim();
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone == null ? null : linkPhone.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan == null ? null : createMan.trim();
    }

    public String getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(String assignMan) {
        this.assignMan = assignMan == null ? null : assignMan.trim();
    }

    public Date getAssignTime() {
        return assignTime;
    }

    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "SaleChance{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", chanceSource='" + chanceSource + '\'' +
                ", customerName='" + customerName + '\'' +
                ", cgjl=" + cgjl +
                ", overview='" + overview + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", linkPhone='" + linkPhone + '\'' +
                ", description='" + description + '\'' +
                ", createMan='" + createMan + '\'' +
                ", assignMan='" + assignMan + '\'' +
                ", assignTime=" + assignTime +
                ", state=" + state +
                ", devResult=" + devResult +
                ", isValid=" + isValid +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}