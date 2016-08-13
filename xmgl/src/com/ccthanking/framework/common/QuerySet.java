package com.ccthanking.framework.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.ccthanking.framework.common.dataset.DataObj;
public interface QuerySet extends Serializable
{
    /**
     * 取sql表达式
     * get select sql of this DataSet
     * @return
     */
    public String getSql();

    /**
     * 取参数
     * get parameters of this DataSet's prepared sql statement
     * @return
     */
    public Object[] getParas();

    /**
     * 取一行记录
     * get DataObj
     * @param rowIndex
     * @return
     */
    public DataObj getDataObj(int rowIndex);

    /**
     * 取所有记录
     * get all DataObjs
     * @return
     */
    public ArrayList getDataObjs();

    /**
     * 取列名－列位置对照表
     * get mapping of attribute->column index
     * @return
     */
    public HashMap getAttrMap();

    /**
     * 取放入内存中的记录数
     * get number of rows
     * @return
     */
    public int getRowCount();

    /**
     * 取列总数
     * get number of columns
     * @return
     */
    public int getColumnCount();

    /**
     * 取每页允许最大记录数
     * get page size
     * @return
     */
    public int getPageSize();

    /**
     * 取当前页数
     * get page number
     * @return
     */
    public int getPageNo();

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a String in the Java programming language.
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public String getString(int rowIndex,int colIndex);

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a String in the Java programming language.
     * @param rowIndex
     * @param name
     * @return
     */
    public String getString(int rowIndex,String name);

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a boolean in the Java programming language.
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws OPException
     */
    public boolean getBoolean(int rowIndex,int colIndex) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a boolean in the Java programming language.
     * @param rowIndex
     * @param name
     * @return
     * @throws OPException
     */
    public boolean getBoolean(int rowIndex,String name) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as an int in the Java programming language.
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws OPException
     */
    public int getInt(int rowIndex,int colIndex) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as an int in the Java programming language.
     * @param rowIndex
     * @param name
     * @return
     * @throws OPException
     */
    public int getInt(int rowIndex,String name) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a long in the Java programming language.
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws OPException
     */
    public long getLong(int rowIndex,int colIndex) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a long in the Java programming language.
     * @param rowIndex
     * @param name
     * @return
     * @throws OPException
     */
    public long getLong(int rowIndex,String name) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a float in the Java programming language.
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws OPException
     */
    public float getFloat(int rowIndex,int colIndex) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a float in the Java programming language.
     * @param rowIndex
     * @param name
     * @return
     * @throws OPException
     */
    public float getFloat(int rowIndex,String name) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a double in the Java programming language.
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws OPException
     */
    public double getDouble(int rowIndex,int colIndex) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a double in the Java programming language.
     * @param rowIndex
     * @param name
     * @return
     * @throws OPException
     */
    public double getDouble(int rowIndex,String name) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a java.math.BigDecimal with full precision.
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws OPException
     */
    public BigDecimal getBigDecimal(int rowIndex,int colIndex) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a java.math.BigDecimal with full precision.
     * @param rowIndex
     * @param name
     * @return
     * @throws OPException
     */
    public BigDecimal getBigDecimal(int rowIndex,String name) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a java.sql.Timestamp object in the Java programming language.
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws OPException
     */
    public Timestamp getTimestamp(int rowIndex,int colIndex) throws OPException;

    /**
     * 取指定字段的值
     * Retrieves the value of the designated column in the current row of this DataSet as a java.sql.Timestamp object.
     * @param rowIndex
     * @param name
     * @return
     * @throws OPException
     */
    public Timestamp getTimestamp(int rowIndex,String name) throws OPException;

    /**
     * 取指定字段的值
     * Gets the value of the designated column in the current row of this DataSet as an Object in the Java programming language.
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public Object getObject(int rowIndex,int colIndex);

    /**
     * 取指定字段的值
     * Gets the value of the designated column in the current row of this DataSet as an Object in the Java programming language.
     * @param rowIndex
     * @param name
     * @return
     */
    public Object getObject(int rowIndex,String name);
    /**
     * 取字段类型
     * Gets the value of the designated column in the current row of this DataSet as an Object in the Java programming language.
     * @param columnType
     * @return
     */
    public int getType(int column);
}
