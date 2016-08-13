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

/**
 * Servlet implementation class CharServlet
 */
public class CharServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CharServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("GBK");
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        
        String catogoryChart = request.getParameter("catogoryChart");
        String targetPage = request.getParameter("targetPage");
        
        // 结算汇总统计报表
        if ("jshzChart".equals(catogoryChart)) {
    		String year1 = "".equals(request.getParameter("year1")) ? "2010" : request.getParameter("year1");
    		String year2 = "".equals(request.getParameter("year2")) ? "2013" : request.getParameter("year2");
    		
    		Connection conn = DBUtil.getConnection();
    		String sql = "SELECT * FROM FS_CHART WHERE YEAR >= '" + year1 + "' AND YEAR <= '" + year2 + "'" ;
    		
    		try {
    			String[][] re  = DBUtil.querySql(conn, sql);
    			
    			conn.close();
    			request.setAttribute("chartResult", re);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}

    		request.setAttribute("year1", year1);
    		request.setAttribute("year2", year2);
		} else if("xmkgChart".equals(catogoryChart)) {
			String year = "".equals(request.getParameter("year")) ? "2013" : request.getParameter("year");
			
			String sql = "select * from FS_CHART_PROJECT where year='" + year + "'";
			Connection conn = DBUtil.getConnection();
			
			try {
				String[][] rs = DBUtil.query(conn, sql);
				conn.close();
    			request.setAttribute("chartResult", rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		request.setAttribute("year", year);
		}
		request.getRequestDispatcher("/jsp/char/" + targetPage).forward(request, response);
	//	response.sendRedirect("/jsp/char/charDemo.jsp");
		out.println("");
        out.flush();
        out.close();
	}

}
