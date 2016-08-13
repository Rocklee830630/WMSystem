package com.ccthanking.framework.coreapp.orgmanage.orgrole;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.base.BaseBO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.RoleVO;
import com.ccthanking.framework.common.cache.CacheManager;

public class OrgRoleBO extends BaseBO {
	public OrgRoleBO() {
	}

	public static void insert(Object obj, HttpServletRequest request)
			throws Exception {
		RoleVO tempVO = (RoleVO) obj;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			OrgRoleDAO.insert(tempVO, conn);
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_ROLE,
					tempVO.getName(), CacheManager.ADD);
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static void update(Object obj, HttpServletRequest request)
			throws Exception {
		RoleVO tempVO = (RoleVO) obj;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			OrgRoleDAO.update(tempVO, conn);
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_ROLE,
					tempVO.getName(), CacheManager.UPDATE);
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}