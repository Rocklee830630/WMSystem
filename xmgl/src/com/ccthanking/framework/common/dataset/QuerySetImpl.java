package com.ccthanking.framework.common.dataset;

/**
 * <p>Title: ppx</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
import java.math.*;
import java.sql.*;
import java.util.*;

import com.ccthanking.framework.common.OPException;
import com.ccthanking.framework.common.QuerySet;
public class QuerySetImpl implements QuerySet
{
    protected String sql; //sql语句。sql statement
    protected Object[] paras; //prepared参数。parameters for prepared sql statement
    protected ArrayList dataObjs = new ArrayList(); //数据。datas
    protected HashMap attrMap = new HashMap(); //列名－列位置对照表。mapping of attribute name->column index
    protected HashMap columnType = new HashMap();//取列类型

    protected int pageSize=15; //每页最大允许记录数。pageSize
    protected int pageNo=1; //当前页码。current page number

    /**
     * 保留默认的构造器，使得可以直接灵活构造对象
     */
    public QuerySetImpl(){
   }
    /**
     * 构造函数
     * @param sql
     * @param paras
     * @param dataObjs
     * @param attrMap
     * @param rsCount
     */
    public QuerySetImpl(String sql,Object[] paras,ArrayList dataObjs,HashMap attrMap,HashMap columnType)
    {
        this.sql=sql;
        this.paras=paras;
        this.dataObjs=dataObjs;
        this.attrMap=attrMap;
        this.columnType = columnType;
        if(this.dataObjs == null) this.dataObjs =  new ArrayList();
        if(this.attrMap == null) this.attrMap =  new HashMap();
        if(this.columnType == null) this.columnType = new HashMap();
    }


    public String getSql()
    {
        return sql;
    }

    public Object[] getParas()
    {
        return paras;
    }

    public DataObj getDataObj(int rowIndex)
    {
        return(DataObj)dataObjs.get(rowIndex-1);
    }

    public ArrayList getDataObjs()
    {
        return dataObjs;
    }

    public HashMap getAttrMap()
    {
        return attrMap;
    }

    public int getRowCount()
    {
        if(dataObjs == null) return 0;
        return dataObjs.size();
    }

    public int getColumnCount()
    {
        if(attrMap == null) return 0;
        return attrMap.size();
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public int getType(int column)
    {
        Object type=this.columnType.get(new Integer(column));
        if(type==null)
            throw new RuntimeException(OPException.NO_SUCH_ATTRIBUTE_EXISTS);

        return((Integer)type).intValue();
    }

    public String getString(int rowIndex,int colIndex)
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getString(colIndex);
    }

    public String getString(int rowIndex,String name)
    {
        return getString(rowIndex,getIndex(name));
    }

    public boolean getBoolean(int rowIndex,int colIndex) throws OPException
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getBoolean(colIndex);
    }

    public boolean getBoolean(int rowIndex,String name) throws OPException
    {
        return getBoolean(rowIndex,getIndex(name));
    }

    public int getInt(int rowIndex,int colIndex) throws OPException
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getInt(colIndex);
    }

    public int getInt(int rowIndex,String name) throws OPException
    {
        return getInt(rowIndex,getIndex(name));
    }

    public long getLong(int rowIndex,int colIndex) throws OPException
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getLong(colIndex);
    }

    public long getLong(int rowIndex,String name) throws OPException
    {
        return getLong(rowIndex,getIndex(name));
    }

    public float getFloat(int rowIndex,int colIndex) throws OPException
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getFloat(colIndex);
    }

    public float getFloat(int rowIndex,String name) throws OPException
    {
        return getFloat(rowIndex,getIndex(name));
    }

    public double getDouble(int rowIndex,int colIndex) throws OPException
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getDouble(colIndex);
    }

    public double getDouble(int rowIndex,String name) throws OPException
    {
        return getDouble(rowIndex,getIndex(name));
    }

    public BigDecimal getBigDecimal(int rowIndex,int colIndex) throws OPException
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getBigDecimal(colIndex);
    }

    public BigDecimal getBigDecimal(int rowIndex,String name) throws OPException
    {
        return getBigDecimal(rowIndex,getIndex(name));
    }

    public Timestamp getTimestamp(int rowIndex,int colIndex) throws OPException
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getTimestamp(colIndex);
    }

    public Timestamp getTimestamp(int rowIndex,String name) throws OPException
    {
        return getTimestamp(rowIndex,getIndex(name));
    }

    public Object getObject(int rowIndex,int colIndex)
    {
        return((DataObj)dataObjs.get(rowIndex-1)).getObject(colIndex);
    }

    public Object getObject(int rowIndex,String name)
    {
        return getObject(rowIndex,getIndex(name));
    }

    /**
     * 取列名所对应的列位置
     * @param attrName
     * @return
     * @throws RuntimeException
     */
    protected int getIndex(String attrName) throws RuntimeException
    {
        Object index=attrMap.get(attrName.toLowerCase());
        if(index==null)
            throw new RuntimeException(OPException.NO_SUCH_ATTRIBUTE_EXISTS);

        return((Integer)index).intValue();
    }

}
