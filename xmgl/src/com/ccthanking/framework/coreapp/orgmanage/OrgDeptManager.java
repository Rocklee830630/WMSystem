package com.ccthanking.framework.coreapp.orgmanage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.OrgDeptVO;
import com.ccthanking.framework.common.cache.Cache;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.Pub;

/**
 * @author leo leochou oss@tom.com
 * @version 1.0 2007-01-30
 */
public class OrgDeptManager implements Cache {
	private Hashtable orgtable;
	static private OrgDeptManager instance;
	static private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("OrgDeptManager");

	private OrgDeptManager() throws Exception {
		init();
	}
	
	public void reBuildMemory() throws Exception{
		if(orgtable != null){
			orgtable.clear();
			orgtable = null;
		}
		init();
		OrgDeptToXml.exportAllXml(AppInit.appPath, "ZZJG");
	
	}
	
	public void synchronize(String deptid, int action) throws Exception {
		this.orgtable.remove(deptid);
		if (action == CacheManager.ADD || action == CacheManager.UPDATE) { 
			OrgDept orgdept = this.getDeptByID(deptid);
			this.orgtable.put(deptid, orgdept);
		} else if (action == CacheManager.DELETE) {
			this.orgtable.remove(deptid);
		}
	
	}

	private void init() throws Exception {

		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String querySql = "select ROW_ID, DEPT_NAME,DEPT_PARANT_ROWID,DEPTTYPE,BMJC,ACTIVE_FLAG,SSXQ,JZ,CUS_DEPT_LEVEL,PDM,PM,JGLB,EXTEND1,FZR,FGZR,YBZR from FS_org_dept ";
			String[][] list = DBUtil.query(conn, querySql);
			if (list != null) {
				orgtable = new Hashtable(list.length);
				for (int i = 0; i < list.length; i++) {
					OrgDept dept = new OrgDeptVO();
					dept.setDeptID(list[i][0]);
					// 部门全称 add by xhb
					dept.setDeptFullName(list[i][1]);
					// 部门简称，字典内存为简称 add by xhb
					dept.setDeptName(list[i][4]);
					dept.setDeptParentID(list[i][2]);
					dept.setActiveFlag(list[i][5]);
					dept.setDeptType(list[i][3]);
					dept.setBmjc(list[i][4]);
					dept.setSsxq(list[i][6]);
					dept.setJz(list[i][7]);
					dept.setCus_dept_level(list[i][8]);
					dept.setPdm(list[i][9]);
					dept.setPm(list[i][10]);
					dept.setJglb(list[i][11]);
					dept.setExtend1(list[i][12]);
					dept.setFzr(list[i][13]);
					dept.setFgzr(list[i][14]);
					dept.setYbzr(list[i][15]);
					orgtable.put(list[i][0], dept);
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace(System.out);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
	}
	/**
	 * 获得所有子部门列表（递归实现）
	 * @param dept
	 * @return
	 */
/*	private ArrayList getChildDeptArrayList(OrgDept dept){
		ArrayList al = new ArrayList();
		String deptId = dept.getDeptID();
		if(dept != null)
			al.add(dept);
		Iterator it = orgtable.keySet().iterator();
		while(it.hasNext()){
			String temp_key = (String) it.next();
			OrgDept temp_dept = (OrgDept) orgtable.get(temp_key);
			if(temp_dept.getDeptParentID().equals(deptId)){
				//al.add(temp_dept);
				ArrayList al_childs = getChildDeptArrayList(temp_dept);
				for(int i = 0 ; i < al_childs.size() ; i++){
					al.add(al_childs.get(i));
				}
			}
		}
		return al;
	}*/
	/**
	 * 获得所有子部门数组
	 * @param dept
	 * @return
	 */
/*	public OrgDept[] getChildDeptArray(OrgDept dept){
		ArrayList al = getChildDeptArrayList(dept);
		OrgDept[] orgDeptArray = new OrgDept[al.size()];
		orgDeptArray = (OrgDept[]) al.toArray(orgDeptArray);
		return orgDeptArray;
	}*/
	/**
	 * 获得所有子部门ID字符串
	 * @param dept
	 * @return
	 */
/*	public String getChildDeptIdStr(OrgDept dept){
		OrgDept[] depts = getChildDeptArray(dept);
		String idStr = "";
		for(int i = 0 ; i < depts.length ; i++){
			OrgDept temp_dept = (OrgDept) depts[i];
			if(i == depts.length - 1){
				idStr = idStr + temp_dept.getDeptID();
			}else{
				idStr = idStr + temp_dept.getDeptID() + ",";
			}
		}
		return idStr;
	}*/
	public OrgDept getDeptByID(String deptID) {
		if (Pub.empty(deptID))
			return null;
		OrgDept res = (OrgDept) orgtable.get(deptID);
		if (res == null) {
			Connection conn = null;
			try {
				conn = DBUtil.getConnection();
				res = getDeptByID(conn, deptID);
			} catch (Exception e) {
				log.error(e);
			} finally {
				try {
					if (conn != null)
						conn.close();
				} catch (Exception ex) {
				}
			}
		}
		return res;
	}

	public OrgDept getDeptByID(Connection conn, String deptID) throws Exception {

		if (Pub.empty(deptID))
			return null;
		Object res = orgtable.get(deptID);
		if (res != null)
			return (OrgDept) res;
		String[][] list = DBUtil.query(conn,"select ROW_ID, DEPT_NAME,DEPT_PARANT_ROWID,DEPTTYPE,BMJC,ACTIVE_FLAG,SSXQ,"
				+"JZ,CUS_DEPT_LEVEL,PDM,PM,JGLB,EXTEND1,FZR,FGZR,YBZR from FS_org_dept where row_id='" + deptID + "'");
		if (list != null) {
			int i = 0;
			OrgDept dept = new OrgDeptVO();
			dept.setDeptID(list[i][0]);
			dept.setDeptName(list[i][1]);
			dept.setDeptFullName(list[i][1]);
			dept.setDeptParentID(list[i][2]);
			dept.setActiveFlag(list[i][5]);
			dept.setDeptType(list[i][3]);
			dept.setBmjc(list[i][4]);
			dept.setSsxq(list[i][6]);
			dept.setJz(list[i][7]);
			dept.setCus_dept_level(list[i][8]);
			dept.setPdm(list[i][9]);
			dept.setPm(list[i][10]);
			dept.setJglb(list[i][11]);
			orgtable.put(list[i][0], dept);
			return dept;
		}
		return null;
	}

	static synchronized public OrgDeptManager getInstance() throws Exception {
		if (instance == null) {
			instance = new OrgDeptManager();
			//CacheManager.register(CacheManager.CACHE_ORG_DEPT, instance);
		}
		return instance;
	}
}