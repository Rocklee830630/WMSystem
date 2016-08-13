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
 
 String sql = "";
 if(!Pub.empty(bdbh)){
	 sql = "select distinct( PONIT),xmmc,bdmc,a.xmid,b.bdid  from  baidu_map a,gc_jh_sj b where a.xmid = b.xmid and a.bdid = b.bdid and a.XMID='"+xmbh+"' and a.BDID='"+bdbh+"'";
 }else{
	 sql = "select distinct( PONIT),xmmc,bdmc,a.xmid,b.bdid  from  baidu_map a,gc_jh_sj b where a.xmid = b.xmid and a.bdid = b.bdid and a.XMID='"+xmbh+"'";
 }
 QuerySet qs = DBUtil.executeQuery(sql, null);
 String point = "";
 ArrayList bdList = new ArrayList();
 if(qs.getRowCount()>0){
	 for(int i=0;i<qs.getRowCount();i++){
	   ArrayList al = new ArrayList();
	   point =qs.getString(i+1, "PONIT");
	   StringTokenizer t =  new StringTokenizer(point, "|");
		while (t.hasMoreTokens()) {
			al.add((String) t.nextElement());
		}
		bdList.add(al);
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

var getRandomColor = function(){
	  return  '#' +
	    (function(color){
	    return (color +=  '0123456789abcdef'[Math.floor(Math.random()*16)])
	      && (color.length == 6) ?  color : arguments.callee(color);
	  })('');
	}


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
		 //标注地图折线
		 
		 <%
		 if(bdList.size()>0){
           // String color = "red";
			for(int j=0;j<bdList.size();j++){
				ArrayList as = (ArrayList)bdList.get(j);
		      if(as.size()>0){
		    	 
		 %>
		  var color = getRandomColor();
		  var test_polyline<%=j%> = new BMap.Polyline([  
		               <%
		               for(int i =0;i<as.size();i++){

		               %>
		                                         new BMap.Point(<%=(String)as.get(i)%>),  
		               <%}%>
		                                       ],  
		                                       {strokeColor:color, strokeWeight:6, strokeOpacity:0.5}  
		                                      );  
		                                      map.addOverlay(test_polyline<%=j%>); 
		 <%
		 
		   }
		  }
		 }
		 %>  
		 
		 var xArray=new Array();
		 var yArray=new Array();
		 var bdmc  =new Array();
		 var bdid  =new Array();
		 
		 <%
		 if(bdList.size()>0){
            String color = "red";
			for(int j=0;j<bdList.size();j++){
			  ArrayList as = (ArrayList)bdList.get(j);
		      if(as.size()>0){
		    	  
		 %>
             var t = "<%=(String)as.get(0)%>";
             xArray[<%=j%>] = t.split(",")[0];
             yArray[<%=j%>] = t.split(",")[1];    
             bdmc[<%=j%>]  = "<%=qs.getString(j+1,"BDMC")%>";
             bdid[<%=j%>]  = "<%=qs.getString(j+1,"BDID")%>";
		 <%
		   }
		  }
		 }
		 %> 
		 
		 for(i=0;i<xArray.length;i++){
				var point = new BMap.Point(xArray[i],yArray[i]);
				
				//var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {  
                  //  offset: new BMap.Size(10, 25), // 指定定位位置  
                   // imageOffset: new BMap.Size(0, 0 - 10 * 25) // 设置图片偏移  
               // });  
  //  var marker=new BMap.Marker(point,{icon:myIcon});  
				var marker=new BMap.Marker(point);//创建标注
				var html='';
				var infoWindow = new BMap.InfoWindow(html);//创建窗口信息
                var bdid_ = bdid[i];
				marker.infoWindow=infoWindow;//给当前标注新增一个属性以便保存窗口信息infoWindow
				marker.addEventListener("click", function(e){//添加标注的点击事件回调
	                // this.openInfoWindow(e.target.infoWindow);//点击标注时，打开改标注对应的回调信息
	                var xmid =xmbh;
	               // $(window).manhuaDialog(xmscUrl(xmid));
	               $(window).manhuaDialog({"title":"标段划分>详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bdhf/bdView.jsp?id="+bdid_,"modal":"1"}); 
					//$(window).manhuaDialog({"title":"详细信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/tcjh_gtt.jsp?id=","modal":"1"});
				});					 	
				map.addOverlay(marker);//添加标注到地图
				var myLabel = new BMap.Label(bdmc[i],     //为lable填写内容
					    {offset:new BMap.Size(-60,-60),                  //label的偏移量，为了让label的中心显示在点上
					    position:point});                                //label的位置

					//myLabel.setTitle("我是文本标注label");               //为label添加鼠标提示
					map.addOverlay(myLabel);   
			
			}
		 
		 
		 //设置标注点
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
<app:dialogs/>
  <div id="allmap" style="width: 1000px;height: 500px;" ></div><br>
  <input id="info" type ="hidden" value="<%=point%>">
  <button id="setbz" class="btn" onclick="setbz()" style="display:none;"  type="button">设置标注</button>
  <button id="cancelbz" class="btn" onclick="cancelbz()" style="display:none;" type="button">取消标注</button>
 
  <button id="save" class="btn" onclick="save()" style="display:none;" type="button">保存</button>
  <button id="w_open" class="btn" style="display:none;" onclick="map.clearOverlays();document.getElementById('info').value = '';points=[];" type="button">清除标点</button>
  <br>
  <div id="panel" ></div>
</body>
</html>