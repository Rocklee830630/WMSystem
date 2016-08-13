package com.ccthanking.framework.CommonChart.showchart.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;
import com.ibm.icu.text.DecimalFormat;

/**
 * Servlet implementation class XmkgXmlServlet
 */
public class HtlxTjServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HtlxTjServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

 public void doGet(HttpServletRequest request, HttpServletResponse response)  
    		   throws ServletException, IOException { 
    		  process(request, response);  
    		 }  
 
    		  
  public void doPost(HttpServletRequest request, HttpServletResponse response)  
    		   throws ServletException, IOException {  
    		  process(request, response);  
    		 }
  
  private void process(HttpServletRequest request,  
		   HttpServletResponse response) throws ServletException, IOException {
	  try {
		
			//add data
		
		

		  	response.setContentType("text/html;charset=gbk");  
		  	
		  	String nd = request.getParameter("nd");
		  	
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();//生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			
			
		
		 

		
			String[][] re = null;
			String getsql = "";
			//已完工
			 getsql = "select ht.htlx,count(*) zs, decode(sum(ht.zhtqdj),NULL,0,sum(ht.zhtqdj)) zqj from gc_htgl_ht ht where ht.htzt>0 and ht.qdnf='"+nd+"' group by ht.htlx ";
			try {
				re  = DBUtil.querySql(conn, getsql);

				if(re == null) {
					out.println("");
					out.flush();
					out.close();
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (conn != null) {
					conn.close();
				}
			}
			
			
			
			//add data
			
			//sb.append("<chart pieRadius = \"70\" use3DLighting = \"0\" showShadow = \"0\"  palette=\"4\" decimals=\"0\" numberSuffix=\"个\" enableSmartLabels=\"1\" enableRotation=\"0\" bgColor=\"#FFFFFF\"  bgAngle=\"360\" showBorder=\"0\" startingAngle=\"70\" baseFont=\"Arial\" baseFontSize=\"12\" xAxisName=\"用户\" showFCMenuItem=\"0\">");
			sb.append("<chart use3DLighting='0' showShadow='0' pieRadius = '70' baseFont='微软雅黑' palette='4' decimals='0' decimals=\"0\" numberSuffix=\"个\" enableSmartLabels='1' enableRotation='1' bgColor='#FFFFFF'  bgAngle='360' showBorder='0' startingAngle='70' baseFont='Arial' baseFontSize='12'  xAxisName='用户' showFCMenuItem='0'>");
			if(re!=null){
				int sumPs = 0;
				String text = "";
				String hovertext = "";
				for (int i = 0; i < re.length; i++) {
					sumPs += Integer.parseInt(re[i][1]);
				}
				for (int i = 0; i < re.length; i++) {
					text = Pub.getDictValueByCode("HTLX", re[i][0]);
					String bs = getBs((float)Integer.parseInt(re[i][1])/(float)sumPs);
					hovertext = Pub.getDictValueByCode("HTLX", re[i][0])+"(共"+re[i][2]+"元),"+bs+"%";
					sb.append("<set label=\""+text+"\" value=\""+re[i][1]+"\" hoverText=\""+hovertext+"\"/>");
				}
			}
			sb.append("</chart>");
	

			
			out = response.getWriter();  
			out.println(sb);  
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}

	  
  }
  private String getBs(float num){
	  DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
	  return df.format(num*100);//返回的是String类型的
  }
}
