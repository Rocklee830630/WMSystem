package com.ccthanking.business.cjjh;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcCjjhVO extends BaseVO{

	public GcCjjhVO(){
		this.addField("GC_CJJH_ID",OP_STRING|this.TP_PK);
		this.addField("JWXM",OP_STRING);
		this.addField("WBXM",OP_STRING);
		this.addField("XH",OP_STRING);
		this.addField("MXS",OP_STRING);
		this.addField("ZXSM",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("XMJSWZ",OP_STRING);
		this.addField("XMJSNRGM",OP_STRING);
		this.addField("JH_GC",OP_STRING);
		this.addField("JH_ZQ",OP_STRING);
		this.addField("JH_QT",OP_STRING);
		this.addField("JH_XJ",OP_STRING);
		this.addField("XMJSJD",OP_STRING);
		this.addField("ND_GC",OP_STRING);
		this.addField("ND_ZQ",OP_STRING);
		this.addField("ND_QT",OP_STRING);
		this.addField("ND_XJ",OP_STRING);
		this.addField("ND_JSMB",OP_STRING);
		this.addField("ZRBM",OP_STRING);
		this.addField("XMFR",OP_STRING);
		this.addField("XMLX",OP_STRING);
		this.addField("PARENTID",OP_STRING);
		this.addField("NODELEVEL",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("PXH",OP_STRING);
		this.addField("PXH_SJ",OP_STRING);
		this.addField("ISXM",OP_STRING);
		this.addField("XMSX",OP_STRING);
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
		this.addField("DXZX",OP_STRING);
		this.addField("XMXZ",OP_STRING);
		this.addField("TZLX",OP_STRING);
		this.addField("SSXZ",OP_STRING);
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_CJJH");
	}

	public void setGc_cjjh_id(String gc_cjjh_id){
		this.setInternal("GC_CJJH_ID",gc_cjjh_id);
	}
	public String getGc_cjjh_id(){
		return (String)this.getInternal("GC_CJJH_ID");
	}
	public void setJwxm(String jwxm){
		this.setInternal("JWXM",jwxm);
	}
	public String getJwxm(){
		return (String)this.getInternal("JWXM");
	}
	public void setWbxm(String wbxm){
		this.setInternal("WBXM",wbxm);
	}
	public String getWbxm(){
		return (String)this.getInternal("WBXM");
	}
	public void setXh(String xh){
		this.setInternal("XH",xh);
	}
	public String getXh(){
		return (String)this.getInternal("XH");
	}
	public void setMxs(String mxs){
		this.setInternal("MXS",mxs);
	}
	public String getMxs(){
		return (String)this.getInternal("MXS");
	}
	public void setZxsm(String zxsm){
		this.setInternal("ZXSM",zxsm);
	}
	public String getZxsm(){
		return (String)this.getInternal("ZXSM");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setXmjswz(String xmjswz){
		this.setInternal("XMJSWZ",xmjswz);
	}
	public String getXmjswz(){
		return (String)this.getInternal("XMJSWZ");
	}
	public void setXmjsnrgm(String xmjsnrgm){
		this.setInternal("XMJSNRGM",xmjsnrgm);
	}
	public String getXmjsnrgm(){
		return (String)this.getInternal("XMJSNRGM");
	}
	public void setJh_gc(String jh_gc){
		this.setInternal("JH_GC",jh_gc);
	}
	public String getJh_gc(){
		return (String)this.getInternal("JH_GC");
	}
	public void setJh_zq(String jh_zq){
		this.setInternal("JH_ZQ",jh_zq);
	}
	public String getJh_zq(){
		return (String)this.getInternal("JH_ZQ");
	}
	public void setJh_qt(String jh_qt){
		this.setInternal("JH_QT",jh_qt);
	}
	public String getJh_qt(){
		return (String)this.getInternal("JH_QT");
	}
	public void setJh_xj(String jh_xj){
		this.setInternal("JH_XJ",jh_xj);
	}
	public String getJh_xj(){
		return (String)this.getInternal("JH_XJ");
	}
	public void setXmjsjd(String xmjsjd){
		this.setInternal("XMJSJD",xmjsjd);
	}
	public String getXmjsjd(){
		return (String)this.getInternal("XMJSJD");
	}
	public void setNd_gc(String nd_gc){
		this.setInternal("ND_GC",nd_gc);
	}
	public String getNd_gc(){
		return (String)this.getInternal("ND_GC");
	}
	public void setNd_zq(String nd_zq){
		this.setInternal("ND_ZQ",nd_zq);
	}
	public String getNd_zq(){
		return (String)this.getInternal("ND_ZQ");
	}
	public void setNd_qt(String nd_qt){
		this.setInternal("ND_QT",nd_qt);
	}
	public String getNd_qt(){
		return (String)this.getInternal("ND_QT");
	}
	public void setNd_xj(String nd_xj){
		this.setInternal("ND_XJ",nd_xj);
	}
	public String getNd_xj(){
		return (String)this.getInternal("ND_XJ");
	}
	public void setNd_jsmb(String nd_jsmb){
		this.setInternal("ND_JSMB",nd_jsmb);
	}
	public String getNd_jsmb(){
		return (String)this.getInternal("ND_JSMB");
	}
	public void setZrbm(String zrbm){
		this.setInternal("ZRBM",zrbm);
	}
	public String getZrbm(){
		return (String)this.getInternal("ZRBM");
	}
	public void setXmfr(String xmfr){
		this.setInternal("XMFR",xmfr);
	}
	public String getXmfr(){
		return (String)this.getInternal("XMFR");
	}
	public void setXmlx(String xmlx){
		this.setInternal("XMLX",xmlx);
	}
	public String getXmlx(){
		return (String)this.getInternal("XMLX");
	}
	public void setParentid(String parentid){
		this.setInternal("PARENTID",parentid);
	}
	public String getParentid(){
		return (String)this.getInternal("PARENTID");
	}
	public void setNodelevel(String nodelevel){
		this.setInternal("NODELEVEL",nodelevel);
	}
	public String getNodelevel(){
		return (String)this.getInternal("NODELEVEL");
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
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setPxh(String pxh){
		this.setInternal("PXH",pxh);
	}
	public String getPxh(){
		return (String)this.getInternal("PXH");
	}
	public void setPxh_sj(String pxh_sj){
		this.setInternal("PXH_SJ",pxh_sj);
	}
	public String getPxh_sj(){
		return (String)this.getInternal("PXH_SJ");
	}
	public void setIsxm(String isxm){
		this.setInternal("ISXM",isxm);
	}
	public String getIsxm(){
		return (String)this.getInternal("ISXM");
	}
	public void setXmsx(String xmsx){
		this.setInternal("XMSX",xmsx);
	}
	public String getXmsx(){
		return (String)this.getInternal("XMSX");
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
	public void setDxzx(String dxzx){
		this.setInternal("DXZX",dxzx);
	}
	public String getDxzx(){
		return (String)this.getInternal("DXZX");
	}
	public void setXmxz(String xmxz){
		this.setInternal("XMXZ",xmxz);
	}
	public String getXmxz(){
		return (String)this.getInternal("XMXZ");
	}
	public void setTzlx(String tzlx){
		this.setInternal("TZLX",tzlx);
	}
	public String getTzlx(){
		return (String)this.getInternal("TZLX");
	}
	public void setSsxz(String ssxz){
		this.setInternal("SSXZ",ssxz);
	}
	public String getSsxz(){
		return (String)this.getInternal("SSXZ");
	}
}