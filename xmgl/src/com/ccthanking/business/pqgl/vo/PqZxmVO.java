package com.ccthanking.business.pqgl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class PqZxmVO extends BaseVO{

	public PqZxmVO(){
		this.addField("GC_PQ_ZXM_ID",OP_STRING|this.TP_PK);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("GXLB",OP_STRING);
		this.addField("ZXMMC",OP_STRING);
		this.addField("PQDD",OP_STRING);
		this.addField("PXH",OP_STRING);
		this.addField("PQFA",OP_STRING);
		this.addField("JZFKID",OP_STRING);
		this.addField("JZQK",OP_STRING);
		this.addField("SYGZL",OP_STRING);
		this.addField("CZWT",OP_STRING);
		this.addField("JJFA",OP_STRING);
		this.addField("ISJHWC",OP_STRING);
		this.addField("KGSJ",OP_DATE);
		this.addField("WCSJ",OP_DATE);
		this.addField("KGSJ_JH",OP_DATE);
		this.addField("WCSJ_JH",OP_DATE);
		this.addField("CDYJ_PQ",OP_DATE);
		this.addField("XMFZR",OP_STRING);
		this.addField("SSR",OP_STRING);
		this.addField("SSDW",OP_STRING);
		this.addField("SSRQ",OP_DATE);
		this.addField("SSDBH",OP_STRING);
		this.addField("LLDBH",OP_STRING);
		this.addField("PSBGBH",OP_STRING);
		this.addField("WTHBH",OP_STRING);
		this.addField("SSZ",OP_STRING);
		this.addField("LXR",OP_STRING);
		this.addField("LXDH",OP_STRING);
		this.addField("SDRQ",OP_DATE);
		this.addField("SDZ",OP_STRING);
		this.addField("SJZ",OP_STRING);
		this.addField("HTID",OP_STRING);
		this.addField("HTZT",OP_STRING);
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
		this.addField("SFWTH",OP_STRING);
		this.addField("QHRQ",OP_DATE);
		this.addField("HTSX",OP_STRING);
		this.setFieldDateFormat("KGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("WCSJ","yyyy-MM-dd");
		this.setFieldDateFormat("KGSJ_JH","yyyy-MM-dd");
		this.setFieldDateFormat("WCSJ_JH","yyyy-MM-dd");
		this.setFieldDateFormat("CDYJ_PQ","yyyy-MM-dd");
		this.setFieldDateFormat("SSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("SDRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("QHRQ","yyyy-MM-dd");
		this.bindFieldToDic("ISJHWC", "SF");
		this.setVOTableName("GC_PQ_ZXM");
	}

	public void setGc_pq_zxm_id(String gc_pq_zxm_id){
		this.setInternal("GC_PQ_ZXM_ID",gc_pq_zxm_id);
	}
	public String getGc_pq_zxm_id(){
		return (String)this.getInternal("GC_PQ_ZXM_ID");
	}
	public void setJhid(String jhid){
		this.setInternal("JHID",jhid);
	}
	public String getJhid(){
		return (String)this.getInternal("JHID");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
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
	public void setGxlb(String gxlb){
		this.setInternal("GXLB",gxlb);
	}
	public String getGxlb(){
		return (String)this.getInternal("GXLB");
	}
	public void setZxmmc(String zxmmc){
		this.setInternal("ZXMMC",zxmmc);
	}
	public String getZxmmc(){
		return (String)this.getInternal("ZXMMC");
	}
	public void setPqdd(String pqdd){
		this.setInternal("PQDD",pqdd);
	}
	public String getPqdd(){
		return (String)this.getInternal("PQDD");
	}
	public void setPxh(String pxh){
		this.setInternal("PXH",pxh);
	}
	public String getPxh(){
		return (String)this.getInternal("PXH");
	}
	public void setPqfa(String pqfa){
		this.setInternal("PQFA",pqfa);
	}
	public String getPqfa(){
		return (String)this.getInternal("PQFA");
	}
	public void setJzfkid(String jzfkid){
		this.setInternal("JZFKID",jzfkid);
	}
	public String getJzfkid(){
		return (String)this.getInternal("JZFKID");
	}
	public void setJzqk(String jzqk){
		this.setInternal("JZQK",jzqk);
	}
	public String getJzqk(){
		return (String)this.getInternal("JZQK");
	}
	public void setSygzl(String sygzl){
		this.setInternal("SYGZL",sygzl);
	}
	public String getSygzl(){
		return (String)this.getInternal("SYGZL");
	}
	public void setCzwt(String czwt){
		this.setInternal("CZWT",czwt);
	}
	public String getCzwt(){
		return (String)this.getInternal("CZWT");
	}
	public void setJjfa(String jjfa){
		this.setInternal("JJFA",jjfa);
	}
	public String getJjfa(){
		return (String)this.getInternal("JJFA");
	}
	public void setIsjhwc(String isjhwc){
		this.setInternal("ISJHWC",isjhwc);
	}
	public String getIsjhwc(){
		return (String)this.getInternal("ISJHWC");
	}
	public void setKgsj(Date kgsj){
		this.setInternal("KGSJ",kgsj);
	}
	public Date getKgsj(){
		return (Date)this.getInternal("KGSJ");
	}
	public void setWcsj(Date wcsj){
		this.setInternal("WCSJ",wcsj);
	}
	public Date getWcsj(){
		return (Date)this.getInternal("WCSJ");
	}
	public void setKgsj_jh(Date kgsj_jh){
		this.setInternal("KGSJ_JH",kgsj_jh);
	}
	public Date getKgsj_jh(){
		return (Date)this.getInternal("KGSJ_JH");
	}
	public void setWcsj_jh(Date wcsj_jh){
		this.setInternal("WCSJ_JH",wcsj_jh);
	}
	public Date getWcsj_jh(){
		return (Date)this.getInternal("WCSJ_JH");
	}
	public void setCdyj_pq(Date cdyj_pq){
		this.setInternal("CDYJ_PQ",cdyj_pq);
	}
	public Date getCdyj_pq(){
		return (Date)this.getInternal("CDYJ_PQ");
	}
	public void setXmfzr(String xmfzr){
		this.setInternal("XMFZR",xmfzr);
	}
	public String getXmfzr(){
		return (String)this.getInternal("XMFZR");
	}
	public void setSsr(String ssr){
		this.setInternal("SSR",ssr);
	}
	public String getSsr(){
		return (String)this.getInternal("SSR");
	}
	public void setSsdw(String ssdw){
		this.setInternal("SSDW",ssdw);
	}
	public String getSsdw(){
		return (String)this.getInternal("SSDW");
	}
	public void setSsrq(Date ssrq){
		this.setInternal("SSRQ",ssrq);
	}
	public Date getSsrq(){
		return (Date)this.getInternal("SSRQ");
	}
	public void setSsdbh(String ssdbh){
		this.setInternal("SSDBH",ssdbh);
	}
	public String getSsdbh(){
		return (String)this.getInternal("SSDBH");
	}
	public void setLldbh(String lldbh){
		this.setInternal("LLDBH",lldbh);
	}
	public String getLldbh(){
		return (String)this.getInternal("LLDBH");
	}
	public void setPsbgbh(String psbgbh){
		this.setInternal("PSBGBH",psbgbh);
	}
	public String getPsbgbh(){
		return (String)this.getInternal("PSBGBH");
	}
	public void setWthbh(String wthbh){
		this.setInternal("WTHBH",wthbh);
	}
	public String getWthbh(){
		return (String)this.getInternal("WTHBH");
	}
	public void setSsz(String ssz){
		this.setInternal("SSZ",ssz);
	}
	public String getSsz(){
		return (String)this.getInternal("SSZ");
	}
	public void setLxr(String lxr){
		this.setInternal("LXR",lxr);
	}
	public String getLxr(){
		return (String)this.getInternal("LXR");
	}
	public void setLxdh(String lxdh){
		this.setInternal("LXDH",lxdh);
	}
	public String getLxdh(){
		return (String)this.getInternal("LXDH");
	}
	public void setSdrq(Date sdrq){
		this.setInternal("SDRQ",sdrq);
	}
	public Date getSdrq(){
		return (Date)this.getInternal("SDRQ");
	}
	public void setSdz(String sdz){
		this.setInternal("SDZ",sdz);
	}
	public String getSdz(){
		return (String)this.getInternal("SDZ");
	}
	public void setSjz(String sjz){
		this.setInternal("SJZ",sjz);
	}
	public String getSjz(){
		return (String)this.getInternal("SJZ");
	}
	public void setHtid(String htid){
		this.setInternal("HTID",htid);
	}
	public String getHtid(){
		return (String)this.getInternal("HTID");
	}
	public void setHtzt(String htzt){
		this.setInternal("HTZT",htzt);
	}
	public String getHtzt(){
		return (String)this.getInternal("HTZT");
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
	public void setSfwth(String sfwth){
		this.setInternal("SFWTH",sfwth);
	}
	public String getSfwth(){
		return (String)this.getInternal("SFWTH");
	}
	public void setQhrq(Date qhrq){
		this.setInternal("QHRQ",qhrq);
	}
	public Date getQhrq(){
		return (Date)this.getInternal("QHRQ");
	}
	public void setHtsx(String htsx){
		this.setInternal("HTSX",htsx);
	}
	public String getHtsx(){
		return (String)this.getInternal("HTSX");
	}
}