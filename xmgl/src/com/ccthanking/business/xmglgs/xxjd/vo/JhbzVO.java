package com.ccthanking.business.xmglgs.xxjd.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class JhbzVO extends BaseVO{

	public JhbzVO(){
		this.addField("GC_XMGLGS_XXJD_JHBZ_ID",OP_STRING|this.TP_PK);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("XMBH",OP_STRING);
		this.addField("SJDW",OP_STRING);
		this.addField("JLDW",OP_STRING);
		this.addField("SGDW",OP_STRING);
		this.addField("ZJ",OP_DATE);
		this.addField("ZJ_SJ",OP_DATE);
		this.addField("ZJ_WT",OP_STRING);
		this.addField("CT",OP_DATE);
		this.addField("CT_SJ",OP_DATE);
		this.addField("CT_WT",OP_STRING);
		this.addField("DZ",OP_DATE);
		this.addField("DZ_SJ",OP_DATE);
		this.addField("DZ_WT",OP_STRING);
		this.addField("ZXL",OP_DATE);
		this.addField("ZXL_SJ",OP_DATE);
		this.addField("ZXL_WT",OP_STRING);
		this.addField("ZD",OP_DATE);
		this.addField("ZD_SJ",OP_DATE);
		this.addField("ZD_WT",OP_STRING);
		this.addField("QTFS",OP_DATE);
		this.addField("QTFS_SJ",OP_DATE);
		this.addField("QTFS_WT",OP_STRING);
		this.addField("TFHT",OP_DATE);
		this.addField("TFHT_SJ",OP_DATE);
		this.addField("TFHT_WT",OP_STRING);
		this.addField("DNSSAZ",OP_DATE);
		this.addField("DNSSAZ_SJ",OP_DATE);
		this.addField("DNSSAZ_WT",OP_STRING);
		this.addField("DNLQPZC",OP_DATE);
		this.addField("DNLQPZC_SJ",OP_DATE);
		this.addField("DNLQPZC_WT",OP_STRING);
		this.addField("DNFSSSJZX",OP_DATE);
		this.addField("DNFSSSJZX_SJ",OP_DATE);
		this.addField("DNFSSSJZX_WT",OP_STRING);
		this.addField("FDJC",OP_DATE);
		this.addField("FDJC_SJ",OP_DATE);
		this.addField("FDJC_WT",OP_STRING);
		this.addField("FDMC",OP_DATE);
		this.addField("FDMC_SJ",OP_DATE);
		this.addField("FDMC_WT",OP_STRING);
		this.addField("FZDDLFS",OP_DATE);
		this.addField("FZDDLFS_SJ",OP_DATE);
		this.addField("FZDDLFS_WT",OP_STRING);
		this.addField("WHZJ",OP_DATE);
		this.addField("WHZJ_SJ",OP_DATE);
		this.addField("WHZJ_WT",OP_STRING);
		this.addField("ZSWM",OP_DATE);
		this.addField("ZSWM_SJ",OP_DATE);
		this.addField("ZSWM_WT",OP_STRING);
		this.addField("GLJHNTZC",OP_DATE);
		this.addField("GLJHNTZC_SJ",OP_DATE);
		this.addField("GLJHNTZC_WT",OP_STRING);
		this.addField("TFKW",OP_DATE);
		this.addField("TFKW_SJ",OP_DATE);
		this.addField("TFKW_WT",OP_STRING);
		this.addField("ZTDB",OP_DATE);
		this.addField("ZTDB_SJ",OP_DATE);
		this.addField("ZTDB_WT",OP_STRING);
		this.addField("ZTBQJCQ",OP_DATE);
		this.addField("ZTBQJCQ_SJ",OP_DATE);
		this.addField("ZTBQJCQ_WT",OP_STRING);
		this.addField("ZTDCB",OP_DATE);
		this.addField("ZTDCB_SJ",OP_DATE);
		this.addField("ZTDCB_WT",OP_STRING);
		this.addField("ZDYJDSM",OP_STRING);
		this.addField("CZZYWT",OP_STRING);
		this.addField("XXJDID",OP_STRING);
		this.addField("ZT_BZ",OP_STRING);
		this.addField("ZT_FK",OP_STRING);
		this.addField("FKRQ",OP_DATE);
		this.addField("FKRID",OP_STRING);
		this.addField("FKRXM",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
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
		this.addField("JHKFID",OP_STRING);
		this.addField("JD1",OP_DATE);
		this.addField("JD1_SJ",OP_DATE);
		this.addField("JD1_WT",OP_STRING);
		this.addField("JD2",OP_DATE);
		this.addField("JD2_SJ",OP_DATE);
		this.addField("JD2_WT",OP_STRING);
		this.addField("JD3",OP_DATE);
		this.addField("JD3_SJ",OP_DATE);
		this.addField("JD3_WT",OP_STRING);
		this.addField("JD4",OP_DATE);
		this.addField("JD4_SJ",OP_DATE);
		this.addField("JD4_WT",OP_STRING);
		this.addField("JD5",OP_DATE);
		this.addField("JD5_SJ",OP_DATE);
		this.addField("JD5_WT",OP_STRING);
		this.addField("JD6",OP_DATE);
		this.addField("JD6_SJ",OP_DATE);
		this.addField("JD6_WT",OP_STRING);
		this.addField("TJJSWCSJ",OP_DATE);
		this.addField("TJSJWCSJ",OP_DATE);
		this.addField("TJYWYY",OP_STRING);
		this.addField("JCJSWCSJ",OP_DATE);
		this.addField("JCSJWCSJ",OP_DATE);
		this.addField("JCYWYY",OP_STRING);
		this.addField("MCJSWCSJ",OP_DATE);
		this.addField("MCSJWCSJ",OP_DATE);
		this.addField("MCYWYY",OP_STRING);
		this.addField("FZJSWCSJ",OP_DATE);
		this.addField("FZSJWCSJ",OP_DATE);
		this.addField("FZYWYY",OP_STRING);
		this.addField("ZJCJSWCSJ",OP_DATE);
		this.addField("ZJCSJWCSJ",OP_DATE);
		this.addField("ZJCYWYY",OP_STRING);
		this.addField("DZJSWCSJ",OP_DATE);
		this.addField("DZSJWCSJ",OP_DATE);
		this.addField("DZYWYY",OP_STRING);
		this.addField("QLSBJSWCSJ",OP_DATE);
		this.addField("QLSBSJWCSJ",OP_DATE);
		this.addField("QLSBYWYY",OP_STRING);
		this.addField("QLFSJSWCSJ",OP_DATE);
		this.addField("QLFSSJWCSJ",OP_DATE);
		this.addField("QLFSYWYY",OP_STRING);
		this.addField("TFJSWCSJ",OP_DATE);
		this.addField("TFSJWCSJ",OP_DATE);
		this.addField("TFYWYY",OP_STRING);
		this.addField("JGJSWCSJ",OP_DATE);
		this.addField("JGSJWCSJ",OP_DATE);
		this.addField("JGYWYY",OP_STRING);
		this.addField("FSJSWCSJ",OP_DATE);
		this.addField("FSSJWCSJ",OP_DATE);
		this.addField("FSYWYY",OP_STRING);
		this.addField("XMLX",OP_STRING);
		this.addField("DLJHKGSJ",OP_DATE);
		this.addField("DLSJKGSJ",OP_DATE);
		this.addField("DLKGYWYY",OP_STRING);
		this.addField("DLJHWGSJ",OP_DATE);
		this.addField("DLSJWGSJ",OP_DATE);
		this.addField("DLWGYWYY",OP_STRING);
		this.addField("QLJHKGSJ",OP_DATE);
		this.addField("QLSJKGSJ",OP_DATE);
		this.addField("QLKGYWYY",OP_STRING);
		this.addField("QLJHWGSJ",OP_DATE);
		this.addField("QLSJWGSJ",OP_DATE);
		this.addField("QLWGYWYY",OP_STRING);
		this.addField("PSJHKGSJ",OP_DATE);
		this.addField("PSSJKGSJ",OP_DATE);
		this.addField("PSKGYWYY",OP_STRING);
		this.addField("PSJHWGSJ",OP_DATE);
		this.addField("PSSJWGSJ",OP_DATE);
		this.addField("PSWGYWYY",OP_STRING);
		this.addField("KGJHKGSJ",OP_DATE);
		this.addField("KGSJKGSJ",OP_DATE);
		this.addField("KGKGYWYY",OP_STRING);
		this.addField("KGJHWGSJ",OP_DATE);
		this.addField("KGSJWGSJ",OP_DATE);
		this.addField("KGWGYWYY",OP_STRING);
		this.addField("TJJHKGSJ",OP_DATE);
		this.addField("TJSJKGSJ",OP_DATE);
		this.addField("TJKGYWYY",OP_STRING);
		this.addField("JCJHKGSJ",OP_DATE);
		this.addField("JCSJKGSJ",OP_DATE);
		this.addField("JCKGYWYY",OP_STRING);
		this.addField("MCJHKGSJ",OP_DATE);
		this.addField("MCSJKGSJ",OP_DATE);
		this.addField("MCKGYWYY",OP_STRING);
		this.addField("FZJHKGSJ",OP_DATE);
		this.addField("FZSJKGSJ",OP_DATE);
		this.addField("FZKGYWYY",OP_STRING);
		this.addField("ZJJHKGSJ",OP_DATE);
		this.addField("ZJSJKGSJ",OP_DATE);
		this.addField("ZJKGYWYY",OP_STRING);
		this.addField("CTJJZJHKGSJ",OP_DATE);
		this.addField("CTJJZSJKGSJ",OP_DATE);
		this.addField("CTJJZKGYWYY",OP_STRING);
		this.addField("QLSBJGJHKGSJ",OP_DATE);
		this.addField("QLSBJGSJKGSJ",OP_DATE);
		this.addField("QLSBJGKGYWYY",OP_STRING);
		this.addField("QLFSJHKGSJ",OP_DATE);
		this.addField("QLFSSJKGSJ",OP_DATE);
		this.addField("QLFSKGYWYY",OP_STRING);
		this.addField("TFJHKGSJ",OP_DATE);
		this.addField("TFSJKGSJ",OP_DATE);
		this.addField("TFKGYWYY",OP_STRING);
		this.addField("JGJHKGSJ",OP_DATE);
		this.addField("JGSJKGSJ",OP_DATE);
		this.addField("JGKGYWYY",OP_STRING);
		this.addField("FSJHKGSJ",OP_DATE);
		this.addField("FSSJKGSJ",OP_DATE);
		this.addField("FSKGYWYY",OP_STRING);
		this.setFieldDateFormat("ZJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZJ_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("CT","yyyy-MM-dd");
		this.setFieldDateFormat("CT_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("DZ","yyyy-MM-dd");
		this.setFieldDateFormat("DZ_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZXL","yyyy-MM-dd");
		this.setFieldDateFormat("ZXL_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZD","yyyy-MM-dd");
		this.setFieldDateFormat("ZD_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("QTFS","yyyy-MM-dd");
		this.setFieldDateFormat("QTFS_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("TFHT","yyyy-MM-dd");
		this.setFieldDateFormat("TFHT_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("DNSSAZ","yyyy-MM-dd");
		this.setFieldDateFormat("DNSSAZ_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("DNLQPZC","yyyy-MM-dd");
		this.setFieldDateFormat("DNLQPZC_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("DNFSSSJZX","yyyy-MM-dd");
		this.setFieldDateFormat("DNFSSSJZX_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("FDJC","yyyy-MM-dd");
		this.setFieldDateFormat("FDJC_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("FDMC","yyyy-MM-dd");
		this.setFieldDateFormat("FDMC_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("FZDDLFS","yyyy-MM-dd");
		this.setFieldDateFormat("FZDDLFS_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("WHZJ","yyyy-MM-dd");
		this.setFieldDateFormat("WHZJ_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZSWM","yyyy-MM-dd");
		this.setFieldDateFormat("ZSWM_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("GLJHNTZC","yyyy-MM-dd");
		this.setFieldDateFormat("GLJHNTZC_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("TFKW","yyyy-MM-dd");
		this.setFieldDateFormat("TFKW_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZTDB","yyyy-MM-dd");
		this.setFieldDateFormat("ZTDB_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZTBQJCQ","yyyy-MM-dd");
		this.setFieldDateFormat("ZTBQJCQ_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZTDCB","yyyy-MM-dd");
		this.setFieldDateFormat("ZTDCB_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("FKRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JD1","yyyy-MM-dd");
		this.setFieldDateFormat("JD1_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("JD2","yyyy-MM-dd");
		this.setFieldDateFormat("JD2_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("JD3","yyyy-MM-dd");
		this.setFieldDateFormat("JD3_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("JD4","yyyy-MM-dd");
		this.setFieldDateFormat("JD4_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("JD5","yyyy-MM-dd");
		this.setFieldDateFormat("JD5_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("JD6","yyyy-MM-dd");
		this.setFieldDateFormat("JD6_SJ","yyyy-MM-dd");
		this.setFieldDateFormat("TJJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TJSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JCJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JCSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("MCJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("MCSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FZJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FZSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZJCJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZJCSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("DZJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("DZSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLSBJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLSBSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLFSJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLFSSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TFJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TFSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JGJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JGSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FSJSWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FSSJWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("DLJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("DLSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("DLJHWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("DLSJWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLJHWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLSJWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("PSJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("PSSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("PSJHWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("PSSJWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("KGJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("KGSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("KGJHWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("KGSJWGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TJJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TJSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JCJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JCSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("MCJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("MCSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FZJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FZSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZJJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZJSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("CTJJZJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("CTJJZSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLSBJGJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLSBJGSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLFSJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QLFSSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TFJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TFSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JGJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JGSJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FSJHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("FSSJKGSJ","yyyy-MM-dd");
		this.setVOTableName("GC_XMGLGS_XXJD_JHBZ");
	}

	public void setGc_xmglgs_xxjd_jhbz_id(String gc_xmglgs_xxjd_jhbz_id){
		this.setInternal("GC_XMGLGS_XXJD_JHBZ_ID",gc_xmglgs_xxjd_jhbz_id);
	}
	public String getGc_xmglgs_xxjd_jhbz_id(){
		return (String)this.getInternal("GC_XMGLGS_XXJD_JHBZ_ID");
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
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setXmbh(String xmbh){
		this.setInternal("XMBH",xmbh);
	}
	public String getXmbh(){
		return (String)this.getInternal("XMBH");
	}
	public void setSjdw(String sjdw){
		this.setInternal("SJDW",sjdw);
	}
	public String getSjdw(){
		return (String)this.getInternal("SJDW");
	}
	public void setJldw(String jldw){
		this.setInternal("JLDW",jldw);
	}
	public String getJldw(){
		return (String)this.getInternal("JLDW");
	}
	public void setSgdw(String sgdw){
		this.setInternal("SGDW",sgdw);
	}
	public String getSgdw(){
		return (String)this.getInternal("SGDW");
	}
	public void setZj(Date zj){
		this.setInternal("ZJ",zj);
	}
	public Date getZj(){
		return (Date)this.getInternal("ZJ");
	}
	public void setZj_sj(Date zj_sj){
		this.setInternal("ZJ_SJ",zj_sj);
	}
	public Date getZj_sj(){
		return (Date)this.getInternal("ZJ_SJ");
	}
	public void setZj_wt(String zj_wt){
		this.setInternal("ZJ_WT",zj_wt);
	}
	public String getZj_wt(){
		return (String)this.getInternal("ZJ_WT");
	}
	public void setCt(Date ct){
		this.setInternal("CT",ct);
	}
	public Date getCt(){
		return (Date)this.getInternal("CT");
	}
	public void setCt_sj(Date ct_sj){
		this.setInternal("CT_SJ",ct_sj);
	}
	public Date getCt_sj(){
		return (Date)this.getInternal("CT_SJ");
	}
	public void setCt_wt(String ct_wt){
		this.setInternal("CT_WT",ct_wt);
	}
	public String getCt_wt(){
		return (String)this.getInternal("CT_WT");
	}
	public void setDz(Date dz){
		this.setInternal("DZ",dz);
	}
	public Date getDz(){
		return (Date)this.getInternal("DZ");
	}
	public void setDz_sj(Date dz_sj){
		this.setInternal("DZ_SJ",dz_sj);
	}
	public Date getDz_sj(){
		return (Date)this.getInternal("DZ_SJ");
	}
	public void setDz_wt(String dz_wt){
		this.setInternal("DZ_WT",dz_wt);
	}
	public String getDz_wt(){
		return (String)this.getInternal("DZ_WT");
	}
	public void setZxl(Date zxl){
		this.setInternal("ZXL",zxl);
	}
	public Date getZxl(){
		return (Date)this.getInternal("ZXL");
	}
	public void setZxl_sj(Date zxl_sj){
		this.setInternal("ZXL_SJ",zxl_sj);
	}
	public Date getZxl_sj(){
		return (Date)this.getInternal("ZXL_SJ");
	}
	public void setZxl_wt(String zxl_wt){
		this.setInternal("ZXL_WT",zxl_wt);
	}
	public String getZxl_wt(){
		return (String)this.getInternal("ZXL_WT");
	}
	public void setZd(Date zd){
		this.setInternal("ZD",zd);
	}
	public Date getZd(){
		return (Date)this.getInternal("ZD");
	}
	public void setZd_sj(Date zd_sj){
		this.setInternal("ZD_SJ",zd_sj);
	}
	public Date getZd_sj(){
		return (Date)this.getInternal("ZD_SJ");
	}
	public void setZd_wt(String zd_wt){
		this.setInternal("ZD_WT",zd_wt);
	}
	public String getZd_wt(){
		return (String)this.getInternal("ZD_WT");
	}
	public void setQtfs(Date qtfs){
		this.setInternal("QTFS",qtfs);
	}
	public Date getQtfs(){
		return (Date)this.getInternal("QTFS");
	}
	public void setQtfs_sj(Date qtfs_sj){
		this.setInternal("QTFS_SJ",qtfs_sj);
	}
	public Date getQtfs_sj(){
		return (Date)this.getInternal("QTFS_SJ");
	}
	public void setQtfs_wt(String qtfs_wt){
		this.setInternal("QTFS_WT",qtfs_wt);
	}
	public String getQtfs_wt(){
		return (String)this.getInternal("QTFS_WT");
	}
	public void setTfht(Date tfht){
		this.setInternal("TFHT",tfht);
	}
	public Date getTfht(){
		return (Date)this.getInternal("TFHT");
	}
	public void setTfht_sj(Date tfht_sj){
		this.setInternal("TFHT_SJ",tfht_sj);
	}
	public Date getTfht_sj(){
		return (Date)this.getInternal("TFHT_SJ");
	}
	public void setTfht_wt(String tfht_wt){
		this.setInternal("TFHT_WT",tfht_wt);
	}
	public String getTfht_wt(){
		return (String)this.getInternal("TFHT_WT");
	}
	public void setDnssaz(Date dnssaz){
		this.setInternal("DNSSAZ",dnssaz);
	}
	public Date getDnssaz(){
		return (Date)this.getInternal("DNSSAZ");
	}
	public void setDnssaz_sj(Date dnssaz_sj){
		this.setInternal("DNSSAZ_SJ",dnssaz_sj);
	}
	public Date getDnssaz_sj(){
		return (Date)this.getInternal("DNSSAZ_SJ");
	}
	public void setDnssaz_wt(String dnssaz_wt){
		this.setInternal("DNSSAZ_WT",dnssaz_wt);
	}
	public String getDnssaz_wt(){
		return (String)this.getInternal("DNSSAZ_WT");
	}
	public void setDnlqpzc(Date dnlqpzc){
		this.setInternal("DNLQPZC",dnlqpzc);
	}
	public Date getDnlqpzc(){
		return (Date)this.getInternal("DNLQPZC");
	}
	public void setDnlqpzc_sj(Date dnlqpzc_sj){
		this.setInternal("DNLQPZC_SJ",dnlqpzc_sj);
	}
	public Date getDnlqpzc_sj(){
		return (Date)this.getInternal("DNLQPZC_SJ");
	}
	public void setDnlqpzc_wt(String dnlqpzc_wt){
		this.setInternal("DNLQPZC_WT",dnlqpzc_wt);
	}
	public String getDnlqpzc_wt(){
		return (String)this.getInternal("DNLQPZC_WT");
	}
	public void setDnfsssjzx(Date dnfsssjzx){
		this.setInternal("DNFSSSJZX",dnfsssjzx);
	}
	public Date getDnfsssjzx(){
		return (Date)this.getInternal("DNFSSSJZX");
	}
	public void setDnfsssjzx_sj(Date dnfsssjzx_sj){
		this.setInternal("DNFSSSJZX_SJ",dnfsssjzx_sj);
	}
	public Date getDnfsssjzx_sj(){
		return (Date)this.getInternal("DNFSSSJZX_SJ");
	}
	public void setDnfsssjzx_wt(String dnfsssjzx_wt){
		this.setInternal("DNFSSSJZX_WT",dnfsssjzx_wt);
	}
	public String getDnfsssjzx_wt(){
		return (String)this.getInternal("DNFSSSJZX_WT");
	}
	public void setFdjc(Date fdjc){
		this.setInternal("FDJC",fdjc);
	}
	public Date getFdjc(){
		return (Date)this.getInternal("FDJC");
	}
	public void setFdjc_sj(Date fdjc_sj){
		this.setInternal("FDJC_SJ",fdjc_sj);
	}
	public Date getFdjc_sj(){
		return (Date)this.getInternal("FDJC_SJ");
	}
	public void setFdjc_wt(String fdjc_wt){
		this.setInternal("FDJC_WT",fdjc_wt);
	}
	public String getFdjc_wt(){
		return (String)this.getInternal("FDJC_WT");
	}
	public void setFdmc(Date fdmc){
		this.setInternal("FDMC",fdmc);
	}
	public Date getFdmc(){
		return (Date)this.getInternal("FDMC");
	}
	public void setFdmc_sj(Date fdmc_sj){
		this.setInternal("FDMC_SJ",fdmc_sj);
	}
	public Date getFdmc_sj(){
		return (Date)this.getInternal("FDMC_SJ");
	}
	public void setFdmc_wt(String fdmc_wt){
		this.setInternal("FDMC_WT",fdmc_wt);
	}
	public String getFdmc_wt(){
		return (String)this.getInternal("FDMC_WT");
	}
	public void setFzddlfs(Date fzddlfs){
		this.setInternal("FZDDLFS",fzddlfs);
	}
	public Date getFzddlfs(){
		return (Date)this.getInternal("FZDDLFS");
	}
	public void setFzddlfs_sj(Date fzddlfs_sj){
		this.setInternal("FZDDLFS_SJ",fzddlfs_sj);
	}
	public Date getFzddlfs_sj(){
		return (Date)this.getInternal("FZDDLFS_SJ");
	}
	public void setFzddlfs_wt(String fzddlfs_wt){
		this.setInternal("FZDDLFS_WT",fzddlfs_wt);
	}
	public String getFzddlfs_wt(){
		return (String)this.getInternal("FZDDLFS_WT");
	}
	public void setWhzj(Date whzj){
		this.setInternal("WHZJ",whzj);
	}
	public Date getWhzj(){
		return (Date)this.getInternal("WHZJ");
	}
	public void setWhzj_sj(Date whzj_sj){
		this.setInternal("WHZJ_SJ",whzj_sj);
	}
	public Date getWhzj_sj(){
		return (Date)this.getInternal("WHZJ_SJ");
	}
	public void setWhzj_wt(String whzj_wt){
		this.setInternal("WHZJ_WT",whzj_wt);
	}
	public String getWhzj_wt(){
		return (String)this.getInternal("WHZJ_WT");
	}
	public void setZswm(Date zswm){
		this.setInternal("ZSWM",zswm);
	}
	public Date getZswm(){
		return (Date)this.getInternal("ZSWM");
	}
	public void setZswm_sj(Date zswm_sj){
		this.setInternal("ZSWM_SJ",zswm_sj);
	}
	public Date getZswm_sj(){
		return (Date)this.getInternal("ZSWM_SJ");
	}
	public void setZswm_wt(String zswm_wt){
		this.setInternal("ZSWM_WT",zswm_wt);
	}
	public String getZswm_wt(){
		return (String)this.getInternal("ZSWM_WT");
	}
	public void setGljhntzc(Date gljhntzc){
		this.setInternal("GLJHNTZC",gljhntzc);
	}
	public Date getGljhntzc(){
		return (Date)this.getInternal("GLJHNTZC");
	}
	public void setGljhntzc_sj(Date gljhntzc_sj){
		this.setInternal("GLJHNTZC_SJ",gljhntzc_sj);
	}
	public Date getGljhntzc_sj(){
		return (Date)this.getInternal("GLJHNTZC_SJ");
	}
	public void setGljhntzc_wt(String gljhntzc_wt){
		this.setInternal("GLJHNTZC_WT",gljhntzc_wt);
	}
	public String getGljhntzc_wt(){
		return (String)this.getInternal("GLJHNTZC_WT");
	}
	public void setTfkw(Date tfkw){
		this.setInternal("TFKW",tfkw);
	}
	public Date getTfkw(){
		return (Date)this.getInternal("TFKW");
	}
	public void setTfkw_sj(Date tfkw_sj){
		this.setInternal("TFKW_SJ",tfkw_sj);
	}
	public Date getTfkw_sj(){
		return (Date)this.getInternal("TFKW_SJ");
	}
	public void setTfkw_wt(String tfkw_wt){
		this.setInternal("TFKW_WT",tfkw_wt);
	}
	public String getTfkw_wt(){
		return (String)this.getInternal("TFKW_WT");
	}
	public void setZtdb(Date ztdb){
		this.setInternal("ZTDB",ztdb);
	}
	public Date getZtdb(){
		return (Date)this.getInternal("ZTDB");
	}
	public void setZtdb_sj(Date ztdb_sj){
		this.setInternal("ZTDB_SJ",ztdb_sj);
	}
	public Date getZtdb_sj(){
		return (Date)this.getInternal("ZTDB_SJ");
	}
	public void setZtdb_wt(String ztdb_wt){
		this.setInternal("ZTDB_WT",ztdb_wt);
	}
	public String getZtdb_wt(){
		return (String)this.getInternal("ZTDB_WT");
	}
	public void setZtbqjcq(Date ztbqjcq){
		this.setInternal("ZTBQJCQ",ztbqjcq);
	}
	public Date getZtbqjcq(){
		return (Date)this.getInternal("ZTBQJCQ");
	}
	public void setZtbqjcq_sj(Date ztbqjcq_sj){
		this.setInternal("ZTBQJCQ_SJ",ztbqjcq_sj);
	}
	public Date getZtbqjcq_sj(){
		return (Date)this.getInternal("ZTBQJCQ_SJ");
	}
	public void setZtbqjcq_wt(String ztbqjcq_wt){
		this.setInternal("ZTBQJCQ_WT",ztbqjcq_wt);
	}
	public String getZtbqjcq_wt(){
		return (String)this.getInternal("ZTBQJCQ_WT");
	}
	public void setZtdcb(Date ztdcb){
		this.setInternal("ZTDCB",ztdcb);
	}
	public Date getZtdcb(){
		return (Date)this.getInternal("ZTDCB");
	}
	public void setZtdcb_sj(Date ztdcb_sj){
		this.setInternal("ZTDCB_SJ",ztdcb_sj);
	}
	public Date getZtdcb_sj(){
		return (Date)this.getInternal("ZTDCB_SJ");
	}
	public void setZtdcb_wt(String ztdcb_wt){
		this.setInternal("ZTDCB_WT",ztdcb_wt);
	}
	public String getZtdcb_wt(){
		return (String)this.getInternal("ZTDCB_WT");
	}
	public void setZdyjdsm(String zdyjdsm){
		this.setInternal("ZDYJDSM",zdyjdsm);
	}
	public String getZdyjdsm(){
		return (String)this.getInternal("ZDYJDSM");
	}
	public void setCzzywt(String czzywt){
		this.setInternal("CZZYWT",czzywt);
	}
	public String getCzzywt(){
		return (String)this.getInternal("CZZYWT");
	}
	public void setXxjdid(String xxjdid){
		this.setInternal("XXJDID",xxjdid);
	}
	public String getXxjdid(){
		return (String)this.getInternal("XXJDID");
	}
	public void setZt_bz(String zt_bz){
		this.setInternal("ZT_BZ",zt_bz);
	}
	public String getZt_bz(){
		return (String)this.getInternal("ZT_BZ");
	}
	public void setZt_fk(String zt_fk){
		this.setInternal("ZT_FK",zt_fk);
	}
	public String getZt_fk(){
		return (String)this.getInternal("ZT_FK");
	}
	public void setFkrq(Date fkrq){
		this.setInternal("FKRQ",fkrq);
	}
	public Date getFkrq(){
		return (Date)this.getInternal("FKRQ");
	}
	public void setFkrid(String fkrid){
		this.setInternal("FKRID",fkrid);
	}
	public String getFkrid(){
		return (String)this.getInternal("FKRID");
	}
	public void setFkrxm(String fkrxm){
		this.setInternal("FKRXM",fkrxm);
	}
	public String getFkrxm(){
		return (String)this.getInternal("FKRXM");
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
	public void setJhkfid(String jhkfid){
		this.setInternal("JHKFID",jhkfid);
	}
	public String getJhkfid(){
		return (String)this.getInternal("JHKFID");
	}
	public void setJd1(Date jd1){
		this.setInternal("JD1",jd1);
	}
	public Date getJd1(){
		return (Date)this.getInternal("JD1");
	}
	public void setJd1_sj(Date jd1_sj){
		this.setInternal("JD1_SJ",jd1_sj);
	}
	public Date getJd1_sj(){
		return (Date)this.getInternal("JD1_SJ");
	}
	public void setJd1_wt(String jd1_wt){
		this.setInternal("JD1_WT",jd1_wt);
	}
	public String getJd1_wt(){
		return (String)this.getInternal("JD1_WT");
	}
	public void setJd2(Date jd2){
		this.setInternal("JD2",jd2);
	}
	public Date getJd2(){
		return (Date)this.getInternal("JD2");
	}
	public void setJd2_sj(Date jd2_sj){
		this.setInternal("JD2_SJ",jd2_sj);
	}
	public Date getJd2_sj(){
		return (Date)this.getInternal("JD2_SJ");
	}
	public void setJd2_wt(String jd2_wt){
		this.setInternal("JD2_WT",jd2_wt);
	}
	public String getJd2_wt(){
		return (String)this.getInternal("JD2_WT");
	}
	public void setJd3(Date jd3){
		this.setInternal("JD3",jd3);
	}
	public Date getJd3(){
		return (Date)this.getInternal("JD3");
	}
	public void setJd3_sj(Date jd3_sj){
		this.setInternal("JD3_SJ",jd3_sj);
	}
	public Date getJd3_sj(){
		return (Date)this.getInternal("JD3_SJ");
	}
	public void setJd3_wt(String jd3_wt){
		this.setInternal("JD3_WT",jd3_wt);
	}
	public String getJd3_wt(){
		return (String)this.getInternal("JD3_WT");
	}
	public void setJd4(Date jd4){
		this.setInternal("JD4",jd4);
	}
	public Date getJd4(){
		return (Date)this.getInternal("JD4");
	}
	public void setJd4_sj(Date jd4_sj){
		this.setInternal("JD4_SJ",jd4_sj);
	}
	public Date getJd4_sj(){
		return (Date)this.getInternal("JD4_SJ");
	}
	public void setJd4_wt(String jd4_wt){
		this.setInternal("JD4_WT",jd4_wt);
	}
	public String getJd4_wt(){
		return (String)this.getInternal("JD4_WT");
	}
	public void setJd5(Date jd5){
		this.setInternal("JD5",jd5);
	}
	public Date getJd5(){
		return (Date)this.getInternal("JD5");
	}
	public void setJd5_sj(Date jd5_sj){
		this.setInternal("JD5_SJ",jd5_sj);
	}
	public Date getJd5_sj(){
		return (Date)this.getInternal("JD5_SJ");
	}
	public void setJd5_wt(String jd5_wt){
		this.setInternal("JD5_WT",jd5_wt);
	}
	public String getJd5_wt(){
		return (String)this.getInternal("JD5_WT");
	}
	public void setJd6(Date jd6){
		this.setInternal("JD6",jd6);
	}
	public Date getJd6(){
		return (Date)this.getInternal("JD6");
	}
	public void setJd6_sj(Date jd6_sj){
		this.setInternal("JD6_SJ",jd6_sj);
	}
	public Date getJd6_sj(){
		return (Date)this.getInternal("JD6_SJ");
	}
	public void setJd6_wt(String jd6_wt){
		this.setInternal("JD6_WT",jd6_wt);
	}
	public String getJd6_wt(){
		return (String)this.getInternal("JD6_WT");
	}
	public void setTjjswcsj(Date tjjswcsj){
		this.setInternal("TJJSWCSJ",tjjswcsj);
	}
	public Date getTjjswcsj(){
		return (Date)this.getInternal("TJJSWCSJ");
	}
	public void setTjsjwcsj(Date tjsjwcsj){
		this.setInternal("TJSJWCSJ",tjsjwcsj);
	}
	public Date getTjsjwcsj(){
		return (Date)this.getInternal("TJSJWCSJ");
	}
	public void setTjywyy(String tjywyy){
		this.setInternal("TJYWYY",tjywyy);
	}
	public String getTjywyy(){
		return (String)this.getInternal("TJYWYY");
	}
	public void setJcjswcsj(Date jcjswcsj){
		this.setInternal("JCJSWCSJ",jcjswcsj);
	}
	public Date getJcjswcsj(){
		return (Date)this.getInternal("JCJSWCSJ");
	}
	public void setJcsjwcsj(Date jcsjwcsj){
		this.setInternal("JCSJWCSJ",jcsjwcsj);
	}
	public Date getJcsjwcsj(){
		return (Date)this.getInternal("JCSJWCSJ");
	}
	public void setJcywyy(String jcywyy){
		this.setInternal("JCYWYY",jcywyy);
	}
	public String getJcywyy(){
		return (String)this.getInternal("JCYWYY");
	}
	public void setMcjswcsj(Date mcjswcsj){
		this.setInternal("MCJSWCSJ",mcjswcsj);
	}
	public Date getMcjswcsj(){
		return (Date)this.getInternal("MCJSWCSJ");
	}
	public void setMcsjwcsj(Date mcsjwcsj){
		this.setInternal("MCSJWCSJ",mcsjwcsj);
	}
	public Date getMcsjwcsj(){
		return (Date)this.getInternal("MCSJWCSJ");
	}
	public void setMcywyy(String mcywyy){
		this.setInternal("MCYWYY",mcywyy);
	}
	public String getMcywyy(){
		return (String)this.getInternal("MCYWYY");
	}
	public void setFzjswcsj(Date fzjswcsj){
		this.setInternal("FZJSWCSJ",fzjswcsj);
	}
	public Date getFzjswcsj(){
		return (Date)this.getInternal("FZJSWCSJ");
	}
	public void setFzsjwcsj(Date fzsjwcsj){
		this.setInternal("FZSJWCSJ",fzsjwcsj);
	}
	public Date getFzsjwcsj(){
		return (Date)this.getInternal("FZSJWCSJ");
	}
	public void setFzywyy(String fzywyy){
		this.setInternal("FZYWYY",fzywyy);
	}
	public String getFzywyy(){
		return (String)this.getInternal("FZYWYY");
	}
	public void setZjcjswcsj(Date zjcjswcsj){
		this.setInternal("ZJCJSWCSJ",zjcjswcsj);
	}
	public Date getZjcjswcsj(){
		return (Date)this.getInternal("ZJCJSWCSJ");
	}
	public void setZjcsjwcsj(Date zjcsjwcsj){
		this.setInternal("ZJCSJWCSJ",zjcsjwcsj);
	}
	public Date getZjcsjwcsj(){
		return (Date)this.getInternal("ZJCSJWCSJ");
	}
	public void setZjcywyy(String zjcywyy){
		this.setInternal("ZJCYWYY",zjcywyy);
	}
	public String getZjcywyy(){
		return (String)this.getInternal("ZJCYWYY");
	}
	public void setDzjswcsj(Date dzjswcsj){
		this.setInternal("DZJSWCSJ",dzjswcsj);
	}
	public Date getDzjswcsj(){
		return (Date)this.getInternal("DZJSWCSJ");
	}
	public void setDzsjwcsj(Date dzsjwcsj){
		this.setInternal("DZSJWCSJ",dzsjwcsj);
	}
	public Date getDzsjwcsj(){
		return (Date)this.getInternal("DZSJWCSJ");
	}
	public void setDzywyy(String dzywyy){
		this.setInternal("DZYWYY",dzywyy);
	}
	public String getDzywyy(){
		return (String)this.getInternal("DZYWYY");
	}
	public void setQlsbjswcsj(Date qlsbjswcsj){
		this.setInternal("QLSBJSWCSJ",qlsbjswcsj);
	}
	public Date getQlsbjswcsj(){
		return (Date)this.getInternal("QLSBJSWCSJ");
	}
	public void setQlsbsjwcsj(Date qlsbsjwcsj){
		this.setInternal("QLSBSJWCSJ",qlsbsjwcsj);
	}
	public Date getQlsbsjwcsj(){
		return (Date)this.getInternal("QLSBSJWCSJ");
	}
	public void setQlsbywyy(String qlsbywyy){
		this.setInternal("QLSBYWYY",qlsbywyy);
	}
	public String getQlsbywyy(){
		return (String)this.getInternal("QLSBYWYY");
	}
	public void setQlfsjswcsj(Date qlfsjswcsj){
		this.setInternal("QLFSJSWCSJ",qlfsjswcsj);
	}
	public Date getQlfsjswcsj(){
		return (Date)this.getInternal("QLFSJSWCSJ");
	}
	public void setQlfssjwcsj(Date qlfssjwcsj){
		this.setInternal("QLFSSJWCSJ",qlfssjwcsj);
	}
	public Date getQlfssjwcsj(){
		return (Date)this.getInternal("QLFSSJWCSJ");
	}
	public void setQlfsywyy(String qlfsywyy){
		this.setInternal("QLFSYWYY",qlfsywyy);
	}
	public String getQlfsywyy(){
		return (String)this.getInternal("QLFSYWYY");
	}
	public void setTfjswcsj(Date tfjswcsj){
		this.setInternal("TFJSWCSJ",tfjswcsj);
	}
	public Date getTfjswcsj(){
		return (Date)this.getInternal("TFJSWCSJ");
	}
	public void setTfsjwcsj(Date tfsjwcsj){
		this.setInternal("TFSJWCSJ",tfsjwcsj);
	}
	public Date getTfsjwcsj(){
		return (Date)this.getInternal("TFSJWCSJ");
	}
	public void setTfywyy(String tfywyy){
		this.setInternal("TFYWYY",tfywyy);
	}
	public String getTfywyy(){
		return (String)this.getInternal("TFYWYY");
	}
	public void setJgjswcsj(Date jgjswcsj){
		this.setInternal("JGJSWCSJ",jgjswcsj);
	}
	public Date getJgjswcsj(){
		return (Date)this.getInternal("JGJSWCSJ");
	}
	public void setJgsjwcsj(Date jgsjwcsj){
		this.setInternal("JGSJWCSJ",jgsjwcsj);
	}
	public Date getJgsjwcsj(){
		return (Date)this.getInternal("JGSJWCSJ");
	}
	public void setJgywyy(String jgywyy){
		this.setInternal("JGYWYY",jgywyy);
	}
	public String getJgywyy(){
		return (String)this.getInternal("JGYWYY");
	}
	public void setFsjswcsj(Date fsjswcsj){
		this.setInternal("FSJSWCSJ",fsjswcsj);
	}
	public Date getFsjswcsj(){
		return (Date)this.getInternal("FSJSWCSJ");
	}
	public void setFssjwcsj(Date fssjwcsj){
		this.setInternal("FSSJWCSJ",fssjwcsj);
	}
	public Date getFssjwcsj(){
		return (Date)this.getInternal("FSSJWCSJ");
	}
	public void setFsywyy(String fsywyy){
		this.setInternal("FSYWYY",fsywyy);
	}
	public String getFsywyy(){
		return (String)this.getInternal("FSYWYY");
	}
	public void setXmlx(String xmlx){
		this.setInternal("XMLX",xmlx);
	}
	public String getXmlx(){
		return (String)this.getInternal("XMLX");
	}
	public void setDljhkgsj(Date dljhkgsj){
		this.setInternal("DLJHKGSJ",dljhkgsj);
	}
	public Date getDljhkgsj(){
		return (Date)this.getInternal("DLJHKGSJ");
	}
	public void setDlsjkgsj(Date dlsjkgsj){
		this.setInternal("DLSJKGSJ",dlsjkgsj);
	}
	public Date getDlsjkgsj(){
		return (Date)this.getInternal("DLSJKGSJ");
	}
	public void setDlkgywyy(String dlkgywyy){
		this.setInternal("DLKGYWYY",dlkgywyy);
	}
	public String getDlkgywyy(){
		return (String)this.getInternal("DLKGYWYY");
	}
	public void setDljhwgsj(Date dljhwgsj){
		this.setInternal("DLJHWGSJ",dljhwgsj);
	}
	public Date getDljhwgsj(){
		return (Date)this.getInternal("DLJHWGSJ");
	}
	public void setDlsjwgsj(Date dlsjwgsj){
		this.setInternal("DLSJWGSJ",dlsjwgsj);
	}
	public Date getDlsjwgsj(){
		return (Date)this.getInternal("DLSJWGSJ");
	}
	public void setDlwgywyy(String dlwgywyy){
		this.setInternal("DLWGYWYY",dlwgywyy);
	}
	public String getDlwgywyy(){
		return (String)this.getInternal("DLWGYWYY");
	}
	public void setQljhkgsj(Date qljhkgsj){
		this.setInternal("QLJHKGSJ",qljhkgsj);
	}
	public Date getQljhkgsj(){
		return (Date)this.getInternal("QLJHKGSJ");
	}
	public void setQlsjkgsj(Date qlsjkgsj){
		this.setInternal("QLSJKGSJ",qlsjkgsj);
	}
	public Date getQlsjkgsj(){
		return (Date)this.getInternal("QLSJKGSJ");
	}
	public void setQlkgywyy(String qlkgywyy){
		this.setInternal("QLKGYWYY",qlkgywyy);
	}
	public String getQlkgywyy(){
		return (String)this.getInternal("QLKGYWYY");
	}
	public void setQljhwgsj(Date qljhwgsj){
		this.setInternal("QLJHWGSJ",qljhwgsj);
	}
	public Date getQljhwgsj(){
		return (Date)this.getInternal("QLJHWGSJ");
	}
	public void setQlsjwgsj(Date qlsjwgsj){
		this.setInternal("QLSJWGSJ",qlsjwgsj);
	}
	public Date getQlsjwgsj(){
		return (Date)this.getInternal("QLSJWGSJ");
	}
	public void setQlwgywyy(String qlwgywyy){
		this.setInternal("QLWGYWYY",qlwgywyy);
	}
	public String getQlwgywyy(){
		return (String)this.getInternal("QLWGYWYY");
	}
	public void setPsjhkgsj(Date psjhkgsj){
		this.setInternal("PSJHKGSJ",psjhkgsj);
	}
	public Date getPsjhkgsj(){
		return (Date)this.getInternal("PSJHKGSJ");
	}
	public void setPssjkgsj(Date pssjkgsj){
		this.setInternal("PSSJKGSJ",pssjkgsj);
	}
	public Date getPssjkgsj(){
		return (Date)this.getInternal("PSSJKGSJ");
	}
	public void setPskgywyy(String pskgywyy){
		this.setInternal("PSKGYWYY",pskgywyy);
	}
	public String getPskgywyy(){
		return (String)this.getInternal("PSKGYWYY");
	}
	public void setPsjhwgsj(Date psjhwgsj){
		this.setInternal("PSJHWGSJ",psjhwgsj);
	}
	public Date getPsjhwgsj(){
		return (Date)this.getInternal("PSJHWGSJ");
	}
	public void setPssjwgsj(Date pssjwgsj){
		this.setInternal("PSSJWGSJ",pssjwgsj);
	}
	public Date getPssjwgsj(){
		return (Date)this.getInternal("PSSJWGSJ");
	}
	public void setPswgywyy(String pswgywyy){
		this.setInternal("PSWGYWYY",pswgywyy);
	}
	public String getPswgywyy(){
		return (String)this.getInternal("PSWGYWYY");
	}
	public void setKgjhkgsj(Date kgjhkgsj){
		this.setInternal("KGJHKGSJ",kgjhkgsj);
	}
	public Date getKgjhkgsj(){
		return (Date)this.getInternal("KGJHKGSJ");
	}
	public void setKgsjkgsj(Date kgsjkgsj){
		this.setInternal("KGSJKGSJ",kgsjkgsj);
	}
	public Date getKgsjkgsj(){
		return (Date)this.getInternal("KGSJKGSJ");
	}
	public void setKgkgywyy(String kgkgywyy){
		this.setInternal("KGKGYWYY",kgkgywyy);
	}
	public String getKgkgywyy(){
		return (String)this.getInternal("KGKGYWYY");
	}
	public void setKgjhwgsj(Date kgjhwgsj){
		this.setInternal("KGJHWGSJ",kgjhwgsj);
	}
	public Date getKgjhwgsj(){
		return (Date)this.getInternal("KGJHWGSJ");
	}
	public void setKgsjwgsj(Date kgsjwgsj){
		this.setInternal("KGSJWGSJ",kgsjwgsj);
	}
	public Date getKgsjwgsj(){
		return (Date)this.getInternal("KGSJWGSJ");
	}
	public void setKgwgywyy(String kgwgywyy){
		this.setInternal("KGWGYWYY",kgwgywyy);
	}
	public String getKgwgywyy(){
		return (String)this.getInternal("KGWGYWYY");
	}
	public void setTjjhkgsj(Date tjjhkgsj){
		this.setInternal("TJJHKGSJ",tjjhkgsj);
	}
	public Date getTjjhkgsj(){
		return (Date)this.getInternal("TJJHKGSJ");
	}
	public void setTjsjkgsj(Date tjsjkgsj){
		this.setInternal("TJSJKGSJ",tjsjkgsj);
	}
	public Date getTjsjkgsj(){
		return (Date)this.getInternal("TJSJKGSJ");
	}
	public void setTjkgywyy(String tjkgywyy){
		this.setInternal("TJKGYWYY",tjkgywyy);
	}
	public String getTjkgywyy(){
		return (String)this.getInternal("TJKGYWYY");
	}
	public void setJcjhkgsj(Date jcjhkgsj){
		this.setInternal("JCJHKGSJ",jcjhkgsj);
	}
	public Date getJcjhkgsj(){
		return (Date)this.getInternal("JCJHKGSJ");
	}
	public void setJcsjkgsj(Date jcsjkgsj){
		this.setInternal("JCSJKGSJ",jcsjkgsj);
	}
	public Date getJcsjkgsj(){
		return (Date)this.getInternal("JCSJKGSJ");
	}
	public void setJckgywyy(String jckgywyy){
		this.setInternal("JCKGYWYY",jckgywyy);
	}
	public String getJckgywyy(){
		return (String)this.getInternal("JCKGYWYY");
	}
	public void setMcjhkgsj(Date mcjhkgsj){
		this.setInternal("MCJHKGSJ",mcjhkgsj);
	}
	public Date getMcjhkgsj(){
		return (Date)this.getInternal("MCJHKGSJ");
	}
	public void setMcsjkgsj(Date mcsjkgsj){
		this.setInternal("MCSJKGSJ",mcsjkgsj);
	}
	public Date getMcsjkgsj(){
		return (Date)this.getInternal("MCSJKGSJ");
	}
	public void setMckgywyy(String mckgywyy){
		this.setInternal("MCKGYWYY",mckgywyy);
	}
	public String getMckgywyy(){
		return (String)this.getInternal("MCKGYWYY");
	}
	public void setFzjhkgsj(Date fzjhkgsj){
		this.setInternal("FZJHKGSJ",fzjhkgsj);
	}
	public Date getFzjhkgsj(){
		return (Date)this.getInternal("FZJHKGSJ");
	}
	public void setFzsjkgsj(Date fzsjkgsj){
		this.setInternal("FZSJKGSJ",fzsjkgsj);
	}
	public Date getFzsjkgsj(){
		return (Date)this.getInternal("FZSJKGSJ");
	}
	public void setFzkgywyy(String fzkgywyy){
		this.setInternal("FZKGYWYY",fzkgywyy);
	}
	public String getFzkgywyy(){
		return (String)this.getInternal("FZKGYWYY");
	}
	public void setZjjhkgsj(Date zjjhkgsj){
		this.setInternal("ZJJHKGSJ",zjjhkgsj);
	}
	public Date getZjjhkgsj(){
		return (Date)this.getInternal("ZJJHKGSJ");
	}
	public void setZjsjkgsj(Date zjsjkgsj){
		this.setInternal("ZJSJKGSJ",zjsjkgsj);
	}
	public Date getZjsjkgsj(){
		return (Date)this.getInternal("ZJSJKGSJ");
	}
	public void setZjkgywyy(String zjkgywyy){
		this.setInternal("ZJKGYWYY",zjkgywyy);
	}
	public String getZjkgywyy(){
		return (String)this.getInternal("ZJKGYWYY");
	}
	public void setCtjjzjhkgsj(Date ctjjzjhkgsj){
		this.setInternal("CTJJZJHKGSJ",ctjjzjhkgsj);
	}
	public Date getCtjjzjhkgsj(){
		return (Date)this.getInternal("CTJJZJHKGSJ");
	}
	public void setCtjjzsjkgsj(Date ctjjzsjkgsj){
		this.setInternal("CTJJZSJKGSJ",ctjjzsjkgsj);
	}
	public Date getCtjjzsjkgsj(){
		return (Date)this.getInternal("CTJJZSJKGSJ");
	}
	public void setCtjjzkgywyy(String ctjjzkgywyy){
		this.setInternal("CTJJZKGYWYY",ctjjzkgywyy);
	}
	public String getCtjjzkgywyy(){
		return (String)this.getInternal("CTJJZKGYWYY");
	}
	public void setQlsbjgjhkgsj(Date qlsbjgjhkgsj){
		this.setInternal("QLSBJGJHKGSJ",qlsbjgjhkgsj);
	}
	public Date getQlsbjgjhkgsj(){
		return (Date)this.getInternal("QLSBJGJHKGSJ");
	}
	public void setQlsbjgsjkgsj(Date qlsbjgsjkgsj){
		this.setInternal("QLSBJGSJKGSJ",qlsbjgsjkgsj);
	}
	public Date getQlsbjgsjkgsj(){
		return (Date)this.getInternal("QLSBJGSJKGSJ");
	}
	public void setQlsbjgkgywyy(String qlsbjgkgywyy){
		this.setInternal("QLSBJGKGYWYY",qlsbjgkgywyy);
	}
	public String getQlsbjgkgywyy(){
		return (String)this.getInternal("QLSBJGKGYWYY");
	}
	public void setQlfsjhkgsj(Date qlfsjhkgsj){
		this.setInternal("QLFSJHKGSJ",qlfsjhkgsj);
	}
	public Date getQlfsjhkgsj(){
		return (Date)this.getInternal("QLFSJHKGSJ");
	}
	public void setQlfssjkgsj(Date qlfssjkgsj){
		this.setInternal("QLFSSJKGSJ",qlfssjkgsj);
	}
	public Date getQlfssjkgsj(){
		return (Date)this.getInternal("QLFSSJKGSJ");
	}
	public void setQlfskgywyy(String qlfskgywyy){
		this.setInternal("QLFSKGYWYY",qlfskgywyy);
	}
	public String getQlfskgywyy(){
		return (String)this.getInternal("QLFSKGYWYY");
	}
	public void setTfjhkgsj(Date tfjhkgsj){
		this.setInternal("TFJHKGSJ",tfjhkgsj);
	}
	public Date getTfjhkgsj(){
		return (Date)this.getInternal("TFJHKGSJ");
	}
	public void setTfsjkgsj(Date tfsjkgsj){
		this.setInternal("TFSJKGSJ",tfsjkgsj);
	}
	public Date getTfsjkgsj(){
		return (Date)this.getInternal("TFSJKGSJ");
	}
	public void setTfkgywyy(String tfkgywyy){
		this.setInternal("TFKGYWYY",tfkgywyy);
	}
	public String getTfkgywyy(){
		return (String)this.getInternal("TFKGYWYY");
	}
	public void setJgjhkgsj(Date jgjhkgsj){
		this.setInternal("JGJHKGSJ",jgjhkgsj);
	}
	public Date getJgjhkgsj(){
		return (Date)this.getInternal("JGJHKGSJ");
	}
	public void setJgsjkgsj(Date jgsjkgsj){
		this.setInternal("JGSJKGSJ",jgsjkgsj);
	}
	public Date getJgsjkgsj(){
		return (Date)this.getInternal("JGSJKGSJ");
	}
	public void setJgkgywyy(String jgkgywyy){
		this.setInternal("JGKGYWYY",jgkgywyy);
	}
	public String getJgkgywyy(){
		return (String)this.getInternal("JGKGYWYY");
	}
	public void setFsjhkgsj(Date fsjhkgsj){
		this.setInternal("FSJHKGSJ",fsjhkgsj);
	}
	public Date getFsjhkgsj(){
		return (Date)this.getInternal("FSJHKGSJ");
	}
	public void setFssjkgsj(Date fssjkgsj){
		this.setInternal("FSSJKGSJ",fssjkgsj);
	}
	public Date getFssjkgsj(){
		return (Date)this.getInternal("FSSJKGSJ");
	}
	public void setFskgywyy(String fskgywyy){
		this.setInternal("FSKGYWYY",fskgywyy);
	}
	public String getFskgywyy(){
		return (String)this.getInternal("FSKGYWYY");
	}
}