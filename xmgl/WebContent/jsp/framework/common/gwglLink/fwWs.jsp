<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="java.util.Map,java.util.HashMap,java.util.List,java.util.ArrayList" %>
<%@ page import="com.ccthanking.common.YwlxManager" %>
<%@ page import="com.ccthanking.framework.wsdy.PubWS" %>

<%
	String sjbh=(String)request.getParameter("sjbh");
    String isview = (String)request.getParameter("isview");
    	if(isview==null) isview = "";
	String queryWsInfoSql = "select  t.fieldname,t.domain_value,t.lrrid,t.lrrxm,APPROVEROLE," 
			+ "to_char(LRSJ,'YYYY-MM-DD') as hdate,WSWH_FLAG,ywlx,WS_TEMPLATE_ID from AP_PROCESS_WS t " 
			+ "where sjbh='" + sjbh + "' and lrsj is not null order by lrsj";
	QuerySet qs = DBUtil.executeQuery(queryWsInfoSql, null);

	List<Map<String, String>> ls1 = new ArrayList<Map<String, String>>();
	List<Map<String, String>> ls2 = new ArrayList<Map<String, String>>();
	List<Map<String, String>> ls3 = new ArrayList<Map<String, String>>();
	Map<String, String> map = new HashMap<String, String>();
	Map<String, String> imgMap = new HashMap<String, String>();
//	Map<String, String> dateMap = new HashMap<String, String>();
	boolean isLd = false;
	for(int i = 0; i < qs.getRowCount(); i++) {
		String fieldName = qs.getString(i+1, "FIELDNAME");
		String fieldType = qs.getString(i+1, "WSWH_FLAG"); 
		String ywlx =  qs.getString(i+1, "YWLX"); 
		String templateid = qs.getString(i+1, "WS_TEMPLATE_ID"); 
		String domain_value = qs.getString(i+1, "DOMAIN_VALUE") == null ? "" : qs.getString(i+1, "DOMAIN_VALUE");
		String lrrid = qs.getString(i+1, "LRRID");
		String lrrxm = qs.getString(i+1, "LRRXM");
		String hdate = qs.getString(i+1, "hdate");
		String role = qs.getString(i+1, "APPROVEROLE");
		domain_value = "BMFZRA".equals(fieldName) ? lrrxm : domain_value;
		domain_value = "BGSWZZH".equals(fieldName) ? lrrxm : domain_value;
		domain_value = "BGSZR".equals(fieldName) ? lrrxm : domain_value;
		//部门分管领导 常务副主任 建管中心主任三个角色为领导
 		if( "e9635cef-e899-441e-a76f-384b37e92107".equals(role)||"219d48ca-866c-4bfd-a02a-613d15ee4c94".equals(role)) {
			isLd = true;
			fieldName = "isLd";
		}
		if("3".equals(fieldType)){
			PubWS p = new PubWS();
			domain_value = p.changeFieldText(templateid, fieldName, sjbh, ywlx, request);
		}
		
		
		map.put(fieldName, domain_value);
		imgMap.put(fieldName, lrrid);
//		dateMap.put(fieldName, hdate);
		
		if("LDQF".equals(fieldName)) {
			Map<String, String> m = new HashMap<String, String>();
			m.put(fieldName, domain_value);
			m.put("hdate", hdate);
			ls1.add(m);
		}
		if("YGBMHQ".equals(fieldName)) {
			Map<String, String> m = new HashMap<String, String>();
			m.put(fieldName, domain_value);
			m.put("hdate", hdate);
			ls2.add(m);
		}
		if("isLd".equals(fieldName)) {
			Map<String, String> m = new HashMap<String, String>();
			m.put(fieldName, domain_value);
			m.put("hdate", hdate);
			ls3.add(m);
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<script language=JavaScript>
function printIframe(){
	window.print();
}

</script>
<form id="wsTemplate" name="wsTemplate">
<P class=STYLE3 align=center>长春市政府投资建设项目管理中心</P>
<DIV class=STYLE3 align=center>发&nbsp;文&nbsp;稿&nbsp;纸</DIV>
<TABLE class=table1 cellSpacing=0 width=648 bgColor=#000000 align=center>
<TBODY>
<TR>
<TD class=STYLE1 bgColor=#ffffff height=85 colSpan=6>标题：<%=map.get("BT") %></TD></TR>
<TR>
<TD class=STYLE1 bgColor=#ffffff height=45 vAlign=top colSpan=6>主送：<BR><%=map.get("ZS") %></TD></TR>
<TR>
<TD class=STYLE1 bgColor=#ffffff height=45 vAlign=top colSpan=6>抄送：<BR><%=map.get("CS") %></TD></TR>
<TR>
<TD class=STYLE1 bgColor=#ffffff height=150 vAlign=top width=326 colSpan=3>
签发：<BR>
<%
	if(isLd){
%>
<%
	for(int i = 0; i < ls3.size(); i++) {
		Map<String, String> map3 = ls3.get(i);
%>
<%=map3.get("isLd") == null ? "" : map3.get("isLd") %><br>
<img src='<%=request.getContextPath()+"/fileUploadController.do?getFile&account="+imgMap.get("isLd")+"&ywlx="+(YwlxManager.SYSTEM_USER_QM)%>'>
&nbsp;&nbsp;<font color=Blue><%=map3.get("hdate") == null ? "" : map3.get("hdate") %></font><BR>
<%	} %>
<%
	} else {
%>
<%
	for(int i = 0; i < ls1.size(); i++) {
		Map<String, String> map1 = ls1.get(i);
%>
<%=map1.get("LDQF") == null ? "" : map1.get("LDQF") %><br>
<img src='<%=request.getContextPath()+"/fileUploadController.do?getFile&account="+imgMap.get("LDQF")+"&ywlx="+(YwlxManager.SYSTEM_USER_QM)%>'>
&nbsp;&nbsp;<font color=Blue><%=map1.get("hdate") == null ? "" : map1.get("hdate") %></font><BR>
<%	} %>
<%		
	}
%>

</TD>
<TD class=STYLE1 bgColor=#ffffff height=150 vAlign=top width=315 colSpan=3>
会签：<BR>
<%
	for(int i = 0; i < ls2.size(); i++) {
		Map<String, String> map2 = ls2.get(i);
%>
<%=map2.get("YGBMHQ") == null ? "" : map2.get("YGBMHQ") %><br>
<img src='<%=request.getContextPath()+"/fileUploadController.do?getFile&account="+imgMap.get("YGBMHQ")+"&ywlx="+(YwlxManager.SYSTEM_USER_QM)%>'>
&nbsp;&nbsp;<font color=Blue><%=map2.get("hdate") == null ? "" : map2.get("hdate") %></font><BR>
<%	} %>
</TD>
</TR>
<TR>
<TD class=STYLE1 bgColor=#ffffff height=45 colspan=3>拟稿：<%=map.get("NG") %></TD>
<TD class=STYLE1 bgColor=#ffffff height=45 colspan=3>部门负责人：<%=map.get("BMFZRA") == null ? "" : map.get("BMFZRA") %></TD></TR>
<TR>
<TD class=STYLE1 bgColor=#ffffff height=45 colspan=3>办公室文字综合：<%=map.get("BGSWZZH") == null ? "" : map.get("BGSWZZH") %></TD>
<TD class=STYLE1 bgColor=#ffffff height=45 colspan=3>办公室主任：<%=map.get("BGSZR") == null ? "" : map.get("BGSZR") %></TD></TR>
<TR>
<TD class=STYLE1 width="34%" height=45 colspan=2>打字：<%=map.get("DZ") == null ? "" : map.get("DZ") %></TD>
<TD class=STYLE1 width="34%" height=45 colspan=2>印刷：<%=map.get("YS") == null ? "" : map.get("YS") %></TD>
<TD class=STYLE1 width="32%" height=45 colspan=2>校对：<%=map.get("JD") == null ? "" : map.get("JD") %></TD></TR>
<TR>
<TD class=STYLE1 width="34%" height=45 colspan=2>监印：<%=map.get("JY") == null ? "" : map.get("JY") %></TD>
<TD  class=STYLE1 width="34%" height=45 colspan=2>密级：<%=map.get("MJ") == null ? "" : map.get("MJ") %></TD>
<TD  class=STYLE1 width="32%" height=45 colspan=2>份数：<%=map.get("FS") == null ? "" : map.get("FS") %></TD></TR>
<TR>
 <%if(("1".equals(isview))){ %>
<TD class=STYLE1 bgColor=#ffffff height=45 colspan=3>文号：<%=map.get("BH") == null ? "" : map.get("BH") %></TD>
<TD class=STYLE1 bgColor=#ffffff height=45 colspan=3>发出日期：<%=map.get("FCRQ") == null ? "" : map.get("FCRQ") %></TD></TR>
<%}else{ %>
<TD class=STYLE1 bgColor=#ffffff height=45 colspan=3>文号：<input type="text" id="BH" fieldname="BH"  value="<%=map.get("BH") %>"></TD>
<TD class=STYLE1 bgColor=#ffffff height=45 colspan=3>发出日期：<input type="date" id="FCRQ" fieldname="FCRQ"  value="<%=map.get("FCRQ") %>"></TD></TR>
<%} %>
<TR>
<TD class=STYLE1 bgColor=#ffffff height=45 colSpan=6><SPAN class=STYLE1>要求办完时限：<%=map.get("YQBWSX") == null ? "" : map.get("YQBWSX") %></SPAN></TD></TR>
<TR>
<TD class=STYLE1 bgColor=#ffffff height=97 colSpan=6><%=map.get("KY") == null ? "" : map.get("KY") %></TD></TR></TBODY></TABLE>
<STYLE type=text/css>
<!--
.STYLE1 {font-size: 18px}
.STYLE2 {font-size: 22px}
.STYLE3 {font-size: 20px}
.table1 {   
 border: 1px solid #000000;   
 padding:0;    
 margin:0 auto;   
 border-collapse: collapse;   
 } 
 .table2 {   
 border: 0px solid #000000;   
 padding:0;    
 margin:0 auto;   
 border-collapse: collapse;   
 } 
td {   
 border: 1px solid #000000;   
 background: #fff;   
 font-size:18px;   
}
.td1 {   
 border: 0px solid #000000;   
 background: #fff;   
 font-size:18px;   
}
-->
</STYLE>
</form>
</body>
</html>