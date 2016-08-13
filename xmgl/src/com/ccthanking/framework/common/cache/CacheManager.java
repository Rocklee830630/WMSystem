package com.ccthanking.framework.common.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class CacheManager{
	
	public static final String CACHE_ORG_DEPT = "base.common.ORG_DEPT";
	public static final String CACHE_USER = "base.common.USER";
	public static final String CACHE_ROLE = "base.common.ROLE";
	public static final String CACHE_MENU = "base.common.MENU";
	public static final String CACHE_PARAMS = "base.common.PARAM";
	public static final String CACHE_XZJG = "base.common.XZJG";
	public static final String CACHE_DIC = "base.common.DIC";
	public static final String CACHE_XMLVO = "base.common.XMLVO";
	public static final String CACHE_HXML = "base.common.HXML";
	public static final String CACHE_WSPRO = "com.ccthanking.common.WSProperty";
	public static final String CACHE_OUT_DIC = "base.common.OUTDIC";
	public static final int ADD = 1, UPDATE = 2, DELETE = 3, MAP_CHANGEED = 4;

	private static HashMap cacheObjectInstance = new HashMap();
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger("CacheManager");
    
    public static void reBuildMemory(String cacheObject) throws Exception {
		java.util.ArrayList nodes = getClusterNodes();
		if (nodes != null && nodes.size() > 1) {
			for (int i = 0; i < nodes.size(); i++) {
				try {
					sendHttpRequest(nodes.get(i).toString(), cacheObject);
				} catch (Exception E) {
					logger.info(E);
					E.printStackTrace();
				}
			}
		} else {
			synchronizeCache(cacheObject);
		}
	}
    
	public static void synchronizeCache(String cacheObject) throws Exception {
		Object objectInstance = cacheObjectInstance.get(cacheObject);
		if (objectInstance == null)
			throw new Exception("cache object instance not found !");
		Cache cache = (Cache) objectInstance;
		cache.reBuildMemory();
	}
	
	private static void sendHttpRequest(String address, String cacheObject)
			throws Exception {
		try {
			if (address.endsWith("/")) {
				address = address.substring(0, address.length() - 1);
			}
			address = address
					+ "/FrameWork/CacheSynchronizeAction.do?method=reBuildMemory";
			java.net.URL url = new URL(address);
			java.net.HttpURLConnection uc = (HttpURLConnection) url
					.openConnection();
			uc.setDoInput(true);
			uc.setDoOutput(true);

			OutputStream outStream = uc.getOutputStream();
			DataOutputStream dos = new DataOutputStream(outStream);
			dos.writeUTF("&CACHE_TYPE=" + cacheObject);
			dos.close();
			uc.connect();
			InputStream inStream = uc.getInputStream();
			DataInputStream in = new DataInputStream(inStream);
			byte b[] = new byte[10];
			while (in.read(b) > 0) {
				System.out.println(new String(b, "GBK"));
			}
			uc.disconnect();
		} catch (Exception E) {
			throw E;
		}
	}
	
	public static void broadcastChanges(String cacheObject, String key,
			int action) throws Exception {
		java.util.ArrayList nodes = getClusterNodes();
		if (nodes != null && nodes.size() > 1) {
			for (int i = 0; i < nodes.size(); i++) {
				try {
					sendHttpRequest(nodes.get(i).toString(), cacheObject, key,
							action);
				} catch (Exception E) {
					logger.info(E);
					E.printStackTrace();
				}
			}
		} else {
			synchronizeCache(cacheObject, key, action);
		}
	}

	public static void synchronizeCache(String cacheObject, String key,
			int action) throws Exception {
		Object objectInstance = cacheObjectInstance.get(cacheObject);
		if (objectInstance == null)
			throw new Exception("cache object instance not found !");
		Cache cache = (Cache) objectInstance;
		cache.synchronize(key, action);
	}

	private static void sendHttpRequest(String address, String cacheObject,
			String data, int action) throws Exception {
		try {
			if (address.endsWith("/")) {
				address = address.substring(0, address.length() - 1);
			}
			address = address
					+ "/FrameWork/CacheSynchronizeAction.do?method=SynchronizeCache";
			java.net.URL url = new URL(address);
			java.net.HttpURLConnection uc = (HttpURLConnection) url
					.openConnection();
			uc.setDoInput(true);
			uc.setDoOutput(true);

			OutputStream outStream = uc.getOutputStream();
			DataOutputStream dos = new DataOutputStream(outStream);
			dos.writeUTF("&CACHE_TYPE=" + cacheObject + "&CACHE_DATA=" + data
					+ "&CACHE_OPERATION=" + action);
			dos.close();
			uc.connect();
			InputStream inStream = uc.getInputStream();
			DataInputStream in = new DataInputStream(inStream);
			byte b[] = new byte[10];
			while (in.read(b) > 0) {
				System.out.println(new String(b, "GBK"));
			}
			uc.disconnect();
		} catch (Exception E) {
			throw E;
		}
	}

	/**
	 * 得到配置的集群节点
	 * @return
	 */
	private static java.util.ArrayList getClusterNodes() {
		java.util.ArrayList clusterNodes = new java.util.ArrayList();
		com.ccthanking.framework.params.ParaManager paramanager = com.ccthanking.framework.params.ParaManager
				.getInstance();
		com.ccthanking.framework.params.SysPara.SysParaConfigureVO syspara = paramanager
				.getSysParameter("CLUSTER_NODES");
		String nodes = syspara == null ? null : syspara
				.getSysParaConfigureParavalue1();
		if (nodes != null && nodes.length() > 0) {
			String[] nodeslist = nodes.split(",");
			if (nodeslist.length > 0) {
				clusterNodes = new java.util.ArrayList();
				for (int i = 0; i < nodeslist.length; i++) {
					clusterNodes.add(nodeslist[i]);
				}
			}
		}
		return clusterNodes;
	}

	public static void register(String cacheObject, Object cacheInstance) {
		if (cacheObjectInstance.containsKey(cacheObject))
			return;
		cacheObjectInstance.put(cacheObject, cacheInstance);
	}
}