package com.ccthanking.framework.common.dataset;

/**
 * <p>Title: UniEAP V1.7</p>
 * <p>Description: 通用企业应用平台V1.7</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 沈阳东软软件股份有限公司</p>
 * @author 邹红宇 zhou_hy@neusoft.com
 * @version 1.7
 */

/*******************************************************************
 * function   :数据存储单位，代表结构集中每个字段的值，用于DataSet内。the data storage of database. represent the data of one line
 * build time :2003-04-15
 * changes    :
 *******************************************************************/

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import com.ccthanking.framework.common.OPException;

public interface DataObj extends Serializable
{
    //DataObj的状态常量
    //const for dataObj status
    public static final String STATUS_UNCHANGED="UNCHANGED";
    public static final String STATUS_INSERTED="INSERTED";
    public static final String STATUS_DELETED="DELETED";
    public static final String STATUS_UPDATED="UPDATED";
    public static final String STATUS_CANCELED="CANCELED";

    //DataObj的持久化状态常量
    //const for dataObj persistent status
    public static final String PERSISTENT_NOT_START="PERSISTENT_NOT_START";
    public static final String PERSISTENT_SUCCESSED="PERSISTENT_SUCCESSED";
    public static final String PERSISTENT_FAILED="PERSISTENT_FAILED";

    /**
     * 设置该记录为删除状态
     * set the status to delete
     */
    public void setDelete();

    /**
     * 设置持久化状态
     * set persistent status
     * @param status 取值为DataObj的持久化状态常量
     */
    public void setPersistentStatus(String persistentStatus);

    /**
     * 取得该记录的状态
     * Retrieves the status of this DataObj.
     * @return DataObj的状态常量
     */
    public String getStatus();

    /**
     * 取得持久化状态
     * Retrieves the persistent status of this DataObj.
     * @return DataObj的持久化状态常量
     */
    public String getPersistentStatus();

    /**
     * 接受持久化，将该行记录与数据库中的同步
     * persistentStatus->persistent not start
     * insert,update->unchanged
     * delete,canceled->canceled
     */
    public void acceptChanges();

    /**
     * 取消该行记录的修改
     * Cancels the changes made to the current row.
     * insert,cancel->cancel
     * unchanged,delete,update->unchanged
     */
    public void cancelUpdates();

    /**
     * 取得总列数
     * get column count
     * @return
     */
    public int getColumnCount();

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a String in the Java programming language.
     * @param colIndex
     * @return
     */
    public String getString(int colIndex);

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a String in the Java programming language.
     * @param name
     * @return
     */
    public String getString(String name);

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a boolean in the Java programming language.
     * @param colIndex
     * @return
     * @throws OPException
     */
    public boolean getBoolean(int colIndex) throws OPException;

    /**
     *
     * Retrieves the value of the designated column in the current row of this DataSet as a boolean in the Java programming language.
     * @param name
     * @return
     * @throws OPException
     */
    public boolean getBoolean(String name) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as an int in the Java programming language.
     * @param colIndex
     * @return
     * @throws OPException
     */
    public int getInt(int colIndex) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as an int in the Java programming language.
     * @param name
     * @return
     * @throws OPException
     */
    public int getInt(String name) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a long in the Java programming language.
     * @param colIndex
     * @return
     * @throws OPException
     */
    public long getLong(int colIndex) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a long in the Java programming language.
     * @param name
     * @return
     * @throws OPException
     */
    public long getLong(String name) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a float in the Java programming language.
     * @param colIndex
     * @return
     * @throws OPException
     */
    public float getFloat(int colIndex) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a float in the Java programming language.
     * @param name
     * @return
     * @throws OPException
     */
    public float getFloat(String name) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a double in the Java programming language.
     * @param colIndex
     * @return
     * @throws OPException
     */
    public double getDouble(int colIndex) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a double in the Java programming language.
     * @param name
     * @return
     * @throws OPException
     */
    public double getDouble(String name) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a java.math.BigDecimal with full precision.
     * @param colIndex
     * @return
     * @throws OPException
     */
    public BigDecimal getBigDecimal(int colIndex) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a java.math.BigDecimal with full precision.
     * @param name
     * @return
     * @throws OPException
     */
    public BigDecimal getBigDecimal(String name) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a java.sql.Timestamp object in the Java programming language.
     * @param colIndex
     * @return
     * @throws OPException
     */
    public Timestamp getTimestamp(int colIndex) throws OPException;

    /**
     * 取指定列的值
     * Retrieves the value of the designated column in the current row of this DataSet as a java.sql.Timestamp object.
     * @param name
     * @return
     * @throws OPException
     */
    public Timestamp getTimestamp(String name) throws OPException;

    /**
     * 取指定列的值
     * Gets the value of the designated column.
     * @param index
     * @return new value if this dataItem has been modified, original value if not.
     */
    public Object getObject(int index);

    /**
     * 取指定列的值
     * Gets the value of the designated column name.
     * @param name
     * @return 如果被修改，返回新值，否则返回原值。new value if this dataItem has been modified, original value if not.
     */
    public Object getObject(String name);

    /**
     * 取指定列的原值
     * Gets the original value of the designated column.
     * @param index
     * @return
     */
    public Object getOriObject(int index);

    /**
     * 取指定列的原值
     * Gets the original value of the designated column name.
     * @param name
     * @return
     */
    public Object getOriObject(String name);

    /**
     * 更新指定列的数值
     * Updates the designated column with an Object value.
     * @param index
     * @param obj
     * @return
     */
    public boolean updateObject(int index,Object obj);

    /**
     * 更新指定列的数值
     * Updates the designated column with an Object value.
     * @param name
     * @param obj
     * @return
     */
    public boolean updateObject(String name,Object obj);

    public boolean updateOriObject(int index,Object obj);

    public boolean updateOriObject(String name,Object obj);

    /**
     * 指定列是否被修改
     * Retrieves whether the designated column has been updated.
     * @param index
     * @return
     */
    public boolean isModefied(int index);

    /**
     * 指定列是否被修改
     * Retrieves whether the designated column has been updated.
     * @param name
     * @return
     */
    public boolean isModefied(String name);

    /**
     * 取得所有被修改的列编号
     * Retrieves all modified column indexes.
     * @return
     */
    public ArrayList getAllModifiedColIndex();

    /**
     * 取得所有被修改的列名
     * Retrieves all modified column names.
     * @return
     */
    public ArrayList getAllModifiedColName();

    /**
     * 清空错误记录
     * clear error message
     */
    public void clearErrorMsg();

    /**
     * 增加错误记录
     * set error message
     * @param errorMsg
     */
    public void addErrorMsg(String errorMsg);

    /**
     * 取得错误记录
     * get error message in persistence
     * @return
     */
    public String getErrorMsg();

}
