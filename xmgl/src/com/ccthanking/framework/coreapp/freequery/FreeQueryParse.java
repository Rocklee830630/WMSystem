//v20090409-12
//v20090531-13
//v20090604-14
//v20090618-14
package com.ccthanking.framework.coreapp.freequery;

/*parseH
 * author : michael wang
 * create time : 2007.03.08
 * you are free to copy,modify,rewrite this code,but please you reserve author's name.
 *
 * version 1.0 2007.03.08 create
 *         1.1 2007.05.03 apply for jwzh
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.freequery.structure.Relation;
import com.ccthanking.framework.coreapp.orgmanage.SpellCache;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;


public class FreeQueryParse {
	
   private int intRowPerPage = 20;
   private boolean hasPrint = false;
   private boolean hasDownload = false;
   private String strTemplate = "1";
   private boolean hasPicBrowse = false;
   private boolean hasPicSet = false;
   private String strTableTitle = "";
   private String strTitleAlign = "center";
   private String strTitleColor = "";
   private String strTitleBGColor = "";
   private String strFontSize = "10";
   private String strOrderList = "";
   private String strSwitchFlag = "0";
   private String strCrossColor = "0";
   private String strRowColor1 = "#C5DCFE";
   private String strRowColor2 = "";
//   private String strDataSource = "";

   private int intXCells = 0;

   private String strTableList = "";
   private String strRelationList = "";
   private String strFilterList = "";

   private String strImgTable = "";
   private String strImgField = "";

//   private ArrayList alFilterList = new ArrayList();

   private Map mapUsedTables = new HashMap(0);
   private ArrayList alDefineTableList = new ArrayList();

   private ArrayList alRelImageList = new ArrayList();
   private ArrayList alImageDispList = new ArrayList();

   private ArrayList alRelDetailList = new ArrayList();
   private ArrayList alDetailTableList = new ArrayList();
   private ArrayList alDisplayFieldList = new ArrayList();
   private ArrayList alQueryTableList = new ArrayList();
   private ArrayList alQueryMapList = new ArrayList();
   private StringBuffer bufferJoin = new StringBuffer();

   private int intPages;
   private int intCurPage;
   private int intCount;

   public FreeQueryParse() {
	
   }
	
   private boolean empty(String s) {
		if (s == null || s.trim().equals(""))
			return true;
		else
			return false;
   }
	
   private String trimChar(String p_Source,char p_Char,int p_Mode){
		//1 odd
		//2 even
	
		String bResult = "";
	
		if (!"".equals(p_Source))
		{
			char[] arrSource = p_Source.toCharArray();
			int num = 0;
			for (int i = arrSource.length - 1 ; i >= 0; i--){
				if (arrSource[i] == p_Char)
					num++;
				else
					break;
			}
			if (p_Mode == 1){
				if (num > 0 && num%2 == 0 )
				   num--;
			}
			else if (p_Mode == 2){
				if (num%2 == 1)
				   num--;
			}
			bResult = p_Source.substring(0,arrSource.length - num);
		}
	
		return bResult;
   }

   private String convertString(String p_SrcString){
   	String strResult = p_SrcString;

   	strResult = strResult.replaceAll("&", "&amp;");
   	strResult = strResult.replaceAll("<", "&lt;");
   	strResult = strResult.replaceAll(">", "&gt;");
   	strResult = strResult.replaceAll("\"", "&quot;");
   	strResult = strResult.replaceAll("'", "&apos;");

   	return strResult;
   }

   private String getAgeDateCondition(String p_Type, String p_Operation,
			String p_Value) {
		String format = "'YYYYMMDDHH24MISS'";
		String strResult = "";
		Calendar objCal = Calendar.getInstance() ;

		if ("like".equalsIgnoreCase(p_Operation))
			p_Operation = "=";
		if (p_Value == null || "".equals(p_Value))
			p_Value = "0";
		if ("1".equals(p_Type)) {
			int realYear = objCal.get(Calendar.YEAR)
					- Integer.parseInt(p_Value);
			if (">".equals(p_Operation))
				strResult = " < to_date('" + realYear + "0101000000" + "',"
						+ format + ")";
			else if (">=".equals(p_Operation))
				strResult = " <= to_date('" + realYear + "1231235959" + "',"
						+ format + ")";
			else if ("<".equals(p_Operation))
				strResult = " > to_date('" + realYear + "1231235959" + "',"
						+ format + ")";
			else if ("<=".equals(p_Operation))
				strResult = " >= to_date('" + realYear + "0101000000" + "',"
						+ format + ")";
			else if ("<>".equals(p_Operation))
				strResult = " not between to_date('" + realYear + "0101000000"
						 + "'," + format + ") and to_date('" + realYear
							+ "1231235959" + "'," + format + ")";
			else
				strResult = " between to_date('" + realYear + "0101000000"
						+ "'," + format + ") and to_date('" + realYear
						+ "1231235959" + "'," + format + ")";
		} else if ("2".equals(p_Type)) {
			String[] arrAge = p_Value.split("-");
			int intAge1 = 0;
			int intAge2 = 150;
			try {
				if (arrAge.length == 1)
					intAge1 = Integer.parseInt(arrAge[0]);
				else if (arrAge.length == 2) {
					if (arrAge[0] != "")
						intAge1 = Integer.parseInt(arrAge[0]);
					if (arrAge[1] != "")
						intAge2 = Integer.parseInt(arrAge[1]);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			int realAge1Year = objCal.get(Calendar.YEAR) - intAge1;
			int realAge2Year = objCal.get(Calendar.YEAR) - intAge2;
			strResult = " between to_date('" + realAge2Year + "0101000000"
					+ "'," + format + ") and to_date('" + realAge1Year
					+ "1231235959" + "'," + format + ")";
		} else
			strResult = " " + p_Operation + " to_date('" + p_Value + "',"
					+ format + ")";
		return strResult;
	}

   private String getYearDateCondition(String p_Operation,
			String p_Value) {
		String format = "'YYYYMMDDHH24MISS'";
		String strResult = "";
		Calendar objCal = Calendar.getInstance() ;

		if ("like".equalsIgnoreCase(p_Operation))
			p_Operation = "=";
		int realYear;
		if (p_Value == null || "".equals(p_Value))
			realYear = objCal.get(Calendar.YEAR);
		else
		    realYear = Integer.parseInt(p_Value);
		if (">".equals(p_Operation))
			strResult = " > to_date('" + realYear + "1231235959" + "',"
					+ format + ")";
		else if (">=".equals(p_Operation))
			strResult = " >= to_date('" + realYear + "0101000000" + "',"
					+ format + ")";
		else if ("<".equals(p_Operation))
			strResult = " < to_date('" + realYear + "0101000000" + "',"
					+ format + ")";
		else if ("<=".equals(p_Operation))
			strResult = " <= to_date('" + realYear + "1231235959" + "',"
					+ format + ")";
		else if ("<>".equals(p_Operation))
			strResult = " not between to_date('" + realYear + "0101000000"
					  + "'," + format + ") and to_date('" + realYear
					  + "1231235959" + "'," + format + ")";
		else
			strResult = " between to_date('" + realYear + "0101000000"
						+ "'," + format + ") and to_date('" + realYear
						+ "1231235959" + "'," + format + ")";

		return strResult;
	}

   private String getYearMonthDateCondition(String p_Operation,
			String p_Value) {
		String format = "'YYYYMMDDHH24MISS'";
		String strResult = "";
		Calendar objCal = Calendar.getInstance() ;

		if ("like".equalsIgnoreCase(p_Operation))
			p_Operation = "=";
		int realYear;
		int realMonth;
		int maxDays;
		if (p_Value == null || "".equals(p_Value)){
			realYear = objCal.get(Calendar.YEAR);
			realMonth = objCal.get(Calendar.MONTH) + 1;
			maxDays = objCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		else{
			int intTemp = Integer.parseInt(p_Value);
		    realYear = intTemp / 100;
		    realMonth = intTemp % 100;
		    objCal.set(Calendar.YEAR, realYear);
		    objCal.set(Calendar.MONTH, realMonth -1);
		    maxDays = objCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}

		if (">".equals(p_Operation))
			strResult = " > to_date('" + p_Value + "01000000" + "',"
					+ format + ")";
		else if (">=".equals(p_Operation))
			strResult = " >= to_date('" + p_Value + maxDays + "235959" + "',"
					+ format + ")";
		else if ("<".equals(p_Operation))
			strResult = " < to_date('" + p_Value + "01000000" + "',"
					+ format + ")";
		else if ("<=".equals(p_Operation))
			strResult = " <= to_date('" + p_Value + maxDays + "235959" + "',"
					+ format + ")";
		else if ("<>".equals(p_Operation))
			strResult = " not between to_date('" + p_Value + "01000000"
					  + "'," + format + ") and to_date('" + p_Value + maxDays
					  + "235959" + "'," + format + ")";
		else
			strResult = " between to_date('" + p_Value + "01000000"
						+ "'," + format + ") and to_date('" + p_Value + maxDays
						+ "235959" + "'," + format + ")";

		return strResult;
	}

   private String getPageSQL(String p_SQL,int p_RowsOfPage,int p_CurPage){
	   String strResult;

	   strResult = " SELECT * FROM (SELECT s.*,ROWNUM idnum FROM ("
		         + p_SQL + ") s "
		         + " WHERE  ROWNUM <=" + p_CurPage * p_RowsOfPage + " ) q WHERE  idnum> "
		         + (p_CurPage - 1) * p_RowsOfPage;
      // ��һ�ε���
	   return strResult;
   }

   private boolean useTable(String p_TableName,ArrayList p_TableList){
	   boolean bFlag = true;
	   String strCurTable;
	   for(int i = 0; i < p_TableList.size();i++){
		   strCurTable = (String)p_TableList.get(i);
		   if (p_TableName.equals(strCurTable)){
			   bFlag = false;
			   break;
		   }
	   }
	   if (bFlag)
	      p_TableList.add(p_TableName);
	   return bFlag;
   }

   private boolean useField(FieldVO p_VO,ArrayList p_FieldList){
	   boolean bFlag = true;
	   FieldVO objVO;
	   for(int i = 0; i < p_FieldList.size();i++){
		   objVO = (FieldVO)p_FieldList.get(i);
		   if (p_VO.getField().equals(objVO.getField())){
			   bFlag = false;
			   break;
		   }
	   }
	   if (bFlag)
		   p_FieldList.add(p_VO);
	   return bFlag;
   }

   private String  getTables(){
	   StringBuffer strResult = new StringBuffer();

	   Iterator iter = mapUsedTables.keySet().iterator();
	   while(iter.hasNext())
	   {
		   String tName = (String)iter.next();
		   if (!"".equals(strResult.toString()))
				  strResult.append(",");
			  strResult.append(tName);
	   }
	   
	   return strResult.toString();
   }

   private Element getTableElement(String p_Table,Element p_DetailElement){
	   Element tmpElement = null;
	   List tmpList = p_DetailElement.elements("TABLE");
	   boolean bFlag = true;
	   String strTable;
	   for(int i = 0 ; i < tmpList.size(); i++){
		   tmpElement = (Element)tmpList.get(i);
		   strTable = tmpElement.attributeValue("id");
		   if (p_Table.equals(strTable)){
			   bFlag = false;
			   break;
		   }
	   }
	   if (bFlag)
		    tmpElement = null;
	   return tmpElement;
   }

   private void parseH(Element p_NodeH,Element p_DetailElement){

       Element nodeHeaderStyle = p_NodeH.element("STYLE");

	   if (nodeHeaderStyle != null){
		   Element nodeSimple = nodeHeaderStyle.element("SIMPLE");
		   if (nodeSimple != null){

			   String strTemp ;

//			 ��ʾ����
			   strTemp = nodeSimple.attributeValue("rnum");
			   if (strTemp != null && !"".equals(strTemp))
			   {
				   intRowPerPage = Integer.parseInt(strTemp);
			   }
			   strTemp = nodeSimple.attributeValue("template");
			   if (strTemp != null && !"".equals(strTemp))
				   strTemplate = strTemp;
			   strTemp = nodeSimple.attributeValue("print");
			   if ("true".equals(strTemp))
				   hasPrint = true;
			   strTemp = nodeSimple.attributeValue("download");
			   if ("true".equals(strTemp))
				   hasDownload = true;
			   strTemp = nodeSimple.attributeValue("browse");
			   if ("true".equals(strTemp))
				   hasPicBrowse = true;
               
			   strTemp = nodeSimple.attributeValue("intercolor");
			   if ("true".equals(strTemp))
				   strCrossColor = "1";

			   strRowColor1 = nodeSimple.element("COLOR1").getText();

			   if ("1".equals(strCrossColor))
			      strRowColor2 = nodeSimple.element("COLOR2").getText();

		   }
	   }

	   Element nodeHeaderFormat = p_NodeH.element("FORMAT");

	   if (nodeHeaderFormat != null)
	   {
		   strTableTitle = nodeHeaderFormat.attributeValue("title");
		   strTitleAlign = nodeHeaderFormat.attributeValue("align");
		   strTitleBGColor = nodeHeaderFormat.attributeValue("titleBGColor");
		   strTitleColor = nodeHeaderFormat.attributeValue("titleColor");
		   strFontSize = nodeHeaderFormat.attributeValue("fontSize");

		   if (nodeHeaderFormat.element("ORDER") != null){

			   List tmpList = nodeHeaderFormat.element("ORDER").elements("FIELD");
			   if (tmpList != null) {
				   for(int i = 0; i < tmpList.size(); i++){

					   Element nodeField = (Element) tmpList.get(i);
					   if (i == 0)
						   strOrderList = " ORDER BY ";
					       strOrderList += nodeField.getText()
					                    + ("2".equals(nodeField.attributeValue("dir"))?"":" DESC ");
					   if (i < tmpList.size() - 1)
						   strOrderList +=",";
				   }
			   }
		   }
	   }

	   alDetailTableList.clear();

	   Element nodeHeaderDetail = p_NodeH.element("DETAIL");
	   if (nodeHeaderDetail != null){
		   List tmpList = nodeHeaderDetail.elements("ITEM");

		   if (tmpList != null){
			   for(int i = 0 ;i < tmpList.size();i++){

				   Element tmpNode = (Element) tmpList.get(i);
				   String strOwner ;
				   String strTable ;
				   String strField ;
				   String strStyle ;
				   String strName  ;
				   String strDic ;
				   //add by wangzh ������ܶ�ȡ���ڸ�ʽ���� 2009-12-3 begin
				   String strFmt ;
                   //add by wangzh ������ܶ�ȡ���ڸ�ʽ���� 2009-12-3 end
				   String strTableID;

				   strOwner = tmpNode.element("ROW").elementText("OWNER");
				   strTable = tmpNode.element("ROW").elementText("TNAME");
				   strField = tmpNode.element("ROW").elementText("FNAME");
				   strStyle = tmpNode.element("ROW").elementText("STYLE");
				   strName = tmpNode.element("ROW").elementText("FNOTE");
				   strDic = tmpNode.element("ROW").elementText("DIC_CODE");
				   //add by wangzh ������ܶ�ȡ���ڸ�ʽ���� 2009-12-3 begin
				   strFmt = tmpNode.element("ROW").elementText("FMT");
                   //add by wangzh ������ܶ�ȡ���ڸ�ʽ���� 2009-12-3 end
				   strTableID = strOwner+"."+strTable;
				   recordUsedTables(strTableID);

				   Element objTableElement;
				   if (useTable(strTableID ,alDetailTableList)){
					   objTableElement = p_DetailElement.addElement("TABLE");
					   objTableElement.addAttribute("id", strTableID);
				   }
				   else
					   objTableElement = getTableElement(strTableID,p_DetailElement);
				   Element objItemElement;
				   objItemElement = objTableElement.addElement("ITEM");
				   objItemElement.addElement("TID").addText(strTableID);
				   objItemElement.addElement("FID").addText(strField);
				   objItemElement.addElement("TYPE").addText(strStyle);
				   objItemElement.addElement("NAME").addText(strName);
				   objItemElement.addElement("DIC").addText(strDic);
//				 add by wangzh ������ܶ�ȡ���ڸ�ʽ���� 2009-12-3 begin
				   objItemElement.addElement("FMT").addText(strFmt);
//				 add by wangzh ������ܶ�ȡ���ڸ�ʽ���� 2009-12-3 end

			   }
		   }

	   }

   }

   private String parseX(Element p_NodeX){

	 String strResult = "";

	 if (p_NodeX == null)
		 return strResult;
     List tmpList = p_NodeX.elements("CELL");

     alDisplayFieldList.clear();
     intXCells = 0;

     for(int i = 0; i < tmpList.size();i++){

    	 Element nodeCell = (Element) tmpList.get(i);

    	 String strValue = null;
    	 String strExp = null;
    	 String strType = null;
    	 String strWidth = "0";
    	 String strHeight = "0";
    	 String strLength = "0";

    	 Element nodeFormat = nodeCell.element("FORMAT");

    	 if (nodeFormat != null){

    		 Element nodeTemp = nodeFormat.element("EXP");
    		 if (nodeTemp != null)
    		   strExp = nodeTemp.getText();

    		 nodeTemp = nodeFormat.element("VALUE");
    		 if (nodeTemp != null)
    		   strValue = nodeTemp.getText();

    		 strType = nodeFormat.attributeValue("type");
    		 strWidth = nodeFormat.attributeValue("width");
    		 strHeight = nodeFormat.attributeValue("height");
    		 strLength = nodeFormat.attributeValue("length");

    	 }

    	 Element nodeSummary = nodeCell.element("SUMMARY");

    	 if ((strValue == null || "".equals(strValue)) && nodeSummary == null)
    		 continue;

    	 if (strExp == null || "".equals(strExp))
    		 strExp = "[1]";

    	 List rowList = nodeSummary.elements("ROW");

    	 if ("9".equals(strType)){
    		 if (strValue == null || "".equals(strValue))
    			 continue;
    		 strExp = "'" + strValue + "'";
    	 }

    	 intXCells++;
    	 DisplayVO objDisp = new DisplayVO();
    	 if (rowList != null){
	    	 String strColumn = "";
	    	 for(int j = 1 ; j <= rowList.size(); j++){

	    		 Element nodeRow = (Element)rowList.get(j - 1);
	    		 String strTempTable;
	    		 strTempTable = nodeRow.elementText("OWNER")
		                      + "." + nodeRow.elementText("TNAME");
	    		 strColumn =  nodeRow.elementText("FNAME");
//	    		 useTable(strTempTable,alUseTableList);
	    		 recordUsedTables(strTempTable);
	             strExp = strExp.replaceAll("\\[" + j + "\\]", strColumn);
	             if (strValue == null || "".equals(strValue))
	            	 strValue = nodeRow.elementText("FNOTE");
	             objDisp.setTable(strTempTable);
	             if (strType == null || "".equals(strType))
	                objDisp.setType(nodeRow.elementText("STYLE"));
	             else
	            	 objDisp.setType(strType);
	             objDisp.setDic(nodeRow.elementText("DIC_CODE"));
	             objDisp.setFmt(nodeRow.elementText("FMT"));
                 objDisp.setDicTable(nodeRow.elementText("DIC_TABLE"));
                 objDisp.setKeyField(nodeRow.elementText("KEY_FIELD"));
                 objDisp.setValueField(nodeRow.elementText("VALUE_FIELD"));
	    	 }
	    	 objDisp.setField(strExp);
	    	 if (rowList.size() > 1){
	    		 objDisp.setType("1");
	    		 objDisp.setDic("");
	    	 }
    	 }
    	 
    	 if (strWidth == null || "".equals(strWidth))
    		 objDisp.setWidth("0");
    	 else
    		 objDisp.setWidth(strWidth);
    	 
    	 if (strHeight == null || "0".equals(strHeight))
    		 objDisp.setHeight("0");
    	 else
    		 objDisp.setHeight(strHeight);
    	 
    	 if (strLength == null || "0".equals(strLength))
    		 objDisp.setLength("0");
    	 else
    		 objDisp.setLength(strLength);

         objDisp.setName(strValue);


         alDisplayFieldList.add(objDisp);
    	 if (intXCells > 1)
    	     strResult += ","+strExp;
    	 else
    		 strResult = strExp;

     }
     return strResult;

   }

   private String parseRelation(Element p_NodeRelation)
   {
	   String strResult = "";

	   if (p_NodeRelation == null)
		   return strResult;

	   List tmpList = p_NodeRelation.elements("RELATION");

	   {
		   //�������¼�������ı���Ϣ
		   Map vertex = new HashMap(0);
		   String[][] info = new String[tmpList.size()][2];
		   for(int i = 0 ; i < tmpList.size(); i++){
			   Element nodeRelation = (Element) tmpList.get(i);
	
			   String strTempTable1 = nodeRelation.element("FIELD1").element("ROW").elementText("OWNER")
					           + "."
					           + nodeRelation.element("FIELD1").element("ROW").elementText("TNAME");
			   String strTempTable2 = nodeRelation.element("FIELD2").element("ROW").elementText("OWNER")
					           + "."
					           + nodeRelation.element("FIELD2").element("ROW").elementText("TNAME");
			   if(!vertex.containsKey(strTempTable1))
				   vertex.put(strTempTable1, null);
			   if(!vertex.containsKey(strTempTable2))
				   vertex.put(strTempTable2, null);
			   info[i][0] = strTempTable1;
			   info[i][1] = strTempTable2;
		   }
		   
		   if(vertex.size()!=mapUsedTables.size())
		   {
			   Relation rel = new Relation();
			   mapUsedTables = rel.getTables(info, mapUsedTables);
		   }
		   vertex = null;
		   info = null;
	   }
	   
	   for(int i = 0 ; i < tmpList.size(); i++){

		   Element nodeRelation = (Element) tmpList.get(i);
		   String strRelation = nodeRelation.attributeValue("value");

		   String strTempTable1 = nodeRelation.element("FIELD1").element("ROW").elementText("OWNER")
				           + "."
				           + nodeRelation.element("FIELD1").element("ROW").elementText("TNAME");
		   String strTempTable2 = nodeRelation.element("FIELD2").element("ROW").elementText("OWNER")
				           + "."
				           + nodeRelation.element("FIELD2").element("ROW").elementText("TNAME");

		   if(mapUsedTables.get(strTempTable1)==null || mapUsedTables.get(strTempTable2)==null)
			   continue;
		   
		   if (!strResult.equals(""))
			   strResult += " AND ";
		   
		   if ("=(+)".equals(strRelation)){
			   strResult += strTempTable1
	             + "."
	             + nodeRelation.element("FIELD1").element("ROW").elementText("FNAME")
	             + "="
	             + strTempTable2
	             + "."
	             + nodeRelation.element("FIELD2").element("ROW").elementText("FNAME")+"(+)";
		   }
		   else{
			   strResult += strTempTable1
	             + "."
	             + nodeRelation.element("FIELD1").element("ROW").elementText("FNAME")
	             + strRelation
	             + strTempTable2
	             + "."
	             + nodeRelation.element("FIELD2").element("ROW").elementText("FNAME");
		   }

//		   useTable(strTempTable1,alUseTableList);
//		   useTable(strTempTable2,alUseTableList);
//			 recordUsedTables(strTempTable1);
//			 recordUsedTables(strTempTable2);
	   }

	   return strResult;
   }

   private boolean parseImageSet(Element p_NodeImageSet) {

	   boolean bResult = false;
	   if (p_NodeImageSet == null)
		   return bResult;

	   Element nodeImage = p_NodeImageSet.element("IMAGE");
	   strImgTable = nodeImage.elementText("TABLE");
	   strImgField = nodeImage.elementText("FIELD");

	   alImageDispList.clear();

	   Element nodeDisplays = p_NodeImageSet.element("DISPLAYS");
	   if (nodeDisplays != null){

		   List tmpList = nodeDisplays.elements("DISPLAY");
//		   String strField;
		   String strName;
		   for(int i = 0 ; i < tmpList.size();i++){
			   Element nodeDisplay = (Element) tmpList.get(i);
			   if (nodeDisplay != null){
				   DisplayVO objDisp = new DisplayVO();
				   String strDispTable ;
				   String strDispField ;
				   String strDispType ;
				   String strDispDic ;
				   String strFmt;

				   Element nodeRow = nodeDisplay.element("ROW");
				   strDispTable = nodeRow.elementText("OWNER") + "." +
				                  nodeRow.elementText("TNAME");
				   strDispField = nodeRow.elementText("FNAME");
				   strDispDic = nodeRow.elementText("DIC_CODE");
				   strDispType = nodeRow.elementText("STYLE");
				   strFmt = nodeRow.elementText("FMT");

//				   strField =  strDispTable
//				             + "."
//				             + strDispField;
				   strName = nodeRow.elementText("FNOTE");

//				   alDispFieldList.add(strField);
//				   alDispNameList.add(strName);

				   objDisp.setTable(strDispTable);
				   objDisp.setField(strDispField);
				   objDisp.setName(strName);
				   objDisp.setDic(strDispDic);
				   objDisp.setType(strDispType);
				   objDisp.setFmt(strFmt);

				   alImageDispList.add(objDisp);

			   }
          }

	   }

	   bResult = true;

	   return bResult;
   }

   private void parseImageRelation(Element p_NodeRelation){

		if (p_NodeRelation == null)
			return;

		alRelImageList.clear();

		List tmpList = p_NodeRelation.elements("RELATION");
		for (int i = 0; i < tmpList.size(); i++) {

			Element nodeRelation = (Element) tmpList.get(i);

			String strSourceTable = nodeRelation.element("FIELD1").element("ROW").elementText("OWNER")
								+ "."
								+ nodeRelation.element("FIELD1").element("ROW").elementText("TNAME");

	        String strDestTable = nodeRelation.element("FIELD2").element("ROW").elementText("OWNER")
								+ "."
								+ nodeRelation.element("FIELD2").element("ROW").elementText("TNAME");
	        if (strImgTable.equals(strSourceTable) || strImgTable.equals(strDestTable)){
				RelationVO objRel = new RelationVO();

				String strOperate = nodeRelation.attributeValue("value");

				if ("=(+)".equals(strOperate))
					strOperate = "=";


				String strRelImgType;
				String strRelImgField;
				String strRelDataField;
			    if (strImgTable.equals(strSourceTable)){
				    strRelImgType  = nodeRelation.element("FIELD1").element("ROW").elementText("STYLE");
				    strRelImgField = nodeRelation.element("FIELD1").element("ROW").elementText("FNAME");
				    strRelDataField = nodeRelation.element("FIELD2").element("ROW").elementText("FNAME");
				    objRel.setSourceTable(strSourceTable);
				    objRel.setDestTable(strDestTable);
				    objRel.setDestField(strRelDataField);
				    objRel.setSourceField(strRelImgField);
				    objRel.setSourceType(strRelImgType);
				    strRelDataField = strDestTable + "." + strRelDataField;
			    }
			    else{
//			    	String strTmpOP = "";
			    	if (">".equals(strOperate))
			    		strOperate = "<";
					else if (">=".equals(strOperate))
						strOperate = "<=";
					else if ("<".equals(strOperate))
						strOperate = ">";
					else if ("<=".equals(strOperate))
						strOperate = ">=";

				    strRelImgType  = nodeRelation.element("FIELD2").element("ROW").elementText("STYLE");
				    strRelImgField = nodeRelation.element("FIELD2").element("ROW").elementText("FNAME");
				    strRelDataField = nodeRelation.element("FIELD1").element("ROW").elementText("FNAME");
				    objRel.setSourceTable(strDestTable);
				    objRel.setDestTable(strSourceTable);
				    objRel.setDestField(strRelDataField);
				    objRel.setSourceField(strRelImgField);
				    objRel.setSourceType(strRelImgType);
				    strRelDataField = strDestTable + "." + strRelDataField;
			   }
			    objRel.setOp(strOperate);

			   alRelImageList.add(objRel);

	        }
		}

   }

   private void parseDetailRelation(Element p_NodeRelation,Element p_DetailElement){

		if (p_NodeRelation == null)
			return;

		alRelDetailList.clear();
		String strTableID;
		int intFlag ;
		List tmpList = p_NodeRelation.elements("RELATION");

		for (int i = 0; i < tmpList.size(); i++) {

			Element nodeRelation = (Element) tmpList.get(i);

			String strSourceTable = nodeRelation.element("FIELD1").element("ROW").elementText("OWNER")
								+ "."
								+ nodeRelation.element("FIELD1").element("ROW").elementText("TNAME");

	        String strDestTable = nodeRelation.element("FIELD2").element("ROW").elementText("OWNER")
								+ "."
								+ nodeRelation.element("FIELD2").element("ROW").elementText("TNAME");
	        intFlag = 0;
	        for (int j = 0; j < alDetailTableList.size();j++){
	        	strTableID = (String) alDetailTableList.get(j);
	        	if (strTableID.equals(strSourceTable)){
	        		intFlag = 1;
	        		break;
	        	}else if (strTableID.equals(strDestTable)){
	        		intFlag = 2;
	        		break;
	        	}
	        }
	        if (intFlag == 1){
	        	Element objTableElement = getTableElement(strSourceTable,p_DetailElement);
	        	String strSourceField;
	        	String strSourceType;
	        	String strDestField;
	        	String strOperate = nodeRelation.attributeValue("value");

	        	if ("=(+)".equals(strOperate))
	        		strOperate = "=";

	        	if (objTableElement != null){

	        		strSourceField = nodeRelation.element("FIELD1").element("ROW").elementText("FNAME");
	        		strSourceType = nodeRelation.element("FIELD1").element("ROW").elementText("STYLE");
	        		strDestField = nodeRelation.element("FIELD2").element("ROW").elementText("FNAME");


	        		Element objRelElement = objTableElement.addElement("REL");
	        		objRelElement.addElement("TID").addText(strSourceTable);
	        		objRelElement.addElement("FID").addText(strSourceField);
	        		objRelElement.addElement("TYPE").addText(strSourceType);
	        		objRelElement.addElement("OP").addText(strOperate) ;
	        		objRelElement.addElement("RTID").addText(strDestTable);
	        		objRelElement.addElement("RFID").addText(strDestField);

	        		RelationVO objRelVO = new RelationVO();
	        		objRelVO.setOp(strOperate);
	        		objRelVO.setSourceTable(strSourceTable);
	        		objRelVO.setSourceField(strSourceField);
	        		objRelVO.setSourceType(strSourceType);
	        		objRelVO.setDestField(strDestField);
	        		objRelVO.setDestTable(strDestTable);
	        		alRelDetailList.add(objRelVO);

	        	}

	        	objTableElement = getTableElement(strDestTable,p_DetailElement);
                if (objTableElement != null){
	        		strSourceField = nodeRelation.element("FIELD2").element("ROW").elementText("FNAME");
	        		strSourceType = nodeRelation.element("FIELD2").element("ROW").elementText("STYLE");
	        		strDestField = nodeRelation.element("FIELD1").element("ROW").elementText("FNAME");

	        		String strTmpOP;
	        		if (">".equals(strOperate))
	        			strTmpOP = "<";
	        		else if (">=".equals(strOperate))
	        			strTmpOP = "<=";
	        		else if ("<".equals(strOperate))
	        			strTmpOP = ">";
	        		else if ("<=".equals(strOperate))
	        			strTmpOP = ">=";
	        		else
	        			strTmpOP = strOperate;

	        		Element objRelElement = objTableElement.addElement("REL");
	        		objRelElement.addElement("TID").addText(strDestTable);
	        		objRelElement.addElement("FID").addText(strSourceField);
	        		objRelElement.addElement("TYPE").addText(strSourceType);
	        		objRelElement.addElement("OP").addText(strTmpOP) ;
	        		objRelElement.addElement("RTID").addText(strSourceTable);
	        		objRelElement.addElement("RFID").addText(strDestField);

	        		RelationVO objRelVO = new RelationVO();
	        		objRelVO.setOp(strTmpOP);
	        		objRelVO.setSourceTable(strDestTable);
	        		objRelVO.setSourceField(strSourceField);
	        		objRelVO.setSourceType(strSourceType);
	        		objRelVO.setDestTable(strSourceTable);
	        		objRelVO.setDestField(strDestField);

	        		alRelDetailList.add(objRelVO);

	        	}
	        }
	        else if (intFlag == 2){
	        	Element objTableElement = getTableElement(strDestTable,p_DetailElement);
	        	String strSourceField;
	        	String strSourceType;
	        	String strDestField;
	        	String strOperate = nodeRelation.attributeValue("value");

	        	if ("=(+)".equals(strOperate))
	        		strOperate = "=";

	        	if (objTableElement != null){

	        		strSourceField = nodeRelation.element("FIELD2").element("ROW").elementText("FNAME");
	        		strSourceType = nodeRelation.element("FIELD2").element("ROW").elementText("STYLE");
	        		strDestField = nodeRelation.element("FIELD1").element("ROW").elementText("FNAME");

	        		String strTmpOP;
	        		if (">".equals(strOperate))
	        			strTmpOP = "<";
	        		else if (">=".equals(strOperate))
	        			strTmpOP = "<=";
	        		else if ("<".equals(strOperate))
	        			strTmpOP = ">";
	        		else if ("<=".equals(strOperate))
	        			strTmpOP = ">=";
	        		else
	        			strTmpOP = strOperate;

	        		Element objRelElement = objTableElement.addElement("REL");
	        		objRelElement.addElement("TID").addText(strDestTable);
	        		objRelElement.addElement("FID").addText(strSourceField);
	        		objRelElement.addElement("TYPE").addText(strSourceType);
	        		objRelElement.addElement("OP").addText(strTmpOP) ;
	        		objRelElement.addElement("RTID").addText(strSourceTable);
	        		objRelElement.addElement("RFID").addText(strDestField);

	        		RelationVO objRelVO = new RelationVO();
	        		objRelVO.setOp(strTmpOP);
	        		objRelVO.setSourceTable(strDestTable);
	        		objRelVO.setSourceField(strSourceField);
	        		objRelVO.setSourceType(strSourceType);
	        		objRelVO.setDestTable(strSourceTable);
	        		objRelVO.setDestField(strDestField);

	        		alRelDetailList.add(objRelVO);

	        	}

	        	objTableElement = getTableElement(strSourceTable,p_DetailElement);
                if (objTableElement != null){

	        		strSourceField = nodeRelation.element("FIELD1").element("ROW").elementText("FNAME");
	        		strSourceType = nodeRelation.element("FIELD1").element("ROW").elementText("STYLE");
	        		strDestField = nodeRelation.element("FIELD2").element("ROW").elementText("FNAME");

	        		Element objRelElement = objTableElement.addElement("REL");
	        		objRelElement.addElement("TID").addText(strSourceTable);
	        		objRelElement.addElement("FID").addText(strSourceField);
	        		objRelElement.addElement("TYPE").addText(strSourceType);
	        		objRelElement.addElement("OP").addText(strOperate) ;
	        		objRelElement.addElement("RTID").addText(strDestTable);
	        		objRelElement.addElement("RFID").addText(strDestField);

	        		RelationVO objRelVO = new RelationVO();
	        		objRelVO.setOp(strOperate);
	        		objRelVO.setSourceTable(strSourceTable);
	        		objRelVO.setSourceField(strSourceField);
	        		objRelVO.setSourceType(strSourceType);
	        		objRelVO.setDestTable(strDestTable);
	        		objRelVO.setDestField(strDestField);

	        		alRelDetailList.add(objRelVO);

	        	}
	        }
		}
  }

   private String getRowFilterCondition(Element p_NodeFilter){

	   StringBuffer buffer = new StringBuffer();
	   buffer.append("");

	   String strCode = p_NodeFilter.elementText("CODE");
//       String strFieldKind = p_NodeFilter.elementText("FIELDKIND");
	   if (strCode == null)
		  strCode = "";

		String strValue ;
		strValue = p_NodeFilter.elementText("VALUE");

		String strTemp = p_NodeFilter.elementText("FIELDKIND");

		if (strTemp == null){
			strTemp = p_NodeFilter.element("ROW").elementText("STYLE");
			if (strTemp == null || "".equals(strTemp))
			    strTemp = "1";
			if ("2".equals(strTemp)){
				strTemp = "7";
			}
			else if ("3".equals(strTemp)){
				strTemp = "6";
			}
			else if ("7".equals(strTemp)){
				strTemp = "2";
			}
		}

		int intType = Integer.parseInt(strTemp);


		String strFieldName;
		String strFieldValue;

		if (!"".equals(strValue))
			if (intType != Integer.valueOf(FreeQueryProps.FIELD_KIND_DIC_6).intValue() )
				strFieldValue = strValue;
			else
				strFieldValue = strCode;
		else
			strFieldValue = "";
		boolean nullFlag = false;  //null operation flag
		String strOperate = p_NodeFilter.elementText("OPERATE");
		if ("is null".equals(strOperate) || "is not null".equals(strOperate)){
			nullFlag = true;
		}

		if (!nullFlag && "".equals(strFieldValue)){
//            if(strValue.equals("") && strCode.equals(""))//add by wuxp 20080507 ����ʱ������ѯ
            return buffer.toString();
		}

	    Element nodeFormat = p_NodeFilter.element("FORMAT");

		strTemp = nodeFormat.attributeValue("type");
	    if (strTemp == null || "".equals(strTemp))
	       strTemp = "0";
		int intFormatType = Integer.parseInt(strTemp);
		String strFormatTarget = nodeFormat.attributeValue("target");

		String strFormat;

//		if (strFormat == null)
//			strFormat = "YYYYMMDDHH24MISS";
//		else if ("undefined".equals(strFormat))
//			strFormat = "YYYYMMDDHH24MISS";

//		String strLogic = " AND ";
		Element nodeRow = p_NodeFilter.element("ROW");
		strFormat = p_NodeFilter.elementText("FIELDMEMO");
		if (strFormat == null)
			strFormat = nodeRow.elementText("FMT");
		String strTempTable =  nodeRow.elementText("OWNER") + "."
	                        + nodeRow.elementText("TNAME");
		strFieldName = strTempTable + "."
				            + nodeRow.elementText("FNAME");
//        useTable(strTempTable,alUseTableList);
		 recordUsedTables(strTempTable);

		if (nullFlag){
	     	buffer.append( strFieldName + " " + strOperate + " ");
            return buffer.toString();
		}
		String strSetStyle = p_NodeFilter.elementText("SETSTYLE");
		ArrayList alValueList = new ArrayList();

		//set and extent type operation
		if (FreeQueryProps.SET_STYLE_SET_2.equals(strSetStyle) || FreeQueryProps.SET_STYLE_RANGE_8.equals(strSetStyle)){

			try {
				Document tmpDoc = DocumentHelper.parseText("<ROOT>" + strCode +  "</ROOT>");

				Element rootElement = tmpDoc.getRootElement();
				List tmpSetList = rootElement.elements();
				for(int tmpi = 0; tmpi < tmpSetList.size(); tmpi++){
					Element tmpElement = (Element) tmpSetList.get(tmpi);
					String strNum = tmpElement.attributeValue("num");
					String strLOp = tmpElement.elementText("LOP");
					String strROp = tmpElement.elementText("ROP");
					String strVal = tmpElement.elementText("VAL");
					String strFmt = tmpElement.elementText("FMT");
//					��ѯ�������ֵ䡢��������ģ��Ĳ�ѯ
					if (intType == 6 && "like".equals(strROp)&& !Pub.empty(strVal))
						strVal = "%" + strVal + "%";
					SeekValueVO tmpSeekVO = new SeekValueVO();
					tmpSeekVO.setNum(strNum);
					tmpSeekVO.setLOp(strLOp);
					tmpSeekVO.setROp(strROp);
					tmpSeekVO.setVal(strVal);
					tmpSeekVO.setFmt(strFmt);
					alValueList.add(tmpSeekVO);
				}
				tmpDoc = null;
			} catch (DocumentException e) {
                alValueList.add(null);
				e.printStackTrace();
			}

		}
		else{
//			�̶���������Ϊ�ֵ�
			if (intType == Integer.valueOf(FreeQueryProps.FIELD_KIND_DIC_6).intValue() && "like".equals(strOperate)&& !Pub.empty(strFieldValue))
				strFieldValue = "%" + strFieldValue + "%";
			SeekValueVO tmpSeekVO = new SeekValueVO();
			tmpSeekVO.setNum("0");
			tmpSeekVO.setLOp("");
			tmpSeekVO.setROp(strOperate);
			tmpSeekVO.setVal(strFieldValue);
			tmpSeekVO.setFmt(strFormat);

			alValueList.add(tmpSeekVO);
		}


		boolean isAssign = false;
		if (!"0".equals(strFormatTarget)) {
            switch (intFormatType) {//1��ȡ;2����;3�Զ���;4��0;5ż0;6ȫ0;7�ֵ����(������ʽ)     //add by jiawh 20080805

			case 1:
				if (intType == 1 || intType == 7) {
					String strStart = nodeFormat.elementText("START");
					String strEnd = nodeFormat.elementText("END");

					if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
						for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
							SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
							String tmpString = tmpSeekVO.getVal();
							if (!"".equals(strStart) && !"".equals(strEnd)) {
								tmpString = " substr('" + tmpString
										+ "'," + strStart + "," + strEnd + ") ";
								isAssign = true;
							} else if (!"".equals(strStart)) {
								tmpString = " substr('" + tmpString
										+ "'," + strStart + ")";
								isAssign = true;
							}
							tmpSeekVO.setVal(tmpString);
							alValueList.set(tmpi, tmpSeekVO);
						}
					} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
						if (!"".equals(strStart) && !"".equals(strEnd))
							strFieldName = " substr(" + strFieldName + ","
									+ strStart + "," + strEnd + ") ";
						else if (!"".equals(strStart))
							strFieldName = " substr(" + strFieldName + ","
									+ strStart + ")";
					}
				}
				break;
			case 2:

				if (intType == 4 || intType == 5) {//����
					String strMode = nodeFormat.elementText("MODE");
                    strFieldName = " to_char(" + strFieldName + ",'" + strMode + "')";
					for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
						SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
						String tmpString = tmpSeekVO.getVal();
						if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
							tmpString = "'" + tmpString + "'";
//							tmpString = " to_char('" + tmpString
//									  + "','" + strMode + "')";
						} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
							tmpString = "'" + tmpString + "'";
//							tmpString = " to_date('" + tmpString
//									  + "','" + strMode + "')";
						}
						isAssign = true;
						tmpSeekVO.setVal(tmpString);
						alValueList.set(tmpi, tmpSeekVO);
					}
				}

				break;
			case 3:
				String strMode = nodeFormat.elementText("MODE");
				switch (intType) {
				case 1:
				case 6:
				case 7:
					if (!"".equals(strMode)) {
						if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
							for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
								SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
								String tmpString = tmpSeekVO.getVal();
								tmpString = strMode.replaceAll("?",tmpString);
								isAssign = true;
								tmpSeekVO.setVal(tmpString);
								alValueList.set(tmpi, tmpSeekVO);
							}

						} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
							strFieldName = strMode.replaceAll("?",strFieldName);
						}
					}
                   break;
				case 4:
				case 5:
//				case 8:
					if (!"".equals(strMode)) {
						if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
							for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
								SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
								String tmpString = tmpSeekVO.getVal();
								tmpString = strMode.replaceAll("?",tmpString);
								isAssign = true;
								tmpSeekVO.setVal(tmpString);
								alValueList.set(tmpi, tmpSeekVO);
							}

						} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
							strFieldName = strMode.replaceAll("?",strFieldName);
//							strFieldValue = " to_date('" + strFieldValue
//									+ "','" + strFormat + "')";
//							isAssign = true;
						}
					}
					break;
				case 2:
				case 3:
					if (!"".equals(strMode)) {
						if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
							for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
								SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
								String tmpString = tmpSeekVO.getVal();
								tmpString = strMode.replaceAll("?",tmpString);
								isAssign = true;
								tmpSeekVO.setVal(tmpString);
								alValueList.set(tmpi, tmpSeekVO);
							}

						} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
							strFieldName = strMode.replaceAll("?",strFieldName);
//							isAssign = true;
						}
					}
					break;
				}
				break;
			case 4:
				switch (intType) {
				case 1:
				case 6:
				case 7:
					if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
						for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
							SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
							String tmpString = tmpSeekVO.getVal();
							tmpString = "'" + trimChar(tmpString, '0', 1) + "'";
							isAssign = true;
							tmpSeekVO.setVal(tmpString);
							alValueList.set(tmpi, tmpSeekVO);
						}

					} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
						strFieldName = "trimChar('" + strFieldName + "','0','1')";
					}
					break;
				}
				break;
			case 5:
				switch (intType) {
				case 1:
				case 6:
				case 7:
					if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
						for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
							SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
							String tmpString = tmpSeekVO.getVal();
							tmpString = "'"	+ trimChar(tmpString, '0', 2) + "'";
							isAssign = true;
							tmpSeekVO.setVal(tmpString);
							alValueList.set(tmpi, tmpSeekVO);
						}

					} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
						strFieldName = "trimChar('" + strFieldName + "','0','2')";
					}
					break;
				}
				break;
			case 6:
				switch (intType) {
				case 1:
				case 6:
				case 7:
					if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
						for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
							SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
							String tmpString = tmpSeekVO.getVal();
							tmpString = "'"	+ trimChar(tmpString, '0', 3) + "'";
							isAssign = true;
							tmpSeekVO.setVal(tmpString);
							alValueList.set(tmpi, tmpSeekVO);
						}

					} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
						strFieldName = "trimChar('" + strFieldName + "','0','3')";
					}
					break;
				}
				break;
			}

		}

		if (strSetStyle != null) {//2.����,3.����,4.ͬ��(��),5.ͬ��(ȫ),8.���
			if (FreeQueryProps.SET_STYLE_TYJ_4.equals(strSetStyle)) {// ͬ����
				if (intType == 1) {
					 if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget))
						 strFieldName = "toSpell(" + strFieldName + ")";
					 else{
						 String strSpellField = p_NodeFilter.elementText("SPELL");
						 if (strSpellField != null )
							 strFieldName = strSpellField;
					 }
					 for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
						 SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
							String tmpString = tmpSeekVO.getVal();
						if (isAssign){
                            tmpString = SpellCache.getInstance().getSpell(tmpString);
                        }
						else {
                            tmpString = "'" + SpellCache.getInstance().getSpell(tmpString) + "'";
						}
						isAssign = true;
						tmpSeekVO.setVal(tmpString);
						alValueList.set(tmpi, tmpSeekVO);
					 }
				}
			} else if (FreeQueryProps.SET_STYLE_TYQ_5.equals(strSetStyle)) {// ͬ��ȫ

				if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget))
					strFieldName = "toASpell(" + strFieldName + ")";
				else{
					 String strSpellField = p_NodeFilter.elementText("ASPELL");
					 if (strSpellField != null )
						 strFieldName = strSpellField;
				 }

				for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
					SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
					String tmpString = tmpSeekVO.getVal();
					if (isAssign)
//						tmpString = "toASpell(" + tmpString
//								+ ")";
                        tmpString = SpellCache.getInstance().getAspell(tmpString);

					else {
//						tmpString = "toASpell('" + tmpString
//								+ "')";
                        tmpString = "'" + SpellCache.getInstance().getAspell(tmpString) + "'";
					}
					isAssign = true;

					tmpSeekVO.setVal(tmpString);
					alValueList.set(tmpi, tmpSeekVO);
			   }
		   }
	   }

//		if (intType == 4 && intFormatType == 7) {
//			for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
//				SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
//				String tmpString = tmpSeekVO.getVal();
//				buffer.append(strFieldName + getDateCondition("1", strOperate, tmpString));
//			}
//		} else {

			if (!isAssign) {
				boolean isCard = false;
				for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){
					SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
					String tmpString = tmpSeekVO.getVal();
					String strDateFormat = tmpSeekVO.getFmt();
					String strROp = tmpSeekVO.getROp();
					
					switch (intType) {
					case 1:
					case 6:
						tmpString = "'" + tmpString + "'";
						break;
					case 7:
						if (tmpString.length() == 15 || tmpString.length() == 18)
						{
							if(strSetStyle.equals("13"))
							{
								isCard = true;
								if (tmpString.length() == 15)
								{
									tmpString = "(" + strFieldName + " " + strROp + " '" + tmpString + "' OR "
									          + strFieldName + " " + strROp + " '" + Pub.CalID_15to18(tmpString) + "')";
								}
								else
								{
									tmpString = "(" + strFieldName + " " + strROp + " '" + tmpString + "' OR "
							          + strFieldName + " " + strROp + " '" + tmpString.substring(0,6)+tmpString.substring(8,17) + "')";
								}
							}
							else
								   tmpString = "'" + tmpString + "'";
						}
						else
						   tmpString = "'" + tmpString + "'";
						break;
					case 4:
					case 5:
					case 8:
//						if ("2".equals(strDateFormat)){
						if (tmpString.length() < 4){

							tmpString = getAgeDateCondition("1", strROp, tmpString);
							strROp = "";

						}
//						else if ("4".equals(strDateFormat)){
					    else if (tmpString.length() == 4){
//							tmpString = getYearDateCondition( strROp, tmpString);
//							strROp = "";					    	
					    	strFieldName = " to_char(" + strFieldName + ",'mmdd')";
					    	tmpString = "'" + tmpString +"'";
						}
//						else if ("6".equals(strDateFormat)){
					    else if (tmpString.length() == 6){
//							tmpString = getYearMonthDateCondition( strROp, tmpString);
//							strROp = "";
					    	strFieldName = " to_char(" + strFieldName + ",'yymmdd')";
					    	tmpString = "'" + tmpString +"'";
						}
						else {
							if (FreeQueryProps.FMT_TARGER_VALUE_1.equals(strFormatTarget)) {
								strFieldName = " to_char(" + strFieldName + ",'" + strDateFormat + "')";
								tmpString = "'" + tmpString + "'";
								
							} else if (FreeQueryProps.FMT_TARGER_SOURCE_2.equals(strFormatTarget)) {
								strFieldName = " to_char(" + strFieldName + ",'" + strDateFormat + "')";
								tmpString = "'" + tmpString + "'";

							} else {
								tmpString = " to_date('" + tmpString + "','" + strDateFormat + "')";
							}
					    }
						break;
					}
					buffer.append(tmpSeekVO.getLOp());
					if (!isCard)
					    buffer.append(strFieldName + " " + strROp + " " + tmpString);
					else
						buffer.append(tmpString);

				}
			}
			else{
				for(int tmpi = 0 ; tmpi < alValueList.size(); tmpi++){

					SeekValueVO tmpSeekVO = (SeekValueVO) alValueList.get(tmpi);
					String tmpString = tmpSeekVO.getVal();

					buffer.append(tmpSeekVO.getLOp());
					buffer.append(strFieldName + " " + tmpSeekVO.getROp() + " " + tmpString);

				}
			}
			alValueList.clear();
//		}
		if (FreeQueryProps.SET_STYLE_SET_2.equals(strSetStyle) || FreeQueryProps.SET_STYLE_RANGE_8.equals(strSetStyle))
			return "(" + buffer.toString() + ")";
		else
		    return buffer.toString();
   }
   
   private String parseCombineFilters(Document combineCondition)
   {

		QueryConditionList list = RequestUtil.getConditionList(combineCondition);
		String condition = list == null ? "" : list.getConditionWhere();
		
		List cList = combineCondition.selectNodes("//CONDITIONS/CONDITION");
		for(int i=0; i<cList.size(); i++)
		{
			Element elem = (Element)cList.get(i);
			String fName = elem.selectSingleNode("FIELDNAME").getText();
			String tName = fName.substring(0, fName.lastIndexOf("."));
			recordUsedTables(tName);
		}
		
		return condition;
   }

   private String parseFilters(Element p_NodeFilters,ArrayList p_JoinItem) {

		String strResult = "";

		if (p_NodeFilters == null)
			return strResult;

		ArrayList alFilterList = new ArrayList();//�����������
		HashMap mapFixFilter = new HashMap();//��Ź̶���������
		ArrayList alDynamicFilter = new ArrayList();//��Ŷ�̬��������
		List tmpList = p_NodeFilters.elements("FILTER");

//		alFilterList.clear();
		String strRowCondition;
		if (p_JoinItem == null){
			for (int i = 0; i < tmpList.size(); i++) {
				Element nodeFilter = (Element) tmpList.get(i);
				Element elCondition = nodeFilter.element("CONDITION");
                String strType = elCondition.attributeValue("type");
                strRowCondition = getRowFilterCondition(nodeFilter);
                if ("2".equals(strType))
                {
                	alDynamicFilter.add(strRowCondition);
                }
                else if ("1".equals(strType))
                {
                	String strKey = String.valueOf(i+1);
                	mapFixFilter.put(strKey, strRowCondition);
                }
				
				alFilterList.add(strRowCondition);
//				strResult += strRowCondition;
			}

			String strExp = p_NodeFilters.elementText("EXPRESSION");

			if ("".equals(strExp)){
				StringBuffer tmpBuffer = new StringBuffer();
				for(int i = 0; i < alFilterList.size();i++){

					strRowCondition = (String)alFilterList.get(i);
					if (!"".equals(strRowCondition) && tmpBuffer.length() > 0)
						tmpBuffer.append(" AND ");
					tmpBuffer.append(strRowCondition);

				}
				strResult = tmpBuffer.toString();

			}else{
				StringBuffer tmpBuffer = new StringBuffer();
				char curChar;
				String strDigit = "";
//				int intDigit ;
				strRowCondition = "";
				for(int i = 0 ; i < strExp.length(); i++){
					curChar = strExp.charAt(i);
					if (Character.isDigit(curChar)){
						strDigit += curChar;
						continue;
					}else{
						if (!"".equals(strDigit)){
//							intDigit = Integer.parseInt(strDigit);
//							intDigit--;
//							if (intDigit >= 0 && intDigit < alFilterList.size()){
//								strRowCondition = (String)alFilterList.get(intDigit);
//								tmpBuffer.append(strRowCondition);
//							}else
//								tmpBuffer.append(strDigit);
							
							strRowCondition = (String)mapFixFilter.get(strDigit);
							if (strRowCondition == null){
								strDigit = "";
								if ('(' == curChar || ')' == curChar)
									tmpBuffer.append(curChar);
								continue;
							}
                            tmpBuffer.append(strRowCondition);
							
						}
						strDigit = "";
						if (curChar == '*')
							tmpBuffer.append(" AND ");
						else if (curChar == '+')
							tmpBuffer.append(" OR ");
						else
						   tmpBuffer.append(curChar);
					}
				}
				if (!"".equals(strDigit)){
//					intDigit = Integer.parseInt(strDigit);
//					intDigit--;
//					if (intDigit >= 0 && intDigit < alFilterList.size()){
//						strRowCondition = (String)alFilterList.get(intDigit);
//						tmpBuffer.append(strRowCondition);
//					}else
//						tmpBuffer.append(strDigit);
					strRowCondition = (String)mapFixFilter.get(strDigit);
					if (strRowCondition != null)
                        tmpBuffer.append(strRowCondition);
				}
				for(int i = 0 ; i < alDynamicFilter.size(); i++)
				{
					strRowCondition = (String)alDynamicFilter.get(i);
					if (!"".equals(strRowCondition) && tmpBuffer.length() > 0)
						tmpBuffer.append(" AND ");
					tmpBuffer.append(strRowCondition);
				}
				strResult = tmpBuffer.toString();
			}

		}
		else{
			bufferJoin.delete(0, bufferJoin.length());//������Ӳ�ѯ����
            //modify by jiawh ���ڹ�����ѯ�����ӹ������������
			for (int i = 0; i < tmpList.size(); i++) {
                strRowCondition = "";
                String strValue = "";
				Element nodeFilter = (Element) tmpList.get(i);
                String typeCondition = nodeFilter.element("CONDITION").attribute("type").getText();
				String strRName = nodeFilter.elementText("RNAME");
                boolean filter1Flag = null!=typeCondition && FreeQueryProps.COND_FILTER_1.equals(typeCondition);
                if(filter1Flag)
                {
                    strValue = nodeFilter.element("CODE").getText().equals("")?nodeFilter.element("VALUE").getText():nodeFilter.element("CODE").getText();
                }
//              ��ù��������е��ֶ�
                else if (strRName == null || "".equals(strRName)){
					continue;
				}

//				��ù���������ֵ
				for(int j = 0; j < p_JoinItem.size();j++){

					JoinMapVO tmpVO = (JoinMapVO) p_JoinItem.get(j);
                    if (null!=strRName && strRName.equalsIgnoreCase(tmpVO.getDRelationName())){
						strValue = tmpVO.getSFieldValue();
						break;
					}

				}
				if ("".equals(strValue))
					continue;

				Element nodeRow = nodeFilter.element("ROW");
				String strTemp = nodeRow.elementText("STYLE");
			    if (strTemp == null || "".equals(strTemp))
			       strTemp = "1";
				int intType = Integer.parseInt(strTemp);

				String strFieldName;
				String strFieldValue = "";

				switch (intType){
				case 1:
				case 2:
				case 3:
				case 9:
					strFieldValue = "'" + strValue + "'";
					break;
				case 4:
					strFieldValue = "to_date('" + strValue + "','YYYYMMDDHH24MISS')";
					break;
				case 5:
				case 6:
					break;
				case 7:
					strFieldValue = strValue;
					break;
				}
				if ("".equals(strFieldValue))
					continue;

				String strLogic = " AND ";

				String strTempTable =  nodeRow.elementText("OWNER") + "."
			                        + nodeRow.elementText("TNAME");
				recordUsedTables(strTempTable);
				strFieldName = strTempTable + "." + nodeRow.elementText("FNAME");
				
				bufferJoin.append(nodeRow.elementText("FNOTE") + "=" + strValue);
                if(filter1Flag)
                {
                    strRowCondition = getRowFilterCondition(nodeFilter);
                }
                else{

                        strRowCondition = strFieldName + " = " + strFieldValue;
                }

				if (!"".equals(strResult)&&!"".equals(strRowCondition))
				    strResult += strLogic + strRowCondition;
				else
					strResult = strRowCondition;
			}
			if ("".equals(strResult))
				strResult = " 1=2 ";
			
		}
       if( strResult.trim().equals("OR") ||strResult.trim().equals("AND"))
       {
           strResult=" ";
       }
       alFilterList.clear();
	   mapFixFilter.clear();
	   alDynamicFilter.clear();
	   return strResult;
	}

   private void getSingleTableRelation(Element p_NodeH,String p_TableName,Element p_DetailElement){

	  String strTableID = null;

	  Element nodeHeaderDetail = p_NodeH.element("DETAIL");
	  if (nodeHeaderDetail != null){
		   List tmpList = nodeHeaderDetail.elements("ITEM");

		   if (tmpList != null){
			   Element tmpNode = (Element) tmpList.get(0);
			   strTableID = tmpNode.element("ROW").elementText("TID");
		   }
	  }
      if (strTableID != null){
    	  Statement  stmt = null;
    	  Connection conn = DBUtil.getConnection();
    	  try{
    	    stmt = conn.createStatement();
    	    String strSQL = "SELECT field_name,style FROM "+FreeQueryProps.USER_FIELD+" WHERE table_id ='" +strTableID + "' AND is_key = '1'";
			ResultSet rs = stmt.executeQuery(strSQL);
			Element objTableElement = getTableElement(p_TableName,p_DetailElement);
			while (rs.next()){

	        	String strSourceField;
	        	String strSourceType;
	        	String strOperate = "=";

	        	strSourceField = rs.getString(1);
	        	strSourceType = rs.getString(2);

        		Element objRelElement = objTableElement.addElement("REL");
        		objRelElement.addElement("TID").addText(p_TableName);
        		objRelElement.addElement("FID").addText(strSourceField);
        		objRelElement.addElement("TYPE").addText(strSourceType);
        		objRelElement.addElement("OP").addText(strOperate) ;
        		objRelElement.addElement("RTID").addText(p_TableName);
        		objRelElement.addElement("RFID").addText(strSourceField);

        		RelationVO objRelVO = new RelationVO();
        		objRelVO.setOp(strOperate);
        		objRelVO.setSourceTable(p_TableName);
        		objRelVO.setSourceField(strSourceField);
        		objRelVO.setSourceType(strSourceType);
        		objRelVO.setDestField(strSourceField);
        		objRelVO.setDestTable(p_TableName);
        		alRelDetailList.add(objRelVO);
			}
			rs.close();
    	  }
    	  catch (Exception e){
    		  e.printStackTrace();
    	  }
    	  finally{
    		  try{
    		     if (stmt != null)
    		    	 stmt.close();
    		     if (conn != null)
    		    	 conn.close();
    		  }
    		  catch(Exception e){
    			  e.printStackTrace();
    		  }
    	  }
      }

   }

   private void getSingleImageRelation(Element p_NodeH,String p_TableName){

		  String strTableID = null;

		  Element nodeHeaderImage = p_NodeH.element("IMAGE");
		  if (nodeHeaderImage != null){
			  strTableID = nodeHeaderImage.element("ROW").elementText("TID");
		  }
	      if (strTableID != null){
	    	  Statement  stmt = null;
	    	  Connection conn = DBUtil.getConnection();
	    	  try{
	    	    stmt = conn.createStatement();
	    	    String strSQL = "SELECT field_name,style FROM "+FreeQueryProps.USER_FIELD+" WHERE table_id ='" +strTableID + "' AND is_key = '1'";
				ResultSet rs = stmt.executeQuery(strSQL);

				while (rs.next()){

		        	String strSourceField;
		        	String strSourceType;
		        	String strOperate = "=";

		        	strSourceField = rs.getString(1);
		        	strSourceType = rs.getString(2);

		        	boolean flag = false;
		        	for(int i = 0 ; i < alRelImageList.size();i++){
		        		RelationVO objTmpRelVO = (RelationVO)alRelImageList.get(i);
		        		if ( p_TableName.equals(objTmpRelVO.getSourceTable())
		        				&& strSourceField.equals(objTmpRelVO.getSourceField())
		        				&& strOperate.equals(objTmpRelVO.getOp())){
		        			flag = true;
		        			break;
		        		}
		        	}
		        	if (flag)
		        		continue;

//	        		Element objRelElement = p_ImageElement.addElement("REL");
//	 			    objRelElement.addElement("TID").addText(p_TableName);
//	 			    objRelElement.addElement("FID").addText(strSourceField);
//	 			    objRelElement.addElement("TYPE").addText(strSourceType);
//	 			    objRelElement.addElement("OP").addText(strOperate);
//	 			    objRelElement.addElement("RTID").addText(p_TableName);
//	 			    objRelElement.addElement("RFID").addText(strSourceField);

	        		RelationVO objRelVO = new RelationVO();
	        		objRelVO.setOp(strOperate);
	        		objRelVO.setSourceTable(p_TableName);
	        		objRelVO.setSourceField(strSourceField);
	        		objRelVO.setSourceType(strSourceType);
	        		objRelVO.setDestField(strSourceField);
	        		objRelVO.setDestTable(p_TableName);
	        		alRelImageList.add(objRelVO);
				}
				rs.close();
	    	  }
	    	  catch (Exception e){
	    		  e.printStackTrace();
	    	  }
	    	  finally{
	    		  try{
	    		     if (stmt != null)
	    		    	 stmt.close();
	    		     if (conn != null)
	    		    	 conn.close();
	    		  }
	    		  catch(Exception e){
	    			  e.printStackTrace();
	    		  }
	    	  }
	      }

	   }

   private void parseJoin(Element p_NodeJoin)
   {
//	   String strResult = "";

	   if (p_NodeJoin == null)
		   return ;

	   Element queryElement = p_NodeJoin.element("QUERY");

	   List tmpList = queryElement.elements("ROW");

	   alQueryTableList.clear();
	   alQueryMapList.clear();
	   ArrayList alTmpQueryList = new ArrayList();

	   for(int i = 0 ; i < tmpList.size(); i++){

		   Element rowElement = (Element) tmpList.get(i);
		   QueryJoinVO tmpVO = new QueryJoinVO();

		   String strRelationID = rowElement.elementText("RID");
		   tmpVO.setRelationID(strRelationID);

		   String strQueryID = rowElement.elementText("QID");
		   tmpVO.setQueryID(strQueryID);

		   String strQueryName = rowElement.elementText("QNAME");
		   tmpVO.setQueryName(strQueryName);

		   String strRelationName = rowElement.elementText("RNAME");
		   tmpVO.setRelationName(strRelationName);

		   String strTableID = rowElement.elementText("TID");
		   tmpVO.setTableID(strTableID);

		   String strFieldName = rowElement.elementText("FNAME");
		   tmpVO.setFieldName(strFieldName);

		   alTmpQueryList.add(tmpVO);

	   }

	   tmpList = p_NodeJoin.elements("MAP");
	   for(int i = 0 ; i < tmpList.size(); i++){

		   Element mapElement = (Element) tmpList.get(i);
		   Element field1Element = mapElement.element("FIELD1").element("ROW");
		   Element field2Element = mapElement.element("FIELD2").element("ROW");

		   JoinMapVO tmpVO = new JoinMapVO();

		   String strSTableName;
		   String strSFieldName;
		   String strSFieldType;
		   String strSFieldDic;
		   String strSFieldFmt;
		   String strDRelationName;
		   String strDQueryID;
		   String strDQueryName;

		   strSTableName = field1Element.elementText("OWNER") + "." + field1Element.elementText("TNAME");
		   strSFieldName = field1Element.elementText("FNAME");
		   strSFieldType = field1Element.elementText("STYLE");
		   strSFieldDic = field1Element.elementText("DIC_CODE");
		   strSFieldFmt = field1Element.elementText("FMT");

		   strDRelationName = field2Element.elementText("RNAME");
		   strDQueryID = field2Element.elementText("QID");
		   strDQueryName = field2Element.elementText("QNAME");
		   tmpVO.setSTableName(strSTableName);
		   tmpVO.setSFieldName(strSFieldName);
		   tmpVO.setSFieldType(strSFieldType);
		   tmpVO.setSFieldDic(strSFieldDic);
		   tmpVO.setSFieldFmt(strSFieldFmt);
		   tmpVO.setDRelationName(strDRelationName);
		   tmpVO.setDQueryID(strDQueryID);
		   tmpVO.setDQueryName(strDQueryName);

		   alQueryMapList.add(tmpVO);
//		   useTable(strSTableName,alUseTableList);
  		   recordUsedTables(strSTableName);

		   for(int j = 0 ; j < alTmpQueryList.size(); j++){

			   QueryJoinVO tmpJoinVO = (QueryJoinVO) alTmpQueryList.get(j);
			   if (strDQueryID.equals(tmpJoinVO.getQueryID())){

				   alQueryTableList.add(tmpJoinVO);
				   alTmpQueryList.remove(j);
				   break;

			   }

		   }
	   }

   }
   
   //��¼ʹ�õı�
   private void recordUsedTables(String tableName)
   {
	   if(mapUsedTables.get(tableName)==null)
		   mapUsedTables.put(tableName, "1");
   }

   public int getRowPerPage(){
	   return intRowPerPage;
   }

   public void setRowPerPage(int p_Rows){
	   intRowPerPage = p_Rows;
   }

   public boolean getPrintFlag(){
	   return hasPrint;
   }

   public boolean getDownFlag(){
	   return hasDownload;
   }

   public boolean getBrowseFlag(){
       return hasPicBrowse;
   }

   public boolean getPicSetFlag(){
	   return hasPicSet;
   }

   public String getSwitchFlag(){
	   return strSwitchFlag;
   }

   public void setBrowseFlag(boolean p_Flag){
	   hasPicBrowse = p_Flag;
   }

   public String getTableTitle(){
	   return strTableTitle;
   }

   public String getTemplate(){
	   return strTemplate;
   }

   public String getTitleAlign(){
	   return strTitleAlign;
   }

   public String getTitleColor(){
	   return strTitleColor;
   }

   public String getTitleBGColor(){
	   return strTitleBGColor;
   }

   public String getFontSize(){
	   return strFontSize;
   }

   public String getDataOrder(){
	   return strOrderList;
   }

   public String getDataTables(){
	   return strTableList;
   }

   public String getDataRelations(){
	   return strRelationList;
   }

   public String getDataFilters(){
	   return strFilterList;
   }

//   public ArrayList getDataFilterList(){
//	   return alFilterList;
//   }

   public String getImgTable(){
	   return strImgTable;
   }

   public String getImgField(){
	   return strImgField;
   }
   
//   public String getJoinCondition()
//   {
//	   return bufferJoin.toString();
//   }

   public void  parseDocument(Document p_Doc,HttpServletRequest p_Request,
		   String p_UserName,ArrayList p_JoinItem) throws Exception
   {
	   parseDocument(p_Doc, p_Request, p_UserName, p_JoinItem, null);
   }
   
   public void  parseDocument(Document p_Doc,HttpServletRequest p_Request,
		   String p_UserName,ArrayList p_JoinItem, Document p_CombineCondition) throws Exception
   {
		Document objDocument ;
		Element objRootElement ;
		Element objDetailElement;
		Element objJoinElement;
		Element objTablesElement;
		String strDataSource = "";

		objDocument = DocumentFactory.getInstance().createDocument();
		objRootElement = objDocument.addElement("CONF");
		objDetailElement = objRootElement.addElement("DETAIL");
		objJoinElement = objRootElement.addElement("JOIN");
		objTablesElement = objRootElement.addElement("TABLES");

	   Element nodeRoot = p_Doc.getRootElement();

	   Element nodeHeader = nodeRoot.element("H");
	   
	   String detailUrl = null;
       Element nodeDetail = nodeHeader.element("DETAIL");
       if(nodeDetail !=null)
       {
    	   detailUrl = nodeDetail.attributeValue("url");
       }

       if(detailUrl !=null && detailUrl.length()>0)
       {
           objDetailElement.addAttribute("url",detailUrl);
       }
	   
       mapUsedTables.clear();
	   alDefineTableList.clear();

      //����intRowPerPage
	   parseH(nodeHeader,objDetailElement);
	   if ("".equals(strTableTitle))
	      strTableTitle = nodeRoot.attributeValue("title");
	   Element nodeX = nodeRoot.element("X");
	   parseX(nodeX);

	   Element nodeTable = nodeRoot.element("TABLES");

//	   parseTable(nodeTable);
	   if (nodeTable != null){

		   List tmpList = nodeTable.elements("ROW");
		   strDataSource = "";
		   Element objItemElement ;
	       for(int i = 0 ; i < tmpList.size(); i++){
	    	  Element nodeRow = (Element) tmpList.get(i);
	    	  String strTempTable = nodeRow.elementText("OWNER")
						          + "."
						          + nodeRow.elementText("TNAME");
	    	  String strTempDS = nodeRow.elementText("DS");
	    	  if (strTempDS == null)
	    		  strTempDS = "";
	    	  if ("".equals(strDataSource)){
	    	    if (strTempDS != null)
	    	    	strDataSource = strTempDS;
	    	  }
	    	  objItemElement = objTablesElement.addElement("ROW");
	    	  objItemElement.addElement("TID").setText(strTempTable);
	    	  objItemElement.addElement("DS").setText(strTempDS);
	    	  alDefineTableList.add(strTempTable);
	       }
	   }

	   Element nodeRelations = nodeRoot.element("RELATIONS");

	   Element nodeFilters = nodeRoot.element("FILTERS");
	   String strCombineFilterList = "";
	   if(null!=p_CombineCondition)
		   strCombineFilterList = parseCombineFilters(p_CombineCondition);
	   
	   strFilterList = parseFilters(nodeFilters,p_JoinItem);

       Element nodeImageSet = nodeRoot.element("IMAGESET");
       hasPicSet = parseImageSet(nodeImageSet);

       Element nodeJoins = nodeRoot.element("JOINS");
       parseJoin(nodeJoins);

	   strRelationList = parseRelation(nodeRelations);

	   strTableList = getTables();

//	   if (nodeRelations == null)
	   parseDetailRelation(nodeRelations,objDetailElement);

	   Element objJoinFilterElement = objJoinElement.addElement("FILTER");//������ʾ��������
	   if (bufferJoin.length() > 0)
		   objJoinFilterElement.setText(bufferJoin.toString());
	   else
	       objJoinFilterElement.setText("");
	   
	   Element objImageElement = objRootElement.addElement("IMAGE");
	   Element objPartElement = objRootElement.addElement("PART");

	   if (hasPicSet){
		      parseImageRelation(nodeRelations);
		      getSingleImageRelation(nodeImageSet,strImgTable);
	   }

       Connection conn = null;
       try {
    	   if (!"".equals(strDataSource))
		       conn = DBUtil.getConnection(strDataSource);
    	   else
    		   conn = DBUtil.getConnection();

	       if (alDetailTableList.size() == 1 && alRelDetailList.size() == 0){

	    	   String strTempTableName = (String)alDetailTableList.get(0);
	    	   getSingleTableRelation(nodeHeader,strTempTableName,objDetailElement);
		   }


		   strSwitchFlag = (String) p_Request.getParameter("flag");
		   if (strSwitchFlag == null || "".equals(strSwitchFlag) || "0".equals(strSwitchFlag)){
			   if (hasPicBrowse)
				   strSwitchFlag = "2";
			   else
				   strSwitchFlag = "1";
		   }
		   p_Request.setAttribute("flag", strSwitchFlag);
		   String whereSQL = "";

	       if (!empty(strRelationList) || !empty(strFilterList) ||!empty(strCombineFilterList))
	    	   whereSQL = " WHERE ";

	       if (!empty(strRelationList))
	     	  whereSQL += "(" + strRelationList + ")";

	       if (!empty(strFilterList))
	       {
	           if (!empty(strRelationList))
	         	  whereSQL += " AND ";
	           whereSQL += "(" + strFilterList + ")";
	       }
	       
	       if(!empty(strCombineFilterList))
	       {
	    	   if(!whereSQL.equalsIgnoreCase(" WHERE "))
	    		   whereSQL += " AND ";
	           whereSQL += "(" + strCombineFilterList + ")";
	       }
	       
		   String countSQL = "SELECT COUNT(1) FROM " + strTableList +  whereSQL ;
	       String dataSQL ;

	       int maxRows = intRowPerPage>FreeQueryProps.ROWS_LIMITED_INT ? intRowPerPage : FreeQueryProps.ROWS_LIMITED_INT;
           //20081205 by lig ,����count����ÿ�η�������10000��
           if (countSQL.indexOf("WHERE") == -1)
           {  countSQL = countSQL + " WHERE ROWNUM <= "+maxRows;}
           else
           {  countSQL = countSQL + " AND ROWNUM <= "+maxRows;
           } //END

	       ArrayList alImageSeekFieldList = new ArrayList();
	       ArrayList alDataSeekFieldList = new ArrayList();


	       String strSeekImgField = "";
	       String strSeekDataField = "";
		   FieldVO objFieldVO;

          //ȡ��������

//          intRowPerPage = 30;

		   Element objSetElement = objRootElement.addElement("SET");
 		   objSetElement.addElement("PROWS").setText(String.valueOf(intRowPerPage));
		   Element objPrintElement = objSetElement.addElement("PRINT");
		   if (hasPrint)
			   objPrintElement.setText("1");
		   else
			   objPrintElement.setText("0");
		   Element objDownElement = objSetElement.addElement("DOWN");
		   if (hasDownload)
			   objDownElement.setText("1");
		   else
			   objDownElement.setText("0");
		   objSetElement.addElement("TEMPLATE").setText(strTemplate);

		   Element objPicSetElement = objSetElement.addElement("PICSET");
		   if (hasPicSet)
			   objPicSetElement.setText("1");
		   else
			   objPicSetElement.setText("0");

		   objSetElement.addElement("TITLE").setText(strTableTitle);
		   objSetElement.addElement("TALIGN").setText(strTitleAlign);
		   objSetElement.addElement("TCOLOR").setText(strTitleColor);
		   objSetElement.addElement("TFSIZE").setText(strFontSize);
		   objSetElement.addElement("TBGCOLOR").setText(strTitleBGColor);
		   objSetElement.addElement("CROSS").setText(strCrossColor);
		   objSetElement.addElement("RCOLOR1").setText(strRowColor1);
		   if ("1".equals(strCrossColor))
			 objSetElement.addElement("RCOLOR2").setText(strRowColor2);

		   String strQueryID = nodeRoot.attributeValue("id");
		   if (strQueryID != null)
			   objSetElement.addElement("QID").setText(strQueryID);
		   else
			   objSetElement.addElement("QID").setText("");



		   for(int i = 0; i < alDisplayFieldList.size();i++)
		   {

			   DisplayVO objTmpVO;
			   objTmpVO = (DisplayVO) alDisplayFieldList.get(i);
			   objFieldVO = new FieldVO();
			   objFieldVO.setDic(objTmpVO.getDic());
			   objFieldVO.setField(objTmpVO.getField());
			   objFieldVO.setTable(objTmpVO.getTable());
			   objFieldVO.setType(objTmpVO.getType());
			   objFieldVO.setFmt(objTmpVO.getFmt());

               objFieldVO.setDicTable(objTmpVO.getDicTable());
               objFieldVO.setKeyField(objTmpVO.getKeyField());
               objFieldVO.setValueField(objTmpVO.getValueField());

			   useField(objFieldVO,alDataSeekFieldList);

			   Element objItemElement = objPartElement.addElement("ITEM");
			   objItemElement.addElement("TID").addText(objTmpVO.getTable());
			   objItemElement.addElement("FID").addText(objTmpVO.getField());
			   objItemElement.addElement("NAME").addText(objTmpVO.getName());
			   objItemElement.addElement("TYPE").addText(objTmpVO.getType());
			   objItemElement.addElement("DIC").addText(objTmpVO.getDic());
//               objItemElement.addElement("DIC_TABLE").addText(Pub.em objTmpVO.getDicTable() );
//               objItemElement.addElement("KEY_FIELD").addText(objTmpVO.getKeyField()  );
//               objItemElement.addElement("VALUE_FIELD").addText(objTmpVO.getValueField() );
			   objItemElement.addElement("FMT").addText(objTmpVO.getFmt());
			   objItemElement.addElement("W").addText(objTmpVO.getWidth());
			   objItemElement.addElement("H").addText(objTmpVO.getHeight());
			   objItemElement.addElement("L").addText(objTmpVO.getLength());
			   
		   }

		   for(int i = 0; i < alImageDispList.size();i++)
		   {

			   DisplayVO objTmpVO;
			   objTmpVO = (DisplayVO) alImageDispList.get(i);
			   objFieldVO = new FieldVO();
			   objFieldVO.setDic(objTmpVO.getDic());
			   objFieldVO.setField(objTmpVO.getField());
			   objFieldVO.setTable(objTmpVO.getTable());
			   objFieldVO.setType(objTmpVO.getType());
			   objFieldVO.setFmt(objTmpVO.getFmt());
			   useField(objFieldVO,alImageSeekFieldList);

			   Element objItemElement = objImageElement.addElement("ITEM");
			   objItemElement.addElement("TID").addText(objTmpVO.getTable());
			   objItemElement.addElement("FID").addText(objTmpVO.getField());
			   objItemElement.addElement("NAME").addText(objTmpVO.getName());
			   objItemElement.addElement("TYPE").addText(objTmpVO.getType());
			   objItemElement.addElement("DIC").addText(objTmpVO.getDic());
			   objItemElement.addElement("FMT").addText(objTmpVO.getFmt());
//			   objItemElement.addElement("W").addText(objTmpVO.getWidth());
//			   objItemElement.addElement("H").addText(objTmpVO.getHeight());

		   }

		   for(int i = 0 ; i < alRelImageList.size();i++)
		   {

			   RelationVO objTmpVO;
			   objTmpVO = (RelationVO) alRelImageList.get(i);
			   objFieldVO = new FieldVO();
			   objFieldVO.setDic("");
			   objFieldVO.setField(objTmpVO.getSourceField());
			   objFieldVO.setTable(objTmpVO.getSourceTable());
			   objFieldVO.setType(objTmpVO.getSourceType());
			   objFieldVO.setFmt("");
			   useField(objFieldVO,alImageSeekFieldList);
			   objFieldVO = new FieldVO();
			   objFieldVO.setDic("");
			   objFieldVO.setField(objTmpVO.getDestField());
			   objFieldVO.setTable(objTmpVO.getDestTable());
			   objFieldVO.setType("1");
			   objFieldVO.setFmt("");
			   useField(objFieldVO,alImageSeekFieldList);

			   Element objRelElement = objImageElement.addElement("REL");
			   objRelElement.addElement("TID").addText(objTmpVO.getSourceTable());
			   objRelElement.addElement("FID").addText(objTmpVO.getSourceField());
			   objRelElement.addElement("TYPE").addText(objTmpVO.getSourceType());
			   objRelElement.addElement("OP").addText(objTmpVO.getOp());
			   objRelElement.addElement("RTID").addText(objTmpVO.getDestTable());
			   objRelElement.addElement("RFID").addText(objTmpVO.getDestField());
		   }

		   for(int i = 0 ; i < alRelDetailList.size();i++)
		   {

			   RelationVO objTmpVO;
			   objTmpVO = (RelationVO) alRelDetailList.get(i);
			   objFieldVO = new FieldVO();
			   objFieldVO.setDic("");
			   objFieldVO.setField(objTmpVO.getSourceField());
			   objFieldVO.setTable(objTmpVO.getSourceTable());
			   objFieldVO.setType(objTmpVO.getSourceType());
			   objFieldVO.setFmt("");
			   useField(objFieldVO,alImageSeekFieldList);
			   useField(objFieldVO,alDataSeekFieldList);
			   objFieldVO = new FieldVO();
			   objFieldVO.setDic("");
			   objFieldVO.setField(objTmpVO.getDestField());
			   objFieldVO.setTable(objTmpVO.getDestTable());
			   objFieldVO.setType("1");
			   objFieldVO.setFmt("");
			   useField(objFieldVO,alImageSeekFieldList);
			   useField(objFieldVO,alDataSeekFieldList);

		   }

		   for(int i = 0 ; i < alQueryMapList.size() ; i++){
			   JoinMapVO tmpVO = (JoinMapVO) alQueryMapList.get(i);
			   objFieldVO = new FieldVO();
			   objFieldVO.setDic(tmpVO.getSFieldDic());
			   objFieldVO.setField(tmpVO.getSFieldName());
			   objFieldVO.setTable(tmpVO.getSTableName());
			   objFieldVO.setType(tmpVO.getSFieldType());
			   objFieldVO.setFmt(tmpVO.getSFieldFmt());
			   useField(objFieldVO,alDataSeekFieldList);
		   }

		   Element objImageSeekElement =  objImageElement.addElement("SEEK");
		   for(int i = 0 ;i < alImageSeekFieldList.size();i++)
		   {

			   objFieldVO = (FieldVO) alImageSeekFieldList.get(i);
			   Element objRelElement = objImageSeekElement.addElement("ITEM");
			   objRelElement.addElement("TID").addText(objFieldVO.getTable());
			   objRelElement.addElement("FID").addText(objFieldVO.getField());
			   objRelElement.addElement("TYPE").addText(objFieldVO.getType());
			   objRelElement.addElement("DIC").addText(objFieldVO.getDic());
			   objRelElement.addElement("FMT").addText(objFieldVO.getFmt());
			   if (!"".equals(strSeekImgField))
				   strSeekImgField += "," + objFieldVO.getTable()+"."+objFieldVO.getField();
			   else
				   strSeekImgField = objFieldVO.getTable()+"."+objFieldVO.getField();

		   }

		   for(int i = 0 ;i < alQueryTableList.size(); i++){

			   QueryJoinVO tmpQueryVO = (QueryJoinVO) alQueryTableList.get(i);
			   String strQID = tmpQueryVO.getQueryID();
			   String strQName = tmpQueryVO.getQueryName();
			   Element mapElement = objJoinElement.addElement("MAP");
			   mapElement.addAttribute("qid", strQID);
			   mapElement.addAttribute("qname", strQName);
			   for(int j = 0 ; j < alQueryMapList.size() ; j++){
				   JoinMapVO tmpMapVO = (JoinMapVO) alQueryMapList.get(j);
				   if (strQID.equals(tmpMapVO.getDQueryID())){
					   Element itemElement = mapElement.addElement("ITEM");
					   itemElement.addElement("TID").setText(tmpMapVO.getSTableName());
					   itemElement.addElement("FID").setText(tmpMapVO.getSFieldName());
					   itemElement.addElement("TYPE").setText(tmpMapVO.getSFieldType());

					   itemElement.addElement("DIC").setText(tmpMapVO.getSFieldDic());
					   itemElement.addElement("FMT").setText(tmpMapVO.getSFieldFmt());
					   itemElement.addElement("RNAME").setText(tmpMapVO.getDRelationName());
//					   alQueryMapList.remove(j);
				   }
			   }
		   }

		   if ("".equals(strSeekImgField))
			   strSeekImgField = " 1 ";

		   Element objPartSeekElement =  objPartElement.addElement("SEEK");
		   for(int i = 0 ;i < alDataSeekFieldList.size();i++)
		   {
			   objFieldVO = (FieldVO) alDataSeekFieldList.get(i);
			   Element objRelElement = objPartSeekElement.addElement("ITEM");
			   objRelElement.addElement("TID").addText(objFieldVO.getTable());
			   objRelElement.addElement("FID").addText(objFieldVO.getField());
			   objRelElement.addElement("TYPE").addText(objFieldVO.getType());
			   objRelElement.addElement("DIC").addText(objFieldVO.getDic());
			   objRelElement.addElement("FMT").addText(objFieldVO.getFmt());

			   if (!"".equals(strSeekDataField))
				   strSeekDataField += "," + objFieldVO.getTable()+"."+objFieldVO.getField();
			   else
				   strSeekDataField = objFieldVO.getTable()+"."+objFieldVO.getField();
		   }


	       if ("1".equals(strSwitchFlag))
	    	   dataSQL =  "SELECT " + strSeekDataField + " FROM " + strTableList + whereSQL + strOrderList;
	       else if ("2".equals(strSwitchFlag))
	    	   dataSQL = " SELECT " + strSeekImgField + " FROM " + strTableList + whereSQL + strOrderList;
	       else
	    	   dataSQL =  "";

		   String strTemp = (String) p_Request.getParameter("p");
		   if (strTemp == null || "".equals(strTemp))
			   strTemp = "1";
		   intCurPage = Integer.parseInt(strTemp);
		   if (intCurPage == 0)
			   intCurPage = 1;

		   if (conn == null)
		       System.out.println("conn= null");
		   else

		   intCount = Integer.parseInt(DBUtil.getSignalValue(conn, countSQL));

		   intPages = intCount / intRowPerPage;
		   if (intCount % intRowPerPage > 0)
		     intPages++;

		   String pageSQL = getPageSQL(dataSQL,intRowPerPage,intCurPage);


		   p_Request.setAttribute("prows", String.valueOf(intRowPerPage));
		   p_Request.setAttribute("records", String.valueOf(intCount));
		   p_Request.setAttribute("pages", String.valueOf(intPages));
		   p_Request.setAttribute("pnum", String.valueOf(intCurPage));
		   com.ccthanking.framework.coreapp.freequery.FreeQuerySet objQS = new com.ccthanking.framework.coreapp.freequery.FreeQuerySet();

		   if ("1".equals(strSwitchFlag))
		   {
			   for (int i = 0; i < alDataSeekFieldList.size(); i++) {
					objFieldVO = (FieldVO) alDataSeekFieldList.get(i);
					objQS.addColumn(objFieldVO);
			   }

		   }
		   else if ("2".equals(strSwitchFlag))
		   {
			   for (int i = 0; i < alImageSeekFieldList.size(); i++) {
					objFieldVO = (FieldVO) alImageSeekFieldList.get(i);
					objQS.addColumn(objFieldVO);
			   }
		   }
		   else
		   {

		   }


		   objQS.execQuery(conn, pageSQL);

          //���ݸ�-SeekDisplayStyle%.jsp
		   p_Request.setAttribute("objset", objQS);

           String strImg = "SELECT " + strImgField + " FROM " + strImgTable;

    	   dataSQL = " SELECT " + strSeekImgField + " FROM " + strTableList + whereSQL + strOrderList;
		   objImageElement.addElement("TID").addText(strImgTable);
		   objImageElement.addElement("FID").addText(strImgField);
		   objImageElement.addElement("STAT").addText(countSQL);
		   objImageElement.addElement("DATA").addText(dataSQL);
		   objImageElement.addElement("IMG").addText(strImg);

		   dataSQL =  "SELECT " + strSeekDataField + " FROM " + strTableList + whereSQL + strOrderList;
		   objPartElement.addElement("STAT").addText(countSQL);
		   objPartElement.addElement("DATA").addText(dataSQL);

		   String strXML = objDocument.asXML();
		   sun.misc.BASE64Encoder baseEncode = new sun.misc.BASE64Encoder();
		   String strEncodeConf = baseEncode.encode(strXML.getBytes("GBK"));
		   strEncodeConf = strEncodeConf.replaceAll("\r\n", "");
		   p_Request.setAttribute("conf", strEncodeConf);
		   p_Request.setAttribute("show", strTemplate);

//		   Element objCustomElement = objRootElement.addElement("CUSTOM");
		   CustomColumn objCustomColumn = new CustomColumn();
		   ArrayList alTmpColumnList = objCustomColumn.getCustomColumnList(alDisplayFieldList,strQueryID,p_UserName);
		   String strCustomColumn = "";
		   for(int i = 0 ; i < alTmpColumnList.size(); i++ ){
			   String strTempNo = (String)alTmpColumnList.get(i);
//			   objCustomElement.addElement("ITEM").setText(strTempNo);
			   strCustomColumn += strTempNo+";";
		   }
		   p_Request.setAttribute("colset", strCustomColumn);

	} catch (NumberFormatException e) {
		throw e;
	}
	finally{
		mapUsedTables.clear();
		alDefineTableList.clear();
		alRelImageList.clear();
		alImageDispList.clear();
		alRelDetailList.clear();
		alDetailTableList.clear();
		alDisplayFieldList.clear();
		alQueryTableList.clear();
		alQueryMapList.clear();
		try {
			if (conn != null)
				conn.close();
	    } catch (SQLException e) {
			throw e;
	    }
	}

   }

   public void setDynamicValue(Document doc, Document doccond,
                                HttpServletRequest request)
       throws Exception
   {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       Element rootcond = doccond.getRootElement();
//       String strExpress = rootcond.elementText("EXP");

       //add by wuxp
//       String strHasZero = rootcond.element("EXP").attribute("HASZERO").getText();
       //add by wuxp
/*       
       Element nodeExpress = (Element)doc.selectSingleNode("//FILTERS/EXPRESSION");
       if (strExpress != null)
       {
           if(strHasZero.equals("false"))
           {
               String strExp = strExpress;
//               String strRowCondition="";
               StringBuffer tmpBuffer = new StringBuffer();
                char curChar;
                String strDigit = "";
                int intDigit ;
                for(int i = 0 ; i < strExp.length(); i++){
                    curChar = strExp.charAt(i);
                    if (Character.isDigit(curChar)){
                        strDigit += curChar;
                        continue;
                    }else{
                        if (!"".equals(strDigit)){
                            intDigit = Integer.parseInt(strDigit);
                            intDigit++;

                            tmpBuffer.append(String.valueOf(intDigit));
                        }
                        strDigit = "";
                        if (curChar == '*')
                            tmpBuffer.append(" * ");
                        else if (curChar == '+')
                            tmpBuffer.append(" + ");
                        else
                            tmpBuffer.append(curChar);
                    }
                }
                if (!"".equals(strDigit)){
                    intDigit = Integer.parseInt(strDigit);
                    intDigit++;
                    tmpBuffer.append(String.valueOf(intDigit));
                }
                strExpress = tmpBuffer.toString();
            }


           nodeExpress.setText(strExpress);
       }
       else
    	   nodeExpress.setText("");
*/    	   
       List cList = rootcond.elements("CONDITION");
       List rList = doc.selectNodes("//FILTERS/FILTER");
       for (int i = 0,j = 0; i < rList.size(); i++)
       {
           Element filter = (Element) rList.get(i);
           Element elCondition = filter.element("CONDITION");

           String strType = elCondition.attributeValue("type");

           if (FreeQueryProps.COND_DYNIMIC_2.equals(strType)){
           	 String strTemp = "";
           	 Element rowElement = filter.element("ROW");
           	 Element elSeekCond = (Element)cList.get(j);
//           	 filter.element("CODE").setText(elSeekCond.elementText("CODE"));
           	 strTemp = elSeekCond.elementText("VALUE");
           	 filter.element("VALUE").setText(strTemp);
           	 strTemp = elSeekCond.elementText("OPERATE");
           	 filter.element("OPERATE").setText(strTemp);
                //add by jiawh ����ѯ���������ֵ�����ģ���ѯ��CODEֵ����"%"-----start
                if(strTemp.equals("like") && elSeekCond.elementText("KIND").equals("dic") && !Pub.empty(elSeekCond.elementText("CODE")))
                {
                  filter.element("CODE").setText("%"+elSeekCond.elementText("CODE")+"%");
                }else{
                  filter.element("CODE").setText(elSeekCond.elementText("CODE"));
                }
                //add by jiawh ����ѯ���������ֵ�����ģ���ѯ��CODEֵ����"%"-----end
//           	 filter.element("OPERATE").setText(strTemp);
           	 strTemp = elSeekCond.elementText("STYLE");

           	 if (strTemp != null)
           	   filter.element("SETSTYLE").setText(strTemp);
           	 if (FreeQueryProps.SET_STYLE_SET_2.equals(strTemp)||FreeQueryProps.SET_STYLE_RANGE_8.equals(strTemp)){

           		 StringBuffer buffer = new StringBuffer();
           		 List tmpList = elSeekCond.elements("DATA");

           		 for(int k = 0; k < tmpList.size(); k++){
           			 Element tmpElement = (Element) tmpList.get(k);
           			 buffer.append( tmpElement.asXML());
           		 }
           		 filter.element("CODE").setText(buffer.toString());
           		 filter.element("VALUE").setText(buffer.toString());

           	 }

           	 strTemp = elSeekCond.elementText("FMT");
           	 rowElement.element("FMT").setText(strTemp);

           	 j++;
           }
           else{
           	String strFixed = elCondition.attributeValue("fixed");

               if (FreeQueryProps.FIXED_YHZH_1.equals(strFixed)){
               	filter.element("VALUE").setText(user.getAccount());
               }
               else if (FreeQueryProps.FIXED_YHBM_2.equals(strFixed)){
               	filter.element("VALUE").setText(user.getDepartment());
                   filter.element("CODE").setText(user.getDepartment());
               }
               else if (FreeQueryProps.FIXED_XTSJ_3.equals(strFixed)){
//                 Element elFormat = filter.element("FORMAT");
//               	String strTarget = elFormat.attributeValue("target");
//               	String strFormatType = elFormat.attributeValue("type");
               	String strDateFormat;

//               	if ("1".equals(strTarget) && "3".equals(strFormatType)){
//               		strDateFormat = elFormat.elementText("MODE");
//               		strDateFormat = strDateFormat.replaceAll("YYYY", "yyyy");
//               		strDateFormat = strDateFormat.replaceAll("DD", "dd");
//               		strDateFormat = strDateFormat.replaceAll("HH24", "hh");
//               		strDateFormat = strDateFormat.replaceAll("MI", "mm");
//               		strDateFormat = strDateFormat.replaceAll("SS", "ss");
//               	}
//               	else
               		strDateFormat = "yyyyMMddhhmmss";
               	Element rowElement = filter.element("ROW");
               	rowElement.element("FMT").setText("YYYYMMDDHH24MISS");
               	filter.element("CODE").setText(Pub.getDate(strDateFormat));
                   filter.element("VALUE").setText(Pub.getDate(strDateFormat));
               }
               else if (FreeQueryProps.FIXED_MJJB_4.equals(strFixed)){
               	filter.element("VALUE").setText(user.getScretLevel());
                   filter.element("CODE").setText(user.getScretLevel());
               }
           }

       }
   }

   public void setFixedValue(Document doc, HttpServletRequest request)
       throws Exception
   {
       User user = (User) request.getSession().getAttribute(Globals.
           USER_KEY);
       Element root = doc.getRootElement();
       List rList = root.selectNodes("//FILTERS/FILTER");
       for (int i = 0; i < rList.size(); i++)
       {

       	Element filter = (Element) rList.get(i);
           Element elCondition = filter.element("CONDITION");
           String strFixed = elCondition.attributeValue("fixed");

           if (FreeQueryProps.FIXED_YHZH_1.equals(strFixed)){
           	filter.element("VALUE").setText(user.getAccount());
           }
           else if (FreeQueryProps.FIXED_YHBM_2.equals(strFixed)){
           	filter.element("VALUE").setText(user.getDepartment());
               filter.element("CODE").setText(user.getDepartment());
           }
           else if (FreeQueryProps.FIXED_XTSJ_3.equals(strFixed)){
//             Element elFormat = filter.element("FORMAT");
//           	String strTarget = elFormat.attributeValue("target");
//           	String strFormatType = elFormat.attributeValue("type");
           	String strDateFormat;

//           	if ("1".equals(strTarget) && "3".equals(strFormatType)){
//           		strDateFormat = elFormat.elementText("MODE");
//           		strDateFormat = strDateFormat.replaceAll("YYYY", "yyyy");
//           		strDateFormat = strDateFormat.replaceAll("DD", "dd");
//           		strDateFormat = strDateFormat.replaceAll("HH24", "hh");
//           		strDateFormat = strDateFormat.replaceAll("MI", "mm");
//           		strDateFormat = strDateFormat.replaceAll("SS", "ss");
//           	}
//           	else
           		strDateFormat = "yyyyMMddhhmmss";
           	filter.element("CODE").setText(Pub.getDate(strDateFormat));
               filter.element("VALUE").setText(Pub.getDate(strDateFormat));
           }
           else if (FreeQueryProps.FIXED_MJJB_4.equals(strFixed)){
           	filter.element("VALUE").setText(user.getScretLevel());
               filter.element("CODE").setText(user.getScretLevel());
           }

       }
   }

   public ArrayList parseDynamicCondition(Document doc, User user)
   {
       try
       {
           ArrayList res = null;
           List rList = doc.selectNodes("//FILTERS/FILTER");
           for (int i = 0; i < rList.size(); i++)
           {
               Element filter = (Element) rList.get(i);
               Element elRow = filter.element("ROW");
               if (elRow == null)
               	continue;

//               Element fieldFormat = filter.element("FIELDFORMAT");
               Element elFieldKind = filter.element("FIELDKIND");
               Element elFieldMemo = filter.element("FIELDMEMO");
               Element elFieldMust = filter.element("FIELDMUST");
               Element elCondition = filter.element("CONDITION");
               String strFixed = elCondition.attributeValue("fixed");
               Element dateFmtNode = filter.element("FORMAT");
               String strTarget = dateFmtNode.attributeValue("target");
               String strDateFmt = "";
               String strFmtMode = "";
               if (!FreeQueryProps.FMT_TARGER_NULL_0.equals(strTarget)){
            	   strDateFmt = dateFmtNode.attributeValue("type");
            	   if (FreeQueryProps.FMT_TYPE_DATE_2.equals(strDateFmt))
            		   strDateFmt = dateFmtNode.elementText("MODE");
                   else if (FreeQueryProps.FMT_TYPE_DIC_7.equals(strDateFmt))//�ֵ����͸�ʽ�� add by jiawh �����ֵ��������
                   {
                      strFmtMode = dateFmtNode.elementText("MODE") == null ? "" : dateFmtNode.elementText("MODE");
                   }
            	   else
            		   strDateFmt = "";
               }

               String strMust = "false";

               if (elFieldMust != null){
            	   if ("1".equals(elFieldMust.getText()))
            		   strMust = "true";
               }

               String code = "";
               String value = "";
               if (FreeQueryProps.FIXED_WSZ_0.equals(strFixed)){
               	code = filter.element("CODE") == null ? "" : filter.elementText("CODE");
               	value = filter.elementText("VALUE");
               }
               else if (FreeQueryProps.FIXED_YHZH_1.equals(strFixed)){
//               	value = user.getAccount();
               	  value = user.getName();
//               	value = "�����û�";
               }
               else if (FreeQueryProps.FIXED_YHBM_2.equals(strFixed)){
                   code = user.getDepartment();
               	   value = user.getOrgDept().getDeptName();
//               	code = "1";
//               	value = "���Բ���";
               }
               else if (FreeQueryProps.FIXED_XTSJ_3.equals(strFixed)){
               	  String strDateFormat;
               	  if (elFieldMemo != null){
               		  strDateFormat = elFieldMemo.getText();
					  if (!"".equals(strDateFormat)) {
							strDateFormat = strDateFormat.replaceAll("YYYY","yyyy");
							strDateFormat = strDateFormat.replaceAll("DD", "dd");
							strDateFormat = strDateFormat.replaceAll("HH24","hh");
							strDateFormat = strDateFormat.replaceAll("MI", "mm");
							strDateFormat = strDateFormat.replaceAll("SS", "ss");
					  }
					  else
							strDateFormat = "yyyyMMdd";
               	  }
               	  else
               		  strDateFormat = "yyyyMMdd";

                   code = Pub.getDate(strDateFormat);
               	value = Pub.getDate(strDateFormat);
               }
               else if (FreeQueryProps.FIXED_MJJB_4.equals(strFixed)){
                  code = user.getScretLevel() ;
               	  value = "�ܼ�" + user.getScretLevel();
//               	code = "1";
//               	value = "�ܼ�1";
               }


//               String format = filter.elementText("FORMAT");
               String cname = filter.element("ROW").element("OWNER").getText()
                            + "." + filter.element("ROW").element("TNAME").getText()
                            + "." + filter.element("ROW").element("FNAME").getText();
//               String dicfile = filter.element("ROW").element("DIC_FILE").getText();
//               String strFixed = elCondition.attributeValue("fixed");
               String strTypeValue = elCondition.attributeValue("type");


               if (FreeQueryProps.COND_DYNIMIC_2.equals(strTypeValue))
               {
            	   String strType ;
            	   String strMemo ;
            	   String strKind = "text";
                   String strClassName = "Edit";
              	   String dateFmtMode = "";

            	   Hashtable tab = new Hashtable();
                   tab.put("fname", cname);
                   tab.put("condindex", "" + i);
                   tab.put("fnote",elRow.elementText("FNOTE"));

            	   if (elFieldKind != null){

            		   strType = elFieldKind.getText();
            		   strMemo = elFieldMemo.getText();
                       tab.put("ftype", strType);
                       tab.put("fmemo", strMemo);
                       if (FreeQueryProps.FIELD_KIND_CHAR_1.equals(strType)){
                    	   strKind = "text";
                       }
                       else if (FreeQueryProps.FIELD_KIND_DIC_6.equals(strType)){
                    	   strKind = "dic";
                       }
                       else if (FreeQueryProps.FIELD_KIND_CARD_7.equals(strType)){
                    	   strKind = "card";
                       }
                       else{
                    	   strKind = "text";
                       }
            	   }
            	   else{

            		   strType = elRow.elementText("STYLE");
            		   strMemo = elRow.elementText("DIC_FILE");

                       if (FreeQueryProps.FIELD_TYPE_CHAR_1.equals(strType)){
                    	   strKind = "text";
                    	   strType = FreeQueryProps.FIELD_KIND_CHAR_1;
                       }
                       else if (FreeQueryProps.FIELD_TYPE_IDCARD_2.equals(strType)){
                    	   strKind = "card";
                    	   strType = FreeQueryProps.FIELD_KIND_CARD_7;
                       }
                       else if (FreeQueryProps.FIELD_TYPE_DIC_3.equals(strType)){
                    	   strKind = "dic";
                    	   strType = FreeQueryProps.FIELD_KIND_DIC_6;
                       }
                       else if (FreeQueryProps.FIELD_TYPE_DATE_4.equals(strType)){
                    	   strKind = "text";
                    	   strMemo = "YYYYMMDD";
                       }
                       tab.put("fmemo", strMemo);
                       tab.put("ftype", strType);
            	   }
            	   tab.put("fclass", strClassName);
            	   tab.put("fkind", strKind);
                   tab.put("fmtmode",strFmtMode);

                   List list = filter.elements("LOGIC");
                   String dlogic = "";

                   String defaultLogic = null;
                   for (int j = 0; j < list.size(); j++)
                   {
                       Element dylogic = (Element) list.get(j);
                       dlogic += "<option value='" +
                           convertString(dylogic.attributeValue("op")) + "'>"
//                           dylogic.attributeValue("op") + "'>"
                           + dylogic.getText() + "</option>\n";
                       if (j == 0)
                           defaultLogic = convertString(dylogic.attributeValue("op"));
//                       	defaultLogic = dylogic.attributeValue("op");
                   }

                   String dyStyle = "";
                   String unionStyle = "";
                   String extentStyle = "";
                   List styleList = filter.elements("STYLE");
                   for (int j = 0; j < styleList.size();j++)
                   {
                   	Element elStyle = (Element)styleList.get(j);

                   	String strStyle = elStyle.getText();
//                   	if ("1".equals(strStyle))
//                   		dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" + i + "' flag=false onclick=\"closeSet("
//                   		        + i + ",this)\">ģ��&nbsp;";
                   	 if (FreeQueryProps.SET_STYLE_SET_2.equals(strStyle)){
                   		dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" +i+"' onclick=\"displaySet("
                   		        + i + ",this)\">��ֵ&nbsp;";
                   		unionStyle = "<select id='selectstyle_" + i + "' style='display:none'>"
                   		         + "</select>&nbsp;"
                   		         + "<input type='button' value='����' id='addbutton_" + i + "' style='display:none' onclick=\"addClause("
                   		         + i + ")\">&nbsp;"
                   		         + "<input type='button' value='ɾ��' id='delbutton_" + i + "' style='display:none' onclick=\"delClause("
                		         + i + ")\">" ;
                   	}
//                   	else if ("3".equals(strStyle))
//                   		dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" + i + "' flag=false onclick=\"closeSet("
//           		        + i + ",this)\">����&nbsp;";
//                   	else
                    else if (FreeQueryProps.SET_STYLE_TYJ_4.equals(strStyle))
                   		dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" +i+"' flag=false  onclick=\"closeSet("
           		        + i + ",this)\">ͬ��(��)&nbsp;";
                   	else if (FreeQueryProps.SET_STYLE_TYQ_5.equals(strStyle))
                   		dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" +i+"' flag=false  onclick=\"closeSet("
           		        + i + ",this)\">ͬ��(ȫ)&nbsp;";
//                   	else if ("6".equals(strStyle))
//                   		dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" +i+"' flag=false  onclick=\"closeSet("
//           		        + i + ",this)\">���򸡶�&nbsp;";
//                   	else if ("7".equals(strStyle))
//                   		dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" +i+"' flag=false  onclick=\"closeSet("
//           		        + i + ",this)\">���򸡶�&nbsp;";
                   	else if (FreeQueryProps.SET_STYLE_RANGE_8.equals(strStyle)){
                   		dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" +i+"' onclick=\"displayExtent("
           		                + i + ",this)\">���&nbsp;";
                   		
                   		/*	add by liu.ld start 20090305	����dateFmtMode��صĲ���	*/
                   		if("YYYYMMDD".equals(strMemo))
                   		{
                   			dateFmtMode = "YYYYMMDD";
	                        if("YYYYMMDDHH24MISS".equals(strDateFmt))
	                        	dateFmtMode = "YYYYMMDDHHMISS";
                   		}
                   		/*	add by liu.ld end 20090305	*/
                   		extentStyle =  "<label style='display:none'>��&nbsp;&nbsp;"
	      		                    + "<input type='text' class=\"" + strClassName + "\"  id='extentcond_" + i + "' "
	      		                    + " kind=\"" + strKind + "\""
	      		                    + " fieldname=\"" + cname + "\""
	      		                    + " fieldkind='" + strType + "' "
	      		                    + " datefmtmode='" + dateFmtMode + "' "
	      		                    + " fieldmem='" + strMemo + "' "
	      		                    + " showInput='false'>&nbsp;</label>";
                   	}
                    else if (FreeQueryProps.SET_STYLE_TREEDIC_11.equals(strStyle))
                    {
                   		tab.put("treedic","true");
                   		tab.put("filter", elStyle.attributeValue("filter")==null?"":elStyle.attributeValue("filter"));
                    }
                    else if (FreeQueryProps.SET_STYLE_TABSEL_12.equals(strStyle))
                    {
                   		tab.put("tabsel","true");
                   		tab.put("code", elStyle.attributeValue("code")==null?"":elStyle.attributeValue("code"));
                    }
                    else if (FreeQueryProps.SET_STYLE_1518ID_13.equals(strStyle))
                    {
                    	dyStyle += "<input type='radio' value='"+strStyle+"' name='chkstyle_" +i+"' onclick=\"closeSet("
           		        + i + ",this)\">����15λ��18λ����&nbsp;";
                    }
//               	    String strDicInfo = "";
//               	    if ("6".equals(strKindCode))
//               	    	strDicInfo = "src=\"" + strMemo + "\"";
//               	    String strDateInfo = "";
//               	    if ("4".equals(strKindCode))
//               	    	strDateInfo = "fieldformat=\""+strSetFormat + "\" hasbtn='true' maxlength='8' check='false' callFunction=\"riliShowDate('extentcond_" + i +"')\"";

//                   	extentStyle = "<select id='logicstyle_" + i + "' style='display:none'>"
//			                   	 + "<option value=' AND '>����</option>\n"
//			      		         + "<option value=' OR '>����</option>\n"
//			      		         + "</select>&nbsp;"
//			      		         + "<select id='operatestyle_" + i + "' style='display:none'>"
//			      		         + "<option value='&gt;'>����</option>\n"
//			      		         + "<option value='&lt;'>С��</option>\n"
//			      		         + "<option value='&gt;='>���ڵ���</option>\n"
//			      		         + "<option value='&lt;='>С�ڵ���</option>\n"
//			      		         + "</select>&nbsp;"
//			      		         + "<label style='display:none'>"
//			      		         + "<input type='text' class=\"" + strClassName + "\"  id='extentcond_" + i + "' "
//			      		         + " kind=\"" + strKindValue + "\""
//			      		         + " fieldname=\"" + cname + "\""
//			      		         + " " + strDicInfo + " "
//			      		         + " " + strDateInfo + " "

//			      		         + " showInput='false'>&nbsp;</label>";

                   }

                   dyStyle = extentStyle + dyStyle + unionStyle;

                   tab.put("dynamicstyle", dyStyle);
                   tab.put("dynamiclogic", dlogic);
                   tab.put("defaultLogic", defaultLogic);
                   tab.put("defaultvalue", value);
                   tab.put("defaultcode", code);
                   tab.put("fmust", strMust);
                   tab.put("fmttype", strDateFmt);
                   tab.put("datefmtmode", dateFmtMode);



                   if (res == null)
                       res = new ArrayList();
                   res.add(tab);
               }
           }
           return res;
       }
       catch (Exception e)
       {
           e.printStackTrace();
           return null;
       }
   }

   public boolean hasDynamicCondition(Document doc)
       throws Exception
   {
   	boolean hasDynamic = false;
       List rList = doc.selectNodes("//FILTERS/FILTER");
       for (int i = 0; i < rList.size(); i++)
       {
           Element filter = (Element) rList.get(i);
           Element elCondition = filter.element("CONDITION");
           if (elCondition != null){
           	String strType = elCondition.attributeValue("type");
           	if (FreeQueryProps.COND_DYNIMIC_2.equals(strType)){
           		hasDynamic = true;
           		break;
           	}
           }
       }
       return hasDynamic;
   }

   public boolean hasFixedCondition(Document doc)
       throws Exception
   {
       List rList = doc.selectNodes("//FILTERS/FILTER");
       boolean hasFix = false;
       for (int i = 0; i < rList.size(); i++)
       {
           Element filter = (Element) rList.get(i);
           Element elCondition = filter.element("CONDITION");
           if (elCondition != null){
           	String strFixed = elCondition.attributeValue("fixed");
           	if (!FreeQueryProps.FIXED_YHBM_2.equals(strFixed)){
           		hasFix = true;
           		break;
           	}
           }
       }
       return hasFix;
   }

}
