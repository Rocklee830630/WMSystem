package com.ccthanking.business.xtbg.wdgzrz.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xtbg.wdgzrz.vo.GzrzVo;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class GzrzServiceImpl {
	
	/**
	 * 查看工作日志
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param mark
	 * @return
	 * @throws SQLException
	 */
	public String queryGzrz(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += " AND USERID='" + user.getAccount() + "'";
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT DIARYID, USERID, FBSJ, NR, RZLB, JLR, JLSJ, ZHXGSJ, YWLX, SJBH, BZ, " 
					+ "LRR, LRSJ, LRBM, LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX FROM XTBG_GZBG_GZRZ";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("RZLB", "RZLB");
			bs.setFieldFileUpload("DIARYID","3003");
			bs.setFieldClob("NR");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/**
	 * 查看工作日志
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param mark
	 * @return
	 * @throws SQLException
	 */
	public String queryWorkGzrz(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			
			condition += " AND RZLB='2' ";
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT DIARYID, USERID, FBSJ, NR, RZLB, JLR, JLSJ, ZHXGSJ, YWLX, SJBH, BZ, " 
					+ "LRR, LRSJ, LRBM, LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX FROM XTBG_GZBG_GZRZ";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("RZLB", "RZLB");
			bs.setFieldFileUpload("DIARYID","3003");
			bs.setFieldClob("NR");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/**
	 * 查看我的历史工作日志
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param mark
	 * @return
	 * @throws SQLException
	 */
	public String queryMyHistoryRz(String json, User user, String type) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);

			for (int i = 0; i < list.size(); i++) {
				if ("Dbms_Lob.Substr(t.daycontent)".equals(list.getQueryCondition(i).getField())) {
					String daycontent = list.getQueryCondition(i).getValue().replace("%", "");
					byte[] b = daycontent.getBytes();
					String result = bytesToHexString(b);   // 转换成十六进制
					list.getQueryCondition(i).setValue("%"+result+"%");
				}
			}
			String condition = list == null ? "" : list.getConditionWhere();
			
			
			condition += BusinessUtil.getCommonCondition(user, null);
			condition += "all".equals(type) ? " and DBMS_LOB.GETLENGTH(t.daycontent)<2000" : " AND t.DAYIEMPNAME='" + user.getName() + "' and DBMS_LOB.GETLENGTH(t.daycontent)<2000";
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT T.DAYID,T.DAYDATE,T.DAYIEMPNAME,T.DAYCONTENT,T.DAYIDATE,T.DAYMDATE,T.DAYTYPE," +
					"(select ta.ACCBELONGID from TAC_ACCESSORY ta where ta.accdir='Daily' and  ta.accbelongid=t.dayid and rownum=1) fj FROM TOA_DAILY T";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
	//		bs.setFieldFileUpload("DAYID","3003");
			bs.setFieldClob("DAYCONTENT");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/**
	 * 修改公告信息：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param operatorSign 操作标识
	 * @param ywid 业务ID（表示上传附件的ID）
	 * @return
	 * @throws Exception
	 */
	public String executeGzrz(String json, User user, String operatorSign, String ywid)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		GzrzVo gzrzVo = new GzrzVo();
		int sign = Integer.parseInt(operatorSign);
		try {
			conn.setAutoCommit(false);
			JSONArray list = gzrzVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			

			switch (sign) {
			case 1:
				msg = "添加";
				if (!"".equals(ywid)) {
					FileUploadService.updateFjztByYwid(ywid);
					jsonObj.put("DIARYID", ywid);
				} else {
					jsonObj.put("DIARYID", new RandomGUID().toString());
				}
				jsonObj.put("USERID", user.getAccount());
				jsonObj.put("JLR", user.getName());
				jsonObj.put("YWLX", YwlxManager.OA_GZBG_GZRZ);
				
				String time = Pub.getDate("HH:mm:ss", new Date());
				String date = jsonObj.getString("FBSJ");
				String datetime = date + " " + time;
				jsonObj.put("FBSJ", datetime);
				
				gzrzVo.setValueFromJson(jsonObj);
				BusinessUtil.setInsertCommonFields(gzrzVo, user);
				BaseDAO.insert(conn, gzrzVo);
				
				break;

			case 2:
				msg = "修改";
				jsonObj.put("ZHXGSJ", new Date());
				
				time = Pub.getDate("HH:mm:ss", new Date());
				date = jsonObj.getString("FBSJ");
				datetime = date + " " + time;
				jsonObj.put("FBSJ", datetime);
				
				gzrzVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(gzrzVo, user);
				BaseDAO.update(conn, gzrzVo);
				break;
				
			case 3:

				msg = "删除";
				jsonObj.put("SFYX", "0");
				jsonObj.put("ZHXGSJ", new Date());
				gzrzVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(gzrzVo, user);
				BaseDAO.update(conn, gzrzVo);
				break;
			
			default:
				break;
			}
			
			resultVo = gzrzVo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_GZBG_GZRZ,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "工作日志成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_GZBG_GZRZ,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "工作日志失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	private String bytesToHexString(byte[] src){
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString().toUpperCase();
	} 
	
	public String queryUnique(String date, User user) {
		String sql = "SELECT COUNT(1) CNT FROM XTBG_GZBG_GZRZ " 
				+ "WHERE to_date(to_char(fbsj,'yyyy-mm-dd'),'yyyy-mm-dd')=to_date('"+date+"','yyyy-mm-dd') " 
				+ "and userid='"+user.getAccount()+"'";
		String[][] cnt = DBUtil.query(sql);
		JSONObject jsonObj = new JSONObject();
		if ("0".equals(cnt[0][0])) {
			jsonObj.put("isUnique", "可以使用");
			jsonObj.put("sign", "0");
		} else {
			jsonObj.put("isUnique", "工作日志发布日志重复，请重新填写");
			jsonObj.put("sign", "1");
		}
		return jsonObj.toString();
	}
}
