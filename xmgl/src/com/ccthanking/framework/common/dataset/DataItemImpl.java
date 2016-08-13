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
public class DataItemImpl implements DataItem
{
    private Object value; //原值。original value
    private Object newValue; //新值。new value
    private boolean readOnly=false; //是否只读。is readOnly
    private boolean modified=false; //是否被更改。is modified

    /**
     * 构造函数
     * constructor
     * @param value 原值。original value
     */
    public DataItemImpl(Object value)
    {
        this.value=value;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly=readOnly;
    }

    public boolean setNewValue(Object newValue)
    {
        if(isReadOnly())
            return false;

        this.newValue=newValue;
        this.modified=true;
        return true;
    }

    public boolean setOriValue(Object oriValue)
    {
        this.value=oriValue;
        return true;
    }

    public Object getValue()
    {
        return value;
    }

    public Object getNewValue()
    {
        return newValue;
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }

    public boolean isModified()
    {
        return modified;
    }

    public void acceptChanges()
    {
        if(!isModified())
            return;

        value=newValue;
        newValue=null;

        modified=false;
    }

    public void cancelUpdate()
    {
        if(!isModified())
            return;

        newValue=null;
        modified=false;
    }
}
