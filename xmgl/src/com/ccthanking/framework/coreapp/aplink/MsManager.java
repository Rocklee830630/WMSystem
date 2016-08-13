package com.ccthanking.framework.coreapp.aplink;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultText;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;

public class MsManager {

	private static Logger log = Logger.getLogger(MsManager.class);
	public static final String[] ERR_MSG = new String[] { "", "帐号登陆失败", // -1
			"错误的手机号", // -2
			"手机号为黑名单用户", // -3
			"短信内容不正确", // -4
			"错误的子号码", // -5
			"子号码超长", // -6
			"账户余额不足", // -7
			"请求参数错误" }; // -8

	@SuppressWarnings("unchecked")
	public static void sendMsg(String phone, String msg, URL url) {
		StringBuffer smsurl = new StringBuffer();
		try {
			smsurl.append("&Phone=").append(phone).append("&Content=")
					.append(URLEncoder.encode(msg, "UTF-8"));
			if (log.isDebugEnabled()) {
				log.debug("sms post data:" + smsurl.toString());
			}
			// SysParaConfigureVO syspara = (SysParaConfigureVO)
			// ParaManager.getInstance().getSysParameter("MSM");
			// if (syspara != null &&
			// syspara.getSysParaConfigureParavalue1().equals("1"))
			// {
			// url = new URL(syspara.getSysParaConfigureParavalue2());
			// }else{
			// return;
			// //url = new
			// URL("http://10.15.35.209:8888/ema/http/SendSms?Account=01&Password=96a3be3cf272e017046d1b2674a52bd3");
			// }

			// url = new URL(WebUtils.getConfigSetting("smsUrl"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Cache-Control", "no-cache");
			OutputStream buf = conn.getOutputStream();
			buf = new BufferedOutputStream(buf);
			OutputStreamWriter out = new OutputStreamWriter(buf);
			out.write(smsurl.toString());
			out.flush();
			out.close();
			int code = conn.getResponseCode();
			if (code == 200) {
				// 接收数据
				InputStream in = new BufferedInputStream(conn.getInputStream());
				BufferedReader rData = new BufferedReader(
						new InputStreamReader(in));
				StringBuffer sb = new StringBuffer();
				String str = rData.readLine();
				while (str != null) {
					sb.append(str);
					str = rData.readLine();
				}
				in.close();
				Document doc = null;
				doc = DocumentHelper.parseText(sb.toString());
				// List list = doc.selectNodes("/DATAINFO/ROW");
				List<DefaultText> rlist = doc.selectNodes("/result/response");
				Element row = (Element) rlist.get(0);
				str = row.getText();
				if (str.length() > 5) {
					if (log.isDebugEnabled()) {
						log.debug("send [" + msg + "] to [" + phone
								+ "] success! id[" + str + "]");
					}
				} else {
					str = "send [" + msg + "] to [" + phone
							+ "] failed! cause by:"
							+ ERR_MSG[Math.abs(Integer.parseInt(str))];
					log.error(str);
					throw new RuntimeException(str);
				}
			} else {
				throw new RuntimeException("connect to sms server failed![code"
						+ code + "]");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加查询审批人电话方法，发送手机短信
	 */
	public static void sendMessage(String userid, String desc) {
		// add by songxb@2010-01-27发送手机短信
		SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager
				.getInstance().getSysParameter("MSM");
		URL url = null;
		String dbr = "";
		try {
			if (syspara != null
					&& syspara.getSysParaConfigureParavalue1().equals("1")) {
				url = new URL(syspara.getSysParaConfigureParavalue2());
				if (null == syspara.getSysParaConfigureParavalue2()
						|| "".equals(syspara.getSysParaConfigureParavalue2())) {
					// url = new
					// URL("http://10.15.35.209:8888/ema/http/SendSms?SubCode=01&Account=01&Password=96a3be3cf272e017046d1b2674a52bd3");
					System.out
							.println("未配置短信接口ip地址信息，请到sys_para_configer表中配置相关的记录！");
				}
				dbr = userid;
				if (dbr != null && !"undefined".equals(dbr) && dbr.length() > 0) {
					String sql = "select sjhm from org_person where account = '"
							+ userid + "'";
					String resultStr[][] = null;

					resultStr = DBUtil.query(sql);
					if (resultStr != null && isNumber(resultStr[0][0])) {
						MsManager.sendMsg(resultStr[0][0], desc, url);
					}

				}
			} else {
				System.out
						.println("未配置是否调用短信接口参数 ，请到sys_para_configer表中配置相关的记录！");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {

		}

	}

	/**
	 * 判断手机号码是否正确
	 * 
	 */
	public static boolean isNumber(String sjhm) {
		boolean flag = true;
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher isNum = p.matcher(sjhm);
		flag = isNum.find();
		return flag;
	}

}
