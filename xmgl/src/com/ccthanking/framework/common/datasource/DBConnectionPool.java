package com.ccthanking.framework.common.datasource;

import java.io.PrintWriter;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DBConnectionPool implements DataSource,java.io.Serializable

{

  /**

  * 取回连接的用户名

  * @return 用户名

  */

  public String getConnectionUserName() {
    return _user;

  }

  /**

  * 设定连接的用户名

  * @param s 用户名 username

  */

  public void setConnectionUserName(String s)

  {

    _user = s;

  }

  /**

  * 设定连接的密码

  * @param s 密码 password

  */

  public void setConnectionPassword(String s)

  {

    _pass = s;

  }

  /**

  * 取回连接的URL

  * @return 连接URL

  */

  public String getConnectionURL()

  {

    return _url;

  }

  /**

  * 设定连接的URL

  * @param s 连接URL

  */

  public void setConnectionURL(String s)

  {

    _url = s;

  }

  /**

  * 取回连接的Driver名字

  * @return 连接的Driver名

  */

  public String getConnectionDriverName()

  {

    return _driver;

  }

  /**

  * 设定连接的Driver名字

  * @param s 连接的Driver名

  */

  public void setConnectionDriverName(String s)

  {

    _driver = s;

  }

  /**

  * 取回连接池允许的最大连接数

  * @return 最大连接数

  */

  public int getMaxPool()

  {

    return _maxPool;

  }

  /**

  * 设定连接池允许的最大连接数

  * @param i 连接池允许的最大连接数

  */

  public void setMaxPool(int i)

  {

    _maxPool = i;

  }

  /**

  * 取回连接池允许的最小连接数

  * @return 最小连接数

  */

  public int getMinPool()

  {

    return _minPool;

  }

  /**

  * 设定连接池允许的最小连接数

  * @param i 连接池允许的最小连接数

  */

  public void setMinPool(int i)

  {

    _minPool = i;

  }

  /**

  * 取回日志的writer，不使用该方法。抛出NotSupportException

  * @return 日志的writer

  * @throws NotSupportException

  */

   public PrintWriter getLogWriter()

 {

      // return _out;
    // throw new OPUnsupportedException("getLogWriter");
    return null;

 }

  /**

  * 设定日志的writer，不使用该方法。抛出NotSupportException

  * @param printwriter 日志的writer

  * @throws NotSupportException

  */

 public void setLogWriter(PrintWriter printwriter)

 {

 //  _out = printwriter;
 //throw new OPUnsupportedException("setLogWriter");

 }

  /**

  * 取回连接请求的等待时间

  * @return 连接请求的等待时间

  * @throws SQLException

  */

  public int getLoginTimeout()

  throws SQLException

  {

    return _timeout;

  }

  /**

  * 设定连接请求的等待时间

  * @param i 连接请求的等待时间

  * @throws SQLException

  */

  public void setLoginTimeout(int i)  throws SQLException

  {

    _timeout = i;

  }

  /**

  * 取回连接允许占用时间

  * @return 连接允许占用时间

  */

  public int getAutoReturnTimeout()

  {

    return _autoReturn;

  }

  /**

  * 设定连接允许占用时间

  * @param i 连接允许占用时间

  */

  public void setAutoReturnTimeout(int i)

  {

    _autoReturn = i;

  }

  /**

  * 初始化连接池，返回一个可用的的连接对象

  * @return Connection对象

  * @throws SQLException

  */

  public Connection getConnection() throws SQLException

  {

     return getConnection(_user, _pass);

  }

  /**

  * 初始化连接池，返回一个可用的的连接对象

  * @param s user

  * @param s1 password

  * @return Connection对象

  * @throws SQLException

  */

  public synchronized Connection getConnection(String s, String s1) throws SQLException

  {
    if(_maxPool < 1) {
//	  //添加性能分析测试 5/17
//		if(this.isAnalyse()==true){
//		    PerformanceManager pm=PerformanceManager.getInstance();
//		    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//			pl.addElement("get Connection object at:"+new java.lang.Long(System.currentTimeMillis()).toString());
//		}
//	  //5/12
		return new DBConnection(_url, s, s1, this);
	}

    for(; _pbusy.size() + _pfree.size() < _minPool; _pfree.add(new DBConnection(_url,_user, _pass, this)));

    DBConnection con = findConnection(s, s1);

    if(con != null) {//5/17添加纪录
//	  if(this.isAnalyse()==true){
//			PerformanceManager pm=PerformanceManager.getInstance();
//		    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//			pl.addElement("get Connection object at:"+new java.lang.Long(System.currentTimeMillis()).toString());
//	  }
	return con;
	}

    long l = System.currentTimeMillis();

    for(long l1 = l + (long)(_timeout * 1000); l < l1; l = System.currentTimeMillis())

    {

      try {

        wait(l1 - l);

      }

      catch(InterruptedException interruptedexception) {

      }

      DBConnection con1 = findConnection(s, s1);

      if(con1 != null) {//5/17添加纪录
//	    if(this.isAnalyse()==true){
//		    PerformanceManager pm=PerformanceManager.getInstance();
//		    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//			pl.addElement("get Connection object at:"+new java.lang.Long(System.currentTimeMillis()).toString());
//		}
	    return con1;
	  }

    }//end for

    throw new SQLException("timeout"+_timeout );

  }

  /**

  *  重新启动连接池

  */

  public synchronized void  reStart() {

    try{

      close();

      for(; _pbusy.size() + _pfree.size() < _minPool; _pfree.add(new DBConnection(_url, _user, _pass, this)));

    }

    catch(Exception e){

    }

  }

  /**

  * 取回连接的Driver

  * @return Driver

  */

  public String getDriver()

  {

    return _driver;

  }

  /**

  * 关闭连接池

  */

  public synchronized void close()  {

    try{

      DBConnection connection = null;

      for(Iterator iterator = _pbusy.entrySet().iterator(); iterator.hasNext();) {

        java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();

        connection = (DBConnection)entry.getValue();

        connection.disconnect();

      }

      _pbusy.clear();

      for(Iterator iterator1 = _pfree.iterator(); iterator1.hasNext();){

        connection = (DBConnection)iterator1.next();

        connection.disconnect();

      }

      _pfree.clear();

    }

    catch(Exception e)

    {

 //     log(getClass().getName()+":clear:"+e.getMessage());

    }

  }

  /**

  * 整理连接池，移去已关闭的连接和占用超时的连接

  * @throws Exception

  */

  private void clean() throws Exception

  {

    long l = System.currentTimeMillis();

    for(Iterator iterator = _pbusy.entrySet().iterator(); iterator.hasNext();) {

      java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();

      if(((Number)entry.getValue()).longValue() + (long)(_autoReturn * 1000) < l){

        iterator.remove();

        releaseConnection((DBConnection)entry.getKey());

      }

      else

      if(((Connection)entry.getKey()).isClosed())

      iterator.remove();

    }

    //deal with 2 parts

    for(Iterator iterator1 = _pfree.iterator(); iterator1.hasNext();)

    if(((DBConnection)iterator1.next()).isClosed())

    iterator1.remove();

  }

  /**

  * 在空闲连接中查找可用连接

  * @param s user 连接的用户名

  * @param s1 password 连接的密码

  * @return OPConnection 对象

  */

  private DBConnection findConnection(String s, String s1) //throws Exception

  {

    try{

      clean();

      if(_pbusy.size() >= _maxPool) return null;

      DBConnection connection = null;

      for(Iterator iterator = _pfree.iterator(); iterator.hasNext();){

        connection = (DBConnection)iterator.next();

        if(equal(connection.getUserName(), s) && equal(connection.getPassword(), s1)) {

          iterator.remove();

          _pbusy.put(connection, new Long(System.currentTimeMillis())); //EntryTime

          return connection;

        }

      }

      connection = null;

      if(_pbusy.size() + _pfree.size() < _maxPool) connection = new DBConnection(_url, s, s1, this);

      if(connection == null && _pfree.size() > 0){

        Iterator iterator1 = _pfree.iterator();

        ((DBConnection)iterator1.next()).disconnect();

        iterator1.remove();

        connection = new DBConnection(_url, s, s1, this);

      }

      if(connection != null) _pbusy.put(connection, new Long(System.currentTimeMillis()));

      return connection;

    } catch(Exception e){

      return null;

    }

  }

  /**

  * 判断String是否相等

  * @param s String 1

  * @param s1 String 2

  * @return 相等返回true,否则返回false

  */

  private boolean equal(String s, String s1)

  {

    return s==null ? (s1 == null):(s.equals(s1));

  }

  /**

  * 日志纪录，不使用，改用log4j规范

  * @param s

  */

 // void log(String s){

//    if(_out != null){

//      _out.println(s);

//      _out.flush();

//    }

//  }

  /**

  * 释放连接，如未达到最大连接数，将连接返回池中；否则断开连接

  * @param connection connection对象

  */

  public synchronized void releaseConnection(DBConnection connection)

  {

    try

    {

      _pbusy.remove(connection);

      if(!connection.isClosed())

		if(_pbusy.size() + _pfree.size() < _maxPool){
		  //添加性能分析测试 5/17
//		  if(this.isAnalyse()==true){
//		    PerformanceManager pm=PerformanceManager.getInstance();
//		    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//			pl.addElement("connection returned to pool at:"+new java.lang.Long(System.currentTimeMillis()).toString());
//			pm.removeLog(Thread.currentThread().getName());
//		  }
		  //5/17
		  _pfree.add(connection);
		}
        else

        connection.disconnect();

    }

    catch(Exception Exception)

    {

  //    log(Exception.getMessage());

    }

    // notifyAll();

    notify();

  }

  /**

  * 设定连接池对象对应的DataSourceID

  * @param i 连接池对象对应的DataSourceID

  */

  public void setDatasourceID(String s) {

    _strSourceID =s;

  }

  /**

  * 取回连接池对象对应的DataSourceID

  * @return 连接池对象对应的DataSourceID

  */

  public String  getDatasourceID() {

    return _strSourceID;

  }

  /**

  * 构造函数

  */

  public DBConnectionPool()

  {

  //  _out = null;

    _url = null;

    _user = null;

    _pass = null;

    _driver = null;

    _minPool = 2;

    _maxPool = 300;

    _timeout = 30;

    _autoReturn = 60;

    _pbusy = new WeakHashMap();

    _pfree = new HashSet();

  }

  /**

  * 取回当前池中所有的连接数

  * @return 当前池中所有的连接数

  */

  public int getSize()

  {

    return _pbusy.size()+_pfree.size();

  }

  /**

  * 构造函数

  * @param s 连接的dirver

  * @param s1 连接的url

  * @param s2 连接的用户名

  * @param s3 连接的密码

  */

  public DBConnectionPool(String s,String s1,String s2,String s3)

  {

 //   _out = null;

    _url = s1;

    _user = s2;

    _pass = s3;

    _driver = s;

    _minPool = 2;

    _maxPool = 300;

    _timeout = 30;

    _autoReturn = 60;

    _pbusy = new WeakHashMap();

    _pfree = new HashSet();

  }

  /**

  * 构造函数

  * @param s 连接的Driver

  * @param s1 连接的url

  * @param s2 连接的用户名

  * @param s3 连接的密码

  * @param min pool的最小连接数

  * @param max pool的最大连接数

  * @param mswait 连接请求的超时时间

  */

  public DBConnectionPool(String s,String s1,String s2,String s3,int min,int max,int mswait)

  {

  //  _out = null;

    _url = s1;

    _user = s2;

    _pass = s3;

    _driver = s;

    _minPool =min;

    _maxPool = max;

    _timeout = mswait;

    _autoReturn = 60;

    _pbusy = new WeakHashMap();

    _pfree = new HashSet();

  }

  /**

  * 池中使用中Connection，添加时要记录始用时间,构造函数中初始化

  */

  private Map _pbusy;

  /**

  * 池中空闲Connection,构造函数中初始化

  */

  private Set _pfree;

  /**

  * 日志的记录者

  */

 // private PrintWriter _out;

  /**

  * 数据库连接的url

  */

  private String _url;

  /**

  * 数据库连接的用户

  */

  private String _user;

  /**

  * 数据库连接的密码

  */

  private String _pass;

  /**

  * 数据库连接的的驱动期名

  */

  private String _driver;

  /**

  * 池中最小的连接数 构造函数中初始值2

  */

  private int _minPool;

  /**

  * 池中最大的连接数,构造函数中初始值10

  */

  private int _maxPool;

  /**

  * 一个请求在池中生存时间(即请求超时),构造函数中初始值30

  */

  private int _timeout;

  /**

  * 用户允许占用连接时间,构造函数中初始值30

  */

  private int _autoReturn;

  /**

  * 若该连接池对应的数据源有一个唯一的整数标识,则成员变量_iSourceID就等于这个值，该值可以为空

  */

 // protected int _iSourceID;
  protected String  _strSourceID;

  /**
   * 性能日志纪录5/17
   */
  private boolean _isAnalyse;
  /**
   * 性能分析 5/17
   * @param flag
   */
  private void setAnalyse(boolean flag){
    _isAnalyse=flag;
  }
  public boolean isAnalyse(){
	return _isAnalyse;
  }

@Override
public <T> T unwrap(Class<T> iface) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean isWrapperFor(Class<?> iface) throws SQLException {
	// TODO Auto-generated method stub
	return false;
}

@Override
public Logger getParentLogger() throws SQLFeatureNotSupportedException {
	// TODO Auto-generated method stub
	return null;
}
}

