package com.ccthanking.business.ztb.zbxq;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZtbXqsjYsVO extends BaseVO{

	public GcZtbXqsjYsVO(){
		this.addField("GC_ZTB_XQSJ_YS_ID",OP_STRING|this.TP_PK);
		this.addField("ZTBXQID",OP_STRING);
		this.addField("ZTBSJID",OP_STRING);
		this.addField("SJZT",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_ZTB_XQSJ_YS");
	}

	public void setGc_ztb_xqsj_ys_id(String gc_ztb_xqsj_ys_id){
		this.setInternal("GC_ZTB_XQSJ_YS_ID",gc_ztb_xqsj_ys_id);
	}
	public String getGc_ztb_xqsj_ys_id(){
		return (String)this.getInternal("GC_ZTB_XQSJ_YS_ID");
	}
	public void setZtbxqid(String ztbxqid){
		this.setInternal("ZTBXQID",ztbxqid);
	}
	public String getZtbxqid(){
		return (String)this.getInternal("ZTBXQID");
	}
	public void setZtbsjid(String ztbsjid){
		this.setInternal("ZTBSJID",ztbsjid);
	}
	public String getZtbsjid(){
		return (String)this.getInternal("ZTBSJID");
	}
	public void setSjzt(String sjzt){
		this.setInternal("SJZT",sjzt);
	}
	public String getSjzt(){
		return (String)this.getInternal("SJZT");
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
}