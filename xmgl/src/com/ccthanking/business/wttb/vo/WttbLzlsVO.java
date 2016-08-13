package com.ccthanking.business.wttb.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class WttbLzlsVO extends BaseVO{

	public WttbLzlsVO(){
		this.addField("WTTB_LZLS_ID",OP_STRING|this.TP_PK);
		this.addField("WTID",OP_STRING);
		this.addField("FSR",OP_STRING);
		this.addField("FSBM",OP_STRING);
		this.addField("FSSJ",OP_DATE);
		this.addField("JSR",OP_STRING);
		this.addField("JSBM",OP_STRING);
		this.addField("JSSJ",OP_DATE);
		this.addField("BLRJS",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("XWWCSJ",OP_DATE);
		this.addField("HFQK",OP_STRING);
		this.addField("MYCD",OP_STRING);
		this.addField("ZFID",OP_STRING);
		this.addField("SFDQBLR",OP_STRING);
		this.setFieldDateFormat("FSSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JSSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("XWWCSJ","yyyy-MM-dd");
		this.setVOTableName("WTTB_LZLS");
	}

	public void setWttb_lzls_id(String wttb_lzls_id){
		this.setInternal("WTTB_LZLS_ID",wttb_lzls_id);
	}
	public String getWttb_lzls_id(){
		return (String)this.getInternal("WTTB_LZLS_ID");
	}
	public void setWtid(String wtid){
		this.setInternal("WTID",wtid);
	}
	public String getWtid(){
		return (String)this.getInternal("WTID");
	}
	public void setFsr(String fsr){
		this.setInternal("FSR",fsr);
	}
	public String getFsr(){
		return (String)this.getInternal("FSR");
	}
	public void setFsbm(String fsbm){
		this.setInternal("FSBM",fsbm);
	}
	public String getFsbm(){
		return (String)this.getInternal("FSBM");
	}
	public void setFssj(Date fssj){
		this.setInternal("FSSJ",fssj);
	}
	public Date getFssj(){
		return (Date)this.getInternal("FSSJ");
	}
	public void setJsr(String jsr){
		this.setInternal("JSR",jsr);
	}
	public String getJsr(){
		return (String)this.getInternal("JSR");
	}
	public void setJsbm(String jsbm){
		this.setInternal("JSBM",jsbm);
	}
	public String getJsbm(){
		return (String)this.getInternal("JSBM");
	}
	public void setJssj(Date jssj){
		this.setInternal("JSSJ",jssj);
	}
	public Date getJssj(){
		return (Date)this.getInternal("JSSJ");
	}
	public void setBlrjs(String blrjs){
		this.setInternal("BLRJS",blrjs);
	}
	public String getBlrjs(){
		return (String)this.getInternal("BLRJS");
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
	public void setXwwcsj(Date xwwcsj){
		this.setInternal("XWWCSJ",xwwcsj);
	}
	public Date getXwwcsj(){
		return (Date)this.getInternal("XWWCSJ");
	}
	public void setHfqk(String hfqk){
		this.setInternal("HFQK",hfqk);
	}
	public String getHfqk(){
		return (String)this.getInternal("HFQK");
	}
	public void setMycd(String mycd){
		this.setInternal("MYCD",mycd);
	}
	public String getMycd(){
		return (String)this.getInternal("MYCD");
	}
	public void setZfid(String zfid){
		this.setInternal("ZFID",zfid);
	}
	public String getZfid(){
		return (String)this.getInternal("ZFID");
	}
	public void setSfdqblr(String sfdqblr){
		this.setInternal("SFDQBLR",sfdqblr);
	}
	public String getSfdqblr(){
		return (String)this.getInternal("SFDQBLR");
	}
}