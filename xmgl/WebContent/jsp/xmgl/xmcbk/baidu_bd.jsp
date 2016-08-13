<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.common.UserTemplate"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<app:base />
<%
 String xmbh =  request.getParameter("xmbh");
 String bdbh =  request.getParameter("bdbh");
 //QuerySet qs = DBUtil.executeQuery("select bddd  from  GC_XMBD where XMID='"+xmbh+"' and GC_XMBD_ID='"+bdbh+"'", null);
 //String bddd = "";
 //if(qs.getRowCount()>0){
//	 bddd = qs.getString(1, "BDDD");
	 
 //}
 QuerySet qs = DBUtil.executeQuery("select PONIT  from  baidu_map where XMID='"+xmbh+"' and BDID='"+bdbh+"'", null);
 String point = "";
 ArrayList al = new ArrayList();
 if(qs.getRowCount()>0){
	 point =qs.getString(1, "PONIT");
	 StringTokenizer t =  new StringTokenizer(point, "|");
	  // int t = 0;
		while (t.hasMoreTokens()) {
			al.add((String) t.nextElement());
		//	al.add(s);
		//	t++;
		}

 }


%>


<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.4"></script>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/demo/DemoController.do";
var xmbh = "<%=xmbh%>";
var bdbh = "<%=bdbh%>";

var map = null;  //实例化百度地图
var address = "人民广场";      //详细地址
var lableName = "国家图书馆";        //建筑名称
var city = "长春";                 //城市

var myGeo = new BMap.Geocoder();   //地址解析器
var key = 1;    //开关
var newpoint;   //一个经纬度
var points = [];    //数组，放经纬度信息
var polyline = new BMap.Polyline(); //折线覆盖物
function initMap(){
	/**
	 * 根据地址获得位置坐标，然后实例化百度地图
	 */
	myGeo.getPoint(address, function(point){	
	  if(point){
		  map = new BMap.Map("allmap");  //实例化百度地图
	      map.enableScrollWheelZoom();          //启用滚轮放大缩小      
	      map.centerAndZoom(point,15);          //初始化地图,设置中心点坐标和地图级别。
	      map.addControl(new BMap.NavigationControl());  //添加平移缩放控件
		  map.addControl(new BMap.OverviewMapControl());  //添加地图缩略图控件      
	       //创建标注（类似定位小红旗）
		//   var marker = new BMap.Marker(point); 
		   //标注提示文本
		//   var label = new BMap.Label(lableName,{"offset":new BMap.Size(20,-20)});       
		 //  marker.setLabel(label); //添加提示文本  
		   //创建消息框
		 //  var infoWindow = new BMap.InfoWindow(address);  
		   //绑定标注单击事件，设置显示的消息框
		 //  marker.addEventListener("click",function(){this.openInfoWindow(infoWindow);});
		 //  map.addOverlay(marker);  //把标注添加到地图
		 
		 
		 <%if(al.size()>0){
		 
		 %>
		 
		  var test_polyline = new BMap.Polyline([  
		               <%
		               for(int i =0;i<al.size();i++){
		               %>
		                                         new BMap.Point(<%=(String)al.get(i)%>),  
		                                       
		               <%}%>
		                                       ],  
		                                       {strokeColor:"red", strokeWeight:6, strokeOpacity:0.5}  
		                                      );  
		                                      map.addOverlay(test_polyline); 
		 <%}
		 %>                                    
	  }
	}, city);
	
}

function save(){
	
	var point = document.getElementById("info").value;
	if(point==null||point==""){
		xAlert("提示信息",'请标点','3');
		return;
	}
	$.ajax({
		url:controllername + "?savePoint&point="+point+"&bdbh="+bdbh+"&xmbh="+xmbh,
		data:"",
		dataType:"json",
		async:false,
		type : 'post',

		success:function(result){
			var res = result.msg;
			xAlert("提示信息",res);
		}
	});
}
function setbz(){
	 map.addEventListener("click",function(e){   //单击地图，形成折线覆盖物
		    newpoint = new BMap.Point(e.point.lng,e.point.lat);

		        points.push(newpoint);  //将新增的点放到数组中
		        polyline.setPath(points);   //设置折线的点数组
		        map.addOverlay(polyline);   //将折线添加到地图上
		        document.getElementById("info").value += "" + e.point.lng + "," + e.point.lat + "|";    //输出数组里的经纬度

		});
	
}
function cancelbz(){
	map.removeEventListener("click",function(e){   //单击地图，形成折线覆盖物
	    newpoint = new BMap.Point(e.point.lng,e.point.lat);

        points.push(newpoint);  //将新增的点放到数组中
        polyline.setPath(points);   //设置折线的点数组
        map.addOverlay(polyline);   //将折线添加到地图上
        document.getElementById("info").value += "" + e.point.lng + "," + e.point.lat + "|";    //输出数组里的经纬度

});
}

function findPath(){
 var b = document.getElementById("begin").value;  //起始位置
 var e = document.getElementById("end").value;    //结束位置
	//初始化公共公交查询系统
    var transit = new BMap.TransitRoute(map,{
      renderOptions:{
	      map:map,
	      panel:'panel'
	    }
     });

    transit.search(b,e);  //查询
}

window.onload= initMap();   //初始化百度地图
</script>
</head>
<body>
  <div id="allmap" style="width: 1000px;height: 500px;" ></div><br>
  <input id="info" type ="hidden" value="<%=point%>">
  <button id="setbz" class="btn" onclick="setbz()" type="button">设置标注</button>
  <button id="cancelbz" class="btn" onclick="cancelbz()" style="display:none;" type="button">取消标注</button>
 
  <button id="save" class="btn" onclick="save()" type="button">保存</button>
  <button id="w_open" class="btn" onclick="map.clearOverlays();document.getElementById('info').value = '';points=[];" type="button">清除标点</button>
  <br>
  <div id="panel" ></div>
</body>
</html>