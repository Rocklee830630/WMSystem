package com.ccthanking.framework.coreapp.orgmanage.org_dept;

import com.ccthanking.framework.base.BaseVO;

public class OrgDeptVO
    extends BaseVO
{
    public OrgDeptVO()
    {
        super(7);
        this.addField("ROW_ID", this.OP_STRING | this.TP_PK); //组织机构编号
        this.addField("DEPT_NAME", this.OP_STRING); //单位或部门名称
        this.addField("DEPT_PARANT_ROWID", this.OP_STRING); //父类号
        this.addField("ACTIVE_FLAG", this.OP_STRING); //0：无效，1：有效
        this.addField("SSXQ", this.OP_STRING); //行政区划
        this.addField("DEPTTYPE", this.OP_STRING); //部门类别：1 省厅,2 市级,3 分局,4 派出所,6 派出所同级单位,7 警务区，8 责任区
        this.addField("BMJC", this.OP_STRING); //部门简称
        this.addField("EXTEND1", this.OP_STRING); //是否项目管理公司
        this.addField("FZR", this.OP_STRING); //负责人
        this.addField("FGZR", this.OP_STRING); //分管主任
        this.addField("YBZR", this.OP_STRING); //一把主任
        this.addField("SORT", this.OP_STRING); //排序号
        
		this.bindFieldToOrgDept("DEPT_PARANT_ROWID");
		this.bindFieldToDic("EXTEND1", "SFSXMGLGS");
		this.bindFieldToUserid("FZR");
		this.bindFieldToUserid("FGZR");
		this.bindFieldToUserid("YBZR");
        
        this.setVOTableName("FS_ORG_DEPT");
    }

    /**
     * 设置组织机构编号
     * @param rowId String
     */
    public void setRowId(String rowId)
    {
        this.setInternal("ROW_ID", rowId); //组织机构编号
    }

    /**
     * 设置单位或部门名称
     * @param deptName String
     */
    public void setDeptName(String deptName)
    {
        this.setInternal("DEPT_NAME", deptName); //单位或部门名称
    }

    /**
     * 设置父类号
     * @param deptParantRowid String
     */
    public void setDeptParantRowid(String deptParantRowid)
    {
        this.setInternal("DEPT_PARANT_ROWID", deptParantRowid); //父类号
    }

    /**
     * 设置0：无效，1：有效
     * @param activeFlag String
     */
    public void setActiveFlag(String activeFlag)
    {
        this.setInternal("ACTIVE_FLAG", activeFlag); //0：无效，1：有效
    }

    /**
     * 设置行政区划
     * @param ssxq String
     */
    public void setSsxq(String ssxq)
    {
        this.setInternal("SSXQ", ssxq); //行政区划
    }

    /**
     * 设置部门类别：1 省厅,2 市级,3 分局,4 派出所,6 派出所同级单位,7 警务区，8 责任区
     * @param depttype String
     */
    public void setDepttype(String depttype)
    {
        this.setInternal("DEPTTYPE", depttype); //部门类别：1 省厅,2 市级,3 分局,4 派出所,6 派出所同级单位,7 警务区，8 责任区
    }

    /**
     * 设置部门简称
     * @param bmjc String
     */
    public void setBmjc(String bmjc)
    {
        this.setInternal("BMJC", bmjc); //部门简称
    }

    /**
     * 获得组织机构编号
     * @return String
     */
    public String getRowId()
    {
        return (String)this.getInternal("ROW_ID");
    }

    /**
     * 获得单位或部门名称
     * @return String
     */
    public String getDeptName()
    {
        return (String)this.getInternal("DEPT_NAME");
    }

    /**
     * 获得父类号
     * @return String
     */
    public String getDeptParantRowid()
    {
        return (String)this.getInternal("DEPT_PARANT_ROWID");
    }

    /**
     * 获得0：无效，1：有效
     * @return String
     */
    public String getActiveFlag()
    {
        return (String)this.getInternal("ACTIVE_FLAG");
    }

    /**
     * 获得行政区划
     * @return String
     */
    public String getSsxq()
    {
        return (String)this.getInternal("SSXQ");
    }

    /**
     * 获得部门类别：1 省厅,2 市级,3 分局,4 派出所,6 派出所同级单位,7 警务区，8 责任区
     * @return String
     */
    public String getDepttype()
    {
        return (String)this.getInternal("DEPTTYPE");
    }

    /**
     * 获得部门简称
     * @return String
     */
    public String getBmjc()
    {
    	return (String)this.getInternal("BMJC");
    }
 
    
    public String getExtend1()
    {
        return (String)this.getInternal("EXTEND1");
    }
    
    public void setExtend1(String extend1)
    {
        this.setInternal("EXTEND1", extend1); 
    }
    
    public String getFzr()
    {
    	return (String)this.getInternal("FZR");
    }
    
    public void setFzr(String fzr)
    {
    	this.setInternal("FZR", fzr); 
    }
    
    public String getFgzr()
    {
    	return (String)this.getInternal("FGZR");
    }
    
    public void setFgzr(String fgzr)
    {
    	this.setInternal("FGZR", fgzr); 
    }
    
    
    public String getYbzr()
    {
    	return (String)this.getInternal("YBZR");
    }
    
    public void setYbzr(String ybzr)
    {
    	this.setInternal("YBZR", ybzr); 
    }
    public String getSort()
    {
    	return (String)this.getInternal("SORT");
    }
    
    public void setSort(String sort)
    {
    	this.setInternal("SORT", sort); 
    }


}