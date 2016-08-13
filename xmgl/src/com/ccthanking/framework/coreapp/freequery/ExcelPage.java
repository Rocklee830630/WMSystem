//v20071212-1
//v20081224-2
//v20090306-3
package com.ccthanking.framework.coreapp.freequery;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;

public class ExcelPage extends BasePageConfig {

	private String strHeader;
	private String strBody;
	private String strTail;
	public ExcelPage() {

	    StringBuffer strBuffer = new StringBuffer();
	    strBuffer.append("<html>");
	    strBuffer.append("<head>");
	    strBuffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">");
	    strBuffer.append("<meta name=ProgId content=Excel.Sheet>");
	    strBuffer.append("<style>");
//	    strBuffer.append(".xtable{");
//	    strBuffer.append(" border-collapse: collapse;");
//	    strBuffer.append("}");
	    strBuffer.append(".x2{");
	    strBuffer.append(" font-family:宋体;");
	    strBuffer.append(" font-size:10.5pt;");
	    strBuffer.append(" text-align:center;");
//	    strBuffer.append(" border:0.5pt solid windowtext;");
	    strBuffer.append("}");
	    strBuffer.append(".x3{");
	    strBuffer.append(" font-family:宋体;");
	    strBuffer.append(" font-size:9.0pt;");
//	    strBuffer.append(" text-align:center;");
//	    strBuffer.append(" border:0.5pt solid windowtext;");
	    strBuffer.append("}");
	    strBuffer.append("</style>");
	    strBuffer.append("</head>");
	    strBuffer.append("<body leftmargin='150' topmargin='150'>");

	    strHeader = strBuffer.toString();

	    /*
	     .x_table {
   border-collapse: collapse;
   font-size:9pt;
   text-align:center;
  }
	    .x2{
	        padding-top:2px;
	    	padding-right:1px;
	    	padding-left:1px;
	    	mso-ignore:padding;
	    	font-weight:400;
	    	font-style:normal;
	    	text-decoration:none;
	    	font-family:宋体;
	    	font-size:10.5pt
	    	text-align:center;
	    	border:0.5pt solid windowtext;
	    	white-space:nowrap;
	      }
	      .x3{
	    	padding-top:1px;
	    	padding-right:1px;
	    	padding-left:1px;
	    	mso-ignore:padding;
	    	font-weight:400;
	    	font-style:normal;
	    	text-decoration:none;
	    	font-family:宋体;
	    	text-align:center;
	    	border:0.5pt solid windowtext;
	    	white-space:normal;
	      }
		*/


		strTail = "</body></html>";

		/*
		String header = "<html><head><title></title>\n" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">\n" +
        "<meta name=ProgId content=Excel.Sheet>\n" +
        "<style>\n" +
        "<!--\n" +
        "a {\n" +
        "font-size:          9pt;\n" +
        "color:              navy;\n" +
        "text-decoration:    none;\n" +
        "}\n" +
        "a:hover {\n" +
        "font-size:          9pt;\n" +
        "color:              darkorange;\n" +
        "text-decoration:    underline;\n" +
        "}\n" +
        "-->\n" +
        "</style>\n" +
        "</head>\n" +
        "<body>\n";
		 */
	}

	public void getBody(String p_UserName){
		StringBuffer strBuffer = new StringBuffer();
		ArrayList alTempPartDispList = new ArrayList();

		strBuffer.append("<table align='center' border='1' cellpadding='0' cellspacing='0' width='100%' >");

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
			rs.close();
		    if (strSelectColumns == ""){
		    	for(int i = 0; i < alPartDispList.size(); i++){
		    		DisplayVO objDispVO = (DisplayVO) alPartDispList.get(i);
					alTempPartDispList.add(objDispVO);
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
						if ("1".equals(strColStatus))
							alTempPartDispList.add(objDispVO);;
			    	}
		    	}
		    	else{
		    		for(int i = 0; i < alPartDispList.size(); i++){
			    		DisplayVO objDispVO = (DisplayVO) alPartDispList.get(i);
						alTempPartDispList.add(objDispVO);
			    	}
		    	}
		    }

		    strBuffer.append("<tr>");
			String dispName ;
			for(int i = 0; i < alTempPartDispList.size(); i++){
				DisplayVO objDispVO = (DisplayVO) alTempPartDispList.get(i);
				dispName = objDispVO.getName();
				strBuffer.append("<td nowrap class='x2'");
				if (!"0".equals(objDispVO.getWidth()))
					strBuffer.append(" width='" + objDispVO.getWidth() +"' ");
				if (!"0".equals(objDispVO.getHeight()))
					strBuffer.append(" height='" + objDispVO.getHeight() +"' ");
				strBuffer.append(">");
				strBuffer.append(dispName);
				strBuffer.append("</td>");
			}
			strBuffer.append("</tr>");

			FreeQuerySet objQS = new FreeQuerySet();
			String strPageSQL = "SELECT * FROM (SELECT s.*,ROWNUM idnum FROM (" + strPDataSQL 
			                  + ") s WHERE  ROWNUM <=" + intCurrentPage * intRows + " ) q WHERE  idnum> " 
			                  + (intCurrentPage - 1) * intRows;
			
			for(int i = 0 ; i < alPartSeekList.size();i++){

				FieldVO objFieldVO = (FieldVO) alPartSeekList.get(i);
				objQS.addColumn(objFieldVO);

			}

			objQS.execQuery(conn, strPageSQL);


			for(int intNo = 0; intNo < objQS.getRows(); intNo++){
				strBuffer.append("<tr>");
				for(int j = 0; j < alTempPartDispList.size(); j++){
					DisplayVO objDispVO = (DisplayVO) alTempPartDispList.get(j);

					String dispField = objDispVO.getField();
					String dispValue = objQS.getValue(intNo, dispField);
					String strType = objDispVO.getType();
					if ("3".equals(strType)){
						 String strDic = objDispVO.getDic();
                         //modify by jiawh 20080802
//						 dispValue = Pub.getDictValueByCode(strDic,dispValue);
                         try {
							dispValue = Pub.getValueByCode(strDic,dispValue);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
                    //add by jiawh 对日期进行处理
                    else if("4".equals(strType)){
                       //String strFmt = objDispVO.getFmt();
                       if(!Pub.empty(dispValue))
                       {
                           dispValue = dispValue.substring(0,4)+"-"+dispValue.substring(4,6)+"-"+dispValue.substring(6,8);
                        }
                    }
//					else if ("5".equals(strType))
//					    dispValue = "&nbsp;";
	                if (dispValue == null)
	                    dispValue = "&nbsp;";
	                else
	                	dispValue += "&nbsp";
	                strBuffer.append("<td nowrap class='x3'");
	                if (!"0".equals(objDispVO.getWidth()))
						strBuffer.append(" width='" + objDispVO.getWidth() +"'");
					if (!"0".equals(objDispVO.getHeight()))
						strBuffer.append(" height='" + objDispVO.getHeight() +"'");
					strBuffer.append(">");
	    			strBuffer.append(dispValue);
	    			strBuffer.append("</td>");
				}
				strBuffer.append("</tr>");
			}
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

		strBuffer.append("</table>");
		strBody = strBuffer.toString();
	}

	public void outputPage(HttpServletRequest p_Request,HttpServletResponse p_Response) throws IOException,DocumentException{

        User user = (User) p_Request.getSession().getAttribute(Globals.USER_KEY);
        OutputStream os = p_Response.getOutputStream();
        if (user != null) {
			String strConf = p_Request.getParameter("conf");
			String strCurrentPage = p_Request.getParameter("page");
			String strRows = p_Request.getParameter("rows");
            
			try {
				intCurrentPage = Integer.parseInt(strCurrentPage);
				intRows = Integer.parseInt(strRows);
			} catch (NumberFormatException e) {
				intCurrentPage = 0;
				intRows = 0;
				e.printStackTrace();
			}
			init(strConf);
			getPartDispList();
			getPartSeekList();
			getSetInfo();
			p_Response.reset();
			p_Response.setHeader("content-disposition", "attachment;filename="
					+ "test" + ".xls");
			p_Response.setContentType("application/x-msdownload;");

			getBody(user.getAccount());

			os.write(strHeader.getBytes("GBK"));
			os.write(strBody.getBytes("GBK"));
			os.write(strTail.getBytes("GBK"));
		}
        else
        	os.write("会话失效".getBytes("GBK"));
	    os.flush();
	    os.close();
	    free();


	}
	
}
