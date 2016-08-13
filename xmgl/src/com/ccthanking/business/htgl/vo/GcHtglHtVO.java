package com.ccthanking.business.htgl.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcHtglHtVO extends BaseVO {

    public GcHtglHtVO() {
        this.addField("ID", OP_STRING | this.TP_PK);// 编号
        this.addField("HTLX", OP_STRING);// 合同类型
        this.addField("ZTBID", OP_STRING);// 招投标编号
        this.addField("HTBM", OP_STRING);// 合同编码
        this.addField("HTMC", OP_STRING);// 合同名称
        this.addField("SFXNHT", OP_STRING);// 是否虚拟合同
        this.addField("HTZT", OP_STRING);// 合同状态，履行中、已结算、已结束、已中止
        this.addField("SFSDKLXHT", OP_STRING);// 是否是贷款利息合同，Y为是
        this.addField("FBFS", OP_STRING);// 发包方式：公开招标、邀请招标、内部比选、直接委托
        this.addField("HTSJKSRQ", OP_DATE);// 合同实际开始日期
        this.addField("HTJSRQ", OP_DATE);// 合同结束日期
        this.addField("HTJQDRQ", OP_DATE);// 合同签订日期
        this.addField("YHSJE", OP_STRING);// 印花税金额(元)
        this.addField("YHSL", OP_STRING);// 印花税率
        this.addField("QHSSFYT", OP_STRING);// 印花税是否已贴：未贴为N、已贴为Y、不需要贴为空
        this.addField("BXJE", OP_STRING);// 保修金额
        this.addField("BXJL", OP_STRING);// 保修金率
        this.addField("BXQ", OP_STRING);// 保修期--数字
        this.addField("BXQDWLX", OP_STRING);// 保修期单位：年、季、月、日
        this.addField("JFDW", OP_STRING);// 甲方单位
        this.addField("ZBBM", OP_STRING);// 主办部门
        this.addField("JFJBR", OP_STRING);// 甲方经办人
        this.addField("JFQDR", OP_STRING);// 甲方签订人
        this.addField("YFDW", OP_STRING);// 乙方单位
        this.addField("YFDWQDR", OP_STRING);// 乙方单位签订人
        this.addField("YFDW2", OP_STRING);// 乙方单位2
        this.addField("YFDWQDR2", OP_STRING);// 乙方单位签订人2
        this.addField("YFDW3", OP_STRING);// 乙方单位3
        this.addField("YFDWQDR3", OP_STRING);// 乙方单位签订人3
        this.addField("DBDW", OP_STRING);// 担保单位
        this.addField("HTFS", OP_STRING);// 合同份数
        this.addField("HTZYNR", OP_STRING);// 合同主要内容
        this.addField("HTWYTK", OP_STRING);// 合同违约条款
        this.addField("HTFKFS", OP_STRING);// 合同付款方式
        this.addField("HTZSYJ", OP_STRING);// 合同结束依据
        this.addField("HTZZLY", OP_STRING);// 合同中止理由
        this.addField("QDNF", OP_STRING);// 签订年份
        this.addField("BJXS", OP_STRING);// 报价系数
        this.addField("YFID", OP_STRING);// 乙方uid
        this.addField("ZHTQDJ", OP_STRING);// 总合同签订价(元)
        this.addField("ZHTZF", OP_STRING);// 总合同支付
        this.addField("ZWCZF", OP_STRING);// 总完成投资
        this.addField("ZBGJE", OP_STRING);// 总变更金额
        this.addField("ZHTJS", OP_STRING);// 总合同结算
        this.addField("ZZXHTJ", OP_STRING);// 总最新合同价
        this.addField("ZJSRQ", OP_DATE);// 实际结算日期
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
        this.addField("ZBBMID", OP_STRING);// 主办部门ID
        this.addField("JFID", OP_STRING);// 甲方ID
        this.addField("JFJBRID", OP_STRING);// 甲方经办人ID
        this.addField("JFRDRID", OP_STRING);// 甲方签订人ID
        this.addField("YF2ID", OP_STRING);// 乙方单位2ID
        this.addField("YF3ID", OP_STRING);// 乙方单位2ID
        this.addField("SFDXMHT", OP_STRING);// 是否单项目合同
        this.addField("SFZFJTZ", OP_STRING);// 是否支付即投资
        this.setFieldDateFormat("HTSJKSRQ", "yyyy-MM-dd");
        this.setFieldDateFormat("HTJSRQ", "yyyy-MM-dd");
        this.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");
        this.setFieldDateFormat("ZJSRQ", "yyyy-MM-dd");
        this.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
        this.setFieldDateFormat("GXSJ", "yyyy-MM-dd");

        // 字典
        this.bindFieldToDic("HTLX", "HTLX");
        this.bindFieldToDic("HTZT", "HTRXZT");// 合同签订状态
        this.bindFieldToDic("FBFS", "FBFS");// 发包方式
        this.bindFieldToDic("BXQDWLX", "BXQDW");// 保修期单位：年、季、月、日
        this.bindFieldToDic("QDNF", "XMNF");// 项目年份
        this.bindFieldToDic("SFXNHT", "SF");// 是否虚拟合同

        this.bindFieldToThousand("YHSJE");// 印花税金额(元)
        this.bindFieldToThousand("BXJE");// 保修金额
        this.bindFieldToThousand("ZHTQDJ");// 总合同签订价(元)
        this.bindFieldToThousand("ZHTZF");// 总合同支付
        this.bindFieldToThousand("ZWCZF");// 总完成投资
        this.bindFieldToThousand("ZBGJE");// 总变更金额
        this.bindFieldToThousand("ZHTJS");// 总合同结算
        this.bindFieldToThousand("ZZXHTJ");// 总最新合同价

        this.setVOTableName("GC_HTGL_HT");
    }

    public void setId(String id) {
        this.setInternal("ID", id);
    }

    public String getId() {
        return (String) this.getInternal("ID");
    }

    public void setHtlx(String htlx) {
        this.setInternal("HTLX", htlx);
    }

    public String getHtlx() {
        return (String) this.getInternal("HTLX");
    }

    public void setZtbid(String ztbid) {
        this.setInternal("ZTBID", ztbid);
    }

    public String getZtbid() {
        return (String) this.getInternal("ZTBID");
    }

    public void setHtbm(String htbm) {
        this.setInternal("HTBM", htbm);
    }

    public String getHtbm() {
        return (String) this.getInternal("HTBM");
    }

    public void setHtmc(String htmc) {
        this.setInternal("HTMC", htmc);
    }

    public String getHtmc() {
        return (String) this.getInternal("HTMC");
    }

    public void setSfxnht(String sfxnht) {
        this.setInternal("SFXNHT", sfxnht);
    }

    public String getSfxnht() {
        return (String) this.getInternal("SFXNHT");
    }

    public void setHtzt(String htzt) {
        this.setInternal("HTZT", htzt);
    }

    public String getHtzt() {
        return (String) this.getInternal("HTZT");
    }

    public void setSfsdklxht(String sfsdklxht) {
        this.setInternal("SFSDKLXHT", sfsdklxht);
    }

    public String getSfsdklxht() {
        return (String) this.getInternal("SFSDKLXHT");
    }

    public void setFbfs(String fbfs) {
        this.setInternal("FBFS", fbfs);
    }

    public String getFbfs() {
        return (String) this.getInternal("FBFS");
    }

    public void setHtsjksrq(Date htsjksrq) {
        this.setInternal("HTSJKSRQ", htsjksrq);
    }

    public Date getHtsjksrq() {
        return (Date) this.getInternal("HTSJKSRQ");
    }

    public void setHtjsrq(Date htjsrq) {
        this.setInternal("HTJSRQ", htjsrq);
    }

    public Date getHtjsrq() {
        return (Date) this.getInternal("HTJSRQ");
    }

    public void setHtjqdrq(Date htjqdrq) {
        this.setInternal("HTJQDRQ", htjqdrq);
    }

    public Date getHtjqdrq() {
        return (Date) this.getInternal("HTJQDRQ");
    }

    public void setYhsje(String yhsje) {
        this.setInternal("YHSJE", yhsje);
    }

    public String getYhsje() {
        return (String) this.getInternal("YHSJE");
    }

    public void setYhsl(String yhsl) {
        this.setInternal("YHSL", yhsl);
    }

    public String getYhsl() {
        return (String) this.getInternal("YHSL");
    }

    public void setQhssfyt(String qhssfyt) {
        this.setInternal("QHSSFYT", qhssfyt);
    }

    public String getQhssfyt() {
        return (String) this.getInternal("QHSSFYT");
    }

    public void setBxje(String bxje) {
        this.setInternal("BXJE", bxje);
    }

    public String getBxje() {
        return (String) this.getInternal("BXJE");
    }

    public void setBxjl(String bxjl) {
        this.setInternal("BXJL", bxjl);
    }

    public String getBxjl() {
        return (String) this.getInternal("BXJL");
    }

    public void setBxq(String bxq) {
        this.setInternal("BXQ", bxq);
    }

    public String getBxq() {
        return (String) this.getInternal("BXQ");
    }

    public void setBxqdwlx(String bxqdwlx) {
        this.setInternal("BXQDWLX", bxqdwlx);
    }

    public String getBxqdwlx() {
        return (String) this.getInternal("BXQDWLX");
    }

    public void setJfdw(String jfdw) {
        this.setInternal("JFDW", jfdw);
    }

    public String getJfdw() {
        return (String) this.getInternal("JFDW");
    }

    public void setZbbm(String zbbm) {
        this.setInternal("ZBBM", zbbm);
    }

    public String getZbbm() {
        return (String) this.getInternal("ZBBM");
    }

    public void setJfjbr(String jfjbr) {
        this.setInternal("JFJBR", jfjbr);
    }

    public String getJfjbr() {
        return (String) this.getInternal("JFJBR");
    }

    public void setJfqdr(String jfqdr) {
        this.setInternal("JFQDR", jfqdr);
    }

    public String getJfqdr() {
        return (String) this.getInternal("JFQDR");
    }

    public void setYfdw(String yfdw) {
        this.setInternal("YFDW", yfdw);
    }

    public String getYfdw() {
        return (String) this.getInternal("YFDW");
    }

    public void setYfdwqdr(String yfdwqdr) {
        this.setInternal("YFDWQDR", yfdwqdr);
    }

    public String getYfdwqdr() {
        return (String) this.getInternal("YFDWQDR");
    }

    public void setYfdw2(String yfdw2) {
        this.setInternal("YFDW2", yfdw2);
    }

    public String getYfdw2() {
        return (String) this.getInternal("YFDW2");
    }

    public void setYfdwqdr2(String yfdwqdr2) {
        this.setInternal("YFDWQDR2", yfdwqdr2);
    }

    public String getYfdwqdr2() {
        return (String) this.getInternal("YFDWQDR2");
    }

    public void setYfdw3(String yfdw3) {
        this.setInternal("YFDW3", yfdw3);
    }

    public String getYfdw3() {
        return (String) this.getInternal("YFDW3");
    }

    public void setYfdwqdr3(String yfdwqdr3) {
        this.setInternal("YFDWQDR3", yfdwqdr3);
    }

    public String getYfdwqdr3() {
        return (String) this.getInternal("YFDWQDR3");
    }

    public void setDbdw(String dbdw) {
        this.setInternal("DBDW", dbdw);
    }

    public String getDbdw() {
        return (String) this.getInternal("DBDW");
    }

    public void setHtfs(String htfs) {
        this.setInternal("HTFS", htfs);
    }

    public String getHtfs() {
        return (String) this.getInternal("HTFS");
    }

    public void setHtzynr(String htzynr) {
        this.setInternal("HTZYNR", htzynr);
    }

    public String getHtzynr() {
        return (String) this.getInternal("HTZYNR");
    }

    public void setHtwytk(String htwytk) {
        this.setInternal("HTWYTK", htwytk);
    }

    public String getHtwytk() {
        return (String) this.getInternal("HTWYTK");
    }

    public void setHtfkfs(String htfkfs) {
        this.setInternal("HTFKFS", htfkfs);
    }

    public String getHtfkfs() {
        return (String) this.getInternal("HTFKFS");
    }

    public void setHtzsyj(String htzsyj) {
        this.setInternal("HTZSYJ", htzsyj);
    }

    public String getHtzsyj() {
        return (String) this.getInternal("HTZSYJ");
    }

    public void setHtzzly(String htzzly) {
        this.setInternal("HTZZLY", htzzly);
    }

    public String getHtzzly() {
        return (String) this.getInternal("HTZZLY");
    }

    public void setQdnf(String qdnf) {
        this.setInternal("QDNF", qdnf);
    }

    public String getQdnf() {
        return (String) this.getInternal("QDNF");
    }

    public void setBjxs(String bjxs) {
        this.setInternal("BJXS", bjxs);
    }

    public String getBjxs() {
        return (String) this.getInternal("BJXS");
    }

    public void setYfid(String yfid) {
        this.setInternal("YFID", yfid);
    }

    public String getYfid() {
        return (String) this.getInternal("YFID");
    }

    public void setZhtqdj(String zhtqdj) {
        this.setInternal("ZHTQDJ", zhtqdj);
    }

    public String getZhtqdj() {
        return (String) this.getInternal("ZHTQDJ");
    }

    public void setZhtzf(String zhtzf) {
        this.setInternal("ZHTZF", zhtzf);
    }

    public String getZhtzf() {
        return (String) this.getInternal("ZHTZF");
    }

    public void setZwczf(String zwczf) {
        this.setInternal("ZWCZF", zwczf);
    }

    public String getZwczf() {
        return (String) this.getInternal("ZWCZF");
    }

    public void setZbgje(String zbgje) {
        this.setInternal("ZBGJE", zbgje);
    }

    public String getZbgje() {
        return (String) this.getInternal("ZBGJE");
    }

    public void setZhtjs(String zhtjs) {
        this.setInternal("ZHTJS", zhtjs);
    }

    public String getZhtjs() {
        return (String) this.getInternal("ZHTJS");
    }

    public void setZzxhtj(String zzxhtj) {
        this.setInternal("ZZXHTJ", zzxhtj);
    }

    public String getZzxhtj() {
        return (String) this.getInternal("ZZXHTJ");
    }

    public void setZjsrq(Date zjsrq) {
        this.setInternal("ZJSRQ", zjsrq);
    }

    public Date getZjsrq() {
        return (Date) this.getInternal("ZJSRQ");
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

    public void setZbbmid(String zbbmid) {
        this.setInternal("ZBBMID", zbbmid);
    }

    public String getZbbmid() {
        return (String) this.getInternal("ZBBMID");
    }

    public void setJfid(String jfid) {
        this.setInternal("JFID", jfid);
    }

    public String getJfid() {
        return (String) this.getInternal("JFID");
    }

    public void setJfjbrid(String jfjbrid) {
        this.setInternal("JFJBRID", jfjbrid);
    }

    public String getJfjbrid() {
        return (String) this.getInternal("JFJBRID");
    }

    public void setJfrdrid(String jfrdrid) {
        this.setInternal("JFRDRID", jfrdrid);
    }

    public String getJfrdrid() {
        return (String) this.getInternal("JFRDRID");
    }

    public void setYf2id(String yf2id) {
        this.setInternal("YF2ID", yf2id);
    }

    public String getYf2id() {
        return (String) this.getInternal("YF2ID");
    }

    public void setYf3id(String yf3id) {
        this.setInternal("YF3ID", yf3id);
    }

    public String getYf3id() {
        return (String) this.getInternal("YF3ID");
    }

    public void setSfdxmht(String sfdxmht) {
        this.setInternal("SFDXMHT", sfdxmht);
    }

    public String getSfdxmht() {
        return (String) this.getInternal("SFDXMHT");
    }

    public void setSfzfjtz(String sfzfjtz) {
        this.setInternal("SFZFJTZ", sfzfjtz);
    }

    public String getSfzfjtz() {
        return (String) this.getInternal("SFZFJTZ");
    }
}