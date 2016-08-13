$(function() {
	$.fn.manhuaDialog = function(options) {
		var defaults = {
			Event : "click",								//触发响应事件
			title : "title",								//弹出层的标题
			type : "text",									//弹出层类型(text、容器ID、URL、Iframe)
			content : "about:blank",					    //弹出层的内容(text文本、容器ID名称、URL地址、Iframe的地址)
			width : 500,									//弹出层的宽度
			height : 400,									//弹出层的高度
			scrollTop : 200,								//层滑动的高度也就是弹出层时离顶部滑动的距离
			data  :"",                                      //弹出层传输的数据
			modal  : 1                                      //弹出层类型 1为全屏，2为中间弹出，3为自定义大小 
		};
		
		var openModal = [
		    {boxclass:'windowOpenBox hide fade',urlclass:'windowOpenBox-body'},
		    {boxclass:'ztqyzzOpenBox hide fade',urlclass:'windowOpenBoxMain-body'},
		    {boxclass:'modal hide fade',urlclass:'modal-body'},
		    {boxclass:'xxtcc150OpenBox hide fade',urlclass:'xxtcc150OpenBox-body',height:'405'},
	    ];
		
        var nowlocation = window.location;
        nowlocation = nowlocation.toString();
		var options = $.extend(defaults,options);
		var thismodal = openModal[options.modal-1];
		var boxclass = thismodal.boxclass;
		var urlclass = thismodal.urlclass;
		var height = thismodal.height?('height='+thismodal.height):'';
        var html_text ="<div p_location=\""+nowlocation+"\" isopenwindow=\"true\" class=\""+boxclass+"\"  tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"
		    	+"<div class=\"modal-header\" style=\"background:#0866c6;\"><button type=\"button\" class=\"close\" data-dismiss=\"modal\"   aria-hidden=\"true\"><i class=\"icon-remove icon-white\"></i></button>"
		    	+"<h3 style=\"color:white;\" >&nbsp;</h3></div><div   class=\""+urlclass+"\"></div>"
		    	+"";
		var parentObj = this[0].parent;//获得框架的主jsp对象
		var isframe = parentObj.$("#menuiframe");
		while (true){
			if(isframe.length==1){
				break;
			}else{
			   parentObj = parentObj.parent;
			   isframe = parentObj.$("#menuiframe");
			}
		}

		var bodyObj = (parentObj.document.getElementsByTagName("body"))[0];//获得body对象
		var $bodyObj = $(bodyObj);//转换为jquery对象
		//生成的弹出窗口采用顺序加载方式，避免后弹出的层显示不出来
		var prependObj = null;
		var isopenwindowObj = parentObj.$("[isopenwindow]");
		if(isopenwindowObj.length==0){
			prependObj = $bodyObj;
			prependObj.prepend(html_text);
		}else{
			prependObj = $(isopenwindowObj[isopenwindowObj.length-1]);
			prependObj.after(html_text);
		}
		var new_windowObj = parentObj.$("[isopenwindow]");
		if(new_windowObj.length>0){
			new_windowObj = $(new_windowObj[new_windowObj.length-1]);
		}
		var $blank = new_windowObj;//遮罩层对象
		$blank.on('show', function () {
        });
        $blank.on('hide', function () {
        	closeFunction();
            $(this).remove();
        });
		var $title = new_windowObj.find("h3");            //弹出层标题对象
		var $content = new_windowObj.find("."+urlclass);//弹出层内容对象
		var $close = new_windowObj.find("button");	//关闭层按钮对象
		$title.html(options.title);              //设置标题
		//设置url
		$content.html('<iframe  src="'+options.content+'" width="100%" ' +height+ ' scrolling="auto" frameborder="0"></iframe>');
		parentObj.windowHeight();
		//$blank.modal("show"); 
		$blank.modal('show').on('shown',function () {
	  		$('body > div.modal-backdrop:last').css('z-index','1050').insertBefore(this);
	  	})
		frameObj =new_windowObj.find("iframe")[0].contentWindow;
	};
	
	function getUrl(url){
		if(url!=null&&url!=""){
			var wh = url.indexOf('?');
			if(wh>-1){
				url = url.substring(0,wh);
			}
			return url
			
		}else{
			return url
		}
	}
	
	function closeFunction(){
		var parentObj = window.parent;//获得框架的主jsp对象
		var isframe = parentObj.$("#menuiframe");
		while (true){
			if(isframe.length==1){
				break;
			}else{
			   parentObj = parentObj.parent;
			   isframe = parentObj.$("#menuiframe");
			}
		}
		var new_windowObj = parentObj.$("[isopenwindow]");
		var  frameObj;
		if(new_windowObj.length>0){
			new_windowObj = $(new_windowObj[new_windowObj.length-1]);
			 frameObj =new_windowObj.find("iframe")[0].contentWindow;
		}
		var closeReturnValue = frameObj.closeNowCloseFunction();
		window.closeParentCloseFunction(closeReturnValue);
	}
	var data = "" ;
	$.fn.manhuaDialog.setData = function(dataJson){
		data = dataJson;
	};
	$.fn.manhuaDialog.getParentObj = function (){
		var parentObj = window.parent;//获得框架的主jsp对象
		return getParentWindowObj(parentObj);
	};
	$.fn.manhuaDialog.getFrameObj = function (){
		var parentObj = window.parent;//获得框架的主jsp对象
		var isframe = parentObj.$("#menuiframe");
		while (true){
			if(isframe.length==1){
				break;
			}else{
			   parentObj = parentObj.parent;
			   isframe = parentObj.$("#menuiframe");
			}
		}
		return parentObj;
	};
	$.fn.manhuaDialog.changeUrl = function (title,url){
		var parentObj = window.parent;//获得框架的主jsp对象
		var isframe = parentObj.$("#menuiframe");
		while (true){
			if(isframe.length==1){
				break;
			}else{
			   parentObj = parentObj.parent;
			   isframe = parentObj.$("#menuiframe");
			}
		}
//		var new_windowObj = parentObj.$("[isopenwindow]");
//		var  frameObj;
//		if(new_windowObj.length>0){
//			new_windowObj = $(new_windowObj[new_windowObj.length-1]);
//			var $title = new_windowObj.find("h3");            //弹出层标题对象
//			$title.html(title);
//			new_windowObj.find("iframe")[0].src = url
//		}	
		var newpath = window.location;
		newpath =getUrl(newpath.toString());
		var new_windowObj =getNowWindowObj(newpath,parentObj);
		if(new_windowObj.length>0){
			var $title = new_windowObj.find("h3"); 
			new_windowObj.find("iframe")[0].src = url;
		}

	}
	function getNowWindowObj(nowpath,parentObj){
		var isframe = parentObj.$("#menuiframe");
		while (true){
			if(isframe.length==1){
				break;
			}else{
			   parentObj = parentObj.parent;
			   isframe = parentObj.$("#menuiframe");
			}
		}
		var new_windowObjs = parentObj.$("[isopenwindow]");
		if(nowpath!=null){
			for(var i=0;i<new_windowObjs.length;i++){
				var new_windowObj = $(new_windowObjs[i]);
				var path = new_windowObj.find("iframe").attr("src");
				path = getUrl(path);
				if(nowpath.indexOf(path)>-1){
					return new_windowObj;
				}
			}
		}
		return null;
	}
	
	function getParentWindowObj(parentObj){
		//	var parentObj = obj.parent;//获得框架的主jsp对象
			var isframe = parentObj.$("#menuiframe");
			while (true){
				if(isframe.length==1){
					break;
				}else{
				   parentObj = parentObj.parent;
				   isframe = parentObj.$("#menuiframe");
				}
			}
			var path = "";
			var p_windowPath = null;
			var new_windowObjs = parentObj.$("[isopenwindow]");
			var newpath = window.location;
			newpath =getUrl(newpath.toString());
			if(new_windowObjs.length>1){
				for(var i=0;i<new_windowObjs.length;i++){
					var new_windowObj = $(new_windowObjs[i]);
					//var path = new_windowObj.$("[p_location]");
					var path = new_windowObj.find("iframe").attr("src");
					if(newpath.indexOf(getUrl(path))>-1){
						p_windowPath = new_windowObj.attr("p_location");
						p_windowPath = getUrl(p_windowPath);
						break;
					}
				}
			 }
			if(p_windowPath!=null){
				for(var i=0;i<new_windowObjs.length;i++){
					var new_windowObj = $(new_windowObjs[i]);
					var path = new_windowObj.find("iframe").attr("src");
					if(p_windowPath.indexOf(getUrl(path))>-1){
						return new_windowObj.find("iframe")[0].contentWindow;
					}
				}
			}
			return parentObj.$("#menuiframe")[0].contentWindow;
		}
	function getParentWindowObj_bak(parentObj){
	//	var parentObj = obj.parent;//获得框架的主jsp对象
		var isframe = parentObj.$("#menuiframe");
		while (true){
			if(isframe.length==1){
				break;
			}else{
			   parentObj = parentObj.parent;
			   isframe = parentObj.$("#menuiframe");
			}
		}
		var new_windowObj = parentObj.$("[isopenwindow]");
		if(new_windowObj.length>1){
			new_windowObj = $(new_windowObj[new_windowObj.length-2]);
			var frameObj =new_windowObj.find("iframe")[0].contentWindow;
			return frameObj
		}else{
			return parentObj.frames["menuiframe"];
		}
	}
	
	
	$.fn.manhuaDialog.sendData = function (){
		var parentObj = window.parent;//获得框架的主jsp对象
		var isframe = parentObj.$("#menuiframe");
		while (true){
			if(isframe.length==1){
				break;
			}else{
			   parentObj = parentObj.parent;
			   isframe = parentObj.$("#menuiframe");
			}
		}
		parentObj =  getParentWindowObj(parentObj);
		parentObj.getWinData(data);
	};
	$.fn.manhuaDialog.close = function(){
		var parentObj = window.parent;//获得框架的主jsp对象
		var isframe = parentObj.$("#menuiframe");
		while (true){
			if(isframe.length==1){
				break;
			}else{
			   parentObj = parentObj.parent;
			   isframe = parentObj.$("#menuiframe");
			}
		}
		var newpath = window.location;
		newpath =getUrl(newpath.toString());
		var new_windowObj =getNowWindowObj(newpath,parentObj);
		if(new_windowObj.length>0){
			new_windowObj.find("button").click();
		}
	};
	
});