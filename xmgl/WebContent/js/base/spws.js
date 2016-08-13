/*
 * 该js文件将执法办案模块中能够用到的一些通用的方法整理到
 * 该js文件中,整理后将接口加到下边
 * 现有接口：
 * CQSP(AJBH,SJBH,YWLX,BusName)			通用呈请审批
 * openDoc(docID,SJBH,YWLX,isEdit,b,arg)通用制作、预览文档
 *
 *
 */
var g_sAppName = "/xmgl";
var spRes = "";
var strActionSP     = g_sAppName+"/SPAction.do";//审批用action
var strActionrole  = g_sAppName+"/PubWS.do";//判断角色存在

function doCqsp (obj){

  //alert(obj.name);
    var AJBH,SJBH,YWLX,BusName,AJMC,tablist,condition;
    AJBH = obj.AJBH;
    SJBH = obj.SJBH;
    YWLX = obj.YWLX;
    AJMC = obj.AJMC;
    tablist = obj.tablist;
    condition = obj.condition;

    var strUrl = g_sAppName+"/jsp/framework/common/spFlow/mdSpFlowView.jsp?sjbh="+SJBH+"&ywlx="+YWLX+"&ajbh="+AJBH+"&ajmc="+AJMC;
	if(condition&&condition!="undefined")
   		strUrl += "&condition="+condition;
   	var res=window.showModalDialog( strUrl,window,'dialogWidth:610pt;');
   	if(res == null||res==""){
	 	return false;
	}

    strAction =strActionSP+"?StartSP&isEdit=1&eventid="+SJBH+"&ywlx="+YWLX+"&ajbh="+AJBH+"&title="+AJMC+"&cqnr="+AJBH+YWLX;
    if(res){
    	strAction+='&dbr='+res;
    }
    if(condition&&condition!="undefined") //add by songxb@2008-06-12
           strAction += "&condition="+condition;
	$.ajax({
		url : strAction,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			return true;
		},
	    error : function(result) {
	    	return false;
	    }
	});
}
/*
 * 获取返回值
 * obj:审批选择人员的账号
 */
/*function getCqspRes(obj){
	spRes = obj;
}*/
//弹出区域回调
getWinData = function(data){
	
	var jsrStr= "",jsdwStr="";
	for(var i=0;i<data.length;i++){
		var tempJson = data[i];
		jsrStr +=tempJson.ACCOUNT+",";
		jsdwStr +=tempJson.DEPTID+",";
		
	}
	jsrStr = jsrStr.substring(0,jsrStr.length-1);
	jsdwStr = jsdwStr.substring(0,jsdwStr.length-1);

	submitSPTS(jsrStr,jsdwStr);
	
};
var commActionUrl = "";
function doCqspnolevel (obj){
	
 
    var AJBH,SJBH,YWLX,BusName,AJMC,tablist,condition,con;
    AJBH = obj.AJBH;
    SJBH = obj.SJBH;
    YWLX = obj.YWLX;
    AJMC = obj.AJMC;
    tablist = obj.tablist;
    con =  obj.con;
    var dbr = obj.dbr;
    condition = obj.condition;
    operationoid = obj.operationoid;
    var mind = obj.mind;
	
	commActionUrl =strActionSP+"?StartSP&isEdit=1&eventid="+SJBH+"&ywlx="+YWLX+"&ajbh="+AJBH+"&title="+AJMC+"&cqnr="+AJBH+YWLX;
	commActionUrl += "&condition="+condition+"&operationoid="+operationoid+"&dbr="+dbr+"&mind="+mind;
	submitSP(JSON.stringify(obj));
 	
}

function doCqspTS (obj){

	  //alert(obj.name);
	    var AJBH,SJBH,YWLX,BusName,AJMC,tablist,condition,con;
	    AJBH = obj.AJBH;
	    SJBH = obj.SJBH;
	    YWLX = obj.YWLX;
	    AJMC = obj.AJMC;
	    tablist = obj.tablist;
	    con =  obj.con;
	    dbr = obj.dbr;
	    condition = obj.condition;
	    operationoid = obj.operationoid;
	    isReStart = obj.isReStart;

		commActionUrl =strActionSP+"?StartSP&isEdit=1&eventid="+SJBH+"&ywlx="+YWLX+"&ajbh="+AJBH+"&title="+AJMC+"&cqnr="+AJBH+YWLX;
		commActionUrl += "&condition="+condition+"&operationoid="+operationoid+"&dbr="+dbr+"&isReStart="+isReStart;
		submitSP(JSON.stringify(obj));
		
	   	
}
//弹出区域回调
function submitSP(obj)
{
    var data = defaultJson.packSaveJson(obj);
	$.ajax({
		url : commActionUrl,
		data : data,
		dataType : 'json',
		async :	false,
		type : 'post',
	//	contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			$(window).manhuaDialog.getParentObj().prcCallback();
			$(window).manhuaDialog.getParentObj().getMessage(result.message);
			$(window).manhuaDialog.close();
			
			return true;
		},
	    error : function(result) {
	    	return false;
	    }
	});

}
function getMessage(msg)
{
	xAlert("运行结果",msg);
	
}

function hasSprole(templateid,ywlx)
{
	var action1 = strActionrole+"?getSproles&ywlx="+ywlx+"&templateid="+templateid;

	var hasroles = null;
	$.ajax({
		url : action1,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//alert(123);
		       //xmlDoms.loadXML(result);
		      
			if(result.MSG=="1")
			{
				hasroles = result.hasroles;
			}
			
		},
	    error : function(result) {
	    	xAlert("查询角色失败","");
	    	hasroles =  false;
	     	//alert(234);
	    }
	});
	return hasroles;
} 

function isTs(wsid,ywlx,operationoid)
{
	var action1 = strActionrole+"?getTsMethod&wsid="+wsid+"&ywlx="+ywlx+"&operationoid="+operationoid;

	var isTs = false;
	$.ajax({
		url : action1,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//alert(123);
		       //xmlDoms.loadXML(result);
		      
			if(result.MSG=="1")
			{
				isTs = result.isTs;
			}
			
		},
	    error : function(result) {
	    	xAlert("查询流程失败","");
	    	isTs =  false;
	     	//alert(234);
	    }
	});
	return isTs;
} 
//弹出区域回调
function submitSPTS(obj)
{
	
	var commActionUrl =strActionSP+"?StartSP&isEdit=1&eventid="+sjbh+"&ywlx="+ywlx+"&ajbh="+ajbh+"&title="+""+"&cqnr=";
	commActionUrl += "&condition=&operationoid="+operationoid;;
	//替换呈请特送审批的方法,使用StartSPTs方法
	//    
    commActionUrl+='&dbr='+selectRY+'&dbdw='+selectDW;
   
  // alert(commActionUrl); 
    
	$.ajax({
		url : commActionUrl,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//alert('sfsdfds');

			$(window).manhuaDialog.getParentObj().getMessage(result.message);
			$(window).manhuaDialog.close();
			
			return true;
		},
	    error : function(result) {
	    	return false;
	    }
	});

}
// 呈请审批
//该方法只适用于通用刑事的呈请类文书,特殊的审批业务不要使用该方法
/*
AJBH 案件编号
SJBH 事件编号
YWLX 业务类型
BusName 业务名
AJMC  案件名称
tablist tablist控件
*/
function CQSP(AJBH,SJBH,YWLX,BusName,AJMC,tablist,condition,operationoid,dbr,dbrName,mind)
{
    
    strAction='';
    if(SJBH==null || SJBH.length==0)
	{
		alert('事件编号为空,请检查事件编号域!');
		return;
	}
	if(BusName) BusName = "<"+BusName+">";
	else
	  BusName = "";
	 //审批流程选择 addby zhangj 2009年7月22日
	//如果页面中设置了审批级别，则直接发起审批
	if(dbrName==undefined){
		dbrName = "";
	}
	
	if(condition&&condition!="undefined"){
		var obj = {AJBH:AJBH,SJBH:SJBH,YWLX:YWLX,BusName:BusName,AJMC:AJMC,tablist:tablist,condition:condition,operationoid:operationoid,dbr:dbr,mind:mind};//此处为入参
		doCqspnolevel(obj);
	}
  //没有设置级别需要判断：一个审批流程 直接发起审批； 多个审批流程 弹出选择页面
  else {
		
      if(operationoid!=null){//一个审批流程
    	var res = null;
  		var obj = {AJBH:AJBH,SJBH:SJBH,YWLX:YWLX,BusName:BusName,AJMC:AJMC,tablist:tablist,condition:condition,operationoid:operationoid,dbr:dbr,mind:mind};//此处为入参
  		doCqspnolevel(obj);	
  		
      }
           
  	}
}

	/*
	 * 呈请特送审批
	*/
	
	function CQSPTS(AJBH,SJBH,YWLX,BusName,AJMC,tablist,condition,operationoid,dbr,dbrName,isReStart)
	{
	    strAction='';
	    if(SJBH==null || SJBH.length==0)
		{
			alert('事件编号为空,请检查事件编号域!');
			return;
		}
		if(BusName) BusName = "<"+BusName+">";
		else
		  BusName = "";
		 //审批流程选择 addby zhangj 2009年7月22日
		//如果页面中设置了审批级别，则直接发起审批

		dbrName = !dbrName ? "" : dbrName;
		if(condition&&condition!="undefined"){
			var obj = {AJBH:AJBH,SJBH:SJBH,YWLX:YWLX,BusName:BusName,AJMC:AJMC,tablist:tablist,condition:condition,operationoid:operationoid,dbr:dbr,isReStart:isReStart};//此处为入参
			doCqspTS(obj);
	  }
	  //没有设置级别需要判断：一个审批流程 直接发起审批； 多个审批流程 弹出选择页面
		
	  else {
	    	  var res = null;
	    	  var obj = {AJBH:AJBH,SJBH:SJBH,YWLX:YWLX,BusName:BusName,AJMC:AJMC,tablist:tablist,condition:condition,operationoid:operationoid,dbr:dbr,isReStart:isReStart};//此处为入参
	  		  doCqspTS(obj);
	  }
	}
/*
该方法为通用的审批调用方法
审批成功后会在页面上查找 EVENTSJZT域,并且将值填充
同时将对应的tabList控件上的选中行锁头
需要传递的参数为
sjbh 事件编号
ywlx 业务类型
tablist 列表控件
tile    案件名称或者是其他信息
*/
function createSpFlow(sjbh,ywlx,tablist,title)
{
    var action = strActionSP + "?StartSP&eventid="+sjbh+"&ywlx="+ywlx+"&title="+title;
    if(doGlobalPost(action))
    {
		var eventsjzt = document.getElementById('EVENTSJZT');
	    if(eventsjzt != null)
		{
			eventsjzt.value = '1';
		}
		if(tablist !=null && tablist != 'undefine')
        {
			if(tablist.rows !=null && tablist.rows.length>0)
			{
                var image = g_sImgPath+"state_lock.gif";
                var selectedRow = tablist.rows[tablist.getSelectedRowIndex()];
                //selectedRow.firstChild.firstChild.style.display="none";
                var imageObj = document.createElement("IMAGE");
                imageObj.src = image;
                selectedRow.firstChild.appendChild(imageObj);
			}
        }
		return true;
    }
	else return false;
}
/*
 * 创建审批业务
 */
function createSPconf(sjbh,ywlx,condition) {
	if(sjbh==null ||sjbh == undefined || sjbh =="")
		return;
	if(ywlx==null ||ywlx == undefined || ywlx =="")
		return;
	var templateid,operationoid,processtype;
	//插入AP_PROCESSCONFIG表信息
	var action1 = strActionrole+"?createSPYW&ywlx="+ywlx+"&sjbh="+sjbh+"&condition="+condition;
	$.ajax({
		url : action1,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//alert(123);
		       //xmlDoms.loadXML(result);
		      
			if(result.MSG=="1")
			{
				templateid = result.templateid;
				operationoid = result.operationoid;
				processtype = result.processtype;
				
			}else
			{
				sjbh = null;
			}
			
		},
	    error : function(result) {
	    	alert("创建申请失败");
	    	sjbh =  null;
	     	//alert(234);
	    }
	});
	if(sjbh==null ||sjbh == undefined || sjbh =="")
		return;
	if(templateid==null||templateid==""){
		creatSPNoWs(ywlx,operationoid,processtype,sjbh);
	}else{
		creatSP(templateid,ywlx,operationoid,processtype,sjbh);
	}
		
		
   }

/*
 * 判断审批是否完成,完成1，未完成0
 */
function getProIsover(sjbh,ywlx) {
	if(sjbh==null ||sjbh == undefined || sjbh =="")
		return;
	if(ywlx==null ||ywlx == undefined || ywlx =="")
		return;
	var isSpover = "0";
	//插入AP_PROCESSCONFIG表信息
	var action1 = strActionrole+"?getProIsover&ywlx="+ywlx+"&sjbh="+sjbh;
	$.ajax({
		url : action1,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
				isSpover = result.isSpover;
				
			
		},
	    error : function(result) {
	    	
	    	isSpover = "0";
	    }
	});

		return isSpover;
   }
/*
 * 获取审批流程的wsid
 * getProwsid(ywlx,operationoid)
 */
/*
 * 有具体业务的审批需要调用此审批方法
 * 
 */
function getProwsid(ywlx,operationoid) {
	var wsid = '';
	var action1 = strActionrole+"?getProwsid&ywlx="+ywlx+"&operationoid="+operationoid;
	$.ajax({
		url : action1,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		success : function(result) {
			if(result.MSG=="1")
			{
				wsid = result.wsid;
			}
		}
	});
	return wsid;
}
function creatSP(templateid,ywlx,operationoid,processtype,sjbh) {
	//alert("sjbh=="+sjbh);

    var wsActionURL = strActionrole+"?getXMLPrintAction|templateid="+templateid+"|isEdit=1|ywlx="+ywlx+"|sjbh="+sjbh+"|isrefresh=1|rowid="+Math.random()+".mht";
    wsActionURL = g_sAppName+"/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+
    		"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+templateid+"&isEdit=1"+"&operationoid="+operationoid+"&processtype="+processtype;
    
    $(window).manhuaDialog({"title":"流程申请","type":"text","content":wsActionURL,"modal":"1"});
}
//无文书呈请审批
function creatSPNoWs(ywlx,operationoid,processtype,sjbh) {
    wsActionURL = g_sAppName+"/jsp/framework/print/pubPrintNoWs.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&operationoid="+operationoid+"&processtype="+processtype;
    $(window).manhuaDialog({"title":"流程申请","type":"text","content":wsActionURL,"modal":"2"});
}

/**
 * 审批前判断是否打印审批文书
 * SJBH  事件编号
 * docID 文书模板编号
 * spzt  审批状态，9:默认状态；多个审批状态:1|2|7
 */
function hasDocDone(SJBH,docID,YWLX,SPZT)
{
    if(SJBH == null || docID == null)
    {
        alert("缺少关键参数！");return;
    }
	// modified by guanchb@2009-01-11 start
	// 解决问题：点击审批通过的已录入信息后，呈请文书制作按钮可用。
    if(!SPZT) SPZT = "1|2|6|7|8|9";
	// modified by guanchb@2009-01-11 end
    var action = strActionSP + "?method=hasDocDone&SJBH="+SJBH+"&docID="+docID+"&YWLX="+YWLX+"&SPZT="+SPZT;
    var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
    xmlhttp2.Open("POST",action,false);
    xmlhttp2.Send(null);
    var message = xmlhttp2.responseText;
    if(message == "-1")
        return false;
    else
        return message;
}
/**
 * function getWswh()
 * add by guanchb@2009-01-05
 * 获得文书文号
 * SJBH  事件编号
 * docID 文书模板编号
 * spzt  审批状态，9:默认状态；多个审批状态:1|2|7
 */
function getWswh(SJBH,docID,YWLX,SPZT)
{
    if(SJBH == null || docID == null)
    {
        alert("缺少关键参数！");return;
    }
	// modified by guanchb@2009-01-11 start
	// 解决问题：获得所有审批状态的文书的文书文号
    if(!SPZT) SPZT = "1|2|6|7|8|9";
	// modified by guanchb@2009-01-11 end
    var action = strActionSP + "?method=getWswh&SJBH="+SJBH+"&docID="+docID+"&YWLX="+YWLX+"&SPZT="+SPZT;
    var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
    xmlhttp2.Open("POST",action,false);
    xmlhttp2.Send(null);
    var message = xmlhttp2.responseText;
    return message;
}


//a: Array 按钮名称数组
/**
 * 将按钮设可用disabled = false; catch中的alert在发布时去掉
 * a    Array 按钮名称数组
 */
function inBtns(a)
{
  if(a)
    for(var i=0;i<a.length;i++)
      try{ document.all(a[i]).disabled = false; }catch(e){ alert("名称为["+a[i]+"]的按钮写错了或按钮不存在"); }
}
/**
 * 将按钮设可用disabled = true; catch中的alert在发布时去掉
 * a    Array 按钮名称数组
 */
function unBtns(a)
{
  if(a)
    for(var i=0;i<a.length;i++)
     try{ document.all(a[i]).disabled = true; }catch(e){ alert("名称为["+a[i]+"]的按钮写错了或按钮不存在"); }
}



//在sXML串中寻找名为sName的值，返回值是数组 zpp用函数
//用法：XWXH.value = getValueByName(document.frmPost.resultXML.value,"XWXH")[0];
function getValueByName(sXML,sName) {
	var tmpDom = createDOMDocument();
	tmpDom.loadXML(sXML);
	var selectElements = tmpDom.getElementsByTagName(sName);
	var valueArray = new Array();
	for (i=0; i<selectElements.length; i++) {
        var selectElement = selectElements(i);
        if(selectElement.hasChildNodes() && selectElement.firstChild.nodeType == 3) {
        	valueArray[i] = selectElement.firstChild.text;
        }
    }
    tmpDom = null;
   	return 	valueArray;
}


function changeValueByName(sXML,sName,sValue) {
	var domdoc = createDOMDocument();
  	domdoc.loadXML(sXML);
  	var beChangedElements = domdoc.getElementsByTagName(sName);
  	var newTextNode = domdoc.createTextNode(sValue);
  	for (i=0; i<beChangedElements.length; i++) {
  		var beChangedElement = beChangedElements(i);
         if(beChangedElement.hasChildNodes()) {
        	beChangedElement.replaceChild(newTextNode, beChangedElement.firstChild);
        }else {
        	beChangedElement.appendChild(newTextNode);
        }
  	}
  	return domdoc;
}


//判断文书是否存在,返回1：有文书,0：没有文书
function checkWSCreated(sjbh,ywlx,wsid){
    if(sjbh == null || wsid == null)
    {
        alert("缺少关键参数！");return;
    }
    var action = g_sAppName+"/ZfbaCommon/SPAction.do?method=hasDocDone&SJBH="+sjbh+"&docID="+wsid+"&YWLX="+ywlx;
    var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
    xmlhttp2.Open("POST",action,false);
    xmlhttp2.Send(null);
	var retVal = xmlhttp2.responseText;
	return retVal;
}
//判断是否发起审批,false:没有发起审批
function checkSPCreated(sjbh){
	var spbh = getSpbh(sjbh);
	if(spbh=="") {
		return false;
	}else{
		return true;
	}
}

//对页面上两个日期的校验
//sStart 起始日期
//sEnd   终止日期
//sStartLabel 起始日期标签
//sEndLabel 终止日期标签
//返回值如果为false，提示不允许继续
function dateCompare(sStart,sEnd,sStartLabel,sEndLabel){

	var startDate = "";
	var endDate = "";
	var retMsg = "";
	startDate = document.getElementById(sStart).value;
	endDate = document.getElementById(sEnd).value;
	if(startDate==""||endDate=="")	return true;
	var d = startDate.localeCompare(endDate);
    if(d>0){
	   retMsg = sStartLabel+"不能大于"+sEndLabel;
	   alert(retMsg);
	   return false;
    }
    return true;
}

// add by liul 2007-09-17 end;
// add by guanchb 2007-09-26 start
// 根据也面的出生日期获得年龄 function getAge（obj）
// obj 时间控件
function delStr(str){
	if( str.substring(0,1) == "0" ){
		str = str.substring(1,str.length);
	}
	return str ;
}
function getAge(obj){
	if( obj.value && obj.code ){
		var csrq = obj.code ;
		var nowDate = new Date();
		var nowYear   = nowDate.getYear();
		var nowMonth  = nowDate.getMonth() + 1;
		var nowDay    = nowDate.getDate();

		var tempYear = delStr(csrq.substring(0,4));
		var tempMonth = delStr(csrq.substring(4,6));
		var tempDay = delStr(csrq.substring(6,8));

		var csrqYear = 	parseInt(tempYear);
		var csrqMonth = parseInt(tempMonth);
		var csrqDay = parseInt(tempDay);

		var age = nowYear - csrqYear ;
		if(age >= 0){
			if(age == 0 || age == 1)
				return 1 ;
			if( nowMonth - csrqMonth < 0 ){
				age = age - 1 ;
			}else if( nowMonth - csrqMonth == 0 ){
				if( nowDay - csrqDay < 0 ){
					age = age -1 ;
				}
			}
			return age ;
		}if(age < 0 ){
			return null ;
		}
	}else{
		return null ;
	}
}
// add by guanchb 2007-09-26


/**
* added by xukx 签字函数
*param:sjbh:事件编号
*      template_id:文书模版id号
*      tablename：更新业务表名
*      fieldname: 更新字段
*      fieldvalue:更新字段值，默认为"1"
*/
function qz(sjbh,template_id,tablename,fieldname,fieldvalue,ywlx,otherparams)
{
   var sActionURL = g_sAppName+"/ZfbaCommon/QzAction.do?method=doQz&sjbh="+sjbh+"&template_id="+template_id;
   if(tablename)
       sActionURL +="&tablename="+tablename;
   if(fieldname)
       sActionURL +="&fieldname="+fieldname;
   else
   {
      alert("参数错误");
      return;
   }
   if(fieldvalue)
       sActionURL +="&fieldvalue="+fieldvalue;
   else
       sActionURL +="&fieldvalue=1";
   /*if(confirm("是否确认签字？"))    zl modi 2007.11.2
   {
    try
 	{
		if(sActionURL.substring(0,1)!="/")
			sActionURL = "/" + sActionURL ;
		if(sActionURL.indexOf(g_sAppName)<0)
			sActionURL = g_sAppName + sActionURL;
  		var responseText = doPost(sActionURL);
  		if(!responseText)
  		{
			alert("没有返回应答结果!") ;
			return false;
  		}
       	var xmlDom = createDOMDocument();
      	xmlDom.loadXML(responseText);
		var res = showGlobalResult(xmlDom);
		xmlDom = null;
		return res;
  	 }
  	 catch(e)
  	 {
		alert(e.description);
		return false ;
  	 }
   }*/
   var res=window.showModalDialog(g_sAppName+"/jsp/zfba/common/qrqz.jsp",this,"dialogWidth=400px;dialogHeight=300px");
   if (res)
   {
   	var rlt=res.substring(0,1);
   	if (rlt=="1")
   		sActionURL+="&SFQZ=1";
   	else
   		sActionURL+="&SFQZ=2&JZRXM="+res.substring(2,res.length);

   	if(sActionURL.substring(0,1)!="/")
		sActionURL = "/" + sActionURL ;
	if(sActionURL.indexOf(g_sAppName)<0)
		sActionURL = g_sAppName + sActionURL;
	//增加自定义业务：
	if(ywlx)
	{
	  	switch(parseInt(ywlx))
	  	{
	   		case 040323 :
            	sActionURL+= "&ywlx="+ywlx+otherparams;
	        	break;
	   		case 040300 :
	   			sActionURL=g_sAppName+"/Sawpcl/SzjwpAction.do?method=doQz&sjbh="+sjbh;
	   			break;
			case 040201 :
				sActionURL+= "&ywlx="+ywlx+otherparams;
				break;
			/*** 注释【040425】：调取证据签字操作，在QzAction类中实现 modified by guanchb 2008-12-16 20:16 start
			case 040425 :
	   			sActionURL=g_sAppName+"/Zcgl/ZaZfbaXsajWpDqzjxxdjAction.do?method=doQz&sjbh="+sjbh;
	   			break;
      end modified by guanchb 2008-12-16 20:16 ***/
			case 040489 :
	   			sActionURL=g_sAppName+"/Zcgl/ZaZfbaJcxxWpKywpdjxxAction.do?method=doQz&sjbh="+sjbh;
	   			break;
			case 040490 :
	   			sActionURL=g_sAppName+"/Zcgl/ZaZfbaJcxxWpJckywpdjxxAction.do?method=doQz&sjbh="+sjbh;
	   			break;
	   		case 040331 :// 没收物品
			case 040332 :// 发还物品
            case 040350 :// 解除先行登记保存
				sActionURL+= "&ywlx="+ywlx;
	   		default:
	   		    sActionURL+= "&ywlx="+ywlx;
            	break;
	  	}
	}

  	var responseText = doPost(sActionURL);
  	if(!responseText)
  	{
		alert("没有返回应答结果!") ;
		return false;
  	}
       	var xmlDom = createDOMDocument();
      	xmlDom.loadXML(responseText);
	var res = showGlobalResult(xmlDom);
	xmlDom = null;
	return true;

   }


}

// 设置结束时间，设置成功后返回 true , 设置失败后返回 false
function set_JS_Time(jstime,busID){

}
//回调函数，笔录制作完成后调用。
function finishBl() {
}
//回调函数，笔录制作未完成调用
function closeBlWithoutSava() {
}

//处理自动加盖印章方法
function stampOperation(sjbh,ywlx,templateid,org_azt,cor_azt)
{
    var strActionUrl = g_sAppName+"/SpBack/TaskBackAction.do?method=stampAction&sjbh="+sjbh+"&ywlx="+ywlx+"&templateid="+templateid+"&org_azt="+org_azt+"&cor_azt="+cor_azt;
    var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
    xmlhttp2.open("POST",strActionUrl,false);
    xmlhttp2.send();
    var xmlDom = createDOMDocument();
    xmlDom.loadXML(xmlhttp2.responseText);
    var errMessage = xmlDom.selectSingleNode("//RESPONSE/ERRMESSAGE");
  	if(errMessage != null && errMessage.text != null)
  	{
    	alert("审批返回操作失败:"+errMessage.text);
    	return false;
  	}
    xmlDom = null;
    xmlhttp2 = null;
    return true;
}
//处理自动加盖印章方法
function customstampOperation(sjbh,ywlx,templateid,org_azt,cor_azt,level,fieldname)
{
    var strActionUrl = g_sAppName+"/SpBack/TaskBackAction.do?method=customStampAction&sjbh="+sjbh+"&ywlx="+ywlx+"&templateid="+templateid+"&org_azt="+org_azt+"&cor_azt="+cor_azt+"&org_type="+level+"&yzfieldname="+fieldname;
    var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
    xmlhttp2.open("POST",strActionUrl,false);
    xmlhttp2.send();
    var xmlDom = createDOMDocument();
    xmlDom.loadXML(xmlhttp2.responseText);
    var errMessage = xmlDom.selectSingleNode("//RESPONSE/ERRMESSAGE");
  	if(errMessage != null && errMessage.text != null)
  	{
    	alert("审批返回操作失败:"+errMessage.text);
    	return false;
  	}
    xmlDom = null;
    xmlhttp2 = null;
    return true;
}
function viewWS(sjbh,ywlx,wsid,arg){
	// modified by guanchb@2009-01-11 start
	// 解决问题：传入文书spzt值，判断文书是否制作，解决刑事受案登记页面中，发起审批后，点击【查看刑事受案登记表】按钮提示"请先制作文书，才可查看文书"。
	if(hasDocDone(sjbh,wsid,ywlx,"1|2|7|8|9")){
	// modified by guanchb@2009-01-11 end
		openDoc(wsid,sjbh,ywlx,"0",true,"0",arg);
	}else{
		alert("请先制作文书，才可查看文书");
	}
}
//在线帮助公用方法
function HelpHtml(ywlb,filename)
{
  window.open(g_sAppName+'/manual/html/'+ywlb+'/'+filename+'.html',"","width=950px,height=700px,toolar=0,menubar=0,scrollbars=yes,status=1,resizable=1,screenX=0,screenY=0");
}
//审批过程中判断审批人是否已签名
function IsHaveSigned(tempID,sjbh,ywlx)
{
    var strActionUrl = g_sAppName+"/SpBack/TaskBackAction.do?method=IsSignedAction&sjbh="+sjbh+"&ywlx="+ywlx+"&templateid="+tempID;
    var xmlhttp2 = new ActiveXObject("Msxml2.XMLHTTP");
    xmlhttp2.open("POST",strActionUrl,false);
    xmlhttp2.send();
    var rlt=xmlhttp2.responseText;
    return rlt;


}

/*
 * getProcConf
 */
function getPrcconf(sjbh,ywlx,condition) {
	if(sjbh==null ||sjbh == undefined || sjbh =="")
		return;
	if(ywlx==null ||ywlx == undefined || ywlx =="")
		return;
	
	var data = new Array();
	//插入AP_PROCESSCONFIG表信息
	var action1 = strActionrole+"?createSPYW&ywlx="+ywlx+"&sjbh="+sjbh+"&condition="+condition;
	$.ajax({
		url : action1,
		data : null,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//alert(123);
		       //xmlDoms.loadXML(result);
		      
			if(result.MSG=="1")
			{
				var obj = new Object();
				obj.templateid = result.templateid;;
				obj.operationoid = result.operationoid;
				obj.processtype = result.processtype;
				//alert('aa:'+result.processtype);
				data.push(obj);
				
			}
			
		},
	    error : function(result) {
	    	alert("创建申请失败");
	    	sjbh =  null;
	    }
	});
	return data;
   }

function OpenWs(strAction){
     window.open(strAction,"", 'title=print,toolbar=no, menubar=yes,scrollbars=yes,resizable=yes');
}
