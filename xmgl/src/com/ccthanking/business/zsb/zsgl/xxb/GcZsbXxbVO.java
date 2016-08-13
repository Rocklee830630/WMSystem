package com.ccthanking.business.zsb.zsgl.xxb;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZsbXxbVO extends BaseVO{

	public GcZsbXxbVO(){
		this.addField("GC_ZSB_XXB_ID",OP_STRING|this.TP_PK);
		this.addField("ZSXMID",OP_STRING);
		this.addField("QY",OP_STRING);
		this.addField("ZRDW",OP_STRING);
		this.addField("MDWCRQ",OP_DATE);
		this.addField("MDGZBZ",OP_STRING);
		this.addField("JMHS",OP_STRING);
		this.addField("QYJS",OP_STRING);
		this.addField("JTTDMJ",OP_STRING);
		this.addField("GYTDMJ",OP_STRING);
		this.addField("ZMJ",OP_STRING);
		this.addField("MDGS",OP_STRING);
		this.addField("SJFWCQ",OP_STRING);
		this.addField("SJWCRQ",OP_DATE);
		this.addField("QWTRQ",OP_DATE);
		this.addField("QWTBZ",OP_STRING);
		this.addField("ZJDWJE",OP_STRING);
		this.addField("ZJDWRQ",OP_DATE);
		this.addField("ZJDWBZ",OP_STRING);
		this.addField("SJRQ",OP_DATE);
		this.addField("CQSSBZ",OP_STRING);
		this.addField("CDYJRQ",OP_DATE);
		this.addField("CDYJBZ",OP_STRING);
		this.addField("TDFWYJRQ",OP_DATE);
		this.addField("TDFWBZ",OP_STRING);
		this.addField("CQBZ",OP_STRING);
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
		this.addField("LJJMZL",OP_STRING);
		this.addField("LJQYZL",OP_STRING);
		this.addField("LJZDMJ",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("JHID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("CQRWMC",OP_STRING);
		this.addField("FZR",OP_STRING);
		this.addField("XYJE",OP_STRING);
		this.addField("WTJFX",OP_STRING);
		this.bindFieldToDic("SJFWCQ", "SF");
		this.bindFieldToThousand("XYJE");
		this.bindFieldToThousand("MDGS");
		this.bindFieldToThousand("ZJDWJE");
		this.setFieldDateFormat("MDWCRQ","yyyy-MM-dd");
		this.setFieldDateFormat("SJWCRQ","yyyy-MM-dd");
		this.setFieldDateFormat("QWTRQ","yyyy-MM-dd");
		this.setFieldDateFormat("ZJDWRQ","yyyy-MM-dd");
		this.setFieldDateFormat("SJRQ","yyyy-MM-dd");
		this.setFieldDateFormat("CDYJRQ","yyyy-MM-dd");
		this.setFieldDateFormat("TDFWYJRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_ZSB_XXB");
	}

	public void setGc_zsb_xxb_id(String gc_zsb_xxb_id){
		this.setInternal("GC_ZSB_XXB_ID",gc_zsb_xxb_id);
	}
	public String getGc_zsb_xxb_id(){
		return (String)this.getInternal("GC_ZSB_XXB_ID");
	}
	public void setZsxmid(String zsxmid){
		this.setInternal("ZSXMID",zsxmid);
	}
	public String getZsxmid(){
		return (String)this.getInternal("ZSXMID");
	}
	public void setQy(String qy){
		this.setInternal("QY",qy);
	}
	public String getQy(){
		return (String)this.getInternal("QY");
	}
	public void setZrdw(String zrdw){
		this.setInternal("ZRDW",zrdw);
	}
	public String getZrdw(){
		return (String)this.getInternal("ZRDW");
	}
	public void setMdwcrq(Date mdwcrq){
		this.setInternal("MDWCRQ",mdwcrq);
	}
	public Date getMdwcrq(){
		return (Date)this.getInternal("MDWCRQ");
	}
	public void setMdgzbz(String mdgzbz){
		this.setInternal("MDGZBZ",mdgzbz);
	}
	public String getMdgzbz(){
		return (String)this.getInternal("MDGZBZ");
	}
	public void setJmhs(String jmhs){
		this.setInternal("JMHS",jmhs);
	}
	public String getJmhs(){
		return (String)this.getInternal("JMHS");
	}
	public void setQyjs(String qyjs){
		this.setInternal("QYJS",qyjs);
	}
	public String getQyjs(){
		return (String)this.getInternal("QYJS");
	}
	public void setJttdmj(String jttdmj){
		this.setInternal("JTTDMJ",jttdmj);
	}
	public String getJttdmj(){
		return (String)this.getInternal("JTTDMJ");
	}
	public void setGytdmj(String gytdmj){
		this.setInternal("GYTDMJ",gytdmj);
	}
	public String getGytdmj(){
		return (String)this.getInternal("GYTDMJ");
	}
	public void setZmj(String zmj){
		this.setInternal("ZMJ",zmj);
	}
	public String getZmj(){
		return (String)this.getInternal("ZMJ");
	}
	public void setMdgs(String mdgs){
		this.setInternal("MDGS",mdgs);
	}
	public String getMdgs(){
		return (String)this.getInternal("MDGS");
	}
	public void setSjfwcq(String sjfwcq){
		this.setInternal("SJFWCQ",sjfwcq);
	}
	public String getSjfwcq(){
		return (String)this.getInternal("SJFWCQ");
	}
	public void setSjwcrq(Date sjwcrq){
		this.setInternal("SJWCRQ",sjwcrq);
	}
	public Date getSjwcrq(){
		return (Date)this.getInternal("SJWCRQ");
	}
	public void setQwtrq(Date qwtrq){
		this.setInternal("QWTRQ",qwtrq);
	}
	public Date getQwtrq(){
		return (Date)this.getInternal("QWTRQ");
	}
	public void setQwtbz(String qwtbz){
		this.setInternal("QWTBZ",qwtbz);
	}
	public String getQwtbz(){
		return (String)this.getInternal("QWTBZ");
	}
	public void setZjdwje(String zjdwje){
		this.setInternal("ZJDWJE",zjdwje);
	}
	public String getZjdwje(){
		return (String)this.getInternal("ZJDWJE");
	}
	public void setZjdwrq(Date zjdwrq){
		this.setInternal("ZJDWRQ",zjdwrq);
	}
	public Date getZjdwrq(){
		return (Date)this.getInternal("ZJDWRQ");
	}
	public void setZjdwbz(String zjdwbz){
		this.setInternal("ZJDWBZ",zjdwbz);
	}
	public String getZjdwbz(){
		return (String)this.getInternal("ZJDWBZ");
	}
	public void setSjrq(Date sjrq){
		this.setInternal("SJRQ",sjrq);
	}
	public Date getSjrq(){
		return (Date)this.getInternal("SJRQ");
	}
	public void setCqssbz(String cqssbz){
		this.setInternal("CQSSBZ",cqssbz);
	}
	public String getCqssbz(){
		return (String)this.getInternal("CQSSBZ");
	}
	public void setCdyjrq(Date cdyjrq){
		this.setInternal("CDYJRQ",cdyjrq);
	}
	public Date getCdyjrq(){
		return (Date)this.getInternal("CDYJRQ");
	}
	public void setCdyjbz(String cdyjbz){
		this.setInternal("CDYJBZ",cdyjbz);
	}
	public String getCdyjbz(){
		return (String)this.getInternal("CDYJBZ");
	}
	public void setTdfwyjrq(Date tdfwyjrq){
		this.setInternal("TDFWYJRQ",tdfwyjrq);
	}
	public Date getTdfwyjrq(){
		return (Date)this.getInternal("TDFWYJRQ");
	}
	public void setTdfwbz(String tdfwbz){
		this.setInternal("TDFWBZ",tdfwbz);
	}
	public String getTdfwbz(){
		return (String)this.getInternal("TDFWBZ");
	}
	public void setCqbz(String cqbz){
		this.setInternal("CQBZ",cqbz);
	}
	public String getCqbz(){
		return (String)this.getInternal("CQBZ");
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
	public void setLjjmzl(String ljjmzl){
		this.setInternal("LJJMZL",ljjmzl);
	}
	public String getLjjmzl(){
		return (String)this.getInternal("LJJMZL");
	}
	public void setLjqyzl(String ljqyzl){
		this.setInternal("LJQYZL",ljqyzl);
	}
	public String getLjqyzl(){
		return (String)this.getInternal("LJQYZL");
	}
	public void setLjzdmj(String ljzdmj){
		this.setInternal("LJZDMJ",ljzdmj);
	}
	public String getLjzdmj(){
		return (String)this.getInternal("LJZDMJ");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
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
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setCqrwmc(String cqrwmc){
		this.setInternal("CQRWMC",cqrwmc);
	}
	public String getCqrwmc(){
		return (String)this.getInternal("CQRWMC");
	}
	public void setFzr(String fzr){
		this.setInternal("FZR",fzr);
	}
	public String getFzr(){
		return (String)this.getInternal("FZR");
	}
	public void setXyje(String xyje){
		this.setInternal("XYJE",xyje);
	}
	public String getXyje(){
		return (String)this.getInternal("XYJE");
	}
	public void setWtjfx(String wtjfx){
		this.setInternal("WTJFX",wtjfx);
	}
	public String getWtjfx(){
		return (String)this.getInternal("WTJFX");
	}
}