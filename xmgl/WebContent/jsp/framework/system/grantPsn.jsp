<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.ccthanking.framework.coreapp.orgmanage.orgrolepsnmap.*"%>
<%@ page import="com.ccthanking.framework.common.RoleVO.*"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.coreapp.orgmanage.*"%>

<%
        String roleName=request.getParameter("roleName");//new String(request.getParameter("roleName").getBytes("8859_1"), "GBK");
        if(roleName == null){
		roleName = "";
	}
	String levelName=request.getParameter("levelName");
	if(levelName == null){
		levelName = "";
	}
        String selectedPsn="";
	Role role = OrgRoleManager.getInstance().getRole(roleName);

	try
	{
        	User[] users=null;

                users =role.getUsers();//((RoleVO)role).getUsers();

		if(users != null)
		{

			for(int i=0;i<users.length;i++)
   			{
				if (users[i]!=null && users[i].getAccount()!="")
					selectedPsn += users[i].getAccount()+"|";
	   		}
			//System.out.println(selectedPsn);
		}

	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
%>
<HTML XMLNS:ELEMENT>

<head>
<title>浜哄憳妗ｆ淇℃伅</title>
<meta http-equiv="Content-Type" content="text/html;charset=GBK">

<LINK type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/default.css">
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/TimePicker.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/default.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/xTabPage.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/xHint.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/xWin.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/xScrollBar.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/xDic.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/xDics.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/xQuery.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/more.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/commonjs/xPucker.js"></SCRIPT>
<SCRIPT language="JavaScript">
<!--
var strAction='OrgPerson/OrgPersonAction.do';

var selPsn='<%=selectedPsn%>';
var selRows="";
var g_iHeight=400;
function doInit()
{
	selRows=selPsn;


}
function doSearch()
{
		if(doQuery(0))
	{
		processbar.style.display="";

		document.frmPost.action = strAction+"?method=grantPsnList&roleName=<%=roleName%>&levelName=<%=levelName%>";

		doGlobalQuery(document.frmPost.action,0,0,0,10,0);
		var objs = tabList1.rows;
	   	for( var n=0;n<objs.length;n++)
	   	{
	   		if(selPsn.indexOf(objs[n].ACCOUNT+"|")>=0)
	   		{
	   			objs[n].cells[0].firstChild.checked=true;
	   		}
	   	}
		processbar.style.display="none";
	}
}

function tr_click(obj)
{
      	obj.cells[0].firstChild.checked = !obj.cells[0].firstChild.checked;
      	var objs = tabList1.rows;
	
	for( var n=1;n<objs.length;n++)
	{
      		if (objs[n].cells[0].firstChild.checked)
      		{
        		if (selRows.indexOf(objs[n].ACCOUNT+"|")<0)
				selRows+=objs[n].ACCOUNT+"|";

      		}
      		else
      		{
			if (selRows.indexOf(objs[n].ACCOUNT+"|")>=0)
        		{

				selRows=selRows.substring(0,selRows.indexOf(objs[n].ACCOUNT+"|"))+selRows.substring(selRows.indexOf(objs[n].ACCOUNT+"|")+objs[n].ACCOUNT.length+1,selRows.length);
			}
      		}

	}
	

}

function PreNextFlashRownum()
{
    var objHtc = getPageControlByPage(g_xTabPane.selectedIndex);
    doGlobalQuery(document.frmPost.queryAction,g_xTabPane.selectedIndex,objHtc.currentnum,objHtc.totalnum,objHtc.rowsperpage,objHtc.countrows);
    var objs = tabList1.rows;
for( var n=0;n<objs.length;n++)
{
      if(selRows.indexOf(objs[n].ACCOUNT+"|")>=0)
      {
              objs[n].cells[0].firstChild.checked=true;
      }
}

}

function check_click(obj)
{
     var objs = tabList1.rows;

for( var n=1;n<objs.length;n++)
{
      if (objs[n].cells[0].firstChild.checked)
      {
        if (selRows.indexOf(objs[n].ACCOUNT+"|")<0)
		selRows+=objs[n].ACCOUNT+"|";

      }
      else
      {
	if (selRows.indexOf(objs[n].ACCOUNT+"|")>=0)
        {

		selRows=selRows.substring(0,selRows.indexOf(objs[n].ACCOUNT+"|"))+selRows.substring(selRows.indexOf(objs[n].ACCOUNT+"|")+objs[n].ACCOUNT.length+1,selRows.length);
	}
      }

}


}

function onOk()
{
	/*var objs = tabList1.getSelectedRows();
	var strAdd= "";
   	for( var n=0;n<objs.length;n++)
	{
   		if(objs[n].cells[0].firstChild.checked)
			strAdd+=objs[n].ACCOUNT+"|";
   	}*/
	window.returnValue=selRows;//strAdd;
	window.close();
}

function onCancel()
{
   window.returnValue="";
   window.close();
}

-->
</SCRIPT>

</head>
<body leftmargin="10" topmargin="0" >

<div id="processbar" class="loading" style="display:none" align="center">姝ｅ湪鏌ヨ锛岃绋嶅�...</div>
<div class="tab-pane" id="tabPane" width="100%" style="display:none" selectedIndex="0">
  <div class="tab-page"><span class="tab" title="鍥哄畾鏌ヨ">浜哄憳鍒楄〃</span>
    <div class="tab-layout">
      <TABLE class="editTabTitle" width="98%" cellpadding="2" cellspacing="1">
             <TR>
                <TD class="label"  >鎵�湪閮ㄩ棬</TD>
                <TD class="editLabel"><INPUT type="text" class="Edit"  kind="dic" dicwidth="500" src="ZZJG"  fieldname="DEPARTMENT"  operation="like"> </td>
                <TD  class="label"  >鐢ㄦ埛鍚�/TD>
                <TD class="editLabel"><INPUT type="text" class="Edit" kind="text" fieldType="text"  showInput="false"  fieldname="ACCOUNT"  maxlength="10" operation="like">
                </TD>
             </TR>
            <TR  style="display:none;">
            <TD colspan="2">
            <INPUT type="hidden" class="Edit" kind="text"  fieldname="rownum"  value="1000" operation="&lt;=" >
            </TD>
        </TR>
      </TABLE>
      <br/>
      <ELEMENT:newcontrol style="" onproper="PreNextFlashRownum()" id="page1" tablist="tabList1"/>
      <table id="tabList1" class="TabList" pagecontrol="page1" width="750" frame="box" cellPadding="0" border="1" cellSpacing="0"borderColorLight="silver" borderColorDark="white" style="width:98%;">
            <TR class="TabListTitle" align="center">
                <TH type="multi"></TH>
                <TH fieldname="ACCOUNT">鐢ㄦ埛鍚�/TH>
                <TH fieldname="DEPARTMENT">鎵�湪閮ㄩ棬</TH>
                <TH fieldname="NAME">濮撳悕</TH>
            </TR>
      </table>
    </div>
  </div>
</div>
<div align="center">
  <FORM name="frmPost" action=""  method="post" style="display:none"  >
        <input type="hidden" name="queryXML" id="queryXML">
        <input type="hidden" name="txtXML">
        <input type="hidden" name="txtFilter">
    <input type="button" class="TabButton" name="cmdQuery" value="鏌ヨ" onclick="doSearch()">
    <input type="button" class="TabButton" name="cmdOk" value="纭畾" onclick="onOk()">
    <input type="button" class="TabButton" name="cmdCancel" value="鍙栨秷" onclick="onCancel()">
  </FORM>
</div>
</body>
</html>
