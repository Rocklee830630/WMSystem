package com.ccthanking.business.gcgl;

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
@RequestMapping("/ShowChart/ShowChartController_gcb")

public class ShowChartController_gcb {


	//单系列图表的数据源
	@RequestMapping(params="single_chartData_gcb_jl")
	@ResponseBody
	public void single_chartData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {	  
			  	response.setContentType("text/html;charset=gbk");  
			  	Connection conn = DBUtil.getConnection();			  	
				PrintWriter out = response.getWriter();
				String getnd = request.getParameter("nd");
				StringBuffer sb = new StringBuffer();//生成的数据xml字符串
				if(!Pub.empty(getnd)){
					getnd="  and nd='"+getnd+"'";
				}	
				//查询出各个月计量审定值
				String sql="select sum(DYJLSDZ) sum_je ,to_char(jlyf,'mm') mouth   from gc_xmglgs_jlb where sfyx='1'  "+getnd+" group by to_char(jlyf,'mm')";
				//查询最大月计量值
				String max_sql="select max(sum_je) from ("+sql+")";
				String[][] re_max = null;
				re_max  = DBUtil.querySql(conn, max_sql);
				String maxY="5";
				if(null!=re_max&&re_max.length>0&&!Pub.empty(re_max[0][0]))
				{
					double max=Math.ceil(MathTool.div(Double.parseDouble(re_max[0][0])*1.1, 10000000,1)/5)*5;
					 maxY=String.valueOf(max);
					 maxY=maxY.substring(0, maxY.lastIndexOf("."));
				}	

				
				String getsql = "";
				getsql = " select sum_je  from (select dic_code yf from FS_DIC_TREE t where parent_id = '9000000000127' and sfyx='1') dic_mouth  left join"+ 
						 " ("+sql+") jl"+
						 " on dic_mouth.yf=jl.mouth order by yf asc";
				
				String[][] re = null;
				String[] strText = null;
				try {
					re  = DBUtil.querySql(conn, getsql);
					
					if(Pub.emptyArray(re)){
						strText= new String[re.length];
						for(int i=0;i<re.length;i++){
							strText[i]=Pub.NumberAddDec(Pub.NumberToThousand(re[i][0]));
						}
						
					}
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
				boolean b = true;//表示所有数据为空
				for(int x=0;x<re.length;x++){
					if(Pub.empty(re[x][0])){
						
					}else{
						b=false;
						break;
					}
				}
				if(re != null && !b){
					//                                        y轴名称                           名称展示方式（垂直/水平）   名称宽度																			                                         是否展示边框									隐藏画布边框			        取消水平线之间的颜色交替显示		隐藏Y轴值			隐藏横线			取消格式化处理	     添加后缀
					/*sb.append("<chart plotGradientColor = '' xAxisName='月份' yAxisName='元' rotateYAxisName='0' baseFontSize='15'  bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13' showValues='0' showBorder='0' showLegend= '0' shownames='1' canvasBorderAlpha='100' showAlternateHGridColor='0' showYAxisValues='0' divlineAlpha='0'  formatNumberScale='0' numberSuffix='元' >");*/
					sb.append("<chart plotGradientColor = ''   yAxisMaxValue='"+maxY+"' rotateYAxisName='0' baseFontSize='13'  " +
							"bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13' showValues='1' showBorder='0' " +
							"showLegend= '0' shownames='1' canvasBorderAlpha='100' showAlternateHGridColor='0' " +
							"showYAxisValues='1'  formatNumberScale='0' numberSuffix='' yaxisname='千万'   outCnvBaseFont='Microsoft YaHei' " +
							"divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' bgColor='#FFFFFF'" +
							"showAlternateHGridColor='0' showPlotBorder='0'>");
					for(int i=0;i<re.length;i++)
					{
						if(Pub.empty(re[i][0]))
						{
							sb.append("<set color='3398db' label='"+(i+1)+"月' toolText='"+strText[i]+"' value='' />");
						}
						else
						{
							sb.append("<set color='3398db' label='"+(i+1)+"月' toolText='"+strText[i]+"元' value='"+MathTool.div(Double.parseDouble(re[i][0]), 10000000, 1)+"' />");													
						}	
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
