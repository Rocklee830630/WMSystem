package com.ccthanking.framework.CommonChart.showchart.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;

/**
 * Servlet implementation class XmkgXmlServlet
 */
public class YdzxqkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YdzxqkServlet() {
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
			String month = 	"";
			String kssj = "";
			String jssj = "";
			String getnd = "";
			if(nd != null)
			{
				getnd = nd.substring(0, 4);
				month = "12";
			}
			int yuefen = 0;
				if(month.substring(0,1).equals("0"))
				{
					yuefen = Integer.parseInt(month.substring(1,2));
				}else{
					yuefen = Integer.parseInt(month.substring(0,2));
				}
				Date pakssj = null;
				Date pajssj = null;
				Date temp = null;
				kssj = getnd+"-01-01";
				String getsql = "";
			//循环月份定时间点为每月20日
				
				sb.append("<chart showvalues='1' caption=\"月度执行情况\" lineThickness=\"1\"  formatNumberScale=\"0\" anchorRadius=\"2\"   showAlternateHGridColor=\"1\" alternateHGridAlpha=\"5\" shadowAlpha=\"40\" labelStep=\"1\" numvdivlines=\"5\" chartRightMargin=\"35\" bgColor=\"#FFFFFF\" showBorder=\"0\" bgAngle=\"270\" bgAlpha=\"10,10\" xAxisName=\"月份\"  yAxisName=\"项目数\" rotateYAxisName='0'  baseFont=\"微软雅黑\" baseFontSize=\"12\" divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' bgColor='#FFFFFF' showAlternateHGridColor='0'>");
				
				//摘要
				sb.append("<categories>");
				for(int i=1;i<=yuefen;i++){

					if(i==1){
						DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				          pakssj =  format1.parse(kssj);
				          pakssj = DateTimeUtil.addMonths(pakssj, -1);
				          pajssj = DateTimeUtil.addMonths(pakssj, 1);
					}else{
						pakssj =   pajssj;
						pajssj = DateTimeUtil.addMonths(pakssj, 1);
					}
						DateFormat df = new SimpleDateFormat("MM");
							String tmpyf = df.format(pajssj).startsWith("0")?df.format(pajssj).substring(1):df.format(pajssj);
							sb.append("<category label=\""+tmpyf+"月\"/>");
								
				}
				sb.append("</categories>");
				//计划完工
				sb.append("<dataset seriesName=\"计划完工\" color=\"1D8BD1\" anchorBorderColor=\"1D8BD1\" anchorBgColor=\"1D8BD1\">");

				for(int i=1;i<=yuefen;i++){

					if(i==1){
						DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						 pakssj =  format1.parse(kssj);
				          pakssj = DateTimeUtil.addMonths(pakssj, -1);
				          pajssj = DateTimeUtil.addMonths(pakssj, 1);
					}else{
						pakssj =   pajssj;
						pajssj = DateTimeUtil.addMonths(pakssj, 1);
					}
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					
						getsql = "select count(wgsj) as jhwg from   GC_JH_SJ t where nd = '"+getnd+"' and sfyx = '1' and iswgsj = '1' and wgsj > to_date('"+df.format(pakssj)+"','yyyy-mm-dd') and wgsj <= to_date('"+df.format(pajssj)+"','yyyy-mm-dd')";
										
					 //
												String[][] re = null;
						
						try {
							re  = DBUtil.querySql(conn, getsql);
							
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
						}finally {
							if (conn != null) {
								conn.close();
							}
						}
						
						//add data
						if(re != null){
								
							sb.append("<set value=\""+re[0][0]+"\" />");
							
						}
				          
						
				}
				sb.append("</dataset>");
			//实际完工
				sb.append("<dataset seriesName=\"实际完工\" color=\"F1683C\" anchorBorderColor=\"F1683C\" anchorBgColor=\"F1683C\">");

				for(int i=1;i<=yuefen;i++){

					if(i==1){
						DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						 pakssj =  format1.parse(kssj);
				          pakssj = DateTimeUtil.addMonths(pakssj, -1);
				          pajssj = DateTimeUtil.addMonths(pakssj, 1);
					}else{
						pakssj =   pajssj;
						pajssj = DateTimeUtil.addMonths(pakssj, 1);
					}
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						getsql = "select count(wgsj_sj) as jhwg from   GC_JH_SJ t where nd = '"+getnd+"' and sfyx = '1' and wgsj_sj > to_date('"+df.format(pakssj)+"','yyyy-mm-dd') and wgsj_sj <= to_date('"+df.format(pajssj)+"','yyyy-mm-dd')";
										
					 //
												String[][] re = null;
						
						try {
							re  = DBUtil.querySql(conn, getsql);
							
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
						}finally {
							if (conn != null) {
								conn.close();
							}
						}
						
						//add data
						if(re != null){
								
							sb.append("<set value=\""+re[0][0]+"\" />");
							
						}
				          
						
				}
				sb.append("</dataset>");
			// add tail
			sb.append("<styles>");	
			sb.append("<definition>");
			sb.append("<style name=\"CaptionFont\" type=\"font\" size=\"12\" />");
			sb.append("</definition>");
			sb.append("<application>");
			sb.append("<apply toObject=\"CAPTION\" styles=\"CaptionFont\" />");
			sb.append("<apply toObject=\"SUBCAPTION\" styles=\"CaptionFont\" />");
			sb.append("</application>");
			sb.append("</styles>");

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
