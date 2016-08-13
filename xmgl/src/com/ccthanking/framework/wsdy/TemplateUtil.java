package com.ccthanking.framework.wsdy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.ccthanking.common.FjlbManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.dic.Dics;
import com.ccthanking.framework.dic.TreeNode;
import com.ccthanking.framework.util.Pub;

/**
 * <p>Title: ppx</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TemplateUtil
{
    public TemplateUtil()
    {
    }



/*    *//**
     * 处理html模版文件打印
     * @param request
     * @param response
     * @param doc
     * @param templateBlob
     * @throws java.lang.Exception
     *//*
    public StringBuffer printHTML(String templateType,HttpServletRequest request,HttpServletResponse response,Document doc,byte[] templateBlob,String templateid,String sjbh,String ywlx,String isEdit,String isSp,String spzt)
        throws Exception
    {
    	// add by guanchb@2009-01-12 start
    	//Connection conn = DBUtil.getConnection();
    	// add by guanchb@2009-01-12 end
        StringBuffer buffer = new StringBuffer();
        List printRoot = doc.selectNodes("//PRINTDATA/ROW");
//        response.setContentType("application/x-msdownload");
//        response.setHeader("Content-type","application/x-msdownload");
//        response.setHeader("Accept-Ranges","bytes");
//        response.setHeader("Content-Disposition","attachment; filename="+"wwss");
//
        String fieldname = null;
        String domain  = null;
        String canEdit = null;
        String domain_style = null;
        String spTextAreaScript = null;
        String mutilRow = null;
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(templateBlob);
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteInputStream,"GBK"));
        String s = null;
        String amount ="=";
        //if("3".equalsIgnoreCase(templateType)){
        response.setContentType("message/rfc822");
        HashMap hash = getMutilPageField(doc);
//        System.out.println(doc.asXML());
        String mutilPageField = this.getMutilPageField();
        int pageCount = 0;
        int pageSize = hash.size();


        //}
        User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
        boolean hasAzt = false;
        try
        {
            s = reader.readLine();
            while (s != null)

            {
                buffer.append(s + "\r\n");
                s = reader.readLine();
                if (s!=null&&s.indexOf("<@") > 0 && s.indexOf("@>") > 0)//解析普通域
                {
                    String tagName = s.substring(s.indexOf("<@")+2,s.indexOf("@>"));
                    String tdText = "";
                    for (int i = 0; i < printRoot.size(); i++)
                    {
                        Element ele = (Element) printRoot.get(i);
                        if (ele.getNodeType() != Node.ELEMENT_NODE)
                        {
                            continue;
                        }
                        fieldname = ele.attributeValue("fieldname");
                        domain = ele.attributeValue("domain");
                        canEdit = ele.attributeValue("canedit");
                        domain_style = ele.attributeValue("domain_style");
                        mutilRow = ele.attributeValue("mutilrow");
                        if(tagName.equalsIgnoreCase(fieldname)){
                            if(!(PubWS.FIELDEXTEN_IMAGE).equals(domain)&&!(PubWS.FIELDEXTEN_WRITE).equals(domain)&&!(PubWS.FIELDEXTEN_WRITEANDMIND).equals(domain)&&!(PubWS.FIELDEXTEN_MIND).equals(domain)&&!(PubWS.FIELDEXTEN_SEAL).equals(domain)&&!(PubWS.FIELDEXTEN_SEALANDMIND).equals(domain)&&!(PubWS.FIELDEXTEN_DATE).equals(domain)&&!(PubWS.FIELDEXTEN_USER).equals(domain)){
                                if("1".equals(canEdit)){//
                                    if("1".equals(isEdit)){
                                        String str = ele.getText();
                                        if(str!=null&&!"".equals(str)){
                                            if(str.indexOf("<br>")>0){
                                                str = str.replaceAll("<br>","\r\n");
                                            }
                                            if(str.indexOf("<br/>")>0){
                                                str = str.replaceAll("<br/>","\r\n");
                                            }
                                        }
                                        String textAreaText = null;
                                        if (!Pub.empty(domain_style))
                                        {
                                        	textAreaText = "<textArea id"+amount+""+fieldname+"TextArea fieldname"+amount+""+fieldname+" style"+amount+"\""+domain_style+"\">"+str+"</textArea>";
                                        }else
                                        {
                                         textAreaText = "<textArea id"+amount+""+fieldname+"TextArea fieldname"+amount+""+fieldname+" style"+amount+"\"width:100%;height:100%\" rows"+amount+"8>"+str+"</textArea>";
                                        }
                                         tdText = textAreaText;
                                        break;
                                    }else{
                                      if("true".equalsIgnoreCase(mutilRow)){//处理多值项
                                          //判断是否有规则
                                          List rule = ele.elements("RULE");
                                          HashMap ruleMap = new HashMap();
                                          if(rule!=null&&rule.size()>0)
                                          {
                                              for(int j =0;j<rule.size();j++)
                                              {
                                                  Element crule = (Element)rule.get(j);
                                                  ruleMap.put(crule.attributeValue("ruletype"),crule.getText());
                                              }
                                          }

                                          String eStyle = "";
                                          String stext = "<table width"+amount+"\"100%\" height"+amount+"\"100%\" style"+amount+"\"padding: 0 0 0 0;\" cellpadding"+amount+"0 cellspacing"+amount+"0>";
                                          List rol =  ele.elements("ROL");
                                          if(rol!=null&&rol.size()>0){
                                              for (int m = 0; m < rol.size(); m++)
                                              {
                                                  stext += "<tr>";
                                                  Element erol = (Element)rol.get(m);
                                                  List col =  erol.elements("COL");
                                                  if(col!=null&&col.size()>0){
                                                      String customCssText = "";//自定义样式
                                                      if(ruleMap.size()>0)
                                                      {
                                                        if(m ==0){
                                                            if((String)ruleMap.get(PubWS.RULE_MUTIL_STYLE_FIRST)!=null)
                                                            {
                                                               customCssText = (String)ruleMap.get(PubWS.RULE_MUTIL_STYLE_FIRST);
                                                            }
                                                            else if((String)ruleMap.get(PubWS.RULE_MUTIL_STYLE)!=null)
                                                            {
                                                                customCssText = (String)ruleMap.get(PubWS.RULE_MUTIL_STYLE);
                                                            }
                                                        }else{
                                                            if((String)ruleMap.get(PubWS.RULE_MUTIL_STYLE)!=null)
                                                            {
                                                                customCssText = (String)ruleMap.get(PubWS.RULE_MUTIL_STYLE);
                                                            }
                                                        }
                                                      }
                                                      //|号分割的方式描述每个单元格的样式
                                                     StringTokenizer t = null;
                                                     if(!Pub.empty(customCssText))
                                                     t =  new StringTokenizer(customCssText,"|");
                                                     ArrayList customCss = new ArrayList();
                                                     if(t!=null)
                                                     while(t.hasMoreTokens())
                                                     {
                                                         customCss.add(t.nextToken());
                                                     }
                                                      for (int n = 0;n < col.size(); n++)
                                                      {
                                                          Element ecol = (Element)col.get(n);
                                                          if(m==0){
                                                              if(n==0){
                                                                 eStyle="border-top:none;border-right:none;border-left:none;";
                                                              }else if(n==col.size()-1){
                                                                  eStyle="border-left:none;border-top:none;border-right:none;";
                                                              }else{
                                                                  eStyle="border-left:none;border-top:none;";
                                                              }
                                                          }else if(m==rol.size()-1){
                                                              if(n==0){
                                                                  eStyle = "border-top:none;border-left:none;";
                                                             }else if(n==col.size()-1){
                                                                 eStyle = "border-top:none;border-left:none;border-right:none;";
                                                             }else{
                                                                 eStyle = "border-top:none;border-left:none;";
                                                             }
                                                          }else{
                                                              if(n==0){
                                                                  eStyle = "border-top:none;border-left:none;";
                                                             }else if(n==col.size()-1){
                                                                 eStyle = "border-top:none;border-left:none;border-bottom:none;border-right:none;";
                                                             }else{
                                                                 eStyle = "border-top:none;border-left:none;";
                                                             }
                                                          }
                                                          //自定义格式
                                                          String ocssText = "";
                                                          if(customCss.size()>0)
                                                          {
                                                              ocssText = (String)customCss.get(n);
                                                          }
                                                          stext += "<td  style"+amount+"\"mso-style-parent:style0;border:.5pt solid windowtext;"+eStyle+""+ocssText+"\" align"+amount+"center >"+ecol.getText()+"</td>";
                                                      }
                                                  }
                                                  stext += "</tr>";
                                              }
                                          }
                                          stext += "</table>";
                                          tdText = stext;
                                          break;
                                      }else{
                                        String[] ss = ele.getText().split("\n");
                                        if(fieldname.equalsIgnoreCase(mutilPageField)){
                                            if(pageSize>0&&pageCount<pageSize){
                                                ss =((String)hash.get(String.valueOf(pageCount+1))).split("\n");
                                                pageCount++;
                                            }else{
                                                ss = new String[0];
                                            }
                                        }

                                        String textAreaText = "";
                                        try
                                        {
                                            for(int l=0;l<ss.length;l++){
                                                if (!Pub.empty(domain_style))
                                                {
                                                    textAreaText +="<span style" +amount + "'" +domain_style + "'>" +ss[l] +"</span><br>";
                                                }
                                                else
                                                {
                                                    textAreaText += "" +ss[l]  + "<br>";
                                                }
                                            }
                                            //textAreaText +="</table>";
                                            tdText = textAreaText;
                                            break;
                                        }
                                        catch (Exception ex2)
                                        {
                                            ex2.printStackTrace();
                                        }
                                        finally{
                                        }
                                      }
                                    }
                                }else{
                                  if("true".equalsIgnoreCase(mutilRow)){//处理多值项
                                        //判断是否有规则
                                        List rule = ele.elements("RULE");
                                        HashMap ruleMap = new HashMap();
                                        if(rule!=null&&rule.size()>0)
                                        {
                                            for(int j =0;j<rule.size();j++)
                                            {
                                                Element crule = (Element)rule.get(j);
                                                ruleMap.put(crule.attributeValue("ruletype"),crule.getText());
                                            }
                                        }

                                        String eStyle = "";
                                        String stext = "<table width"+amount+"\"100%\" height"+amount+"\"100%\" style"+amount+"\"padding: 0 0 0 0;\" cellpadding"+amount+"0 cellspacing"+amount+"0>";
                                        List rol =  ele.elements("ROL");
                                        if(rol!=null&&rol.size()>0){
                                            for (int m = 0; m < rol.size(); m++)
                                            {
                                                stext += "<tr>";
                                                Element erol = (Element)rol.get(m);
                                                List col =  erol.elements("COL");
                                                if(col!=null&&col.size()>0){
                                                    String customCssText = "";//自定义样式
                                                    if(ruleMap.size()>0)
                                                    {
                                                      if(m ==0){
                                                          if((String)ruleMap.get(PubWS.RULE_MUTIL_STYLE_FIRST)!=null)
                                                          {
                                                             customCssText = (String)ruleMap.get(PubWS.RULE_MUTIL_STYLE_FIRST);
                                                          }
                                                          else if((String)ruleMap.get(PubWS.RULE_MUTIL_STYLE)!=null)
                                                          {
                                                              customCssText = (String)ruleMap.get(PubWS.RULE_MUTIL_STYLE);
                                                          }
                                                      }else{
                                                          if((String)ruleMap.get(PubWS.RULE_MUTIL_STYLE)!=null)
                                                          {
                                                              customCssText = (String)ruleMap.get(PubWS.RULE_MUTIL_STYLE);
                                                          }
                                                      }
                                                    }
                                                    //|号分割的方式描述每个单元格的样式
                                                   StringTokenizer t = null;
                                                   if(!Pub.empty(customCssText))
                                                   t =  new StringTokenizer(customCssText,"|");
                                                   ArrayList customCss = new ArrayList();
                                                   if(t!=null)
                                                   while(t.hasMoreTokens())
                                                   {
                                                       customCss.add(t.nextToken());
                                                   }
                                                    for (int n = 0;n < col.size(); n++)
                                                    {
                                                        Element ecol = (Element)col.get(n);
                                                        if(m==0){
                                                            if(n==0){
                                                               eStyle="border-top:none;border-left:none;";
                                                            }else if(n==col.size()-1){
                                                                eStyle="border-left:none;border-top:none;border-right:none;";
                                                            }else{
                                                                eStyle="border-left:none;border-top:none;";
                                                            }
                                                        }else if(m==rol.size()-1){
                                                            if(n==0){
                                                                eStyle = "border-top:none;border-left:none;";
                                                           }else if(n==col.size()-1){
                                                               eStyle = "border-top:none;border-left:none;border-right:none;";
                                                           }else{
                                                               eStyle = "border-top:none;border-left:none;";
                                                           }
                                                        }else{
                                                            if(n==0){
                                                                eStyle = "border-top:none;border-left:none;";
                                                           }else if(n==col.size()-1){
                                                               eStyle = "border-top:none;border-left:none;border-right:none;";
                                                           }else{
                                                               eStyle = "border-top:none;border-left:none;";
                                                           }
                                                        }
                                                        //自定义格式
                                                        String ocssText = "";
                                                        if(customCss.size()>0)
                                                        {
                                                            ocssText = (String)customCss.get(n);
                                                        }
                                                        stext += "<td  style"+amount+"\"mso-style-parent:style0;border:.5pt solid windowtext;"+eStyle+""+ocssText+"\" align"+amount+"center >"+ecol.getText()+"</td>";
                                                    }
                                                }
                                                stext += "</tr>";
                                            }
                                        }
                                        stext += "</table>";
                                        tdText = stext;
                                        break;


                                  }else{

                                    String[] ss = ele.getText().split("\n");
                                    if(fieldname.equalsIgnoreCase(mutilPageField)){
                                        if(pageSize>0&&pageCount<pageSize){
                                            ss =((String)hash.get(String.valueOf(pageCount+1))).split("\n");
                                            pageCount++;
                                        }else{
                                            ss = new String[0];
                                        }

                                    }
                                    //处理部分规则内容
                                    String hasrule = ele.attributeValue("hasrule");
                                    String ruleText = null;
                                    int NfirstPage = 0;
                                    int Cpage = 0;
                                    String NcssText = "";
                                    String nullRuleText = "";
                                    if("true".equalsIgnoreCase(hasrule))
                                    {
                                         List rule = ele.elements("RULE");
                                         if(rule!=null)
                                         for(int j = 0;j<rule.size();j++)
                                         {
                                                 Element erule = (Element)rule.get(j);
                                                 //域为空时，显示“规则内容”
                                                 if(PubWS.RULE_NULL_PAGE.equalsIgnoreCase(erule.attributeValue("ruletype"))){
                                                         nullRuleText = erule.getText();
                                                 }
                                                 //多页显示规则内容
                                                 if(PubWS.RULE_MUTIL_PAGE.equalsIgnoreCase(erule.attributeValue("ruletype"))){
                                                         ruleText = erule.getText();
                                                 }
                                         }
                                    }
                                    if(ruleText!=null){
                                        //分页描述 用|分割方式 第一个参数为首页显示行数,2为其他也显示行数,3为尾页显示页数,4为每行显示字数,5如果独占一页时ie分页打印首页行数，6没页行数，7没页第一行样式
                                        StringTokenizer t = null;
                                        if(!Pub.empty(ruleText))
                                        t =  new StringTokenizer(ruleText,"|");
                                        ArrayList customRule = new ArrayList();
                                        if(t!=null)
                                        while(t.hasMoreTokens())
                                        {
                                            customRule.add(t.nextToken());
                                        }
                                        //modified by xukx 如果没有配置5,6,7的话,有越界错误,增加一个判断
                                        if(customRule.size()>4 && customRule.get(4)!=null){
                                            NfirstPage =  Integer.parseInt((String)customRule.get(4));
                                          }
                                        if(customRule.size()>5 && customRule.get(5)!=null){
                                            Cpage =  Integer.parseInt((String)customRule.get(5));
                                          }
                                        if(customRule.size()>6 && customRule.get(6)!=null){
                                            NcssText =  (String)customRule.get(6);
                                          }
                                    }
                                    int te = NfirstPage;
                                    String addCssText = "";
                                    for(int l=0;l<ss.length;l++){
                                        if(ss[l].equals("")){
                                            ss[l]=nullRuleText;
                                        }
                                        if(te>0){
                                            if(l == te){
                                                addCssText = NcssText;
                                                te += Cpage;
                                                //added by xukx 上横线就是不能显示在下一页,没辄了,加了个带下划线的空行,空行高度是0%
                                                tdText +="<span style"+amount+"'"+domain_style+" line-height:0%; "+"'>"+"<br>"+"</span>";
                                                te++;
                                                //end
                                            }else {
                                                addCssText = "";
                                            }
                                        }

                                        if(!Pub.empty(domain_style)){
                                            tdText +="<span style"+amount+"'"+domain_style+addCssText+"'>"+ss[l]+"</span><br>";
                                        }else{
                                            tdText += ss[l]+ "<br>";
                                        }
                                    }
                                    //added by andy 解决落款带局长电子印章，打印时分页显示切割问题20081230
                                    if(NfirstPage>0)
                                    {
	                                    switch(Integer.parseInt(templateid,10))
	                                    {
	                                       case 330:
	                                       case 373:
			                                    	   int n = ss.length;
			                                    	   if(n<NfirstPage+Cpage && n<NfirstPage*2)
			                                    	   {	   
				                                    	   if(n+3>NfirstPage && (n+3)%NfirstPage>0)
				                                    	   {	
					                                              while(n%NfirstPage<NfirstPage && (n%NfirstPage+3)>NfirstPage)
					                                              {
					                                           	    tdText += "<span style"+amount+"'"+domain_style+addCssText+"'>"+""+"</span><br>";
					                                           	    n++;
					                                              }
				                                    	   }
			                                    	   }else
			                                    	   {
			                                    		   if(n-NfirstPage+3>Cpage && (n-NfirstPage+3)%Cpage>0)
				                                    	   {	
					                                              while((n-NfirstPage)%Cpage<Cpage && ((n-NfirstPage)%Cpage+3)>Cpage)
					                                              {
					                                           	    tdText += "<span style"+amount+"'"+domain_style+addCssText+"'>"+""+"</span><br>";
					                                           	    n++;
					                                              }
				                                    	   }
			                                    	   }
	                                    	   break;
	                                    }
                                    }   
                                    //end
                                    break;
                                  }
                                }
                            }else if((PubWS.FIELDEXTEN_MIND).equals(domain)||(PubWS.FIELDEXTEN_DATE).equals(domain)||(PubWS.FIELDEXTEN_USER).equals(domain)){
                                boolean isNowSpYj = false;
                                if(!"1".equals(isSp)){
                                    if(ele.attributeValue("avail")!=null&&"true".equalsIgnoreCase(ele.attributeValue("avail"))){//判断审批意见域是否有效
                                    	//modified by andy 20090105 将审批意见域的\n替换成<br>
                                    	String temp = ele.getText();
                                    	if(temp.indexOf("\r\n")>=0)
                                    		temp = temp.replaceAll("\r\n", "<br>");
                                    	else if(temp.indexOf("\n")>=0)
                                    		temp = temp.replaceAll("\n", "<br>");
                                    	
                                    	 tdText = temp;
                                    	//end;
                                        break;
                                    }else{
                                        continue;
                                    }
                                }else if((PubWS.FIELDEXTEN_DATE).equals(domain)||(PubWS.FIELDEXTEN_USER).equals(domain)){ //add by songxb@2007-12-21
                                    if(ele.attributeValue("avail")!=null&&"true".equalsIgnoreCase(ele.attributeValue("avail"))){//判断审批人/审批时间是否有效
                                        tdText = ele.getText();
                                        break;
                                    }else{
                                        continue;
                                    }
                                }
                                String mSpText = ele.getText();
                                if(mSpText == null) mSpText = "";
                                String textAreaText = "<textArea id"+amount+"textAreaText style"+amount+"\"width:100%;height100%;\" rows"+amount+"4>"+mSpText+"</textArea>";
                                String domainrole = ele.attributeValue("approverole");
                                String domainlevel = ele.attributeValue("approvelevel");
                                if (Pub.empty(domainrole) && Pub.empty(domainlevel))
                                {
                                    tdText = textAreaText;
                                    isNowSpYj = true;
                                    break;
                                }
                                else if (!Pub.empty(domainrole) && Pub.empty(domainlevel))
                                { //文书定义的role非空
                                    Role[] role = user.getRoles();
                                    for(int k = 0;k<role.length;k++){
                                        if (domainrole.equalsIgnoreCase(role[k].getName()))
                                        { //节点role不为空，判断两个role
                                            tdText = textAreaText;
                                            isNowSpYj = true;
                                            break;
                                        }
                                    }

                                }
                                else if (!Pub.empty(domainlevel) && Pub.empty(domainrole))
                                { //节点level不为空
                                    if (domainlevel.equals(user.getLevelName()))
                                    {
                                        tdText = textAreaText;
                                        isNowSpYj = true;
                                        break;
                                    }
                                }
                                else if (!Pub.empty(domainrole) && !Pub.empty(domainlevel))
                                { //文书定义的role,level非空
                                    Role[] role = user.getRoles();
                                    for(int k = 0;k<role.length;k++){
                                        if (domainrole.equalsIgnoreCase(role[k].getName()))
                                        {
                                            if (domainlevel.equals(user.getLevelName()))
                                            {
                                                tdText = textAreaText;
                                                isNowSpYj = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(!isNowSpYj){
                                    if(ele.attributeValue("avail")!=null&&"true".equalsIgnoreCase(ele.attributeValue("avail"))){
                                        //modified by andy 20090105 将审批意见域的\n替换成<br>
	                                    	String temp = ele.getText();
	                                    	if(temp.indexOf("\r\n")>=0)
	                                    		temp = temp.replaceAll("\r\n", "<br>");
	                                    	else if(temp.indexOf("\n")>=0)
	                                    		temp = temp.replaceAll("\n", "<br>");
	                                    	
	                                    	tdText = temp;
                                    	//end;
                                     //   break;
                                    }else{
                                        continue;
                                    }
                                }
                            }else if((PubWS.FIELDEXTEN_IMAGE).equals(domain)){
                                tdText ="<object classid"+amount+"\"clsid:25B67EB3-5F48-4775-BE04-E42FA3D121BA\" id"+amount+"\""+fieldname+"\" style"+amount+"\"WIDTH: 100%;HEIGHT: 100%;Z-Index: 100;\" border"+amount+"2>\r\n";
                                //modified by zhangt 20090427 start: 将照片域默认样式背景颜色改为白色。
                                tdText +="  	<param name=3D\"Color\" value=3D\"FFFFFF\">\r\n";
                                //modified by zhangt 20090427 end;
                                tdText +="  	<param name=3D\"MaxImgSize\" value=3D\"1024\">\r\n";
                                tdText +="  	<param name=3D\"Stretch\" value=3D\"1\">\r\n";
                                tdText +="  	<param name=3D\"Mode\" value=3D\"Pic\">\r\n";
                                tdText +="      <param name=3D\"IsEdit\" value=3D\"false\">\r\n";
                                tdText += "</object>\r\n";
                            }

                        }
                       // break;

                    }
                    s = s.substring(0,s.indexOf("<@"))+tdText+s.substring(s.indexOf("@>")+2,s.length());

                }else if( s!=null&&s.indexOf("AztScript")>0){//解析是否盖章 并形成盖章的js
                   s="";
                   ArrayList arrayList = new ArrayList();
                   QuerySet qs = DBUtil.executeQuery("select * from ws_electron_print where WS_TEMPLATE_ID='"+templateid+"' and sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'",null);

                   PubWS pubws = new PubWS();

                   if(qs.getRowCount()>0){
                       hasAzt = true;
                       for(int i = 0;i<qs.getRowCount();i++){
                       String[] aztDetail = new String[3];
                       aztDetail[0] = qs.getString(i+1,"XH");//序号
                       String approverole = qs.getString(i+1,"APPROVEROLE");
                       String approvelevel =  qs.getString(i+1,"APPROVELEVEL");
                       //获得盖章域的域名称（模版标签位置）
                       if(PubWS.YZLB_QM.equalsIgnoreCase(qs.getString(i+1,"YZLX"))){
                           Element aztDomain = pubws.canGetAzt(doc,approverole,approvelevel,pubws.FIELDEXTEN_WRITE);
                           if(aztDomain!=null)
                               aztDetail[1] = aztDomain.attributeValue("fieldname");//印章域
                           else
                               aztDetail[1] = "";
                               //获得印章保护域的域名称（模版标签位置），如果找不到则默认为印章域和保护域是同一个
                           Element aztProtectDomain = pubws.canGetAzt(doc,approverole,approvelevel,pubws.FIELDEXTEN_WRITEANDMIND);
                           if(aztProtectDomain!=null)
                               aztDetail[2] = aztProtectDomain.attributeValue("fieldname");//印章保护域
                           else
                               aztDetail[2]  = aztDetail[1] ;
                           arrayList.add(aztDetail);
                       }
                       if(PubWS.YZLB_YZ.equalsIgnoreCase(qs.getString(i+1,"YZLX"))){
                           Element aztDomain = pubws.canGetAzt(doc,approverole,approvelevel,pubws.FIELDEXTEN_SEAL);
                           if(aztDomain!=null)
                               aztDetail[1] = aztDomain.attributeValue("fieldname");//盖章域
                           else
                               aztDetail[1] = "";
                               //获得盖章保护域的域名称（模版标签位置），如果找不到则默认为盖章域和保护域是同一个
                           Element aztProtectDomain = pubws.canGetAzt(doc,approverole,approvelevel,pubws.FIELDEXTEN_SEALANDMIND);
                           if(aztProtectDomain!=null)
                               aztDetail[2] = aztProtectDomain.attributeValue("fieldname");//盖章保护域
                           else
                               aztDetail[2]  = aztDetail[1] ;
                           arrayList.add(aztDetail);
                         }
                       }

                   String function_ESAGetData = "function ESAGetData(control){\r\n";//监视盖章保护域脚本
                   String function_onShow = "function onShow(){\r\n";//显示印章脚本
                   String objects = "";//印章object
                   String pub_js = getPub_js(amount);//公用脚本
                   QuerySet qs_rule = null;//查询偏移量规则
                   for(int i=0;i<arrayList.size();i++){
                       String[] detail = (String[])arrayList.get(i);
                       //objects +="<object classid"+amount+"\"clsid:49FB4BA2-3A7A-4635-91CD-9C2D5470F8CF\" CODEBASE"+amount+"\"http://"+request.getHeader("host")+""+request.getContextPath()+"/cab/AztSOW.CAB#version"+amount+"1,0,0,0\" style"+amount+"\"position:absolute;top:300;left:100;width:1;height:1;z-index"+amount+"8\" width"+amount+"\"1\" height"+amount+"\"1\"id"+amount+"\"AztSow"+String.valueOf(i+1)+"\"></object>\r\n";

                       function_ESAGetData +="	       if (control "+amount+""+amount+" \"AztSow"+String.valueOf(i+1)+"\"){\r\n";
                       function_ESAGetData +="          if(document.all[\"" + detail[2] +"\"]){\r\n";
                       function_ESAGetData +="			return document.all[\"" + detail[2] +"\"].innerText ;\r\n";
                       function_ESAGetData +="          }else{\r\n";
                       function_ESAGetData +="          return \"\";}\r\n";
                       function_ESAGetData +="	       }\r\n";

                       //modified by xukx 20080218 印章偏移量从配置表中读取
                       String ws_electron_rule = " select left,top,domain_name from WS_ELECTRON_RULE where ws_template_id='"+templateid+"' ";
                       qs_rule = DBUtil.executeQuery(ws_electron_rule, null);
                       String left_excur = "0";
                       String top_excur = "0";
                       if(qs_rule.getRowCount()>0)
                       {
                    	 boolean flag = false;//标识 是否匹配成功
                    	 for(int m =0;m<qs_rule.getRowCount();m++)
                    	 {
                    	   String left = qs_rule.getString(m+1, "left");
                    	   String top = qs_rule.getString(m+1, "top");
                    	   String domain_name = qs_rule.getString(m+1, "domain_name");
                    	   if(detail[1].equals(domain_name))
                    	   {
                    	     left_excur = left;
                    	     top_excur = top;
                    	     flag = true;
                    	     break;
                    	   }
                    	 }
                       }

                       //end


                       function_onShow +="      if(document.all[\"" + detail[2] +"\"]&&document.all[\"" + detail[1] +"\"]){\r\n";

                       //added by xukx 解决打印后，印章位置偏移问题
                       function_onShow +="        var fieldText "+amount+" document.all[\"" + detail[1] +"\"].innerText;"+"\r\n";
                       function_onShow +="        var s "+amount+" '';"+"\r\n";
                       function_onShow +="        s +"+amount+" '<!--[if gte vml 1]>';"+"\r\n";
                       function_onShow +="        s +"+amount+" '<v:shapetype id"+amount+"\\\"_x0000_t75\\\" coordsize"+amount+"\\\"21600,21600\\\" o:spt"+amount+"\\\"175\\\" o:preferrelative"+amount+"\\\"t\\\" path"+amount+"\\\"m@4@5l@4@11@9@11@9@5xe\\\" filled"+amount+"\\\"f\\\" stroked"+amount+"\\\"f\\\"> ';"+"\r\n";
                       function_onShow +="		  s +"+amount+" '</v:shapetype>';"+"\r\n";
                       function_onShow +="        s +"+amount+" '<v:shape id"+amount+"\\\"shape"+String.valueOf(i+1)+"\\\" type"+amount+"\\\"#_x0000_t75\\\" style"+amount+"\\\"position:absolute;margin-left:"+left_excur+";margin-top:"+top_excur+";width:0;height:0;z-index:1\\\" filled"+amount+"\\\"t\\\" fillcolor"+amount+"\\\"window [65]\\\" strokecolor"+amount+"\\\"windowText [64]\\\" o:insetmode"+amount+"\\\"auto\\\">';"+"\r\n";
                       function_onShow +="        s +"+amount+" '</v:shape>';"+"\r\n";
                       function_onShow +="        s +"+amount+" '<span id"+amount+"\\\""+detail[1]+"\\\" name"+amount+"\\\""+detail[1]+"\\\">';"+"\r\n";
                       function_onShow +="        s +"+amount+" '<object classid"+amount+"\\\"clsid:49FB4BA2-3A7A-4635-91CD-9C2D5470F8CF\\\" CODEBASE"+amount+"\\\"http://"+request.getHeader("host")+""+request.getContextPath()+"/cab/AztSOW.CAB#version"+amount+"1,0,0,0\\\" style"+amount+"\\\"position:absolute;top:300;left:100;width:1;height:1;z-index"+amount+"8\\\" width"+amount+"\\\"1\\\" height"+amount+"\\\"1\\\"id"+amount+"\\\"AztSow"+String.valueOf(i+1)+"\\\" v:shapes"+amount+"\\\"shape"+String.valueOf(i+1)+"\\\" class"+amount+"shape></object>';"+"\r\n";
                       function_onShow +="        s +"+amount+" fieldText;"+"\r\n";
                       function_onShow +="        s +"+amount+" '</span><!----><![endif]>';"+"\r\n";
                       //function_onShow +="        alert(s);"+"\r\n";
                       function_onShow +="        var parentObj "+amount+" document.all[\"" + detail[1] +"\"].parentElement;"+"\r\n";
                       function_onShow +="        parentObj.innerHTML"+amount+"s;"+"\r\n";
                       //function_onShow +="        alert(parentObj.innerText);"+"\r\n";
                       //end

                       function_onShow +="        var SignContentURL"+String.valueOf(i+1)+" "+amount+" \"http://"+request.getHeader("host")+""+request.getContextPath()+"/WsPrint/PubWS.do?method"+amount+"getAztXML&xh"+amount+""+detail[0]+"\";\r\n"; //获取xml格式印章信息的URL";
                       function_onShow +="    	   AztSow"+String.valueOf(i+1)+".ESAUnmarshal(SignContentURL"+String.valueOf(i+1)+"); //解析xml格式印章信息\r\n";

                       //function_onShow +="	   var v "+amount+" GetAbsoluteLocation(\""+detail[1]+"\");\r\n";
                       //function_onShow +="         if(v)\r\n";
                       //function_onShow +="	   doMove(v[1]-10, v[0]-20, \"AztSow"+String.valueOf(i+1)+"\");\r\n";


                       //added by xukx 固定每个签名图片的显示大小  避免有的签名图片过大，显示超边的情况
                       //function_onShow +="     alert('degbu1');alert(document.all['AztSow"+String.valueOf(i+1)+"'].style.pixelWidth+':'+document.all['AztSow"+String.valueOf(i+1)+"'].style.pixelHeight);\r\n";
                       function_onShow +="     var aztObj "+amount+" document.all['AztSow"+String.valueOf(i+1)+"'];"+"\r\n";
                       function_onShow +="     var nw "+amount+" aztObj.style.pixelWidth;" +"\r\n";
                       function_onShow +="     var nh "+amount+" aztObj.style.pixelHeight;" +"\r\n";
                       function_onShow +="     if(aztObj.ESAGetProperty('SEALTYPE') && aztObj.ESAGetProperty('SEALTYPE')"+amount+amount+"'2'){"+"\r\n";
                       function_onShow +="       var aztWidth "+amount+" aztObj.style.pixelWidth;"+"\r\n";
                       function_onShow +="       var aztHeight "+amount+" aztObj.style.pixelHeight;"+"\r\n";
                       function_onShow +="       if(parseInt(aztWidth) > 82){"+"\r\n";
                       function_onShow +="          nh "+amount+" parseInt(nh * (1-(parseInt(aztWidth)-82)/parseInt(aztWidth)))"+";//按比例缩放  "+"\r\n";
                       function_onShow +="          nw "+amount+" 82;"+"\r\n";
                       function_onShow +="       }"+"\r\n";
                       function_onShow +="       if(parseInt(aztHeight) > 56){"+"\r\n";
                       function_onShow +="          nw "+amount+" parseInt(nw * (1-(parseInt(aztHeight)-56)/parseInt(aztHeight)))"+";//按比例缩放  "+"\r\n";
                       function_onShow +="          nh "+amount+" 56;"+"\r\n";
                       function_onShow +="       }"+"\r\n";
                       function_onShow +="     }"+"\r\n";
                       function_onShow +="     document.all[\"shape"+String.valueOf(i+1)+"\"].style.width"+amount+"nw;"+"\r\n";
                       function_onShow +="     document.all[\"shape"+String.valueOf(i+1)+"\"].style.height"+amount+"nh;"+"\r\n";
                       //end

                       function_onShow +="	   AztSow"+String.valueOf(i+1)+".ESAVerifySubmit();//校验印章信息\r\n ";
                       function_onShow +="      }\r\n";
                   }
                   function_ESAGetData +="}\r\n";

                   function_onShow +="      initSpyj();\r\n";
                   function_onShow +="      initBjqy();\r\n";
                   function_onShow +="      initImage();\r\n";
                   function_onShow +="}\r\n";

                   //buffer.append(objects);
                   //buffer.append(js);
                   buffer.append("<script language"+amount+"\"JavaScript\">\r\n");
                   buffer.append("onShow();\r\n");
                   buffer.append("function initImage(){\r\n");
                   buffer.append(getImageJs(doc,amount));
                   buffer.append("}\r\n");
                   buffer.append(function_ESAGetData);
                   // modified by guanchb@2009-01-11 start
                   // 解决问题：刑事呈请报告文书审批通过后，在“在办案件查询”模块中查看此文书无电子印章。
//                   String sql = " select ws_template_id from za_zfba_jcxx_ws_tycqspwslb where ws_template_id='"+templateid+"'";
//                   String[][] res = DBUtil.query(conn, sql);
//                   if(res != null){
//                	   if("9".equals(spzt)||"6".equals(spzt)||"2".equals(spzt))                   
//                		   buffer.append(function_onShow);
//                   }else{
//                	   buffer.append(function_onShow);
//                   }
				   // modified by guanchb@2009-01-11 end
                   buffer.append(pub_js);
                   buffer.append("function initSpyj(){\r\n");
                   buffer.append("    var spAreaText "+amount+" document.all(\"textAreaText\");\r\n");
                   buffer.append("    if(spAreaText){\r\n");
                   buffer.append("     var parentMind "+amount+" top.opener.document.all('txtCheckMind');\r\n");
                   buffer.append("     if(parentMind&&parentMind.value.length>0){\r\n");
                   buffer.append("     spAreaText.value "+amount+" parentMind.value ;\r\n");
                   buffer.append("      }\r\n");
                   buffer.append("    }\r\n");
                   buffer.append("}\r\n");
                   buffer.append("function initBjqy(){\r\n");
                   buffer.append("   var textAreaText "+amount+" document.getElementsByTagName(\"textArea\");\r\n");
                   buffer.append("   if(textAreaText){\r\n");
                   buffer.append("   if(textAreaText.length>0){\r\n");
                   buffer.append("      for(var i "+amount+"0;i<textAreaText.length;i++){\r\n");
                   buffer.append("       if(textAreaText[i].fieldname){\r\n");
                   buffer.append("       if(top.AlltextAreaArr){\r\n");
                   buffer.append("        if( top.AlltextAreaArr[textAreaText[i].fieldname])\r\n");
                   buffer.append("        textAreaText[i].value "+amount+" top.AlltextAreaArr[textAreaText[i].fieldname];\r\n");
                   buffer.append("        }\r\n");
                   buffer.append("     }\r\n");
                   buffer.append("    }\r\n");
                   buffer.append("   }\r\n");
                   buffer.append("}\r\n");
                   buffer.append("}\r\n");
                   buffer.append("</script>\r\n");
                   }else{
                       buffer.append("<script language"+amount+"\"JavaScript\">\r\n");
                       buffer.append("function onShow(){\r\n");
                       buffer.append("  initSpyj();\r\n");
                       buffer.append("  initBjqy();\r\n");
                       buffer.append("  initImage();\r\n");
                       buffer.append("}\r\n");
                       buffer.append("function initImage(){\r\n");
                       buffer.append(getImageJs(doc,amount));
                       buffer.append("}\r\n");
                       buffer.append("function initSpyj(){\r\n");
                       buffer.append("    var spAreaText "+amount+" document.all(\"textAreaText\");\r\n");
                       buffer.append("    if(spAreaText){\r\n");
                       buffer.append("     var parentMind "+amount+" top.opener.document.all('txtCheckMind');\r\n");
                       buffer.append("     if(parentMind&&parentMind.value.length>0){\r\n");
                       buffer.append("     spAreaText.value "+amount+" parentMind.value ;\r\n");
                       buffer.append("      }\r\n");
                       buffer.append("    }\r\n");
                       buffer.append("}\r\n");
                       buffer.append("function initBjqy(){\r\n");
                       buffer.append("   var textAreaText "+amount+" document.getElementsByTagName(\"textArea\");\r\n");
                       buffer.append("   if(textAreaText){\r\n");
                       buffer.append("   if(textAreaText.length>0){\r\n");
                       buffer.append("      for(var i "+amount+"0;i<textAreaText.length;i++){\r\n");
                       buffer.append("       if(textAreaText[i].fieldname){\r\n");
                       buffer.append("       if(top.AlltextAreaArr){\r\n");
                       buffer.append("        if( top.AlltextAreaArr[textAreaText[i].fieldname])\r\n");
                       buffer.append("        textAreaText[i].value "+amount+" top.AlltextAreaArr[textAreaText[i].fieldname];\r\n");
                       buffer.append("        }\r\n");
                       buffer.append("     }\r\n");
                       buffer.append("    }\r\n");
                       buffer.append("   }\r\n");
                       buffer.append("}\r\n");
                       buffer.append("}\r\n");
                       buffer.append("</script>\r\n");
                   }
               }else if(s!=null&&s.indexOf("TextAreaScript")>0){
                   s="";
                   buffer.append("<script language"+amount+"\"JavaScript\">\r\n");
                   buffer.append("function getEditText(){\r\n");
                   buffer.append("    var spAreaText "+amount+" document.all(\"textAreaText\");\r\n");
                   buffer.append("    if(spAreaText){\r\n");
                   buffer.append("    var parentMind "+amount+" top.opener.document.all('txtCheckMind');\r\n");
                   buffer.append("      if(parentMind)\r\n");
                   buffer.append("    parentMind.value "+amount+" spAreaText.value;\r\n");
                   buffer.append("    }\r\n");
                   buffer.append("   var textAreaText "+amount+" document.getElementsByTagName(\"textArea\");\r\n");
                   buffer.append("   if(textAreaText){\r\n");
                   buffer.append("   if(textAreaText.length>0){\r\n");
                   buffer.append("   var xmlText "+amount+" \"<EDITS>\";\r\n");
                   buffer.append("      for(var i "+amount+"0;i<textAreaText.length;i++){\r\n");
                   buffer.append("       if(textAreaText[i].fieldname){\r\n");
                   buffer.append("       if(top.AlltextAreaArr){\r\n");
                   buffer.append("        top.AlltextAreaArr[textAreaText[i].fieldname] "+amount+" textAreaText[i].value;\r\n");
                   buffer.append("       }else{\r\n");
                   buffer.append("        top.AlltextAreaArr"+amount+"[];\r\n");
                   buffer.append("        top.AlltextAreaArr[textAreaText[i].fieldname] "+amount+" textAreaText[i].value;\r\n");
                   buffer.append("       }\r\n");

                   buffer.append("       xmlText +"+amount+"\"<ROW tagname"+amount+"'\"+textAreaText[i].fieldname+\"'>\"+textAreaText[i].value+\"</ROW>\";\r\n");
                   buffer.append("       }\r\n");
                   buffer.append("      }\r\n");
                   buffer.append("       xmlText +"+amount+"\"</EDITS>\";\r\n");
                   if(PubWS.FILE_HTML.equalsIgnoreCase(templateType)&&"1".equals(isEdit)){
                   buffer.append("       var sActionURL "+amount+"\"/jwzh/WsPrint/PubWS.do?method"+amount+"getEditXML&templateid"+amount+""+templateid+"&sjbh"+amount+sjbh+"&ywlx"+amount+ywlx+"\";\r\n");
                   buffer.append("       top.opener.doGetWsEdit(sActionURL,xmlText);\r\n");
                   }
                   buffer.append("     }\r\n");
                   buffer.append("   }\r\n");
                   if(PubWS.FILE_HTML.equalsIgnoreCase(templateType)&&"1".equals(isEdit)){
                   //buffer.append("return \"保存退出?\";\r\n");
                   buffer.append("return ;\r\n");
                   }
                   buffer.append("   }\r\n");
                   buffer.append("</script>\r\n");

                   *//*****************************
                    * add by songxb @2007-10-25
                    * 单sheet页 html
                    * begin
                    *****************************//*

                       s="";
                   buffer.append("<script language"+amount+"\"JavaScript\">\r\n");
                   buffer.append("function parentGetEditText(){\r\n");
                   buffer.append("    var spAreaText "+amount+" document.all(\"textAreaText\");\r\n");
                   buffer.append("    if(spAreaText){\r\n");
                   buffer.append("    var parentMind "+amount+" top.top.opener.document.all('txtCheckMind');\r\n");
                   buffer.append("      if(parentMind)\r\n");
                   buffer.append("    parentMind.value "+amount+" spAreaText.value;\r\n");
                   buffer.append("    }\r\n");
                   buffer.append("   var textAreaText "+amount+" document.getElementsByTagName(\"textArea\");\r\n");
                   buffer.append("   if(textAreaText){\r\n");
                   buffer.append("   if(textAreaText.length>0){\r\n");
                   buffer.append("   var xmlText "+amount+" \"<EDITS>\";\r\n");
                   buffer.append("      for(var i "+amount+"0;i<textAreaText.length;i++){\r\n");
                   buffer.append("       if(textAreaText[i].fieldname){\r\n");
                   buffer.append("       if(top.top.AlltextAreaArr){\r\n");
                   buffer.append("        top.top.AlltextAreaArr[textAreaText[i].fieldname] "+amount+" textAreaText[i].value;\r\n");
                   buffer.append("       }else{\r\n");
                   buffer.append("        top.top.AlltextAreaArr"+amount+"[];\r\n");
                   buffer.append("        top.top.AlltextAreaArr[textAreaText[i].fieldname] "+amount+" textAreaText[i].value;\r\n");
                   buffer.append("       }\r\n");

                   buffer.append("       xmlText +"+amount+"\"<ROW tagname"+amount+"'\"+textAreaText[i].fieldname+\"'>\"+textAreaText[i].value+\"</ROW>\";\r\n");
                   buffer.append("       }\r\n");
                   buffer.append("      }\r\n");
                   buffer.append("       xmlText +"+amount+"\"</EDITS>\";\r\n");
                   if(PubWS.FILE_HTML.equalsIgnoreCase(templateType)&&"1".equals(isEdit)){
                   buffer.append("       var sActionURL "+amount+"\"/jwzh/WsPrint/PubWS.do?method"+amount+"getEditXML&templateid"+amount+""+templateid+"&sjbh"+amount+sjbh+"&ywlx"+amount+ywlx+"\";\r\n");
                   buffer.append("       top.top.opener.doGetWsEdit(sActionURL,xmlText);\r\n");
                   }
                   buffer.append("     }\r\n");
                   buffer.append("   }\r\n");
                   if(PubWS.FILE_HTML.equalsIgnoreCase(templateType)&&"1".equals(isEdit)){
                   //buffer.append("return \"保存退出?\";\r\n");
                   buffer.append("return ;\r\n");
                   }
                   buffer.append("   }\r\n");
                   buffer.append("</script>\r\n");
                   *//*****************************
                    * add by songxb @2007-10-25
                    * 单sheet页 html
                    * end
                    *****************************//*

               }else if(s!=null&&s.indexOf("FrameSetUnloadScript")>0){
                   s="";
//                   buffer.append("<script language"+amount+"\"JavaScript\">\r\n");
                   buffer.append("function frameunload(){\r\n");
                   if("1".equals(isEdit)){
                   buffer.append("try\r\n");
                   buffer.append("{\r\n");
                   buffer.append("for(var i "+amount+"0; i<frames.length; i++){\r\n");
                   buffer.append(" if(frames[i].document.body.onbeforeunload)\r\n");
                   buffer.append(" {\r\n");
                   buffer.append("  var func "+amount+" frames[i].document.body.onbeforeunload;\r\n");
                   buffer.append("  func();\r\n");
                   buffer.append(" }\r\n");
                   buffer.append("}\r\n");
                   buffer.append("}\r\n");
                   buffer.append("catch(e)\r\n");
                   buffer.append("{\r\n");
                   buffer.append(" alert(e.description);\r\n");
                   buffer.append("}\r\n");
                   buffer.append("  var xmlText "+amount+" \"<EDITS>\";\r\n");
                   buffer.append("for(var i in top.AlltextAreaArr){\r\n");
                   buffer.append("   xmlText +"+amount+"\"<ROW tagname"+amount+"'\"+i+\"'>\"+top.AlltextAreaArr[i]+\"</ROW>\";\r\n");
                   buffer.append("}\r\n");
                   buffer.append(" xmlText +"+amount+"\"</EDITS>\";\r\n");
                   buffer.append("       var sActionURL "+amount+"\"/jwzh/WsPrint/PubWS.do?method"+amount+"getEditXML&templateid"+amount+""+templateid+"&sjbh"+amount+sjbh+"&ywlx"+amount+ywlx+"\";\r\n");
                   buffer.append("       top.opener.doGetWsEdit(sActionURL,xmlText);\r\n");
                   buffer.append("return ;\r\n");
                   //buffer.append("return \"保存退出?\";\r\n");
                   }
                   buffer.append("}\r\n");
                   *//*****************************
                    * add by songxb @2007-10-25
                    * 多sheet页 mht
                    * begin
                    *****************************//*
                   s="";
                   buffer.append("function parentFrameunload(){\r\n");
                   if("1".equals(isEdit)){
                   buffer.append("try\r\n");
                   buffer.append("{\r\n");
                   buffer.append("for(var i "+amount+"0; i<frames.length; i++){\r\n");
                   buffer.append(" if(frames[i].document.body.onbeforeunload)\r\n");
                   buffer.append(" {\r\n");
                   buffer.append("  var func "+amount+" frames[i].document.body.onbeforeunload;\r\n");
                   buffer.append("  func();\r\n");
                   buffer.append(" }\r\n");
                   buffer.append("}\r\n");
                   buffer.append("}\r\n");
                   buffer.append("catch(e)\r\n");
                   buffer.append("{\r\n");
                   buffer.append(" alert(e.description);\r\n");
                   buffer.append("}\r\n");
                   buffer.append("  var xmlText "+amount+" \"<EDITS>\";\r\n");
                   buffer.append("for(var i in top.top.AlltextAreaArr){\r\n");
                   buffer.append("   xmlText +"+amount+"\"<ROW tagname"+amount+"'\"+i+\"'>\"+top.top.AlltextAreaArr[i]+\"</ROW>\";\r\n");
                   buffer.append("}\r\n");
                   buffer.append(" xmlText +"+amount+"\"</EDITS>\";\r\n");
                   buffer.append("       var sActionURL "+amount+"\"/jwzh/WsPrint/PubWS.do?method"+amount+"getEditXML&templateid"+amount+""+templateid+"&sjbh"+amount+sjbh+"&ywlx"+amount+ywlx+"\";\r\n");
                   buffer.append("       top.top.opener.doGetWsEdit(sActionURL,xmlText);\r\n");
                   //buffer.append("return \"保存退出?\";\r\n");
                   buffer.append("return ;\r\n");
                   }
                   buffer.append("}\r\n");

                   *//*****************************
                    * add by songxb @2007-10-25
                    * 多sheet页 mht
                    * end
                    *****************************//*
//                   buffer.append("</script>\r\n");

               }else if(s!=null&&s.indexOf("var c_lTabs")>0){
            	   
            	   //added by andy 20080630 根据业务记录数，计算页数
            	   int pageSize_bak = pageSize;
            	   String items = request.getParameter("items");
            	   if(items == null) items = "";
            	   String perpagesize = "";
            	   String sql_ws_template_variable = "select VARIABLE_FROM_VALUE from ws_template_variable where WS_TEMPLATE_ID='"+templateid+"' and VARIABLE_NAME='perpagesize'";
            	   try
            	   {
            	      QuerySet qs = DBUtil.executeQuery(sql_ws_template_variable, null);
            	      if(qs != null && qs.getRowCount()>0)
            		      perpagesize = qs.getString(1, 1);
            	      if(!"".equals(items) && !"".equals(perpagesize))
            	      {
            	          pageSize = Integer.parseInt(items)/Integer.parseInt(perpagesize)+(Integer.parseInt(items)%Integer.parseInt(perpagesize)==0?0:1);
            	      }    
            	      
            	   }catch(Exception e)
            	   {
            		   pageSize = pageSize_bak;
            	   }
            	   //end
            	   
                   if(pageSize>=1){
                       s = " var c_lTabs=3D" + (pageSize + totalP) + ";";
                   }else if(totalP > 0){
                       s = " var c_lTabs=3D" + (1 + totalP) + ";";
                   }
               }


               if(s!=null&&(s.indexOf("us-ascii")>0||s.indexOf("US-ASCII")>0)){
                try
                {
                    s = s.substring(0, s.indexOf("us-ascii")) + "UTF-8" +
                        s.substring(s.indexOf("us-ascii") + 8, s.length());
                }
                catch (Exception ex1)
                {
                    ex1.printStackTrace();
                }
               }


            }
            if(hasAzt){
                buffer.append("<script language"+amount+"JavaScript>\r\n");
                buffer.append("	onShow();\r\n");
                buffer.append("</script>\r\n");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }finally{
            reader.close();
            byteInputStream.close();
        }

        return buffer;

    }*/
    
    public StringBuffer printViewWS(HttpServletRequest request,HttpServletResponse response,String templateid)
            throws Exception
        {
        	User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
        	StringBuffer buffer = new StringBuffer();
            BlobUtil blobUtil = new BlobUtil();
            //获得文书模版blob
            String ws_template_sql = "select WS_TEMPLATE from WS_TEMPLATE where WS_TEMPLATE_ID ='"+templateid+"'";
            byte[] templateBlob = blobUtil.get("blob",ws_template_sql);//文书模版blob

            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(templateBlob);
            BufferedReader reader = new BufferedReader(new InputStreamReader(byteInputStream,"GBK"));
            String s = null;
            String amount ="=";

            response.setContentType("message/rfc822");

            
            boolean hasAzt = false;
            try
            {
                s = reader.readLine();
                while (s != null)
                {
                    buffer.append(s + "\r\n");
                    s = reader.readLine();
                    if (s!=null&&s.indexOf("<@") > 0 && s.indexOf("@>") > 0)//解析普通域
                    {

                        s = s.substring(0,s.indexOf("<@"))+""+s.substring(s.indexOf("@>")+2,s.length());
                    }
                }
            
                    buffer.append("<script language=JavaScript>\r\n");
                    buffer.append("function printIframe()\r\n");
                    buffer.append("{\r\n");
                    buffer.append("  window.print(); \r\n");
                    buffer.append("} \r\n");
                    buffer.append("</script>\r\n");
                
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }finally{
                reader.close();
                byteInputStream.close();
            }
            return buffer;
        }
    /**
     * 处理html模版文件打印
     * @param request
     * @param response
     * @param doc
     * @param templateBlob
     * @throws java.lang.Exception
     */
    public StringBuffer printHTML(String templateType,HttpServletRequest request,HttpServletResponse response,
    		String templateid,String sjbh,String ywlx,String isEdit,String isSp,String spzt)
        throws Exception
    {
    	User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    	StringBuffer buffer = new StringBuffer();
        BlobUtil blobUtil = new BlobUtil();
        //获得文书模版blob
        String ws_template_sql = "select WS_TEMPLATE from WS_TEMPLATE where WS_TEMPLATE_ID ='"+templateid+"'";
        byte[] templateBlob = blobUtil.get("blob",ws_template_sql);//文书模版blob
        String roles = user.getRoleListIdString();
        //String sql = "select d.ferffdname,d.ferresult,e.ffdbopinion,to_char(t.flehdate,'YYYY-MM-DD HH24:MI:SS'),t.flehempid from TAC_FLOWEXECUTE t,TAC_FLOWEXECUTERESULT d,TAC_FLOWFIELDS e where t.fleid = d.ferfleid and d.ferffdname = e.ffdname and t.fleflaid ="+flaid+" order by d.ferffdname";
        
        ArrayList al = getWsResult(templateid,sjbh,ywlx);//DBUtil.executeQuery(sql,null);
        
        
        String fieldname = null;
        String domain  = null;
        String canEdit = null;
        String domain_style = null;
        String spTextAreaScript = null;
        String mutilRow = null;
        String hdate = null;
        String empid = null;
        String empname = null;
        String domaintype = null;
        String approverole = null;
        String codepage = null;
        String qzrq = null;
        String approvelevel = null;
        String spylb = null;
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(templateBlob);
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteInputStream,"GBK"));
        String s = null;
        String amount ="=";

        response.setContentType("message/rfc822");

        String noScrollStyle = " onpropertychange=\"this.style.height=this.scrollHeight+'px';\"  oninput=\"this.style.height=this.scrollHeight+'px';\" ";
        boolean hasAzt = false;
        try
        {
            s = reader.readLine();
            while (s != null)
            {
                buffer.append(s + "\r\n");
                s = reader.readLine();
                if (s!=null&&s.indexOf("<@") > 0 && s.indexOf("@>") > 0)//解析普通域
                {
                    String tagName = s.substring(s.indexOf("<@")+2,s.indexOf("@>"));
                    String tdText = "";
                    for (int i = 0; i < al.size(); i++)
                    {
                    	WSFieldResult wr = (WSFieldResult)al.get(i);
                    	fieldname = wr.getName();
                        domain_style = wr.getDomainstyle();
                        String str = "";
                        domain = wr.getType();
                        str = wr.getValue();
                        hdate = wr.getDate();
      					empid = wr.getEmpid();
      					empname = wr.getEmpname();
      					domaintype = wr.getDomaintype();
      					approverole = wr.getApproverole();
      					codepage = wr.getCodepage();
      					//add by cbl start
      					canEdit = wr.getCanedit();
      					qzrq = wr.getQzrq();
      					approvelevel = wr.getApprovelevel();
      					spylb = wr.getSpylb();
      					//add by cbl end
      					//if(approverole==null)/
      					//	approverole = "";
//                    	if(tagName.equals("FlaADptName"))
//                    	{
//                    		String str1 = "select d.DPTNAME from TAB_DEPARTMENT d,TAC_FLOWAPPLY t where d.DPTID = t.flaadptid and t.flaid = "+flaid;
//                    		String b[][] = DBUtil.query(str1);
//                    		tdText += b[0][0];
//                    		break;
//                    	}
                    	if(Pub.empty(domain_style))
                    	{
                    		domain_style = "width:100%;height:100%;";
                    	}
                        if(tagName.equalsIgnoreCase(fieldname)){
                        	if(PubWS.FIELDEXTEN_WSWH.equals(domain))
                        	{
                        		String[] ss = str.split("\n");
                                String textAreaText = "";
                                try
                                {
                                    for(int l=0;l<ss.length;l++){
                                        if (!Pub.empty(domain_style))
                                        {
                                            textAreaText +="<span style" +amount + "'" +domain_style + "'>" +ss[l] +"</span>";
                                        }
                                        else
                                        {
                                            textAreaText += "" +ss[l]  + "";
                                        }
                                    }
                                    tdText = textAreaText;
                                    break;
                                }
                                catch (Exception ex2)
                                {
                                    ex2.printStackTrace();
                                }
                                finally{
                                }
                        		
                        	}else if(PubWS.FIELDEXTEN_CONTENT.equals(domain)){

                        //           if("1".equals(isEdit)&&approverole!=null&&roles.contains(approverole)){
                        		   if("1".equals(isEdit)){
                                        if(str!=null&&!"".equals(str)){
                                            if(str.indexOf("<br>")>0){
                                                str = str.replaceAll("<br>","\r\n");
                                            }
                                            if(str.indexOf("<br/>")>0){
                                                str = str.replaceAll("<br/>","\r\n");
                                            }
                                        }
                                        //alter by cbl add canEdit需要从文书控制是否可以编辑
                                        if(!"1".equals(canEdit)){
                                        	String isrefresh = request.getParameter("isrefresh");
                                        	if("1".equals(isrefresh)){
                                        		PubWS p = new PubWS();
                                        		String temp = p.changeFieldText(templateid, fieldname, sjbh, ywlx, request);
                                        		if(temp!=null){
                                        			str = temp;
                                        		}
                                        	}
                                        	tdText = str;
                                        }//add by cbl end canEdit
                                        else{
                                            if("date".equals(domaintype))
                                            {
                                            	tdText = "<input type=\"date\" id=\""+fieldname+"\" fieldname=\""+fieldname+"\" style=\""+domain_style+"\" value=\""+str+"\">";
                                            	
                                            }else if("datetime".equals(domaintype)){
                                            	//add by cbl start 增加dateTime类型
                                            	tdText = "<input type=\"text\"  id=\""+fieldname+"\" fieldname=\""+fieldname+"\" style=\""+domain_style+"\" value=\""+str+"\">";
                                            	//add by cbl end 增加dateTime
                                            }else if("text".equals(domaintype))
                                            {
                                            	tdText = "<input type=\"text\" id=\""+fieldname+"\" fieldname=\""+fieldname+"\" style=\""+domain_style+"\" value=\""+str+"\">";
                                            }else if("select".equals(domaintype))
                                            {
                                            	tdText = "<select class=\"span12\" id=\""+fieldname+"\" fieldname=\""+fieldname+"\" style=\"width:90%\">\r\n";
                                            	tdText +="<option value=''>请选择</option>\r\n";
                                            	if(!Pub.empty(codepage))
                                            	{
                                            		if("T#".equals(codepage.substring(0,2).toUpperCase())){
                                            			String vparam = codepage.substring(2);//去掉T#
                                            			String strTabname = null;
                                            			String strCodeCol = null;
                                            			String strValueCol = null;
                                            			String swhere = null;
                                            			String defaultValue = "";
                                            			String[] temps = vparam.split(":");
                                            			if (temps == null)
                                            				throw new ServletException("错误的参数设置，无法获取字典内容！");
                                            			strTabname = temps[0];
                                            			strCodeCol = temps[1];
                                            			strValueCol = temps[2];
                                            			if (temps.length >= 4)
                                            				swhere = temps[3];
                                            			if (temps.length >= 5)
                                            				defaultValue = temps[4];
                                            			String sql = null;
                                            			if (strTabname != "" && strCodeCol != "" && strValueCol != "") {
                                            				sql = "select " + strCodeCol + "," + strValueCol +
                                            					" from "+ strTabname;
                                            			} else
                                            				throw new ServletException("错误的参数设置，无法获取字典内容！");
                                            			if (!Pub.empty(swhere))
                                            				sql += " where " + swhere;
                                            		String[][] ss =	DBUtil.query(sql);
                                            		if(ss!=null&&ss.length>0)
                                            		{
                                            			for(int g = 0;g<ss.length;g++)
                                            			{
                                            				if(Pub.empty(str)){
                                            				  if(ss[g][0].equalsIgnoreCase(defaultValue)){
                                            					tdText +="<option value='"+ss[g][1]+"' selected=\"selected\">"+ss[g][1]+"</option>";
                                            				  }else{
                                            					tdText +="<option value='"+ss[g][1]+"'>"+ss[g][1]+"</option>";
                                            				  }
                                            				}else{
                                            					if(ss[g][0].equalsIgnoreCase(str)){
                                                					tdText +="<option value='"+ss[g][1]+"' selected=\"selected\">"+ss[g][1]+"</option>";
                                                				  }else{
                                                					tdText +="<option value='"+ss[g][1]+"'>"+ss[g][1]+"</option>";
                                                				  }
                                            				}
                                            			}
                                            		}
                                            			
                                            		}else if("E#".equals(codepage.substring(0,2).toUpperCase())){
                                            			
                                            		}else{
                                            			String defaultValue = "";
                                            			if(codepage!=null&&codepage.indexOf(":")>-1){
                                            				String[] temps = codepage.split(":");
                                            				codepage = temps[0];
                                            				defaultValue = temps[1];
                                            			}
                                            			
                                            			TreeNode tn = Dics.getDicByName(codepage);
                                            			if(tn!=null)
                                            			{
                                            				TreeNode[] t = tn.getChilds();
                                            				for(int ss =0;ss<t.length;ss++){
                                            				  if(Pub.empty(str)){
                                            					if(t[ss].getDicCode().equalsIgnoreCase(defaultValue)){
                                            						tdText +="<option value='"+t[ss].getDicValue()+"' selected=\"selected\">"+t[ss].getDicValue()+"</option>";
                                            					}else{
                                            						tdText +="<option value='"+t[ss].getDicValue()+"'>"+t[ss].getDicValue()+"</option>";
                                            					}
                                            				  }else{
                                            					  // 当字典是系统字典时，字典code应该转换成文本
                                            					if(Pub.getDictValueByCode(codepage, t[ss].getDicCode()).equalsIgnoreCase(str)){
                                              						tdText +="<option value='"+t[ss].getDicValue()+"' selected=\"selected\">"+t[ss].getDicValue()+"</option>";
                                              					}else{
                                              						tdText +="<option value='"+t[ss].getDicValue()+"'>"+t[ss].getDicValue()+"</option>";
                                              					}
                                              				}
                                            				}
                                            			}
                                            		}
                                            	}
                                            	tdText +="</select>\r\n";
                                            }
                                            else
                                            {

                                            	tdText = "<textArea id=\""+fieldname+"\" fieldname=\""+fieldname+"\" style=\""+domain_style+"\" "+noScrollStyle+">"+str+"</textArea>";
                                            	
                                            }
                                        }
                                        break;
                                    }else{
                                        
                                    	String isrefresh = request.getParameter("isrefresh");
                                    	if("1".equals(isrefresh)){
                                    		PubWS p = new PubWS();
                                    		String temp = p.changeFieldText(templateid, fieldname, sjbh, ywlx, request);
                                    		if(temp!=null){
                                    			str = temp;
                                    		}
                                    	}
                                    	
                                    	String[] ss = str.split("\n");
                                        String textAreaText = "";
                                        try
                                        {
                                        	/**
                                        	 * 工程甩项申请会签单系列的会签单样式决定的。此会签单的【会签意见】域是类别是普通域（3）
                                        	 * 原来的普通域默认是break。但是当遇到【甩项申请会签】时不必break，把所有意见都显示出来
                                        	 * add by xiahongbo on 2015-01-08 start
                                        	 */
                                            if("27".equals(templateid) || "26".equals(templateid) || "20".equals(templateid) || "22".equals(templateid)) {
                                            	int tmpNum = ss.length;
                                                for(int l=0;l<tmpNum;l++){
                                                    if (!Pub.empty(domain_style))
                                                    {
                                                    	//对换行进行处理，否则编辑好的格式不生效 add by hongpeng.dong at 2014.2.12
                                                    	if(0 != l && l != tmpNum-1){
                                                    		textAreaText += "<br/>";
                                                    	}
                                                    	
                                                        textAreaText +="<span style" +amount + "'" +domain_style + "'>" +ss[l] +"</span><br>";
                                                    }
                                                    else
                                                    {
                                                        textAreaText += "" +ss[l]  + "<br>";
                                                    }
                                                }
                                                tdText += textAreaText;
                                            } else {
                                            	int tmpNum = ss.length;
                                                for(int l=0;l<tmpNum;l++){
                                                    if (!Pub.empty(domain_style))
                                                    {
                                                    	//对换行进行处理，否则编辑好的格式不生效 add by hongpeng.dong at 2014.2.12
                                                    	if(0 != l && l != tmpNum-1){
                                                    		textAreaText += "<br/>";
                                                    	}
                                                    	
                                                        textAreaText +="<span style" +amount + "'" +domain_style + "'>" +ss[l] +"</span>";
                                                    }
                                                    else
                                                    {
                                                        textAreaText += "" +ss[l]  + "";
                                                    }
                                                }
                                                tdText = textAreaText;
                                                break;
                                            }
                                            /** add by xiahongbo on 2015-01-08 end */
                                        }
                                        catch (Exception ex2)
                                        {
                                            ex2.printStackTrace();
                                        }
                                        finally{
                                        }
                                    }

                            }else if(PubWS.FIELDEXTEN_MIND.equals(domain)){
                            		
                                String[] ss = str.split("\n");
                                String textAreaText = "";
                                
                                boolean ishb = true;
                                if("1".equals(spylb)){
                                	ishb = false;
                                }
                              /*  if(!Pub.empty(approvelevel))
                                {
                                	for(int k = 0;k<al.size();k++)
                                	{
                                		WSFieldResult wr_f = (WSFieldResult)al.get(k);
                                		String approvelevel_f = wr_f.getApprovelevel();
                                		String domain_f =  wr_f.getType();
                                		if(!Pub.empty(approvelevel_f)){
                                			if(approvelevel.equals(approvelevel_f)&&"10".equals(domain_f)){
                                				ishb = false;
                                			}
                                		}
                                	}
                                }*/
                                
                                try
                                {
                                	if (str!=null&&str.length()>0) {
                                		
                                	 if(ishb==true){
                                		
										for (int l = 0; l < ss.length; l++) {
											if (!Pub.empty(domain_style)) {
												textAreaText += "<span style"
														+ amount + "'"
														+ domain_style + "'>"
														+ ss[l] + "</span><br>";
											} else {
												textAreaText += "" + ss[l]
														+ "<br>";
											}
										}
										// &belongs="+empid+"&accDir=PersonSign
										textAreaText += "<div style='width:100%;text-align:left'><font face=宋体 color=Blue>";
										String filename[] = null;
										if (empid != null && empid.length() > 0) {
											filename = getNewQMbyEmpid(empid);
										}

										if (filename != null) {
											String url = request.getContextPath()+"/fileUploadController.do?getFile&account="+empid+"&ywlx="+(YwlxManager.SYSTEM_USER_QM);
											textAreaText += "<img src='" + url + "'>";
										} else {
											textAreaText += empname;
										}

										textAreaText += "</font>&nbsp;&nbsp;<font color=Blue>"
												+ hdate + "</font></div><br>";
										tdText += textAreaText;
                                	  }else{
                                		  
                                		  tdText += str;
  									  }
                                	  
									}else if("1".equals(isSp)&&approverole!=null&&roles.contains(approverole))
                                	{
										if("000002".equals(ywlx) || "000003".equals(ywlx)) {  // 业务类型为000002时，是工作联系单（自由报送）。文书审批域控制。签发部门负责人的审批域
											// 当前操作人的部门
											String currentUserDept = user.getDepartment();
											// 查询创建人的部门
											String queryFqrSql = "select CJDWDM from AP_PROCESSINFO t where eventid = '" + sjbh + "'";
											String[][] rs = DBUtil.query(queryFqrSql);
											String fqrDept = rs[0][0];
											if (!currentUserDept.equals(fqrDept)) {  // 操作人和创建人部门不同，不显示部门负责人审批域
												if (!"BMFZRYJ".equals(fieldname)) {
													tdText += "<textArea id"+amount+""+fieldname+" fieldname"+amount+""+fieldname+" style"+amount+"\""+domain_style+"\" "+noScrollStyle+">"+str+"</textArea>";
												}
											} else {  // 操作人和创建人部门相同
												if ("BMFZRYJ".equals(fieldname)) {
													tdText += "<textArea id"+amount+""+fieldname+" fieldname"+amount+""+fieldname+" style"+amount+"\""+domain_style+"\" "+noScrollStyle+">"+str+"</textArea>";
												} else if("JSBMFKNR".equals(fieldname)) {
													tdText += "<textArea id"+amount+""+fieldname+" fieldname"+amount+""+fieldname+" style"+amount+"\""+domain_style+"\" "+noScrollStyle+">"+str+"</textArea>";
												}
											}
										} else {
											tdText += "<textArea id"+amount+""+fieldname+" fieldname"+amount+""+fieldname+" style"+amount+"\""+domain_style+"\" "+noScrollStyle+">"+str+"</textArea>";;
										}
										
                                	}
										
                                    //break;
                                }
                                catch (Exception ex2)
                                {
                                    ex2.printStackTrace();
                                }
                                finally{
                                }
                            }else if(PubWS.FIELDEXTEN_WRITE.equals(domain)){//签字域
                            		

                                String textAreaText = "";
                                
                                try
                                {
									if (!Pub.empty(str)) {
										if("33".equals(templateid)) {
											if(tdText.indexOf(empname) == -1) {
												textAreaText += empname;
												tdText += textAreaText;
											}
										} else {
											textAreaText += "<font face=宋体 color=Blue>";
											String filename[] = null;
											if (empid != null && empid.length() > 0) {
												filename = getNewQMbyEmpid(empid);
											}
											if (filename != null) {
												String url = request
														.getContextPath()
														+ "/fileUploadController.do?getFile&account="
														+ empid
														+ "&ywlx="
														+ (YwlxManager.SYSTEM_USER_QM);
												textAreaText += "<img src='" + url
														+ "'>";
											} else {
												textAreaText += empname;
											}

											textAreaText += "</font>";
											tdText += textAreaText;
										}
										
									}
										
                                    //break;
                                }
                                catch (Exception ex2)
                                {
                                    ex2.printStackTrace();
                                }
                                finally{
                                }
                            }else if(PubWS.FIELDEXTEN_DATE.equals(domain)){//审批时间
                            		
                                
                                try
                                {
									if (!Pub.empty(str)&&!Pub.empty(qzrq)) {
										
										tdText += "<font face=宋体 color=Blue>"+hdate+"</font><br>";
									}
										
                                    //break;
                                }
                                catch (Exception ex2)
                                {
                                    ex2.printStackTrace();
                                }
                                finally{
                                }
                            }else if(PubWS.FIELDEXTEN_12.equals(domain)){//工作联系单
                            		
                                String[] ss = str.split("\n");
                                String textAreaText = "";
                            	if (str!=null&&str.length()>0) {
                            		for (int l = 0; l < ss.length; l++) {
    									if (!Pub.empty(domain_style)) {
    										textAreaText += "<span style"
    												+ amount + "'"
    												+ domain_style + "'>"
    												+ ss[l] + "</span><br>";
    									} else {
    										textAreaText += "" + ss[l] + "<br>";
    									}
    								}
    								textAreaText += "<div style='width:100%;text-align:left'><font face=宋体 color=Blue>";
    								String filename[] = null;
    								if (empid != null && empid.length() > 0) {
    									filename = getNewQMbyEmpid(empid);
    								}

    								if (filename != null) {
    									String url = request.getContextPath()+"/fileUploadController.do?getFile&account="+empid+"&ywlx="+(YwlxManager.SYSTEM_USER_QM);
    									textAreaText += "<img src='" + url + "'>";
    								} else {
    									textAreaText += empname;
    								}

    								textAreaText += "</font>&nbsp;&nbsp;<font color=Blue>" + hdate + "</font></div><br>";
    								tdText += textAreaText;
                            	} else if("1".equals(isEdit)&&approverole!=null&&user.getAccount().equals(approverole)) {
                            		tdText += "<textArea id"+amount+""+fieldname+" fieldname"+amount+""+fieldname+" style"+amount+"\""+domain_style+"\" "+noScrollStyle+">"+str+"</textArea>";;
								}
                            	
								
                            }
                        }
                    }
                    s = s.substring(0,s.indexOf("<@"))+tdText+s.substring(s.indexOf("@>")+2,s.length());
                }
            }
            
            
                buffer.append("<script language=JavaScript>\r\n");
                buffer.append("function printIframe()\r\n");
                buffer.append("{\r\n");
                buffer.append("var list = document.getElementsByTagName('textArea');");
                buffer.append("for(i=0;i<list.length;i++){");
                buffer.append("document.getElementById(list[i].id).style.overflowY='hidden';");
                buffer.append("document.getElementById(list[i].id).style.border='0';");
                buffer.append("}");
                buffer.append("\r\n");
                buffer.append("  window.print(); \r\n");
                buffer.append("} \r\n");
                buffer.append("</script>\r\n");
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }finally{
            reader.close();
            byteInputStream.close();
        }
        return buffer;
    }
    public WSFieldResult getWsResultByfieldname(String ws_templateid, String sjbh, String ywlx,String fieldname)
    {
    	String tempsql1  = "";
    	String tempsql2  = "";
		WSFieldResult wr = new WSFieldResult();

    	try{
    		int templateid = Integer.parseInt(ws_templateid);
    		String sql = "select FIELDNAME,DOMAIN_VALUE,WSWH_FLAG,to_char(LRSJ,'YYYY-MM-DD HH24:MI:SS') as hdate,LRRID,LRRXM,DOMAIN_TYPE,CODEPAGE,APPROVEROLE,DOMAIN_STYLE,canedit from AP_PROCESS_WS where WS_TEMPLATE_ID = '"+ws_templateid+"' and SJBH = '"+sjbh+"' and ywlx = '"+ywlx+"' and FIELDNAME='"+fieldname+"' order by LRSJ";
    		QuerySet qs = DBUtil.executeQuery(sql, null);
    		for(int i=0;i<qs.getRowCount();i++)
    		{
    			wr.setName(qs.getString(i+1, 1));
    			String rvalue = getValueFromQS(qs,i+1,2);
    			if(rvalue == null)
    				rvalue = "";
    			wr.setValue(rvalue);
    			wr.setType(qs.getString(i+1, 3));
    			wr.setDate(qs.getString(i+1, 4));
    			wr.setEmpid(qs.getString(i+1, 5));
    			wr.setEmpname(qs.getString(i+1, 6));
    			wr.setDomaintype(qs.getString(i+1, 7));
    			wr.setCodepage(qs.getString(i+1, 8));
    			wr.setApproverole(qs.getString(i+1, 9));
    			wr.setDomainstyle(qs.getString(i+1, 10));
    			wr.setCanedit(qs.getString(i+1, 11));
    		}
    		
    	}catch(Exception e)
    	{
    		
    	}finally
    	{
    		
    	}
    	
    	return wr;
    }
    

    private ArrayList getWsResult(String ws_templateid, String sjbh, String ywlx)
    {
    	ArrayList al = new ArrayList();
    	String tempsql1  = "";
    	String tempsql2  = "";
    	try{
    		int templateid = Integer.parseInt(ws_templateid);
    		String sql = "select FIELDNAME,DOMAIN_VALUE,WSWH_FLAG,to_char(LRSJ,'YYYY-MM-DD HH24:MI:SS') as hdate,LRRID,LRRXM,DOMAIN_TYPE,CODEPAGE,APPROVEROLE,DOMAIN_STYLE,canedit,to_char(LRSJ,'yyyy\"年\"mm\"月\"dd\"日\"') as qzdate,APPROVELEVEL,SPYLB from AP_PROCESS_WS where WS_TEMPLATE_ID = '"+ws_templateid+"' and SJBH = '"+sjbh+"' and ywlx = '"+ywlx+"' order by LRSJ";
    		QuerySet qs = DBUtil.executeQuery(sql, null);
    		for(int i=0;i<qs.getRowCount();i++)
    		{
    			WSFieldResult wr = new WSFieldResult();
    			wr.setName(qs.getString(i+1, 1));
    			String rvalue = getValueFromQS(qs,i+1,2);
    			if(rvalue == null)
    				rvalue = "";
    			wr.setValue(rvalue);
    			wr.setType(qs.getString(i+1, 3));
    			wr.setDate(qs.getString(i+1, 4));
    			wr.setEmpid(qs.getString(i+1, 5));
    			wr.setEmpname(qs.getString(i+1, 6));
    			wr.setDomaintype(qs.getString(i+1, 7));
    			wr.setCodepage(qs.getString(i+1, 8));
    			wr.setApproverole(qs.getString(i+1, 9));
    			wr.setDomainstyle(qs.getString(i+1, 10));
    			//add by cbl add canedit
    			wr.setCanedit(qs.getString(i+1, 11));
    			wr.setQzrq(qs.getString(i+1, 12));
    			wr.setApprovelevel(qs.getString(i+1, 13));
    			wr.setSpylb(qs.getString(i+1, 14));

         //   	System.out.println(wr.getType()+"|"+wr.getName()+"||"+rvalue);
    			//add by cbl end
    			al.add(wr);
    		}

    		switch(templateid)
    		{
    			case 380:
    				break;
    			default:
    				break;
    		}
    		
    	}catch(Exception e)
    	{
    		
    	}finally
    	{
    		
    	}
    	
    	return al;
    }
    
    /**
     * 处理html模版文件打印
     * @param request
     * @param response
     * @param doc
     * @param templateBlob
     * @throws java.lang.Exception
     */
    public StringBuffer printOldHTML(HttpServletRequest request,HttpServletResponse response,String flaid, String flaflwid, String flwno, String isEdit)
        throws Exception
    {
    	StringBuffer buffer = new StringBuffer();
        BlobUtil blobUtil = new BlobUtil();
        //获得文书模版blob
        String ws_template_sql = "select fblhtmldisplay from TAC_FLOWBILL where fblno ='"+flwno+"'";
        byte[] templateBlob = blobUtil.get("blob",ws_template_sql);//文书模版blob
        
        
        //String sql = "select d.ferffdname,d.ferresult,e.ffdbopinion,to_char(t.flehdate,'YYYY-MM-DD HH24:MI:SS'),t.flehempid from TAC_FLOWEXECUTE t,TAC_FLOWEXECUTERESULT d,TAC_FLOWFIELDS e where t.fleid = d.ferfleid and d.ferffdname = e.ffdname and t.fleflaid ="+flaid+" order by d.ferffdname";
        
        ArrayList al = getOldWsResult(flaid,flaflwid,flwno);//DBUtil.executeQuery(sql,null);
        
        
        String fieldname = null;
        String domain  = null;
        String canEdit = null;
        String domain_style = null;
        String spTextAreaScript = null;
        String mutilRow = null;
        String hdate = null;
        String empid = null;
        String empname = null;
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(templateBlob);
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteInputStream,"GBK"));
        String s = null;
        String amount ="";

        response.setContentType("message/rfc822");

        User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
        boolean hasAzt = false;
        try
        {
            s = reader.readLine();
            while (s != null)
            {
                buffer.append(s + "\r\n");
                s = reader.readLine();
                if (s!=null&&s.indexOf("<@") > 0 && s.indexOf("@>") > 0)//解析普通域
                {
                    String tagName = s.substring(s.indexOf("<@")+2,s.indexOf("@>"));
                    String tdText = "";
                    for (int i = 0; i < al.size(); i++)
                    {
                    	WSFieldResult wr = (WSFieldResult)al.get(i);
                    	fieldname = wr.getName();
                        domain_style = "";
                        String str = "";
                        domain = wr.getType();
                        str = wr.getValue();
                        hdate = wr.getDate();
      					empid = wr.getEmpid();
      					empname = wr.getEmpname();
//                    	if(tagName.equals("FlaADptName"))
//                    	{
//                    		String str1 = "select d.DPTNAME from TAB_DEPARTMENT d,TAC_FLOWAPPLY t where d.DPTID = t.flaadptid and t.flaid = "+flaid;
//                    		String b[][] = DBUtil.query(str1);
//                    		tdText += b[0][0];
//                    		break;
//                    	}
                        if(tagName.equalsIgnoreCase(fieldname)){
          					
                            if("0".equals(domain)){

                                    if("1".equals(isEdit)){
  
                                        if(str!=null&&!"".equals(str)){
                                            if(str.indexOf("<br>")>0){
                                                str = str.replaceAll("<br>","\r\n");
                                            }
                                            if(str.indexOf("<br/>")>0){
                                                str = str.replaceAll("<br/>","\r\n");
                                            }
                                        }
                                        String textAreaText = "<textArea id"+amount+""+fieldname+" fieldname"+amount+""+fieldname+" style"+amount+"\"width:100%;height:100%\" rows"+amount+"8>"+str+"</textArea>";
                                        tdText = textAreaText;
                                        break;
                                    }else{

                                        String[] ss = str.split("\n");
                                        String textAreaText = "";
                                        try
                                        {
                                            for(int l=0;l<ss.length;l++){
                                                if (!Pub.empty(domain_style))
                                                {
                                                    textAreaText +="<span style" +amount + "'" +domain_style + "'>" +ss[l] +"</span><br>";
                                                }
                                                else
                                                {
                                                    textAreaText += "" +ss[l]  + "";
                                                }
                                            }
                                            tdText = textAreaText;
                                            break;
                                        }
                                        catch (Exception ex2)
                                        {
                                            ex2.printStackTrace();
                                        }
                                        finally{
                                        }
                                    }

                            }else if("1".equals(domain)){
                            		
                                String[] ss = str.split("\n");
                                String textAreaText = "";
                                try
                                {
                                    for(int l=0;l<ss.length;l++){
                                        if (!Pub.empty(domain_style))
                                        {
                                            textAreaText +="<span style" +amount + "'" +domain_style + "'>" +ss[l] +"</span><br>";
                                        }
                                        else
                                        {
                                            textAreaText += "" +ss[l]  + "<br>";
                                        }
                                    }
                                    //&belongs="+empid+"&accDir=PersonSign
                                    textAreaText += "<div style='width:100%;text-align:left'><font face=宋体 color=Blue>";
                                    String filename[] = null;
                                    if(empid!=null&&empid.length()>0)
                                    {
                                    	filename = getQMbyEmpid(empname,empid);
                                    }
                                    
                                    if(filename!=null)
                                    {
                                    	String url = request.getContextPath()+"/PubWS.do?getPersonSignAction&filename="+filename[0]+"&filenametype="+filename[1];
                                    	textAreaText += "<img src='"+url+"'>";
                                    }else
                                    {
                                    	textAreaText += empname;
                                    }
                                    		
                                    textAreaText += "</font>&nbsp;&nbsp;<font color=Blue>"+hdate+"</font></div><br>";
                                    tdText += textAreaText;
                                    //break;
                                }
                                catch (Exception ex2)
                                {
                                    ex2.printStackTrace();
                                }
                                finally{
                                }
                            }
                        }
                    }
                    s = s.substring(0,s.indexOf("<@"))+tdText+s.substring(s.indexOf("@>")+2,s.length());
                }
            }
            
            
                buffer.append("<script language=JavaScript>\r\n");
                buffer.append("function printIframe()\r\n");
                buffer.append("{\r\n");
                buffer.append("  window.print(); \r\n");
                buffer.append("} \r\n");
                buffer.append("</script>\r\n");
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }finally{
            reader.close();
            byteInputStream.close();
        }
        return buffer;
    }
    
	public String getValueFromQS(QuerySet qs,int rowIndex,int colIndex) throws Exception {
		
		int colType = java.sql.Types.VARCHAR;
		String va = "";
		if (qs.getRowCount() != 0) {
				int columnType = qs.getType(colIndex);
				if (columnType == java.sql.Types.BLOB&& colType != java.sql.Types.BLOB) {
					colType = java.sql.Types.CLOB;
				}
				if (colType == java.sql.Types.VARCHAR) {

						va = qs.getString(rowIndex, colIndex);

				} else if (colType == java.sql.Types.BLOB) {
					Blob dbBlob;
					dbBlob = (Blob) qs.getObject(1, 1);
					if (dbBlob != null) {
						int length = (int) dbBlob.length();
						byte[] buffer = dbBlob.getBytes(1, length);
						va = Pub.toBase64(buffer);
					}
				} else if (colType == java.sql.Types.CLOB) {
					Blob dbBlob;
					dbBlob = (Blob) qs.getObject(rowIndex, colIndex);
					if (dbBlob != null) {
						int length = (int) dbBlob.length();
						byte[] buffer = dbBlob.getBytes(1, length);
						va = new String(buffer, "GBK");
					}
				}
			
		}

		return va;
	}


	
	
    private ArrayList getOldWsResult(String flaid, String flaflwid, String flwno)
    {
    	ArrayList al = new ArrayList();
    	String tempsql1  = "";
    	String tempsql2  = "";
    	try{
    		int flow = Integer.parseInt(flaflwid);
    		String sql = "select d.ferffdname,d.ferresult,e.ffdbopinion,to_char(t.flehdate,'YYYY-MM-DD HH24:MI:SS') as hdate,t.flehempid,t.flehempname from TAC_FLOWEXECUTE t,TAC_FLOWEXECUTERESULT d,TAC_FLOWFIELDS e where t.fleid = d.ferfleid and d.ferffdname = e.ffdname and t.fleflaid ="+flaid+" order by hdate";
    		QuerySet qs = DBUtil.executeQuery(sql, null);
    		QuerySet qs1 = null;
    		QuerySet qs2 = null;
    		for(int i=0;i<qs.getRowCount();i++)
    		{
    			WSFieldResult wr = new WSFieldResult();
    			wr.setName(qs.getString(i+1, 1));
    			String rvalue = getValueFromQS(qs,i+1,2);
    			if(rvalue == null)
    				rvalue = "";
    			wr.setValue(rvalue);
    			wr.setType(qs.getString(i+1, 3));
    			wr.setDate(qs.getString(i+1, 4));
    			wr.setEmpid(qs.getString(i+1, 5));
    			wr.setEmpname(qs.getString(i+1, 6));
    			al.add(wr);
    		}
    		String astr[] = {"FlaNo","FlaAEmpName","FlaADptName"};
    		String commonsql = "select FlaNo,FlaAEmpName,(select dptname from TAB_DEPARTMENT where dptid = t.flaadptid ) as FlaADptName from TAC_FLOWAPPLY t where t.flaid = "+flaid;
			qs2 = DBUtil.executeQuery(commonsql, null);
			if (qs2.getRowCount() >= 0) {
				for (int i = 0; i < astr.length; i++) {
					WSFieldResult wr = new WSFieldResult();
					wr.setName(astr[i]);
					String rvalue = qs2.getString(1, astr[i]);
					if (rvalue == null)
						rvalue = "";
					wr.setValue(rvalue);
					wr.setType("0");
					wr.setDate("");
					wr.setEmpid("");
					al.add(wr);
				}
			}
    		switch(flow)
    		{
    			case 62://收文处理单
    				tempsql1 = "select t.* from TAC_FLOWFIELDS t,TAC_FLOWBILL D  where t.ffdfblid = d.fblid and d.fblno = '"+flwno+"' and  ffdfrom = '业务系统'";
    	    		tempsql2 = "select FinTitle ,FinNo,FinRecDate,FinFutName,FinFileNo,FinPress,FinFileType,FinSec from TOA_FILEIN t where t.finflaid = "+flaid;
    				qs = DBUtil.executeQuery(tempsql1, null);
    				qs1 = DBUtil.executeQuery(tempsql2, null);
    				if(qs.getRowCount() <= 0 || qs.getRowCount() <= 0 )
    					break;
    	    		for(int i=0;i<qs.getRowCount();i++)
    	    		{
    	    			WSFieldResult wr = new WSFieldResult();
    	    			wr.setName(qs.getString(i+1, "FFDNAME"));
    	    			String rename = qs.getString(i+1, "FFDENAME");
    	    			String rvalue = qs1.getString(1, rename);
    	    			if(rvalue==null)
    	    				rvalue = "";
    	    			wr.setValue(rvalue);
    	    			wr.setType("0");
    	    			wr.setDate("");
    	    			wr.setEmpid("");
    	    			al.add(wr);
    	    		}
    				
    				
    				break;
    			case 56:  //合同会签单
    				tempsql1 = "select t.* from TAC_FLOWFIELDS t,TAC_FLOWBILL D  where t.ffdfblid = d.fblid and d.fblno = '"+flwno+"' and  ffdfrom = '业务系统'";
    	    		tempsql2 = "select conid,conprjid, conccsid,ConNo,ConName,conuntid,condptname, conempname, ConUntName,ConZbNoticeNo from TPJ_CONTRACT t where t.conflaid = "+flaid;
    				qs = DBUtil.executeQuery(tempsql1, null);
    				qs1 = DBUtil.executeQuery(tempsql2, null);
    				if(qs.getRowCount() <= 0 || qs.getRowCount() <= 0 )
    					break;
    	    		for(int i=0;i<qs.getRowCount();i++)
    	    		{
    	    			WSFieldResult wr = new WSFieldResult();
    	    			wr.setName(qs.getString(i+1, "FFDNAME"));
    	    			String rename = qs.getString(i+1, "FFDENAME");
    	    			String rvalue = qs1.getString(1, rename);
    	    			if(rvalue==null)
    	    				rvalue = "";
    	    			wr.setValue(rvalue);
    	    			wr.setType("0");
    	    			wr.setDate("");
    	    			wr.setEmpid("");
    	    			al.add(wr);
    	    		}
    				
    				
    				break;	
    			case 61:  //发文稿纸（不带文号）
    				tempsql1 = "select t.* from TAC_FLOWFIELDS t,TAC_FLOWBILL D  where t.ffdfblid = d.fblid and d.fblno = '"+flwno+"' and  ffdfrom = '业务系统'";
    				tempsql2 = "select FITTITLE,FITMSEND,FITCSEND from TOA_FILEOUT t where t.FITFLAID = "+flaid;
    				qs = DBUtil.executeQuery(tempsql1, null);
    				qs1 = DBUtil.executeQuery(tempsql2, null);
    				if(qs.getRowCount() <= 0 || qs.getRowCount() <= 0 )
    					break;
    				for(int i=0;i<qs.getRowCount();i++)
    				{
    					WSFieldResult wr = new WSFieldResult();
    					wr.setName(qs.getString(i+1, "FFDNAME"));
    					String rename = qs.getString(i+1, "FFDENAME");
    					String rvalue = qs1.getString(1, rename);
    					if(rvalue==null)
    						rvalue = "";
    					wr.setValue(rvalue);
    					wr.setType("0");
    					wr.setDate("");
    					wr.setEmpid("");
    					al.add(wr);
    				}
    				
    				
    				break;	
    			default:
    				break;
    		}
    		
    	}catch(Exception e)
    	{
    		
    	}finally
    	{
    		
    	}
    	
    	return al;
    }

    public String[] getNewQMbyEmpid(String empid) throws Exception
    {
    	//String path = "E:\\PersonSign";
    	String sql = "select account from FS_ORG_PERSON_FS t where account='"+empid+"' and fslb='"+FjlbManager.FS_USER_YHQM+"' and sfyx='1' and fsid is not null";
    	String accname[][] = DBUtil.query(sql);
    	if(accname!=null)
    		return accname[0];
    	else 
    		return null;
    	//File file = new (path+accname[][]);
    }
    
    
    
    public String[] getQMbyEmpid(String empid) throws Exception
    {
    	//String path = "E:\\PersonSign";
    	String sql = "select accnname,acconame from TAC_ACCESSORY where accDir = 'PersonSign' and ACCBELONGID = '"+empid+"' order by accid desc";
    	String accname[][] = DBUtil.query(sql);
    	if(accname!=null)
    		return accname[0];
    	else 
    		return null;
    	//File file = new (path+accname[][]);
    }
    
    public String[] getQMbyEmpid(String empname,String empid) throws Exception
    {
    	//String path = "E:\\PersonSign";
    	String sql = "select accnname,acconame from TAC_ACCESSORY where accDir = 'PersonSign' and ACCBELONGID = '"+empid+"' and acconame like '%"+empname+"%' order by accid desc";
    	String accname[][] = DBUtil.query(sql);
    	if(accname!=null)
    		return accname[0];
    	else 
    		return getQMbyEmpid(empid);
    	//File file = new (path+accname[][]);
    }
    
    public String getImageJs(Document doc,String amount){
        StringBuffer buffer = new StringBuffer();
        List printRoot = doc.selectNodes("//PRINTDATA/ROW");
        String domain = null;
        String fieldname = null;


        for (int i = 0; i < printRoot.size(); i++)
        {
            Element ele = (Element) printRoot.get(i);
            if (ele.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            domain = ele.attributeValue("domain");
            fieldname = ele.attributeValue("fieldname");
            if(domain.equalsIgnoreCase(PubWS.FIELDEXTEN_IMAGE))
            {
                buffer.append("var "+fieldname+""+amount+"document.all('"+fieldname+"');\r\n");
                String base64 = ele.getText();
                if(!Pub.empty(base64)){
                    base64 = base64.replaceAll("=",amount);
                }else{
                    base64 = "";
                }
                buffer.append("var imageValue "+amount+" \""+base64+"\";\r\n");
                buffer.append(" if("+fieldname+"){\r\n");
                buffer.append("   if("+fieldname+".length>0){\r\n");
                buffer.append("    for(var i "+amount+"0;i<"+fieldname+".length;i++)\r\n");
                buffer.append("    {\r\n");
                buffer.append("      if("+fieldname+"[i].tagName"+amount+""+amount+"\"OBJECT\")\r\n");
                buffer.append("      {\r\n");
                buffer.append("       "+fieldname+"[i].value "+amount+"imageValue;\r\n");
                buffer.append("      }\r\n");
                buffer.append("    }\r\n");
                buffer.append("   }else {\r\n");
                buffer.append("        "+fieldname+".value "+amount+"imageValue;\r\n");
                buffer.append("   }\r\n");
                buffer.append("  }\r\n");
            }
        }
        return buffer.toString();


    }
    /**
     * html模板盖章公用js
     *
     * @return
     */
    public String getPub_js(String amount){
        String pub_js ="";
        pub_js +="	function resizeControl(c, a, b)\r\n";
        pub_js +="	{\r\n";
        pub_js +="		doResizeControl(a, b, c);\r\n";
        pub_js +="	}\r\n";
        pub_js +="	function doResizeControl(a, b, c)\r\n";
        pub_js +="	{\r\n";
        pub_js +="		document.all[c].style.visibility "+amount+" \"hidden\";\r\n";
        pub_js +="		document.all[c].style.pixelWidth "+amount+" a;\r\n";
        pub_js +="		document.all[c].style.pixelHeight "+amount+" b;\r\n";
        pub_js +="		document.all[c].style.visibility "+amount+" \"visible\";\r\n";
        pub_js +="	}\r\n";
        pub_js +="	function moveControl(c, a, b)\r\n";
        pub_js +="	{\r\n";
        pub_js +="		doMove(a, b, c);\r\n";
        pub_js +="	}\r\n";
        pub_js +="	function doMove(a, b, c)\r\n";
        pub_js +="	{\r\n";
        pub_js +="		document.all[c].style.visibility "+amount+" \"hidden\";\r\n";
        pub_js +="		var nw "+amount+" document.all[c].style.pixelWidth;\r\n";
        pub_js +="		var nh "+amount+" document.all[c].style.pixelHeight;\r\n";
        pub_js +="		document.all[c].style.left "+amount+" a;\r\n";
        pub_js +="		document.all[c].style.top "+amount+" b;\r\n";
        pub_js +="		document.all[c].style.visibility "+amount+" \"visible\";\r\n";
        pub_js +="	}\r\n";
        pub_js +="function GetAbsoluteLocation(objName){\r\n";
        pub_js +="	var element "+amount+" window.document.getElementById(objName);\r\n";
        pub_js +="    if ( arguments.length !"+amount+" 1 || element "+amount+""+amount+" null ){\r\n";
        pub_js +="        return null;\r\n";
        pub_js +="    }\r\n";
        pub_js +="    var offsetTop "+amount+" element.offsetTop;\r\n";
        pub_js +="    var offsetLeft "+amount+" element.offsetLeft;\r\n";
        pub_js +="    var offsetWidth "+amount+" element.offsetWidth;\r\n";
        pub_js +="    var offsetHeight "+amount+" element.offsetHeight;\r\n";
        pub_js +="    while( element "+amount+" element.offsetParent )\r\n";
        pub_js +="    {\r\n";
        pub_js +="        offsetTop +"+amount+" element.offsetTop;\r\n";
        pub_js +="        offsetLeft +"+amount+" element.offsetLeft;\r\n";
        pub_js +="    }\r\n";
        pub_js +="    	var labels "+amount+" new Array(offsetTop,offsetLeft,offsetWidth,offsetHeight);\r\n";
        pub_js +="		return labels;\r\n";
        pub_js +="}\r\n";
        return pub_js;
    }
    /**
     * 处理多页显示规则
     * @param doc
     * @return
     */
    String mutilPageField = null;
    public void setMutilPageField(String mutilPageField){
        this.mutilPageField = mutilPageField;
    }
    public String getMutilPageField(){
        return this.mutilPageField;
    }
    int  totalP = 0;
    public HashMap getMutilPageField(Document doc){
        HashMap mfield = new HashMap();

        List printRoot = doc.selectNodes("//PRINTDATA/ROW");
        String hasrule = null;


        for (int i = 0; i < printRoot.size(); i++)
        {
            Element ele = (Element) printRoot.get(i);
            String text = ele.getText();
            if (ele.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            hasrule = ele.attributeValue("hasrule");
            if("true".equalsIgnoreCase(hasrule))
            {
                List rule = ele.elements("RULE");
                if(rule!=null)
                    for(int j = 0;j<rule.size();j++)
                    {
                        Element erule = (Element)rule.get(j);
                        if(PubWS.RULE_MUTIL_PAGE.equalsIgnoreCase(erule.attributeValue("ruletype")))
                        {
                            setMutilPageField(ele.attributeValue("fieldname"));
                            String ruleText = erule.getText();//分页描述 用|分割方式 第一个参数为首页显示行数,2为其他也显示行数,3为尾页显示页数,4为每行显示字数
                            StringTokenizer t = null;
                            if(!Pub.empty(ruleText))
                            t =  new StringTokenizer(ruleText,"|");
                            ArrayList customRule = new ArrayList();
                            if(t!=null)
                            while(t.hasMoreTokens())
                            {
                                customRule.add(t.nextToken());
                            }
                            int firstRows = Integer.parseInt((String)customRule.get(0));
                            int otherRows = Integer.parseInt((String)customRule.get(1));
                            totalP = Integer.parseInt((String)customRule.get(2));
                            int rowCount = Integer.parseInt((String)customRule.get(3));
                            String content = ele.getText();
                            if(content != null && content.length() > 0){
                                content = content.replaceAll("<br>","\r\n");
                                content = content.replaceAll("<br/>","\r\n");
                            }
 
                            String s = null;
                            String pageTextTemp = "";
                            int pageCountTemp = 0;
                            ArrayList rowAll = new ArrayList();
                            int c = 0;
                            BufferedReader reader =null;
                            try
                            {
                                reader =  new BufferedReader( new StringReader(content));
                                s = reader.readLine();
                                String nextline = "";
                                // modified by guanchb 2008-04-17 start
                                // 保证笔录内容从WORD粘贴过来的内容与显示的内容一致
                                // 还需要保证处理意见文书中的现查明内容的格式有换行
                                // while (s != null && s.length() > 0)
                                while (s != null)
                                {
                                    if(s.length() <= 0){
                                        s = " ";
                                        //continue;
                                    }
                                    // modified by guanchb 2008-04-17 end;
                                    
                                    //addded by andy 
                                	//转换空格为&nbsp; begin
                                	// modified by guanchb@2009-04-30 start
                                	// 问题描述：呈请报告文书中出现空格时，文书预览和制作时显示空白页面。
                                	// 解决方法：除了英文输入法下的空格替换为&nbsp;外，中文输入法下的空格也替换为&nbsp;
                                	String temp_s = s.replaceAll(" ", "&nbsp;").replaceAll("　", "&nbsp;");
                                	// modified by guanchb@2009-04-30 end.
                                    if(s.length()<=rowCount)
                                    {
                                        if(s.indexOf("\r\n")>0){
                                            rowAll.add(temp_s);
                                        }else{
                                            rowAll.add(temp_s+"\r\n");
                                        }
                                    //end
                                    }else{//超出设置最大行字数，进行换行处理
                                        String rowN = "";
                                        int st = 0;
                                        while(s.length()>0){
                                           String temp = s.substring(0,1);
                                           byte[] d = temp.getBytes();//每个字符转换成字节数组，汉字或全角标点符号字节数组为2，其他为1
                                           //如果temp是空格,转换为&nbsp; begin
                                           // modified by guanchb@2009-04-30 start
                                           // 问题描述：呈请报告文书中出现空格时，文书预览和制作时显示空白页面。
                                           // 解决方法：原来只有英文输入法下的空格作为判断条件，增加中文输入法下空格的判断条件。
                                           if(temp.equals(" ") || temp.equals("　"))
                                           // modified by guanchb@2009-04-30 end.
                                        	   temp = "&nbsp;";
                                           //end
                                           st +=d.length;
                                           if(st<=rowCount*2){//每超出最大行字数，暂存到临时字符串
                                               rowN +=temp;
                                               s = s.substring(1,s.length());
                                           }else{//超出最大行字符数，将临时字符串放入链表中，初次化记录变量
                                              // rowN +=temp;
                                        	   //added by andy 20081204 判断是否是标点符号打头，如是，则向上取第一个汉字字符
                                        	   if(!"&nbsp;".equals(temp) && isHz(temp)==false)
                                        	   {
                                        		     int len = rowN.length();
                                        		     String ss = rowN.substring(len-1, len);
                                        		     String temps = ss;
                                        		     int ii = 1;
                                        		     while(isHz(ss)==false)
                                        		     {
                                        		    	 ii++;
                                        		    	 ss = rowN.substring(len-ii, len-ii+1);
                                        		    	 temps = ss+temps;
                                        		     }
                                        		     s = temps+s;
                                        		     rowN = rowN.substring(0,len-ii);
                                        	   }
                                               if(rowN.indexOf("\r\n")>0){
                                                   rowAll.add(rowN);
                                               }else{
                                                   rowAll.add(rowN+"\r\n");
                                               }
                                               rowN = "";
                                               st = 0;
                                           }
                                           continue;
                                        }//将换行后剩余字符串放入链表
                                        if(!rowN.equals("")){
                                            if (rowN.indexOf("\r\n") > 0)
                                            {
                                                rowAll.add(rowN);
                                            }
                                            else
                                            {
                                                rowAll.add(rowN + "\r\n");
                                            }
                                        }
                                    }
                                    	 s = reader.readLine();
                                }
                                if(rowAll.size()>0)
                                {
                                    int js1 = 0;
                                    int js2 = 0;
                                    int js3 = 0;
                                    int page = 1;
                                    String temp = "";

                                    while(js1<rowAll.size()){
                                        temp +=(String)rowAll.get(js1);
                                        js1++;
                                        if(js1 == firstRows){
                                            break;
                                        }
                                    }
                                    mfield.put(String.valueOf(page),temp);
                                    page++;
                                    temp = "";
                                    if(js1 == firstRows){

                                        while (js2 < rowAll.size()-firstRows)
                                        {
                                            temp += (String) rowAll.get(js2+firstRows);
                                            js2++;
                                            js3++;
                                            if(js3 == otherRows){
                                                mfield.put(String.valueOf(page),temp);
                                               page++;
                                               temp = "";
                                               js3 = 0;
                                            }

                                        }
                                        if(js3 < otherRows){
                                            mfield.put(String.valueOf(page),temp);

                                        }

                                    }
//                                    System.out.println(mfield.size());
                                }

                            }
                            catch (Exception ex)
                            {
                                System.out.println("读取分页域出错！");

                                ex.printStackTrace();
                            }



                            break;
                        }
                    }
            }else
            {
            	//added by andy 20081111 将节点内容中的空格转换成&nbsp;
            	if(text != null && !"".equals(text))
            	{
            	  text = text.replaceAll(" ", "&nbsp;");
            	  ele.setText(text);
            	}  
            }
        }
        return mfield;

    }
//added by andy 20081204 判断字符是否是汉字
public boolean isHz(String i)
{
	boolean res = true;
	
	//对于双/单引号特殊判断
//	if("“".equals(i)) return true;
//	if("‘".equals(i)) return true;
//	if("《".equals(i)) return true;
	//用字典的方式：允许打头的标点符号在字典表中配置
	String dic_value = Pub.getDictValueByCode("DTZF", i);
	if(dic_value != null) 
		return true;
	
	String regEx = "[\\u4e00-\\u9fa5]";
	Pattern p = Pattern.compile(regEx);
	res = p.matches(regEx, i);
	return res;
}




}
