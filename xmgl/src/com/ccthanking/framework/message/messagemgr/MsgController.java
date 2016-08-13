package com.ccthanking.framework.message.messagemgr;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ccthanking.common.vo.PubAttachmentVO;
import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

@Controller
@RequestMapping("/msgController")
public class MsgController {

	public MsgController() {
	}
	
	/**
	 * 
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public requestJson getUserMessage(HttpServletRequest request,requestJson json) throws Exception{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		try{
			resultVO = this.getJsonResult(user);
			j.setMsg(resultVO);
		}catch(Exception e){
			
		}
		return j;

	}
	private String getJsonResult(User user) throws Exception{
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json = "{querycondition: {conditions: [{'value':'1000','fieldname':'rownum','operation':'<=','logic':'and'},{'value':'%1107%','fieldname':'XMMC','operation':'like','logic':'and'} ]}}";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			PageManager page = null;
			String condition = list == null ? "" : list.getConditionWhere();
			if (page == null){
				page = new PageManager();
			}
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select * from FS_MESSAGE_INFO";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}
	// 定向到一个页面
	private void forward(String url, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.forward(request, response);
	}

	// 全部消息列表查询
	public ActionForward queryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String orderFilter = RequestUtil.getOrderFilter(doc);

		String condition = list == null ? "" : list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		condition += orderFilter;

		condition += " and STATE='1'"; // 过滤已删除信息
		condition += " ORDER  BY OPID DESC"; // 按消息号排序

		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Document domresult = null;
		OutputStream os = null;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "select USERFROMNAME,USERTONAME ,TITLE,CONTENT,OPTIME,SYSMESSAGE,EMAILMESSAGE,SMSMESSAGE,OPID from MESSAGE_INFO ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("OPTIME", "yyyy-MM-dd HH:mm");
			// bs.setFieldDateFormat("DELTIME", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldDic("EMAILMESSAGE", "SF");
			bs.setFieldDic("SYSMESSAGE", "SF");
			bs.setFieldDic("SMSMESSAGE", "SF");
			// bs.setFieldDic("mz","MZ");
			domresult = bs.getDocument();
			conn.commit();
			Pub.writeXmlDocument(response, domresult, "UTF-8");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 查询发给用户自己的信息
	public ActionForward queryDptList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String orderFilter = RequestUtil.getOrderFilter(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		condition += orderFilter;
		String userAccount = user.getAccount(); // 用户登录帐号
		String userDept = user.getDepartment(); // 用户所属部门
		condition += " and USERTO='" + user.getAccount() + "'";
		condition += " and SYSMESSAGE='1'"; // 系统公告
		condition += " and STATE='1'"; // 过滤已删除信息
		condition += " ORDER  BY OPID DESC"; // 按消息号排序
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Document domresult = null;
		OutputStream os = null;
		Connection conn = DBUtil.getConnection("p3");
		try {
			conn.setAutoCommit(false);
			String sql = "select USERFROMNAME,USERTONAME ,TITLE,CONTENT,OPTIME,SYSMESSAGE,EMAILMESSAGE,SMSMESSAGE,OPID from MESSAGE_INFO ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("OPTIME", "yyyy-MM-dd HH:mm");
			// bs.setFieldDateFormat("DELTIME", "yyyy-MM-dd HH:mm:ss");
			bs.setFieldDic("EMAILMESSAGE", "SF");
			bs.setFieldDic("SYSMESSAGE", "SF");
			bs.setFieldDic("SMSMESSAGE", "SF");
			// bs.setFieldDic("mz","MZ");
			domresult = bs.getDocument();
			Pub.writeXmlDocument(response, domresult, "UTF-8");
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 更新消息
	public ActionForward updateList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		OutputStream os = null;
		Connection conn = DBUtil.getConnection("p3");
		try {
			conn.setAutoCommit(false);
			List list = doc.selectNodes("/DATAINFO/ROW");
			for (int i = 0; i < list.size(); i++) {
				Element row = (Element) list.get(i);
				MessageInfoVO vo = new MessageInfoVO();
				vo.setSTATE("2"); // 删除状态
				vo.setDELOPER(user.getAccount()); // 删除人
				vo.setDELTIME(new Date()); // 删除时间
				vo.setValue(row);
				BaseDAO.update(conn, vo);
			}
			conn.commit();
			Pub.writeXmlInfoMessage(response, "删除成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	// 发送消息
	public ActionForward insertList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int randomNum = 0;
		String strAccessoryAdr = ""; // 附件地址
		boolean hasAttachment = false;
		// 定义一个HashMap，存放请求参数
		Map parameters = new HashMap();
		Connection conn = DBUtil.getConnection();
		String fjbhs = "";

		try {
			conn.setAutoCommit(false);
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload fu = new ServletFileUpload(factory);
			fu.setHeaderEncoding("gbk");
			// fu.setHeaderEncoding("UTF-8");//一定要设定
			// 设置最大文件尺寸，-1为不限制
			fu.setSizeMax(-1);
			// 设置缓冲区大小，这里是4kb
			factory.setSizeThreshold(4096);
			;
			// 设置临时目录：
			List fileItems = new ArrayList();
			// 得到所有的文件，以及其它请求参数：
			fileItems = fu.parseRequest(request);
			Iterator files = fileItems.iterator();
			// 保存附件的目录
			java.util.Random random = new Random();
			randomNum = random.nextInt(1000000);
			// 依次处理每一个文件，以及请求参数：
			while (files.hasNext()) {
				FileItem fi = (FileItem) files.next();
				// 如果是文件项，则保存文件到上传目录
				if (!fi.isFormField()) {
					hasAttachment = true;
					// 获得文件名，这个文件名包括路径：
					String fileName = fi.getName();
					long filesize = fi.getSize();
					if (fi.getName() != null && fi.getSize() != 0) {
						PubAttachmentVO pubvo = new PubAttachmentVO();
						pubvo.setData(fi.get());
						pubvo.setFilename(fileName.substring(fileName
								.lastIndexOf("\\") + 1));
						pubvo.setCjsj(Pub.getCurrentDate());
						pubvo.setState("1");
						pubvo.setFjlx(fi.getContentType());
						String fjbh = null;
						fjbh = DBUtil.getSequenceValue(
								"seq_common_serival_number", conn);
						pubvo.setFjbh(fjbh);
						BaseDAO.insert(conn, pubvo);
						fjbhs += pubvo.getFjbh() + ",";
					}
				} else {
					String name = fi.getFieldName();
					String value = fi.getString();
					if (value == null)
						value = "";
					parameters.put(name, value);
				}
			}
			// //////////////处理上传附件结束/////////////////////////////////////////////
			/*
			 * 处理完成后，此时所有请求参数存放在 parameters 变量中 可以通过
			 * (String)parameters.get("xxx") 来读取参数
			 */
			User user = (User) request.getSession().getAttribute(
					Globals.USER_KEY);
			String USERTODEPT = (String) parameters.get("hidUSERTODEPT"); // 收信部门编号
			if (USERTODEPT == null)
				USERTODEPT = "";
			USERTODEPT = new String(USERTODEPT.getBytes("8859_1"), "GB2312");
			String arrTODept[] = USERTODEPT.split(","); // 收信部门编号arr
			String USERTODEPTNAME = (String) parameters
					.get("hidUSERTODEPTNAME"); // 收信部门名称号
			if (USERTODEPTNAME == null)
				USERTODEPTNAME = "";
			USERTODEPTNAME = new String(USERTODEPTNAME.getBytes("8859_1"),
					"GB2312");

			String USERTOPERSON = (String) parameters.get("hidUSERTOPERSON"); // 收信人编号
			if (USERTOPERSON == null)
				USERTOPERSON = "";
			USERTOPERSON = new String(USERTOPERSON.getBytes("8859_1"), "GB2312");
			String arrTOPerson[] = USERTOPERSON.split(","); // 收信部门编号arr
			String USERTOPERSONNAME = (String) parameters
					.get("hidUSERTOPERSONNAME"); // 收信人姓名
			if (USERTOPERSONNAME == null)
				USERTOPERSONNAME = "";
			USERTOPERSONNAME = new String(USERTOPERSONNAME.getBytes("8859_1"),
					"GB2312");
			String txtXML = (String) parameters.get("txtXML");
			txtXML = new String(txtXML.getBytes("8859_1"), "GB2312");
			Document doc = DocumentHelper.parseText(txtXML);
			OutputStream os = null;
			List list = doc.selectNodes("/DATAINFO/ROW");
			if (USERTODEPT.length() == 0 && USERTOPERSON.length() == 0) { // 收信人是全部系统用户
				arrTOPerson = new String[UserManager.getInstance()
						.getUserTable().size()];
				Enumeration enumer = UserManager.getInstance().getUserTable()
						.keys();
				int i = -1;
				while (enumer.hasMoreElements()) {
					arrTOPerson[++i] = (String) enumer.nextElement();
				}
			} else if (USERTODEPT.length() > 0 && USERTOPERSON.length() == 0) { // 收信人是部门
				if (arrTODept != null) {
					String persons = "";
					for (int i = 0; i < arrTODept.length; i++) {
						String[][] usersAccount = DBUtil
								.query("select account from org_person where DEPARTMENT = '"
										+ arrTODept[i] + "'");
						if (usersAccount != null)
							for (int j = 0; j < usersAccount.length; j++) {
								persons += "," + usersAccount[j][0];
							}
					}
					if (persons.length() > 1)
						arrTOPerson = persons.substring(1).split(",");
				}
			} else if (USERTOPERSON.length() > 0) { // 收信人是人员
			}
			String strTitle = null; // 消息标题
			String strContent = null; // 消息内容
			for (int i = 0; i < list.size(); i++) {
				Element row = (Element) list.get(i);
				List alist = row.elements("FSFS"); // 复选框
				strTitle = row.element("TITLE").getText(); // 消息题目
				strContent = row.element("CONTENT").getText(); // 消息内容
				boolean isSys = false;
				boolean isMail = false;
				boolean isSms = false;
				for (int j = 0; j < alist.size(); j++) {
					Element tmpElement = (Element) alist.get(j);

					if (tmpElement.getText().equals("1")) { // 系统公告
						isSys = true;
					}
					if (tmpElement.getText().equals("2")) { // 邮件发送
						isMail = true;
					}
					if (tmpElement.getText().equals("3")) { // 短信发送,预留
						isSms = true;
					}
				}
				if (!hasAttachment) {

				}
				if (arrTOPerson != null)
					for (int p = 0; p < arrTOPerson.length; p++) {
						if (Pub.empty(arrTOPerson[p]))
							continue;
						sendMessage.sendMessageToPerson(conn, strTitle,
								strContent, user.getAccount(), arrTOPerson[p],
								isSys, isMail, isSms, fjbhs);
					}

			}
			conn.commit();
			request.setAttribute("isFlag", "1"); // 消息发送成功
			forward("/jsp/framework/message/msgmgr.jsp", request, response);

		} catch (Exception ex) {
			conn.rollback();
			request.setAttribute("isFlag", "0"); // 消息发送出错
			forward("/jsp/framework/message/msgmgr.jsp", request, response);
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}

		return null;
	}

	public ActionForward getMessageCount(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String sql = "select count(*) from message_info where USERTO='"
				+ user.getAccount() + "' and SYSMESSAGE=1" + " and STATE=1";
		String[][] res = DBUtil.query(sql);
		if (res != null)
			response.getOutputStream().print(res[0][0]);
		else
			response.getOutputStream().println("");
		response.getOutputStream().flush();
		response.getOutputStream().close();
		return null;
	}

	public ActionForward deleteList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		OutputStream os = null;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			List list = doc.selectNodes("/DATAINFO/ROW");
			for (int i = 0; i < list.size(); i++) {
				Element row = (Element) list.get(i);
				MessageInfoVO vo = new MessageInfoVO();
				vo.setOPID(row.element("OPID").getText());
				vo.setDELOPER(user.getAccount());
				vo.setDELTIME(new Date());
				vo.setSTATE("2");
				BaseDAO.update(conn, vo);
			}
			conn.commit();
			Pub.writeXmlInfoMessage(response, "删除成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

}