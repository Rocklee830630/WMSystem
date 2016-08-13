<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%

%>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap-carousel.js"></script>
</head>
<body>

<div id="myCarousel" class="carousel slide">
  <ol class="carousel-indicators">
    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
    <li data-target="#myCarousel" data-slide-to="1"></li>
    <li data-target="#myCarousel" data-slide-to="2"></li>
  </ol>
  <!-- Carousel items -->
  <div class="carousel-inner" id="picViewDiv">
    <div class="active item" align="center">
    	 <img src="Blue hills.jpg" alt="">
    </div>
    <div class="item" align="center">
    	 <img src="Sunset.jpg" alt="">
    </div>
    <div class="item" align="center">
    	 <img src="Water lilies.jpg" alt="">
    </div>
  </div>
  <!-- Carousel nav -->
  <a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
  <a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
</div>

<script type="text/javascript" charset="utf-8">


$(function(){
	$('.carousel').carousel({
		  interval: 2000
		})
});

</script>

</body>
</html>