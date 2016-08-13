package com.ccthanking.framework.wsdy.pub_blob;

import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.ccthanking.framework.base.*;
import org.dom4j.*;

import com.ccthanking.framework.dic.*;
import com.ccthanking.framework.util.*;

import java.sql.Connection;
import com.ccthanking.framework.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.wsdy.pub_blob.Pub_BlobVO;
import java.io.OutputStream;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.wsdy.PubWS;

public class Pub_BlobAction extends BaseDispatchAction {

	/*
	 * 附件管理表插入
	 */
	public void Insert(Pub_BlobVO vo,Connection conn)
			throws Exception {

	         OutputStream os = null;

             if(vo!=null){
                 vo.setFjbh(new RandomGUID().toString());
             }
	     try
	        {
//	            conn.setAutoCommit(false);
                //此处可以设置主键的序列值
	            BaseDAO dao = new BaseDAO();
	            dao.insert(conn,vo);
//	            conn.commit();
	        }
	        catch (Exception e)
	        {


//	            conn.rollback();

	            e.printStackTrace(System.out);
                throw new Exception("插入pub_blob错误："+e.getMessage());
	        }
	        finally
	        {
//	            if (conn != null)
//	                conn.close();
	        }
	}
    public static String WipeOff(String value) {
      if (value == null || "".equals(value) || "0".equals(value)) {
        return value;
      }
      if ("0".equals(value.substring(value.length() - 1, value.length()))) {
        return WipeOff(value.substring(0, value.length() - 1));
      }
      else {
        return value;
      }
    }
	/*
	 * 查询附件管理表
	 */
	public ActionForward QueryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        OrgDept dept = user.getOrgDept();
        String deptid = dept.getDeptID();
        deptid = WipeOff(deptid);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String orderFilter = RequestUtil.getOrderFilter(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;


        condition +=" and a.sjbh = b.sjbh and a.sfgz ='1' and b.pcsdm like '"+deptid+"%' and a.sjbh not in (select sjbh from ws_electron_print)";
        condition += orderFilter;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            String sql = "select FJBH,FILENAME,WSLBDM,MULTIMEDIA,ACTIVE_FLAG,ZJBS,FJBS,ZJPXH,FJPXH,DAH,WSWH,TBSJ,SFGZ,a.SJBH,a.YWLX,KEY_ID,WS_TEMPLATE_ID,'未加盖' as JGYZ from PUB_BLOB a,event b ";
            BaseResultSet bs = DBUtil.query(conn, sql,
                                            page);
            bs.setFieldDic("YWLX","YWLX");
            bs.setFieldDateFormat("TBSJ","yyyy年MM月dd日");

            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
        }

        finally {
            if (conn != null)
                conn.close();
        }
        return null;
	}
	/*
	 * 附件管理表修改
	 */
	public ActionForward Update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

             User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
             Document doc = RequestUtil.getDocument(request);
	     OutputStream os = null;
	     Connection conn = DBUtil.getConnection();
	     try
	        {
	            conn.setAutoCommit(false);
	            List list = doc.selectNodes("/DATAINFO/ROW");
	            Element row = (Element) list.get(0);
	            Pub_BlobVO vo = new Pub_BlobVO();
	            vo.setValue(row);
	            BaseDAO dao = new BaseDAO();
	            dao.update(conn,vo);
	            conn.commit();
	            Pub.writeXmlInfoMessage(response,"操作成功！");
	        }
	        catch (Exception e)
	        {
	            conn.rollback();
	            e.printStackTrace(System.out);
	            Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
	        }
	        finally
	        {
	            if (conn != null)
	                conn.close();
	        }
	        return null;
	}
    public ActionForward AddAzt(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

             User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
             Document doc = RequestUtil.getDocument(request);
             OutputStream os = null;
             Connection conn = DBUtil.getConnection();
             String templateid = request.getParameter("templateid");
             String sjbh = request.getParameter("sjbh");
             String ywlx = request.getParameter("ywlx");
         try
            {
                conn.setAutoCommit(false);
                PubWS pubws = new PubWS();
                pubws.insertAztPrint(conn,templateid,sjbh,ywlx,"","",doc.asXML(),PubWS.YZLB_YZ);
                conn.commit();
                Pub.writeXmlInfoMessage(response,null);
            }
            catch (Exception e)
            {
                conn.rollback();
                e.printStackTrace(System.out);
                Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
            }
            finally
            {
                if (conn != null)
                    conn.close();
            }
            return null;
    }
    //add by songxb@2008-02-29
    public ActionForward UpdateWstemplate(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
        throws Exception
    {

        User user = (User) request.getSession().getAttribute(com.ccthanking.
            framework.Globals.USER_KEY);
//         Document doc = RequestUtil.getDocument(request);
         String ws_template_id = Pub.val(request,"WS_TEMPLATE_ID");
         if(ws_template_id==null){
             ws_template_id = "";
         }
//        Connection conn = DBUtil.getConnection();
        try
        {
            com.ccthanking.framework.wsdy.UpdateWstemplate ws = new com.ccthanking.framework.wsdy.UpdateWstemplate();
            String  message = ws.update(ws_template_id);
            Pub.writeMessage(response, message);
        }
        catch (Exception e)
        {
//            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeMessage(response, this.handleError(e));
        }
        finally
        {

        }
        return null;
    }

}
