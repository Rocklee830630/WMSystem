<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<title>组织机构信息</title>
<meta http-equiv="Content-Type" content="text/html;charset=GBK">

<SCRIPT language="JavaScript">
<!--
var strAction='OrgDept/OrgDeptAction.do';
var opertType='1';

function doInit(){
    queryForm.Q_ROW_ID.value="";
    queryForm.Q_DEPT_NAME.value="";
    queryForm.Q_DEPTTYPE.value="";
    queryForm.Q_DEPT_PARANT_ROWID.value="";
}

function SaveClick(n)
{
    clearTxtXML();
    var meth="";
     if(n!="3"&&tabList.getSelectedRowIndex()>0){
         n = 2;
     }
    if(n=="1")
    {
        if(DEPT_PARANT_ROWID.value == '')
        {
            alert("请先选择一个组织机构部门作为新建组织机构的上级部门！");
            return;
        }
        meth="insert";
    }
    else if(n=="2")
        meth="update";
    else if(n=="3")
        meth="delete";
    var auto="no";
    if(document.frmPost.AUTOSYNC.checked) auto="yes";
        document.frmPost.action = strAction+"?method="+meth+"&auto="+auto;
    var res = getValueByPages([2]);
    if(res)
    {
        appendXML();
        doGlobalPost(document.frmPost.action);
        resetValueByPages([2]);
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
         g_xTabPane.setSelectedIndex(1);
    }
}

function doSearch(n)
{
     if(g_xDic)  g_xDic.hidden();  //关闭字典
    var i = g_xTabPane.selectedIndex;
    //alert(i);
    if(doQuery(0))
    {
        processbar.style.display="";
        tabList.querycondition = frmPost.queryXML.value;
        tabList.queryaction = frmPost.action = strAction+"?method=queryList";
        doGlobalQuery(document.frmPost.action,1,n,0,10);
        g_xTabPane.setSelectedIndex(1);
        processbar.style.display="none";
    }
}

function searchSon(obj,n)
{
    //将查询条件初始化为空
    queryForm.Q_ROW_ID.value="";
    queryForm.Q_DEPT_NAME.value="";
    queryForm.Q_DEPTTYPE.value="";
    var pid="";
    if(typeof(obj) == 'object')
    {
         pid = obj.ROW_ID ;//父部门的id
    }
    else
        pid = obj;
    queryForm.Q_DEPT_PARANT_ROWID.value=pid;  //查询页面赋值
    DEPT_PARANT_ROWID.value=pid;    //添加页面赋值
    if(doQuery(0))
    {
        processbar.style.display="";
        document.frmPost.action = strAction+"?method=queryList";
        doGlobalQuery(document.frmPost.action,1,n,0,15);
    //    alert(pid);
        createNavi(pid);
        queryForm.Q_DEPT_PARANT_ROWID.value="";
        processbar.style.display="none";
    }
}

//导航信息
function createNavi(pid)
{
    document.all.p_name.innerHTML="";
    document.all.p_name1.innerHTML="";
    var sActionURL="<%=request.getContextPath()%>/"+strAction+"?method=navigate&pid="+pid;
    var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
      xmlhttp2.Open("POST",sActionURL,false);
      xmlhttp2.Send(frmPost.queryXML.value);
      var res = xmlhttp2.responseText;
    xmlhttp2 = null;
//    var res = doPost(sActionURL);
    var xmlDom = createDOMDocument();

    xmlDom.loadXML(res);
     //     alert(res);
    var navStr ="";
    var message = xmlDom.selectSingleNode("//RESPONSE/@message");
    if(message != null  && message.text != null){
        navStr =  message.text;
    }
//    alert(navStr);
    var navList = navStr.split("|");
    if(navList != null && navList.length>0)
    {
        var str="";
    //    alert(navList.length);
        for(var i=0;i<navList.length-1;i+=2)
        {
        //    alert(i);
            if(i!=0)  str += " | ";
            str += '<a href="javascript:onclick=searchSon('+navList[i+1]+',1)">'+navList[i]+'</a>';
        }
        document.all.p_name.innerHTML=str;
        document.all.p_name1.innerHTML=str;
    }
    g_xTabPane.setSelectedIndex(1);
}

function tr_click(obj)
{
    obj.cells[0].firstChild.checked=!obj.cells[0].firstChild.checked;
    //将选取的行值填充到录入界面的对应输入框中
    setValueByPages(obj,[2]);
    //设置操作标记为更新状态
    g_xTabPane.setSelectedIndex(2);
//    ROW_ID.readonly="true";
}

function radio_click(obj)
{
    //将选取的行值填充到录入界面的对应输入框中
    setValueByPages(obj.row,[2]);
    //设置操作标记为更新状态
    g_xTabPane.setSelectedIndex(2);
//    ROW_ID.readonly="true";
}

function selectFunc(obj)
{
    if(obj.checked)
    {
        document.frmPost.createDic.disabled="true";
    }else
    {
        document.frmPost.createDic.disabled="";
    }
}
function executeRequest(sActionURL ,method,sendDatas,urlparas)
{
     var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
     target =sActionURL+"?method="+method ;

      if(urlparas)
        target +=("&"+    urlparas) ;

      xmlhttp2.Open("POST",target,false);
      xmlhttp2.Send(sendDatas);

      var responseText =xmlhttp2.responseText ;
      xmlhttp2 = null;
      return responseText ;
}
//生成组织机构xml文件
function createInDependent(tag)
{
    processbar.style.display="";

    var strAction= "<%=request.getContextPath()%>/DicTree/dicTreeAction.do";
    var result =executeRequest(strAction ,"expOrg_DeptDic" ,"","type="+tag)  ;
    if (!result)
         alert("操作失败,没有应答结果!") ;
    else
        alert(result) ;
    processbar.style.display="none";
}

//切换页签
function onSwitchPage()
{
    switch(g_xTabPane.selectedIndex)
    {
        case 0:
            showButtons(["cmdSearch"]);
            hideButtons(["cmdInsert","cmdUpdate","cmdDel"]);
            break;
        case 1:
            hideButtons(["cmdInsert","cmdUpdate","cmdDel","cmdSearch"]);
            break;
        case 2:
            showButtons(["cmdInsert","cmdDel"]);
            hideButtons(["cmdSearch","cmdUpdate"]);
            break;
        default:
            break;
    }
}

-->
</SCRIPT>
</head>
<body>
<app:dialogs/>

<div class="container-fluid">
  <div class="tab-page"><span class="tab" title="固定查询">固定查询</span>
    <div class="tab-layout">
      <form name="queryForm">
        <TABLE class="editTabTitle" width="98%" cellpadding="2" cellspacing="1">
          <TR>
            <TD class="label" >组织机构编号</TD>
            <TD class="editLabel" ><INPUT type="text" name="Q_ROW_ID" class="Edit" kind="text"   fieldType="text"  showInput="false"  fieldname="ROW_ID" operation="=" maxlength="30" > </TD>
          </TR>
          <TR>
            <TD class="label" >单位或部门名称</TD>
            <TD class="editLabel" ><INPUT type="text" name="Q_DEPT_NAME"  class="Edit" kind="text"   fieldType="text"  showInput="false"  fieldname="DEPT_NAME" operation="like" maxlength="100" > </TD>
          </TR>
           <TR>
            <TD class="label" >部门类型</TD>
            <TD class="editLabel" ><INPUT type="text" name="Q_DEPTTYPE"  class="Edit" kind="dic" src="BMJB"  fieldType="text"   showInput="false"  fieldname="DEPTTYPE" operation="=" maxlength="100" > </TD>
          </TR>
           <TR style="display:none">
            <TD class="label" >父类编号</TD>
            <TD class="editLabel" ><INPUT type="text" name="Q_DEPT_PARANT_ROWID"  class="Edit" kind="text"   fieldType="text" showInput="false"  fieldname="DEPT_PARANT_ROWID"  operation="=" value=""> </TD>
          </TR>
        </TABLE>
      </form>
    </div>
  </div>

  <div class="tab-page"><span class="tab" title="组织机构列表">组织机构列表</span>
    <div class="tab-layout">
      <TABLE id="tablePic" width="98%" border="0" cellpadding="2" cellspacing="1" >
        <TR>
          <TD id="p_name" ></TD>
        </TR>
      </TABLE>
      <br/>
      <ELEMENT:newcontrol style="" onproper="PreNextFlashRownum()" id="page" tablist="tabList"/>
      <table id="tabList" class="TabList" pagecontrol="page" width="750" frame="box" cellPadding="0" border="1" cellSpacing="0"borderColorLight="silver" borderColorDark="white" style="width:98%;">
        <TR class="TabListTitle" align="center">
          <TH type="single"></TH>
          <TH>部门编号</TH>
          <TH>部门名称</TH>
          <TH>行政区划</TH>
          <TH>上级部门</TH>
          <TH>部门类别</TH>
          <TH type='link' value="下级机构" action="searchSon(this.parentElement.parentElement,1)">下级机构</TH>
        </TR>
      </table>
    </div>
  </div>

  <div class="tab-page"><span class="tab" title="组织机构信息">组织机构信息</span>
    <div class="tab-layout">
      <TABLE id="tablePic" width="60%" border="0" cellpadding="2" cellspacing="1">
        <TR>
          <TD id="p_name1" ></TD>
        </TR>
      </TABLE>
      <br/>
      <TABLE class="editTabTitle" width="98%" cellpadding="2" cellspacing="1">
        <TR >
          <TD  class="label"  >组织机构编号</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" id="ROW_ID" name="ROW_ID" kind="text" showInput="false"  fieldname="ROW_ID"  must="true"  maxlength="30"  > </TD>
          <TD  class="label"  >组织机构名称</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" id="DEPT_NAME" name="DEPT_NAME" kind="text" showInput="false"  fieldname="DEPT_NAME"  must="true"  maxlength="40"  > </TD>
        </TR>
        <TR >
          <TD  class="label"  >是否有效状态</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" id="ACTIVE_FLAG" name="ACTIVE_FLAG" kind="dic" src="SF" showInput="false" fieldname="ACTIVE_FLAG" must="true" maxlength="1"> </TD>
          <TD  class="label"  >部门类别</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" id="DEPTTYPE" name="DEPTTYPE" kind="dic" src="BMJB" showInput="false"  fieldname="DEPTTYPE"    must="true"   maxlength="10"> </TD>
        </TR>
        <TR >
          <TD  class="label"  >组织机构简称</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" id="BMJC" name="BMJC" kind="text" showInput="false"  fieldname="BMJC"    must="true"  maxlength="30"  > </TD>
          <TD  class="label"  >所属省市区县</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" id="SSXQ" name="SSXQ" kind="dic" src="XZJG" showInput="false"  fieldname="SSXQ" must="true" maxlength="30"> </TD>
        </TR>
        <TR style="display:none;">
          <TD  class="label"  >上级部门</TD>
          <TD class="editLabel" ><INPUT type="text" class="Edit" id="DEPT_PARANT_ROWID" name="DEPT_PARANT_ROWID" kind="text" showInput="false"  fieldname="DEPT_PARANT_ROWID"    must="false"   readonly     maxlength="30" value=""  >
        </TR>
      </TABLE>
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
    <INPUT type="button" name="cmdInsert" class="TabButton" style="display:none" value="#S保  存"  onclick="SaveClick('1')">
    <INPUT type="button" name="cmdUpdate" class="TabButton" style="display:none" value="#S保  存"  onclick="SaveClick('2')">
    <INPUT type="button" name="cmdDel"    class="TabButton" style="display:none" value="#D删  除" onclick="SaveClick('3')">
    <INPUT type="button" name="createDic" class="TabButton" value="同步组织机构" onclick="createInDependent('zzjg')">
    <input type="checkbox" name="AUTOSYNC"  value="自动同步" onClick="selectFunc(this)">自动同步
  </FORM>
</div>
</body>
</html>
