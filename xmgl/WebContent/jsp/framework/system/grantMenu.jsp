<%@ page contentType="text/html;charset=GBK"%>

<%
    String strSelectId=(String) request.getAttribute("selectId");
    if(strSelectId == null){
          strSelectId="" ;
    }
    String levelName=request.getParameter("levelName");
    if(levelName == null){
        levelName = "";
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
var strSelectId='<%=strSelectId%>';
var selNode;
var strActionName='EapMenu/eapMenuAction';

var strShowHas="";
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

 /**   if (window.onunload != null) {
        var oldOnunload = window.onunload;
        window.onunload = function (e) {
            doFree();
            oldOnunload(e);
        };
    }
    else {
        window.onunload = doFree;
    }*/
}
function doInit(){
  if(strSelectId.length>0){
    var cObj=document.all("Span_"+strSelectId);
    if(cObj != null){
     selNode=cObj;
     showChildNode(cObj);
    }
  }
  //选中拥有的权限
beginShow();
}
//展开指定节点  包括父节点
function showChildNode(obj){
  sthis(obj.parentNode);
  while(obj != null ){

    if(obj.tagName=="tr" || obj.tagName=="TR"){
     if(obj.value=="p"){
       var tt=obj.previousSibling;
       //alert("showChildNode: "+tt.childNodes[0].childNodes[0].tagName);
       tt.childNodes[0].childNodes[0].src=img2;
      // menuChange(obj.childNodes[0].childNodes[0])
       obj.style.display="";
      }
    }
    obj=obj.parentNode;
  }
}


function onClickNode(selNodes){

  if(selNode != null){
     usthis(selNode.parentNode);
  }

  selNode=selNodes;

  sthis(selNode.parentNode);
  var strValues=selNode.value;
  var strId="";
  var strParentId="";
  var strDicCode="";
  var strDicValue="";
  var strDicSpell="";
  var strDicSpellA="";
  var strDicLayer1="";
  var strDicLayer2="";
  var iNum1=-1;
  var iNum2=-1;

  if(strValues != null){
    iNum1=strValues.indexOf("|");
    if(iNum1 > 0){
      strDicCode=strValues.substring(0,iNum1);
    }
    iNum2=strValues.indexOf("|",iNum1+1);
    if(iNum2 >0){
      strDicValue=strValues.substring(iNum1+1,iNum2);
      iNum1=iNum2;
    }
    iNum2=strValues.indexOf("|",iNum1+1);
    if(iNum2 >0){
      strId=strValues.substring(iNum1+1,iNum2);
      iNum1=iNum2;
    }
    iNum2=strValues.indexOf("|",iNum1+1);
    if(iNum2 >0){
      strParentId=strValues.substring(iNum1+1,iNum2);
      iNum1=iNum2;
    }
    iNum2=strValues.indexOf("|",iNum1+1);
    if(iNum2 >0){
      strDicLayer1=strValues.substring(iNum1+1,iNum2);
      iNum1=iNum2;
    }
    iNum2=strValues.indexOf("|",iNum1+1);
    if(iNum2 >0){
      strDicLayer2=strValues.substring(iNum1+1,iNum2);
      iNum1=iNum2;
    }
    iNum2=strValues.indexOf("|",iNum1+1);
    if(iNum2 >0){
      strDicSpell=strValues.substring(iNum1+1,iNum2);
      iNum1=iNum2;
    }
    iNum2=strValues.indexOf("|",iNum1+1);
    if(iNum2 >0){
      strDicSpellA=strValues.substring(iNum1+1,iNum2);
      iNum1=iNum2;
    }
  }
  if(strId.length > 0){
    //alert(strValues);
    doQuery( strId);
  }

}


function onDBLClickNode(selNodes){
/*modify by liuyang 20060227 屏蔽掉双击事件关闭窗口 start*/
// window.returnValue=selNodes.value;
// window.close();
  return;
/*modify by liuyang 20060227 屏蔽掉双击事件关闭窗口 end*/
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
//树状菜单对象
function Tree() {
  //菜单开始，bTop表示是否为最外层。
  this.start=function() {
    document.writeln("<table cellpadding=1 cellspacing=0>");
  }
  //菜单结束
  this.end=function() {
    document.writeln("</table>");
  }
  //为菜单增加项，参数依次为：菜单文字、菜单链接、是否有子菜单、子菜单是否显示  双击事件 节点值。
  this.addItem=function(cText,cLink,bChild,bShow,dbClick,values,sId) {
    document.writeln("<tr value='c'><td   nowrap");
    document.writeln(bChild?"onDBLClick='menuChange(this.childNodes[0])'>":">");
    document.writeln("<img id='Img_"+sId+"' border=0"+(bChild?" onclick=menuChange(this)":""));
    document.writeln(" src="+(bChild?(bShow?img2:img1):img3));
    document.writeln("> <span  id='Span_"+sId+"' onClick='"+cLink+"' value='"+values+"' onDBLClick='"+dbClick+"'>"+cText+"</span><input type='checkbox' name='cMenu' onclick='onMenuCheck(this)' value='"+sId+"'></td></tr>");
  }
  //子选项开始，bShow为是否显示
  this.childStart=function(bShow) {
    document.writeln("<tr value='p' style='display:"+(bShow?"block":"none")+"'><td>");
    document.writeln("<table cellpadding=1 cellspacing=0 style='margin-left:12px;'>");
  }
  //子选项结束
  this.childEnd=function() {
    document.writeln("</table>");
    document.writeln("</td></tr>");
  }
}

function menuChange(obj) { //控制菜单显示/隐藏
  obj=obj.parentNode.parentNode;
  obj.nextSibling.style.display=(obj.nextSibling.style.display=='none'?'block':'none');
  if (obj.nextSibling.style.display=='none')
    obj.cells[0].childNodes[0].src=img1;
  else
    obj.cells[0].childNodes[0].src=img2;
  obj.cells[0].childNodes[2].click();//空格为1
}

/**
* 录入，修改，删除等的提交
*/
function doQuery( strId) {
          var strAction= "<%=request.getContextPath()%>/"+strActionName+".do?method=";
          var meth="query&deptId="+strId;
          strAction += meth;
      var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
      xmlhttp2.Open("POST",strAction,false);
         xmlhttp2.Send("");
      getReady_onWSresult(xmlhttp2);
         xmlhttp2 = null;

}
function createDOMDocument()
{
    //在JSP调用的时候,文件和文件之间的调用,存在巨大的问题
    //alert("createDOMDocument调用可能存在问题");
    try {

        var objXML = new ActiveXObject("Msxml2.DOMDocument");
        objXML.async = false;
        return objXML
    }
    catch (e) {
        return null;
    }
}
function getReady_onWSresult(xmlhttp2) {
       var resonseXml = xmlhttp2.responseText;
      var xmlDom = createDOMDocument();
          // alert(resonseXml);
      xmlDom.loadXML(resonseXml);
     //调序使用
     var colNames=new Array('EAP_MENU___NAME','EAP_MENU___TITLE','EAP_MENU___PARENT','EAP_MENU___ORDERNO','EAP_MENU___TARGET','EAP_MENU___LOCATION','EAP_MENU___CHIEF','EAP_MENU___IMAGE','EAP_MENU___ALTIMAGE','EAP_MENU___DESCRIPTION','EAP_MENU___ROLES','EAP_MENU___APP_NAME','EAP_MENU___LAYERSNO');
      var ndperson = xmlDom.selectNodes("//RESPONSE/RESULT/ROW");
      //显示页面控制之后的各种条数信息
       //清空当前选中项
      strCurrentId="";


      for(var i=0;i<ndperson.length;i++){


        for(var j=0;j<colNames.length;j++){
          temp=ndperson[i].selectSingleNode(colNames[j]);
          if(temp != null){
            var obj= document.all(colNames[j]);
             if(obj != null){
              var sv = temp.getAttribute("sv");
              if(sv != null ){
               obj.value=temp.text;
               obj.value=sv;
              }else{
               obj.value=temp.text;
              }

             }
          }
        }
        break;
      }


}
function onMenuCheck(cValue)
{
       if(cValue.checked)
    {//有子
         setParentCheckTrue(cValue);
        setChildCheckTrue(cValue)
       }
       else
    {//子
         setChildCheckFalse(cValue);
    }
}
//
function setParentCheckTrue(cCheck){
  //alert("setParentCheckTrue");
  while(cCheck != null ){

    if(cCheck.tagName=="tr" || cCheck.tagName=="TR"){
     if(cCheck.value=="p"){
       var tt=cCheck.previousSibling;
       //alert("setParentCheckTrue: "+tt.childNodes[0].childNodes[3].tagName);
       tt.childNodes[0].childNodes[3].checked=true;
       //break;
      // menuChange(obj.childNodes[0].childNodes[0])
       //cCheck.style.display="";
      }
    }
    cCheck=cCheck.parentNode;
  }
}
//
function setChildCheckFalse(cCheck){

  if(cCheck != null ){
     //  alert("setChildCheckFalse:"+cCheck.tagName);
    if(cCheck.tagName == "INPUT" && cCheck.name =="cMenu"){
       cCheck.checked = false;
      while(cCheck != null ){

      if(cCheck.tagName=="tr" || cCheck.tagName=="TR"){
        //alert(typeof(cCheck.value));

        cCheck=cCheck.nextSibling;

       if(cCheck != null && typeof(cCheck.value) !="undefined" && cCheck.value == "p"){
      // alert("in");
        //alert("setParentCheckTrue: "+tt.childNodes[0].childNodes[3].tagName);
        //tt.childNodes[0].childNodes[3].checked=true;
        setChildCheckFalse(cCheck);
        break;
       }
      // menuChange(obj.childNodes[0].childNodes[0])
       //cCheck.style.display="";

       }
       if(cCheck != null){
       cCheck=cCheck.parentNode;
       }
      }
    }
     else if(cCheck.childNodes != null){
      // alert("else:"+cCheck.tagName);
       for(var n=0;n<cCheck.childNodes.length;n++){
         setChildCheckFalse(cCheck.childNodes[n]);
       }
    }

  }
}
//选中父节点时选中所有子节点 2007-4-18  yum
function setChildCheckTrue(cCheck)
{
    if(cCheck != null )
    {
          // alert("setChildCheckTrue:"+cCheck.tagName);
        if(cCheck.tagName == "INPUT" && cCheck.name =="cMenu")
        {
               cCheck.checked = true;
              while(cCheck != null )
            {
                  if(cCheck.tagName=="tr" || cCheck.tagName=="TR")
                {
                    cCheck=cCheck.nextSibling;
                       if(cCheck != null && typeof(cCheck.value) !="undefined" && cCheck.value == "p")
                    {
                        setChildCheckTrue(cCheck);
                        break;
                    }
                   }
               if(cCheck != null)
                    cCheck=cCheck.parentNode;
            }
        }else if(cCheck.childNodes != null)
        {
           for(var n=0;n<cCheck.childNodes.length;n++)
                 setChildCheckTrue(cCheck.childNodes[n]);
        }
    }
}

//
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

}
//
function showCheck(cCheck){

  if(cCheck != null ){
       //alert("setChildCheckFalse:"+cCheck.tagName);
    if(cCheck.tagName == "INPUT" && cCheck.name =="cMenu"){
       for(var n=0;n< strsPro.length;n++){
         if(strsPro[n]== cCheck.value){
           cCheck.checked = true;
           break;
         }
       }

      while(cCheck != null ){

      if(cCheck.tagName=="tr" || cCheck.tagName=="TR"){
       //if(cCheck.value=="p"){
        cCheck=cCheck.nextSibling;
       //alert("showCheck:"+cCheck.childNodes[0].childNodes[0].tagName);
        //alert("setParentCheckTrue: "+tt.childNodes[0].childNodes[3].tagName);
        //tt.childNodes[0].childNodes[3].checked=true;
        showCheck(cCheck);
        break;
      // menuChange(obj.childNodes[0].childNodes[0])
       //cCheck.style.display="";

       }
       cCheck=cCheck.parentNode;
      }
    }
     else if(cCheck.childNodes != null){
      // alert("else:"+cCheck.tagName);
       for(var n=0;n<cCheck.childNodes.length;n++){
         showCheck(cCheck.childNodes[n]);
       }
    }

  }


}

//
function onOk(){
   var strRes="";
   var strAdds= [];
   var strDels= [];
   for( var n=0;n<cMenu.length;n++){
      if(cMenu[n].checked){
      var isHas=false;
      for(var m=0;m< strsPro.length;m++){
         if(strsPro[m]==cMenu[n].value ){
           isHas = true;
           break;
         }
       }
       if(!isHas){
         strAdds[strAdds.length]=cMenu[n].value;
       }
     }
   }
   for(var n=0;n< strsPro.length;n++){
      var isHas=false;
      for( var m=0;m<cMenu.length;m++){
      if(cMenu[m].checked){
        var isHas=false;
        if(strsPro[n]== cMenu[m].value){
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
    //alert(strWillAdd);
   window.returnValue=strWillAdd+"#$#"+strWillDel;
   window.close();
}

function onCancel(){
   window.returnValue="";
   window.close();

}
</SCRIPT>
</head>
<BODY leftMargin=0 topMargin=0>
<br>
<table width="400" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#838383">
  <tr>
    <td bgcolor="#FFFFFF">
    <table width="400" height="302" border="0" align="center" cellpadding="0" cellspacing="5" bgcolor="#F7F7F7">
      <tr bgcolor="#F7F7F7">
        <td width="400" valign="top">
        <div id=divShow style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; OVERFLOW: auto; BORDER-LEFT: gray 1px solid; WIDTH: 400px; BORDER-BOTTOM: gray 1px solid; HEIGHT: 300px">
        <table id="tMenu">
            <TR>
            <TD  align="left" valign="top">
                <framework:menuTree roleName="<%=%>"/>
            </TD>
            </TR>
        </table>
        </div>
        </TD>
        <TD align="left" valign="top">
            <input type="button" class="TabButton" name="cmdOk" value="确定" onclick="onOk()">
            <input type="button" class="TabButton" name="cmdCancel" value="取消" onclick="onCancel()">
        </TD>
    </TR>
</TABLE>
</body>
</html>

