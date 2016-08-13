package com.ccthanking.framework.CommonChart.showchart.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;

/**
 * Servlet implementation class XmkgXmlServlet
 */
public class DwjhzxqkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DwjhzxqkServlet() {
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
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();//生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			String nd = request.getParameter("nd");
			//获取公司名称
				String dwmc = request.getParameter("dwmc");
				if(Pub.empty(dwmc)){
					dwmc = "";
				}
			   String zxqk = request.getParameter("zxqk");
				if(Pub.empty(zxqk)){
					zxqk = "";
				}
				String getsql = "";
				if(zxqk.equals("kg")){
					 getsql = "select (select count(nvl(to_char(KGSJ_SJ), 0)) from (select c.bmjc, a.KGSJ_SJ,a.iskgsj from GC_TCJH_XMXDK t, GC_JH_SJ a, FS_ORG_DEPT c where t.gc_tcjh_xmxdk_id = a.xmid and c.row_id = t.xmglgs and a.nd = '"+nd+"' and a.sfyx = '1' and c.row_id = '"+dwmc+"') a where (iskgsj = '1' and KGSJ_SJ is not null) or (iskgsj = '0')) as a, " +
							 "(select count(nvl(to_char(KGSJ_SJ), 0)) from (select c.bmjc, a.KGSJ_SJ,a.iskgsj from GC_TCJH_XMXDK t, GC_JH_SJ a, FS_ORG_DEPT c where t.gc_tcjh_xmxdk_id = a.xmid and c.row_id = t.xmglgs and a.nd = '"+nd+"' and a.sfyx = '1' and c.row_id = '"+dwmc+"') a where iskgsj = '1' and KGSJ_SJ is null) as b from dual ";
				
				}else{
					getsql = "select (select count(nvl(to_char(WGSJ_SJ), 0)) from (select c.bmjc, a.WGSJ_SJ,a.iswgsj from GC_TCJH_XMXDK t, GC_JH_SJ a, FS_ORG_DEPT c where t.gc_tcjh_xmxdk_id = a.xmid and c.row_id = t.xmglgs and a.nd = '"+nd+"' and a.sfyx = '1' and c.row_id = '"+dwmc+"') a where (iswgsj = '1' and WGSJ_SJ is not null) or (iswgsj = '0')) as a, " +
							 "(select count(nvl(to_char(WGSJ_SJ), 0)) from (select c.bmjc, a.WGSJ_SJ,a.iswgsj from GC_TCJH_XMXDK t, GC_JH_SJ a, FS_ORG_DEPT c where t.gc_tcjh_xmxdk_id = a.xmid and c.row_id = t.xmglgs and a.nd = '"+nd+"' and a.sfyx = '1' and c.row_id = '"+dwmc+"') a where iswgsj = '1' and WGSJ_SJ is null) as b from dual ";

				}
					
			String[][] re = null;
			
			try {
				re  = DBUtil.querySql(conn, getsql);
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (conn != null) {
					conn.close();
				}
			}
			
			//add data
			if(zxqk.equals("kg"))
			{
				sb.append("<chart use3DLighting='0' showShadow='0' bgColor='#FFFFFF' pieRadius = '40' palette='4' decimals='0' enableSmartLabels='1' enableRotation='1'  showBorder='0'  startingAngle='70' baseFont='微软雅黑' baseFontSize='12'  showFCMenuItem='0'>");
			}else{
				sb.append("<chart use3DLighting='0' showShadow='0' bgColor='#FFFFFF' pieRadius = '40' palette='4' decimals='0' enableSmartLabels='1' enableRotation='1'   showBorder='0' startingAngle='70' baseFont='微软雅黑' baseFontSize='12' showFCMenuItem='0'>");
			}
						
			
			if(re != null){
				for(int i=0;i< re.length;i++)
				{
					if(zxqk.equals("kg"))
					{
						sb.append("<set label=\"开工数\" color='"+ChartUtil.chartColor2+"' value=\""+re[i][0]+"\" />");
						sb.append("<set label=\"未开工数\" color='"+ChartUtil.chartColor6+"' value=\""+re[i][1]+"\" />");
					}else{
						sb.append("<set label=\"完工数\" color='"+ChartUtil.chartColor2+"' value=\""+re[i][0]+"\" />");
						sb.append("<set label=\"未完工数\" color='"+ChartUtil.chartColor6+"' value=\""+re[i][1]+"\" />");
					}

				}
			}else{
				if(zxqk.equals("kg"))
				{
					sb.append("<set label=\"开工数\" color='"+ChartUtil.chartColor2+"' value=\"0\" />");
					sb.append("<set label=\"未开工数\" color='"+ChartUtil.chartColor6+"' value=\"0\" />");
				}else{
					sb.append("<set label=\"完工数\" color='"+ChartUtil.chartColor2+"' value=\"0\" />");
					sb.append("<set label=\"未完工数\" color='"+ChartUtil.chartColor6+"' value=\"0\" />");
				}
			}
			sb.append("</chart>");
			//add data

			
			out = response.getWriter();  
			out.println(sb);  
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	  
  }
}
