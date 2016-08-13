package com.ccthanking.framework.coreapp.aplink;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/ProcessJumpAction")
public class ProcessJumpAction {

	/**
	 * 重启工作联系单
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "restart000002")
	@ResponseBody
	public String restart000002(HttpServletRequest request,requestJson json) {
		Connection conn = null;
		JSONObject jsonObj = new JSONObject();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sjbh = request.getParameter("sjbh");
/*			String ywlx = request.getParameter("ywlx");
			String processId = request.getParameter("processId");*/
			
	        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String date = Pub.getDate("yyyy-MM-dd HH:mm:ss");
			
			String processinfoSql = "select CJRID,CJDWDM from AP_PROCESSINFO t where eventid = '"+sjbh+"'";
			String[][] processinfo = DBUtil.query(conn, processinfoSql);
			String restartStepAccount="",restartDept="", restartStepAccountXm="";
			if (processinfo != null && processinfo.length != 0) {
				restartStepAccount = processinfo[0][0];
				restartStepAccountXm = Pub.getUserNameByLoginId(restartStepAccount);
				restartDept = processinfo[0][1];
			}
	        
			String taskScheduleSql = "insert into ap_task_schedule " 
					+ " cols  (id,sjbh,ywlx,spbh,spjg,rwzt,yxbs," 
					+ "rwlx,dbdwdm,dbryid,dbrrys,cjsj," 
					+ "cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl," 
					+ "seq,dbrole,cjrxm,cjdwdm) " 
		            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_VALID + "','1'," 
					+ "rwlx,'"+restartDept+"','"+restartStepAccount+"','',"
		            + " to_date('"+date+"','YYYY-MM-DD HH24:MI:SS') "+"," 
		            + "'"+user.getAccount()+"',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,'" 
		            + newseq + "','','" + user.getName() + "','" + user.getDepartment() + "' "
		            + "from ap_task_schedule where CJRID='"+restartStepAccount+"'and sjbh='"+sjbh+"' and rownum=1";
        	DBUtil.execUpdateSql(conn, taskScheduleSql);
        	
        	String updateProcessinfo = "UPDATE AP_PROCESSINFO SET CLOSETIME = null,RESULT=0 where eventid = '"+sjbh+"'";
        	DBUtil.execUpdateSql(conn, updateProcessinfo);

			String updateEventSql = "update fs_event set sjzt='1' where sjbh='"+sjbh+"'";
        	DBUtil.execUpdateSql(conn, updateEventSql);

        	conn.commit();
			jsonObj.put("msg", "操作成功！");
			jsonObj.put("cjrxm", restartStepAccountXm);
		} catch (Exception e) {
			jsonObj.put("msg", "操作失败！");
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonObj.toString();
	}

	/**
	 * 越级
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "getProcessDetail")
	@ResponseBody
	public String getProcessDetail(HttpServletRequest request,requestJson json) {
		Connection conn = null;
		JSONObject jsonObj = new JSONObject();
		try {
			String jumpText = "自动越级审批";
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String dbdw = user.getOrgDept().getDeptParentID();
			String date = Pub.getDate("yyyy-MM-dd HH:mm:");
			date += "10";
			
			String processId = request.getParameter("processId");
			String sjbh = request.getParameter("sjbh");
			String stepSeq = request.getParameter("stepSeq");
			String currentStepCode = request.getParameter("currentStepCode");
			String currentStepName = request.getParameter("currentStepName");
			
		//	System.out.println(processId + "|" + sjbh + "|" + ywlx + "|" + stepSeq);
			
			// 修改AP_PROCESSDETAIL表
			int currentStepSeq = Integer.parseInt(stepSeq);
			
			// 查询待办表【AP_TASK_SCHEDULE】中已经审批完成的信息。
			String queryApSch = "select stepsequence,seq,dbryid  from AP_TASK_SCHEDULE where sjbh='"+sjbh+"' and rwzt='"+TaskVO.TASK_STATUS_VALID+"'";
			String[][] ss = DBUtil.query(queryApSch);
			int exedMaxStepSeq = Integer.parseInt(ss == null ? "1" : ss[0][0]);
			int currentTaskSeq = Integer.parseInt(ss[0][1]);
			String dbryid = ss[0][2];
			
	//		System.out.println("currentStepSeq:"+currentStepSeq);
			
			// 修改待办信息
			String updateSql = "update ap_task_schedule set " 
					+ "rwzt='" + TaskVO.TASK_STATUS_EXECUTED + "'," 
					+ "wcsj=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
					+ "wcrid='"+user.getAccount()+"',yxbs='1'," 
					+ "result='1',resultdscr='"+jumpText+"'," 
					+ "wcrxm='"+user.getName()+"',wcdwdm='"+dbdw+"' " 
					+ "where seq='"+currentTaskSeq+"'";
        	DBUtil.execUpdateSql(conn, updateSql);
        	
			String updateApDetail = "update AP_PROCESSDETAIL set " 
					+ "actor='"+dbryid+"'," 
					+ "state='"+Process.AP_SUCCESS+"'," 
					+ "closetime=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
					+ "processtime=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
					// 此修改必须添加，否则AP_PROCESSDETAIL和ap_task_schedule关联不上
					+ "taskseq='"+currentTaskSeq+"'," 
					+ "result='1',resultdscr='"+jumpText+"' " 
					+ "where processoid='"+processId+"' and stepsequence='"+exedMaxStepSeq+"'";
        	DBUtil.execUpdateSql(conn, updateApDetail);
			
        	
	//		System.out.println(updateApDetail);
	//		System.out.println(updateSql);
			// 添加越级的信息记录
			String[][] dbrArray = getProcessStepActor(conn, processId, exedMaxStepSeq, currentStepSeq);
			int dbrIndex = 0;
			for (int stepseq = exedMaxStepSeq+1; stepseq < currentStepSeq; stepseq++) {
				// 此处这么做是，可以按时间正常排序
				date = Pub.getDate("yyyy-MM-dd HH:mm:");
				date += (20+stepseq);
				
		        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);
		        dbryid = dbrArray[dbrIndex][0];
		        dbryid = "未找到人员，请联系管理员".equals(dbryid) ? user.getAccount() : dbryid;
		        dbdw = Pub.getUserDepartmentById(dbryid);
	//			System.out.println(stepseq + "=========" + newseq);
				String sql = "insert into ap_task_schedule " 
						+ " cols  (id,sjbh,ywlx,spbh,spjg,rwzt,yxbs," 
						+ "rwlx,dbdwdm,dbryid,dbrrys,cjsj,wcsj,wcdwdm," 
						+ "cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl," 
						+ "seq,dbrole,cjrxm,cjdwdm,wcrid," 
						+ "result,spr,wcrxm,stepsequence,resultdscr) " 
			            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_EXECUTED + "','1'," 
						+ "rwlx,'"+dbdw+"','"+dbryid+"','',"
			            + " to_date('"+date+"','YYYY-MM-DD HH24:MI:SS') "+"," 
			            + " to_date('"+date+"','YYYY-MM-DD HH24:MI:SS') "+",'"+dbdw+"'," 
			            + "'"+user.getAccount()+"',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,'" 
			            + newseq + "','','" + user.getName() + "','" + user.getDepartment() + "','"+user.getAccount()+"', "
			            + "'1','"+user.getAccount()+"','" + user.getName() + "','"+stepseq+"','"+jumpText+"' "
			            + "from ap_task_schedule where seq='" + currentTaskSeq + "'";
	//			System.out.println("sql|||||||:"+sql);
	        	DBUtil.execUpdateSql(conn, sql);

				updateApDetail = "update AP_PROCESSDETAIL set " 
						+ "actor='"+dbryid+"'," 
						+ "state='"+Process.AP_SUCCESS+"'," 
						+ "closetime=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
						+ "processtime=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
						// 此修改必须添加，否则AP_PROCESSDETAIL和ap_task_schedule关联不上
						+ "taskseq='"+newseq+"'," 
						+ "result='1',resultdscr='"+jumpText+"' " 
						+ "where processoid='"+processId+"' and stepsequence='"+stepseq+"'";
	//			System.out.println("updateApDetail--------:"+updateApDetail);
	        	DBUtil.execUpdateSql(conn, updateApDetail);
	        	
	        	dbrIndex++;
			}


			String temp_updateApDetail = "update AP_PROCESSDETAIL set " 
					+ "actor=null," 
					+ "state='"+Process.AP_CREATED+"'," 
					+ "closetime=null," 
					+ "processtime=null," 
					+ "taskseq=null," 
					+ "result=null,resultdscr=null " 
					+ "where processoid='"+processId+"' and stepsequence>"+currentStepSeq;
        	DBUtil.execUpdateSql(conn, temp_updateApDetail);
        	
			String current_updateApDetail = "update AP_PROCESSDETAIL set " 
					+ "actor='"+currentStepCode+"'," 
					+ "state='"+Process.AP_CREATED+"'," 
					+ "closetime=null," 
					+ "processtime=null," 
					+ "taskseq=null," 
					+ "result=null,resultdscr=null " 
					+ "where processoid='"+processId+"' and stepsequence="+currentStepSeq;
        	DBUtil.execUpdateSql(conn, current_updateApDetail);
			

		//	dbryid = getProcessStepActor(conn, processId, String.valueOf(currentStepSeq));
		//	String blrxm = Pub.getUserNameByLoginId(dbryid);
			System.out.println("currentStepName---------:"+currentStepName);
			System.out.println("currentStepCode---------:"+currentStepCode);
			dbdw = Pub.getUserDepartmentById(currentStepCode);
			date = Pub.getDate("yyyy-MM-dd HH:mm:")+"39";
	        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);
	        // 添加待办信息
			String taskScheduleSql = "insert into ap_task_schedule " 
					+ " cols  (id,sjbh,ywlx,spbh,spjg,rwzt,yxbs," 
					+ "rwlx,dbdwdm,dbryid,dbrrys,cjsj," 
					+ "cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl," 
					+ "seq,dbrole,cjrxm,cjdwdm,stepsequence) " 
		            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_VALID + "','1'," 
					+ "rwlx,'"+dbdw+"','"+currentStepCode+"','',"
		            + " to_date('"+date+"','YYYY-MM-DD HH24:MI:SS') "+"," 
		            + "'"+user.getAccount()+"',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,'" 
		            + newseq + "','','" + user.getName() + "','" + user.getDepartment() + "', '"+currentStepSeq+"' "
		            + "from ap_task_schedule where seq='" + currentTaskSeq + "'";
	//		System.out.println(taskScheduleSql);
        	DBUtil.execUpdateSql(conn, taskScheduleSql);
        	
        	conn.commit();

			jsonObj.put("msg", "操作成功！");
			jsonObj.put("blrxm", currentStepName);
		} catch (Exception e) {
	        DBUtil.rollbackConnetion(conn);
			jsonObj.put("msg", "操作失败！");
			e.printStackTrace();
			return 	jsonObj.toString();
		} finally {
	        DBUtil.closeConnetion(conn);
		}
		return 	jsonObj.toString();
	}
	
	/**
	 * 获取当前节点应该待办的人员
	 * @param conn
	 * @param processId
	 * @param stepsequence
	 * @return
	 */
	private String getProcessStepActor(Connection conn, String processId, String stepsequence) {
		String actor = "";
		try {
			String sql = "select distinct " +
					"(case  " +
					// 角色定位
					"when ap.isms='2' then DECODE(actor,'',to_char((select op.account from fs_org_role_psn_map rpm,fs_org_person op where rpm.role_id=ap.rolename and rpm.person_account=op.account and rownum=1)),actor) " +  
				    // 自由报送
				    "when ap.isms='3' then decode(actor,'',to_char('手动选择人员') ,actor) " +
				    // 返回发起人
				    "when ap.isms='4' then DECODE(actor,'',to_char((select op.account from ap_processinfo info,fs_org_person op where info.processoid=ap.processoid and info.cjrid=op.account)),actor)   " +
				    //  本部门负责人
				    "when ap.isms='5' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fzr and od.row_id=ap.deptid)),actor)  " +
				    // 部门分管副主任
				    "when ap.isms='6' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fgzr and od.row_id=ap.deptid)),actor)  " +
				    // 部门负责人
				    "when ap.isms='7' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fzr and od.row_id=ap.deptid)),actor)  " +
				    // 发起部门负责人
				    "when ap.isms='8' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op,ap_processinfo info where info.cjdwdm=od.row_id and op.account=od.fzr and info.processoid=ap.processoid)),actor) " +  
				    // 发起部门分管主任
				    "when ap.isms='9' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op,ap_processinfo info where info.cjdwdm=od.row_id and op.account=od.fgzr and info.processoid=ap.processoid)),actor)  " +
					"else to_char('未找到人员，请联系管理员') end) pname1, rolename, actor " +
					"from ap_processdetail ap ,fs_org_role_psn_map rp " +
					"where processoid = '"+processId+"' and stepsequence='"+stepsequence+"' and ap.rolename=rp.role_id(+) order by ap.stepsequence";
			
			String[][] s = DBUtil.query(conn, sql);
			
			// 获取节点人员
			actor = s == null ? "" : s[0][0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actor;
	}
	

	/**
	 * 获取当前节点应该待办的人员
	 * @param conn
	 * @param processId
	 * @param stepsequence
	 * @return
	 */
	private String[][] getProcessStepActor(Connection conn, String processId, int start_stepsequence, int end_stepsequence) {
		String[][] s = null;
		try {
			String sql = "select distinct " +
					"(case  " +
					// 角色定位
					"when ap.isms='2' then DECODE(actor,'',to_char((select op.account from fs_org_role_psn_map rpm,fs_org_person op where rpm.role_id=ap.rolename and rpm.person_account=op.account and rownum=1)),actor) " +  
				    // 自由报送
				    "when ap.isms='3' then decode(actor,'',to_char('手动选择人员') ,actor) " +
				    // 返回发起人
				    "when ap.isms='4' then DECODE(actor,'',to_char((select op.account from ap_processinfo info,fs_org_person op where info.processoid=ap.processoid and info.cjrid=op.account)),actor)   " +
				    //  本部门负责人
				    "when ap.isms='5' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fzr and od.row_id=ap.deptid)),actor)  " +
				    // 部门分管副主任
				    "when ap.isms='6' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fgzr and od.row_id=ap.deptid)),actor)  " +
				    // 部门负责人
				    "when ap.isms='7' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op where op.account=od.fzr and od.row_id=ap.deptid)),actor)  " +
				    // 发起部门负责人
				    "when ap.isms='8' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op,ap_processinfo info where info.cjdwdm=od.row_id and op.account=od.fzr and info.processoid=ap.processoid)),actor) " +  
				    // 发起部门分管主任
				    "when ap.isms='9' then DECODE(actor,'',to_char((select op.account from fs_org_dept od,fs_org_person op,ap_processinfo info where info.cjdwdm=od.row_id and op.account=od.fgzr and info.processoid=ap.processoid)),actor)  " +
					"else to_char('未找到人员，请联系管理员') end) pname1, ap.stepsequence, actor " +
					"from ap_processdetail ap ,fs_org_role_psn_map rp " +
					"where processoid = '"+processId+"' and " + 
					"ap.stepsequence >"+start_stepsequence+" and ap.stepsequence <"+end_stepsequence+" and " + 
					"ap.rolename=rp.role_id(+) order by ap.stepsequence";
			
			 s = DBUtil.query(conn, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 重启的处理
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "getRestartProcessDetail")
	@ResponseBody
	public String getRestartProcessDetail(HttpServletRequest request,requestJson json) {
		Connection conn = null;
		JSONObject jsonObj = new JSONObject();
		try {
			conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String processId = request.getParameter("processId");
			String sjbh = request.getParameter("sjbh");
			String restartStepTaskseq = request.getParameter("restartStepTaskseq");
			String stepsequence = request.getParameter("stepsequence");
			String restartStepAccount = request.getParameter("restartStepAccount");
			String blrxm = Pub.getUserNameByLoginId(restartStepAccount);
			String restartDept = Pub.getUserDepartmentById(restartStepAccount);
	//		System.out.println(processId + "|" + sjbh + "|" + restartStepAccount + " | blrxm:" + blrxm);
			
			// 更改流程状态。字典表中dic_code=LCZT
			String updateApInfoSql = "update ap_processinfo set result='0',CLOSETIME=null where processoid='"+processId+"'";
	//		System.out.println("----------------:"+updateApInfoSql);
        	DBUtil.execUpdateSql(conn, updateApInfoSql);
			
			// 更改事件状态。字典表中dic_code=SJZT
			String updateEventSql = "update fs_event set sjzt='1' where sjbh='"+sjbh+"'";
	//		System.out.println("----------------:"+updateEventSql);
        	DBUtil.execUpdateSql(conn, updateEventSql);

	        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);
			String date = Pub.getDate("yyyy-MM-dd HH:mm:ss");
			
			String querySeqSql = "select seq from ap_task_schedule where sjbh='"+sjbh+"' and STEPSEQUENCE=1 and rownum=1";
			String[][] seqArray = DBUtil.query(conn, querySeqSql);
			String seq = seqArray == null ? "" : seqArray[0][0];
			String taskScheduleSql= "";
			String updateApDetail = "";
			if("0".equals(restartStepTaskseq)) {// 发起人
		        // 添加待办信息
				taskScheduleSql = "insert into ap_task_schedule " 
						+ " cols  (id,sjbh,ywlx,spbh,spjg,rwzt,yxbs," 
						+ "rwlx,dbdwdm,dbryid,dbrrys,cjsj," 
						+ "cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl," 
						+ "seq,dbrole,cjrxm,cjdwdm,stepsequence) " 
			            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_VALID + "','1'," 
						+ "rwlx,'"+restartDept+"','"+restartStepAccount+"','',"
			            + " to_date('"+date+"','YYYY-MM-DD HH24:MI:SS') "+"," 
			            + "'"+user.getAccount()+"',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,'" 
			            + newseq + "','','" + user.getName() + "','" + user.getDepartment() + "','1' "
			            + "from ap_task_schedule where seq='"+seq+"'";
				
				// 修改detailed表中的状态。
				updateApDetail = "update AP_PROCESSDETAIL set " 
						+ "actor=null," 
						+ "state='"+Process.AP_CREATED+"'," 
						+ "closetime=null," 
						+ "processtime=null," 
						// 此修改必须添加，否则AP_PROCESSDETAIL和ap_task_schedule关联不上
						+ "taskseq=null," 
						+ "result=null,resultdscr=null " 
						+ "where processoid='"+processId+"'";
			} else {
		        // 添加待办信息
				taskScheduleSql = "insert into ap_task_schedule " 
					+ " cols  (id,sjbh,ywlx,spbh,spjg,rwzt,yxbs," 
					+ "rwlx,dbdwdm,dbryid,dbrrys,cjsj," 
					+ "cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl," 
					+ "seq,dbrole,cjrxm,cjdwdm,stepsequence) " 
		            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_VALID + "','1'," 
					+ "rwlx,'"+restartDept+"','"+restartStepAccount+"','',"
		            + " to_date('"+date+"','YYYY-MM-DD HH24:MI:SS') "+"," 
		            + "'"+user.getAccount()+"',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,'" 
		            + newseq + "','','" + user.getName() + "','" + user.getDepartment() + "','"+stepsequence+"' "
		            + "from ap_task_schedule where seq='"+restartStepTaskseq+"'"; //

				// 修改detailed表中的状态。
				updateApDetail = "update AP_PROCESSDETAIL set " 
						+ "actor=null," 
						+ "state='"+Process.AP_CREATED+"'," 
						+ "closetime=null," 
						+ "processtime=null," 
						// 此修改必须添加，否则AP_PROCESSDETAIL和ap_task_schedule关联不上
						+ "taskseq=null," 
						+ "result=null,resultdscr=null " 
						+ "where processoid='"+processId+"' and stepsequence>="+stepsequence;
			}
	//		System.out.println("----------------:"+taskScheduleSql);
	//		System.out.println("----------------:"+updateApDetail);
        	DBUtil.execUpdateSql(conn, taskScheduleSql);
        	DBUtil.execUpdateSql(conn, updateApDetail);
			
			
			conn.commit();

			jsonObj.put("msg", "操作成功！");
			jsonObj.put("blrxm", blrxm);
		} catch (Exception e) {
	        DBUtil.rollbackConnetion(conn);
			jsonObj.put("msg", "操作失败！");
			e.printStackTrace();
			return 	jsonObj.toString();
		} finally {
	        DBUtil.closeConnetion(conn);
		}
		return 	jsonObj.toString();
	}
	
	/**
	 * 退回的处理
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "backTaskProcessDetail")
	@ResponseBody
	public String backTaskProcessDetail(HttpServletRequest request,requestJson json) {
		Connection conn = null;
		JSONObject jsonObj = new JSONObject();
		try {
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String dbdw = user.getOrgDept().getDeptParentID();
			String date = Pub.getDate("yyyy-MM-dd HH:mm:ss");
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			String backText = "管理员退回处理";

			String processId = request.getParameter("processId");
			String sjbh = request.getParameter("sjbh");
			String currentStepSeq = request.getParameter("stepSeq");
	        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);

			// 查询待办表【AP_TASK_SCHEDULE】中已经审批完成的信息。
			String queryApSch = "select stepsequence,seq,dbryid from AP_TASK_SCHEDULE where sjbh='"+sjbh+"' and stepsequence='"+currentStepSeq+"' and rownum=1";
			String[][] ss = DBUtil.query(queryApSch);
	//		int exedMaxStepSeq = Integer.parseInt(ss[0][0] == null ? "1" : ss[0][0]);
			int currentTaskSeq = Integer.parseInt(ss[0][1]);
			String dbryid = ss[0][2];
			String blrxm = Pub.getUserNameByLoginId(dbryid);
			System.out.println("blrxm:"+blrxm);
			System.out.println("dbryid:"+dbryid);
			
			// 修改待办信息为已办理
			String updateSql = "update ap_task_schedule set " 
					+ "rwzt='" + TaskVO.TASK_STATUS_EXECUTED + "'," 
					+ "wcsj=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
					+ "wcrid='"+user.getAccount()+"',yxbs='1'," 
					+ "result='3',resultdscr='"+backText+"'," 
					+ "wcrxm='"+user.getName()+"',wcdwdm='"+dbdw+"' " 
					+ "where sjbh='"+sjbh+"' and rwzt='"+TaskVO.TASK_STATUS_VALID+"'";
	//		System.out.println(updateSql);
			DBUtil.execUpdateSql(conn, updateSql);

			dbdw = Pub.getUserDepartmentById(dbryid);
        	// 添加待办信息
			String taskScheduleSql = "insert into ap_task_schedule " 
				+ " cols  (id,sjbh,ywlx,spbh,spjg,rwzt,yxbs," 
				+ "rwlx,dbdwdm,dbryid,dbrrys,cjsj," 
				+ "cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl," 
				+ "seq,dbrole,cjrxm,cjdwdm,stepsequence) " 
	            + " select id,sjbh,ywlx,spbh,'','" + TaskVO.TASK_STATUS_VALID + "','1'," 
				+ "rwlx,'"+dbdw+"','"+dbryid+"','',"
	            + " to_date('"+date+"','YYYY-MM-DD HH24:MI:SS') "+"," 
	            + "'"+user.getAccount()+"',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl,'" 
	            + newseq + "','','" + user.getName() + "','" + user.getOrgDept().getDeptParentID() + "','"+currentStepSeq+"' "
	            + "from ap_task_schedule where seq='"+currentTaskSeq+"'";
	//		System.out.println(taskScheduleSql);
			DBUtil.execUpdateSql(conn, taskScheduleSql);

			// 修改detailed表中的状态。
			String updateApDetail = "update AP_PROCESSDETAIL set " 
			//		+ "actor=null," 
					+ "state='"+Process.AP_CREATED+"'," 
					+ "closetime=null," 
					+ "processtime=null," 
					+ "taskseq=null," 
					+ "result=null,resultdscr=null " 
					+ "where processoid='"+processId+"' and stepsequence>="+currentStepSeq;
		//	System.out.println(updateApDetail);
			DBUtil.execUpdateSql(conn, updateApDetail);

			conn.commit();
			jsonObj.put("msg", "操作成功！");
			jsonObj.put("blrxm", blrxm);
		} catch (Exception e) {
	        DBUtil.rollbackConnetion(conn);
			jsonObj.put("msg", "操作失败！");
			e.printStackTrace();
		} finally {
	        DBUtil.closeConnetion(conn);
		}
		return 	jsonObj.toString();
	}
	
	/**
	 * 退回給發起人
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "backToFqrProcessDetail")
	@ResponseBody
	public String backToFqrProcessDetail(HttpServletRequest request,requestJson json) {
		Connection conn = null;
		JSONObject jsonObj = new JSONObject();
		try {
			conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String processId = request.getParameter("processId");
			String sjbh = request.getParameter("sjbh");
			String ywlx = request.getParameter("ywlx");
			String fqr = request.getParameter("fqr");
			String blrxm = Pub.getUserNameByLoginId(fqr);
			System.out.println("fqr:"+fqr);
			System.out.println("blrxm:"+blrxm);
			String restartDept = Pub.getUserDepartmentById(fqr);

	        String newseq = DBUtil.getSequenceValue("AP_TASK_S", conn);
			String date = Pub.getDate("yyyy-MM-dd HH:mm:ss");
			
			String querySeqSql = "select seq,id,memo,dbryid,stepsequence from ap_task_schedule where sjbh='"+sjbh+"' and WCSJ is null";
			String[][] seqArray = DBUtil.query(conn, querySeqSql);
			String seq = seqArray == null ? "" : seqArray[0][0];
			String id = seqArray == null ? "" : seqArray[0][1];
			String memo = seqArray == null ? "" : seqArray[0][2];
			String dbryid = seqArray == null ? "" : seqArray[0][3];
			String stepsequence = seqArray == null ? "" : seqArray[0][4];
			
			// 修改待办信息
			String updateScheduleSql = "update ap_task_schedule set " 
					+ "rwzt='" + TaskVO.TASK_STATUS_EXECUTED + "'," 
					+ "wcsj=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
					+ "wcrid='"+user.getAccount()+"',yxbs='1'," 
					+ "result='3',resultdscr='管理员退回处理'," 
					+ "wcrxm='"+user.getName()+"',wcdwdm='"+user.getDepartment()+"' " 
					+ "where seq='"+seq+"'";
			
		    // 添加待办信息
			String taskScheduleSql = "insert into ap_task_schedule " 
					+ " cols  (id,sjbh,ywlx,spbh,spjg,rwzt,yxbs," 
					+ "rwlx,dbdwdm,dbryid,dbrrys,cjsj," 
					+ "cjrid,rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,memo,linkurl," 
					+ "seq,dbrole,cjrxm,cjdwdm) " 
		            + " select '"+id+"','"+sjbh+"','"+ywlx+"','"+processId+"','3','" + TaskVO.TASK_STATUS_VALID + "','1'," 
					+ "rwlx,'"+restartDept+"','"+fqr+"','',"
		            + " to_date('"+date+"','YYYY-MM-DD HH24:MI:SS') "+"," 
		            + "'"+user.getAccount()+"',rysbh,xh,hh,ssxq,pcsdm,fjdm,sjdm,'"+memo+"',linkurl,'" 
		            + newseq + "','','" + user.getName() + "','" + user.getDepartment() + "' "
		            + "from ap_task_schedule where stepsequence is null and result='4' and rownum=1";
			
			
				
			// 修改detailed表中的状态。
			String updateApDetail = "update AP_PROCESSDETAIL set " 
					+ "actor='"+dbryid+"'," 
					+ "state='11'," 
					+ "closetime=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
					+ "processtime=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS')," 
					+ "taskseq="+seq+"," 
					+ "result='3',resultdscr='管理员退回处理' " 
					+ "where processoid='"+processId+"' and stepoid='"+stepsequence+"'";
			
			DBUtil.execUpdateSql(conn, updateScheduleSql);
        	DBUtil.execUpdateSql(conn, taskScheduleSql);
        	DBUtil.execUpdateSql(conn, updateApDetail);
        	
        	
			// 这里修改业务
        	if("300101".equals(ywlx)) {
//				XQZT 修改提请款状态
//        	    1  未提交     新建
//        	    2  会签审批
//        	    3  已审批
//        	    5  招标中
//        	    6  已招标
        		String ywSql = "update gc_ztb_xq set xqzt='2' where sjbh = '"+sjbh+"' and ywlx = '"+ywlx+"'";
        		DBUtil.execUpdateSql(conn, ywSql);
        	} else if("700101".equals(ywlx)) {
//        	    HTRXZT 合同履行类型
//        	    -3  未审批 新建
//        	    -2  审批中 呈请审批
//        	    -1  已审批 审批信息   审批通过   查看审批信息
//        	    0   审核中 部门：部门提交审批    合同管理部： 签订审核
//        	    1   履行中 合同管理部：签订履行
//        	    2   已结算
//        	    3   已结束
//        	    4   已中止
        	    //退回发起人 合同状态状态为新建状态
        		String ywSql = "update GC_HTGL_HT set HTZT='-2' where sjbh = '"+sjbh+"' and ywlx = '"+ywlx+"'";
        		DBUtil.execUpdateSql(conn, ywSql);
        	} else if(YwlxManager.GC_ZJGL_TQK.equals(ywlx)) {
//				TQKZT 修改提请款状态
//        	    1  未提交     新建
//        	    4   待处理    部门领导通过
//        	    5   审批中    呈请审批
//        	    6   已审批    审批通过
//        	    7   已拔付
        	    //只有审批中的在删除审核流程时，才把改为待处理状态
        		String ywSql = "update gc_zjgl_tqk set tqkzt='1' where sjbh = '"+sjbh+"' and ywlx='"+ywlx+"'";
        	    DBUtil.execUpdateSql(conn, ywSql);
        	}
        	
        	DBUtil.execUpdateSql(conn, "update fs_event set sjzt='7' where sjbh='"+sjbh+"'");
        	
        	System.out.println(updateScheduleSql);
        	System.out.println(taskScheduleSql);
        	System.out.println(updateApDetail);
			conn.commit();

			jsonObj.put("msg", "操作成功！");
			jsonObj.put("blrxm", blrxm);
		} catch (Exception e) {
	        DBUtil.rollbackConnetion(conn);
			jsonObj.put("msg", "操作失败！");
			e.printStackTrace();
			return 	jsonObj.toString();
		} finally {
	        DBUtil.closeConnetion(conn);
		}
		return 	jsonObj.toString();
	}
}
