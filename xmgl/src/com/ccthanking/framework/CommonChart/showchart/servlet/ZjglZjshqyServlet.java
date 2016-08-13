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
public class ZjglZjshqyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZjglZjshqyServlet() {
        super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=gbk");

            String nd = request.getParameter("nd");
            DecimalFormat df = new DecimalFormat("#.0");
            String ndWhere = "";
            if (nd != null && !nd.equals("")) {
                ndWhere = " and to_char(zj.zfrq,'yyyy')='" + nd + "' ";
            }

            PrintWriter out = response.getWriter();
            StringBuffer sb = new StringBuffer();// 生成的数据xml字符串的chart
            StringBuffer sb2 = new StringBuffer();//数据内容
            Connection conn = DBUtil.getConnection();
            String sql = "";
            String[][] re = null;

            // sql =
            // " select decode(sum(zj.ZFJE),null,0,sum(zj.ZFJE)) je ,COUNT(*) SUMBS,zj.qy from gc_zjgl_zszj_xmzszjqk zj where 1=1 "
            // + ndWhere + " group by zj.qy order by zj.qy";
            sql = "SELECT  (SELECT decode(sum(zj.ZFJE), null, 0, sum(zj.ZFJE)) FROM gc_zjgl_zszj_xmzszjqk zj WHERE zj.qy=q.dic_code xxxxxxxxxxxxx) AS je, "
                    + "(SELECT  COUNT(*) FROM gc_zjgl_zszj_xmzszjqk zj WHERE zj.qy=q.dic_code xxxxxxxxxxxxx) SUMBS, q.dic_code AS qy , q.dic_value  FROM VIEW_ZJGL_BMGK_Qy q  ";
            sql = sql.replaceAll("xxxxxxxxxxxxx", ndWhere);
            re = DBUtil.querySql(conn, sql);

            // add data
            sb.append("<chart plotGradientColor = ''  rotateYAxisName='0' baseFontSize='11'  " +
							"bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13' showValues='0' showBorder='0' " +
							"showLegend= '0' shownames='1' canvasBorderAlpha='100' showAlternateHGridColor='0' " +
							"showYAxisValues='1'  formatNumberScale='0' yaxisname='百万' " +
							"divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' bgColor='#FFFFFF'" +
							"showAlternateHGridColor='0' showPlotBorder='0' baseFont='微软雅黑' ");
            boolean noValue = true;
            if (re != null) {
                for (int i = 0; i < re.length; i++) {
                    if (!"".equals(re[i][2])) {
                        // String hoverText = Pub.getDictValueByCode("QY",
                        // re[i][2]) + "," + Float.parseFloat(re[i][0]) / 10000
                        // + "万元(共"
                        String hoverText = re[i][3] + "," + Pub.MoneyFormat(df.format(Float.parseFloat(re[i][0]) / 1000000)) + "百万元(共" + re[i][1] + "笔)";
                        // sb.append("<set label=\"" +
                        // Pub.getDictValueByCode("QY", re[i][2]) +
                        // "\" value=\"" + Float.parseFloat(re[i][0])
                        sb2.append("<set color='3398db' label=\"" + re[i][3] + "\" value=\"" + df.format(Float.parseFloat(re[i][0]) / 1000000) + "\" hoverText=\""
                                + hoverText + "\"/>");
                        if(Float.parseFloat(re[i][0]) / 1000000!=0){
                        	noValue = false;
                        }
                    }

                }
            }
            if(noValue){
            	sb.append(" yAxisMaxValue='100' ");
            }
            sb.append(">");
            sb.append(sb2.toString());
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
