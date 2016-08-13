package com.ccthanking.framework.coreapp.aplink.apStep;
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

public class ApStepAction
    extends BaseDispatchAction {
  //环节定义插入

 	public ActionForward Insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		 User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
		 Document doc = RequestUtil.getDocument(request);
	   Connection conn = DBUtil.getConnection();

	     try
	        {
	            conn.setAutoCommit(false);
	            List list = doc.selectNodes("/DATAINFO/ROW");

	            Element row = (Element) list.get(0);
	            ApStepVO vo = new  ApStepVO();
	            vo.setValue(row);
                if(vo!=null){
                    vo.bindFieldToSequence("STEPOID","AP_TASK_S");
                }

                BaseDAO dao = new BaseDAO();
	            dao.insert(conn,vo);


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

  //环节定义查询

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
        Connection conn = DBUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            String sql = "select ACTOR , DEPTID , DEPTLEVEL , MEMO , NAME , ROLENAME , STEPOID from AP_STEP";
            BaseResultSet bs = DBUtil.query(conn, sql,page);
             		//设置字典翻译定义
            bs.setFieldUserID("ACTOR");
            bs.setFieldOrgDept("DEPTID");
//bs.setFieldOrgDeptSimpleName("DEPTID");
            bs.setFieldDic("DEPTLEVEL","BMJB");
            bs.setFieldOrgRole("ROLENAME");
					  		//设置时间的显示格式
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
 //环节定义修改

	public ActionForward Update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

             User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
             Document doc = RequestUtil.getDocument(request);
	     Connection conn = DBUtil.getConnection();
	     try
	        {
	            conn.setAutoCommit(false);
	            List list = doc.selectNodes("/DATAINFO/ROW");
	            Element row = (Element) list.get(0);
	            ApStepVO vo = new  ApStepVO();
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
   //环节定义删除

    public ActionForward Delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

             User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
             Document doc = RequestUtil.getDocument(request);
         Connection conn = DBUtil.getConnection();
         try
            {
                conn.setAutoCommit(false);
                List list = doc.selectNodes("/DATAINFO/ROW");
                Element row = (Element) list.get(0);
                ApStepVO vo = new  ApStepVO();
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
