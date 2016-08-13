package com.ccthanking.business.xmglgs.zybgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcXmglgsZbbVO extends BaseVO{

	public GcXmglgsZbbVO(){
		this.addField("ID",OP_STRING|this.TP_PK);
		this.addField("SJID",OP_STRING);
		this.addField("XDKID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("BZJH",OP_STRING);
		this.addField("BZWC",OP_STRING);
		this.addField("BNWC",OP_STRING);
		this.addField("LJWC",OP_STRING);
		this.addField("XZJH",OP_STRING);
		this.addField("GXMC",OP_STRING);
		this.addField("PQWCSJ",OP_DATE);
		this.addField("PABZJZ",OP_STRING);
		this.addField("CQWMC",OP_STRING);
		this.addField("CQWCSJ",OP_DATE);
		this.addField("CQBZJZ",OP_STRING);
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
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.setFieldDateFormat("PQWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("CQWCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_XMGLGS_ZBB");
	}

	public void setId(String id){
		this.setInternal("ID",id);
	}
	public String getId(){
		return (String)this.getInternal("ID");
	}
	public void setSjid(String sjid){
		this.setInternal("SJID",sjid);
	}
	public String getSjid(){
		return (String)this.getInternal("SJID");
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
	public void setLjwc(String ljwc){
		this.setInternal("LJWC",ljwc);
	}
	public String getLjwc(){
		return (String)this.getInternal("LJWC");
	}
	public void setXzjh(String xzjh){
		this.setInternal("XZJH",xzjh);
	}
	public String getXzjh(){
		return (String)this.getInternal("XZJH");
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
	public void setPabzjz(String pabzjz){
		this.setInternal("PABZJZ",pabzjz);
	}
	public String getPabzjz(){
		return (String)this.getInternal("PABZJZ");
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
	public void setCqbzjz(String cqbzjz){
		this.setInternal("CQBZJZ",cqbzjz);
	}
	public String getCqbzjz(){
		return (String)this.getInternal("CQBZJZ");
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