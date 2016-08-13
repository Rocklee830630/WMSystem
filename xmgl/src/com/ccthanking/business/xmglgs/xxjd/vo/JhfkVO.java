package com.ccthanking.business.xmglgs.xxjd.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class JhfkVO extends BaseVO{

	public JhfkVO(){
		this.addField("GC_XMGLGS_XXJD_JHFK_ID",OP_STRING|this.TP_PK);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("XMBH",OP_STRING);
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
		this.addField("JHBZID",OP_STRING);
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
		this.setVOTableName("GC_XMGLGS_XXJD_JHFK");
	}

	public void setGc_xmglgs_xxjd_jhfk_id(String gc_xmglgs_xxjd_jhfk_id){
		this.setInternal("GC_XMGLGS_XXJD_JHFK_ID",gc_xmglgs_xxjd_jhfk_id);
	}
	public String getGc_xmglgs_xxjd_jhfk_id(){
		return (String)this.getInternal("GC_XMGLGS_XXJD_JHFK_ID");
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
	public void setJhbzid(String jhbzid){
		this.setInternal("JHBZID",jhbzid);
	}
	public String getJhbzid(){
		return (String)this.getInternal("JHBZID");
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
}