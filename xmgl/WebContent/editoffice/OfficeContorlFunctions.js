
var OFFICE_CONTROL_OBJ;//控件对象
var IsFileOpened=true;      //控件是否打开文档
var fileType ;
var fileTypeSimple;
function intializePage(fileUrl)
{
	OFFICE_CONTROL_OBJ = document.getElementById("TANGER_OCX");
	//OFFICE_CONTROL_OBJ.AddDocTypePlugin(".pdf","PDF.NtkoDocument","4.0.0.2","ntkopdfdoc.cab",51,true);
	initCustomMenus();
	//objside();
	//fileUrl = encodeURI(fileUrl);
	//alert(fileUrl);
	//OFFICE_CONTROL_OBJ.openlocalfile("d:\\ntko.doc");
//NTKO_OCX_OpenDoc("http://192.168.1.160:8080/ntkodemo/"+fileUrl);
	NTKO_OCX_OpenDoc(fileUrl);
}
window.onbeforeunload=onPageClose;
function onPageClose(){
	if(!OFFICE_CONTROL_OBJ.activeDocument.saved)
	{
		if(confirm( "文档修改过,还没有保存,是否需要保存?"))
		{
			saveFileToUrl();
		
			testntko();
		
		}
	}
}

function testntko(){
	OFFICE_CONTROL_OBJ.SaveAsOtherFormatFile(3,"d:\\ntko1.rtf");
}
function NTKO_OCX_OpenDoc(fileUrl)
{
	//var url=encodeURI(fileUrl);
	//alert("cbltest:"+fileUrl);
	OFFICE_CONTROL_OBJ.BeginOpenFromURL(fileUrl);
}
function objside()
{
	document.all.officecontrol.style.position="absolute";
	document.all.officecontrol.style.left="0px";
	document.all.officecontrol.style.top="0px";
	document.all.officecontrol.style.width=window.screen.availWidth-20;//网页可见区域宽
	document.all.officecontrol.style.height=window.screen.availHeight-30;//网页可见区域高availHeight  scrollHeight
}
function setFileOpenedOrClosed(bool)
{
	IsFileOpened = bool;
	fileType = OFFICE_CONTROL_OBJ.DocType ;
}
function trim(str)




{ //删除左右两端的空格
return str.replace(/(^\s*)|(\s*$)/g, "");
}

function doOnsave()
{
	//alert("啊啊啊啊啊");
}
function check(){
	//alert("a");
	OFFICE_CONTROL_OBJ.VerifyDigitalSignature();
	//alert("b");
}

function saveFileToUrl()
{
	//OFFICE_CONTROL_OBJ.AddDigitalSignature();
	var myUrl =document.forms[0].action ;
	//alert("myUrl:"+myUrl);
	//alert("IsFileOpened:"+IsFileOpened);
	//alert("OFFICE_CONTROL_OBJ.doctype:"+OFFICE_CONTROL_OBJ.doctype);
	var fileName =document.getElementById("filename").value;// document.all("fileName").value;
	var result  ;
	if(IsFileOpened)
	{
		switch (OFFICE_CONTROL_OBJ.doctype)
		{
			case 1:
				fileType = "Word.Document";
				break;
			case 2:
				fileType = "Excel.Sheet";
				break;
			case 3:
				fileType = "PowerPoint.Show";
				break;
			case 4:
				fileType = "Visio.Drawing";
				break;
			case 5:
				fileType = "MSProject.Project";
				break;
			case 6:
				fileType = "WPS Doc";
				break;
			case 7:
				fileType = "Kingsoft Sheet";
				break;
			default :
				fileType = "unkownfiletype";
		}
	
		result = OFFICE_CONTROL_OBJ.savetourl(myUrl,//提交到的url地址
		"upLoadFile",//文件域的id，类似<input type=file id=upLoadFile 中的id
		"fileType="+fileType,           //与控件一起提交的参数如："p1=a&p2=b&p3=c"
		fileName,    //上传文件的名称，类似<input type=file 的value
		0 ,
		false//与控件一起提交的表单id，也可以是form的序列号，这里应该是0.
		);
		//alert("cbltest:"+result);
		
		
		
		
		
		
		
		
	
		//result=trim(result);
		//document.all("statusBar").innerHTML="服务器返回信息:"+result;
		//alert(result);
		//window.close();
	}
}
function addpicfromurl(){
	OFFICE_CONTROL_OBJ.addpicfromlocal("name",true,true,10,0,1,100,1);
}
function saveFileAsHtmlToUrl()
{
	var myUrl =document.forms[0].action ;
	//var myUrl = "upLoadHtmlFile.jsp"	;
	var htmlFileName = document.getElementById("filename").value+".mht";
	//alert(htmlFileName);
	var result;
	if(IsFileOpened)
	{
		//OFFICE_CONTROL_OBJ.ActiveDocument.WebOptions.Encoding = 65001;
			result = OFFICE_CONTROL_OBJ.saveasotherformattourl(1,myUrl,//提交到的url地址
				"upLoadFile",//文件域的id，类似<input type=file id=upLoadFile 中的id
				"ntko="+"ntkontko",          //与控件一起提交的参数如："p1=a&p2=b&p3=c"
				htmlFileName,    //上传文件的名称，类似<input type=file 的value
				0 ,
				false//与控件一起提交的表单id，也可以是form的序列号，这里应该是0.
				);
		//result=OFFICE_CONTROL_OBJ.PublishAsHTMLToURL("upLoadHtmlFile.jsp","uploadHtml","htmlFileName="+htmlFileName,htmlFileName);
		result=trim(result);
		document.all("statusBar").innerHTML="服务器返回信息:"+result;
		//alert(result);
		//window.close();
	}
}
function saveFileAsPdfToUrl()
{
	var myUrl = "upLoadPdfFile.jsp"	;
	var pdfFileName = document.getElementById("filename").value+".pdf";
	if(IsFileOpened)
	{
		OFFICE_CONTROL_OBJ.PublishAsPdfToURL(myUrl,"uploadPdf","ntko="+"nkto/ntko",pdfFileName,"","",true,false);
	}
}
function saveasother2()
{
	var myUrl = "uploadOther.jsp";
	//var otherFileName = document.getElementByID("fileName").value+".doc";
	//alert(otherFileName);
	OFFICE_CONTROL_OBJ.SaveAsOtherFormatToUrl(5,myUrl,"uploadOther","","otherFileName",0);
}
function testFunction()
{
	//alert(IsFileOpened);
}
function webget(){
	var result=OFFICE_CONTROL_OBJ.DoWebGet("webget.jsp");
	//alert(result);
}
function doexecute()
{
	var result=OFFICE_CONTROL_OBJ.DoWebExecute("execute.jsp","id=0");
	//alert(result);
}
function doexecute2()
{
	var result=OFFICE_CONTROL_OBJ.DoWebExecute2("execute2.jsp","id=0");
	//alert(result);
}
function addheader2()
{	
	//alert("in");
	OFFICE_CONTROL_OBJ.Addhttpheader("webget:ntko");
	//alert("out");
}
function addServerSecSign()
{
	var signUrl=document.all("secSignFileUrl").options[document.all("secSignFileUrl").selectedIndex].value;
	if(IsFileOpened)
	{
		if(OFFICE_CONTROL_OBJ.doctype==1||OFFICE_CONTROL_OBJ.doctype==2)
		{
			try
			{
			alert("正式版本用户请插入EKEY！\r\n\r\n此为电子印章系统演示功能，请购买正式版本！");
					OFFICE_CONTROL_OBJ.AddSecSignFromURL("ntko",signUrl);
			}
			catch(error){}
		}
		else
		{alert("不能在该类型文档中使用安全签名印章.");}
	}	
}
function addLocalSecSign()
{
	if(IsFileOpened)
	{
		if(OFFICE_CONTROL_OBJ.doctype==1||OFFICE_CONTROL_OBJ.doctype==2)
		{
			try
			{OFFICE_CONTROL_OBJ.AddSecSignFromLocal("ntko","");}
			catch(error){}
		}
		else
		{alert("不能在该类型文档中使用安全签名印章.");}
	}	
}
function addEkeySecSign()
{
	if(IsFileOpened)
	{
		if(OFFICE_CONTROL_OBJ.doctype==1||OFFICE_CONTROL_OBJ.doctype==2)
		{
			try
			{OFFICE_CONTROL_OBJ.AddSecSignFromEkey("ntko");}
			catch(error){}
		}
		else
		{alert("不能在该类型文档中使用安全签名印章.");}
	}
}
function addHandSecSign()
{
	if(IsFileOpened)
	{
		if(OFFICE_CONTROL_OBJ.doctype==1||OFFICE_CONTROL_OBJ.doctype==2)
		{
			try
			{OFFICE_CONTROL_OBJ.AddSecHandSign("ntko");}
			catch(error){}
		}
		else
		{alert("不能在该类型文档中使用安全签名印章.");}
	}
}

function addServerSign(signUrl)
{
	if(IsFileOpened)
	{
			try
			{
				OFFICE_CONTROL_OBJ.AddSignFromURL("ntko",//印章的用户名
				signUrl,//印章所在服务器相对url
				100,//左边距
				100,//上边距 根据Relative的设定选择不同参照对象
				"ntko",//调用DoCheckSign函数签名印章信息,用来验证印章的字符串
				3,  //Relative,取值1-4。设置左边距和上边距相对以下对象所在的位置 1：光标位置；2：页边距；3：页面距离 4：默认设置栏，段落
				100,//缩放印章,默认100%
				1);   //0印章位于文字下方,1位于上方
				
			}
			catch(error){}
	}		
}

function addLocalSign()
{
	if(IsFileOpened)
	{
			try
			{
				OFFICE_CONTROL_OBJ.AddSignFromLocal("ntko",//印章的用户名
					"",//缺省文件名
					true,//是否提示选择
					100,//左边距
					100,//上边距 根据Relative的设定选择不同参照对象
					"ntko",//调用DoCheckSign函数签名印章信息,用来验证印章的字符串
					3,  //Relative,取值1-4。设置左边距和上边距相对以下对象所在的位置 1：光标位置；2：页边距；3：页面距离 4：默认设置栏，段落
					100,//缩放印章,默认100%
					1);   //0印章位于文字下方,1位于上方
			}
			catch(error){}
	}
}
function addPicFromUrl(picURL)
{
	if(IsFileOpened)
	{
		if(OFFICE_CONTROL_OBJ.doctype==1||OFFICE_CONTROL_OBJ.doctype==2)
		{
			try
			{
				OFFICE_CONTROL_OBJ.AddPicFromURL(picURL,//图片的url地址可以时相对或者绝对地址
				false,//是否浮动,此参数设置为false时,top和left无效
				100,//left 左边距
				100,//top 上边距 根据Relative的设定选择不同参照对象
				1,  //Relative,取值1-4。设置左边距和上边距相对以下对象所在的位置 1：光标位置；2：页边距；3：页面距离 4：默认设置栏，段落
				100,//缩放印章,默认100%
				1);   //0印章位于文字下方,1位于上方
				
			}
			catch(error){}
		}
		else
		{alert("不能在该类型文档中使用安全签名印章.");}
	}		
}
function addPicFromLocal()
{
	if(IsFileOpened)
	{
			try
			{
				OFFICE_CONTROL_OBJ.AddPicFromLocal("",//印章的用户名
					true,//缺省文件名
					false,//是否提示选择
					100,//左边距
					100,//上边距 根据Relative的设定选择不同参照对象
					1,  //Relative,取值1-4。设置左边距和上边距相对以下对象所在的位置 1：光标位置；2：页边距；3：页面距离 4：默认设置栏，段落
					100,//缩放印章,默认100%
					1);   //0印章位于文字下方,1位于上方
			}
			catch(error){}
	}
}

function TANGER_OCX_AddDocHeader(strHeader)
{
	if(!IsFileOpened)
	{return;}
	var i,cNum = 30;
	var lineStr = "";
	try
	{
		for(i=0;i<cNum;i++) lineStr += "_";  //生成下划线
		with(OFFICE_CONTROL_OBJ.ActiveDocument.Application)
		{
			Selection.HomeKey(6,0); // go home
			Selection.TypeText(strHeader);
			Selection.TypeParagraph(); 	//换行
			Selection.TypeText(lineStr);  //插入下划线
			// Selection.InsertSymbol(95,"",true); //插入下划线
			Selection.TypeText("★");
			Selection.TypeText(lineStr);  //插入下划线
			Selection.TypeParagraph();
			//Selection.MoveUp(5, 2, 1); //上移两行，且按住Shift键，相当于选择两行
			Selection.HomeKey(6,1);  //选择到文件头部所有文本
			Selection.ParagraphFormat.Alignment = 1; //居中对齐
			with(Selection.Font)
			{
				NameFarEast = "宋体";
				Name = "宋体";
				Size = 12;
				Bold = false;
				Italic = false;
				Underline = 0;
				UnderlineColor = 0;
				StrikeThrough = false;
				DoubleStrikeThrough = false;
				Outline = false;
				Emboss = false;
				Shadow = false;
				Hidden = false;
				SmallCaps = false;
				AllCaps = false;
				Color = 255;
				Engrave = false;
				Superscript = false;
				Subscript = false;
				Spacing = 0;
				Scaling = 100;
				Position = 0;
				Kerning = 0;
				Animation = 0;
				DisableCharacterSpaceGrid = false;
				EmphasisMark = 0;
			}
			Selection.MoveDown(5, 3, 0); //下移3行
		}
	}
	catch(err){
		alert("错误：" + err.number + ":" + err.description);
	}
	finally{
	}
}

function insertRedHeadFromUrl()
{
	var headFileURL=document.getElementById('redHeadTemplateFile').options[document.getElementById('redHeadTemplateFile').selectedIndex].value;
	if(headFileURL==''){
	return;
	}else{ 
		if(OFFICE_CONTROL_OBJ.doctype!=1)//OFFICE_CONTROL_OBJ.doctype=1为word文档
		{return;}
		OFFICE_CONTROL_OBJ.ActiveDocument.Application.Selection.HomeKey(6,0);//光标移动到文档开头
		OFFICE_CONTROL_OBJ.addtemplatefromurl(headFileURL);//在光标位置插入红头文档
		}
}
function openTemplateFileFromUrl(templateUrl)
{
	OFFICE_CONTROL_OBJ.openFromUrl(templateUrl);
}
function doHandSign()
{
	/*if(OFFICE_CONTROL_OBJ.doctype==1||OFFICE_CONTROL_OBJ.doctype==2)//此处设置只允许在word和excel中盖章.doctype=1是"word"文档,doctype=2是"excel"文档
	{*/
		OFFICE_CONTROL_OBJ.DoHandSign2(
									"ntko",//手写签名用户名称
									"ntko",//signkey,DoCheckSign(检查印章函数)需要的验证密钥。
									0,//left
									0,//top
									1,//relative,设定签名位置的参照对象.0：表示按照屏幕位置插入，此时，Left,Top属性不起作用。1：光标位置；2：页边距；3：页面距离 4：默认设置栏，段落（为兼容以前版本默认方式）
									100);
	//}
}
function SetReviewMode(boolvalue)
{
	if(OFFICE_CONTROL_OBJ.doctype==1)
	{
		//alert(232);
		OFFICE_CONTROL_OBJ.ActiveDocument.TrackRevisions = boolvalue;//设置是否保留痕迹
	}
} 

function setShowRevisions(boolevalue)
{
	if(OFFICE_CONTROL_OBJ.doctype==1)
	{
		OFFICE_CONTROL_OBJ.ActiveDocument.ShowRevisions =boolevalue;//设置是否显示痕迹
	}
}
function setFilePrint(boolvalue)
{
	OFFICE_CONTROL_OBJ.fileprint=boolvalue;//是否允许打印
}
function setFileNew(boolvalue)
{
	OFFICE_CONTROL_OBJ.FileNew=boolvalue;//是否允许新建
}
function setFileSaveAs(boolvalue)
{
	OFFICE_CONTROL_OBJ.FileSaveAs=boolvalue;//是否允许另存为
}
function showdialog(){
	OFFICE_CONTROL_OBJ.showdialog(3);
}

function setIsNoCopy(boolvalue)
{
	OFFICE_CONTROL_OBJ.IsNoCopy=boolvalue;//是否禁止粘贴
}
//验证文档控件自带印章功能盖的章
function DoCheckSign()
{
   if(IsFileOpened)
   {	
			var ret = OFFICE_CONTROL_OBJ.DoCheckSign
			(
			false,/*可选参数 IsSilent 缺省为FAlSE，表示弹出验证对话框,否则，只是返回验证结果到返回值*/
			"ntko"//使用盖章时的signkey,这里为"ntko"
			);//返回值，验证结果字符串
			//alert(ret);
   }	
}
//设置工具栏
function setToolBar()
{
	OFFICE_CONTROL_OBJ.ToolBars=!OFFICE_CONTROL_OBJ.ToolBars;
}
//控制是否显示所有菜单
function setMenubar()
{
		OFFICE_CONTROL_OBJ.Menubar=!OFFICE_CONTROL_OBJ.Menubar;
}
//控制”插入“菜单栏
function setInsertMemu()
{
		OFFICE_CONTROL_OBJ.IsShowInsertMenu=!OFFICE_CONTROL_OBJ.IsShowInsertMenu;
	}
//控制”编辑“菜单栏
function setEditMenu()
{
		OFFICE_CONTROL_OBJ.IsShowEditMenu=!OFFICE_CONTROL_OBJ.IsShowEditMenu;
	}
//控制”工具“菜单栏
function setToolMenu()
{
	OFFICE_CONTROL_OBJ.IsShowToolMenu=!OFFICE_CONTROL_OBJ.IsShowToolMenu;
	}
	
//自定义菜单功能函数
function initCustomMenus()
{
	var myobj = OFFICE_CONTROL_OBJ;	
	
	for(var menuPos=0;menuPos<3;menuPos++)
	{
		myobj.AddCustomMenu2(menuPos,"菜单"+menuPos+"(&"+menuPos+")"); 
		for(var submenuPos=0;submenuPos<10;submenuPos++)
		{
			if(1 ==(submenuPos % 3)) //主菜单增加分隔符。第3个参数是-1是在主菜单增加
			{
				myobj.AddCustomMenuItem2(menuPos,submenuPos,-1,false,"-",true);
			}
			else if(0 == (submenuPos % 2)) //主菜单增加子菜单，第3个参数是-1是在主菜单增加
			{
				myobj.AddCustomMenuItem2(menuPos,submenuPos,-1,true,"子菜单"+menuPos+"-"+submenuPos,false);
				//增加子菜单项目
				for(var subsubmenuPos=0;subsubmenuPos<9;subsubmenuPos++)
				{
					if(0 == (subsubmenuPos % 2))//增加子菜单项目
					{
						myobj.AddCustomMenuItem2(menuPos,submenuPos,subsubmenuPos,false,
							"子菜单项目"+menuPos+"-"+submenuPos+"-"+subsubmenuPos,false,menuPos*100+submenuPos*20+subsubmenuPos);
					}
					else //增加子菜单分隔
					{
						myobj.AddCustomMenuItem2(menuPos,submenuPos,subsubmenuPos,false,
							"-"+subsubmenuPos,true);
					}
					//测试禁用和启用
					if(2 == (subsubmenuPos % 4))
					{
						myobj.EnableCustomMenuItem2(menuPos,submenuPos,subsubmenuPos,false);
					}
				}				
			}
			else //主菜单增加项目，第3个参数是-1是在主菜单增加
			{
				myobj.AddCustomMenuItem2(menuPos,submenuPos,-1,false,"菜单项目"+menuPos+"-"+submenuPos,false,menuPos*100+submenuPos);
			}
			
			//测试禁用和启用
			if(1 == (submenuPos % 4))
			{
				myobj.EnableCustomMenuItem2(menuPos,submenuPos,-1,false);
			}
		}
	}
}
//打印
function printwithoutheader() 
{ 
		var i=null;
	if(document.getElementById('CB_orprint').children[0].checked==true)//为了判断是否套打，套打情况下隐藏红头
	{
	try{
			if(wjid!="" || wjid_2!="")
			{
				TANGER_OCX_SetMarkModify(false);
			}
			TANGER_OCX_ShowRevisions(false);
			document.all("TANGER_OCX").SetSignsVisible("*",false,"",0);
			var ary=new Array();
			var xx_s=0;
			//for(i=1;i<=document.all("TANGER_OCX").activedocument.Range().ShapeRange.Count;i++)
			//{
				//if(document.all("TANGER_OCX").activedocument.Range().ShapeRange(i).Line!=null)
				//{
				//	ary[i]=document.all("TANGER_OCX").activedocument.Range().ShapeRange(i);
				//	document.all("TANGER_OCX").activedocument.Range().ShapeRange(i).Line.ForeColor.RGB = 16777215;
				//}
			//}
			var ary=new Array();
			//线颜色改变
			for(var i=1;i<=document.all("TANGER_OCX").activedocument.Range().ShapeRange.Count;i++)
			{
				if(document.all("TANGER_OCX").activedocument.Range().ShapeRange(i).Line!=null)
				{
					ary[i]=document.all("TANGER_OCX").activedocument.Range().ShapeRange(i);
					//alert(document.all("TANGER_OCX").activedocument.Range().ShapeRange(i).Line.ForeColor.RGB);
					if(document.all("TANGER_OCX").activedocument.Range().ShapeRange(i).Line.ForeColor.RGB==255)
					{
					document.all("TANGER_OCX").activedocument.Range().ShapeRange(i).Line.ForeColor.RGB = 16777215;
					xx_s=parseInt(xx_s)+parseInt(1);
					}
				}
			}
			
			//标头颜色改变
			var bt_s=0;
			for(var m=1;m<=TANGER_OCX_OBJ.ActiveDocument.Bookmarks.Count;m++)
			{
				TANGER_OCX_OBJ.ActiveDocument.Bookmarks(m).Range.Font.Color =16777215;
				bt_s=parseInt(bt_s)+parseInt(1);
			}
			
			//删除五角星
			var sel=document.all("TANGER_OCX").ActiveDocument.Application.Selection;
			var shapes=document.all("TANGER_OCX").ActiveDocument.Shapes;
			var count =shapes.count;
			var count_s=0;
			//取消印章
			var yzs=0;
			//alert(count);
			for(var i=1;i<count;i++)
			{
					//alert(shapes(i).WrapFormat.Type);
					if(7==shapes(i).WrapFormat.Type)
					{
						shapes(i).Delete();
						count_s=parseInt(count_s)+parseInt(1);
					}
			}
			
			try
			{
				TANGER_OCX_OBJ.PrintOut(true); //打印 
			}
			catch(e)
			{
				//document.all("TANGER_OCX").SetSignsVisible("*",true,"",0);
				
				var ss=parseInt(xx_s)+parseInt(bt_s)+parseInt(count_s);
				document.all("TANGER_OCX").ActiveDocument.Undo(ss); 
				TANGER_OCX_ShowRevisions(true);
				if(wjid!="")
				{
					if(statex=="执行中")
					{
						TANGER_OCX_SetMarkModify(true);
					}
				}
				document.all("TANGER_OCX").SetSignsVisible("*",true,"",0);
				
				return;
			}
			//document.all("TANGER_OCX").SetSignsVisible("*",true,"",0);
			
			var ss=parseInt(xx_s)+parseInt(bt_s)+parseInt(count_s);
			document.all("TANGER_OCX").ActiveDocument.Undo(ss);
			TANGER_OCX_ShowRevisions(true);
			if(wjid!="")
			{
				if(statex=="执行中")
				{
					TANGER_OCX_SetMarkModify(true);
				}
			}
			document.all("TANGER_OCX").SetSignsVisible("*",true,"",0); 
				
			//for(i=1;i<ary.length;i++)
			//{
			//	ary[i].Line.ForeColor.RGB=255;
			//}
			
			//for(var m=1;m<=TANGER_OCX_OBJ.ActiveDocument.Bookmarks.Count;m++)
			//{
			//	TANGER_OCX_OBJ.ActiveDocument.Bookmarks(m).Range.Font.Color =255;
			//s}
		}
		catch(e)
		{
			if(window.confirm("此文件没有套用模板，不能套打，还要继续打印吗？"))
			{
				try
				{
					document.all("TANGER_OCX").SetSignsVisible("*",false,"",0);
					TANGER_OCX_OBJ.PrintOut(true); //打印 
					TANGER_OCX_ShowRevisions(true);
					TANGER_OCX_SetMarkModify(true);
					document.all("TANGER_OCX").SetSignsVisible("*",true,"",0);
				}
				catch(e)
				{
					TANGER_OCX_ShowRevisions(true);
					TANGER_OCX_SetMarkModify(true);
					document.all("TANGER_OCX").SetSignsVisible("*",true,"",0);
					TANGER_OCX_ShowRevisions(true);
					return;
				}
			}
		}
	}
	else
	{
		try{
				TANGER_OCX_ShowRevisions(false);
				document.all("TANGER_OCX").SetSignsVisible("*",false,"",0);
				TANGER_OCX_OBJ.PrintOut(true); //打印 
				TANGER_OCX_SetMarkModify(true);
				document.all("TANGER_OCX").SetSignsVisible("*",true,"",0);
					
				TANGER_OCX_ShowRevisions(true);
		}
		catch(e)
		{
			TANGER_OCX_ShowRevisions(false);
			TANGER_OCX_SetMarkModify(true);
			document.all("TANGER_OCX").SetSignsVisible("*",true,"",0);
					
			TANGER_OCX_ShowRevisions(true);
			return;
		}
	}
	
} 
