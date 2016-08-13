/**
     Title:系统框架空工程
     Version:1.0
     Remark：You can copy and/or use and/or modify this program free,
             but please reserve the segment above.
             Please mail me if you have any question.
*/
(function($) {
	$.fn.extend({
		
	insertResult:function(jsonStr,tablistID,index){
		
		//if(index < 0 || index >= $("tr",$(this)).size()) index = 1;
		//alert("jsonStr:"+jsonStr);
		//将json 串转为json对象
		var $thistab = $("#"+tablistID.id);
        var rows = $("tbody tr" ,$thistab);
        var tabthrows = $("thead tr" ,$thistab);
		var jsonObj;
		jsonStr = dealInputSpecialCharactor(jsonStr);
		jsonObj = convertJson.string2json1(jsonStr);
		//add by zhangbr@ccthanking.com 清空查询为空的小图标
		var dataLength = $("#page_"+tablistID.id+" form").find("img.noresult").length;
		if(dataLength==1){
			$("#page_"+tablistID.id).find("form").remove();//删除旧图片
		}
		
		//定义定长数组
		//var tableObject = $(this.id);//获取id为#DT1的table对象 
		var tableObject =  $("#"+tablistID.id);
		var tbHead = tableObject.children('thead');//获取table对象下的thead 
		var tbHeadTh = tbHead.find('tr th');//获取thead下的tr下的th 
		var showHTML ="<tr rowJson = "+JSON.stringify(jsonObj)+" isSelect = 0 >";
		//
		  var throwsize = tabthrows.size();//表头行尺寸
			//获取所有fieldname的记录数
	        var k = 0;
	        var tharrays= new Array();
			if(throwsize>=2){//多表头处理

				for(var t =0;t<tbHeadTh.size();t++)
					{
						
							if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
								{
									//tharrays[k] = tbHeadTh.eq(t);
									k++;
								}
						
					}
			if(k>0){
				for(var arr=1;arr<=k;arr++)
				{
					for(var t =0;t<tbHeadTh.size();t++)
					{
						if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
							{
								if(tbHeadTh.eq(t).attr("colindex") == arr){
									tharrays[arr-1] = tbHeadTh.eq(t);
									break;
								}
							}
					}
				}
				}
				//
			}
		//
		if(throwsize>=2){//多表头处理
			for(var i =0 ;i<tharrays.length;i++){
				//获取th属性值
				var val = tharrays[i].attr("fieldname");
				//
				var maxlength = tharrays[i].attr("maxlength");
				var colspan = tharrays[i].attr("colspan");//add by songxb@2013-08-07
				//文本位置和链接参数
				var align = tharrays[i].attr("tdalign");
				var hasLink = tharrays[i].attr("hasLink");
			     var linkfunction = tharrays[i].attr("linkFunction");
			     if(linkfunction!=undefined&&hasLink=="true")
			     {
			       hasLink = true;
			     }else{
			       hasLink = false;
			     }
				var strlength ;
			     //自定义函数返回值
				 var CustomFunction = tharrays[i].attr("CustomFunction");
				 var returnFunctionValue="";
				 if(CustomFunction!=undefined)
				 {
					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(jsonObj)+")");
				 }
				 //是否相同内容列合并
				 var rowMerge =tharrays[i].attr("rowMerge");
				 if(rowMerge!=undefined&&rowMerge=="true")
			     {
					 rowMerge = true;
			     }else{
			    	 rowMerge = false;
			     }
				 //如果相同内容合并，判断上一行内容是否和当前行一致如果一致则不生成td标签，
				 //判断下一行内容是否和当前行一致，如果一致增加rowspan属性，并计算下面几行相同，得到rowspan值
				   var rowSpan_ = "";

				 if( rowMerge ==true){
				 //判断上一行内容，begin
				  var tab = $("#"+tablistID.id);	
				 // alert(tab)
				  var s= tab.getTableRows();
				   if(s>0){
					   var t = 0;
					   for ( var ss = 0; ss < s; ss++) {
						     var preRow = tab.getRowJsonObjByIndex(ss);
					    	 if(preRow[val]==jsonObj[val]){
					    		 t = t+1;
					    	 }else{
					    		 break;
					    	 }
					   } 
					   if(t>0){
						   t = t+1;
						   rowSpan_ = " rowspan =\""+t+"\""; 
						   //清除下一行的td标签
						   var next_tr = tab.getTrObjByIndex(0);
                           var c = tharrays.length-$(next_tr).find("td").length-1;
                           var ss = i-1-c;
                           if(ss<0){
                        	   ss=0;
                           }
						   $(next_tr).find("td").eq(ss).remove();
						   //如果有列合并，显示长度为最大程度*合并多少列
    					   if(maxlength!= undefined){
    					     // maxlength = maxlength*t;
    					   }
					   }					
				   }
				 }
				 //判断上一行内容，end
				 //计算rowspan end
				//
				if(val != undefined){
				//
	   				var value_SV = jsonObj[val+"_SV"];
					if(value_SV!= undefined)
					{
						value_SV = dealSpecialCharactor(value_SV);
							strlength = value_SV.length;
							if(maxlength!= undefined)
							{
								if(align != undefined){//位置处理
									//链接处理
									if(hasLink){//有链接
										if(strlength > maxlength){//超过长度隐藏显示
											showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+value_SV+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+rows.size()+"')\"> "+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
										}else{
											showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV +"</a></td>";
										}
									}else{//无链接
										if(strlength > maxlength){//超过长度隐藏显示
											showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
											  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
											}else{
											  showHTML +="<td align="+align+" "+rowSpan_+">"+value_SV +"</td>";
											}
										}
									}
								}else{
									if(hasLink){//有链接
										if(strlength > maxlength){//超过长度隐藏显示
											showHTML +="<td "+rowSpan_+"><abbr title=\""+value_SV+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\"> "+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
										}else{
											showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+rows.size()+"')\">"+value_SV +"</a></td>";
										}
									}else{
										if(strlength > maxlength){//超过长度隐藏显示
											showHTML +="<td "+rowSpan_+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
											}else{
												  showHTML +="<td "+rowSpan_+">"+value_SV +"</td>";
											}
										}
									}
								}
							}else{
								if(align != undefined){//位置处理
									if(hasLink){//有链接
										showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV +"</a></td>";
									}else{
										if(returnFunctionValue&&returnFunctionValue!=""){
											  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
										}else{
											  var temp  = dealSpecialCharactor(value_SV);
											  //showHTML +="<td align="+align+" "+rowSpan_+">"+value_SV +"</td>";//modify by zhangbr@ccthanking.com
											  showHTML +="<td align="+align+" "+rowSpan_+">"+temp +"</td>";
										}
									}
									
								}else{
									if(hasLink){//有链接
										showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV +"</a></td>";
									}else{
										if(returnFunctionValue&&returnFunctionValue!=""){
											  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
										}else{
											  var temp  = dealSpecialCharactor(value_SV);
											  //showHTML +="<td "+rowSpan_+">"+value_SV +"</td>";//modify by zhangbr@ccthanking.com;
											  showHTML +="<td "+rowSpan_+">"+temp +"</td>";
										}
									}
									
								}

							}
							//
							
					}else{//无SV处理
							var jsonval =  jsonObj[val];
							if(jsonval==undefined)
							{
								showHTML +="<td></td>";
							}else
							{
								jsonval = dealSpecialCharactor(jsonval);
							strlength = jsonval.length;
							//modfied by songxb@2013-08-07 begin
							if(maxlength!= undefined && strlength > maxlength)//定义了maxlength
							{
								if(colspan!=undefined && colspan>1)//合并列
								{
									var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
									
									if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
									{
										showHTML +="<td colspan=\""+colspan+"\" "+rowSpan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
									}else
									{
										for(var z=0;z<colspan;z++)
										{
											
											if(temp[z].length > maxlength)
											{
												showHTML +="<td "+rowSpan_+"><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
											}else
											{
												showHTML +="<td "+rowSpan_+">"+temp[z] +"</td>";
											}
										}
										
									}

								}else{//非合并列
									if(align != undefined){//位置处理
										if(hasLink){//有链接
											showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+jsonval+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
										}else{
											showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
										}
									}else{
										if(hasLink){//有链接
											showHTML +="<td  "+rowSpan_+"><abbr title=\""+jsonval+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
										}else{
											showHTML +="<td  "+rowSpan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
										}
									}
								}
							}else{//未定义maxlength
										    									
								if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
								{
									var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
									//alert(temp);
									//alert(temp.length);
									if(temp.length>0 && temp.length!=colspan)
									{
										showHTML +="<td colspan=\""+colspan+"\" "+rowSpan_+">"+jsonval+"</td>";
									}else
									{
										for(var z=0;z<colspan;z++)
										{
											showHTML +="<td "+rowSpan_+">"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
										}
										
									}
									//modified by songxb@2013-08-07 end
								}else{
									if(align != undefined){//位置处理
										if(hasLink){//有链接
											showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+ jsonval +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												showHTML +="<td align="+align+" "+rowSpan_+">"+ returnFunctionValue +"</td>";	
											}else{
											  var temp  = dealSpecialCharactor(jsonval);
											  //showHTML +="<td align="+align+" "+rowSpan_+">"+ jsonval +"</td>";//modify by zhangbr@ccthanking.com;
											  showHTML +="<td align="+align+" "+rowSpan_+">"+ temp +"</td>";
											}
										}
									}else{
										if(hasLink){//有链接
											showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+ jsonval +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												showHTML +="<td "+rowSpan_+">"+ returnFunctionValue +"</td>";	
											}else{
												var temp  = dealSpecialCharactor(jsonval);
												//showHTML +="<td "+rowSpan_+">"+ jsonval +"</td>";//modify by zhangbr@ccthanking.com;
												showHTML +="<td "+rowSpan_+">"+ temp +"</td>";
											}
											
										}
										
									}

								}
			    			}
							}
						}
					}else{
							//序号处理
		    				var Strxh = tharrays[i].attr("id"); 
		    				if(Strxh != undefined && Strxh == "_XH"){
		    					showHTML+="<th align=center><strong>"+parseInt(rows.size()+1)+"</strong></th>";
					}
				}
			}
		}else{//单表头处理
			tbHeadTh.each(function(i) {//遍历thead的tr下的th 
				//获取th属性值
				var val = $(this).attr("fieldname");
				//
				var maxlength = $(this).attr("maxlength");    
				var colspan = $(this).attr("colspan");//add by songxb@2013-08-07
				var strlength ;
				//文本位置和链接参数
				var align = $(this).attr("tdalign");
				var hasLink = $(this).attr("hasLink");
			     var linkfunction = $(this).attr("linkFunction");
			     if(linkfunction!=undefined&&hasLink=="true")
			     {
			       hasLink = true;
			     }else{
			       hasLink = false;
			     }
			     //自定义函数返回值
				 var CustomFunction = $(this).attr("CustomFunction");
				 var returnFunctionValue="";
				 if(CustomFunction!=undefined)
				 {
					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(jsonObj)+")");
				 }
				 
				 //是否相同内容列合并
				 var rowMerge =$(this).attr("rowMerge");
				 if(rowMerge!=undefined&&rowMerge=="true")
			     {
					 rowMerge = true;
			     }else{
			    	 rowMerge = false;
			     }
				 //如果相同内容合并，判断上一行内容是否和当前行一致如果一致则不生成td标签，
				 //判断下一行内容是否和当前行一致，如果一致增加rowspan属性，并计算下面几行相同，得到rowspan值
				   var rowSpan_ = "";

				 if( rowMerge ==true){
				 //判断上一行内容，begin
				  var tab = $("#"+tablistID.id);	
				 // alert(tab)
				  var s= tab.getTableRows();
				   if(s>0){
					   var t = 0;
					   for ( var ss = 0; ss < s; ss++) {
						     var preRow = tab.getRowJsonObjByIndex(ss);
					    	 if(preRow[val]==jsonObj[val]){
					    		 t = t+1;
					    	 }else{
					    		 break;
					    	 }
					   }  
					   if(t>0){
						   t = t+1;
						   rowSpan_ = " rowspan =\""+t+"\""; 
						   //清除下一行的td标签
						   var next_tr = tab.getTrObjByIndex(0);

						  //  alert(i);
						   var c = tbHeadTh.length-$(next_tr).find("td").length-1;
                           var ss = i-1-c;
                           if(ss<0){
                        	   ss=0;
                           }
						   $(next_tr).find("td").eq(ss).remove();
						   //如果有列合并，显示长度为最大程度*合并多少列
    					   if(maxlength!= undefined){
    					   //   maxlength = maxlength*t;
    					   }
					   }					
				   }
				 }
				 //判断上一行内容，end
				 //计算rowspan end
			     
				if(val != undefined){
				//
	   				var value_SV = jsonObj[val+"_SV"];
					if(value_SV!= undefined)
						{
						value_SV = dealSpecialCharactor(value_SV);

							strlength = value_SV.length;
							if(maxlength!= undefined)
							{
								if(strlength > maxlength){//超过长度隐藏显示
									if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+value_SV+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td "+rowSpan_+"><abbr title=\""+value_SV+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td "+rowSpan_+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}	
										}
									
								}else{
									if(align != undefined){//位置处理
										if(hasLink){//有链接
											showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV +"</a></td>";
										}else{
											showHTML +="<td align="+align+" "+rowSpan_+">"+value_SV +"</td>";
										}
										}else{
											if(hasLink){//有链接
												showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV +"</a></td>";
											}else{
												showHTML +="<td "+rowSpan_+">"+value_SV +"</td>";
											}
										}
								}
							}else{
								if(align != undefined){//位置处理
									if(hasLink){//有链接
										showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV +"</a></td>";
									}else{
										if(returnFunctionValue&&returnFunctionValue!=""){
											  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
										}else{
											 var temp  = dealSpecialCharactor(value_SV);
											  //showHTML +="<td align="+align+" "+rowSpan_+">"+value_SV +"</td>";//modify by zhangbr@ccthanking.com
											  showHTML +="<td align="+align+" "+rowSpan_+">"+temp +"</td>";
											  //showHTML +="<td align="+align+" "+rowSpan_+">"+value_SV +"</td>";
										}
									}
								}else{
									if(hasLink){//有链接
										showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+value_SV +"</a></td>";
									}else{
										if(returnFunctionValue&&returnFunctionValue!=""){
											  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
										}else{
											var temp  = dealSpecialCharactor(value_SV);
											  //showHTML +="<td align="+align+" "+rowSpan_+">"+value_SV +"</td>";//modify by zhangbr@ccthanking.com
											showHTML +="<td "+rowSpan_+">"+temp +"</td>";
											  //showHTML +="<td "+rowSpan_+">"+value_SV +"</td>";
										}
									}
								}
							}
							//
							
						}else{
							
							var jsonval =  jsonObj[val];
							if(jsonval==undefined)
							{
								showHTML +="<td></td>";
							}else
							{
								jsonval = dealSpecialCharactor(jsonval);

							strlength = jsonval.length;
							//modfied by songxb@2013-08-07 begin
							if(maxlength!= undefined && strlength > maxlength)
							{
								if(colspan!=undefined && colspan>1)//合并列不处理
								{
									var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
									
									if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
									{
										showHTML +="<td colspan=\""+colspan+"\" "+rowSpan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
									}else
									{
										for(var z=0;z<colspan;z++)
										{
											
											if(temp[z].length > maxlength)
											{
												showHTML +="<td "+rowSpan_+"><abbr title=\""+temp[z]+"\" > "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
											}else
											{
												showHTML +="<td "+rowSpan_+">"+temp[z] +"</td>";
											}
										}
										
									}

								}else{
									if(align != undefined){//位置处理
										if(hasLink){//有链接
											showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+jsonval+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\"> "+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
										}else{
											showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
										}
									}else{
										if(hasLink){//有链接
											showHTML +="<td  "+rowSpan_+"><abbr title=\""+jsonval+"\" "+rowSpan_+"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
										}else{
											showHTML +="<td   "+rowSpan_+"><abbr title=\""+jsonval+"\" "+rowSpan_+"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
										}
									}
								}

							}else{
										    									
								if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
								{
									var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串

									if(temp.length>0 && temp.length!=colspan)
									{
										showHTML +="<td colspan=\""+colspan+"\" "+rowSpan_+">"+jsonval+"</td>";
									}else
									{
										for(var z=0;z<colspan;z++)
										{
											showHTML +="<td "+rowSpan_+">"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
										}
									}

								}else{
									if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+ jsonval +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td align="+align+" "+rowSpan_+">"+jsonval +"</td>";
												}
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('0')\">"+ jsonval +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td "+rowSpan_+">"+jsonval +"</td>";
												}
											}
										}
									
								}
								//modified by songxb@2013-08-07 end
			    			}
						}
						}
					}else{
							//序号处理
		    				var Strxh = $(this).attr("id"); 
		    				if(Strxh != undefined && Strxh == "_XH"){
		    					showHTML+="<th align=center><strong>"+parseInt(rows.size()+1)+"</strong></th>";
						    }
					}
			});
		}
		showHTML  +="</tr>";
		//alert(showHTML);
		if(index==1){
		 $("#"+tablistID.id).find("tbody").prepend(showHTML);
		}else{
			var trObj =  $("#"+tablistID.id).getTrObjByIndex(index-2);
			trObj.after(showHTML);
		}
		$("#"+tablistID.id).TableTdXhOrder();
		//
		bindClickEvent(tablistID);
    },
    
    TableTdXhOrder:function(){
    	var $this = $(this);
    	var tabrows = $("tbody tr" ,$this);
    	//循环行记录,为序号重新赋值
    	var i =1;
    	for(var t=0;t< tabrows.size();t++)
    		{
    			//alert(i);
    			tabrows.eq(t).children().eq(0).html("<strong>"+i+"</strong>");
    			i++;
    		}
    	
    },
    /*
     * 获取表格的表头数组。返回编辑的表头的字段数组
     * getTableThArrays()
     * return tharrays [XMMC,XMNF...];
     */
    getTableThArrays:function(){
    	var $this = $(this);
    	var tabthrows = $("thead tr" ,$this);
    	var throwsize = tabthrows.size();//表头行尺寸
    	var k = 0,val;
    	var tbHead = $this.children('thead');//获取table对象下的thead 
		var tbHeadTh = tbHead.find('tr th');//获取thead下的tr下的th 
		var tharrays= new Array();
    	 if(throwsize>=2){//多表头处理
				var i = 0;
					for(var t =0;t<tbHeadTh.size();t++)
					{
						if(typeof(tbHeadTh.eq(t).attr("fieldname")) != "undefined")
							{
							if(typeof(tbHeadTh.eq(t).attr("type")) != "undefined")
							{

									//获取fieldname的值
									val = tbHeadTh.eq(t).attr("fieldname");
									tharrays[i] = val;
									i++;
								}
							}
							}
				//
			}else{//单表头处理
				tbHeadTh.each(function(i) {//遍历thead的tr下的th 
					//获取th属性值
					 val = $(this).attr("fieldname");
					 var valtype = $(this).attr("type");
					//获取只有fieldname的TH组成tharrays
					if(val != undefined){
						if(valtype != undefined){
						tharrays[i] = val;
					}
				}
				});
			}
    	 if(tharrays != null){
    		 return tharrays;
    	 }
    },
    /*多选表格，组成多选后更新表格时所需要的字符串
     *comprisesData(indexarry,tharrays) 
     * params
     * indexarry:选中的行记录的索引数组
     * tharrays:表格的表头数组
     * return:
     * arraydata:规定格式的json串
     */
    comprisesData:function(indexarry,tharrays){
    	var $this = $(this);
    	//组成json串
    	var tempjson = "";
    	var arraydata;
		for(var i=0;i < indexarry.length;i++)
		{
			var indexval = indexarry[i];
			//var data = $this.comprisesColumnsJsonByEditColumns(indexval);
			var data = $this.updateRowJsonByIndex(indexval,tharrays);
			tempjson += JSON.stringify(data)+",";
//			alert("tempjsonaaa:"+tempjson);
		}
		tempjson = tempjson.substring(0, tempjson.length - 1);
		//包装json串的格式后返回结果
		arraydata = defaultJson.packSaveJson(tempjson);
		return arraydata;
    },
	/*
	 * 如果为可编辑表格,获取修改的行记录的rowJson字符串,
	 * 与返回的josn串组成新的rowJson串,生成新的行记录，替换原有行内容
	 * 返回组成后的每行的新json串
	 * subresultmsgobj更新子表返回的json串对象
	 * indexval:要更新的记录索引从0开始
	 * newrowJsonData:返回的拼接后的新json串
	 */
    comprisesJson:function(subresultmsgobj,indexval){
    	if(indexval<0){
    		return null;
    	}
    	var $this = $(this);
    	
    	var rowJson = $this.getSelectedRowJsonByIndex(indexval);

    	var newrowJson = updateRowJsonByReturnData(JSON.parse(rowJson),subresultmsgobj);
    	
		return newrowJson;
    },
    /*
     * 更新表格指定行的内容
     * paras
     * jsonStr
     * tablistID:表格对象
     * index:更新的行
     */
    updateResult:function(jsonStr,tablistID,index){
    	if(index<0){
    		return ;
    	}
    	
		var $thistab = $("#"+tablistID.id);
        var rows = $("tbody tr" ,$thistab);
        var tabthrows = $("thead tr" ,$thistab);
        var tableEditable = "0";//默认不可编辑
        if(typeof($thistab.attr("editable")) != "undefined")
        {
        		tableEditable = $thistab.attr("editable");
        }
        //获取修改记录的序号的值	
        var tdxhval = rows.eq(index).children().eq(0).html();
        //
		var jsonObj;
		jsonStr = dealInputSpecialCharactor(jsonStr);
		jsonObj = convertJson.string2json1(jsonStr);
		//定义定长数组
			var arres = new Array();
		//var tableObject = $(this.id);//获取id为#DT1的table对象 
		var tableObject =  $("#"+tablistID.id);
		var tbHead = tableObject.children('thead');//获取table对象下的thead 
		var tbHeadTh = tbHead.find('tr th');//获取thead下的tr下的th 
		var showHTML ;
        //
		  var throwsize = tabthrows.size();//表头行尺寸
			//获取所有fieldname的记录数
	        var k = 0;
	        var tharrays= new Array();
			if(throwsize>=2){//多表头处理

				for(var t =0;t<tbHeadTh.size();t++)
					{
						
							if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
								{
									//tharrays[k] = tbHeadTh.eq(t);
									k++;
								}
						
					}
			if(k>0){
				for(var arr=1;arr<=k;arr++)
				{
					for(var t =0;t<tbHeadTh.size();t++)
					{
						if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
							{
								if(tbHeadTh.eq(t).attr("colindex") == arr){
									tharrays[arr-1] = tbHeadTh.eq(t);
									break;
								}
							}
					}
				}
				}
				//
			}
		//
        if(index > -1 && index < rows.size())
        {
       
	   showHTML ="<tr rowJson = "+JSON.stringify(jsonObj)+" isSelect = 0 >";
		var k = 0;
		if(tableEditable == "1"){//可编辑表格th attr type = {dic:src="XMNF",text,date} default type="text"
			
			if(throwsize>=2){//多表头处理
				for(var i =0 ;i<tharrays.length;i++){
					var val = tharrays[i].attr("fieldname");
					var Thtype = tharrays[i].attr("type");
        			var colspan = tharrays[i].attr("colspan");// add by songxb@2013-08-07
        			var maxlength = tharrays[i].attr("maxlength");// add by songxb@2013-08-07
        			//文本位置和链接参数
    				var align = tharrays[i].attr("tdalign");
    				var hasLink = tharrays[i].attr("hasLink");
    			     var linkfunction = tharrays[i].attr("linkFunction");
    			     if(linkfunction!=undefined&&hasLink=="true")
    			     {
    			     hasLink = true;
    			     }else{
    			     hasLink = false;
    			     }
    			     //文本位置和链接参数
    				 var CustomFunction = tharrays[i].attr("CustomFunction");
    				 var returnFunctionValue="";
    				 if(CustomFunction!=undefined)
    				 {
    					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(jsonObj)+")");
    				 }
        			var strlength ;
					if(val != undefined){
						if(Thtype == undefined){//不可编辑
			   				var value_SV = jsonObj[val+"_SV"];
							if(value_SV!= undefined)
							{
								value_SV  = dealSpecialCharactor(value_SV);

								strlength = value_SV.length;
								if(maxlength!= undefined)
								{
									if(strlength > maxlength){//超过长度隐藏显示
										if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+"><abbr title=\""+value_SV+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td align="+align+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td ><abbr title=\""+value_SV+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\"> "+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td ><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}
									}else{
										if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td align="+align+">"+value_SV +"</td>";
												}
												
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td>"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td>"+value_SV +"</td>";
												}
											}
										}
									}
								}else{
									if(align != undefined){//位置处理
										if(hasLink){//有链接
											showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
											}else{
												  var temp  = dealSpecialCharactor(value_SV);
												  showHTML +="<td align="+align+">"+temp +"</td>";
											}
										}
									}else{
										if(hasLink){//有链接
											showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td>"+returnFunctionValue +"</td>";
											}else{
												var temp  = dealSpecialCharactor(value_SV);
												  showHTML +="<td>"+temp+"</td>";
											}
										}
									}
								}
								
							}else{
									
									var jsonval =  jsonObj[val];

									if(jsonval==undefined)//add by songxb@2013-08-09异常处理
									{
										showHTML +="<td></td>";
									}else
									{
										jsonval  = dealSpecialCharactor(jsonval);

									strlength = jsonval.length;
									//modfied by songxb@2013-08-07 begin
									if(maxlength!= undefined && strlength > maxlength)
									{
										if(colspan!=undefined && colspan>1)//合并表格
										{
											var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
											
											if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
											{
												showHTML +="<td colspan=\""+colspan+"\"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
											}else
											{
												for(var z=0;z<colspan;z++)
												{
													
													if(temp[z].length > maxlength)
													{
														showHTML +="<td><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
													}else
													{
														showHTML +="<td>"+temp[z] +"</td>";
													}
												}
												
											}

										}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+"><abbr title=\""+jsonval+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\"> "+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
												}else{
													showHTML +="<td align="+align+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td ><abbr title=\""+jsonval+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\"> "+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
												}else{
													showHTML +="<td ><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
												}
											}
										}
									}else{
												    									
										if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
										{
											var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
											if(temp.length>0 && temp.length!=colspan)
											{
												showHTML +="<td colspan=\""+colspan+"\">"+jsonval+"</td>";
											}else
											{
												for(var z=0;z<colspan;z++)
												{
													showHTML +="<td>"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
												}
												
											}

										}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+ jsonval +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
														  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
													}else{
														  showHTML +="<td align="+align+">"+jsonval +"</td>";
													}
												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+ jsonval +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
														  showHTML +="<td>"+returnFunctionValue +"</td>";
													}else{
														  showHTML +="<td>"+jsonval +"</td>";
													}
												}
											}

										}
					    			 }
									}
								}
					    	
						}else{
							switch(Thtype.toLowerCase())
	        				{
	        					case 'text':

	        							showHTML +="<td><input type=\"text\"  class=\"span12\"  placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
	        		
	        						break;
	        					case 'number':

        							showHTML +="<td><input type=\"number\"  class=\"span12\"  placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
        		
        						    break;
	        					case 'dic':
	        						var changeFunction = tharrays[i].attr("changeFunction");
	        						if(!changeFunction){
	        					         changeFunction = "";
	        					     }else{
	        					         changeFunction = " onchange=\""+changeFunction+"(this,'"+tharrays[i].attr("fieldname")+"')\" ";
	        					    }

	        						
	        						//获取src值
	        						var htsrc = tharrays[i].attr("src");
	        						var noNullSelect =tharrays[i].attr("noNullSelect");
	        						if(noNullSelect){
	        							noNullSelect = " noNullSelect =\""+noNullSelect+"\" ";
	        						 }else{
	        							 noNullSelect = "";
	        						 }
	        							
	        	        			if(htsrc != undefined)
	    	        				{
	        	        				showHTML +="<td><select  class=\"span12\" type= \"select-one\" name = \""+val+"\" "+changeFunction+"  fieldname= \""+val+"\" kind=\"dic\" src=\""+htsrc+"\" "+noNullSelect+"></select></td>";
	    	        				}
	        	        			break;
	        					case 'date':
		        							showHTML +="<td><input type=\"date\" class=\"span12\" placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
	        						break;
	        					default:
	        						break;
	        				}
						}
								}else{
									//序号处理
				    				var Strxh = tharrays[i].attr("id"); 
				    				if(Strxh != undefined && Strxh == "_XH"){
				    					showHTML+="<th align=center><strong>"+tdxhval+"</strong></th>";
								}
						}
				}
			}else{
				tbHeadTh.each(function(i) {//遍历thead的tr下的th 
					
					//获取th属性值	
    				var Thtype = $(this).attr("type");
        			var val = $(this).attr("fieldname");
        			var colspan = $(this).attr("colspan");// add by songxb@2013-08-07
        			var maxlength = $(this).attr("maxlength");// add by songxb@2013-08-07
        			//
        			var align = $(this).attr("tdalign");
        			var hasLink = $(this).attr("hasLink");
        		    var linkfunction = $(this).attr("linkFunction");
        		    
        		     if(linkfunction!=undefined&&hasLink=="true")
        		     {
        		     hasLink = true;
        		     }else{
        		     hasLink = false;
        		     }
    				 var CustomFunction = $(this).attr("CustomFunction");
    				 var returnFunctionValue="";
    				 if(CustomFunction!=undefined)
    				 {
    					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(jsonObj)+")");
    				 } 
        			var strlength ;
        			if(Thtype == undefined)
        				{
        					Thtype = "text"; //默认编辑框为文本类型
        				}

					if(val != undefined){
						if(Thtype == undefined){//不可编辑字段
			   				var value_SV = jsonObj[val+"_SV"];
							if(value_SV!= undefined)
								{
								value_SV  = dealSpecialCharactor(value_SV);

								strlength = value_SV.length;
								if(maxlength!= undefined)
								{
									if(strlength > maxlength){//超过长度隐藏显示
										if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+"><abbr title=\""+value_SV+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\"> "+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td align="+align+"><abbr title=\""+value_SV+"\">"+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td ><abbr title=\""+value_SV+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td ><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}
										
									}else{
										if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td align="+align+">"+value_SV +"</td>";
												}
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td>"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td>"+value_SV +"</td>";
												}
											}
										}
									}
								}else{
									if(align != undefined){//位置处理
										if(hasLink){//有链接
											showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
											}else{
												var temp  = dealSpecialCharactor(value_SV);
												  showHTML +="<td align="+align+">"+temp +"</td>";
											}

										}
									}else{
										if(hasLink){//有链接
											showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td>"+returnFunctionValue +"</td>";
											}else{
												var temp  = dealSpecialCharactor(value_SV);
												  showHTML +="<td>"+temp+"</td>";
											}
										}
									}
									
								}
								
								}else{//无SV处理
									
									var jsonval =  jsonObj[val];
									if(jsonval==undefined)//add by songxb@2013-08-09异常处理
									{
										showHTML +="<td></td>";
									}else
									{
										jsonval  = dealSpecialCharactor(jsonval);

									strlength = jsonval.length;
									//modfied by songxb@2013-08-07 begin
									if(maxlength!= undefined && strlength > maxlength)
									{
										if(colspan!=undefined && colspan>1)
										{
											var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
											
											if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
											{
												showHTML +="<td colspan=\""+colspan+"\"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
											}else
											{
												for(var z=0;z<colspan;z++)
												{
													
													if(temp[z].length > maxlength)
													{
														showHTML +="<td><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
													}else
													{
														showHTML +="<td>"+temp[z] +"</td>";
													}
												}
												
											}

										}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+"><abbr title=\""+jsonval+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\"> "+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
												}else{
													showHTML +="<td align="+align+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td ><abbr title=\""+jsonval+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
												}else{
													showHTML +="<td ><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
												}
											}
											
										}
											
										//}else{
										//	showHTML +="<td>"+arres[j][i] +"</td>";
										//}
									}else{
												    									
										if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
										{
											var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
											//alert(temp);
											//alert(temp.length);
											if(temp.length>0 && temp.length!=colspan)
											{
												showHTML +="<td colspan=\""+colspan+"\">"+jsonval+"</td>";
											}else
											{
												for(var z=0;z<colspan;z++)
												{
													showHTML +="<td>"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
												}
												
											}

										}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+ jsonval +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
														  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
													}else{
														  showHTML +="<td align="+align+">"+jsonval +"</td>";
													}
												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+ jsonval +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
														  showHTML +="<td>"+returnFunctionValue +"</td>";
													}else{
														  showHTML +="<td>"+jsonval +"</td>";
													}
												}
											}

										}
										//showHTML +="<td>"+ jsonval +"</td>";
										//modified by songxb@2013-08-07 end
									}
									}
					    		}
						}else{
							switch(Thtype.toLowerCase())
	        				{
	        					case 'text':

	        							showHTML +="<td><input type=\"text\"  class=\"span12\" placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
	        						break;
	        					case 'number':

        							    showHTML +="<td><input type=\"number\"  class=\"span12\"  placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\"></td>";
        		
        						    break;	
	        					case 'dic':
	        						var changeFunction = tharrays[i].attr("changeFunction");
	        						if(!changeFunction){
	        					         changeFunction = "";
	        					     }else{
	        					         changeFunction = " onchange=\""+changeFunction+"(this,'"+tharrays[i].attr("fieldname")+"')\" ";
	        					    }

	        						var htsrc = tharrays[i].attr("src");
	        						var noNullSelect =tharrays[i].attr("noNullSelect");
	        						if(noNullSelect){
	        							noNullSelect = " noNullSelect =\""+noNullSelect+"\" ";
	        						 }else{
	        							 noNullSelect = "";
	        						 }
	        	        			if(htsrc != undefined)
	    	        				{
	        	        				showHTML +="<td><select  class=\"span12\" type= \"select-one\" name = \""+val+"\" "+changeFunction+"   fieldname= \""+val+"\" kind=\"dic\" src=\""+htsrc+"\" "+noNullSelect+"></select></td>";
	    	        				}
	        	        			break;
	        					case 'date':
		        							
		        							showHTML +="<td><input type=\"date\"  class=\"span12\"  placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
		        					
	        						break;
	        					default:
	        						break;
	        				}
						}
								}else{
									//序号处理
				    				var Strxh = $(this).attr("id"); 
				    				if(Strxh != undefined && Strxh == "_XH"){
				    					showHTML+="<th align=center><strong>"+tdxhval+"</strong></th>";
								}
						}
				});
			}
		}else{//不可编辑表格
			if(throwsize>=2){//多表头处理
				for(var i =0 ;i<tharrays.length;i++){
					var val = tharrays[i].attr("fieldname");
					var maxlength = tharrays[i].attr("maxlength"); 
					var colspan = tharrays[i].attr("colspan"); //add by songxb@2013-08-07
        			//文本位置和链接参数
    				var align = tharrays[i].attr("tdalign");
    				var hasLink = tharrays[i].attr("hasLink");
    			     var linkfunction = tharrays[i].attr("linkFunction");
    			     if(linkfunction!=undefined&&hasLink=="true")
    			     {
    			     hasLink = true;
    			     }else{
    			     hasLink = false;
    			     }
    			     var rowMerge =tharrays[i].attr("rowMerge");
    				 if(rowMerge!=undefined&&rowMerge=="true")
    			     {
    					 rowMerge = true;
    			     }else{
    			    	 rowMerge = false;
    			     }
    			     
    			     var next_tr =$thistab.getTrObjByIndex(index);
    			     var ttt = $(next_tr).find("td").eq(i-1).attr("rowspan");
    			     var rowspan_ = "";
    			     if(ttt!=undefined&&ttt!=null){
    			    	 rowspan_ = " rowspan =\""+ttt+"\"";
    			    	//如果有列合并，显示长度为最大程度*合并多少列
  					   if(maxlength!= undefined){
  					    //  maxlength = maxlength*ttt;
  					   }
    			     }else{
    			        if(rowMerge==true){
    			         if(index>0){
    			    	  var preRow = $thistab.getRowJsonObjByIndex(index-1);
    					  if(val != undefined){
    						if(jsonObj[val]==preRow[val]){
    							//showHTML +="<td></td>";
    							continue;
    						}
    					  }
    				  	 }
    			       }
    			     }
    			    
    				 var CustomFunction = tharrays[i].attr("CustomFunction");
    				 var returnFunctionValue="";
    				 if(CustomFunction!=undefined)
    				 {
    					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(jsonObj)+")");
    				 }
    			     //文本位置和链接参数
					var strlength ;
					if(val != undefined){
						//
			   				var value_SV = jsonObj[val+"_SV"];

							if(value_SV!= undefined)
								{
								
								strlength = value_SV.length;
								if(maxlength!= undefined)
								{
									if(strlength > maxlength){//超过长度隐藏显示
										if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+" "+rowspan_+"><abbr title=\""+value_SV+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td align="+align+" "+rowspan_+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td "+rowspan_+"><abbr title=\""+value_SV+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\"> "+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td "+rowspan_+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}
										
									}else{
										if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+" "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td align="+align+" "+rowspan_+">"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td align="+align+" "+rowspan_+">"+value_SV +"</td>";
												}
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td "+rowspan_+">"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td "+rowspan_+">"+value_SV +"</td>";
												}
											}
										}
										
									}
								}else{
									if(align != undefined){//位置处理
										if(hasLink){//有链接
											showHTML +="<td align="+align+" "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td align="+align+" "+rowspan_+">"+returnFunctionValue +"</td>";
											}else{
												var temp  = dealSpecialCharactor(value_SV);
												  showHTML +="<td align="+align+" "+rowspan_+">"+temp +"</td>";
											}
										}
									}else{
										if(hasLink){//有链接
											showHTML +="<td "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td "+rowspan_+">"+returnFunctionValue +"</td>";
											}else{
												var temp  = dealSpecialCharactor(value_SV);
												  showHTML +="<td "+rowspan_+">"+temp+"</td>";
											}
										}
									}
									
								}
								
							}else{//非字典处理
									
									var jsonval =  jsonObj[val];
									if(jsonval==undefined)//add by songxb@2013-08-09异常处理
									{
										showHTML +="<td></td>";
									}else
									{
									strlength = jsonval.length;
									//modfied by songxb@2013-08-07 begin
									if(maxlength!= undefined && strlength > maxlength)
									{
										if(colspan!=undefined && colspan>1)
										{
											var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
											
											if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
											{
												showHTML +="<td colspan=\""+colspan+"\" "+rowspan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
											}else
											{
												for(var z=0;z<colspan;z++)
												{
													
													if(temp[z].length > maxlength)
													{
														showHTML +="<td "+rowspan_+"><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
													}else
													{
														showHTML +="<td "+rowspan_+">"+temp[z] +"</td>";
													}
												}
												
											}

										}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+" "+rowspan_+"><abbr title=\""+jsonval+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\"> "+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
												}else{
													showHTML +="<td align="+align+" "+rowspan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td "+rowspan_+"><abbr title=\""+jsonval+"\" > <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
												}else{
													showHTML +="<td "+rowspan_+"><abbr title=\""+jsonval+"\" > "+jsonval.substring(0,maxlength) +"...</abbr></td>";
												}
											}
											
										}
											

									}else{
												    									
										if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
										{
											var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串

											if(temp.length>0 && temp.length!=colspan)
											{
												showHTML +="<td colspan=\""+colspan+"\" "+rowspan_+">"+jsonval+"</td>";
											}else
											{
												for(var z=0;z<colspan;z++)
												{
													showHTML +="<td "+rowspan_+">"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
												}
												
											}

										}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+" "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+ jsonval +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
														  showHTML +="<td align="+align+" "+rowspan_+">"+returnFunctionValue +"</td>";
													}else{
														  showHTML +="<td align="+align+" "+rowspan_+">"+jsonval +"</td>";
													}
												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+ jsonval +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
														  showHTML +="<td "+rowspan_+">"+returnFunctionValue +"</td>";
													}else{
														  showHTML +="<td "+rowspan_+">"+jsonval +"</td>";
													}
												}
											}
										}
										//showHTML +="<td>"+ jsonval +"</td>";
										//modified by songxb@2013-08-06 end
									}
					    		}
							}
							}else{
									//序号处理
				    				var Strxh = tharrays[i].attr("id"); 
				    				if(Strxh != undefined && Strxh == "_XH"){
				    					showHTML+="<th align=center><strong>"+tdxhval+"</strong></th>";
				    				}
							}
					}
			}else{
				tbHeadTh.each(function(i) {//遍历thead的tr下的th 
					
					var val = $(this).attr("fieldname");
					var maxlength = $(this).attr("maxlength");
					var colspan = $(this).attr("colspan"); //add by songxb@2013-08-07
					var strlength ;
					//文本位置和链接参数
    				var align = $(this).attr("tdalign");
    				var hasLink = $(this).attr("hasLink");
    			     var linkfunction = $(this).attr("linkFunction");
    			     if(linkfunction!=undefined&&hasLink=="true")
    			     {
    			     hasLink = true;
    			     }else{
    			     hasLink = false;
    			     }
    				 var CustomFunction = $(this).attr("CustomFunction");
    				 var returnFunctionValue="";
    				 if(CustomFunction!=undefined)
    				 {
    					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(jsonObj)+")");
    				 }
    				 
    				 var rowMerge =$(this).attr("rowMerge");
    				 if(rowMerge!=undefined&&rowMerge=="true")
    			     {
    					 rowMerge = true;
    			     }else{
    			    	 rowMerge = false;
    			     }
    				 
    				 var next_tr =$thistab.getTrObjByIndex(index);
    			     var ttt = $(next_tr).find("td").eq(i-1).attr("rowspan");
    			     var rowspan_ = "";
    			     if(ttt!=undefined&&ttt!=null){
    			    	 rowspan_ = " rowspan =\""+ttt+"\"";
    			    	//如果有列合并，显示长度为最大程度*合并多少列
  					   if(maxlength!= undefined){
  					    //  maxlength = maxlength*ttt;
  					   }
    			     }else{
     			        if(rowMerge==true){
       			         if(index>0){
       			    	  var preRow = $thistab.getRowJsonObjByIndex(index-1);
       					  if(val != undefined){
       						if(jsonObj[val]==preRow[val]){
       							//showHTML +="<td></td>";
       							return true;
       						}
       					  }
       				  	 }
       			       }
       			     }
    			     //文本位置和链接参数
					if(val != undefined){
						//
			   				var value_SV = jsonObj[val+"_SV"];
							if(value_SV!= undefined)
								{
								
								strlength = value_SV.length;
								if(maxlength!= undefined)
								{
									if(strlength > maxlength){//超过长度隐藏显示
										if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+" "+rowspan_+"><abbr title=\""+value_SV+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td align="+align+" "+rowspan_+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td "+rowspan_+"><abbr title=\""+value_SV+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\"> "+value_SV.substring(0,maxlength) +"...</a></abbr></td>";
											}else{
												showHTML +="<td "+rowspan_+"><abbr title=\""+value_SV+"\"> "+value_SV.substring(0,maxlength) +"...</abbr></td>";
											}
										}
										
									}else{
										if(align != undefined){//位置处理
											if(hasLink){//有链接
												showHTML +="<td align="+align+" "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td align="+align+" "+rowspan_+">"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td align="+align+" "+rowspan_+">"+value_SV +"</td>";
												}
											}
										}else{
											if(hasLink){//有链接
												showHTML +="<td "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
											}else{
												if(returnFunctionValue&&returnFunctionValue!=""){
													  showHTML +="<td "+rowspan_+">"+returnFunctionValue +"</td>";
												}else{
													  showHTML +="<td "+rowspan_+">"+value_SV +"</td>";
												}
											}
										}
										
									}
								}else{
									if(align != undefined){//位置处理
										if(hasLink){//有链接
											showHTML +="<td align="+align+" "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td align="+align+" "+rowspan_+">"+returnFunctionValue +"</td>";
											}else{
												var temp  = dealSpecialCharactor(value_SV);
												  showHTML +="<td align="+align+" "+rowspan_+">"+temp +"</td>";
											}
										}
									}else{
										if(hasLink){//有链接
											showHTML +="<td "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+value_SV +"</a></td>";
										}else{
											if(returnFunctionValue&&returnFunctionValue!=""){
												  showHTML +="<td "+rowspan_+">"+returnFunctionValue +"</td>";
											}else{
												var temp  = dealSpecialCharactor(value_SV);
												  showHTML +="<td "+rowspan_+">"+temp +"</td>";
											}
										}
									}
									
								}
								
								}else{
									
									var jsonval =  jsonObj[val];
									if(jsonval==undefined)//add by songxb@2013-08-09异常处理
									{
										showHTML +="<td></td>";
									}else
									{
									strlength = jsonval.length;
									//modfied by songxb@2013-08-07 begin
									if(maxlength!= undefined && strlength > maxlength)
									{
										if(colspan!=undefined && colspan>1)
										{
											var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
											
											if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
											{
												showHTML +="<td colspan=\""+colspan+"\" "+rowspan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
											}else
											{
												for(var z=0;z<colspan;z++)
												{
													
													if(temp[z].length > maxlength)
													{
														showHTML +="<td "+rowspan_+"><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
													}else
													{
														showHTML +="<td "+rowspan_+">"+temp[z] +"</td>";
													}
												}
												
											}

										}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+" "+rowspan_+"><abbr title=\""+jsonval+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
												}else{
													showHTML +="<td align="+align+" "+rowspan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td "+rowspan_+"><abbr title=\""+jsonval+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+jsonval.substring(0,maxlength) +"...</a></abbr></td>";
												}else{
													showHTML +="<td "+rowspan_+"><abbr title=\""+jsonval+"\"> "+jsonval.substring(0,maxlength) +"...</abbr></td>";
												}
											}
											
										}
											
										//}else{
										//	showHTML +="<td>"+arres[j][i] +"</td>";
										//}
									}else{
												    									
										if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
										{
											var temp = jsonval.split("||");//从后台取出出带"||"号分隔的字符串
											//alert(temp);
											//alert(temp.length);
											if(temp.length>0 && temp.length!=colspan)
											{
												showHTML +="<td colspan=\""+colspan+"\" "+rowspan_+">"+jsonval+"</td>";
											}else
											{
												for(var z=0;z<colspan;z++)
												{
													showHTML +="<td "+rowspan_+">"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
												}
												
											}

										}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+" "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+ jsonval +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
														  showHTML +="<td align="+align+" "+rowspan_+">"+returnFunctionValue +"</td>";
													}else{
														  showHTML +="<td align="+align+" "+rowspan_+">"+jsonval +"</td>";
													}
												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td "+rowspan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+index+"')\">"+ jsonval +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
														  showHTML +="<td "+rowspan_+">"+returnFunctionValue +"</td>";
													}else{
														  showHTML +="<td "+rowspan_+">"+jsonval +"</td>";
													}
												}
											}
	
										}
										//showHTML +="<td>"+ jsonval +"</td>";
										//modified by songxb@2013-08-06 end

									 }
									}
					    		}
							}else{
									//序号处理
				    				var Strxh = $(this).attr("id"); 
				    				if(Strxh != undefined && Strxh == "_XH"){
				    					showHTML+="<th align=center><strong>"+tdxhval+"</strong></th>";
							}
						}
				});
			}
		}
		showHTML  +="</tr>";
		//alert(showHTML);
        }
		//循环table_row替换特定行
        var row = rows.eq(index);
        row.replaceWith(showHTML);
        //
     	//如果可编辑表格,初始化表格字典,为表格字典赋值
    	var tableEditable ;
        if(typeof($("#"+tablistID.id).attr("editable")) != "undefined")
        	{
        		tableEditable = $("#"+tablistID.id).attr("editable");
        	}else{
        		tableEditable = "0";
        	}
        
        if(tableEditable == "1"){
        	//生成表格中的字典
        	setStyle(tablistID,null);
        	//为tr rowJSON值赋给表格中对应的行
        	setRowDefaultValueByIndex(tablistID,index);
        }
        //
        bindClickEvent(tablistID);
    },
    /*
     * 
     */
    InsertTableRows:function (json,tablistID){
    	
    	var b;
    	// 使用方法一
    	b = convertJson.string2json1(json);
        var rowDataJsons = b.response;


    	//定义数组
    		var arres = new Array(); //声明一维数组
    		//
    		var $thistab = $("#"+tablistID.id);
            var rows = $("thead tr" ,$thistab);
          
    	var tableObject = $("#"+tablistID.id);//获取id为#DT1的table对象 
 //   	var tabletype = tableObject.attr("type");
    	var tbHead = tableObject.children('thead');//获取table对象下的thead 
    	var tbHeadTh = tbHead.find('tr th');//获取thead下的tr下的th 
        var showHTML = "";
        var throwsize = rows.size();//表头行尺寸
        var tableEditable = "0";//默认不可编辑
        if(typeof($thistab.attr("editable")) != "undefined")
        	{
        		tableEditable = $thistab.attr("editable");
        	}
		//获取所有fieldname的记录数
        var k = 0;
        var tharrays= new Array();
		if(throwsize>=2){//多表头处理

			for(var t =0;t<tbHeadTh.size();t++)
				{
					
						if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
							{
								//tharrays[k] = tbHeadTh.eq(t);
								k++;
							}
					
				}
		if(k>0){
			for(var arr=1;arr<=k;arr++)
			{
				for(var t =0;t<tbHeadTh.size();t++)
				{
					if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
						{
							if(tbHeadTh.eq(t).attr("colindex") == arr){
								tharrays[arr-1] = tbHeadTh.eq(t);
								break;
							}
						}
				}
			}
			}
			//
		}
		if(tableEditable == "1"){//可编辑表格th attr type = {dic:src="XMNF",text,date} default type="text"
			
			
			for ( var j = 0; j < rowDataJsons.data.length; j++) {
	    		arres[j] = new Array(); //再声明二维
	    		//showHTML  +="<tr rowJson = \"\">";
	    		showHTML  +="<tr rowJson = "+JSON.stringify(rowDataJsons.data[j])+" isSelect = 0 >";
	    		//showHTML  +="<tr rowJson = "+defaultJson.json2str(rowDataJsons.data[j])+">";
	    		
	    		if(throwsize>=2){//多表头处理
	    		
	    			for(var i =0 ;i<tharrays.length;i++){
	    				
	    				//alert(l+":tharrays:"+tharrays[l].attr("fieldname"));
	    				
//	        			var thisIndex = tbHeadTh.index($(this));//获取th所在的列号
	    				
	        			//获取th属性值	
	    				var Thtype = tharrays[i].attr("type");
	        			var val = tharrays[i].attr("fieldname");
	        			var maxlength = tharrays[i].attr("maxlength");   
	        			var colspan = tharrays[i].attr("colspan");//add by songxb@2013-08-07
	        			//文本位置和链接参数
	    				var align = tharrays[i].attr("tdalign");
	    				var hasLink = tharrays[i].attr("hasLink");
	    			     var linkfunction = tharrays[i].attr("linkFunction");
	    			     if(linkfunction!=undefined&&hasLink=="true")
	    			     {
	    			     hasLink = true;
	    			     }else{
	    			     hasLink = false;
	    			     }
	    				 var CustomFunction = tharrays[i].attr("CustomFunction");
	    				 var returnFunctionValue="";
	    				 if(CustomFunction!=undefined)
	    				 {
	    					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(rowDataJsons.data[j])+")");
	    				 }
	    			     //文本位置和链接参数
	        			//alert(colspan>1); 
	        			if(val != undefined){
	        				if(Thtype == undefined ){
	        					//可编辑字段
		        				var value_SV = rowDataJsons.data[j][val+"_SV"];

		        				if(value_SV!= undefined)//有SV处理
		        					{

		        						arres[j][i] = rowDataJsons.data[j][val+"_SV"];

		        						if(arres[j][i]!=null){
		        							arres[j][i] = dealSpecialCharactor(arres[j][i]);
		        							strlength = arres[j][i].length;
		        							if(maxlength!= undefined)
		        							{
		        								if(strlength > maxlength){//超过长度隐藏显示
		    										if(align != undefined){//位置处理
		    											if(hasLink){//有链接
		    												showHTML +="<td align="+align+"><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
		    											}else{
		    												showHTML +="<td align="+align+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    											}
		    										}else{
		    											if(hasLink){//有链接
		    												showHTML +="<td><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
		    											}else{
		    												showHTML +="<td><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    											}
		    										}
		        									
		        								}else{
		    										if(align != undefined){//位置处理
		    											if(hasLink){//有链接
		    												showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
		    											}else{
		    												if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
		  												    }else{
		  													  showHTML +="<td align="+align+">"+arres[j][i] +"</td>";
		  												    }
		    											}
		    										}else{
		    											if(hasLink){//有链接
		    												showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
		    											}else{
		    												if(returnFunctionValue&&returnFunctionValue!=""){
			  													  showHTML +="<td>"+returnFunctionValue +"</td>";
			  												}else{
			  													  showHTML +="<td>"+arres[j][i] +"</td>";
			  												}
		    											}
		    										}
		        									
		        								}
		        							}else{
												if(align != undefined){//位置处理
													if(hasLink){//有链接
														showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
													}else{
														if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
		  												 }else{
		  													var temp  = dealSpecialCharactor(arres[j][i]);
		  													  showHTML +="<td align="+align+">"+temp+"</td>";
		  												 }
													}
												}else{
													if(hasLink){//有链接
														showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
													}else{
														if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td>"+returnFunctionValue +"</td>";
		  												}else{
		  													var temp  = dealSpecialCharactor(arres[j][i]);
		  													  showHTML +="<td>"+temp+"</td>";
		  												}
													}
												}
		        								
		        							}
		        							
		        		    				  
		        		    			}
		        					}else{//无SV处理
		        						arres[j][i] = rowDataJsons.data[j][val];
		        						if(arres[j][i]!=null){

		        							strlength = arres[j][i].length;
		        							//modfied by songxb@2013-08-07 begin
		        							if(maxlength!= undefined && strlength > maxlength)
		        							{
		        								//if(strlength > maxlength){//超过长度隐藏显示
		    									if(colspan!=undefined && colspan>1)
		    									{
		    										//alert();
		    										var temp = arres[j][i].split("||");//从后台取出出带"||"号分隔的字符串
		    										
		    										if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
		    										{
		    											showHTML +="<td colspan=\""+colspan+"\"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    										}else
		    										{
		    											for(var z=0;z<colspan;z++)
		    											{
		    												
		    												if(temp[z].length > maxlength)
		    												{
		    													showHTML +="<td><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
		    												}else
		    												{
		    													showHTML +="<td>"+temp[z] +"</td>";
		    												}
		    											}
		    											
		    										}

		    									}else{
		    										if(align != undefined){//位置处理
		    											if(hasLink){//有链接
		    												showHTML +="<td align="+align+"><abbr title=\""+arres[j][i]+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
		    											}else{
		    												showHTML +="<td align="+align+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    											}
		    										}else{
		    											if(hasLink){//有链接
		    												showHTML +="<td><abbr title=\""+arres[j][i]+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
		    											}else{
		    												showHTML +="<td><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    											}
		    										}
		    										
		    									}
		        									
		        							}else{//未定义maxlength属性
		        								   									
		    									if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
		    									{
		    										var temp = arres[j][i].split("||");//从后台取出出带"||"号分隔的字符串
		    										//alert(temp);
		    										//alert(temp.length);
		    										if(temp.length>0 && temp.length!=colspan)
		    										{
		    											showHTML +="<td colspan=\""+colspan+"\">"+arres[j][i] +"</td>";
		    										}else
		    										{
		    											for(var z=0;z<colspan;z++)
		    											{
		    												showHTML +="<td>"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
		    											}
		    											
		    										}

		    									}else{
		    										if(align != undefined){//位置处理
		    											if(hasLink){//有链接
				    										showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
		    											}else{
		    												if(returnFunctionValue&&returnFunctionValue!=""){
			  													  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
			  												 }else{
			  													  showHTML +="<td align="+align+">"+arres[j][i] +"</td>";
			  												 }
		    											}
		    										}else{
		    											if(hasLink){//有链接
				    										showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
		    											}else{
		    												if(returnFunctionValue&&returnFunctionValue!=""){
			  													  showHTML +="<td>"+returnFunctionValue +"</td>";
			  												}else{
			  													  showHTML +="<td>"+arres[j][i] +"</td>";
			  												}
		    											}
		    										}
		    									}
		    									//showHTML +="<td>"+arres[j][i] +"</td>";
		    									//modified by songxb@2013-08-07 end
		        								
		        							}
		        		    			}
		        					}
	        				}else{
		        				switch(Thtype.toLowerCase())
		        				{
		        					case 'text':
		        						arres[j][i] = rowDataJsons.data[j][val];
		        						if(arres[j][i]!=null){
		        							showHTML +="<td><input type=\"text\"  class=\"span12\"  placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
		        		    				}
		        						break;
		        					case 'number':
		        						arres[j][i] = rowDataJsons.data[j][val];
		        						if(arres[j][i]!=null){
	        							    showHTML +="<td><input type=\"number\"  class=\"span12\"  placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
		        						}
	        						    break;
		        					case 'dic':
		        						var changeFunction = tharrays[i].attr("changeFunction");
		        						if(!changeFunction){
		        							changeFunction = "";
		        						}else{
		        							changeFunction = " onchange=\""+changeFunction+"(this,'"+tharrays[i].attr("fieldname")+"')\" ";
		        						}
		        						arres[j][i] = rowDataJsons.data[j][val];
		        						//获取src值
		        						var htsrc = tharrays[i].attr("src");
		        						var noNullSelect =tharrays[i].attr("noNullSelect");
		        						if(noNullSelect){
		        							noNullSelect = " noNullSelect =\""+noNullSelect+"\" ";
		        						 }else{
		        							 noNullSelect = "";
		        						 }
		        	        			if(htsrc != undefined)
		    	        				{
		        	        				showHTML +="<td><select  class=\"span12\" type= \"select-one\" name = \""+val+"\" "+changeFunction+" fieldname= \""+val+"\" kind=\"dic\" src=\""+htsrc+"\"  "+noNullSelect+"></select></td>";
		    	        				}
		        	        			break;
		        					case 'date':

			        							showHTML +="<td><input type=\"date\" class=\"span12\" placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";

		        						break;
		        					default:
		        						break;
		        				}
	        				}
	        			}else{
	        				//序号处理
	        				var Strxh = tharrays[i].attr("id"); 
	        				if(Strxh != undefined && Strxh == "_XH"){
	        					showHTML+="<th align=center><strong>"+parseInt(j+1)+"</strong></th>";
	        				}
	        			}
	        		}
	    		}else{//单表头处理
	        		tbHeadTh.each(function(i) {//遍历thead的tr下的th 
//	        		//获取th属性值	
	    				var Thtype = $(this).attr("type");
	        			var val = $(this).attr("fieldname");
	        			var maxlength = $(this).attr("maxlength");   
	        			var colspan = $(this).attr("colspan");   
						//文本位置和链接参数
	    				var align = $(this).attr("tdalign");
	    				var hasLink = $(this).attr("hasLink");
	    			     var linkfunction = $(this).attr("linkFunction");
	    			     if(linkfunction!=undefined&&hasLink=="true")
	    			     {
	    			     hasLink = true;
	    			     }else{
	    			     hasLink = false;
	    			     }
	    				 var CustomFunction = $(this).attr("CustomFunction");
	    				 var returnFunctionValue="";
	    				 if(CustomFunction!=undefined)
	    				 {
	    					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(rowDataJsons.data[j])+")");
	    				 }
	    			     //文本位置和链接参数
	        			if(val != undefined){
	        				if(Thtype == undefined ){//不可编辑字段
		        				var value_SV = rowDataJsons.data[j][val+"_SV"];
		        				if(value_SV!= undefined)//有SV值处理
		        					{
		        						arres[j][i] = rowDataJsons.data[j][val+"_SV"];
		        						if(arres[j][i]!=null){
		        							arres[j][i] = dealSpecialCharactor(arres[j][i]);
		        							strlength = arres[j][i].length;
		        							if(maxlength!= undefined)
		        							{
		        								if(strlength > maxlength){//超过长度隐藏显示
		    										if(align != undefined){//位置处理
		    											if(hasLink){//有链接
		    												showHTML +="<td align="+align+"><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
		    											}else{
		    												showHTML +="<td align="+align+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    											}
		    										}else{
		    											if(hasLink){//有链接
		    												showHTML +="<td><abbr title=\""+arres[j][i]+"\"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
		    											}else{
		    												showHTML +="<td><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    											}
		    										}
		        									
		        								}else{
		    										if(align != undefined){//位置处理
		    											if(hasLink){//有链接
		    												showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
		    											}else{
		    												if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
		  												    }else{
		  													  showHTML +="<td align="+align+">"+arres[j][i] +"</td>";
		  												    }
		    											}
		    										}else{
		    											if(hasLink){//有链接
		    												showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
		    											}else{
		    												if(returnFunctionValue&&returnFunctionValue!=""){
			  													  showHTML +="<td>"+returnFunctionValue +"</td>";
			  												    }else{
			  													  showHTML +="<td>"+arres[j][i] +"</td>";
			  												    }
		    											}
		    										}
		        									
		        								}
		        							}else{
												if(align != undefined){//位置处理
													if(hasLink){//有链接
														showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
													}else{
														if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
		  												}else{
		  													var temp  = dealSpecialCharactor(arres[j][i]);
															showHTML +="<td>"+temp+"</td>";
		  												}
														
													}
												}else{
													if(hasLink){//有链接
														showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
													}else{
														if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td>"+returnFunctionValue +"</td>";
		  												}else{
		  													var temp  = dealSpecialCharactor(arres[j][i]);
		  													  showHTML +="<td>"+temp+"</td>";
		  												}
														}
												}
		        								
		        							}
		        		    			}
		        					}else{//无SV值处理
		        						arres[j][i] = rowDataJsons.data[j][val];
		        						if(arres[j][i]!=null){
		        							arres[j][i] = dealSpecialCharactor(arres[j][i]);
		        							strlength = arres[j][i].length;
		        							//modfied by songxb@2013-08-07 begin
		        							if(maxlength!= undefined && strlength > maxlength)
		        							{
		        								//if(strlength > maxlength){//超过长度隐藏显示
		    									if(colspan!=undefined && colspan>1)
		    									{
		    										var temp = arres[j][i].split("||");//从后台取出出带"||"号分隔的字符串
		    										
		    										if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
		    										{
		    											showHTML +="<td colspan=\""+colspan+"\"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    										}else
		    										{
		    											for(var z=0;z<colspan;z++)
		    											{
		    												
		    												if(temp[z].length > maxlength)
		    												{
		    													showHTML +="<td><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
		    												}else
		    												{
		    													showHTML +="<td>"+temp[z] +"</td>";
		    												}
		    											}
		    											
		    										}

		    									}else{
		    										if(align != undefined){//位置处理
		    											if(hasLink){//有链接
		    												showHTML +="<td align="+align+"><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
		    											}else{
		    												showHTML +="<td align="+align+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    											}
		    										}else{
		    											if(hasLink){//有链接
		    												showHTML +="<td><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
		    											}else{
		    												showHTML +="<td><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
		    											}
		    										}
		    										
		    									}
		        									
		        							}else{//未定义maxlength
		    											    									
		    									if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
		    									{
		    										var temp = arres[j][i].split("||");//从后台取出出带"||"号分隔的字符串
		    										//alert(temp);
		    										//alert(temp.length);
		    										if(temp.length>0 && temp.length!=colspan)
		    										{
		    											showHTML +="<td colspan=\""+colspan+"\">"+arres[j][i] +"</td>";
		    										}else
		    										{
		    											for(var z=0;z<colspan;z++)
		    											{
		    												showHTML +="<td>"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
		    											}
		    											
		    										}

		    									}else{
		    										if(align != undefined){//位置处理
		    											if(hasLink){//有链接
		    												showHTML +="<td align="+align+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
		    											}else{
		    												if(returnFunctionValue&&returnFunctionValue!=""){
			  													  showHTML +="<td align="+align+">"+returnFunctionValue +"</td>";
			  												}else{
			  													  showHTML +="<td align="+align+">"+arres[j][i] +"</td>";
			  												}
		    											}
		    										}else{
		    											if(hasLink){//有链接
		    												showHTML +="<td><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
		    											}else{
		    												if(returnFunctionValue&&returnFunctionValue!=""){
			  													  showHTML +="<td>"+returnFunctionValue +"</td>";
			  												    }else{
			  													  showHTML +="<td>"+arres[j][i] +"</td>";
			  												    }
		    											}
		    										}
		    										
		    									}
		        								
		        							}
		        		    			}
		        					}
	        				}else{
	        					switch(Thtype.toLowerCase())
		        				{
		        					case 'text':
		        						arres[j][i] = rowDataJsons.data[j][val];
		        						if(arres[j][i]!=null){
		        							showHTML +="<td><input type=\"text\"  class=\"span12\" placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
		        		    				}
		        						break;
		        					case 'number':
		        						arres[j][i] = rowDataJsons.data[j][val];
		        						if(arres[j][i]!=null){
		        							showHTML +="<td><input type=\"number\"  class=\"span12\" placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";
		        		    				}
		        						break;	
		        					case 'dic':
		        						var changeFunction = $(this).attr("changeFunction");
		        						if(!changeFunction){
		        							changeFunction = "";
		        						}else{
		        							changeFunction = " onchange=\""+changeFunction+"(this,'"+$(this).attr("fieldname")+"')\" ";
		        						}
		        						arres[j][i] = rowDataJsons.data[j][val];
		        						//获取src值
		        						var htsrc = tharrays[i].attr("src");
		        						var noNullSelect =tharrays[i].attr("noNullSelect");
		        						if(noNullSelect){
		        							noNullSelect = " noNullSelect =\""+noNullSelect+"\" ";
		        						 }else{
		        							 noNullSelect = "";
		        						 }
		        	        			if(htsrc != undefined)
		    	        				{
		        	        				showHTML +="<td><select  class=\"span12\" type= \"select-one\" name = \""+val+"\" "+changeFunction+"   fieldname= \""+val+"\" kind=\"dic\" src=\""+htsrc+"\" "+noNullSelect+"></select></td>";
		    	        				}
		        	        			break;
		        					case 'date':

	        							showHTML +="<td><input type=\"date\" class=\"span12\" placeholder=\"\" name = \""+val+"\" fieldname= \""+val+"\" ></td>";

	        							break;
		        					default:
		        						break;
		        				}
	        				}
	        			}else{
	        				//序号处理
	        				var Strxh = $(this).attr("id"); 
	        				if(Strxh != undefined && Strxh == "_XH"){
	        					showHTML+="<th align=center><strong>"+parseInt(j+1)+"</strong></th>";
	        				}
	        			}
	        		});	
	    		}
	    		showHTML  +="</tr>";
	    		//alert("showHTML:"+showHTML);
	    	}
		}else{//不可编辑编辑表格
			for ( var j = 0; j < rowDataJsons.data.length; j++) {
	    		arres[j] = new Array(); //再声明二维
	    		//showHTML  +="<tr rowJson = \"\">";
	    		showHTML  +="<tr rowJson = "+JSON.stringify(rowDataJsons.data[j])+" isSelect = 0 >";
	    		//showHTML  +="<tr rowJson = "+defaultJson.json2str(rowDataJsons.data[j])+">";
	    		if(throwsize>=2){//多表头处理
	    		
	    			for(var i =0 ;i<tharrays.length;i++){
	    				//alert(l+":tharrays:"+tharrays[l].attr("fieldname"));
	    				
//	        			var thisIndex = tbHeadTh.index($(this));//获取th所在的列号
	        			//获取th属性值	
	    				//alert(tharrays[i].attr("fieldname"));
	        			var val = tharrays[i].attr("fieldname");

	        			var maxlength = tharrays[i].attr("maxlength");    
	        			var colspan = tharrays[i].attr("colspan"); //add by songxb@2013-08-07
	        			var strlength ;
	        			//文本位置和链接参数
	    				var align = tharrays[i].attr("tdalign");
	    				var hasLink = tharrays[i].attr("hasLink");
	    			     var linkfunction = tharrays[i].attr("linkFunction");
	    			     if(linkfunction!=undefined&&hasLink=="true")
	    			     {
	    			        hasLink = true;
	    			     }else{
	    			        hasLink = false;
	    			     }
	    			     //文本位置和链接参数
	    			     //自定义函数返回值
	     				 var CustomFunction = tharrays[i].attr("CustomFunction");
	     				 var returnFunctionValue="";
	    				 if(CustomFunction!=undefined)
	    				 {
	    					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(rowDataJsons.data[j])+")");
	    				 }	    			
	    				 //是否相同内容列合并
	    				 var rowMerge =tharrays[i].attr("rowMerge");
	    				 if(rowMerge!=undefined&&rowMerge=="true")
	    			     {
	    					 rowMerge = true;
	    			     }else{
	    			    	 rowMerge = false;
	    			     }
	    				 //如果相同内容合并，判断上一行内容是否和当前行一致如果一致则不生成td标签，
	    				 //判断下一行内容是否和当前行一致，如果一致增加rowspan属性，并计算下面几行相同，得到rowspan值
	    				   var rowSpan_ = "";

	    				 if( rowMerge ==true){
	    				 //判断上一行内容，begin
	    				   if(j>0){
	    					 var preRow = rowDataJsons.data[j-1];
	    					 if(val != undefined){
	    						if(rowDataJsons.data[j][val]==preRow[val]){
	    							//showHTML +="<td></td>";
	    							continue;
	    						}
	    				  	 }
	    				   }
	    				 //判断上一行内容，end
	    				 //计算rowspan end
	    				   var t = 0;
	    				   if(j<rowDataJsons.data.length-1){
	    				     for ( var ss = j+1; ss < rowDataJsons.data.length; ss++) {
	    				    	 if(rowDataJsons.data[j][val]==rowDataJsons.data[ss][val]){
	    				    		 t = t+1;
	    				    	 }else{
	    				    		 break;
	    				    	 }
	    				     }
	    				   }
	    				   if(t>0){
	    					   t = t+1;
	    					   rowSpan_ = " rowspan =\""+t+"\"";
	    					   //如果有列合并，显示长度为最大程度*合并多少列
	    					   if(maxlength!= undefined){
	    					  //    maxlength = maxlength*t;
	    					   }
	    					
	    				   }

	    		    	}
	        			if(val != undefined){
	        				var value_SV = rowDataJsons.data[j][val+"_SV"];

	        				if(value_SV!= undefined)//有SV字段处理
	        					{

	        						arres[j][i] = rowDataJsons.data[j][val+"_SV"];
	        						if(arres[j][i]!=null){
	        							arres[j][i] = dealSpecialCharactor(arres[j][i]);

	        							strlength = arres[j][i].length;
	        							if(maxlength!= undefined)
	        							{
	        								if(strlength > maxlength){//超过长度隐藏显示
	    										if(align != undefined){//位置处理
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+JSON.stringify(rowDataJsons.data[j])+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
	    											}else{
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    											}
	    										}else{
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
	    											}else{
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\" "+rowSpan_+"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    											}
	    										}
	        									
	        								}else{
	    										if(align != undefined){//位置处理
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
	    											}else{
	    												if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
		  												    }else{
		  													  showHTML +="<td align="+align+" "+rowSpan_+">"+arres[j][i] +"</td>";
		  												    }
	    											}
	    										}else{
	    											if(hasLink){//有链接
	    												showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
	    											}else{
	    												if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
		  												    }else{
		  													  showHTML +="<td "+rowSpan_+">"+arres[j][i] +"</td>";
		  												    }
	    											}
	    										}
	        									
	        								}
	        							}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
			        								showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
	  													  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
	  												    }else{
	  												    	var temp  = dealSpecialCharactor(arres[j][i]);
	  													  showHTML +="<td align="+align+" "+rowSpan_+">"+temp+"</td>";
	  												    }
												}
											}else{
												if(hasLink){//有链接
			        								showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
												}else{
													if(returnFunctionValue&&returnFunctionValue!=""){
	  													  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
	  												    }else{
	  												    	var temp  = dealSpecialCharactor(arres[j][i]);
	  													  showHTML +="<td "+rowSpan_+">"+temp+"</td>";
	  												    }
												}
											}

	        							}
	        							
	        		    				  
	        		    			}
	        					}else{//无SV字段处理
	        						arres[j][i] = rowDataJsons.data[j][val];
	        						if(arres[j][i]!=null){
	        							arres[j][i] = dealSpecialCharactor(arres[j][i]);

	        							strlength = arres[j][i].length;
	        							//modfied by songxb@2013-08-07 begin
	        							if(maxlength!= undefined && strlength > maxlength)
	        							{
	        								//if(strlength > maxlength){//超过长度隐藏显示
	    									if(colspan!=undefined && colspan>1)
	    									{
	    										var temp = arres[j][i].split("||");//从后台取出出带"||"号分隔的字符串
	    										
	    										if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
	    										{
	    											showHTML +="<td colspan=\""+colspan+"\" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    										}else
	    										{
	    											for(var z=0;z<colspan;z++)
	    											{
	    												
	    												if(temp[z].length > maxlength)
	    												{
	    													showHTML +="<td "+rowSpan_+" "+rowSpan_+"><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
	    												}else
	    												{
	    													showHTML +="<td "+rowSpan_+" "+rowSpan_+">"+temp[z] +"</td>";
	    												}
	    											}
	    											
	    										}

	    									}else{

	    										if(align != undefined){//位置处理
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
	    											}else{
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    											}
	    										}else{
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
	    											}else{
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\" "+rowSpan_+"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    											}
	    										}
	    										
	    									}
	        									
	        							}else{//未定义maxlength
	    											    									
	    									if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
	    									{
	    										var temp = arres[j][i].split("||");//从后台取出出带"||"号分隔的字符串
	    										//alert(temp);
	    										//alert(temp.length);
	    										if(temp.length>0 && temp.length!=colspan)
	    										{
	    											showHTML +="<td colspan=\""+colspan+"\" "+rowSpan_+">"+arres[j][i] +"</td>";
	    										}else
	    										{
	    											for(var z=0;z<colspan;z++)
	    											{
	    												showHTML +="<td "+rowSpan_+">"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
	    											}
	    											
	    										}

	    									}else{
	    										
	    										if(align != undefined){//位置处理
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
	    											}else{
	    												if(returnFunctionValue&&returnFunctionValue!=""){
	    													showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
	    												}else{
	    													showHTML +="<td align="+align+" "+rowSpan_+">"+arres[j][i] +"</td>";
	    												}
	    											}
	    										}else{
	    											if(hasLink){//有链接
	    												showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
	    											}else{
	    												if(returnFunctionValue&&returnFunctionValue!=""){
	    													showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
	    												}else{
	    													showHTML +="<td "+rowSpan_+">"+arres[j][i] +"</td>";
	    												}
	    												
	    											}
	    										}
	    										
	    									}
	        		    			
	        							}
	        		    			}
	        					}
	        			}else{
	        				//序号处理
	        				var Strxh = tharrays[i].attr("id"); 
	        				if(Strxh != undefined && Strxh == "_XH"){
	        					showHTML+="<th align=center><strong>"+parseInt(j+1)+"</strong></th>";
	        				}
	        			}
	        		}
	    		}else{//单表头处理
	        		tbHeadTh.each(function(i) {//遍历thead的tr下的th 
//	        			var thisIndex = tbHeadTh.index($(this));//获取th所在的列号
	        			//获取th属性值	
	        			var val = $(this).attr("fieldname");
	        			
	        			var maxlength = $(this).attr("maxlength");    
	        			var colspan = $(this).attr("colspan");//add by songxb@2013-08-07
	        			var strlength ;
	        			//文本位置和链接参数
	    				var align = $(this).attr("tdalign");
	    				var hasLink = $(this).attr("hasLink");
	    			     var linkfunction = $(this).attr("linkFunction");
	    			     if(linkfunction!=undefined&&hasLink=="true")
	    			     {
	    			     hasLink = true;
	    			     }else{
	    			     hasLink = false;
	    			     }
	    				 var CustomFunction = $(this).attr("CustomFunction");
	    				 var returnFunctionValue="";
	    				 if(CustomFunction!=undefined)
	    				 {
	    					  returnFunctionValue = eval(CustomFunction+"("+JSON.stringify(rowDataJsons.data[j])+")");
	    				 }
	    				 //是否相同内容列合并 begin
	    				 var rowMerge =$(this).attr("rowMerge");
	    				 if(rowMerge!=undefined&&rowMerge=="true")
	    			     {
	    					 rowMerge = true;
	    			     }else{
	    			    	 rowMerge = false;
	    			     }
	    				 //如果相同内容合并，判断上一行内容是否和当前行一致如果一致则不生成td标签，
	    				 //判断下一行内容是否和当前行一致，如果一致增加rowspan属性，并计算下面几行相同，得到rowspan值
	    				   var rowSpan_ = "";

	    				 if( rowMerge ==true){
	    				 //判断上一行内容，begin
	    				   if(j>0){
	    					 var preRow = rowDataJsons.data[j-1];
	    					 if(val != undefined){
	    						if(rowDataJsons.data[j][val]==preRow[val]){
	    							//showHTML +="<td></td>";
	    							return true;
	    						}
	    				  	 }
	    				   }
	    				 //判断上一行内容，end
	    				 //计算rowspan begin
	    				   var t = 0;
	    				   if(j<rowDataJsons.data.length-1){
	    				     for ( var ss = j+1; ss < rowDataJsons.data.length; ss++) {
	    				    	 if(rowDataJsons.data[j][val]==rowDataJsons.data[ss][val]){
	    				    		 t = t+1;
	    				    	 }else{
	    				    		 break;
	    				    	 }
	    				     }
	    				   }
	    				   if(t>0){
	    					   t = t+1;
	    					   rowSpan_ = " rowspan =\""+t+"\"";
	    					   //如果有列合并，显示长度为最大程度*合并多少列
	    					   if(maxlength!= undefined){
	    					    //  maxlength = maxlength*t;
	    					   }
	    				   }
	    		    	  }
	    				 //是否相同内容列合并 end
	    				 
	    			     //文本位置和链接参数
	        			if(val != undefined){
	        				var value_SV = rowDataJsons.data[j][val+"_SV"];
	        				if(value_SV!= undefined)
	        					{
	        						arres[j][i] = rowDataJsons.data[j][val+"_SV"];
	        						if(arres[j][i]!=null){
	        							arres[j][i] = dealSpecialCharactor(arres[j][i]);
	        							strlength = arres[j][i].length;
	        							if(maxlength!= undefined)
	        							{
	        								if(strlength > maxlength){//超过长度隐藏显示
	    										if(align != undefined){//位置处理
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
	    											}else{
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";	
	    											}
	    										}else{
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\" "+rowSpan_+"> <a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
	    											}else{
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\" "+rowSpan_+"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    											}
	    										}
	        									
	        								
	        								}else{
	    										if(align != undefined){//位置处理
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
	    											}else{
	    												if(returnFunctionValue&&returnFunctionValue!=""){
	  													  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
	  												    }else{
	  													  showHTML +="<td align="+align+" "+rowSpan_+">"+arres[j][i] +"</td>";
	  												    }
	    											}
	    										}else{
	    											if(hasLink){//有链接
	    												showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
	    											}else{
	    												if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
		  												    }else{
		  													  showHTML +="<td "+rowSpan_+">"+arres[j][i] +"</td>";
		  												    }

	    											}
	    										}
	        									
	        								}
	        							}else{
											if(align != undefined){//位置处理
												if(hasLink){//有链接
													showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
												}else{
    												if(returnFunctionValue&&returnFunctionValue!=""){
	  													  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
	  												    }else{
	  												    	 var temp  = dealSpecialCharactor(arres[j][i]);
	  													  showHTML +="<td align="+align+" "+rowSpan_+">"+temp +"</td>";
	  												    }

												}
											}else{
												if(hasLink){//有链接
													showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
												}else{
    												if(returnFunctionValue&&returnFunctionValue!=""){
	  													  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
	  												    }else{
	  												    var temp  = dealSpecialCharactor(arres[j][i]);
	  													  showHTML +="<td "+rowSpan_+">"+temp+"</td>";
	  												    }
												}
											}
	        								
	        							}
	        							
	        		    				  
	        		    				}
	        					}else{//无SV处理
	        						arres[j][i] = rowDataJsons.data[j][val];
	        						if(arres[j][i]!=null){
	        							arres[j][i] = dealSpecialCharactor(arres[j][i]);
	        							strlength = arres[j][i].length;
	        							//modfied by songxb@2013-08-07 begin
	        							if(maxlength!= undefined && strlength > maxlength)
	        							{
	        								//if(strlength > maxlength){//超过长度隐藏显示
	    									if(colspan!=undefined && colspan>1)
	    									{
	    										var temp = arres[j][i].split("||");//从后台取出出带"||"号分隔的字符串
	    										
	    										if(temp.length>0 && temp.length!=colspan)//后台数据无分隔符，则合并单元格
	    										{
	    											showHTML +="<td colspan=\""+colspan+"\" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    										}else
	    										{
	    											for(var z=0;z<colspan;z++)
	    											{
	    												
	    												if(temp[z].length > maxlength)
	    												{
	    													showHTML +="<td "+rowSpan_+"><abbr title=\""+temp[z]+"\"> "+temp[z].substring(0,maxlength) +"...</abbr></td>";		    													
	    												}else
	    												{
	    													showHTML +="<td "+rowSpan_+">"+temp[z] +"</td>";
	    												}
	    											}
	    											
	    										}

	    									}else{
	    										if(align != undefined){//位置处理
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
	    											}else{
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    											}
	    										}else{
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\"> "+arres[j][i].substring(0,maxlength) +"...</a></abbr></td>";
	    											}else{
	    												showHTML +="<td align="+align+" "+rowSpan_+"><abbr title=\""+arres[j][i]+"\" "+rowSpan_+"> "+arres[j][i].substring(0,maxlength) +"...</abbr></td>";
	    											}
	    										}
	    										
	    									}
	        									
	        							}else{//未定义maxlength属性
	    											    									
	    									if(colspan!=undefined && colspan>1)//已在表头中定义TH中定义了colspan属性
	    									{
	    										var temp = arres[j][i].split("||");//从后台取出出带"||"号分隔的字符串
	    										//alert(temp);
	    										//alert(temp.length);
	    										if(temp.length>0 && temp.length!=colspan)
	    										{
	    											showHTML +="<td colspan=\""+colspan+"\" "+rowSpan_+">"+arres[j][i] +"</td>";
	    										}else
	    										{
	    											for(var z=0;z<colspan;z++)
	    											{
	    												showHTML +="<td "+rowSpan_+">"+temp[z] +"</td>";//按"||"符号分隔数据分别插入到TD中
	    											}
	    											
	    										}

	    									}else{
	    										if(align != undefined){//位置处理
	    											if(hasLink){//有链接
	    												showHTML +="<td align="+align+" "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
	    											}else{
	    												if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td align="+align+" "+rowSpan_+">"+returnFunctionValue +"</td>";
		  												    }else{
		  													  showHTML +="<td align="+align+" "+rowSpan_+">"+arres[j][i] +"</td>";
		  												    }

	    											}
	    										}else{
	    											if(hasLink){//有链接
	    												showHTML +="<td "+rowSpan_+"><a href='javascript:void(0);' onClick=\""+linkfunction+"('"+j+"')\">"+arres[j][i] +"</a></td>";
	    											}else{
	    												if(returnFunctionValue&&returnFunctionValue!=""){
		  													  showHTML +="<td "+rowSpan_+">"+returnFunctionValue +"</td>";
		  												    }else{
		  													  showHTML +="<td "+rowSpan_+">"+arres[j][i] +"</td>";
		  												    }
	    											}
	    										}
	    										
	    									}
	        							}
	        		    			}
	        					}
	        			}else{
	        				//序号处理
	        				var Strxh = $(this).attr("id"); 
	        				if(Strxh != undefined && Strxh == "_XH"){
	        					showHTML+="<th align=center><strong>"+parseInt(j+1)+"</strong></th>";
	        				}
	        			}
	        		});	
	    		}
	    		showHTML  +="</tr>";
	    	}
		}
		//console.log("showHTML:"+showHTML);
    	//showHTML +="</tbody>";	
    	return showHTML;
    	
    },
    
    	
    //clearResult
    clearResult:function(){
        var $this = $(this);
        //alert($(".pageContent").html());
        $("tbody tr",$this).remove();
    },
    //
    getSelectedRowIndex:function(){
    	
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        for(var i=0;i<rows.size();i++){
        	if(rows.eq(i).attr("isSelect") == "1")
        		{
        		return i;
        		break;
        		}
        }
        return -1;
    },
    moveDown:function(index){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        if((index+1)>=rows.size()||index<0){
        	return;
        }    	

   		var trObj_next =  $this.getTrObjByIndex(index+1);
   		var trObj =  $this.getTrObjByIndex(index);
   		var trHtml = trObj[0].outerHTML;
   		trObj_next.after(trHtml);
   		trObj.remove();
   		$this.TableTdXhOrder();

    },
    moveUp:function(index){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        if(index<=0){
        	return;
        }    	
        var trObj =  $this.getTrObjByIndex(index);
        var trHtml = trObj[0].outerHTML;
        if(index==1){
        	$this.find("tbody").prepend(trHtml);
        }else{
        	var trObj_next =  $this.getTrObjByIndex(index-2);
        	trObj_next.after(trHtml);
        }
   		trObj.remove();
   		$this.TableTdXhOrder();

    },
    
    getSelectedRowsIndex:function(){
    	var indexarry = new Array();
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        var k = 0;
        for(var i=0;i<rows.size();i++){
        	if(rows.eq(i).attr("isSelect") == "1")
        		{
        			indexarry[k]=i;
        			k++;
        		}
        }
        return indexarry;
    },
    /*
     * 获得改变行的
     * 
     */
    getChangeRows:function(){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        var indexarry = new Array();
        var tempStr = "";
        for(var i=0;i<rows.size();i++){
        	var data = tabletrToJSON($(rows[i]));
        	var rowJson = $(rows[i]).attr("rowJson");
//        	alert(JSON.stringify(newrowJson));
        	var haschanged = judgeEditRowHasChanged(JSON.parse(rowJson),JSON.parse(data));
        	if(haschanged == true){
        		tempStr += i+",";
        	}
        }
    	if(tempStr.length >0)
		{
			tempStr = tempStr.substring(0, tempStr.length - 1);
			indexarry = tempStr.split(",");
		}
        return indexarry;
    },
    /*
     * 获得所有行的index
     * 
     */
    getAllRowsJOSNString:function(){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        var indexarry = new Array();
        var tempStr = "";
        for(var i=0;i<rows.size();i++){
        	var data = tabletrToJSON($(rows[i]));
        	var rowJson = $(rows[i]).attr("rowJson");
//        	alert(JSON.stringify(newrowJson));
        	//var haschanged = judgeEditRowHasChanged(JSON.parse(rowJson),JSON.parse(data));
        	tempStr += i+",";
        }
    	if(tempStr.length >0)
		{
			tempStr = tempStr.substring(0, tempStr.length - 1);
			indexarry = tempStr.split(",");
		}
        return indexarry;
    },
    
    getTabRowsToJsonArray:function(){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        var jsonarry = new Array(rows.size());
        var tempStr = "";
        for(var i=0;i<rows.size();i++){
        	var rowJson = $(rows[i]).attr("rowJson");
            jsonarry[i] = JSON.parse(rowJson);
        }

        return jsonarry;
    },
   
    
    getSelectedRows:function(){
    	var selectedJsonarry = new Array();
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        var k = 0;
        for(var i=0;i<rows.size();i++){
        	if(rows.eq(i).attr("isSelect") == "1")
        		{
        			selectedJsonarry[k] = rows.eq(i).attr("rowJson");
        			k++;
        		}
        }
        return selectedJsonarry;
    },
    
    getSelectedRowJsonObj:function(){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        for(var i=0;i<rows.size();i++){
        	if(rows.eq(i).attr("isSelect") == "1")
        		{
        		return JSON.parse(rows.eq(i).attr("rowJson"));
        		break;
        		}
        }
        return -1;
    },
    
    getSelectedRow:function(){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        for(var i=0;i<rows.size();i++){
        	if(rows.eq(i).attr("isSelect") == "1")
        		{
        		return rows.eq(i).attr("rowJson");
        		break;
        		}
        }
        return -1;
    },
    //获取行的rowjson字符串通过索引
    getSelectedRowJsonByIndex:function(index){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        for(var i=0;i<rows.size();i++){
        	if(i==index){
        		return rows.eq(i).attr("rowJson");
        		break;
        	}
        }
        return -1;
    },
    
    //获取行的rowjson对象通过索引
    getRowJsonObjByIndex:function(index){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        for(var i=0;i<rows.size();i++){
        	if(i==index){
        		return JSON.parse(rows.eq(i).attr("rowJson"));
        		break;
        	}
        }
        return -1;
    },
    //通过索引值获取选择的行记录
    getTrObjByIndex:function(index){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        for(var i=0;i<rows.size();i++){
        	if(i==index){
        		return rows.eq(i);
        		break;
        	}
        }
        return -1;
    },
    
    /*
     * //更新行rowJson属性通过表格行内容,
     * 返回每行修改后的rowJson对象
     */
    updateRowJsonByIndex:function(index,tharrays){
    	var $this = $(this);
    	var rowJson = $this.getSelectedRowJsonByIndex(index);
    	var selectedrow = $this.getTrObjByIndex(index);
    	var data = tabletrToJSON(selectedrow);
//    	alert(data);
    	var newrowJson = "";
    	
    	newrowJson = updateRowJsonByRowData(JSON.parse(rowJson),JSON.parse(data),tharrays);
//    	alert(JSON.stringify(newrowJson));
    	return newrowJson;//add by songxb
    },
    
    /*
     * //可编辑的列拼装成json,找到编辑列的主键拼装成新的编辑列的json
     * 返回带主键的json串
     */
    comprisesColumnsJsonByEditColumns:function(index){
    	var $this = $(this);
    	var rowJson = $this.getSelectedRowJsonByIndex(index);
    	var selectedrow = $this.getTrObjByIndex(index);
    	var data = tabletrToJSON(selectedrow);
//    	alert(data);
    	//拼装主键,返回
    	var newrowJson = "";
    	
    	newrowJson = comprisesColumnsJson(JSON.parse(rowJson),JSON.parse(data),$this);
//    	alert(JSON.stringify(newrowJson));
    	return newrowJson;
    	},
    	
    removeResult:function(index){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        var ths =  $("tbody th" ,$this);
        for(var i=0;i<rows.size();i++){
        	
        	var trobj = rows.eq(i);
		    var rowspan_index = 0;
    		if(i == index){
    		 //处理列合并样式 begin 
        	 trobj.find("td").each(function() {
        		 rowspan_index = rowspan_index +1;
				 var tdl = $(this);
				 var rowspanl =tdl.attr("rowspan");
				 if(rowspanl!=undefined&&rowspanl!=null)//存在rowspan情况 将当前行的html代码追加到下一列中，并设定rowspan-1
			     {
					 if((i+1)<rows.size()){
						var next_tr =  rows.eq(i+1);
						var tdl_html = tdl.html();
						if(rowspanl>1)
						next_tr.find("td").eq(rowspan_index-1).before("<td rowspan='"+(rowspanl-1)+"'>"+tdl_html+"</td>");
						 
					 }
			     }
        	 });
        	 if(index>0){
	     	    	var tbHead = $this.children('thead');//获取table对象下的thead 
	     	    	var tbHeadTh = tbHead.find('tr th');//获取thead下的tr下的th 
	                var rows = $("thead tr" ,$this);
	     	        var throwsize = rows.size();//表头行尺寸
	     	        //获得列对象 begin
	     	        var tharrays= new Array();
	     	        var k = 0;

	     	    	if(throwsize>=2){//多表头处理
	     				for(var t =0;t<tbHeadTh.size();t++)
	     				{	
	     				  if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
	     				  {
	     					 k++;
	     				  }
	     				}
	     			if(k>0){
	     				for(var arr=1;arr<=k;arr++)
	     				{
	     					for(var t =0;t<tbHeadTh.size();t++)
	     					{
	     						if(typeof(tbHeadTh.eq(t).attr("colindex")) != "undefined")
	     							{
	     								if(tbHeadTh.eq(t).attr("colindex") == arr){
	     									tharrays[arr-1] = tbHeadTh.eq(t);
	     									break;
	     								}
	     							}
	     					}
	     				}
	     			  }
	     			}else{
	     				for(var t =0;t<tbHeadTh.size();t++)
					{
	     					if(tbHeadTh.eq(t).attr("fieldname") == "string"){
	     						tharrays[t] = tbHeadTh.eq(t);
	     					}
					}
	     			}
	     	    	//获得列对象 end
	     	    	for(var k =0;k<tharrays.length;k++){
	     	    		if(typeof(tharrays[k].attr("rowMerge")) == "string"&&tharrays[k].attr("rowMerge")=="true"){
	     	    			var fieldname = tharrays[k].attr("fieldname");
	     	    			var now = $this.getRowJsonObjByIndex(index);
	     	    			var pre = $this.getRowJsonObjByIndex(index-1);
	     	    			if(now[fieldname]==pre[fieldname]){
	     	    				for(var j =index-1;j>=0;j--){
	     	    					var trobj_ = $this.getTrObjByIndex(j);
                                    var td_ = trobj_.find("td")[k-1];
                                    var rs = td_.getAttribute("rowspan");
                                    if(rs){
                                    	if(rs>2){
                                    		td_.setAttribute("rowspan",rs-1);
                                    	}else{
                                    		td_.removeAttribute("rowspan");
                                    	}
                                    	
                                    }

	     	    				}
	     	    				
	     	    			}
	     	    			
	     	    		}
	     	    		
	     	    	}
        	   }
        	//处理列合并样式 end
        	 trobj.remove();
    		}
        	
        	//if(trobj.attr("isSelect") == "1")
        		//{
        	//	trobj.remove();
        	//	}else{
        			//if(i == index){
        			//	trobj.remove();
        			//}
        	//	}
        }
        
        
        
        $this.TableTdXhOrder();
    },

    
    getSelectedRowText:function(){
    	
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        for(var i=0;i<rows.size();i++){
        	if(rows.eq(i).attr("isSelect") == "1")
        		{
        		return JSON.stringify(rows.eq(i).attr("rowJson"));
        		break;
        		}
        }
        return -1;
       },
       
    fnClickAddRowList:function(tablistID,strarr){
    	$("#"+tablistID.id).find("tbody").append(strarr);//增加tbody内容
    	//如果可编辑表格,初始化表格字典,为表格字典赋值
    	var tableEditable ;
        if(typeof($("#"+tablistID.id).attr("editable")) != "undefined")
        	{
        		tableEditable = $("#"+tablistID.id).attr("editable");
        	}else{
        		tableEditable = "0";
        	}
        
        if(tableEditable == "1"){
        	//生成表格中的字典
        	setStyle(tablistID,null);
        	//为tr rowJSON值赋给表格中对应的行
        	setRowsDefaultValue(tablistID);
        }
    	bindClickEvent(tablistID);//为表格绑定行选事件
    },
    
    cancleSelected:function(){
    	var $this = $(this);
        var rows = $("tbody tr" ,$this);
        //单击行时,设置所有行的属性为未选中
        for(var i=0;i<rows.size();i++){
        	rows.eq(i).attr("isSelect" ,"0");
	   		//取消选中样式
        	//如果行对象存在样式则删除样式      	
        	if(rows.eq(i).find('td').attr('class')=='tableActive')
        		{
        			rows.eq(i).find('td').removeClass('tableActive');
        		}
        	if(rows.eq(i).attr("class")=="tableActive"){
        		rows.eq(i).removeClass('tableActive');
        	}
	   		
        }
        
    },
    getTableRows:function(){
        var $this = $(this);
        var rows = $("tbody tr" ,$this);
        return rows.size();
    },
    
    setSelect:function(index)
    {
    	if(index<0){
    		return;
    	}
    	
        var $this = $(this);
        var rows = $("tbody tr" ,$this);
        $this.cancleSelected();
        
        if(index < rows.size() && index >= 0)
        {
        	//rows.eq(index).checked = true;
           // if($this.attr("rowselect")&& $this.attr("rowselect") == 'true')
                rows.eq(index).removeAttr("isSelect");
                rows.eq(index).attr("isSelect" ,"1");
            		rows.eq(index).addClass('tableActive');
        }
        else alertMsg.error('Exceeds the maximum number of records !');
    },
    getCurrentpagenum:function()
    {
    	 var id ="page_"+$(this).attr("id");
    	 var $formObj = $("#"+id).find("form");
    	 return $formObj.attr("currentpagenum");
    },
    sortTabListRowAsc:function(fieldname)//表格升序排序
    {
    	var $this = $(this);
    	var allRowJson = $this.getTabRowsToJsonArray();
    	var temp ;

    	for (var i = 0; i < allRowJson.length; i++)
    	{
    	  for (var j = 0; j < allRowJson.length - i; j++)
    	  {
    		var now = allRowJson[j];
    		var next = allRowJson[j + 1];
    		if(!next) break;
    	    if (now[fieldname] > next[fieldname])
    	    {
    	      temp = allRowJson[j + 1];
    	      allRowJson[j + 1] = allRowJson[j];
    	      allRowJson[j] = temp;
    	     }
    	   }
    	}
    	var rowJsonString = JSON.stringify(allRowJson);
    	rowJsonString ="{response:{data:"+rowJsonString+"}}";;
		$($this[0]).clearResult();
    	var strarr = $this.InsertTableRows(rowJsonString,$this[0]);
		//将返回信息插入datatable
		$($this[0]).fnClickAddRowList($this[0],strarr);
    },
    sortTabListRowDesc:function(fieldname)//表格升序排序
    {
    	var $this = $(this);
    	var allRowJson = $this.getTabRowsToJsonArray();
    	var temp ;

    	for (var i = 0; i < allRowJson.length; i++)
    	{
    	  for (var j = 0; j < allRowJson.length - i; j++)
    	  {
    		var now = allRowJson[j];
    		var next = allRowJson[j + 1];
    		if(!next) break;
    	    if (now[fieldname] < next[fieldname])
    	    {
    	      temp = allRowJson[j + 1];
    	      allRowJson[j + 1] = allRowJson[j];
    	      allRowJson[j] = temp;
    	     }
    	   }
    	}
    	var rowJsonString = JSON.stringify(allRowJson);
    	rowJsonString ="{response:{data:"+rowJsonString+"}}";;
		$($this[0]).clearResult();
    	var strarr = $this.InsertTableRows(rowJsonString,$this[0]);
		//将返回信息插入datatable
		$($this[0]).fnClickAddRowList($this[0],strarr);
    }
 
    //end
	});
	
	//
	function bindClickEvent(tablistID){
		//获取结果表类型
		
		var $thistab = $("#"+tablistID.id);
		var tabletype = $thistab.attr("type");
		if(tabletype== undefined){
			//tabletype =="single";
			$thistab.attr("type","single");
		}
		tabletype = $thistab.attr("type");
		if(tabletype!= undefined){
			if(tabletype == "single"){
				//var tabTab = $("#"+tablistID.id + " tbody").find('tr').bind({
				$("#"+tablistID.id + " tbody").find('tr').unbind("dblclick");//取消双击绑定事件 add by zhangbr@ccthanking.com
				var tabTab = $("#"+tablistID.id + " tbody").find('tr').bind({//只处理tbody下的tr modified by hongpeng.dong at 2014.3.26
					click:function(){
				   		var rowJson = $(this).attr("rowJson");

			    		var strrowJson=convertJson.string2json1(rowJson);
			    		//
			    		var $thistab = $("#"+tablistID.id);
			            var rows = $("tbody tr" ,$thistab);
			            //单击行时,设置所有行的属性为未选中
			            	rows.attr("isSelect" ,"0");
			            //当前未选中,则设置属性为选中
			   	     	if ($(this).attr("isSelect") == "0"){
			   	    		$(this).attr("isSelect" ,"1");
			   	    	}
				   	 	$('.tableActive').removeClass('tableActive');
				   		if (this.className=='tableActive') {
						
						}else{
							if (this.className!='tableActive') {
								$(this).addClass('tableActive');
							}
						}
			   			//	alert($(this).parent().html());
			    		tr_click(strrowJson,tablistID.id);
					},
					dblclick:function(){
						var rowJson = $(this).attr("rowJson");
			    		var strrowJson=convertJson.string2json1(rowJson);
			    		//
			    		var $thistab = $("#"+tablistID.id);
			            var rows = $("tbody tr" ,$thistab);
			            //单击行时,设置所有行的属性为未选中
			            	rows.attr("isSelect" ,"0");
			            //当前未选中,则设置属性为选中
			   	     	if ($(this).attr("isSelect") == "0"){
			   	    		$(this).attr("isSelect" ,"1");
			   	    	}
				   	 	$('.tableActive').removeClass('tableActive');
				   		if (this.className=='tableActive') {
						
						}else{
					   	     if (this.className!='tableActive') {
								$(this).addClass('tableActive');
							}
						}
			   			//增加tr_dbclick方法的异常处理
			   			try{
			   				tr_dbclick(strrowJson,tablistID.id);
			   			}catch(e){
			   			
			   			}
					}
				});
				
			}else if(tabletype == "multi"){
				var tabTab = $("#"+tablistID.id).find('tr').bind({
					click:function(){
			    		//
			    		var $thistab = $("#"+tablistID.id);
			            var rows = $("tbody tr" ,$thistab);
			            //单击行时,设置所有行的属性为未选中
			   	     	if ($(this).attr("isSelect") == "0")
			   	    	 {
			   	     		$(this).removeAttr("isSelect");
			   	    	 	$(this).attr("isSelect" ,"1");
			   	    	 	if($(this).attr("class") == undefined){
			   	    	 		$(this).addClass('tableActive');
			   	    	 	}
			   	    	 }else{
			   	    		 if ($(this).attr("isSelect") == "1")
				   	    	 {
				   	     		$(this).removeAttr("isSelect");
				   	    	 	$(this).attr("isSelect" ,"0");
				   	    	 if($(this).attr("class") =='tableActive'){
					   	     		$(this).removeAttr("class"); 
				   	    	 }
				   	    	 }
			   	    	 }
			   	    		
			   	     	//alert($(this).parent().html());
			   	     	
					},
					dblclick:function(){
						
			    		var $thistab = $("#"+tablistID.id);
			            var rows = $("tbody tr" ,$thistab);
			            //单击行时,设置所有行的属性为未选中
			            
			   	     	if ($(this).attr("isSelect") == "0")
			   	    	 {
			   	     		$(this).removeAttr("isSelect");
			   	    	 	$(this).attr("isSelect" ,"1");
			   	    	 		
			   	    	 	if($(this).attr("class") == undefined){
			   	    	 		$(this).addClass('tableActive');
			   	    	 	}
			   	    	 }else{
			   	    		 if ($(this).attr("isSelect") == "1")
				   	    	 {
				   	     		$(this).removeAttr("isSelect");
				   	    	 	$(this).attr("isSelect" ,"0");
				   	    	 	
				   	    	 if($(this).attr("class") =='tableActive'){
					   	     		$(this).removeAttr("class"); 
				   	    	 }
				   	    	 }
			   	    	 }
					}
				});
			}
		}
    };
    
    
    function setStyle(sObj,rowIndex)
    {

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
       
//                    $this.bind("dblclick",function(event){
//                    });
//                    
//                    $this.bind("blur",function(event){
//                    });
                    if(muss)
                    {
                            var jSpan = $("<span style='font-size:12px;color:red;width:10px; padding-left:2px;height:20px'>*</span>");
                            $this.after(jSpan);
                    }
                    //设置字典
                    if($this.get(0).tagName=="SELECT"&&$this.attr("kind") && $this.attr("kind")=="dic")
                    {
                    	var noNullSelect = false;
                    	if($this.attr("noNullSelect")&&$this.attr("noNullSelect")=="true")
                        {
                        	noNullSelect = true;
                        }
                    	if ($this.attr("src").substr(0,2).toUpperCase() == "T#"){
                    		var vparam = $this.attr("src").substr(2); //去掉T#
                            while(vparam.indexOf("%")>0){
                            	vparam = vparam.replace("%","$");
                            }
                            if(window.ActiveXObejct){
                                request = new ActiveXObject("Microsoft.XMLHTTP");
                             }
                             else if(window.XMLHttpRequest){
                                request = new XMLHttpRequest();
                             }
                                var path =  g_sAppName + "/servlet/GetDicFromTable?param=" + vparam + "&r="+Math.random();
                                $.ajax({
                            		async :	false,
                            		url : path,  
                            		success : function(response,config) {
                            			var parser = new DOMParser(); 
                            			var dic_dom = parser.parseFromString(response, "text/xml"); 
                                    	if(dic_dom){
                                    	   if(noNullSelect == false)
                                     	   $this.append("<option>请选择</option>");
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
            		    	   $this.append("<option></option>");
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
                        	   $this.append("<option>请选择</option>");
                            var list = dic_dom.getElementsByTagName("R");
                            for(var i = 0;i<list.length;i++){
                            	 var code = list[i].getAttribute("c");
                                 var value = list[i].getAttribute("t");
                                 $this.append("<option value='"+code+"'>"+value+"</option>");
                            }
                           }
                         }
                    	
                    	
                    }else{
                        $this.bind("focus",function(event){
                            if($this.attr("id") == "dicFilterEdit")
                                return;
                           // if(!$this.attr("kind") || $this.attr("kind") != "dic")
                            //    g_xDic.hidden();        
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
//            $d.validation();//设置表单验证监听
           // addListener(sId);
        }
    };
    
    /*
     * 为表格的每行设置默认值
     * tabobj:表格对象
     */
    function setRowsDefaultValue(tabobj){
    	//取出行对象,循环赋值
		var $thistab = $("#"+tabobj.id);
        var rows = $("tbody tr" ,$thistab);
        //赋值
        for(var i=0;i<rows.size();i++){
        	
        	var	rowobj = rows.eq(i);
        	var rowJsonobj = convertJson.string2json1(rows.eq(i).attr("rowJson"));
        	// 调用行赋值方法
        	setValueByJson(rowJsonobj,rowobj);
        }
    }
 
    /*
     * 为表格的特定行设置值
     * tabobj:表格对象
     */
    function setRowDefaultValueByIndex(tabobj,index){
    	//取出行对象,循环赋值
		var $thistab = $("#"+tabobj.id);
        var rows = $("tbody tr" ,$thistab);
        //赋值
        for(var i=0;i<rows.size();i++){
        	if(i==index){
        		var	rowobj = rows.eq(i);
            	var rowJsonobj = convertJson.string2json1(rows.eq(i).attr("rowJson"));
            	// 调用行赋值方法
            	setValueByJson(rowJsonobj,rowobj);
            	break;
        	}
        }
    }
    
    /*
     * 单行表格赋值方法
     * //obj:rowJSON json对象; formobj:表格行对象
     */
   
    function setValueByJson(obj,formobj){
    //var setValueByJson = function(obj,formobj) {
		if (!obj) {
			return false;
		}

		$('input,select, textarea',formobj).each(function() {
					var el = $(this);
					var fieldname = el.attr("fieldname");
					if (fieldname) {
						for ( var key in obj) {
							if (el.is("input")) {
								if (el.attr("type") == 'text'|| el.attr("type") == 'hidden'||el.attr("type") == 'number') {
									if(fieldname.toUpperCase()==key)
									{

										if(obj[key+"_SV"])
										{
											el[0].value=obj[key+"_SV"];
											el.attr("code",obj[key]);
										}else{
											el[0].value=obj[key];
										}
									}

								} else if (el.attr("type") == 'date') {// 日期
									if(fieldname.toUpperCase()==key)
									{

										if(obj[key+"_SV"])
										{
											el[0].value=obj[key+"_SV"];
											el.attr("code",obj[key]);
										}else{
											el[0].value=obj[key];
										}
									}
								}
							}
							if (el.is("select")) {// 字典
								if(fieldname.toUpperCase()==key){
									var t =0;
									el.find("option").each(function() {
										
									  if(this.value == obj[key])
									  {
										  return false;
									  } 
									  t++;
									});
									if(t)
									el.get(0).selectedIndex=t;  
								}
							}if (el.is("textarea")) {
								if(fieldname.toUpperCase()==key)
								{
									el[0].value=obj[key];
								}
							}
						}
					}

				});

    };
    
    /* 
     * 将表单中的行对象转换为json串
     * trobj 行对象
     */
    function tabletrToJSON(trobj){
        var json = '{';
    	var e,name,type;
    	var tmpstr;
    	// 循环处理tr的元素
        	var trtds = trobj.find('td');//获取当前行的所有TD元素
        	for(var i=0;i < trtds.size();i++)
        		{
        			var trtd = trtds.eq(i);//获取每个tr的td元素
        			//获取未编辑的表格json串
/*        			var varname = $(this).attr("fieldname");
        			var thtdtype = trtd.attr("type");
        			if(varname != undefined){
        			if(thtdtype == undefined){
        				//获取
        	
        			alert("varname:"+varname);
        			var varvalue = $(this).val();
        			alert("varvalue:"+varvalue);
        			json += '"' + varname + '":"' + varvalue + '",';
        			}
        			}*/
        				
        			//获取已经编辑的表格的json
        			$('input,select',trtd).each(function(j) {
        				//获取存在input,select元素的td处理
        				 e = $(this);
        				 name = e.attr("fieldname");
        				 type = e.attr("type");
        				 switch (type) {
     	    			case 'hidden':
     	    			case 'password':
     	    			case 'text':
     	    			case 'textarea':
     	    				tmpstr = e.val();
     	    				if (tmpstr != null ){  // 空值不处理
     	    			      if($(e).attr("code"))
     	    			    	  {
     	    			    	      json += '"' + name + '":"' + $(e).attr("code") + '",';
     	    			    	  }else{
     	    			    		  json += '"' + name + '":"' + tmpstr + '",';
     	    			    	  }
     	    					 
     	    				}
     	    				break;
     	    			case 'number':
     	    				tmpstr = e.val();
     	    				if (tmpstr != null ){  // 空值不处理
     	    			         json += '"' + name + '":"' + tmpstr + '",';
     	    				}
     	    				break;
     	    			case 'date':
     	    				tmpstr = e.val();
     	    				if (tmpstr != null ){  // 空值不处理
       	    			    		  json += '"' + name + '":"' + tmpstr + '",';
       	    				}
     	    				break;
     	    			case 'select-one':
     	    				tmpstr = getTdSelectResult(e);

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
     	    				break;
        				 }
        			});
        		}
        return json.substring(0, json.length - 1) + '}'; // 返回json字符串
    };
    
    /**
    * 取得SELECT的值组成的json字符串
    * 传入SELECT对象
    * 输出由SELECT元素名称及其被选取的选项的值组成的json字符串
    *     形如：name:["v1","v2","v3]
    */
    function  getTdSelectResult(oS){
    	var eName=oS.attr("fieldname");
    	var eValue="";
//    	alert(oS.find('option:selected').text());
    	/*
    	 * sjl
    	 * 解决行编辑下拉框取值
    	 */
    	eValue += ',"'+oS.find('option:selected').val()+'"';
    	if (eValue != "")
    	{
    		eValue = '"'+eName+'":'+ eValue.substr(1)+"";
    	}
    	return eValue;
    };
    /*
     * jsonobj:处理的json对象
     * element：元素名称
     * attribute：元素内容
     */
    function addjson(jsonobj,element,attribute){
    	
    	var jsonString= JSON.stringify(jsonobj);
    	
    		jsonString = jsonString.substring(0,jsonString.length-1);
    		
    	var jsonArr=",\""+element+"\":\""+attribute+"\"}";
    	
    		jsonString=jsonString.concat(jsonArr);
    		
    		return jsonString;
    };
    
    /*
     * 更新行记录的rowJson值通过修改过的行内容组成的JSon串,tharry的属性名称一定在rowJsonObj中存在
     * params：
     * rowJsonObj：行记录的rowJson属性的对象
     * rowDataObj：行内容组成的Json串对象
     * tharry:表格的表头字段数组
     * return:
     * 修改后的rowJson对象
     */
    function updateRowJsonByRowData(rowJsonObj,rowDataObj,tharrays){
//    	alert(tharrays);
		for(var i=0;i<tharrays.length;i++){
//			alert("thName:"+thName);
			//获取属性名称
			var thName = tharrays[i];
			//从rowDataObj对象获取行记录修改后的值
			var tdNewValue = rowDataObj[thName];
			//rowJSon对象赋予新值
			rowJsonObj[thName] = tdNewValue;
			
		}
//		alert("rowJsonObj:"+JSON.stringify(rowJsonObj));
    	return rowJsonObj;
    };
    
    /*
     * 将行记录的rowjson与修改表的主键拼接成心的rowjson（目前可编辑表格只能属于1张业务表）
     * params：
     * rowJsonObj：行记录的rowJson属性的对象
     * rowDataObj：行内容组成的Json串对象
     * $this:表格对象
     * return:
     * 修改后的rowJson串
     */
    function comprisesColumnsJson(rowJsonObj,rowDataObj,$this){
    	//获取编辑表格的主键toUpperCase()
    	var edittablePK = $this.attr("edittablePK");
    	var pkvalue,newrowJsonstr="";
    	if(edittablePK != null){
    		edittablePK = edittablePK.toUpperCase();
    		 pkvalue = rowJsonObj[edittablePK];
    		 newrowJsonstr =  addjson(rowDataObj,edittablePK,pkvalue);
    	}
    	return newrowJsonstr;
    };
    /*
     * 更新行记录的rowJson值通过修改过的行内容从服务器返回后组成的JSon串
     * params：
     * rowJsonObj：行记录的rowJson属性的对象
     * ReturndataObj：返回行内容组成的Json串对象
     * return:
     * 修改后的rowJson对象
     */
    function updateRowJsonByReturnData(rowJsonObj,ReturndataObj){
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
		for ( var jsonKey in rowJsonObj) {
			if(rowJsonObj[jsonKey+"_SV"]){
				if(rowJsonObj[jsonKey]==""){
					rowJsonObj[jsonKey+"_SV"]="";
				}
			}	
				
		}	
//		alert("rowJsonObj:"+JSON.stringify(rowJsonObj));
    	return rowJsonObj;
    };
    /*
     * 判断行记录是否被修改过
     * params：
     * rowJsonObj：行记录的rowJson属性的对象
     * ReturndataObj：返回行内容组成的Json串对象
     * return:
     * ture false
     */
    function judgeEditRowHasChanged(rowJsonObj,editjsonObj){

	    var haschanged = false;
//		for(var i=0;i<tharrays.length;i++){
			for ( var returnkey in editjsonObj) {
				//alert("returnkey:"+returnkey);

				for ( var jsonKey in rowJsonObj) {
					if( returnkey == jsonKey){
						if(rowJsonObj[jsonKey] != editjsonObj[returnkey]){
							haschanged = true;
							break;
						}
					}
				}
		}
//		alert("rowJsonObj:"+JSON.stringify(rowJsonObj));
    	return haschanged;
    };
    
    function dealSpecialCharactor(v){
    	v=v.toString().replace(new RegExp("\b","g")," ");
    	v=v.toString().replace(new RegExp("&gt;","g"),">");
    	v=v.toString().replace(new RegExp("&lt;","g"),"<");
    	    return v;
    	};
    
    function dealInputSpecialCharactor(v){
        	v=v.toString().replace(new RegExp(" ","g"),"\b");
        	v=v.toString().replace(new RegExp(">","g"),"&gt;");
        	v=v.toString().replace(new RegExp("<","g"),"&lt;");
        	    return v;
        	};
        	
    
        	
})(jQuery);