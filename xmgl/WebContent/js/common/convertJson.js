/******************************
 *功能：处理json数据
 *1.将json字符串转换成json对象
 *2.将json对象转换成json字符串
 *
 ****************************/

var convertJson = new Object();

/**
 *  将字符串转化为json对象的方法一
 */
convertJson.string2json1 = function(strJson){
	try
	{
		var j = "(" + strJson +")";  // 用括号将json字符串括起来
	    return eval(j); // 返回json对象
	}
	catch (e)
	{
		return null;
	}
}


/**
 *  将字符串转化为json对象的方法二
 */
convertJson.string2json2 = function(strJson){
	try
	{
		eval("var j = " + strJson);  // 得到一个变量
		return j; // 返回变量的值
	}
	catch (e)
	{
		return null;
	}
}

/**
 *  将字符串转化为json对象的方法三
 */ 
convertJson.string2json3 = function(strJson){
	try
	{
		var f = new Function("return " + strJson + ";"); // 得到一个函数
		return f(); // 执行函数，并返回函数的值
	}
	catch (e)
	{
		return null;
	}
}


/**
 *  将简单的json对象转换成字符串（不能处理包含数组和嵌套json对象的情况）
 */
convertJson.simplejson2string = function (oJson) {
	var s=""
	for(var property in oJson){
		s += ( ',"'+property +'":"'+oJson[property].valueOf()+'"' );
	}
	return s.substring(1);
}

/**
 * 将json对象转换成字符串
 * 引用：http://hi.baidu.com/ruson/blog/item/a1f740540c5aee5d574e0002.html
 */
convertJson.json2String = function(o) {
	// 闭包函数，处理不同的json属性
	var fmt = function(s) {
		if (s == null) {
			return "null";
		}
		switch (s.constructor) {
			case Array: 
				return "[" + convertJson.json2String(s) + "]";
			case Object: 
				return convertJson.json2String(s); 
			case Number: 
				return s; 
			case String: 
				return '\"' + s.replace(/\//g, '\\/') + '\"'; 
			case Boolean: 
				return s ? "true" : "false"; 
		} 
	}
}

/**********************************
 * 处理json数据的对象的函数定义结束
 *********************************/
