package com.ccthanking.common;

/**
 * 定义业务使用的业务类型变量
 * 
 * @author hongpeng.dong
 */
public final class YwlxManager {

    /***************************************
     * 项目信息
     **************************************/

    // 项目储备库
    public static final String GC_XM_CBK = "010001";
    
    // 项目储备库_审批 
    public static final String GC_CBK_SP = "010007";


    // 项目储备库批次
    public static final String GC_XM_CBK_PC = "010002";

    // 项目下达库
    public static final String GC_XM = "010003";
    // 项目下达库_审批 
    public static final String GC_XM_SP = "010006";

    // 项目年度
    public static final String GC_XM_ND = "010004";

    // 项目标段
    public static final String GC_XM_BD = "010005";

    /***************************************
     * 计划管理
     **************************************/

    // 计划主体
    public static final String GC_JH_ZT = "020001";
    // 计划主体
    public static final String GC_JH_ZT_XM = "020006";

    // 计划数据
    public static final String GC_JH_SJ = "020002";

    // 计划变更版本
    public static final String GC_JH_BGBB = "020003";

    // 计划变更项目
    public static final String GC_JH_BGXM = "020004";

    // 统筹计划下达项目暂存
    public static final String GC_JH_XM_ZC = "020005";

    /***************************************
     * 排迁管理
     **************************************/

    // 排迁管理
    public static final String GC_PQ = "030001";

    // 排迁进展
    public static final String GC_PQ_JZ = "030002";

    // 排迁子项目
    public static final String GC_PQ_ZXM = "030003";

    // 排迁问题
    public static final String GC_PQ_WT = "030004";

    /***************************************
     * 设计管理
     **************************************/

    // 设计管理
    public static final String GC_SJ = "040001";

    // 初步设计批复
    public static final String GC_SJ_CBSJPF = "040002";

    // 报告接收
    public static final String GC_SJ_BGSF_JS = "040003";
    // 报告接收_拆迁图
    public static final String GC_SJ_BGSF_JS_CQT = "040101";
    // 报告接收_排迁图
    public static final String GC_SJ_BGSF_JS_PQT = "040102";
	// 报告接收_施工图
    public static final String GC_SJ_BGSF_JS_SGT = "040103";

    // 资料接收
    public static final String GC_SJ_ZLSF_JS = "040004";

    // 设计变更接收
    public static final String GC_SJ_SJBG_JS = "040005";

    // 材料领取
    public static final String GC_SJ_XGZL_LQ = "040006";

    // 交竣工管理
    public static final String GC_SJGL_JJG = "040007";

    /***************************************
     * 工程管理
     **************************************/
    
    
    // 工程洽商
    public static final String GC_GCGL_GCQS = "050001";

    // 履约保证金
    public static final String GC_GCGL_LVBZJ = "050002";

    // 开工令
    public static final String GC_GCGL_KFTGL = "050003";

    // 开复停工令状态
    public static final String GC_GCGL_KFTGL_ZT = "050004";
    // 复工令
    public static final String GC_GCGL_FGL = "050005";
    // 停工令
    public static final String GC_GCGL_TGL = "050006";

    /***************************************
     * 项目管理公司
     **************************************/

    // 计量信息
    public static final String GC_XMGLGS_JL = "060001";

    // 计量月份
    public static final String GC_XMGLGS_JLYF = "060002";

    // 周报信息
    public static final String GC_XMGLGS_ZB = "060003";

    // 周报时间
    public static final String GC_XMGLGS_ZBSJ = "060004";
    
    //项目信息管理
    public static final String GC_XMXDK_XMXX = "060005";
    
    //形象进度管理
    public static final String GC_XMGLGS_XXJD = "060006";
    
    //形象进度计划编制
    public static final String GC_XMGLGS_XXJD_JHBZ = "060007";
    
    //形象进度计划反馈
    public static final String GC_XMGLGS_XXJD_JHFK = "060008";
    
    //形象进度自定义节点
    public static final String GC_XMGLGS_XXJD_ZDYJD = "060009";

    /***************************************
     * 前期手续管理
     **************************************/

    // 前期手续
    public static final String GC_QQSX = "070001";

    // 前期手续办理
    public static final String GC_QQSX_BL = "070002";

    // 土地审批信息
    public static final String GC_QQSX_TTSP = "070003";

    // 施工许可
    public static final String GC_QQSX_SGXK = "070004";

    // 立项可研
    public static final String GC_QQSX_LXKY = "070005";

    // 规划审批
    public static final String GC_QQSX_GHSP = "070006";

    /***************************************
     * 质量安全管理
     **************************************/

    // 质量安全检查
    public static final String GC_ZLAQ_JC = "080001";

    // 质量安全整改
    public static final String GC_ZLAQ_ZGB = "080002";

    /***************************************
     * 造价管理
     **************************************/

    // 拦标价管理
    public static final String GC_ZJB_LBJ = "090001";

    // 结算管理
    public static final String GC_ZJB_JS = "090002";
    // 提报价反馈
    public static final String GC_ZJB_TBJFK = "090003";
    // 财审反馈
    public static final String GC_ZJB_CSFK = "090004";

    /***************************************
     * 征收管理
     **************************************/

    // 征收项目信息
    public static final String GC_ZS_XM = "100001";

    // 征地拆迁信息
    public static final String GC_ZS_XX = "100002";

    // 征地拆迁进度
    public static final String GC_ZSB_JDB = "100003";

    /***************************************
     * 其他
     **************************************/

    // 参建单位
    public static final String GC_CJDW = "190001";

    // 问题提报基本信息
    public static final String GC_WTTB_XX = "190004";

    // 问题提报信息流转
    public static final String GC_WTTB_LZ = "190005";

    // 问题提报沟通信息
    public static final String GC_WTTB_GT = "190005";

    /***************************************
     * 协同办公 通讯管理
     **************************************/
    // 内部短信
    public static final String OA_TXGL_NBDX = "200101";
    // 手机短信
    public static final String OA_TXGL_SJDX = "200102";
    // 通讯录
    public static final String OA_TXGL_TXL = "200103";

    /***************************************
     * 协同办公 信息中心
     **************************************/
    // 通知公告
    public static final String OA_XXZX_TZGG = "200201";
    // 中心新闻
    public static final String OA_XXZX_ZXXW = "200202";

    /***************************************
     * 协同办公 工作报告
     **************************************/
    // 工作日志
    public static final String OA_GZBG_GZRZ = "200301";

    /***************************************
     * 协同办公 知识管理
     **************************************/
    // 个人文档
    public static final String OA_ZSGL_GRWD = "200401";
    // 公共文档
    public static final String OA_ZSGL_GGWD = "200402";

    /***************************************
     * 协同办公 公文管理
     **************************************/
    // 公文办理
    public static final String OA_GWGL_GWBL = "200501";
    // 收文管理
    public static final String OA_GWGL_SWGL = "200502";
    // 发文管理
    public static final String OA_GWGL_FWGL = "200503";

    /***************************************
     * 合同 and 资金
     **************************************/
    // 合同信息
    public static final String GC_HTGL_HT = "700101";
    // 合同-工程
    public static final String GC_HTGL_HT_GC = "700102";
    // 合同-文件
    public static final String GC_HTGL_HT_WJ = "700103";
    // 合同-合同支付
    public static final String GC_HTGL_HT_HTZF = "700104";
    // 合同-完成投资
    public static final String GC_HTGL_HT_WCTZ = "700105";
    // 合同-合同变更
    public static final String GC_HTGL_HT_HTBG = "700106";

    // 资金管理-履约保证金
    public static final String GC_ZJGL_LYBZJ = "700201";
    // 资金管理-征收资金-支付征收办
    public static final String GC_ZJGL_ZSZJ_ZFZSB = "700202";
    // 资金管理-征收资金-项目征收资金情况
    public static final String GC_ZJGL_ZSZJ_XMZSZJQK = "700203";

    // 资金管理-财务-提请款
    public static final String GC_ZJGL_TQK = "700204";
    // 资金管理-财务-提请款明细
    public static final String GC_ZJGL_TQKMX = "700205";
    // 资金管理-部门-提请款
    public static final String GC_ZJGL_TQKBM = "700206";
    // 资金管理-部门-提请款明细
    public static final String GC_ZJGL_TQKBMMX = "700207";
    
    // 房建履约保证金返还
    public static final String GC_ZJGL_FJLYBZJFH = "700208";
    
    // 资金管理-工程部-提请款
    public static final String GC_ZJGL_TQKGCB = "700209";
    
    //资金管理-工程部-提请款明细
    public static final String GC_ZJGL_TQKGCBMX = "700210";

    /***************************************
     * 系统管理
     **************************************/
    // 日志
    public static final String SYSTEM_LOG = "900001";
    // 菜单管理
    public static final String SYSTEM_MENU = "900002";
    // 角色管理
    public static final String SYSTEM_ROLE = "900003";
    // 用户管理
    public static final String SYSTEM_USER = "900004";
    // 部门管理
    public static final String SYSTEM_DEPT = "900005";
    // 字典管理
    public static final String SYSTEM_DICT = "900006";
    // 读取用户照片
    public static final String SYSTEM_USER_TX = "900007";
    // 读取用户签名
    public static final String SYSTEM_USER_QM = "900008";
    // 授予推送信息角色
    public static final String SYSTEM_PUSHINFO = "900009";
    // 角色类别管理
    public static final String SYSTEM_ROLE_TREE = "900010";
    /***************************************
     * 招投标管理
     **************************************/
    // 招标需求
    public static final String GC_ZTB_XQ = "300101";
    // 招投标
    public static final String GC_ZTB_SJ = "300102";
    // 任务书管理
    public static final String GC_SJGL_RWSGL = "300103";
    // 施工招标反馈
    public static final String GC_ZTB_SGZBFK = "300201";
    // 监理招标反馈
    public static final String GC_ZTB_JLZBFK = "300202";
    
    //标准文档
    public static final String GC_WD_BZWD = "300204";
    
    //工程甩项
    public static final String GC_GCB_GCSX = "040417";
    //办公会
    public static final String GC_BGH_WT = "040418";
    public static final String GC_BGH = "040419";
    //建委计划
    public static final String GC_CJJH = "040420";
    //提交结算
    public static final String GC_XMGLGS_XXJD_TBZJ = "040421";
    //结算文件
    public static final String GC_ZJB_JSWJ = "040422";
    
    
    //招投标条件管理
    public static final String ZTB_TJGL = "500001";
    //开工条件管理
    public static final String KG_TJGL = "600001";
    //年度信息共享
    public static final String XTBG_XXZX_NDXXGX = "700001";
}
