package com.ccthanking.business.zlaq.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ZgxxVO extends BaseVO {
	
	public ZgxxVO()
	{
		this.addField("GC_ZLAQ_ZGB_ID",OP_STRING|this.TP_PK);
		this.addField("JCBID",OP_STRING);
		this.addField("TZRQ",OP_DATE);
		this.addField("HFRQ",OP_DATE);
		this.addField("FCRQ",OP_DATE);
		this.addField("ZGZT",OP_STRING);
		this.addField("CLJY",OP_STRING);
		this.addField("XGRQ",OP_DATE);
		this.addField("JCBM",OP_STRING);
		this.addField("FCR",OP_STRING);
		this.addField("HFNR",OP_STRING);
		this.addField("FCJL",OP_STRING);
		this.addField("JCR",OP_STRING);
		this.addField("SJDW",OP_STRING);
		this.addField("SJR",OP_STRING);
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
		this.addField("LXBS",OP_STRING);
		this.addField("HFR",OP_STRING);
		this.addField("XGQX",OP_STRING);
		this.addField("ZGBH",OP_STRING);
		this.addField("HFBH",OP_STRING);
		this.addField("FCBH",OP_STRING);
		this.addField("FCYJ",OP_STRING);
		this.setFieldDateFormat("TZRQ","yyyy-MM-dd");
		this.setFieldDateFormat("HFRQ","yyyy-MM-dd");
		this.setFieldDateFormat("FCRQ","yyyy-MM-dd");
		this.setFieldDateFormat("XGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		//字典
		this.bindFieldToDic("JCLX", "JCLX");
		this.bindFieldToDic("FCJL", "FCJL");
		this.bindFieldToDic("JCGM", "JCGM");
		this.bindFieldToDic("ZGZT", "ZGZT");
		this.setVOTableName("GC_ZLAQ_JCB");
		this.bindFieldToDic("ISCZWT", "SF");
		this.bindFieldToDic("JCBM", "ZZDW");
		/*this.bindFieldToDic("XMGLGS", "XMGLGS");*/
		this.bindFieldToTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
		this.bindFieldToTranslater("FCR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.setVOTableName("GC_ZLAQ_ZGB");

	}
	public void setGc_zlaq_zgb_id(String gc_zlaq_zgb_id){
		this.setInternal("GC_ZLAQ_ZGB_ID",gc_zlaq_zgb_id);
	}
	public String getGc_zlaq_zgb_id(){
		return (String)this.getInternal("GC_ZLAQ_ZGB_ID");
	}
	public void setJcbid(String jcbid){
		this.setInternal("JCBID",jcbid);
	}
	public String getJcbid(){
		return (String)this.getInternal("JCBID");
	}
	public void setTzrq(Date tzrq){
		this.setInternal("TZRQ",tzrq);
	}
	public Date getTzrq(){
		return (Date)this.getInternal("TZRQ");
	}
	public void setHfrq(Date hfrq){
		this.setInternal("HFRQ",hfrq);
	}
	public Date getHfrq(){
		return (Date)this.getInternal("HFRQ");
	}
	public void setFcrq(Date fcrq){
		this.setInternal("FCRQ",fcrq);
	}
	public Date getFcrq(){
		return (Date)this.getInternal("FCRQ");
	}
	public void setZgzt(String zgzt){
		this.setInternal("ZGZT",zgzt);
	}
	public String getZgzt(){
		return (String)this.getInternal("ZGZT");
	}
	public void setCljy(String cljy){
		this.setInternal("CLJY",cljy);
	}
	public String getCljy(){
		return (String)this.getInternal("CLJY");
	}
	public void setXgrq(Date xgrq){
		this.setInternal("XGRQ",xgrq);
	}
	public Date getXgrq(){
		return (Date)this.getInternal("XGRQ");
	}
	public void setJcbm(String jcbm){
		this.setInternal("JCBM",jcbm);
	}
	public String getJcbm(){
		return (String)this.getInternal("JCBM");
	}
	public void setFcr(String fcr){
		this.setInternal("FCR",fcr);
	}
	public String getFcr(){
		return (String)this.getInternal("FCR");
	}
	public void setHfnr(String hfnr){
		this.setInternal("HFNR",hfnr);
	}
	public String getHfnr(){
		return (String)this.getInternal("HFNR");
	}
	public void setFcjl(String fcjl){
		this.setInternal("FCJL",fcjl);
	}
	public String getFcjl(){
		return (String)this.getInternal("FCJL");
	}
	public void setJcr(String jcr){
		this.setInternal("JCR",jcr);
	}
	public String getJcr(){
		return (String)this.getInternal("JCR");
	}
	public void setSjdw(String sjdw){
		this.setInternal("SJDW",sjdw);
	}
	public String getSjdw(){
		return (String)this.getInternal("SJDW");
	}
	public void setSjr(String sjr){
		this.setInternal("SJR",sjr);
	}
	public String getSjr(){
		return (String)this.getInternal("SJR");
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
	public void setHfr(String hfr){
		this.setInternal("HFR",hfr);
	}
	public String getHfr(){
		return (String)this.getInternal("HFR");
	}
	public void setXgqx(String xgqx){
		this.setInternal("XGQX",xgqx);
	}
	public String getXgqx(){
		return (String)this.getInternal("XGQX");
	}
	public void setZgbh(String zgbh){
		this.setInternal("ZGBH",zgbh);
	}
	public String getZgbh(){
		return (String)this.getInternal("ZGBH");
	}
	public void setLxbs(String lxbs){
		this.setInternal("LXBS",lxbs);
	}
	public String getLxbs(){
		return (String)this.getInternal("LXBS");
	}
	public void setHfbh(String hfbh){
		this.setInternal("HFBH",hfbh);
	}
	public String getHfbh(){
		return (String)this.getInternal("HFBH");
	}
	public void setFcbh(String fcbh){
		this.setInternal("FCBH",fcbh);
	}
	public String getFcbh(){
		return (String)this.getInternal("FCBH");
	}
	public void setFcyj(String fcyj){
		this.setInternal("FCYJ",fcyj);
	}
	public String getFcyj(){
		return (String)this.getInternal("FCYJ");
	}
	
}
