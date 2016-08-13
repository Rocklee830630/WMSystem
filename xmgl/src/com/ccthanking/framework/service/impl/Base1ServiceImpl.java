package com.ccthanking.framework.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.beanutils.MethodUtils;

import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.service.IBaseService;

public class Base1ServiceImpl<T extends BaseVO, PK extends Serializable> implements IBaseService<T, PK> {

    private Class<T> class1;

    @Override
    public T findById(PK id) {

        Connection conn = null;
        try {

            class1 = getSuperClassGenricType(getClass(), 0);

            T t = class1.newInstance();
            String[] pkMoth = t.getPkFields();
            for (String string : pkMoth) {
                MethodUtils.invokeMethod(t, "setInternal", new Object[] { string, id });
            }

            conn = DBUtil.getConnection();

            return (T) BaseDAO.getVOByPrimaryKey(conn, t);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return null;
    }

    @Override
    public List<T> find() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> find(List<PK> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int remove(PK id) {
        return 0;
    }

    @Override
    public int remove(List<PK> ids) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(T bean) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insert(T bean) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * 
     * 如 GcHtglHtServiceImpl extends Base1ServiceImpl<GcHtglHtVO, String>
     * implements GcHtglHtService
     * 
     * @param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

}
