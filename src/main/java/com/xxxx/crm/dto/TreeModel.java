package com.xxxx.crm.dto;

public class TreeModel {
    private Integer id;     //权限资源id
    private String name;    //权限资源名
    private Integer  pId;   //parent_id
    private Boolean checked=false;      //回显使用:List<TreeModel> 增加一个键值对

    public TreeModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "TreeModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pId=" + pId +
                ", checked=" + checked +
                '}';
    }

}
