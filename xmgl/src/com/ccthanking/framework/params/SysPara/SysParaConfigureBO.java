package com.ccthanking.framework.params.SysPara;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseBO;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.log.LogManager;

import java.sql.*;

public class SysParaConfigureBO extends BaseBO {

	private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager
			.getLogger("SysParaConfigureBO");

	public SysParaConfigureBO(javax.servlet.http.HttpSession session) {
	}

	public Object selectById(String pk) throws OPException, Exception {
		Connection conn = DBUtil.getConnection();
		SysParaConfigureVO tempVO = null;
		try {
			SysParaConfigureDAO tempDAO = new SysParaConfigureDAO();
			tempVO = (SysParaConfigureVO) tempDAO.selectById(pk, conn);
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				logger.error(ex);
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return tempVO;
	}

	public String insert(Object obj, HttpServletRequest request)
			throws OPException, Exception {
		// 取得数据库连接
		SysParaConfigureVO tempVO = (SysParaConfigureVO) obj;
		String strRes = "";
		String intId = "";
		intId = DBUtil.getSequenceValue("AP_PARA_CONFIGURE_SN");
		tempVO.setSysParaConfigureSn(intId);
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			SysParaConfigureDAO tempDAO = new SysParaConfigureDAO();
			strRes = tempDAO.insert(tempVO, conn);
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_PARAMS, "",
					CacheManager.ADD);
			LogManager.writeUpdateLog(
					"增加系统参数[" + tempVO.getSysParaConfigureParaname() + "]",
					"新增参数", tempVO.getSysParaConfigureParakey(),
					LogManager.RESULT_SUCCESS, user);
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				logger.error(ex);
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return strRes;
	}

	public String update(Object obj, HttpServletRequest request)
			throws OPException, Exception {
		// 取得数据库连接
		SysParaConfigureVO tempVO = (SysParaConfigureVO) obj;
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String strRes = "";
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			SysParaConfigureDAO tempDAO = new SysParaConfigureDAO();
			strRes = tempDAO.update(tempVO, conn);
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_PARAMS, "",
					CacheManager.UPDATE);
			LogManager.writeUpdateLog(
					"修改系统参数[" + tempVO.getSysParaConfigureParaname() + "]",
					tempVO.getSysParaConfigureParakey(),
					tempVO.getSysParaConfigureParavalue1() + "|"
							+ tempVO.getSysParaConfigureParavalue2() + "|"
							+ tempVO.getSysParaConfigureParavalue3() + "|"
							+ tempVO.getSysParaConfigureParavalue4(),
					LogManager.RESULT_SUCCESS, user);
		} catch (Exception e) {
			try {
				conn.rollback();

			} catch (Exception ex) {
				logger.error(ex);
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return strRes;
	}

	public String delete(Object obj, HttpServletRequest request)
			throws OPException, Exception {
		// 取得数据库连接
		// 建立事务
		SysParaConfigureVO tempVO = (SysParaConfigureVO) obj;
		String strRes = "";
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			SysParaConfigureDAO tempDAO = new SysParaConfigureDAO();

			strRes = tempDAO.delete(tempVO, conn);

			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_PARAMS, "",
					CacheManager.DELETE);
			LogManager.writeUpdateLog(
					"删除系统参数[" + tempVO.getSysParaConfigureParaname() + "]",
					"删除参数", tempVO.getSysParaConfigureParakey(),
					LogManager.RESULT_SUCCESS, user);
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				logger.error(ex);
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return strRes;
	}

}