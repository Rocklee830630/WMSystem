package com.ccthanking.business.htgl.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcHtglHtWctzVO extends BaseVO {

    public GcHtglHtWctzVO() {
        this.addField("ID", OP_STRING | this.TP_PK);// 编号
        this.addField("HTSJID", OP_STRING);// 合同数据编号
        this.addField("NF", OP_STRING);// 年份
        this.addField("YF", OP_STRING);// 月份
        this.addField("WCTZJE", OP_STRING);// 完成投资金额
        this.addField("WCTZLX", OP_STRING);// 完成投资类型(征地、拆迁、其他)
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
        this.addField("HTZFID", OP_STRING);// 合同支付ID
        this.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
        this.setFieldDateFormat("GXSJ", "yyyy-MM-dd");
        this.setVOTableName("GC_HTGL_HT_WCTZ");
    }

    public void setId(String id) {
        this.setInternal("ID", id);
    }

    public String getId() {
        return (String) this.getInternal("ID");
    }

    public void setHtsjid(String htsjid) {
        this.setInternal("HTSJID", htsjid);
    }

    public String getHtsjid() {
        return (String) this.getInternal("HTSJID");
    }

    public void setNf(String nf) {
        this.setInternal("NF", nf);
    }

    public String getNf() {
        return (String) this.getInternal("NF");
    }

    public void setYf(String yf) {
        this.setInternal("YF", yf);
    }

    public String getYf() {
        return (String) this.getInternal("YF");
    }

    public void setWctzje(String wctzje) {
        this.setInternal("WCTZJE", wctzje);
    }

    public String getWctzje() {
        return (String) this.getInternal("WCTZJE");
    }

    public void setWctzlx(String wctzlx) {
        this.setInternal("WCTZLX", wctzlx);
    }

    public String getWctzlx() {
        return (String) this.getInternal("WCTZLX");
    }

    public void setYwlx(String ywlx) {
        this.setInternal("YWLX", ywlx);
    }

    public String getYwlx() {
        return (String) this.getInternal("YWLX");
    }

    public void setSjbh(String sjbh) {
        this.setInternal("SJBH", sjbh);
    }

    public String getSjbh() {
        return (String) this.getInternal("SJBH");
    }

    public void setSjmj(String sjmj) {
        this.setInternal("SJMJ", sjmj);
    }

    public String getSjmj() {
        return (String) this.getInternal("SJMJ");
    }

    public void setSfyx(String sfyx) {
        this.setInternal("SFYX", sfyx);
    }

    public String getSfyx() {
        return (String) this.getInternal("SFYX");
    }

    public void setBz(String bz) {
        this.setInternal("BZ", bz);
    }

    public String getBz() {
        return (String) this.getInternal("BZ");
    }

    public void setLrr(String lrr) {
        this.setInternal("LRR", lrr);
    }

    public String getLrr() {
        return (String) this.getInternal("LRR");
    }

    public void setLrsj(Date lrsj) {
        this.setInternal("LRSJ", lrsj);
    }

    public Date getLrsj() {
        return (Date) this.getInternal("LRSJ");
    }

    public void setLrbm(String lrbm) {
        this.setInternal("LRBM", lrbm);
    }

    public String getLrbm() {
        return (String) this.getInternal("LRBM");
    }

    public void setLrbmmc(String lrbmmc) {
        this.setInternal("LRBMMC", lrbmmc);
    }

    public String getLrbmmc() {
        return (String) this.getInternal("LRBMMC");
    }

    public void setGxr(String gxr) {
        this.setInternal("GXR", gxr);
    }

    public String getGxr() {
        return (String) this.getInternal("GXR");
    }

    public void setGxsj(Date gxsj) {
        this.setInternal("GXSJ", gxsj);
    }

    public Date getGxsj() {
        return (Date) this.getInternal("GXSJ");
    }

    public void setGxbm(String gxbm) {
        this.setInternal("GXBM", gxbm);
    }

    public String getGxbm() {
        return (String) this.getInternal("GXBM");
    }

    public void setGxbmmc(String gxbmmc) {
        this.setInternal("GXBMMC", gxbmmc);
    }

    public String getGxbmmc() {
        return (String) this.getInternal("GXBMMC");
    }

    public void setSortno(String sortno) {
        this.setInternal("SORTNO", sortno);
    }

    public String getSortno() {
        return (String) this.getInternal("SORTNO");
    }

    public void setHtzfid(String htzfid) {
        this.setInternal("HTZFID", htzfid);
    }

    public String getHtzfid() {
        return (String) this.getInternal("HTZFID");
    }
}