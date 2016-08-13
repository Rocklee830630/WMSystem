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
 * function   :DataObj的实现，见接口DataObj。the implement of DataObj, reference please look at inteferface DataObj.
 * build time :2003-04-15
 * changes    :
 *******************************************************************/

import java.math.*;
import java.sql.*;
import java.util.*;

import com.ccthanking.framework.common.OPException;
public class DataObjImpl implements DataObj
{
    private ArrayList dataItems; //数据。dataItems
    private HashMap attrMap; //字段名，位置对应表。mapping of attribute name->column index
    private String status; //状态。status, including DataObj.STATUS_UNCHANGED,DataObj.STATUS_INSERTED,DataObj.STATUS_DELETED,DataObj.STATUS_UPDATED,STATUS_CANCELED.
    private String persistentStatus; //持久化状态。persistent status, including PERSISTENT_NOT_START,PERSISTENT_SUCCESSED,PERSISTENT_FAILED

    private ArrayList errorMsg; //错误信息。error message in persistence

    /**
     * 构造函数
     * @param dataItems
     * @param attrMap
     */
    public DataObjImpl(ArrayList dataItems,HashMap attrMap)
    {
        this.dataItems=dataItems;
        this.attrMap=attrMap;
        this.status=DataObj.STATUS_UNCHANGED;
        this.persistentStatus=DataObj.PERSISTENT_NOT_START;
    }

    /**
     * 构造函数，DataSet中新增记录用
     * @param dataItems
     * @param attrMap
     * @param insert
     */
    public DataObjImpl(ArrayList dataItems,HashMap attrMap,boolean insert)
    {
        this.dataItems=dataItems;
        this.attrMap=attrMap;
        if(insert)
            this.status=DataObj.STATUS_INSERTED;
        else
            this.status=DataObj.STATUS_UNCHANGED;
        this.persistentStatus=DataObj.PERSISTENT_NOT_START;
    }

    public void setDelete()
    {
        if(status==DataObj.STATUS_INSERTED||status==DataObj.STATUS_CANCELED)
            status=DataObj.STATUS_CANCELED;
        else
            status=DataObj.STATUS_DELETED;
    }

    public void setPersistentStatus(String persistentStatus)
    {
        this.persistentStatus=persistentStatus;
    }

    public String getStatus()
    {
        return status;
    }

    public String getPersistentStatus()
    {
        return persistentStatus;
    }

    public void acceptChanges()
    {
        if(status==DataObj.STATUS_INSERTED||status==DataObj.STATUS_UPDATED)
        {
            for(int i=0;i<dataItems.size();i++)
            {
                ((DataItem)dataItems.get(i)).acceptChanges();
            }
            status=DataObj.STATUS_UNCHANGED;
        }
        if(status==DataObj.STATUS_DELETED)
            status=DataObj.STATUS_CANCELED;

        persistentStatus=DataObj.PERSISTENT_NOT_START;
    }

    public void cancelUpdates()
    {
        if(status==DataObj.STATUS_INSERTED)
            status=DataObj.STATUS_CANCELED;
        if(status==DataObj.STATUS_UPDATED)
        {
            for(int i=0;i<dataItems.size();i++)
            {
                ((DataItem)dataItems.get(i)).cancelUpdate();
            }
            status=DataObj.STATUS_UNCHANGED;
        }
        if(status==DataObj.STATUS_DELETED)
        {
            for(int i=0;i<dataItems.size();i++)
            {
                ((DataItem)dataItems.get(i)).cancelUpdate();
            }
            status=DataObj.STATUS_UNCHANGED;
        }

        persistentStatus=DataObj.PERSISTENT_NOT_START;
    }

    public void setReadOnly(int index,boolean readOnly)
    {
        DataItem dataItem=(DataItem)dataItems.get(index-1);
        dataItem.setReadOnly(readOnly);
    }

    public int getColumnCount()
    {
        return attrMap.size();
    }

//  the column value; if the value is SQL NULL, the value returned is null
    public String getString(int colIndex)
    {
        Object obj=getObject(colIndex);
        if(obj==null)
            return null;

        return obj.toString();
    }

    public String getString(String name)
    {
        return getString(getIndex(name));
    }

//  the column value; if the value is SQL NULL, the value returned is false
    public boolean getBoolean(int colIndex) throws OPException{
        Object obj = getObject(colIndex);
        if(obj == null){
            return false;
        }
        try{
            return((Boolean)obj).booleanValue();
        } catch(Exception e){
            throw new OPException(OPException.PARSING_ERROR, e);
        }
    }

    public boolean getBoolean(String name) throws OPException
    {
        return getBoolean(getIndex(name));
    }

//  the column value; if the value is SQL NULL, the value returned is 0
    public int getInt(int colIndex) throws OPException
    {
        Object obj=getObject(colIndex);

        if(obj == null){
            return 0 ;
        }

        try{
            return Integer.parseInt((String)obj);
        } catch(Exception e){
            throw new OPException(OPException.PARSING_ERROR, e);
        }
    }

    public int getInt(String name) throws OPException
    {
        return getInt(getIndex(name));
    }

//  the column value; if the value is SQL NULL, the value returned is 0
    public long getLong(int colIndex) throws OPException
    {
        Object obj=getObject(colIndex);
        if(obj == null){
            return 0;
        }

        try{
            return Long.parseLong((String)obj);
            //return((Number)obj).longValue();
        } catch(Exception e){
            throw new OPException(OPException.PARSING_ERROR,e);
        }
    }

    public long getLong(String name) throws OPException
    {
        return getLong(getIndex(name));
    }

//  the column value; if the value is SQL NULL, the value returned is 0
    public float getFloat(int colIndex) throws OPException
    {
        Object obj=getObject(colIndex);
        if(obj == null){
            return 0;
        }
        try
        {
            return Float.parseFloat((String)obj);
           // return((Number)obj).floatValue();
        }
        catch(Exception e)
        {
            throw new OPException(OPException.PARSING_ERROR,e);
        }
    }

    public float getFloat(String name) throws OPException
    {
        return getFloat(getIndex(name));
    }

//  the column value; if the value is SQL NULL, the value returned is 0
    public double getDouble(int colIndex) throws OPException
    {
        Object obj=getObject(colIndex);
        if(obj == null){
            return 0;
        }
        try
        {
            return Double.parseDouble((String)obj);
            //return((Number)obj).doubleValue();
        }
        catch(Exception e)
        {
            throw new OPException(OPException.PARSING_ERROR,e);
        }
    }

    public double getDouble(String name) throws OPException
    {
        return getDouble(getIndex(name));
    }

//  the column value (full precision); if the value is SQL NULL, the value returned is null in the Java programming language.
    public BigDecimal getBigDecimal(int colIndex) throws OPException
    {
        Object obj=getObject(colIndex);
        if(obj==null){
            return null;
        }
        try
        {
            return(BigDecimal)obj;
        }
        catch(Exception e)
        {
            throw new OPException(OPException.PARSING_ERROR,e);
        }
    }

    public BigDecimal getBigDecimal(String name) throws OPException
    {
        return getBigDecimal(getIndex(name));
    }

// the column value; if the value is SQL NULL, the value returned is null
    public Timestamp getTimestamp(int colIndex) throws OPException
    {
        Object obj=getObject(colIndex);
        if(obj == null){
            return null;
        }

        try
        {
            return(Timestamp)obj;
        }
        catch(Exception e)
        {
            throw new OPException(OPException.PARSING_ERROR,e);
        }
    }

    public Timestamp getTimestamp(String name) throws OPException
    {
        return getTimestamp(getIndex(name));
    }

    public Object getObject(int index)
    {
        DataItem dataItem=(DataItem)dataItems.get(index-1);

        if(dataItem.isModified())
            return dataItem.getNewValue();
        return dataItem.getValue();
    }

    public Object getObject(String name)
    {
        return getObject(getIndex(name));
    }

    public Object getOriObject(int index)
    {
        DataItem dataItem=(DataItem)dataItems.get(index-1);

        return dataItem.getValue();
    }

    public Object getOriObject(String name)
    {
        return getOriObject(getIndex(name));
    }

    public boolean updateObject(int index,Object obj)
    {
        DataItem dataItem=(DataItem)dataItems.get(index-1);
        if(!dataItem.setNewValue(obj))
            return false;

        if(status==DataObj.STATUS_UNCHANGED)
            status=DataObj.STATUS_UPDATED;

        persistentStatus=DataObj.PERSISTENT_NOT_START;

        return true;
    }

    public boolean updateObject(String name,Object obj)
    {
        return updateObject(getIndex(name),obj);
    }

    public boolean updateOriObject(int index,Object obj)
    {
        DataItem dataItem=(DataItem)dataItems.get(index-1);
        dataItem.setOriValue(obj);

        return true;
    }

    public boolean updateOriObject(String name,Object obj)
    {
         return updateOriObject(getIndex(name),obj);
    }

    public boolean isModefied(int index)
    {
        DataItem dataItem=(DataItem)dataItems.get(index-1);
        return dataItem.isModified();
    }

    public boolean isModefied(String name)
    {
        return isModefied(getIndex(name));
    }

    public ArrayList getAllModifiedColIndex()
    {
        ArrayList result=new ArrayList();

        String name;
        for(Iterator e=attrMap.keySet().iterator();e.hasNext();)
        {
            name=e.next().toString();
            if(isModefied(name))
                result.add(new Integer(getIndex(name)));
        }

        return result;
    }

    public ArrayList getAllModifiedColName()
    {
        ArrayList result=new ArrayList();

        for(Iterator e=attrMap.keySet().iterator();e.hasNext();)
        {
            String name=e.next().toString();
            if(isModefied(name))
                result.add(name);
        }

        return result;
    }

    public void clearErrorMsg()
    {
        errorMsg=null;
    }

    public void addErrorMsg(String errorMsg)
    {
        if(this.errorMsg==null)
            this.errorMsg=new ArrayList();

        this.errorMsg.add(errorMsg);
    }

    public String getErrorMsg()
    {
        if(errorMsg==null||errorMsg.size()==0)
            return null;

        StringBuffer sb=new StringBuffer();
        for(int i=0;i<errorMsg.size();i++)
        {
            sb.append(errorMsg.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * 根据列名取得列的位置
     * get index of designate attribute name
     * @param attrName
     * @return
     * @throws RuntimeException
     */
    private int getIndex(String attrName) throws RuntimeException
    {
        Object obj=attrMap.get(attrName.toLowerCase());
        if(obj==null)
            throw new RuntimeException(OPException.NO_SUCH_ATTRIBUTE_EXISTS);

        return((Integer)obj).intValue();
    }
}
