package com.ccthanking.framework.tx;

import java.sql.Connection;
import java.util.Date;

import com.ccthanking.common.vo.AlertInfoVO;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.Role;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

public class AlertManager {

	public static boolean AlertFinish(Connection conn, User user, String XH) // 提醒归档
	{
		try {
			AlertInfoVO vo = new AlertInfoVO();
			vo.setGdr(user.getAccount());
			vo.setGdsj(Pub.getCurrentDate());
			vo.setZt("0");
			vo.setXh(XH);
			BaseDAO dao = new BaseDAO();
			dao.update(conn, vo);
		} catch (Exception e) {
			return false;
		}
		return true;

	}
	
	public static boolean AlertFinish(Connection conn, User user,String ywlx,String sjbh){
		try{
			String sql = "update ALERT_INFO set zt = '0',GDR = '" + user.getAccount() + "',GDSJ = sysdate where ywlx = '" 
					+ ywlx + "' and sjbh = '" + sjbh + "'";
			return DBUtil.execUpdateSql(conn, sql);
		} catch (Exception e) {
			return false;
		}
		
	}

	public static String AlertSet(Connection conn, String Desr,
			String OverRun, String Txr, String Txdw, String TxRole,
			String ywlx, String sjbh, String LinkUrl) // 设置提醒
	{
		AlertInfoVO vo = new AlertInfoVO();
		try {
			vo.setXh(new RandomGUID().toString());
			vo.setDesr(Desr);
			vo.setOverrun(OverRun);
			vo.setDjsj(Pub.getCurrentDate());
			vo.setZt("2");
			vo.setTxr(Txr);
			vo.setTxdw(Txdw);
			vo.setTxrole(TxRole);
			vo.setYwlx(ywlx);
			vo.setSjbh(sjbh);
			vo.setLinkurl(LinkUrl);
			BaseDAO dao = new BaseDAO();
			dao.insert(conn, vo);
		} catch (Exception e) {
			return null;
		}
		return vo.getXh();
	}

	public static boolean AlertKnown(Connection conn, String XH) // 提醒已知晓
	{
		try {
			AlertInfoVO vo = new AlertInfoVO();
			vo.setZt("1");
			vo.setXh(XH);
			BaseDAO dao = new BaseDAO();
			dao.update(conn, vo);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	/**
	 * 增加Djsj参数，可以自定义提醒的起算时间
	 * 
	 * @param conn
	 * @param Djsj
	 * @param Desr
	 * @param OverRun
	 * @param Txr
	 * @param Txdw
	 * @param TxRole
	 * @param ywlx
	 * @param sjbh
	 * @param LinkUrl
	 * @return
	 */
	public static String AlertSet(Connection conn, Date Djsj, String Desr,
			String OverRun, String Txr, String Txdw, String TxRole,
			String ywlx, String sjbh, String LinkUrl) // 设置提醒
	{
		AlertInfoVO vo = new AlertInfoVO();
		try {
			/**
			vo.setXh(com.ccthanking.common.SequenceUtil
					.getCommonSerivalNumber());
			**/
			vo.setXh(new RandomGUID().toString());
			vo.setDesr(Desr);
			vo.setOverrun(OverRun);
			vo.setDjsj(Djsj);
			vo.setZt("2");
			vo.setTxr(Txr);
			vo.setTxdw(Txdw);
			vo.setTxrole(TxRole);
			vo.setYwlx(ywlx);
			vo.setSjbh(sjbh);
			vo.setLinkurl(LinkUrl);
			BaseDAO dao = new BaseDAO();
			dao.insert(conn, vo);
		} catch (Exception e) {
			return null;
		}
		return vo.getXh();
	}
	
	
	/**
	 * 根据ywlx和sjbh判断是否存在未处理的提醒
	 * @param conn
	 * @param ywlx
	 * @param sjbh
	 * @return
	 */
	public static boolean HasValidAlert(Connection conn,
			String ywlx, String sjbh)
	{
		boolean result = false;
		try {
			String sql = "select count(*) from ALERT_INFO where ywlx = ? and sjbh = ? and zt <> '0'";
			Object[] paras = new Object[2];
			paras[0] = ywlx;
			paras[1] = sjbh;
			String alerts[][] = DBUtil.querySql(conn, sql, paras);
			if(null != alerts && alerts.length > 0){
				String num = alerts[0][0];
				result = new Integer(num) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
	/**
	 * 根据xh判断是否存在未处理的提醒
	 * @param conn
	 * @param xh
	 * @return
	 */
	public static boolean HasValidAlert(Connection conn,
			String xh)
	{
		boolean result = false;
		try {
			String sql = "select count(*) from ALERT_INFO where xh = ? and zt <> '0'";
			Object[] paras = new Object[1];
			paras[0] = xh;
			String alerts[][] = DBUtil.querySql(conn, sql, paras);
			if(null != alerts && alerts.length > 0){
				String num = alerts[0][0];
				result = new Integer(num) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
	/**
	 * 重新开启提醒
	 * @param conn
	 * @param XH
	 * @return
	 */
	public static boolean AlertReload(Connection conn, String XH){
		try {
			AlertInfoVO vo = new AlertInfoVO();
			vo.setZt("2");
			vo.setXh(XH);
			BaseDAO dao = new BaseDAO();
			dao.update(conn, vo);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据ywlx和sjbh判断是否存在提醒(不判断状态)
	 * @param conn
	 * @param ywlx
	 * @param sjbh
	 * @return
	 */
	public static boolean HasAlert(Connection conn,
			String ywlx, String sjbh)
	{
		boolean result = false;
		try {
			String sql = "select count(*) from ALERT_INFO where ywlx = ? and sjbh = ?";
			Object[] paras = new Object[2];
			paras[0] = ywlx;
			paras[1] = sjbh;
			String alerts[][] = DBUtil.querySql(conn, sql, paras);
			if(null != alerts && alerts.length > 0){
				String num = alerts[0][0];
				result = new Integer(num) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
}