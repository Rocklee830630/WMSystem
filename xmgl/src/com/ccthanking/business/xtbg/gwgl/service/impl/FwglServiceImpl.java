package com.ccthanking.business.xtbg.gwgl.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xtbg.gwgl.vo.FwglVo;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class FwglServiceImpl {
	
	public String queryFw(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT FWID, WJBH, WJBT, LSH, DQZT, NGDW, NGRQ, NGR, " 
						+ "FWSJ, WZ, HJ, SSXM, ZS, CS, MJ, DYFS,SJBH,YWLX,LHFW FROM XTBG_GWGL_FWGL";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
//			bs.setFieldTranslater(fieldName, tableName, codeField, valueField);
			bs.setFieldTranslater("ZS", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
			bs.setFieldTranslater("CS", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
			bs.setFieldDic("HJ", "HJ1000000000302");
			bs.setFieldDic("DQZT", "SFWZT");
			bs.setFieldDic("WZ", "WZ");
			bs.setFieldOrgDept("NGDW");
			bs.setFieldSjbh("SJBH");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	public String executeFw(String json, User user, String operatorSign, String ywid)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		try {
			FwglVo fwglVo = new FwglVo();
			conn.setAutoCommit(false);
			JSONArray list = fwglVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "登记";
				if (!"".equals(ywid)) {
					FileUploadService.updateFjztByYwid(conn, ywid);

					jsonObj.put("FWID", ywid);
				} else {
					jsonObj.put("FWID", new RandomGUID().toString());
				}
				
				
				String wz = jsonObj.getString("WZ").toString();
				
				jsonObj.put("YWLX", YwlxManager.OA_GWGL_FWGL);
				jsonObj.put("DQZT", Globals.OA_GWGL_DJ);
				fwglVo.setValueFromJson(jsonObj);
				//add by cbl
				EventVO eventVO = EventManager.createEvent(conn, fwglVo.getYwlx(), user);//生成事件
				fwglVo.setSjbh(eventVO.getSjbh());
				//add by cbl end
				BusinessUtil.setInsertCommonFields(fwglVo, user);
				BaseDAO.insert(conn, fwglVo);
				
				//add by liujs at 2013年11月13日 18:15:47 start	附件信息
				FileUploadVO inVo=new FileUploadVO();
				inVo.setFjzt("1");
				inVo.setYwlx(YwlxManager.OA_GWGL_FWGL);
				inVo.setSjbh(fwglVo.getSjbh());
				FileUploadService.updateVOByYwid(conn,inVo,fwglVo.getFwid(),user);
				//add end
				
				String sql = "update fs_fileupload set sjbh='" + eventVO.getSjbh() + "'," 
						+ "ywlx='" + YwlxManager.OA_GWGL_FWGL + "' where ywid='"+ywid+"'";
				DBUtil.execUpdateSql(conn, sql);
				//插入ap_processconfig表
				String operationoid = "2".equals(wz) ? "43454" : "43466";
				String insert_sql =
	                    "insert into AP_PROCESSCONFIG (AP_PROCESS_ID,WS_TEMPLATEID,YWLX,SJBH,LRR,LRSJ,OPERATIONOID) values('" +
	                    		fwglVo.getFwid() + "','" + wz + "','" + fwglVo.getYwlx() + "','"+fwglVo.getSjbh()+"','"+user.getAccount()+"',SYSDATE,'" + operationoid + "')";
	                DBUtil.execSql(conn, insert_sql);
				break;

			case 2:

				msg = "修改";
				fwglVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(fwglVo, user);
				
				//add by liujs at 2013年11月13日 18:16:28 附件问题
				FileUploadVO upVo=new FileUploadVO();
				upVo.setFjzt("1");
				FileUploadService.updateVOByYwid(conn,upVo,fwglVo.getFwid(),user);
				//add end
				
				BaseDAO.update(conn, fwglVo);
				break;
			
			default:
				break;
			}
			
			resultVo = fwglVo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_GWGL_FWGL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "发文信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_GWGL_FWGL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "发文信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	
	/**
	 * 查询历史
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public String queryLs(String json, User user) throws SQLException {
		return queryLs(json,user,"");
	}
	
	/**
	 * 查询历史,可传入条件
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public String queryLs(String json, User user,String defineCondition) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? " 1 = 1" : list.getConditionWhere();
			condition += defineCondition;
			condition += orderFilter;
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "select t.*,(select flaflwid from TAC_FLOWAPPLY where flaid = t.fitflaid) as flowid,(select flwno from TAC_FLOW where flwid = (select flaflwid from TAC_FLOWAPPLY where flaid = t.fitflaid)) as flwno from TOA_FILEOUT t";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDateFormat("FITIDATE", "yyyy-MM-dd");
			bs.setFieldDateFormat("FITDJDATE", "yyyy-MM-dd");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/**
	 * 查询个人历史，含发起和审批
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public String queryLsByPerson(String json, User user) throws SQLException {
		String defineConditon = "  and ((t.FITIEMPNAME = '" + user.getName() + "') or ( t.fitflaid in (select fleflaid from TAC_FLOWEXECUTE where flehempname = '" + user.getName() + "')))" ;
		return queryLs(json,user,defineConditon);
	}

	public String deleteFw(String id) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);

			String sql = "UPDATE XTBG_GWGL_FWGL SET SFYX='0' WHERE FWID='"+id+"'";
			DBUtil.execUpdateSql(conn, sql);
			conn.commit();
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			return "0";
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return "1";
	}
}
