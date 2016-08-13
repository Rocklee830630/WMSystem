<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>

<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.common.UserTemplate"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.wsdy.*"%>
<%@ page import="com.ccthanking.common.YwlxManager"%>

<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@page import="java.text.DecimalFormat"%>

<%
	User user = (User) session.getAttribute(Globals.USER_KEY);
	String sjbh = (String) request.getParameter("sjbh");
	String ywlx = (String) request.getParameter("ywlx");
	String isview = (String) request.getParameter("isview");
	if (isview == null)
		isview = "";
	Connection conn = DBUtil.getConnection();
	PubWS pubws = new PubWS();
	String zbrq = "";//指标日期
	String title = "";//标题
	String zbr = "";//主办人
	String[] cwbfzr = null;//财务部负责人
	String[] fgzr = null;//分管主任
	String[] cwzj = null;//财务总监
	String[] zxfzr = null;//中心负责人
	String dwmc = "";
	
	String qklx ="";//请款类型
	QuerySet qrs = null;//查询结果集
	try {
		conn.setAutoCommit(false);
		pubws.getPrintXml(request, response, "14", sjbh, ywlx, user,
				conn, "", "");
		QuerySet qs = DBUtil
				.executeQuery(
						"select  FIELDNAME,DOMAIN_VALUE,WSWH_FLAG,to_char(LRSJ,'YYYY-MM-DD HH24:MI:SS') as hdate,LRRID,LRRXM,DOMAIN_TYPE,CODEPAGE,APPROVEROLE,DOMAIN_STYLE,canedit from ap_process_ws t where ws_template_id = '14' and sjbh = '"
								+ sjbh
								+ "' and ywlx = '"
								+ ywlx
								+ "' and lrsj is not null ", null, conn);
								
								
	
		if (qs.getRowCount() > 0) {
			for (int i = 0; i < qs.getRowCount(); i++) {
				String fieldname = qs.getString(i + 1, "FIELDNAME");
				if ("zbrq".equalsIgnoreCase(fieldname)) {
					zbrq = qs.getString(i + 1, "DOMAIN_VALUE");
				}
				if ("title".equalsIgnoreCase(fieldname)) {
					title = qs.getString(i + 1, "DOMAIN_VALUE");
				}
				if ("zbr".equalsIgnoreCase(fieldname)) {
					zbr = qs.getString(i + 1, "DOMAIN_VALUE");
				}
				if ("cwbfzr".equalsIgnoreCase(fieldname)) {
					cwbfzr = new String[3];
					cwbfzr[0] = qs.getString(i + 1, "DOMAIN_VALUE");
					cwbfzr[1] = qs.getString(i + 1, "HDATE");
					cwbfzr[2] = qs.getString(i + 1, "LRRID");
				}
				if ("fgzr".equalsIgnoreCase(fieldname)) {
					fgzr = new String[3];
					fgzr[0] = qs.getString(i + 1, "DOMAIN_VALUE");
					fgzr[1] = qs.getString(i + 1, "HDATE");
					fgzr[2] = qs.getString(i + 1, "LRRID");
				}
				if ("cwzj".equalsIgnoreCase(fieldname)) {
					cwzj = new String[3];
					cwzj[0] = qs.getString(i + 1, "DOMAIN_VALUE");
					cwzj[1] = qs.getString(i + 1, "HDATE");
					cwzj[2] = qs.getString(i + 1, "LRRID");
				}
				if ("zxfzr".equalsIgnoreCase(fieldname)) {
					zxfzr = new String[3];
					zxfzr[0] = qs.getString(i + 1, "DOMAIN_VALUE");
					zxfzr[1] = qs.getString(i + 1, "HDATE");
					zxfzr[2] = qs.getString(i + 1, "LRRID");
				}
			}
		}

		String sql_dw = "select t.sqdw from gc_zjgl_tqk t where t.sjbh=?";
		String[][] rs1 =  DBUtil.querySql(sql_dw, new Object[]{sjbh});
		dwmc = rs1[0][0];
		dwmc = Pub.getDeptNameByID(dwmc);
		
		//查询请款类型
		//String sql_lx = "select qklx from GC_ZJGL_TQK t where t.sjbh='"+sjbh+"'";
		String sql_lx = "select qklx from GC_ZJGL_TQK t where t.sjbh=?";
		QuerySet  set = DBUtil.executeQuery(sql_lx,new Object[]{sjbh},conn);
		if (set.getRowCount() > 0) {
			qklx = set.getString(1, 1);
		}
		
		//查询请款类型对应的申请表
		String sql_sq = "select * from VIEW_ZJGL_TQK_WS v where v.sjbh=?";
		//String sql_sq = "select * from gc_zjgl_tqkbmmx z,gc_zjgl_tqk t where t.qklx=?";
		qrs = DBUtil.executeQuery(sql_sq,new Object[]{sjbh},conn);

	} catch (Exception e) {
		conn.rollback();
	} finally {
		if (conn != null) {
			conn.close();
		}
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<style type="text/css">
body {
	font-size: 14px;
}

.table2 {
	border-left: #000 solid 1px;
	border-top: #000 solid 1px;
}

.marginBottom15px {
	margin-bottom: 15px;
}

.table2 tr td,.table2 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 1px;
	border-bottom: #000 solid 1px;
}

input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
}
</style>
	<body>
		<script language=JavaScript>
	function printIframe() {
		window.print();
	}
</script>

		<form id="wsTemplate" name="wsTemplate">

			<h2 align="center"><%=title%></h2>
			<div style="width: 1123px; height: 20px; margin: 0 auto;">
				<div style="width: 60%; float: left;">
					申请单位（公章）:<%=dwmc %>
				</div>
				<div style="width: 40%; float: right; text-align: right">
					编制日期：<%=zbrq%>&nbsp;&nbsp;&nbsp;&nbsp;单位：元
				</div>

			</div>

			<!--   下面的table代码根据sjbh获得不同的 请款类别重新生成表格 begin-->


			<table width="1123" border="0" align="center" cellpadding="0"
				cellspacing="0" class="table2">
				<tr>
					<th colspan="4">
						<table width="100%" border="0" cellspacing="0" class="table2"
							cellpadding="0">
							<%
								if(qklx.equals("16")){//工程类
							%>
							<tr>
								<td>序号</td>
								<td>融资主体</td>
								<td>施工单位全称</td>
								<td>项目名称（含起止点）</td>
								<td>合同编号</td>
								<td>合同值</td>
								<td>财审值</td>
								<td>审计值</td>
								<td>监理确认的计量款</td>
								<td>已拨付(含预付款)</td>
								<td>本次申请拨款</td>
								<td>累计拨付(含预付款)</td>
								<td>按计量付款比例（%）</td>
								<td>按合同拨付比例（%）</td>
								<td>按财审拨付比例（%）</td>
								<td>按审计拨付比例（%）</td>
								<td>备注</td>
							</tr>
							<%
									if (qrs.getRowCount() > 0) {
										
										DecimalFormat df = new DecimalFormat("#.00");
										
									    String DWMC = "";
									    String XMMCNR = "";
									    
									    String HTBM = "";
									    String ZXHTJ = "";
									    String CSZ = "";
									    String JZQR = "";
									    String YBF = "";
									    String BCSQ = "";
									    String LJBF = "";
									    String AJLFKB = "";
									    String AHTBFB = "";
									    String BZ = "";
									    String SJZ = "";
									    String CSBL = "0";
									    String SJBL = "0";
									    String RZZT = "";
									    for(int i=0;i<qrs.getRowCount();i++){
									        DWMC= qrs.getString(i+1, "DWMC");
									        if(DWMC==null){
									        	DWMC= "";
									        }
									        RZZT = qrs.getString(i+1, "DICT_NAME");
									        XMMCNR= qrs.getString(i+1, "XMMCNR");
									        HTBM= qrs.getString(i+1, "HTBM");
									        if(HTBM==null)HTBM="";
									        ZXHTJ= qrs.getString(i+1, "ZXHTJ");
									        //ZXHTJ = Pub.NumberAddDec(Pub.MoneyFormat(ZXHTJ));
									        ZXHTJ = Pub.MoneyFormat(ZXHTJ);
									        CSZ= qrs.getString(i+1, "CSZ");
									        if(Float.parseFloat(CSZ)!=0){
									        	CSBL =  df.format((Float.parseFloat(qrs.getString(i+1, "LJBF"))/ Float.parseFloat(CSZ))*100);
									        }
									        CSZ = Pub.MoneyFormat(CSZ);
									        JZQR= qrs.getString(i+1, "JZQR");
									        JZQR = Pub.MoneyFormat(JZQR);
									        YBF= qrs.getString(i+1, "YBF");
									        YBF = Pub.MoneyFormat(YBF);
									        BCSQ= qrs.getString(i+1, "CWSHZ");
									        BCSQ = Pub.MoneyFormat(BCSQ);
									        LJBF= qrs.getString(i+1, "LJBF");
									        LJBF = Pub.MoneyFormat(LJBF);
									        AJLFKB= qrs.getString(i+1, "AJLFKB");
									        AHTBFB= qrs.getString(i+1, "AHTBFB");
									        BZ= qrs.getString(i+1, "BZ");
									        if(BZ==null)BZ="";
									        //查询审计值
									        String sqlSJ = "select decode(sum(g3.Sjsdje),null,0,sum(g3.Sjsdje)) from gc_htgl_htsj g2 ,gc_zjb_jsb g3 where g2.jhsjid = g3.jhsjid and g2.id = '"+qrs.getString(i+1, "HTID")+"'";
									        String[][] res = DBUtil.query(conn,sqlSJ);
									        if(res!=null){
									        	SJZ = res[0][0];
									        	if(Float.parseFloat(SJZ)!=0){
										        	SJBL =  df.format((Float.parseFloat(qrs.getString(i+1, "LJBF"))/ Float.parseFloat(SJZ))*100);
										        }
									        	SJZ = Pub.MoneyFormat(SJZ);
									        }
							%>
							<tr>
								<td><%=i+1 %></td>
								<td><%=RZZT %></td>
								<td><%=DWMC %></td>
								<td><%=XMMCNR %></td>
								<td><%=HTBM == null ? "" : HTBM %></td>
								<td><%=ZXHTJ %></td>
								<td><%=CSZ %></td>
								<td><%=SJZ %></td>
								<td><%=JZQR %></td>
								<td><%=YBF %></td>
								<td><%=BCSQ %></td>
								<td><%=LJBF %></td>
								<td><%=Pub.MoneyFormat(AJLFKB) %></td>
								<td><%=Pub.MoneyFormat(AHTBFB) %></td>
								<td><%=Pub.MoneyFormat(CSBL) %></td>
								<td><%=Pub.MoneyFormat(SJBL) %></td>
								<td><%=BZ %></td>
							</tr>
							<%
									    }
									}
								}else if(qklx.equals("14")){//排迁类
							%>
							<tr>
								<td>序号</td>
								<td>融资主体</td>
								<td>施工单位</td>
								<td>工程名称</td>
								<td>合同编号</td>
								<td>合同值</td>
								<td>财审值</td>
								<td>已拨付</td>
								<td>本次拟付款</td>
								<td>累计拨付</td>
								<td>累计付款比例（%）</td>
								<td>按财审拨付比例（%）</td>
								<td>备注</td>
							</tr>
							<%
									if (qrs.getRowCount() > 0) {
										DecimalFormat df = new DecimalFormat("#.00");
									    String DWMC = "";
									    String XMMCNR = "";
									    String RZZT = "";
									    String HTBM = "";
									    String ZXHTJ = "";
									    String CSZ = "";
									    String YBF = "";
									    String BCSQ = "";
									    String LJBF = "";
									    String AHTBFB = "";
									    String BZ = "";
									    float CSZNum ;
									    String CSZBL = "0" ;
									    for(int i=0;i<qrs.getRowCount();i++){
									        DWMC= qrs.getString(i+1, "DWMC");
									        if(DWMC==null){
									        	DWMC= "";
									        }
									        RZZT = qrs.getString(i+1, "DICT_NAME");
									        XMMCNR= qrs.getString(i+1, "XMMCNR");
									        HTBM= qrs.getString(i+1, "HTBM");
									        if(HTBM==null)HTBM="";
									        ZXHTJ= qrs.getString(i+1, "ZXHTJ");
									        //ZXHTJ = Pub.NumberAddDec(Pub.MoneyFormat(ZXHTJ));
									        ZXHTJ = Pub.MoneyFormat(ZXHTJ);
									        CSZ= qrs.getString(i+1, "CSZ");
									        CSZNum = Float.parseFloat(CSZ);
									        CSZ = Pub.MoneyFormat(CSZ);
									        YBF= qrs.getString(i+1, "YBF");
									        YBF = Pub.MoneyFormat(YBF);
									        BCSQ= qrs.getString(i+1, "CWSHZ");
									        BCSQ = Pub.MoneyFormat(BCSQ);
									        LJBF= qrs.getString(i+1, "LJBF");
									        if(CSZNum!=0){
									        	CSZBL =  df.format((Float.parseFloat(qrs.getString(i+1, "LJBF"))/ CSZNum)*100);
									        }
									        LJBF = Pub.MoneyFormat(LJBF);
									        AHTBFB= qrs.getString(i+1, "AHTBFB");
									        BZ= qrs.getString(i+1, "BZ");
									        if(BZ==null)BZ="";
							%>
							<tr>
								<td><%=i+1 %></td>
								<td><%=RZZT %></td>
								<td><%=DWMC %></td>
								<td><%=XMMCNR %></td>
								<td><%=HTBM == null ? "" : HTBM %></td>
								<td><%=ZXHTJ %></td>
								<td><%=CSZ %></td>
								<td><%=YBF %></td>
								<td><%=BCSQ %></td>
								<td><%=LJBF %></td>
								<td><%=Pub.MoneyFormat(AHTBFB) %></td>
								<td><%=Pub.MoneyFormat(CSZBL) %></td>
								<td><%=BZ %></td>
							</tr>
							<%
									    }
									}
							}else{
						%>
							<tr>
								<td>序号</td>
								<td>融资主体</td>
								<td>收款单位</td>
								<td>项目内容</td>
								<td>合同编号</td>
								<td>合同值</td>
								<td>已拨付</td>
								<td>本次拟付款</td>
								<td>累计拨付</td>
								<td>累计付款比例（%）</td>
								<td>备注</td>
							</tr>
							<%
									if (qrs.getRowCount() > 0) {
									    String DWMC = "";
									    String XMMCNR = "";
									    String RZZT = "";
									    String HTBM = "";
									    String ZXHTJ = "";
									    String YBF = "";
									    String BCSQ = "";
									    String LJBF = "";
									    String AHTBFB = "";
									    String BZ = "";
									    
									    for(int i=0;i<qrs.getRowCount();i++){
									        DWMC= qrs.getString(i+1, "DWMC");
									        if(DWMC==null){
									        	DWMC= "";
									        }
									        RZZT = qrs.getString(i+1, "DICT_NAME");
									        XMMCNR= qrs.getString(i+1, "XMMCNR");
									        HTBM= qrs.getString(i+1, "HTBM");
									        if(HTBM==null)HTBM="";
									        ZXHTJ= qrs.getString(i+1, "ZXHTJ");
									        //ZXHTJ = Pub.NumberAddDec(Pub.MoneyFormat(ZXHTJ));
									        ZXHTJ = Pub.MoneyFormat(ZXHTJ);
									        YBF= qrs.getString(i+1, "YBF");
									        YBF = Pub.MoneyFormat(YBF);
									        BCSQ= qrs.getString(i+1, "CWSHZ");
									        BCSQ = Pub.MoneyFormat(BCSQ);
									        LJBF= qrs.getString(i+1, "LJBF");
									        LJBF = Pub.MoneyFormat(LJBF);
									        AHTBFB= qrs.getString(i+1, "AHTBFB");
									        BZ= qrs.getString(i+1, "BZ");
									        if(BZ==null)BZ="";
							%>
							<tr>
								<td><%=i+1 %></td>
								<td><%=RZZT %></td>
								<td><%=DWMC %></td>
								<td><%=XMMCNR %></td>
								<td><%=HTBM == null ? "" : HTBM %></td>
								<td><%=ZXHTJ %></td>
								<td><%=YBF %></td>
								<td><%=BCSQ %></td>
								<td><%=LJBF %></td>
								<td><%=Pub.MoneyFormat(AHTBFB) %></td>
								<td><%=BZ %></td>
							</tr>
							<%
									    }
									}

							} %>
						</table>
						<!--   下面的table代码根据sjbh获得不同的 请款类别重新生成表格 end-->

					</th>
				</tr>
				<tr>
					<td width="25%">
						<p>
							中心副主任：
						</p>
						<%
							if (zxfzr != null) {
						%>
						<p><%=zxfzr[0]%></p>
						<p>
							<img
								src='<%=request.getContextPath()
						+ "/fileUploadController.do?getFile&account="
						+ zxfzr[2] + "&ywlx=" + YwlxManager.SYSTEM_USER_QM%>'>
							&nbsp;&nbsp;
							<font color=Blue><%=zxfzr[1]%></font>
						</p>
						<%
							} else {
						%>
						<p>
							&nbsp;
						</p>
						<p>
							&nbsp;
						</p>
						<%
							}
						%>
					</td>
					<td width="25%">
						<p>
							账务总监：
						</p>
						<%
							if (cwzj != null) {
						%>
						<p><%=cwzj[0]%></p>
						<p>
							<img
								src='<%=request.getContextPath()
						+ "/fileUploadController.do?getFile&account=" + cwzj[2]
						+ "&ywlx=" + YwlxManager.SYSTEM_USER_QM%>'>
							&nbsp;&nbsp;
							<font color=Blue><%=cwzj[1]%></font>
						</p>
						<%
							} else {
						%>
						<p>
							&nbsp;
						</p>
						<p>
							&nbsp;
						</p>
						<%
							}
						%>
					</td>
					<td width="25%">
						<p>
							分管主任：
						</p>
						<%
							if (fgzr != null) {
						%>
						<p><%=fgzr[0]%></p>
						<p>
							<img
								src='<%=request.getContextPath()
						+ "/fileUploadController.do?getFile&account=" + fgzr[2]
						+ "&ywlx=" + YwlxManager.SYSTEM_USER_QM%>'>
							&nbsp;&nbsp;
							<font color=Blue><%=fgzr[1]%></font>
						</p>
						<%
							} else {
						%>
						<p>
							&nbsp;
						</p>
						<p>
							&nbsp;
						</p>
						<%
							}
						%>
					</td>
					<td width="25%">
						<p>
							财务部负责人：
						</p>
						<%
							if (cwbfzr != null) {
						%>
						<p><%=cwbfzr[0]%></p>
						<p>
							<img
								src='<%=request.getContextPath()
						+ "/fileUploadController.do?getFile&account="
						+ cwbfzr[2] + "&ywlx=" + YwlxManager.SYSTEM_USER_QM%>'>
							&nbsp;&nbsp;
							<font color=Blue><%=cwbfzr[1]%></font>
						</p>
						<%
							} else {
						%>
						<p>
							&nbsp;
						</p>
						<p>
							&nbsp;
						</p>
						<%
							}
						%>
					</td>
				</tr>
				<tr>
					<td colspan=4 align="right">
						制表人：<%=zbr%></td>
				</tr>
			</table>
		</form>
	</body>
</html>