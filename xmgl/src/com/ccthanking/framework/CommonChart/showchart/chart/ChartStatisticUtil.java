package com.ccthanking.framework.CommonChart.showchart.chart;
/*
 *
 *
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ccthanking.framework.common.DBUtil;

public class ChartStatisticUtil
{
	//ChartUtil chartUtil0 = new ChartUtil();
    /**
     * 将数值转换成宽度值
     * @param data double[][] 数据
     * @return String[][] 返回宽度值
     */
	 /* public String[][] getChartWidth(double[][] data,int part){
	    	int leftMargin = 0;
	    	int rightMargin = 739;
	    	float per = 0.255f;//3.03f;//柱宽与柱间隔的比例
	    	float per1 = 0;
	    	int index = 0;
	    	String[][] width = new String[2][data[0].length];//要计算的宽度值,柱左边界和柱右边界

	    	for(int i=0;i<part;i++){
	    		long[] datas = chartUtil.getDataPart(data[0],part,i+1);

	    		per1 = datas.length*per+datas.length-1;

	        	long max = datas[0];
	        	for(int j=1;j<datas.length;j++){
	        		if(datas[j] > max){
	        			max = datas[j];
	        		}
	        	}
	        	if(max<10){
	        		leftMargin = 72;
	        	}else if(max<100-4){//max<=95
	        		leftMargin = 69;
	        	}else if(max<1000-47){//max<=952
	        		leftMargin = 75;
	        	}else if(max<10000-476){//max<=9523
	        		leftMargin = 83;
	        	}else if(max<100000-4761){//max<=95238
	        		leftMargin = 89;
	        	}else if(max<1000000-47619){//max<=952380
	        		leftMargin = 95;
	        		rightMargin = 740;
	        	}else if(max<10000000-476190){//max<=9523809
	        		leftMargin = 103;
	        		rightMargin = 740;
	        	}else if(max<100000000-4761904){//max<=95238095
	        		leftMargin = 109;
	        		rightMargin = 741;
	        	}else if(max<1000000000-47619047){//max<=952380952
	        		leftMargin = 115;
	        		rightMargin = 741;
	        	}else{
	        		leftMargin = 123;
	        		rightMargin = 741;
	        	}

	        	float barMargin = (rightMargin-leftMargin)/per1;
	        	float barWidth = barMargin * per;

	        	for(int ei=0;ei<datas.length;ei++){
	        		width[0][index] = String.valueOf(Math.round(leftMargin+(barWidth+barMargin)*ei));
	        		width[1][index] = String.valueOf(Math.round(leftMargin+(barWidth+barMargin)*ei+barWidth));
	        		index++;
	        	}
	    	}
			return width;
	    }*/

	/*  求犯罪统计表中的坐标横轴地区内容
	 *
	 *  目前根据要求写死内容，以后如需再行修改或添加
	 *
	 *
	 */
	    public String[] getAreaColContent()
	    {
	    	String[] areaColContent = {"沈阳","大连","鞍山","抚顺","本溪","丹东","锦州","营口","阜新","辽阳","盘锦","铁岭","朝阳","葫芦岛"};
	    	return areaColContent;
	    }
	    /*  求治安统计表中的坐标横轴内容
	     *
	     *  目前根据要求写死内容，以后如需再行修改或添加
	     *
	     *
	     */
	    public String[] getPolicyColContent()
	    {
	    	String[] policyColContent = {"简易程序","一般案件","结案","行政拘留(人次)"};
	    	return policyColContent;
	    }
	    /*  求犯罪统计表中的坐标横轴内容
	     *
	     *  目前根据要求写死内容，以后如需再行修改或添加
	     *
	     *
	     */
	    public String[] getCrimeColContent()
	    {
	    	String[] crimeColContent = {"受案","立案","破案","传唤(人次)","刑事拘留(人次)"};
	    	return crimeColContent;
	    }

	    /*
	     * 求犯罪统计表中每个地区的每个指标数的坐标横轴内容
	     */
	     public String[] getCrimeNumByCol(String queryDate,String[] queryContent) throws SQLException
	     {
	    	Connection conn = DBUtil.getConnection();
	    	String sqlContent = "";
	    	for(int i =0 ;i<queryContent.length;i++)
	    	{
	    		if(i==queryContent.length-1)
	    		{
	    			sqlContent = sqlContent+"nvl(sum("+queryContent[i]+"),0)";
	    		}
	    		else
	    		{
	    			sqlContent = sqlContent+"nvl(sum("+queryContent[i]+"),0),";
	    		}
	    	}
	    	String sql = "Select "+ sqlContent +
	    				 " from statistic_table where to_date(to_char(registerdate,'yyyymmdd'),'yyyymmdd') = to_date('"+queryDate+"','yyyymmdd')";
	    	PreparedStatement ps = conn.prepareStatement(sql);
	    	ResultSet rs = ps.executeQuery();
	    	String[] numBycol = new String[queryContent.length];//new String[9];
	    	rs.next();
	    	for(int i=0;i<numBycol.length;i++)
	    	{
	    		numBycol[i] = rs.getString(i+1);
	    	}
	    	rs.close();
	    	ps.close();
	    	conn.close();
	    	return numBycol;
	     }
	    /*
	     * 求犯罪统计表中每个地区的每个指标数的坐标横轴内容
	     */
	     public String[] getAreaNumByCol(String colName,String queryDate) throws SQLException
	     {
	    	Connection conn = DBUtil.getConnection();
	    	String sql = "select "+colName+" from statistic_table where to_date(to_char(registerdate,'yyyymmdd'),'yyyymmdd')=to_date("+queryDate+",'yyyymmdd') group by zonecode,"+colName+" order by zonecode";
	    	PreparedStatement ps = conn.prepareStatement(sql);
	    	ResultSet rs = ps.executeQuery();
	    	String[] numBycol = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};//new String[14];
	    	int i = 0;
	    	while(rs.next())
	    	{
	    		numBycol[i] = rs.getString(1);
	    		i++;
	    	}
	    	rs.close();
	    	ps.close();
	    	conn.close();
	    	return numBycol;
	     }
	     /*  求交通统计表中的坐标横轴内容
		     *
		     *  目前根据要求写死内容，以后如需再行修改或添加
		     *
		     *
		     */
		    public String[] getTafficColContent()
		    {
		    	String[] crimeColContent = {"事故次数(起)","死亡人数(人)","受伤人数(人)","财产损失(万元)"};
		    	return crimeColContent;
		    }
		     /*  求火灾统计表中的横轴内容
		     *
		     *  目前根据要求写死内容，以后如需再行修改或添加
		     *
		     *
		     */
		    public String[] getFireColContent()
		    {
		    	String[] crimeColContent = {"事故次数(起)","受灾人数(人)","受伤人数(人)","死亡人数(人)","财产损失(万元)"};
		    	return crimeColContent;
		    }
		     /*  求接处警统计表中的横轴内容
		     *
		     *  目前根据要求写死内容，以后如需再行修改或添加
		     *
		     *
		     */
		    public String[] getPoliceActivityColContent()
		    {
		    	String[] crimeColContent = {"治安案件数","刑事案件数","交通案件数","求助案件数","其他案件数"};
		    	return crimeColContent;
		    }
		    /*
		     * 求交通统计表中每个地区的每个指标数的坐标横轴内容
		     */
		     public String[] getTrafficNumByCol() throws SQLException
		     {
		    	String[] numBycol = {"0","0","0","0"};//new String[9];
		    	return numBycol;
		     }
		     /**
		      * 将数值转换成高度值
		      * @param data double[][] 数据
		      * @param part int 图表部分数
		      * @return String[] 返回高度值
		      */

		     /*public String[] getCrimeChartHeight(double[][] data,int part,int base,int top){
		     	String[] height = new String[data[0].length];//要计算的高度值

		     	int index = 0;
		     	int basicValue = (int)(base - (base-top)*0.03);//柱的最低高度

		     	for(int i=0;i<part;i++){
		     		long[] datas = chartUtil.getDataPart(data[0],part,i+1);

		     		//取最大值
		     		long max = datas[0];
		         	for(int j=1;j<datas.length;j++){
		         		if(datas[j] > max){
		         			max = datas[j];
		         		}
		         	}

		         	if(max>0){
		             	for(int j=0;j<datas.length;j++){
		             		if(datas[j]==0){
		             			height[index] = String.valueOf(base);
		             		}else {
		                 		datas[j] = (int)(base - (base-top)*datas[j]/max);
		                 		if(datas[j] > basicValue){
		                 			datas[j] = basicValue;
		                 		}else if(datas[j] < top){
		                             datas[j] = base;
		                         }
		                 		height[index] = String.valueOf(datas[j]);
		             		}
		                     index++;
		             	}
		         	}else {
		         		for(int j=0;j<datas.length;j++){
		         			height[index++] = String.valueOf(base);
		             	}
		         	}
		     	}
		 		return height;
		     }*/

		     /*
		      * 将2个1维数组合并成一个二维数组
		      * 将第一个数组拆分作为二维数组的首个元素
		      */
//		     public String[][] unionArray(String[] key,String[] value)
//		     {
//		    	 String[][] newArray = null;
//		    	 return null;
//		     }
		     /*
		      * 获取当前日期  yyyy-mm-dd
		      * */

		     public String getCurrentDate()
		     {
		    	Calendar ca = Calendar.getInstance();
		 		java.util.Date currentDate = ca.getTime();
		 		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		 		String queryDate = sdf1.format(currentDate).toString();
		    	return queryDate;
		     }

		     /*
		      * 取得每个地区的各项指标值
		      *
		      */
		     public String[][] getDataValueByArea(String queryDate,String[] queryContent,String endDate) throws SQLException
		     {
		    	 Connection conn = DBUtil.getConnection();
			    	String sqlContent = "";
			    	for(int i =0 ;i<queryContent.length;i++)
			    	{
			    		if(i==queryContent.length-1)
			    		{
			    			sqlContent = sqlContent+"nvl(sum("+queryContent[i]+"),0)";
			    		}
			    		else
			    		{
			    			sqlContent = sqlContent+"nvl(sum("+queryContent[i]+"),0),";
			    		}
			    	}
			    	String whereCondition = "";
			    	if(queryDate.equals(endDate))
					{
						whereCondition = "where to_date(to_char(registerdate,'yyyymmdd'),'yyyymmdd') = to_date('"+queryDate+"','yyyymmdd')";
					}
					else
					{
						whereCondition = "where to_date(to_char(registerdate,'yyyymmdd'),'yyyymmdd') between to_date('"+queryDate+"','yyyymmdd') and to_date('"+endDate+"','yyyymmdd')";
					}
			    	String sql = "Select "+ sqlContent +
			    				 " from statistic_table "+whereCondition+" group by zonecode order by zonecode";
			    	PreparedStatement ps = conn.prepareStatement(sql);
			    	ResultSet rs = ps.executeQuery();
			    	String [] initValue = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
			    	String[][] numBycol = new String[queryContent.length][14];//new String[9];
			    	/*for(int arrayNum=0;arrayNum<numBycol.length;arrayNum++)
			    	{
			    		numBycol[arrayNum] = initValue;
			    	}*/
			    	int i = 0 , j = 0;
			    	while(rs.next())
			    	{
			    		for(int num=0; num<queryContent.length;num++)
			    		{
			    			numBycol[num][i] = rs.getString(num+1);
			    		}
			    		i++;
			    	}
			    	rs.close();
			    	ps.close();
			    	conn.close();
			    	return numBycol;
		     }
		     /*
		      * 将2个数组合并成一个2维数据
		      * 一维数组做维新数组的首个元素
		      */
		     public String[][] unionArray(String[] contentKey,String[][] contentValue)
		     {
		    	 String[][] newArray = new String[contentKey.length][contentValue[0].length+1];
		    	 for(int i=0;i<contentKey.length;i++)
		    	 {
		    		 for(int j=0;j<newArray[i].length;j++)
		    		 {
		    			 if(j==0)
		    			 {
		    				 newArray[i][j] = contentKey[i];
		    			 }
		    			 else
		    			 {
		    				 newArray[i][j] = contentValue[i][j-1];
		    			 }
		    		 }
		    	 }
		    	 return newArray;
		     }
		    
		     /*
		      * 将字符串拆分成数组 含有-
		      *
		      */
		     public String[] splitString(String stringValue)
		     {
		    	 String[] newArray = new String[3];
		    	 int i =0;
		    	 while(stringValue.indexOf("-")!=-1)
		    	 {
		    		 newArray[i] = stringValue.substring(0,stringValue.indexOf("-"));
		    		 stringValue = stringValue.substring(stringValue.indexOf("-")+1,stringValue.length());
		    		 i++;
		    	 }
		    	 newArray[i] = stringValue;
		    	 return newArray;
		     }
		     /*
		      * 根据输入的字段名称找出这个字段中当天的最大值
		      *
		      */
		     public int[] searchMaxValueByGroup(String[] arrayValue,String queryDate,String endDate) throws SQLException
		     {
		    	 int maxInt[] = new int[arrayValue.length];
		    	 String sqlContent = "";
	    		 for(int i =0 ;i<arrayValue.length;i++)
		    	 {
		    		if(i==arrayValue.length-1)
		    		{
		    			sqlContent = sqlContent+"max(to_number(sum("+arrayValue[i]+")))";
		    		}
		    		else
		    		{
		    			sqlContent = sqlContent+"max(to_number(sum("+arrayValue[i]+"))),";
		    		}
		    	 }
    			 String whereCondition = "";
		    	 if(queryDate.equals(endDate))
				 {
				   	 whereCondition = "where to_date(to_char(registerdate,'yyyymmdd'),'yyyymmdd') = to_date('"+queryDate+"','yyyymmdd')";
				 }
				 else
				 {
					 whereCondition = "where to_date(to_char(registerdate,'yyyymmdd'),'yyyymmdd') between to_date('"+queryDate+"','yyyymmdd') and to_date('"+endDate+"','yyyymmdd')";
				 }
	    		 String sql = "Select "+ sqlContent +
				 " from statistic_table "+whereCondition + "group by zonecode";
	    		 Connection conn = DBUtil.getConnection();
	    		 PreparedStatement ps = conn.prepareStatement(sql);
	    		 ResultSet rs = ps.executeQuery();
	    		 rs.next();
	    		 for(int i =1;i<=arrayValue.length;i++)
	    		 {
	    			 maxInt[i-1] = rs.getInt(i);
	    		 }
	    		 rs.close();
			     ps.close();
				 conn.close();
	    		 return maxInt;
		     }
		     /*
		      * 取出一维数组中最大的数值
		      */
		     public int searchMaxValue(int[] value)
		     {
		    	 Arrays.sort(value);
		    	 return value[value.length-1];
		     }
		     /*
		      * 按照传入的参数设置其对应的数值
		      * 如：15 则 将其设置为20 ，240 则将其设置为300
		      */
		     public String setMaxScopeValue(String value)
		     {
		    	 String headValue = "0";
		    	 if(value.length()==1 && Integer.parseInt(value)<=5)
		    	 {
		    		 headValue = "5";
		    	 }
		    	 else if(value.length()==1 && Integer.parseInt(value)>5)
		    	 {
		    		 headValue = "10";
		    	 }
		    	 else
		    	 {
			    		headValue = String.valueOf((Integer.parseInt(value.substring(0,1))+1));
			    		for(int i=1;i<value.length();i++)
			    		{
			    			headValue=headValue+"0";
			    		}
		    	 }
		    	 return headValue;
		     }
}
