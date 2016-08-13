package com.ccthanking.business.xtbg.ggtz.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xtbg.ggtz.service.GgtzService;
import com.ccthanking.business.xtbg.ggtz.vo.GgtzFbVo;
import com.ccthanking.business.xtbg.ggtz.vo.GgtzVo;
import com.ccthanking.business.xtbg.ggtz.vo.GgtzYdrVo;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.aplink.TaskMgrBean;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
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
public class GgtzServiceImpl implements GgtzService {
	
	/**
	 * 查询公告信息
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String queryGgtz(String json, User user) throws SQLException {
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
			
			String querySql = "SELECT GGID, GGBT, GGLB, FBFW, FBR, FBSJ, SHZT,decode(FBFWMC, '' , '全部', FBFWMC) FBFWMC, " 
					+ "YDCS, YWLX, SJBH, BZ, LRR, LRSJ, LRBM, LRBMMC, GXR, GXSJ, IS_TO_PERSON, " 
					+ "GXBM, GXBMMC, SJMJ, SFYX FROM XTBG_XXZX_GGTZ";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("GGLB", "GGLB");
			bs.setFieldDic("SHZT", "GGZT");
			bs.setFieldOrgDept("LRBM");
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	/**
	 * 查询公告信息
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String queryMoreGg(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
	//		String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
	//		condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition +=" and SHZT in('1','2') ";
			condition += " order by fbsj desc,lrsj desc";
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT FBID, GGID, GGID AS FJID, GGBT, GGLB, FBBM, FBR,FBBMMC,TO_CHAR(FBSJ,'yyyy\"年\"fmmm\"月\"dd\"日\"') as FBSJ_SN,NF, " 
					+ "JSR_ACCOUNT,JSR,JSBM,JSBMMC,SFYD,SFYX,FBFWMC,SHZT FROM XTBG_XXZX_GGTZ_FB";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDic("GGLB", "GGLB");
			bs.setFieldFileUpload("FJID", "");
			bs.setFieldUserID("FBR");

			domResult = bs.getJson();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}

	/**
	 * 阅读公告，每次浏览此公告，阅读次数加一
	 * @param json 页面传进来的对象json
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String readGgtz(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVo = null;
		try {
			GgtzVo ggtzVo = new GgtzVo();
			conn.setAutoCommit(false);
			JSONArray list = ggtzVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			String count = "".equals(jsonObj.getString("YDCS")) ? "0" : jsonObj.getString("YDCS");
			int ydcs = Integer.parseInt(count) + 1;
			jsonObj.put("YDCS", ydcs);
			
			/*--------------------插入阅读表数据--------------*/
			// 查詢XTBG_XXZX_GGTZ_YDR（閱讀人表）中此人是否存在
			String isReadSql = "SELECT COUNT(1) FROM XTBG_XXZX_GGTZ_YDR WHERE GGID='" 
					+ jsonObj.getString("GGID") + "' AND USERID='" + user.getAccount() + "'";
			String[][] readCount = DBUtil.query(isReadSql);
			

			GgtzYdrVo ggtzYdrVo = new GgtzYdrVo();
			ggtzYdrVo.setGgid(jsonObj.getString("GGID"));
			ggtzYdrVo.setUserid(user.getAccount());
			// 如果不存在，添加此人
			if ("0".equals(readCount)) {
				ggtzYdrVo.setLastreadtime(new Date());
				BaseDAO.insert(conn, ggtzYdrVo);
			// 如果存在，更改閱讀時間
			} else {
				ggtzYdrVo.setLastreadtime(new Date());
				BaseDAO.update(conn, ggtzYdrVo);
			}
			/*-----------------------------------------------*/
			
			ggtzVo.setValueFromJson(jsonObj);
			BusinessUtil.setInsertCommonFields(ggtzVo, user);
			BaseDAO.update(conn, ggtzVo);

			resultVo = ggtzVo.getRowJson();
			
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_XXZX_TZGG,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "阅读公告", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_XXZX_TZGG,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "阅读公告失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	public void readGgtz(String ggid, String ydcs, User user) throws Exception  {
		Connection conn = DBUtil.getConnection();
		try {
			/*--------------------插入阅读表数据--------------*/
			// 查詢XTBG_XXZX_GGTZ_YDR（閱讀人表）中此人是否存在
			String isReadSql = "SELECT COUNT(1) FROM XTBG_XXZX_GGTZ_YDR WHERE GGID='" 
					+ ggid + "' AND USERID='" + user.getAccount() + "'";
			String[][] readCount = DBUtil.query(isReadSql);
			

			GgtzYdrVo ggtzYdrVo = new GgtzYdrVo();
			ggtzYdrVo.setGgid(ggid);
			ggtzYdrVo.setUserid(user.getAccount());
			// 如果不存在，添加此人
			if ("0".equals(readCount[0][0])) {
				ggtzYdrVo.setLastreadtime(new Date());
				BaseDAO.insert(conn, ggtzYdrVo);
			// 如果存在，更改閱讀時間
			} else {
				ggtzYdrVo.setLastreadtime(new Date());
				BaseDAO.update(conn, ggtzYdrVo);
			}
			/*-----------------------------------------------*/

			/*--------------------处理发布表阅读数据--------------*/
			String readSql = "UPDATE XTBG_XXZX_GGTZ_FB SET SFYD='1', YDSJ=SYSDATE " 
					+ "WHERE GGID='" + ggid + "' AND JSR_ACCOUNT='" + user.getAccount() + "'";
			DBUtil.exec(conn, readSql);
			/*-----------------------------------------------*/

			
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_XXZX_TZGG,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "阅读公告", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_XXZX_TZGG,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "阅读公告失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
	}
	
	/**
	 * 修改公告信息：包括添加、修改、删除，分别用1、2、3表示
	 * @param json 页面传进来的对象json
	 * @param user
	 * @param operatorSign 操作标识
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	@Override
	public String executeGg(String json, User user, String operatorSign, 
			HttpServletRequest request, String ywid,String sffb)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		try {
			GgtzVo ggtzVo = new GgtzVo();
			conn.setAutoCommit(false);
			JSONArray list = ggtzVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "添加";
				if (!"".equals(ywid)) {
					FileUploadService.updateFjztByYwid(conn, ywid);
					jsonObj.put("GGID", ywid);
				} else {
					jsonObj.put("GGID", new RandomGUID().toString());
				}
				
				jsonObj.put("FBR", user.getName());
				jsonObj.put("SHZT", "0");
				jsonObj.put("YWLX", YwlxManager.OA_XXZX_TZGG);

				if ("1".equals(jsonObj.getString("IS_TO_PERSON"))) {
					String jsry = request.getParameter("jsry");
					String jsrmc = request.getParameter("jsrmc");
					jsonObj.put("FBFWMC", jsrmc);
					jsonObj.put("FBFW", jsry);
				}
				ggtzVo.setValueFromJson(jsonObj);
				EventVO eventVO = EventManager.createEvent(conn, ggtzVo.getYwlx(), user);//生成事件
				ggtzVo.setSjbh(eventVO.getSjbh());
				BusinessUtil.setInsertCommonFields(ggtzVo, user);
				BaseDAO.insert(conn, ggtzVo);
				if(!Pub.empty(sffb)&&"1".equals(sffb)) {
					//publishGg(conn,ggtzVo.getGgid(), user, request,"1");
					createSH(conn, ggtzVo.getGgid(),ggtzVo.getSjbh(),ggtzVo.getYwlx(), user, request);
				}
				
				break;

			case 2:

				msg = "修改";
				jsonObj.put("YWLX", YwlxManager.OA_XXZX_TZGG);
				jsonObj.put("FBR", user.getName());
				System.out.println(jsonObj.getString("IS_TO_PERSON"));
				System.out.println(jsonObj);
				if ("1".equals(jsonObj.getString("IS_TO_PERSON"))) {
					String jsry = request.getParameter("jsry");
					String jsrmc = request.getParameter("jsrmc");
					jsonObj.put("FBFWMC", jsrmc);
					jsonObj.put("FBFW", jsry);
				}
				ggtzVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(ggtzVo, user);
				BaseDAO.update(conn, ggtzVo);
				if(!Pub.empty(sffb)&&"1".equals(sffb)) {
					//publishGg(conn,ggtzVo.getGgid(), user, request,"0");
					createSH(conn, ggtzVo.getGgid(),ggtzVo.getSjbh(),ggtzVo.getYwlx(), user, request);
				}
				break;
				
			case 3:
				msg = "作废";
				jsonObj.put("SHZT", "2");
				ggtzVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(ggtzVo, user);
				BaseDAO.update(conn, ggtzVo);
				DBUtil.exec(conn,"update xtbg_xxzx_ggtz_fb set shzt='2' where ggid='"+ggtzVo.getGgid()+"'");
				break;
			case 4:
				msg = "删除";
				jsonObj.put("SFYX", "0");
				ggtzVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(ggtzVo, user);
				BaseDAO.update(conn, ggtzVo);
				// 关联发布表的人员
				DBUtil.exec(conn, "UPDATE XTBG_XXZX_GGTZ_FB SET SFYX='0' WHERE GGID='"+ggtzVo.getGgid()+"'");
				break;
			default:
				break;
			}
			
			resultVo = ggtzVo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_XXZX_TZGG,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "公告信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_XXZX_TZGG,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "公告信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	public String[] cqsh(String ggid,String sjbh,String ywlx, User user, HttpServletRequest request) {
		
		Connection conn = DBUtil.getConnection();
		String[] list = null;
		try {
			conn.setAutoCommit(false);
			createSH(conn, ggid,sjbh,ywlx, user, request);
			conn.commit();

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return list;
	}
	

	private void createSH(Connection conn,String ggid,String sjbh,String ywlx, User user, HttpServletRequest request) throws Exception{
        TaskMgrBean bo = new TaskMgrBean();
        String dscr = user.getName()+"登记的通知公告，请审核！";
        String dbrid = "";
        OrgDept dept = user.getOrgDept();
        dbrid = dept.getFzr();
        //通知公告发送待办功能， 暂时屏蔽
        /*
        TaskVO vo = bo.createTask("", sjbh, ywlx, dscr, user, conn);
        
        vo.setLINKURL("jsp/business/xtbg/xxzx/gg/ggsh.jsp");
        vo.setDBRYID(dbrid);
        vo.setDBDWDM(user.getDepartment());
        vo.setRWZT("01");
        bo.saveTask(conn, vo);
        */
        DBUtil.exec(conn, "update XTBG_XXZX_GGTZ set SHZT='3' where ggid='"+ggid+"'");
		
	}
	
	public void doggsh(String ggid,String shyj,String shjg,String taskid,String sjbh, User user, HttpServletRequest request) {
		TaskMgrBean bo = new TaskMgrBean();
		Connection conn = DBUtil.getConnection();
		String[] list = null;
		try {
			conn.setAutoCommit(false);
			String[] fqr = new String[1];
			String desc = "";
			String title = "";
//			String[][] ss = DBUtil.query(conn, "select cjrid from ap_task_schedule where sjbh = '"+sjbh+"'");
//			if(ss!=null&&ss.length>0){
//				fqr[0] = ss[0][0];
//			}
//			ss = DBUtil.query(conn, "select GGBT from XTBG_XXZX_GGTZ where ggid = '"+ggid+"'");
//			if(ss!=null&&ss.length>0){
//				title = ss[0][0];
//			}
			
			if("1".equals(shjg))//不同意  发送消息
			{
				//desc = "您发布的【 "+title+"】通知公告，领导审核不通过,审核意见为："+shyj;
			    DBUtil.exec(conn, "update XTBG_XXZX_GGTZ set SHZT='0' where ggid='"+ggid+"'");
				
			}else{//同意 信息发布同时发送消息
				//desc = "您发布的【 "+title+"】通知公告，领导已经审核通过,审核意见为："+shyj;
				publishGg(conn, ggid, user, request, "1");
				DBUtil.exec(conn, "update XTBG_XXZX_GGTZ set SHZT='1' where ggid='"+ggid+"'");
			}
//			 sendMessage.sendMessageToPerson(conn, "通知公告审核信息", desc, user.getAccount(),fqr[0], false, false, true, "");
//			PushMessage.pushInfoNotInMsg(request, desc, null, null, YwlxManager.OA_XXZX_TZGG, null,fqr , null);
//			bo.doTask(conn, taskid,"", sjbh, "200201", "", shjg, "", user);
						
			conn.commit();

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		
	}
	
	
	public String[] publishGg(String ggid, User user, HttpServletRequest request) {
		
		Connection conn = DBUtil.getConnection();
		String[] list = null;
		try {
			conn.setAutoCommit(false);
			list = publishGg(conn, ggid, user, request, "1");
			conn.commit();

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return list;
	}
	
	
	private String[] publishGg(Connection conn,String ggid, User user, 
			HttpServletRequest request, String isFb) throws Exception{
	//	try {
			String queryGgxxSql = "SELECT GGBT, GGLB, FBFW, NR, FBR, " 
					+ "TO_CHAR(FBSJ,'YYYY-MM-DD HH24:MI:SS'), " 
					+ "SHZT, YDCS, LRR, LRSJ, LRBM, LRBMMC, SFYX,fbfwmc,IS_TO_PERSON " 
					+ "FROM XTBG_XXZX_GGTZ WHERE GGID='" + ggid + "'";
			String[][] ggxxList = DBUtil.query(conn, queryGgxxSql);
			
			/* 公告标题 */
			String ggbt = ggxxList[0][0];
			/* 公告类别 */
			String gglb = ggxxList[0][1];
			String fbfw = ggxxList[0][2];
			/* 公告时间 */
			String fbsj = ggxxList[0][5];
			/* 年份 */
			String nf = fbsj.substring(0, 4);
			/* 发布人（录入人） */
			String fbr = ggxxList[0][8];
			/* 录入时间 */
			String lrsj = ggxxList[0][9];
			/* 发布部门（录入人所在部门） */
			String fbbm = ggxxList[0][10];
			/* 发布部门名称（录入人所在部门名称） */
			String fbbmmc = ggxxList[0][11];
			/* 是否有效 */
			String sfyx = ggxxList[0][12];

			/* 发布范围 */
			String allFbfw = ggxxList[0][2];
			
			String fbfwmc = ggxxList[0][13];
			
			String isToPerson = ggxxList[0][14];
			
			String[] account = null;
			if ("1".equals(isToPerson)) {
				
				account = fbfw.split(",");
				for (int i = 0; i < account.length; i++) {
					
					GgtzFbVo ggtzFbVo = new GgtzFbVo();

					/* 发布ID */
					String fbid = new RandomGUID().toString();
					/* 接收人ID */
					String jsr_account = account[i];
				//	account[i] = jsr_account;
					/* 接收人 */
					String jsr = UserManager.getInstance().getUserByLoginNameFromNc(jsr_account).getName();
					String jsbm = "";
					String jsbmmc = "";
					
					
						 jsbm = Pub.getUserDepartmentById(account[i]);
						/* 接收部门名称 */
						 jsbmmc = OrgDeptManager.getInstance().getDeptByID(jsbm).getDeptName();
					/* 接收部门 */
					
					
					ggtzFbVo.setFbid(fbid);
					ggtzFbVo.setGgid(ggid);
					ggtzFbVo.setGgbt(ggbt);
					ggtzFbVo.setGglb(gglb);
					ggtzFbVo.setFbr(fbr);
					ggtzFbVo.setFbbm(fbbm);
					ggtzFbVo.setFbbmmc(fbbmmc);
					ggtzFbVo.setFbsj(Pub.toDate("yyyy-MM-dd HH:mm:ss", fbsj));
					ggtzFbVo.setLrsj(Pub.toDate("yyyy-MM-dd HH:mm:ss", lrsj));
					ggtzFbVo.setNf(nf);
					ggtzFbVo.setJsr_account(jsr_account);
					ggtzFbVo.setJsr(jsr);
					ggtzFbVo.setJsbm(jsbm);
					ggtzFbVo.setJsbmmc(jsbmmc);
					ggtzFbVo.setSfyx(sfyx);
					if(Pub.empty(allFbfw)){
						ggtzFbVo.setFbfwmc("全部");
					} else {
						ggtzFbVo.setFbfwmc(fbfwmc);
					}
					
					BaseDAO.insert(conn, ggtzFbVo);
				}
			} else {
				allFbfw = allFbfw.replaceAll(",", "','");
				String condition = "".equals(allFbfw) ? " where flag =1  " : " WHERE DEPARTMENT IN ('" + allFbfw + "') and flag =1";
				/* 发布范围为空，发布范围是所有人；否则在规定的范围内发布 */
				String queryAccountSql = "SELECT ACCOUNT, DEPARTMENT FROM VIEW_YW_ORG_PERSON " + condition;
				String[][] accountList = DBUtil.query(conn, queryAccountSql);
				
				
				/* 在发布范围内发布公告 添加XTBG_XXZX_GGTZ_FB表  start *********************************** */
				account = new String[accountList.length];
				for (int i = 0; i < accountList.length; i++) {
					
					GgtzFbVo ggtzFbVo = new GgtzFbVo();

					/* 发布ID */
					String fbid = new RandomGUID().toString();
					/* 接收人ID */
					String jsr_account = accountList[i][0];
					account[i] = jsr_account;
					/* 接收人 */
					String jsr = UserManager.getInstance().getUserByLoginNameFromNc(jsr_account).getName();
					String jsbm = "";
					String jsbmmc = "";
					if(!Pub.empty(accountList[i][1])){
						 jsbm = accountList[i][1];
						/* 接收部门名称 */
						 jsbmmc = OrgDeptManager.getInstance().getDeptByID(jsbm).getDeptName();
					}
					/* 接收部门 */
					
					
					ggtzFbVo.setFbid(fbid);
					ggtzFbVo.setGgid(ggid);
					ggtzFbVo.setGgbt(ggbt);
					ggtzFbVo.setGglb(gglb);
					ggtzFbVo.setFbr(fbr);
					ggtzFbVo.setFbbm(fbbm);
					ggtzFbVo.setFbbmmc(fbbmmc);
					ggtzFbVo.setFbsj(Pub.toDate("yyyy-MM-dd HH:mm:ss", fbsj));
					ggtzFbVo.setLrsj(Pub.toDate("yyyy-MM-dd HH:mm:ss", lrsj));
					ggtzFbVo.setNf(nf);
					ggtzFbVo.setJsr_account(jsr_account);
					ggtzFbVo.setJsr(jsr);
					ggtzFbVo.setJsbm(jsbm);
					ggtzFbVo.setJsbmmc(jsbmmc);
					ggtzFbVo.setSfyx(sfyx);
					if(Pub.empty(allFbfw)){
						ggtzFbVo.setFbfwmc("全部");
					} else {
						ggtzFbVo.setFbfwmc(fbfwmc);
					}
					
					BaseDAO.insert(conn, ggtzFbVo);
				}
			}
			
			/* 在发布范围内发布公告 添加XTBG_XXZX_GGTZ_FB表  end ************************************* */
			
			// 发送消息图示框
			if ("1".equals(isFb)) {
				//PushMessage.pushInfoNotInMsg(request, "您有一条新的通知公告", null, null, YwlxManager.OA_XXZX_TZGG, null, account, null);
			}
			
			
			
			/* 修改公告状态 XTBG_XXZX_GGTZ（SHZT=1，已发布）  start *************************** */
			GgtzVo ggtzVo = new GgtzVo();
			ggtzVo.setGgid(ggid);
			ggtzVo.setShzt("1");
			BusinessUtil.setUpdateCommonFields(ggtzVo, user);
			BaseDAO.update(conn, ggtzVo);
			
			// 此方法被调用，不需要commit
			return account;
	}
	//首页信息中心显示通知公告
	
    public String frameGgtz (User user) throws Exception {
		
		String userid = user.getAccount();
		String json = "";
		String[][] messages = DBUtil.query("select GGBT,GGBT,GGID from  XTBG_XXZX_GGTZ_FB where JSR_ACCOUNT = '"+userid+"' and SFYX = '1' and SFYD='0' and GGLB in ('GG','TZ') and rownum<=5 order by fbsj desc,lrsj desc");
		String[][] mcount = DBUtil.query("select count(1) from  XTBG_XXZX_GGTZ_FB where JSR_ACCOUNT = '"+userid+"' and SFYX = '1' and SFYD='0' and GGLB in ('GG','TZ') ");
		if(messages!=null&&messages.length>0)
		{
			json +="{count:"+mcount[0][0]+",messages:[";
			for(int i = 0;i<messages.length;i++)
			{
				json +="{title:'"+messages[i][0]+"',content:'"+messages[i][1]+"',ggid:'"+messages[i][2]+"'},";
			}
			json +="]}";
		}else{
			json = "{count:0}";
		}
		
		
		return json;
		
	}
	

	@Override
	public GgtzVo findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GgtzVo> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GgtzVo> find(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int remove(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int remove(List<Integer> ids) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(GgtzVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(GgtzVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}
}
