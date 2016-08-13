<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="org.dom4j.*"%>
<app:checkLogon name="YHGL"/>
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

function doSearch(n)
{
    var i = g_xTabPane.selectedIndex;
    if(doQuery(0))
    {
        processbar.style.display="";
        tabList.querycondition = frmPost.queryXML.value;
        tabList.queryaction = frmPost.action = strAction+"?method=queryList";
        doGlobalQuery(document.frmPost.action,0,n,0,10,0);
        processbar.style.display="none";
    }
}

function SaveClick(n)
{
    clearTxtXML();
    var meth="";
    if(n=="1")
        meth="insert";
    else if(n=="2")
        meth="update";
    else if(n=="3")
        meth="delete";

    document.frmPost.action = strAction+"?method="+meth;
    var res = getValueByPages([1]);
    if(res)
    {
        appendXML();
        doGlobalPost(document.frmPost.action);
        resetValueByPages([1]);
        if(n=="1")
        {
            insertTabList(tabList);
            tabList.setSelect(1);
        }
        if(n=="2")
        {
            updateTabList(tabList);
        }
        if(n=="3")
        {
            deleteTabList(tabList);
        }
        g_xTabPane.setSelectedIndex(0);
    }
}

function tr_click(obj)
{
    obj.cells[0].firstChild.checked=!obj.cells[0].firstChild.checked;
    if(obj.cells[0].firstChild.checked)
    {
        document.frmPost.cmdUpdate.action = "2";
        setValueByPages(obj,[1]);
        g_xTabPane.setSelectedIndex(1);
    }
}

function radio_click(obj)
{
    document.frmPost.cmdUpdate.action = "2";
    setValueByPages(obj.row,[1]);
    g_xTabPane.setSelectedIndex(1);
}

//校验用户名
function checkUserID()
{
    var strTemp = "";
    if(!ACCOUNT.value)
    {
        return;
    }
    else
    {
        strTemp = ACCOUNT.value;
    }
    if(!strTemp.match(/^[\d\w]*$/))
    {
        alert("用户名只能是数字0-9、字母a-z,A-Z和下划线 _ 组成！");
        ACCOUNT.value="";
        ACCOUNT.focus();
        return;
      }
    var action = strAction+"?method=checkAccount&account="+strTemp;
    alert(doPost(action,""));
}

//给用户授予角色
function grantRole()
{
    if(ACCOUNT.value)
    {
        var strUrl='grantRoles.jsp?account='+ACCOUNT.value+'&levelName='+LEVEL_NAME.value;
        var res=window.showModalDialog( strUrl,window,'font-family:Verdana; font-size:12; help:no;  status:no; unadorned:no; dialogWidth:48em; dialogHeight:36em' );
        if(res != null )
        {
            var ih=res.indexOf("#$#");
            if(ih>=0)
            {
                var num2=res.indexOf("#$#",ih+3);
                var vAdd =  res.substring(0,ih);
                var vDel = res.substring(ih+3 ,num2);
                var vChecked = res.substring(num2+3 ,res.length);
                var strActionName = 'OrgRolePsnMap/orgRolePsnMapAction.do';
                var meth="insertOrDelet&person_Account="+ACCOUNT.value+"&add="+vAdd+"&del="+vDel+"&levelName="+LEVEL_NAME.value;
                document.frmPost.action = strActionName+"?method=" + meth;
                if(getValueByPages([1]))
                {
                      appendXML();
                       returnvalue =doGlobalPost(document.frmPost.action);
                }
            }
        }
    }
    else
    {
         alert("用户名不能为空！");
    }
}

//密码初始化
function initPassword()
{
      var row_id=ACCOUNT.value;
      if(row_id!=null && row_id!="")
      {
        document.frmPost.action = strAction+"?method=initPass&account="+row_id;
        if(getValueByPages([1]))
        {
            appendXML();
            doGlobalPost(document.frmPost.action);
        }
      }
     else
    {
           alert("用户名不能为空！");
     }
}

function doInsert()
{
    document.frmPost.cmdUpdate.action = "1";
    showButtons(["cmdSearch"]);
    hideButtons(["cmdInsert","cmdUpdate","cmdDel","cmdReset","cmdInit","cmdRole"]);
    resetValueByPages([1]);
    g_xTabPane.setSelectedIndex(1);
}

//切换页签
function onSwitchPage()
{
    switch(g_xTabPane.selectedIndex)
    {
        case 0:
            showButtons(["cmdSearch"]);
            hideButtons(["cmdInsert","cmdUpdate","cmdDel","cmdReset","cmdInit","cmdRole"]);
            break;
        case 1:
            if(tabList.getSelectedRowIndex() == -1){
            document.frmPost.cmdUpdate.action = "1";
            showButtons(["cmdUpdate","cmdInit","cmdRole"]);
            hideButtons(["cmdSearch","cmdInsert","cmdDel","cmdReset"]);
            }else{
            showButtons(["cmdUpdate","cmdDel","cmdInit","cmdRole"]);
            hideButtons(["cmdSearch","cmdInsert","cmdReset"]);
            }
            break;
        default:
            break;
    }
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
        <TR>
          <TD class="label" >用户名</TD>
          <TD class="editLabel"><INPUT type="text" class="Edit" kind="text" fieldType="text"  fieldname="ACCOUNT"  maxlength="10"> </TD>
          <TD class="label" >所在部门</TD>
          <TD class="editLabel"><INPUT type="text" class="Edit"  kind="dic"  src="ZZJG"  filtercode="<%=deptFilter%>"   fieldname="DEPARTMENT"  operation="=">
        </TR>
        <TR>
          <TD class="label" >姓名</TD>
          <TD class="editLabel" colspan="3"><INPUT type="text" class="Edit" kind="text" fieldType="text"  showInput="false"  fieldname="NAME" > </TD>
        </TR>
        <TR  style="display:none;">
          <TD class="editLabel"><INPUT type="text" class="Edit" kind="text"  fieldname="rownum"  value="1000" operation="&lt;=" ></TD>
        </TR>
      </TABLE>
      <ELEMENT:newcontrol style="" onproper="PreNextFlashRownum()" id="page" tablist="tabList"/>
      <table id="tabList" class="TabList" pagecontrol="page" width="750" frame="box" cellPadding="0" border="1" cellSpacing="0"borderColorLight="silver" borderColorDark="white" style="width:98%;">
        <TR class="TabListTitle" align="center">
          <TH type="single"></TH>
          <TH fieldname="ACCOUNT">用户名</TH>
          <TH fieldname="DEPARTMENT">所在部门</TH>
          <TH fieldname="NAME">姓名</TH>
        </TR>
      </table>
    </div>
  </div>
  <div class="tab-page"><span class="tab" title="用户登记">用户登记</span>
    <div class="tab-layout">
      <TABLE class="editTabTitle" width="98%" cellpadding="2" cellspacing="1">
        <TR>
          <TD class="label">用户名</TD>
          <TD class="editLabel">
          <INPUT type="text" class="Edit" id="ACCOUNT" name="ACCOUNT"  kind="text"  showInput="false"  fieldname="ACCOUNT"   must="true"  maxlength="10">
          <input name="checkAccount" type=button  class="TabButton" value="检测用户名"  onclick="checkUserID()" >
          </TD>
          <TD class="label">所在部门</TD>
          <TD class="editLabel"><INPUT type="text" class="Edit" id="DEPARTMENT" name="DEPARTMENT"  kind="dic" src="ZZJG"  filtercode="<%=deptFilter%>"  fieldname="DEPARTMENT" must="true"  maxlength="16" ></TD>
        </TR>
        <TR >
          <TD class="label">姓名</TD>
          <TD class="editLabel"><INPUT type="text" class="Edit" id="NAME" name="NAME" kind="text" showInput="false"  fieldname="NAME"   must="true" maxlength="256"> </TD>
          <TD class="label">用户类型</TD>
          <TD class="editLabel"><select class="Select"  name="PERSON_KIND" id="PERSON_KIND" fieldname="PERSON_KIND"  src="E#2=管理员:3=普通用户" code="3"></TD>
        </TR>
        <TR>
          <TD class="label" >性别</TD>
          <TD class="editLabel"><select class="Select" id="SEX" name="SEX" src="E#1=男:2=女" fieldname="SEX" code="1"></td>
          <TD class="label">密级级别</TD>
          <TD class="editLabel"><select class="Select" id="SECRET_LEVEL" name="SECRET_LEVEL"  src="E#1=1:2=2:3=3:4=4:5=5:6=6:7=7:8=8"  fieldname="SECRET_LEVEL" code='1'></TD>
        </TR>
        <TR>
          <TD class="label">用户编号（警号）</TD>
          <TD class="editLabel"><INPUT type="text" class="Edit" id="USER_SN" name="USER_SN" kind="text" showInput="false"  fieldname="USER_SN" maxlength="16"> </TD>
          <TD class="label">身份证号</TD>
          <TD class="editLabel"><input type="text" class="Edit" kind="idcard" id="IDCARD" name="IDCARD"  fieldname="IDCARD" showInput="false"/></TD>
        </TR>
        <TR>
          <TD class="label">邮件服务器</TD>
          <TD class="editLabel"><INPUT type="text" class="Edit" id="SMTP" name="SMTP" kind="text" showInput="false"  fieldname="SMTP" maxlength="50"/> </TD>
          <TD class="label">邮件的地址</TD>
          <TD class="editLabel"><input type="text" class="Edit" kind="text" id="MAILFROM" name="MAILFROM"  fieldname="MAILFROM" showInput="false" maxlength="50"/></TD>
        </TR>
        <TR>
          <TD class="label">邮件服务器帐号</TD>
          <TD class="editLabel"><INPUT type="text" class="Edit" id="MAILNAME" name="MAILNAME" kind="text" showInput="false"  fieldname="MAILNAME" maxlength="50"/> </TD>
          <TD class="label">邮件服务器密码</TD>
          <TD class="editLabel"><input type="password" class="Edit" kind="text" id="MAILPSW" name="MAILPSW"  fieldname="MAILPSW" showInput="false" maxlength="20"/></TD>
        </TR>
        <TR>
          <TD class="label">CA 证书序列号</TD>
          <TD class="editLabel" colspan="3"><INPUT type="text" class="Edit" id="CERTCODE" name="CERTCODE" kind="text" showInput="false"  fieldname="CERTCODE" maxlength="16"/> </TD>
        </TR>
        <TR style="display:none;">
          <TD class="label">用户展现风格</TD>
          <TD class="editLabel" colspan=3><select class="Select" id="USERTEMPLATE" name="USERTEMPLATE" src="E#defaultTemplate=默认风格:RedTemplate=红色风格" fieldname="USERTEMPLATE" code="blueTemplate"></td>
        </TR>
        <TR style="display:none;">
          <td colspan=2 style="display:none"><input type="text"    id="LEVEL_NAME" name="LEVEL_NAME"   showInput="false"  fieldname="LEVEL_NAME"></td>
        </TR>
      </TABLE>
    </div>
  </div>
</div>
<div align="center">
  <FORM name="frmPost" action=""  method="post" style="display:none"  >
    <input type="hidden" name="queryXML">
    <input type="hidden" name="txtXML">
    <input type="hidden" name="txtFilter">
    <input type="hidden" name="resultXML">

    <INPUT type="button" name="cmdSearch" class="TabButton" value="#Q查  询" onclick="doSearch(1)">&nbsp;&nbsp;
    <INPUT type="button" name="cmdInsert" class="TabButton" style="display:;" value="#S保  存"  onclick="doInsert();">
    <INPUT type="button" name="cmdUpdate" class="TabButton" action="1" style="display:none" value="#S保  存"  onclick="SaveClick(this.action);">
    <INPUT type="button" name="cmdReset" class="TabButton"  value="#R重   置" onclick="doReset();">
    <INPUT type="button" name="cmdDel"  class="TabButton" style="display:none" value="#D删  除" onclick="SaveClick('3')">
    <INPUT type="button" name="cmdInit" class="TabButton" style="display:none" value="密码初始化" onclick="initPassword()">
    <INPUT type="button" name="cmdRole" class="TabButton" style="display:none" value="授予角色" onclick="grantRole()">
  </FORM>
</div>
</body>
</html>
