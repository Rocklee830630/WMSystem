package com.ccthanking.framework.CommonChart.showchart.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

/**
 * Servlet implementation class XmkgXmlServlet
 */
public class XmtzDtfxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public XmtzDtfxServlet() {
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
			DecimalFormat df = new DecimalFormat("#.0");
			String xmid = request.getParameter("xmid");
			String ndWhere = "";
			if(xmid!=null&&!xmid.equals("")){
				ndWhere = " and v.xmid = '"+xmid+"' ";
			}
			
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();// 生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			String sql = "";
			String[][] re = null;

			sql = " SELECT v.jhtz,v.tctz,v.gs,v.zxhtj,v.htjs FROM VIEW_ZJGL_TZJK v where 1=1 and v.tzlx='zj' " + ndWhere;
			re = DBUtil.querySql(conn, sql);
			String[] color = new String[]{"19bc9c","3398db","9b59b6","34485e","f1c40e"}; 
			// add data
			sb.append("<chart plotGradientColor = ''  rotateYAxisName='0' baseFontSize='11'  "
							+ "bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13' showValues='0' showBorder='0' "
							+ "showLegend= '0' shownames='1' canvasBorderAlpha='100' showAlternateHGridColor='0' "
							+ "showYAxisValues='1'  formatNumberScale='0' yaxisname='百万'  "
							+ "divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' bgColor='#FFFFFF'"
							+ "showAlternateHGridColor='0' showPlotBorder='0' baseFont='微软雅黑'>");
			if(re!=null&&re[0]!=null){
				for (int i = 0; i < re[0].length; i++) {
					
					String label = "";
					if(i==0){
						label = "计财";
					}else if(i==1){
						label = "统筹";
					}else if(i==2){
						label = "概算";
					}else if(i==3){
						label = "合同价";
					}else if(i==4){
						label = "结算";
					}
					float text = 0;
					if(re[0][i]!=null&&!"".equals(re[0][i])){
						text = Float.parseFloat(re[0][i])/1000000;
					}
					sb.append("<set color='"+color[i]+"' label=\"" + label + "\" value=\"" + df.format(text) + "\" hoverText=\""+ Pub.MoneyFormat(df.format(text))+"百万元\"/>");
				}
			}
			sb.append("</chart>");
			out = response.getWriter();
			out.println(sb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String> getMonthList() {
		List<String> months = new ArrayList<String>();
		Date d = new Date();
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM");
		for (int i = 5; i >= 0; i--) {
			gc.setTime(d);
			gc.add(2, -i);
			months.add(sf.format(gc));
		}
		return months;
	}
}
