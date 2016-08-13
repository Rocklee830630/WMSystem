//v20071212-1
//v20090306-2
package com.ccthanking.framework.coreapp.freequery;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;

public class SeekColCustomPage extends BasePageConfig {

	private String strHeader;
	private String strBody;
	private String strTail;

	public SeekColCustomPage() {

	    StringBuffer strBuffer = new StringBuffer();
	    strBuffer.append("<html>\n");
	    strBuffer.append("<head>\n");
	    strBuffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">\n");
	    strBuffer.append("<style>\n");
	    strBuffer.append(".BodyListTable{\n");
	    strBuffer.append(" font-family:宋体;\n");
	    strBuffer.append(" font-size:9pt;\n");
	    strBuffer.append(" width:100%;\n");
	    strBuffer.append(" background-color:#fffff\n");
	    strBuffer.append("}\n");
	    strBuffer.append(".ListTR1{\n");
	    strBuffer.append(" font-family:宋体;\n");
	    strBuffer.append(" font-size:9pt;\n");
	    strBuffer.append(" background-color:#C5DCFE;\n");
	    strBuffer.append("}\n");
	    strBuffer.append("</style>\n");
//		strBuffer.append("<script language='javascript' src='/jwzh/jsp/framework/freequery/FreeQuery.js'></script>\n");
	    strBuffer.append("</head>\n");
	    strBuffer.append("<body leftmargin='10' topmargin='20'>\n");

	    strHeader = strBuffer.toString();

		strTail = "</body></html>\n";

	}

	public void getBody(String p_UserName,String p_Path){
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("<script language='javascript' src='" + p_Path + "/jsp/framework/freequery/js/freequery.js'></script>\n");
		strBuffer.append("<form action='" + p_Path + "/FrameWork/DefineQueryAction.do' name='frmPost' method='post'>\n");
		strBuffer.append("<table id='fieldList' border='0' class='BodyListTable' cellspacing='1' cellpadding='0' align='center'>\n");

		strBuffer.append("<tr class='ListTR1'>\n");
		strBuffer.append("<td colspan='2'>\n");
		strBuffer.append("  <table border='0' width='100%'>\n");

		strBuffer.append("    <tr>\n");
		strBuffer.append("    <td align='left'><font style='font-size:10pt'>数据项定义</font></td>\n");
		strBuffer.append("    <td align='right'><input type='button' value='全部选定' onclick='selectAll()'>&nbsp;\n");
		strBuffer.append("       <input type=\"button\"  value=\"反向选择\" onclick='unSelectAll()'>&nbsp;\n");
		strBuffer.append("       <input type=\"button\"  value=\"保存\" onclick='doSeekSave()'>&nbsp;\n");
		strBuffer.append("       <input type=\"button\"  value=\"上移↑\" onclick='moveUp()'>&nbsp;\n");
		strBuffer.append("       <input type=\"button\"  value=\"下移↓\" onclick='moveDown()'>&nbsp;\n");
		strBuffer.append("    </td>\n");
		strBuffer.append("    </tr>\n");
		strBuffer.append("  </table>\n");
		strBuffer.append("</td>\n");
		strBuffer.append("</tr>\n");


		Connection conn = null;
		Statement stmt = null;
	    String strSelectColumns = "";
	    try {
			conn = DBUtil.getConnection();
			String strSQL = "SELECT columnset FROM "+FreeQueryProps.FREE_CUSTOM+" WHERE queryid='"
				          + getQueryID() + "' AND account='" + p_UserName + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(strSQL);
			if (rs.next())
				strSelectColumns = rs.getString(1);
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

	    String dispName ;
	    if (strSelectColumns == ""){
	    	for(int i = 0; i < alPartDispList.size(); i++){
	    		DisplayVO objDispVO = (DisplayVO) alPartDispList.get(i);
				dispName = objDispVO.getName();
				strBuffer.append("<tr class='ListTR1' onclick='tableClick(this)'>\n");
				strBuffer.append("<td width='5%'>");
				strBuffer.append("<input type='checkbox' value='" + i + "' name='chkField' checked>");
				strBuffer.append("</td>\n");
				strBuffer.append("<td width='90%'>");
				strBuffer.append(dispName);
				strBuffer.append("</td>\n");
				strBuffer.append("</tr>\n");
	    	}
	    }
	    else{
	    	String[] arrColumn;//0=0;3=1; 0:false 1:true;
	    	arrColumn = strSelectColumns.split(";");
	    	if (alPartDispList.size() == arrColumn.length){
		    	for (int i = 0 ; i < arrColumn.length;i++){

		    		String[] arrColInfo = arrColumn[i].split("=");
		    		String strColIndex = arrColInfo[0];
		    		String strColStatus = arrColInfo[1];
		    		int intColIndex = Integer.parseInt(strColIndex);
		    		if (intColIndex >= alPartDispList.size())
		    			continue;

		    		DisplayVO objDispVO = (DisplayVO) alPartDispList.get(intColIndex);
					dispName = objDispVO.getName();
					strBuffer.append("<tr class='ListTR1'  onclick='tableClick(this)'>\n");
					strBuffer.append("<td>");
					if ("1".equals(strColStatus))
					    strBuffer.append("<input type='checkbox' value='" + strColIndex + "' name='chkField' checked>");
					else
						strBuffer.append("<input type='checkbox' value='" + strColIndex + "' name='chkField'>");
					strBuffer.append("</td>\n");
					strBuffer.append("<td>");
					strBuffer.append(dispName);
					strBuffer.append("</td>\n");
					strBuffer.append("</tr>\n");

		    	}
	    	}
	    	else{
	    		for(int i = 0; i < alPartDispList.size(); i++){
		    		DisplayVO objDispVO = (DisplayVO) alPartDispList.get(i);
					dispName = objDispVO.getName();
					strBuffer.append("<tr class='ListTR1'  onclick='tableClick(this)'>\n");
					strBuffer.append("<td>");
					strBuffer.append("<input type='checkbox' value='" + i + "' name='chkField' checked>");
					strBuffer.append("</td>\n");
					strBuffer.append("<td>");
					strBuffer.append(dispName);
					strBuffer.append("</td>\n");
					strBuffer.append("</tr>\n");
		    	}
	    	}

	    }

		strBuffer.append("</table>\n");
		strBuffer.append("<input type='hidden' name='qid' value='" + strQueryID + "'>\n");
		strBuffer.append("<input type='hidden' name='colset' value=''>\n");

		strBuffer.append("</form>\n");
		strBody = strBuffer.toString();
	}

	public void outputPage(HttpServletRequest p_Request,
			HttpServletResponse p_Response) throws IOException,
			DocumentException {

		String strConf = p_Request.getParameter("conf");
		String strWebPath = p_Request.getContextPath();

		init(strConf);
		getPartDispList();
		getSetInfo();

		p_Response.reset();
		OutputStream os = p_Response.getOutputStream();
		User user = (User) p_Request.getSession().getAttribute(Globals.USER_KEY);

		getBody(user.getAccount(),strWebPath);

		os.write(strHeader.getBytes("GBK"));
		os.write(strBody.getBytes("GBK"));
		os.write(strTail.getBytes("GBK"));

		os.flush();
		os.close();
		free();

	}
}
