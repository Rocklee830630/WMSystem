package com.ccthanking.framework.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseBO {

	public BaseBO() {
	}

	public static Log log = LogFactory.getLog(BaseBO.class);

	public static String getDomXml(BaseVO vo) throws Exception {
		if (vo == null)
			return null;
		ArrayList list = new ArrayList();
		list.add(vo);
		return getDomXml(list);
	}

	public static String getDomXml(Collection set) throws Exception {
		if (set == null)
			return null;
		String xml = "<RESPONSE><RESULT>";
		try {
			Iterator itor = set.iterator();
			xml += ((BaseVO) itor.next()).getRowXml();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return xml + "</RESULT></RESPONSE>";
	}

	public static String handleError(Exception e) {
		return BaseDispatchAction.handleError(e);
	}
}
