<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="org.dom4j.Document"%>
<%@ page import="org.dom4j.*"%>
<%
	User user = (User)session.getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String userid = user.getAccount();
	String deptid = user.getDepartment();
%>
<HTML><head>
<title></title>
<meta http-equiv="Content-Type" content="text/html;charset=GBK">
<app:base/>
<app:checkLogon/>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/js/aj/zfba.js"></SCRIPT>
<SCRIPT language="JavaScript">
<!--
//action变量定义
var strAction="<%=request.getContextPath()%>/OrgDept/OrgPrintAction.do";
//页面初始化
function setNameMode(obj,strid)
{
	if(obj.checked)
	{
		document.getElementById(strid).operation = 'like';
		document.getElementById(strid).value = '%';
	}
	else
	{
		document.getElementById(strid).operation = '=';
		document.getElementById(strid).value = '';
	}
}
function doTabListRadioAll(obj)
{
   inBtns(["cmdQy","cmdJy"]);
   doReset();
}

//行选中事件
function tr_click(obj)
{

    obj.cells[0].firstChild.checked = true;
    setValueByPages(obj,[1]);
    if(obj.ACTIVEFLAG=="01"){
      unBtns(["cmdQy"]);
      inBtns(["cmdJy"]);
    }else{
      unBtns(["cmdJy"]);
      inBtns(["cmdQy"]);
    }
    unBtns(["cmdInsert","cmdReset","btnAzt"]);
    g_xTabPane.setSelectedIndex(1);
    var action ="http://<%=request.getHeader("host")%><%=request.getContextPath()%>/OrgDept/OrgPrintAction.do?method=getAztXML&xh="+obj.XH;
    AztSow1.ESAUnmarshal(action); //解析xml格式印章信息
    var v = GetAbsoluteLocation("PRINT");
     if(v)
    doMove(v[1], v[0], "AztSow1");
    AztSow1.ESAVerifySubmit();//校验印章信息

}
//
function radio_click(obj)
{
    setValueByPages(obj.row,[1]);
    if(obj.parentElement.ACTIVEFLAG=="01"){
      unBtns(["cmdQy"]);
      inBtns(["cmdJy"]);
    }else{
      unBtns(["cmdJy"]);
      inBtns(["cmdQy"]);
    }
    unBtns(["cmdInsert","cmdReset","btnAzt"]);
    var action ="http://<%=request.getHeader("host")%><%=request.getContextPath()%>/OrgDept/OrgPrintAction.do?method=getAztXML&xh="+obj.parentElement.XH;
    AztSow1.ESAUnmarshal(action); //解析xml格式印章信息
    var v = GetAbsoluteLocation("PRINT");
     if(v)
    doMove(v[1], v[0], "AztSow1");
    AztSow1.ESAVerifySubmit();//校验印章信息
}
//删除
function doDelete(obj)
{
	window.event.cancelBubble = true;
	if(!confirm('请确认是否删除？'))return;
	var action = strAction+"?method=Delete";
	var row = '<DATAINFO>'+(obj.dataxml?obj.dataxml:obj.rowxml)+'</DATAINFO>'
	frmPost.txtXML.value = row;
	if(doGlobalPost(action))
	{
    		obj.removeNode(true);
	}
  clearTxtXML();
}
//显示详细信息
function doDetail(obj)
{
     var url ="<%=request.getContextPath()%>/jsp/xt/orgprint/orgPrintdetail.jsp";
     window.open ("#", "newwindow", "height=500, width=600, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=no, status=no");
     document.frmPost.action=url;
     document.frmPost.rowData.value = obj.rowxml;
     document.frmPost.target="newwindow";
     document.frmPost.submit();
}
function ESAGetData(control){
            return "" ;
}

function delSign()
{
    AztSow1.ESADelSign();
    AztSow1.style.pixelWidth = 1;
    AztSow1.style.pixelHeight = 1;
}

//插入
function doInsert()
{
    if(PRINT.value.length==0){
      alert("请加盖印章！");
      return;
    }
    var action = strAction+"?method=Insert";
    if (getValueByPages([1]))
        {
            if (appendXML())
            {
                if(doGlobalPost(action))
                {
                    resetValueByPages([1]);
                    tabList2.insertResult(document.frmPost.resultXML.value,1);
                    qyYz("01",tabList2.rows[1].XH,tabList2.rows[1].JG,tabList2.rows[1].PRINTKIND);
                    delSign();
                }
                    clearTxtXML();
            }
        }
}
//编辑
function doUpdate()
{
	  if(tabList3.getSelectedRowIndex()==-1)
    {
           alert('请选择需要编辑的数据!');
           g_xTabPane.setSelectedIndex(1);
           return ;
    }
    var action = strAction+"?method=Update";
    if (getValueByPages([1]))
        {
            if (appendXML())
            {
                if(doGlobalPost(action))
                {
                    tabList3.updateResult(document.frmPost.resultXML.value,tabList3.getSelectedRowIndex());
                }
				    clearTxtXML();
            }
        }
}
//重置编辑录入区域
function doReset()
{
        delSign();
        inBtns(["cmdInsert","cmdReset","btnAzt"]);
	resetValueByPages([1]);
	clearTxtXML();
}
//查询
function doSearch (n)
{	resetValueByPages([1]);
	var i = g_xTabPane.selectedIndex;
	if((i==0 && doQuery(i)))
	{
		processbar.style.display="";
		document.frmPost.action = strAction+"?method=QueryList";
		if(doGlobalQuery(document.frmPost.action,0,0,0,10))
		{
			g_xTabPane.setSelectedIndex(0);
		}
		processbar.style.display="none";
	}
}
//初始化页面
function doInit()
{
     showButtons(["cmdSearch"]);
     hideButtons(["cmdInsert","cmdReset","btnAzt","cmdQy","cmdJy"]);
}
//切换页签
function onSwitchPage()
{
	if(g_xDic && g_xDic.edit)
    {
       g_xDic.edit.blur();
       g_xDic.hidden();
    }
	switch(g_xTabPane.selectedIndex)
	{
		case 0:
                        showButtons(["cmdSearch"]);
                        hideButtons(["cmdInsert","cmdReset","btnAzt","cmdQy","cmdJy"]);
			break;
		case 1:
                        showButtons(["cmdInsert","cmdReset","btnAzt","cmdQy","cmdJy"]);
                        hideButtons(["cmdSearch"]);
			break;
        case 2:
                        hideButtons(["cmdSearch","cmdInsert","cmdReset"]);
			break;
		default:
			break;
	}
}
function linkClick()
{
    var xh = event.srcElement.xh;
    var linksrc = event.srcElement.linksrc;
	g_xPucker.show(document.all('rowData'),linksrc,	"XH.value");
}
	function resizeControl(c, a, b)
	{
		doResizeControl(a, b, c);
	}
	function doResizeControl(a, b, c)
	{
		document.all[c].style.visibility = "hidden";
		document.all[c].style.pixelWidth = a;
		document.all[c].style.pixelHeight = b;
		document.all[c].style.visibility = "visible";
	}
	function moveControl(c, a, b)
	{
		doMove(a, b, c);
	}
	function doMove(a, b, c)
	{
		document.all[c].style.visibility = "hidden";
		var nw = document.all[c].style.pixelWidth;
		var nh = document.all[c].style.pixelHeight;
		document.all[c].style.left = a;
		document.all[c].style.top = b;
		document.all[c].style.visibility = "visible";
	}
function GetAbsoluteLocation(objName)
{
	var element = window.document.getElementById(objName);
    if ( arguments.length != 1 || element == null )
    {
        return null;
    }
    var offsetTop = element.offsetTop;
    var offsetLeft = element.offsetLeft;
    var offsetWidth = element.offsetWidth;
    var offsetHeight = element.offsetHeight;
    while( element = element.offsetParent )
    {
        offsetTop += element.offsetTop;
        offsetLeft += element.offsetLeft;
    }
    	var labels = new Array(offsetTop,offsetLeft,offsetWidth,offsetHeight);
		return labels;
}

function addSign(){
		var lRet = 0;
			lRet = AztSow1.ESAOnAddNew();
			var v = GetAbsoluteLocation("PRINT");
			//定位样章坐标。定位在指定签章区域
			doMove(v[1], v[0], "AztSow1");
			if (lRet != 0){
				AztSow1.ESAShowErrorMsg(lRet);
			}
		var nRet = AztSow1.ESAVerifySubmit();
		//alert(AztSow1.ESAMarshal());
		if (nRet <= 0){
			if (nRet == 0){
				//不能提交只加盖了样章的表单。
				alert("不能提交只加盖了样章的表单");
			}else if (nRet == -1){
				//验证失败
				alert("需求方印章验证失败！");
			}else{
				//其他错误
				AztSow1.ESAShowErrorMsg(nRet);
			}
			return;
		}else if(nRet == 1){
       //验证无误，提交印章信息
                    document.all("PRINT").value =  AztSow1.ESAMarshal();
		}else{
			//其他情况，不提交印章信息
			//document.all["requesterSign"].value = "";
            	alert("其他情况，不提交印章信息");
		}
}
function doQybs(bs){
    var rowIndex = tabList2.getSelectedRowIndex();
    if(rowIndex==-1){
     alert("请选择一条记录！");
     return ;
    }
    var action = strAction+"?method=QyYz&bs="+bs;
    if (getValueByPages([1]))
    {
      if (appendXML())
      {
       if(doGlobalPost(action))
        {
         qyYz(bs,tabList2.getSelectedRow().XH,tabList2.getSelectedRow().JG,tabList2.getSelectedRow().PRINTKIND);
         //tabList2.updateResult(document.frmPost.resultXML.value,tabList2.getSelectedRowIndex());
        }
         clearTxtXML();
       }
     }

}
function qyYz(bs,xh,jg,printkind){
   if(tabList2.rows.length>1){
     for(var i =1;i<tabList2.rows.length;i++)
     {
       if(bs =="01"){
        if(tabList2.rows[i].XH ==xh){
          tabList2.rows[i].ACTIVEFLAG = "01";
          tabList2.rows[i].cells[4].innerText = "有效";
        }else if(tabList2.rows[i].JG==jg&&tabList2.rows[i].PRINTKIND == printkind){
          tabList2.rows[i].ACTIVEFLAG = "02";
          tabList2.rows[i].cells[4].innerText = "过期";
        }
       }else{
        if(tabList2.rows[i].XH ==xh){
          tabList2.rows[i].ACTIVEFLAG = "02";
          tabList2.rows[i].cells[4].innerText = "过期";
        }
       }
     }
    if(bs=="01"){
      unBtns(["cmdQy"]);
      inBtns(["cmdJy"]);
    }else{
      unBtns(["cmdJy"]);
      inBtns(["cmdQy"]);
    }
   }
}


//-->
</SCRIPT>
</head>
<body leftmargin="10" topmargin="0">
<object classid="clsid:49FB4BA2-3A7A-4635-91CD-9C2D5470F8CF" CODEBASE="<%=request.getContextPath()%>/cab/AztSOW.CAB#version=1,0,0,0" style="position:absolute;top:300;left:100;width:1;height:1;z-index=8" width="1" height="1"
id="AztSow1">
</object>

<div id="processbar" class="loading" style="display:none" align="center">正在处理请求，请稍候...</div>
<!-- 查询条件定义区域-->
<div class="tab-pane" width="100%" id="tabPane" style="display:none" selectedIndex="0">
	<div class="tab-page"><span class="tab" title="查询条件页面数据">查询数据</span>
  	<div class="tab-layout">
        <TABLE class="editTabTitle" width="98%" cellpadding="2" cellspacing="1">
         <TR  style="display:none;">
	        <TD class="label" ></TD>
			<TD><INPUT type="text" class="Edit" kind="text"  fieldname="rownum"  value="1000" operation="&lt;=" ></TD>
        </TR>
         <TR >
	        <TD class="label" >组织结构</TD>
			<TD class="editLabel"><INPUT type="text" class="Edit" kind="dic" src="ZZJG" fieldname="JG"   operation="=" ></TD>
	        <TD class="label" >印章类别</TD>
			<TD class="editLabel"><INPUT type="text" class="Edit" kind="dic" src="YZLB" fieldname="PRINTKIND"   operation="=" >
           <span class="label"><input type="text" class="Edit" kind="text" fieldname="dd" style="width:0;"></span></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
               </TABLE>
      <ELEMENT:newcontrol style="" onproper="PreNextFlashRownum()"/>
        <TABLE id="tabList2" class="TabList"  cellPadding="0" cellSpacing="0" frame="box" border="1" borderColorLight="silver" borderColorDark="white" style="width:100%;">
            <tr class="TabListTitle" align="center">
            <TH type="single"></TH>
                         <TH fieldname="JG" maxlength="10">组织结构</TH>
                         <TH fieldname="PRINTKIND" maxlength="10">印章类别</TH>
                         <TH fieldname="TBSJ" maxlength="10">入库时间</TH>
                         <TH fieldname="ACTIVEFLAG" maxlength="10">是否有效</TH>
            </tr>
       </TABLE>
     </div>
     </div>
    <div class="tab-page"><span class="tab" title="组织机构印章">组织机构印章</span>
    <div class="tab-layout">
    	    <!--主键隐藏域-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="XH"  kind="text"   showInput="false"  fieldname="XH"    ></div><!--序号-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="ACTIVEFLAG"  kind="dic"   showInput="false"  fieldname="ACTIVEFLAG"    ></div><!--是否有效-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="TBR"  kind="text"   showInput="false"  fieldname="TBR"    ></div><!--填报人-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="SJBH"  kind="text"   showInput="false"  fieldname="SJBH"    ></div><!--事件编号-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="YWLX"  kind="text"   showInput="false"  fieldname="YWLX"    ></div><!--业务类型-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="TBSJ"  kind="date"   showInput="false"  fieldname="TBSJ"    ></div><!--填报时间-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="SJMJ"  kind="text"   showInput="false"  fieldname="SJMJ"    ></div><!--数据密级-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="GXSJ"  kind="date"   showInput="false"  fieldname="GXSJ"    ></div><!--更新时间-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="GXR"  kind="text"   showInput="false"  fieldname="GXR"    ></div><!--更新人-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="JWQ"  kind="text"   showInput="false"  fieldname="JWQ"    ></div><!--警务区-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="ZRQ"  kind="text"   showInput="false"  fieldname="ZRQ"    ></div><!--责任区-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="SSPCS"  kind="text"   showInput="false"  fieldname="SSPCS"    ></div><!--所属派出所-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="SSXJGAJG"  kind="text"   showInput="false"  fieldname="SSXJGAJG"    ></div><!--所属县级公安机关-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="SSSSXQ"  kind="text"   showInput="false"  fieldname="SSSSXQ"    ></div><!--所属省市县(区)-->
             <div class="label"><INPUT type="hidden" class="Edit"  id="ZHUX"  kind="text"   showInput="false"  fieldname="ZHUX"    ></div><!--注销-->
			     	    <!--主键隐藏域-->
           <TABLE width="100%" class="editTabTitle"  cellpadding="2" cellspacing="1" >
             <TR>
                <TD class="label" >组织结构</TD>
                <TD class ="editLabel"><INPUT type="text" class="Edit" id="JG"  name="JG" dicwidth="500" kind="dic" src="ZZJG" showInput="false" fieldname="JG"  must="true"  > </TD>
                <TD class="label" >印章类别</TD>
                <TD class ="editLabel"><INPUT type="text" class="Edit" id="PRINTKIND"  name="PRINTKIND"  kind="dic" src="YZLB" showInput="false" fieldname="PRINTKIND"  must="true"  > </TD>
             </TR>
             <TR>
                <TD class="label" >备注</TD>
                <TD colspan="4"  class ="editLabel"><textarea rows="8" cols="20" id="MEMO"  name="MEMO"   style="width:510px;height:40px" class="Edit" kind="text" fieldname="MEMO"  must="false" maxlength="100"  ></textarea></TD>
             </TR>
      </TABLE>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <br/>
             <TABLE>
             <TR>
                <TD  class ="label"><INPUT type="text" class="Edit" id="PRINT"  name="PRINT" style="width:0;"  kind="text"  showInput="false" fieldname="PRINT"  must="false"  ></TD>
             </TR>
            </TABLE>
    </div>
    </div>
    </div>
    <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML">
         <input type="hidden" name="txtXML">
         <input type="hidden" name="txtFilter">
         <input type="hidden" name="resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		 <!--按钮定义区域-->
         <INPUT type="button" name="cmdSearch" id="cmdSearch" class="Button" value="#Q查  询" onclick="doSearch(1);">
         <INPUT type="button" name="cmdInsert" id="cmdInsert" class="Button" value="#I保  存" onclick="doInsert();">
         <INPUT type="reset"  name="cmdReset"  id="cmdReset"  class="Button" value="#R重  置" onclick="doReset();">
         <input type="button" name="btnAzt" id="btnAzt"  class="button"  value="#G加盖印章"   onClick="addSign()">
         <INPUT type="button" name="cmdQy" id="cmdQy" class="Button" value="#A启  用" onclick="doQybs('01');">
         <INPUT type="button" name="cmdJy" id="cmdJy" class="Button" value="#B禁  用" onclick="doQybs('02');">

 </FORM>
</div>
</body>
</html>
