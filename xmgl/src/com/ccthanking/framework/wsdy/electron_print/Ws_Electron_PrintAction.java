package com.ccthanking.framework.wsdy.electron_print;

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
import java.io.OutputStream;
import com.ccthanking.framework.base.BaseDispatchAction;

public class Ws_Electron_PrintAction extends BaseDispatchAction {

	/*
	 * 文书电子印章表插入
	 */
	public void Insert(Connection conn,Ws_Electron_PrintVO vo)
			throws Exception {

	         OutputStream os = null;
	       //  Connection conn = DBUtil.getConnection();
	     try
	        {
//	            conn.setAutoCommit(false);
	            BaseDAO dao = new BaseDAO();
	            dao.insert(conn,vo);
//	            conn.commit();
	        }
	        catch (Exception e)
	        {

//	            conn.rollback();

	            e.printStackTrace(System.out);
	        }
	        finally
	        {
//	            if (conn != null)
//	                conn.close();
	        }

	}

	/*
	 * 查询文书电子印章表
	 */
	public ActionForward QueryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String orderFilter = RequestUtil.getOrderFilter(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += orderFilter;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            String sql = "select WS_TEMPLATE_ID,SJBH,YWLX,APPROVEROLE,APPROVELEVEL,MULTIMEDIA from WS_ELECTRON_PRINT";
            BaseResultSet bs = DBUtil.query(conn, sql,
                                            page);

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
	 * 文书电子印章表修改
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
	            Ws_Electron_PrintVO vo = new Ws_Electron_PrintVO();
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
        /*
	 * 文书电子印章表删除
	 */
	public ActionForward Delete(ActionMapping mapping, ActionForm form,
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
	            Ws_Electron_PrintVO vo = new Ws_Electron_PrintVO();
	            vo.setValue(row);
	            BaseDAO dao = new BaseDAO();
	            dao.delete(conn,vo);
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
}
