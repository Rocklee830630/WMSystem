package com.ccthanking.framework.CommonChart.showchart.chart;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * <p>图表制作相关类</p>
 * <p>2009年7月15日</p>
 * <p>Copyright: S.A.S.(c)2009</p>
 * <p>Company: Sky Art Studio</p>
 * @author Crystal
 * @version 1.2
 */
public class ChartUtil {
    public final String dateFormat_1 = "yyyyMMdd";
    public final String dateFormat_2 = "yyyy年MM月dd日";

    /**
     * 获取截止到当前省厅资源库数据总量
     * @return String 资源库数据总量
     */
    public String getResourceTotalNum(){
        String jzrq = "";
        String tn = "0";
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select nvl(sum(hj),0),to_char(sysdate-1,'fmyyyy\"年\"mm\"月\"dd\"日\"') from tjbxx a,tjbb b " +
            "where a.type!='0' and a.tablename=b.tjbm and b.cxtj=(select max(cxtj) from tjbb)";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                tn = rs.getString(1);
                jzrq = rs.getString(2);
            }
            rs.close();
            stmt.close();
            conn.close();

            DecimalFormat df = null;
            if(tn!=null&&tn.length()>=9){
                tn = tn.substring(0,tn.length()-4);
                df = new DecimalFormat("#.##亿");
            }else if(tn!=null&&tn.length()>=5){
                df = new DecimalFormat("#.##万");
            }else{
                df = new DecimalFormat();
            }
            double num = Double.parseDouble(tn)/10000;
            tn = df.format(num);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "截止到"+jzrq+"为止,省厅资源库数据总量约为"+tn+"条";
    }

    /**
     * 获取截止到当前省厅资源库各数据品种的数据总量(饼状图)
     * @param datatype String 数据类型:警综(jz)或省级(sj)
     * @return String[][] 数据品种的数据总量
     */
    public String[][] getResourceAllSum(String datatype){
        String clause = null;
        if(datatype.equals("jz")){
            clause = "id<100";
        }else if(datatype.equals("sj")){
            clause = "id>=100";
        }
        String[][] datas = null;
/*        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql_1 = "select count(*) as allsum from tjbxx a,tjbb b where a.tablename=b.tjbm and a."+clause+" and a.using='1' and b.cxtj=(select max(cxtj) from tjbb)";
        String sql_2 = "select a.tablecomment,b.hj from tjbxx a,tjbb b where a.tablename=b.tjbm and a."+clause+" and a.using='1' " +
            "and b.cxtj=(select max(cxtj) from tjbb) order by a.showindex,a.id";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql_1);
            if(rs.next()){
                int allsum = rs.getInt("allsum");
                datas = new String[2][allsum];
                rs = stmt.executeQuery(sql_2);
                if(rs.next()){
                    int di = 0;
                    do{
                        datas[0][di] = rs.getString(1);
                        datas[1][di] = rs.getString(2);
                        di++;
                    }while(rs.next());
                }else{
                    for(int i=0;i<datas.length;i++){
                        for(int j=0;j<datas[0].length;j++){
                            datas[i][j] = "0";
                        }
                    }
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        datas = new String[2][5];
        datas[0][0]= "数据一";
        datas[1][0]= "10";
        datas[0][1]= "数据二";
        datas[1][1]= "20";
        datas[0][2]= "数据三";
        datas[1][2]= "30";
        datas[0][3]= "数据四";
        datas[1][3]= "40";
        datas[0][4]= "数据五";
        datas[1][4]= "50";

        return datas;
    }

    /**
     * 获取截止到当前省厅资源库各数据品种的数据总量(柱状图)
     * @param datatype String 数据类型:警综(jz)或省级(sj)
     * @return String[][] 数据品种的数据总量
     */
    public String[][] getResourceAllSumData(String datatype){
        String clause = null;
        if(datatype.equals("jz")){
            clause = "id<100";
        }else if(datatype.equals("sj")){
            clause = "id>=100";
        }
        String[][] datas = null;
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql_1 = "select count(*) as allsum from tjbxx where using='1' and " + clause;
        String sql_2 = "select a.tablecomment bm,(select b.hj from tjbb b where b.tjbm=a.tablename and b.cxtj=" +
            "(select max(cxtj) from tjbb)) zs from tjbxx a where a.using='1' and a." + clause + " order by a.showindex,a.id";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql_1);
            if(rs.next()){
                int allsum = rs.getInt("allsum");
                datas = new String[2][allsum];
                rs = stmt.executeQuery(sql_2);
                if(rs.next()){
                     int di = 0;
                     do{
                         datas[0][di] = rs.getString("bm");
                         datas[1][di] = rs.getString("zs");
                         if(datas[1][di]==null){
                             datas[1][di] = "0";
                         }
                         di++;
                     }while(rs.next());
                }else{
                    for(int i=0;i<datas[0].length;i++){
                        datas[0][i] = "";
                        datas[1][i] = "";
                    }
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }

    /**
     * 获取昨天省厅资源库各数据品种的数据增量(柱状图)
     * @param datatype String 数据类型:警综(jz)或省级(sj)
     * @return String[][] 数据品种的数据总量
     */
    public String[][] getResourceAddSumData(String datatype){
        String clause = null;
        if(datatype.equals("jz")){
            clause = "id<100";
        }else if(datatype.equals("sj")){
            clause = "id>=100";
        }
        String[][] datas = null;
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql_1 = "select count(*) as allsum from tjbxx where using='1' and " + clause;
        String sql_2 = "select a.tablecomment bm,(select b.zlhj from tjbzl b where b.tjbm=a.tablename and b.cxtj=" +
            "to_char(sysdate-1,'yyyymmdd')) zs from tjbxx a where a.using='1' and a." + clause + " order by a.showindex,a.id";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql_1);
            if(rs.next()){
                int allsum = rs.getInt("allsum");
                datas = new String[2][allsum];
                rs = stmt.executeQuery(sql_2);
                if(rs.next()){
                     int di = 0;
                     do{
                         datas[0][di] = rs.getString("bm");
                         datas[1][di] = rs.getString("zs");
                         if(datas[1][di]==null){
                             datas[1][di] = "0";
                         }
                         di++;
                     }while(rs.next());
                }else{
                    for(int i=0;i<datas[0].length;i++){
                        datas[0][i] = "";
                        datas[1][i] = "";
                    }
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datas;
    }

    //数据监控->资源库数据趋势分析

    /**
     * 获取生成综合库数据总量/增量(柱状图)需要的表ID、表名称、地市代码字段
     * @return String[][] 返回表的ID、名称
     */
    public String[][] getColumnContents(){
        String[][] columns = new String[3][1];
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select * from (select count(*) from tjbxx where using='1')," +
            "(select id,tablecomment,dsidfield from tjbxx where using='1' order by id)";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            int ci = 0;
            if(rs.next()){
                ci = rs.getInt(1);
                columns = new String[3][ci];
            }
            if(ci>0){
                ci = 0;
                do{
                    columns[0][ci] = rs.getString("id");
                    columns[1][ci] = rs.getString("tablecomment");
                    columns[2][ci] = rs.getString("dsidfield");
                    ci++;
                }while(rs.next());
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    /**
     * 获取生成综合库数据总量/增量(柱状图)需要的数据
     * @param dataType String 数据类型:总量(zol)或增量(zel)
     * @param datas double[][] 存放数据的数组
     * @param date String 查询日期
     */
    public void getChartDatas(String dataType,String[][] datas,String date){
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        String clause = " where a.tjbm=b.tablename and b.using='1' and a.cxtj='" + date + "' order by b.id";
        if(dataType.equals("zol")){//总量查询语句
            sql = "select a.hj as a_hj from tjbb a,tjbxx b" + clause;
        }else if(dataType.equals("zel")){//增量查询语句
            sql = "select a.zlhj as a_hj from tjbzl a,tjbxx b" + clause;
        }

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            int ci = 0;
            int columnCount = datas[0].length;
            while(rs.next()&&ci<columnCount){
                datas[0][ci++] = rs.getString("a_hj");
            }
            rs.close();
            stmt.close();
            conn.close();

            if(datas[0][0]==null){
                for(int i=0;i<datas.length;i++){
                    for(int j=0;j<datas[0].length;j++){
                        datas[i][j] = "";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某天省厅资源库数据的总量总和和增量总和
     * @param date String 查询日期
     * @return long[] 返回总量总和和增量总和
     */
    public String[] getDataSum(String date){
        String[] dataSum = new String[2];
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String clause = " where a.cxtj='"+date+"' and a.tjbm=b.tablename and b.type!='0')";
        String sql = "select * from (select sum(hj) from tjbb a,tjbxx b " + clause + ",(select sum(zlhj) from tjbzl a,tjbxx b" + clause;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                dataSum[0] = rs.getString(1);
                dataSum[1] = rs.getString(2);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSum;
    }

    /**
     * 获取某种数据的总量
     * @param date String 查询日期
     * @param tableId String 表ID
     * @param data String[] 查询结果
     * @param tableType 表类型
     */
    public void getATableZolData(String date,String tableId,String[] data,String tableType){
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select a.* from tjbb a,tjbxx b where a.cxtj='" + date + "' and a.tjbm=b.tablename and b.id=" + tableId;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                data[0] = rs.getString("sy1");
                data[1] = rs.getString("dl1");
                data[11] = rs.getString("tl1");
                data[2] = rs.getString("as1");
                data[5] = rs.getString("dd1");
                data[4] = rs.getString("bx1");
                data[9] = rs.getString("ly1");
                data[12] = rs.getString("cy1");
                data[8] = rs.getString("fx1");
                data[6] = rs.getString("jz1");
                data[3] = rs.getString("fs1");
                data[7] = rs.getString("yk1");
                data[10] = rs.getString("pj1");
                data[13] = rs.getString("hld1");

                if(!tableType.equals("1")){
                    long allsum = rs.getLong("hj");
                    for(int i=0;i<14;i++){
                        allsum -= Long.parseLong(data[i]);
                    }
                    data[14] = String.valueOf(allsum);
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某种数据的增量
     * @param date String 查询日期
     * @param tableId String 表ID
     * @param data String[] 查询结果
     * @param tableType 表类型
     */
    public void getATableZelData(String date,String tableId,String[] data,String tableType){
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select a.* from tjbzl a,tjbxx b where a.cxtj='" + date + "' and a.tjbm=b.tablename and b.id=" + tableId;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                data[0] = rs.getString("sy2");
                data[1] = rs.getString("dl2");
                data[2] = rs.getString("as2");
                data[3] = rs.getString("fs2");
                data[4] = rs.getString("bx2");
                data[5] = rs.getString("dd2");
                data[6] = rs.getString("jz2");
                data[7] = rs.getString("yk2");
                data[8] = rs.getString("fx2");
                data[9] = rs.getString("ly2");
                data[10] = rs.getString("pj2");
                data[11] = rs.getString("tl2");
                data[12] = rs.getString("cy2");
                data[13] = rs.getString("hld2");

                if(!tableType.equals("1")){
                    long allsum = rs.getLong("zlhj");
                    for(int i=0;i<14;i++){
                        allsum -= Long.parseLong(data[i]);
                    }
                    data[14] = String.valueOf(allsum);
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取表的ID、表名、表中文名
     * @param tableId String 表ID
     * @return String[] 返回表名、表中文名、表统计类型
     */
    public String[] getTableInfo(String tableId){
        String[] tinfo = new String[3];
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select tablename,tablecomment,type from tjbxx where id=" + tableId;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                tinfo[0] = rs.getString("tablename");
                tinfo[1] = rs.getString("tablecomment").trim();
                tinfo[2] = rs.getString("type");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tinfo;
    }

    /**
     * 获取默认查询日期
     * @return String 返回昨天日期
     */
    public String getDefaultQueryDate(){
        String date = null;
        String sql = "select to_char(sysdate-1,'yyyy-mm-dd') from dual";
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                date = rs.getString(1);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将'yyyy-MM-dd'格式的日期转换成'yyyyMMdd'或'yyyy年MM月dd日'
     * @param date String 'yyyy-MM-dd'格式的日期
     * @param format String 日期格式
     * @return String 返回'yyyyMMdd'或'yyyy年MM月dd日'格式的日期
     */
    public String parseDate(String date,String format){
        String[] ori_dates = date.split("-");
        String newdate = null;
        if(format.equals(this.dateFormat_1)){
            newdate = ori_dates[0] + ori_dates[1] + ori_dates[2];//查询用
        }else if(format.equals(this.dateFormat_2)){
            if(ori_dates.length==3){
            if(ori_dates[1].charAt(0)=='0'){
                ori_dates[1] = ori_dates[1].substring(1);
            }
            if(ori_dates[2].charAt(0)=='0'){
                ori_dates[2] = ori_dates[2].substring(1);
            }
            newdate = ori_dates[0] + "年" + ori_dates[1] + "月" + ori_dates[2] + "日";//显示用
            }
        }
        return newdate;
    }

    //数据监控->资源库数据趋势分析
    
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
     * 生成饼图json串
     * @param domresult	数据结果集
     * @param chartMap	图表属性
     * @param rowMap	数据属性
     * @return
     */
	public static String makePieChartJsonString(String domresult,
			HashMap chartMap, HashMap rowMap) {
		JSONObject chartColorObject = makeChartColorObject(chartMap,rowMap);
		JSONObject resultObj = new JSONObject();
		// 定义图表属性开始
		JSONObject chartObj = new JSONObject();
		chartObj.put("pieRadius", "70");
		chartObj.put("palette", "4");
		chartObj.put("decimals", "0");
		chartObj.put("enableSmartLabels", "1");	//影响表格是否能分离，为1时，可以分离，并且显示指示线
		chartObj.put("enableRotation", "1");	//为1时，饼图可以旋转
		chartObj.put("bgColor", "#FFFFFF");
		chartObj.put("bgAngle", "360");
		chartObj.put("showBorder", "0");
		chartObj.put("startingAngle", "70");
		chartObj.put("baseFont", "微软雅黑");
		chartObj.put("baseFontSize", "14");
		chartObj.put("showFCMenuItem", "0");
		chartObj.put("formatNumberScale", "0");
		chartObj.put("use3DLighting", "0");
		chartObj.put("showShadow", "0");
		// ---添加或覆盖传入的自定义属性
		if (chartMap != null) {
			Iterator it = chartMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				if (chartObj.containsKey(e.getKey())) {
					chartObj.remove(e.getKey()); // 删除旧的属性
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				} else {
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				}
			}
		}
		// 定义图表属性结束
		// 定义数据属性开始
		JSONArray dataArray = new JSONArray();
		if (domresult != "0") {
			JSONObject jsono = JSONObject.fromObject(domresult);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			int x = 0;
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
						rowObject.put("LABEL", dataObject.get(rowMap
								.get("LABEL")));
					} else {
						rowObject.put("LABEL", dataObject.get("LABEL"));
					}
					// 如果value起了别名，那么取别名对应的值
					if (rowMap.containsKey("VALUE")) {
						rowObject.put("VALUE", dataObject.get(rowMap
								.get("VALUE")));
					} else {
						rowObject.put("VALUE", dataObject.get("VALUE"));
					}
					//如果有点击事件，那么加入点击事件方法
					if(rowMap.containsKey("LINKFUNCTION")&&!"0".equals(dataObject.get("VALUE"))){
						//如果存在参数，那么要把参数拼到方法中
						if(rowMap.containsKey("LINKPARAM")){
							List paramList = new ArrayList();
							paramList = (ArrayList)rowMap.get("LINKPARAM");
							Iterator paraIt = paramList.iterator();
							String paraStr = "";
							while(paraIt.hasNext()){
								String oneParam = "";
								String oneItValue = paraIt.next().toString();
								if(dataObject.containsKey(oneItValue)){
									oneParam = "'"+(String)dataObject.get(oneItValue)+"'";
								}else{
									oneParam = (String)paraIt.next();
								}
								paraStr += oneParam+",";
							}
							
							paraStr = paraStr.length()==0?"":paraStr.substring(0,paraStr.length()-1);
							rowObject.put("link", rowMap.get("LINKFUNCTION")+"("+paraStr+")");
						}else{
							rowObject.put("link", rowMap.get("LINKFUNCTION")+"()");
						}
					}
				} else {
					rowObject.put("LABEL", dataObject.get("LABEL"));
					rowObject.put("VALUE", dataObject.get("VALUE"));
				}
				if (x == 0) {
					if (data.size() == 2) {
						if ("未完成".equals(rowObject.get("LABEL"))) {
							rowObject.put("color", chartColor6);
						} else if ("已完成".equals(rowObject.get("LABEL"))) {
							rowObject.put("color", chartColor2);
						} else {
							rowObject.put("color", chartColorObject.get("chartColor1"));
						}
					} else {
						rowObject.put("color", chartColorObject.get("chartColor1"));
					}
				} else if (x == 1) {
					if (data.size() == 2) {
						if ("未完成".equals(rowObject.get("LABEL"))) {
							rowObject.put("color", chartColor6);
						} else if ("已完成".equals(rowObject.get("LABEL"))) {
							rowObject.put("color", chartColor2);
						} else {
							rowObject.put("color", chartColorObject.get("chartColor2"));
						}
					} else {
						rowObject.put("color", chartColorObject.get("chartColor2"));
					}
				} else if (x == 2) {
					rowObject.put("color", chartColorObject.get("chartColor3"));
				} else if (x == 3) {
					rowObject.put("color", chartColorObject.get("chartColor4"));
				} else if (x == 4) {
					rowObject.put("color", chartColorObject.get("chartColor5"));
				}
				dataArray.add(rowObject);
				x++;
			}
		}
		// 定义数据属性结束
		// 向返回值中加入图表和数据
		resultObj.put("chart", chartObj);
		resultObj.put("data", dataArray);
		return resultObj.toString();
	}
	/**
     * 生成三级饼图json串
     * @param domresult	数据结果集
     * @param chartMap	图表属性
     * @param rowMap	数据属性
     * @return
     */
	public static String makeMultiLevelPieChartJsonString(String domresult,
			HashMap chartMap, HashMap rowMap) {
		JSONObject chartColorObject = makeChartColorObject(chartMap,rowMap);
		JSONObject resultObj = new JSONObject();
		// 定义图表属性开始
		JSONObject chartObj = new JSONObject();
		chartObj.put("pieRadius", "100");
		chartObj.put("palette", "4");
		chartObj.put("decimals", "0");
		chartObj.put("enableSmartLabels", "1");	//影响表格是否能分离，为1时，可以分离，并且显示指示线
		chartObj.put("enableRotation", "1");	//为1时，饼图可以旋转
		chartObj.put("bgColor", "#FFFFFF");
		chartObj.put("bgAngle", "360");
		chartObj.put("showBorder", "0");
		chartObj.put("startingAngle", "70");
		chartObj.put("baseFont", "微软雅黑");
		chartObj.put("baseFontSize", "12");
		chartObj.put("showFCMenuItem", "0");
		chartObj.put("formatNumberScale", "0");
		chartObj.put("use3DLighting", "0");
		chartObj.put("showShadow", "0");
		// ---添加或覆盖传入的自定义属性
		if (chartMap != null) {
			Iterator it = chartMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				if (chartObj.containsKey(e.getKey())) {
					chartObj.remove(e.getKey()); // 删除旧的属性
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				} else {
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				}
			}
		}
		// 定义图表属性结束
		// 定义数据属性开始
		JSONArray dataArray = new JSONArray();
		JSONArray categoryArrayOne = new JSONArray();
		JSONArray categoryArrayTwo = new JSONArray();
		if (!domresult.equals("0")) {
			JSONObject jsono = JSONObject.fromObject(domresult);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			int x = 0;
			List categoryList = new ArrayList();
			HashMap categoryMap = new HashMap();
			for (int k = 0;k<data.size();k++){
				JSONObject dataObject = JSONObject.fromObject(data.get(k));
				JSONObject rowObject = new JSONObject();
				HashMap categoryParam = new HashMap();
				List categoryParamList = new ArrayList();
				if(!categoryList.contains(dataObject.get("CATEGORY"))){
					categoryList.add(dataObject.get("CATEGORY"));
					//如果有点击事件，那么加入点击事件方法
					if(rowMap.containsKey("CATEGORYFUNCTION")){
						//如果存在参数，那么要把参数拼到方法中
						if(rowMap.containsKey("CATEGORYPARAM")){
							List paramList = new ArrayList();
							paramList = (ArrayList)rowMap.get("CATEGORYPARAM");
							Iterator paraIt = paramList.iterator();
							String paraStr = "";
							while(paraIt.hasNext()){
								String oneParam = "";
								String oneItValue = paraIt.next().toString();
								if(dataObject.containsKey(oneItValue)){
									oneParam = "'"+(String)dataObject.get(oneItValue)+"'";
								}else{
									oneParam = (String)paraIt.next();
								}
								paraStr += oneParam+",";
							}
							
							paraStr = paraStr.length()==0?"":paraStr.substring(0,paraStr.length()-1);
							rowObject.put("link", rowMap.get("CATEGORYFUNCTION")+"("+paraStr+")");
						}else{
							rowObject.put("link", rowMap.get("CATEGORYFUNCTION")+"()");
						}
					}
					categoryMap.put((String)dataObject.get("CATEGORY"), rowObject);
				}
			}
			Iterator categoryIt = categoryList.iterator();
			BigDecimal captionValue = new BigDecimal(0);
			while(categoryIt.hasNext()){
				String categoryLabel = (String)categoryIt.next();
				JSONArray oneAttr = new JSONArray();
				String chartColor = "";
				if(x==0){
					chartColor = chartColorObject.getString("chartColor1");
				}else if(x==1){
					chartColor = chartColorObject.getString("chartColor2");
				}else if(x==2){
					chartColor = chartColorObject.getString("chartColor3");
				}else if(x==3){
					chartColor = chartColorObject.getString("chartColor4");
				}else if(x==4){
					chartColor = chartColorObject.getString("chartColor5");
				}
				BigDecimal categoryValue = new BigDecimal(0);
				for (int i = 0; i < data.size(); i++) {
					JSONObject dataObject = JSONObject.fromObject(data.get(i));
					JSONObject rowObject = new JSONObject();
					if(categoryLabel.equals(dataObject.get("CATEGORY"))){
						// ---添加或覆盖传入的自定义属性
						if (rowMap != null) {
							// 如果label起了别名，那么取别名对应的值
							if (rowMap.containsKey("LABEL")) {
								rowObject.put("LABEL", dataObject.get(rowMap
										.get("LABEL")));
							} else {
								rowObject.put("LABEL", dataObject.get("LABEL")+","+dataObject.get("VALUE"));
							}
							// 如果value起了别名，那么取别名对应的值
							if (rowMap.containsKey("VALUE")) {
								rowObject.put("VALUE", dataObject.get(rowMap
										.get("VALUE")));
								rowObject.put("TOOLTEXT", dataObject.get(rowMap
										.get("VALUE")));
							} else {
								categoryValue = categoryValue.add(new BigDecimal((String)dataObject.get("VALUE")));
								rowObject.put("VALUE", dataObject.get("VALUE"));
								rowObject.put("TOOLTEXT", dataObject.get("LABEL")+","+dataObject.get("VALUE"));
							}
							//如果有点击事件，那么加入点击事件方法
							if(rowMap.containsKey("LINKFUNCTION")){
								//如果存在参数，那么要把参数拼到方法中
								if(rowMap.containsKey("LINKPARAM")){
									List paramList = new ArrayList();
									paramList = (ArrayList)rowMap.get("LINKPARAM");
									Iterator paraIt = paramList.iterator();
									String paraStr = "";
									while(paraIt.hasNext()){
										String oneParam = "";
										String oneItValue = paraIt.next().toString();
										if(dataObject.containsKey(oneItValue)){
											oneParam = "'"+(String)dataObject.get(oneItValue)+"'";
										}else{
											oneParam = (String)paraIt.next();
										}
										paraStr += oneParam+",";
									}
									
									paraStr = paraStr.length()==0?"":paraStr.substring(0,paraStr.length()-1);
									rowObject.put("link", rowMap.get("LINKFUNCTION")+"("+paraStr+")");
								}else{
									rowObject.put("link", rowMap.get("LINKFUNCTION")+"()");
								}
							}
						} else {
							rowObject.put("LABEL", dataObject.get("LABEL"));
							rowObject.put("VALUE", dataObject.get("VALUE"));
						}
						rowObject.put("color", chartColor);
						oneAttr.add(rowObject);
					}
				}
				//加入category属性之中
				JSONObject twoCategory = new JSONObject();
				twoCategory.put("label", categoryLabel+","+categoryValue);
				twoCategory.put("value", categoryValue);
				twoCategory.put("alpha", "20");
				twoCategory.put("color", chartColor);
				twoCategory.put("category", oneAttr);
				JSONObject tjo = (JSONObject)categoryMap.get(categoryLabel);
				twoCategory.put("link", tjo.get("link"));
				categoryArrayTwo.add(twoCategory);
//				captionValue += categoryValue;
				captionValue = captionValue.add(categoryValue);
				x++;
			}
			//加入最外层栏目属性之中
			JSONObject oneCategory = new JSONObject();
			String caption = rowMap.containsKey("caption")?(String)rowMap.get("caption"):"";
			oneCategory.put("label", caption+","+captionValue);
			oneCategory.put("color", chartWarnColor1);
			oneCategory.put("alpha", "100");
			oneCategory.put("value", captionValue);
			oneCategory.put("toolText", caption+","+captionValue);
			oneCategory.put("category", categoryArrayTwo);
			categoryArrayOne.add(oneCategory);
		}
		// 定义数据属性结束
		// 向返回值中加入图表和数据
		resultObj.put("chart", chartObj);
		resultObj.put("category", categoryArrayOne);
		return resultObj.toString();
	}
	/**
	 * 生成单一柱形图JSON串
	 * @param domresult
	 * @param chartMap
	 * @param rowMap
	 * @return
	 */
	public static String makeColumn2DChartJsonString(String domresult,HashMap chartMap,HashMap rowMap){
		JSONObject resultObj = new JSONObject();
		// 定义图表属性开始
		JSONObject chartObj = new JSONObject();
		chartObj.put("plotgradientcolor", "");
		chartObj.put("showplotborder", "0");
//		chartObj.put("xaxisname", "月份");x轴标题
		chartObj.put("divlinealpha", "20");
		chartObj.put("canvasborderthickness", "0");
		chartObj.put("canvasbordercolor", "#DDDDDD");
		chartObj.put("rotateyaxisname", "0");
		chartObj.put("baseFont", "微软雅黑");
		chartObj.put("basefontsize", "12");
		chartObj.put("bgcolor", "#FFFFFF");
		chartObj.put("labeldisplay", "WRAP");
		chartObj.put("outcnvbasefontsize", "13");
		chartObj.put("showvalues", "0");
		chartObj.put("showborder", "0");
		chartObj.put("showlegend", "0");
		chartObj.put("shownames", "1");
		chartObj.put("canvasborderalpha", "100");
		chartObj.put("showalternatehgridcolor", "0");
		chartObj.put("showyaxisvalues", "1");
		chartObj.put("formatnumberscale", "0");
		chartObj.put("showvalues", "1");		//显示每个小柱的值
		chartObj.put("placeValuesInside", "0");	//数字是否在柱子里面显示
//		chartObj.put("numbersuffix", "万元");//后缀
		// ---添加或覆盖传入的自定义属性
		if (chartMap != null) {
			Iterator it = chartMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				if (chartObj.containsKey(e.getKey())) {
					chartObj.remove(e.getKey()); // 删除旧的属性
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				} else {
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				}
			}
		}
		// 定义图表属性结束
		// 定义数据属性开始
		double maxValue = 0;
		JSONArray dataArray = new JSONArray();
		if (domresult != "0") {
			JSONObject jsono = JSONObject.fromObject(domresult);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			int x = 0;
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
						rowObject.put("LABEL", dataObject.get(rowMap
								.get("LABEL")));
					} else {
						rowObject.put("LABEL", dataObject.get("LABEL"));
					}
					// 如果value起了别名，那么取别名对应的值
					if (rowMap.containsKey("VALUE")) {
						rowObject.put("VALUE", dataObject.get(rowMap
								.get("VALUE")));
					} else {
						rowObject.put("VALUE", dataObject.get("VALUE"));
					}
				} else {
					rowObject.put("LABEL", dataObject.get("LABEL"));
					rowObject.put("VALUE", dataObject.get("VALUE"));
				}
			/*	if(Integer.parseInt((String)dataObject.get("VALUE"))>maxValue){
					maxValue = Integer.parseInt((String)dataObject.get("VALUE"));//找到记录中的最大值
				}*/
				
				// update by xiahongbo on 2014-09-22  (String)dataObject.get("VALUE")有可能是小数或超长的整数
				BigDecimal val = new BigDecimal((String)dataObject.get("VALUE"));
				BigDecimal maxVal = new BigDecimal(maxValue);
				int result = val.compareTo(maxVal);
				if(result > 1) {
					maxValue = val.doubleValue();//找到记录中的最大值
				}
				
				rowObject.put("color", chartColor4);
				dataArray.add(rowObject);
				x++;
			}
		}
		// 定义数据属性结束
		// 向返回值中加入图表和数据
		chartObj.put("yAxisMaxValue", Math.floor(maxValue*1.15));//提升Y轴最大值，防止没有数字的位置
		resultObj.put("chart", chartObj);
		resultObj.put("data", dataArray);
		return resultObj.toString();
	}
	
	/**
	 * 生成图表颜色对象
	 * @param chartMap
	 * @param rowMap
	 * @return
	 */
	public static JSONObject makeChartColorObject(HashMap chartMap,HashMap rowMap){
		JSONObject chartColorObject = new JSONObject();
		if(rowMap==null && chartMap==null){
			chartColorObject.put("chartColor1", chartColor1);
			chartColorObject.put("chartColor2", chartColor2);
			chartColorObject.put("chartColor3", chartColor3);
			chartColorObject.put("chartColor4", chartColor4);
			chartColorObject.put("chartColor5", chartColor5);
			chartColorObject.put("chartColor6", chartColor6);
		}else if(chartMap!=null && chartMap.get("WARNING")!=null){
			chartColorObject.put("chartColor1", chartWarnColor1);
			chartColorObject.put("chartColor2", chartWarnColor2);
			chartColorObject.put("chartColor3", chartWarnColor3);
			chartColorObject.put("chartColor4", chartWarnColor4);
			chartColorObject.put("chartColor5", chartWarnColor5);
		}else if(rowMap!=null && rowMap.get("COLOR")!=null){
			List list = (List)rowMap.get("COLOR");
			Iterator it = list.iterator();
			int i=1;
			while(it.hasNext()){
				chartColorObject.put("chartColor"+i, it.next());
				i++;
			}
		}else{
			chartColorObject.put("chartColor1", chartColor1);
			chartColorObject.put("chartColor2", chartColor2);
			chartColorObject.put("chartColor3", chartColor3);
			chartColorObject.put("chartColor4", chartColor4);
			chartColorObject.put("chartColor5", chartColor5);
			chartColorObject.put("chartColor6", chartColor6);
		}
		return chartColorObject;
	}
	/**
	 * 生成叠加柱形图
	 * @param domresult
	 * @param chartMap
	 * @param rowMap
	 * @return
	 */
	public static String makeMSStackedColumn2DChartJsonString(String domresult,HashMap chartMap,HashMap rowMap){
		JSONObject chartColorObject = makeChartColorObject(chartMap,rowMap);
		JSONObject resultObj = new JSONObject();
		// 定义图表属性开始
		JSONObject chartObj = new JSONObject();
		
		// 定义图表属性开始
		chartObj.put("plotgradientcolor", "");
		chartObj.put("showplotborder", "0");
		chartObj.put("xaxisname", "");		//X轴标题
		chartObj.put("yaxisname", "");		//Y轴标题
		chartObj.put("numdivlines", "3");
//		chartObj.put("numberprefix", "");	//数据前缀
//		chartObj.put("numbersuffix", "个");	//数据后缀
		chartObj.put("showsum", "1");
//		chartObj.put("useroundedges", "1");	//光线效果，拥有此属性后，会有光照效果，属性为0时，会有渐变效果
		chartObj.put("divlinealpha", "20");
		chartObj.put("canvasborderthickness", "0");
		chartObj.put("canvasbordercolor", "#DDDDDD");
		chartObj.put("rotateyaxisname", "0");
		chartObj.put("bgcolor", "#FFFFFF");
		chartObj.put("labeldisplay", "WRAP");
		chartObj.put("outcnvbasefontsize", "13");
		chartObj.put("showborder", "0");
		chartObj.put("showlegend", "0");
		chartObj.put("shownames", "1");
		chartObj.put("canvasborderalpha", "100");
		chartObj.put("showalternatehgridcolor", "0");
		chartObj.put("showyaxisvalues", "1");	//显示Y轴标尺
		chartObj.put("formatnumberscale", "0");
		chartObj.put("baseFont", "微软雅黑");
		chartObj.put("baseFontSize", "12");
		chartObj.put("showvalues", "1");		//显示每个小柱的值
//		chartObj.put("outCnvbaseFontColor", "009933");//设置y轴字体颜色
		
		
		// ---添加或覆盖传入的自定义属性
		if (chartMap != null) {
			Iterator it = chartMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				if (chartObj.containsKey(e.getKey())) {
					chartObj.remove(e.getKey()); // 删除旧的属性
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				} else {
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				}
			}
		}
		// 定义图表属性结束
		// 定义数据属性开始
		JSONArray categoriesArray = new JSONArray();
		JSONArray datasetArray = new JSONArray();
		if (domresult != "0") {
			JSONObject jsono = JSONObject.fromObject(domresult);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			int x = 0;
			//生成category属性-----------------------------------开始
			JSONObject category = new JSONObject();
			JSONArray label = new JSONArray();
			JSONObject dataLabel = new JSONObject();
			List list = new ArrayList();
			String breakNum = rowMap==null||rowMap.get("BREAKNUM")==null?"100":(String)rowMap.get("BREAKNUM");//-------需要分列的序号
			String totalNum = rowMap==null||rowMap.get("TOTALNUM")==null?"100":(String)rowMap.get("TOTALNUM");//-------总叠加的数量
			String flag=rowMap==null?null:(String)rowMap.get("flag");//-------计划跟踪特殊处理

			int i_breakNum = 1;
			int i_totalNum = Integer.parseInt(totalNum);
			for(int i = 0; i < data.size(); i++){
				JSONObject dataObject = JSONObject.fromObject(data.get(i));
				JSONObject rowObject = new JSONObject();
				if(list==null || !list.contains(dataObject.get("LABEL"))){
					//保证label的值不重复
					list.add(dataObject.get("LABEL"));
					rowObject.put("label", dataObject.get("LABEL"));
					label.add(rowObject);
				}
			}
			dataLabel.put("category", label);
			dataLabel.put("font", "微软雅黑");
			dataLabel.put("fontsize", "12");
			dataLabel.put("fontcolor", "000000");
			category.put("category", dataLabel);
			categoriesArray.add(dataLabel);
			//生成category属性-----------------------------------结束
			//加入data属性----------------------------------------开始
			List seriesnameList = new ArrayList();
			JSONObject dataset =  new JSONObject();
			JSONArray dataseta = new JSONArray();
			for(int m=0;m<data.size();m++){
				JSONObject dataValue = new JSONObject();//最内层data
				JSONObject dataObject = JSONObject.fromObject(data.get(m));
				JSONArray value = new JSONArray();
				JSONObject valueO = new JSONObject();
				if(seriesnameList==null || !seriesnameList.contains(dataObject.get("SERIESNAME"))){
					//保证label的值不重复
					seriesnameList.add(dataObject.get("SERIESNAME"));
					dataValue.put("seriesname", dataObject.get("SERIESNAME"));
										
					for(int n=0;n<data.size();n++){
						JSONObject rowObject = JSONObject.fromObject(data.get(n));
						if(rowObject.get("SERIESNAME").equals(dataObject.get("SERIESNAME"))){
							valueO.put("value", "0".equals(rowObject.get("VALUE"))?"":rowObject.get("VALUE"));//为0的不显示
							if(seriesnameList.size()==1){
								if("未完成".equals(rowObject.get("SERIESNAME"))){
									valueO.put("color",chartColor6);
								}else if("完成".equals(rowObject.get("SERIESNAME"))){
									valueO.put("color",chartColor2);
								}else{
									valueO.put("color", chartColorObject.get("chartColor1"));
								}
							}else if(seriesnameList.size()==2){
								if("完成".equals(rowObject.get("SERIESNAME"))){
									valueO.put("color",chartColor2);
								}else if("未完成".equals(rowObject.get("SERIESNAME"))){
									valueO.put("color",chartColor6);
								}else{
									valueO.put("color", chartColorObject.get("chartColor2"));
								}
							}else if(seriesnameList.size()==3){
								valueO.put("color", chartColorObject.get("chartColor3"));
							}else if(seriesnameList.size()==4){
								valueO.put("color", chartColorObject.get("chartColor4"));
							}else if(seriesnameList.size()==5){
								valueO.put("color", chartColorObject.get("chartColor5"));
							}
							//如果有点击事件，那么加入点击事件方法
							if(rowMap!=null && rowMap.containsKey("LINKFUNCTION")){
								//如果存在参数，那么要把参数拼到方法中
								if(rowMap.containsKey("LINKPARAM")){
									List paramList = new ArrayList();
									paramList = (ArrayList)rowMap.get("LINKPARAM");
									Iterator paraIt = paramList.iterator();
									String paraStr = "";
									while(paraIt.hasNext()){
										String oneParam = "";
										String oneItValue = paraIt.next().toString();
										if(rowObject.containsKey(oneItValue)){
											oneParam = "'"+(String)rowObject.get(oneItValue)+"'";
										}else{
											oneParam = oneItValue;
										}
										paraStr += oneParam+",";
									}
									
									paraStr = paraStr.length()==0?"":paraStr.substring(0,paraStr.length()-1);
									valueO.put("link", rowMap.get("LINKFUNCTION")+"("+paraStr+")");
								}else{
									valueO.put("link", rowMap.get("LINKFUNCTION")+"()");
								}
							}
							value.add(valueO);
							dataValue.put("data", value);	//放入data JSONArray
						}
					}
					if(!Pub.empty(breakNum)) {
						// 柱形图多次折行的处理 add by xiahongbo on 2014-09-23
						if("every".equals(breakNum)) {
							if(m<i_breakNum){
								if(chartColorObject.size()>0){
									String color=chartColorObject.get("chartColor"+(m+1)).toString();
									dataValue.put("color", color.substring(1, color.length()));									
								}
								if(!Pub.empty(flag)&&flag.equals("1")&&i_breakNum==2)
								{	
									dataseta.add(dataValue);		//放入value JSONArray
									dataset.put("dataset", dataseta);
									if(m==i_totalNum-1){
										datasetArray.add(dataset);
									}
								}	
								else
								{

									if(chartColorObject.size()>0){
										String color=chartColorObject.get("chartColor"+(m+1)).toString();
										dataValue.put("color", color.substring(1, color.length()));
									}
									dataseta.add(dataValue);		//放入value JSONArray
									dataset.put("dataset", dataseta);
									if(m==i_breakNum-1){
										datasetArray.add(dataset);
										dataseta = new JSONArray();
										dataset = new JSONObject();
									}									
								}								
							}else{
								if(chartColorObject.size()>0){
									String color=chartColorObject.get("chartColor"+(m+1)).toString();
									dataValue.put("color", color.substring(1, color.length()));									
								}
								dataseta.add(dataValue);		//放入value JSONArray
								dataset.put("dataset", dataseta);
								if(m==i_totalNum-1){
									datasetArray.add(dataset);
								}
							}
							i_breakNum++;
							i_totalNum++;
						} else {
							if(m<Integer.parseInt(breakNum)){
								if(chartColorObject.size()>0){
									String color=chartColorObject.get("chartColor"+(m+1)).toString();
									dataValue.put("color", color.substring(1, color.length()));									
								}
								dataseta.add(dataValue);		//放入value JSONArray
								dataset.put("dataset", dataseta);
								if(m==Integer.parseInt(breakNum)-1){
									datasetArray.add(dataset);
									dataseta = new JSONArray();
									dataset = new JSONObject();
								}
							}else{
								if(chartColorObject.size()>0){
									String color=chartColorObject.get("chartColor"+(m+1)).toString();
									dataValue.put("color", color.substring(1, color.length()));									
								}
								dataseta.add(dataValue);		//放入value JSONArray
								dataset.put("dataset", dataseta);
								if(m==Integer.parseInt(totalNum)-1){
									datasetArray.add(dataset);
								}
							}
						}
					}else{
						if(chartColorObject.size()>0){
							String color=chartColorObject.get("chartColor"+(m+1)).toString();
							dataValue.put("color", color.substring(1, color.length()));									
						}
						dataseta.add(dataValue);		//放入value JSONArray
						dataset.put("dataset", dataseta);
					}
				}
			}
			if(datasetArray.isEmpty()){
				datasetArray.add(dataset);
			}
			//加入data属性----------------------------------------结束
		}
		// 定义数据属性结束
		// 向返回值中加入图表和数据
		resultObj.put("chart", chartObj);
		resultObj.put("categories", categoriesArray);
		resultObj.put("dataset", datasetArray);
		return resultObj.toString();
	}
	/**
	 * 生成折线图
	 * @param domresult
	 * @param chartMap
	 * @param rowMap
	 * @return
	 */
	public static String makeMSLineChartJsonString(String domresult,HashMap chartMap,HashMap rowMap){
		JSONObject resultObj = new JSONObject();
		// 定义图表属性开始
		JSONObject chartObj = new JSONObject();
		
		// 定义图表属性开始
//		chartObj.put("caption", "XXXXXX统计");	//标题
//		chartObj.put("subcaption", "XXXXXX统222计");	//标题
		chartObj.put("lineThickness", "1");
		chartObj.put("showValues", "0");
		chartObj.put("formatNumberScale", "0");
		chartObj.put("anchorRadius", "2");
		chartObj.put("divlineAlpha", "20");
		chartObj.put("divlinecolor", "CC3300");
		chartObj.put("showAlternateHGridColor", "1");
		chartObj.put("alternateHGridAlpha", "5");
		chartObj.put("shadowAlpha", "40");
		chartObj.put("labelStep", "1");
		chartObj.put("numvdivlines", "5");
		chartObj.put("chartRightMargin", "35");
		chartObj.put("bgcolor", "FFFFFF");
		chartObj.put("showborder", "0");
		chartObj.put("bgAngle", "270");
		chartObj.put("bgAlpha", "10,10");
		chartObj.put("rotateYAxisName", "0");
		chartObj.put("canvasBorderThickness", "0");
		chartObj.put("canvasBorderColor", "#DDDDDD");
		chartObj.put("showAlternateHGridColor", "0");
		chartObj.put("baseFont", "微软雅黑");
		chartObj.put("baseFontSize", "12");
		chartObj.put("xaxisname", "");		//X轴标题
		chartObj.put("yaxisname", "");		//Y轴标题
//		chartObj.put("numberprefix", "");	//数据前缀
//		chartObj.put("numbersuffix", "个");	//数据后缀
		
		// ---添加或覆盖传入的自定义属性
		if (chartMap != null) {
			Iterator it = chartMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				if (chartObj.containsKey(e.getKey())) {
					chartObj.remove(e.getKey()); // 删除旧的属性
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				} else {
					chartObj.put(e.getKey(), e.getValue()); // 插入新的属性
				}
			}
		}
		// 定义图表属性结束
		// 定义数据属性开始
		JSONArray categoriesArray = new JSONArray();
		JSONArray datasetArray = new JSONArray();
		JSONObject styleObj = new JSONObject();
		if (domresult != "0") {
			JSONObject jsono = JSONObject.fromObject(domresult);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			int x = 0;
			//生成category属性-----------------------------------开始
			JSONObject category = new JSONObject();
			JSONArray label = new JSONArray();
			JSONObject dataLabel = new JSONObject();
			List list = new ArrayList();
			String breakNum = rowMap==null?null:(String)rowMap.get("BREAKNUM");//-------需要分列的序号
			String totalNum = rowMap==null?null:(String)rowMap.get("TOTALNUM");//-------总叠加的数量 
			for(int i = 0; i < data.size(); i++){
				JSONObject dataObject = JSONObject.fromObject(data.get(i));
				JSONObject rowObject = new JSONObject();
				if(list==null || !list.contains(dataObject.get("LABEL"))){
					//保证label的值不重复
					list.add(dataObject.get("LABEL"));
					rowObject.put("label", dataObject.get("LABEL"));
					label.add(rowObject);
				}
			}
			dataLabel.put("category", label);
//			dataLabel.put("font", "Arial");
//			dataLabel.put("fontsize", "12");
//			dataLabel.put("fontcolor", "000000");
			category.put("category", dataLabel);
			categoriesArray.add(dataLabel);
			//生成category属性-----------------------------------结束
			//加入data属性----------------------------------------开始
			List seriesnameList = new ArrayList();
			JSONObject dataset =  new JSONObject();
			JSONArray dataseta = new JSONArray();
			for(int m=0;m<data.size();m++){
				JSONObject dataValue = new JSONObject();//最内层data
				JSONObject dataObject = JSONObject.fromObject(data.get(m));
				JSONArray value = new JSONArray();
				JSONObject valueO = new JSONObject();
				if(seriesnameList==null || !seriesnameList.contains(dataObject.get("SERIESNAME"))){
					//保证label的值不重复
					seriesnameList.add(dataObject.get("SERIESNAME"));
					dataValue.put("seriesname", dataObject.get("SERIESNAME"));
					if(seriesnameList.size()==1){
						dataValue.put("color", chartColor1);
						dataValue.put("anchorbordercolor", chartColor1);
						dataValue.put("anchorbgcolor", chartColor1);
					}else if(seriesnameList.size()==2){
						dataValue.put("color", chartColor2);
						dataValue.put("anchorbordercolor", chartColor2);
						dataValue.put("anchorbgcolor", chartColor2);
					}else if(seriesnameList.size()==3){
						dataValue.put("color", chartColor3);
						dataValue.put("anchorbordercolor", chartColor3);
						dataValue.put("anchorbgcolor", chartColor3);
					}else if(seriesnameList.size()==4){
						dataValue.put("color", chartColor4);
						dataValue.put("anchorbordercolor", chartColor4);
						dataValue.put("anchorbgcolor", chartColor4);
					}else if(seriesnameList.size()==5){
						dataValue.put("color", chartColor5);
						dataValue.put("anchorbordercolor", chartColor5);
						dataValue.put("anchorbgcolor", chartColor5);
					}
					for(int n=0;n<data.size();n++){
						JSONObject rowObject = JSONObject.fromObject(data.get(n));
						if(rowObject.get("SERIESNAME").equals(dataObject.get("SERIESNAME"))){
							valueO.put("value", rowObject.get("VALUE"));
							value.add(valueO);
							dataValue.put("data", value);	//放入data JSONArray
						}
					}
					if(!Pub.empty(breakNum)){
						if(m<Integer.parseInt(breakNum)){
							dataseta.add(dataValue);		//放入value JSONArray
							dataset.put("dataset", dataseta);
							if(m==Integer.parseInt(breakNum)-1){
								datasetArray.add(dataset);
								dataseta = new JSONArray();
								dataset = new JSONObject();
							}
						}else{
							dataseta.add(dataValue);		//放入value JSONArray
							dataset.put("dataset", dataseta);
							if(m==Integer.parseInt(totalNum)-1){
								datasetArray.add(dataset);
							}
						}
					}else{
						dataseta.add(dataValue);		//放入value JSONArray
						dataset.put("dataset", dataseta);
					}
				}
			}
			if(datasetArray.isEmpty()){
				datasetArray.add(dataseta);
			}
			//加入data属性----------------------------------------结束
			
			//加入styles属性--------------------------------------开始
			JSONArray definition = new JSONArray();
			JSONObject defObj = new JSONObject();
			defObj.put("name", "CaptionFont");
			defObj.put("type", "font");
			defObj.put("size", "12");
			definition.add(defObj);
			
			JSONArray application = new JSONArray();
			JSONObject appCaObj = new JSONObject();
			appCaObj.put("toobject", "CAPTION");
			appCaObj.put("styles", "CaptionFont");
			application.add(appCaObj);
			JSONObject appSubCaObj = new JSONObject();
			appSubCaObj.put("toobject", "SUBCAPTION");
			appSubCaObj.put("styles", "CaptionFont");
			application.add(appSubCaObj);
			styleObj.put("definition", definition);
			styleObj.put("application", application);
			//加入styles属性--------------------------------------结束
		}
		
		// 定义数据属性结束
		// 向返回值中加入图表和数据
		resultObj.put("chart", chartObj);
		resultObj.put("categories", categoriesArray);
		resultObj.put("dataset", datasetArray);
		resultObj.put("styles", styleObj);
		return resultObj.toString();
	}
	/**
	 * 生成EChart饼图
	 * @param domresult
	 * @param chartMap
	 * @param rowMap
	 * @return
	 */
	public static String makePieEChartJsonString(String domresult,HashMap chartMap,HashMap rowMap){
		//处理颜色
		JSONObject chartColorObject = makeChartColorObject(chartMap,rowMap);
		// 定义图表属性开始
		JSONObject optionTipObj = new JSONObject();
		//-----放入提示框
		JSONObject toolTipObj = new JSONObject();
		toolTipObj.put("show", true);
		toolTipObj.put("formatter", "{a} <br/>{b} : {c} ({d}%)");
		optionTipObj.put("tooltip", toolTipObj);
		//-----放入颜色
		JSONArray colorArray = new JSONArray();
		colorArray.add(chartColorObject.get("chartColor1"));//chartWarnColor4
		colorArray.add(chartColorObject.get("chartColor2"));
		colorArray.add(chartColorObject.get("chartColor3"));
		optionTipObj.put("color", colorArray);
		//-----放入工具栏
		JSONObject toolboxObj = new JSONObject();
		toolboxObj.put("show", true);
		optionTipObj.put("toolboxObj", toolboxObj);
		//-----放入calculable
		optionTipObj.put("calculable", false);
		//-----放入series
		JSONArray seriesArray = new JSONArray();
		JSONObject seriesObj = new JSONObject();
		//------------生成常规显示
		JSONArray radiusArr = new JSONArray();
		radiusArr.add("60%");
		radiusArr.add("80%");
		JSONObject itemStyleObj = new JSONObject();
		JSONObject normalObj = new JSONObject();
		JSONObject nlabelObj = new JSONObject();
		nlabelObj.put("position", "inner");
		nlabelObj.put("formatter", "{c}");
		nlabelObj.put("show", false);	//控制环中是否显示value
		normalObj.put("label", nlabelObj);
		JSONObject nlabelLineObj = new JSONObject();
		nlabelLineObj.put("show", false);
		normalObj.put("labelLine", nlabelLineObj);
		itemStyleObj.put("normal", normalObj);
		//-----------生成鼠标移上显示
		JSONObject emphasisObj = new JSONObject();
		JSONObject elabelObj = new JSONObject();
		elabelObj.put("show", false);	//控制鼠标移上时，环中间是否出现数字
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
		legendObj.put("show", true);
		legendObj.put("orient", "vertical");
		legendObj.put("x", "left");
		legendObj.put("data", lDataArray);
		optionTipObj.put("legend", legendObj);
		
		seriesObj.put("name", "开工情况");
		seriesObj.put("type", "pie");
		seriesObj.put("radius", radiusArr);
		seriesObj.put("itemStyle", itemStyleObj);
		seriesObj.put("data", dataArr);
		seriesObj.put("legendHoverLink", true);
		/**
		 * 这段内容用于生成标注，倒立的水滴一样，内部是数字，移上有tooltip
		JSONArray markPointDataArray = new JSONArray();
		for(int m=0;m<dataArr.size();m++){
			JSONObject markPointDataObj = (JSONObject)dataArr.get(m);
			markPointDataObj.remove("COLOR");
			markPointDataObj.put("x", 50);
			markPointDataObj.put("y", 100);
			markPointDataArray.add(markPointDataObj);
		}
		JSONObject markPointData = new JSONObject();
		markPointData.put("data", markPointDataArray);
		seriesObj.put("markPoint", markPointData);
		*/
//		seriesObj.put("selectedMode", "single");//选中模式--单选，点击环形的一部分，环形区会从整体中移出
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
		//处理颜色
		JSONObject chartColorObject = makeChartColorObject(chartMap,rowMap);
		// 定义图表属性开始
		JSONObject optionTipObj = new JSONObject();
		//-----放入提示框
		JSONObject toolTipObj = new JSONObject();
		toolTipObj.put("trigger", "axis");
		optionTipObj.put("tooltip", toolTipObj);
		//-----放入颜色
		JSONArray colorArray = new JSONArray();
		if(rowMap!=null && rowMap.containsKey("color")){
			for(int i=0;i<chartColorObject.size();i++){
				colorArray.add(chartColorObject.get("chartColor"+i));
			}
		}else{
			colorArray.add(chartColor4);//chartWarnColor4
			colorArray.add(chartColor2);
			colorArray.add(chartColor3);
		}
		optionTipObj.put("color", colorArray);
		//-----放入工具栏
		JSONObject toolboxObj = new JSONObject();
		toolboxObj.put("show", true);
		optionTipObj.put("toolboxObj", toolboxObj);
		//-----放入calculable
		optionTipObj.put("calculable", false);
		
		JSONObject gridObj = new JSONObject();
		gridObj.put("x", "120px");
		optionTipObj.put("grid", gridObj);
		//-------------------------放入series
		JSONArray seriesArray = new JSONArray();
		//-------------------------放入X轴属性
		JSONArray xAxis = new JSONArray();
		JSONObject xAxisType = new JSONObject();
		xAxisType.put("type", "value");
		xAxis.add(xAxisType);
		optionTipObj.put("xAxis", xAxis);
		//------------生成数据
		// 定义数据属性开始

		JSONArray lDataArray = new JSONArray();
		JSONArray yAxisDataArray = new JSONArray();
		JSONArray linkArr = new JSONArray();
		if (domresult != "0") {
			JSONObject jsono = JSONObject.fromObject(domresult);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			//-------------------下面这段用于获取有效的label
			boolean effectiveFlag = true;//用于后续判断的标志位，无意义
			List<String> effectiveList = new ArrayList<String>();
			if(chartMap.containsKey("EFFECTIVE")){
				effectiveFlag = false;
				for(int dataNum=data.size()-1;dataNum>=0;dataNum--){
					JSONObject seriesDataObject = JSONObject.fromObject(data.get(dataNum));
					if(!"0".equals(seriesDataObject.get("VALUE"))){
						effectiveList.add(seriesDataObject.get("LABEL").toString());
					}
				}
			}
			//--------------------获取有效label结束
			int size = data.size() -1;
			for (int i = size; i >=0 ; i--) {
				JSONObject seriesObj = new JSONObject();
				JSONArray dataArr = new JSONArray();
				JSONObject dataObject = JSONObject.fromObject(data.get(i));
				JSONObject rowObject = new JSONObject();
				// ---添加或覆盖传入的自定义属性
				String label = dataObject.get("LABEL").toString();
				String seriesname = dataObject.get("SERIESNAME").toString();
				if(!yAxisDataArray.contains(label)){
					if(effectiveFlag){
						yAxisDataArray.add(dataObject.get("LABEL").toString());
					}else{
						if(effectiveList.contains(label)){
							yAxisDataArray.add(dataObject.get("LABEL").toString());
						}else{
							continue;
						}
					}
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
							if(effectiveFlag){
								dataArr.add(seriesDataObject.get("VALUE"));
							}else{
								if(effectiveList.contains(seriesDataObject.get("LABEL"))){
									dataArr.add(seriesDataObject.get("VALUE"));
								}
							}
						}
					}
					seriesObj.put("name", seriesname);
					seriesObj.put("type", "bar");
					if(rowMap.containsKey("STACK")){
						HashMap stackMap = (HashMap)rowMap.get("STACK");
						if(stackMap.get(seriesname)!=null){
							seriesObj.put("stack", stackMap.get(seriesname));
						}
					}
					seriesObj.put("data", dataArr);
					seriesArray.add(seriesObj);
				}
				if(rowMap!=null && rowMap.containsKey("LINKFUNCTION")){
					if(rowMap.containsKey("LINKPARAM")){
						List paramList = new ArrayList();
						paramList = (ArrayList)rowMap.get("LINKPARAM");
						Iterator paraIt = paramList.iterator();
						String paraStr = "";
						while(paraIt.hasNext()){
							String oneParam = "";
							String oneItValue = paraIt.next().toString();
							if(dataObject.containsKey(oneItValue)){
								oneParam = "'"+(String)dataObject.get(oneItValue)+"'";
							}else{
								oneParam = oneItValue;
							}
							paraStr += oneParam+",";
						}
						
						paraStr = paraStr.length()==0?"":paraStr.substring(0,paraStr.length()-1);
						linkArr.add(rowMap.get("LINKFUNCTION")+"("+paraStr+")");
					}else{
						linkArr.add(rowMap.get("LINKFUNCTION")+"()");
					}
				}
			}
		}
		JSONObject yAxisData = new JSONObject();
		yAxisData.put("data", yAxisDataArray);
		yAxisData.put("type", "category");
		//-------------------------放入axis
		JSONObject splitLine = new JSONObject();
		splitLine.put("show", false);
		yAxisData.put("splitLine", splitLine);
		
		optionTipObj.put("yAxis", yAxisData);
		//-----放入标题
		JSONObject legendObj = new JSONObject();
		JSONArray legendArray = new JSONArray();
		if(lDataArray.size()>2){
			legendArray.add(lDataArray.get(2));
		}
		
		if(lDataArray.size()>0){
		legendArray.add(lDataArray.get(0));
		legendArray.add(lDataArray.get(1));	
		}
		if(chartMap!=null && chartMap.containsKey("legend-x")){
			legendObj.put("x", chartMap.get("legend-x"));
		}
		optionTipObj.put("link", linkArr);
		legendObj.put("data", legendArray);
		optionTipObj.put("legend", legendObj);
		optionTipObj.put("series", seriesArray);
		
		if(lDataArray.size()<=0){
			optionTipObj.clear();
		}
		System.out.println(optionTipObj.toString());
		// 定义数据属性结束
		return optionTipObj.toString();
	}
	
	/**
	 * 生成甘特图
	 * @param domresult
	 * @param chartMap
	 * @param rowMap
	 * @return
	 * @throws ParseException 
	 */
	public static String makeGanttEChartJsonString(String[][] data,String[] names,String max,String min) throws ParseException{
		String mouth[]={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
		String title[]={"计划开始","计划结束","实际开始","实际结束"};
		
		String font="微软雅黑";//字体样式
		String fontSize="16";//字体样式
		
		String json="<chart showshadow='0'   taskbarfillmix='{light-0}' font='"+font+"' fontsize='"+fontSize+"' manageresize='1' dateFormat='yyyy-mm-dd'  ganttwidthpercent='58' " +
				"canvasbordercolor='999999' " +//整体图形外边框颜色
				"canvasborderthickness='' " +//整体图形外边框厚度
				"canvasbgcolor='edf1f9' " +	//右侧任务背景色，如果用两个颜色，则颜色从左向右横向渐变
				"gridbordercolor='FFFFFF' " +//左侧表格线条
				"gridborderalpha='100' " +//左侧表格线条透明度
				"ganttLineColor='FFFFFF'" +//右侧表格线条
				" ganttpaneduration='14' ganttpanedurationunit='m' baseFont='微软雅黑' >";
		String yearJson="<categories  font='"+font+"' fontsize='"+fontSize+"' bgcolor='4567aa' fontcolor='ffffff' fontsize='15' isbold='1' align='center' hoverbandcolor='EEEEEE' hoverbandalpha='20'>";
		String mouthJson="<categories  font='"+font+"' fontsize='"+fontSize+"' bgcolor='4567aa' fontcolor='ffffff' fontsize='14' isbold='1' align='center' hoverbandcolor='EEEEEE' hoverbandalpha='20'>";
		String nameJson="<processes  font='"+font+"' fontsize='"+fontSize+"' headerfontsize='"+fontSize+"' headertext='形象进度' bgcolor='edf1f9' headerbordercolor='FFFFFF'       isanimated='1'  headervalign='middle' headeralign='center' headerbgcolor='4567aa' headerfontcolor='ffffff' headerfontsize='14' align='left' isbold='1' hoverbandcolor='869cc8' hoverbandalpha='25'>";
		String titledata="<datatable showprocessname='1' bgcolor='BCE0E0' namealign='left' fontcolor='000000' fontsize='13'  valign='right' align='center' headervalign='middle' headeralign='center' headerbgcolor='4567aa' headerfontcolor='ffffff' headerfontsize='14'>";
		String titletime="";
		String taskdata="";
		String milestonesDate="";
		String sjStartcolor="9BBB59";//实际图形前半部颜色
		String sjEndcolor="FFFFFF";//实际图形后半部颜色
		String milestonesShape="triangular";//图标的形状
		String milestonesNumSides="3";//图标的属性
		String milestonesRadius="5";//图标的半径
		String milestonescolor="333333";//图标的颜色
		String milestonesborderColor="FFFFFF";//图标的边线颜色
		
		
		int maxnd=Integer.parseInt(max.substring(0, 4));
		int maxyf=Integer.parseInt(max.substring(5,7 ));
		int minnd=Integer.parseInt(min.substring(0, 4));
		int minyf=Integer.parseInt(min.substring(5,7 ));
		//shape="triangular" numSides="3";
		String sjMiddle="";
	/*	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
		String dateBegin=fmt.format(carrierCommand.getDateBegin()); 
		String dateEnd=fmt.format(carrierCommand.getDateEnd()); */
		//如果获得的日期格式不是'2008-05-22',就必须要格式化一下日期 
		/*String dateBegin = request.getParameter("dateBegin"); 
		String dateEnd = request.getParameter("dateEnd"); */
		//Date maxDate = new SimpleDateFormat("yyyy-MM-dd").parse(max.substring(0, 10)); 
		//Date minDate = new SimpleDateFormat("yyyy-MM-dd").parse(min.substring(0, 10));
		//Date maxDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-05-01"); 
		//Date minDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-01");
		String start="";String end="";int nf;
		for(int nfnum=0;nfnum<(maxnd-minnd+1);nfnum++)
		{
			if(minyf+nfnum==1)//最小月份为1月份
			{
				start=(minnd-1)+"-12-1'";
				nf=minnd-1;	
			}
			else
			{
				if(minnd+nfnum==minnd)//最小月份非1月份
				{
					start=minnd+"-"+(minyf-1)+"-1";	
					nf=minnd;
				}
				else//最小月份其他情况
				{
					start=minnd+nfnum+"-1-1";
					nf=minnd+nfnum;
				}	
			}
			if(minnd+nfnum==maxnd)//最小年度和最大年度为同一年
			{
				if(maxyf==12)//最大月份为12月
				{
					end=(maxnd+1)+"-1-1";
				}
				else//最大月份非12月
				{
					end=maxnd+"-"+(maxyf+1)+"-1";					
				}					
			}
			else//其他情况
			{
				end=(minnd+nfnum+1)+"-1-1";
			}	
			yearJson+="<category start='"+start+"'   end='"+end+"' label='"+nf+"' />";			
		}
		yearJson+="</categories>";
		int mouthnum = (maxnd-minnd)*12+maxyf-minyf;
		int nd=minnd;
		for(int num=0;num<mouthnum+3;num++)
		{
			if((minyf+num-2)%12==11)
			{
				//mouthJson+="<category start='"+nd+"-12-1"+"' end='"+(nd+1)+"-1-1"+"' label='"+nd+"   "+mouth[(minyf+num-2)%12]+"' />";
				mouthJson+="<category start='"+nd+"-12-1"+"' end='"+(nd+1)+"-1-1"+"' label='"+mouth[(minyf+num-2)%12]+"' />";
				nd++;
			}
			else
			{
				if((minyf+num)==1)
				{
					//mouthJson+="<category start='"+(nd-1)+"-12-1"+"' end='"+nd+"-1-1"+"' label='"+(nd-1)+"   "+mouth[11]+"' />";
					mouthJson+="<category start='"+(nd-1)+"-12-1"+"' end='"+nd+"-1-1"+"' label='"+mouth[11]+"' />";
				}
				else
				{
					//mouthJson+="<category start='"+nd+"-"+((minyf+num-2)%12+1)+"-1"+"' end='"+nd+"-"+((minyf+num-2)%12+2)+"-1"+"' label='"+nd+"   "+mouth[(minyf+num-2)%12]+"' />";
					mouthJson+="<category   start='"+nd+"-"+((minyf+num-2)%12+1)+"-1"+"' end='"+nd+"-"+((minyf+num-2)%12+2)+"-1"+"' label='"+mouth[(minyf+num-2)%12]+"' />";	
				}	
			}								
		}
		mouthJson+="</categories>";		
		for(int i=0;i<names.length;i++)
		{
			nameJson+="<process label='"+names[i]+"' id='"+(i+1)+"' />";
		}
		nameJson+="</processes>";
		for(int l=0;l<title.length;l++)
		{
			titletime+="<datacolumn headertext='"+title[l]+"' isbold='1'>";
			for(int i=0;i<names.length;i++)
			{
				if(null!=data[0][(i*4)+l]&&!data[0][(i*4)+l].equals(""))
				{
					titletime+="<text label='"+data[0][(i*4)+l].substring(0, 10)+"'/>";					
				}
				else
				{
					titletime+="<text label='' />";			
				}	
			}
			titletime+="</datacolumn>";
		}
		titledata+=titletime+"</datatable>";
		taskdata+="<tasks>";
		milestonesDate+="<milestones>";
		String jhstart="",jhend="",sjstart="",sjend="";
		for(int m=0;m<data[0].length-2;m=m+4)
		{
			if(null!=data[0][m]&&!data[0][m].equals(""))
			{
				jhstart=data[0][m].substring(0,10);
				if(null!=data[0][m+1]&&!data[0][m+1].equals(""))
				{
					jhend=data[0][m+1].substring(0,10);
				}
				else
				{
					jhend=jhstart;
				}	
				taskdata+="<task label='计划' processid='"+((m/4)+1)+"' start='"+jhstart+"'   end='"+jhend+"' id='"+((m/4)+1)+"-1' color='4F81BD' height='4' bordercolor='4F81BD' toppadding='12%'/>";				
				milestonesDate+="<milestone date='"+jhstart+"' taskid='"+((m/4)+1)+"-1' radius='"+milestonesRadius+"' color='4F81BD' bordercolor='4F81BD' shape='"+milestonesShape+"' numsides='"+milestonesNumSides+"' borderthickness='1' />";
				milestonesDate+="<milestone date='"+jhend+"' taskid='"+((m/4)+1)+"-1' radius='"+milestonesRadius+"' color='4F81BD' bordercolor='4F81BD' shape='"+milestonesShape+"' numsides='"+milestonesNumSides+"' borderthickness='1' />";
				//milestonesDate+="<milestone date='"+jhstart+"' taskid='"+((m/4)+1)+"' radius='5' color='333333' bordercolor='FFFFFF' shape='"+milestonesShape+"' numsides='"+milestonesNumSides+"' borderthickness='1' />";
				//milestonesDate+="<milestone date='"+jhend+"' taskid='"+((m/4)+1)+"' radius='5' color='333333' bordercolor='FFFFFF' shape='"+milestonesShape+"' numsides='"+milestonesNumSides+"' borderthickness='1' />";
			}	
			if(null!=data[0][m+2]&&!data[0][m+2].equals(""))
			{
				sjstart=data[0][m+2].substring(0,10);
				//实际结束时间不为空
				if(null!=data[0][m+3]&&!data[0][m+3].equals(""))
				{
					sjend=data[0][m+3].substring(0,10);
					if(!Pub.empty(jhstart)){
						if(java.sql.Date.valueOf(sjstart).after(java.sql.Date.valueOf(jhstart))||java.sql.Date.valueOf(sjend).after(java.sql.Date.valueOf(jhend))){ 
								//起始日期大于结束日期 
								sjStartcolor="B22222";//红色   实际开始和结束有一个晚于计划就是红色
							}else{
								sjStartcolor="9BBB59";//绿色   实际开始和结束都早于或等于计划 
							}
					}
				}
				else//实际结束时间为空  当前时间之前为绿色  之后为虚线
				{
					if(!Pub.empty(jhstart)){
						sjend=DateTimeUtil.getDate();
						
						//计算后半段的开始时间--实际开始时间和当前时间的中间点
					 /*  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					    long to = df.parse(sjMiddle).getTime();
					    long from = df.parse(sjstart).getTime();
					    int tianshu=(int) (((to - from) / (1000 * 60 * 60 * 24))/2);
					     Calendar   calendar   =   new   GregorianCalendar(); 
					     calendar.setTime(df.parse(sjstart)); 
					     calendar.add(calendar.DATE,tianshu);//把日期往后增加一天.整数往后推,负数往前移动 
					     Date date=calendar.getTime();   
					     sjend=df.format(date);  */
						if(java.sql.Date.valueOf(sjstart).after(java.sql.Date.valueOf(jhstart))){
							sjStartcolor="B22222";//红色   实际开始晚于计划就是红色
						}else{
							sjStartcolor="9BBB59";//绿色   实际开始早于或等于计划 
						}
						//计划结束时间在当前时间之前 结束点是当前日期
							if(java.sql.Date.valueOf(sjend).after(java.sql.Date.valueOf(jhend))){
								taskdata+="<task label='实际'  processid='"+((m/4)+1)+"' start='"+sjend+"' end='"+sjend+"' id='"+((m/4)+1)+"'   color='"+sjEndcolor+"'  bordercolor='"+sjStartcolor+"' height='4'	toppadding='62%'/>";
							}else{
								taskdata+="<task label='实际'  processid='"+((m/4)+1)+"' start='"+sjend+"' end='"+jhend+"' id='"+((m/4)+1)+"'   color='"+sjEndcolor+"'  bordercolor='"+sjStartcolor+"' height='4'	toppadding='62%'/>";
							}
						}else{
						sjend=sjstart;
					}
				}
				taskdata+="<task label='实际'  processid='"+((m/4)+1)+"' start='"+sjstart+"' end='"+sjend+"' id='"+((m/4)+1)+"'   color='"+sjStartcolor+"' bordercolor='"+sjStartcolor+"' height='4'	toppadding='62%'/>";
				//图标的生成
				milestonesDate+="<milestone date='"+sjstart+"' taskid='"+((m/4)+1)+"' radius='"+milestonesRadius+"' color='"+sjStartcolor+"' bordercolor='"+sjStartcolor+"' shape='"+milestonesShape+"' numsides='"+milestonesNumSides+"' borderthickness='1' />";
				//如果没有结束时间只实线开始部分有图标
				if(null!=data[0][m+3]&&!data[0][m+3].equals("")){
					milestonesDate+="<milestone date='"+sjend+"' taskid='"+((m/4)+1)+"' radius='"+milestonesRadius+"' color='"+sjStartcolor+"' bordercolor='"+sjStartcolor+"' shape='"+milestonesShape+"' numsides='"+milestonesNumSides+"' borderthickness='1' />";
				}
				
			}	

		}		    
		taskdata+="</tasks>";
		//右侧竖线任务组
		taskdata+="<trendlines><line start='"+DateTimeUtil.getDate()+"' displayvalue='Today' color='333333' thickness='2' dashed='1' />" +
				"</trendlines>";
		milestonesDate+="</milestones>";
		String finaldata="<legend><item label='计划' color='4F81BD' /><item label='正常执行' color='9BBB59' /><item label='超期执行' color='B22222' /></legend>";
		finaldata+="<styles><definition><style type='Font' name='legendFont' size='12' /></definition><application><apply toobject='LEGEND' styles='legendFont' /></application></styles>";
		json+=yearJson+mouthJson+nameJson+taskdata+milestonesDate+finaldata+"</chart>";
		return json;
	}
}
