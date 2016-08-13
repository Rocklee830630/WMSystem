package com.ccthanking.framework.coreapp.orgmanage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.cache.Cache;

/**
 * @auther xhb 
 */
public class PushPersonManager implements Cache {

	private List<Map<String, String>> cachePushPsn;
	private static PushPersonManager instance;
	
	private PushPersonManager() {
		init();
	}
	
	private void init() {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT OPERATOR_NO, USERID, DEPTID FROM FS_PUSHINFO_PSN";
			String[][] rs = DBUtil.query(conn, sql);
			cachePushPsn = new ArrayList<Map<String, String>>();
			if(rs!=null && rs.length>0){
				for (int i = 0; i < rs.length; i++) {
					Map<String, String> rsMap = new HashMap<String, String>();
					rsMap.put("OPERATOR_NO", rs[i][0]);
					rsMap.put("USERID", rs[i][1]);
					rsMap.put("DEPTID", rs[i][2]);
					cachePushPsn.add(rsMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
	}
	
	public String[] loadUserId(String operateId) {
		List<String> userIdList = new ArrayList<String>();
		Iterator<Map<String, String>> i = cachePushPsn.iterator();
		while (i.hasNext()) {
			Map<String, String> map = i.next();
			String valueOperId = map.get("OPERATOR_NO");
			if (valueOperId.equals(operateId)) {
				userIdList.add(map.get("USERID"));
			}
		}
		return (String[]) userIdList.toArray(new String[userIdList.size()]);
	}
	
	@Override
	public void synchronize(String data, int action) throws Exception {
		
	}

	@Override
	public void reBuildMemory() throws Exception {
		if (cachePushPsn != null) {
			cachePushPsn.clear();
			cachePushPsn = null;
		}
		init();
	}
	
	public static PushPersonManager getInstance() {
		if (instance == null) {
			instance = new PushPersonManager();
		}
		return instance;
	}
}
