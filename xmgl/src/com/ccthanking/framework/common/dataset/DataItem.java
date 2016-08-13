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
import java.io.*;

public interface DataItem extends Serializable
{
    /**
     * 设为只读
     * set readOnly
     * @param readOnly
     */
    public void setReadOnly(boolean readOnly);

    /**
     * 设置新数值，状态值改为修改
     * set new value, status change to modified
     * @param newValue
     * @return 如果为只读，返回false。false if the value is not updatable
     */
    public boolean setNewValue(Object newValue);

    public boolean setOriValue(Object oriValue);

    /**
     * 取原值
     * get original value
     * @return original value
     */
    public Object getValue();

    /**
     * 取新值
     * get new value
     * @return new value
     */
    public Object getNewValue();

    /**
     * 是否为只读
     * if this dataItem is readOnly
     * @return
     */
    public boolean isReadOnly();

    /**
     * 是否已被修改
     * if this dataItem has been modified
     * @return
     */
    public boolean isModified();

    /**
     * 接受持久化，将该点记录与数据库中的同步
     * value->newValue
     * newValue->null
     * modified->false
     */
    public void acceptChanges();

    /**
     * 取消修改，状态恢复
     * cancel changed, status back to unmodified
     */
    public void cancelUpdate();
}
