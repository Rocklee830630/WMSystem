//v20071212-1
package com.ccthanking.framework.coreapp.freequery;

/**
 * create time : 2007.05.17
 * Description : parse config information
 * Note : you are free to copy,modify,rewrite this code,but please you reserve author's name.
 * @author michael wang
 * @version 1.0
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import sun.misc.BASE64Decoder;

public class BasePageConfig {

	private Document objDoc;
	protected Element objRootElement;
	
	//getPartDispList() get
	protected ArrayList alPartDispList;

	//getDetailRelList() get 
    protected ArrayList alDetailRelList;
	
	//getSetInfo() get
	protected String strPrint ;
	protected String strDownLoad ;
	protected String strTemplate ;
	protected String strPicSet ;
	protected String strTableTitle ;   
	protected String strTitleAlign ;
	protected String strTitleColor ;
	protected String strFontSize ;
	protected String strTitleBGColor ;
	protected String strCrossColor ;
	protected String strRowColor1 ;
	protected String strRowColor2;
	protected String strQueryID;
	
	//getPartSeekList()
	protected ArrayList alPartSeekList;
	protected String strPCountSQL;
    protected String strPDataSQL;
	
	//getImgSeekList()
	protected ArrayList alImgSeekList;
	
	//getImgDispList()
	protected ArrayList alImgDispList;
    protected String strICountSQL;
    protected String strIDataSQL;
    
    protected int intCurrentPage = 0;
    protected int intRows = 0;
    
	
	public BasePageConfig() {
		
		objDoc = null;
		objRootElement = null;
		alPartDispList = null;
		alDetailRelList = null;
		alPartSeekList = null;
		alImgDispList = null;
		alImgSeekList = null;
		
		strPrint = "";       
		strDownLoad = "";    
		strTemplate = "";    
		strPicSet = "";      
		strTableTitle = "";  
		strTitleAlign = "";  
		strTitleColor = "";  
		strFontSize = "";    
		strTitleBGColor = "";
		strCrossColor = "";  
		strRowColor1 = "";   
		strRowColor2 = ""; 
		strQueryID = "";

	}
	
	public void init(String p_Conf) throws IOException,DocumentException {
		BASE64Decoder objBase64 = new BASE64Decoder();
		byte arrConf[] = objBase64.decodeBuffer(p_Conf);
		String strDeConf = new String(arrConf,"GBK");
		//out.println(strDeConf);
		objDoc = DocumentHelper.parseText(strDeConf);
		objRootElement = objDoc.getRootElement();
	}
	
	public void free(){
		objDoc = null;
		objRootElement = null;
		alPartDispList = null;
		alDetailRelList = null;
		alPartSeekList = null;
		alImgDispList = null;
		alImgSeekList = null;
	}
	
	public ArrayList getPartDispList(){
		
		if (alPartDispList == null){
			Element objPartElement = objRootElement.element("PART");
			alPartDispList = new ArrayList();
			List objPartDispList = objPartElement.elements("ITEM");
			
			for (int i = 0; i < objPartDispList.size(); i++) {
				Element tmpElement = (Element) objPartDispList.get(i);
				DisplayVO objDispVO = new DisplayVO();
			
				String strTable;
				String strField;
				String strName;
				String strType;
				String strDic;
				String strWidth;
				String strHeight;
				String strLength;
			
				strTable = tmpElement.elementText("TID");
				strField = tmpElement.elementText("FID");
				strName = tmpElement.elementText("NAME");
				strType = tmpElement.elementText("TYPE");
				strDic = tmpElement.elementText("DIC");
				strWidth = tmpElement.elementText("W");
				strHeight = tmpElement.elementText("H"); 
				strLength = tmpElement.elementText("L");
				
				objDispVO.setTable(strTable);
				objDispVO.setField(strField);
				objDispVO.setName(strName);
				objDispVO.setType(strType);
				objDispVO.setDic(strDic);
				objDispVO.setWidth(strWidth);
				objDispVO.setHeight(strHeight);
				objDispVO.setLength(strLength);
			
				alPartDispList.add(objDispVO);
			
			}
			
		}
		
		return alPartDispList;
	}
	
	public void clearPartDispList(){
		alPartDispList = null;
	}
	
	public ArrayList getPartSeekList(){
		
		if (alPartSeekList == null){
			Element objPartElement = objRootElement.element("PART");
			strPCountSQL = objPartElement.element("STAT").getText();
			strPDataSQL = objPartElement.element("DATA").getText();
			Element objSeekElement = objPartElement.element("SEEK");
			alPartSeekList = new ArrayList();
			List objSeekList = objSeekElement.elements("ITEM");
			
			for (int i = 0; i < objSeekList.size(); i++) {
				Element tmpElement = (Element) objSeekList.get(i);
				FieldVO objFieldVO = new FieldVO();
			
				String strTable;
				String strField;
				String strType;
				String strDic;
				String strFmt;
				
				strTable = tmpElement.elementText("TID");
				strField = tmpElement.elementText("FID");
				strType = tmpElement.elementText("TYPE");
				strDic = tmpElement.elementText("DIC");
				strFmt = tmpElement.elementText("FMT");
				
				objFieldVO.setTable(strTable);
				objFieldVO.setField(strField);
				objFieldVO.setType(strType);
				objFieldVO.setDic(strDic);
				objFieldVO.setFmt(strFmt);
			
				alPartSeekList.add(objFieldVO);
			
			}
		}
		return alPartSeekList;
	}
	
	public void clearPartSeekList(){
		alPartSeekList = null;
	}
    
	public ArrayList getImgSeekList(){
		
		if (alImgSeekList == null){
			Element objPartElement = objRootElement.element("IMAGE");
			strICountSQL = objPartElement.element("STAT").getText();
			strIDataSQL = objPartElement.element("DATA").getText();
			Element objSeekElement = objPartElement.element("SEEK");
			alImgSeekList = new ArrayList();
			List objSeekList = objSeekElement.elements("ITEM");
			
			for (int i = 0; i < objSeekList.size(); i++) {
				Element tmpElement = (Element) objSeekList.get(i);
				FieldVO objFieldVO = new FieldVO();
			
				String strTable;
				String strField;
				String strType;
				String strDic;
				String strFmt;
				
				strTable = tmpElement.elementText("TID");
				strField = tmpElement.elementText("FID");
				strType = tmpElement.elementText("TYPE");
				strDic = tmpElement.elementText("DIC");
				strFmt = tmpElement.elementText("FMT");
				
				objFieldVO.setTable(strTable);
				objFieldVO.setField(strField);
				objFieldVO.setType(strType);
				objFieldVO.setDic(strDic);
				objFieldVO.setFmt(strFmt);
			
				alImgSeekList.add(objFieldVO);
			
			}
		}
		return alImgSeekList;
	}
	
	public void clearImgSeekList(){
		alImgSeekList = null;
	}
    
	public ArrayList getDetailRelList(){
		
		if (alDetailRelList == null)
		{
			alDetailRelList = new ArrayList();
			
			Element objDetailElement = objRootElement.element("DETAIL");
			
			List objTableList = objDetailElement.elements("TABLE");
			for(int i = 0; i < objTableList.size(); i++){
				Element tmpTableElement = (Element) objTableList.get(i);
				List objRelList = tmpTableElement.elements("REL");
				for(int j = 0; j < objRelList.size(); j++)
				{
					Element tmpRelElement = (Element) objRelList.get(j);
			
			    	String strDestField;
			    	
			    	strDestField = tmpRelElement.elementText("RFID");
			    	
			    	boolean bFlag = true;
			    	for(int k = 0; k < alDetailRelList.size();k++){
			    		FieldVO objTmpVO = (FieldVO) alDetailRelList.get(k);
			    		if (strDestField.equals(objTmpVO.getField())){
			    			bFlag = false;
			    			break;
			    		}
			    	}
			    	if (bFlag){
			    		String strDestTable;
						String strType;
			    		FieldVO objFieldVO = new FieldVO();
			        	strType = tmpRelElement.elementText("TYPE");
			        	strDestTable = tmpRelElement.elementText("RTID");
			        	
			        	objFieldVO.setTable(strDestTable);
			        	objFieldVO.setField(strDestField);
			        	objFieldVO.setType(strType);
			    		alDetailRelList.add(objFieldVO);
			    	}
				}
			}
		}
		return alDetailRelList;
	}
	
	public void clearDetailRelList(){
		alDetailRelList = null;
	}
	
	public void getSetInfo(){
		Element objSetElement = objRootElement.element("SET");
		strPrint = objSetElement.elementText("PRINT");
		strDownLoad = objSetElement.elementText("DOWN");
		strTemplate = objSetElement.elementText("TEMPLATE");
		strPicSet = objSetElement.elementText("PICSET");
		strTableTitle = objSetElement.elementText("TITLE");   
		strTitleAlign = objSetElement.elementText("TALIGN");
		strTitleColor = objSetElement.elementText("TCOLOR");
		strFontSize = objSetElement.elementText("TFSIZE");
		strTitleBGColor = objSetElement.elementText("TBGCOLOR");
		strCrossColor = objSetElement.elementText("CROSS");
		strRowColor1 = objSetElement.elementText("RCOLOR1");
		if ("1".equals(strCrossColor))
		    strRowColor2 = objSetElement.elementText("RCOLOR2");
		else
			strRowColor2 = strRowColor1;
		strQueryID = objSetElement.elementText("QID");
		
	}

	public String getPrintSet(){
		return strPrint;
	}
	
	public String getDownLoadSet(){
		return strDownLoad;
	}
	
	public String getTemplateSet(){
		return strTemplate;
	}
	
	public String getPicSet(){
		return strPicSet;
	}
	
	public String getTableTitle(){
		return strTableTitle;
	}
	
	public String getTitleAlign(){
		return strTitleAlign;
	}
	
	public String getCrossColor() {
		return strCrossColor;
	}

	public String getRowColor1() {
		return strRowColor1;
	}

	public String getRowColor2() {
		return strRowColor2;
	}

	public String getTitleBGColor() {
		return strTitleBGColor;
	}

	public String getTitleColor() {
		return strTitleColor;
	}

	public String getFontSize(){
		return strFontSize;
	}

	public String getPCountSQL(){
		return strICountSQL;
	}
	
	public String getPDataSQL(){
		return strPDataSQL;
	}
	
	public String getICountSQL(){
		return strICountSQL;
	}
	
	public String getIDataSQL(){
		return strIDataSQL;
	}
	
	public String getQueryID(){
		return strQueryID;
	}
	
	
	/*
	String strPRows = (String) request.getAttribute("prows");
	String strPages = (String) request.getAttribute("pages");
	String strCurrent = (String) request.getAttribute("pnum");
	String strCount = (String) request.getAttribute("records");
	
	
	int intPRows = Integer.parseInt(strPRows);
	int intCount = Integer.parseInt(strCount);
	int intPages = Integer.parseInt(strPages);
	int intCurrent = Integer.parseInt(strCurrent);
	
	//   intPRows = 30;
	
	FreeQuerySet qs = (FreeQuerySet) request.getAttribute("objset");
	
	int pageRowCount;
	pageRowCount = qs.getRows();
	int columnCount = objPartDispList.size();
	
	if (alDetailRelList.size() > 0 )
		columnCount++;
	*/
}
