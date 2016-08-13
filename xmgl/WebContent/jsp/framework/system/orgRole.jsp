<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="org.dom4j.*"%>
<app:checkLogon name="RoleManager"/>
<%
    User user = (User) session.getAttribute(Globals.USER_KEY);
    String curDeptId=user.getDepartment();
    if(curDeptId == null) curDeptId="";
%>
<HTML><head>
<title>角色管理</title>
<meta http-equiv="Content-Type" content="text/html;charset=GBK">
<app:base/>
<SCRIPT language="JavaScript">
<!--
var strAction='OrgRole/orgRoleAction.do';

function doSearch(n)
{
    if(doQuery(0))
    {
        processbar.style.display="";
        document.frmPost.action = strAction+"?method=queryList";
        doGlobalQuery(document.frmPost.action,n,1,0,10,0);
        processbar.style.display="none";
    }
}

function SaveClick(n)
{
    clearTxtXML();
    var meth="";
     if(n!="3"&&tabList1.getSelectedRowIndex()>0){
         n = 2;
     }
    if(n=="1")
        meth="insert";
    else if(n=="2")
        meth="update";
    else if(n=="3")
        meth="delete";
    document.frmPost.action = strAction+"?method="+meth;
    if(getValueByPages([1]))
    {
        appendXML();
        if(!doGlobalPost(document.frmPost.action))
            return;
    }
    if(n == "3")
    {
        resetValueByPages([1]);
        doSearch(1);
//        document.frmPost.cmdDel.style.display = 'none';
//        document.frmPost.cmdUpdate.style.display = 'none';
    }
    else if(n == '1')
    {
        appendTabList(tabList1);
    }
    else if(n == '2')
    {
        updateTabList(tabList1);
    }
}

function tr_click(obj)
{
    obj.cells[0].firstChild.checked = true;
    radio_click(obj.cells[0].firstChild);
}

function radio_click(obj)
{
    setValueByPages(obj.row,[1]);
    if(obj.row.DEPTID == "<%=curDeptId%>")
        document.frmPost.cmdDel.style.display = '';
    else
        document.frmPost.cmdDel.style.display = 'none';
}

function doReset()
{
    tabList1.cancelSelect();
    resetValueByPages([1]);
}

function grantPriv()
{
      var flag =false;
      for(var n=0;n<tabList1_radio.length;n++)
      {
           if( tabList1_radio[n].checked==true)
           {
            flag =true;
           }
      }

     if(flag)
     {
          var strUrl='grantMenu.jsp?roleName='+NAME.value+"&levelName="+LEVEL_NAME.code;
        var res=window.showModalDialog( strUrl,window,'font-family:Verdana; font-size:12; help:no;  status:yes; scrollbar:yes; unadorned:no; dialogWidth:40em; dialogHeight:32em' );
        if(res != null )
        {
            var ih=res.indexOf("#$#");
            if(ih>=0)
            {
                   var vAdd =  res.substring(0,ih);
                   var vDel = res.substring(ih+3 ,res.length);
                   var strActionName='OrgRoleMenuMap/orgRoleMenuMapAction.do';
                   //var meth="insertOrDelet&roleName="+NAME.value+"&add="+vAdd+"&del="+vDel+"&levelName="+LEVEL_NAME.code;
                   var meth="insertOrDelet&roleName="+NAME.value ;//+"&add="+vAdd+"&del="+vDel+"&levelName="+LEVEL_NAME.code;
                   document.frmPost.txtXML.value = "<data><add>"+vAdd+"</add><del>"+vDel+"</del></data>";
                document.frmPost.action = strActionName+"?method=" + meth;
                if(getValue())
                {
                     returnvalue =doGlobalPost(document.frmPost.action);
                }
            }
         }
     }else{
       alert("请选择一条记录！！");
     }
}

function grantPsn()
{
      var flag =false;
      for(var n=0;n<tabList1_radio.length;n++)
      {
           if( tabList1_radio[n].checked==true)
           {
            flag =true;
           }
      }

     if(flag)
     {
          var strUrl='grantPsn.jsp?roleName='+NAME.value+"&levelName="+LEVEL_NAME.code;
        var res=window.showModalDialog( strUrl,window,'help:no;  status:yes; scrollbar:yes; unadorned:no; dialogWidth:43.5em; dialogHeight:34em' );
        if(res != null && res.length>0)
        {
            var strActionName='OrgRolePsnMap/orgRolePsnMapAction.do';
            var meth="grantPsn&roleName="+NAME.value+"&add="+res;
            document.frmPost.action = strActionName+"?method=" + meth;
            if(getValue())
            {
               returnvalue =doGlobalPost(document.frmPost.action);
            }
         }
     }else{
       alert("请选择一条记录！！");
     }
}
function doInit(){
            showButtons(["cmdSearch"]);
            hideButtons(["cmdInsert","cmdUpdate","cmdDel","cmdPriv1","cmdPriv2","cmdReset"]);
}

//切换页签
function onSwitchPage()
{
    switch(g_xTabPane.selectedIndex)
    {
        case 0:
            showButtons(["cmdSearch"]);
            hideButtons(["cmdInsert","cmdUpdate","cmdDel","cmdPriv1","cmdPriv2","cmdReset"]);
            break;
        case 1:
            hideButtons(["cmdSearch","cmdUpdate"]);
            showButtons(["cmdInsert","cmdDel","cmdPriv1","cmdPriv2","cmdReset"]);
            break;
        default:
            break;
    }
}

</SCRIPT>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0"  style="overflow:visible">
<div id="processbar" class="loading" style="display:none" align="center">正在查询，请稍候...</div>
<div class="tab-pane" id="tabPane" width="100%" style="display:none" selectedIndex="0">
  <div class="tab-page"><span class="tab" title="固定查询">固定查询</span>
    <div class="tab-layout">
      <TABLE class="editTabTitle" width="98%" cellpadding="2" cellspacing="1">
        <TR>
          <TD class="label">角色名称</TD>
          <TD class="editLabel" colspan="3"><INPUT type="text" class="Edit" kind="text"    operation="like" fieldname="NAME" maxlength=32></TD>
        </TR>
        <TR>
          <TD class="label">级别名称</td>
          <Td class="editLabel" colspan="3"><INPUT type="text" class="Edit" kind="dic" src="BMJB" fieldname="LEVEL_NAME"></TD>
        </TR>
        <TR  style="display:none;">
          <TD class="editLabel" colspan="2"><INPUT type="text" class="Edit" kind="text"  fieldname="rownum"  value="1000" operation="&lt;=" ></TD>
        </TR>
        <TR style="display:none;">
          <TD class="label">级别名称</td>
          <Td class="editLabel"><INPUT type="text" class="Edit" kind="text" value="<%=curDeptId%>" fieldname="DEPTID"></TD>
        </TR>
        </TABLE>
    </div>
  </div>
  <div class="tab-page"><span class="tab" title="授权管理">授权管理</span>
    <div class="tab-layout">
      <TABLE class="editTabTitle" width="98%" cellpadding="2" cellspacing="1">
        <TR>
          <TD class="label">角色名称</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" kind="text" showInput="false" fieldname="NAME" must="true" name="NAME" maxlength=32></TD>
          <TD class="label">级别名称</td>
          <Td class="editLabel" ><INPUT type="text" name="LEVEL_NAME" class="Edit"  kind="dic" src="BMJB"  fieldname="LEVEL_NAME"  must="true"></TD>
        </TR>
        <TR style="display:none;">
          <TD class="label" colspan="3">角色名称</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" kind="dic" showInput="false" fieldname="DEPTID" must="false" name="DEPTID" maxlength=32 value="<%=user.getOrgDept().getBmjc()%>" code="<%=curDeptId%>"></TD>
        </TR>
        <TR>
          <TD class="label">备注</TD>
          <TD class="editLabel" colspan="3"><textarea rows="8" cols="20" style="width:450px;height:40px" class="Edit" kind="text" fieldname="S_MEMO" maxlength=32></textarea></TD>
        </TR>
      </TABLE>
      <br/>
      <ELEMENT:newcontrol style="" onproper="PreNextFlashRownum()" id="page1" tablist="tabList1"/>
      <table id="tabList1" class="TabList" pagecontrol="page1" width="750" frame="box" cellPadding="0" border="1" cellSpacing="0"borderColorLight="silver" borderColorDark="white" style="width:98%;">
        <TR class="TabListTitle" align="center">
          <TH type="single"></TH>
          <TH>角色名称</TH>
          <TH>级别名称</TH>
          <TH>管理部门</TH>
          <TH>备注</TH>
        </TR>
      </table>
    </div>
  </div>
</div>
<div align="center">
  <FORM name="frmPost" action="" method="post" style="display:none">
    <input type="hidden" name="queryXML">
    <input type="hidden" name="txtXML">
    <input type="hidden" name="txtFilter">
    <input type="hidden" name="resultXML">

    <INPUT type="button" name="cmdSearch" class="TabButton" value="#Q查  询" onclick="doSearch(1)">
    <INPUT type="button" name="cmdInsert" class="TabButton" value="#S保  存"  onclick="SaveClick('1')" style="display:none">
    <INPUT type="button" name="cmdUpdate" class="TabButton" value="#S保  存"  onclick="SaveClick('2')" style="display:none">
    <INPUT type="button" name="cmdDel"   class="TabButton"  value="#D删  除" onclick="SaveClick('3')" style="display:none">
    <INPUT type="button" name="cmdReset" class="TabButton"  value="#R重   置" onclick="doReset();">
    <INPUT type="button" name="cmdPriv1" class="TabButton"  value="授予权限" onclick="grantPriv()" style="display:none">
    <INPUT type="button" name="cmdPriv2" class="TabButton"  value="给人员授权" onclick="grantPsn()" style="display:none">
  </FORM>
</div>
</body>
</html>
