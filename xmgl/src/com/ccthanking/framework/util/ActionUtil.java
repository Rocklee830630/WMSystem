package com.ccthanking.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ccthanking.framework.base.BaseDispatchAction;

/**
 * @des action通用类,误删
 */
public class ActionUtil extends BaseDispatchAction {

	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String filepath = request.getParameter("path");
		if (filepath == null || "".equals(filepath))
			throw new Exception("没有找到文件路径！");
		filepath = filepath.replaceAll("\\|", "\\\\");
		String filename = request.getParameter("filename");
		if (filename == null || "".equals(filename))
			filename = "temp.xls";
		try {
			File fp = new File(filepath);
			if (fp.exists() && fp.isFile()) {
				FileInputStream fr = new FileInputStream(fp);
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-type", "application/x-msdownload");
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader(
						"Content-Disposition",
						"attachment; filename="
								+ new String(filename.getBytes(), "ISO8859_1"));

				int i = 0;
				int ch = -1;

				OutputStream os = response.getOutputStream();
				while ((ch = fr.read()) != -1) {
					i++;
					os.write(ch);
				}
				os.flush();
				os.close();
				fr.close();
			}
			fp = null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

}