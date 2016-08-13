package com.ccthanking.framework.CommonChart.showchart.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author xiahongbo
 * @date 2014-11-4
 */
public class EChartUtil {
	
	public static final String chartColor1 = "#E7BA10";	//黄色
	public static final String chartColor2 = "#9BBB59";	//浅绿色（已完成）
	public static final String chartColor3 = "#8064A2";	//浅紫色
	public static final String chartColor4 = "#4F81BD";	//蓝色
	public static final String chartColor5 = "#52596B";	//灰色（未完成）
	public static final String chartColor6 = "#DDDDDD";
	public static final String chartColor7 = "#ADD8E6";

	public static final String chartWarnColor1 = "#19BC9C";	//浅蓝色
	public static final String chartWarnColor2 = "#5555FF";	//蓝色 
	public static final String chartWarnColor3 = "#9B59B6";	//紫色
	public static final String chartWarnColor4 = "#F1C40E";	//黄色（深一点）
//	public static final String chartWarnColor5 = "#FF0000";	//红色
	public static final String chartWarnColor5 = "#C0504D";	//红色

	/**
	 * 生成EChart饼图
	 * @param domresult
	 * @param chartMap
	 * @param rowMap
	 * @return
	 */
	public static String makePieEChartJsonString(String domresult,HashMap chartMap,HashMap rowMap){
		// 定义图表属性开始
		JSONObject optionTipObj = new JSONObject();

		//-----放入标题
		JSONObject titleObj = new JSONObject();
		titleObj.put("text", chartMap.get("titleText"));
		titleObj.put("x", chartMap.get("x"));

		JSONObject titleTextStyleObj = new JSONObject();
		titleTextStyleObj.put("fontSize", "20");
		titleObj.put("textStyle", titleTextStyleObj);
		optionTipObj.put("title", titleObj);

		//-----放入工具栏
		JSONObject toolboxObj = new JSONObject();
		toolboxObj.put("show", true);
		
		JSONObject restoreObj = new JSONObject();
		restoreObj.put("show", true);
		JSONObject featureObj = new JSONObject();
		featureObj.put("restore", restoreObj);
		toolboxObj.put("feature", featureObj);
		optionTipObj.put("toolbox", toolboxObj);
		//-----放入提示框
		JSONObject toolTipObj = new JSONObject();
		toolTipObj.put("trigger", "item");
		toolTipObj.put("formatter", "{a} <br/>{b} : {c} ({d}%)");
		optionTipObj.put("tooltip", toolTipObj);
		//-----放入颜色
		JSONArray colorArray = makeChartColorObject(chartMap);
		optionTipObj.put("color", colorArray);
		//-----放入calculable
		optionTipObj.put("calculable", true);
		//-----放入series
		JSONArray seriesArray = new JSONArray();
		JSONObject seriesObj = new JSONObject();
		//------------生成常规显示
		JSONArray radiusArr = new JSONArray();
		radiusArr.add("30%");
		radiusArr.add("60%");
		JSONObject itemStyleObj = new JSONObject();
		JSONObject normalObj = new JSONObject();
		JSONObject nlabelObj = new JSONObject();
		nlabelObj.put("position", "inner");
		nlabelObj.put("formatter", "{c}");
		normalObj.put("label", nlabelObj);
		JSONObject nlabelLineObj = new JSONObject();
		nlabelLineObj.put("show", false);
		normalObj.put("labelLine", nlabelLineObj);
		itemStyleObj.put("normal", normalObj);
		//-----------生成鼠标移上显示
		JSONObject emphasisObj = new JSONObject();
		JSONObject elabelObj = new JSONObject();
		elabelObj.put("show", true);
		elabelObj.put("position", "center");
		JSONObject textStyleObj = new JSONObject();
		textStyleObj.put("fontSize", "12");
		textStyleObj.put("fontWeight", "bold");
		elabelObj.put("textStyle", textStyleObj);
		emphasisObj.put("label", elabelObj);
		itemStyleObj.put("emphasis", emphasisObj);
		//------------生成数据
		JSONArray dataArr = new JSONArray();
		JSONObject rowObj1 = new JSONObject();
		rowObj1.put("value", 320);
		rowObj1.put("name", "已完成");
		JSONObject rowObj2 = new JSONObject();
		rowObj2.put("value", 210);
		rowObj2.put("name", "未完成");
		// 定义数据属性开始

		JSONArray lDataArray = new JSONArray();
		JSONArray dataArray = new JSONArray();
		if (domresult != "0") {
			JSONObject jsono = JSONObject.fromObject(domresult);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			int x = 0;
			List uniqueList = new ArrayList();
			for (int i = 0; i < data.size(); i++) {
				JSONObject dataObject = JSONObject.fromObject(data.get(i));
				JSONObject rowObject = new JSONObject();
				// ---添加或覆盖传入的自定义属性
				if (rowMap != null) {
					Iterator rit = rowMap.entrySet().iterator();
					while (rit.hasNext()) {
						Map.Entry re = (Map.Entry) rit.next();
						if (!"LABEL".equals(re.getKey())
								&& !"VALUE".equals(re.getKey())) {
							rowObject.put(re.getKey(), re.getValue()); // 插入新的属性
						}
					}
					// 如果label起了别名，那么取别名对应的值
					if (rowMap.containsKey("LABEL")) {
						rowObject.put("NAME", dataObject.get(rowMap
								.get("LABEL")));
					} else {
						if(!uniqueList.contains(dataObject.get("LABEL").toString())){
							rowObject.put("name", dataObject.get("LABEL"));
							uniqueList.add(dataObject.get("LABEL").toString());
							lDataArray.add(dataObject.get("LABEL").toString());
						}
					}
					// 如果value起了别名，那么取别名对应的值
					if (rowMap.containsKey("VALUE")) {
						rowObject.put("VALUE", dataObject.get(rowMap
								.get("VALUE")));
					} else {
						rowObject.put("value", dataObject.get("VALUE"));
					}
				} else {
					rowObject.put("value", dataObject.get("VALUE"));
					if(!uniqueList.contains(dataObject.get("LABEL").toString())){
						rowObject.put("name", dataObject.get("LABEL"));
						uniqueList.add(dataObject.get("LABEL").toString());
						lDataArray.add(dataObject.get("LABEL").toString());
					}
				}
				dataArr.add(rowObject);
				x++;
			}
		}
		//-----放入标题
		JSONObject legendObj = new JSONObject();
		legendObj.put("show", false);
		legendObj.put("orient", "vertical");
		legendObj.put("x", "bottom");
		legendObj.put("data", lDataArray);
	//	optionTipObj.put("legend", legendObj);
		
		seriesObj.put("name", chartMap.get("seriesName"));
		seriesObj.put("type", "pie");
		seriesObj.put("radius", "55%");
		JSONArray centerArray = new JSONArray();
		centerArray.add("50%");
		centerArray.add("60%");
	//	seriesObj.put("itemStyle", itemStyleObj);
		seriesObj.put("center", centerArray);
		seriesObj.put("data", dataArr);
		seriesArray.add(seriesObj);
		optionTipObj.put("series", seriesArray);
		
		
		// 定义数据属性结束
		
		return optionTipObj.toString();
	}
	/**
	 * 生成EChart条形图
	 * @param domresult
	 * @param chartMap
	 * @param rowMap
	 * @return
	 */
	public static String makeBarEChartJsonString(String domresult,HashMap chartMap,HashMap rowMap){
		// 定义图表属性开始
		JSONObject optionTipObj = new JSONObject();

		//-----放入标题
		JSONObject titleObj = new JSONObject();
		titleObj.put("text", chartMap.get("titleText"));
		titleObj.put("x", chartMap.get("x"));

		JSONObject titleTextStyleObj = new JSONObject();
		titleTextStyleObj.put("fontSize", "20");
		titleObj.put("textStyle", titleTextStyleObj);
		optionTipObj.put("title", titleObj);
		//-----放入提示框
		JSONObject toolTipObj = new JSONObject();
		toolTipObj.put("trigger", "axis");
		optionTipObj.put("tooltip", toolTipObj);
		//-----放入颜色
		JSONArray colorArray = makeChartColorObject(chartMap);
		optionTipObj.put("color", colorArray);
		//-----放入工具栏
		JSONObject toolboxObj = new JSONObject();
		toolboxObj.put("show", true);
		optionTipObj.put("toolboxObj", toolboxObj);
		//-----放入calculable
		optionTipObj.put("calculable", false);
		
		JSONObject gridObj = new JSONObject();
		String gridX = chartMap.get("gridX") == null ? "120px" : chartMap.get("gridX").toString();
		String gridX2 = chartMap.get("gridX2") == null ? "10px" : chartMap.get("gridX2").toString();
		gridObj.put("x", gridX);
		gridObj.put("x2", gridX2);
		optionTipObj.put("grid", gridObj);
		//-----放入series
		JSONArray seriesArray = new JSONArray();

		//------------生成数据
		// 定义数据属性开始

		JSONArray lDataArray = new JSONArray();
		JSONArray axisDataArray = new JSONArray();
		if (domresult != "0") {
			JSONObject jsono = JSONObject.fromObject(domresult);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			
			String order = chartMap.get("order") == null ? "false" : chartMap.get("order").toString();
			if("true".equals(order)) {
				for (int i = 0; i < data.size() ; i++) {
					JSONObject seriesObj = new JSONObject();
					JSONArray dataArr = new JSONArray();
					JSONObject dataObject = JSONObject.fromObject(data.get(i));
					JSONObject rowObject = new JSONObject();
					// ---添加或覆盖传入的自定义属性
					String label = dataObject.get("LABEL").toString();
					String seriesname = dataObject.get("SERIESNAME").toString();
					if(!axisDataArray.contains(label)){
						axisDataArray.add(dataObject.get("LABEL").toString());
					}
					if(lDataArray.contains(seriesname)){
						//如果已经读取过，就不再读取了
					}else{
						//如果未读取过，先放入lDataArray，再进行读取
						lDataArray.add(seriesname);
						for(int dataNum=0;dataNum<data.size();dataNum++){
							JSONObject seriesDataObject = JSONObject.fromObject(data.get(dataNum));
							if(seriesname.equals(seriesDataObject.get("SERIESNAME"))){
								dataArr.add(seriesDataObject.get("VALUE"));
							}
						}
						seriesObj.put("name", seriesname);
						seriesObj.put("type", chartMap.get("seriesType") == null ? "bar" : chartMap.get("seriesType"));
						if("bar".equals(seriesObj.get("type"))) {
							if(rowMap.containsKey("seriesItemStyle")) {
							//	itemStyle : { normal: {label : {show: true, position: 'insideRight'}}}
								JSONObject labelObj = new JSONObject();
								labelObj.put("show", true);
								labelObj.put("position", "insideRight");
								
								JSONObject normalObj = new JSONObject();
								normalObj.put("label", labelObj);
								
								JSONObject itemStyleObj = new JSONObject();
								itemStyleObj.put("normal", normalObj);
								
								seriesObj.put("itemStyle", itemStyleObj);
							}
						}
						if(rowMap.containsKey("STACK")){
							HashMap stackMap = (HashMap)rowMap.get("STACK");
							if(stackMap.get(seriesname)!=null){
								seriesObj.put("stack", stackMap.get(seriesname));
							}
						}
						seriesObj.put("data", dataArr);
						seriesArray.add(seriesObj);
					}
				}
			} else {
				int size = data.size() -1;
				for (int i = size; i >=0 ; i--) {
					JSONObject seriesObj = new JSONObject();
					JSONArray dataArr = new JSONArray();
					JSONObject dataObject = JSONObject.fromObject(data.get(i));
					JSONObject rowObject = new JSONObject();
					// ---添加或覆盖传入的自定义属性
					String label = dataObject.get("LABEL").toString();
					String seriesname = dataObject.get("SERIESNAME").toString();
					if(!axisDataArray.contains(label)){
						axisDataArray.add(dataObject.get("LABEL").toString());
					}
					if(lDataArray.contains(seriesname)){
						//如果已经读取过，就不再读取了
					}else{
						//如果未读取过，先放入lDataArray，再进行读取
						lDataArray.add(seriesname);
						int dataNumSize = data.size()-1;
						for(int dataNum=dataNumSize;dataNum>=0;dataNum--){
							JSONObject seriesDataObject = JSONObject.fromObject(data.get(dataNum));
							if(seriesname.equals(seriesDataObject.get("SERIESNAME"))){
								dataArr.add(seriesDataObject.get("VALUE"));
							}
						}
						seriesObj.put("name", seriesname);
						seriesObj.put("type", chartMap.get("seriesType") == null ? "bar" : chartMap.get("seriesType"));
						if("bar".equals(seriesObj.get("type"))) {
							if(rowMap.containsKey("seriesItemStyle")) {
							//	itemStyle : { normal: {label : {show: true, position: 'insideRight'}}}
								JSONObject labelObj = new JSONObject();
								labelObj.put("show", true);
								labelObj.put("position", "insideRight");
								
								JSONObject normalObj = new JSONObject();
								normalObj.put("label", labelObj);
								
								JSONObject itemStyleObj = new JSONObject();
								itemStyleObj.put("normal", normalObj);
								
								seriesObj.put("itemStyle", itemStyleObj);
							}
						}
						if(rowMap.containsKey("STACK")){
							HashMap stackMap = (HashMap)rowMap.get("STACK");
							if(stackMap.get(seriesname)!=null){
								seriesObj.put("stack", stackMap.get(seriesname));
							}
						}
						seriesObj.put("data", dataArr);
						seriesArray.add(seriesObj);
					}
				}
			}
			
		}
		//-----放入X轴属性
		JSONObject xAxisType = new JSONObject();
		xAxisType.put("type", chartMap.get("xAxisType"));
		
		//-----放入Y轴属性
		JSONObject yAxisData = new JSONObject();
		yAxisData.put("type", chartMap.get("yAxisType"));
	//	yAxisData.put("data", yAxisDataArray);
		
		if("category".equals(chartMap.get("xAxisType"))) {
			xAxisType.put("data", axisDataArray);
		} else {
			yAxisData.put("data", axisDataArray);
		}
		
		optionTipObj.put("xAxis", xAxisType);
		optionTipObj.put("yAxis", yAxisData);
		
		//-----放入标题
		JSONObject legendObj = new JSONObject();
		legendObj.put("y", "bottom");
		legendObj.put("data", lDataArray);
		optionTipObj.put("legend", legendObj);
		optionTipObj.put("series", seriesArray);
		
		// 定义数据属性结束
		return optionTipObj.toString();
	}
	
	
	/**
	 * 生成图表颜色对象
	 * @param chartMap
	 * @param rowMap
	 * @return
	 */
	public static JSONArray makeChartColorObject(HashMap chartMap) {
		JSONArray colorArray = new JSONArray();
		if(chartMap != null && chartMap.containsKey("COLOR")) {
			List list = (List)chartMap.get("COLOR");
			Iterator it = list.iterator();
			int i=1;
			while(it.hasNext()){
				colorArray.add(it.next());
				i++;
			}
		} else {
			colorArray.add(chartColor1);//chartWarnColor4
			colorArray.add(chartColor2);
			colorArray.add(chartColor3);
			colorArray.add(chartColor4);
			colorArray.add(chartColor5);
			colorArray.add(chartColor6);
		}
		return colorArray;
	}
}
