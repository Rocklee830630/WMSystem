package com.ccthanking.business.sjgl.sj;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcSjVO extends BaseVO{

	public GcSjVO(){
		this.addField("GC_SJ_ID",OP_STRING|this.TP_PK);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("WCSJ_CQT",OP_DATE);
		this.addField("WCSJ_PQT",OP_DATE);
		this.addField("WCSJ_SGT_SSB",OP_DATE);
		this.addField("WCSJ_SGT_ZSB",OP_DATE);
		this.addField("WCSJ_KCBG",OP_DATE);
		this.addField("WCSJ_YS",OP_DATE);
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
		this.addField("SJWYBH",OP_STRING);
		this.setFieldDateFormat("WCSJ_CQT","yyyy-MM-dd");
		this.setFieldDateFormat("WCSJ_PQT","yyyy-MM-dd");
		this.setFieldDateFormat("WCSJ_SGT_SSB","yyyy-MM-dd");
		this.setFieldDateFormat("WCSJ_SGT_ZSB","yyyy-MM-dd");
		this.setFieldDateFormat("WCSJ_KCBG","yyyy-MM-dd");
		this.setFieldDateFormat("WCSJ_YS","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_SJ");
	}

	public void setGc_sj_id(String gc_sj_id){
		this.setInternal("GC_SJ_ID",gc_sj_id);
	}
	public String getGc_sj_id(){
		return (String)this.getInternal("GC_SJ_ID");
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
	public void setWcsj_cqt(Date wcsj_cqt){
		this.setInternal("WCSJ_CQT",wcsj_cqt);
	}
	public Date getWcsj_cqt(){
		return (Date)this.getInternal("WCSJ_CQT");
	}
	public void setWcsj_pqt(Date wcsj_pqt){
		this.setInternal("WCSJ_PQT",wcsj_pqt);
	}
	public Date getWcsj_pqt(){
		return (Date)this.getInternal("WCSJ_PQT");
	}
	public void setWcsj_sgt_ssb(Date wcsj_sgt_ssb){
		this.setInternal("WCSJ_SGT_SSB",wcsj_sgt_ssb);
	}
	public Date getWcsj_sgt_ssb(){
		return (Date)this.getInternal("WCSJ_SGT_SSB");
	}
	public void setWcsj_sgt_zsb(Date wcsj_sgt_zsb){
		this.setInternal("WCSJ_SGT_ZSB",wcsj_sgt_zsb);
	}
	public Date getWcsj_sgt_zsb(){
		return (Date)this.getInternal("WCSJ_SGT_ZSB");
	}
	public void setWcsj_kcbg(Date wcsj_kcbg){
		this.setInternal("WCSJ_KCBG",wcsj_kcbg);
	}
	public Date getWcsj_kcbg(){
		return (Date)this.getInternal("WCSJ_KCBG");
	}
	public void setWcsj_ys(Date wcsj_ys){
		this.setInternal("WCSJ_YS",wcsj_ys);
	}
	public Date getWcsj_ys(){
		return (Date)this.getInternal("WCSJ_YS");
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
	public void setSjwybh(String sjwybh){
		this.setInternal("SJWYBH",sjwybh);
	}
	public String getSjwybh(){
		return (String)this.getInternal("SJWYBH");
	}
}