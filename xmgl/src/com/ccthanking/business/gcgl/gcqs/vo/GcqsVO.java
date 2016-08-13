package com.ccthanking.business.gcgl.gcqs.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcqsVO extends BaseVO{

	public GcqsVO(){
		this.addField("GC_GCGL_GCQS_ID",OP_STRING|this.TP_PK);
		this.addField("BDID",OP_STRING);
		this.addField("JLDWXMJL",OP_STRING);
		this.addField("QSTCRQ",OP_DATE);
		this.addField("QSYY",OP_STRING);
		this.addField("QSNR",OP_STRING);
		this.addField("BLRXM",OP_STRING);
		this.addField("BLRQ",OP_DATE);
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
		this.addField("XMID",OP_STRING);
		this.addField("BLRID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("QSBT",OP_STRING);
		this.addField("SJDWFZR",OP_STRING);
		this.addField("SJDWFZRLXFS",OP_STRING);
		this.addField("JLDWXMJLLXFS",OP_STRING);
		this.addField("SGDWXMZJ",OP_STRING);
		this.addField("SGDWXMZJLXFS",OP_STRING);
		this.addField("GSZJ",OP_STRING);
		this.addField("SFSJPQ",OP_STRING);

		this.addField("YZDB",OP_STRING);
		this.setFieldDateFormat("QSTCRQ","yyyy-MM-dd");
		this.setFieldDateFormat("BLRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_GCGL_GCQS");
	}

	public void setGc_gcgl_gcqs_id(String gc_gcgl_gcqs_id){
		this.setInternal("GC_GCGL_GCQS_ID",gc_gcgl_gcqs_id);
	}
	public String getGc_gcgl_gcqs_id(){
		return (String)this.getInternal("GC_GCGL_GCQS_ID");
	}

	public void setYzdb(String yzdb){
		this.setInternal("YZDB",yzdb);
	}
	public String getYzdb(){
		return (String)this.getInternal("YZDB");
	}
	
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setJldwxmjl(String jldwxmjl){
		this.setInternal("JLDWXMJL",jldwxmjl);
	}
	public String getJldwxmjl(){
		return (String)this.getInternal("JLDWXMJL");
	}
	public void setQstcrq(Date qstcrq){
		this.setInternal("QSTCRQ",qstcrq);
	}
	public Date getQstcrq(){
		return (Date)this.getInternal("QSTCRQ");
	}
	public void setQsyy(String qsyy){
		this.setInternal("QSYY",qsyy);
	}
	public String getQsyy(){
		return (String)this.getInternal("QSYY");
	}
	public void setQsnr(String qsnr){
		this.setInternal("QSNR",qsnr);
	}
	public String getQsnr(){
		return (String)this.getInternal("QSNR");
	}
	public void setBlrxm(String blrxm){
		this.setInternal("BLRXM",blrxm);
	}
	public String getBlrxm(){
		return (String)this.getInternal("BLRXM");
	}
	public void setBlrq(Date blrq){
		this.setInternal("BLRQ",blrq);
	}
	public Date getBlrq(){
		return (Date)this.getInternal("BLRQ");
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
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setBlrid(String blrid){
		this.setInternal("BLRID",blrid);
	}
	public String getBlrid(){
		return (String)this.getInternal("BLRID");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setQsbt(String qsbt){
		this.setInternal("QSBT",qsbt);
	}
	public String getQsbt(){
		return (String)this.getInternal("QSBT");
	}
	public void setSjdwfzr(String sjdwfzr){
		this.setInternal("SJDWFZR",sjdwfzr);
	}
	public String getSjdwfzr(){
		return (String)this.getInternal("SJDWFZR");
	}
	public void setSjdwfzrlxfs(String sjdwfzrlxfs){
		this.setInternal("SJDWFZRLXFS",sjdwfzrlxfs);
	}
	public String getSjdwfzrlxfs(){
		return (String)this.getInternal("SJDWFZRLXFS");
	}
	public void setJldwxmjllxfs(String jldwxmjllxfs){
		this.setInternal("JLDWXMJLLXFS",jldwxmjllxfs);
	}
	public String getJldwxmjllxfs(){
		return (String)this.getInternal("JLDWXMJLLXFS");
	}
	public void setSgdwxmzj(String sgdwxmzj){
		this.setInternal("SGDWXMZJ",sgdwxmzj);
	}
	public String getSgdwxmzj(){
		return (String)this.getInternal("SGDWXMZJ");
	}
	public void setSgdwxmzjlxfs(String sgdwxmzjlxfs){
		this.setInternal("SGDWXMZJLXFS",sgdwxmzjlxfs);
	}
	public String getSgdwxmzjlxfs(){
		return (String)this.getInternal("SGDWXMZJLXFS");
	}
	public void setGszj(String gszj){
		this.setInternal("GSZJ",gszj);
	}
	public String getGszj(){
		return (String)this.getInternal("GSZJ");
	}
	public void setSfsjpq(String sfsjpq){
		this.setInternal("SFSJPQ",sfsjpq);
	}
	public String getSfsjpq(){
		return (String)this.getInternal("SFSJPQ");
	}
}