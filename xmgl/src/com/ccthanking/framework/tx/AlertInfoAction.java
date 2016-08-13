package com.ccthanking.framework.tx;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.AlertInfoVO;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.NotesInfoVO;
import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.Role;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.msgmgrVO;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

public class AlertInfoAction extends BaseDispatchAction {
	
	
	// 提醒信息表插入
	public ActionForward Insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		Connection conn = DBUtil.getConnection();
		String ywlx = ""; // 业务类型
		try {
			conn.setAutoCommit(false);
			EventVO eventVO = EventManager.createEvent(conn, ywlx, user);// 生成事件
			List list = doc.selectNodes("/DATAINFO/ROW");

			Element row = (Element) list.get(0);
			AlertInfoVO vo = new AlertInfoVO();
			vo.setValue(row);

			BaseDAO dao = new BaseDAO();
			dao.insert(conn, vo);
			EventManager.archiveEvent(conn, eventVO, user);
			conn.commit();
			Pub.writeXmlMessage(response, vo.getRowXml(), "操作成功！", "MESSAGE");
			LogManager.writeUserLog(eventVO.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					Pub.getDictValueByCode("YWLX", ywlx), user,"XH",vo.getXh());
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog("", ywlx, Globals.OPERATION_TYPE_INSERT,
					LogManager.RESULT_FAILURE,
					Pub.getDictValueByCode("YWLX", ywlx)+","+e.getMessage(), user,null,null);
			Pub.writeXmlErrorMessage(response, this.handleError(e));

		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	
	// 提醒信息表查询
	public ActionForward QueryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String orderFilter = RequestUtil.getOrderFilter(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		// 此处可以设置自定义的过滤条件
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		condition += orderFilter;
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Document domresult = null;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "select XH , DESR , OVERRUN , ZT , DJSJ , TXR , TXROLE , TXDW , GDR , GDSJ , YWLX , SJBH , LINKURL from ALERT_INFO";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			// 设置字典翻译定义

			// 设置时间的显示格式
			bs.setFieldDateFormat("DJSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("GDSJ", "yyyy-MM-dd");
			domresult = bs.getDocument();
			Pub.writeXmlDocument(response, domresult, "UTF-8");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	
	// 提醒信息表修改
	public ActionForward Update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		Connection conn = DBUtil.getConnection();
		String ywlx = ""; // 业务类型
		AlertInfoVO vo = new AlertInfoVO();
		try {
			conn.setAutoCommit(false);
			EventVO eventVO = EventManager.createEvent(conn, ywlx, user);// 生成事件
			List list = doc.selectNodes("/DATAINFO/ROW");
			Element row = (Element) list.get(0);
			vo.setValue(row);

			BaseDAO dao = new BaseDAO();
			dao.update(conn, vo);
			EventManager.archiveEvent(conn, eventVO, user);
			conn.commit();
			LogManager.writeUserLog(eventVO.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					Pub.getDictValueByCode("YWLX", ywlx), user,"XH",vo.getXh());
			Pub.writeXmlMessage(response, vo.getRowXml(), "操作成功！", "MESSAGE");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog("", ywlx, Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_FAILURE,
					Pub.getDictValueByCode("YWLX", ywlx)+","+e.getMessage(), user,"XH",vo.getXh());
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	
	// 提醒信息表删除
	public ActionForward Delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			List list = doc.selectNodes("/DATAINFO/ROW");
			Element row = (Element) list.get(0);
			AlertInfoVO vo = new AlertInfoVO();
			vo.setValue(row);
			BaseDAO dao = new BaseDAO();
			dao.delete(conn, vo);
			conn.commit();
			Pub.writeXmlInfoMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	public ActionForward QueryAlertInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		
		PageManager page = new PageManager();
		page.setPageRows(1000);

		Document domresult = null;
		String mode = request.getParameter("mode");
		try {
			if (user == null)
				throw new Exception("");
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "";
			//根据提醒对应的提醒人进行查询
			if (mode.equals("1")){//只查询未处理的
				sql = "select XH , DESR , OVERRUN , ZT , DJSJ , TXR , TXROLE , TXDW , GDR , GDSJ , YWLX , SJBH , LINKURL,'提醒' as lx,'tx' as RWLX from ALERT_INFO where ZT='2' and SYSDATE>=DJSJ+OVERRUN/24 and (TXR='"
						+ user.getAccount() + "' ";
			}else if(mode.equals("2")){//查询未处理和已知晓的
				sql = "select XH , DESR , OVERRUN , ZT , DJSJ , TXR , TXROLE , TXDW , GDR , GDSJ , YWLX , SJBH , LINKURL,'提醒' as lx,'tx' as RWLX from ALERT_INFO where ZT<>'0' and SYSDATE>=DJSJ+OVERRUN/24 and (TXR='"
						+ user.getAccount()+ "' ";
			}else{
				sql = "select XH , DESR , OVERRUN , ZT , DJSJ , TXR , TXROLE , TXDW , GDR , GDSJ , YWLX , SJBH , LINKURL,'提醒' as lx,'tx' as RWLX from ALERT_INFO where ZT = '0' and SYSDATE>=DJSJ+OVERRUN/24 and (TXR='"
						+ user.getAccount()+ "' ";
			}
			
			//根据角色
			Role[] role = user.getRoles();
			if (role != null) {
				sql += " or ";
				String roles = ""; 
				for (int i = 0; i < role.length; i++){
					if(0 == i){
						roles += "'"+role[i].getName()+"'";
						continue;
					}
					roles += ",'"+role[i].getName()+"'";
				}
				roles = "(" + roles + ")";
				sql += " (ywlx in (select ALERT_ID from SYS_ALERT_ROLE_MAP where ROLE_NAME in "+roles+")";
				sql += "  and TXDW like '"+user.getOrgDept().getSsxq()+"%')";
			}
			
			sql += ")";
			sql += " order by djsj desc";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getDocument();
			Pub.writeXmlDocument(response, domresult, "UTF-8");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			response.getOutputStream().print("");
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
	
	
	public ActionForward QueryAlertInfoByPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		
		Document doc = RequestUtil.getDocument(request);
		PageManager page = RequestUtil.getPageManager(doc);
		if (page == null) {
			page = new PageManager();
			page.setPageRows(1000);
		}
		
		Document domresult = null;
		String mode = request.getParameter("mode");
		try {
			if (user == null)
				throw new Exception("");
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "";
			//根据提醒对应的提醒人进行查询
			if (mode.equals("1")){//只查询未处理的
				sql = "select XH , DESR , OVERRUN , ZT , DJSJ , TXR , TXROLE , TXDW , GDR , GDSJ , YWLX , SJBH , LINKURL,'提醒' as lx,'tx' as RWLX from ALERT_INFO where ZT='2' and SYSDATE>=DJSJ+OVERRUN/24 and (TXR='"
						+ user.getAccount() + "' ";
			}else if(mode.equals("2")){//查询未处理和已知晓的
				sql = "select XH , DESR , OVERRUN , ZT , DJSJ , TXR , TXROLE , TXDW , GDR , GDSJ , YWLX , SJBH , LINKURL,'提醒' as lx,'tx' as RWLX from ALERT_INFO where ZT<>'0' and SYSDATE>=DJSJ+OVERRUN/24 and (TXR='"
						+ user.getAccount()+ "' ";
			}else{
				sql = "select XH , DESR , OVERRUN , ZT , DJSJ , TXR , TXROLE , TXDW , GDR , GDSJ , YWLX , SJBH , LINKURL,'提醒' as lx,'tx' as RWLX from ALERT_INFO where ZT = '0' and SYSDATE>=DJSJ+OVERRUN/24 and (TXR='"
						+ user.getAccount()+ "' ";
			}
			
			//根据角色
			Role[] role = user.getRoles();
			if (role != null) {
				sql += " or ";
				String roles = ""; 
				for (int i = 0; i < role.length; i++){
					if(0 == i){
						roles += "'"+role[i].getName()+"'";
						continue;
					}
					roles += ",'"+role[i].getName()+"'";
				}
				roles = "(" + roles + ")";
				sql += " (ywlx in (select ALERT_ID from SYS_ALERT_ROLE_MAP where ROLE_NAME in "+roles+")";
				sql += "  and TXDW like '"+user.getOrgDept().getSsxq()+"%')";
			}
			
			sql += ")";
			sql += " order by djsj desc";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getDocument();
			Pub.writeXmlDocument(response, domresult, "UTF-8");
			
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			response.getOutputStream().print("");
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
	

	public ActionForward SetAlertKnown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String xh = request.getParameter("xh");
		try {
			conn.setAutoCommit(false);
			AlertManager.AlertKnown(conn, xh);
			conn.commit();
			Pub.writeXmlInfoMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}

		return null;
	}

	public ActionForward SetMsgKnown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		// Document doc = RequestUtil.getDocument(request);
		String opid = request.getParameter("xh");
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);

			msgmgrVO vo = new msgmgrVO();
			vo.setOPID(opid);
			vo.setDELOPER(user.getAccount());
			vo.setDELTIME(Pub.getCurrentDate());
			vo.setSTATE("2");
			BaseDAO.update(conn, vo);

			conn.commit();
			Pub.writeXmlInfoMessage(response, "删除成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	public ActionForward SetDbKnown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		// Document doc = RequestUtil.getDocument(request);
		String opid = request.getParameter("xh");
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			String dbdw = dept.getDeptID();
			switch (dept.getDeptLevel()) {
			case 1:
				dbdw = dept.getDeptID().substring(0, 2) + "0000000000";
				break;
			case 2:
				dbdw = dept.getDeptID().substring(0, 4) + "00000000";
				break;
			case 3:
				dbdw = dept.getDeptID().substring(0, 6) + "000000";
				break;
			default:
			case 4:
				dbdw = dept.getDeptID();
				break;
			}
			String updateSql = " update ap_task_schedule set isknow='1' where seq=? and RWZT = '01' and cjsj > sysdate - 4 ";
			updateSql += "and (DBDWDM='" + dbdw + "' or DBDWDM='"
					+ dept.getDeptID() + "')";
			updateSql += " and (DBRYID = '" + user.getAccount()
					+ "' or DBRYID is null)";
			updateSql += " and (DBROLE in (" + user.getRoleListString().trim()
					+ ") or DBROLE is null)";
			Object[] objs = new Object[1];
			objs[0] = opid;
			DBUtil.executeUpdate(conn, updateSql, objs);
			conn.commit();
			Pub.writeXmlInfoMessage(response, "删除成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	public ActionForward SetAlertFinish(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);

		Connection conn = DBUtil.getConnection();
		String xh = request.getParameter("xh");
		//增加提醒类别,对于待办提醒,点击完成,直接更新任务状态标识
		String txlb = request.getParameter("txlb");
		if (txlb == null || "".equals(txlb))
			txlb = "1";
		String rwlx = request.getParameter("rwlx");
		if (rwlx == null || "".equals(rwlx))
			throw new Exception("提醒无任务类型!");
		try {
			List list = doc.selectNodes("/DATAINFO/ROW");
			Element row = (Element) list.get(0);
			AlertInfoVO vo = new AlertInfoVO();
			conn.setAutoCommit(false);
			vo.setValue(row);
			if ("1".equals(txlb)) // 提醒
			{
				AlertManager.AlertFinish(conn, user, xh);
			} else if ("2".equals(txlb)) { // 待办
				/**
				 * TaskMgrBean bo = new TaskMgrBean(); EventVO event =
				 * EventManager.getEventByID(conn, vo.getSjbh());
				 * if("3".equals(rwlx)) bo.doGeneralTask(conn, vo.getSjbh(),
				 * vo.getYwlx(), user); else if("6".equals(rwlx))
				 * bo.doRollBackTask(conn, vo.getSjbh(), vo.getYwlx(), user);
				 **/
			}
			// end
			conn.commit();
			Pub.writeXmlInfoMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}

		return null;
	}

	// 查询工作提示
	public ActionForward QueryPrompt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		String ywlx = request.getParameter("ywlx");
		Document domresult = null;// add by hongf 2009-8-12 通过Document向前台传值
		Connection conn = DBUtil.getConnection();

		try {
			// 由于将desr字段由varchar改为blob
			// 修改了后台查询及向前台传值方法
			/*
			 * String prompt=""; String sqlQuery
			 * =" select DESR from ALERT_MESSAGE where ywlx= '"
			 * +ywlx+"' and zhux='0'"; QuerySet qs =
			 * DBUtil.executeQuery(sqlQuery,null,conn); if (qs.getRowCount() >
			 * 0) {
			 * 
			 * prompt = qs.getString(1, "DESR"); } String xmlStr =
			 * "<ROW><SUCCESS>"+prompt+"</SUCCESS></ROW>";
			 * Pub.writeMessage(response, xmlStr,"UTF-8");
			 */
			String sql = " select DESR from ALERT_MESSAGE where ywlx= '" + ywlx
					+ "' and zhux='0'";
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			bs.setFieldClob("DESR");
			domresult = bs.getDocument();
			Pub.writeXmlDocument(response, domresult, "UTF-8");
			// modified by hongf 2009-8-12 end
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;

	}

	// 备忘信息表查询 查询过去、现在或未来的提示信息
	public ActionForward QueryListNotes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		String date = Pub.val(request, "date");
		// 此处可以设置自定义的过滤条件
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		if (date.equals("today"))
			condition += " and to_char(txsj,'yyyymmdd') = to_char(sysdate,'yyyymmdd')";
		if (date.equals("tomorrow"))
			condition += " and to_char(txsj,'yyyymmdd') > to_char(sysdate,'yyyymmdd')";
		if (date.equals("yesterday"))
			condition += " and to_char(txsj,'yyyymmdd') < to_char(sysdate,'yyyymmdd')";
		condition += " and tbr= '"
				+ user.getAccount()
				+ "' and (zhux is null or zhux = '0') order by txsj desc ,tbsj desc";
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Document domresult = null;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "select XH , TITLE , CONTENT , TXSJ , SJBH , TBSJ , TBR , ZT ,YWLX from NOTES_INFO ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			// 设置时间的显示格式
			bs.setFieldDateFormat("TXSJ", "yyyy-MM-dd");
			bs.setFieldUserID("TBR");
			bs.setFieldDic("ZT", "BWZT");
			bs.setFieldClob("CONTENT");
			domresult = bs.getDocument();
			Pub.writeXmlDocument(response, domresult, "UTF-8");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 备忘信息表查询 更具条件查询
	public ActionForward QueryListNotes2(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		// 此处可以设置自定义的过滤条件
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		condition += " and tbr= '"
				+ user.getAccount()
				+ "' and (zhux is null or zhux = '0') order by txsj desc,tbsj desc ";
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Document domresult = null;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "select XH , TITLE , CONTENT , TXSJ , SJBH , TBSJ , TBR , ZT ,YWLX from NOTES_INFO ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			// 设置时间的显示格式
			bs.setFieldDateFormat("TXSJ", "yyyy-MM-dd");
			bs.setFieldUserID("TBR");
			bs.setFieldClob("CONTENT");
			domresult = bs.getDocument();
			Pub.writeXmlDocument(response, domresult, "UTF-8");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 备忘信息表查询 判断是否有新备忘
	public ActionForward QueryListNotes3(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String date = Pub.val(request, "date");
		try {
			conn.setAutoCommit(false);
			String sql = "select XH , TITLE , CONTENT , TXSJ , SJBH , TBSJ , TBR , ZT ,YWLX from NOTES_INFO ";
			sql += " where to_char(txsj,'yyyymmdd') = to_char(sysdate,'yyyymmdd')  and (zhux is null or zhux = '0') and zt=2 and tbr= '"
					+ user.getAccount() + "'";

			String array[][] = DBUtil.query(conn, sql);
			if (null != array) {
				Pub.writeMessage(response, "true", "UTF-8");
			} else {
				Pub.writeMessage(response, "false", "UTF-8");
			}
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 备忘信息表查询 首页显示的备忘信息
	public ActionForward QueryListShowNotes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document domresult = null;
		Connection conn = DBUtil.getConnection();
		String xh = Pub.val(request, "XH");
		try {
			conn.setAutoCommit(false);
			String sql = "select XH , TITLE , CONTENT , TXSJ , SJBH , TBSJ , TBR , ZT ,YWLX from NOTES_INFO ";
			sql += " where to_char(txsj,'yyyymmdd') = to_char(sysdate,'yyyymmdd')  and (zhux is null or zhux = '0') and zt=2 and tbr= '"
					+ user.getAccount() + "'";
			if (!xh.equals(""))
				sql += " and xh = " + xh;
			sql += " order by tbsj desc";
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			// 设置时间的显示格式
			bs.setFieldDateFormat("TXSJ", "yyyy-MM-dd");
			bs.setFieldUserID("TBR");
			bs.setFieldClob("CONTENT");
			domresult = bs.getDocument();
			Pub.writeXmlDocument(response, domresult, "UTF-8");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 备忘信息表插入
	public ActionForward InsertNotes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		Connection conn = DBUtil.getConnection();
		String ywlx = "040516"; // 业务类型
		try {
			conn.setAutoCommit(false);
			EventVO eventVO = EventManager.createEvent(conn, ywlx, user);// 生成事件
			List list = doc.selectNodes("/DATAINFO/ROW");

			Element row = (Element) list.get(0);
			NotesInfoVO vo = new NotesInfoVO();
			String sequence = com.ccthanking.common.SequenceUtil
					.getCommonSerivalNumber(conn);
			vo.setValue(row);
			String zt = "2";
			vo.setXh(sequence);
			vo.setSjbh(eventVO.getSjbh());
			vo.setTbr(user.getAccount()); // 填报用户
			vo.setTbsj(Pub.getCurrentDate()); // 填报时间
			vo.setZt(zt);// 提醒状态为未处理

			BaseDAO dao = new BaseDAO();
			dao.insert(conn, vo);
			EventManager.archiveEvent(conn, eventVO, user);
			conn.commit();
			Pub.writeXmlMessage(response, vo.getRowXml(), "操作成功！", "MESSAGE");
			LogManager.writeUserLog(eventVO.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					Pub.getDictValueByCode("YWLX", ywlx), user,"XH",vo.getXh());
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog("", ywlx, Globals.OPERATION_TYPE_INSERT,
					LogManager.RESULT_FAILURE,
					Pub.getDictValueByCode("YWLX", ywlx)+","+e.getMessage(), user,null,null);
			Pub.writeXmlErrorMessage(response, this.handleError(e));

		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 备忘信息表修改
	public ActionForward UpdateNotes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		Connection conn = DBUtil.getConnection();
		String ywlx = "040516"; // 业务类型
		NotesInfoVO vo = new NotesInfoVO();
		try {
			conn.setAutoCommit(false);
			EventVO eventVO = EventManager.createEvent(conn, ywlx, user);// 生成事件
			List list = doc.selectNodes("/DATAINFO/ROW");
			Element row = (Element) list.get(0);
			vo.setValue(row);
			BaseDAO dao = new BaseDAO();
			dao.update(conn, vo);
			EventManager.archiveEvent(conn, eventVO, user);
			conn.commit();
			LogManager.writeUserLog(eventVO.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					Pub.getDictValueByCode("YWLX", ywlx), user,"XH",vo.getXh());
			Pub.writeXmlMessage(response, vo.getRowXml(), "操作成功！", "MESSAGE");

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog("", ywlx, Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_FAILURE,
					Pub.getDictValueByCode("YWLX", ywlx)+","+e.getMessage(), user,"XH",vo.getXh());
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 将备忘信息状态置为已知道
	public ActionForward SetNotesKnown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		// Document doc = RequestUtil.getDocument(request);
		String xh = request.getParameter("xh");
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);

			NotesInfoVO vo = new NotesInfoVO();
			vo.setXh(xh);
			vo.setZt("1");
			BaseDAO.update(conn, vo);

			conn.commit();
			Pub.writeXmlInfoMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	// 备注信息删除（注销）
	public ActionForward DeleteNotes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		String xh = request.getParameter("xh");
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = " UPDATE NOTES_INFO SET ZHUX = 1  WHERE XH = " + xh;
			DBUtil.execSql(conn, sql);
			Pub.writeXmlInfoMessage(response, "删除成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
	
	/**
	 * 得到角色对应提醒树
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getAlertConfTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Document document = null;
		try {
			String role_name = Pub.val(request,"role_name");//角色名称
			document = DocumentFactory.getInstance().createDocument();
			Element root = document.addElement("tree");
			root.addAttribute("id", "0");
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String sql = "select id,name,url,(select count(*) from sys_alert_role_map where alert_id = id and ROLE_NAME = '"+role_name+"') checked from SYS_ALERT_CONF where sfyx = '1' order by orderno desc";
			String result[][] = DBUtil.query(sql);
			if(null != result && result.length > 0){
				for(int i=0;i<result.length;i++){
					Element alertDetail = root.addElement("item");
					alertDetail.addAttribute("text",result[i][1]).addAttribute("id",result[i][0]).addAttribute("open","1").addAttribute("call","1");
					if(new Integer(result[i][3]) > 0){
						alertDetail.addAttribute("checked",result[i][3]);
					}
				}
			}
			String xml =  document.asXML();
			Pub.writeMessage(response, xml);
			return null;
			
		} catch (Exception e) {
			throw e;
		} finally {
			document = null;
		}
	}
	
	
	/**
	 * 保存角色和提醒关联信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveRoleAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		try {
			String role_name = Pub.val(request,"role_name");//角色名称
			String alertIds = Pub.val(request,"alertIds");//角色名称
			String alertId[] = alertIds.split(",");
			String sql = "delete from SYS_ALERT_ROLE_MAP where role_name = ?";
			Object paras[] = new Object[1];
			paras[0] = role_name;
			DBUtil.executeUpdate(conn, sql, paras);
			paras = new Object[2];
			paras[0] = role_name;
			sql = "insert into SYS_ALERT_ROLE_MAP(role_name,alert_id) values(?,?)";
			for(int i=0;i<alertId.length;i++){
				if(Pub.empty(alertId[i])){
					continue;
				}
				paras[1] = alertId[i];
				DBUtil.executeUpdate(conn, sql, paras);
			}
			conn.commit();
			Pub.writeMessage(response, "操作成功");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeMessage(response, "操作失败");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

}
