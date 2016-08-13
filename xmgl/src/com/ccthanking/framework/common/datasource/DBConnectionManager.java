package com.ccthanking.framework.common.datasource;

import java.io.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.naming.*;

import com.ccthanking.framework.common.datasource.DBConnectionPool;
import com.ccthanking.framework.log.log;
import com.ccthanking.framework.util.*;
public class DBConnectionManager
{
    static private DBConnectionManager instance;
    static private int clients;
    private Vector drivers = new Vector();
    private org.apache.log4j.Logger lg =log.getLogger("DBConnectionManager");
    private Hashtable pools = new Hashtable();
    private Hashtable dsTable = new Hashtable();
    private Hashtable dsName = new Hashtable();
    private Hashtable dsPass = new Hashtable();
    private Hashtable ctxTable = new Hashtable();
    private Hashtable ejbHome = new Hashtable();
    public Object getEjbHome(String beanname)
    {
        return getEjbHome("default",beanname);
    }
    //add by wuxp 2008.05.07
    //获取已经存在的数据源名称
    public String[] getDataSource()
    {
        String result[] = new String[this.pools.size()];
        java.util.Enumeration emd = this.pools.keys();
        int i=0;
        while(emd.hasMoreElements())
        {
            result[i]=emd.nextElement().toString();
            i++;
        }
        return result;
    }
    //

    public Object getEjbHome(String app,String bean)
    {
        if(Pub.empty(bean)) return null;
        if(Pub.empty(app)) app = "default";
        if(ejbHome.get(app+bean) != null) return ejbHome.get(app+bean);
        else if(ejbHome.get(bean) != null) return ejbHome.get(bean);
        else
        {
            Context ctx = this.getContext(app);
            if(ctx == null) return null;
            try
            {
                Object handle = ctx.lookup(bean);
                if(handle == null) return null;
                this.ejbHome.put(app+bean,handle);
                this.ejbHome.put(bean,handle);
                return handle;
            }
            catch (NamingException ex)
            {
                ex.printStackTrace(System.out);
                return null;
            }
        }
    }

    public int getCounts()
    {
        return this.clients;
    }

    static synchronized public DBConnectionManager getInstance()
    {
        if (instance == null)
        {
            instance = new DBConnectionManager();
        }
        clients++;
        return instance;
    }

    public String getUrl(String name)
    {
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null)
        {
            return pool.getConnectionURL();
        }
        return null;
    }

    private DBConnectionManager()
    {
        init();
    }
    /*
     * 获取一个默认的数据库连接
     */
    public Connection getDefaultConnection()
    {
        return this.getConnection("P3");
    }
/**
     * 获得一个可用的(空闲的)连接.如果没有可用连接,且已有连接数小于最大连接数
     * 限制,则创建并返回新连接
     *
     * @param name 在属性文件中定义的连接池名字
     * @return Connection 可用连接或null
     */
    public Connection getConnection(String dataSourceName)
    {
        String name = dataSourceName.toUpperCase();
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        try
        {
            if (pool != null)
            {
                return pool.getConnection();
            }

            DataSource ds = (DataSource) dsTable.get(name);
            if (ds != null)
            {
                String username = (String) dsName.get(name);
                String password = (String) dsPass.get(name);
                if (Pub.empty(username) && Pub.empty(password))
                    return ds.getConnection();
                else
                    return ds.getConnection(username==null?"":username.trim(),password==null?"":password.trim());//ds.getConnection("weblogic","weblogic");//
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            lg.error("取数据库连接失败！");
        }
        return null;
    }
/**
     * 关闭所有连接,撤销驱动程序的注册
     */
    public synchronized void release()
    {
        // 等待直到最后一个客户程序调用
        if (--clients != 0)
        {
            return;
        }
        Enumeration allPools = pools.elements();
        while (allPools.hasMoreElements())
        {
            DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();

        }
        /*
        Enumeration allDrivers = drivers.elements();
        while (allDrivers.hasMoreElements())
        {
            Driver driver = (Driver) allDrivers.nextElement();
            try
            {
                DriverManager.deregisterDriver(driver);
                lg.info("撤销JDBC驱动程序 " + driver.getClass().getName() + "的注册");
            }
            catch (SQLException e)
            {
                lg.error( "无法撤销下列JDBC驱动程序的注册: " + driver.getClass().getName());
            }
        }
        */
    }

    private void createPools(Properties props)
    {
        Enumeration propNames = props.propertyNames();
        while (propNames.hasMoreElements())
        {
            String name = (String) propNames.nextElement();
            if (name.endsWith(".url"))
            {
                String poolName =  name.substring(0, name.lastIndexOf("."));
                String url = props.getProperty(poolName + ".url");
                if (url == null)
                {
                    lg.info("没有为连接池[" + poolName + "]指定URL\n");
                    continue;
                }
                String user = props.getProperty(poolName + ".user");
                String password = props.getProperty(poolName + ".password");
                String maxconn = props.getProperty(poolName + ".maxconn", "0");
                String driver  =props.getProperty(poolName + ".driver");
                int max;
                try
                {
                    max = Integer.valueOf(maxconn).intValue();
                }
                catch (NumberFormatException e)
                {
                    lg.info("错误的最大连接数限制: " + maxconn + " .连接池: " + poolName);
                    max = 0;
                }

                DBConnectionPool pool = new DBConnectionPool(driver, url,user, password,2,max,30);


                pools.put(poolName.toUpperCase(), pool);

                lg.info("成功创建连接池[" + poolName+"]\n");
            }
        }
    }

    private void loadDataSource(Properties props)
    {
        Enumeration propNames = props.propertyNames();
        while (propNames.hasMoreElements())
        {
            String name = (String) propNames.nextElement();
            if (name.endsWith(".ds"))
            {
                String poolName = name.substring(0, name.lastIndexOf("."));
                String url = props.getProperty(name,"");
                if (Pub.empty(url.trim()))
                {
                    lg.info("没有为连接池[" + poolName + "]指定JNDI名称");
                    continue;
                }
                String user = props.getProperty(poolName + ".user");
                String password = props.getProperty(poolName + ".password");
                try
                {
                    Context ctx = this.getContext(poolName);
                    if(ctx == null) ctx = this.getContext();
                    if(ctx == null)
                    {
                        lg.info("找不到上下文环境[" + poolName+"]\n");
                        continue;
                    }
                    DataSource ds = (DataSource) ctx.lookup(url);
                    if (ds != null)
                        lg.info("成功创建连接池[" + poolName+"]\n");
                    else
                        continue;
                    dsTable.put(poolName.toUpperCase(), ds);
                    dsName.put(poolName.toUpperCase(), user == null ? "" : user);
                    dsPass.put(poolName.toUpperCase(), password == null ? "" : password);
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace(System.out);

                    lg.info("创建连接池[" + poolName + "]失败！");
                }
            }
        }
    }
    private Context createContext(String ejb_url,String ini_ctx,String uname,String upass)
    {
        try
        {
            Context ctx = null;
            Properties properties = new Properties();
            if(!Pub.empty(ini_ctx))
                properties.put(Context.INITIAL_CONTEXT_FACTORY,ini_ctx);
            if(!Pub.empty(ejb_url))
                properties.put(Context.PROVIDER_URL,ejb_url);
            if(properties.size() > 0)
                ctx = new InitialContext(properties);
            else
                ctx = new InitialContext();
            return ctx;
        }
        catch(Exception e)
        {
            System.out.println("Create context error! :"+e.getMessage());
            e.printStackTrace(System.out);
            return null;
        }
    }
    public Context getContext()
    {
        return getContext("default");
    }
    public Context getContext(String name)
    {
        Context ctx = (Context) this.ctxTable.get(name);
        return ctx;
    }
    public String getAppURL()
    {
        return getAppURL("default");
    }
    public String getAppURL(String name)
    {
        return (String) this.ctxTable.get(name+".appserver");
    }
    public String getAppUserName()
    {
        return getAppUserName("default");
    }
    public String getAppUserName(String name)
    {
        return (String) this.ctxTable.get(name+".appusername");
    }
    public String getAppUserPass()
    {
        return getAppUserPass("default");
    }
    public String getAppUserPass(String name)
    {
        return (String) this.ctxTable.get(name+".appuserpass");
    }
    public String getAppCtx()
    {
        return (String) this.ctxTable.get("default");
    }
    public String getAppCtx(String name)
    {
        return (String) this.ctxTable.get(name+".appctx");
    }
    private void loadAppServerURL(Properties props)
    {
        Enumeration propNames = props.propertyNames();
        while (propNames.hasMoreElements())
        {
            String name = (String) propNames.nextElement();
            if (name.endsWith(".appserver"))
            {
                String appName = name.substring(0, name.lastIndexOf("."));
                String url = props.getProperty(name,"");
                if (Pub.empty(url.trim()))
                {
                    lg.info("没有为应用服务器[" + appName + "]指定URL\n");
                    //continue;
                }
                String user = props.getProperty(appName + ".appusername","");
                String password = props.getProperty(appName + ".appuserpass","");
                String ini_ctx = props.getProperty(appName+".appctx","");
                try
                {
                    Context ctx = this.createContext(url,ini_ctx,user,password);
                    if (ctx != null)
                        lg.info("成功获取上下文环境[" + appName+"]\n");
                    else
                        continue;
                    ctxTable.put(appName+".appserver",url);
                    ctxTable.put(appName+".appusername",user);
                    ctxTable.put(appName+".appuserpass",password);
                    ctxTable.put(appName+".appctx",ini_ctx);
                    ctxTable.put(appName,ctx);
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace(System.out);
                    lg.info("获取[" + appName + "]服务器上下文环境失败！\n");
                }
            }
        }
    }


    public Connection getConnectionFromDs(String name)
    {
        try
        {
            DataSource ds = (DataSource) dsTable.get(name);
            if (ds != null)
            {
                String username = (String) dsName.get(name);
                String password = (String) dsPass.get(name);
                if (Pub.empty(username) && Pub.empty(password))
                    return ds.getConnection();
                else
                    return ds.getConnection(username.trim(),password.trim());
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            lg.error("取数据库连接失败！");
        }
        return null;
    }

    public Connection getConnectionFromDs(String name, String username,
                                    String password)
    {
        try
        {
            DataSource ds = (DataSource) dsTable.get(name);
            if (ds != null)
            {
                if (Pub.empty(username) && Pub.empty(password))
                    return ds.getConnection();
                else
                    return ds.getConnection(username==null?"":username.trim()
                                            ,password==null?"":password.trim());
            }
        }
        catch (Exception e)
        {
            lg.error("取数据库连接失败！");
        }
        return null;
    }

    //add by wuxp
    //数据源配置文件路径
    private static  String ConnectionPath="";

    public static void setConnectionPath(String path)
    {
        ConnectionPath=path;
    }

    //add by wuxp
    private void init()
    {
        Properties dbProps = new Properties();
        String path=ConnectionPath+"connection.ini";
        FileInputStream fis=null;
        try
        {
            fis = new FileInputStream(path);
            dbProps.load(fis);
        }
        catch (IOException ex)
        {
            System.err.println("不能读取属性文件. " + "请确保配制文件在指定的路径中");
            return;
        }
        finally
        {
            try
            {
                if(fis != null)fis.close();
            }
            catch(IOException ex)
            {
                System.err.println("fis 关闭失败");
            }
        }

        createPools(dbProps);

        loadAppServerURL(dbProps);

        loadDataSource(dbProps);
    }

    private void log(String msg)
    {
        lg.info(new java.util.Date() + ": " + msg);
    }

    private void log(Throwable e, String msg)
    {
        lg.info(new java.util.Date() + ": " + msg);
        lg.info(msg,e);
    }
}
