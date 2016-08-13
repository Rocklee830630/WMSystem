package com.ccthanking.business.htgl.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcHtglHtHtzfVO extends BaseVO {

    public GcHtglHtHtzfVO() {
        this.addField("ID", OP_STRING | this.TP_PK);// 编号
        this.addField("HTSJID", OP_STRING);// 合同数据编号
        this.addField("ZJZH", OP_STRING);// 资金账号
        this.addField("ZFNF", OP_STRING);// 支付年份
        this.addField("ZFYF", OP_STRING);// 支付月份
        this.addField("ZFRQ", OP_DATE);// 支付日期
        this.addField("ZFJE", OP_STRING);// 支付金额--自动计算
        this.addField("YFK", OP_STRING);// 应付款--自动计算
        this.addField("BCWCGZL", OP_STRING);// 本次完成工作量
        this.addField("BLYFK", OP_STRING);// 备料预付款
        this.addField("GCYFK", OP_STRING);// 工程预付款
        this.addField("FQTK", OP_STRING);// 付其他款
        this.addField("FHBXJ", OP_STRING);// 返回保修金
        this.addField("KKZH", OP_STRING);// 扣款总和--自动计算
        this.addField("KBLK", OP_STRING);// 扣备料款
        this.addField("KGZYFK", OP_STRING);// 扣工程预付款
        this.addField("KQTK", OP_STRING);// 扣其他款
        this.addField("KBXJ", OP_STRING);// 扣保修金
        this.addField("SFDZF", OP_STRING);// 是否待支付
        this.addField("ZFYT", OP_STRING);// 支付用途(征地、拆迁、其他)
        this.addField("DZFRQ", OP_DATE);// 待支付日期
        this.addField("HTDID", OP_STRING);// 会签单ID
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
        this.setFieldDateFormat("ZFRQ", "yyyy-MM-dd");
        this.setFieldDateFormat("DZFRQ", "yyyy-MM-dd");
        this.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
        this.setFieldDateFormat("GXSJ", "yyyy-MM-dd");
        this.setVOTableName("GC_HTGL_HT_HTZF");
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

    public void setZjzh(String zjzh) {
        this.setInternal("ZJZH", zjzh);
    }

    public String getZjzh() {
        return (String) this.getInternal("ZJZH");
    }

    public void setZfnf(String zfnf) {
        this.setInternal("ZFNF", zfnf);
    }

    public String getZfnf() {
        return (String) this.getInternal("ZFNF");
    }

    public void setZfyf(String zfyf) {
        this.setInternal("ZFYF", zfyf);
    }

    public String getZfyf() {
        return (String) this.getInternal("ZFYF");
    }

    public void setZfrq(Date zfrq) {
        this.setInternal("ZFRQ", zfrq);
    }

    public Date getZfrq() {
        return (Date) this.getInternal("ZFRQ");
    }

    public void setZfje(String zfje) {
        this.setInternal("ZFJE", zfje);
    }

    public String getZfje() {
        return (String) this.getInternal("ZFJE");
    }

    public void setYfk(String yfk) {
        this.setInternal("YFK", yfk);
    }

    public String getYfk() {
        return (String) this.getInternal("YFK");
    }

    public void setBcwcgzl(String bcwcgzl) {
        this.setInternal("BCWCGZL", bcwcgzl);
    }

    public String getBcwcgzl() {
        return (String) this.getInternal("BCWCGZL");
    }

    public void setBlyfk(String blyfk) {
        this.setInternal("BLYFK", blyfk);
    }

    public String getBlyfk() {
        return (String) this.getInternal("BLYFK");
    }

    public void setGcyfk(String gcyfk) {
        this.setInternal("GCYFK", gcyfk);
    }

    public String getGcyfk() {
        return (String) this.getInternal("GCYFK");
    }

    public void setFqtk(String fqtk) {
        this.setInternal("FQTK", fqtk);
    }

    public String getFqtk() {
        return (String) this.getInternal("FQTK");
    }

    public void setFhbxj(String fhbxj) {
        this.setInternal("FHBXJ", fhbxj);
    }

    public String getFhbxj() {
        return (String) this.getInternal("FHBXJ");
    }

    public void setKkzh(String kkzh) {
        this.setInternal("KKZH", kkzh);
    }

    public String getKkzh() {
        return (String) this.getInternal("KKZH");
    }

    public void setKblk(String kblk) {
        this.setInternal("KBLK", kblk);
    }

    public String getKblk() {
        return (String) this.getInternal("KBLK");
    }

    public void setKgzyfk(String kgzyfk) {
        this.setInternal("KGZYFK", kgzyfk);
    }

    public String getKgzyfk() {
        return (String) this.getInternal("KGZYFK");
    }

    public void setKqtk(String kqtk) {
        this.setInternal("KQTK", kqtk);
    }

    public String getKqtk() {
        return (String) this.getInternal("KQTK");
    }

    public void setKbxj(String kbxj) {
        this.setInternal("KBXJ", kbxj);
    }

    public String getKbxj() {
        return (String) this.getInternal("KBXJ");
    }

    public void setSfdzf(String sfdzf) {
        this.setInternal("SFDZF", sfdzf);
    }

    public String getSfdzf() {
        return (String) this.getInternal("SFDZF");
    }

    public void setZfyt(String zfyt) {
        this.setInternal("ZFYT", zfyt);
    }

    public String getZfyt() {
        return (String) this.getInternal("ZFYT");
    }

    public void setDzfrq(Date dzfrq) {
        this.setInternal("DZFRQ", dzfrq);
    }

    public Date getDzfrq() {
        return (Date) this.getInternal("DZFRQ");
    }

    public void setHtdid(String htdid) {
        this.setInternal("HTDID", htdid);
    }

    public String getHtdid() {
        return (String) this.getInternal("HTDID");
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
}