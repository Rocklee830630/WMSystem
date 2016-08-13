package com.ccthanking.business.zsb.zsgl.xmb;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZsbXmbVO extends BaseVO{

	public GcZsbXmbVO(){
		this.addField("GC_ZSB_XMB_ID",OP_STRING|this.TP_PK);
		this.addField("SFCQ",OP_STRING);
		this.addField("SJWCRQ",OP_DATE);
		this.addField("CDSJSJ",OP_DATE);
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
		this.addField("JHID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("WTJFX",OP_STRING);
		this.addField("ISFK",OP_STRING);
		this.bindFieldToDic("SFCQ", "SF");
		this.bindFieldToDic("ISFK", "SF");
		this.setFieldDateFormat("SJWCRQ","yyyy-MM-dd");
		this.setFieldDateFormat("CDSJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_ZSB_XMB");
	}

	public void setGc_zsb_xmb_id(String gc_zsb_xmb_id){
		this.setInternal("GC_ZSB_XMB_ID",gc_zsb_xmb_id);
	}
	public String getGc_zsb_xmb_id(){
		return (String)this.getInternal("GC_ZSB_XMB_ID");
	}
	public void setSfcq(String sfcq){
		this.setInternal("SFCQ",sfcq);
	}
	public String getSfcq(){
		return (String)this.getInternal("SFCQ");
	}
	public void setSjwcrq(Date sjwcrq){
		this.setInternal("SJWCRQ",sjwcrq);
	}
	public Date getSjwcrq(){
		return (Date)this.getInternal("SJWCRQ");
	}
	public void setCdsjsj(Date cdsjsj){
		this.setInternal("CDSJSJ",cdsjsj);
	}
	public Date getCdsjsj(){
		return (Date)this.getInternal("CDSJSJ");
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
	public void setJhid(String jhid){
		this.setInternal("JHID",jhid);
	}
	public String getJhid(){
		return (String)this.getInternal("JHID");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setWtjfx(String wtjfx){
		this.setInternal("WTJFX",wtjfx);
	}
	public String getWtjfx(){
		return (String)this.getInternal("WTJFX");
	}
	public void setIsfk(String isfk){
		this.setInternal("ISFK",isfk);
	}
	public String getIsfk(){
		return (String)this.getInternal("ISFK");
	}
}