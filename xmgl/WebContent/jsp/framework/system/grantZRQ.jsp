<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="org.dom4j.*"%>
<app:checkLogon name="ZrqManager"/>
<%
    User user = (User)session.getAttribute(Globals.USER_KEY);
    String curUserLevel = (String)session.getAttribute("USERLEVEL");
    String curDeptID=(String)session.getAttribute("DEPTID");
    String deptFilter="";
    switch ((curUserLevel == null || curUserLevel.equals("")) ? 0 : Integer.parseInt(curUserLevel))
    {
    case 1:
        deptFilter = curDeptID.substring(0,2) ;
        break;// 省厅用户
    case 2:
        deptFilter = curDeptID.substring(0, 4);
        break;// 市级用户
    case 3:
        deptFilter = curDeptID.substring(0, 6);
        break;// 分局用户
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
        deptFilter =  curDeptID;
        break;// 派出所用户和派出所同级别用户，警务区，责任区用户
    }
%>
<HTML>

<head>
<title>人员档案信息</title>
<meta http-equiv="Content-Type" content="text/html;charset=GBK">
<app:base/>
<SCRIPT language="JavaScript">
<!--
var strAction='OrgPerson/OrgPersonAction.do';

function doSearch()
{
    if(doQuery(0))
    {
        processbar.style.display="";
        tabList.querycondition = frmPost.queryXML.value;
        tabList.queryaction = frmPost.action = strAction+"?method=queryZrqList";
        doGlobalQuery(document.frmPost.action,0,0,0,10,0);
        processbar.style.display="none";
    }
}

function tr_click(obj)
{
    obj.cells[0].firstChild.checked=!obj.cells[0].firstChild.checked;
}

function radio_click(obj)
{
}

function doDelete(row)
{
    if(!row.ACCOUNT)
    {
        alert('当前责任区尚未分配管区民警！');
        return;
    }
    var action = strAction+"?method=deleteZrqPerson&pid="+row.ACCOUNT;
    if(showGlobalResult(doPost(action,"")))
    {
        row.ACCOUNT = '';
        row.NAME = '';
        row.cells[4].innerHTML = "";
    }
}

function doSave()
{
    if(zrq.code && persons.code)
    {
        if (showGlobalResult(doPost(strAction+"?zrq="+zrq.code+"&persons="+persons.code,"","saveZrqPerson")))
        {
            doSearch();
        }
    }
    else
        alert('责任区 和 管区民警不能空！');
}

function dofocuscond(obj)
{
    obj.src = obj.src_bak;
    if(deptpid.code)
    {
        obj.src += " and DEPT_PARANT_ROWID='"+deptpid.code+"'";
    }
}

//切换页签
function onSwitchPage()
{
}

function doInit()
{
  
}
//-->
</SCRIPT>
</head>
<body leftmargin="10" topmargin="0" >
<div id="processbar" class="loading" style="display:none" align="center">正在查询，请稍候...</div>
<div class="tab-pane" id="tabPane" width="100%" style="display:none" selectedIndex="0">
  <div class="tab-page"><span class="tab" title="固定查询">固定查询</span>
    <div class="tab-layout">
      <TABLE class="editTabTitle" width="98%" cellpadding="2" cellspacing="1">
        <TR  style="display:none;"><TD colspan="4"><INPUT type="text" class="Edit" kind="text"  fieldname="rownum"  value="1000" operation="&lt;=" ></TD></TR>
        <TR>
          <TD class="label" >责任区所属单位</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit"  kind="dic"  src="ZZJG"  filtercode="<%=deptFilter%>" editignore="true" id="deptpid" fieldname="d.DEPT_PARANT_ROWID"  operation="="></TD>
          <TD class="label" >责任区民警</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit"  kind="dic" id="persons" src="T#org_person:ACCOUNT:NAME:DEPARTMENT like '<%=deptFilter%>$'" fieldname="p.ACCOUNT" multi="true" operation="in"></TD>
        </TR>
        <TR>
          <TD class="label" >责任区</TD>
          <TD class="editLabel" colspan="3"><INPUT type="text" class="Edit"  kind="dic" id="zrq" src_bak="T#org_dept:ROW_ID:DEPT_NAME:DEPTTYPE=8 and ROW_ID like '<%=deptFilter%>$'"  filtercode="<%=deptFilter%>.{<%=(16-deptFilter.length())%>}"   fieldname="d.ROW_ID"  operation="=" onfocus="dofocuscond(this)"></TD>
        </TR>
      </TABLE>
      <br/>
      <ELEMENT:newcontrol style="" onproper="PreNextFlashRownum()" id="page" tablist="tabList"/>
      <table id="tabList" class="TabList" pagecontrol="page" width="750" frame="box" cellPadding="0" border="1" cellSpacing="0"borderColorLight="silver" borderColorDark="white" style="width:98%;">
        <TR class="TabListTitle" align="center">
                <TH type="single"></TH>
                <TH >责任区编码</TH>
                <TH >责任区名称</TH>
                <TH >所属部门</TH>
                <TH >负责民警</TH>
                <TH type="link" value="删除" action="doDelete(this.parentElement.parentElement);">删除</TH>
        </TR>
      </table>
    </div>
  </div>
</div>
  <div align="center">
    <FORM name="frmPost" action=""  method="post" style="display:none"  >
         <input type="hidden" name="queryXML">
         <input type="hidden" name="txtXML">
         <input type="hidden" name="txtFilter">
         <input type="hidden" name="resultXML">

         <INPUT type="button" name="cmdSearch" class="TabButton" value="#Q查  询" onclick="doSearch()">
         <INPUT type="button" name="cmdSave"   class="TabButton" value="#S保  存" onclick="doSave();">
    </FORM>
  </div>
</body>
</html>
