package com.ccthanking.business.gcgl.zbgl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZbbVO extends BaseVO{

	public GcZbbVO(){
		this.addField("GC_XMGLGS_ZBB_ID",OP_STRING|this.TP_PK);
		this.addField("HTID",OP_STRING);
		this.addField("XDKID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("GXMC",OP_STRING);
		this.addField("PQWCSJ",OP_DATE);
		this.addField("CQWMC",OP_STRING);
		this.addField("CQWCSJ",OP_DATE);
		this.addField("ZJLBZ",OP_STRING);
		this.addField("ZJLND",OP_STRING);
		this.addField("ZJLLJWC",OP_STRING);
		this.addField("QQWT",OP_STRING);
		this.addField("HTZJWT",OP_STRING);
		this.addField("SJWT",OP_STRING);
		this.addField("ZCWT",OP_STRING);
		this.addField("PQWT",OP_STRING);
		this.addField("FJID",OP_STRING);
		this.addField("NOTE",OP_STRING);
		this.addField("LRRQ",OP_DATE);
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
		this.addField("XMBH",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("BDBH",OP_STRING);
		this.addField("BDMC",OP_STRING);
		this.addField("XMDZ",OP_STRING);
		this.addField("JSRW",OP_STRING);
		this.addField("KFGRQ",OP_DATE);
		this.addField("YZDB",OP_STRING);
		this.addField("SGDW",OP_STRING);
		this.addField("JLDW",OP_STRING);
		this.addField("KSSJ",OP_DATE);
		this.addField("JSSJ",OP_DATE);
		this.addField("XMGLGS",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("LJWC",OP_CLOB);
		this.addField("BZJH",OP_CLOB);
		this.addField("BZWC",OP_CLOB);
		this.addField("BNWC",OP_CLOB);
		this.addField("XZJH",OP_CLOB);
		this.addField("PABZJZ",OP_CLOB);
		this.addField("CQBZJZ",OP_CLOB);
		
		//金额
		this.bindFieldToThousand("ZJLBZ"); 
		this.bindFieldToThousand("ZJLND"); 
		this.bindFieldToThousand("ZJLLJWC");
		//this.bindFieldToTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
		this.bindFieldToDic("XMLX", "XMLX");
		/*this.bindFieldToDic("XMGLGS", "XMGLGS");*/
		this.bindFieldToTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
		this.setFieldDateFormat("PQWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("CQWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("KFGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("KSSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JSSJ","yyyy-MM-dd");
		this.setVOTableName("GC_XMGLGS_ZBB");
	}

	public void setGc_xmglgs_zbb_id(String gc_xmglgs_zbb_id){
		this.setInternal("GC_XMGLGS_ZBB_ID",gc_xmglgs_zbb_id);
	}
	public String getGc_xmglgs_zbb_id(){
		return (String)this.getInternal("GC_XMGLGS_ZBB_ID");
	}
	public void setHtid(String htid){
		this.setInternal("HTID",htid);
	}
	public String getHtid(){
		return (String)this.getInternal("HTID");
	}
	public void setXdkid(String xdkid){
		this.setInternal("XDKID",xdkid);
	}
	public String getXdkid(){
		return (String)this.getInternal("XDKID");
	}
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setGxmc(String gxmc){
		this.setInternal("GXMC",gxmc);
	}
	public String getGxmc(){
		return (String)this.getInternal("GXMC");
	}
	public void setPqwcsj(Date pqwcsj){
		this.setInternal("PQWCSJ",pqwcsj);
	}
	public Date getPqwcsj(){
		return (Date)this.getInternal("PQWCSJ");
	}
	public void setCqwmc(String cqwmc){
		this.setInternal("CQWMC",cqwmc);
	}
	public String getCqwmc(){
		return (String)this.getInternal("CQWMC");
	}
	public void setCqwcsj(Date cqwcsj){
		this.setInternal("CQWCSJ",cqwcsj);
	}
	public Date getCqwcsj(){
		return (Date)this.getInternal("CQWCSJ");
	}
	public void setZjlbz(String zjlbz){
		this.setInternal("ZJLBZ",zjlbz);
	}
	public String getZjlbz(){
		return (String)this.getInternal("ZJLBZ");
	}
	public void setZjlnd(String zjlnd){
		this.setInternal("ZJLND",zjlnd);
	}
	public String getZjlnd(){
		return (String)this.getInternal("ZJLND");
	}
	public void setZjlljwc(String zjlljwc){
		this.setInternal("ZJLLJWC",zjlljwc);
	}
	public String getZjlljwc(){
		return (String)this.getInternal("ZJLLJWC");
	}
	public void setQqwt(String qqwt){
		this.setInternal("QQWT",qqwt);
	}
	public String getQqwt(){
		return (String)this.getInternal("QQWT");
	}
	public void setHtzjwt(String htzjwt){
		this.setInternal("HTZJWT",htzjwt);
	}
	public String getHtzjwt(){
		return (String)this.getInternal("HTZJWT");
	}
	public void setSjwt(String sjwt){
		this.setInternal("SJWT",sjwt);
	}
	public String getSjwt(){
		return (String)this.getInternal("SJWT");
	}
	public void setZcwt(String zcwt){
		this.setInternal("ZCWT",zcwt);
	}
	public String getZcwt(){
		return (String)this.getInternal("ZCWT");
	}
	public void setPqwt(String pqwt){
		this.setInternal("PQWT",pqwt);
	}
	public String getPqwt(){
		return (String)this.getInternal("PQWT");
	}
	public void setFjid(String fjid){
		this.setInternal("FJID",fjid);
	}
	public String getFjid(){
		return (String)this.getInternal("FJID");
	}
	public void setNote(String note){
		this.setInternal("NOTE",note);
	}
	public String getNote(){
		return (String)this.getInternal("NOTE");
	}
	public void setLrrq(Date lrrq){
		this.setInternal("LRRQ",lrrq);
	}
	public Date getLrrq(){
		return (Date)this.getInternal("LRRQ");
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
	public void setBdbh(String bdbh){
		this.setInternal("BDBH",bdbh);
	}
	public String getBdbh(){
		return (String)this.getInternal("BDBH");
	}
	public void setBdmc(String bdmc){
		this.setInternal("BDMC",bdmc);
	}
	public String getBdmc(){
		return (String)this.getInternal("BDMC");
	}
	public void setXmdz(String xmdz){
		this.setInternal("XMDZ",xmdz);
	}
	public String getXmdz(){
		return (String)this.getInternal("XMDZ");
	}
	public void setJsrw(String jsrw){
		this.setInternal("JSRW",jsrw);
	}
	public String getJsrw(){
		return (String)this.getInternal("JSRW");
	}
	public void setKfgrq(Date kfgrq){
		this.setInternal("KFGRQ",kfgrq);
	}
	public Date getKfgrq(){
		return (Date)this.getInternal("KFGRQ");
	}
	public void setYzdb(String yzdb){
		this.setInternal("YZDB",yzdb);
	}
	public String getYzdb(){
		return (String)this.getInternal("YZDB");
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
	public void setKssj(Date kssj){
		this.setInternal("KSSJ",kssj);
	}
	public Date getKssj(){
		return (Date)this.getInternal("KSSJ");
	}
	public void setJssj(Date jssj){
		this.setInternal("JSSJ",jssj);
	}
	public Date getJssj(){
		return (Date)this.getInternal("JSSJ");
	}
	public void setXmglgs(String xmglgs){
		this.setInternal("XMGLGS",xmglgs);
	}
	public String getXmglgs(){
		return (String)this.getInternal("XMGLGS");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setLjwc(String ljwc){
		this.setInternal("LJWC",ljwc);
	}
	public String getLjwc(){
		return (String)this.getInternal("LJWC");
	}
	public void setBzjh(String bzjh){
		this.setInternal("BZJH",bzjh);
	}
	public String getBzjh(){
		return (String)this.getInternal("BZJH");
	}
	public void setBzwc(String bzwc){
		this.setInternal("BZWC",bzwc);
	}
	public String getBzwc(){
		return (String)this.getInternal("BZWC");
	}
	public void setBnwc(String bnwc){
		this.setInternal("BNWC",bnwc);
	}
	public String getBnwc(){
		return (String)this.getInternal("BNWC");
	}
	public void setXzjh(String xzjh){
		this.setInternal("XZJH",xzjh);
	}
	public String getXzjh(){
		return (String)this.getInternal("XZJH");
	}
	public void setPabzjz(String pabzjz){
		this.setInternal("PABZJZ",pabzjz);
	}
	public String getPabzjz(){
		return (String)this.getInternal("PABZJZ");
	}
	public void setCqbzjz(String cqbzjz){
		this.setInternal("CQBZJZ",cqbzjz);
	}
	public String getCqbzjz(){
		return (String)this.getInternal("CQBZJZ");
	}
}