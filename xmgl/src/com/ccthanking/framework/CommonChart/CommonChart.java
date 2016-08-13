package com.ccthanking.framework.CommonChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.CategoryDataset;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.*;
import org.jfree.chart.plot.*;
import java.awt.*;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.*;
import org.jfree.chart.urls.*;
import java.io.PrintWriter;
import org.jfree.data.time.*;

public class CommonChart {
  private int chartkind;
  private String title;
  private String XLabel;
  private String YLabel;
  private String[][] data;

  private int dateformat=0;
  public void setDateFormat(int format)
  {
    this.dateformat = format;
  }

  public CommonChart(int kind,//图表类型
                     String chartTitle,//图表标题
                     String chartXLabel,//横坐标标题
                     String chartYLabel,//纵向标题
                     String[][] datas)//数据
  {
      this.chartkind = kind;
      this.title = chartTitle;
      this.XLabel = chartXLabel;
      this.YLabel = chartYLabel;
      this.data = datas;

  }
  //创建图表
  private void createChart()throws Exception
  {
     String[] titles= this.data[0];
     String[] values = data[1];

     switch(this.chartkind)
      {
        case 0://一般柱状图
          DefaultCategoryDataset dsBar = new DefaultCategoryDataset();
          for( int i=0;i<titles.length;i++)
          {
            dsBar.addValue(Double.parseDouble(values[i]),"",titles[i]);
          }
          this.varChart =
             org.jfree.chart.ChartFactory.createBarChart(this.title,XLabel,YLabel,dsBar,PlotOrientation.VERTICAL,true,true,false);
          break;
        case 1://3d柱状图
          DefaultCategoryDataset dsBar3D = new DefaultCategoryDataset();
          for( int i=0;i<titles.length;i++)
          {
            dsBar3D.addValue(Double.parseDouble(values[i]),"",titles[i]);
          }
          this.varChart =
             ChartFactory.createBarChart3D(title,XLabel,YLabel,dsBar3D,PlotOrientation.VERTICAL,true,true,false);
          this.varChart.getPlot().setForegroundAlpha(0.80f);
          break;
        case 2://一般饼状图
          org.jfree.data.DefaultPieDataset pieds = new org.jfree.data.DefaultPieDataset();

          for( int i=0;i<titles.length;i++)
          {
            pieds.setValue(titles[i],Double.parseDouble(values[i]));
          }

          this.varChart =  ChartFactory.createPieChart(this.title,pieds,true,true,false);


          break;
        case 3://3d饼状图
          org.jfree.data.DefaultPieDataset pieds3d = new org.jfree.data.DefaultPieDataset();

          for( int i=0;i<titles.length;i++)
          {
           pieds3d.setValue(titles[i],Double.parseDouble(values[i]));
          }
          this.varChart =
             ChartFactory.createPie3DChart(this.title,pieds3d,true,true,false);

          PiePlot plot = (PiePlot)this.varChart.getPlot();
          plot.setSectionLabelType(PiePlot.NAME_AND_PERCENT_LABELS);
          break;
        case 4://时间线图形
         Class dateclass = null;
         switch (this.dateformat)
         {
           case 0:
             dateclass =org.jfree.data.time.Year.class;
             break;
           case 1:
             dateclass =org.jfree.data.time.Month.class;
             break;
           case 2:
             dateclass =org.jfree.data.time.Day.class;
             break;
            default:
              dateclass =org.jfree.data.time.Year.class;
               break;
         }

         TimeSeries series = new TimeSeries(this.title,dateclass);

         for( int i=0;i<titles.length;i++)
         {
         switch (this.dateformat)
         {
           case 0:
             java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
             series.add( new Year(sdf.parse(titles[i])) , Double.parseDouble(values[i]) );
             break;
           case 1:
             java.text.SimpleDateFormat sdm = new java.text.SimpleDateFormat("mm");
             series.add( new Month(sdm.parse(titles[i])) , Double.parseDouble(values[i]));
             break;
           case 2:
             java.text.SimpleDateFormat sdd= new java.text.SimpleDateFormat("dd");
             series.add(new Day(sdd.parse(titles[i])), Double.parseDouble(values[i]) );
             break;
            default:
              java.text.SimpleDateFormat sdt = new java.text.SimpleDateFormat("yyyy");
              series.add(new Year(sdt.parse(titles[i])), Double.parseDouble(values[i]) );
               break;
         }
         }
         TimeSeriesCollection dataset = new TimeSeriesCollection(series);
         this.varChart = ChartFactory.createTimeSeriesChart(this.title,
                                                              this.XLabel,
                                                              this.YLabel,
                                                              dataset,
                                                              true,
                                                              true,
                                                              false);
          break;
        default:
            break;
      }

        this.varChart.setBackgroundPaint(this.varBackgroundColor);
  }

  private Color varBackgroundColor=Color.WHITE;

  public void setBackgroundColor(Color color)
  {
    this.varBackgroundColor = color;
  }
  public Color getBackgroundColor()
  {
    return this.varBackgroundColor;
  }

  private org.jfree.chart.JFreeChart varChart;

  private int defaultWidth=600;
  private int defaultHeight=400;

  //该方法解决集群环境下图片显示问题
  public String WriteAsJpeg(javax.servlet.http.HttpServletResponse  response)
  {

    try
     {
       if(this.varChart == null)
      {
        this.createChart();
      }

      // return org.jfree.chart.servlet.ServletUtilities.saveChartAsJPEG(this.varChart,
      // this.defaultWidth, this.defaultHeight, session);
      org.jfree.chart.ChartUtilities.writeChartAsJPEG(response.getOutputStream(),this.varChart,this.defaultWidth,this.defaultHeight);
     }
     catch(Exception E)
     {
       System.out.println(E);
     }
     return "";

  }

  public String SaveAsJpeg(javax.servlet.http.HttpSession session)
  {
    try
    {

      if(this.varChart == null)
     {
       this.createChart();
     }

      String path = System.getProperty("java.io.tmpdir");
      return org.jfree.chart.servlet.ServletUtilities.saveChartAsJPEG(this.varChart,
          this.defaultWidth, this.defaultHeight, session);

    }
    catch(Exception E)
    {
      System.out.println(E);
    }
    return "";
  }

  public void SaveAsOutputStream()
  {

  }

  public String SaveAsMapJpeg(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
  {
    String filename="";
    try
    {
      if(this.varChart == null)
      {
        this.createChart();
      }

    ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

    //org.jfree.chart.ChartUtilities.saveChartAsJPEG(file1,this.varChart, this.defaultWidth, this.defaultHeight, info);

    filename =org.jfree.chart.servlet.ServletUtilities.saveChartAsJPEG(this.varChart,
          this.defaultWidth, this.defaultHeight, info,request.getSession());

    PrintWriter writer = response.getWriter();
    org.jfree.chart.ChartUtilities.writeImageMap(writer,filename,info);

    }
    catch(Exception E)
    {
      System.out.println(E);
    }
    return filename;
  }






}