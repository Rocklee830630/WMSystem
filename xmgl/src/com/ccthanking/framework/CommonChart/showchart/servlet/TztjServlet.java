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
public class TztjServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TztjServlet() {
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
			//DecimalFormat df = new DecimalFormat("#.0");
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();// 生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			String nd = request.getParameter("nd");
			String xmid = request.getParameter("xmid");
			String sql = "";
			String[][] re = null;

			

			// add data
			sb.append("<chart plotGradientColor = ''  rotateYAxisName='0' baseFontSize='11'  "
							+ "bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13' showValues='0' showBorder='0' "
							+ "showLegend= '0' shownames='1' canvasBorderAlpha='100' showAlternateHGridColor='0' "
							+ "showYAxisValues='1'  formatNumberScale='0' yaxisname='百万' yAxisMaxValue='100'  "
							+ "divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' bgColor='#FFFFFF' "
							+ "showAlternateHGridColor='0' showPlotBorder='0' baseFont='微软雅黑'>");
			sb.append("<categories><category label='计财计划'></category><category label='统筹计划'></category><category label='拦标价'></category><category label='最新合同价'></category><category label='结算'></category></categories>");
			sql = "select * from (select 1 sort_num,'计财计划' tname,t1.jhztze*10000 jhztze  from gc_tcjh_xmcbk t1 left join gc_tcjh_xmxdk t2 on t2.xmcbk_id = t1.gc_tcjh_xmcbk_id where t2.gc_tcjh_xmxdk_id = 'xxxxxmidxxxx' " 
				+" union select 2 sort_num,'统筹计划' tname,t3.jhztze jhztze from gc_tcjh_xmxdk t3 where t3.gc_tcjh_xmxdk_id = 'xxxxxmidxxxx' "
				+" union select 4 sort_num,'最新合同价' tname,(select decode(sum(ghh.zxhtj),null,0,sum(zxhtj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ) hj from dual "  
				+" union select 3 sort_num,'拦标价' tname,(select decode(sum(gz.cssdz),null,0,sum(gz.cssdz)) from gc_htgl_htsj ghh left join GC_ZJB_LBJB gz on gz.jhsjid = ghh.jhsjid left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ) hj from dual "
				+" union select 5 sort_num,'结算' tname,(select decode(sum(ghh.htjsj),null,0,sum(ghh.htjsj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ) hj from dual ) a order by a.sort_num";
			sql = sql.replace("xxxxxmidxxxx", xmid);
			re = DBUtil.query(conn, sql);
			sb.append("<dataset seriesName=\"投资情况\" color=\"1D8BD1\" anchorBorderColor=\"1D8BD1\" anchorBgColor=\"1D8BD1\">");
			for(int i=0;i<re.length;i++){
				sb.append("<set value='"+Float.parseFloat(re[i][2])/1000000+"' text='"+re[i][1]+"' hoverText='"+re[i][1]+"("+(Float.parseFloat(re[i][2])/1000000)+"百万元)"+"'/>");
			}
			sb.append("</dataset>");
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
