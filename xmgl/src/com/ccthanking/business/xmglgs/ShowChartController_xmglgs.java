package com.ccthanking.business.xmglgs;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.MathTool;
import com.ccthanking.framework.util.Pub;

@Controller
@RequestMapping("/ShowChart/ShowChartController_xmglgs")
public class ShowChartController_xmglgs {


	//单系列图表的数据源
	@RequestMapping(params="single_chartData_jl")
	@ResponseBody
	public void single_chartData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {	  
			  	response.setContentType("text/html;charset=gbk");  
			  	Connection conn = DBUtil.getConnection();
			  	String deptId = request.getParameter("xmglgs");
			  	String getnd = request.getParameter("nd");
			  	String depStr = "";
			  	if(!Pub.empty(deptId)){
			  		depStr = " and xmglgs='"+deptId+"'  ";
			  	}
			  	
				PrintWriter out = response.getWriter();
				StringBuffer sb = new StringBuffer();//生成的数据xml字符串				
				String[] strText = null;
				if(!Pub.empty(getnd)){
					getnd="  and nd='"+getnd+"'";
				}
				//查询出各个月计量审定值
				String sql="select sum(DYJLSDZ) sum_je ,to_char(jlyf,'mm') mouth   from gc_xmglgs_jlb where sfyx='1'"+depStr+getnd+"  group by to_char(jlyf,'mm')";
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
				int count = 0;
				try {
					re  = DBUtil.querySql(conn, getsql);	
					if(Pub.emptyArray(re)){
						strText= new String[re.length];
						for(int i=0;i<re.length;i++){
							if(Pub.empty(re[i][0].toString())){
								strText[i] = "";
								count++;
							}else{
								strText[i]=Pub.NumberToThousand(re[i][0]);
							}
						}						
					}
					if(count == re.length) {
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
					//                                       	 									名称宽度																			                                         是否展示边框									隐藏画布边框			        取消水平线之间的颜色交替显示		隐藏Y轴值			隐藏横线			取消格式化处理	     添加后缀
					sb.append("<chart plotGradientColor = ''  yAxisMaxValue='"+maxY+"' showPlotBorder='0' showValues='1'  divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' rotateYAxisName='0' baseFontSize='13'  bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13'  showBorder='0' showLegend= '0' shownames='1' canvasBorderAlpha='100' showAlternateHGridColor='0' showYAxisValues='1'  formatNumberScale='0' numberSuffix='' yaxisname='千万'   outCnvBaseFont='Microsoft YaHei'  >");
					for(int i=0;i<re.length;i++)
					{
						if(Pub.empty(re[i][0]))
						{
							sb.append("<set color='3398db' label='"+(i+1)+"月' toolText='"+strText[i]+"' value='' />");
						}
						else
						{
							sb.append("<set color='3398db' showValues='1' label='"+(i+1)+"月' toolText='"+strText[i]+"元' value='"+MathTool.div(Double.parseDouble(re[i][0]), 10000000, 1)+"' />");													
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
