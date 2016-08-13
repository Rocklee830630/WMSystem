package com.ccthanking.common.vo;

import com.ccthanking.framework.base.BaseVO;

public class ApWsTypzVO
    extends BaseVO
{
    public ApWsTypzVO()
    {
        this.addField("YWLX", this.TP_PK | OP_STRING); //业务类型
        this.addField("WS_TEMPLATE_ID", this.TP_PK | OP_STRING); //文书模板编号
        this.addField("OPERATIONOID", this.TP_PK | OP_STRING); //业务流程编号
        this.addField("ROLENAME", OP_STRING); //角色名称
        this.addField("DEPT_LEVEL", OP_STRING); //适用的级别
        this.addField("DEPTID", OP_STRING); //部门编号

        this.setVOOwner("empty");
        this.setVOTableName("ApWsTypz");
    }

    //设置角色名称
    public void setRolename(String aRolename)
    {
        this.setInternal("ROLENAME", aRolename);
    }

    //获取角色名称
    public String getRolename()
    {
        return (String)this.getInternal("ROLENAME");
    }

    //设置业务类型
    public void setYwlx(String aYwlx)
    {
        this.setInternal("YWLX", aYwlx);
    }

    //获取业务类型
    public String getYwlx()
    {
        return (String)this.getInternal("YWLX");
    }

    //设置文书模板编号
    public void setWs_template_id(String aWs_template_id)
    {
        this.setInternal("WS_TEMPLATE_ID", aWs_template_id);
    }

    //获取文书模板编号
    public String getWs_template_id()
    {
        return (String)this.getInternal("WS_TEMPLATE_ID");
    }

    //设置业务流程编号
    public void setOperationoid(String aOperationoid)
    {
        this.setInternal("OPERATIONOID", aOperationoid);
    }

    //获取业务流程编号
    public String getOperationoid()
    {
        return (String)this.getInternal("OPERATIONOID");
    }

    //设置适用的级别(派出所、分局、市局)
    public void setDept_level(String aDept_level)
    {
        this.setInternal("DEPT_LEVEL", aDept_level);
    }

    //获取适用的级别(派出所、分局、市局)
    public String getDept_level()
    {
        return (String)this.getInternal("DEPT_LEVEL");
    }

    //设置部门编号
    public void setDeptid(String aDeptid)
    {
        this.setInternal("DEPTID", aDeptid);
    }

    //获取部门编号
    public String getDeptid()
    {
        return (String)this.getInternal("DEPTID");
    }
}
