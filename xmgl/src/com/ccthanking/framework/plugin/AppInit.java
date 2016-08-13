package com.ccthanking.framework.plugin;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.ccthanking.framework.common.datasource.DBConnectionManager;
import com.ccthanking.framework.dic.Dics;
import com.ccthanking.framework.log.log;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;

/* 
 * 应用系统初始化类
 * 该类负责字典的初始化
 * 及组织机构的初始化工作
 */
public class AppInit  {

    public static String appPath = "";
	public void init()//ActionServlet arg0, ModuleConfig arg1
			throws ServletException {

		Logger lg = log.getLogger(AppInit.class);
		try {
			String path= System.getProperty("webApp.root");
			this.appPath = path;
			String connpath = path + "/WEB-INF/conf/";
			DBConnectionManager dm = null;
		
			DBConnectionManager.setConnectionPath(connpath);
			
			lg.info("初始化字典信息...");
			Dics.getInstance();

			lg.info("初始化系统参数...");
			SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager
					.getInstance().getSysParameter("LOADINGDIC");

			lg.info("生成字典xml");
			if (syspara != null
					&& syspara.getSysParaConfigureParavalue1().equals("1")) {
				Dics.printDicToXml(path + "/dic");
			}
			Dics.createDataSourceDic(DBConnectionManager.getInstance()
					.getDataSource(), path + "/dic");

			lg.info("初始化系统用户...");
			com.ccthanking.framework.coreapp.orgmanage.UserManager
					.getInstance();

			lg.info("初始化组织机构...");
			com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager.getInstance();
			// 组织机构全称
			com.ccthanking.framework.coreapp.orgmanage.OrgDeptToXml.exportAllXml(path, "ZZJG", null);
			// 组织机构简称
			com.ccthanking.framework.coreapp.orgmanage.OrgDeptToXml.exportDeptFullNameXml(path, "ZZJGJC", null);
			
			lg.info("初始化系统角色...");
			com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager
					.getInstance();

			lg.info("初始化拼音字典...");
			com.ccthanking.framework.coreapp.orgmanage.SpellCache.getInstance();

			lg.info("初始化菜单信息...");
			com.ccthanking.framework.coreapp.orgmanage.MenuManager
					.getInstance();


			lg.info("初始化信息推送配置信息...");
			com.ccthanking.framework.coreapp.orgmanage.PushPersonManager.getInstance();
			// 此代码在web应用打成war包后在weblogic下发布有问题，要修改
			// 预留初始化其它组件的接口
			/**
			File appConfig = new File(path + "/WEB-INF/conf/AppConfig.xml");
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(appConfig);
			List list = document.selectNodes("//COMPONENT");
			Iterator iterator = list.iterator();
			lg.info("正在读取初始化列表.......");
			String componentClassName = null;
			String componentDescription = null;
			Element element = null;
			while (iterator.hasNext()) {
				element = (Element) iterator.next();
				componentClassName = element.elementText("CLASSNAME");
				componentDescription = element.elementText("DESCRIPTION");
				lg.info("正在初始化" + componentDescription);
				if (componentClassName.trim().length() == 0)
					continue;
				try {
					Class t = Class.forName(componentClassName);
					PlugIn init = (PlugIn) t.newInstance();
					init.init(arg0, arg1);
				} catch (Exception E) {
					lg.error(E);
				}
			}
			**/
			

			
			lg.info("系统信息初始化完毕.......");
		} catch (Exception E) {
			lg.error(E);
			E.printStackTrace();
			throw new ServletException(E);
		}
	}

	public void destroy() {

	}
}