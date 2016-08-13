
$(document).ready(function(){
	setStyle($('form'));
	setTablePage();
	setAlertHTML();
	//setTableOrder();//表格排序功能，暂时屏蔽
});

function setTableOrder(){
	var $table_advanced_th = $('table.table-activeTd').find('thead th');
	for(var i=0;i<$table_advanced_th.length;i++){
	    var $field_th = $table_advanced_th.eq(i);
	    var fieldname = $field_th.attr("fieldname");
	    if(fieldname&&fieldname!=""){
	    	$field_th.addClass('sorting');
			//$('table.table_advanced').find('thead th:first').removeClass('sorting').addClass('sorting_asc');
			$field_th.click(function(e) {
				if ($(this).hasClass('sorting')) {
					$(this).removeClass('sorting');
					$(this).addClass('sorting_asc');
					$(this).siblings().removeClass().addClass('sorting');
				}
				else if (($(this).hasClass('sorting_asc'))) {
					$(this).removeClass('sorting_asc');
					$(this).addClass('sorting_desc');
				}
				else {
					$(this).removeClass('sorting_desc');
					$(this).addClass('sorting_asc');
				}
				var $tab = $($(this)[0].parentElement.parentElement.parentElement);
				var class_name = $(this).attr("class");
				if(class_name=="sorting_asc"){
					$tab.sortTabListRowAsc($(this).attr("fieldname"));
				}else{
					$tab.sortTabListRowDesc($(this).attr("fieldname"));
				}
		    });
	    }
	}
}

function setAlertHTML()
{
	var alertHTML  = "<div id=\"pubAlert\" class=\"alert alert-success success-b-open\">";
	    alertHTML += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\"></button>";
        alertHTML += "<h4></h4>";
        alertHTML += "<span class=\"alertContent\"></span >";
        alertHTML += "</div>";
    $("body").prepend(alertHTML);
}

function setTablePage()
{

	$("table").each(function(index,element){
		 var tl = $(this);
		 var tableid = tl.attr("id");
		 if(tl.attr("class")=="table-hover table-activeTd B-table"||tl.attr("class")=="table-hover table-activeTd B-table LockingTableRoot"||tl.attr("class")=="table-hover table-activeTd B-table table_advanced")
		 {
		 var pageHTML = "<div class=\"pagination pagination-right\" style=\"display:none\" id=\"page_"+tableid+"\" tableid=\""+tableid+"\">";
			 pageHTML +="<form class=\"form-inline\">";
			 pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"首页\" ><i class=\"icon-fast-backward\"></i></button>";
			 pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"上一页\"><i class=\"icon-step-backward\"></i></button>";
			 pageHTML +="<a href=\"#\" class=\"btn disabled\">[0/0]</a>";
			 pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"下一页\"><i class=\"icon-step-forward\"></i></button>";
			 pageHTML +="<button type=\"button\" disabled class=\"btn btn-mini btn-link\" title=\"末页\"><i class=\"icon-fast-forward\"></i></button>";
             pageHTML +="转到：";
             pageHTML +="<input type=\"text\" class=\"span1\" placeholder=\"\">";
             pageHTML +="<button type=\"button\" class=\"btn btn-mini\">GO</button>";
             pageHTML +="<span> 条/页　共     条记录</span>";
             pageHTML +="</form>";
			 pageHTML +="</div>";
			 var parent_div = tl[0].parentElement;
			 if(parent_div.tagName =="DIV")
			 {
				 $(parent_div).after(pageHTML);
			 }	 
			 
			//alert( tl[0].parentElement.tagName);
			// tl.after(pageHTML);
		 }
		  
	});
	

}
//----------------------------------------------------
//-add by zhangbr@ccthanking.com
//-查询无结果时，添加div
//-@param	str	放数据的table的ID字符串
//----------------------------------------------------
function setTableNoResultImg(str){
	if($("#"+str).attr("noPromptMsg")=="true"){
		//有的表格不需要“查询无结果”的图片
	}else{
		$("table").each(function(index,element){
			var tl = $(this);
			var tableid = tl.attr("id");
			if(tableid==str &&( tl.attr("class")=="table-hover table-activeTd B-table"||tl.attr("class")=="table-hover table-activeTd B-table LockingTableRoot"||tl.attr("class")=="table-hover table-activeTd B-table table_advanced")){
				var pageHTML = "<div class='pagination pagination-right' id='page_"+tableid+"' tableid='"+tableid+"'>";
				pageHTML +="<form class=\"form-inline\" style='width:100%;text-align:center'>";
				pageHTML +='<img class="noresult" style="width:50px;" src="'+g_sAppName+'/images/noresult.png"/>&nbsp;&nbsp;没有找到记录，请尝试更换查询条件。';
				pageHTML +="</form>";
				pageHTML +="</div>";
				$("#page_"+tableid).remove();//删除旧图片
				var parent_div = tl[0].parentElement;
				if(parent_div.tagName =="DIV"){
					$(parent_div).after(pageHTML);
				}	 
			}
		});
	}
}
function loadXmlFile(xmlFile)//xmlFile 是xml文件的地址
{   

    var xmlDom = null;
    if (window.ActiveXObject)//IE浏览器中读取xml文件
    {
        xmlDom = new ActiveXObject("Microsoft.XMLDOM");
        xmlDom.async="false";
        xmlDom.load(xmlFile);

    }
    else if(document.implementation && document.implementation.createDocument)
    {

        //Firefox，Chrome 浏览器中读取xml文件
        var xmlhttp = new window.XMLHttpRequest();
        xmlhttp.open("GET", xmlFile, false);
        xmlhttp.send(null);
        xmlDom = xmlhttp.responseXML;
    }
    else
    {
        xmlDom = null;
    }
    return xmlDom;
}



function setStyle(sObj)
{
    var sId= "",$d = null;
    if(sObj && typeof(sObj) == "object")
    {   
        $d = sObj;
    }
    else if(sObj)
        $d = $("#"+sObj);

    
    if($d)
    {

        $("input,select,textarea",$d).each(function(index,element){
            var $this = $(this);
            if($this.val())
                $this.attr("title",$this.val());
            var muss = $this.attr("must");
            var must_s = false;
            $this.bind("dblclick",function(event){
            	//alert('dbclick');
            });
            
            $this.bind("blur",function(event){
//            	alert('blur')
            });
            $this.bind("propertychange","input",function(event){
            	//alert('change'); 
             });
            if(muss)
            {
                    var jSpan = $("<span style='font-size:12px;color:red;width:10px; padding-left:2px;height:20px'>*</span>");
                    $this.after(jSpan);
            }
            if(($this.attr("type")=="radio"||$this.attr("type")=="checkbox")&&$this.attr("kind") && $this.attr("kind")=="dic"){
            	if ($this.attr("src").substr(0,2).toUpperCase() == "T#"){
            		var vparam = $this.attr("src").substr(2); //去掉T#
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
                    			var dic_dom = parser.parseFromString(response, "text/xml"); 
                            	if(dic_dom){
                                 var list = dic_dom.getElementsByTagName("R");
                                 var temp ="";
                                 for(var i = 0;i<list.length;i++){
                                 	 var code = list[i].getAttribute("c");
                                      var value = list[i].getAttribute("t");
              		    		  	  var onclickHTML = "";
                                      if($this.attr("onclick")){
              		    				onclickHTML = "onclick=\""+$this.attr("onclick")+"\"";
              		    			  }
                                      var lineType = $this.attr("lineType");
                                      if(lineType!=undefined&&lineType!=null&&lineType=="upline"){
                                      	  lineType = "";
                                        }else{
                                      	  lineType = " inline";
                                        }
                                      
                                      if($this.attr("type")=="radio"){
                                        temp +=("<label class=\"radio"+lineType+"\"><input type=\"radio\" "+onclickHTML+" name=\""+$this.attr("name")+"\" value='"+code+"' sv='"+value+"'>"+value+"</label>");
                                      }else{
                                    	temp +=("<label class=\"checkbox"+lineType+"\"><input type=\"checkbox\" "+onclickHTML+" name=\""+$this.attr("name")+"\" value='"+code+"'  sv='"+value+"'>"+value+"</label>");  
                                      }
                                 }
                                 $this.after(temp);
                                 $this.hide(); 
                                }

                    		}
                    	});
	
            		
            	}else if ($this.attr("src").substr(0,2).toUpperCase() == "E#"){
            		var vparam = $this.attr("src").substr(2);
    		    	var list = vparam.split(':');
		    		 var temp = "";
    		    	if(list.length>0){
    		    	 for(var i=0;i<list.length;i++)
    		    	 {
    		    		var unit = list[i].split('=');
    		    		if(unit.length == 2)
    		    		{
    		    			var onclickHTML = "";
    		    			if($this.attr("onclick")){
    		    				onclickHTML = "onclick=\""+$this.attr("onclick")+"\"";
    		    			}
    		    			var lineType = $this.attr("lineType");
                            if(lineType!=undefined&&lineType!=null&&lineType=="upline"){
                          	  lineType = "";
                            }else{
                          	  lineType = " inline";
                            }
    		    			if($this.attr("type")=="radio"){
                                temp +=("<label class=\"radio"+lineType+"\"><input  type=\"radio\" "+onclickHTML+" name=\""+$this.attr("name")+"\" value='"+unit[0]+"'  sv='"+unit[1]+"'>"+unit[1]+"</label>");
                             }else{
                            	temp +=("<label class=\"checkbox"+lineType+"\"><input type=\"checkbox\"  "+onclickHTML+" name=\""+$this.attr("name")+"\" value='"+unit[0]+"'  sv='"+unit[1]+"'>"+unit[1]+"</label>");  
                              }
    		    		}
    		    		else continue;
    		    	 }
    		    	  $this.after(temp);
                      $this.hide(); 
            	    }
            	}else{
            		 var dic_dom = loadXmlFile(g_sDicPath + $this.attr("src") + ".xml");
                     if(dic_dom){
                    	 if(typeof($this.attr("defaultMemo"))!="undefined"){
                    		 $this.append("<option value=''>"+$this.attr("defaultMemo")+"</option>");
                    	 }else{
                    		 $this.append("<option value=''>请选择</option>");
                    	 }
                      var list = dic_dom.getElementsByTagName("R");
                      var temp = "";
                      for(var i = 0;i<list.length;i++){
                      	 var code = list[i].getAttribute("c");
                           var value = list[i].getAttribute("t");
   		    		  	  var onclickHTML = "";
                          if($this.attr("onclick")){
  		    				onclickHTML = "onclick=\""+$this.attr("onclick")+"\"";
  		    			  }
                          var lineType = $this.attr("lineType");
                          if(lineType!=undefined&&lineType!=null&&lineType=="upline"){
                          	  lineType = "";
                          }else{
                          	  lineType = " inline";
                          }
                           if($this.attr("type")=="radio"){
                               temp +=("<label class=\"radio"+lineType+"\"><input type=\"radio\"  "+onclickHTML+" name=\""+$this.attr("name")+"\" value='"+code+"'  sv='"+value+"'>"+value+"</label>");
                           }else{
                               temp +=("<label class=\"checkbox"+lineType+"\"><input type=\"checkbox\"  "+onclickHTML+" name=\""+$this.attr("name")+"\" value='"+code+"'  sv='"+value+"'>"+value+"</label>");  
                           }
                      }
                      $this.after(temp);
                      $this.hide(); 
                 }
            	
               
            	
             }
            	//处理默认值
            	if($this.attr("defaultValue")){
            		var defaultValue = $this.attr("defaultValue");
            		var s = defaultValue.split(",");
            		if(s&&s.length>0){
            			for(var i =0;i<s.length;i++ )
            			{
            				var t = $("input[name="+$this.attr("name")+"][value="+s[i]+"]");
            				if(t.length>0)
            					t[0].checked=true;
            			}
            		}
            	}
            }
            
            
            //设置下拉字典
            if($this.get(0).tagName=="SELECT"&&$this.attr("kind") && $this.attr("kind")=="dic")
            {
                var defaultMemo = "请选择";
                var noNullSelect = false;
                if($this.attr("defaultMemo")&&$this.attr("defaultMemo")!="")
                {
                	defaultMemo = $this.attr("defaultMemo");
                }
                if($this.attr("noNullSelect")&&$this.attr("noNullSelect")=="true")
                {
                	noNullSelect = true;
                }

            	
            	if ($this.attr("src").substr(0,2).toUpperCase() == "T#"){
            		var vparam = $this.attr("src").substr(2); //去掉T#
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
                    			var dic_dom = parser.parseFromString(response, "text/xml"); 
                            	if(dic_dom){
                            	   if(noNullSelect == false)
                             	   $this.append("<option value=''>"+defaultMemo+"</option>");
                                 var list = dic_dom.getElementsByTagName("R");
                                 for(var i = 0;i<list.length;i++){
                                 	 var code = list[i].getAttribute("c");
                                      var value = list[i].getAttribute("t");
                                      $this.append("<option value='"+code+"'>"+value+"</option>");
                                 }
                                }

                    		}
                    	});
	
            		
            	}else if ($this.attr("src").substr(0,2).toUpperCase() == "E#"){
            		var vparam = $this.attr("src").substr(2);
    		    	var list = vparam.split(':');
    		    	if(list.length>0){
    		    	   if(noNullSelect == false)
    		    	   $this.append("<option value=''>"+defaultMemo+"</option>");
    		    	 for(var i=0;i<list.length;i++)
    		    	 {
    		    		var unit = list[i].split('=');
    		    		if(unit.length == 2)
    		    		{
    		    			$this.append("<option value='"+unit[0]+"'>"+unit[1]+"</option>");
    		    		}
    		    		else continue;
    		    	 }
            	    }
            	}else{
            	
                   var dic_dom = loadXmlFile(g_sDicPath + $this.attr("src") + ".xml");
                   if(dic_dom){
                	   if(noNullSelect == false)
                	   $this.append("<option value=''>"+defaultMemo+"</option>");
                    var list = dic_dom.getElementsByTagName("R");
                    for(var i = 0;i<list.length;i++){
                    	 var code = list[i].getAttribute("c");
                         var value = list[i].getAttribute("t");
                         $this.append("<option value='"+code+"'>"+value+"</option>");
                    }
                   }
                 }
            	//处理默认值
            	if($this.attr("defaultValue")){
            		var defaultValue = $this.attr("defaultValue");
            		var t =0;
            		$this.find("option").each(function() {
						
						  if(this.value == defaultValue)
						  {
							  return false;
						  } 
						  t++;
						});
            		if(t>0)
            			$this.get(0).selectedIndex=t;  
            	}
            }else{
                $this.bind("focus",function(event){
                    if($this.attr("id") == "dicFilterEdit")
                        return;
                    if(!$this.attr("kind") || $this.attr("kind") != "dic")
                        ;//g_xDic.hidden();        
                    });
            }
            //set BtnStyle
            if($this.attr("hasBtn") && $this.attr("hasBtn") == "true")
            {
                var $div = $("<img></img>");
                $div.attr("src", g_webAppImagePath+"/default/search.png");
                $div.width(15);
                $div.height(15);
                $div.attr("title","点击按钮");
                var $span = $("<span></span>");
                $span.append($div);
                if($this.attr("callFunction"))
                {
                    $span.bind("click",function(){
                        eval($this.attr("callFunction"));
                    });
                }
                $this.after($span);
            }
        });
        
        $d.validation();//设置表单验证监听
       // addListener(sId);
    }else
    {
        $("input,select,textarea").each(function(index,element){
            var $this = $(this);
            if($this.val())
                $this.attr("title",$this.val());
            var dataType = $this.attr("datatype");
            var must_s = false;
            $this.bind("dblclick",function(event){
               if($this.is(":hidden")) return true;
               if($this.attr("kind")) return true;
               if($this.get(0).tagName=="SELECT") return true;
               if($this.hasClass("Wdate")) return true;
               expand_textarea($this); 
            });
            if(dataType)
            {
                var arr = dataType.split(",");
                if(arr && arr.length > 0)
                {
                    if(arr[0] == 0)
                        must_s = true;
                    //设置input框的maxlength属性
                    if(arr[2])
                    {
                        var maxLen = arr[2];
                        $this.attr("maxlength",maxLen);
                    }
                }
                //设置input输入框must=true样式
                if(must_s == true)
                {
                    var jSpan = $("<span style='font-size:12pt;color:red;width:10px; padding-left:2px;height:18px'>*</span>");
                    $this.after(jSpan);
                }
            }
            //设置字典
            if($this.attr("kind") && $this.attr("kind")=="dic")
            {
                $this.bind("focus",{},function(event){
                    g_bCheckChanged = true;
                    if(!g_xDic)
                    {
                    	g_xDic = new new_xDic();
                    	g_xDic.object.size();
                    }
                    g_xDic.show($this,$this.attr("code"),$this.val());
                });
                if($this.get(0).tagName=="SELECT")
                {
                    $this.combox();
                } 
                else
                    $this.bind("keyup",function(){
                        doPropChange($(this));
                    });
            }else{
                $this.bind("focus",function(event){
                    if($this.attr("id") == "dicFilterEdit")
                        return;
                    if(!$this.attr("kind") || $this.attr("kind") != "dic")
                        g_xDic.hidden();                    
                });
            }
            //set BtnStyle
            if($this.attr("hasBtn") && $this.attr("hasBtn") == "true")
            {
                var $div = $("<img></img>");
                $div.attr("src", g_webAppImagePath+"/default/search.png");
                $div.width(15);
                $div.height(15);
                $div.attr("title","点击按钮");
                var $span = $("<span></span>");
                $span.append($div);
                if($this.attr("callFunction"))
                {
                    $span.bind("click",function(){
                        eval($this.attr("callFunction"));
                    });
                }
                $this.after($span);
            }            
        });
    }
}

//根据表格行json赋值表单（暂时）
function setValueByArr(obj, targetArr) {
	// alert(JSON.stringify(obj));
	if (!obj || !targetArr) {
		return false;
	}
	for ( var i = 0; i < targetArr.length; i++) {
		var tmp = document.getElementById(targetArr[i]);
		if (tmp.tagName == 'SELECT') {// 字典
			for ( var s = 0; s < tmp.options.length; s++) {
				if (tmp.options[s].value == eval('obj.' + targetArr[i])) {
					tmp.options[s].selected = true;
				}
			}
		} else if (tmp.tagName == 'INPUT') {// 普通
			if (tmp.type == 'text' || tmp.type == 'hidden') {
				tmp.value = eval('obj.' + targetArr[i]);
			} else if (tmp.type == 'date') {// 日期
				tmp.value = eval('obj.' + targetArr[i] + '_SV');
			}
		} else if (tmp.tagName == 'TEXTAREA') {
			tmp.value = eval('obj.' + targetArr[i]);
		}
	}
}
