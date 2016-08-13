package com.ccthanking.business.xmglgs.xxjd.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class XxjdVO extends BaseVO{

	public XxjdVO(){
		this.addField("GC_XMGLGS_XXJD_ID",OP_STRING|this.TP_PK);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("XMBH",OP_STRING);
		this.addField("SJDW",OP_STRING);
		this.addField("JLDW",OP_STRING);
		this.addField("SGDW",OP_STRING);
		this.addField("JZFK",OP_STRING);
		this.addField("FKRQ",OP_DATE);
		this.addField("FXMS",OP_STRING);
		this.addField("SJKGSJ",OP_DATE);
		this.addField("SJWGSJ",OP_DATE);
		this.addField("BZ",OP_STRING);
		this.addField("ZT",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
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
		this.bindFieldToDic("ZT", "XXJDZT");
		//表选
		this.bindFieldToTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		
		this.setFieldDateFormat("FKRQ","yyyy-MM-dd");
		this.setFieldDateFormat("SJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SJWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_XMGLGS_XXJD");
	}

	public void setGc_xmglgs_xxjd_id(String gc_xmglgs_xxjd_id){
		this.setInternal("GC_XMGLGS_XXJD_ID",gc_xmglgs_xxjd_id);
	}
	public String getGc_xmglgs_xxjd_id(){
		return (String)this.getInternal("GC_XMGLGS_XXJD_ID");
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
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setXmbh(String xmbh){
		this.setInternal("XMBH",xmbh);
	}
	public String getXmbh(){
		return (String)this.getInternal("XMBH");
	}
	public void setSjdw(String sjdw){
		this.setInternal("SJDW",sjdw);
	}
	public String getSjdw(){
		return (String)this.getInternal("SJDW");
	}
	public void setJldw(String jldw){
		this.setInternal("JLDW",jldw);
	}
	public String getJldw(){
		return (String)this.getInternal("JLDW");
	}
	public void setSgdw(String sgdw){
		this.setInternal("SGDW",sgdw);
	}
	public String getSgdw(){
		return (String)this.getInternal("SGDW");
	}
	public void setJzfk(String jzfk){
		this.setInternal("JZFK",jzfk);
	}
	public String getJzfk(){
		return (String)this.getInternal("JZFK");
	}
	public void setFkrq(Date fkrq){
		this.setInternal("FKRQ",fkrq);
	}
	public Date getFkrq(){
		return (Date)this.getInternal("FKRQ");
	}
	public void setFxms(String fxms){
		this.setInternal("FXMS",fxms);
	}
	public String getFxms(){
		return (String)this.getInternal("FXMS");
	}
	public void setSjkgsj(Date sjkgsj){
		this.setInternal("SJKGSJ",sjkgsj);
	}
	public Date getSjkgsj(){
		return (Date)this.getInternal("SJKGSJ");
	}
	public void setSjwgsj(Date sjwgsj){
		this.setInternal("SJWGSJ",sjwgsj);
	}
	public Date getSjwgsj(){
		return (Date)this.getInternal("SJWGSJ");
	}
	public void setBz(String bz){
		this.setInternal("BZ",bz);
	}
	public String getBz(){
		return (String)this.getInternal("BZ");
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