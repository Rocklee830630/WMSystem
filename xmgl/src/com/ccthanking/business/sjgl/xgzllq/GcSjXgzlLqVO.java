package com.ccthanking.business.sjgl.xgzllq;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcSjXgzlLqVO extends BaseVO{

	public GcSjXgzlLqVO(){
		this.addField("GC_SJ_XGZL_LQ_ID",OP_STRING|this.TP_PK);
		this.addField("ZLLJSD",OP_STRING);
		this.addField("ZLLB",OP_STRING);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("FS",OP_STRING);
		this.addField("LQBM",OP_STRING);
		this.addField("LQRQ",OP_DATE);
		this.addField("LQR",OP_STRING);
		this.addField("BLR",OP_STRING);
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
		this.bindFieldToTranslater("LQBM", "FS_ORG_DEPT", "ROW_ID", "DEPT_NAME");
		this.bindFieldToTranslater("LQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.bindFieldToTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.setFieldDateFormat("LQRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_SJ_XGZL_LQ");
	}

	public void setGc_sj_xgzl_lq_id(String gc_sj_xgzl_lq_id){
		this.setInternal("GC_SJ_XGZL_LQ_ID",gc_sj_xgzl_lq_id);
	}
	public String getGc_sj_xgzl_lq_id(){
		return (String)this.getInternal("GC_SJ_XGZL_LQ_ID");
	}
	public void setZlljsd(String zlljsd){
		this.setInternal("ZLLJSD",zlljsd);
	}
	public String getZlljsd(){
		return (String)this.getInternal("ZLLJSD");
	}
	public void setZllb(String zllb){
		this.setInternal("ZLLB",zllb);
	}
	public String getZllb(){
		return (String)this.getInternal("ZLLB");
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
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setFs(String fs){
		this.setInternal("FS",fs);
	}
	public String getFs(){
		return (String)this.getInternal("FS");
	}
	public void setLqbm(String lqbm){
		this.setInternal("LQBM",lqbm);
	}
	public String getLqbm(){
		return (String)this.getInternal("LQBM");
	}
	public void setLqrq(Date lqrq){
		this.setInternal("LQRQ",lqrq);
	}
	public Date getLqrq(){
		return (Date)this.getInternal("LQRQ");
	}
	public void setLqr(String lqr){
		this.setInternal("LQR",lqr);
	}
	public String getLqr(){
		return (String)this.getInternal("LQR");
	}
	public void setBlr(String blr){
		this.setInternal("BLR",blr);
	}
	public String getBlr(){
		return (String)this.getInternal("BLR");
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