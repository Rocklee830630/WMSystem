<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%	String id = request.getParameter("infoID");
			String type = request.getParameter("type");
		%>
		<app:base />
		<title>批复问题</title>
		<style type="text/css">
			ul{list-style-type:none;}
			#star{position:relative;width:100%;}
			#star ul,#star span{float:left;display:inline;height:19px;line-height:19px;}
			#star ul{margin:0 10px;}
			#star li{float:left;width:24px;cursor:pointer;text-indent:-9999px;background:url(${pageContext.request.contextPath }/images/star.png) no-repeat;}
			#star strong{color:#f60;padding-left:10px;}
			#star li.on{background-position:0 -28px;}
			#star p{position:absolute;top:20px;width:159px;height:60px;display:none;background:url(${pageContext.request.contextPath }/images/icon.gif) no-repeat;padding:7px 10px 0;}
			#star p em{color:#f60;display:block;font-style:normal;}
		</style>
		<script type="text/javascript" charset="utf-8">
			var btnNum = 0;
			var p_id = '<%=id%>';
			var p_type = '<%=type%>';
			$(function(){
				doInit();
				$("#btnSave").click(function(){
					if($("#insertForm").validationButton()){
		 				//生成json串
		 				if(p_score!=undefined){
		 					$("#mycd").val(p_score);
		 				}else{
		 					xInfoMsg("请对满意程度进行评分!","警告");
		 					return;
		 				}
		 		 		var data = Form2Json.formToJSON(insertForm);
						$(window).manhuaDialog.setData(data);
						$(window).manhuaDialog.sendData();
						$(window).manhuaDialog.close();
					}
				});
			});
			function doInit(){
				$("#wtid").val(p_id);
				var sysdate = getCurrentDate();
				if(p_type=="wjj"){
					$(".jjqk").hide();
				}else{
					$("#jjsj").val(sysdate);
				}
			}
			var p_score;
			window.onload = function (){
				var oStar = document.getElementById("star");
				var aLi = oStar.getElementsByTagName("li");
				var oUl = oStar.getElementsByTagName("ul")[0];
				var oSpan = oStar.getElementsByTagName("span")[1];
				var oP = oStar.getElementsByTagName("p")[0];
				var i = iScore = iStar = 0;
				var aMsg = [
							"不满意|",
							"基本满意|",
							"非常满意|"
							]
				
				for (i = 1; i <= aLi.length; i++)
				{
					aLi[i - 1].index = i;
					//鼠标移过显示分数
					aLi[i - 1].onmouseover = function ()
					{
						fnPoint(this.index);
						//浮动层显示
						oP.style.display = "block";
						//计算浮动层位置
						oP.style.left = oUl.offsetLeft + this.index * this.offsetWidth - 104 + "px";
						//匹配浮动层文字内容
						oP.innerHTML = "<em><b>" + this.index + "</b> 分 " + aMsg[this.index - 1].match(/(.+)\|/)[1] + "</em>"
					};
					//鼠标离开后恢复上次评分
					aLi[i - 1].onmouseout = function ()
					{
						fnPoint();
						//关闭浮动层
						oP.style.display = "none"
					};
					//点击后进行评分处理
					aLi[i - 1].onclick = function ()
					{
						p_score=this.index;
						iStar = this.index;
						oP.style.display = "none";
						oSpan.innerHTML = "<strong>" + (this.index) + " 分</strong> (" + aMsg[this.index - 1].match(/\|(.+)/)[1] + ")"
					}
				}
				//评分处理
				function fnPoint(iArg){
					//分数赋值
					iScore = iArg || iStar;
					for (i = 0; i < aLi.length; i++) aLi[i].className = i < iScore ? "on" : "";	
				}
			};
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button id="btnSave" class="btn" type="button">
								保存
							</button>
						</span>
					</h4>
					<form method="post" action="" id="insertForm">
						<table class="B-table" width="100%">
							<!-- 这里需要一个隐藏域，存放比如：问题编号,接受人账号，接受单位等信息 -->
							<tr style="display: none;">
								<th width="8%" class="right-border bottom-border">
									问题编号
								<br></th>
								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" name="WTTB_INFO_ID" fieldname="WTTB_INFO_ID"
										id="wtid">
								<br></td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border jjqk">
									解决时间
								</th>
								<td width="42%" colspan=5 class="bottom-border jjqk">
									<input class="span12 date" type="date" name="SJSJ" fieldname="SJSJ" id="jjsj">
								</td>
								<th width="8%" class="right-border bottom-border">
									满意程度
								</th>
								<td width="42%" colspan=5 class="bottom-border">
									<input class="span12" type="text" name="MYCD" fieldname="MYCD" id="mycd" style="display:none;">
									<div id="star">
										<ul>
											<li>
												<a href="javascript:;">1</a>
											</li>
											<li>
												<a href="javascript:;">2</a>
											</li>
											<li>
												<a href="javascript:;">3</a>
											</li>
										</ul>
										<span></span>
										<p></p>
									</div>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</body>
</html>
