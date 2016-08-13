
package com.ccthanking.framework.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


import com.ccthanking.framework.service.BaseService;


public class BaseServiceImpl<T> implements BaseService<T> {


	@Override
	public T findById(int id) {
		return null;
//		return mapper.selectByPrimaryKey(id);
	}

	public T findById(int id,Class example,Class mapper) throws InstantiationException, IllegalAccessException{
		Object instance=example.newInstance();
		return null;
	}

	@Override
	public List<T> find() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<T> find(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int remove(int id) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int remove(List<Integer> ids) {
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
}
