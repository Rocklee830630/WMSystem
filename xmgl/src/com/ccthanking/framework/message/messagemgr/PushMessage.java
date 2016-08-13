package com.ccthanking.framework.message.messagemgr;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.PushPersonManager;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;

public class PushMessage {

    // 计财处和统筹计划
    // =====================================================

    /* 计财项目下达 */
    public static final String XMCBK_XMXD = "001";
    /* 统筹计划下发 */
    public static final String TCJHXF = "020005";

    // 前期手续组业务
    // =====================================================
    /* 土地审批手续 */
    public static final String TDSPSX = "070003";
    /* 规划审批手续 */
    public static final String GHSPSX = "070006";

    // 设计组业务
    // =====================================================
    /* 【规划条件】 */
    public static final String ZLSF_GHTJ = "014";
    // 设计变更
    public static final String SJBG = "020002";
    /** 资料收发 */
    /* 【排迁图】 */
    public static final String ZLSF_PQT = "010";
    /* 【拆迁图】 */
    public static final String ZLSF_CQT = "011";
    /* 【施工图（送审版）】 */
    public static final String ZLSF_SGT_SS = "012";
    /* 【施工图（正式版）】 */
    public static final String ZLSF_SGT_ZS = "013";
    /* 【方案设计】 */
    public static final String ZLSF_GASJ = "015";
    /* 【勘测资料】 */
    public static final String ZLSF_KCZL = "016";
    /* 【施工图审查报告】 */
    public static final String ZLSF_SGTSCBG = "017";
    /* 桩基检测 */
    public static final String BGSF = "040003";

    // 造价部
    // =====================================================
    public static final String ZJB_LBJ = "060001";

    // 招投标与合同部业务
    // =====================================================
    public static final String ZTB_BDHF = "05001";

    // 工程部
    // =====================================================
    /* 【保函到期提醒】 */
    public static final String LYBZJ_BHTX = "700201";
    
    /* 【提交合同到招标合同部】 */
    public static final String HT_TJHT = "020";
    
    /* 【反馈开工时间】 */
    public static final String KGSJ_SJ_FK = "068";

    /* 【反馈完工时间】 */
    public static final String WGSJ_SJ_FK = "069";

    /* 【招标需求归档】 */
    public static final String ZBXQGD = "300101";

    public static final String ZBXQ_TJDHT = "031";
    /* 【施工招标需提交到合同】 */
    public static final String SG_ZBXQ_TJDHT = "032";
    /* 【监理招标需提交到合同】 */
    public static final String JL_ZBXQ_TJDHT = "033";
    
    /* 【合同会签单审批】 */
    public static final String HT_SPTG = "700101";
    
    /* 【合同已签订审核】 */
    public static final String HT_QDSH = "028";
    //=======================================================
    //--[问题提报]从08开始
    public static final String WTTB_FQRJS = "080001";//--[问题提报]发起人结束问题
    public static final String WTTB_ZBRHF = "080002";//--[问题提报]主办人回复
    public static final String WTTB_BLRHF = "080003";//--[问题提报]主办人回复

    /**
     * 信息推送方法（无标题title）
     * 
     * @param request
     * @param operateid
     *            String 操作編號
     * @param msg
     *            String 提示信息
     * @param url
     *            String 打開窗口URL
     * @param sjbh
     *            String 事件編號
     * @param ywlx
     *            String 業務類型
     * @param ywzj
     *            String 業務操作主鍵
     * @return
     * @throws SQLException
     */
    public static boolean push(Connection conn,HttpServletRequest request, String operateid, String msg, String url, String sjbh, 
    		String ywlx, String ywzj) throws SQLException {

        String[] person = PushPersonManager.getInstance().loadUserId(operateid);
        String title = queryTitle(conn, operateid);
        push(conn, request, operateid, msg, url, sjbh, ywlx, ywzj, person, title);
        return true;
    }

    /**
     * 信息推送方法（有标题title）
     * 
     * @param request
     * @param operateid
     *            String 操作編號
     * @param msg
     *            String 提示信息
     * @param url
     *            String 打開窗口URL
     * @param sjbh
     *            String 事件編號
     * @param ywlx
     *            String 業務類型
     * @param ywzj
     *            String 業務操作主鍵
     * @param title
     *            String 标题
     * @return
     * @throws SQLException
     */
    public static boolean push(Connection conn,HttpServletRequest request, String operateid, String msg, String url, String sjbh, String ywlx, String ywzj,
            String title) throws SQLException {

        String[] person = PushPersonManager.getInstance().loadUserId(operateid);
        push(conn, request, operateid, msg, url, sjbh, ywlx, ywzj, person, title);
        return true;
    }

    /**
     * 信息推送方法
     * 
     * @param request
     * @param operateid
     *            String 操作編號
     * @param msg
     *            String 提示信息
     * @param url
     *            String 打開窗口URL
     * @param sjbh
     *            String 事件編號
     * @param ywlx
     *            String 業務類型
     * @param ywzj
     *            String 業務操作主鍵
     * @param person
     *            String[] 用戶ID
     * @return
     * @throws SQLException
     */
    public static boolean push(Connection conn,HttpServletRequest request, String operateid, String msg, String url, String sjbh, String ywlx, String ywzj,
            String[] person) throws SQLException {

        String title = queryTitle(conn, operateid);
        push(conn, request, operateid, msg, url, sjbh, ywlx, ywzj, person, title);
        return true;
    }

    public static boolean push(Connection conn,HttpServletRequest request, String msg, String url, String sjbh, String ywlx, String ywzj, String[] person,
            String title) throws SQLException {

        push(conn, request, null, msg, url, sjbh, ywlx, ywzj, person, title);
        return true;
    }

    public static boolean pushInfoNotInMsg(Connection conn,HttpServletRequest request, String msg, String url, String sjbh, String ywlx, String ywzj,
            String[] person, String title) throws SQLException {
        if (null == person) {
            return false;
        }
        ServletContext application = request.getSession().getServletContext();

        for (int i = 0; i < person.length; i++) {
            //DwrService.remindToPerson(application, new String[] { msg, (String) person[i], url, sjbh, ywlx, title });
        }
        return true;
    }

    /**
     * 信息推送方法
     * 
     * @param request
     * @param operateid
     *            String 操作編號
     * @param msg
     *            String 提示内容
     * @param url
     *            String 打開窗口URL
     * @param sjbh
     *            String 事件編號
     * @param ywlx
     *            String 業務類型
     * @param ywzj
     *            String 業務操作主鍵
     * @param person
     *            String[] 用戶ID
     * @param title
     *            String 标题
     * @return
     * @throws SQLException 
     * @throws SQLException
     */
    public static boolean push(Connection conn,HttpServletRequest request, String operateid, String msg, String url, String sjbh, 
    		String ywlx, String ywzj, String[] person, String title) throws SQLException {
        if (null == person) {
            return false;
        }
    	Connection p4Conn = null;
        try {
        	p4Conn = DBUtil.getConnection("P4");
        	if(p4Conn!=null){
        		p4Conn.setAutoCommit(false);
        	}
        	
            String currentUser = "superman";
            if(request != null) {
                User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
            	currentUser = user.getAccount();
            }
            
            //获取推送信息链接(临时查询处理)
            String sql = "select OPERATOR_LINK from FS_PUSHINFO where OPERATOR_NO = ?";
            Object para[] = new String[1];
            para[0] = operateid;
            String urls[][] = DBUtil.querySql(conn, sql, para);
            String realURL = "";
            if(urls==null){
            	//
            }else{
            	realURL = urls[0][0];
            }
            //结合业务传入参数
            url = Pub.empty(url)?"":url;
            realURL = realURL + url;
            
            // 是否允许重复录入 1：可以重复录入，0：不可以重复录入
            String sfyxcflr = sfyxcflr(conn, operateid);
            if("1".equals(sfyxcflr)) {
                for (int i = 0; i < person.length; i++) {
                	MessageInfoVO mvo = sendMsg(conn, title, msg, currentUser, person[i], realURL, sjbh, ywlx, operateid);
                    if(p4Conn!=null){
                    	// start 向添加MYSQL数据库中添加数据    add by xiahongbo by 2014-10-16
	                    String userId = queryImUsers(p4Conn, person[i]);
	                    if(!"0".equals(userId)) {
	                        insertMysql(p4Conn, userId,title,msg,realURL,person[i],request,mvo);
	                    }
                    }
                    // end
                }
            } else if("0".equals(sfyxcflr)) {
            	// 判断消息表中是否已经有此operateid此sjbh的数据，没有再推送消息
            	if(!isyz(conn, sjbh, operateid)) {
                    for (int i = 0; i < person.length; i++) {
                    	MessageInfoVO mvo = sendMsg(conn, title, msg, currentUser, person[i], realURL, sjbh, ywlx, operateid);
                        if(p4Conn!=null){
	                        // start 向添加MYSQL数据库中添加数据    add by xiahongbo by 2014-10-16
	                        String userId = queryImUsers(p4Conn, person[i]);
	                        if(!"0".equals(userId)) {
	                            insertMysql(p4Conn, userId,title,msg,realURL,person[i],request,mvo);
	                        }
	                        // end
                        }
                    }
            	}
            }else{
            	//配置表中未找到状态，不知道怎么处理，业务类调用就发送消息
            	for (int i = 0; i < person.length; i++) {
            		sendMsg(conn, title, msg, currentUser, person[i], realURL, sjbh, ywlx, operateid);
            	}
            }
            if(p4Conn!=null){
            	p4Conn.commit();
            }
            return true;
        } catch (Exception e) {
        	if(p4Conn!=null){
        		p4Conn.rollback();
        	}
            e.printStackTrace();
            return false;
        } finally {
        	if(p4Conn!=null){
        		p4Conn.close();
        	}
        }
    }

    /**
     * 将推送的消息存入FS_MESSGE_INFO表中
     * 
     * @param conn
     *            Conenction 建立的数据库链接
     * @param strTitle
     *            String 标题
     * @param strContent
     *            String 提示内容
     * @param strUserfrom
     *            String 发送者
     * @param strUserto
     *            String 接收者
     * @param linkurl
     *            String 打開窗口URL
     * @param sjbh
     *            String 事件編號
     * @param ywlx
     *            String 業務類型
     * @throws Exception
     */
    private static MessageInfoVO sendMsg(Connection conn, String strTitle, String strContent, String strUserfrom, String strUserto, String linkurl,
            String sjbh, String ywlx, String oid) throws Exception {
    	MessageInfoVO vo = sendMessage.sendMessageToPerson(conn, strTitle, strContent, strUserfrom, strUserto, false, false, false, null, linkurl, sjbh, ywlx, oid);
    	return vo;
    }
    
    /**
     * 查询操作ID对应的名称，当做发送消息的标题
     * @param oid
     * @return
     */
    private static String queryTitle(Connection conn,String oid) {
    	String sql = "select OPERATOR_NAME from FS_PUSHINFO t where OPERATOR_NO='"+oid+"'";
    	String[][] rs = DBUtil.query(conn,sql);
    	String title = rs == null ? "" : rs[0][0];
    	return title;
    }
    
    /**
     * 查询消息表中是否已经有此类消息
     * @param conn
     * @param sjbh
     * @param person
     * @return
     */
    private static boolean isyz(Connection conn,String sjbh, String oid) {
    	String sql = "select count(*) from FS_MESSAGE_INFO where SJBH='"+sjbh+"' and OPERATOR_NO='"+oid+"'";
    	String[][] rs = DBUtil.query(conn, sql);
    	String cnt = rs[0][0];
    	boolean isyz = "0".equals(cnt) ? false : true;
    	return isyz;
    }
    
    /**
     * 查询推送配置信息中，此操作项是否允许重复录入
     * @param conn
     * @param oid
     * @return
     */
    private static String sfyxcflr(Connection conn, String oid) {
    	String sql = "select SFYXCFLR from FS_PUSHINFO where OPERATOR_NO='"+oid+"'";
    	String[][] rs = DBUtil.query(conn, sql);
    	String sfyxcflr = rs == null ? "" : rs[0][0];
    	return sfyxcflr;
    }
    
    private static String queryImUsers(Connection p4Conn, String username) {
    	String userId = "0";
    	try {
			String sql = "select userid from im_users where username='"+username+"'";
			String[][] rs = DBUtil.query(p4Conn, sql);
			userId = rs == null ? "0" : rs[0][0];
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return userId;
    }
    
    private static long queryMsgMaxId(Connection p4Conn) {
    	long msgId = 0;
    	try {
			String sql = "SELECT MAX(msgid) FROM im_msg";
			String[][] rs = DBUtil.query(p4Conn, sql);
			String id = rs[0][0];
			msgId = Long.parseLong(id)+1;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return msgId;
    }
    
	private static void insertMysql(Connection p4Conn, String userId,
			String title, String msg, String url, String account,
			HttpServletRequest request, MessageInfoVO mvo) {
    	try {
    		long id = queryMsgMaxId(p4Conn);
    		String time = DateTimeUtil.getDateTime();
//    		String ss= "ÄúÕýÔÚÊ¹ÓÃµÄÊÇ²âÊÔ°æ±¾£¬¼ÓQQ80666323»ñÈ¡Ô´´úÂë£¡";
    		//字符集转换
			msg=new String(msg.getBytes(),"ISO-8859-1");
			title=new String(title.getBytes(),"ISO-8859-1");
			
			//处理url
			User user = UserManager.getInstance().getUserByLoginName(account);
			String password = user.getPassWord();
			password = StringUtils.newStringUtf8(Base64.encodeBase64(StringUtils.getBytesUtf8(password)));
			url = StringUtils.newStringUtf8(Base64.encodeBase64(StringUtils.getBytesUtf8(url)));
			String msid = StringUtils.newStringUtf8(Base64.encodeBase64(StringUtils.getBytesUtf8(mvo.getOPID())));
			String link = "/simulateLogin.jsp?u="+account+"&p="+password+"&l="+url+"&msid="+msid;
			link=new String(link.getBytes(),"ISO-8859-1");
			
			String insertMsgSql = "INSERT im_msg (msgid,msgtime,msgintro,msgurl,msgtitle,msg,userid,TYPE,domainid,msgflag) VALUES "
					+"("+id+",UNIX_TIMESTAMP('"+time+"'),'"+msg+"','"+link+"','"+title+"','content4',"+userId+",1,0,0)";
			
			DBUtil.exec(p4Conn, insertMsgSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
