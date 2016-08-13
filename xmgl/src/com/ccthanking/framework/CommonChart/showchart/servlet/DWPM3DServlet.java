package com.ccthanking.framework.CommonChart.showchart.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.framework.CommonChart.showchart.chart.*;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.*;

public class DWPM3DServlet extends HttpServlet{
    public DWPM3DServlet(){
        super();
    }
    public void destroy() {
        super.destroy();
    }
    public void init() throws ServletException {
        //put your code here
    }

    public void doGet_1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=GBK");
            PrintWriter out = response.getWriter();
            ChartStatisticUtil csu = new ChartStatisticUtil();
            Connection conn = DBUtil.getConnection();
            String sql = request.getParameter("sql");
            String[][] str;
			try {
				str = DBUtil.querySql(conn,sql);
            if(str!=null){
               String[]   title = new String[str.length];
               String[]   newtitle = new String[2];
               String[][]   value = new String[str.length+1][str.length];
               
               for(int j=1; j<3;j++){
            	   
            	   newtitle[0] = "考评案件数量"; 
            	   newtitle[1] = "优质案件数量"; 
            	   
            	   for(int i=0; i<str.length;i++){
            		   
            		   title[i] = str[i][0];
            	       value[j-1][i] = str[i][j]; 
            	  
            	   }
               } 
             
             
            String[][] value1 = csu.unionArray(newtitle, value);
            String[] color = {"fff000","FDC12E","56B9F9","C9198D","00ff00"};
            FusionChartUtil fcu = new FusionChartUtil();
            String xmlData =fcu.generateGroupPoleXmlInfo(title, value1,color,"500");
            out.println(xmlData);
            out.flush();
            out.close();
            }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
            	if (conn != null)
                {
                    try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
                }
            }

    }
    public void doGet_2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=GBK");
            PrintWriter out = response.getWriter();
            ChartStatisticUtil csu = new ChartStatisticUtil();
            Connection conn = DBUtil.getConnection();
            String sql = request.getParameter("sql");
            String[][] str;
			try {
				str = DBUtil.querySql(conn,sql);
            if(str!=null){
               String[]   title = new String[str.length];
               String[]   newtitle = new String[3];
               String[][]   value = new String[str.length+1][str.length];
               
               for(int j=2; j<5;j++){
            	   
            	   newtitle[0] = "考评案件数量"; 
            	   newtitle[1] = "考评得分"; 
            	   newtitle[2] = "优质案件数量";
            	   for(int i=0; i<str.length;i++){
            		   
            		   title[i] = str[i][0];
            	       value[j-2][i] = str[i][j]; 
            	  
            	   }
               } 
             
             
            String[][] value1 = csu.unionArray(newtitle, value);
            String[] color = {"fff000","FDC12E","56B9F9","C9198D","00ff00"};
            FusionChartUtil fcu = new FusionChartUtil();
            String xmlData =fcu.generateGroupPoleXmlInfo(title, value1,color,"500");
            out.println(xmlData);
            out.flush();
            out.close();
            }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
            	if (conn != null)
                {
                    try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
                }
            }

    }
    public void doGet_3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        ChartStatisticUtil csu = new ChartStatisticUtil();
        Connection conn = DBUtil.getConnection();
        String sql = request.getParameter("sql");
        String[][] str;
		try {
			str = DBUtil.querySql(conn,sql);
        if(str!=null){
           String[]   title = new String[str.length];
           String[]   newtitle = new String[3];
           String[][]   value = new String[str.length+1][str.length];
           
           for(int j=1; j<4;j++){
        	   
        	   newtitle[0] = "考评案件数量"; 
        	   newtitle[1] = "考评得分"; 
        	   newtitle[2] = "优质案件数量";
        	   for(int i=0; i<str.length;i++){
        		   
        		   title[i] = str[i][0];
        	       value[j-1][i] = str[i][j]; 
        	  
        	   }
           } 
         
         
        String[][] value1 = csu.unionArray(newtitle, value);
        String[] color = {"fff000","FDC12E","56B9F9","C9198D","00ff00"};
        FusionChartUtil fcu = new FusionChartUtil();
        String xmlData =fcu.generateGroupPoleXmlInfo(title, value1,color,"500");
        out.println(xmlData);
        out.flush();
        out.close();
        }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        	if (conn != null)
            {
                try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        }

}
    public void doGet_4(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=GBK");
            PrintWriter out = response.getWriter();
            ChartStatisticUtil csu = new ChartStatisticUtil();
            Connection conn = DBUtil.getConnection();
            String sql = request.getParameter("sql");
            String[][] str;
			try {
				str = DBUtil.querySql(conn,sql);
            if(str!=null){
               String[]   title = new String[str.length];
               String[]   newtitle = new String[2];
               String[][]   value = new String[str.length+1][str.length];
               
               for(int j=1; j<3;j++){
            	   
            	   newtitle[0] = "考评案件数量"; 
            	   newtitle[1] = "优质案件数量"; 
            	   
            	   for(int i=0; i<str.length;i++){
            		   
            		   title[i] = str[i][0];
            	       value[j-1][i] = str[i][j]; 
            	  
            	   }
               } 
             
             
            String[][] value1 = csu.unionArray(newtitle, value);
            String[] color = {"fff000","FDC12E","56B9F9","C9198D","00ff00"};
            FusionChartUtil fcu = new FusionChartUtil();
            String xmlData =fcu.generateGroupPoleXmlInfo(title, value1,color,"500");
            out.println(xmlData);
            out.flush();
            out.close();
            }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
            	if (conn != null)
                {
                    try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
                }
            }

    }
    public void doGet_9(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=GBK");
            PrintWriter out = response.getWriter();
            ChartStatisticUtil csu = new ChartStatisticUtil();
            Connection conn = DBUtil.getConnection();
            String sql = request.getParameter("sql");
            String[][] str;
			try {
				str = DBUtil.querySql(conn,sql);
            if(str!=null){
               String[]   title = new String[str.length];
               String[]   newtitle = new String[2];
               String[][]   value = new String[str.length+1][str.length];
               
               for(int j=2; j<4;j++){
            	   
            	   newtitle[0] = "案件数量"; 
            	   newtitle[1] = "优质案件数量"; 
            	   
            	   for(int i=0; i<str.length;i++){
            		   
            		   title[i] = str[i][0];
            	       value[j-2][i] = str[i][j]; 
            	  
            	   }
               } 
             
             
            String[][] value1 = csu.unionArray(newtitle, value);
            String[] color = {"fff000","FDC12E","56B9F9","C9198D","00ff00"};
            FusionChartUtil fcu = new FusionChartUtil();
            String xmlData =fcu.generateGroupPoleXmlInfo(title, value1,color,"500");
            out.println(xmlData);
            out.flush();
            out.close();
            }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
            	if (conn != null)
                {
                    try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
                }
            }
           

    }

    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=GBK");
        String sql = request.getParameter("sql");
    	if(sql!=null){
    	int sqll = sql.length();
    	String bs = sql.substring(sqll-1,sqll);
    	if(!bs.equals("")&&bs.equals("1")){
    		doGet_1(request, response);
    	}
    	if(!bs.equals("")&&bs.equals("2")){
    		doGet_2(request, response);
    	}
    	if(!bs.equals("")&&bs.equals("3")){
    		doGet_3(request, response);
    	}
    	if(!bs.equals("")&&bs.equals("4")){
    		doGet_4(request, response);
    	}
    	if(!bs.equals("")&&bs.equals("9")){
    		doGet_9(request, response);
    	}
    	}
        //doGet(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=GBK");
        String sql = request.getParameter("sql");
    	if(sql!=null){
    	int sqll = sql.length();
    	String bs = sql.substring(sqll-1,sqll);
    	if(!bs.equals("")&&bs.equals("1")){
    		doGet_1(request, response);
    	}
    	if(!bs.equals("")&&bs.equals("2")){
    		doGet_2(request, response);
    	}
    	if(!bs.equals("")&&bs.equals("3")){
    		doGet_3(request, response);
    	}
    	if(!bs.equals("")&&bs.equals("4")){
    		doGet_4(request, response);
    	}
    	if(!bs.equals("")&&bs.equals("9")){
    		doGet_9(request, response);
    	}
    	}
        //doGet(request, response);
    }
}