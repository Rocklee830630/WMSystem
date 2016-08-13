package com.ccthanking.framework.spflow;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.aplink.Process;
import com.ccthanking.framework.coreapp.aplink.ProcessMgr;
import com.ccthanking.framework.coreapp.aplink.Step;
import com.ccthanking.framework.coreapp.aplink.TaskMgrBean;
import com.ccthanking.framework.coreapp.aplink.TaskVO;
import com.ccthanking.framework.dic.Dics;
import com.ccthanking.framework.dic.TreeNode;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.sendMessage;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.util.WorkdayUtils;


/*
 呈请action
 */

@Controller
@RequestMapping("/SPAction")
public class SPAction
    extends BaseDispatchAction
{
    public SPAction()
    {
    }

    public ActionForward getXMLPrintAction(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
        throws Exception
    {

        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        String templateid = request.getParameter("templateid"); // 模板id
        String sjbh = request.getParameter("sjbh"); // 事件编号
        String ywlx = request.getParameter("ywlx"); // 业务类型
        String isEdit = request.getParameter("isEdit"); // 是否编辑
        String fieldname = request.getParameter("fieldname"); // 自定义文件名
        // 刑事案件的通用呈请文书编号,由于现在前台都默认的是传递171的编号
        // 所以暂用这个作为判定条件
        if (templateid != null && templateid.equals("171") && ywlx != null)
        {
            TreeNode tn = Dics.getDicByName("YWLX");
            if (tn != null)
            {
                tn = tn.getNodeByCode(ywlx);
                fieldname = "呈请" + tn.getDicValue() + "报告书";
            }
        }
        else if (templateid != null && templateid.equals("175") && ywlx != null)
        {
            TreeNode tn = Dics.getDicByName("YWLX");
            if (tn != null)
            {
                tn = tn.getNodeByCode(ywlx);
                fieldname = tn.getDicValue() + "审批表";
            }
        }

        WsConfigManager config = new WsConfigManager();
        String tempId = config.getWsTemplateId(ywlx, user); // 根据业务类型及用户级别定位文书模板编号
        if (tempId != null)
        {
            templateid = tempId;
        }
        com.ccthanking.framework.wsdy.PubWS ws = new com.ccthanking.framework.wsdy.
            PubWS();
        if (fieldname != null)
        {
            ws.setFieldName(fieldname);
        }
        Connection conn = null;
        try
        {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            ws.getPrintXml(request, response, templateid, sjbh, ywlx, user,
                           conn,fieldname,"0"); // 生成文书xml串
            conn.commit();
            ws.print(request, response, templateid, sjbh, ywlx, isEdit, "1","9"); // 打印显示
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
            conn = null;
        }
        return null;
    }

    /*
     * 该方法为通用的文书预览的功能 预览时在后台查询对应的事件已获得是否已经发起审批
     */
    public ActionForward PreviewWS(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {

        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Connection conn = DBUtil.getConnection();
        EventVO evo = null;
        String eventid = (String) request.getParameter("eventid");
        String ywlx = (String) request.getParameter("ywlx"); // 业务类型
        String name = Pub.getDictValueByCode("YWLX", ywlx);
        try
        {
            conn.setAutoCommit(false);
            evo = EventManager.getEventByID(eventid);

            Pub.writeXmlInfoMessage(response, "呈请审批成功！");
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);

            Pub.writeXmlErrorMessage(response, "意外错误，呈请审批失败！！" + e.toString());
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
            conn = null;
        }
        return null;
    }
    
    //	查询审批流程
    public ActionForward QueryFqSplc(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {

        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String orderFilter = RequestUtil.getOrderFilter(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
        {
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;

            //此处可以设置自定义的过滤条件
        }
        condition += " AND to_number(a.eventid)=b.sjbh and b.cjsj = (select max(c.cjsj) from ap_task_schedule c where c.ywlx=b.ywlx and c.sjbh=b.sjbh) and b.rwzt='06'";
        condition += orderFilter;
        if (page == null)
        {
            page = new PageManager();
        }
        page.setFilter(condition);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "select a.PROCESSOID , a.PROCESSTYPEOID , a.CREATETIME , a.CLOSETIME , a.EVENTID , a.STATE , a.OPERATIONOID , a.MEMO , a.TASKOID , a.PROCESSEVENT , a.RESULT , a.RESULTDSCR,b.CJRID,b.CJSJ,b.YWLX,b.DBRYID,b.SPYJ,b.SPR,b.id from ap_processinfo a,ap_task_schedule b  ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldDic("YWLX", "YWLX");
            bs.setFieldUserID("DBRYID");
            //设置字典翻译定义
            //设置时间的显示格式
            bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd HH:mm");
            bs.setFieldDateFormat("CLOSETIME", "yyyy-MM-dd HH:mm");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult, "UTF-8");
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, this.handleError(e));
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return null;
    }
    
    /*
     * 该方法为通用的呈请审批的方法
     */
    @RequestMapping(params = "StartSP", method = RequestMethod.POST)
	@ResponseBody
    public requestJson StartSP(HttpServletRequest request,HttpServletResponse response,requestJson json)
        throws Exception
    {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        String jsonString = json.getMsg();
	      BaseVO bv = new BaseVO();
	      JSONArray list = bv.doInitJson(jsonString);
	      JSONObject joList = (JSONObject)list.get(0);
	      
        Connection conn = DBUtil.getConnection();
        String eventid = joList.getString("SJBH");
        String ywlx = joList.getString("YWLX"); // 业务类型
        String title = joList.getString("AJMC"); // 标题
        String type = ""; //几级审批
        
        // 重启一个审批完成的收文 add by xhb@2013-10-10   start
        String isReStart = !joList.containsKey("isReStart") ? "" : joList.getString("isReStart");
        // 重启一个审批完成的收文 add by xhb@2013-10-10   end
        
        // modified by guanchb@2008-12-23 start type参数暂时不使用，js中使用condition参数代替type参数
        // type参数暂设置为空字符串
        // modified by guanchb@2008-12-23 start 
        try
        {
            EventVO evo = null;
            
            String name = Pub.getDictValueByCode("YWLX", ywlx);
            //add by cbl start 描述信息调整
            String tempdesc = "";
            String rsdesc = "";
            tempdesc = Pub.getProdesc(conn, ywlx, eventid);
            if(Pub.empty(tempdesc)){
            	rsdesc = name;
            }else{
            	rsdesc = tempdesc;
            }
            String desc = "";
            if (title == null || "".equals(title))
            {   
            	//desc = user.getOrgDept().getDeptName() + "—"+ user.getName() + "—办理【" + rsdesc + "】的业务";
            	desc = "【" + rsdesc + "】的业务";
            }else
            	//desc = user.getOrgDept().getDeptName() + "—"+ user.getName() + "—办理 【" + rsdesc + "】的业务";
            	desc = " 【" + rsdesc + "】的业务";
            //add by cbl 描述信息调整 end;
            
            // 当收文为重启标识时，需要需求一下待办的名称
            desc = "isReStart".equals(isReStart) ? (desc + "【重启】") : desc;
            try
            {
                conn.setAutoCommit(false);
                evo = EventManager.getEventByID(eventid);
                EventManager.updateEvent(conn, evo, user);
//                TaskMgrBean taskMgr = new TaskMgrBean();
//                TaskVO task = null;

                Class classsp = null;
                try
                {
                    classsp = Class.forName("com.ccthanking.framework.spflow.DealSP.ImpDealSP" + ywlx);
                }
                catch (Exception E)
                {
                	//add by songxb@2008-05-21
                    classsp =Class.forName("com.ccthanking.framework.spflow.DealSP.DefaultDealSP");

                }
                IDealSP dealsp = (IDealSP) classsp.newInstance();
                dealsp.dealSP(request, response, conn, evo, ywlx, desc,user, type, joList);
                //更新pub_blob表的SPZT为'1'
                String update = " update pub_blob set spzt='1' where sjbh='"+eventid+"' and ywlx='"+ywlx+"' and zfbs='0' ";
                String update2 = " update AP_PROCESSCONFIG set spzt='1' where sjbh='"+eventid+"' and ywlx='"+ywlx+"' ";
                DBUtil.executeUpdate(conn, update, null);
                DBUtil.executeUpdate(conn, update2, null);
                
                LogManager.writeUserLog(evo.getSjbh(), ywlx,
                                        Globals.OPERATION_TYPE_INSERT,
                                        LogManager.RESULT_SUCCESS, desc + "成功。",
                                        user, "", "");
                conn.commit();
  	          JSONObject jo = new JSONObject();
  	          jo.put("message", "呈请成功！");
              Pub.writeMessage(response, jo.toString(),"UTF-8");
            }
            catch (Exception e)
            {
                conn.rollback();
                e.printStackTrace(System.out);
                LogManager.writeUserLog(evo == null ? "" : evo.getSjbh(),
                                        ywlx, Globals.OPERATION_TYPE_INSERT,
                                        LogManager.RESULT_FAILURE, desc + "失败。",
                                        user, "", "");
    	          JSONObject jo = new JSONObject();
    	          jo.put("message", "意外错误，呈请审批失败！！"+ e.toString());
                  Pub.writeMessage(response, jo.toString(),"UTF-8");

            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);

        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
            conn = null;
        }
        return null;
    }

    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward hasDocDone(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
        throws Exception
    {
        String SJBH = request.getParameter("SJBH");
        String docID = request.getParameter("docID");
        String YWLX = request.getParameter("YWLX");
        String SPZT = request.getParameter("SPZT");
        if(SPZT == null || "".equals(SPZT)) SPZT = "9";
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        String userLevel = user.getLevelName();
        String sql = null;
        try
        {
            /**
               sql = "select ws_template_id from za_zfba_jcxx_ws_typz where ywlx = ? and dept_level = ? ";
               QuerySet qs = DBUtil.executeQuery(sql, new Object[] { YWLX,
                 userLevel });
               if (qs.getRowCount() > 0) {
                docID = qs.getString(1, "ws_template_id");
               }
             */
            sql = "select t.spzt from pub_blob t where t.sjbh = ? and t.ws_template_id = ? and ZFBS='0' and instr('"+SPZT+"',spzt,1,1)>0";
            QuerySet qs = DBUtil.executeQuery(sql, new Object[]
                                              {SJBH, docID});

            if (qs.getRowCount() < 1)
            {
                Pub.writeMessage(response, "-1");
            }
            else
            {
                Pub.writeMessage(response, qs.getString(1, 1));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            Pub.writeMessage(response, "意外错误，判断[文书是否已存在]失败！！"); // + e.toString());
        }
        return null;
    }
    // add by guanchb@2009-01-05 
    // 增加获得文书文号的方法 
    public ActionForward getWswh(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String SJBH = request.getParameter("SJBH");
		String docID = request.getParameter("docID");
		String YWLX = request.getParameter("YWLX");
		String SPZT = request.getParameter("SPZT");
		if (SPZT == null || "".equals(SPZT))
			SPZT = "9";
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String userLevel = user.getLevelName();
		String sql = null;
		try {
			sql = "select t.WSWH from pub_blob t where t.sjbh = ? and t.ws_template_id = ? and ZFBS='0' and instr('"
					+ SPZT + "',spzt,1,1)>0";
			QuerySet qs = DBUtil.executeQuery(sql, new Object[] { SJBH, docID });
			if (qs.getRowCount() < 1) {
				Pub.writeMessage(response, "-1");
			} else {
				Pub.writeMessage(response, qs.getString(1, 1));
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Pub.writeMessage(response, "-1"); 
		}
		return null;
	}
    public ActionForward setAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	String sjbh = request.getParameter("sjbh"); if(sjbh == null) sjbh = "";
    	String ywlx = request.getParameter("ywlx"); if(ywlx == null) ywlx = "";
    	String linksrc = request.getParameter("linksrc"); if(linksrc == null) linksrc = "";
    	String desc = request.getParameter("desc"); if(desc == null) desc = "";
    	String overrun = request.getParameter("overrun"); if(overrun == null) overrun = "";
    	String txr = request.getParameter("txr"); if(txr == null) txr = "";
    	String txdw = request.getParameter("txdw"); if(txdw == null) txdw = "";
    	String txrole = request.getParameter("txrole"); if(txrole == null) txrole = "";
    	if("".equals(sjbh)||"".equals(ywlx)||"".equals(linksrc)||"".equals(desc)||"".equals(overrun)||("".equals(txr)&&"".equals(txdw)||"".equals(txrole))){
    		throw new Exception("缺少关键参数！");
    	}
    	Connection conn = DBUtil.getConnection();
    	try {
    		conn.setAutoCommit(false);
    		if(txr.equals("")){
    			String selTxr = "select t1.account from org_person t1,org_role_psn_map t2 "
    				+ " where t1.account = t2.person_account ";
    			if(!txdw.equals("") && !txrole.equals("")){
    				selTxr = selTxr + " and (t1.department='"+txdw+"' and t2.role_name='"+txrole+"')";
    			}else if(txdw.equals("")){
    				selTxr = selTxr + " and (t2.role_name='"+txrole+"')";
    			}else{
    				selTxr = selTxr + " and (t1.department='"+txdw+"')";
    			}
    			String[][] txrArr = DBUtil.query(conn, selTxr);
    			if (txrArr != null) {
					for (int i = 0; i < txrArr.length; i++) {
						//AlertManager.AlertSet(conn, desc, overrun,txrArr[i][0], "", "", ywlx, sjbh, linksrc);
					}
				}
    		}else{
    			//AlertManager.AlertSet(conn,desc,overrun,txr,"","",ywlx,sjbh,linksrc);
    		}
    		conn.commit();
    		Pub.writeMessage(response, "1");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Pub.writeMessage(response, "-1"); 
		}
		return null;
	}    

	/*
	 * 该方法为多选的呈请审批的方法
	 */
	/** delete by xukx 什么东东，去掉，垃圾代码 */
	public ActionForward fqsp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		EventVO evo = null;
		String eventid = (String) request.getParameter("eventid");
		String ywlx = (String) request.getParameter("ywlx"); // 业务类型
		String title = (String) request.getParameter("title");// 标题
		if (title == null || "".equals(title))
			title = "案件";
		String name = Pub.getDictValueByCode("YWLX", ywlx);
		String desc = user.getOrgDept().getDeptName() + " " + user.getName()
				+ " 办理 【" + title + "】的" + name + "业务";
		try {
			conn.setAutoCommit(false);
			evo = EventManager.getEventByID(eventid);
			TaskMgrBean taskMgr = new TaskMgrBean();
			TaskVO task = taskMgr.createApproveTask(conn, evo.getSjbh(), ywlx,
					desc + "，请审批。", user);
			if (task == null) { // 该业务不需要审批
				EventManager.archiveEvent(conn, evo, user);
			} else {
				taskMgr.createApproveWS(request, response, evo.getSjbh(), ywlx,
						conn);
			}
			conn.commit();
			LogManager.writeUserLog(evo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					desc + "成功。", user, desc, desc);
			writeXmlMessage(response, "");
			// Qzcsjbxx qzjcsjbxx = new Qzcsjbxx();
			// ZaZfbaXzajWsXzcfxxVO vo = new ZaZfbaXzajWsXzcfxxVO();
			// qzjcsjbxx.InsertFromXzcf(conn, eventid,vo);
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			LogManager.writeUserLog(evo == null ? "" : evo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					desc + "失败。", user, desc, desc);
			Pub.writeXmlErrorMessage(response, "意外错误，呈请审批失败！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}
     

    // 写返回xml信息，去掉操作成功等信息
    public static Document writeXmlMessage(HttpServletResponse response,
                                           String strRowxml)
        throws Exception
    {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("RESPONSE");
        if (root != null)
        {
            root.addAttribute("errorcode", "0");
            root.addAttribute("code", "0");
            Element resultRoot = root.addElement("RESULT");
            resultRoot.addText("$");
        }
        String message = doc.asXML();
        int index = message.indexOf("$");
        message = message.substring(0, index) + strRowxml
            + message.substring(index + 1, message.length());
        writeMessage(response, message, "UTF-8");
        return doc;
    }
    
//	查询审批流程
    @RequestMapping(params = "QueryAllSplc")
	@ResponseBody
    public String QueryAllSplc(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		QueryConditionList list = RequestUtil.getConditionList(json);
		PageManager page =  RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = list == null ? "" : list.getConditionWhere();
//	    condition += " AND to_number(a.eventid)=b.sjbh ";
		condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			
			 String sql = "select processoid, processtypeoid, createtime, closetime, eventid, state, operationoid, memo, taskoid, processevent, result, resultdscr, yxbs, value1, value2, value3, value4 from ap_processinfo ";

			  BaseResultSet bs = DBUtil.query(conn, sql, page);
	            bs.setFieldDic("value3", "YWLX");
	            bs.setFieldDic("RESULT", "LCZT");
	            //设置字典翻译定义
	            //设置时间的显示格式
	            bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd");
	            bs.setFieldDateFormat("CLOSETIME", "yyyy-MM-dd");
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}
    
//	查询审批流程
    public ActionForward QuerySplc(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {

        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String orderFilter = RequestUtil.getOrderFilter(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
        {
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;

            //此处可以设置自定义的过滤条件
        }
        condition += " AND to_number(a.eventid)=b.sjbh ";
        condition += orderFilter;
        if (page == null)
        {
            page = new PageManager();
        }
        page.setFilter(condition);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "select a.PROCESSOID , a.PROCESSTYPEOID , a.CREATETIME , a.CLOSETIME , a.EVENTID , a.STATE , a.OPERATIONOID , a.MEMO , a.TASKOID , a.PROCESSEVENT , a.RESULT , a.RESULTDSCR,b.CJRID,b.CJSJ,b.YWLX,b.DBRYID,b.SPYJ,b.SPR from ap_processinfo a,ap_task_schedule b  ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldDic("YWLX", "YWLX");
            bs.setFieldUserID("DBRYID");
            //设置字典翻译定义
            //设置时间的显示格式
            bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd");
            bs.setFieldDateFormat("CLOSETIME", "yyyy-MM-dd");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult, "UTF-8");
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, this.handleError(e));
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return null;
    }

    // 写返回xml信息
    public static void writeMessage(HttpServletResponse response, String str,
                                    String encode)
        throws Exception
    {
        OutputStream os = response.getOutputStream();
        if (Pub.empty(encode))
        {
            os.write(str.getBytes());
        }
        else
        {
            os.write(str.getBytes(encode));
        }
        os.flush();
        os.close();
    }

    private boolean countBar(Connection conn, String sAJBH)
        throws Exception
    {
        String sqlStr =
            "select count(*) num from ZA_ZFBA_JCXX_RK_BARXX where AJBH ='"
            + sAJBH + "'";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStr);

        while (rs.next())
        {
            if (rs.getInt("num") >= 2)
            {
                return true;
            }
        }
        return false;
    }

//	查询审批流程中的审批创建人
    public ActionForward QuerySPCJR(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
        throws Exception
    {
        String ywlx = request.getParameter("ywlx");
        String sjbh = request.getParameter("sjbh");
        String spbh = request.getParameter("spbh");
        if (ywlx.equals("") || ywlx == null)
        {
            throw new Exception("获得业务类型失败");
        }
        if (sjbh.equals("") || sjbh == null)
        {
            throw new Exception("获得事件编号失败");
        }
        if (spbh.equals("") || spbh == null)
        {
            throw new Exception("获得审批编号失败");
        }
        Connection conn = DBUtil.getConnection();
        try
        {
            Document domresult = null;
            conn.setAutoCommit(false);
            String cjrid = "";
            String cjrxm = "";
            Object[] objs = new Object[3];
            objs[0] = sjbh;
            objs[1] = ywlx;
            objs[2] = spbh;
            String sql = "select t.cjrid, t.cjrxm from ap_task_schedule t where t.sjbh=? and t.ywlx=? and t.spbh=? order by t.seq asc";
            String[][] qsList = DBUtil.querySql(conn, sql, objs);
            if (qsList != null)
            {
                cjrid = qsList[0][0];
                cjrxm = qsList[0][1];
                String xmlStr = "<RESPONSE><ROW><CJRID>" + cjrid +
                    "</CJRID><CJRXM>" + cjrxm + "</CJRXM></ROW></RESPONSE>";
                domresult = DocumentHelper.parseText(xmlStr);
                Pub.writeXmlDocument(response, domresult, "UTF-8");
            }
            else
            {
                throw new Exception("未查询到审批创建人信息");
            }
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, this.handleError(e));
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return null;
    }

//setLytx
    public ActionForward setLytx(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        User user = (User) request.getSession().getAttribute(
            com.ccthanking.framework.Globals.USER_KEY);
        Connection conn = DBUtil.getConnection();
        //String sjbh = request.getParameter("sjbh");
        //String ywlx = request.getParameter("ywlx");
        String txr = request.getParameter("txr");
        String desc = request.getParameter("desc");
        //if(sjbh.equals("")||sjbh==null){
        //    throw new Exception("获得事件编号失败");
        //}
        //if(ywlx.equals("")||ywlx==null){
        //    throw new Exception("获得业务类型失败");
        //}
        if (txr.equals("") || txr == null)
        {
            throw new Exception("获得被提醒人信息失败");
        }
        if (desc.equals("") || desc == null)
        {
            throw new Exception("获得提醒描述内容失败");
        }
        try
        {
            conn.setAutoCommit(false);
            //AlertManager.AlertSet(conn,desc,"0",txr,"","",ywlx,sjbh,"#");
            sendMessage.sendMessageToPerson(conn, "审批留言", desc, user.getAccount(),
                                            txr, false, false, true, "");
            conn.commit();
            Pub.writeXmlInfoMessage(response, "发起留言提醒成功");
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "发起留言提醒失败");
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return null;
    }

// add by guanchb 2008-03-28
// 变更处理意见审批级数，解决一级审批变三级审批情况。
// 做法：清空当前处理意见关联的处罚信息的处理意见序号和处理意见审批级数字段
    public ActionForward doBgspjs(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
        throws Exception
    {
        User user = (User) request.getSession().getAttribute(
            com.ccthanking.framework.Globals.USER_KEY);
        Connection conn = DBUtil.getConnection();
        String sjbh = request.getParameter("sjbh");
        String ywlx = request.getParameter("ywlx");
        if(sjbh.equals("")||sjbh==null){
            throw new Exception("获得事件编号失败");
        }
        if(ywlx.equals("")||ywlx==null){
            throw new Exception("获得业务类型失败");
        }
        try
        {
            conn.setAutoCommit(false);
            String desc = "";
            ChangeSP chsp = new ChangeSP();
            desc = chsp.change(conn,sjbh,ywlx);
            conn.commit();
            Pub.writeXmlInfoMessage(response, desc);
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "变更审批级数失败");
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return null;
    }
    //  add by guanchb@2009-02-10 start;
    //	查询已归档任务审批流程
    public ActionForward QueryFinishSplc(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {

        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String orderFilter = RequestUtil.getOrderFilter(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
        {
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;

            //此处可以设置自定义的过滤条件
        }
        condition += " AND to_number(a.eventid)=b.sjbh and b.cjsj = (select max(c.cjsj) from ap_task_schedule c where c.ywlx=b.ywlx and c.sjbh=b.sjbh) and b.rwzt='06'";
        condition += orderFilter;
        if (page == null)
        {
            page = new PageManager();
        }
        page.setFilter(condition);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "select a.PROCESSOID , a.PROCESSTYPEOID , a.CREATETIME , a.CLOSETIME , a.EVENTID , a.STATE , a.OPERATIONOID , a.MEMO , a.TASKOID , a.PROCESSEVENT , a.RESULT , a.RESULTDSCR,b.CJRID,b.CJSJ,b.YWLX,b.DBRYID,b.SPYJ,b.SPR,b.id from ap_processinfo a,ap_task_schedule b  ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldDic("YWLX", "YWLX");
            bs.setFieldUserID("DBRYID");
            //设置字典翻译定义
            //设置时间的显示格式
            bs.setFieldDateFormat("CREATETIME", "yyyy-MM-dd HH:mm");
            bs.setFieldDateFormat("CLOSETIME", "yyyy-MM-dd HH:mm");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult, "UTF-8");
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, this.handleError(e));
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
        return null;
    }
    // add by guanchb@2009-02-10 
    // 恢复已归档代办任务
    public ActionForward hfYgdrw(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		String rwid = Pub.val(request, "rwid");
		String cgbz = "";
		Connection conn = DBUtil.getConnection();
		try {
			if (rwid.equals("")) {
				throw new Exception("获得主键信息失败，无法恢复该任务！");
			}
			conn.setAutoCommit(false);
			String sql = " UPDATE AP_TASK_SCHEDULE SET ISHF='1',RWZT='01' WHERE ID = '"+rwid+"' ";
			DBUtil.execSql(conn, sql);
			conn.commit();
			cgbz = "1";
			Pub.writeXmlInfoMessage(response, cgbz);
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

    @RequestMapping(params = "TsFqr", method = RequestMethod.POST)
    public requestJson TsFqr(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		User user = (User) request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
        Connection conn = DBUtil.getConnection();
		
		String sjbh = request.getParameter("sjbh");
		String ywlx = request.getParameter("ywlx");
        String dbr = request.getParameter("dbr");
        
        
        String name = Pub.getDictValueByCode("YWLX", ywlx);
        String tempdesc = Pub.getProdesc(conn, ywlx, sjbh);
        String rsdesc = "";
        
        if(Pub.empty(tempdesc)){
        	rsdesc = name;
        }else{
        	rsdesc = tempdesc;
        }
        String desc = "【" + rsdesc + "】的业务";
        
		if (Pub.empty(sjbh) || Pub.empty(ywlx) || desc == null
				|| user == null) {
			throw new Exception("不满足调用条件！");
		}
		
		String compliteSql = "update AP_TASK_SCHEDULE set rwzt='06', rwlx='2', " 
				+ "wcrid='" + user.getAccount() + "' ,wcrxm='" + user.getName() + "', " 
				+ "wcdwdm='" + user.getDepartment() + "', wcsj=sysdate, result='1' " 
				+ "where sjbh='" + sjbh + "' and dbryid='" + user.getAccount() + "' and wcsj is null ";
		
		try {
			
			conn.setAutoCommit(false);
			DBUtil.execSql(conn, compliteSql);
	        TaskMgrBean taskMgr = new TaskMgrBean();
			
	        Process proc = ProcessMgr.getProcessByEvent(conn, sjbh);
			String[] multiDbr = dbr.split(",");
			for (int i = 0; i < multiDbr.length; i++) {
				//创建待办对象
				TaskVO vo = taskMgr.createTask(TaskVO.TASK_TYPE_APPROVE, sjbh, ywlx, desc, user, conn);
				// add by wuxp
				//创建流程实例通过待办对象。注：一个流程对应AP_PROCESSINFO一条记录，这里发起特送时，依然属于一个流程。
				vo.setRWLX(TaskVO.TASK_TYPE_APPROVE);
				vo.setRWZT(TaskVO.TASK_STATUS_VALID);
				vo.setDBRYID(multiDbr[i]);
				
				vo.setSPBH(proc.getProcessOID());
				Step step = proc.getFirstStep();
				// 发起审批后,获取ap_processdetail表的实例记录，更新此记录的超期日期维护到系统中
				String SHEDULE_DAYS = step.getShedule_days();// 获取超期天数
				if (!Pub.empty(SHEDULE_DAYS)) {
					// 计算超期日期，在当前工作日期后增加特定天数,取时间为当前日期
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					WorkdayUtils workdayUtils = new WorkdayUtils();
					Date date = workdayUtils.getWorkday(new Date(),
							Integer.parseInt(SHEDULE_DAYS));// 获取计算后的时间
					// 转换为字符串更新到ap_processdetail表中
					String updatesql = "update AP_PROCESSDETAIL a  set a.SHEDULE_TIME = to_date('"
							+ format.format(date)
							+ " 23:59:59','yyyy-MM-dd hh24:mi:ss') where PROCESSOID = '"
							+ step.getProcessOID() + "' and STEPSEQUENCE='" + step.getStepSequence() + "'";
					DBUtil.exec(conn, updatesql);
					// 设置待办表规定完成时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					vo.setShedule_time(sdf.parse(format.format(date) + " 23:59:59"));
				}
				vo.setDBDWDM("");

				// 特送无需查询角色
				vo.setDbRole("");
				//设置待办序号发起序号为1
				vo.SetStepsequence("0");
				//
		        EventManager.setEventState(conn, sjbh, Globals.EVENT_STATE_APPROVE);
				taskMgr.saveTask(conn, vo);
			}
			conn.commit();
	        JSONObject jo = new JSONObject();
	        jo.put("message", "审批成功");
            Pub.writeMessage(response, jo.toString(),"UTF-8");
		} catch (SQLException e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		
    	return null;
    }
    
}