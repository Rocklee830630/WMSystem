package com.ccthanking.framework.coreapp.orgmanage;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.config.*;
import java.io.*;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.MenuVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;

public class RightManager
{
  private RightManager()
  {
  }

  public static boolean checkPower(HttpServletRequest request,HttpServletResponse response,String menuName) throws Exception
  {
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    if(user == null)
    {
      request.setAttribute(Globals.KEY_ERROR_MESSAGE,"无效会话，请登录后访问！");
      request.setAttribute(Globals.KEY_WHEN_ERROR_GOTO,"/index.jsp");
      response.sendRedirect("/error.jsp");
      throw new Exception("无效会话，请登录后访问！");
    }
    else
    {
      if(user.getPersonKind().equals(User.SUPER_USER)) return true;
      if(Pub.empty(menuName)) return true;
      MenuVo[] menus = user.getAllowedMenus();
      if(menus != null)
      {
        for (int i = 0; i < menus.length; i++) {
          if (menus[i] == null)
            continue;
          if (menuName.equals(menus[i].getName())) {
            return true;
          }
        }
      }
      request.setAttribute(Globals.KEY_ERROR_MESSAGE,"无效页面，请通知系统管理员！");
      request.setAttribute(Globals.KEY_WHEN_ERROR_GOTO,"/index.jsp");
      response.sendRedirect("/error.jsp");
      throw new Exception("无效页面，请通知系统管理员！");
    }
  }

  public static boolean checkPowerWriteXml(HttpServletRequest request,HttpServletResponse response,String menuName) throws Exception
  {
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    if(user == null)
    {
      Pub.writeXmlMessage(response,"无效会话，请登录后访问！","ERRMESSAGE");
      throw new Exception("无效会话，请登录后访问！");
    }
    else
    {
      if(user.getPersonKind().equals(User.SUPER_USER)) return true;
      if(Pub.empty(menuName)) return true;
      MenuVo[] menus = user.getAllowedMenus();
      if(menus != null)
      {
        for (int i = 0; i < menus.length; i++) {
          if (menus[i] == null)
            continue;
          if (menuName.equals(menus[i].getName())) {
            return true;
          }
        }
      }
      Pub.writeXmlMessage(response,"无效页面，请通知系统管理员！","ERRMESSAGE");
      throw new Exception("无效页面，请通知系统管理员！");
    }
  }


}