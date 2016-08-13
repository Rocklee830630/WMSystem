package com.ccthanking.business.bgh.bgh;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcBghVO extends BaseVO{

	public GcBghVO(){
		this.addField("GC_BGH_ID",OP_STRING|this.TP_PK);
		this.addField("HYSJ",OP_DATE);
		this.addField("HC",OP_STRING);
		this.addField("HYDD",OP_STRING);
		this.addField("HYZC",OP_STRING);
		this.addField("CHBM",OP_STRING);
		this.addField("HYZT",OP_STRING);
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
		this.addField("CHRY",OP_STRING);
		this.setFieldDateFormat("HYSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_BGH");
	}

	public void setGc_bgh_id(String gc_bgh_id){
		this.setInternal("GC_BGH_ID",gc_bgh_id);
	}
	public String getGc_bgh_id(){
		return (String)this.getInternal("GC_BGH_ID");
	}
	public void setHysj(Date hysj){
		this.setInternal("HYSJ",hysj);
	}
	public Date getHysj(){
		return (Date)this.getInternal("HYSJ");
	}
	public void setHc(String hc){
		this.setInternal("HC",hc);
	}
	public String getHc(){
		return (String)this.getInternal("HC");
	}
	public void setHydd(String hydd){
		this.setInternal("HYDD",hydd);
	}
	public String getHydd(){
		return (String)this.getInternal("HYDD");
	}
	public void setHyzc(String hyzc){
		this.setInternal("HYZC",hyzc);
	}
	public String getHyzc(){
		return (String)this.getInternal("HYZC");
	}
	public void setChbm(String chbm){
		this.setInternal("CHBM",chbm);
	}
	public String getChbm(){
		return (String)this.getInternal("CHBM");
	}
	public void setHyzt(String hyzt){
		this.setInternal("HYZT",hyzt);
	}
	public String getHyzt(){
		return (String)this.getInternal("HYZT");
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
	public void setChry(String chry){
		this.setInternal("CHRY",chry);
	}
	public String getChry(){
		return (String)this.getInternal("CHRY");
	}
}