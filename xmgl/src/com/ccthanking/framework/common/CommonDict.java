package com.ccthanking.framework.common;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;
/**
 * @auther xhb 
 */
public class CommonDict extends BaseVO {
	
	
	public CommonDict(){
		this.addField("DICT_ID",OP_STRING|this.TP_PK);
		this.addField("DICT_NAME",OP_STRING);
		this.addField("DICT_CATEGORY",OP_STRING);
		this.addField("UPDATEPERSON",OP_STRING);
		this.addField("UPDATEDATE",OP_DATE);
		this.addField("PXH",OP_STRING);
		
		this.setFieldDateFormat("UPDATEDATE","yyyy-MM-dd");
		
		this.setVOTableName("FS_COMMON_DICT");
	}

	public void setDict_id(String dict_id){
		this.setInternal("DICT_ID",dict_id);
	}
	public String getDict_id(){
		return (String)this.getInternal("DICT_ID");
	}
	public void setDict_name(String dict_name){
		this.setInternal("DICT_NAME",dict_name);
	}
	public String getDict_name(){
		return (String)this.getInternal("DICT_NAME");
	}
	public void setDict_category(String dict_category){
		this.setInternal("DICT_CATEGORY",dict_category);
	}
	public String getDict_category(){
		return (String)this.getInternal("DICT_CATEGORY");
	}
	public void setUpdateperson(String updateperson){
		this.setInternal("UPDATEPERSON",updateperson);
	}
	public String getUpdateperson(){
		return (String)this.getInternal("UPDATEPERSON");
	}
	public void setUpdatedate(Date updatedate){
		this.setInternal("UPDATEDATE",updatedate);
	}
	public Date getUpdatedate(){
		return (Date)this.getInternal("UPDATEDATE");
	}
	public void setPxh(String pxh){
		this.setInternal("PXH",pxh);
	}
	public String getPxh(){
		return (String)this.getInternal("PXH");
	}
	
}
