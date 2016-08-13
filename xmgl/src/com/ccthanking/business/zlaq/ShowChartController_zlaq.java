package com.ccthanking.business.zlaq;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.MathTool;
import com.ccthanking.framework.util.Pub;
@Controller
@RequestMapping("/ShowChart/ShowChartController_zlaq")
public class ShowChartController_zlaq {

	//单系列图表的数据源
	@RequestMapping(params="single_chartData_zlaq")
	@ResponseBody
	public void single_chartData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			

			  
		  	response.setContentType("text/html;charset=gbk");  
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();//生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			String nd=request.getParameter("nd");
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
			}
			
			//查询出每个月检查次数
			String sql="select  count(GC_ZLAQ_JCB_ID) sum ,to_char(JCRQ,'mm') mm from GC_ZLAQ_JCB where sfyx='1' "+sqlnd+"  group by to_char(JCRQ,'mm')";
			
			//查询出月最大检查数
			String max_sql="select max(sum) from ("+sql+")";
			String[][] re_max = null;
			re_max  = DBUtil.querySql(conn, max_sql);
			String maxY="5";
			if(null!=re_max&&re_max.length>0&&!Pub.empty(re_max[0][0]))
			{
				 double max=Math.ceil(MathTool.div(Double.parseDouble(re_max[0][0])*1.2, 1,1)/5)*5;
				 maxY=String.valueOf(max);
				 maxY=maxY.substring(0, maxY.lastIndexOf("."));
			}	

			String getsql = "select yf,sum from ( select dic_code yf from FS_DIC_TREE t where parent_id = '9000000000127' and sfyx='1' ) month "+
						" left join ("+sql+") jcb on mm=month.yf order by yf asc";
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
			
			if(re != null){
				sb.append("<chart plotGradientColor = ''  yAxisMaxValue='"+maxY+"'  showPlotBorder='0' baseFontSize='13' divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' showAlternateHGridColor='0' outCnvbaseFont='Microsoft YaHei' rotateYAxisName='0' bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13' showBorder='0' showLegend= '0' shownames='1' >");
				for(int s=0;s<12;s++)
				{
					sb.append("<set color='3398db' label='"+(s+1)+"月'  value='"+re[s][1]+"' />");					
				}	
				sb.append("</chart>");			
			}
				
			
			out = response.getWriter();  
			out.println(sb);  
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
