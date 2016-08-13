package com.ccthanking.business.bgh;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcBghWtVO extends BaseVO{

	public GcBghWtVO(){
		this.addField("GC_BGH_WT_ID",OP_STRING|this.TP_PK);
		this.addField("BGHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("WTBT",OP_STRING);
		this.addField("WTMS",OP_STRING);
		this.addField("FQR",OP_STRING);
		this.addField("XWJJSJ",OP_DATE);
		this.addField("ZT",OP_STRING);
		this.addField("FQSJ",OP_DATE);
		this.addField("SHR",OP_STRING);
		this.addField("SHSJ",OP_DATE);
		this.addField("SHYJ",OP_STRING);
		this.addField("BGHJL",OP_STRING);
		this.addField("BGHHF",OP_STRING);
		this.addField("ZZBM",OP_STRING);
		this.addField("ZZR",OP_STRING);
		this.addField("ZZZXLD",OP_STRING);
		this.addField("YQJJSJ",OP_DATE);
		this.addField("DBCS",OP_STRING);
		this.addField("ISJJ",OP_STRING);
		this.addField("JJSJ",OP_DATE);
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
		this.addField("WTLX",OP_STRING);
		this.addField("FQBM",OP_STRING);
		this.addField("XUHAO",OP_STRING);
		this.addField("WTID",OP_STRING);
		this.addField("WTHXCS",OP_STRING);
		this.addField("SFGK",OP_STRING);
		this.setFieldDateFormat("XWJJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FQSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SHSJ","yyyy-MM-dd");
		this.setFieldDateFormat("YQJJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.bindFieldToDic("ZT", "BGHZT");
	    this.bindFieldToDic("ISJJ", "SF");
	    this.bindFieldToDic("WTLX", "LX");
	    this.bindFieldToTranslater("FQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
	    this.bindFieldToTranslater("FQBM", "FS_ORG_DEPT", "ROW_ID", "DEPT_NAME");
		this.setVOTableName("GC_BGH_WT");
	}

	public void setGc_bgh_wt_id(String gc_bgh_wt_id){
		this.setInternal("GC_BGH_WT_ID",gc_bgh_wt_id);
	}
	public String getGc_bgh_wt_id(){
		return (String)this.getInternal("GC_BGH_WT_ID");
	}
	public void setWtid(String wtid){
		this.setInternal("WTID",wtid);
	}
	public String getWtid(){
		return (String)this.getInternal("WTID");
	}
	public void setBghid(String bghid){
		this.setInternal("BGHID",bghid);
	}
	public String getBghid(){
		return (String)this.getInternal("BGHID");
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
	public void setWtbt(String wtbt){
		this.setInternal("WTBT",wtbt);
	}
	public String getWtbt(){
		return (String)this.getInternal("WTBT");
	}
	public void setWtms(String wtms){
		this.setInternal("WTMS",wtms);
	}
	public String getWtms(){
		return (String)this.getInternal("WTMS");
	}
	public void setFqr(String fqr){
		this.setInternal("FQR",fqr);
	}
	public String getFqr(){
		return (String)this.getInternal("FQR");
	}
	public void setXwjjsj(Date xwjjsj){
		this.setInternal("XWJJSJ",xwjjsj);
	}
	public Date getXwjjsj(){
		return (Date)this.getInternal("XWJJSJ");
	}
	public void setZt(String zt){
		this.setInternal("ZT",zt);
	}
	public String getZt(){
		return (String)this.getInternal("ZT");
	}
	public void setFqsj(Date fqsj){
		this.setInternal("FQSJ",fqsj);
	}
	public Date getFqsj(){
		return (Date)this.getInternal("FQSJ");
	}
	public void setShr(String shr){
		this.setInternal("SHR",shr);
	}
	public String getShr(){
		return (String)this.getInternal("SHR");
	}
	public void setShsj(Date shsj){
		this.setInternal("SHSJ",shsj);
	}
	public Date getShsj(){
		return (Date)this.getInternal("SHSJ");
	}
	public void setShyj(String shyj){
		this.setInternal("SHYJ",shyj);
	}
	public String getShyj(){
		return (String)this.getInternal("SHYJ");
	}
	public void setBghjl(String bghjl){
		this.setInternal("BGHJL",bghjl);
	}
	public String getBghjl(){
		return (String)this.getInternal("BGHJL");
	}
	public void setBghhf(String bghhf){
		this.setInternal("BGHHF",bghhf);
	}
	public String getBghhf(){
		return (String)this.getInternal("BGHHF");
	}
	public void setZzbm(String zzbm){
		this.setInternal("ZZBM",zzbm);
	}
	public String getZzbm(){
		return (String)this.getInternal("ZZBM");
	}
	public void setZzr(String zzr){
		this.setInternal("ZZR",zzr);
	}
	public String getZzr(){
		return (String)this.getInternal("ZZR");
	}
	public void setZzzxld(String zzzxld){
		this.setInternal("ZZZXLD",zzzxld);
	}
	public String getZzzxld(){
		return (String)this.getInternal("ZZZXLD");
	}
	public void setYqjjsj(Date yqjjsj){
		this.setInternal("YQJJSJ",yqjjsj);
	}
	public Date getYqjjsj(){
		return (Date)this.getInternal("YQJJSJ");
	}
	public void setDbcs(String dbcs){
		this.setInternal("DBCS",dbcs);
	}
	public String getDbcs(){
		return (String)this.getInternal("DBCS");
	}
	public void setIsjj(String isjj){
		this.setInternal("ISJJ",isjj);
	}
	public String getIsjj(){
		return (String)this.getInternal("ISJJ");
	}
	public void setJjsj(Date jjsj){
		this.setInternal("JJSJ",jjsj);
	}
	public Date getJjsj(){
		return (Date)this.getInternal("JJSJ");
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
	public void setWtlx(String wtlx){
		this.setInternal("WTLX",wtlx);
	}
	public String getWtlx(){
		return (String)this.getInternal("WTLX");
	}
	public void setFqbm(String fqbm){
		this.setInternal("FQBM",fqbm);
	}
	public String getFqbm(){
		return (String)this.getInternal("FQBM");
	}
	public void setXuhao(String xuhao){
		this.setInternal("XUHAO",xuhao);
	}
	public String getXuhao(){
		return (String)this.getInternal("XUHAO");
	}

	public void setWthxcs(String wthxcs){
		this.setInternal("WTHXCS",wthxcs);
	}
	public String getWthxcs(){
		return (String)this.getInternal("WTHXCS");
	}
	public void setSfgk(String sfgk){
		this.setInternal("SFGK",sfgk);
	}
	public String getSfgk(){
		return (String)this.getInternal("SFGK");
	}
}