package com.ccthanking.business.bmjkcommon;

import java.sql.Connection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryCondition;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/bmjkCommonController")
public class BmjkCommonController {

	@Autowired
	private BmjkCommonServiceImpl bmjkCommonService;
	private static String theadPropertyFileName = "com.ccthanking.properties.business.bmjk.bmjk_gc";
	/**
	 * 项目信息表查询
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryXmxx")
	@ResponseBody
	public requestJson queryXmxx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String bmjkLx = request.getParameter("bmjkLx");
		String ywlx = request.getParameter("ywlx");
		String nd = request.getParameter("nd");
		String tableName = request.getParameter("tableName");
		String xmglgs = request.getParameter("xmglgs");
		try {
			String domResult = bmjkCommonService.queryXmxx(json.getMsg(), bmjkLx, ywlx, nd, tableName, xmglgs);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 项目-标段信息表查询
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryXmxxBdxx")
	@ResponseBody
	public requestJson queryXmxxBdxx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String bmjkLx = request.getParameter("bmjkLx");
		String ywlx = request.getParameter("ywlx");
		String nd = request.getParameter("nd");
		String tableName = request.getParameter("tableName");
		String xmglgs = request.getParameter("xmglgs");
		try {
			String domResult = bmjkCommonService.queryXmxxBdxx(json.getMsg(), bmjkLx, ywlx, nd, tableName, xmglgs);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			String ywlx = request.getParameter("ywlx");	//
			String bmjkLx = request.getParameter("bmjkLx");	//
			String nd = request.getParameter("nd");	//
			String deptid = request.getParameter("deptid");	//
			String dqblr = request.getParameter("dqblr");	//当前办理人
			String propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_bgs";
			try {
				
				String sql1 =Pub.getPropertiesSqlValue(propertiesName, bmjkLx);
				//拼接年度查询条件
				nd = Pub.empty(nd)?"":" and to_char(ap.CREATETIME,'yyyy')='"+nd+"'";
				deptid = Pub.empty(deptid)?"":" and  cjdwdm ='"+deptid+"'";
				sql1 = sql1.replaceAll("%ndCondition%", nd);
				sql1 = sql1.replaceAll("%deptCondition%", deptid);
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
				condition+=" and ap.eventid = t.sjbh(+) and ap.cjrid=P.account(+) ";
				//add by zhangbr@ccthanking.com 添加流程当前办理人查询条件
				if(!Pub.empty(dqblr)){
					condition += " and ap.EVENTID in (select T.SJBH from AP_TASK_SCHEDULE T,FS_ORG_PERSON P where T.DBRYID=P.ACCOUNT and WCSJ is null and P.NAME like '%"+dqblr+"%') ";
				}
					condition +=" and ap.PROCESSOID in ("+sql1+") ";
				condition += orderFilter;
				if (page == null)
					page = new PageManager();
				page.setFilter(condition);
				conn.setAutoCommit(false);

				String sql = "select processoid, processtypeoid, createtime, closetime, eventid, state, ap.operationoid, memo, taskoid, processevent, result, resultdscr, yxbs, value1, value2, value3, value4,cjrid,cjdwdm, WS_TEMPLATEID,FUN_DQBLR(ap.EVENTID) DQBLR from ap_processinfo ap , ap_processconfig t,FS_ORG_PERSON P";

				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("value3", "YWLX");
				bs.setFieldDic("RESULT", "LCZT");
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
		

}
