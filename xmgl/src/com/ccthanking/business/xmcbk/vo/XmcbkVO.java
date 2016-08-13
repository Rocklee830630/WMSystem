package com.ccthanking.business.xmcbk.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class XmcbkVO extends BaseVO{

	public XmcbkVO(){
		this.addField("GC_TCJH_XMCBK_ID",OP_STRING|this.TP_PK);
		this.addField("XMBH",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("PXH",OP_STRING);
		this.addField("QY",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMLX",OP_STRING);
		this.addField("XMDZ",OP_STRING);
		this.addField("JSNR",OP_STRING);
		this.addField("JSYY",OP_STRING);
		this.addField("JSRW",OP_STRING);
		this.addField("JSBYX",OP_STRING);
		this.addField("JHZTZE",OP_STRING);
		this.addField("GC",OP_STRING);
		this.addField("ZC",OP_STRING);
		this.addField("QT",OP_STRING);
		this.addField("XMSX",OP_STRING);
		this.addField("ISBT",OP_STRING);
		this.addField("PCH",OP_STRING);
		this.addField("XDLX",OP_STRING);
		this.addField("ISXD",OP_STRING);
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
		this.addField("PCID",OP_STRING);
		this.addField("SPXXID",OP_STRING);
		this.addField("ZTZTZE",OP_STRING);
		this.addField("ZTGC",OP_STRING);
		this.addField("ZTZC",OP_STRING);
		this.addField("ZTQT",OP_STRING);
		this.addField("XMBM",OP_STRING);
		this.addField("CBK_XMBM",OP_STRING);
		this.addField("ZRBM",OP_STRING);
		this.addField("XMFR",OP_STRING);
		this.addField("ISNRTJ",OP_STRING);
		this.addField("WCZTZE",OP_STRING);
		this.addField("WCGC",OP_STRING);
		this.addField("WCZC",OP_STRING);
		this.addField("WCQT",OP_STRING);
		this.addField("NDMB",OP_STRING);
		this.addField("ISAMBWC",OP_STRING);
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		
		  //字典
	    this.bindFieldToDic("XMLX", "XMLX");
	    this.bindFieldToDic("QY", "QY");
	    this.bindFieldToDic("ND", "XMNF");
	    this.bindFieldToDic("XMSX", "XMSX");
	    this.bindFieldToDic("ISBT", "SF");
	    this.bindFieldToDic("ISNRTJ", "SF");
	    this.bindFieldToDic("ISXD", "XMZT");
	    this.bindFieldToDic("SFYX", "SF");
	    this.bindFieldToDic("ISNRTJ", "SF");
	    this.bindFieldToDic("PCH", "YJPCH");
	    this.bindFieldToThousand("GC"); //数字千分位
	    this.bindFieldToThousand("ZC");
	    this.bindFieldToThousand("QT");
	    this.bindFieldToThousand("JHZTZE");
	    
		this.setVOTableName("GC_TCJH_XMCBK");
	}

	public void setGc_tcjh_xmcbk_id(String gc_tcjh_xmcbk_id){
		this.setInternal("GC_TCJH_XMCBK_ID",gc_tcjh_xmcbk_id);
	}
	public String getGc_tcjh_xmcbk_id(){
		return (String)this.getInternal("GC_TCJH_XMCBK_ID");
	}
	public void setXmbh(String xmbh){
		this.setInternal("XMBH",xmbh);
	}
	public String getXmbh(){
		return (String)this.getInternal("XMBH");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setPxh(String pxh){
		this.setInternal("PXH",pxh);
	}
	public String getPxh(){
		return (String)this.getInternal("PXH");
	}
	public void setQy(String qy){
		this.setInternal("QY",qy);
	}
	public String getQy(){
		return (String)this.getInternal("QY");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setXmlx(String xmlx){
		this.setInternal("XMLX",xmlx);
	}
	public String getXmlx(){
		return (String)this.getInternal("XMLX");
	}
	public void setXmdz(String xmdz){
		this.setInternal("XMDZ",xmdz);
	}
	public String getXmdz(){
		return (String)this.getInternal("XMDZ");
	}
	public void setJsnr(String jsnr){
		this.setInternal("JSNR",jsnr);
	}
	public String getJsnr(){
		return (String)this.getInternal("JSNR");
	}
	public void setJsyy(String jsyy){
		this.setInternal("JSYY",jsyy);
	}
	public String getJsyy(){
		return (String)this.getInternal("JSYY");
	}
	public void setJsrw(String jsrw){
		this.setInternal("JSRW",jsrw);
	}
	public String getJsrw(){
		return (String)this.getInternal("JSRW");
	}
	public void setJsbyx(String jsbyx){
		this.setInternal("JSBYX",jsbyx);
	}
	public String getJsbyx(){
		return (String)this.getInternal("JSBYX");
	}
	public void setJhztze(String jhztze){
		this.setInternal("JHZTZE",jhztze);
	}
	public String getJhztze(){
		return (String)this.getInternal("JHZTZE");
	}
	public void setGc(String gc){
		this.setInternal("GC",gc);
	}
	public String getGc(){
		return (String)this.getInternal("GC");
	}
	public void setZc(String zc){
		this.setInternal("ZC",zc);
	}
	public String getZc(){
		return (String)this.getInternal("ZC");
	}
	public void setQt(String qt){
		this.setInternal("QT",qt);
	}
	public String getQt(){
		return (String)this.getInternal("QT");
	}
	public void setXmsx(String xmsx){
		this.setInternal("XMSX",xmsx);
	}
	public String getXmsx(){
		return (String)this.getInternal("XMSX");
	}
	public void setIsbt(String isbt){
		this.setInternal("ISBT",isbt);
	}
	public String getIsbt(){
		return (String)this.getInternal("ISBT");
	}
	public void setPch(String pch){
		this.setInternal("PCH",pch);
	}
	public String getPch(){
		return (String)this.getInternal("PCH");
	}
	public void setXdlx(String xdlx){
		this.setInternal("XDLX",xdlx);
	}
	public String getXdlx(){
		return (String)this.getInternal("XDLX");
	}
	public void setIsxd(String isxd){
		this.setInternal("ISXD",isxd);
	}
	public String getIsxd(){
		return (String)this.getInternal("ISXD");
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
	public void setPcid(String pcid){
		this.setInternal("PCID",pcid);
	}
	public String getPcid(){
		return (String)this.getInternal("PCID");
	}
	public void setSpxxid(String spxxid){
		this.setInternal("SPXXID",spxxid);
	}
	public String getSpxxid(){
		return (String)this.getInternal("SPXXID");
	}
	public void setZtztze(String ztztze){
		this.setInternal("ZTZTZE",ztztze);
	}
	public String getZtztze(){
		return (String)this.getInternal("ZTZTZE");
	}
	public void setZtgc(String ztgc){
		this.setInternal("ZTGC",ztgc);
	}
	public String getZtgc(){
		return (String)this.getInternal("ZTGC");
	}
	public void setZtzc(String ztzc){
		this.setInternal("ZTZC",ztzc);
	}
	public String getZtzc(){
		return (String)this.getInternal("ZTZC");
	}
	public void setZtqt(String ztqt){
		this.setInternal("ZTQT",ztqt);
	}
	public String getZtqt(){
		return (String)this.getInternal("ZTQT");
	}
	public void setXmbm(String xmbm){
		this.setInternal("XMBM",xmbm);
	}
	public String getXmbm(){
		return (String)this.getInternal("XMBM");
	}
	public void setCbk_xmbm(String cbk_xmbm){
		this.setInternal("CBK_XMBM",cbk_xmbm);
	}
	public String getCbk_xmbm(){
		return (String)this.getInternal("CBK_XMBM");
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
	public void setIsnrtj(String isnrtj){
		this.setInternal("ISNRTJ",isnrtj);
	}
	public String getIsnrtj(){
		return (String)this.getInternal("ISNRTJ");
	}
	public void setWcztze(String wcztze){
		this.setInternal("WCZTZE",wcztze);
	}
	public String getWcztze(){
		return (String)this.getInternal("WCZTZE");
	}
	public void setWcgc(String wcgc){
		this.setInternal("WCGC",wcgc);
	}
	public String getWcgc(){
		return (String)this.getInternal("WCGC");
	}
	public void setWczc(String wczc){
		this.setInternal("WCZC",wczc);
	}
	public String getWczc(){
		return (String)this.getInternal("WCZC");
	}
	public void setWcqt(String wcqt){
		this.setInternal("WCQT",wcqt);
	}
	public String getWcqt(){
		return (String)this.getInternal("WCQT");
	}
	public void setNdmb(String ndmb){
		this.setInternal("NDMB",ndmb);
	}
	public String getNdmb(){
		return (String)this.getInternal("NDMB");
	}
	public void setIsambwc(String isambwc){
		this.setInternal("ISAMBWC",isambwc);
	}
	public String getIsambwc(){
		return (String)this.getInternal("ISAMBWC");
	}
}