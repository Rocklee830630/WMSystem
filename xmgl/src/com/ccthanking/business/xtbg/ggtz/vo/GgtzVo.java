package com.ccthanking.business.xtbg.ggtz.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class GgtzVo extends BaseVO {
	
	public GgtzVo() {
		this.addField("GGID",OP_STRING|this.TP_PK);
		this.addField("GGBT",OP_STRING);
		this.addField("GGLB",OP_STRING);
		this.addField("FBFW",OP_STRING);
		this.addField("NR",OP_CLOB);
		this.addField("FBR",OP_STRING);
		this.addField("YDCS",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("FBFWMC", OP_STRING);
		this.addField("IS_TO_PERSON",OP_STRING);
		

		this.addField("SHZT",OP_STRING);
		this.addField("FBSJ",OP_DATE);
		
		this.bindFieldToDic("GGLB", "GGLB");
		this.bindFieldToOrgDept("LRBM");
		
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		this.setVOTableName("XTBG_XXZX_GGTZ");

	}
	
	public void setGgid(String ggid){
		this.setInternal("GGID",ggid);
	}
	public String getGgid(){
		return (String)this.getInternal("GGID");
	}
	public void setGgbt(String ggbt){
		this.setInternal("GGBT",ggbt);
	}
	public String getGgbt(){
		return (String)this.getInternal("GGBT");
	}
	public void setGglb(String gglb){
		this.setInternal("GGLB",gglb);
	}
	public String getGglb(){
		return (String)this.getInternal("GGLB");
	}
	public void setFbfw(String fbfw){
		this.setInternal("FBFW",fbfw);
	}
	public String getFbfw(){
		return (String)this.getInternal("FBFW");
	}
	public void setNr(Byte[] nr){
		this.setInternal("NR",nr);
	}
	public String getNr(){
		return (String)this.getInternal("NR");
	}
	public void setFbr(String fbr){
		this.setInternal("FBR",fbr);
	}
	public String getFbr(){
		return (String)this.getInternal("FBR");
	}
	public void setYdcs(String ydcs){
		this.setInternal("YDCS",ydcs);
	}
	public String getYdcs(){
		return (String)this.getInternal("YDCS");
	}
	public void setYwlx(String ywlx){
		this.setInternal("YWLX",ywlx);
	}
	public String getYwlx(){
		return (String)this.getInternal("YWLX");
	}
	public void setSjbh(String sjbh){
		this.setInternal("SJBH",sjbh);
	}
	public String getSjbh(){
		return (String)this.getInternal("SJBH");
	}
	public void setBz(String bz){
		this.setInternal("BZ",bz);
	}
	public String getBz(){
		return (String)this.getInternal("BZ");
	}
	public void setLrr(String lrr){
		this.setInternal("LRR",lrr);
	}
	public String getLrr(){
		return (String)this.getInternal("LRR");
	}
	public void setLrsj(Date lrsj){
		this.setInternal("LRSJ",lrsj);
	}
	public Date getLrsj(){
		return (Date)this.getInternal("LRSJ");
	}
	public void setLrbm(String lrbm){
		this.setInternal("LRBM",lrbm);
	}
	public String getLrbm(){
		return (String)this.getInternal("LRBM");
	}
	public void setLrbmmc(String lrbmmc){
		this.setInternal("LRBMMC",lrbmmc);
	}
	public String getLrbmmc(){
		return (String)this.getInternal("LRBMMC");
	}
	public void setGxr(String gxr){
		this.setInternal("GXR",gxr);
	}
	public String getGxr(){
		return (String)this.getInternal("GXR");
	}
	public void setGxsj(Date gxsj){
		this.setInternal("GXSJ",gxsj);
	}
	public Date getGxsj(){
		return (Date)this.getInternal("GXSJ");
	}
	public void setGxbm(String gxbm){
		this.setInternal("GXBM",gxbm);
	}
	public String getGxbm(){
		return (String)this.getInternal("GXBM");
	}
	public void setGxbmmc(String gxbmmc){
		this.setInternal("GXBMMC",gxbmmc);
	}
	public String getGxbmmc(){
		return (String)this.getInternal("GXBMMC");
	}
	public void setSjmj(String sjmj){
		this.setInternal("SJMJ",sjmj);
	}
	public String getSjmj(){
		return (String)this.getInternal("SJMJ");
	}
	public void setSfyx(String sfyx){
		this.setInternal("SFYX",sfyx);
	}
	public String getSfyx(){
		return (String)this.getInternal("SFYX");
	}
	

	public void setShzt(String shzt){
		this.setInternal("SHZT",shzt);
	}
	public String getShzt(){
		return (String)this.getInternal("SHZT");
	}
	
	public void setFbsj(Date fbsj){
		this.setInternal("FBSJ",fbsj);
	}
	public Date getFbsj(){
		return (Date)this.getInternal("FBSJ");
	}

	public void setFbfwmc(String fbfwmc){
		this.setInternal("FBFWMC",fbfwmc);
	}
	public String getFbfwmc(){
		return (String)this.getInternal("FBFWMC");
	}

	public void setIsToPerson(String isToPerson){
		this.setInternal("IS_TO_PERSON",isToPerson);
	}
	public String getIsToPerson(){
		return (String)this.getInternal("IS_TO_PERSON");
	}
}
