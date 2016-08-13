/**
* 将Form 的数据转化成查询条件json字符串的函数
* 2013-7-15
* cbl
*/

var combineQuery = new Object();

/**
* 将web Form 采集的数据转化成json字符串
* 传入web form对象
* 输出由form元素名称及其值组成的json字符串
* 每个元素对应的值全部是数组
*/
combineQuery.getQueryData = function (form)  {
   // var json = '{ querycondition: {conditions: [';
    var json ='';
	var i = 0;
	var max = form.elements.length;
	var e,name,operation,value,fieldtype,fieldformat;
	var tmpstr;
	// 循环处理form的元素
    for (i=0; i < max; i++) {
        e = form.elements[i];
        name = $(e).attr("fieldname");	//只判断有fieldname属性的表单
        if(!name) continue;
        switch (e.type) {
            case 'radio':
			case 'checkbox':
			if (json.indexOf('"' + name + '":') < 0) // 表示该checkbox没有处理过
			{
				// 每个元素组成一个json对象
				// 获取checkbox值
				tmpstr = combineQuery.getCheckboxResult(form, e);
				if (tmpstr != "") // 空值不处理
				{
					json += '{' + tmpstr + ',"fieldname' + '":"'
							+ $(e).attr("fieldname") + '"';
					// 获取fieldtype:date,fieldformat:YYYYMMDD
					// if(typeof($("#aid").attr("rel"))=="undefined")
					if (typeof ($(e).attr("fieldtype")) != "undefined") {
						json += ',"fieldtype' + '":"' + $(e).attr("fieldtype")
								+ '"';
					}
					// fieldformat
					if (typeof ($(e).attr("fieldformat")) != "undefined") {
						json += ',"fieldformat' + '":"'
								+ $(e).attr("fieldformat") + '"';
					}
					// operation
					if (typeof ($(e).attr("operation")) != "undefined") {
						json += ',"operation' + '":"' + $(e).attr("operation")
								+ '"';
					}
					json += ',"logic' + '":"' + 'and"';
					json += "},";
				}
			}
			break; 
			
			case 'hidden':
			case 'password':
			case 'text':
			case 'number'://add by zhangbr@ccthanking.com 添加对number类型条件的处理
			case 'date':
			case 'month':
			case 'textarea':
				tmpstr = e.value;
				if (tmpstr != "" )  // 空值不处理
					{
						if(typeof($(e).attr("operation")) != "undefined"&&$(e).attr("operation") == "like")
							{
							    json +='{'+  '"value":"%' + tmpstr + '%",'+'"fieldname'+ '":"'+$(e).attr("fieldname")+'"';
							}else{
								json +='{'+  '"value":"' + tmpstr + '",'+'"fieldname'+ '":"'+$(e).attr("fieldname")+'"';
							}
				
				if(typeof($(e).attr("fieldtype")) != "undefined")
				{
					json +=',"fieldtype'+ '":"'+$(e).attr("fieldtype")+'"';
				}
			if(typeof($(e).attr("fieldformat")) != "undefined")
			{
			json +=',"fieldformat'+ '":"'+$(e).attr("fieldformat")+'"';
			}
			//operation
			if(typeof($(e).attr("operation")) != "undefined")
			{
			json +=',"operation'+ '":"'+$(e).attr("operation")+'"';
			}
			json +=',"logic'+'":"'+'and"';
				json +="},";
					}
				break;
			case 'select-one':
				tmpstr = combineQuery.getSelectResult(e);
				if (tmpstr  != '"value":""' && tmpstr!=""){   // 空值不处理//modify by zhangbr 加入tmpstr!=""，防止当下拉框无选项时报错
					json += '{' + tmpstr+',"fieldname'+ '":"'+$(e).attr("fieldname")+'"';
			
					if(typeof($(e).attr("fieldtype")) != "undefined")
					{
						json +=',"fieldtype'+ '":"'+$(e).attr("fieldtype")+'"';
					}
					//fieldformat
					if(typeof($(e).attr("fieldformat")) != "undefined")
					{
						json +=',"fieldformat'+ '":"'+$(e).attr("fieldformat")+'"';
					}
					//operation
					if(typeof($(e).attr("operation")) != "undefined")
					{
						json +=',"operation'+ '":"'+$(e).attr("operation")+'"';
					}
					json +=',"logic'+'":"'+'and"';
					json +="},";
				}
				break;
			case 'select-multiple':
				tmpstr = combineQuery.getSelectResult(e);
				if (tmpstr  != '"value":""' && tmpstr!="")   // 空值不处理// 空值不处理//modify by zhangbr 加入tmpstr!=""，防止当下拉框无选项时报错
					{
					json += '{' + tmpstr+',"fieldname'+ '":"'+$(e).attr("fieldname")+'"';
				
				if(typeof($(e).attr("fieldtype")) != "undefined")
				{
					json +=',"fieldtype'+ '":"'+$(e).attr("fieldtype")+'"';
				}
			//fieldformat
			if(typeof($(e).attr("fieldformat")) != "undefined")
			{
			json +=',"fieldformat'+ '":"'+$(e).attr("fieldformat")+'"';
			}
			//operation
			if(typeof($(e).attr("operation")) != "undefined")
			{
			json +=',"operation'+ '":"'+$(e).attr("operation")+'"';
			}
			json +=',"logic'+'":"'+'and"';
				json +="},";
					}
				break;
			case 'button':
			case 'file':
			case 'image':
			case 'reset':
			case 'submit':
			
				//alert(e.value);
				break;
			default:
        }
    };
    return json.substring(0, json.length - 1) ; // 返回json字符串
    
}
/** 
 * 获取隐藏表单frmPost查询条件
 * 
 */

combineQuery.getQueryOrderData = function (form)  {
	 var json ="orders:[";
		var j = 0;
		var max = form.elements.length;
		var e;
		// 循环处理form的元素
	    for (i=0; i < max; i++) {
	        e = form.elements[i];
	        if(e.name=="txtFilter")
	        	{
	        	if($(e).attr("fieldname")==""||$(e).attr("fieldname")==null){
	        	    	return "";
	        	}
	        	if(j!=0)json+=",";
	        	//get order
	        	if(typeof($(e).attr("order")) != "undefined")
					{
	        			json +='{"order'+ '":"'+$(e).attr("order")+'"';
					}
	        	//get value
	        	if(typeof($(e).attr("fieldname")) != "undefined")
					{
	        			json +=',"fieldname'+ '":"'+$(e).attr("fieldname")+'"}';
					}
	        	j++;
	        	}
	        
	    }
	    //if(j==0) json+="{}";
	    json += "]";
	    return json;
}

/** 
 * 将getQueryData方法的串与getQueryOrderData生成的串组成新的串,新的串即为查询条件串
 * queryform查询表单formname
 * orderform隐藏表单formname
 * tableid  查询表单tableid
 */

combineQuery.getQueryCombineData = function (queryform,orderform,tableid) {
	var querydata="";
	var orderdata="";
	var tempdata = "";
	var json = "";
	var pageNum = "10";
	if(tableid)
	{
		pageNum = $("#"+tableid.id).attr("pageNum");
		if(pageNum==null||pageNum=="")
		{
			pageNum="10";
		}
	}
	querydata = combineQuery.getQueryData(queryform);
	orderdata = combineQuery.getQueryOrderData(orderform);
	json = "{querycondition: {conditions: [" +querydata+" ]},pages:{recordsperpage:"+pageNum+", currentpagenum:1, totalpage:0, countrows:0}";
	if(orderdata!=null&&orderdata!=""){
		json +=","+orderdata;
	}
	json +="}";
	return json;
}

/**
* 取得SELECT的值组成的json字符串
* 传入SELECT对象
* 输出由SELECT元素名称及其被选取的选项的值组成的json字符串
*     形如：name:["v1","v2","v3]
*/
combineQuery.getSelectResult = function (oS) {
	var l = oS.options.length;
	var i = 0;
	var eName=oS.name;
	var eValue="";
	for (i=0; i<l; i++)
	{
	  if (oS[i].selected == true )
	  {
		 eValue += ',"'+oS[i].value+'"';
	  }
	}
	if (eValue != "")
	{
		eValue = '"value":'+ eValue.substr(1)+"";
	}
	return eValue;
}

/**
* 取得form中所有同名的checkbox的值组成的json字符串
* 传入form和checkbox对象
* 输出由checkbox元素名称及其值组成的json字符串
*     形如：checkboxname:["c1","c2","c3]
*/
combineQuery.getCheckboxResult = function (form,e) {
	var  max = form.elements.length;
	var i=0;
	var oE;
	var strTemp="\"";
    for (i=0; i < max; i++) {
		oE = form.elements[i];
		if (oE.name != e.name)
			continue;   // 元素名称不同，就跳过
		if (oE.checked)  // 只有选中的才处理
			strTemp += oE.value+',';
	}
    if(strTemp.length>1){
    	strTemp = strTemp.substring(0, strTemp.length-1);
    }
    strTemp +="\"";
	if (strTemp.length >2)
	{
		strTemp = '"value":'+ strTemp+'';
	}else{
		strTemp ="";
	}
	return strTemp;
}