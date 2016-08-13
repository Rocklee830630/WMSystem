package com.ccthanking.business.sjgl.gs;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcSjCbsjpfVO extends BaseVO{

	public GcSjCbsjpfVO(){
		this.addField("GC_SJ_CBSJPF_ID",OP_STRING|this.TP_PK);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("XMJYSPF",OP_DATE);
		this.addField("KXXYJBGPF",OP_DATE);
		this.addField("HPBGPF",OP_DATE);
		this.addField("YDQKH",OP_DATE);
		this.addField("XMXZYJS",OP_DATE);
		this.addField("JNDJB",OP_DATE);
		this.addField("CBSJPF",OP_DATE);
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
		this.addField("QQSXID",OP_STRING);
		this.addField("PFNR",OP_STRING);
		this.addField("GS",OP_STRING);
		this.addField("SSJS",OP_DATE);
		this.addField("SSE",OP_STRING);
		this.addField("GCJE",OP_STRING);
		this.addField("ZCJE",OP_STRING);
		this.addField("QTJE",OP_STRING);
		this.addField("YS_GCJE",OP_STRING);
		this.addField("YS_ZCJE",OP_STRING);
		this.addField("YS_QTJE",OP_STRING);
		this.addField("SJWYBH",OP_STRING);
		this.setFieldDateFormat("XMJYSPF","yyyy-MM-dd");
		this.setFieldDateFormat("KXXYJBGPF","yyyy-MM-dd");
		this.setFieldDateFormat("HPBGPF","yyyy-MM-dd");
		this.setFieldDateFormat("YDQKH","yyyy-MM-dd");
		this.setFieldDateFormat("XMXZYJS","yyyy-MM-dd");
		this.setFieldDateFormat("JNDJB","yyyy-MM-dd");
		this.setFieldDateFormat("CBSJPF","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SSJS","yyyy-MM-dd");
		this.setVOTableName("GC_SJ_CBSJPF");
	}

	public void setGc_sj_cbsjpf_id(String gc_sj_cbsjpf_id){
		this.setInternal("GC_SJ_CBSJPF_ID",gc_sj_cbsjpf_id);
	}
	public String getGc_sj_cbsjpf_id(){
		return (String)this.getInternal("GC_SJ_CBSJPF_ID");
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
	public void setXmjyspf(Date xmjyspf){
		this.setInternal("XMJYSPF",xmjyspf);
	}
	public Date getXmjyspf(){
		return (Date)this.getInternal("XMJYSPF");
	}
	public void setKxxyjbgpf(Date kxxyjbgpf){
		this.setInternal("KXXYJBGPF",kxxyjbgpf);
	}
	public Date getKxxyjbgpf(){
		return (Date)this.getInternal("KXXYJBGPF");
	}
	public void setHpbgpf(Date hpbgpf){
		this.setInternal("HPBGPF",hpbgpf);
	}
	public Date getHpbgpf(){
		return (Date)this.getInternal("HPBGPF");
	}
	public void setYdqkh(Date ydqkh){
		this.setInternal("YDQKH",ydqkh);
	}
	public Date getYdqkh(){
		return (Date)this.getInternal("YDQKH");
	}
	public void setXmxzyjs(Date xmxzyjs){
		this.setInternal("XMXZYJS",xmxzyjs);
	}
	public Date getXmxzyjs(){
		return (Date)this.getInternal("XMXZYJS");
	}
	public void setJndjb(Date jndjb){
		this.setInternal("JNDJB",jndjb);
	}
	public Date getJndjb(){
		return (Date)this.getInternal("JNDJB");
	}
	public void setCbsjpf(Date cbsjpf){
		this.setInternal("CBSJPF",cbsjpf);
	}
	public Date getCbsjpf(){
		return (Date)this.getInternal("CBSJPF");
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
	public void setQqsxid(String qqsxid){
		this.setInternal("QQSXID",qqsxid);
	}
	public String getQqsxid(){
		return (String)this.getInternal("QQSXID");
	}
	public void setPfnr(String pfnr){
		this.setInternal("PFNR",pfnr);
	}
	public String getPfnr(){
		return (String)this.getInternal("PFNR");
	}
	public void setGs(String gs){
		this.setInternal("GS",gs);
	}
	public String getGs(){
		return (String)this.getInternal("GS");
	}
	public void setSsjs(Date ssjs){
		this.setInternal("SSJS",ssjs);
	}
	public Date getSsjs(){
		return (Date)this.getInternal("SSJS");
	}
	public void setSse(String sse){
		this.setInternal("SSE",sse);
	}
	public String getSse(){
		return (String)this.getInternal("SSE");
	}
	public void setGcje(String gcje){
		this.setInternal("GCJE",gcje);
	}
	public String getGcje(){
		return (String)this.getInternal("GCJE");
	}
	public void setZcje(String zcje){
		this.setInternal("ZCJE",zcje);
	}
	public String getZcje(){
		return (String)this.getInternal("ZCJE");
	}
	public void setQtje(String qtje){
		this.setInternal("QTJE",qtje);
	}
	public String getQtje(){
		return (String)this.getInternal("QTJE");
	}
	public void setYs_gcje(String ys_gcje){
		this.setInternal("YS_GCJE",ys_gcje);
	}
	public String getYs_gcje(){
		return (String)this.getInternal("YS_GCJE");
	}
	public void setYs_zcje(String ys_zcje){
		this.setInternal("YS_ZCJE",ys_zcje);
	}
	public String getYs_zcje(){
		return (String)this.getInternal("YS_ZCJE");
	}
	public void setYs_qtje(String ys_qtje){
		this.setInternal("YS_QTJE",ys_qtje);
	}
	public String getYs_qtje(){
		return (String)this.getInternal("YS_QTJE");
	}
	public void setSjwybh(String sjwybh){
		this.setInternal("SJWYBH",sjwybh);
	}
	public String getSjwybh(){
		return (String)this.getInternal("SJWYBH");
	}
}