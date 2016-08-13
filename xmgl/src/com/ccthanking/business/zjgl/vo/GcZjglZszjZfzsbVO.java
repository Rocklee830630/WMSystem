package com.ccthanking.business.zjgl.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZjglZszjZfzsbVO extends BaseVO {

    public GcZjglZszjZfzsbVO() {
        this.addField("ID", OP_STRING | this.TP_PK);// 编号
        this.addField("ZFRQ", OP_DATE);// 支付日期
        this.addField("ZFJE", OP_STRING);// 支付金额--自动计算
        this.addField("LXR", OP_STRING);// 联系人
        this.addField("LXFS", OP_STRING);// 联系方式
        this.addField("PZBH", OP_STRING);// 凭证编号
        this.addField("HTDID", OP_STRING);// 会签单ID
        this.addField("QY", OP_STRING);// 区域
        this.addField("BKDSPB", OP_STRING);// 拨款单审批表
        this.addField("BKXGDID", OP_STRING);// 拨款相关单ID
        this.addField("JHSJID", OP_STRING);// 统筹计划数据ID
        this.addField("JBR", OP_STRING);// 经办人
        this.addField("BFDW", OP_STRING);// 拔付单位
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
        this.addField("HTSJID", OP_STRING);// 合同数据编号
        this.setFieldDateFormat("ZFRQ", "yyyy-MM-dd");
        this.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
        this.setFieldDateFormat("GXSJ", "yyyy-MM-dd");

        this.bindFieldToThousand("ZFJE");// 支付金额--自动计算

        this.setVOTableName("GC_ZJGL_ZSZJ_ZFZSB");
    }

    public void setId(String id) {
        this.setInternal("ID", id);
    }

    public String getId() {
        return (String) this.getInternal("ID");
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

    public void setLxr(String lxr) {
        this.setInternal("LXR", lxr);
    }

    public String getLxr() {
        return (String) this.getInternal("LXR");
    }

    public void setLxfs(String lxfs) {
        this.setInternal("LXFS", lxfs);
    }

    public String getLxfs() {
        return (String) this.getInternal("LXFS");
    }

    public void setPzbh(String pzbh) {
        this.setInternal("PZBH", pzbh);
    }

    public String getPzbh() {
        return (String) this.getInternal("PZBH");
    }

    public void setHtdid(String htdid) {
        this.setInternal("HTDID", htdid);
    }

    public String getHtdid() {
        return (String) this.getInternal("HTDID");
    }

    public void setQy(String qy) {
        this.setInternal("QY", qy);
    }

    public String getQy() {
        return (String) this.getInternal("QY");
    }

    public void setBkdspb(String bkdspb) {
        this.setInternal("BKDSPB", bkdspb);
    }

    public String getBkdspb() {
        return (String) this.getInternal("BKDSPB");
    }

    public void setBkxgdid(String bkxgdid) {
        this.setInternal("BKXGDID", bkxgdid);
    }

    public String getBkxgdid() {
        return (String) this.getInternal("BKXGDID");
    }

    public void setJhsjid(String jhsjid) {
        this.setInternal("JHSJID", jhsjid);
    }

    public String getJhsjid() {
        return (String) this.getInternal("JHSJID");
    }

    public void setJbr(String jbr) {
        this.setInternal("JBR", jbr);
    }

    public String getJbr() {
        return (String) this.getInternal("JBR");
    }

    public void setBfdw(String bfdw) {
        this.setInternal("BFDW", bfdw);
    }

    public String getBfdw() {
        return (String) this.getInternal("BFDW");
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

    public void setHtsjid(String htsjid) {
        this.setInternal("HTSJID", htsjid);
    }

    public String getHtsjid() {
        return (String) this.getInternal("HTSJID");
    }
}