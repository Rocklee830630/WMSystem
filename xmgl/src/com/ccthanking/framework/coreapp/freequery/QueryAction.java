//v20090306-7
//v20090409-8
//v20090413-9
//v20090426-10
package com.ccthanking.framework.coreapp.freequery;

/*****************************************************************************
 * query class. for html and java(jsp/servlet) application with Database
 * operation. (oracle / based on sql)
 * Version 1.0
 * Author: leo Leo.Chou decay@163.com
 * create time : 2006.06.01
 * modify :
 *        2007.03.08 michael wang
 *        2007.05.03 michael wang  apply for jwzh
 * You can copy and/or use and/or modify this program free,but please reserve
 * the segment above. Please mail me if you have any question, Good day!
 *
 * notice:
 * if you do not use weblogic ,you must modify  executeInsert and executeUpdate
 * function to do blob data type
 * you must modify variable header21,replace "freequery" with your web applica-
 * tion name
 *****************************************************************************
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.RightManager;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;


public class QueryAction
    extends BaseDispatchAction
{

    public QueryAction()
    {
    }

//  ִ�н��ķ�����Ϣ
    private Document WriteXmlMessage(Document doc, String strMessage,
			String strTagName) {
		try {
			Element root = null;
			if (doc == null) {
				doc = DocumentFactory.getInstance().createDocument();

				root = doc.addElement("RESPONSE");
				root.addAttribute("errorcode", "0");
				root.addAttribute("code", "0");
				Element resultRoot = root.addElement("RESULT");

				resultRoot.addAttribute("currentpagenum", String.valueOf(1));
				resultRoot.addAttribute("recordsperpage", String.valueOf(12));

				resultRoot.addAttribute("totalpage", String.valueOf(0));
				resultRoot.addAttribute("countrows", String.valueOf(0));
			} else {
				root = doc.getRootElement();
			}
			if (root != null && strMessage != null && strTagName != null) {
				root.addElement(strTagName).addText(strMessage);
			}
		} catch (Exception e) {
			System.out.println("WriteXmlMessage(Document doc,String strMessage,String strTagName):"
							+ e.toString());
		}
		return doc;
	}

//  ���涨��Ĳ�ѯ
    public ActionForward saveTableDefine(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
        throws
        Exception
    {
		RightManager.checkPower(request, response, "querydefine");
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);

        Document domresult = null;
        OutputStream os = null;

        String  strUserID = user.getAccount();
        String  strUserName = user.getName();
        String  strDeptID = user.getDepartment();
//        String  strUserID = "uid";
//        String  strUserName = "test";
//        String  strDeptID = "did";

        String strTableLevel = null;
        String strTableTitle = null;

        Element root = doc.getRootElement();

        org.dom4j.Attribute attID = root.attribute("id");
        String strID = attID == null ? null : attID.getValue();

        Element tableTitleNode = root.element("TABLETITLE");
        Element memoNode = root.element("MEMO");
        org.dom4j.Attribute attLevel = tableTitleNode.attribute("level");
        strTableLevel = attLevel != null ? attLevel.getValue() : null;
        strTableTitle = tableTitleNode.getText();

        String strMemo = memoNode != null ? memoNode.getText() : null;
        Element menuNode = root.element("MENU");
        String strParentMenu ;
        if (menuNode != null)
        	strParentMenu = menuNode.attributeValue("parent");
        else
        	strParentMenu = "";


        ArrayList alQueryJoinList = new ArrayList();
        List filterList = doc.selectNodes("/FREE/FILTERS/FILTER");
        for(int i = 0 ; i < filterList.size(); i ++){

        	Element filterElement = (Element) filterList.get(i);
        	String tempRName = filterElement.elementText("RNAME");
        	if (tempRName == null || "".equals(tempRName))
        		continue;
        	String tempTID = filterElement.element("ROW").elementText("TID");
//        	String tempQID = "";
        	String tempField = filterElement.element("ROW").elementText("FNAME");

        	QueryJoinVO tmpVO = new QueryJoinVO();
//        	tmpVO.setQueryID(tempQID);
        	tmpVO.setTableID(tempTID);
        	tmpVO.setQueryName(strTableTitle);
        	tmpVO.setFieldName(tempField);
        	tmpVO.setRelationName(tempRName);
        	alQueryJoinList.add(tmpVO);

        }

        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);

            Object objFieldValue[] = new Object[7];

            objFieldValue[0] = strDeptID;
            objFieldValue[1] = strUserID;
            objFieldValue[2] = strUserName;
            objFieldValue[3] = strTableLevel;
            objFieldValue[4] = strTableTitle;
            objFieldValue[5] = strMemo;
            objFieldValue[6] = alQueryJoinList.size() > 0 ? "2":"1";

            FreeQueryVO objVO = new FreeQueryVO();

            if (strID == null)
            {
                String strSEQ = DBUtil.getSequenceValue("seq_common_serival_number",conn);
                strID = strSEQ;

                root.addAttribute("ID", strSEQ);
                String strTableInfo = doc.asXML();
                objVO.executeInsert(conn, objFieldValue,strTableInfo,strSEQ);
                objVO.doRelationTable(conn,strSEQ,alQueryJoinList);
            }
            else
            {
                String strTableInfo = doc.asXML();
                objVO.executeUpdate(conn, objFieldValue,strTableInfo,strID);
                objVO.doRelationTable(conn,strID,alQueryJoinList);
            }

            if (strParentMenu != null || "".equals(strParentMenu)){

               String strLayersno = menuNode.attributeValue("layersno");
               String strMenuTitle = menuNode.getText();
               String strMenuName = "query_"+strID;

               objVO.buildMenu(conn, strMenuName, strMenuTitle, strParentMenu, strLayersno,strID);

            }
            
            alQueryJoinList.clear();
            objFieldValue = null;

            String strTitle = "MESSAGE";
            String strMsg = "����ɹ���";
            conn.commit();
            domresult = WriteXmlMessage(domresult, strMsg, strTitle);
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();

        }
        catch (java.sql.SQLException ex)
        {
            conn.rollback();
            ex.printStackTrace();
            domresult = WriteXmlMessage(domresult, "�������\n" + ex.getMessage(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
        }
        finally
        {
        	if (os != null)
        		os.close();
            if (conn != null)

            	conn.close();
            conn = null;
        }
        doc = null;
        domresult = null;
        return null;
    }

//  ɾ���Ѷ���Ĳ�ѯ
    public ActionForward delFreeQuery(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
        throws
        Exception
    {
		RightManager.checkPower(request, response, "querydefine");
        Document doc = RequestUtil.getDocument(request);

        Document domresult = null;
        OutputStream os = null;
        String strSQL = "";

        Element root = doc.getRootElement();

        String strErrorMessage = "";
        org.dom4j.Attribute attID = root.attribute("id");
        String strID = attID == null ? null : attID.getValue();
        if (strID == null)
            strErrorMessage = "��ݲ�����";

        Connection conn = DBUtil.getConnection();
        try
        {
            if ("".equals(strErrorMessage))
            {
            	strSQL = " DELETE FROM "+FreeQueryProps.FREE_QUERY+" WHERE id = " + strID;
                DBUtil.execSql(conn, strSQL);
                strSQL = " DELETE FROM "+FreeQueryProps.FREE_RELATION+" WHERE queryid = " + strID;
                DBUtil.execSql(conn, strSQL);
                strSQL = " DELETE FROM "+FreeQueryProps.FREE_CUSTOM+" WHERE queryid = " + strID;
                DBUtil.execSql(conn, strSQL);
                strSQL = " DELETE FROM "+FreeQueryProps.MENU+" WHERE name = 'query_" + strID + "'";
                DBUtil.execSql(conn, strSQL);

                conn.commit();
            }

            String strTitle = "";
            String strMsg = "";
            if ("".equals(strErrorMessage))
            {
                strTitle = "MESSAGE";
                strMsg = "ɾ����ݳɹ���";
            }
            else
            {
                strTitle = "ERRMESSAGE";
                strMsg = "�������\n" + strErrorMessage;
            }

            domresult = WriteXmlMessage(domresult, strMsg, strTitle);
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();

        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            domresult = WriteXmlMessage(domresult, "�������\n" + ex.getMessage(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
        }
        finally
        {
        	if(os != null)
        		os.close();
            if (conn != null)
                conn.close();
            conn = null;
        }

        doc = null;
        domresult = null;
        return null;
    }

//  ȡ�������Ѿ�����Ĳ�ѯ
    public ActionForward getFreeQueryList(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
        throws
        Exception
    {
		RightManager.checkPower(request, response, "querymanage");
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String condition = list == null ? "1=1" : list.getConditionWhere();
        condition += " AND (userid='" + user.getAccount() +
            "' OR tablelevel=1) AND rownum < " + Constants.MAX_RECORD_LIMITED;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "SELECT tabletitle,username,deptid,createtime,lastdate,"
            	       + " decode(tablelevel,'1','����','2','����','3','�쵼') as tablelevel,id"
            	       + " FROM "+FreeQueryProps.FREE_QUERY;
            BaseResultSet bs = DBUtil.query(conn,sql, page);
            bs.setFieldOrgDept("deptid");
            bs.setFieldDateFormat("createtime", "yyyy-MM-dd");
            bs.setFieldDateFormat("lastdate", "yyyy-MM-dd");
            domresult = bs.getDocument();
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
            bs.Close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
        }
        finally
        {
        	if(os != null)
        		os.close();
            if (conn != null)
            	 conn.close();
            conn = null;
        }
        
        doc = null;
        domresult = null;
        return null;
    }

//  ȡ��ѡ�е���ݱ���ֶ���Ϣ�б�
    public ActionForward getFieldList(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
        throws
        Exception
    {
		RightManager.checkPower(request, response, "");
//        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);

        String condition =" t1.table_id = t2.table_id ";
        List list = doc.selectNodes("//ROW");
        Document domresult = null;
        OutputStream os = null;
        try
        {
            domresult = getTablesFields(list);
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
        }
        finally
        {
        	if (os != null)
        		os.close();
        }
        doc = null;
        domresult = null;
        return null;
    }
    
    //��Ϊ���������ӡ�getFieldList�����������������ȡ���б���ֶΣ�add by liu.ld_20090325
    private Document getTablesFields(List list) throws Exception
    {
        String condition =" t1.table_id = t2.table_id ";
        Iterator itor = list.iterator();

        StringBuffer tempBuffer = new StringBuffer();//AND t1.table_id IN (
        while (itor.hasNext())
        {
              Element row = (Element) itor.next();
              if (tempBuffer.length() > 0)
            	  tempBuffer.append(",");
              tempBuffer.append("'" + row.element("TID").getText() + "'");
        }
        if (tempBuffer.length() > 0 )
        	condition += " AND t1.table_id IN (" + tempBuffer.toString() + ") ";
        else
        	condition += " AND 1=2 ";
        condition += " AND rownum < " + Constants.MAX_RECORD_LIMITED + " ORDER BY tname,show_index ";
        PageManager page = new PageManager();
        page.setPageRows(Constants.MAX_RECORD_LIMITED);
        page.setFilter(condition);
        Document domresult = null;
        Connection conn = DBUtil.getConnection();
       try
       {
            conn.setAutoCommit(false);

            String sql = "SELECT t2.table_id tid, t2.table_owner owner,t2.table_name tname,t2.table_comment tnote"
            	       + ",t1.field_name fname,t1.field_comment fnote,t1.field_type,t1.indexname"
            	       + ",t1.style,t1.date_format fmt,t1.secret_level"
            	       + ",t1.dict_tbl dic_table,t1.dic_file,t1.dict_field dic_code,t1.code_field key_field,t1.content_field_name value_field"
            	       + ",t1.is_codefield c,t1.is_nchar n,t1.is_partition p "
                       + " FROM "+FreeQueryProps.USER_TABLE+" t2,"+FreeQueryProps.USER_FIELD+" t1 ";
            BaseResultSet bs = DBUtil.query(conn, sql,page);
            domresult = bs.getDocument();
            bs.Close();
       }
       finally
       {
           if (conn != null)
              conn.close();
           conn = null;
       }
    	return domresult;
    }

//    ȡ��ָ�����Դ���ʻ�����ݱ�
    public ActionForward getTableList(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
        throws
        Exception
    {
		RightManager.checkPower(request, response, "");
//        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String condition = list == null ? "1=1" : list.getConditionWhere();
        condition += " AND rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += " ORDER BY table_name";
        if (page == null)
            page = new PageManager();
        page.setPageRows(Constants.MAX_RECORD_LIMITED);
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "SELECT table_id tid,table_name tname,table_owner owner,"
              + "table_comment tnote,data_source ds FROM "+FreeQueryProps.USER_TABLE;
            BaseResultSet bs = DBUtil.query(conn,sql,page);
            domresult = bs.getDocument();
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
            bs.Close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
        }
        finally
        {
        	if (os != null)
        		os.close();
            if (conn != null)
            	 conn.close();
            conn = null;
        }
        doc = null;
        domresult = null;
        return null;
    }

//    ģ���ѯȡ��ָ�����Դ���ʻ�����ݱ�
    public ActionForward getCustomTableList(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
        throws
        Exception
    {
		RightManager.checkPower(request, response, "");
//        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        String tableName =(String)request.getParameter("tablename");
        String owner=(String)request.getParameter("tableowner");
        String datasource=(String)request.getParameter("datasource");
        PageManager page = new PageManager();
        String condition =" data_source='"+datasource+"' AND table_owner='"+owner+"'";
        if (tableName !=null && tableName.length()>0)
        {
            condition+=" AND (table_comment like '%"+tableName+"%' OR  table_name like '%"+tableName+"%')";
        }

        condition += " AND rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += " ORDER BY table_name";
        if (page == null)
            page = new PageManager();
        page.setPageRows(Constants.MAX_RECORD_LIMITED);
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "SELECT table_id tid,table_name tname,table_owner owner,"
              + "table_comment tnote,data_source ds FROM "+FreeQueryProps.USER_TABLE+" ";
            BaseResultSet bs = DBUtil.query(conn,sql,page);
            domresult = bs.getDocument();
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
            bs.Close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
        }
        finally
        {
        	if (os != null)
        		os.close();
            if (conn != null)
                 conn.close();
            conn = null;
        }
        domresult = null;
        return null;
    }

//    ��ȡ�˵��б?����ѡ�񸸲˵�
    public ActionForward getMenuList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPower(request, response, "");
		// User user = (User)
		// request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		PageManager page = RequestUtil.getPageManager(doc);
		page.setPageRows(Constants.MAX_RECORD_LIMITED);
		Document domresult = null;
		OutputStream os = null;

		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);

			String sql = "SELECT name, title,layersno FROM "
				       + "  (SELECT t.name,t.title,t.layersno,"
				       + "        (SELECT COUNT(name) FROM "+FreeQueryProps.MENU+" n WHERE n.parent=t.name) num"
				       + "  FROM "+FreeQueryProps.MENU+" t ) "
                       + "WHERE num > 0";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getDocument();
			os = response.getOutputStream();
			XMLWriter writer = new XMLWriter(os);
			writer.write(domresult);
			os.flush();
			os.close();
			bs.Close();
		} catch (Exception e) {
			e.printStackTrace();
			domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
					"ERRMESSAGE");
			if (os == null) {
				os = response.getOutputStream();
			}
			XMLWriter writer = new XMLWriter(os);
			writer.write(domresult);
			os.flush();
			os.close();
		} finally {
			if(os != null)
				os.close();
			if (conn != null)
				conn.close();
			conn = null;
		}
		doc = null;
        domresult = null;
		return null;
	}

//    ȡ�ù�����ѯ�б�
    public ActionForward getJoinList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
		Document doc = RequestUtil.getDocument(request);

		List list = doc.selectNodes("//ROW");
		Iterator itor = list.iterator();
		StringBuffer tempBuffer = new StringBuffer();
		while (itor.hasNext()){
	        Element row = (Element) itor.next();
	        if (tempBuffer.length() > 0)
	      	  tempBuffer.append(" OR ");
	        tempBuffer.append("queryid = '" + row.element("QID").getText() + "'");
		}
		doc = null;

        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            String sql = "SELECT relationid rid,queryid qid,queryname qname,"
                       + "relationname rname,tableid tid,fieldname fname FROM "+FreeQueryProps.FREE_RELATION+" ";
            String condition;
            if (tempBuffer.length() > 0)
            	condition  = tempBuffer.toString();
            else
            	condition = "";
            PageManager page = new PageManager();
            page.setPageRows(Constants.MAX_RECORD_LIMITED);
            page.setFilter(condition);
            BaseResultSet bs = DBUtil.query(conn,sql,page);
            domresult = bs.getDocument();
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
            bs.Close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
            os.close();
        }
        finally
        {
        	if (os != null)
        		os.close();
            if (conn != null)
            	 conn.close();
            conn = null;
        }
        
        doc = null;
        domresult = null;
        return null;
    }

    /**
     * @description	 ������ѯ�������ĵ�
     * @param p_Doc
     * @param p_Request
     * @param p_Flag
     * @param p_UserName
     * @param p_NewSeek
     * @return
     */
    public String parseDoc(Document p_Doc,HttpServletRequest p_Request,String p_Flag,String p_UserName,boolean p_NewSeek)
    {
    	return parseDoc(p_Doc, p_Request, p_Flag, p_UserName, p_NewSeek,false);
    }
    
    public String parseDoc(Document p_Doc,HttpServletRequest p_Request,String p_Flag,String p_UserName,boolean p_NewSeek,boolean getCount)
    {
    	Connection conn = null;
    	String strReturn = "";
    	try{

    		Element objRootElement = p_Doc.getRootElement();
    		Element objStatElement = null;
    		Element objDataElement = null;
    		Element objSeekElement = null;


    		if ("1".equals(p_Flag))
    		{
    			Element objPartElement = objRootElement.element("PART");
    			objStatElement = objPartElement.element("STAT");
    			objDataElement = objPartElement.element("DATA");
    			objSeekElement = objPartElement.element("SEEK");
    			if (p_NewSeek)
    				strReturn = "sdata";
    			else
    			    strReturn = "data";
    		}
    		else if ("2".equals(p_Flag))
    		{
    			Element objImageElement = objRootElement.element("IMAGE");
    			objStatElement = objImageElement.element("STAT");
    			objDataElement = objImageElement.element("DATA");
    			objSeekElement = objImageElement.element("SEEK");
    			if (p_NewSeek)
    				strReturn = "simage";
    			else
    			    strReturn = "image";
    		}


    		if (!"".equals(strReturn))
    		{
    			Element objTablesElement = null;
    			objTablesElement = objRootElement.element("TABLES");
    			List objRowList = objTablesElement.elements("ROW");
    			String strDataSource = "";
    			if (objRowList != null){
    				Element objRowElement = (Element) objRowList.get(0);
    				strDataSource = objRowElement.elementText("DS");
    				if (strDataSource == null)
    					strDataSource = "";
    			}
    			if (!"".equals(strDataSource))
    			    conn = DBUtil.getConnection(strDataSource);
    			else
    				conn = DBUtil.getConnection();
				String strCountSQL = objStatElement.getText();
				if(getCount)
				{
					int index = strCountSQL.indexOf(" WHERE ROWNUM <");
					if(index!=-1)
						strCountSQL = strCountSQL.substring(0, index);
					index = strCountSQL.indexOf(" AND ROWNUM <");
					if(index!=-1)
						strCountSQL = strCountSQL.substring(0, index);
				}
				String strDataSQL = objDataElement.getText();

				String strTemp = (String) p_Request.getParameter("r");
	  		    if (strTemp == null || "".equals(strTemp))
	  			   strTemp = "20";
	  		    int intRowPerPage =  Integer.parseInt(strTemp);

                //intRowPerPage = 30;

	            strTemp = (String) p_Request.getParameter("p");
			    if (strTemp == null || "".equals(strTemp))
				   strTemp = "1";
	  		    int intCurPage = Integer.parseInt(strTemp);
	  		    if (intCurPage == 0)
	  			   intCurPage = 1;

	  		    int intCount = Integer.parseInt(DBUtil.getSignalValue(conn, strCountSQL));
	  		    int intPages ;
	  		    intPages = intCount / intRowPerPage;
			    if (intCount % intRowPerPage > 0)
			      intPages++;

			    p_Request.setAttribute("flag", p_Flag);
			    p_Request.setAttribute("prows", String.valueOf(intRowPerPage));
			    p_Request.setAttribute("records", String.valueOf(intCount));
			    p_Request.setAttribute("pages", String.valueOf(intPages));
			    p_Request.setAttribute("pnum", String.valueOf(intCurPage));
				FreeQuerySet objQS = new FreeQuerySet();

                //���κ��ȡ��ҳ���
				String strPageSQL = " SELECT * FROM (SELECT s.*,ROWNUM idnum FROM ("
						         + strDataSQL + ") s "
						         + " WHERE  ROWNUM <=" + intCurPage * intRowPerPage + " ) q WHERE  idnum> "
						         + (intCurPage - 1) * intRowPerPage;

				List objSeekList = objSeekElement.elements("ITEM");
				for (int i = 0; i < objSeekList.size(); i++) {
					Element tmpElement = (Element) objSeekList.get(i);

					FieldVO objFieldVO = new FieldVO();

					String strTable;
					String strField;
					String strType;
					String strDic;

					strTable = tmpElement.elementText("TID");
					strField = tmpElement.elementText("FID");
					strType = tmpElement.elementText("TYPE");
					strDic = tmpElement.elementText("DIC");

					objFieldVO.setTable(strTable);
					objFieldVO.setField(strField);
					objFieldVO.setType(strType);
					objFieldVO.setDic(strDic);

					objQS.addColumn(objFieldVO);

				}

				objQS.execQuery(conn, strPageSQL);
			    p_Request.setAttribute("objset", objQS);
			    Element objPartElement = objRootElement.element("PART");
			    List objDispList = objPartElement.elements("ITEM");
			    ArrayList alTmpDispList = new ArrayList();
			    for(int i = 0; i < objDispList.size(); i++)
			       alTmpDispList.add(String.valueOf(i));

			    Element objSetElement = objRootElement.element("SET");
			    String strQueryID = objSetElement.elementText("QID");

			    CustomColumn objCustomColumn = new CustomColumn();
			    ArrayList alTmpColumnList = objCustomColumn.getCustomColumnList(alTmpDispList, strQueryID, p_UserName);
			    String strCustomColumn = "";
			    for(int i = 0 ; i < alTmpColumnList.size(); i++ ){
				   String strTempNo = (String)alTmpColumnList.get(i);
				   strCustomColumn += strTempNo+";";
			   }
			   alTmpColumnList.clear();
			   alTmpDispList.clear();
			   p_Request.setAttribute("colset", strCustomColumn);
    		}
    		else
    			strReturn = "error";

    	}
    	catch(Exception e){
    		e.printStackTrace();
    		strReturn = "error";
    	}
    	finally{
    		try {
				if (conn != null)
					 conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				strReturn = "error";
			}
            conn = null;
    	}
    	return strReturn;
    }

    public ActionForward doSeek(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
    	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    	String strConf = request.getParameter("conf");

    	if (strConf == null ||strConf == "")
    	{
    		return mapping.findForward("error");
    	}
    	String strReturn ;
    	sun.misc.BASE64Decoder objBase64 = new sun.misc.BASE64Decoder();
		byte arrConf[] = objBase64.decodeBuffer(strConf);
		String strDeConf = new String(arrConf);
		Document objDoc = org.dom4j.DocumentHelper.parseText(strDeConf);


		String strSwitchFlag =(String) request.getParameter("flag");
		strReturn = parseDoc(objDoc,request,strSwitchFlag,user.getAccount(),false);
		String strShow =(String) request.getParameter("show");
		request.setAttribute("conf", strConf);
		request.setAttribute("show", strShow);
		objDoc = null;
        return mapping.findForward(strReturn);
	}

//    ���ͼƬ
    public ActionForward doSeekImage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		RightManager.checkPower(request, response, "");
    	String strReturn = "";
		try {
			String strQuery = request.getParameter("clause");
			String strDataSource = request.getParameter("ds");

			response.setContentType("image/jpeg");
			sun.misc.BASE64Decoder basedecode = new sun.misc.BASE64Decoder();
			byte[] byteImg = basedecode.decodeBuffer(strQuery.replaceAll(" ","+"));
			String strImgSQL = new String(byteImg);
			byteImg = null;

			Statement stmt = null;
			ResultSet rs = null;
			Connection conn;
			if (!"".equals(strDataSource))
				conn = DBUtil.getConnection(strDataSource);
			else
				conn = DBUtil.getConnection();
			byte buffer[];
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(strImgSQL);
				long lFileLen = 0;
				int intReads;
				buffer = new byte[4096];
				ServletOutputStream out = response.getOutputStream();
				if (rs.next()) {

					InputStream inStream = rs.getBinaryStream(1);
					if (inStream != null) {
						while ((intReads = inStream.read(buffer)) > 0) {
							lFileLen += intReads;
							out.write(buffer, 0, intReads);
						}
						inStream.close();
					}
				}
                if (lFileLen == 0) {
					InputStream iStream = request.getSession().getServletContext()
							.getResourceAsStream("/jsp/framework/freequery/images/noimage.gif");

					while ((intReads = iStream.read(buffer)) > 0)
						out.write(buffer, 0, intReads);
					iStream.close();
				}
                
				rs.close();

			} catch (java.sql.SQLException ex) {
				ex.printStackTrace();
				strReturn = "error";
			}

			finally {
				buffer = null;
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				conn = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			strReturn = "error";
		}

		if ("".equals(strReturn))
			return null;
		else
			return mapping.findForward(strReturn);
    }

//    ��ת����ϸ��Ϣҳ
    public ActionForward doDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
    	String strConf = request.getParameter("conf");
    	String strRel = request.getParameter("rel");

    	request.setAttribute("conf", strConf);
    	request.setAttribute("rel", strRel);

    	return mapping.findForward("detail");
    }

//    ���EXCEL���
    public ActionForward doExcel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
    	ExcelPage objPage = new ExcelPage();
    	objPage.outputPage(request, response);

    	return null;
    }

//    ��ת���Զ�����ҳ
    public ActionForward doCustom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
    	ColCustomPage objPage = new ColCustomPage();
    	objPage.outputPage(request, response);

    	return null;
    }

//    �����Զ�������Ϣ
    public ActionForward doCustomSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
    	String strColumnSet = request.getParameter("colset");
    	String strQueryID = request.getParameter("qid");

    	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);

    	Connection conn = null;
		Statement stmt = null;

	    String strOpSQL;
	    String strWhereSQL = " WHERE queryid='" + strQueryID + "' AND account='" + user.getAccount() + "'";

	    try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
			if ("".equals(strColumnSet)){
				strOpSQL = "DELETE FROM  "+FreeQueryProps.FREE_CUSTOM+" " + strWhereSQL;
			}
			else{
				String strSQL = "SELECT 1 FROM "+FreeQueryProps.FREE_CUSTOM+" "+ strWhereSQL;
				ResultSet rs = stmt.executeQuery(strSQL);

				if (rs.next())
					strOpSQL = "UPDATE "+FreeQueryProps.FREE_CUSTOM+" SET columnset = '" + strColumnSet + "' " + strWhereSQL;
				else
					strOpSQL = "INSERT INTO "+FreeQueryProps.FREE_CUSTOM+" (queryid,account,columnset) VALUES ('"
						     + strQueryID + "','" + user.getAccount() + "','" + strColumnSet + "')";
				rs.close();
			}
			stmt.executeUpdate(strOpSQL);

	    }
	    catch(SQLException e){
	    	e.printStackTrace();
	    }
	    finally{
	    	try {
	    		if (stmt != null)
	    			stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }

    	return null;
    }

    public ActionForward doJoin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPower(request, response, "");
		try {
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			request.setAttribute("queryid", "");

			String strRel = request.getParameter("rel");
			sun.misc.BASE64Decoder objBase64 = new sun.misc.BASE64Decoder();
			byte arrRel[] = objBase64.decodeBuffer(strRel);
			String strDeRel = new String(arrRel);
			arrRel = null;
			Document objDoc = org.dom4j.DocumentHelper.parseText(strDeRel);
			Element rootElement = objDoc.getRootElement();
			String strQueryID = rootElement.attributeValue("qid");

			List itemList = rootElement.elements("ITEM");
			ArrayList alItemList = new ArrayList();
			for(int i = 0; i < itemList.size(); i++){
				String strRName ;
				String strRValue;

				Element tmpItem = (Element) itemList.get(i);

				strRName = tmpItem.elementText("RNAME");
				strRValue = tmpItem.elementText("RVALUE");

				JoinMapVO tmpVO = new JoinMapVO();
				tmpVO.setDRelationName(strRName);
				tmpVO.setSFieldValue(strRValue);
				alItemList.add(tmpVO);
			}

			objDoc = null;

			FreeQueryVO objVO = new FreeQueryVO();
	        String docstr = objVO.getFreeQueryDefine(strQueryID);

            Document doc = DocumentHelper.parseText(docstr);
	        Element rootNode = doc.getRootElement();
	        Element tjbNode = rootNode.element("TJB");
	        rootNode.remove(tjbNode);
            FreeQueryParse objParse = new FreeQueryParse();

            objParse.parseDocument(doc, request,user.getAccount(),alItemList);
            String strFlag = objParse.getSwitchFlag();
            doc = null;

            if ("1".equals(strFlag)){
            	return mapping.findForward("data");
            }
            else if ("2".equals(strFlag)){
        		return mapping.findForward("image");
            }
            else {
            	return mapping.findForward("error");
            }

		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}

	}

    private String returnPageFlag(String p_Flag){
    	if ("1".equals(p_Flag)) {
			return "sdata";
		} else if ("2".equals(p_Flag)) {
			return "simage";
		} else {
			return "error";
		}
    }

    private String executeSeek(String configText,User p_User,HttpServletRequest p_Request) throws Exception{

		Document doc = DocumentHelper.parseText(configText);
		Element rootNode = doc.getRootElement();
		Element tjbNode = rootNode.element("TJB");
		rootNode.remove(tjbNode);
		FreeQueryParse objParse = new FreeQueryParse();
		List tablesList = doc.selectNodes("//TABLES/ROW");
		getCombineCondition(tablesList,p_Request);

		sun.misc.BASE64Encoder baseEncode = new sun.misc.BASE64Encoder();
		String strXML = doc.asXML();
//		String strEncode = baseEncode.encode(strXML.getBytes());
		String strEncode = baseEncode.encode(strXML.getBytes("GBK"));
		strEncode = strEncode.replaceAll("\r\n", "");
//		String strExpress = rootNode.element("FILTERS").elementText("EXPRESSION");
//		if (strExpress != null)
//			p_Request.setAttribute("queryexp", strExpress);
//		else
//			p_Request.setAttribute("queryexp", "");
		p_Request.setAttribute("querydoc", strEncode);

		if (objParse.hasDynamicCondition(doc)) {
			ArrayList filterList = objParse.parseDynamicCondition(doc, p_User);
			if (filterList != null)
				p_Request.setAttribute("dynamic", filterList);

			return "scond";

		} else if (objParse.hasFixedCondition(doc))
			objParse.setFixedValue(doc, p_Request);

		objParse.parseDocument(doc, p_Request, p_User.getAccount(), null);
		doc = null;
//		String strFlag = objParse.getSwitchFlag();
//		return returnPageFlag(strFlag);
		return "fcond";

    }

    //ȡ��ϲ�ѯ�������ֶΣ�add by liu.ld_20090325
    private void getCombineCondition(List list, HttpServletRequest req) throws Exception
    {
		Document combineDoc = getTablesFields(list);
		
		List fieldList = combineDoc.selectNodes("//RESULT/ROW");
		List fields = new ArrayList();
		try{
		for(int i=0; i<fieldList.size(); i++)
		{
			Element element = (Element)fieldList.get(i);
			Element secret_level = element.element("SECRET_LEVEL");
			if(!secret_level.getText().equalsIgnoreCase(FreeQueryProps.COMBINECONDITION_SHOW))
				continue;
			
			Hashtable ht = new Hashtable();
			List propList = element.elements();
			for(int j=0; j<propList.size(); j++)
			{
				Element elem = (Element)propList.get(j);
				String key = elem.getName();
				String val = elem.getText();
				if("STYLE".equalsIgnoreCase(key))
				{
					if(FreeQueryProps.FIELD_TYPE_CHAR_1.equalsIgnoreCase(val))
						val = "text";
					else if(FreeQueryProps.FIELD_TYPE_IDCARD_2.equalsIgnoreCase(val))
						val = "idcard";
					else if(FreeQueryProps.FIELD_TYPE_DIC_3.equalsIgnoreCase(val))
						val = "dic";
					else if(FreeQueryProps.FIELD_TYPE_DATE_4.equalsIgnoreCase(val))
						val = "date";
                    else if (FreeQueryProps.FIELD_KIND_CARD_7.equals(val))
                    	val = "idcard";
					else
						val = "text";
				}
				ht.put(key, val);
			}
			if("date".equalsIgnoreCase((String)ht.get("STYLE")))
			{
				ht.put("FMT", "YYYYMMDD");
			}
			fields.add(ht);
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		req.setAttribute("fields", fields);
    	
    }
    
//    ��ѯ����ҳ����õ�ִ�в�ѯ����
    public ActionForward doSeekExec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
		try {
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String id = request.getParameter("id");
			if (id == null)
				throw new Exception("error get definition!");
			request.setAttribute("queryid", id);
			FreeQueryVO objVO = new FreeQueryVO();
			String docstr = objVO.getFreeQueryDefine(id);
			return mapping.findForward(executeSeek(docstr,user,request));
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}

	}

//  ��ѯҳ����õ�ִ����ϲ�ѯ�ķ�����add by liu.ld_20090325
	public ActionForward doSeekRunCombine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
		try
		{
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String queryXML = request.getParameter("queryXML");
			Document combineCondition = org.dom4j.DocumentHelper.parseText(queryXML);
			String strQueryDoc = request.getParameter("querydoc");
	
			sun.misc.BASE64Decoder objBase64 = new sun.misc.BASE64Decoder();
			byte arrDecodeXML[] = objBase64.decodeBuffer(strQueryDoc);
			String strDeXML = new String(arrDecodeXML,"GBK");
			Document doc = org.dom4j.DocumentHelper.parseText(strDeXML);
	
			FreeQueryParse objParse = new FreeQueryParse();
			objParse.parseDocument(doc, request, user.getAccount(), null, combineCondition);
	
			String strFlag = objParse.getSwitchFlag();
			doc = null;
			return mapping.findForward(returnPageFlag(strFlag));
		} catch (Exception e) {
			e.printStackTrace();
			//return mapping.findForward("error");
		}
		return null;
	}
	
//    ��ѯҳ����õ�ִ�в�ѯ�ķ���
	public ActionForward doSeekRun(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPower(request, response, "");
		try {
			FreeQueryParse objParse = new FreeQueryParse();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			// String docstr = Pub.val(request, "txtXML");
			String docstr = request.getParameter("txtXML");

			String strQueryDoc = request.getParameter("querydoc");

			Document dynamic = null;
			if(docstr!=null && !docstr.equals(""))
				dynamic = DocumentHelper.parseText(docstr);
			Document doc;

			sun.misc.BASE64Decoder objBase64 = new sun.misc.BASE64Decoder();
			byte arrDecodeXML[] = objBase64.decodeBuffer(strQueryDoc);
			String strDeXML = new String(arrDecodeXML,"GBK");
			doc = org.dom4j.DocumentHelper.parseText(strDeXML);

            //�޸���ʾָ��rownumber��������Ϣ
            Element nodeRoot = doc.getRootElement();
            Element nodeHeader = nodeRoot.element("H");
            Element nodeHeaderStyle = nodeHeader.element("STYLE");
            String rowNumber = request.getParameter("rowNumber");
            if (nodeHeaderStyle != null)
            {
	            Element nodeSimple = nodeHeaderStyle.element("SIMPLE");
	            Attribute attributernum = nodeSimple.attribute("rnum");
	            //��ʾ�û�ָ���������ΪĬ��ֵ
	            if (rowNumber != null & !"".equals(rowNumber))
	               attributernum.setValue(rowNumber);
            }
            else
            {
            	if (rowNumber != null & !"".equals(rowNumber))
                   objParse.setRowPerPage(Integer.parseInt(rowNumber));
            }

            if(dynamic != null)
            	objParse.setDynamicValue(doc, dynamic, request);
			objParse.parseDocument(doc, request, user.getAccount(), null);
			String strFlag = objParse.getSwitchFlag();
			doc = null;
			return mapping.findForward(returnPageFlag(strFlag));
		} catch (Exception e) {
			e.printStackTrace();
			//return mapping.findForward("error");
		}
		return null;
	}

//	Ԥ����ѯ
	public ActionForward doSeekView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
		try {
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			request.setAttribute("queryid", "");
			String docstr = request.getParameter("txtXML");
			return mapping.findForward(executeSeek(docstr,user,request));
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}

	}

//	��ѯ��������ִ�й�����ѯʱ���õķ���
	public ActionForward doParseJoin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		RightManager.checkPower(request, response, "");
		try {

			String strRel = request.getParameter("rel");
			request.setAttribute("join", strRel);
//		request.setAttribute("join", "<ROOT><HEAD></HEAD><DATA></DATA><JOIN id='0' qname='��ʵ���˿ڲ�ѯ'>PEpPSU4gcWlkPSI3NTAyIj48L0pPSU4+</JOIN></ROOT>");

            return mapping.findForward("sjoin");

		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}


	}

//	ִ�й�����ѯ
	public ActionForward doSeekJoin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPower(request, response, "");
		try {
			User user = (User) request.getSession().getAttribute(
					Globals.USER_KEY);
			request.setAttribute("queryid", "");

			String strRel = request.getParameter("rel");
			sun.misc.BASE64Decoder objBase64 = new sun.misc.BASE64Decoder();
			byte arrRel[] = objBase64.decodeBuffer(strRel);
			String strDeRel = new String(arrRel);
			arrRel = null;
			Document objDoc = org.dom4j.DocumentHelper.parseText(strDeRel);
			Element rootElement = objDoc.getRootElement();
			String strQueryID = rootElement.attributeValue("qid");

			List itemList = rootElement.elements("ITEM");
			ArrayList alItemList = new ArrayList();
			for(int i = 0; i < itemList.size(); i++){
				String strRName ;
				String strRValue;

				Element tmpItem = (Element) itemList.get(i);

				strRName = tmpItem.elementText("RNAME");
				strRValue = tmpItem.elementText("RVALUE");

				JoinMapVO tmpVO = new JoinMapVO();
				tmpVO.setDRelationName(strRName);
				tmpVO.setSFieldValue(strRValue);
				alItemList.add(tmpVO);
			}

			objDoc = null;

			FreeQueryVO objVO = new FreeQueryVO();
	        String docstr = objVO.getFreeQueryDefine(strQueryID);

            Document doc = DocumentHelper.parseText(docstr);
	        Element rootNode = doc.getRootElement();
	        Element tjbNode = rootNode.element("TJB");
	        rootNode.remove(tjbNode);
            FreeQueryParse objParse = new FreeQueryParse();

            objParse.parseDocument(doc, request,user.getAccount(),alItemList);
            String strFlag = objParse.getSwitchFlag();
            doc = null;

            return mapping.findForward(returnPageFlag(strFlag));

		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}

	}

//	ȷ����ѯ�ķ���ҳ
	 public ActionForward doSeekPage(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			RightManager.checkPower(request, response, "");
	    	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	    	String strConf = request.getParameter("conf");

	    	if (strConf == null ||strConf == "")
	    	{
	    		return mapping.findForward("error");
	    	}
	    	String strReturn ;
	    	sun.misc.BASE64Decoder objBase64 = new sun.misc.BASE64Decoder();
			byte arrConf[] = objBase64.decodeBuffer(strConf);
			String strDeConf = new String(arrConf);
			arrConf = null;
			Document objDoc = org.dom4j.DocumentHelper.parseText(strDeConf);

			String strSwitchFlag =(String) request.getParameter("flag");
			
			boolean getCountFlag = false;
			String strGetCountFlag = (String) request.getParameter("getCount");
			if(null!=strGetCountFlag)
				getCountFlag = Boolean.valueOf((String) request.getParameter("getCount")).booleanValue();
			
			strReturn = parseDoc(objDoc,request,strSwitchFlag,user.getAccount(),true,getCountFlag);
			String strShow =(String) request.getParameter("show");
			request.setAttribute("conf", strConf);
			request.setAttribute("show", strShow);
			objDoc = null;
	        return mapping.findForward(strReturn);
	}

//	 �Զ����ѯ��ʾ����Ϣ���ݣ����Ѷ��������ѡ��
	 public ActionForward doSeekCustom(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			RightManager.checkPower(request, response, "");
		    SeekColCustomPage objPage = new SeekColCustomPage();
	    	objPage.outputPage(request, response);

	    	return null;
	    }
}
