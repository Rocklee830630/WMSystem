!function($) {
    var obj;
    $.fn.validation = function(options) {
        return this.each(function() {
            globalOptions = $.extend({}, $.fn.validation.defaults, options);
            obj=this;
            reg(obj);
            validationForm(obj);
        });
    };
    $.fn.validationField = function(options) {
        return this.each(function() {
            globalOptions = $.extend({}, $.fn.validation.defaults, options);
            obj=this;
            reg(obj);
        });
    };
    $.fn.validationButton = function(options){
    	  this.each(function() {
    	  globalOptions = $.extend({}, $.fn.validation.defaults, options);
          obj=this;
    	  });
        return  validationButton(obj);
      
    };
    $.fn.setFormValues = function(obj){
    	formobj=this;
  
    	setValueByJson(obj,formobj);
    };
    $.fn.setFormValuesByText = function(jsontext){
    	var obj = convertJson.string2json1(jsontext);
    	formobj=this;
    	setValueByJson(obj,formobj);
    };
    $.fn.clearFormResult = function(){
    	obj=this;
    	clearForm(obj);
    };
    
    $.fn.validation.defaults = {
        validRules : [
            {name: 'required', validate: function(value) {return ($.trim(value) == '');}, defaultMsg: '请输入内容。'},
            {name: 'number', validate: function(value) {
            	if(value==''){
            		return false;
            	}else if(value.length>30){
            		return true;
            	}else{
            	 return (!/^-?[0-9]\d*$/.test(value));
                }
            	
            }, defaultMsg: '请输入数字。'},
            {name: 'mail', validate: function(value) {return (!/^[a-zA-Z0-9]{1}([\._a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+){1,3}$/.test(value));}, defaultMsg: '请输入正确的邮箱地址。'},
            {name: 'char', validate: function(value) {
            	if(value==''){
            		return false;
            	}else{
            	 return (!/^[a-z\_\-A-Z]*$/.test(value));
            	}
            }, defaultMsg: '请输入英文字符。'},
            {name: 'chinese', validate: function(value) {
            	if(value==''){
            		return false;
            	}else{
            	 return (!/^[\u4e00-\u9fff]$/.test(value));
            	}
            
            }, defaultMsg: '请输入汉字。'},
            {name: 'maxlength', validate: function(value,field) {
            	if(value==''){
            		return false;
            	}else{
            		var maxlength = field.attr("maxlength");
            		if(maxlength){
            			 var inputlength = fucCheckLength(value,"UTF");
            			 if(inputlength>maxlength){
            				 return true;
            			 }
            		}
            			 return false;

            	}
            
            }, defaultMsg: '输入的内容超过最大长度。'}
        ]
    };
	var fucCheckLength = function(strTemp,charset) 	{ 
	  var i,sum; 
	   sum=0; 
	  var charlength = 1;
	  var chineselength = 2;
	  switch (charset){
	     case 'GBK':
	    	 chineselength=2;
	     case 'UTF':
	    	 chineselength=3;

	  }
	   for(i=0;i<strTemp.length;i++) 
	   { 
	      if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=255)) 
	         sum=sum+charlength; 
	      else 
	      sum=sum+chineselength; 
	   } 
	      return sum; 
	  }; 


    var formState = false, fieldState = false, wFocus = false, globalOptions = {};

    var validateField = function(field, valid) { // 验证字段
        var el = $(field), error = false, errorMsg = '',

        
        crule=(el.attr('check-el')==undefined)?null:el.attr('check-el').split(' '),
        msg = (el.attr('check-err')==undefined)?null:el.attr('check-err').split(' ');
        if(el.attr("tagName") =="SELECT"){
        	
           if(el.val()=='')
           {	
        	    error = true;
                errorMsg ="请至少选择一项内容";
           }
        
        }else {
          if(crule){
            if( ! eval(crule[0]).test(el.val()) ) {
            
                error = true;
                errorMsg =msg;
            }
            
          } else {
            for (i = 0; (i < valid.length) ; i++) {
                var x = true, flag = valid[i];

                if (flag.substr(0, 1) == '!') {
                    x = false;
                    flag = flag.substr(1, flag.length - 1);
                }
                var rules = globalOptions.validRules;
                for (j = 0; j < rules.length; j++) {
                    var rule = rules[j];
                    if (flag == rule.name) {
                    
                        if (rule.validate.call(field, el.val(),el) == x) {
                            error = true;
                            errorMsg = (msg == null)?rule.defaultMsg:msg;
                            break;
                        }
                    }
                }

                if (error) {break;}
            }
        }
       }
        var controls = el.parent().find('a');
        var len=controls.length;

        if (error) {
        	el.attr("title", errorMsg);
        	el.css("border","1px solid red");
            /* var cls= (el.attr('check-class')==undefined)?null:el.attr('check-class').split(' ');
             if ( len<=0) {      
            	//el.attr("title", errorMsg);
                el.after('  <a  style=" visibility:hidden;" data-placement="bottom"  data-content="'+errorMsg+ '"  data-toggle="popover" href="#" >aa</a> ');
             }
             el.next().popover("show");  
             var pop=el.parent().find(".popover"),pos=pop.offset();
             pos.top=pos.top-el.next().height();
             pop.offset(pos);          
             
             if ( cls  ) pop.addClass("checkclass");*/
          
        } else {
        	el.attr("title", "");
        	el.css("border","1px solid #cccccc");
        }
        return !error;
    };

    var reg=function(obj){
        $('input,select, textarea').each(function() {
            var el = $(this), valid = (el.attr('check-type')==undefined)?null:el.attr('check-type').split(' '),
            valid1 = (el.attr('check-el')==undefined)?null:el.attr('check-el').split(' ');
            if (valid != null && valid.length > 0   || valid1 != null && valid1.length > 0 ) {                       
                el.blur(function() { // 失去焦点时
                    validateField(this, valid);
                });
            }
        });
    };
    var validationButton = function(obj) { // 按钮点击验证方法
            var validationError = false;
            $('input,select, textarea', obj).each(function () {
                var el = $(this), valid = (el.attr('check-type')==undefined)?null:el.attr('check-type').split(' '),
                valid1 = (el.attr('check-el')==undefined)?null:el.attr('check-el').split(' ');
                if (valid != null && valid.length > 0   || valid1 != null && valid1.length > 0 ) {    
                    if (!validateField(this, valid)) {
                        validationError = true;
                    }
                }
            });
            if (validationError) {
                return false;
            }
            return true;


    };
    
   var  clearForm= function(obj) {
       $('input,select, textarea', obj).each(function () {
           var el = $(this);
           var fieldname = $(this).attr("fieldname");
           var canClear = true;
           var keep = $(this).attr("keep");
            if(keep!=undefined&&keep=="true"){
            	canClear = false;
            }
           if(fieldname != undefined&&canClear==true){
            if (el.is("input")||el.is("textarea")) {
            	if(el.attr("type")=="checkbox"||el.attr("type")=="radio"){
            		var fieldname = el.attr("name");
            		 $("input[name='"+fieldname+"']").each(function(){
            			   $(this).attr("checked",false);
            		 });  
            	}else{
            		el[0].value="";
                	//BEGIN add by zhangbr@ccthanking.com
                	if(el.attr("code")!= undefined){
                		el.removeAttr("code");
                	}
                	//END add by zhangbr@ccthanking.com
            	}
            	
            }
            if (el.is("select")) {
        	   el.get(0).selectedIndex=0;  
            }
           }
       });


         
   };
    var validationForm = function(obj) { // 表单验证方法
        $(obj).submit(function() { // 提交时验证
            if (formState) { // 重复提交则返回
                return false;
            }
            formState = true;
            var validationError = false;
            $('input,select, textarea', this).each(function () {
                var el = $(this), valid = (el.attr('check-type')==undefined)?null:el.attr('check-type').split(' ');
                if (valid != null && valid.length > 1) {
                    if (!validateField(this, valid)) {
                        if (wFocus == false) {
                            scrollTo(0, el[0].offsetTop - 50);
                            wFocus = true;
                        }
                        validationError = true;
                    }
                }
            });

            wFocus = false;
            fieldState = true;

            if (validationError) {
                formState = false; 
                return false;
            }

            return true;
        });


    };
        var setFieldByObj =function(field,obj) {
        	var el = field;
        	var rs;
        	//alert(el)
        	var fieldname = el.attr("fieldname");
			if (fieldname) {
				for ( var key in obj) {
					if (el.is("input")) {
					  if (el.attr("type") == 'text'|| el.attr("type") == 'hidden'||el.attr("type") == 'month'||el.attr("type")=='number') {	
						  
						if (el.attr("type") == 'hidden') {
							if (fieldname.toUpperCase() == key){
								rs = dealSpecialCharactor(obj[key]);
								el[0].value = rs;
							}
						}else if (el.attr("type") == 'number') {
							if (fieldname.toUpperCase() == key){
								rs = dealSpecialCharactor(obj[key]);
								el[0].value = rs;
							}
						} else {
							if (fieldname.toUpperCase() == key) {

								if (obj[key + "_SV"]) {
									el[0].value = obj[key + "_SV"];
									el.attr("code", obj[key]);
								} else {
									rs = dealSpecialCharactor(obj[key]);
									el[0].value = rs;
								}
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
						if (el.attr("type") == 'radio'){
							if(fieldname.toUpperCase()==key)
							{
								if(obj[key]!=""){
									var op = $("input[name="+el.attr("name")+"][value="+obj[key]+"]");
									if(op.length>0)
									$("input[name="+el.attr("name")+"][value="+obj[key]+"]")[0].checked=true;
								}else{
									
									$("input[name="+el.attr("name")+"]").each(function(){
										this.checked = false;
									});
								}
							}
						}
						if (el.attr("type") == 'checkbox'){
							if(fieldname.toUpperCase()==key)
							{
								if(obj[key]!=""){
									var s=obj[key].split(",");
									for(var i=0;i<s.length;i++){
										var op = $("input[name="+el.attr("name")+"][value="+s[i]+"]");
									//	alert(op[0].nextSibling.nodeValue)
										if(op.length>0)
										$("input[name="+el.attr("name")+"][value="+s[i]+"]")[0].checked=true;
									}
								}else{
									
									$("input[name="+el.attr("name")+"]").each(function(){
										this.checked = false;
									});
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
							if(t>0)
							el.get(0).selectedIndex=t;  
							else
							el.get(0).options[0].selected=true;  
						}
					}if (el.is("textarea")) {
						if(fieldname.toUpperCase()==key)
						{
							 rs	= dealSpecialCharactor(obj[key]);
							el[0].value=rs;
						}
					}
				}
			}
        };
        

        var dealSpecialCharactor = function(v){
        	v=v.toString().replace(new RegExp('([\b])', 'g')," ");
        	    return v;
        	};
        	
	    var setValueByJson = function(obj,formobj) {
		if (!obj||!formobj) {
			return false;
		}
		$('input,select, textarea',formobj).each(function() {
			setFieldByObj($(this),obj);
					

		});
		var setValueJsonByArr = function (obj,targetArr) {
			if (!obj || !targetArr) {
				return false;
			}
			for ( var i = 0; i < targetArr.length; i++) {
				setFieldByObj($(document.getElementById(targetArr[i])),obj);
				
			}
		};


    };

}(window.jQuery);
