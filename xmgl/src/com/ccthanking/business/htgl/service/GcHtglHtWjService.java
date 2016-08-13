/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtWjService.java
 * 创建日期： 2013-09-03 上午 09:40:06
 * 功能：    接口：合同文件
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-03 上午 09:40:06  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service;


import com.ccthanking.business.htgl.vo.GcHtglHtWjVO;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.IBaseService;


/**
 * <p> GcHtglHtWjService.java </p>
 * <p> 功能：合同文件 </p>
 *
 * <p><a href="GcHtglHtWjService.java.html"><i>查看源代码</i></a></p>  
 *
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-03
 * 
 */
public interface GcHtglHtWjService extends IBaseService<GcHtglHtWjVO, String> {

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
    
    /**
     * 新增记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String insert(String json, User user) throws Exception;

    /**
     * 修改记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String update(String json, User user) throws Exception;

    /**
     * 删除记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String delete(String json, User user) throws Exception;

}
