package com.ccthanking.framework.CommonChart.showchart.chart;
public class FusionChartUtil
{
    //	3D柱状图xml头信息
    private String poleHead = "<chart caption='' xAxisName='' yAxisName='' decimalPrecision='0' formatNumberScale='0' baseFontSize='11' bgAlpha='0,0' showValues='0' numDivLines='9'>\r\n";
    //	xml主体内容名称
    private String bodyName = "<set name=\"";
    //	xml主体内容值
    private String bodyValue = "\" value=\"";
    //	xml主体内容颜色
    private String bodyColor = "\" color=\"";
    //	xml主体内容链接
    private String bodyLink = "\" link=\"";
    //	xml主体内容链接
    private String bodyEnd = "\"/>\r\n";
    //	xml主体内容链接
    private String bodySEnd = "\">\r\n";
    //	xml尾部内容
    private String end = "</graph>";
    //	xml尾部内容2
    private String cend = "</chart>";
    //   line2D线状图XML头信息
    private String lineHead = "<graph caption='' subcaption='' xAxisName='' yAxisMinValue='15000' yAxisName='' decimalPrecision='1' formatNumberScale='0' showNames='1' showValues='0'  showAlternateHGridColor='1' AlternateHGridColor='ff5904' divLineColor='ff5904' divLineAlpha='20' alternateHGridAlpha='5' baseFontSize='12' bgAlpha='0,0' labelDisplay='wrap' showBorder='0' numberSuffix='%' decimals='2'>\r\n";
    //   Pie3D饼状图XML头信息
    private String pieHead = "<graph showNames='1' decimalPrecision='0' baseFontSize='12' formatNumberScale='0' formatNumber='1' pieYScale='45' pieRadius='100' bgAlpha='0,0' chartTopMargin='3' chartBottomMargin='3' skipOverlapLabels='0'>\r\n";
    //   MSColumn3D分组柱状图头信息前半部
    private String groupPoleHeadBegin = "<graph caption='' xaxisname='' yaxisname='' hovercapbg='DEDEBE' hovercapborder='889E6D' yAxisMaxValue='";
    //   MSColumn3D分组柱状图头信息后半部
    private String groupPoleHeadEnd = "' rotateNames='0' numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='2' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='' subcaption='' baseFontSize='10' bgAlpha='0,0'>\r\n";
    //   MSColumn3D分组柱状图标题部分
    private String groupPoleTitle = "<categories fontSize='12' fontColor='000000'>\r\n";
    //   MSColumn3D分组柱状图单个标题
    private String groupPoleTitleContent = "<category name=\"";
    //   MSColumn3D分组柱状图单个标题结束
    private String groupPoleTitleEnd = "</categories>\r\n";
    //   MSColumn3D分组柱状图1组内每个柱的属性
    private String groupPoleOneContent = "<dataset seriesname=\"";
    //   MSColumn3D分组柱状图1组内每个柱的值
    private String groupPoleOneValue = "<set value=\"";
    //   MSColumn3D分组柱状图1组内每个柱的属性
    private String groupPoleOneContentEnd = "</dataset>\r\n";
    //   Col3DLineDY柱和线状图头信息
    private String poleAndLineHead= "<graph caption='' PYAxisName='' SYAxisName='' showvalues='0' numDivLines='4' formatNumberScale='0' decimalPrecision='0' anchorSides='10' anchorRadius='3' anchorBorderColor='009900' baseFontSize='11' formatNumber='1' bgAlpha='0,0' chartBottomMargin='5'>\r\n";
    //   Col3DLineDY柱和线状图是否是线
    private String lineInfo = "\" parentYAxis=\"S";
    //   层次图和多组线状图不显示图上的数值头信息
    private String commonHead = "<graph caption='' subcaption='' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumberScale='0' decimalPrecision='0' showvalues='0' numdivlines='3' numVdivlines='0' rotateNames='1' baseFontSize='12' bgAlpha='0,0'>";
    /*
     * 生成柱状图xml
     * String[] title 显示每个柱的标题  如果显示柱名称，请将此处值设置为空
     * String[] value 显示每个柱的数值  如果不显示柱高，请将此处值设置为0
     * String[] color 显示每个柱体颜色
     * String[] link  每个柱体的链接页面
     */
    public String generatePoleXmlInfo(String[] title,String[] value,String[] color,String[] link)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(poleHead);
        for(int i=0;i<value.length;i++)
        {
            if(i<title.length)
            {
                stringBuffer.append(bodyName);
                stringBuffer.append(title[i]);
            }
            if(i<value.length)
            {
                stringBuffer.append(bodyValue);
                stringBuffer.append(value[i]);
            }
            if(i<color.length)
            {
                stringBuffer.append(bodyColor);
                stringBuffer.append(color[i]);
            }
            if(link!=null&&i<link.length&&link[i]!=null&&!link[i].equals(""))//modified on 2009.5.8
            {
                stringBuffer.append(bodyLink);
                stringBuffer.append(link[i]);
            }
            stringBuffer.append(bodyEnd);
        }
        stringBuffer.append(cend);
        return stringBuffer.toString();
    }
    /*
     * 生成线状xml
     * String[] title 显示每个线的标题  如果显示柱名称，请将此处值设置为空
     * String[] value 显示每个线的数值  如果不显示柱高，请将此处值设置为0
     */
    public String generateLineXmlInfo(String[] title,String[] value)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(lineHead);
        for(int i=0;i<value.length;i++)
        {
            if(i<title.length)
            {
                stringBuffer.append(bodyName);
                stringBuffer.append(title[i]);
            }
            if(i<value.length)
            {
                stringBuffer.append(bodyValue);
                stringBuffer.append(value[i]);
            }
            stringBuffer.append(bodyEnd);
        }
        stringBuffer.append(end);
        return stringBuffer.toString();
    }
    /*
     * 生成饼状xml
     * String[] title 显示每个饼的标题	如果显示柱名称，请将此处值设置为空
     * String[] value 显示每个饼的数值  如果不显示柱高，请将此处值设置为0
     */
    public String generatePieXmlInfo(String[] title,String[] value)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(pieHead);
        for(int i=0;i<value.length;i++)
        {
            if(i<title.length)
            {
                stringBuffer.append(bodyName);
                stringBuffer.append(title[i]);
            }
//            if(i<value.length)
//            {
                stringBuffer.append(bodyValue);
                stringBuffer.append(value[i%title.length]);
//            }
            stringBuffer.append(bodyEnd);
        }
        stringBuffer.append(end);
        return stringBuffer.toString();
    }
    /*
     * 生成分组柱状图xml
     * String[] title 显示每组柱的标题	 如果显示柱名称，请将此处值设置为空
     * String[][] property 显示每组饼的数值  一维为每组内单个柱的标题,二维为每个柱的数值
     * String[] color 每组内单个柱体的颜色
     */
    public String generateGroupPoleXmlInfo(String[] title,String[][] property,String[] color,String yMaxValue)
    {
    	String groupPoleHead = groupPoleHeadBegin+yMaxValue+groupPoleHeadEnd;
        return this.commonXmlInfo(title, property, color,groupPoleHead) ;
    }
    /*
     * 生成柱和线状图xml
     * String[] title 显示每组柱的标题	 如果显示柱名称，请将此处值设置为空
     * String[][] poleProperty 显示每组柱的数值  一维为每组内单个柱的标题,二维为每个柱的数值
     * String[][] lineProperty 显示每条线的数值  一维为每组内每条线的标题,二维为每条线的数值
     * String[] color 每组内单个柱体的颜色
     */
    public String generatePoleAndLineXmlInfo(String[] title,String[][] poleProperty,String[][] lineProperty,String[] color)
    {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(poleAndLineHead);
        stringBuffer.append(groupPoleTitle);
        for(int i=0;i<title.length;i++)
        {
            stringBuffer.append(groupPoleTitleContent);
            stringBuffer.append(title[i]);
            stringBuffer.append(bodyEnd);
        }
        stringBuffer.append(groupPoleTitleEnd);
        for(int i=0;i<poleProperty.length;i++)
        {
            stringBuffer.append(groupPoleOneContent);
            stringBuffer.append(poleProperty[i][0]);
            if(i<color.length)
            {
                stringBuffer.append(bodyColor);
                stringBuffer.append(color[i]);
            }
            stringBuffer.append(bodySEnd);
            for(int j=1;j<poleProperty[i].length;j++)
            {
                stringBuffer.append(groupPoleOneValue);
                stringBuffer.append(poleProperty[i][j]);
                stringBuffer.append(bodyEnd);
            }
            stringBuffer.append(groupPoleOneContentEnd);
        }
        for(int i=0;i<lineProperty.length;i++)
        {
            stringBuffer.append(groupPoleOneContent);
            stringBuffer.append(lineProperty[i][0]);
            if(i<color.length)
            {
                stringBuffer.append(bodyColor);
                stringBuffer.append(color[color.length-1]);
            }
            stringBuffer.append(lineInfo);
            stringBuffer.append(bodySEnd);
            for(int j=1;j<lineProperty[i].length;j++)
            {
                stringBuffer.append(groupPoleOneValue);
                stringBuffer.append(lineProperty[i][j]);
                stringBuffer.append(bodyEnd);
            }
            stringBuffer.append(groupPoleOneContentEnd);
        }
        stringBuffer.append(end);
        return stringBuffer.toString();
    }
    /*
     * 生成柱状层次图xml
     * String[] title 显示每组柱的标题	 如果显示柱名称，请将此处值设置为空
     * String[][] poleProperty 显示每组饼的数值  一维为每组内单个柱的标题,二维为每个柱的数值
     * String[] color 每组内单个柱体的颜色
     */
    public String generateLayerPoleXmlInfo(String[] title,String[][] poleProperty,String[] color)
    {
        return this.commonXmlInfo(title, poleProperty, color,commonHead) ;
    }
    /*
     * 生成多组线状图xml
     * String[] title 显示每组柱的标题	 如果显示柱名称，请将此处值设置为空
     * String[][] poleProperty 显示每组饼的数值  一维为每组内单个柱的标题,二维为每个柱的数值
     * String[] color 每组内单个柱体的颜色
     */
    public String generateMultiLineXmlInfo(String[] title,String[][] poleProperty,String[] color)
    {
            return this.commonXmlInfo(title, poleProperty, color,commonHead) ;
    }
    /*
     * 公共图xml
     * String[] title 显示每组柱的标题	 如果显示柱名称，请将此处值设置为空
     * String[][] poleProperty 显示每组饼的数值  一维为每组内单个柱的标题,二维为每个柱的数值
     * String[] color 每组内单个柱体的颜色
     */
    public String commonXmlInfo(String[] title,String[][] property,String[] color,String headInfo)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(headInfo);
        stringBuffer.append(groupPoleTitle);
        for(int i=0;i<title.length;i++)
        {
            stringBuffer.append(groupPoleTitleContent);
            stringBuffer.append(title[i]);
            stringBuffer.append(bodyEnd);
        }
        stringBuffer.append(groupPoleTitleEnd);
        for(int i=0;i<property.length;i++)
        {
            stringBuffer.append(groupPoleOneContent);
            stringBuffer.append(property[i][0]);
            if(i<color.length)
            {
                stringBuffer.append(bodyColor);
                stringBuffer.append(color[i]);
            }
            stringBuffer.append(bodySEnd);
            for(int j=1;j<property[i].length;j++)
            {
                stringBuffer.append(groupPoleOneValue);
                stringBuffer.append(property[i][j]);
                stringBuffer.append(bodyEnd);
            }
            stringBuffer.append(groupPoleOneContentEnd);
        }
        stringBuffer.append(end);
        return stringBuffer.toString();
    }
}
