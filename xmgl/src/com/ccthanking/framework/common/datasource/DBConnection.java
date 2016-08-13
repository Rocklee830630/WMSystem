package com.ccthanking.framework.common.datasource;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import com.ccthanking.framework.common.datasource.*;

public class DBConnection implements Connection {
	

	  /**
	   * 取得数据库类型
	   * @return 数据库类型
	   */
	  public String getDBType() {

	    return _dbType;
	    
	  }

	  public PreparedStatement prepareStatement(String sql,  String[] columnNames) throws SQLException
	  {
	     return  _conn.prepareStatement(sql,columnNames);


	    }

	    public void setHoldability(int holdability)
	                    throws SQLException{
	                      _conn.setHoldability(holdability);

	                      }


	    public int getHoldability()
	                   throws SQLException{
	                     return _conn.getHoldability();
	                     }

	    public void releaseSavepoint(Savepoint savepoint)
	                      throws SQLException{
	                        _conn.releaseSavepoint(savepoint);

	                        }


	                        public void rollback(Savepoint savepoint)
	              throws SQLException{
	                _conn.rollback(savepoint);
	                }


	                public Savepoint setSavepoint(String name)
	                       throws SQLException{
	                         return _conn.setSavepoint(name);
	                         }
	              public Savepoint setSavepoint()
	                                          throws SQLException{
	                                            return _conn.setSavepoint();
	                                            }


	    public PreparedStatement prepareStatement(String sql,  int column) throws SQLException
	 {


	    return  _conn.prepareStatement(sql,column);
	   }

	   public Statement createStatement(int resultSetType,
	                                   int resultSetConcurrency,
	                                   int resultSetHoldability)
	                            throws SQLException{
	                          return _conn.createStatement(resultSetType,
	                                  resultSetConcurrency,
	                                  resultSetHoldability);


	                              }



	   public PreparedStatement prepareStatement(String sql,
	                                            int resultSetType,
	                                            int resultSetConcurrency,
	                                            int resultSetHoldability)
	      throws SQLException {

	    return  _conn.prepareStatement(sql,
	                              resultSetType,
	                              resultSetConcurrency,
	                              resultSetHoldability);

	}





	   public CallableStatement prepareCall(String sql,
	                                      int resultSetType,
	                                      int resultSetConcurrency,
	                                      int resultSetHoldability)
	                               throws SQLException{
	                             return  _conn.prepareCall(  sql,
	                                                             resultSetType,
	                                                             resultSetConcurrency,
	                                                             resultSetHoldability);

	              }




	    public PreparedStatement prepareStatement(String sql,  int[] columnNames) throws SQLException
	    {
	       return  _conn.prepareStatement(sql,columnNames);
	      }


	  /**

	  * 取得连接对象名

	  * @return Connection对象名

	  */

	  public Connection getConnection()  {

	    return _conn;

	  }

	  /**

	  * 取得URL

	  * @return URL

	  */

	  public String getURL()  {

	    return _url;

	  }

	  /**

	  * 取得用户名

	  * @return username

	  */

	  public String getUserName()

	  {

	    return _user;

	  }

	  /**

	  * 取得密码

	  * @return password

	  */

	  public String getPassword()

	  {

	    return _pass;

	  }

	  /**

	  * 取得连接池

	  * @return 内部变量_source

	  */

	  public javax.sql.DataSource  getParent()

	  {

	    return _source;

	  }

	  /**

	  * 取得log的writer

	  * @return

	  */

//	  public PrintWriter getLogWriter()

//	  {

//	    return _out;

//	  }

	  /**

	  * 设定log的writer

	  * @param printwriter

	  */

//	  public void setLogWriter(PrintWriter printwriter)

//	  {

//	    _out = printwriter;

//	  }

	  /**

	  * 建立一个向数据库发送SQL语句的Stament对象

	  * @return Statment 对象

	  * @throws SQLException

	  */

	  public Statement createStatement() throws SQLException

	  {

	    try

	  {
//		  //添加性能分析测试 5/17
//			if(this.isAnalyse()==true){
//			    PerformanceManager pm=PerformanceManager.getInstance();
//			    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//				pl.addElement("statement created at:"+new java.lang.Long(System.currentTimeMillis()).toString());
//			}
	      return new DBStatement(_conn.createStatement(), this);

	    }

	    catch(SQLException sqlexception) {

	      //log(sqlexception.getMessage());

	      return null;

	    }

	  }

	  /**

	  * 写日志，连接到数据库

	  * @throws SQLException

	  */

	  public void connect() throws SQLException{



	  //  log("connect: " + _url + " (" + _user + ")");

	    try{
//		  //添加性能分析测试 5/17
//			if(this.isAnalyse()==true){
//			    PerformanceManager pm=PerformanceManager.getInstance();
//			    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//				pl.addElement("connection connected at:"+new java.lang.Long(System.currentTimeMillis()).toString()+"with:("+_url+","+_user+")");
//			}
	      Class.forName(_source==null? _driver:_source.getDriver());
	      
	     

	    }

	    catch (ClassNotFoundException ex){

	      ex.printStackTrace();

	    }

	    _conn = DriverManager.getConnection(_url, _user, _pass);

	  }

	  /**

	  * 记日志，断开连接

	  * @throws SQLException

	  */

	  void disconnect() throws SQLException

	  {
//	  //添加性能分析测试 5/17
//			if(this.isAnalyse()==true){
//			    PerformanceManager pm=PerformanceManager.getInstance();
//			    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//				pl.addElement("connection closed at:"+new java.lang.Long(System.currentTimeMillis()).toString()+"with:("+_url+","+_user+")");
//				pm.removeLog(Thread.currentThread().getName());
//			}
	   // log("close: " + _url + " (" + _user + ")");

	    _conn.close();

	  }

	  /**

	  * 输出日志，不使用该方法，使用log4j规范

	  * @param s

	  */

//	  void log(String s){

	 //   if(_out != null){

	 //     _out.println(s);

//	      _out.flush();

//	    }

	 // }

	  /**

	  * 建立一个向数据库发送参数化SQL语句的PreparedStatment

	  * @param s SQL

	  * @return PreparedStatment

	  * @throws SQLException

	  */

	  public PreparedStatement prepareStatement(String s) throws SQLException

	  {

	    return _conn.prepareStatement(s);

	  }

	  /**

	  * 建立一个调用数据库存储过程的CallableStatement对象

	  * @param s SQL

	  * @return CallableStatement

	  * @throws SQLException

	  */

	  public CallableStatement prepareCall(String s) throws SQLException

	  {

	    return _conn.prepareCall(s);

	  }

	  /**

	  * 把SQL语句翻译成系统本地的

	  * @param s SQL

	  * @return system native SQL grammar

	  * @throws SQLException

	  */

	  public String nativeSQL(String s)  throws SQLException

	  {

	    return _conn.nativeSQL(s);

	  }

	  /**

	  * 设定自动提交

	  * @param flag

	  * @throws SQLException

	  */

	  public void setAutoCommit(boolean flag) throws SQLException

	  {

	    _conn.setAutoCommit(flag);

	  }

	  /**

	  * 取得当前auto-commit状态

	  * @return 是返回true 否返回false

	  * @throws SQLException

	  */

	  public boolean getAutoCommit()  throws SQLException

	  {

	    return _conn.getAutoCommit();

	  }

	  /**

	  * 保存所有从上一次提交/回滚操作后所有改变，释放该连接数据库锁定

	  * @throws SQLException

	  */

	  public void commit() throws SQLException

	  {

	    _conn.commit();

	  }

	  /**

	  * 放弃所有从上一次提交/回滚操作后所有改变，释放该连接数据库锁定

	  * @throws SQLException

	  */

	  public void rollback() throws SQLException

	  {

	    _conn.rollback();

	  }

	  /**

	  * 立即释放连接的数据库和JDBC资源，不等待其自动释放

	  * @throws SQLException

	  */

	  public void close()  throws SQLException

	  {

	    if(_source != null)

	    _source.releaseConnection(this);
	    else
//		//添加性能分析测试 5/17
//			if(isAnalyse()==true){
//			    PerformanceManager pm=PerformanceManager.getInstance();
//			    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//				pl.addElement("connection closed at:"+new java.lang.Long(System.currentTimeMillis()).toString()+"with:("+_url+","+_user+")");
//				pm.removeLog(Thread.currentThread().getName());
//			}
	    _conn.close();

	  }

	  /**

	  * 判断连接是否关闭

	  * @return boolean

	  * @throws SQLException

	  */

	  public boolean isClosed()  throws SQLException

	  {

	    return _conn.isClosed();

	  }

	  /**

	  * 取得该Connection的数据库的metadata

	  * @return DatabaseMetaData

	  * @throws SQLException

	  */

	  public DatabaseMetaData getMetaData() throws SQLException

	  {

	    return _conn.getMetaData();

	  }

	  /**

	  * 将连接设置为只读状态

	  * @param flag flag

	  * @throws SQLException

	  */

	  public void setReadOnly(boolean flag)

	  throws SQLException

	  {

	    _conn.setReadOnly(flag);

	  }

	  /**

	  * 判断连接是否为只读

	  * @return 是返回true,否返回false

	  * @throws SQLException

	  */

	  public boolean isReadOnly()

	  throws SQLException

	  {

	    return _conn.isReadOnly();

	  }

	  /**

	  * 设定当前数据库的catalog名

	  * @param s catalog

	  * @throws SQLException

	  */

	  public void setCatalog(String s)

	  throws SQLException

	  {

	    _conn.setCatalog(s);

	  }

	  /**

	  * 取得当前数据库的catalog名

	  * @return catalog

	  * @throws SQLException

	  */

	  public String getCatalog()

	  throws SQLException

	  {

	    return _conn.getCatalog();

	  }

	  /**

	  * 尝试改变给定事务的隔离层

	  * @param i level

	  * @throws SQLException

	  */

	  public void setTransactionIsolation(int i)

	  throws SQLException

	  {

	    _conn.setTransactionIsolation(i);

	  }

	  /**

	  * 得到当前Connection的事务的隔离层

	  * @return level

	  * @throws SQLException

	  */

	  public int getTransactionIsolation()

	  throws SQLException

	  {

	    return _conn.getTransactionIsolation();

	  }

	  /**

	  * 返回该连接报告的第一个warning

	  * @return SQLWarning

	  * @throws SQLException

	  */

	  public SQLWarning getWarnings()

	  throws SQLException

	  {

	    return _conn.getWarnings();

	  }

	  /**

	  * 清除该Connection对象报告的的所有warning

	  * @throws SQLException

	  */

	  public void clearWarnings()

	  throws SQLException

	  {

	    _conn.clearWarnings();

	  }
	  /**
	   * 判断是否纪录性能日志
	   * @return boolean
	   */
	    public boolean isAnalyse(){
	   if(_source!=null)
	   return _source.isAnalyse();
	   else return false;
	  }

	  /**

	  * 建立一个根据给定的type和concurrency生成ResultSet对象的Statment对象

	  * @param i resultSetType

	  * @param j resultSetConcurrency

	  * @return Statment

	  * @throws SQLException

	  */

	  public Statement createStatement(int i, int j)

	  throws SQLException

	  {
//		//添加性能分析测试 5/17
//			if(this.isAnalyse()==true){
//			    PerformanceManager pm=PerformanceManager.getInstance();
//			    PerformanceLog pl=pm.getPerformanceLog(Thread.currentThread().getName());
//				pl.addElement("statement created at:"+new java.lang.Long(System.currentTimeMillis()).toString());
//			}
	    return _conn.createStatement(i, j);

	  }

	  /**

	  * 建立一个向给定的type和concurrency生成ResultSet对象的PreparedStatment对象

	  * @param s SQL

	  * @param i resultSetType

	  * @param j resultSetConcurrency

	  * @return PreparedStatment

	  * @throws SQLException

	  */

	  public PreparedStatement prepareStatement(String s, int i, int j)

	  throws SQLException

	  {

	    return _conn.prepareStatement(s, i, j);

	  }

	  /**

	  * 建立一个向给定的type和concurrency生成ResultSet对象的CallableStatement对象

	  * @param s SQL

	  * @param i resultSetType

	  * @param j resultSetConcurrency

	  * @return CallableStatement

	  * @throws SQLException

	  */

	  public CallableStatement prepareCall(String s, int i, int j)

	  throws SQLException

	  {

	    return _conn.prepareCall(s, i, j);

	  }

	  /**

	  * 取得连接关联的type map对象

	  * @return type map

	  * @throws SQLException

	  */

	  public Map getTypeMap()

	  throws SQLException

	  {

	    return _conn.getTypeMap();

	  }

	/*  *//**

	  * 设定连接的type map

	  * @param map

	  * @throws SQLException

	  *//*

	  public void setTypeMap(Map map)

	  throws SQLException

	  {

	    _conn.setTypeMap(map);

	  }*/


	  /**

	  * 构造函数，调用this(s, s1, s2, "")<p>

	  * @param s 连接的url

	  * @param s1 连接的用户名

	  * @param s2 连接的密码

	  * @throws SQLException

	  */

	  public DBConnection(String s, String s1, String s2)

	  throws SQLException

	  {

	    this(s, s1, s2, "");

	  }

	  /**

	  * 构造函数，并建立连接，相应设定如下<p>

	  * @param s 连接的url

	  * @param s1 连接的用户名

	  * @param s2 连接的密码

	  * @param p 连接对应的pool

	  * @throws SQLException

	  */

	  public DBConnection(String s, String s1, String s2, DBConnectionPool p)

	  throws SQLException {

	    _conn = null;

	 //   _out = null;

	    _url = s;

	    _user = s1;

	    _pass = s2;

	    _source = p;

	 //   if(p != null) _out = p.getLogWriter();

	    connect();
	    _dbType = _conn.getMetaData().getDatabaseProductName();

	  }

	  /**

	  * 构造函数，并建立连接

	  * @param driver 连接的driver

	  * @param url 连接的url

	  * @param user 连接的用户名

	  * @param pass 连接的密码

	  * @throws SQLException

	  */

	  public DBConnection(String driver, String url , String user, String pass)

	  throws SQLException {

	    _conn = null;
	    _conn.getAutoCommit();

	  //  _out = null;

	    _driver = driver;

	    _url = url;

	    _user = user;

	    _pass = pass;

	    _source =null;

	    connect();

	    _dbType = _conn.getMetaData().getDatabaseProductName();

	  }

	  /**

	  * 方法的具体操作的Connection对象

	  */

	  private Connection _conn;

	  /**

	  * Connection对应的连接池对象

	  */

	  private DBConnectionPool _source;

	  /**

	  * 连接的url

	  */

	  private String _url;

	  /**

	  * 连接的用户名

	  */

	  private String _user;

	  /**

	  * 连接的密码

	  */

	  private String _pass;

	  /**

	  * 连接的driver

	  */

	  private String _driver;

	  /**

	  * 日志记录

	  */

	 // private PrintWriter _out;


	  private String _dbType;

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
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}


	}