package com.ccthanking.business.sjgl.sjrws;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcSjglRwsglVO extends BaseVO{

	public GcSjglRwsglVO(){
		this.addField("GC_SJGL_RWSGL_ID",OP_STRING|this.TP_PK);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("JSDW",OP_STRING);
		this.addField("FSSJ",OP_DATE);
		this.addField("JBR",OP_STRING);
		this.addField("RWSLX",OP_STRING);
		this.addField("LB",OP_STRING);
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
		this.bindFieldToTranslater("JBR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.bindFieldToTranslater("JSDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToDic("RWSLX", "RWSLX");
		this.bindFieldToDic("LB", "KCSJRWS");
		
		this.setFieldDateFormat("FSSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_SJGL_RWSGL");
	}

	public void setGc_sjgl_rwsgl_id(String gc_sjgl_rwsgl_id){
		this.setInternal("GC_SJGL_RWSGL_ID",gc_sjgl_rwsgl_id);
	}
	public String getGc_sjgl_rwsgl_id(){
		return (String)this.getInternal("GC_SJGL_RWSGL_ID");
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
	public void setJsdw(String jsdw){
		this.setInternal("JSDW",jsdw);
	}
	public String getJsdw(){
		return (String)this.getInternal("JSDW");
	}
	public void setFssj(Date fssj){
		this.setInternal("FSSJ",fssj);
	}
	public Date getFssj(){
		return (Date)this.getInternal("FSSJ");
	}
	public void setJbr(String jbr){
		this.setInternal("JBR",jbr);
	}
	public String getJbr(){
		return (String)this.getInternal("JBR");
	}
	public void setRwslx(String rwslx){
		this.setInternal("RWSLX",rwslx);
	}
	public String getRwslx(){
		return (String)this.getInternal("RWSLX");
	}
	public void setLb(String lb){
		this.setInternal("LB",lb);
	}
	public String getLb(){
		return (String)this.getInternal("LB");
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