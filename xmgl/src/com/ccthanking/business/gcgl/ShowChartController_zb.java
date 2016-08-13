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
@RequestMapping("/ShowChart/ShowChartController_zb")
public class ShowChartController_zb {

	// 单系列图表的数据源
	@RequestMapping(params = "single_chartData_zb")
	@ResponseBody

	public void single_chartData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		  	response.setContentType("text/html;charset=gbk");  
			PrintWriter out = response.getWriter();
			StringBuffer sb = new StringBuffer();//生成的数据xml字符串
			Connection conn = DBUtil.getConnection();
			String deptId = request.getParameter("xmglgs");
			String str = "";
			if (!Pub.empty(deptId)) {
				str = "and zb.xmglgs='" + deptId + "'";
			}
			String getnd = request.getParameter("nd");
			if(!Pub.empty(getnd)){
				getnd="  and tcjh.nd='"+getnd+"'";
			}
			String[][] lengthArray = null;
			String[][] valueArray = null;
			String[] strText = null;
			//处理正常数据时是否对单柱数据进行处理的标识数组，默认所有数据中无单柱情况，此时值为-1，
			//当是单柱是对的数组值将更新为单柱所在月份的索引值
			int [] flag_array={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};//标识数组
			int[]nums={0,1,2,3,4,5,6,7,8,9};
			String[] colorArray = { "F1c40e", "19bc9c", "9b59b6", "3398db","34485e" };
			try {
				StringBuffer sbSql1 = new StringBuffer();
				sbSql1.append(" select cd,ayf,sum_bz from (select dic_code ayf from FS_DIC_TREE t where parent_id = '9000000000127'  and sfyx = '1') a");
				sbSql1.append(" left join (select count(yf) cd, substr(yf, 6, 2) byf,sum(sum_bz) sum_bz  from (select sum(ZJLBZ) sum_bz, to_char(kssj, 'yyyy-mm') yf   from GC_XMGLGS_ZBB zb left join gc_jh_sj tcjh  on zb.jhsjid=tcjh.gc_jh_sj_id where  zb.sfyx=1 "
								+ str + getnd
								+ " group by kssj,jssj) group by yf  order by yf asc) b");
				sbSql1.append(" on  a.ayf = b.byf order by ayf asc");

				String sql1 = sbSql1.toString();
				lengthArray = DBUtil.querySql(conn, sql1);

				StringBuffer sbSql2 = new StringBuffer();
				sbSql2.append("select sum (ZJLBZ) sum ,to_char(kssj,'yyyy-mm') yf,kssj,jssj  from GC_XMGLGS_ZBB zb left join gc_jh_sj tcjh on zb.jhsjid=tcjh.gc_jh_sj_id where zb.sfyx=1 and tcjh.sfyx=1 "
						+  str+ getnd+ "  group by kssj,jssj  order by kssj asc");
				String sql2 = sbSql2.toString();
				valueArray = DBUtil.querySql(conn, sql2);
				
				
				if (Pub.emptyArray(lengthArray)) {
					strText = new String[lengthArray.length];
					for (int i = 0; i < lengthArray.length; i++) {
						if (!Pub.empty(lengthArray[i][0])) {
							strText[i] = Pub.NumberAddDec(Pub
									.NumberToThousand(lengthArray[i][0]));
						}
					}
				}
				if (!Pub.emptyArray(lengthArray) && !Pub.emptyArray(valueArray)) {
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
				
			if (lengthArray != null && valueArray != null) {	
				sb.append("<chart plotGradientColor = '' yaxisname='千万'   outCnvBaseFont='Microsoft YaHei'  showPlotBorder='0'  divlineAlpha='20' canvasBorderThickness='0' canvasBorderColor='#DDDDDD' rotateYAxisName='0' baseFontSize='13'  bgColor='#FFFFFF' labelDisplay='WRAP' outCnvbaseFontSize='13' showValues='1' showBorder='0' showLegend= '0' shownames='1' canvasBorderAlpha='100' showAlternateHGridColor='0' showYAxisValues='1'  formatNumberScale='0' numberSuffix='' >");
						sb.append("<categories font='Microsoft YaHei' fontSize='13' >");
							sb.append("<category label='1月'/>");
							sb.append("<category label='2月'/>");
							sb.append("<category label='3月'/>");
							sb.append("<category label='4月'/>");
							sb.append("<category label='5月'/>");
							sb.append("<category label='6月'/>");
							sb.append("<category label='7月'/>");
							sb.append("<category label='8月'/>");
							sb.append("<category label='9月'/>");
							sb.append("<category label='10月'/>");
							sb.append("<category label='11月'/>");
							sb.append("<category label='12月'/>");
						sb.append("</categories>");
					
						sb.append("<dataset>");
						int len_val=0;
						
						//循环12个月，判断每个月有几周
						for(int s=0;s<12;s++)
						{
							int ll=Integer.parseInt(Pub.empty(lengthArray[s][0]) ? "0": lengthArray[s][0]);
							len_val=len_val+ll;
							//判断该月是只有1周，如果有一周做单个柱只显示一个值的处理
							if(ll==1)
							{
								sb.append("<dataset showValues='0' color='" + colorArray[0]+ "'>");
								for(int set1=0; set1<12; set1++)
								{
									String value = valueArray[len_val-1][0];
									if(set1==s && !Pub.empty(value))
									{
										String ksrq=valueArray[len_val-1][2].substring(5, 7)+"月"+valueArray[len_val-1][2].substring(8,10)+"日";
										String jsrq=valueArray[len_val-1][3].substring(5, 7)+"月"+valueArray[len_val-1][3].substring(8,10)+"日";
										/*String value = valueArray[len_val-1][0];
										String money = "";
										if(Pub.empty(value)){
											value = "0";
											money = "0";
										}else{
											money = String.valueOf(MathTool.div(Double.parseDouble(value), 1000, 1));
										}*/
										sb.append("<set toolText='【"+ksrq+"—"+jsrq+"】,"+Pub.MoneyFormat(MathTool.mul(value, "10000"))+"元' value='" +MathTool.div(Double.parseDouble(value), 1000, 1) + "'/>");
									} 
									else
									{										
										sb.append("<set toolText='0元' value=''/>");
									}		
								}
								sb.append("</dataset>");
								flag_array[s]=s;
							}
							else
							{
								//如果大于1周
								if(ll>1)
								{
									//循环判断本月每周是否都有值
									for(int value=1;value<=ll;value++)
									{
										//lengthArray[s][2]是每个月周计量合计，用该值和每周进行对比，如果相等说明本月有空值的周
										//做单个柱只显示一个值的处理
										if(valueArray[len_val-value][0].equals(lengthArray[s][2]))
										{
											sb.append("<dataset showValues='0' color='" + colorArray[ll-value]+ "'>");
											for(int set2=0; set2<12; set2++)
											{
												if(set2==s&&!Pub.empty(valueArray[len_val-value][0]))
												{
													String ksrq=valueArray[len_val-value][2].substring(5, 7)+"月"+valueArray[len_val-value][2].substring(8,10)+"日";
													String jsrq=valueArray[len_val-value][3].substring(5, 7)+"月"+valueArray[len_val-value][3].substring(8,10)+"日";
													sb.append("<set toolText='【"+ksrq+"—"+jsrq+"】,"+Pub.MoneyFormat(MathTool.mul(valueArray[len_val-value][0], "10000"))+"元' value='" + MathTool.div(Double.parseDouble(valueArray[len_val-value][0]), 1000, 1)+ "'/>");
												}
												else
												{										
													sb.append("<set toolText='0元' value=''/>");
												}		
											}
											sb.append("</dataset>");
											flag_array[s]=s;
											break;
										}	
									}	
								}	
							}	
						}					
						
						//处理正常数据
						for (int i = 0; i < 5; i++) {
							sb.append("<dataset color='" + colorArray[i]+ "'>");					
							int m = -1;
							for (int l = 0; l < valueArray.length; l = l+ Integer.parseInt(Pub.empty(lengthArray[m][0]) ? "0": lengthArray[m][0])) {
								m++;
								if (m==flag_array[m]||Pub.empty(lengthArray[m][0])|| Integer.parseInt(lengthArray[m][0]) <= i) {
									sb.append("<set toolText='0元' value=''/>");
								} else {
									String ksrq=valueArray[l + i][2].substring(5, 7)+"月"+valueArray[l + i][2].substring(8,10)+"日";
									String jsrq=valueArray[l + i][3].substring(5, 7)+"月"+valueArray[l + i][3].substring(8,10)+"日";
									if(Pub.empty(valueArray[l + i][0]))
									{
										sb.append("<set toolText='【"+ksrq+"—"+jsrq+"】,0元' value=''/>");
									}
									else
									{
										sb.append("<set toolText='【"+ksrq+"—"+jsrq+"】,"+Pub.MoneyFormat(MathTool.mul(valueArray[l + i][0], "10000"))+"元' value='" + MathTool.div(Double.parseDouble(valueArray[l+i][0]), 1000, 1)+ "'/>");										
									}	
									
								}
							}
							sb.append("</dataset>");
						}
						sb.append("</dataset>");					
					sb.append("</chart>");
				}
			out = response.getWriter();  
			out.println(sb);  
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
