package com.ccthanking.framework.CommonChart.showchart.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ccthanking.framework.util.QueryTable;

public class DisplayChartServlet extends HttpServlet{
    public DisplayChartServlet(){
        super();
    }
    public void destroy() {
        super.destroy();
    }
    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setCharacterEncoding("GBK");
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
//        QueryTable table = (QueryTable) session.getAttribute("QTABLE");
        com.ccthanking.framework.util.QueryTable table = (com.ccthanking.framework.util.QueryTable) session.getAttribute("QTABLE");
        int start_index = 0;
        if (session.getAttribute("REPORT_DATA_START") != null){
            start_index = Integer.parseInt( (String) session.getAttribute("REPORT_DATA_START"));
        }
        int title_line = 0;
        if (session.getAttribute("REPORT_TITLE_COLUMN") != null){
            title_line = Integer.parseInt( (String) session.getAttribute("REPORT_TITLE_COLUMN"));
        }
        String tblx = request.getParameter("tblx").toString();
        String selectedline = (String) session.getAttribute("SELECTEDLINES");
        String selectedcolumn = (String) session.getAttribute("SELECTEDCOLUMNS");
        String[] selectedlines = selectedline.split(",");
        String[] selectedcolumns = selectedcolumn.split(",");

        String[][] data = new String[selectedlines.length][selectedcolumns.length];
        int i_index = 0;
        for (int i = 0; i < table.getRows() && i_index < selectedlines.length; i++){
            String series = table.getData()[i][title_line].trim();
            if (selectedline.indexOf(series) < 0)continue;
            int j_index = 0;
            data[i_index][j_index] = series;
            for (int j = start_index; j < table.getCols(); j++){
                if (selectedcolumn.indexOf(table.fields[j].getAlias().trim()) <0)continue;
                if (table.getData()[i][j] != null && table.getData()[i][j].trim().length() > 0){
                    data[i_index][j_index] = table.getData()[i][j];
                }else{
                    data[i_index][j_index] = "0";
                }
                j_index++;
            }
            i_index++;
        }

        String chartCaption = null;
        StringBuffer buff = new StringBuffer();

        if(selectedlines.length>1 && selectedcolumns.length>1){
            chartCaption = "统计图表";
            buff.append("<graph caption='");
            buff.append(chartCaption);
            buff.append("' formatNumberScale='0' decimalPrecision='0' showvalues='0' numdivlines='4'  baseFontSize='12' bgAlpha='0,0' showborder='0' formatNumberScale='0' numvdivlines='");
            buff.append(selectedcolumns.length);
            buff.append("'>\r\n<categories>\r\n");
            for(int i=0;i<selectedcolumns.length;i++){
                buff.append("<category name=\"");
                buff.append(selectedcolumns[i]);
                buff.append("\"/>\r\n");
            }
            buff.append("</categories>\r\n");
            for(int i=0;i<selectedlines.length;i++){
                buff.append("<dataset seriesname=\"");
                buff.append(selectedlines[i]);
                buff.append("\">\r\n");
                for(int j=0;j<selectedcolumns.length;j++){
                    buff.append("<set value=\"");
                    buff.append(data[i][j]);
                    buff.append("\"/>\r\n");
                }
                buff.append("</dataset>\r\n");
            }
        }else{
            if (selectedlines.length == 1){
                chartCaption = selectedline;
            }else if(selectedlines.length > 1 && selectedcolumns.length == 1){
                chartCaption = selectedcolumn;
            }
            buff.append("<graph caption='");
            buff.append(chartCaption);
            buff.append("' showNames='1' decimalPrecision='0' baseFontSize='12' formatNumberScale='0' formatNumber='1' bgAlpha='0,0' chartTopMargin='3' chartBottomMargin='3' skipOverlapLabels='0' showborder='0'");
            if (tblx.equals("0") || tblx.equals("1"))
                buff.append(" showValues='0' maxColWidth='55' useRoundEdges='1'");
            else if (tblx.equals("2") || tblx.equals("3"))
                buff.append(" pieYScale='50' pieRadius='150' pieSliceDepth='25'");
            buff.append(">\r\n");
            if(selectedlines.length == 1){
                for (int i=0;i<selectedcolumns.length;i++){
                    buff.append("<set name=\"");
                    buff.append(selectedcolumns[i]);
                    buff.append("\" value=\"");
                    buff.append(data[0][i]);
                    buff.append("\"/>\r\n");
                }
            }else if(selectedlines.length > 1 && selectedcolumns.length == 1){
                for (int i=0;i<selectedlines.length;i++){
                    buff.append("<set name=\"");
                    buff.append(selectedlines[i]);
                    buff.append("\" value=\"");
                    buff.append(data[i][0]);
                    buff.append("\"/>\r\n");
                }
            }
        }
        buff.append("<styles>\n");
        buff.append("<definition><style name='CaptionFont' type='font' size='20'/></definition>\n");
        buff.append("<application><apply toObject='CAPTION' styles='CaptionFont'/></application>\n");
        buff.append("</styles>\n");
        buff.append("</graph>");

//        String xmlData = buff.toString(); 
        out.println(buff.toString());
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
