package com.ccthanking.framework.CommonChart.showchart.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ccthanking.framework.CommonChart.showchart.chart.*;


/**
 * 首页资源分布饼状图
 * <p>2009年7月15日</p>
 * <p>Copyright: S.A.S.(c)2009</p>
 * <p>Company: Sky Art Studio</p>
 * @author Crystal
 * @version 1.1
 */
public class PCDServlet extends HttpServlet{
    public PCDServlet(){
        super();
    }
    public void destroy() {
        super.destroy();
    }
    public void init() throws ServletException {
        //put your code here
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String dt = (String)request.getParameter("dt");
            FusionChartUtil fcu = new FusionChartUtil();
            String[][] datas = new ChartUtil().getResourceAllSum(dt);
            String xmlData =fcu.generatePieXmlInfo(datas[0], datas[1]);
            out.println(xmlData);
            out.flush();
            out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
