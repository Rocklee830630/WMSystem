package com.ccthanking.framework.wsdy;

import java.util.ArrayList;
//import com.f1j.ss.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.*;
import java.awt.image.BufferedImage;
import com.ccthanking.framework.wsdy.gif.*;
import javax.imageio.ImageIO;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.DBUtil;

public class Util {
  public Util() {
  }

  public String[] value = null;



  /**
   *
   * @param m_Workbook
   * @param totalSheet
   * @throws java.lang.Exception
   * 复制模板类，此类只实用与有两个sheet页的模板，将两页模板一一复制
   *
   */

//  public void copyFormat(BookModelImpl m_Workbook, int totalSheet) throws
//      Exception {
//    Sheet template = m_Workbook.getBook().getSheet(0);
//    int lastRow = template.getLastRow();
//    int lastCol = template.getLastCol();
//    //----copyRange(int dstSheet, int dstRow1, int dstCol1, int dstRow2,
//    //int dstCol2, BookModel srcBookModel, int srcSheet, int srcRow1, int srcCol1,
//    //int srcRow2, int srcCol2, short what)
//    //
//
//    //--------
//    for (int i = 1; i < ( (totalSheet - 1) / 2) + 1; i++) {
//      m_Workbook.setSheet(i * 2);
//      m_Workbook.copyRange(i * 2, 0, 0, lastRow, lastCol, m_Workbook, 0, 0, 0,
//                           lastRow, lastCol);
//      Sheet disSheet = m_Workbook.getBook().getSheet(totalSheet);
//      for (int row = 0; row <= lastRow; row++) {
//        for (int col = 0; col <= lastCol; col++) {
//          m_Workbook.setColWidth(col, template.getColWidth(col));
//          m_Workbook.setRowHeight(row, template.getRowHeight(row));
//
//        }
//      }
//    }
//    Sheet template1 = m_Workbook.getBook().getSheet(1);
//    int lastRow1 = template1.getLastRow();
//    int lastCol1 = template1.getLastCol();
//    for (int i = 1; i < ( (totalSheet - 1) / 2) + 1; i++) {
//      m_Workbook.setSheet(i * 2 + 1);
//      m_Workbook.copyRange(i * 2 + 1, 0, 0, lastRow1, lastCol1, m_Workbook, 1,
//                           0, 0, lastRow1, lastCol1);
//      Sheet disSheet = m_Workbook.getBook().getSheet(totalSheet);
//      for (int row = 0; row <= lastRow1; row++) {
//        for (int col = 0; col <= lastCol1; col++) {
//          m_Workbook.setColWidth(col, template1.getColWidth(col));
//          m_Workbook.setRowHeight(row, template1.getRowHeight(row));
//
//        }
//      }
//    }
//  }
  /**
   *
   * @param m_Workbook
   * @param totalSheet
   * @throws java.lang.Exception
   * 复制模板类，此类只实用与有两个sheet页的模板，将两页模板一一复制
   *
   */

//  public void copySheet(BookModelImpl m_Workbook, int totalSheet,int sheet_id) throws
//      Exception {
//    Sheet template = m_Workbook.getBook().getSheet(sheet_id);
//    int lastRow = template.getLastRow();
//    int lastCol = template.getLastCol();
//    //----copyRange(int dstSheet, int dstRow1, int dstCol1, int dstRow2,
//    //int dstCol2, BookModel srcBookModel, int srcSheet, int srcRow1, int srcCol1,
//    //int srcRow2, int srcCol2, short what)
//    //
//
//    //--------
//    for (int i = 1; i <= totalSheet ; i++) {
//      m_Workbook.setSheet(sheet_id+i);
//      m_Workbook.copyRange(sheet_id+i, 0, 0, lastRow, lastCol, m_Workbook, 0, 0, 0,lastRow, lastCol);
//      Sheet disSheet = m_Workbook.getBook().getSheet(sheet_id+i);
//      for (int row = 0; row <= lastRow; row++) {
//        for (int col = 0; col <= lastCol; col++) {
//          m_Workbook.setColWidth(col, template.getColWidth(col));
//          m_Workbook.setRowHeight(row, template.getRowHeight(row));
//
//        }
//      }
//    }
//
//  }





  /**
   * 解析sql语句
   * @param sql
   * @return
   * @throws java.lang.Exception
   */

  public String ParseSQL(String sql) throws Exception {

    String value = null;
    String[] sqlstr = null, sqlvalue = null;
    if (sql == null)
      throw new Exception("传输sql语句错误：null");
    else {
      if (sql.indexOf(":") == -1) {

      }
      else {
        sqlstr = sql.split(":");
        sqlvalue = new String[sqlstr.length - 1];
        for (int i = 0; i < sqlstr.length - 1; i++) {

          int length = sqlstr[i + 1].length();
          if (sqlstr[i + 1].indexOf(" ") != -1 ||
              sqlstr[i + 1].indexOf(")") != -1) {
            for (int j = 0; j < length; j++) {
              char t = sqlstr[i + 1].charAt(j);
              if (sqlvalue[i] == null)
                sqlvalue[i] = "";
              if (" ".compareTo(String.valueOf(t)) == 0 ||
                  ")".compareTo(String.valueOf(t)) == 0) {
                break;
              }
              else
                sqlvalue[i] = sqlvalue[i].concat(String.valueOf(t));
              if (sqlvalue[i] != null) {
                sqlvalue[i] = sqlvalue[i].trim();
              }
            }
            sqlstr[i + 1] = "?" + sqlstr[i +
                1].substring(sqlvalue[i].length(), sqlstr[i + 1].length());

          }
          else {
            sqlvalue[i] = sqlstr[i + 1];
            if (sqlvalue[i] != null) {
              sqlvalue[i] = sqlvalue[i].trim();
            }
            sqlstr[i + 1] = "? ";
          }
          sql = "";
          for (int k = 0; k < sqlstr.length; k++) {
            sql = sql.concat(sqlstr[k]);
          }
        }
      }
      this.value = sqlvalue;
    }

    return sql;
  }
  /**
   * 判断字符串是否为空
   * @param resource 源串
   * @return
   */
  public static boolean isNull(String resource) {
    if(resource == null || resource.trim().equalsIgnoreCase("null") || resource.trim().equals(""))
      return true;
    else
      return false;
  }
  /**
   * 将日期格式化，2005-11-14 00:00:00.0 参数formate为1 转换为2005年11月14日 下午01时22分
   *                为0 转换为2005年11月14日
   * @param dates
   * @param formate
   * @return
   */
  public String getDateFormat(String dates,String formate){

    if (isNull(dates) == false) {

      //dates = dates.substring(0,dates.length()-2);
      dates = dates.replaceAll("-","/");

      Date date = new Date(dates);
      DateFormat longDateFormat = DateFormat.getDateTimeInstance(DateFormat.
          LONG,
          DateFormat.LONG);
      String str = longDateFormat.format(date);
      if ("1".equals(formate))
        return str.substring(0, str.length() - 3);
      else if ("0".equals(formate))
        return str.substring(0, str.length()-12);
    }
    return dates;
  }
  public void ToGIF(byte[] bytes, String gifPath){
    java.io.ByteArrayInputStream is = new java.io.ByteArrayInputStream( bytes);
    try {
      BufferedImage src = ImageIO.read(is); // 读入文件
      AnimatedGifEncoder e = new AnimatedGifEncoder();
      e.setRepeat(0);
      e.start(gifPath);
      e.setDelay(3000); // 1 frame per sec
      e.addFrame(src);
      e.finish();
    }
    catch (Throwable e3) {
      System.out.println("get exception trying to add picture e=" +
                         e3.getMessage());
    }finally{
      try {
        is.close();
      }
      catch (IOException ex) {
        ex.printStackTrace();
        System.out.println("ToGIF close Exception:"+ex.getMessage());
      }
    }
  }
  public byte[] getGIF(String filePath) throws Exception{
    java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream(256);
    File file = new File(filePath);
    FileInputStream ins = new FileInputStream(file);
    try {
      if (ins != null) {
        byte buf[] = new byte[256];
        int bytes;
        while ( (bytes = ins.read(buf)) > 0) {
          if (bytes > 0)
            os.write(buf, 0, bytes);
        }
      }
    }
    catch (Throwable e3) {
      System.out.println("got exception trying to add picture e=" +
                         e3.getMessage());
    }
    finally {
      os.close();
      ins.close();
      if (file.isFile() && file.exists()) {
        file.delete();
      }
    }
    return os.toByteArray();
  }

}
