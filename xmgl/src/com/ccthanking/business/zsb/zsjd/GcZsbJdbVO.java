package com.ccthanking.business.zsb.zsjd;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZsbJdbVO extends BaseVO{

	public GcZsbJdbVO(){
		this.addField("GC_ZSB_JDB_ID",OP_STRING|this.TP_PK);
		this.addField("ZDXXID",OP_STRING);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("SBRQ",OP_DATE);
		this.addField("LJJMZL",OP_STRING);
		this.addField("LJQYZL",OP_STRING);
		this.addField("BCWCJMS",OP_STRING);
		this.addField("BCWCQYS",OP_STRING);
		this.addField("BCZDMJ",OP_STRING);
		this.addField("LJZDMJ",OP_STRING);
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
		this.addField("WTJFX",OP_STRING);
		this.setFieldDateFormat("SBRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_ZSB_JDB");
	}

	public void setGc_zsb_jdb_id(String gc_zsb_jdb_id){
		this.setInternal("GC_ZSB_JDB_ID",gc_zsb_jdb_id);
	}
	public String getGc_zsb_jdb_id(){
		return (String)this.getInternal("GC_ZSB_JDB_ID");
	}
	public void setZdxxid(String zdxxid){
		this.setInternal("ZDXXID",zdxxid);
	}
	public String getZdxxid(){
		return (String)this.getInternal("ZDXXID");
	}
	public void setJhid(String jhid){
		this.setInternal("JHID",jhid);
	}
	public String getJhid(){
		return (String)this.getInternal("JHID");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
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
	public void setSbrq(Date sbrq){
		this.setInternal("SBRQ",sbrq);
	}
	public Date getSbrq(){
		return (Date)this.getInternal("SBRQ");
	}
	public void setLjjmzl(String ljjmzl){
		this.setInternal("LJJMZL",ljjmzl);
	}
	public String getLjjmzl(){
		return (String)this.getInternal("LJJMZL");
	}
	public void setLjqyzl(String ljqyzl){
		this.setInternal("LJQYZL",ljqyzl);
	}
	public String getLjqyzl(){
		return (String)this.getInternal("LJQYZL");
	}
	public void setBcwcjms(String bcwcjms){
		this.setInternal("BCWCJMS",bcwcjms);
	}
	public String getBcwcjms(){
		return (String)this.getInternal("BCWCJMS");
	}
	public void setBcwcqys(String bcwcqys){
		this.setInternal("BCWCQYS",bcwcqys);
	}
	public String getBcwcqys(){
		return (String)this.getInternal("BCWCQYS");
	}
	public void setBczdmj(String bczdmj){
		this.setInternal("BCZDMJ",bczdmj);
	}
	public String getBczdmj(){
		return (String)this.getInternal("BCZDMJ");
	}
	public void setLjzdmj(String ljzdmj){
		this.setInternal("LJZDMJ",ljzdmj);
	}
	public String getLjzdmj(){
		return (String)this.getInternal("LJZDMJ");
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
	public void setWtjfx(String wtjfx){
		this.setInternal("WTJFX",wtjfx);
	}
	public String getWtjfx(){
		return (String)this.getInternal("WTJFX");
	}
}