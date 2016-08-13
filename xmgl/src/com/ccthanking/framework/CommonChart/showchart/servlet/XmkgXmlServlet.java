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
 * Servlet implementation class XmkgXmlServlet
 */
public class XmkgXmlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmkgXmlServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("GBK");
		response.setContentType("text/html;charset=GBK");
		PrintWriter out = response.getWriter();

		String year = "".equals(request.getParameter("year")) ? "2013" : request.getParameter("year");
		
		String chartsLx = request.getParameter("chartsLx");
		String first = request.getParameter("first");
		
		// 点击菜单进入页面时，不进行任何查询
		if ("1".equals(first)) {
			out.println("");
			out.flush();
			out.close();
			return;
		}

		String sql = "select * from FS_CHART_PROJECT where year='" + year + "'";
		Connection conn = DBUtil.getConnection();
		String[][] re = null;
		try {
			re  = DBUtil.querySql(conn, sql);
			
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
		}		
		
		String title = year + "年各项目公司项目开工情况统计";
		
		// 饼形图
		if ("3".equals(chartsLx)) {
			String xml = "<chart caption='" + title + "' formatNumberScale='0'>\r\n";
			for (int i = 0; i < re.length; i++) {
				String data = re[i][2];
				String label = re[i][1];
				
				String setData = "<set label='" + label + "' value='" + data + "'  />\r\n";
				xml += setData;
			}
			xml += "</chart>\r\n";
			out.println(xml);
			out.flush();
			out.close();
			return;
		}

		
		String normalHead = "<chart palette=\"2\" caption=\"" + title + "\" showLabels=\"1\" showvalues=\"0\" decimals=\"0\" numberPrefix=\"\" clustered=\"0\" exeTime=\"1.5\" showPlotBorder=\"0\" zGapPlot=\"30\" zDepth=\"90\" divLineEffect=\"emboss\" startAngX=\"10\" endAngX=\"18\" startAngY=\"-10\" endAngY=\"-40\">\r\n";
		
		String lineHead = "<chart bgColor=\"FFFFFF\" outCnvBaseFontColor=\"666666\" caption=\"" + title + "\"  xAxisName=\"年\" yAxisName=\"个\" numberPrefix=\"\" showLabels=\"1\" "
				+ " showValues=\"0\" plotFillAlpha=\"70\" numVDivLines=\"10\" showAlternateVGridColor=\"1\" AlternateVGridColor=\"e1f5ff\" divLineColor=\"999999\" baseFontColor=\"666666\" "
				+ " canvasBorderThickness=\"1\" showPlotBorder=\"0\" plotBorderThickness=\"0\" zgapplot=\"0\" zdepth=\"120\" exeTime=\"1.2\" dynamicShading=\"1\" YZWallDepth=\"5\" ZXWallDepth=\"5\" "
				+ " XYWallDepth=\"5\" canvasBgColor=\"FBFBFB\" startAngX=\"0\" startAngY=\"0\" endAngX=\"5\" endAngY=\"-25\" divLineEffect=\"bevel\">";
		
		String chartHead = "4".equals(chartsLx) ? lineHead : normalHead;
		String category = "";
		for (int i = 0; i < re.length; i++) {
			String label = re[i][1];
			category += "<category label=\"" + label + "\" />\r\n";
		}
		
		String[] color = {"8BBA00","F6BD0F","AFD8F8","A133F8"};
		String[] seriesName = {"开工-完工","开工-在建","开工-拆迁项目","未开工-拆迁项目"};
		String lineMSCombi3D = "4".equals(chartsLx) ? " renderAs=\"line\" " : "";
		
		String dataset = "";
		int cnt = 0;
		for (int i = 0; i < re[0].length; i++) {
			if (i == 4 || i == 5 || i == 6 || i == 8) {
				String plotBorderColor = "4".equals(chartsLx) ? " plotBorderColor=\"" + color[cnt] + "\" " : "";
				String tempData = "<dataset seriesName=\"" + seriesName[cnt] + "\" color=\"" + color[cnt] + "\" showValues=\"0\" " + plotBorderColor + lineMSCombi3D + ">\r\n";
				for (int j = 0; j < 4; j++) {
					String data = re[j][i];
					tempData += "<set value=\"" + data + "\" />\r\n";
				}
				tempData += "</dataset>\r\n";
				dataset += tempData;
				cnt++;
			}
			
		}
		
		String xml = chartHead + "<categories>\r\n" +category + "</categories>\r\n" + dataset 
				
				+"<styles>\r\n"
				+"<definition>\r\n"
				+"<style name=\"captionFont\" type=\"font\" size=\"15\" />\r\n"
				+"</definition>\r\n"
				+"<application>\r\n"
				+"<apply toObject=\"caption\" styles=\"captionfont\" />\r\n"
				+"</application>\r\n"
				+"</styles>\r\n"
				+"</chart>";
		out.println(xml);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
