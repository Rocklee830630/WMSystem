package com.ccthanking.business.xtbg.zxxw.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class ZxxwVo extends BaseVO {
	
	public ZxxwVo() {
		this.addField("NEWSID",OP_STRING|this.TP_PK);
		this.addField("XWBT",OP_STRING);
		this.addField("XWLB",OP_STRING);
		this.addField("NR",OP_CLOB);
		this.addField("FBR",OP_STRING);
		this.addField("FBSJ",OP_DATE);
		this.addField("YDCS",OP_STRING);
		this.addField("ZT",OP_STRING);
		this.addField("ZHHFR",OP_STRING);
		this.addField("ZHHFSJ",OP_DATE);
		this.addField("HFCS",OP_STRING);
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
		this.addField("NF",OP_STRING);
		
		this.setFieldDateFormat("FBSJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZHHFSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		this.setVOTableName("XTBG_XXZX_ZXXW");
	}
	
	public void setNf(String nf){
		this.setInternal("NF",nf);
	}
	public String getNf(){
		return (String)this.getInternal("NF");
	}
	
	public void setNewsid(String newsid){
		this.setInternal("NEWSID",newsid);
	}
	public String getNewsid(){
		return (String)this.getInternal("NEWSID");
	}
	public void setXwbt(String xwbt){
		this.setInternal("XWBT",xwbt);
	}
	public String getXwbt(){
		return (String)this.getInternal("XWBT");
	}
	public void setXwlb(String xwlb){
		this.setInternal("XWLB",xwlb);
	}
	public String getXwlb(){
		return (String)this.getInternal("XWLB");
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
	public void setFbsj(Date fbsj){
		this.setInternal("FBSJ",fbsj);
	}
	public Date getFbsj(){
		return (Date)this.getInternal("FBSJ");
	}
	public void setYdcs(String ydcs){
		this.setInternal("YDCS",ydcs);
	}
	public String getYdcs(){
		return (String)this.getInternal("YDCS");
	}
	public void setZt(String zt){
		this.setInternal("ZT",zt);
	}
	public String getZt(){
		return (String)this.getInternal("ZT");
	}
	public void setZhhfr(String zhhfr){
		this.setInternal("ZHHFR",zhhfr);
	}
	public String getZhhfr(){
		return (String)this.getInternal("ZHHFR");
	}
	public void setZhhfsj(Date zhhfsj){
		this.setInternal("ZHHFSJ",zhhfsj);
	}
	public Date getZhhfsj(){
		return (Date)this.getInternal("ZHHFSJ");
	}
	public void setHfcs(String hfcs){
		this.setInternal("HFCS",hfcs);
	}
	public String getHfcs(){
		return (String)this.getInternal("HFCS");
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
