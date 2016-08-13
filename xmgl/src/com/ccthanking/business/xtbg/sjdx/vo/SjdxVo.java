package com.ccthanking.business.xtbg.sjdx.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class SjdxVo extends BaseVO {
	
	public SjdxVo() {
		this.addField("SMSID",OP_STRING|this.TP_PK);
		this.addField("JSR",OP_STRING);
		this.addField("JSRID",OP_STRING);
		this.addField("JSRHM",OP_STRING);
		this.addField("FSR",OP_STRING);
		this.addField("FSRID",OP_STRING);
		this.addField("FSSJ",OP_DATE);
		this.addField("FSXX",OP_STRING);
		this.addField("ZT",OP_STRING);
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
		
		this.setFieldDateFormat("FSSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		this.setVOTableName("XTBG_SJDX");
	}
	
	public void setSmsid(String smsid){
		this.setInternal("SMSID",smsid);
	}
	public String getSmsid(){
		return (String)this.getInternal("SMSID");
	}
	public void setJsr(String jsr){
		this.setInternal("JSR",jsr);
	}
	public String getJsr(){
		return (String)this.getInternal("JSR");
	}
	public void setJsrid(String jsrid){
		this.setInternal("JSRID",jsrid);
	}
	public String getJsrid(){
		return (String)this.getInternal("JSRID");
	}
	public void setJsrhm(String jsrhm){
		this.setInternal("JSRHM",jsrhm);
	}
	public String getJsrhm(){
		return (String)this.getInternal("JSRHM");
	}
	public void setFsr(String fsr){
		this.setInternal("FSR",fsr);
	}
	public String getFsr(){
		return (String)this.getInternal("FSR");
	}
	public void setFsrid(String fsrid){
		this.setInternal("FSRID",fsrid);
	}
	public String getFsrid(){
		return (String)this.getInternal("FSRID");
	}
	public void setFssj(Date fssj){
		this.setInternal("FSSJ",fssj);
	}
	public Date getFssj(){
		return (Date)this.getInternal("FSSJ");
	}
	public void setFsxx(String fsxx){
		this.setInternal("FSXX",fsxx);
	}
	public String getFsxx(){
		return (String)this.getInternal("FSXX");
	}
	public void setZt(String zt){
		this.setInternal("ZT",zt);
	}
	public String getZt(){
		return (String)this.getInternal("ZT");
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
