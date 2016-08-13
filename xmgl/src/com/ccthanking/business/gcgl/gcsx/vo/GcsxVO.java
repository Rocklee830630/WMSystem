package com.ccthanking.business.gcgl.gcsx.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcsxVO extends BaseVO{

	public GcsxVO(){
		this.addField("GC_GCGL_GCSX_ID",OP_STRING|this.TP_PK);
		this.addField("BDID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("SFSJPQ",OP_STRING);
		this.addField("FQLX",OP_STRING);
		this.addField("XMGLGS",OP_STRING);
		this.addField("XMGLGSYZDB",OP_STRING);
		this.addField("XMGLGSYZDBLXFS",OP_STRING);
		this.addField("GCBYZDB",OP_STRING);
		this.addField("GCBYZDBLXFS",OP_STRING);
		this.addField("GCKGRQ",OP_DATE);
		this.addField("SXSQRQ",OP_DATE);
		this.addField("SXSQNR",OP_STRING);
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

		this.addField("SXMC",OP_STRING);
		this.setFieldDateFormat("GCKGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("SXSQRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_GCGL_GCSX");
	}

	public void setGc_gcgl_gcsx_id(String gc_gcgl_gcsx_id){
		this.setInternal("GC_GCGL_GCSX_ID",gc_gcgl_gcsx_id);
	}
	public String getGc_gcgl_gcsx_id(){
		return (String)this.getInternal("GC_GCGL_GCSX_ID");
	}

	public void setSxmc(String sxmc){
		this.setInternal("SXMC",sxmc);
	}
	public String getSxmc(){
		return (String)this.getInternal("SXMC");
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
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setSfsjpq(String sfsjpq){
		this.setInternal("SFSJPQ",sfsjpq);
	}
	public String getSfsjpq(){
		return (String)this.getInternal("SFSJPQ");
	}
	public void setFqlx(String fqlx){
		this.setInternal("FQLX",fqlx);
	}
	public String getFqlx(){
		return (String)this.getInternal("FQLX");
	}
	public void setXmglgs(String xmglgs){
		this.setInternal("XMGLGS",xmglgs);
	}
	public String getXmglgs(){
		return (String)this.getInternal("XMGLGS");
	}
	public void setXmglgsyzdb(String xmglgsyzdb){
		this.setInternal("XMGLGSYZDB",xmglgsyzdb);
	}
	public String getXmglgsyzdb(){
		return (String)this.getInternal("XMGLGSYZDB");
	}
	public void setXmglgsyzdblxfs(String xmglgsyzdblxfs){
		this.setInternal("XMGLGSYZDBLXFS",xmglgsyzdblxfs);
	}
	public String getXmglgsyzdblxfs(){
		return (String)this.getInternal("XMGLGSYZDBLXFS");
	}
	public void setGcbyzdb(String gcbyzdb){
		this.setInternal("GCBYZDB",gcbyzdb);
	}
	public String getGcbyzdb(){
		return (String)this.getInternal("GCBYZDB");
	}
	public void setGcbyzdblxfs(String gcbyzdblxfs){
		this.setInternal("GCBYZDBLXFS",gcbyzdblxfs);
	}
	public String getGcbyzdblxfs(){
		return (String)this.getInternal("GCBYZDBLXFS");
	}
	public void setGckgrq(Date gckgrq){
		this.setInternal("GCKGRQ",gckgrq);
	}
	public Date getGckgrq(){
		return (Date)this.getInternal("GCKGRQ");
	}
	public void setSxsqrq(Date sxsqrq){
		this.setInternal("SXSQRQ",sxsqrq);
	}
	public Date getSxsqrq(){
		return (Date)this.getInternal("SXSQRQ");
	}
	public void setSxsqnr(String sxsqnr){
		this.setInternal("SXSQNR",sxsqnr);
	}
	public String getSxsqnr(){
		return (String)this.getInternal("SXSQNR");
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