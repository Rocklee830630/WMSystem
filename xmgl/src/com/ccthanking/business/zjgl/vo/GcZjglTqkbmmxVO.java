package com.ccthanking.business.zjgl.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZjglTqkbmmxVO extends BaseVO {

    public GcZjglTqkbmmxVO() {
        this.addField("ID", OP_STRING | this.TP_PK);// 编号
        this.addField("HTID", OP_STRING);// 合同编号
        this.addField("TQKID", OP_STRING);// 提请款编号
        this.addField("DWID", OP_STRING);// 单位ID
        this.addField("DWMC", OP_STRING);// 单位全称
        this.addField("XMMCNR", OP_STRING);// 项目名称内容
        this.addField("HTBM", OP_STRING);// 合同编码
        this.addField("ZXHTJ", OP_STRING);// 最新合同价
        this.addField("YBF", OP_STRING);// 已拔付
        this.addField("BCSQ", OP_STRING);// 本次申请拔款
        this.addField("LJBF", OP_STRING);// 累计拔付
        this.addField("AHTBFB", OP_STRING);// 按合同付款比例
        this.addField("CSZ", OP_STRING);// 财审值
        this.addField("JZQR", OP_STRING);// 监理确认计量款
        this.addField("AJLFKB", OP_STRING);// 按计量付款比例
        this.addField("BMBLR", OP_STRING);// 部门办理人
        this.addField("BMSPR", OP_STRING);// 部门审批人
        this.addField("BMTQKMXZT", OP_STRING);// 明细状态
        this.addField("CWBLR", OP_STRING);// 财务办理人
        this.addField("JCBLR", OP_STRING);// 计财办理人
        this.addField("CWSHZ", OP_STRING);// 财务审核值
        this.addField("JCHDZ", OP_STRING);// 计财核定值
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
        this.addField("JCPCH", OP_STRING);// 计财批次号
        this.addField("JCZFRQ", OP_DATE);// 计财支付日期
        this.addField("JGC", OP_STRING);// 甲供材
        this.addField("KQJGC", OP_STRING);// 扣除甲供材

        this.setFieldDateFormat("JCZFRQ", "yyyy-MM-dd");
        this.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
        this.setFieldDateFormat("GXSJ", "yyyy-MM-dd");

        this.bindFieldToThousand("ZXHTJ");// 最新合同价
        this.bindFieldToThousand("YBF");// 已拔付
        this.bindFieldToThousand("BCSQ");// 本次申请拔款
        this.bindFieldToThousand("LJBF");// 累计拔付
        this.bindFieldToThousand("CSZ");// 财审值
        this.bindFieldToThousand("JZQR");// 监理确认计量款
        this.bindFieldToThousand("CWSHZ");// 财务审核值
        this.bindFieldToThousand("JCHDZ");// 计财核定值
        this.bindFieldToThousand("JGC");// 计财核定值
        this.bindFieldToThousand("KQJGC");// 计财核定值

        this.setVOTableName("GC_ZJGL_TQKBMMX");
    }

    public void setId(String id) {
        this.setInternal("ID", id);
    }

    public String getId() {
        return (String) this.getInternal("ID");
    }

    public void setHtid(String htid) {
        this.setInternal("HTID", htid);
    }

    public String getHtid() {
        return (String) this.getInternal("HTID");
    }

    public void setTqkid(String tqkid) {
        this.setInternal("TQKID", tqkid);
    }

    public String getTqkid() {
        return (String) this.getInternal("TQKID");
    }

    public void setDwid(String dwid) {
        this.setInternal("DWID", dwid);
    }

    public String getDwid() {
        return (String) this.getInternal("DWID");
    }

    public void setDwmc(String dwmc) {
        this.setInternal("DWMC", dwmc);
    }

    public String getDwmc() {
        return (String) this.getInternal("DWMC");
    }

    public void setXmmcnr(String xmmcnr) {
        this.setInternal("XMMCNR", xmmcnr);
    }

    public String getXmmcnr() {
        return (String) this.getInternal("XMMCNR");
    }

    public void setHtbm(String htbm) {
        this.setInternal("HTBM", htbm);
    }

    public String getHtbm() {
        return (String) this.getInternal("HTBM");
    }

    public void setZxhtj(String zxhtj) {
        this.setInternal("ZXHTJ", zxhtj);
    }

    public String getZxhtj() {
        return (String) this.getInternal("ZXHTJ");
    }

    public void setYbf(String ybf) {
        this.setInternal("YBF", ybf);
    }

    public String getYbf() {
        return (String) this.getInternal("YBF");
    }

    public void setBcsq(String bcsq) {
        this.setInternal("BCSQ", bcsq);
    }

    public String getBcsq() {
        return (String) this.getInternal("BCSQ");
    }

    public void setLjbf(String ljbf) {
        this.setInternal("LJBF", ljbf);
    }

    public String getLjbf() {
        return (String) this.getInternal("LJBF");
    }

    public void setAhtbfb(String ahtbfb) {
        this.setInternal("AHTBFB", ahtbfb);
    }

    public String getAhtbfb() {
        return (String) this.getInternal("AHTBFB");
    }

    public void setCsz(String csz) {
        this.setInternal("CSZ", csz);
    }

    public String getCsz() {
        return (String) this.getInternal("CSZ");
    }

    public void setJzqr(String jzqr) {
        this.setInternal("JZQR", jzqr);
    }

    public String getJzqr() {
        return (String) this.getInternal("JZQR");
    }

    public void setAjlfkb(String ajlfkb) {
        this.setInternal("AJLFKB", ajlfkb);
    }

    public String getAjlfkb() {
        return (String) this.getInternal("AJLFKB");
    }

    public void setBmblr(String bmblr) {
        this.setInternal("BMBLR", bmblr);
    }

    public String getBmblr() {
        return (String) this.getInternal("BMBLR");
    }

    public void setBmspr(String bmspr) {
        this.setInternal("BMSPR", bmspr);
    }

    public String getBmspr() {
        return (String) this.getInternal("BMSPR");
    }

    public void setBmtqkmxzt(String bmtqkmxzt) {
        this.setInternal("BMTQKMXZT", bmtqkmxzt);
    }

    public String getBmtqkmxzt() {
        return (String) this.getInternal("BMTQKMXZT");
    }

    public void setCwblr(String cwblr) {
        this.setInternal("CWBLR", cwblr);
    }

    public String getCwblr() {
        return (String) this.getInternal("CWBLR");
    }

    public void setJcblr(String jcblr) {
        this.setInternal("JCBLR", jcblr);
    }

    public String getJcblr() {
        return (String) this.getInternal("JCBLR");
    }

    public void setCwshz(String cwshz) {
        this.setInternal("CWSHZ", cwshz);
    }

    public String getCwshz() {
        return (String) this.getInternal("CWSHZ");
    }

    public void setJchdz(String jchdz) {
        this.setInternal("JCHDZ", jchdz);
    }

    public String getJchdz() {
        return (String) this.getInternal("JCHDZ");
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

    public void setJcpch(String jcpch) {
        this.setInternal("JCPCH", jcpch);
    }

    public String getJcpch() {
        return (String) this.getInternal("JCPCH");
    }

    public void setJczfrq(Date jczfrq) {
        this.setInternal("JCZFRQ", jczfrq);
    }

    public Date getJczfrq() {
        return (Date) this.getInternal("JCZFRQ");
    }

    public void setJgc(String jgc) {
        this.setInternal("JGC", jgc);
    }

    public String getJgc() {
        return (String) this.getInternal("JGC");
    }

    public void setKqjgc(String kqjgc) {
        this.setInternal("KQJGC", kqjgc);
    }

    public String getKqjgc() {
        return (String) this.getInternal("KQJGC");
    }
}