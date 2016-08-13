package com.ccthanking.business.bzwd.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class BzwdVO extends BaseVO{

	public BzwdVO(){
		this.addField("GC_WD_BZWD_ID",OP_STRING|this.TP_PK);
		this.addField("WDID",OP_STRING);
		this.addField("WDMC",OP_STRING);
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
		this.addField("SORT",OP_STRING);
		this.addField("WDDJ",OP_STRING);
		this.addField("PARENT_ID",OP_STRING);
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_WD_BZWD");
	}

	public void setGc_wd_bzwd_id(String gc_wd_bzwd_id){
		this.setInternal("GC_WD_BZWD_ID",gc_wd_bzwd_id);
	}
	public String getGc_wd_bzwd_id(){
		return (String)this.getInternal("GC_WD_BZWD_ID");
	}
	public void setWdid(String wdid){
		this.setInternal("WDID",wdid);
	}
	public String getWdid(){
		return (String)this.getInternal("WDID");
	}
	public void setWdmc(String wdmc){
		this.setInternal("WDMC",wdmc);
	}
	public String getWdmc(){
		return (String)this.getInternal("WDMC");
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
	public void setSort(String sort){
		this.setInternal("SORT",sort);
	}
	public String getSort(){
		return (String)this.getInternal("SORT");
	}
	public void setWddj(String wddj){
		this.setInternal("WDDJ",wddj);
	}
	public String getWddj(){
		return (String)this.getInternal("WDDJ");
	}
	public void setParent_id(String parent_id){
		this.setInternal("PARENT_ID",parent_id);
	}
	public String getParent_id(){
		return (String)this.getInternal("PARENT_ID");
	}
}