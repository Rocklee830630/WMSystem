package com.ccthanking.framework.common.cache;

/**
 * <p>Title: </p>
 * <p>Description: Cache object interface . </p>
 */

public interface Cache {

  /**
   * update cache use data ,
   *
   * @param data cache object independant data to be send to cache .
   * may be the primary key of database Table ,or a vo of row .
   * @param action : CacheManager.ADD OR CacheManager.UPDATE or CacheManager.DELETE
   */

	public void synchronize(String data, int action) throws Exception;

	public void reBuildMemory() throws Exception;
	
}