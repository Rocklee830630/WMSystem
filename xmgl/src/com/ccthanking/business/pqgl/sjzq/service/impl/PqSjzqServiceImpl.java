package com.ccthanking.business.pqgl.sjzq.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.pqgl.sjzq.service.PqSjzqService;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class PqSjzqServiceImpl implements PqSjzqService{
	private static String propertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_pq";
	/**
	 * 数据钻取查询查询
	 * 排迁内业和排迁任务都用这个方法呢，修改的时候要注意
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingDataZxm(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL_ZXM");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and S.ND='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("WGSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("KGSJ_JH", "yyyy-MM-dd");
			bs.setFieldDateFormat("WCSJ_JH", "yyyy-MM-dd");
			bs.setFieldDateFormat("QHRQ", "yyyy-MM-dd");
			bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldUserID("XMFZR");
			bs.setFieldDic("ISJHWC", "SF");
			bs.setFieldDic("HTZT", "HTRXZT");//合同状态
			bs.setFieldDic("GXLB", "GXLB");	//管线类别
			bs.setFieldDic("HTSX", "HTSX");	//合同属性
			bs.setFieldThousand("HTQDJ");	//合同签订价
			bs.setFieldThousand("SDZ");		//合同结算价
			bs.setFieldThousand("HTZF");	//已支付合同款
			bs.setFieldThousand("WFHTK");	//未支付合同款
			bs.setFieldFileUpload("ZYGXPQT","0001");
			bs.setFieldFileUpload("PQLLD","0002");
			bs.setFieldFileUpload("YSSSD","0003");
			bs.setFieldFileUpload("GCYSSDB","0004");
			bs.setFieldFileUpload("WTH","0005");
			bs.setFieldFileUpload("HT","0006");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 数据钻取查询查询
	 * 排迁内业和排迁任务都用这个方法呢，修改的时候要注意
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@Override
	public String queryDrillingDataZh(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		try { 
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String sql =Pub.getPropertiesSqlValue(propertyFileName, "THEADSQL_ZH");
			sql = sql.replace("%CONDSQL%",Pub.getPropertiesSqlValue(propertyFileName, proKey));
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and S.ND='"+nd+"' ";
			sql = sql.replaceAll("%ndCondition%", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("WGSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("KGSJ_JH", "yyyy-MM-dd");
			bs.setFieldDateFormat("WCSJ_JH", "yyyy-MM-dd");
			bs.setFieldDateFormat("QHRQ", "yyyy-MM-dd");
			bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			bs.setFieldUserID("XMFZR");
			bs.setFieldDic("ISJHWC", "SF");
			bs.setFieldDic("HTZT", "HTRXZT");//合同状态
			bs.setFieldDic("GXLB", "GXLB");	//管线类别
			bs.setFieldDic("HTSX", "HTSX");	//合同属性
			bs.setFieldThousand("HTQDJ");	//合同签订价
			bs.setFieldThousand("SDZ");		//合同结算价
			bs.setFieldThousand("HTZF");	//已支付合同款
			bs.setFieldThousand("WFHTK");	//未支付合同款
			bs.setFieldFileUpload("ZYGXPQT","0001");
			bs.setFieldFileUpload("PQLLD","0002");
			bs.setFieldFileUpload("YSSSD","0003");
			bs.setFieldFileUpload("GCYSSDB","0004");
			bs.setFieldFileUpload("WTH","0005");
			bs.setFieldFileUpload("HT","0006");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
