package com.ccthanking.framework.spflow;

import java.sql.Connection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.aplink.TaskVO;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.model.responseJson;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryCondition;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/*
 呈请action
 */

@Controller
@RequestMapping("/ProcessAction")
public class ProcessAction {
	// 查询审批流程

	
	 /*
	  * 查询我的审批流程，包括我发起的，我参与的
	  */
	
	@RequestMapping(params = "QueryMySplc")
	@ResponseBody
	public requestJson QueryMySplc(HttpServletRequest request, requestJson json)
			throws Exception

	{
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String sjson = json.getMsg();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  userid = user.getAccount();
		String zt  = request.getParameter("lczt");
		String dqblr = request.getParameter("dqblr");	//当前办理人
		if(Pub.empty(zt)) {
			zt = "all";
		}
		try {

			QueryConditionList list = RequestUtil.getConditionList(sjson);
			PageManager page = RequestUtil.getPageManager(sjson);
			String orderFilter = RequestUtil.getOrderFilter(sjson);
			String condition = list == null ? "" : list.getConditionWhere();
			if("all".equals(zt)) {
				//我处理的流程中，不再显示我发起的流程
				condition += " and (ap.processoid in(select processoid from ap_processdetail t,AP_PROCESSTYPE P where t.processtypeoid=p.processtypeoid and p.processtype!=4 and  t.actor ='"+userid+"' and t.state='11'))";
				orderFilter = orderFilter.replace("CREATETIME", "BLSJ");
			} else if("2".equals(zt)) {
				condition +=" and cjrid='"+userid+"'";
				//add by zhangbr@ccthanking.com 添加流程当前办理人查询条件
				if(!Pub.empty(dqblr)) {
					condition += " and ap.EVENTID in (select T.SJBH from AP_TASK_SCHEDULE T,FS_ORG_PERSON P where T.DBRYID=P.ACCOUNT and WCSJ is null and P.NAME like '%"+dqblr+"%') ";
				}
			} else {
				//我处理的流程中，不再显示我发起的流程。【并且完成的流程 state='11' add by xhb 2014-04-15】
				condition +=" and (ap.processoid in(select processoid from ap_processdetail t,AP_PROCESSTYPE P where t.processtypeoid=p.processtypeoid and p.processtype!=4 and t.actor ='"+userid+"' and t.state='11')) and RESULT='"+zt+"'";
				orderFilter = orderFilter.replace("CREATETIME", "BLSJ");
			}
			condition +=" and ap.eventid = t.sjbh(+) and ap.processoid = af.processoid";
			
			// 当状态为不为2时是表示【我处理的流程】
			String tempSql = ",af.closetime as sfsy";
			if(!"2".equals(zt)) {
				condition += getMycl(user, list, zt);
				// 【我处理的流程】，不查詢af.closetime這個字段
				tempSql = "";
			}
			
			condition += orderFilter;
			if (page == null)
				page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			
			String sql = "select ap.processoid, processtypeoid, createtime, ap.closetime, eventid, state, ap.operationoid, memo, taskoid, " + 
					"processevent, result, resultdscr, yxbs, value1, value2, value3, value4,cjrid,cjdwdm"+tempSql+",FUN_DQBLR(ap.EVENTID) DQBLR,af.closetime BLSJ " +
					"from ap_processinfo ap , ap_processconfig t,(select processoid, closetime from ap_processdetail where actor = '"+userid+"' and state = '11') af";

			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("VALUE3", "YWLX");
			bs.setFieldDic("RESULT", "LCZT");
			bs.setFieldOrgDept("cjdwdm");
			bs.setFieldUserID("cjrid");
			// 设置字典翻译定义
			// 设置时间的显示格式
			bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldDateFormat("CLOSETIME", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldDateFormat("BLSJ", "yyyy-MM-dd HH:mm:ss");
			domresult = bs.getJson();

		} catch (Exception e) {
			e.printStackTrace(System.out);

		} finally {
			if (conn != null)
				conn.close();
		}
		requestJson j = new requestJson();
		j.setMsg(domresult);
		return j;
	}
  /*
   * 查询所有的审批流程
   */
	@RequestMapping(params = "QueryAllSplc")
	@ResponseBody
	public requestJson QueryAllSplc(HttpServletRequest request, requestJson json)
			throws Exception

	{
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String sjson = json.getMsg();
		String dqblr = request.getParameter("dqblr");	//当前办理人
		try {

			QueryConditionList list = RequestUtil.getConditionList(sjson);
			PageManager page = RequestUtil.getPageManager(sjson);
			String orderFilter = RequestUtil.getOrderFilter(sjson);
			String sfcq_value="";
			int sfcq_index = -1;
			if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    QueryCondition condi = list.getQueryCondition(i);
                    if ("SFCQ".equalsIgnoreCase(condi.getField()) && StringUtils.isNotBlank(condi.getValue())) {
                    	sfcq_index = i;
                    	sfcq_value = condi.getValue();
                    }
                }
            }
			if(sfcq_index>0){
				list.deleteQueryCondition(sfcq_index);
			}
			String condition = list == null ? "" : list.getConditionWhere();
			String sfcq_where = "";
			if("1".equals(sfcq_value)){
				sfcq_where =" and ap.processoid in (select processoid from ap_processdetail  t where closetime-SHEDULE_TIME>0 or (closetime is null and sysdate -SHEDULE_TIME>0)) ";
			}else if ("0".equals(sfcq_value)){
				sfcq_where =" and ap.processoid not in (select processoid from ap_processdetail  t where closetime-SHEDULE_TIME>0 or (closetime is null and sysdate -SHEDULE_TIME>0)) ";
			}
			
			if("".equals(condition))
			{
				condition +=" ap.eventid = t.sjbh(+) and ap.cjrid=P.account(+) ";
			}else{
				condition +=" and ap.eventid = t.sjbh(+) and ap.cjrid=P.account(+) ";
			}
			condition +=sfcq_where;
			//add by zhangbr@ccthanking.com 添加流程当前办理人查询条件
			if(!Pub.empty(dqblr)){
				condition += " and ap.EVENTID in (select T.SJBH from AP_TASK_SCHEDULE T,FS_ORG_PERSON P where T.DBRYID=P.ACCOUNT and WCSJ is null and P.NAME like '%"+dqblr+"%') ";
			}
			condition += orderFilter;
			if (page == null)
				page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);

			String sql = "select processoid, processtypeoid, createtime, closetime, eventid sjbh, state, ap.operationoid, memo, taskoid, processevent, result, resultdscr, yxbs, value1, value2, value3, value4,cjrid,cjdwdm, WS_TEMPLATEID,FUN_DQBLR(ap.EVENTID) DQBLR,fun_isgd(ap.EVENTID) isgd from ap_processinfo ap , ap_processconfig t,FS_ORG_PERSON P";

			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("value3", "YWLX");
			bs.setFieldDic("RESULT", "LCZT");
			bs.setFieldSjbh("SJBH");
			bs.setFieldOrgDept("cjdwdm");
			bs.setFieldUserID("cjrid");
			// 设置字典翻译定义
			// 设置时间的显示格式
			bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldDateFormat("CLOSETIME", "yyyy-MM-dd HH:mm:ss");
			domresult = bs.getJson();
			if(!"0".equals(domresult)){
				String newjsonString = "";
				newjsonString  = "{\"response\":";
				newjsonString +="{\"data\":[";
				JSONObject jsono = JSONObject.fromObject(domresult);
				JSONObject response = (JSONObject) jsono.get("response");
				JSONArray data1 = (JSONArray) response.get("data");
				Iterator iter = data1.iterator();
				JSONObject data = null;
				int i = 0;
				while (iter.hasNext())
				{
					data = (JSONObject) iter.next();
					String processoid = data.getString("PROCESSOID");
					String processdetail = "select PROCESSOID,STEPSEQUENCE,NAME,CLOSETIME,SHEDULE_TIME,(closetime-SHEDULE_TIME)*24 as timex ,(sysdate -SHEDULE_TIME)*24 as timey from ap_processdetail where processoid='"+processoid+"'";
					QuerySet qs = DBUtil.executeQuery(processdetail, null,conn);
					String sfcq = "0";
					String sfcq_sv = "";
					if(qs.getRowCount()>0)
					{
						for(int k=0;k<qs.getRowCount();k++)
						{
							boolean cs = false;
							sfcq_sv +="流程第"+qs.getString(k+1, "STEPSEQUENCE")+"节点'"+qs.getString(k+1, "NAME");
							String closetime = qs.getString(k+1, "CLOSETIME");
							if(Pub.empty(closetime))
							{
								String timey = qs.getString(k+1, "TIMEY");
								if(!Pub.empty(timey)&&Float.valueOf(timey)>0){
									cs = true;
									sfcq_sv +="'超期未办理";
									sfcq = "1";
								}
							}else{
								String timex = qs.getString(k+1, "TIMEX");
								if(!Pub.empty(timex)&&Float.valueOf(timex)>0){
									cs = true;
									sfcq_sv +="'超期办理";
									sfcq = "1";
								}
							}
							if(cs == false){
								if(Pub.empty(closetime))
								{
									sfcq_sv += "'尚未办理";
								}else{
									sfcq_sv += "'正常办理";
								}
								
							}
							sfcq_sv +="\r\n";
						}
					}
					
					data.element("SFCQ", sfcq);
					data.element("SFCQ_SV", sfcq_sv);
					data1.remove(i);
					data1.add(i, data);
					i++;
				}
				domresult =jsono.toString();// BaseDAO.EncodeJsString( jsono.toString());
			}
			

		} catch (Exception e) {
			e.printStackTrace(System.out);

		} finally {
			if (conn != null)
				conn.close();
		}
		requestJson j = new requestJson();
		j.setMsg(domresult);
		return j;
	}
	
	@RequestMapping(params = "frameGgtz")
	@ResponseBody
	public responseJson frameGgtz(final HttpServletRequest request) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);

		Connection conn = DBUtil.getConnection();
		String json = "";
		String title = "";
		String memo = "";
		String linkurl = "";
		try {

			String condition = "";
	        condition += " RWZT='"+ TaskVO.TASK_STATUS_VALID + "' ";
	        condition += " and (DBRYID='" + user.getAccount() + "' or DBRYID is null) and  t.spbh = a.processoid ";
	        condition += " order by cjsj desc";
			String sql = "select t.ywlx,t.memo,t.linkurl,t.id ,t.seq,t.sjbh,t.spbh,t.rwlx from ap_task_schedule t, ap_processinfo a where ";
			String sql_count = "select count(1) from ap_task_schedule t, ap_processinfo a where ";
			String[][] lc = DBUtil.query("select * from ("+sql+condition+" ) where rownum<=5");
			String[][] mcount = DBUtil.query(sql_count+condition);

			if(lc!=null&&lc.length>0)
			{
				json +="{count:"+mcount[0][0]+",messages:[";
				for(int i = 0;i<lc.length;i++)
				{   
					String ywlx = lc[i][0];
					String ywlx_sv = "";
					if(!Pub.empty(ywlx))
					{
						ywlx_sv = Pub.getDictValueByCode("YWLX", ywlx);
					}
					//add by zhangbr@ccthanking.com 添加对特殊符号的处理
					title = BaseDAO.EncodeJsString(ywlx_sv);
					memo = BaseDAO.EncodeJsString(lc[i][1]);
					linkurl = BaseDAO.EncodeJsString(lc[i][2]);
					json +="{title:'"+title+"',content:'"+memo+"',linkurl:'"+linkurl+"',id:'"+lc[i][3]+"',seq:'"+lc[i][4]+"'" +
							",sjbh:'"+lc[i][5]+"',ywlx:'"+ywlx+"',spbh:'"+lc[i][6]+"',rwlx:'"+lc[i][7]+"'},";
				}
				json +="]}";
			}else{
				json = "{count:0}";
			}

			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		responseJson j = new responseJson();
		j.setMsg(json);
		j.setSuccess(true);
		return j;
	}
	
	@RequestMapping(params = "yjView")
	@ResponseBody
	public Object yjView(final HttpServletRequest request) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		JSONObject obj = null;
		try {
			conn = DBUtil.getConnection();
			JSONArray jsonArray = new JSONArray();
			
			String sjbh = request.getParameter("sjbh");
			String sql = "select t.spwsid, t.fieldname, t.domain_value, t.lrrid, t.lrrxm, to_char(t.lrsj,'yyyy-mm-dd hh24:mi:ss'), s.domain_comment,t.ws_template_id " 
					+ "from ap_process_ws t,ws_template_sql s " 
					+ "where t.sjbh = '"+ sjbh +"' and t.lrrid = '"+ user.getAccount() +"' " 
					+ "and t.wswh_flag = 5 and t.lrsj is not null " 
					+ "and t.ws_template_id = s.ws_template_id and t.fieldname = s.fieldname order by t.lrsj asc";
			
			String[][] rsList = DBUtil.query(conn, sql);
			
			if (rsList != null) {
				for (int i = 0; i < rsList.length; i++) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("SPWSID", rsList[i][0]);
					jsonObj.put("FIELDNAME", rsList[i][1]);
					jsonObj.put("DOMAIN_VALUE", rsList[i][2]);
					jsonObj.put("LRRID", rsList[i][3]);
					jsonObj.put("LRRXM", rsList[i][4]);
					jsonObj.put("LRSJ", rsList[i][5]);
					jsonObj.put("DOMAIN_COMMENT", rsList[i][6]);
					jsonObj.put("WS_TEMPLATE_ID", rsList[i][7]);
					jsonArray.add(jsonObj);
				}
			}

			obj = new JSONObject();
			obj.put("rsList", jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return obj;
	}
	
	@RequestMapping(params = "bcyj")
	@ResponseBody
	public String bcyj(final HttpServletRequest request) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		JSONObject obj = new JSONObject();
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray jsonArray = new JSONArray();
			
			String sjbh = request.getParameter("sjbh");
			String ywlx = request.getParameter("ywlx");
			String mind = request.getParameter("mind");
			String wsid = request.getParameter("wsid");
			String fieldname = request.getParameter("fieldname");

            String id = new RandomGUID().toString();
            
            String objs[] = {mind};
            String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM,SPYLB)"+
          		  	" values('"+id+"','5','"+wsid+"','',?,'','"+fieldname+"','','','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"','') ";
            DBUtil.executeUpdate(conn, insertSql, objs);
            conn.commit();

			obj.put("msg", "意见补充成功");
		} catch (Exception e) {
			obj.put("msg", "意见补充失败");
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return obj.toString();
	}

	private String getMycl(User user, QueryConditionList list, String zt) {
		
		String ztSql = "all".equals(zt) ? "" : " and i.RESULT='"+zt+"' ";
		String sql = " union select i.processoid, i.processtypeoid, "
				+ "i.createtime, i.closetime, i.eventid, "
				+ "i.state, i.operationoid, i.memo, i.taskoid, "
				+ "i.processevent, i.result,i.resultdscr, "
				+ "i.yxbs,i.value1,i.value2,i.value3,i.value4, "
				+ "i.cjrid,i.cjdwdm,"
				+ "FUN_DQBLR(i.eventid) DQBLR,s.wcsj BLSJ "
				+ "from ap_processinfo i, (select t.sjbh ,t.wcrid,max(t.wcsj) wcsj from ap_task_schedule t group by t.sjbh,t.wcrid having t.wcrid='" + user.getAccount() + "') s "
				+ "where i.eventid = s.sjbh "

				+ "and (i.operationoid = '2486' or i.operationoid = '2487' or i.operationoid='43489') "
				+ "and s.wcrid = '" + user.getAccount() + "' " + ztSql;

		
		for (int i = 0; i < list.size(); i++) {
			String field = list.getQueryCondition(i).getField();
			String value = list.getQueryCondition(i).getValue();
			if ("CJRID".equals(field)) {
				sql = sql + " and i.CJRID = '" + value + "'";
			} else if("MEMO".equals(field)) {
				sql = sql + " and i.MEMO like '" + value + "'";
			} else if("CJDWDM".equals(field)) {
				sql = sql + " and i.CJDWDM = '" + value + "'";
			} else if("VALUE3".equals(field)) {
				sql = sql + " and i.VALUE3 = '" + value + "'";
			}else if("to_char(CREATETIME,'yyyy')".equals(field)) {
				sql = sql + " and to_char(CREATETIME, 'yyyy')= '" + value + "'";
			}
		}
		return sql;
	}
	

	@RequestMapping(params = "mycllc")
	@ResponseBody
	public requestJson mycllc(HttpServletRequest request, requestJson json) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String sjson = json.getMsg();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  userid = user.getAccount();
		String dqblr = request.getParameter("dqblr");	//当前办理人
		try {

			QueryConditionList list = RequestUtil.getConditionList(sjson);
			PageManager page = RequestUtil.getPageManager(sjson);
			String orderFilter = RequestUtil.getOrderFilter(sjson);
			String condition = list == null ? "" : list.getConditionWhere();
			
			condition += " and cjrid = '"+userid+"' and ap.processoid = af.processoid ";

			if(!Pub.empty(dqblr)) {
				condition += " and ap.EVENTID in (select T.SJBH from AP_TASK_SCHEDULE T,FS_ORG_PERSON P " 
						+ "where T.DBRYID=P.ACCOUNT and WCSJ is null and P.NAME like '%"+dqblr+"%') ";
			}
			
			condition += orderFilter;
			if (page == null)
				page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			
			String sql = "select ap.processoid, processtypeoid, createtime, ap.closetime, eventid, state, " 
					+ "ap.operationoid, memo, taskoid, processevent, result, resultdscr, yxbs, value1, " 
					+ "value2, value3, value4, cjrid, cjdwdm, af.closetime as sfsy, FUN_DQBLR(ap.EVENTID) DQBLR " 
					+ "from AP_PROCESSINFO ap, (select processoid, closetime from ap_processdetail t1,AP_PROCESSTYPE P " 
					+ "where t1.processtypeoid=p.processtypeoid and  t1.stepsequence=1 ) af";

			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("VALUE3", "YWLX");
			bs.setFieldDic("RESULT", "LCZT");
			bs.setFieldOrgDept("cjdwdm");
			bs.setFieldUserID("cjrid");
			// 设置字典翻译定义
			// 设置时间的显示格式
			bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldDateFormat("CLOSETIME", "yyyy-MM-dd HH:mm:ss");
			domresult = bs.getJson();

		} catch (Exception e) {
			e.printStackTrace(System.out);

		} finally {
			if (conn != null)
				conn.close();
		}
		requestJson j = new requestJson();
		j.setMsg(domresult);
		return j;
	}
}