package com.ccthanking.framework.common;

import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.util.Pub;

/**
 * 组织机构信息
 */
public class OrgDeptVO implements java.io.Serializable, OrgDept {
	private String ROW_ID;
	private String DEPT_NAME;
	private String DEPT_PARANT_ROWID;
	private String ACTIVE_FLAG;
	private String DEPTTYPE;
	private String bmjc;
	private String ssxq;
	private String jz;
	private String cus_dept_level;
	private String pdm;
	private String pm;
	private String jglb;
	private String extend1;
	private String fzr;
	private String fgzr;
	private String ybzr;
	
	private String deptFullName;

	public String getJglb() {
		return this.jglb;
	}

	public void setJglb(String jglb) {
		this.jglb = jglb;
	}

	public String getJz() {
		return this.jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}

	public String getCus_dept_level() {
		return this.cus_dept_level;
	}

	public void setCus_dept_level(String cus_dept_level) {
		this.cus_dept_level = cus_dept_level;
	}

	public String getPdm() {
		return this.pdm;
	}

	public void setPdm(String pdm) {
		this.pdm = pdm;
	}

	public String getPm() {
		return this.pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	// end

	public String getSsxq() {
		return this.ssxq;
	}

	public void setSsxq(String ssxq) {
		this.ssxq = ssxq;
	}

	public String getBmjc() {
		return this.bmjc;
	}

	public void setBmjc(String bmjc) {
		this.bmjc = bmjc;
	}

	public OrgDept getParent() throws Exception {
		if (Pub.empty(this.DEPT_PARANT_ROWID)
				|| this.DEPT_PARANT_ROWID.equals("0"))
			return null;
		return OrgDeptManager.getInstance().getDeptByID(this.DEPT_PARANT_ROWID);
	}

	public String getDeptID() {
		return this.ROW_ID;
	}

	public void setDeptID(String rowId) {
		this.ROW_ID = rowId;
	}


	public String getDept_Name() {
		return this.DEPT_NAME;
	}

	// end
	public String getDeptName() {
		return this.DEPT_NAME;
	}

	public void setDeptName(String deptName) {
		this.DEPT_NAME = deptName;
	}

	public String getDeptParentID() {
		return this.DEPT_PARANT_ROWID;
	}

	public void setDeptParentID(String deptParentROWID) {
		this.DEPT_PARANT_ROWID = deptParentROWID;
	}

	public String getDeptType() {
		return this.DEPTTYPE;
	}

	public void setDeptType(String deptType) {
		this.DEPTTYPE = deptType;
	}

	public String getActiveFlag() {
		return this.ACTIVE_FLAG;
	}

	public void setActiveFlag(String activeFlag) {
		this.ACTIVE_FLAG = activeFlag;
	}

	public int getDeptLevel() {
		return Pub.toInt(this.DEPTTYPE);
	}
	
	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getFgzr() {
		return fgzr;
	}

	public void setFgzr(String fgzr) {
		this.fgzr = fgzr;
	}

	public String getYbzr() {
		return ybzr;
	}

	public void setYbzr(String ybzr) {
		this.ybzr = ybzr;
	}

	public void setDeptFullName(String deptFullName) {
		this.deptFullName = deptFullName;
	}

	public String getDeptFullName() {
		return deptFullName;
	}
	
}
