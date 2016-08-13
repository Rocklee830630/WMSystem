/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkbmService.java
 * 创建日期： 2013-09-29 上午 07:31:33
 * 功能：    接口：提请款部门
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-29 上午 07:31:33  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl.service;


import java.util.HashMap;

import com.ccthanking.business.zjgl.vo.GcZjglTqkbmVO;
import com.ccthanking.business.zjgl.vo.GcZjglTqkgcbVO;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.IBaseService;


/**
 * <p> GcZjglTqkbmService.java </p>
 * <p> 功能：提请款部门 </p>
 *
 * <p><a href="GcZjglTqkbmService.java.html"><i>查看源代码</i></a></p>  
 *
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-29
 * 
 */
public interface GcZjglTqkGcbService extends IBaseService<GcZjglTqkgcbVO, String> {

	/**
     * 根据条件查询记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryCondition(String json, User user) throws Exception;
    
    String queryGcbTqkMx(String json, User user) throws Exception;
	
	String updateGcbMxzt(String json, User user, HashMap map) throws Exception;

}
