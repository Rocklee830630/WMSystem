package com.ccthanking.framework.common;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.util.Pub;

@Controller
@RequestMapping("/ShowChart/ShowChartController")
public class ShowChartController {

	// 单系列图表的数据源
	@RequestMapping(params = "single_chartData")
	@ResponseBody
	public void single_chartData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			response.setContentType("text/html;charset=gbk");
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();// 生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			String nd = request.getParameter("nd");
			String zxqk = request.getParameter("zxqk");
			if (Pub.empty(zxqk)) {
				zxqk = "";
			}
			// 手续情况包括：可研批复，划拔决定书，工程规划许可证，施工许可
			// 设计情况包括：初步设计批复，拆迁图，排迁图，施工图
			// 造价情况包括：提报价，财审
			// 招标情况包括：监理单位，施工单位
			// 征拆情况包括：征拆
			// 排迁情况包括：排迁
			// 开工情况包括：开工
			// 完工情况包括：完工

			String getsql = "";
			String kypf = "";
			if ("sxqk".equals(zxqk)) {// 手续情况
				getsql = "select (select count(*) from GC_JH_SJ t where t.nd = '"+ nd + "' and t.sfyx = '1' and ISKYPF= '1' and kypf_SJ is not null) kypf,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISKYPF= '1' and kypf is not null and kypf_sj is null) kywbl,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISHPJDS= '1' and HPJDS_SJ is not null) HPJDS,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISHPJDS= '1' and HPJDS is not null and HPJDS_SJ is null) HPJDSwbl,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISGCXKZ= '1' and GCXKZ_SJ is not null) GCXKZ,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISGCXKZ= '1' and GCXKZ is not null and GCXKZ_SJ is null) GCXKZWBL,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISSGXK= '1' and SGXK_SJ is not null) SGXK,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISSGXK= '1' and SGXK is not null and SGXK_SJ is null) SGXKWBL from dual";
			} else if ("sjqk".equals(zxqk)) {// 设计情况
				getsql = "select (select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISCBSJPF= '1' and CBSJPF_SJ is not null) CBSJPF,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISCBSJPF= '1' and CBSJPF is not null and CBSJPF_sj is null) CBSJPFwbl,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISCQT= '1' and CQT_SJ is not null) CQT,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISCQT= '1' and CQT is not null and CQT_SJ is null) CQTwbl,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISPQT= '1' and PQT_SJ is not null) PQT,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISPQT= '1' and PQT is not null and PQT_SJ is null) PQTWBL,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISSGT= '1' and SGT_SJ is not null) SGT, "
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISSGT= '1' and SGT is not null and SGT_SJ is null) SGTWBL from dual";
			} else if ("zjqk".equals(zxqk)) {// 造价情况
				getsql = "select (select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISTBJ= '1' and TBJ_SJ is not null) TBJ,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISTBJ= '1' and TBJ is not null and TBJ_sj is null) TBJwbl,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISCS= '1' and CS_SJ is not null) CS,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISCS= '1' and CS is not null and CS_SJ is null) CSwbl from dual";
			} else if ("zbqk".equals(zxqk)) {// 招标情况
				getsql = "select (select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISJLDW= '1' and JLDW_SJ is not null) JLDW,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISJLDW= '1' and JLDW is not null and JLDW_sj is null) JLDWwbl,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISSGDW= '1' and SGDW_SJ is not null) SGDW,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISSGDW= '1' and SGDW is not null and SGDW_SJ is null) SGDWwbl from dual";
			} else if ("zcqk".equals(zxqk)) {// 征拆情况
				getsql = "select (select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISZC= '1' and ZC_SJ is not null) ZC,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISZC= '1' and ZC_SJ is not null and ZC_sj is null) ZCwbl from dual";
			} else if ("pqqk".equals(zxqk)) {// 排迁情况
				getsql = "select (select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISPQ= '1' and PQ_SJ is not null) PQ,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' and ISPQ= '1' and PQ_SJ is not null and PQ_sj is null) PQwbl from dual";
			} else if ("kgqk".equals(zxqk)) {// 开工情况
				getsql = "select (select count(*) from GC_JH_SJ t where t.nd = '"+ nd + "' and t.sfyx = '1'  and " +
						"((ISKGSJ = '1' AND KGSJ_SJ is not null) OR (ISKGSJ = '0'))) KGSJ,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"+ nd + "' and t.sfyx = '1' and " +
						"ISKGSJ = '1' AND KGSJ is not null and KGSJ_sj is null) KGSJwbl from dual";
			} else if ("wgqk".equals(zxqk)) {// 完工情况
				getsql = "select (select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1'  and ((ISWGSJ = '1' AND WGSJ_SJ is not null) OR (ISWGSJ = '0'))) WGSJ,"
						+ "(select count(*) from GC_JH_SJ t where t.nd = '"
						+ nd
						+ "' and t.sfyx = '1' AND ISWGSJ = '1' AND WGSJ is not null and WGSJ_sj is null) WGSJwbl from dual";

			}

			String[][] re = null;
			try {
				re = DBUtil.querySql(conn, getsql);

				if (re == null) {
					out.println("");
					out.flush();
					out.close();
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.close();
				}
			}

			if (re != null) {

				if (!Pub.empty(zxqk)) {
					if ("sxqk".equals(zxqk)) {// 手续情况
						sb.append("<chart " + "plotGradientColor = '' "
								+ "bgColor='#FFFFFF' " + "baseFontSize='12' "
								+ "showBorder='0' " + "showLegend= '0' "
								+ "shownames='1' " + "showplotborder='0' "
								+ "xaxisname='手续情况' " + "baseFont='微软雅黑' "
								+ "showvalues = '1' " + "divlineAlpha='20' "
								+ "canvasBorderThickness='0' yAxisMaxValue='5'"
								+ "canvasBorderColor='" + ChartUtil.chartColor6
								+ "' " + "showAlternateHGridColor='0'" + ">");
						sb.append("<categories font='微软雅黑' fontSize='12' fontColor='000000'>");
						// 手续情况包括：可研批复，划拔决定书，工程规划许可证，施工许可
						sb.append("<category label='可研批复'/>");
						sb.append("<category label='划拔决定'/>");
						sb.append("<category label='规划许可'/>");
						sb.append("<category label='施工许可'/>");
						sb.append("</categories>");
						sb.append("<dataset>");
						sb.append("<dataset  seriesName='已办理'>");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][0] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][2] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][4] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][6] + "' />");
						sb.append("</dataset>");
						sb.append("<dataset  seriesName='未办理'>");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][1] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][3] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][5] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][7] + "' />");
						sb.append("</dataset>");
						sb.append("</dataset>");
						sb.append("</chart>");
					} else if ("sjqk".equals(zxqk)) {// 设计情况
						sb.append("<chart " + "plotGradientColor = '' "
								+ "bgColor='#FFFFFF' " + "baseFontSize='12' "
								+ "showBorder='0' " + "showLegend= '0' "
								+ "shownames='1' " + "showplotborder='0' "
								+ "showvalues='1' " + "xaxisname='设计情况' "
								+ "baseFont='微软雅黑' " + "divlineAlpha='20' "
								+ "canvasBorderThickness='0' yAxisMaxValue='5'"
								+ "canvasBorderColor='" + ChartUtil.chartColor6
								+ "' " + "showAlternateHGridColor='0'" + ">");
						sb.append("<categories font='微软雅黑' fontSize='12' fontColor='000000'>");
						// 设计情况包括：初步设计批复，拆迁图，排迁图，施工图
						sb.append("<category label='初设批复'/>");
						sb.append("<category label='拆迁图'/>");
						sb.append("<category label='排迁图'/>");
						sb.append("<category label='施工图'/>");
						sb.append("</categories>");
						// 初步设计批复
						sb.append("<dataset>");
						sb.append("<dataset seriesName='已办理' >");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][0] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][2] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][4] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][6] + "' />");
						sb.append("</dataset>");
						sb.append("<dataset seriesName='未办理'>");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][1] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][3] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][5] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][7] + "' />");
						sb.append("</dataset>");
						sb.append("</dataset>");

						sb.append("</chart>");
					} else if ("zjqk".equals(zxqk)) {// 造价情况
						sb.append("<chart " + "plotGradientColor = '' "
								+ "bgColor='#FFFFFF' " + "baseFontSize='12'  "
								+ "showBorder='0' " + "showLegend= '0' "
								+ "shownames='1' " + "showvalues='1'"
								+ "xaxisname='造价情况' " + "baseFont='微软雅黑'"
								+ "showplotborder='0' " + "divlineAlpha='20' "
								+ "canvasBorderThickness='0' yAxisMaxValue='5'"
								+ "canvasBorderColor='" + ChartUtil.chartColor6
								+ "' " + "showAlternateHGridColor='0'" + ">");
						sb.append("<categories font='微软雅黑' fontSize='12' fontColor='000000'>");
						// 造价情况包括：提报价，财审
						sb.append("<category label='提报价'/>");
						sb.append("<category label='财审'/>");
						sb.append("</categories>");
						// 提报价
						sb.append("<dataset>");
						sb.append("<dataset seriesName='已办理'>");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][0] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][2] + "' />");
						sb.append("</dataset>");
						sb.append("<dataset seriesName='未办理'>");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][1] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][3] + "' />");
						sb.append("</dataset>");
						sb.append("</dataset>");

						sb.append("</chart>");
					} else if ("zbqk".equals(zxqk)) {// 招标情况
						sb.append("<chart " + "plotGradientColor = '' "
								+ "bgColor='#FFFFFF' " + "baseFontSize='12' "
								+ "showBorder='0' " + "showLegend= '0' "
								+ "shownames='1' " + "showvalues='1'"
								+ "xaxisname='招标情况' " + "baseFont='微软雅黑' "
								+ "showplotborder='0' " + "divlineAlpha='20' "
								+ "canvasBorderThickness='0' yAxisMaxValue='5'"
								+ "canvasBorderColor='" + ChartUtil.chartColor6
								+ "' " + "showAlternateHGridColor='0'" + ">");
						sb.append("<categories font='微软雅黑' fontSize='12' fontColor='000000'>");
						// 招标情况包括：监理单位，施工单位
						sb.append("<category label='监理单位'/>");
						sb.append("<category label='施工单位'/>");
						sb.append("</categories>");
						// 监理单位
						sb.append("<dataset>");
						sb.append("<dataset seriesName='已办理'>");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][0] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][2] + "' />");
						sb.append("</dataset>");
						sb.append("<dataset seriesName='未办理'>");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][1] + "' />");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][3] + "' />");
						sb.append("</dataset>");
						sb.append("</dataset>");
						sb.append("</chart>");
					} else if ("zcqk".equals(zxqk)) {// 征拆情况
						sb.append("<chart " + "plotGradientColor = '' "
								+ "bgColor='#FFFFFF' " + "baseFontSize='12' "
								+ "showBorder='0' " + "showLegend= '0' "
								+ "showplotborder='0' " + "shownames='1' "
								+ "showvalues='1'" + "xaxisname='征拆情况' "
								+ "baseFont='微软雅黑'" + "divlineAlpha='20' "
								+ "canvasBorderThickness='0' yAxisMaxValue='5'"
								+ "canvasBorderColor='" + ChartUtil.chartColor6
								+ "' " + "showAlternateHGridColor='0'" + ">");
						sb.append("<categories font='微软雅黑' fontSize='12' fontColor='000000'>");
						// 征拆情况包括：征拆
						sb.append("<category label=''/>");
						sb.append("</categories>");
						// 征拆
						sb.append("<dataset>");
						sb.append("<dataset seriesName='征拆计划已办理'>");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][0] + "' />");
						sb.append("</dataset>");
						sb.append("<dataset seriesName='征拆计划未办理'>");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][1] + "' />");
						sb.append("</dataset>");
						sb.append("</dataset>");
						sb.append("</chart>");
					} else if ("pqqk".equals(zxqk)) {// 排迁情况
						sb.append("<chart " + "plotGradientColor = '' "
								+ "bgColor='#FFFFFF' " + "baseFontSize='12' "
								+ "showBorder='0' " + "showLegend= '0' "
								+ "shownames='1' " + "showplotborder='0' "
								+ "showvalues='1'" + "xaxisname='排迁情况' "
								+ "baseFont='微软雅黑'" + "divlineAlpha='20' "
								+ "canvasBorderThickness='0' yAxisMaxValue='5'"
								+ "canvasBorderColor='" + ChartUtil.chartColor6
								+ "' " + "showAlternateHGridColor='0'" + ">");
						sb.append("<categories font='微软雅黑' fontSize='12' fontColor='000000'>");
						// 排迁情况包括：排迁
						sb.append("<category label=''/>");
						sb.append("</categories>");
						sb.append("<dataset>");
						sb.append("<dataset seriesName='排迁计划已办理'>");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][0] + "' />");
						sb.append("</dataset>");
						sb.append("<dataset seriesName='排迁计划未办理'>");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][1] + "' />");
						sb.append("</dataset>");
						sb.append("</dataset>");
						sb.append("</chart>");
					} else if ("kgqk".equals(zxqk)) {// 开工情况
						sb.append("<chart " + "plotGradientColor = '' "
								+ "bgColor='#FFFFFF' " + "baseFontSize='12' "
								+ "showBorder='0' " + "showLegend= '0'"
								+ "shownames='1' " + "showvalues='1'"
								+ "showplotborder='0' " + "xaxisname='开工情况' "
								+ "baseFont='微软雅黑'" + "divlineAlpha='20' "
								+ "canvasBorderThickness='0' yAxisMaxValue='5'"
								+ "canvasBorderColor='" + ChartUtil.chartColor6
								+ "' " + "showAlternateHGridColor='0'" + ">");
						sb.append("<categories font='微软雅黑' fontSize='12' fontColor='000000'>");
						// 开工情况包括：开工
						sb.append("<category label=''/>");
						sb.append("</categories>");
						// 开工
						sb.append("<dataset>");
						sb.append("<dataset seriesName='开工计划已办理'>");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][0] + "' />");
						sb.append("</dataset>");
						sb.append("<dataset seriesName='开工计划未办理'>");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][1] + "' />");
						sb.append("</dataset>");
						sb.append("</dataset>");
						sb.append("</chart>");
					} else if ("wgqk".equals(zxqk)) {// 完工情况
						sb.append("<chart " + "plotGradientColor = '' "
								+ "bgColor='#FFFFFF' " + "baseFontSize='12' "
								+ "showBorder='0' " + "showLegend= '0' "
								+ "shownames='1' " + "showvalues='1'"
								+ "showplotborder='0' " + "xaxisname='完工情况' "
								+ "baseFont='微软雅黑'" + "divlineAlpha='20' "
								+ "canvasBorderThickness='0' yAxisMaxValue='5'"
								+ "canvasBorderColor='" + ChartUtil.chartColor6
								+ "' " + "showAlternateHGridColor='0'" + ">");
						sb.append("<categories font='微软雅黑' fontSize='12' fontColor='000000'>");
						// 完工情况包括：完工
						sb.append("<category label=''/>");
						sb.append("</categories>");
						// 完工
						sb.append("<dataset>");
						sb.append("<dataset seriesName='完工计划已办理'>");
						sb.append("<set color='" + ChartUtil.chartColor2
								+ "' value='" + re[0][0] + "' />");
						sb.append("</dataset>");
						sb.append("<dataset seriesName='完工计划未办理'>");
						sb.append("<set color='" + ChartUtil.chartColor6
								+ "' value='" + re[0][1] + "' />");
						sb.append("</dataset>");
						sb.append("</dataset>");
						sb.append("</chart>");
					}
				}

			}

			out = response.getWriter();
			out.println(sb);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}