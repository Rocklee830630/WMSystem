package com.ccthanking.framework.CommonChart.showchart.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.DBUtil;

/**
 * Servlet implementation class XmkgXmlServlet
 */
public class XmzxqkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmzxqkServlet() {
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

		  	response.setContentType("text/html;charset=gbk");  
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();//生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			String nd = request.getParameter("nd");
			String[][] re = null;
			String ywg = "";//已完工
			String zczx = "";//正常执行
			String yjxm = "";//预警项目
			String yqxm = "";//延期项目
			String getsql = "";
			//已完工
			 getsql = "select count(*) from GC_JH_SJ t where t.nd = '"+nd+"' and t.sfyx = '1' and ((T.ISWGSJ  ='1' AND  WGSJ_SJ is not null) OR T.ISWGSJ = '0') AND (T.XMBS = '1' OR ISNOBDXM = '1')";
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
			
			if(re != null){
				ywg = re[0][0];
			}
			//正常执行
			//getsql = "select count(*) from GC_JH_SJ t where t.nd = '"+getnd+"' and t.sfyx = '1' and wgsj_sj is null and wgsj is not null and (wgsj-sysdate) >0" ;
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT COUNT(GC_JH_SJ_ID) AS ZCZX ");
			sbSql.append("FROM GC_JH_SJ T ");
			sbSql.append("WHERE ");
			sbSql.append("(((T.ISWGSJ = '1' AND T.WGSJ IS NOT NULL AND T.WGSJ_SJ IS NOT NULL AND T.WGSJ_SJ<=T.WGSJ) OR (T.WGSJ IS NULL AND T.WGSJ_SJ IS NOT NULL) OR (T.WGSJ IS NULL AND T.WGSJ_SJ IS NULL) OR (T.WGSJ IS NOT NULL AND trunc(SYSDATE)>=T.WGSJ)) OR T.ISWGSJ = '0') ");
 			sbSql.append("AND T.ND = '"+nd+"' AND T.SFYX = '1' AND (T.XMBS = '1' OR ISNOBDXM = '1') ");
			try {
				re  = DBUtil.querySql(conn, sbSql.toString());
				
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
			
			if(re != null){
				zczx = re[0][0];
			}
			//预警项目数提前5天预警
			getsql = "select count(*) from GC_JH_SJ t where t.nd = '"+nd+"' and t.sfyx = '1' and ISWGSJ = '1' AND wgsj is not null and wgsj_sj is null and trunc(SYSDATE)>wgsj and trunc(SYSDATE)-wgsj<=5 AND (T.XMBS = '1' OR ISNOBDXM = '1')" ;
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
			
			if(re != null){
				yjxm = re[0][0];
			}
			//延期项目数
			sbSql = new StringBuffer();
			sbSql.append("SELECT COUNT(GC_JH_SJ_ID) AS YQ ");
			sbSql.append("FROM GC_JH_SJ T ");
			sbSql.append("WHERE ");
			sbSql.append("(T.ISWGSJ = '1' AND T.WGSJ_SJ IS NULL AND T.WGSJ IS NOT NULL AND  (trunc(SYSDATE)>T.WGSJ AND trunc(SYSDATE)-T.WGSJ>5))  ");
			sbSql.append("AND T.ND = '"+nd+"' AND T.SFYX = '1' AND (T.XMBS = '1' OR ISNOBDXM = '1')");
			
			//getsql = "select count(*) from GC_JH_SJ t where t.nd = '"+getnd+"' and t.sfyx = '1' and wgsj_sj is not null and wgsj is not null and (wgsj_sj-wgsj) > 0 ";
			try {
				re  = DBUtil.querySql(conn, sbSql.toString());
				
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
			
			if(re != null){
				yqxm = re[0][0];
			}
			sb.append("<chart use3DLighting='0' showShadow='0' pieRadius = '70' palette='4' decimals='0' enableSmartLabels='1' enableRotation='1' bgColor='#FFFFFF'  bgAngle='360' showBorder='0' startingAngle='70' baseFont='微软雅黑' baseFontSize='12'  xAxisName='用户' showFCMenuItem='0'>");		
			sb.append("<set color=\""+ChartUtil.chartColor1+"\" label=\"已完工项目\" value=\""+ywg+"\" />");
			sb.append("<set color=\""+ChartUtil.chartColor2+"\" label=\"正常执行\" value=\""+zczx+"\" />");
			sb.append("<set color=\""+ChartUtil.chartColor3+"\" label=\"预警项目\" value=\""+yjxm+"\" />");
			sb.append("<set color=\""+ChartUtil.chartColor4+"\" label=\"延期项目\" value=\""+yqxm+"\" />");
			sb.append("</chart>");
	

			
			out = response.getWriter();  
			out.println(sb);  
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}

	  
  }
}
