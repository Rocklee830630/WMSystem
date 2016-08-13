package com.ccthanking.framework.message.comet;

import java.util.Date;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ccthanking.framework.util.Pub;

public class DwrService implements ApplicationContextAware {
	private ApplicationContext ctx;

	public void setApplicationContext(ApplicationContext ctx) {
		this.ctx = ctx;
	}

//	public void perform() {
//		for (int i = 0; i < 100; i++) {
//			PerformInfo info = new PerformInfo();
//			info.setId(i);
//			info.setMsg("发送" + i + "信息");
//			info.setTime(new Date());
//			InfoEvent evt = new InfoEvent(info);
//			ctx.publishEvent(evt);
//		}
//	}
	
	/*
	 * 推送提醒给所在的岗位
	 * params obj = new String[]{"这是一个消息","岗位名称"}
	 */
	public static void remindToPost(ServletContext application,Object obj){
		
	}
	/*
	 * 推送提醒给所在的角色
	 * params obj = new String[]{"这是一个消息","角色名称"}
	 */
	public static void remindToRole(ServletContext application,Object obj){
		
	}
	/*
	 * 推送提醒给所在的单位
	 * params obj = new String[]{"这是一个消息","单位名称"}
	 */
	public static void remindToDept(ServletContext application,Object obj){
		
	}
	/*
	 * 推送提醒给所在的个人
	 * params obj = new String[]{"这是一个消息","个人账户"}
	 */
	
	public static void remindToPerson(ServletContext application,Object obj){
		if(application != null){
			ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(application);
			if(obj instanceof String[]){
				String[] strArr = (String[]) obj;
				PerformInfo info = new PerformInfo();
				info.setId(1);
				info.setMsg(strArr[0]);
				info.setReceiver(strArr[1]);
				if(strArr.length>2)
				if(!Pub.empty(strArr[2]))
				{
					String url = strArr[2];
					if(!Pub.empty(strArr[3])){
						if(url.indexOf("?")>-1){
						   url +="&sjbh="+strArr[3];
						}
						else{
						   url +="?sjbh="+strArr[3];
						}
					}
					if(!Pub.empty(strArr[4])){
						if(url.indexOf("?")>-1){
							url +="&ywlx="+strArr[4];
						}
						else{
							url +="?ywlx="+strArr[4];
						}
					}
					info.setUrl(url);
				}
				if(strArr.length > 5){
					info.setTitle(strArr[5]);
				}
				info.setTime(new Date());
				InfoEvent evt = new InfoEvent(info);
				context.publishEvent(evt);
			}
		}
	}
}