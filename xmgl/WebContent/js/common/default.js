var defaultJson = new Object();
//+------------------------------------
// 全局变量
//-------------------------------------
g_iHeight   = document.documentElement.clientHeight;
var g_sAppName 		=	"/xmgl"; //工程路径
var g_sImgPath      =   g_sAppName+"/images/default/";        // 图片文件的路径
var g_sDicPath      =   g_sAppName+"/dic/";                  // 字典文件的路径
var g_sQueryPath    =   g_sAppName+"/jsp/";                // 查询文件的路径

var g_sRootNodeName      =   "DATAINFO";     //xml根节点,用于录入时,产生的XML字符串的根节点
var g_oCurrentXML        =   null;           //获取页面输入信息而生成的XML文档对象
var g_oTxtXML            =   null;           //隐藏域txtXML文档对象

var g_bCheckChanged = false;            // 检测字典输入
var g_bDicFixed = false;
var g_xTabPane  =   null;               // 标签面板
var g_xDic      =   null;               // 字典对象
var g_xHint     =   null;               // 提示层
var g_xQuery    =   null;               // 查询窗口
var g_xPucker   =   null;               // 折叠窗口

var rowsPerPage = 10; //默认查询返回结果行数
var g_bAlertWhenNoResult = true;
var g_bLoading = true;	//是否显示遮罩loading

var g_iSortIndex = -1; //排序索引列
var g_iSelectedIndex = -1; //tablist 最后一次点击的行
var g_aRelationMap = [];
var m_content = [];                      //textarea扩展
var g_prompt = new Array("操作成功！");
var g_nameMaxlength = 12;//附件名称显示最大长度
var g_beforeQuerySelectIndex = 0;	//执行查询之前的选中行的行号
var g_setDefaultSelectedRowIndexFlag = false;	//查询完毕后，是否设置默认选中行

$(document).ajaxStart(function () {
	if(g_bLoading){
		loadingCss3 ();
	}
 }).ajaxStop(function () { 
	 closeLoading3(); 
 }); 
//计算弹出页的高度
var getDivStyleHeight=function getDivStyleHeight(){
	var gaodu="";
	parent.$("body  div").each(function(i){
		if($(this).attr("isopenwindow")=="true"){
			$(this).find("div").each(function(i){
				var divClass = $(this).attr("class");
				if(divClass=="xxtcc150OpenBox-body"){
					divStyleNumber = "4";
					gaodu=$(this).find('iframe')[0].getAttribute("height");
					return false;
				}else if(divClass=="windowOpenBoxMain-body"){
					divStyleNumber = "2";
					gaodu=$(this).find('iframe')[0].style.height;
					gaodu=gaodu.substring(0,gaodu.length-2);
					return false;
				}else if(divClass=="windowOpenBox-body"){
					divStyleNumber = "1";
					gaodu=$(this).find('iframe')[0].style.height;
					gaodu=gaodu.substring(0,gaodu.length-2);
					return false;
				}
			});
		}
	});
	return gaodu;
}
//----------------------------------------
/**
 * 将json串组成保存json串格式
 * param:data1为json  字符串对象
 */
defaultJson.packSaveJson = function(data1) {
	// 开始解析json对象
	if (data1 == null) return "";
	
	var data1 = "{\"response\":{\"data\":[" + data1 + "]}}}";
	//return JSON.parse(data1);
	var data = {

		msg : data1
	}
	return data;
};
//----------------------------------------
//-显示缩略图
//----------------------------------------
function checkupFiles(n){
	/**
	var iframe_count = 0;
	//获取父页面弹出层个数
	parent.$("body  div").each(function(i){
		if($(this).attr("aria-labelledby")=="myModalLabel"){
			if($(this).attr("aria-hidden")=="true"){
				//do nothing
			}else{
				iframe_count++;
			}
		}
	});
	if(iframe_count==0){
		//如果没有弹出层，那么在父页面弹出一个新层预览
		parent.$("body").manhuaDialog({"title" : "附件预览","type" : "text","content" : g_sAppName+"/fileUploadController.do?doPreview&fileid="+n,"modal":"2"});
	}else{
		//如果已经有弹出层，那么在当前页面弹出新层预览
		$("body").manhuaDialog({"title" : "附件预览","type" : "text","content" : g_sAppName+"/fileUploadController.do?doPreview&fileid="+n,"modal":"2"});
	}
	*/
	var obj = $(document).find("#previewFileid");
	if(obj.attr("id")==undefined){
		$(document).find("form").each(function(i){
			if($(this).attr("name")=="frmPost"){
				$(this).append("<input type='hidden' id='previewFileid'>");
			}
		});
	}
	$("#previewFileid").val(n);
	window.open(encodeURI(g_sAppName+"/jsp/file_upload/showPreview.jsp"));
}
//----------------------------------
//年度
//-@param	n	年度条件的ID字符串，默认为ND
//----------------------------------
function setDefaultNd(n){
	if(n==""||n==undefined){
		n="ND";
	}
	var obj = $("#"+n);
	reloadSelectTableDic(obj);
	//获取当前年
	var date = new Date();
	var sysYear = date.getFullYear();
	var len = obj.find('option').length;
	//遍历select，如果选项中没有当前年，那么年份减1，直到找出存在的年份选项
	var existYear = "";
	for(var x=sysYear;x>0;x--){
		var hasValueFlag = false;
		obj.find("option").each(function(){
			if($(this).text()==x){
				hasValueFlag=true;
				return false;
			}
		});
		if(hasValueFlag==true){
			existYear=x;
			break;
		}
	}
	obj.val(existYear);
}
//-----------------------
//-显示项目的二维码
//-@param id	gc_tcjh_xmxdk_id 项目下达库主键
//---------------------------
function openTwoDimensionCode(id){
	
	//window.open(encodeURI(g_sAppName+"/jsp/TwoDimensionCode/showPreview.jsp?id="+id));
	$(window).manhuaDialog({"title":"查看二维码","type":"text","content":encodeURI(g_sAppName+"/jsp/TwoDimensionCode/showPreview.jsp?id="+id),"modal":"4"});
	
}

//----------------------------------------
//-打开用户选择界面 采用foxmail方式实现
//-@param m	选择类型 单选single 多选multi
//-@param n	默认被选中的用户	以逗号分隔的账号字符串，比如“superman,admin,jhbz”,单选类型可以不使用这个参数
//-@param o callback方法的名字
//-@param d 选择指定部门人员
//----------------------------------------
function selectUserTree(m,n,o,d){
	var url = g_sAppName+"/jsp/framework/system/user/selectUsers.jsp";
	if(m!="" && m!=undefined){
		url += "?type="+m;
	}
	if(n!="" && n!=undefined){
		url += "&checkNodeString="+n;
	}
	if(o!="" && o!=undefined){
		url += "&callback="+o;
	}
	if(d!="" && d!=undefined){
		url += "&dept="+d;
	}
	$(window).manhuaDialog({"title" : "显示人员列表","type" : "text","content" : url,"modal":"2"});
}

//----------------------------------------
//-打开用户选择界面
//-@param m	选择类型 单选single 多选multi
//-@param n	默认被选中的用户	以逗号分隔的账号字符串，比如“superman,admin,jhbz”,单选类型可以不使用这个参数
//-@param o callback方法的名字
//-@param d 选择指定部门人员
//----------------------------------------
function openUserTree(m,n,o,d){
	//var url = g_sAppName+"/jsp/framework/system/user/getUsers.jsp";
	var url = g_sAppName+"/jsp/framework/system/user/selectUsers.jsp";

	if(m!="" && m!=undefined){
		url += "?type="+m;
	}
	if(n!="" && n!=undefined){
		url += "&checkNodeString="+n;
	}
	if(o!="" && o!=undefined){
		url += "&callback="+o;
	}
	if(d!="" && d!=undefined){
		url += "&dept="+d;
	}
	//$(window).manhuaDialog({"title" : "显示人员列表","type" : "text","content" : url,"modal":"4"});
	$(window).manhuaDialog({"title" : "显示人员列表","type" : "text","content" : url,"modal":"2"});
}
//----------------------------------------
//-打开部门选择界面
//-@param m	选择类型 单选single 多选multi
//-@param n	默认被选中的部门	以逗号分隔的部门ID字符串	，单选类型可以不使用这个参数
//-@param o callback方法的名字
//----------------------------------------
function openDeptTree(m,n,o){
	var url = g_sAppName+"/jsp/framework/system/dept/getDepts.jsp";
	if(m!="" && m!=undefined){
		url += "?type="+m;
	}
	if(n!="" && n!=undefined){
		url += "&checkNodeString="+n;
	}
	if(o!="" && o!=undefined){
		url += "&callback="+o;
	}
	$(window).manhuaDialog({"title" : "显示部门列表","type" : "text","content" : url,"modal":"4"});
}
//----------------------------------------
//-打开部门选择界面
//-@param m	计划数据ID
//-@param n	反馈类型前4位
//-@param o 查询表单的ID
//-@param p 查询数据列表的ID
//-@param q 查询对应的URL
//-@param r 弹出页大小
//----------------------------------------
function openJhfkPage(m,n,o,p,q,r){
	var url = g_sAppName+"/jsp/business/jhfk/jhfkMain.jsp";
	if(m!="" && m!=undefined){
		url += "?jhsjid="+m;
	}
	if(n!="" && n!=undefined){
		url += "&fklx="+n;
	}
	if(o!="" && o!=undefined){
		url += "&queryFormID="+o;
	}
	if(p!="" && p!=undefined){
		url += "&tabListID="+p;
	}
	if(q!="" && q!=undefined){
		url += "&queryURL="+q;
	}
	var pageSize = "4";
	if(r!=""&&r!=undefined){
		pageSize = r;
	}
	$(window).manhuaDialog({"title" : "计划反馈","type" : "text","content" : url,"modal":pageSize});
}
//---------------------------------------
//-重新查询表格中的数据
//---------------------------------------
function doRefreshPageWithCondition(formID,tableID,queryURL){
	var data = combineQuery.getQueryCombineData(document.getElementById(formID),frmPost,document.getElementById(tableID));
	data = defaultJson.getQueryConditionWithNowPageNum(data,tableID);
	defaultJson.doQueryJsonList(queryURL,data,document.getElementById(tableID));	
}
//----------------------------------------
//-打开标准文档查看界面
//-@param m	文档ID
//-@param n	默认被选中的部门	以逗号分隔的部门ID字符串	，单选类型可以不使用这个参数
//-@param o callback方法的名字
//----------------------------------------
function showBzwdList(m,n){
	var url = g_sAppName+"/jsp/business/bzwd/bzwdQuery.jsp";
	if(m!="" && m!=undefined){
		url += "?wdid="+m;
	}
	var titleStr = "标准文档列表";
	if(n!="" && n!=undefined){
		titleStr = n;
	}
	$(window).manhuaDialog({"title" : titleStr,"type" : "text","content" : url,"modal":"4"});
}
//--------------------------------------------------
//-add by zhangbr@2013-11-28
//-把查询条件中的默认查询页改为当前页，返回新查询条件json串
//-@param data	原查询条件json串
//-@param tableID	查询结果列表的ID值
//--------------------------------------------------
defaultJson.getQueryConditionWithNowPageNum=function(data,tableID){
	var tempJson = convertJson.string2json1(data);
	g_beforeQuerySelectIndex = $("#"+tableID).getSelectedRowIndex();
	if($("#"+tableID).getCurrentpagenum ()!=undefined){
		tempJson.pages.currentpagenum = $("#"+tableID).getCurrentpagenum();
	}
	data = JSON.stringify(tempJson);
	g_setDefaultSelectedRowIndexFlag = true;
	return data;
};
//--------------------------------------------------
//-add by zhangbr@2013-11-28
//-和上面的方法一起使用，当列表刷新后，使之前被选中的行再次被选中
//--------------------------------------------------
function callbackSetDefaultSelectedRowIndex(tableID){
	g_setDefaultSelectedRowIndexFlag = false;
	if(g_beforeQuerySelectIndex!=-1 && ($(tableID).getRowJsonObjByIndex(g_beforeQuerySelectIndex))!=-1){
		$(tableID).setSelect(g_beforeQuerySelectIndex);
	}
}
/**
 * 通用删除调用函数
 * param： actionName 访问spring路径
 * param:  data1 删除的数据 json 对象
 * param： tableisID 操作的表对象
 */
defaultJson.doDeleteJson = function(actionName, data1,tablistID,callbackFunction) {
    var success  = true;
    var isAsync = false;
    if(callbackFunction!=undefined){
 	   isAsync = true;
    }
	$.ajax({
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	isAsync,
		type : 'post',
		success : function(result) {
			//将返回值增加到TabList中,将返回的串转换为数组形式
			//add by zhangbr@ccthanking.com BEGIN 
			//上传附件时，需要使用resultXML中的值，所以此处查询成功后，要将返回结果赋值给resultXML
			//如果页面不存在resultXML，附件上传时也有判断，不会报错
			//add by zhangbr@ccthanking.com END
			$("#resultXML").val(result.msg);
			if(tablistID != null)
			{
		 	  clearLockTable(tablistID);
			  var rowindex = $("#"+tablistID.id).getSelectedRowIndex();
			  $("#"+tablistID.id).removeResult(rowindex);
			  modifyLockTable(tablistID);
			}
			var prompt = result.prompt;
 			if(!prompt){
 				prompt =g_prompt[0];
 			}
			xAlert("信息提示",prompt);
			defaultJson.clearTxtXML();
			success = true;
			if(isAsync==true){
				  eval(callbackFunction+"()");
			}
		},
	    error : function(result) {
		     	//alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;

		}
	});
	 return success;
};


/**
 * 通用输入插入调用函数
 * param： actionName 访问spring路径
 * param:  data1 插入的数据 json 对象
 * param： tableisID 操作的表对象
 */
defaultJson.doInsertJson = function(actionName, data1,tablistID,callbackFunction) {
    var success  = true;

	var isAsync = false;
	if (callbackFunction != undefined) {
		isAsync = true;
	}
	$.ajax({
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	isAsync,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//将返回值增加到TabList中,将返回的串转换为数组形式
			//alert(result.msg);
			//add by zhangbr@ccthanking.com BEGIN 
			//上传附件时，需要使用resultXML中的值，所以此处查询成功后，要将返回结果赋值给resultXML
			//如果页面不存在resultXML，附件上传时也有判断，不会报错
			//add by zhangbr@ccthanking.com END
			$("#resultXML").val(result.msg);
			if(tablistID != null)
			{
			clearLockTable(tablistID);
			//add by zhangbr@ccthanking.com 清空查询为空的小图标
			var dataLength = $("#page_"+tablistID.id+" form").find("img.noresult").length;
			if(dataLength==1){
				$("#page_"+tablistID.id).find("form").remove();//删除旧图片
			}
			var res = dealSpecialCharactor(result.msg);
			var subresultmsgobj = defaultJson.dealResultJson(res);
			var strarr = $("#"+tablistID.id).insertResult(JSON.stringify(subresultmsgobj),tablistID,1);
			modifyLockTable(tablistID);
			}
			var prompt = result.prompt;
 			if(!prompt){
 				prompt =g_prompt[0];
 			}
			
			xAlert("信息提示",prompt);
			defaultJson.clearTxtXML();

			success = true;
			if(isAsync==true){
			  eval(callbackFunction+"()");
			}
			},
	    error : function(result) {
			    defaultJson.clearTxtXML();
			    success = false;

		}
	});
	 return success;
};
//转换字符串中的特殊字符
function dealSpecialCharactor(v){
	v=v.replace(/\\[b]/g," ");
//	v=v.replace(new RegExp("\\[b]","g"),"&nbsp;");
   	v=v.replace(new RegExp("&gt;","g"),">");
   	v=v.replace(new RegExp("&lt;","g"),"<");
	return v;
}
//---------------------------------
//-获取当前时间的方法
//-@param	formatStr:需要返回的字符串格式，默认为“YYYY-MM-DD”
//---------------------------------
function getCurrentDate(formatStr){
	var myDate = new Date();
	var currDate = myDate.toLocaleDateString();
	if(formatStr=="" || formatStr==undefined){
		formatStr = "YYYY-MM-DD";
	}
	var str = formatStr;   
	var Week = ['日','一','二','三','四','五','六'];  
	
	str=str.replace(/yyyy|YYYY/,myDate.getFullYear());   
	str=str.replace(/yy|YY/,(myDate.getYear() % 100)>9?(myDate.getYear() % 100).toString():'0' + (myDate.getYear() % 100));   
	
	str=str.replace(/MM/,(myDate.getMonth()+1)>9?(myDate.getMonth()+1).toString():'0' + (myDate.getMonth()+1));   
	str=str.replace(/M/g,(myDate.getMonth()+1));   
	
	str=str.replace(/w|W/g,Week[myDate.getDay()]);   
	
	str=str.replace(/dd|DD/,myDate.getDate()>9?myDate.getDate().toString():'0' + myDate.getDate());   
	str=str.replace(/d|D/g,myDate.getDate());   
	
	str=str.replace(/hh|HH/,myDate.getHours()>9?myDate.getHours().toString():'0' + myDate.getHours());   
	str=str.replace(/h|H/g,myDate.getHours());   
	str=str.replace(/mm/,myDate.getMinutes()>9?myDate.getMinutes().toString():'0' + myDate.getMinutes());   
	str=str.replace(/m/g,myDate.getMinutes());   
	
	str=str.replace(/ss|SS/,myDate.getSeconds()>9?myDate.getSeconds().toString():'0' + myDate.getSeconds());   
	str=str.replace(/s|S/g,myDate.getSeconds());   
	
	return str;   
}
function doInsertAfter(){
	
}
/*
 * 处理插入后返回的json串
 * 
 * resultdata:接收返回的字符串
 * return 
 * insertJsonObj
 * 插入格式的json对象
 */
defaultJson.dealResultJson = function(resultdata) {
	var subresultmsgobj;
	var resultmsgobj = convertJson.string2json1(resultdata);
	subresultmsgobj = resultmsgobj.response.data[0];
	return subresultmsgobj;
};

/**
 * 通用输入更新调用函数
 * param： actionName 访问spring路径
 * param:  data1 更新的数据 json 对象
 * param： tableisID 操作的表对象
 */
defaultJson.doUpdateJson = function(actionName, data1,tablistID,callbackFunction) {
    var success  = true;
    var isAsync = false;
    if(callbackFunction!=undefined){
 	   isAsync = true;
    }
	$.ajax({
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	isAsync,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			$("#resultXML").val(result.msg);
			//将返回值增加到TabList中,将返回的串转换为数组形式
			if(tablistID != null)
			{
					var rowindex = $("#"+tablistID.id).getSelectedRowIndex();
					var subresultmsgobj = defaultJson.dealResultJson(result.msg);
					/*
					 * 如果返回结果为单表，更新的table也为单表则可以直接将subresultmsgobj传给updateResult使用，否则需要如下方式组成新的json
					 * 
					 */
					var comprisesJson = $("#"+tablistID.id).comprisesJson(subresultmsgobj,rowindex);
					var strarr = $("#"+tablistID.id).updateResult(JSON.stringify(comprisesJson),tablistID,rowindex);

					$("#"+tablistID.id).setSelect(rowindex);
					
			}
			var prompt = result.prompt;
 			if(!prompt){
 				prompt =g_prompt[0];
 			}
 			var withoutAlertFlag = $(tablistID).attr("withoutAlertFlag");
 			if(withoutAlertFlag=="true"){
 				//如果不需要成功提示，那么就不提示操作成功了
 			}else{
				xAlert("信息提示",prompt);
			}
			defaultJson.clearTxtXML();
			success = true;
			if(isAsync==true){
			  eval(callbackFunction+"()");
			}
		},
	    error : function(result) {
	     	//alert(result.msg);
		    defaultJson.clearTxtXML();
		    success = false;
	    }
	});
	 return success;
};


/*
 * 批量更新表格方法
 * param： actionName 访问spring路径
 * param:  data1 插入的数据 json 对象
 * param： tableisID 操作的表对象
 */
defaultJson.doUpdateBatchJson = function(actionName, data1,tablistID,indexarry,callbackFunction) {
    var success  = true;
    var isAsync = false;
    if(callbackFunction!=undefined){
 	   isAsync = true;
    }
	$.ajax({
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	isAsync,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {
			//$("#resultXML").val(result.msg);
			//将返回值增加到TabList中,将返回的串转换为数组形式
			if(tablistID != null)
			{
				var isUpdate = $("#"+tablistID.id).attr("isUpdate");
				if(!isUpdate) {
					//var indexarry = $("#"+tablistID.id).getSelectedRowsIndex();
					var resultmsg = result.msg;
					var resultmsgobj = convertJson.string2json1(resultmsg);
					for(var i=0;i < indexarry.length;i++)
					{
						var indexval = indexarry[i];
						var subresultmsgobj = resultmsgobj.response.data[i];
						var comprisesJson = $("#"+tablistID.id).comprisesJson(subresultmsgobj,indexval);
						   $("#"+tablistID.id).updateResult(JSON.stringify(comprisesJson),tablistID,indexval);
					}
				}
		    }
			var prompt = result.prompt;
 			if(!prompt){
 				prompt =g_prompt[0];
 			}
			xAlert("信息提示",prompt);
			defaultJson.clearTxtXML();
			success = true;
		
			if (isAsync == true) {
				eval(callbackFunction + "()");
			}
		},
	    error : function(result) {
	     	//alert(result.msg);
		    defaultJson.clearTxtXML();
		    success = false;
	    }
	});
	 return success;
};
//datatable表格使用
defaultJson.dealResult = function (json,tablistID) {
//function dealResult(json,tablistID) {
	//将json 串转为json对象
	var b;
	// 使用方法一
	b = convertJson.string2json1(json);

		var trs = $("table#"+tablistID.id+" th");

	//定义定长数组
		var arres = new Array(trs.length);
//	var arres = new Array(); //声明一维数组
	var tableObject = $("#"+tablistID.id);//获取id为#DT1的table对象 

	var tbHead = tableObject.children('thead');//获取table对象下的thead 
	var tbHeadTh = tbHead.find('tr th');//获取thead下的tr下的th 
	tbHeadTh.each(function(i) {//遍历thead的tr下的th 
		var thisIndex = tbHeadTh.index($(this));//获取th所在的列号
		//获取th属性值
		var val = $(this).attr("fieldname");
		if(val != "undefined"){
		//通过val获取传入的json串值
		//val = val.toLocaleLowerCase();
		//插入DT1
		arres[i] = b.data[0][val];
		}
	});

	return arres;
};

//回调函数datetable
defaultJson.fnClickAddRow = function(tablistID,strarr){
	$("#"+tablistID.id).dataTable().fnAddData(strarr);
};
	
//重新装载tab
defaultJson.reloadDataTable = function(tablistID){

	var oTable = $("#"+tablistID.id).dataTable(); 	 
	  // Immediately 'nuke' the current rows (perhaps waiting for an Ajax callback...)
	  oTable.fnClearTable();  
};
//datatable
defaultJson.dealQueryResult = function (json,tablistID) {
	
		//将json 串转为json对象
		var obj;
		// 将接收串转为json对象obj
		obj = convertJson.string2json1(json);
		//获取结果集子对象
		var subobj = obj.response;
		//定义二维数组
		var arres = new Array(); //声明一维数组
		var tableObject = $("#"+tablistID.id);//获取id为#DT1的table对象 
		var tbHead = tableObject.children('thead');//获取table对象下的thead 
		var tbHeadTh = tbHead.find('tr th');//获取thead下的tr下的th 
		//为数组赋值
		for ( var j = 0; j < subobj.data.length; j++) {
			arres[j] = new Array(); //再声明二维
			var k = 0;
			tbHeadTh.each(function(i) {//遍历thead的tr下的th 
				var thisIndex = tbHeadTh.index($(this));//获取th所在的列号
				//获取th属性值
				var val = $(this).attr("fieldname");
				//通过val获取传入的json串值
				// val = val.toLocaleLowerCase();
				if(val != undefined){
					//插入DT1
					arres[j][k] = subobj.data[j][val];
					k++;
				}
			});

		}
		return arres;
	};
	
	//弹出窗口回调函数
	getWinData = function(data){

	};

/**
 * 通用查询方法
 * param： actionName 访问spring路径
 * param:  data1 查询条件json字符串
 * param： tableisID 操作的表对象
 * param:  callbackFunction ajax执行后的回调函数名称
 * param:  noAlertnoResult  没有查询结果是否提示信息
 */
defaultJson.doQueryJsonList = function(actionName, data1,tablistID,callbackFunction,noAlertnoResult){
	//function doQueryJson(actionName, data1) {

       var success  = true;
       var isAsync = false;
       if(callbackFunction!=undefined){
    	   isAsync = true;
       }
       var noalert = false; 
       if(noAlertnoResult!=undefined){
    	   if(noAlertnoResult==true||noAlertnoResult=="true"){
    		   noalert = true;
    	   }
       }

		var data = {
			msg : data1
		};
		$.ajax({
			url : actionName,
			data : data,
			cache : false,
			async :	isAsync,
			dataType : "json",  
			type : 'post',
			success : function(result) {
				//查询清空结果
				$(tablistID).clearResult();
				clearLockTable(tablistID);
				var returnMsg = result.msg;
				if(returnMsg=="0")
				{
					doInitPages(tablistID);
					var thNum = $("#" + tablistID.id +" thead tr th[fieldname]").length+1;
					setTableNoResultImg(tablistID.id);//设置无结果的图片
					if(noalert==true){
					}else{
						if(g_bAlertWhenNoResult){
							  //xAlert("查询结果","查询无记录，请重新设定查询条件！",'3');
						}
					}
					if(isAsync==true){
						  eval(callbackFunction+"()");
					}
					return;
				}else if(strarr == "-1")
				{
					doInitPages(tablistID);
					xAlert("查询结果","查询过程中出现错误，请联系管理员！",'2');
					return;
				}
				$("#queryResult").val(result.msg);
				 // alert(result.msg); 
				//将返回值增加到table中,将返回的串转换为数组形式
	//			var strarr = defaultJson.InsertTableRows(result.msg,tablistID);
				var strarr = $(tablistID.id).InsertTableRows(result.msg,tablistID);
				//将返回信息插入datatable
				$(tablistID.id).fnClickAddRowList(tablistID,strarr);
				//if(!tablistID.getAttribute("queryPath"))
			    tablistID.setAttribute("queryPath",actionName);
			    if(callbackFunction!=undefined){
			      tablistID.setAttribute("callbackFunction",callbackFunction);
			    }

				if(!tablistID.getAttribute("queryData")){
					 tablistID.queryData = undefined;
					 tablistID.setAttribute("queryData",data1);
				}else{
					 tablistID.setAttribute("queryData",data1);
				}
				doQueryPage(actionName, data1,tablistID,result.msg);
				modifyLockTable(tablistID);
				//处理分页相关信息
				//$("#page_"+tablistID.id).find("form").remove(); return;
				success = true;
				if(g_setDefaultSelectedRowIndexFlag==true){
			  		eval("callbackSetDefaultSelectedRowIndex("+tablistID.id+")");
			  	}
				if(isAsync==true){
				  	eval(callbackFunction+"()");
				}
			}
		});
        return success;
		
	};

	/**
	 * 清楚隐藏form的txtXML域内容
	 */
	defaultJson.clearTxtXML = function(){
		var txt  = $("#txtXML");
		if(txt)
			txt.attr("value","");
		
	};
	/**
	 * 翻页响应函数
	 */
	PreNextFlashRownum = function(tableid,operation,recordsperpage,currentpagenum,totalpage,countrows){
		var tablistID = document.getElementById(tableid);
		var queryPath = document.getElementById(tableid).getAttribute("queryPath");
		var queryData = convertJson.string2json1(document.getElementById(tableid).getAttribute("queryData"));
		var callbackFunction  =  document.getElementById(tableid).getAttribute("callbackFunction");
		var op_page = 1;
		switch(operation)
		{
		  case 'first':
			op_page  = 1;
		  break;
		  case 'pre':
			op_page  = currentpagenum-1;
		  break;
		  case 'next':
			op_page  = currentpagenum+1;
		  break;
		  case 'end':
			op_page  = totalpage;
		  break;
		  case 'go':
				op_page  = parseInt(document.getElementById("go_"+tableid).value);
		  break;
		  default:
			op_page = 1;
		}
		if(op_page>totalpage)
		{
			return;
		}		

		queryData.pages.recordsperpage = recordsperpage;
		queryData.pages.currentpagenum = op_page;
		queryData.pages.totalpage = totalpage;
		queryData.pages.countrows = countrows;

					//
				var data = {
					msg : JSON.stringify(queryData)
				};
				$.ajax({
					url : queryPath,
					data : data,
					cache : false,
					async :	true,
					dataType : "json",  
					success : function(result) {
						//查询清空结果
						$(tablistID).clearResult();
						var returnMsg = result.msg;
						var strarr = $(tablistID.id).InsertTableRows(result.msg,tablistID);
						//将返回信息插入datatable
						$(tablistID.id).fnClickAddRowList(tablistID,strarr);
						if(!tablistID.getAttribute("queryPath"))
						   tablistID.setAttribute("queryPath",actionName);
						if(!tablistID.getAttribute("queryData"))
						   tablistID.setAttribute("queryData",data1);
						doQueryPage(queryPath, JSON.stringify(queryData),tablistID,result.msg);
						//处理分页相关信息
						var pagingFunction  = tablistID.getAttribute("pagingFunction");
						if(pagingFunction!=undefined&&pagingFunction!=null){
							eval(pagingFunction+"('"+tableid+"')");
						}
						if($("#queryResult")){
						  $("#queryResult").val(result.msg);
						}
						if(callbackFunction)
						{
							eval(callbackFunction+"()");
						}

					}
				});
		
		//alert();
		//alert(operation);
	};
	
	//处理分页相关信息
	doQueryPage = function(actionName, queryconfig,tablistID,resultJson){
		
    	var b = convertJson.string2json1(resultJson);
    	var recordsperpage = b.pages.recordsperpage;
    	var currentpagenum = b.pages.currentpagenum;
    	var totalpage = b.pages.totalpage;
    	var countrows = b.pages.countrows;
    	var tableid = tablistID.id;
    	
   	    var pageHTML  ="<form class=\"form-inline\" currentpagenum='"+currentpagenum+"'>";
   	       if (currentpagenum==1){
	          pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"首页\" ><i class=\"icon-fast-backward\"></i></button>";
              pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"上一页\"><i class=\"icon-step-backward\"></i></button>";
   	       }else{
   	          pageHTML +="<button type=\"button\"  class=\"btn btn-mini btn-link\" onclick=\"PreNextFlashRownum('"+tableid+"','first',"+recordsperpage+","+currentpagenum+","+totalpage+","+countrows+")\" title=\"首页\" ><i class=\"icon-fast-backward\"></i></button>";
              pageHTML +="<button type=\"button\"  class=\"btn btn-mini btn-link\" onclick=\"PreNextFlashRownum('"+tableid+"','pre',"+recordsperpage+","+currentpagenum+","+totalpage+","+countrows+")\"   title=\"上一页\"><i class=\"icon-step-backward\"></i></button>";
   	       }
	        pageHTML +="["+currentpagenum+"/"+totalpage+"]";
	        if (currentpagenum==totalpage){
	          pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"下一页\"><i class=\"icon-step-forward\"></i></button>";
	          pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"末页\"><i class=\"icon-fast-forward\"></i></button>";
	        }else{
		      pageHTML +="<button type=\"button\"  class=\"btn btn-mini btn-link\" onclick=\"PreNextFlashRownum('"+tableid+"','next',"+recordsperpage+","+currentpagenum+","+totalpage+","+countrows+")\" title=\"下一页\"><i class=\"icon-step-forward\"></i></button>";
		      pageHTML +="<button type=\"button\"  class=\"btn btn-mini btn-link\" onclick=\"PreNextFlashRownum('"+tableid+"','end',"+recordsperpage+","+currentpagenum+","+totalpage+","+countrows+")\"  title=\"末页\"><i class=\"icon-fast-forward\"></i></button>";
	        }
            pageHTML +="转到：";
            pageHTML +="<input type=\"text\" class=\"span1\" id=\"go_"+tablistID.id+"\" placeholder=\"\">";
            pageHTML +="<button type=\"button\" class=\"btn\" onclick=\"PreNextFlashRownum('"+tableid+"','go',"+recordsperpage+","+currentpagenum+","+totalpage+","+countrows+")\" >GO</button>";
            pageHTML +="<span>"+recordsperpage+"条/页　共"+countrows+"条记录</span>";
            pageHTML +="</form>";
            $("#page_"+tablistID.id).find("form").remove();
	   	    $("#page_"+tablistID.id).html(pageHTML);   
	   	    var noPage =  tablistID.getAttribute("noPage");
	    	if(countrows>0){
	    		if(noPage&&noPage=="true"){
	    			$("#page_"+tablistID.id)[0].style.display="none";
	    		}else{
	    			$("#page_"+tablistID.id)[0].style.display="";
	    		}
	    	}else{
	    		$("#page_"+tablistID.id)[0].style.display="none";
	    	}

		
	};
	//初始化分页控件
	doInitPages  = function (tablistID){
		
	 var pageHTML  ="<form class=\"form-inline\">";
		 pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"首页\" ><i class=\"icon-fast-backward\"></i></button>";
		 pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"上一页\"><i class=\"icon-step-backward\"></i></button>";
		 pageHTML +="[0/0]";
		 pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"下一页\"><i class=\"icon-step-forward\"></i></button>";
		 pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"末页\"><i class=\"icon-fast-forward\"></i></button>";
         pageHTML +="转到：";
         pageHTML +="<input type=\"text\" class=\"span1\" placeholder=\"\">";
         pageHTML +="<button type=\"button\" class=\"btn\">GO</button>";
         pageHTML +="<span> 条/页　共     条记录</span>";
         pageHTML +="</form>";
        $("#page_"+tablistID.id).find("form").remove();
 		$("#page_"+tablistID.id).html(pageHTML);
 		$("#page_"+tablistID.id)[0].style.display="none";
		
	};
	
	
	xAlert =function(title,content,type)
	{  
		if(title==null||title==undefined||title==""){
			title = "信息提示";
		}
		if(type==undefined){
			type = "1";//默认状态成功
		}
		if(title==undefined||title ==null){
			title = "";
		}
		var cssName = $("#pubAlert").attr("class");
		$("#pubAlert").removeClass(cssName)
		if(type==1){//成功
			$("#pubAlert").addClass("alert alert-success success-b-open");
		}
		if(type==2){//失败
			$("#pubAlert").addClass("alert alert-error error-b-open");
		}
		if(type==3){//警告

			$("#pubAlert").addClass("alert alert-block block-b-open");
		}
		
		$("#pubAlert").find("h4").text(title);
		$("#pubAlert").find("span").html(content);
		
		$("#pubAlert").slideDown('fast').delay(2000).slideUp('fast');

//		
//		$('#pubAlert').modal({
//		    backdrop:false,
//		    keyboard:false,
//		    show:true
//		});
		

	};
	xConfirm =function(title,content)
	{
	 if($("#myConfirm")&&$("#myConfirm").length>0){
		$("#myConfirmTitle").text(title);
		$("#myConfirmContent").html(content);
		$("#myConfirm").modal("show");
	 }
	};

    printTabList = function(tableid,templateName,fieldnames,startXY){
    	var tabid = "";
    	if(typeof(tableid)=="string"){
    		tabid = tableid;
    	}else{
    		tabid = tableid.id;
    	}
    	if(!templateName) templateName = "";
    	if(!fieldnames) fieldnames = "";
    	if(!startXY) startXY = "";
    	$(window).manhuaDialog({"title":"导出","type":"text","content":g_sAppName+"/jsp/framework/print/TabListEXP.jsp?tabId="+tabid+"&templateName="+templateName+"&fieldnames="+fieldnames+"&startXY="+startXY,"modal":"3"});

    	
    };
    printCustomTabList = function(tableid,path){
    	var tabid = "";
    	if(typeof(tableid)=="string"){
    		tabid = tableid;
    	}else{
    		tabid = tableid.id;
    	}
    	$(window).manhuaDialog({"title":"导出","type":"text","content":g_sAppName+"/jsp/framework/print/TabListCustomEXP.jsp?tabId="+tabid+"&path="+path,"modal":"3"});

    	
    };
    //--------------------------------------
    //-add by zhangbr@ccthanking.com 用于自定义导出Excel
    //--------------------------------------
	function doCustomExportExcel(tableid,templateName,fieldnames,startXY,controllerName,methodName){
		var tabid = "";
		if(typeof(tableid)=="string"){
			tabid = tableid;
		}else{
			tabid = tableid.id;
		}
		if(!templateName) templateName = "";
		if(!fieldnames) fieldnames = "";
		if(!startXY) startXY = "";
		if(!methodName) methodName = "";
		$(window).manhuaDialog({"title":"导出","type":"text","content":g_sAppName+"/jsp/framework/print/TabListExpOnServer.jsp?tabId="+tabid+"&templateName="+templateName+"&fieldnames="+fieldnames+"&startXY="+startXY+"&controllerName="+controllerName+"&methodName="+methodName,"modal":"3"});
	}
    //弹出窗口关闭--父页面回调空函数
    function closeParentCloseFunction(val){
	}
    //弹出窗口关闭--弹出页面回调空函数
    function closeNowCloseFunction(){
	}


    
    
    
    

 
 /*
 * 表格同步方法
 * param： aimtable 目标表
 * param:  sorucestable 源表
 */
defaultJson.updateBySrcTab = function(aimtable , sourcetab) {

			var sourcetabrows = sourcetab.getTableRows();

			for(var i=0;i < sourcetabrows;i++){
	
				//get sourcetable every row json
				var srcrowjson = sourcetab.getSelectedRowJsonByIndex(i);

				$("#"+aimtable.id).updateResult(srcrowjson,aimtable,i);
			}
		
	};
  
      /*
     * 更新行记录的rowJson值通过修改过的行内容从服务器返回后组成的JSon串
     * params：
     * rowJsonObj：行记录的rowJson属性的对象
     * ReturndataObj：返回行内容组成的Json串对象
     * return:
     * 修改后的rowJson对象
     */
	defaultJson.updateByreturn = function(rowJsonObj,ReturndataObj){ 
 //   function updateRowJsonByReturnData(rowJsonObj,ReturndataObj){
//    	alert(tharrays);

//		for(var i=0;i<tharrays.length;i++){
			for ( var returnkey in ReturndataObj) {
				//alert("returnkey:"+returnkey);
			    var hascol = false;
				for ( var jsonKey in rowJsonObj) {
					if( returnkey == jsonKey){
					//	alert("ReturndataObj[returnkey]:"+ReturndataObj[returnkey]);
						rowJsonObj[jsonKey] = ReturndataObj[returnkey];
						hascol = true;
						break;
					}
				}
				if(hascol == false){
					//获取returnkey 还有value 插入对象中
					var temstr = addjson(rowJsonObj,returnkey,ReturndataObj[returnkey]);
					rowJsonObj = JSON.parse(temstr);
				}
		}
//		alert("rowJsonObj:"+JSON.stringify(rowJsonObj));
    	return rowJsonObj;
    };
    
	function addjson(jsonobj,element,attribute){
	    	
	    	var jsonString= JSON.stringify(jsonobj);
	    	
	    		jsonString = jsonString.substring(0,jsonString.length-1);
	    		
	    	var jsonArr=",\""+element+"\":\""+attribute+"\"}";
	    	
	    		jsonString=jsonString.concat(jsonArr);
	    		
	    		return jsonString;
	    }; 
	    
    /**
     * 自动匹配完成input填写
     * @param ID  input的ID
     * @param controllerName  调用查询的后台类路径
     * @param queryConditionFunction  查询其他对象接口
     */
function  showAutoComplete (ID,controllerName,queryConditionFunction){
		$("#"+ID).autocomplete({
			source:function(query,process){
				var matchCount = this.options.items;
				var queryCondition = "";
				if(queryConditionFunction !=undefined){
					if(typeof(queryConditionFunction)=="string"){
						queryCondition = eval(queryConditionFunction+"()");
					}else if (typeof(queryConditionFunction)=="object"){
						queryCondition= JSON.stringify(queryConditionFunction);
					}
					
				}
				var tablePrefix = $("#"+ID).attr("tablePrefix")?$("#"+ID).attr("tablePrefix"):"";
				var tableName = $("#"+ID).attr("tableName")?$("#"+ID).attr("tableName"):"";
				$.ajaxSetup({ 
					  async: false 
			    }); 
				$.post(controllerName,{"matchInfo":queryCondition,"matchCount":matchCount,"matchInputValue":query,"tablePrefix":tablePrefix,"tableName":tableName},function(respData){
					return process(respData);
				});
			},
			formatItem:function(item){
				//return item["regionName"]+"("+item["regionNameEn"]+"，"+item["regionShortnameEn"]+") - "+item["regionCode"];
			    return item["regionName"];
			},
			setValue:function(item){
				return {'data-value':item["regionName"],'code':item["regionCode"]};
			}
		});
}

function showAutoComplete_simple(ID,controllerName,queryConditionFunction){
	$("#"+ID).autocomplete({
		source:function(query,process){
			var matchCount = this.options.items;
			var queryCondition = "";
			if(queryConditionFunction !=undefined){
				if(typeof(queryConditionFunction)=="string"){
					queryCondition = eval(queryConditionFunction);
				}else if (typeof(queryConditionFunction)=="object"){
					queryCondition= JSON.stringify(queryConditionFunction);
				}
			}
			$.ajaxSetup({ 
				  async: false 
		    });
			$.post(controllerName,{"matchInfo":queryCondition,"matchCount":matchCount,"matchInputValue":query},function(respData){
				return process(respData);
			});
		},
		formatItem:function(item){
			//return item["regionName"]+"("+item["regionNameEn"]+"，"+item["regionShortnameEn"]+") - "+item["regionCode"];
		    return item["regionName"];
		},
		setValue:function(item){
			return {'data-value':item["regionName"],'code':item["regionCode"]};
		},
		items:8	//defalt allow items
	});
}

//行选空函数
function tr_click(obj,tabListid){
	
}
//回调函数
function prcCallback()
{
	
}

/**
 * 加法函数，用来得到精确的加法结果
 * 说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
 * 调用：accAdd(arg1,arg2)
 * 返回值：arg1加上arg2的精确结果  
 */ 
function accAdd(arg1,arg2){ 
  var r1,r2,m; 
  try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;} 
  try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;} 
  m=Math.pow(10,Math.max(r1,r2));
  return (arg1*m+arg2*m)/m;
} 

//给Number类型增加一个add方法，调用起来更加方便。 
String.prototype.numAdd = function (arg){ 
  return accAdd(arg,this); 
};



/**************************************************************************************************************
 * 业务单独添加部分
 **************************************************************************************************************/


//成功提示信息方法(兼容之前的提示方法)
var xAlertMsg = function(msg,title){
	xAlert((title?title:"信息提示"),msg,1);
};

//成功提示信息方法
var xSuccessMsg = function(msg,title){
	xAlert((title?title:"信息提示"),msg,1);
};

//信息提示信息方法（含警告）
var xInfoMsg = function(msg,title){
	xAlert((title?title:"信息提示"),msg,3);
};

//失败提示信息方法
var xFailMsg = function(msg,title){
	xAlert((title?title:"信息提示"),msg,2);
};


//统一要求选择一条数据的提示信息
var requireSelectedOneRow = function(msg){
	xInfoMsg(msg?msg:'请选择一条要操作的数据！',null);
};


//导出提示（暂时这样判断）
var exportRequireQuery = function(tabObj,msg){
	if(0 >= tabObj.getTableRows()){
		xInfoMsg(msg?msg:'请至少查询出一条数据！',null);
		return false;
	}
	return true;
};

var successInfo = function(msg,title){
	xSuccessMsg(msg?msg:'操作成功！',null);
};

//统一form保存验证不通过提示
requireFormMsg = function(msg){
	xInfoMsg(msg?msg:'表单中有未填写完整或不合法的内容，请检查！',null);
};

//根据传入的项目编号获取项目手册页面的json字符
function xmscUrl(id){
	 var  xmsc = {"title":"项目信息卡","type":"text","content":g_sAppName+"/jsp/business/jhb/xdxmk/rowView.jsp?id="+id,"modal":"1"};
	 return xmsc;
}


//重新加载select表选字典
function reloadSelectTableDic(obj){
	var noNullSelect = false;
	if(obj.attr("noNullSelect")&&obj.attr("noNullSelect")=="true")
    {
    	noNullSelect = true;
    }
	
	if (obj.attr("src").substr(0,2).toUpperCase() == "T#"){
		if(obj.attr("src") == 'T#'){
			var defaultMemo = "请选择";
			obj.html('');
			if(noNullSelect == false)
    		obj.append("<option value=''>"+defaultMemo+"</option>");
		}
		var vparam = obj.attr("src").substr(2); //去掉T#
	    while(vparam.indexOf("%")>0){
	    	vparam = vparam.replace("%","$");
	    }
	    var xmlDom = null;
	    var xmlhttp2 ;
	    if(window.ActiveXObejct){
	        request = new ActiveXObject("Microsoft.XMLHTTP");
	     }
	     else if(window.XMLHttpRequest){
	        request = new XMLHttpRequest();
	     }
	        var path =  g_sAppName + "/servlet/GetDicFromTable?param=" + vparam + "&r="+Math.random();
	        $.ajax({
	    		url : path,  
	    		async :	false,
	    		success : function(response,config) {
	    			var parser = new DOMParser(); 
	    			var defaultMemo = "请选择";
	                if(obj.attr("defaultMemo") && obj.attr("defaultMemo")!="")
	                {
	                	defaultMemo = obj.attr("defaultMemo");
	                }
	                
	    			var dic_dom = parser.parseFromString(response, "text/xml"); 
	            	if(dic_dom){
	            		obj.html('');
	            		if(noNullSelect == false)
	            		obj.append("<option value=''>"+defaultMemo+"</option>");
	                 var list = dic_dom.getElementsByTagName("R");
	                 for(var i = 0;i<list.length;i++){
	                 	 var code = list[i].getAttribute("c");
	                      var value = list[i].getAttribute("t");
	                      obj.append("<option value='"+code+"'>"+value+"</option>");
	                 }
	                }
	
	    		}
	    	});
	}else{
        var dic_dom = loadXmlFile(g_sDicPath + obj.attr("src") + ".xml");
        var defaultMemo = "请选择";
        if(obj.attr("defaultMemo") && obj.attr("defaultMemo")!="")
        {
        	defaultMemo = obj.attr("defaultMemo");
        }
        if(dic_dom){
        	obj.html('');
        	 if(noNullSelect == false)
        	 obj.append("<option value=''>"+defaultMemo+"</option>");
	         var list = dic_dom.getElementsByTagName("R");
	         for(var i = 0;i<list.length;i++){
	         	 var code = list[i].getAttribute("c");
	              var value = list[i].getAttribute("t");
	              obj.append("<option value='"+code+"'>"+value+"</option>");
	         }
        }
	}
}

/**
 * 根据计划查询非应急批次
 * ndObj 年度的jquery对象
 * mcObj 计划名称jquery对象
 */
function generateFyjjhNdMc(ndObj,mcObj){
	ndObj.attr("src","T#GC_JH_SJ: distinct ND NDCODE:ND:SFYX='1' order by ND ASC");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	//ndObj.val(new Date().getFullYear());
	setDefaultNd(ndObj.attr("id"));
	setDefaultOption(ndObj,ndObj.val());
	if(mcObj){
		mcObj.attr("kind","dic");
		mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND XFLX='1' AND ND='" + ndObj.val()+"' ORDER BY JHPCH ASC");
		mcObj.html('');
		reloadSelectTableDic(mcObj);
		ndObj.change(function() {
			mcObj.html('');
			if(!ndObj.val().length){
			}
			mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1'  AND XFLX='1' AND ND='" + ndObj.val()+"' ORDER BY JHPCH ASC");
			reloadSelectTableDic(mcObj);
		});
	}
}

//判断是否select对象中存在某一项值
function isExistOption(obj,value) {
	var isExist = false;
	var count = obj.find('option').length;
	for ( var i = 0; i < count; i++) {
		if (obj.get(0).options[i].value == value) {
			isExist = true;
			break;
		}
	}
	return isExist;
}

//指定select对象的选中index
var setOptionSelectedIndex = function(obj,index) {
	if(!index){
		var count =obj.get(0).options.length;
		index = count>0?count-1:0;
	}
	obj.get(0).selectedIndex = index;
};

//设置select对象的默认选中值
var setDefaultOption = function(obj,value){
	obj.val(isExistOption(obj,value)?value:"");
};

//公共选择项目(含标段)
function queryProjectWithBD(paras){
	$(window).manhuaDialog({"title":"项目列表","type":"text","content":g_sAppName + "/jsp/business/gcb/kfglgl/xmcx.jsp?"+paras,"modal":"2"});
	
}

//公共选择项目(不含标段)
function queryProjectNotWithBD(){
	queryProjectWithBD();
}
//公共查看多个附件脚本
function showAllFiles(ywid,lb){
	$(window).manhuaDialog({"title":"附件列表","type":"text","content":g_sAppName +"/jsp/file_upload/fjList.jsp?ywid="+ywid+"&lb="+lb,"modal":"4"});
}
//公共查看多个附件脚本
function showAllFilesWithWybh(wybh,lb){
	$(window).manhuaDialog({"title":"附件列表","type":"text","content":g_sAppName +"/jsp/file_upload/fjListWithWybh.jsp?wybh="+wybh+"&lb="+lb,"modal":"4"});
}
//公共信息推送方法
function sendMsg(title,flag) {
	title = title?title:"信息推送";
	var flagCond = "";
	if(flag!="" && flag!=undefined){
		flagCond = "?flag="+flag;
	}
	var url = g_sAppName+"/jsp/business/xtbg/msg/addNbdx.jsp"+flagCond;
	$(window).manhuaDialog({"title" : title, "type" : "text", "content" : url, "modal":"4"});
}

//一些通用高度，可用于自定义
var pageTopHeight = 17+10;	//页面顶头高度
var pageTitle = 44+5;		//页面标题高度
var pageQuery = 41+5;		//查询层高度
var pageTableTh = 32;	//表格头部高度
var pageNumHeight = 46;	//分页层高度
var pageTableOne = 33;	//表格每一行高度
var pageTab = 41+10;//tab页

function getTableTh(num){
	return 32+(pageTableOne*(num-1));
}

//数字补小数点
function doXSD(num){
	var obj = num.split(".");
	if(obj.length == 1){
		return num+".00";
	}
	for(var i=2;i>obj[1].length;i--){
		num = num + "0";
	}
	return num;
}

//选择参建单位
function selectCjdw(filedname){
	if(!filedname){
		alert("调用选择参建单位方法传入参数不正确！");
		return;
	}
	var paras = "?field=" + $("#"+filedname).attr("selObj");
	if($("#"+filedname).attr("dwlx")){
		paras += "&dwlx=" + $("#"+filedname).attr("dwlx");
	}
	if($("#"+filedname).attr("isLxSelect")){
		paras += "&isLxSelect=" + $("#"+filedname).attr("isLxSelect");
	}
	$(window).manhuaDialog({"title" : "参建单位","type" : "text","content" : g_sAppName + "/jsp/business/gcb/cjdw/cjdw_Query_Add1.jsp"+paras,"modal":"4"});
}

//选择相关字典（收发文用）
function selectLwdw(filedname){
	if(!filedname){
		return false;
	}
	var paras = "?field=" + filedname + "&category="+$("#"+filedname).attr("category");
	$(window).manhuaDialog({"title" : "选择","type" : "text","content" : g_sAppName + "/jsp/framework/system/dict/commonDict.jsp"+paras,"modal":"4"});
}

//自动完成(收发文用)
function autoCommonDict(id,url){
	showAutoComplete_simple(id,url,"getDictQuery('"+id+"')"); 
}
function getDictQuery(id){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"asc","fieldname":"pxh"}]}';
	var jsonData = eval('(' + initData + ')'); 
	var defineCondition = {"value": $("#"+id).attr("category"),"fieldname":"DICT_CATEGORY","operation":"=","logic":"and"};
	jsonData.querycondition.conditions.push(defineCondition);
	var defineCondition = {"value": "%"+$("#"+id).val().trim()+"%","fieldname":"DICT_NAME","operation":"like","logic":"and"};
	jsonData.querycondition.conditions.push(defineCondition);
	return JSON.stringify(jsonData);
}

/***
 * 部门监控页面项目数量的查详细页面
 * @param ywlx			业务类型
 * @param nd			年度
 * @param bmjkLx		部门监控类型，在配置文件中是key值
 * @param tableName		表名
 * @param xmglgs		项目管理公司
 */
function xmxxView(ywlx, nd, bmjkLx, tableName, xmglgs){
	var condition = "bmjkLx="+bmjkLx+"&ywlx="+ywlx+"&nd="+nd+"&tableName="+tableName+"&xmglgs="+xmglgs;
	var  xmsc = {"title":"统筹计划管理信息","type":"text","content":g_sAppName+"/jsp/business/bmjk_common/xmxx.jsp?"+condition,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}

/***
 * 部门监控页面标段数量的查详细页面
 * @param ywlx			业务类型
 * @param nd			年度
 * @param bmjkLx		部门监控类型，在配置文件中是key值
 * @param tableName		表名
 * @param xmglgs		项目管理公司
 */
function xmxx_bdxxView(ywlx, nd, bmjkLx, tableName, xmglgs){
	var condition = "bmjkLx="+bmjkLx+"&ywlx="+ywlx+"&nd="+nd+"&tableName="+tableName+"&xmglgs="+xmglgs;
	var  xmsc = {"title":"统筹计划跟踪信息","type":"text","content":g_sAppName+"/jsp/business/bmjk_common/xmxx_bdxx.jsp?"+condition,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}


/**
 * 处理session过期
 * add by hongpeng.dong at 2013-12-24
 */
$.ajaxSetup({     
    contentType:"application/x-www-form-urlencoded;charset=utf-8",     
    complete:function(XMLHttpRequest,textStatus){  
        // 通过XMLHttpRequest取得响应头，sessionStatus，  
        var sessionstatus=XMLHttpRequest.getResponseHeader("sessionStatus");  
        if(sessionstatus=="sessionOut"){     
        	window.location = g_sAppName+"/jsp/framework/error/500.jsp";     
        }  
    }  
});

/**
 * 警告信息调用，延时显示8秒
 */
xWarning = function(content,title){  
	if(title==null||title==undefined||title==""){
		title = "警告信息";
	}
	var cssName = $("#pubAlert").attr("class");
	$("#pubAlert").removeClass(cssName);
	$("#pubAlert").addClass("alert alert-block block-b-open");
	
	$("#pubAlert").find("h4").text(title);
	$("#pubAlert").find("span").html(content);
	$("#pubAlert").slideDown('fast').delay(8000).slideUp('fast');
};

/**
 * 表格表头点击排序
 * add by hongpeng.dong at 2014.3.24
 * @param tableId 需要排序表格的id
 * 注：
 * 1、页面需排序的th配置sort="true"
 * 2、提供名称为sortQuery的查询方法（可同queryList）
 * 3、页面初始化调用该方法
 */
function initTHSort(tableId){
	$("#"+tableId+" th[sort=true]").each(function(i){
		$(this).append('<img src="'+g_sAppName+'/images/sort_both.png"/>');
		$(this).css("cursor","pointer");
		$(this).click(function(){
			try{
				var filedName = $(this).attr("fieldname");
				var order = $(this).attr("order");
				order = ("desc" == order)?"asc":"desc";
				$("#txtFilter").attr("fieldname",filedName);
				$("#txtFilter").attr("order",order);
				$(this).attr("order",order);
				sortQuery();
			}catch(e){
			}
		})
	})
}

