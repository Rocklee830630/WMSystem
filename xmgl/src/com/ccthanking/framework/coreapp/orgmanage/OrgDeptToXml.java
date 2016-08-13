package com.ccthanking.framework.coreapp.orgmanage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;

public class OrgDeptToXml{
	
    public static String strEncoding = "GB2312";
    
    public OrgDeptToXml(){
    }

    //生成市局xml文件
    public static final void createCityXml(String filePath, String sjCode)
        throws Exception
    {
        String filename = filePath + "\\" + sjCode.substring(0, 4) +
            "00000000.xml";
        String sql = "select row_id code ,dept_name name ,dept_name spell ,dept_name aspell,'1' leaf "
            +" from FS_org_dept t where t.depttype <= 4 start with t.row_id='"+sjCode+"'"
            +" connect by prior t.row_id = t.dept_parant_rowid";
        String[][] qs = DBUtil.query(sql);
        doImp(filename, qs, filePath);
    }

    //组织机构（简称）xml文件
    public static final void exportAllXml(String filePath, String fileName,
                                          String deptTypeList)
        throws
        Exception
    {
        String filename = filePath + "/dic/" + fileName + ".xml";
        // 由部门全程调整成部门简称 add by xhb
		String sql = "select t.row_id code ,t.bmjc name ,t.dept_name spell ,t.dept_name aspell ,decode(nvl(tt.n,0),0,1,2),nvl(t.DEPT_PARANT_ROWID,0) "
			+ " from FS_org_dept t ,(select  t.DEPT_PARANT_ROWID,count(*) n from  FS_org_dept t where t.ACTIVE_FLAG ='1' group by t.DEPT_PARANT_ROWID ) tt "
			//+ " where t.ACTIVE_FLAG ='1' and depttype in (" + deptTypeList + ") and t.row_id = tt.DEPT_PARANT_ROWID(+) order by row_id";
			+ " where t.ACTIVE_FLAG ='1' and t.row_id = tt.DEPT_PARANT_ROWID(+) order by row_id";
        String[][] qs = DBUtil.query(sql);
        doImp(filename, qs, filePath);
    }
    
    //组织机构（全称）xml文件
    public static final void exportDeptFullNameXml(String filePath, String fileName,
                                          String deptTypeList)
        throws
        Exception
    {
        String filename = filePath + "/dic/" + fileName + ".xml";
        // 由部门全程调整成部门简称 add by xhb
		String sql = "select t.row_id code ,t.dept_name name ,t.dept_name spell ,t.dept_name aspell ,decode(nvl(tt.n,0),0,1,2),nvl(t.DEPT_PARANT_ROWID,0) "
			+ " from FS_org_dept t ,(select  t.DEPT_PARANT_ROWID,count(*) n from  FS_org_dept t where t.ACTIVE_FLAG ='1' group by t.DEPT_PARANT_ROWID ) tt "
			//+ " where t.ACTIVE_FLAG ='1' and depttype in (" + deptTypeList + ") and t.row_id = tt.DEPT_PARANT_ROWID(+) order by row_id";
			+ " where t.ACTIVE_FLAG ='1' and t.row_id = tt.DEPT_PARANT_ROWID(+) order by row_id";
        String[][] qs = DBUtil.query(sql);
        doImp(filename, qs, filePath);
    }
    
	public static final void exportAllXml(String filePath, String fileName) throws Exception {
		String filename = filePath + "/dic/" + fileName + ".xml";
		String sql = "select t.row_id code ,t.dept_name name ,t.dept_name spell ,t.dept_name aspell ,decode(nvl(tt.n,0),0,1,2),nvl(t.DEPT_PARANT_ROWID,0) "
				+ " from FS_org_dept t ,(select  t.DEPT_PARANT_ROWID,count(*) n from  FS_org_dept t where t.ACTIVE_FLAG ='1' group by t.DEPT_PARANT_ROWID ) tt "
				+ " where t.ACTIVE_FLAG ='1' and t.row_id = tt.DEPT_PARANT_ROWID(+) order by row_id";
		String[][] qs = DBUtil.query(sql);
		doImp(filename, qs, filePath);
	}
    private static final void doImp(String filename, String[][] dic,
                                    String filePath)
        throws Exception
    {
        FileOutputStream fos = null;
        try
        {
            Document domresult = DocumentFactory.getInstance().createDocument();
            Element root = domresult.addElement("DATA");
            //add by wangzh 设置组织机构字典
            root.setAttributeValue("dicwidth","330");
            File file = new File(filePath);
            file.mkdirs();
            file = null;
            fos = new FileOutputStream(filename);
            OutputStream os = fos;
            SpellCache spellClass = SpellCache.getInstance();
            for (int i = 0; i <= dic.length - 1; i++)
            {
                String code = dic[i][0];
                String desc = dic[i][1];
                String spell = spellClass.getSpell(dic[i][2]);
                String aspell = spellClass.getAspell(dic[i][3]);
                String leaf = spellClass.getAspell(dic[i][4]);
                String parent = spellClass.getAspell(dic[i][5]);
                Element resultRoot = root.addElement("R");
                resultRoot.addAttribute("c", code);
                resultRoot.addAttribute("t", desc);
                resultRoot.addAttribute("s", spell);
                resultRoot.addAttribute("a", aspell);
                resultRoot.addAttribute("l", leaf);
                resultRoot.addAttribute("p", parent);

            }
            OutputFormat of = new OutputFormat();
            of.setEncoding(strEncoding);
            of.setIndent(true);
            of.setNewlines(true);
            XMLWriter writer = new XMLWriter(os, of);
            writer.write(domresult);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (fos != null)
            {
                fos.close();
            }
        }
    }
}