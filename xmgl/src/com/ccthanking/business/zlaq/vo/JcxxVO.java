package com.ccthanking.business.zlaq.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class JcxxVO extends BaseVO {

	public JcxxVO()
	{
		this.addField("GC_ZLAQ_JCB_ID",OP_STRING|this.TP_PK);
		this.addField("XDKID",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("BDMC",OP_STRING);
		this.addField("JCLX",OP_STRING);
		this.addField("JCGM",OP_STRING);
		this.addField("JCNR",OP_STRING);
		this.addField("CZWT",OP_STRING);
		this.addField("JCBM",OP_STRING);
		this.addField("JCRQ",OP_DATE);
		this.addField("JCDW",OP_STRING);
		this.addField("KCSJDW",OP_STRING);
		this.addField("ZT",OP_STRING);
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
		this.addField("JCR",OP_STRING);
		this.addField("XMBH",OP_STRING);
		this.addField("ISCZWT",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMGLGS",OP_STRING);
		this.addField("SGDW",OP_STRING);
		this.addField("FZR_SGDW",OP_STRING);
		this.addField("JLDW",OP_STRING);
		this.addField("FZR_JLDW",OP_STRING);
		this.addField("YZDB",OP_STRING);
		this.addField("SJDW",OP_STRING);
		this.addField("FZR_SJDW",OP_STRING);
		this.addField("FZR_GLGS",OP_STRING);
		this.addField("LXFS_GLGS",OP_STRING);
		this.addField("LXFS_SGDW",OP_STRING);
		this.addField("LXFS_JLDW",OP_STRING);
		this.addField("LXFS_SJDW",OP_STRING);
		this.addField("ZJBS",OP_STRING);
		this.addField("AJBS",OP_STRING);
		this.addField("JCBH",OP_STRING);
		this.addField("GC_JH_SJ_ID",OP_STRING);
		this.setFieldDateFormat("JCRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		//字典
		this.bindFieldToDic("JCLX", "JCLX");
		this.bindFieldToDic("JCGM", "JCGM");
		this.bindFieldToDic("ZT", "ZGZT");
		this.bindFieldToDic("FCJL", "FCJL");
		this.bindFieldToDic("ISCZWT", "SF");
		this.bindFieldToDic("JCBM", "ZZDW");
		/*this.bindFieldToDic("XMGLGS", "XMGLGS");*/
		this.bindFieldToTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
		this.bindFieldToTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.bindFieldToTranslater("FCR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.bindFieldToTranslater("FZR_JLDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.bindFieldToTranslater("FZR_SGDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.bindFieldToTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.bindFieldToTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("JCBM", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("LXFS_JLDW", "FS_ORG_PERSON", "ACCOUNT", "SJHM");
		this.bindFieldToTranslater("LXFS_SGDW", "FS_ORG_PERSON", "ACCOUNT", "SJHM");
		//this.bindFieldToTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");

		this.setVOTableName("GC_ZLAQ_JCB");
	}
	public void setGc_zlaq_jcb_id(String gc_zlaq_jcb_id){
		this.setInternal("GC_ZLAQ_JCB_ID",gc_zlaq_jcb_id);
	}
	public String getGc_zlaq_jcb_id(){
		return (String)this.getInternal("GC_ZLAQ_JCB_ID");
	}
	public void setXdkid(String xdkid){
		this.setInternal("XDKID",xdkid);
	}
	public String getXdkid(){
		return (String)this.getInternal("XDKID");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setBdmc(String bdmc){
		this.setInternal("BDMC",bdmc);
	}
	public String getBdmc(){
		return (String)this.getInternal("BDMC");
	}
	public void setJclx(String jclx){
		this.setInternal("JCLX",jclx);
	}
	public String getJclx(){
		return (String)this.getInternal("JCLX");
	}
	public void setJcgm(String jcgm){
		this.setInternal("JCGM",jcgm);
	}
	public String getJcgm(){
		return (String)this.getInternal("JCGM");
	}
	public void setJcnr(String jcnr){
		this.setInternal("JCNR",jcnr);
	}
	public String getJcnr(){
		return (String)this.getInternal("JCNR");
	}
	public void setCzwt(String czwt){
		this.setInternal("CZWT",czwt);
	}
	public String getCzwt(){
		return (String)this.getInternal("CZWT");
	}
	public void setJcbm(String jcbm){
		this.setInternal("JCBM",jcbm);
	}
	public String getJcbm(){
		return (String)this.getInternal("JCBM");
	}
	public void setJcrq(Date jcrq){
		this.setInternal("JCRQ",jcrq);
	}
	public Date getJcrq(){
		return (Date)this.getInternal("JCRQ");
	}
	public void setJcdw(String jcdw){
		this.setInternal("JCDW",jcdw);
	}
	public String getJcdw(){
		return (String)this.getInternal("JCDW");
	}
	public void setKcsjdw(String kcsjdw){
		this.setInternal("KCSJDW",kcsjdw);
	}
	public String getKcsjdw(){
		return (String)this.getInternal("KCSJDW");
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
	public void setJcr(String jcr){
		this.setInternal("JCR",jcr);
	}
	public String getJcr(){
		return (String)this.getInternal("JCR");
	}
	public void setXmbh(String xmbh){
		this.setInternal("XMBH",xmbh);
	}
	public String getXmbh(){
		return (String)this.getInternal("XMBH");
	}
	public void setIsczwt(String isczwt){
		this.setInternal("ISCZWT",isczwt);
	}
	public String getIsczwt(){
		return (String)this.getInternal("ISCZWT");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setXmglgs(String xmglgs){
		this.setInternal("XMGLGS",xmglgs);
	}
	public String getXmglgs(){
		return (String)this.getInternal("XMGLGS");
	}
	public void setSgdw(String sgdw){
		this.setInternal("SGDW",sgdw);
	}
	public String getSgdw(){
		return (String)this.getInternal("SGDW");
	}
	public void setFzr_sgdw(String fzr_sgdw){
		this.setInternal("FZR_SGDW",fzr_sgdw);
	}
	public String getFzr_sgdw(){
		return (String)this.getInternal("FZR_SGDW");
	}
	public void setJldw(String jldw){
		this.setInternal("JLDW",jldw);
	}
	public String getJldw(){
		return (String)this.getInternal("JLDW");
	}
	public void setFzr_jldw(String fzr_jldw){
		this.setInternal("FZR_JLDW",fzr_jldw);
	}
	public String getFzr_jldw(){
		return (String)this.getInternal("FZR_JLDW");
	}
	public void setYzdb(String yzdb){
		this.setInternal("YZDB",yzdb);
	}
	public String getYzdb(){
		return (String)this.getInternal("YZDB");
	}
	public void setSjdw(String sjdw){
		this.setInternal("SJDW",sjdw);
	}
	public String getSjdw(){
		return (String)this.getInternal("SJDW");
	}
	public void setFzr_sjdw(String fzr_sjdw){
		this.setInternal("FZR_SJDW",fzr_sjdw);
	}
	public String getFzr_sjdw(){
		return (String)this.getInternal("FZR_SJDW");
	}
	public void setFzr_glgs(String fzr_glgs){
		this.setInternal("FZR_GLGS",fzr_glgs);
	}
	public String getFzr_glgs(){
		return (String)this.getInternal("FZR_GLGS");
	}
	public void setLxfs_glgs(String lxfs_glgs){
		this.setInternal("LXFS_GLGS",lxfs_glgs);
	}
	public String getLxfs_glgs(){
		return (String)this.getInternal("LXFS_GLGS");
	}
	public void setLxfs_sgdw(String lxfs_sgdw){
		this.setInternal("LXFS_SGDW",lxfs_sgdw);
	}
	public String getLxfs_sgdw(){
		return (String)this.getInternal("LXFS_SGDW");
	}
	public void setLxfs_jldw(String lxfs_jldw){
		this.setInternal("LXFS_JLDW",lxfs_jldw);
	}
	public String getLxfs_jldw(){
		return (String)this.getInternal("LXFS_JLDW");
	}
	public void setLxfs_sjdw(String lxfs_sjdw){
		this.setInternal("LXFS_SJDW",lxfs_sjdw);
	}
	public String getLxfs_sjdw(){
		return (String)this.getInternal("LXFS_SJDW");
	}
	public void setJcbh(String jcbh){
		this.setInternal("JCBH",jcbh);
	}
	public String getJcbh(){
		return (String)this.getInternal("JCBH");
	}

	public void setGc_jh_sj_id(String gc_jh_sj_id){
		this.setInternal("GC_JH_SJ_ID",gc_jh_sj_id);
	}
	public String getGc_jh_sj_id(){
		return (String)this.getInternal("GC_JH_SJ_ID");
	}
	public void setZjbs(String zjbs){
		this.setInternal("ZJBS",zjbs);
	}
	public String getZjbs(){
		return (String)this.getInternal("ZJBS");
	}
	public void setAjbs(String ajbs){
		this.setInternal("AJBS",ajbs);
	}
	public String getAjbs(){
		return (String)this.getInternal("AJBS");
	}

}


