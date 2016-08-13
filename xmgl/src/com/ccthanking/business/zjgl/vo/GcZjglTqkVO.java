package com.ccthanking.business.zjgl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZjglTqkVO extends BaseVO{

	public GcZjglTqkVO(){
		this.addField("ID",OP_STRING|this.TP_PK);
		this.addField("SQDW",OP_STRING);
		this.addField("QKLX",OP_STRING);
		this.addField("QKNF",OP_STRING);
		this.addField("GCPC",OP_STRING);
		this.addField("QKMC",OP_STRING);
		this.addField("BZRQ",OP_DATE);
		this.addField("HTDID",OP_STRING);
		this.addField("TQKZT",OP_STRING);
		this.addField("ZZXHTJ",OP_STRING);
		this.addField("ZYBF",OP_STRING);
		this.addField("ZBCSQ",OP_STRING);
		this.addField("ZLJBF",OP_STRING);
		this.addField("ZAHTBFB",OP_STRING);
		this.addField("ZCSZ",OP_STRING);
		this.addField("ZJLQR",OP_STRING);
		this.addField("ZAJLFKB",OP_STRING);
		this.addField("YF",OP_STRING);
		this.addField("CWBLR",OP_STRING);
		this.addField("CWBLRID",OP_STRING);
		this.addField("JCBLR",OP_STRING);
		this.addField("JCBLRID",OP_STRING);
		this.addField("CWSHR",OP_STRING);
		this.addField("CWSHRID",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.addField("SORTNO",OP_STRING);
		this.addField("JCPCH",OP_STRING);
		this.addField("RZZT",OP_STRING);
		this.setFieldDateFormat("BZRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		this.bindFieldToThousand("ZZXHTJ");// 总最新合同价
        this.bindFieldToThousand("ZYBF");// 总已拔付
        this.bindFieldToThousand("ZBCSQ");// 总本次申请
        this.bindFieldToThousand("ZLJBF");// 总累计拔付
        this.bindFieldToThousand("ZCSZ");// 总财审值
        this.bindFieldToThousand("ZJLQR");// 总监理确认款
        
		
		this.setVOTableName("GC_ZJGL_TQK");
	}

	public void setId(String id){
		this.setInternal("ID",id);
	}
	public String getId(){
		return (String)this.getInternal("ID");
	}
	public void setSqdw(String sqdw){
		this.setInternal("SQDW",sqdw);
	}
	public String getSqdw(){
		return (String)this.getInternal("SQDW");
	}
	public void setQklx(String qklx){
		this.setInternal("QKLX",qklx);
	}
	public String getQklx(){
		return (String)this.getInternal("QKLX");
	}
	public void setQknf(String qknf){
		this.setInternal("QKNF",qknf);
	}
	public String getQknf(){
		return (String)this.getInternal("QKNF");
	}
	public void setGcpc(String gcpc){
		this.setInternal("GCPC",gcpc);
	}
	public String getGcpc(){
		return (String)this.getInternal("GCPC");
	}
	public void setQkmc(String qkmc){
		this.setInternal("QKMC",qkmc);
	}
	public String getQkmc(){
		return (String)this.getInternal("QKMC");
	}
	public void setBzrq(Date bzrq){
		this.setInternal("BZRQ",bzrq);
	}
	public Date getBzrq(){
		return (Date)this.getInternal("BZRQ");
	}
	public void setHtdid(String htdid){
		this.setInternal("HTDID",htdid);
	}
	public String getHtdid(){
		return (String)this.getInternal("HTDID");
	}
	public void setTqkzt(String tqkzt){
		this.setInternal("TQKZT",tqkzt);
	}
	public String getTqkzt(){
		return (String)this.getInternal("TQKZT");
	}
	public void setZzxhtj(String zzxhtj){
		this.setInternal("ZZXHTJ",zzxhtj);
	}
	public String getZzxhtj(){
		return (String)this.getInternal("ZZXHTJ");
	}
	public void setZybf(String zybf){
		this.setInternal("ZYBF",zybf);
	}
	public String getZybf(){
		return (String)this.getInternal("ZYBF");
	}
	public void setZbcsq(String zbcsq){
		this.setInternal("ZBCSQ",zbcsq);
	}
	public String getZbcsq(){
		return (String)this.getInternal("ZBCSQ");
	}
	public void setZljbf(String zljbf){
		this.setInternal("ZLJBF",zljbf);
	}
	public String getZljbf(){
		return (String)this.getInternal("ZLJBF");
	}
	public void setZahtbfb(String zahtbfb){
		this.setInternal("ZAHTBFB",zahtbfb);
	}
	public String getZahtbfb(){
		return (String)this.getInternal("ZAHTBFB");
	}
	public void setZcsz(String zcsz){
		this.setInternal("ZCSZ",zcsz);
	}
	public String getZcsz(){
		return (String)this.getInternal("ZCSZ");
	}
	public void setZjlqr(String zjlqr){
		this.setInternal("ZJLQR",zjlqr);
	}
	public String getZjlqr(){
		return (String)this.getInternal("ZJLQR");
	}
	public void setZajlfkb(String zajlfkb){
		this.setInternal("ZAJLFKB",zajlfkb);
	}
	public String getZajlfkb(){
		return (String)this.getInternal("ZAJLFKB");
	}
	public void setYf(String yf){
		this.setInternal("YF",yf);
	}
	public String getYf(){
		return (String)this.getInternal("YF");
	}
	public void setCwblr(String cwblr){
		this.setInternal("CWBLR",cwblr);
	}
	public String getCwblr(){
		return (String)this.getInternal("CWBLR");
	}
	public void setCwblrid(String cwblrid){
		this.setInternal("CWBLRID",cwblrid);
	}
	public String getCwblrid(){
		return (String)this.getInternal("CWBLRID");
	}
	public void setJcblr(String jcblr){
		this.setInternal("JCBLR",jcblr);
	}
	public String getJcblr(){
		return (String)this.getInternal("JCBLR");
	}
	public void setJcblrid(String jcblrid){
		this.setInternal("JCBLRID",jcblrid);
	}
	public String getJcblrid(){
		return (String)this.getInternal("JCBLRID");
	}
	public void setCwshr(String cwshr){
		this.setInternal("CWSHR",cwshr);
	}
	public String getCwshr(){
		return (String)this.getInternal("CWSHR");
	}
	public void setCwshrid(String cwshrid){
		this.setInternal("CWSHRID",cwshrid);
	}
	public String getCwshrid(){
		return (String)this.getInternal("CWSHRID");
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
	public void setSortno(String sortno){
		this.setInternal("SORTNO",sortno);
	}
	public String getSortno(){
		return (String)this.getInternal("SORTNO");
	}
	public void setJcpch(String jcpch){
		this.setInternal("JCPCH",jcpch);
	}
	public String getJcpch(){
		return (String)this.getInternal("JCPCH");
	}
	public void setRzzt(String rzzt){
		this.setInternal("RZZT",rzzt);
	}
	public String getRzzt(){
		return (String)this.getInternal("RZZT");
	}
}