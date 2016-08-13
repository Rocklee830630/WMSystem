package com.ccthanking.business.sjgl.sjbggl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcSjSjbgJs2VO extends BaseVO{

	public GcSjSjbgJs2VO(){
		this.addField("GC_SJ_SJBG_JS2_ID",OP_STRING|this.TP_PK);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("BGBH",OP_STRING);
		this.addField("BGLB",OP_STRING);
		this.addField("BGLLDJSRQ",OP_DATE);
		this.addField("SJY",OP_STRING);
		this.addField("FS",OP_STRING);
		this.addField("JBGRQ",OP_DATE);
		this.addField("JSRQ",OP_DATE);
		this.addField("JSR",OP_STRING);
		this.addField("BGNR",OP_STRING);
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
		this.addField("BGFY",OP_STRING);
		this.addField("LLDFFRQ",OP_DATE);
		this.addField("BGZT",OP_STRING);
		this.addField("SJWYBH",OP_STRING);
		this.bindFieldToDic("BGLB", "BGLB2");
		this.bindFieldToTranslater("SJY", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("JSR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.setFieldDateFormat("BGLLDJSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("JBGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("JSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LLDFFRQ","yyyy-MM-dd");
		this.setVOTableName("GC_SJ_SJBG_JS2");
	}

	public void setGc_sj_sjbg_js2_id(String gc_sj_sjbg_js2_id){
		this.setInternal("GC_SJ_SJBG_JS2_ID",gc_sj_sjbg_js2_id);
	}
	public String getGc_sj_sjbg_js2_id(){
		return (String)this.getInternal("GC_SJ_SJBG_JS2_ID");
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
	public void setBgbh(String bgbh){
		this.setInternal("BGBH",bgbh);
	}
	public String getBgbh(){
		return (String)this.getInternal("BGBH");
	}
	public void setBglb(String bglb){
		this.setInternal("BGLB",bglb);
	}
	public String getBglb(){
		return (String)this.getInternal("BGLB");
	}
	public void setBglldjsrq(Date bglldjsrq){
		this.setInternal("BGLLDJSRQ",bglldjsrq);
	}
	public Date getBglldjsrq(){
		return (Date)this.getInternal("BGLLDJSRQ");
	}
	public void setSjy(String sjy){
		this.setInternal("SJY",sjy);
	}
	public String getSjy(){
		return (String)this.getInternal("SJY");
	}
	public void setFs(String fs){
		this.setInternal("FS",fs);
	}
	public String getFs(){
		return (String)this.getInternal("FS");
	}
	public void setJbgrq(Date jbgrq){
		this.setInternal("JBGRQ",jbgrq);
	}
	public Date getJbgrq(){
		return (Date)this.getInternal("JBGRQ");
	}
	public void setJsrq(Date jsrq){
		this.setInternal("JSRQ",jsrq);
	}
	public Date getJsrq(){
		return (Date)this.getInternal("JSRQ");
	}
	public void setJsr(String jsr){
		this.setInternal("JSR",jsr);
	}
	public String getJsr(){
		return (String)this.getInternal("JSR");
	}
	public void setBgnr(String bgnr){
		this.setInternal("BGNR",bgnr);
	}
	public String getBgnr(){
		return (String)this.getInternal("BGNR");
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
	public void setBgfy(String bgfy){
		this.setInternal("BGFY",bgfy);
	}
	public String getBgfy(){
		return (String)this.getInternal("BGFY");
	}
	public void setLldffrq(Date lldffrq){
		this.setInternal("LLDFFRQ",lldffrq);
	}
	public Date getLldffrq(){
		return (Date)this.getInternal("LLDFFRQ");
	}
	public void setBgzt(String bgzt){
		this.setInternal("BGZT",bgzt);
	}
	public String getBgzt(){
		return (String)this.getInternal("BGZT");
	}
	public void setSjwybh(String sjwybh){
		this.setInternal("SJWYBH",sjwybh);
	}
	public String getSjwybh(){
		return (String)this.getInternal("SJWYBH");
	}
}