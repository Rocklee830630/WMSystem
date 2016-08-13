/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglZszjXmzszjqkService.java
 * 创建日期： 2013-08-30 上午 12:52:21
 * 功能：    接口：项目征收资金情况
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-08-30 上午 12:52:21  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.business.zjgl.vo.GcZjglZszjXmzszjqkVO;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.IBaseService;

/**
 * <p>
 * GcZjglZszjXmzszjqkService.java
 * </p>
 * <p>
 * 功能：项目征收资金情况
 * </p>
 * 
 * <p>
 * <a href="GcZjglZszjXmzszjqkService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-08-30
 * 
 */
public interface GcZjglZszjXmzszjqkService extends IBaseService<GcZjglZszjXmzszjqkVO, String> {

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
     * 查询下达项目列表. <br>
     * 用于弹出选 择
     * 
     * @param json
     * @param user
     * @param yjxm
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryXdxmk(String json, User user, String yjxm) throws Exception;

    /**
     * 查询按项目分组.
     * 
     * @param msg
     * @param user
     * @return
     * @since v1.00
     * @see T
     */
    String queryConditionGroup(String json, User user) throws Exception;;

    String queryXdxmk(String json, HttpServletRequest request) throws Exception;

    /**
     * 查询使用金额合计
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     */
    String querySumJe(String json, User user) throws Exception;

    /**
     * 提供公共查询.<br/>
     * 
     * @param msg
     * @param user
     * @param map
     * @return
     * @since v1.00
     * @see T
     */
    String queryCommon(String msg, User user, HashMap map);

}
