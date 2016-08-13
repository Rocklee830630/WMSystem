
package com.ccthanking.framework.service;

import java.util.List;


public interface BaseService<T> {


	T findById(int id);


	List<T> find();


	List<T> find(List<Integer> ids);


	int remove(int id);


	int remove(List<Integer> ids);


	int update(T bean);
	

	int insert(T bean);
}
