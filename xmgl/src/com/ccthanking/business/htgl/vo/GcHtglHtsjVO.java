package com.ccthanking.business.htgl.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcHtglHtsjVO extends BaseVO {

    public GcHtglHtsjVO() {
        this.addField("ID", OP_STRING | this.TP_PK);// 编号
        this.addField("JHSJID", OP_STRING);// 统筹计划数据ID
        this.addField("XMID", OP_STRING);// 项目编号
        this.addField("BDID", OP_STRING);// 标段ID
        this.addField("HTID", OP_STRING);// 合同编号
        this.addField("HTQDJ", OP_STRING);// 合同签订价(元)
        this.addField("GCJGBFB", OP_STRING);// 价格百分比
        this.addField("HTJSJ", OP_STRING);// 合同结算价或中止价
        this.addField("MBLX", OP_STRING);// 目标类型:变更、支付、完成投资、文件
        this.addField("MBID", OP_STRING);// 目标ID
        this.addField("JE", OP_STRING);// 金额
        this.addField("MBSJLX", OP_STRING);// 目标数据类型
        this.addField("RQ", OP_DATE);// 日期
        this.addField("MC", OP_STRING);// 名称
        this.addField("BGTS", OP_STRING);// 变更天数
        this.addField("HTDID", OP_STRING);// 会签单ID
        this.addField("HTZF", OP_STRING);// 合同支付
        this.addField("WCZF", OP_STRING);// 完成投资
        this.addField("BGJE", OP_STRING);// 变更金额
        this.addField("HTJS", OP_STRING);// 合同结算
        this.addField("ZXHTJ", OP_STRING);// 最新合同价
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
        this.setFieldDateFormat("RQ", "yyyy-MM-dd");
        this.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
        this.setFieldDateFormat("GXSJ", "yyyy-MM-dd");
        this.setVOTableName("GC_HTGL_HTSJ");
    }

    public void setId(String id) {
        this.setInternal("ID", id);
    }

    public String getId() {
        return (String) this.getInternal("ID");
    }

    public void setJhsjid(String jhsjid) {
        this.setInternal("JHSJID", jhsjid);
    }

    public String getJhsjid() {
        return (String) this.getInternal("JHSJID");
    }

    public void setXmid(String xmid) {
        this.setInternal("XMID", xmid);
    }

    public String getXmid() {
        return (String) this.getInternal("XMID");
    }

    public void setBdid(String bdid) {
        this.setInternal("BDID", bdid);
    }

    public String getBdid() {
        return (String) this.getInternal("BDID");
    }

    public void setHtid(String htid) {
        this.setInternal("HTID", htid);
    }

    public String getHtid() {
        return (String) this.getInternal("HTID");
    }

    public void setHtqdj(String htqdj) {
        this.setInternal("HTQDJ", htqdj);
    }

    public String getHtqdj() {
        return (String) this.getInternal("HTQDJ");
    }

    public void setGcjgbfb(String gcjgbfb) {
        this.setInternal("GCJGBFB", gcjgbfb);
    }

    public String getGcjgbfb() {
        return (String) this.getInternal("GCJGBFB");
    }

    public void setHtjsj(String htjsj) {
        this.setInternal("HTJSJ", htjsj);
    }

    public String getHtjsj() {
        return (String) this.getInternal("HTJSJ");
    }

    public void setMblx(String mblx) {
        this.setInternal("MBLX", mblx);
    }

    public String getMblx() {
        return (String) this.getInternal("MBLX");
    }

    public void setMbid(String mbid) {
        this.setInternal("MBID", mbid);
    }

    public String getMbid() {
        return (String) this.getInternal("MBID");
    }

    public void setJe(String je) {
        this.setInternal("JE", je);
    }

    public String getJe() {
        return (String) this.getInternal("JE");
    }

    public void setMbsjlx(String mbsjlx) {
        this.setInternal("MBSJLX", mbsjlx);
    }

    public String getMbsjlx() {
        return (String) this.getInternal("MBSJLX");
    }

    public void setRq(Date rq) {
        this.setInternal("RQ", rq);
    }

    public Date getRq() {
        return (Date) this.getInternal("RQ");
    }

    public void setMc(String mc) {
        this.setInternal("MC", mc);
    }

    public String getMc() {
        return (String) this.getInternal("MC");
    }

    public void setBgts(String bgts) {
        this.setInternal("BGTS", bgts);
    }

    public String getBgts() {
        return (String) this.getInternal("BGTS");
    }

    public void setHtdid(String htdid) {
        this.setInternal("HTDID", htdid);
    }

    public String getHtdid() {
        return (String) this.getInternal("HTDID");
    }

    public void setHtzf(String htzf) {
        this.setInternal("HTZF", htzf);
    }

    public String getHtzf() {
        return (String) this.getInternal("HTZF");
    }

    public void setWczf(String wczf) {
        this.setInternal("WCZF", wczf);
    }

    public String getWczf() {
        return (String) this.getInternal("WCZF");
    }

    public void setBgje(String bgje) {
        this.setInternal("BGJE", bgje);
    }

    public String getBgje() {
        return (String) this.getInternal("BGJE");
    }

    public void setHtjs(String htjs) {
        this.setInternal("HTJS", htjs);
    }

    public String getHtjs() {
        return (String) this.getInternal("HTJS");
    }

    public void setZxhtj(String zxhtj) {
        this.setInternal("ZXHTJ", zxhtj);
    }

    public String getZxhtj() {
        return (String) this.getInternal("ZXHTJ");
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