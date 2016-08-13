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
public class XmglgsxmslServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmglgsxmslServlet() {
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
			String getsql = "select sl, bmjc from (select sl,bmjc,decode(sl, 0, "+nd+", nd) nd, decode(sl, 0, 1, sfyx) sfyx from (select count(t.xmglgs) sl, c.bmjc, a.nd, a.sfyx from GC_TCJH_XMXDK t, FS_ORG_DEPT c, GC_JH_SJ a where xmbs='0' and c.row_id = t.xmglgs(+) and c.extend1 = '1'  and t.gc_tcjh_xmxdk_id = a.xmid(+) group by c.row_id, c.bmjc, a.nd, a.sfyx)) where nd = '"+nd+"' and sfyx = '1' order by bmjc";
			String[][] re = null;
			try {
				re  = DBUtil.querySql(conn, getsql);
				if(re == null) {
					
				}else{
/*					dwmc = new String[re.length];
					dwsl = new String[re.length];
					for(int i=0;i<re.length;i++){
						dwmc[i] = re[i][1];
						dwsl[i] = re[i][0];
					}*/
				}
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
			sb.append("<chart use3DLighting='0' placeValuesInside='0' showvalues = '1' showShadow='0' plotGradientColor = '' baseFont='微软雅黑' baseFontSize='12' bgColor=\"#FFFFFF\" baseFontSize=\"13\" showBorder=\"0\" xAxisName=\"公司名称\" yAxisName=\"项目数量\"  palette=\"2\" enableSmartLabels='1' maxColWidth ='30' divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' bgColor='#FFFFFF' enableRotation='1' showAlternateHGridColor='0'>");		
			if(re != null){
				for(int i=0;i< re.length;i++)
				{
					sb.append("<set color=\""+ChartUtil.chartColor2+"\" label=\""+re[i][1]+"\" value=\""+re[i][0]+"\" />");
				}
			}else{
				sb.append("<set label=\" \" value=\" \" />");
			}
			sb.append("</chart>");
			//add data
			/*byte[] utf8Bom =  new byte[]{(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
			String utf8BomStr = new String(utf8Bom,"UTF-8");
			utf8BomStr+=sb.toString();*/
			
			out = response.getWriter();  
			out.println(sb);  
			/*out.print(utf8BomStr);
			out.flush();
			out.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	  
  }
}
