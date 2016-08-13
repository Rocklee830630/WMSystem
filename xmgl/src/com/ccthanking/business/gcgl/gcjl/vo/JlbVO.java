package com.ccthanking.business.gcgl.gcjl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class JlbVO extends BaseVO{

	public JlbVO(){
		this.addField("GC_XMGLGS_JLB_ID",OP_STRING|this.TP_PK);
		this.addField("XMID",OP_STRING);
		this.addField("HTID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("JLYF",OP_DATE);
		this.addField("DYJLSDZ",OP_STRING);
		this.addField("DYZBJ",OP_STRING);
		this.addField("DYYFK",OP_STRING);
		this.addField("LJDYYFK",OP_STRING);
		this.addField("LJJLSDZ",OP_STRING);
		this.addField("LJZBJ",OP_STRING);
		this.addField("JZJLZ",OP_STRING);
		this.addField("LJJLZ",OP_STRING);
		this.addField("WGBFB",OP_STRING);
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
		this.addField("XMGLGS",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("SJDW",OP_STRING);
		this.addField("SGDW",OP_STRING);
		this.addField("JLDW",OP_STRING);
		this.addField("GCK_DY",OP_STRING);
		this.addField("GCK1_DY",OP_STRING);
		this.addField("XJ_DY",OP_STRING);
		this.addField("GCK_LJ",OP_STRING);
		this.addField("GCK1_LJ",OP_STRING);
		this.addField("XJ_LJ",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("BDMC",OP_STRING);
		this.addField("TCJH_SJ_ID",OP_STRING);
		this.addField("HTJ",OP_STRING);
		this.addField("XMSX",OP_STRING);
		this.addField("XMXZ",OP_STRING);
		
		//字典
		//this.bindFieldToDic("JLYF", "YF");
		this.bindFieldToDic("XMSX", "XMSX");
		this.bindFieldToDic("XMXZ", "XMXZ");
		
		//表选
		this.bindFieldToTranslater("XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");
		this.bindFieldToTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
		this.bindFieldToTranslater("ID", "GC_HTGL_HTSJ", "ID", "HTID");
		this.bindFieldToTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
		 
		this.bindFieldToTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		
		//金额
		this.bindFieldToThousand("DYJLSDZ"); 
		this.bindFieldToThousand("DYZBJ"); 
		this.bindFieldToThousand("DYYFK"); 
		this.bindFieldToThousand("LJDYYFK"); 
		this.bindFieldToThousand("LJJLSDZ"); 
		this.bindFieldToThousand("LJZBJ"); 
		this.bindFieldToThousand("JZJLZ"); 
		this.bindFieldToThousand("LJJLZ"); 
		this.bindFieldToThousand("GCK_DY"); 
		this.bindFieldToThousand("GCK1_DY"); 
		this.bindFieldToThousand("XJ_DY"); 
		this.bindFieldToThousand("GCK_LJ"); 
		this.bindFieldToThousand("GCK1_LJ"); 
		this.bindFieldToThousand("XJ_LJ"); 
		this.bindFieldToThousand("HTJ"); 
		
		this.setFieldDateFormat("JLYF","yyyy-MM");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_XMGLGS_JLB");
	}

	public void setGc_xmglgs_jlb_id(String gc_xmglgs_jlb_id){
		this.setInternal("GC_XMGLGS_JLB_ID",gc_xmglgs_jlb_id);
	}
	public String getGc_xmglgs_jlb_id(){
		return (String)this.getInternal("GC_XMGLGS_JLB_ID");
	}
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setHtid(String htid){
		this.setInternal("HTID",htid);
	}
	public String getHtid(){
		return (String)this.getInternal("HTID");
	}
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setJlyf(Date jlyf){
		this.setInternal("JLYF",jlyf);
	}
	public Date getJlyf(){
		return (Date)this.getInternal("JLYF");
	}
	public void setDyjlsdz(String dyjlsdz){
		this.setInternal("DYJLSDZ",dyjlsdz);
	}
	public String getDyjlsdz(){
		return (String)this.getInternal("DYJLSDZ");
	}
	public void setDyzbj(String dyzbj){
		this.setInternal("DYZBJ",dyzbj);
	}
	public String getDyzbj(){
		return (String)this.getInternal("DYZBJ");
	}
	public void setDyyfk(String dyyfk){
		this.setInternal("DYYFK",dyyfk);
	}
	public String getDyyfk(){
		return (String)this.getInternal("DYYFK");
	}
	public void setLjdyyfk(String ljdyyfk){
		this.setInternal("LJDYYFK",ljdyyfk);
	}
	public String getLjdyyfk(){
		return (String)this.getInternal("LJDYYFK");
	}
	public void setLjjlsdz(String ljjlsdz){
		this.setInternal("LJJLSDZ",ljjlsdz);
	}
	public String getLjjlsdz(){
		return (String)this.getInternal("LJJLSDZ");
	}
	public void setLjzbj(String ljzbj){
		this.setInternal("LJZBJ",ljzbj);
	}
	public String getLjzbj(){
		return (String)this.getInternal("LJZBJ");
	}
	public void setJzjlz(String jzjlz){
		this.setInternal("JZJLZ",jzjlz);
	}
	public String getJzjlz(){
		return (String)this.getInternal("JZJLZ");
	}
	public void setLjjlz(String ljjlz){
		this.setInternal("LJJLZ",ljjlz);
	}
	public String getLjjlz(){
		return (String)this.getInternal("LJJLZ");
	}
	public void setWgbfb(String wgbfb){
		this.setInternal("WGBFB",wgbfb);
	}
	public String getWgbfb(){
		return (String)this.getInternal("WGBFB");
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
	public void setXmglgs(String xmglgs){
		this.setInternal("XMGLGS",xmglgs);
	}
	public String getXmglgs(){
		return (String)this.getInternal("XMGLGS");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setSjdw(String sjdw){
		this.setInternal("SJDW",sjdw);
	}
	public String getSjdw(){
		return (String)this.getInternal("SJDW");
	}
	public void setSgdw(String sgdw){
		this.setInternal("SGDW",sgdw);
	}
	public String getSgdw(){
		return (String)this.getInternal("SGDW");
	}
	public void setJldw(String jldw){
		this.setInternal("JLDW",jldw);
	}
	public String getJldw(){
		return (String)this.getInternal("JLDW");
	}
	public void setGck_dy(String gck_dy){
		this.setInternal("GCK_DY",gck_dy);
	}
	public String getGck_dy(){
		return (String)this.getInternal("GCK_DY");
	}
	public void setGck1_dy(String gck1_dy){
		this.setInternal("GCK1_DY",gck1_dy);
	}
	public String getGck1_dy(){
		return (String)this.getInternal("GCK1_DY");
	}
	public void setXj_dy(String xj_dy){
		this.setInternal("XJ_DY",xj_dy);
	}
	public String getXj_dy(){
		return (String)this.getInternal("XJ_DY");
	}
	public void setGck_lj(String gck_lj){
		this.setInternal("GCK_LJ",gck_lj);
	}
	public String getGck_lj(){
		return (String)this.getInternal("GCK_LJ");
	}
	public void setGck1_lj(String gck1_lj){
		this.setInternal("GCK1_LJ",gck1_lj);
	}
	public String getGck1_lj(){
		return (String)this.getInternal("GCK1_LJ");
	}
	public void setXj_lj(String xj_lj){
		this.setInternal("XJ_LJ",xj_lj);
	}
	public String getXj_lj(){
		return (String)this.getInternal("XJ_LJ");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setBdmc(String bdmc){
		this.setInternal("BDMC",bdmc);
	}
	public String getBdmc(){
		return (String)this.getInternal("BDMC");
	}
	public void setTcjh_sj_id(String tcjh_sj_id){
		this.setInternal("TCJH_SJ_ID",tcjh_sj_id);
	}
	public String getTcjh_sj_id(){
		return (String)this.getInternal("TCJH_SJ_ID");
	}
	public void setHtj(String htj){
		this.setInternal("HTJ",htj);
	}
	public String getHtj(){
		return (String)this.getInternal("HTJ");
	}
	public void setXmsx(String xmsx){
		this.setInternal("XMSX",xmsx);
	}
	public String getXmsx(){
		return (String)this.getInternal("XMSX");
	}
	public void setXmxz(String xmxz){
		this.setInternal("XMXZ",xmxz);
	}
	public String getXmxz(){
		return (String)this.getInternal("XMXZ");
	}
}