 package com.ccthanking.framework.spflow.ViewWs;

import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.ccthanking.framework.base.*;
import org.dom4j.*;
import com.ccthanking.framework.dic.*;
import com.ccthanking.framework.util.*;

import java.sql.Connection;
import com.ccthanking.framework.coreapp.aplink.*;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.common.vo.*;
import com.ccthanking.common.EventManager;

/**
 *
 * @author xukx
 * @des    查看案件文书,误删
 */

public class ViewWsAction extends BaseDispatchAction
{
	//查询pub_blob,za_zfba_jcxx_ws_spckws,根据配置表过滤
    public ActionForward QueryList_Rule(ActionMapping mapping, ActionForm form,
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
        String wsmc="";
               int ws = condition.indexOf("%", condition.indexOf("FILENAME"));
               if (ws > 0)
               {
                   int lws =condition.length();
                   wsmc = condition.substring(condition.indexOf("%", ws) + 1,lws);

               }

        //此处可以设置自定义的过滤条件
        if(Pub.empty(condition))
            condition += " t1.ZFBS='0' and t1.ws_template_id = t3.ws_template_id(+) ";
        else
        	condition += " and t1.ZFBS='0' and t1.ws_template_id = t3.ws_template_id(+)";
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        //added by andy 如果配置表中有数据，则按配置规则显示
        String ywlx = request.getParameter("ywlx");
        String sjbh = request.getParameter("sjbh");

        if(sjbh == null) sjbh = "";
        if(ywlx !=null && "040400".equals(ywlx))
        {
        	if(!"".equals(sjbh))
        	  condition += " and t1.WS_TEMPLATE_ID = t2.WS_TEMPLATE_ID and t1.FJBH = t2.FJBH and t1.DAH = t2.DAH and t2.SJBH='"+sjbh+"' ";
        }

        // 查询串并案关联表中与主案件关联的文书，以及刑事行政互转中正在转移的文书 addby zhangj 2009年5月18日 start
        String ajbh="";
        int beginI=condition.indexOf("'",condition.indexOf("DAH"));
        if(beginI>0){
        int length=beginI-condition.indexOf("DAH");
        ajbh=condition.substring(condition.indexOf("'",beginI)+1,condition.indexOf("'",beginI+length));


       if(!wsmc.equals("")){
           wsmc=" and t1.FILENAME like '%"+wsmc;
       }
        condition += " union all select t1.FJBH FJBH, decode(t1.spzt,'2',t1.FILENAME||'(已审批)','1',t1.filename||'(未审批)','1',t1.filename||'(审批中)' ,t1.filename) FILENAME,t1.DAH DAH, t1.WSWH WSWH, t1.SJBH SJBH, t1.YWLX YWLX, t1.WS_TEMPLATE_ID WS_TEMPLATE_ID,t1.SPZT,t1.CFBGBS from PUB_BLOB t1 "
              +" where t1.dah in(select to_char(hzbh) from za_zfba_jcxx_aj_hzb where zajbh='"+ajbh+"' or xajbh='"+ajbh+"')  "+wsmc;
             }

       //   condition += orderFilter;
       // addby zhangj 2009年5月18日 end
          if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        page.setPageRows(200);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "";
            if(ywlx !=null && "040400".equals(ywlx))
               sql = "select t1.FJBH FJBH, decode(t1.spzt,'2',t1.FILENAME||'(已审批)','8',t1.filename||'(未审批)','1',t1.filename||'(审批中)' ,t1.filename) FILENAME,t1.DAH DAH, t1.WSWH WSWH, t1.SJBH SJBH, t1.YWLX YWLX, t1.WS_TEMPLATE_ID WS_TEMPLATE_ID,t1.SPZT,t1.CFBGBS from PUB_BLOB t1,ZA_ZFBA_JCXX_WS_SPCKWS t2,ZA_ZFBA_JCXX_WS_TYCQSPWSLB t3  ";
            else
               sql = "select t1.FJBH FJBH, decode(t1.spzt,'2',t1.FILENAME||'(已审批)','1',t1.filename||'(未审批)','1',t1.filename||'(审批中)' ,t1.filename) FILENAME,t1.DAH DAH, t1.WSWH WSWH, t1.SJBH SJBH, t1.YWLX YWLX, t1.WS_TEMPLATE_ID WS_TEMPLATE_ID,t1.SPZT,t1.CFBGBS from PUB_BLOB t1,ZA_ZFBA_JCXX_WS_TYCQSPWSLB t3 ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            conn.commit();
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
                conn.close();
        }
        return null;
    }
//
    public ActionForward QueryList(ActionMapping mapping, ActionForm form,
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
        //此处可以设置自定义的过滤条件
        if(Pub.empty(condition))
            condition += " ZFBS='0'";
        else
        	condition += " and ZFBS='0'";
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        //added by andy 如果配置表中有数据，则按配置规则显示
        
        //added by hongpeng_dong 加上根据sjbh过滤
        if(!Pub.empty(Pub.val(request, "sjbh"))){
        	condition += " and sjbh in (" + Pub.val(request, "sjbh") + ")";
        }

        condition += orderFilter;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        page.setPageRows(200);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "select FJBH , FILENAME ,DAH , WSWH, SJBH , YWLX , WS_TEMPLATE_ID , SPZT from PUB_BLOB ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldDic("SPZT", "SHZTDM");
            conn.commit();
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
                conn.close();
        }
        return null;
    }
    //通用呈请报告查询:
    public ActionForward QueryList_Tycqbg(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
throws Exception
{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String jz = user.getOrgDept().getJz();
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String orderFilter = RequestUtil.getOrderFilter(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		//此处可以设置自定义的过滤条件
		if(Pub.empty(condition))
		condition += " a.ZFBS='0'";
		else
		condition += " and a.ZFBS='0' and (a.spzt='1' or a.spzt='8') and a.ws_template_id = b.ws_template_id and (b.jz is null or instr(b.jz,'"+jz+"',1,1)>0) ";
		if (Pub.empty(condition))
		condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		//added by andy 如果配置表中有数据，则按配置规则显示

        // 查询串并案关联表中与主案件关联的文书，以及刑事行政互转中正在转移的文书 addby zhangj 2009年5月18日 start
      String ajbh="";
      int beginI=condition.indexOf("'",condition.indexOf("DAH"));
      if(beginI>0){
      int length=beginI-condition.indexOf("DAH");
      ajbh=condition.substring(condition.indexOf("'",beginI)+1,condition.indexOf("'",beginI+length));
      condition += " union all select  t1.FJBH , t1.FILENAME ,t1.DAH , t1.WSWH, t1.SJBH , t1.YWLX , t1.WS_TEMPLATE_ID from PUB_BLOB t1 "
            +" where t1.dah in((select to_char(hzbh) from za_zfba_jcxx_aj_hzb where zajbh='"+ajbh+"') union all "
            +"( select ajbh from ZA_ZFBA_JCXX_AJ_CBAGLB where clbh in (( select clbh from ZA_ZFBA_JCXX_AJ_CBAGLB where zabs='1' and ajbh='"+ajbh+"'))and ajbh !='"+ajbh+"')) and t1.zfbs='0'  and (t1.spzt='1' or t1.spzt='8')";
       }

     //   condition += orderFilter;
     // addby zhangj 2009年5月18日 end

		if (page == null)
		page = new PageManager();
		page.setFilter(condition);
		page.setPageRows(200);
		Document domresult = null;
		Connection conn = DBUtil.getConnection();
		try
		{
			conn.setAutoCommit(false);
			String sql = "select a.FJBH , a.FILENAME ,a.DAH , a.WSWH, a.SJBH , a.YWLX , a.WS_TEMPLATE_ID from PUB_BLOB a,ZA_ZFBA_JCXX_WS_TYCQSPWSLB b ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			conn.commit();
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
		conn.close();
		}
		return null;
}
//  pub_blob查询:查询非审批文书，并且没有电子签名的
    public ActionForward QueryList_wgz(ActionMapping mapping, ActionForm form,
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
        //此处可以设置自定义的过滤条件
        if(Pub.empty(condition))
            condition += " (ZFBS='0' or ZFBS IS NULL)";
        else
        	condition += " and (ZFBS='0' or ZFBS IS NULL)";
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;

        condition += " and (sjbh not in (select b.sjbh from ws_electron_print b where b.yzlx='2' and b.approvelevel='3') ";
        condition += " or (sjbh not in (select b.sjbh from ws_electron_print b where b.yzlx='2' and b.approvelevel='4'))) ";
        condition += " and ws_template_id in (select c.ws_template_id from ws_template c where c.is_sp_flag = '0' and c.sfqm = '1')";
        condition += orderFilter;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        page.setPageRows(200);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "select FJBH , FILENAME ,DAH , WSWH, SJBH , YWLX , WS_TEMPLATE_ID from PUB_BLOB ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            conn.commit();
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
                conn.close();
        }
        return null;
    }
    // add by guanchb : 查询已作废文书
    public ActionForward QueryListZf(ActionMapping mapping, ActionForm form,
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
        //此处可以设置自定义的过滤条件
        if(Pub.empty(condition))
            condition += " ZFBS='1' ";
        else
            condition += " and ZFBS='1' ";
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += orderFilter;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        page.setPageRows(200);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "select FJBH , FILENAME ,DAH , WSWH, SJBH , YWLX , WS_TEMPLATE_ID, ZFR , ZFSJ , ZFYY from PUB_BLOB ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldUserID("ZFR");
            bs.setFieldDateFormat("ZFSJ","yyyy-MM-dd HH:mm");
            conn.commit();
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
                conn.close();
        }
        return null;
    }
    //查询文书类别信息 addby zhangj 2009年9月7日
    public ActionForward QueryWslb(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
		            HttpServletResponse response)
		throws Exception
		{
			
			String lbmc=""; 
			String dah = request.getParameter("dah");
			Connection conn = DBUtil.getConnection();
			String lbdm=""; 
			int total = 0 ;
			try
			{
				
				String sqljy="select t.dic_value,s.sumws,t.dic_code  from (select count(wslbdm) sumws,wslbdm from ( select t1.wslbdm from PUB_BLOB t1, ZA_ZFBA_JCXX_WS_TYCQSPWSLB t3"
					+"  where T1.DAH = '"+dah+"' and t1.ZFBS = '0' and t1.ws_template_id = t3.ws_template_id(+)"
					+" union all select t1.wslbdm  from PUB_BLOB t1 where t1.dah in (select to_char(hzbh) from za_zfba_jcxx_aj_hzb"
					+"  where zajbh = '"+dah+"'  or xajbh = '"+dah+"')) group by wslbdm  ) s,dic_tree t where s.wslbdm=t.dic_code and t.parent_id='200006029'";

				QuerySet  qs = DBUtil.executeQuery(sqljy,null,conn);
				
				if(qs.getRowCount()>0){
					for(int i=0;i<qs.getRowCount();i++){
						total+= Integer.parseInt(qs.getString(i+1, "SUMWS"));
						lbmc += qs.getString(i+1, "DIC_VALUE")+"("+qs.getString(i+1, "SUMWS")+"份)"+"|";
						lbdm += qs.getString(i+1, "DIC_CODE")+"|";
					}
				}
				String xmlStr = "<ROW><LBMC>"+lbmc+"</LBMC><LBDM>"+lbdm+"</LBDM><TOTAL>"+total+"</TOTAL></ROW>";
				Pub.writeMessage(response, xmlStr,"UTF-8");
			}
			catch (Exception e)
			{
				conn.rollback();
				e.printStackTrace(System.out);
			
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
    //查询文书详细信息 addby zhangj 2009年9月7日
    public ActionForward QueryWsxx(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
		            HttpServletResponse response)
		throws Exception
		{
			
			String lbmc=""; 
			String dah = request.getParameter("dah");
			String wslb = request.getParameter("wslb");
			Connection conn = DBUtil.getConnection();
			String wsid=""; 
			String xh = "";
			String wswh = "";
			String sjbh = "";
			String spzt = "";
			String ywlx = "";
			
			try
			{
				
				String sqljy="select rownum,FJBH,FILENAME,DAH,WSWH,SJBH,YWLX,WS_TEMPLATE_ID,SPZT,CFBGBS,wslbdm from("
					+"  select t1.FJBH FJBH,decode(t1.spzt,'2',t1.FILENAME || '(已审批)','1',t1.filename || '(未审批)', '1', t1.filename || '(审批中)',"
					+"  t1.filename) FILENAME,  t1.DAH DAH,t1.WSWH WSWH,t1.SJBH SJBH, t1.YWLX YWLX, t1.WS_TEMPLATE_ID WS_TEMPLATE_ID,t1.SPZT, t1.CFBGBS,t1.wslbdm,t1.tbsj from PUB_BLOB t1, ZA_ZFBA_JCXX_WS_TYCQSPWSLB t3"
					+"  where T1.DAH = '"+dah+"'   and t1.ZFBS = '0'  and t1.ws_template_id = t3.ws_template_id(+) union all select t1.FJBH FJBH,decode(t1.spzt,'2', t1.FILENAME || '(已审批)','1',t1.filename || '(未审批)','1',t1.filename || '(审批中)', t1.filename) FILENAME, t1.DAH DAH, t1.WSWH WSWH, t1.SJBH SJBH, t1.YWLX YWLX, t1.WS_TEMPLATE_ID WS_TEMPLATE_ID,"
					+"  t1.SPZT, t1.CFBGBS,t1.wslbdm,t1.tbsj from PUB_BLOB t1 where t1.dah in (select to_char(hzbh)   from za_zfba_jcxx_aj_hzb    where zajbh = '"+dah+"' or xajbh = '"+dah+"')  ) where wslbdm='"+wslb+"' order by tbsj";

				QuerySet  qs = DBUtil.executeQuery(sqljy,null,conn);
				if(qs.getRowCount()>0){
					for(int i=0;i<qs.getRowCount();i++){
						xh +=  (i+1)+"|";
						lbmc += qs.getString(i+1, "FILENAME")+"|";
						wsid += qs.getString(i+1, "WS_TEMPLATE_ID")+"|";
						wswh += qs.getString(i+1, "WSWH")+"|";
						sjbh += qs.getString(i+1, "SJBH")+"|";
						spzt += qs.getString(i+1, "SPZT")+"|";
						ywlx += qs.getString(i+1, "YWLX")+"|";
						
					}
					
				}
				
				String xmlStr = "<ROW><LBMC>"+lbmc+"</LBMC><XH>"+xh+"</XH><WSID>"+wsid+"</WSID><WSWH>"+wswh+"</WSWH><SJBH>"+sjbh+"</SJBH><SPZT>"+spzt+"</SPZT><YWLX>"+ywlx+"</YWLX></ROW>";
				Pub.writeMessage(response, xmlStr,"UTF-8");
			}
			catch (Exception e)
			{
				conn.rollback();
				e.printStackTrace(System.out);
			
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
}