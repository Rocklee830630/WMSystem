<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en-au">
    <head>
        <title>jQuery.Gantt</title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge;chrome=1" >
<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/gantt/css/style.css" rel="stylesheet" media="screen">
		<style type="text/css">
			body {
				font-family: Helvetica, Arial, sans-serif;
				font-size: 13px;
				padding: 0 0 50px 0;
			}
			.contain {
				width: 800px;
				margin: 0 auto;
			}
			h1 {
				margin: 40px 0 20px 0;
			}
			h2 {
				font-size: 1.5em;
				padding-bottom: 3px;
				border-bottom: 1px solid #DDD;
				margin-top: 50px;
				margin-bottom: 25px;
			}
			table th:first-child {
				width: 150px;
			}
		</style>
    </head>
    <body>


<div class="gantt"></div>
    </body>
<script src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
<script src="${pageContext.request.contextPath }/js/base/bootstrap.js"></script>
<script src="${pageContext.request.contextPath }/gantt/js/jquery.fn.gantt.js"></script>
    <script>

		$(function() {

			"use strict";

			$(".gantt").gantt({
				source: [{
					name: "前期",
					desc: "开始时间",
					values: [{
						id: "t01",
						from: "/Date(1322323200000)/",
						to: "/Date(1322323200000)/",
						label: "需要显示的内容", 
						customClass: "ganttRed",
						dataObj:"sdfsdf",
						nullType:"from"
					}]
				},{
					name: " ",
					desc: "结束时间",
					values: [{
						id: "t02",
						from: "/Date(1324611200000)/",
						to: "/Date(1323302400000)/",
						label: "需要显示的内容", 
						customClass: "ganttRed",
						dep: "t01"
					}]
				},{
					name: "设计",
					desc: "开始时间",
					values: [{
						from: "/Date(1323802400000)/",
						to: "/Date(1325685200000)/",
						label: "需要显示的内容", 
						customClass: "ganttGreen"
					}]
				},{
					name: " ",
					desc: "结束时间",
					values: [{
						from: "/Date(1325685200000)/",
						to: "/Date(1325695200000)/",
						label: "需要显示的内容", 
						customClass: "ganttBlue"
					}]
				},{
					name: "造价",
					desc: "开始时间",
					values: [{
						from: "/Date(1326785200000)/",
						to: "/Date(1325785200000)/",
						label: "需要显示的内容", 
						customClass: "ganttGreen"
					}]
				},{
					name: " ",
					desc: "结束时间",
					values: [{
						from: "/Date(1328785200000)/",
						to: "/Date(1328905200000)/",
						label: "需要显示的内容", 
						customClass: "ganttBlue"
					}]
				},{
					name: "施工",
					desc: "开始时间",
					values: [{
						from: "/Date(1330011200000)/",
						to: "/Date(1336611200000)/",
						label: "需要显示的内容", 
						customClass: "ganttOrange"
					}]
				},{
					name: " ",
					desc: "结束时间",
					values: [{
						from: "/Date(1336611200000)/",
						to: "/Date(1338711200000)/",
						label: "需要显示的内容", 
						customClass: "ganttOrange"
					}]
				},{
					name: " ",
					desc: "延期完成",
					values: [{
						from: "/Date(1336611200000)/",
						to: "/Date(1349711200000)/",
						label: "需要显示的内容", 
						customClass: "ganttOrange"
					}]
				}],
				navigate: "scroll",
				scale: "days",
				maxScale: "months",
				minScale: "days",
				itemsPerPage: 10,
				onItemClick: function(data) {
					alert("Item clicked - show some details");
				},
				onAddClick: function(dt, rowId) {
					alert("Empty space clicked - add an item!");
				},
				onRender: function() {
					if (window.console && typeof console.log === "function") {
						console.log("chart rendered");
					}
				}
			});

			$(".gantt").popover({
				selector: ".bar",
				title: "I'm a popover",
				content: "And I'm the content of said popover.",
				trigger: "hover"
			});

			prettyPrint();

		});

    </script>
</html>