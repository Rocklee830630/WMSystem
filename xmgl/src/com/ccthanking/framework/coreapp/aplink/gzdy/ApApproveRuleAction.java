package com.ccthanking.framework.coreapp.aplink.gzdy;
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
import com.ccthanking.common.vo.*;

public class ApApproveRuleAction
    extends BaseDispatchAction {
  //流程规则定义表插入

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
	            ApApproveRuleVO vo = new  ApApproveRuleVO();
	            vo.setValue(row);

	            BaseDAO dao = new BaseDAO();
	            dao.insert(conn,vo);
	            conn.commit();
	             Pub.writeXmlMessage(response,vo.getRowXml(),"操作成功！","MESSAGE");
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

  //流程规则定义表查询

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
         //此处可以设置自定义的过滤条件
        // 如 condition+=" AND SHZT='0' "
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            String sql = "select RULE_NAME , UNITID , YWLX , OPERATION_OID , PCSDM , FJDM , SJDM , SSXQ , VALUE1 , VALUE2 , VALUE3 , MEMO , RULE_OID from AP_APPROVE_RULE";
            BaseResultSet bs = DBUtil.query(conn, sql,page);
             		//设置字典翻译定义
  								bs.setFieldOrgDept("FJDM");//分局代码
  								bs.setFieldOrgDept("PCSDM");//派出所代码
  								bs.setFieldOrgDept("SJDM");//市局代码
  								bs.setFieldOrgDept("SSXQ");//省市县区
  								bs.setFieldOrgDept("UNITID");//部门代码
  								bs.setFieldDic("YWLX","YWLX");//业务类型

  		  		  		  							  		//设置时间的显示格式
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult, "UTF-8");
        }
        catch (Exception e) {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, this.handleError(e));
        }
        finally {
            if (conn != null)
                conn.close();
        }
        return null;
	}
 //流程规则定义表修改

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
	            ApApproveRuleVO vo = new  ApApproveRuleVO();
	            vo.setValue(row);
              	            BaseDAO dao = new BaseDAO();
	            dao.update(conn,vo);
	            conn.commit();
	            Pub.writeXmlMessage(response,vo.getRowXml(),"操作成功！","MESSAGE");
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
   //流程规则定义表删除

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
                ApApproveRuleVO vo = new  ApApproveRuleVO();
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
                Pub.writeXmlErrorMessage(response, this.handleError(e));
            }
            finally
            {
                if (conn != null)
                    conn.close();
            }
            return null;
    }

}
