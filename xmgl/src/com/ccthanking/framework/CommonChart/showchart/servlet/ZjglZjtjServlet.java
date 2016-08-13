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
public class ZjglZjtjServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ZjglZjtjServlet() {
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
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();// 生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			String sql = "";
			List<String> months = getMonthList();
			String[][] re = null;

			sql = "select decode(sum(vtj.bmze),null,0,sum(vtj.bmze)) ZBCSQ  , "
					+ " decode(sum(vtj.cwze),null,0,sum(vtj.cwze)) ZCWSH , "
					+ " decode(sum(vtj.jcze),null,0,sum(vtj.jcze)) ZJCSH , "
					+ " decode(sum(vtj.bmjls),null,0,sum(vtj.bmjls)) BMJLS , "
					+ " decode(sum(vtj.cwjls),null,0,sum(vtj.cwjls)) CWJLS , "
					+ " decode(sum(vtj.jcjls),null,0,sum(vtj.jcjls)) JCJLS , "
					+ " vf.MONTH_ "
					+ " FROM VIEW_HALFYEARS vf "
					+ " left join view_bmtqk_tj_month vtj on vf.MONTH_ = vtj.mons "
					+ " group by vf.MONTH_  order by to_date(vf.MONTH_,'yyyy/MM') ";
			re = DBUtil.querySql(conn, sql);
			String[] mons = new String[] { "", "", "" };
			for (int i = 0; i < re.length; i++) {
				mons[0] += "<set color='3398db' value=\"" + df.format(Float.parseFloat(re[i][0]) / 1000000) + "\" hoverText=\"申请金额,共" + re[i][3] + "项("
						+ Pub.MoneyFormat(df.format(Float.parseFloat(re[i][0]) / 1000000)) + "百万元)\" />";
				mons[1] += "<set color='f1c40e' value=\"" + df.format(Float.parseFloat(re[i][1]) / 1000000) + "\" hoverText=\"财务审核金额,共" + re[i][4] + "项("
						+ Pub.MoneyFormat(df.format(Float.parseFloat(re[i][1]) / 1000000)) + "百万元)\" />";
				mons[2] += "<set color='9b59b6' value=\"" + df.format(Float.parseFloat(re[i][2]) / 1000000) + "\" hoverText=\"计财核定金额,共" + re[i][5] + "项("
						+ Pub.MoneyFormat(df.format(Float.parseFloat(re[i][2]) / 1000000)) + "百万元)\" />";
			}
			mons[0] = "<dataset seriesname=\"申请金额\" >" + mons[0] + "</dataset>";
			mons[1] = "<dataset seriesname=\"财务审核金额\" >" + mons[1]
					+ "</dataset>";
			mons[2] = "<dataset seriesname=\"计财核定金额\" >" + mons[2]
					+ "</dataset>";

			// add data
			sb
					.append("<chart plotGradientColor = ''  rotateYAxisName='0' baseFontSize='11'  "
							+ "bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13' showValues='0' showBorder='0' "
							+ "showLegend= '0' shownames='1' canvasBorderAlpha='100' showAlternateHGridColor='0' "
							+ "showYAxisValues='1'  formatNumberScale='0' yaxisname='百万' yAxisMaxValue='100' "
							+ "divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' bgColor='#FFFFFF'"
							+ "showAlternateHGridColor='0' showPlotBorder='0' baseFont='微软雅黑'>");
			sb.append("<categories>");
			for (String month : months) {
				sb.append("<category label=\"" + month + "\"/>");

			}
			sb.append("</categories>");
			sb.append(mons[0]).append(mons[1]).append(mons[2]);
			sb.append("</chart>");
			// add data
			/*
			 * byte[] utf8Bom = new byte[]{(byte) 0xef, (byte) 0xbb, (byte)
			 * 0xbf}; String utf8BomStr = new String(utf8Bom,"UTF-8");
			 * utf8BomStr+=sb.toString();
			 */

			out = response.getWriter();
			out.println(sb);
			/*
			 * out.print(utf8BomStr); out.flush(); out.close();
			 */
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
