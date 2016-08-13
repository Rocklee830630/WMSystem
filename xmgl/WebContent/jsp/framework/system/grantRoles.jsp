<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.coreapp.orgmanage.*"%>
<%
     String account=request.getParameter("account");
     if(account == null ){
           account =  "";
     }
    String levelName = request.getParameter("levelName");
    if(Pub.empty(levelName))
    {
        User user = UserManager.getInstance().getUserByLoginName(account);
        if(user != null)
        {
            levelName = user.getLevelName();
        }
        if(Pub.empty(levelName))
            levelName="";
    }
%>

<html>
<head>

<LINK type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/default.css">

<SCRIPT language=javascript >
var sobj=null;
var sc='#CCCCFF',usc='F1F3F2';

var img1='<%=request.getContextPath()%>/images/default/collapse.gif';//+.gif
var img2='<%=request.getContextPath()%>/images/default/expand.gif';//-.gif
var img3='<%=request.getContextPath()%>/images/default/item.gif';//p.gif

var strsPro = [];
// 缚上 onLoad 事件处理
if (typeof(window.attachEvent) != "undefined") {
    window.attachEvent("onload", doInit);
   // window.attachEvent("onunload", doFree);
}
else {
    if (window.onload != null) {
        var oldOnload = window.onload;
        window.onload = function (e) {
            doInit();
            oldOnload(e);
        };
    }
    else {
        window.onload = doInit;
    }
}
function doInit(){
/*  if(strSelectId.length>0){
    var cObj=document.all("Span_"+strSelectId);
    if(cObj != null){
     selNode=cObj;
     showChildNode(cObj);
    }
  }
 */ //modified by xukx 20061211 没有用到树型结果
 // beginShow();

}


function beginShow(){
  var tMenu=document.all("tMenu");
 //strShowHas="eroot|wwww|";
 var num= strShowHas.indexOf("|");
 var num2=0;
 while(num >= 0){
   strsPro[strsPro.length]= strShowHas.substring(num2,num);

   num2=num+1;
   num=strShowHas.indexOf("|",num2);
 }
//alert("beginShow"+strShowHas);
 // showCheck(tMenu);
}
//

function onDBLClickNode(selNodes){
 window.returnValue=selNodes.value;
 window.close();
}

function msthis(obj){
  if (event.button!=2){
    obj.style.backgroundColor=sc;
    if (sobj)
      sobj.style.backgroundColor=usc;

  }else{
    //return true;
    alert("右键!");
  }
}
function sthis(obj){
  if (sobj) sobj.style.backgroundColor=usc;
  obj.style.backgroundColor=sc;
}

function usthis(obj){
  sobj=obj;
  obj.style.backgroundColor=usc;
}
function snode(){
  if (sobj)
    sobj.style.backgroundColor=sc;
}

function onOk(){
   var strRes="";
   var strAdds= [];
   var strDels= [];
   var strChecked="";
  // alert(cRole.length);
   for( var n=0;n<cRole.length;n++){
     if(cRole[n].checked){
      var isHas=false;
      for(var m=0;m< strsPro.length;m++){
         if(strsPro[m]== cRole[n].value){
           isHas = true;
           break;
         }
       }
       if(!isHas){
         strAdds[strAdds.length]=cRole[n].value;
       }
       if(strChecked.length >0){
         strChecked+=',';
       }
         strChecked+=cRole[n].text;
     }
   }
   for(var n=0;n< strsPro.length;n++){
      var isHas=false;
    for( var m=0;m<cRole.length;m++){
     if(cRole[m].checked){
      var isHas=false;
        if(strsPro[n]== cRole[m].value){
           isHas = true;
           break;
         }
     }
    }
    if(!isHas){
      strDels[strDels.length]= strsPro[n];
    }
   }
   var strWillAdd="";
   for(var n=0;n<strAdds.length;n++){
      strWillAdd+=strAdds[n]+"|";
   }
   var strWillDel="";
   for(var n=0;n<strDels.length;n++){
      strWillDel+=strDels[n]+"|";
   }
   window.returnValue=strWillAdd+"#$#"+strWillDel+"#$#"+strChecked;
   window.close();
}

function onCancel(){
   window.returnValue="";
   window.close();

}
</SCRIPT>
</head>
<BODY leftMargin=0 topMargin=0 bgcolor="#838383">
<table width="440" height="50%" style="overflow-y:auto" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#838383">
  <tr>
    <td bgcolor="#FFFFFF">
        <DIV style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; OVERFLOW: auto; BORDER-LEFT: gray 1px solid; WIDTH: 100%; BORDER-BOTTOM: gray 1px solid; HEIGHT: 400px">
            <table width="400"  id="tMenu" class="TabList"
                cellPadding="1" border="1" cellSpacing="1" borderColorLight="silver"    borderColorDark="white">
            <TR align="center">
            <framework:roleMake userId="<%=account%>" levelName="<%=levelName%>"/>
            </TR>
            </table>
        </DIV>
    </td><td valign="top" bgcolor="#FFFFFF"><table align="right" valign="top">
    <tr>
        <TD align="right">
            <input type="button" name="cmdOk" value="确定" onclick="onOk()">
            <input type="button" name="cmdCancel" value="取消" onclick="onCancel()">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </TD>
    </TR>
</TABLE></td></tr></table>

</body>
</html>

