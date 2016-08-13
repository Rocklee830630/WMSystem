/**
* 将web Form 的数据转化成json字符串的函数
*/

var Form2Json = new Object();

/**
* 将web Form 采集的数据转化成json字符串
* 传入web form对象
* 输出由form元素名称及其值组成的json字符串
* 元素的值全部使用escape()进行了转意处理
* 每个元素对应的值全部是数组
* 格式形如：{"xm":["%u5F20%u4E09"],"xb":["%u7537"],"VIP":["h1","h3","h5"]}
*/
Form2Json.formToJSON = function (form)  {
    var json = '{';
    var isarray = false;
	var i = 0;
	var max = form.elements.length;
	var e,name,lastarr;
	var tmpstr;
	// 循环处理form的元素
    for (i=0; i < max; i++) {
        e = form.elements[i];
        name = $(e).attr("fieldname");	//只判断有fieldname属性的表单
        if(!name) continue;
        switch (e.type) {
			case 'checkbox':
			case 'radio':
				if ( json.indexOf('"'+name+'":')<0 ) // 表示该checkbox没有处理过
				{
					tmpstr = Form2Json.getCheckboxResult(form,e);
					
					if (tmpstr != "")   // 空值不处理
						json += tmpstr+",";
				} 
				break; 

			case 'hidden':
			case 'password':
			case 'text':
			case 'number':
			case 'textarea':
				tmpstr = e.value;
				if (tmpstr != null ){  // 空值不处理
			      if($(e).attr("code"))
			    	  {
			    	      json += '"' + name + '":' + JSON.stringify($(e).attr("code")) + ',';
			    	      json += '"' + name + '_SV":' + JSON.stringify(tmpstr) + ',';

			    	  }else{
			    		  tmpstr = JSON.stringify(tmpstr);
			    		  json += '"' + name + '":' + tmpstr + ',';
			    	  }
					 
				}
				break;
			case 'month':
				tmpstr = e.value+"-01";
 				if (tmpstr != null ){  // 空值不处理
   			    		  json += '"' + name + '":' + JSON.stringify(tmpstr) + ',';
   				}
 				break;
			case 'date':
 				tmpstr = e.value;
 				if (tmpstr != null ){  // 空值不处理
   			    		  json += '"' + name + '":' + JSON.stringify(tmpstr) + ',';
   				}
 				break;
			case 'datetime-local':
 				tmpstr = e.value;
 				if (tmpstr != null ){  // 空值不处理
   			    		  json += '"' + name + '":' + JSON.stringify(tmpstr) + ',';
   				}
 				break;
			case 'select-one':
			case 'select-multiple':
				tmpstr = Form2Json.getSelectResult(e);
				if (tmpstr != "")   // 空值不处理
					json += tmpstr+",";
				break;

			case 'button':
			case 'file':
			case 'image':
			case 'reset':
			case 'submit':
				break;
			default:
        }
    };
    if(json.length==1)
    	json += "}";
    else 
    	json = json.substring(0, json.length - 1) + '}';
  // var resultjson = Form2Json.valueReplace(json);
    return json; // 返回json字符串
};

	

/**
* 取得SELECT的值组成的json字符串
* 传入SELECT对象
* 输出由SELECT元素名称及其被选取的选项的值组成的json字符串
*     形如：name:["v1","v2","v3]
*/
Form2Json.getSelectResult = function (oS) {
	var l = oS.options.length;
	var i = 0;
	var eName=$(oS).attr("fieldname");
	var eValue="";
	var eValue_sv = "";
	for (i=0; i<l; i++)
	{
	  if (oS[i].selected == true )
	  {
		 eValue += ','+oS[i].value+'';
		 if(oS[i].value==""){
			 eValue_sv += ',';
		 }else{
			 eValue_sv += ','+oS[i].text+'';
		 }
	  }
	}
	if (eValue != "")
	{
		eValue = '"'+eName+'":'+ JSON.stringify(eValue.substr(1))+',"'+eName+'_SV":'+JSON.stringify(eValue_sv.substr(1));
	}
	return eValue;
};

/**
* 取得form中所有同名的checkbox的值组成的json字符串
* 传入form和checkbox对象
* 输出由checkbox元素名称及其值组成的json字符串
*     形如：checkboxname:["c1","c2","c3]
*/
Form2Json.getCheckboxResult = function (form,e) {
	var  max = form.elements.length;
	var i=0;
	var oE;
	var strTemp="";
	var strTemp_sv ="";
    for (i=0; i < max; i++) {
		oE = form.elements[i];
		if ($(oE).attr("name") != $(e).attr("name"))
			continue;   // 元素名称不同，就跳过
		if (oE.checked && typeof($(oE).attr("kind"))=="undefined"){ // 只有选中的才处理
			strTemp += oE.value+',';
			strTemp_sv +=$(oE).attr("sv")+',';
		} 
		
	}
    if(strTemp_sv.length>0){
    	strTemp = strTemp.substring(0, strTemp.length-1);
    }
    if(strTemp_sv.length>0){
    	strTemp_sv = strTemp_sv.substring(0, strTemp_sv.length-1);
    }
	if (strTemp != "")
	{
		strTemp = '"'+$(e).attr("fieldname")+'":'+ JSON.stringify(strTemp)+',"'+$(e).attr("fieldname")+'_SV":'+JSON.stringify(strTemp_sv);
	}else{
		strTemp = '"'+$(e).attr("fieldname")+'":""';
	}
	return strTemp;
}