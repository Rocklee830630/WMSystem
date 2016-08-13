package com.ccthanking.business.zjb.lbj;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZjbLbjbVO extends BaseVO{

	public GcZjbLbjbVO(){
		this.addField("GC_ZJB_LBJB_ID",OP_STRING|this.TP_PK);
		this.addField("BDID",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("BDMC",OP_STRING);
		this.addField("CSBGBH",OP_STRING);
		this.addField("TXQSJ",OP_DATE);
		this.addField("ZBSJ",OP_DATE);
		this.addField("ZXGS",OP_STRING);
		this.addField("TZJJSJ",OP_DATE);
		this.addField("ZXGSJ",OP_DATE);
		this.addField("SGFAJS",OP_DATE);
		this.addField("ZBWJJS",OP_DATE);
		this.addField("ZXGSRQ",OP_DATE);
		this.addField("JGBCSRQ",OP_DATE);
		this.addField("SBCSZ",OP_STRING);
		this.addField("CZSWRQ",OP_DATE);
		this.addField("CSSDZ",OP_STRING);
		this.addField("SJZ",OP_STRING);
		this.addField("SJBFB",OP_STRING);
		this.addField("ZDJ",OP_STRING);
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
		this.addField("JHID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("XMSX",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("PXH",OP_STRING);
		this.addField("SBCSZRQ",OP_DATE);
		this.addField("CSSDZRQ",OP_DATE);
		this.addField("SJWYBH",OP_STRING);
		this.addField("ZJBZZT",OP_STRING);
		this.addField("ISXYSCS",OP_STRING);
		this.addField("ISZH",OP_STRING);
		this.addField("CSSX",OP_STRING);
		this.addField("ISQTBMFZ",OP_STRING);
		this.setFieldDateFormat("TXQSJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZBSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TZJJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("ZXGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SGFAJS","yyyy-MM-dd");
		this.setFieldDateFormat("ZBWJJS","yyyy-MM-dd");
		this.setFieldDateFormat("ZXGSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("JGBCSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("CZSWRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SBCSZRQ","yyyy-MM-dd");
		this.setFieldDateFormat("CSSDZRQ","yyyy-MM-dd");
		
		this.bindFieldToTranslater("ZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		
		this.setVOTableName("GC_ZJB_LBJB");
	}

	public void setGc_zjb_lbjb_id(String gc_zjb_lbjb_id){
		this.setInternal("GC_ZJB_LBJB_ID",gc_zjb_lbjb_id);
	}
	public String getGc_zjb_lbjb_id(){
		return (String)this.getInternal("GC_ZJB_LBJB_ID");
	}
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
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
	public void setCsbgbh(String csbgbh){
		this.setInternal("CSBGBH",csbgbh);
	}
	public String getCsbgbh(){
		return (String)this.getInternal("CSBGBH");
	}
	public void setTxqsj(Date txqsj){
		this.setInternal("TXQSJ",txqsj);
	}
	public Date getTxqsj(){
		return (Date)this.getInternal("TXQSJ");
	}
	public void setZbsj(Date zbsj){
		this.setInternal("ZBSJ",zbsj);
	}
	public Date getZbsj(){
		return (Date)this.getInternal("ZBSJ");
	}
	public void setZxgs(String zxgs){
		this.setInternal("ZXGS",zxgs);
	}
	public String getZxgs(){
		return (String)this.getInternal("ZXGS");
	}
	public void setTzjjsj(Date tzjjsj){
		this.setInternal("TZJJSJ",tzjjsj);
	}
	public Date getTzjjsj(){
		return (Date)this.getInternal("TZJJSJ");
	}
	public void setZxgsj(Date zxgsj){
		this.setInternal("ZXGSJ",zxgsj);
	}
	public Date getZxgsj(){
		return (Date)this.getInternal("ZXGSJ");
	}
	public void setSgfajs(Date sgfajs){
		this.setInternal("SGFAJS",sgfajs);
	}
	public Date getSgfajs(){
		return (Date)this.getInternal("SGFAJS");
	}
	public void setZbwjjs(Date zbwjjs){
		this.setInternal("ZBWJJS",zbwjjs);
	}
	public Date getZbwjjs(){
		return (Date)this.getInternal("ZBWJJS");
	}
	public void setZxgsrq(Date zxgsrq){
		this.setInternal("ZXGSRQ",zxgsrq);
	}
	public Date getZxgsrq(){
		return (Date)this.getInternal("ZXGSRQ");
	}
	public void setJgbcsrq(Date jgbcsrq){
		this.setInternal("JGBCSRQ",jgbcsrq);
	}
	public Date getJgbcsrq(){
		return (Date)this.getInternal("JGBCSRQ");
	}
	public void setSbcsz(String sbcsz){
		this.setInternal("SBCSZ",sbcsz);
	}
	public String getSbcsz(){
		return (String)this.getInternal("SBCSZ");
	}
	public void setCzswrq(Date czswrq){
		this.setInternal("CZSWRQ",czswrq);
	}
	public Date getCzswrq(){
		return (Date)this.getInternal("CZSWRQ");
	}
	public void setCssdz(String cssdz){
		this.setInternal("CSSDZ",cssdz);
	}
	public String getCssdz(){
		return (String)this.getInternal("CSSDZ");
	}
	public void setSjz(String sjz){
		this.setInternal("SJZ",sjz);
	}
	public String getSjz(){
		return (String)this.getInternal("SJZ");
	}
	public void setSjbfb(String sjbfb){
		this.setInternal("SJBFB",sjbfb);
	}
	public String getSjbfb(){
		return (String)this.getInternal("SJBFB");
	}
	public void setZdj(String zdj){
		this.setInternal("ZDJ",zdj);
	}
	public String getZdj(){
		return (String)this.getInternal("ZDJ");
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
	public void setJhid(String jhid){
		this.setInternal("JHID",jhid);
	}
	public String getJhid(){
		return (String)this.getInternal("JHID");
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
	public void setXmsx(String xmsx){
		this.setInternal("XMSX",xmsx);
	}
	public String getXmsx(){
		return (String)this.getInternal("XMSX");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setPxh(String pxh){
		this.setInternal("PXH",pxh);
	}
	public String getPxh(){
		return (String)this.getInternal("PXH");
	}
	public void setSbcszrq(Date sbcszrq){
		this.setInternal("SBCSZRQ",sbcszrq);
	}
	public Date getSbcszrq(){
		return (Date)this.getInternal("SBCSZRQ");
	}
	public void setCssdzrq(Date cssdzrq){
		this.setInternal("CSSDZRQ",cssdzrq);
	}
	public Date getCssdzrq(){
		return (Date)this.getInternal("CSSDZRQ");
	}
	public void setSjwybh(String sjwybh){
		this.setInternal("SJWYBH",sjwybh);
	}
	public String getSjwybh(){
		return (String)this.getInternal("SJWYBH");
	}
	public void setZjbzzt(String zjbzzt){
		this.setInternal("ZJBZZT",zjbzzt);
	}
	public String getZjbzzt(){
		return (String)this.getInternal("ZJBZZT");
	}
	public void setIsxyscs(String isxyscs){
		this.setInternal("ISXYSCS",isxyscs);
	}
	public String getIsxyscs(){
		return (String)this.getInternal("ISXYSCS");
	}
	public void setIszh(String iszh){
		this.setInternal("ISZH",iszh);
	}
	public String getIszh(){
		return (String)this.getInternal("ISZH");
	}
	public void setCssx(String cssx){
		this.setInternal("CSSX",cssx);
	}
	public String getCssx(){
		return (String)this.getInternal("CSSX");
	}
	public void setIsqtbmfz(String isqtbmfz){
		this.setInternal("ISQTBMFZ",isqtbmfz);
	}
	public String getIsqtbmfz(){
		return (String)this.getInternal("ISQTBMFZ");
	}
}