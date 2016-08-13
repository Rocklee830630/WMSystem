package com.ccthanking.business.xdxmk.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class XmndbVO extends BaseVO{

	public XmndbVO(){
		this.addField("GC_TCJH_XMNDB_ID",OP_STRING|this.TP_PK);
		this.addField("XMID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("NDJSNR",OP_STRING);
		this.addField("NDJSRW",OP_STRING);
		this.addField("NDZTZ",OP_STRING);
		this.addField("GC",OP_STRING);
		this.addField("ZC",OP_STRING);
		this.addField("QT",OP_STRING);
		this.addField("XJXJ",OP_STRING);
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
		
		//字典
		this.bindFieldToDic("XJXJ", "XMXZ");
		
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_TCJH_XMNDB");
	}

	public void setGc_tcjh_xmndb_id(String gc_tcjh_xmndb_id){
		this.setInternal("GC_TCJH_XMNDB_ID",gc_tcjh_xmndb_id);
	}
	public String getGc_tcjh_xmndb_id(){
		return (String)this.getInternal("GC_TCJH_XMNDB_ID");
	}
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setNdjsnr(String ndjsnr){
		this.setInternal("NDJSNR",ndjsnr);
	}
	public String getNdjsnr(){
		return (String)this.getInternal("NDJSNR");
	}
	public void setNdjsrw(String ndjsrw){
		this.setInternal("NDJSRW",ndjsrw);
	}
	public String getNdjsrw(){
		return (String)this.getInternal("NDJSRW");
	}
	public void setNdztz(String ndztz){
		this.setInternal("NDZTZ",ndztz);
	}
	public String getNdztz(){
		return (String)this.getInternal("NDZTZ");
	}
	public void setGc(String gc){
		this.setInternal("GC",gc);
	}
	public String getGc(){
		return (String)this.getInternal("GC");
	}
	public void setZc(String zc){
		this.setInternal("ZC",zc);
	}
	public String getZc(){
		return (String)this.getInternal("ZC");
	}
	public void setQt(String qt){
		this.setInternal("QT",qt);
	}
	public String getQt(){
		return (String)this.getInternal("QT");
	}
	public void setXjxj(String xjxj){
		this.setInternal("XJXJ",xjxj);
	}
	public String getXjxj(){
		return (String)this.getInternal("XJXJ");
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