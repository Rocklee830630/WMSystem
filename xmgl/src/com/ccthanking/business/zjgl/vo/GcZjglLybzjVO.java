package com.ccthanking.business.zjgl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZjglLybzjVO extends BaseVO{

	public GcZjglLybzjVO(){
		   this.addField("ID", OP_STRING | this.TP_PK);// 编号
	        this.addField("HTID", OP_STRING);// 合同编号
	        this.addField("JNFS", OP_STRING);// 交纳方式:现金、保函（则需要对方银行和始终日期）
	        this.addField("JE", OP_STRING);// 金额
	        this.addField("JNRQ", OP_DATE);// 交纳日期
	        this.addField("FHQK", OP_STRING);// 返还情况
	        this.addField("FHRQ", OP_DATE);// 返还日期
	        this.addField("BLR", OP_STRING);// 办理人
	        this.addField("PZBH", OP_STRING);// 凭证编号
	        this.addField("HTDID", OP_STRING);// 会签单ID
	        this.addField("DFYH", OP_STRING);// 对方银行
	        this.addField("BHYXQS", OP_DATE);// 保函有效期始
	        this.addField("BHYXQZ", OP_DATE);// 保函有效期终
	        this.addField("JHSJID", OP_STRING);// 统筹计划数据ID
	        this.addField("XMID", OP_STRING);// 项目编号
	        this.addField("BDID", OP_STRING);// 标段ID
	        this.addField("TXRQ", OP_DATE);// 提醒日期
	        this.addField("JNDW", OP_STRING);// 交纳单位
	        this.addField("FHR", OP_STRING);// 返还人
	        this.addField("YWLX", OP_STRING);// 业务类型
	        this.addField("SJBH", OP_STRING);// 事件编号
	        this.addField("SJMJ", OP_STRING);// 数据密级
	        this.addField("SFYX", OP_STRING);// 是否有效
	        this.addField("BZ", OP_STRING);// 备注
	        this.addField("LRR", OP_STRING);// 录入人
	        this.addField("LRSJ", OP_DATE);// 录入时间
	        this.addField("LRBM", OP_STRING);// 录入部门
	        this.addField("LRBMMC", OP_STRING);// 录入部门名称
	        this.addField("GXR", OP_STRING);// 更新人
	        this.addField("GXSJ", OP_DATE);// 更新时间
	        this.addField("GXBM", OP_STRING);// 更新部门
	        this.addField("GXBMMC", OP_STRING);// 更新部门名称
	        this.addField("SORTNO", OP_STRING);// 排序号
	        this.addField("BHLX", OP_STRING);// 保函类型0工程1施工
	        this.addField("ZTBID", OP_STRING);// 招投标ID
	        this.setFieldDateFormat("JNRQ", "yyyy-MM-dd");
	        this.setFieldDateFormat("FHRQ", "yyyy-MM-dd");
	        this.setFieldDateFormat("BHYXQS", "yyyy-MM-dd");
	        this.setFieldDateFormat("BHYXQZ", "yyyy-MM-dd");
	        this.setFieldDateFormat("TXRQ", "yyyy-MM-dd");
	        this.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
	        this.setFieldDateFormat("GXSJ", "yyyy-MM-dd");
	        

	        this.bindFieldToThousand("JE");// 金额

	        this.bindFieldToUserid("BLR");

	        this.setVOTableName("GC_ZJGL_LYBZJ");
	}

	public void setId(String id){
		this.setInternal("ID",id);
	}
	public String getId(){
		return (String)this.getInternal("ID");
	}
	public void setHtid(String htid){
		this.setInternal("HTID",htid);
	}
	public String getHtid(){
		return (String)this.getInternal("HTID");
	}
	public void setJnfs(String jnfs){
		this.setInternal("JNFS",jnfs);
	}
	public String getJnfs(){
		return (String)this.getInternal("JNFS");
	}
	public void setJe(String je){
		this.setInternal("JE",je);
	}
	public String getJe(){
		return (String)this.getInternal("JE");
	}
	public void setJnrq(Date jnrq){
		this.setInternal("JNRQ",jnrq);
	}
	public Date getJnrq(){
		return (Date)this.getInternal("JNRQ");
	}
	public void setFhqk(String fhqk){
		this.setInternal("FHQK",fhqk);
	}
	public String getFhqk(){
		return (String)this.getInternal("FHQK");
	}
	public void setFhrq(Date fhrq){
		this.setInternal("FHRQ",fhrq);
	}
	public Date getFhrq(){
		return (Date)this.getInternal("FHRQ");
	}
	public void setBlr(String blr){
		this.setInternal("BLR",blr);
	}
	public String getBlr(){
		return (String)this.getInternal("BLR");
	}
	public void setPzbh(String pzbh){
		this.setInternal("PZBH",pzbh);
	}
	public String getPzbh(){
		return (String)this.getInternal("PZBH");
	}
	public void setHtdid(String htdid){
		this.setInternal("HTDID",htdid);
	}
	public String getHtdid(){
		return (String)this.getInternal("HTDID");
	}
	public void setDfyh(String dfyh){
		this.setInternal("DFYH",dfyh);
	}
	public String getDfyh(){
		return (String)this.getInternal("DFYH");
	}
	public void setBhyxqs(Date bhyxqs){
		this.setInternal("BHYXQS",bhyxqs);
	}
	public Date getBhyxqs(){
		return (Date)this.getInternal("BHYXQS");
	}
	public void setBhyxqz(Date bhyxqz){
		this.setInternal("BHYXQZ",bhyxqz);
	}
	public Date getBhyxqz(){
		return (Date)this.getInternal("BHYXQZ");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
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
	public void setTxrq(Date txrq){
		this.setInternal("TXRQ",txrq);
	}
	public Date getTxrq(){
		return (Date)this.getInternal("TXRQ");
	}
	public void setJndw(String jndw){
		this.setInternal("JNDW",jndw);
	}
	public String getJndw(){
		return (String)this.getInternal("JNDW");
	}
	public void setFhr(String fhr){
		this.setInternal("FHR",fhr);
	}
	public String getFhr(){
		return (String)this.getInternal("FHR");
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
	public void setBhlx(String bhlx){
		this.setInternal("BHLX",bhlx);
	}
	public String getBhlx(){
		return (String)this.getInternal("BHLX");
	}
	public void setZtbid(String ztbid){
		this.setInternal("ZTBID",ztbid);
	}
	public String getZtbid(){
		return (String)this.getInternal("ZTBID");
	}
}