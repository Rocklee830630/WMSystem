/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglLybzjService.java
 * 创建日期： 2013-08-30 上午 12:42:04
 * 功能：    接口：履约保证金
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-08-30 上午 12:42:04  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl.service;

import java.util.HashMap;

import com.ccthanking.business.zjgl.vo.GcZjglLybzjVO;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.IBaseService;

/**
 * <p>
 * GcZjglLybzjService.java
 * </p>
 * <p>
 * 功能：履约保证金
 * </p>
 * 
 * <p>
 * <a href="GcZjglLybzjService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-08-30
 * 
 */
public interface GcZjglLybzjService extends IBaseService<GcZjglLybzjVO, String> {

    /**
     * 新增记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String insert(String json, User user, HashMap map) throws Exception;

    /**
     * 修改记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String update(String json, User user, HashMap map) throws Exception;

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

    /**
     * 根据条件查询记录.
     * 
     * @param json
     * @param user
     * @param map 
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryCondition(String json, User user, HashMap map) throws Exception;

    
}
