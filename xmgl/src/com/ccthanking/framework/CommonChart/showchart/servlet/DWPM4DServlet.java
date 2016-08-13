package com.ccthanking.framework.CommonChart.showchart.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.framework.CommonChart.showchart.chart.*;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.*;

public class DWPM4DServlet extends HttpServlet{
    public DWPM4DServlet(){
        super();
    }
    public void destroy() {
        super.destroy();
    }
    public void init() throws ServletException {
        //put your code here
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=GBK");
            PrintWriter out = response.getWriter();
            ChartStatisticUtil csu = new ChartStatisticUtil();
            Connection conn = DBUtil.getConnection();
            
            String hdbh1 = request.getParameter("hdbh");
            String str1[] = hdbh1.split("\\|");
            String hdbh =str1[0];
            String sjgajg =str1[1];
            String row_id1=str1[2];
            
            String sql="select a.row_id,t.khdwzf,t.hdbh,t.row_id  from GG_KHJG_DWPM t,ORG_DEPT a where t.khdw = a.row_id "+
            			"and t.khjgmb = '4'  and t.ssxjgajg='"+sjgajg+"' and t.row_id='"+row_id1+"'";

            String[][] str;
			try {
				str = DBUtil.querySql(conn,sql);
            if(str!=null){
               String[]   title = new String[str.length];
               String[]   value = new String[str.length];
               String[]   hdbh2  = new String [str.length];
               String[]   row_id = new String [str.length];
               String[]   link = new String[str.length];
               for(int i=0; i<str.length;i++){
            	   title[i] = Pub.getDeptNameByID(str[i][0]);    
            	   value[i] = str[i][1]; 
            	   hdbh2[i] = str[i][2];
            	   
            	   row_id[i] = str[i][3];
            	   link[i] = request.getContextPath()+"/jsp/khpj/jgzs/ggKhjgiframe.jsp?hdbh="+hdbh2[i]+"&rowid="+row_id[i];
               }
               String[] color ={"FDC12E"};
            FusionChartUtil fcu = new FusionChartUtil();
            String xmlData =fcu.generatePoleXmlInfo(title,value,color,link);
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
