<%@page import="com.mysql.jdbc.PreparedStatement"%>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ taglib uri="/tld/base.tld" prefix="app"%>
	<%@ page import="com.ccthanking.framework.util.*"%>
	<%@ page import="com.ccthanking.framework.common.*"%>
	<%@ page import="java.lang.StringBuffer"%>
	<%@ page import="java.util.*"%>
	<%@ page import="com.ccthanking.framework.Globals"%>
	<%@ page import="com.ccthanking.framework.plugin.AppInit"%>
	<%@ page import="java.sql.Connection"%>
	<%@ page import="java.text.DateFormat"%>
	<%@ page import="java.text.SimpleDateFormat"%>
	<%
	Connection conn = DBUtil.getConnection();//定义连接
	StringBuffer sbSql = null;//sql语句字符串
	String sql = "";//查询参数字符串
	String nd = request.getParameter("nd");
	if(nd==null||nd.equals("")){
		nd=Pub.getDate("yyyy", new Date());;
	}
%>	
<script type="text/javascript">

//查看合同信息
function loadHtxx(nd) {
	$(window).manhuaDialog({"title":"招投标合同部>已签订合同","type":"text","content":"${pageContext.request.contextPath }/jsp/business/htgl/htxx.jsp?nd="+nd+"&htzt=0&htlx=","modal":"1"});
}
</script>
<fieldset class="b_ddd">  
<legend>累计签订情况</legend>
	<table class="table table-striped"
		style="height: 50%">
		<tr>
			<th class="text-center">
				累计签订
			</th>
			<%
          sbSql = new StringBuffer();
	          sbSql.append("SELECT decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj)), COUNT(*) , "
	        		  +" decode(SUM(ghh.zwczf), NULL,0, SUM(ghh.zwczf)), "
	        		  +" decode(SUM(ghh.zhtzf), NULL,0, SUM(ghh.zhtzf)), "
	        		  +" round(decode(decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj)),0,0,decode(SUM(ghh.zwczf), NULL,0, SUM(ghh.zwczf))/decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj))),2)*100, "
	        		  +" round(decode(decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj)),0,0,decode(SUM(ghh.zhtzf), NULL,0, SUM(ghh.zhtzf))/decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj))),2)*100 "
	        		  +" FROM gc_htgl_ht ghh WHERE ghh.htzt>0 and ghh.sfyx ='1'");
	          sql = sbSql.toString();
	          String[][] ljqdResult = DBUtil.query(conn, sql);
	          int ljqdCount = 0;//总数
	          String ljqdSum = "0";
	          String ztz = "0";
	          String zzf = "0";
	          String ztzb = "0";
	          String zzfb = "0";
	          if(Pub.emptyArray(ljqdResult)){
	          		ljqdSum = Pub.NumberToThousand(ljqdResult[0][0].toString());
	          		if(!Pub.empty(ljqdSum)){
					    ljqdSum = Pub.NumberAddDec(ljqdSum);
					    
					}
	             	ljqdCount = Integer.parseInt(ljqdResult[0][1].toString());
	             	ztz = Pub.NumberToThousand(ljqdResult[0][2].toString());
	             	if(!Pub.empty(ztz)){
	             		ztz = Pub.NumberAddDec(ztz);
					    
					}
	             	zzf = Pub.NumberToThousand(ljqdResult[0][3].toString());
	             	if(!Pub.empty(zzf)){
	             		zzf = Pub.NumberAddDec(zzf);
					    
					}
	             	ztzb = Pub.NumberToThousand(ljqdResult[0][4].toString());
	  	            zzfb = Pub.NumberToThousand(ljqdResult[0][5].toString());
	          }
      	%>
			<td><%=ljqdSum %><b>元(共</b><a href="javascript:void(0)" onclick="loadHtxx('')"><%=ljqdCount %></a><b>个)</b>
			</td>
		</tr>
		<tr>
			<th class="text-center">完成投资
			</th>
			
			<td><%=ztz %><b>元(</b><%=ztzb %><b>%)</b>
			</td>
		</tr>
		<tr>
			<th class="text-center">实际支付
			</th>
				
			<td>
				<%=zzf %>
				<b>元(</b><%=zzfb %><b>%)</b>
			</td>
		</tr>
		</table>
			</fieldset>
		<fieldset class="b_ddd">  
		
		<legend>年度签订情况</legend>
		<table class="table table-striped"
		style="height: 50%">
		<tr>
			<th id="gcqdlist" class="text-center">
				年度签订
			</th>
			<%
				 sbSql = new StringBuffer();
		          sbSql.append("SELECT decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj)), COUNT(*) ,"
		        		  +" decode(SUM(ghh.zwczf), NULL,0, SUM(ghh.zwczf)), "
		        		  +" decode(SUM(ghh.zhtzf), NULL,0, SUM(ghh.zhtzf)), "
		        		  +" round(decode(decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj)),0,0,decode(SUM(ghh.zwczf), NULL,0, SUM(ghh.zwczf))/decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj))),2)*100, "
		        		  +" round(decode(decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj)),0,0,decode(SUM(ghh.zhtzf), NULL,0, SUM(ghh.zhtzf))/decode(SUM(ghh.zhtqdj), NULL,0, SUM(ghh.zhtqdj))),2)*100 "
		        		  +" FROM gc_htgl_ht ghh WHERE ghh.htzt>0 and ghh.qdnf='"+nd+"' AND ghh.sfyx ='1'");
		          sql = sbSql.toString();
		          String[][] ljqdResult2 = DBUtil.query(conn, sql);
		          int ndljqdCount = 0;//总数
		          String ndljqdSum = "0";
		          String ndztz = "0";
		          String ndzzf = "0";
		          String ndztzb = "0";
		          String ndzzfb = "0";
		          if(Pub.emptyArray(ljqdResult2)){
		          		ndljqdSum = Pub.NumberToThousand(ljqdResult2[0][0].toString());
		          		if(!Pub.empty(ndljqdSum)){
						    ndljqdSum = Pub.NumberAddDec(ndljqdSum);
						    
						}
		          		ndljqdCount = Integer.parseInt(ljqdResult2[0][1].toString());
		             	ndztz = Pub.NumberToThousand(ljqdResult2[0][2].toString());
		             	if(!Pub.empty(ndztz)){
		             		ndztz = Pub.NumberAddDec(ndztz);
						    
						}
		             	ndzzf = Pub.NumberToThousand(ljqdResult2[0][3].toString());
		             	if(!Pub.empty(ndzzf)){
		             		ndzzf = Pub.NumberAddDec(ndzzf);
						    
						}
		             	ndztzb = Pub.NumberToThousand(ljqdResult2[0][4].toString());
		  	            ndzzfb = Pub.NumberToThousand(ljqdResult2[0][5].toString());
		          }
			%>
			<td>
			<%=ndljqdSum %>
				<b>元(共</b><a href="javascript:void(0)" onclick="loadHtxx('<%=nd %>')"><%=ndljqdCount %></a>
				<b>个)</b>
			</td>
		</tr>
		<tr>
			<th class="text-center">完成投资
			</th>
			<td>
				<%=ndztz %>
				<b>元(</b><%=ndztzb %>
				<b>%)</b>
			</td>
		</tr>
		<tr>
			<th class="text-center">实际支付
			</th>
			<td>
				<%=ndzzf %>
				<b>元(</b><%=ndzzfb %>
				<b>%)</b>
			</td>
		</tr>
	</table>
	</fieldset>