package com.ccthanking.framework.common;

/**
 * 该接口定义了组织机构信息管理的基本结构 如果有别的业务需要可以在实现该接口类里扩展 需要添加的相关属性信息
 */
public interface OrgDept {
	
	public String getDeptID(); // 获取组织机构编号

	public void setDeptID(String rowId); // 设置组织机构编号

	public String getDeptName(); // 获取部门名称

	public String getDept_Name(); // 获取部门名称

	public void setDeptName(String deptName);// 设置部门名称

	public String getDeptParentID(); // 获取父类号

	public void setDeptParentID(String deptParentROWID);// 设置父类号

	public String getDeptType(); // 获取部门类别（省厅‘1’、市级‘2’、分局‘3’、派出所‘4’）

	public void setDeptType(String deptType); // 设置部门类别

	public int getDeptLevel();

	public String getBmjc(); // 获取部门简称

	public void setBmjc(String bmjc); // 设置部门简称

	public String getActiveFlag(); // 获取是否有效的标志

	public void setActiveFlag(String activeFlag); // 设置是否有效的标志

	public OrgDept getParent() throws Exception;

	public String getSsxq();

	public void setSsxq(String ssxq);

	public String getJz();

	public void setJz(String jz);

	public String getCus_dept_level();

	public void setCus_dept_level(String cus_dept_level);

	public String getPdm();

	public void setPdm(String pdm);

	public String getPm();

	public void setPm(String pm);

	public String getJglb();

	public void setJglb(String jglb);
	
	public String getExtend1();

	public void setExtend1(String extend1);

	public String getFzr();

	public void setFzr(String fzr);

	public String getFgzr();

	public void setFgzr(String fgzr);

	public String getYbzr();

	public void setYbzr(String ybzr);
	
	public void setDeptFullName(String deptFullName);
	
	public String getDeptFullName();
}
