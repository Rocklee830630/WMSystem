package com.ccthanking.business.xtbg.gwgl.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class SwglVo extends BaseVO {
	
	public SwglVo(){
		this.addField("SWID",OP_STRING|this.TP_PK);
		this.addField("SWBH",OP_STRING);
		this.addField("WJBT",OP_STRING);
		this.addField("LWDW",OP_STRING);
		this.addField("DQZT",OP_STRING);
		this.addField("WZ",OP_STRING);
		this.addField("HJ",OP_STRING);
		this.addField("WH",OP_STRING);
		this.addField("SWRQ",OP_DATE);
		this.addField("SWLB",OP_STRING);
		this.addField("SSXM",OP_STRING);
		this.addField("CBKS",OP_STRING);
		this.addField("FWRQ",OP_DATE);
		this.addField("MJ",OP_STRING);
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
		this.addField("SERIAL_NUM",OP_STRING);
		
		this.setFieldDateFormat("SWRQ","yyyy-MM-dd");
		this.setFieldDateFormat("FWRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.bindFieldToTranslater("LWDW", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
		this.bindFieldToTranslater("WZ", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
		
		this.setVOTableName("XTBG_GWGL_SWGL");
	}

	public void setSwid(String swid){
		this.setInternal("SWID",swid);
	}
	public String getSwid(){
		return (String)this.getInternal("SWID");
	}
	public void setSwbh(String swbh){
		this.setInternal("SWBH",swbh);
	}
	public String getSwbh(){
		return (String)this.getInternal("SWBH");
	}
	public void setWjbt(String wjbt){
		this.setInternal("WJBT",wjbt);
	}
	public String getWjbt(){
		return (String)this.getInternal("WJBT");
	}
	public void setLwdw(String lwdw){
		this.setInternal("LWDW",lwdw);
	}
	public String getLwdw(){
		return (String)this.getInternal("LWDW");
	}
	public void setDqzt(String dqzt){
		this.setInternal("DQZT",dqzt);
	}
	public String getDqzt(){
		return (String)this.getInternal("DQZT");
	}
	public void setWz(String wz){
		this.setInternal("WZ",wz);
	}
	public String getWz(){
		return (String)this.getInternal("WZ");
	}
	public void setHj(String hj){
		this.setInternal("HJ",hj);
	}
	public String getHj(){
		return (String)this.getInternal("HJ");
	}
	public void setWh(String wh){
		this.setInternal("WH",wh);
	}
	public String getWh(){
		return (String)this.getInternal("WH");
	}
	public void setSwrq(Date swrq){
		this.setInternal("SWRQ",swrq);
	}
	public Date getSwrq(){
		return (Date)this.getInternal("SWRQ");
	}
	public void setSwlb(String swlb){
		this.setInternal("SWLB",swlb);
	}
	public String getSwlb(){
		return (String)this.getInternal("SWLB");
	}
	public void setSsxm(String ssxm){
		this.setInternal("SSXM",ssxm);
	}
	public String getSsxm(){
		return (String)this.getInternal("SSXM");
	}
	public void setCbks(String cbks){
		this.setInternal("CBKS",cbks);
	}
	public String getCbks(){
		return (String)this.getInternal("CBKS");
	}
	public void setFwrq(Date fwrq){
		this.setInternal("FWRQ",fwrq);
	}
	public Date getFwrq(){
		return (Date)this.getInternal("FWRQ");
	}
	public void setMj(String mj){
		this.setInternal("MJ",mj);
	}
	public String getMj(){
		return (String)this.getInternal("MJ");
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
	
	public void setSerialNum(String SerialNum){
		this.setInternal("SERIAL_NUM",SerialNum);
	}
	public String getSerialNum(){
		return (String)this.getInternal("SERIAL_NUM");
	}
}
