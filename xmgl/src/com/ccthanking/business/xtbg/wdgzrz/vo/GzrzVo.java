package com.ccthanking.business.xtbg.wdgzrz.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class GzrzVo extends BaseVO {
	
	public GzrzVo() {
		this.addField("DIARYID",OP_STRING|this.TP_PK);
		this.addField("USERID",OP_STRING);
		this.addField("FBSJ",OP_DATE);
		this.addField("NR",OP_CLOB);
		this.addField("RZLB",OP_STRING);
		this.addField("JLR",OP_STRING);
		this.addField("JLSJ",OP_DATE);
		this.addField("ZHXGSJ",OP_DATE);
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

		this.bindFieldToDic("RZLB", "RZLB");
		
		this.setFieldDateFormat("FBSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JLSJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZHXGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		this.setVOTableName("XTBG_GZBG_GZRZ");
	}
	
	public void setDiaryid(String diaryid){
		this.setInternal("DIARYID",diaryid);
	}
	public String getDiaryid(){
		return (String)this.getInternal("DIARYID");
	}
	public void setUserid(String userid){
		this.setInternal("USERID",userid);
	}
	public String getUserid(){
		return (String)this.getInternal("USERID");
	}
	public void setFbsj(Date fbsj){
		this.setInternal("FBSJ",fbsj);
	}
	public Date getFbsj(){
		return (Date)this.getInternal("FBSJ");
	}
	public void setNr(Byte[] nr){
		this.setInternal("NR",nr);
	}
	public String getNr(){
		return (String)this.getInternal("NR");
	}
	public void setRzlb(String rzlb){
		this.setInternal("RZLB",rzlb);
	}
	public String getRzlb(){
		return (String)this.getInternal("RZLB");
	}
	public void setJlr(String jlr){
		this.setInternal("JLR",jlr);
	}
	public String getJlr(){
		return (String)this.getInternal("JLR");
	}
	public void setJlsj(Date jlsj){
		this.setInternal("JLSJ",jlsj);
	}
	public Date getJlsj(){
		return (Date)this.getInternal("JLSJ");
	}
	public void setZhxgsj(Date zhxgsj){
		this.setInternal("ZHXGSJ",zhxgsj);
	}
	public Date getZhxgsj(){
		return (Date)this.getInternal("ZHXGSJ");
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
}
