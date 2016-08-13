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
public class HtztTjServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HtztTjServlet() {
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
		  	nd = nd!=null&&!"".equals(nd)?" and ht.qdnf='"+nd+"' ":"";
		  	
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();//生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			
			
		
		 
			//固定颜色
			//String[] color = new String[]{"9b59b6","34485e","f1c40e","3398db","19bc9c"};
			sb.append("<chart use3DLighting='0' showShadow='0' pieRadius = '70' baseFont='微软雅黑' palette='4' decimals='0' decimals=\"0\" numberSuffix=\"个\" enableSmartLabels='1' enableRotation='1' bgColor='#FFFFFF'  bgAngle='360' showBorder='0' startingAngle='70' baseFont='Arial' baseFontSize='12'  xAxisName='用户' showFCMenuItem='0'>");
		
			String[][] re = null;
			String getsql = "";
			try {
				//审批中
				getsql = "select count(*),sum(ht.zhtqdj) from gc_htgl_ht ht where 1=1 " + nd +" and ht.htzt ='-2'";
				re  = DBUtil.querySql(conn, getsql);
				//add data
				int sumPs = 0;
				//sb.append("<chart pieRadius = \"70\" use3DLighting = \"0\" showShadow = \"0\"  palette=\"4\" decimals=\"0\" numberSuffix=\"个\" enableSmartLabels=\"1\" enableRotation=\"0\" bgColor=\"#FFFFFF\"  bgAngle=\"360\" showBorder=\"0\" startingAngle=\"70\" baseFont=\"Arial\" baseFontSize=\"12\" xAxisName=\"用户\" showFCMenuItem=\"0\">");
				if(re!=null){
					String text = "";
					String hovertext = "";
					
						text = "审批中";
						hovertext = "审批中(共"+re[0][1]+"元)";
						sb.append("<set label=\""+text+"\" value=\""+re[0][0]+"\" hoverText=\""+hovertext+"\"/>");
				}


				//待签订
				getsql = "select count(*),sum(ht.zhtqdj) from gc_htgl_ht ht where 1=1 " + nd +" and ht.htzt in ('-1','0')";
				re  = DBUtil.querySql(conn, getsql);
				if(re!=null){
					String text = "";
					String hovertext = "";
					
						text = "待签订";
						hovertext = "待签订(共"+re[0][1]+"元)";
						sb.append("<set label=\""+text+"\" value=\""+re[0][0]+"\" hoverText=\""+hovertext+"\"/>");
				}
				
				//履行中
				getsql = "select count(*),sum(ht.zhtqdj) from gc_htgl_ht ht where 1=1 " + nd +" and ht.htzt ='1'";
				re  = DBUtil.querySql(conn, getsql);
				if(re!=null){
					String text = "";
					String hovertext = "";
					
						text = "履行中";
						hovertext = "履行中(共"+re[0][1]+"元)";
						sb.append("<set label=\""+text+"\" value=\""+re[0][0]+"\" hoverText=\""+hovertext+"\"/>");
				}
				
				//已结算
				getsql = "select count(*),sum(ht.zhtqdj) from gc_htgl_ht ht where 1=1 " + nd +" and ht.htzt ='2'";
				re  = DBUtil.querySql(conn, getsql);
				if(re!=null){
					String text = "";
					String hovertext = "";
					
						text = "已结算";
						hovertext = "已结算(共"+re[0][1]+"元)";
						sb.append("<set label=\""+text+"\" value=\""+re[0][0]+"\" hoverText=\""+hovertext+"\"/>");
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
