/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkbmmxService.java
 * 创建日期： 2013-09-29 上午 07:27:48
 * 功能：    接口：提请款部门明细
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-29 上午 07:27:48  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl.service;

import java.util.HashMap;

import com.ccthanking.business.zjgl.vo.GcZjglTqkbmmxVO;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.IBaseService;

/**
 * <p>
 * GcZjglTqkbmmxService.java
 * </p>
 * <p>
 * 功能：提请款部门明细
 * </p>
 * 
 * <p>
 * <a href="GcZjglTqkbmmxService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-29
 * 
 */
public interface GcZjglTqkbmmxService extends IBaseService<GcZjglTqkbmmxVO, String> {

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
     * 批量修改记录.
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
     * 部门提请款明细,用于上下部移动选择.
     * 
     * @param json
     * @param user
     * @param map
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryConditionBMMX(String json, User user, HashMap map) throws Exception;
    
    /**
     * 部门提请款明细(工程监理类需过滤工程部审核),用于上下部移动选择.
     * 
     * @param json
     * @param user
     * @param map
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryConditionBMMXdisGcb(String json, User user, HashMap map) throws Exception;

    /**
     * 查询某合同的已拔付.<br/>
     * 
     * @param json
     * @param user
     * @param map
     * @return
     * @throws Exception
     * @since v1.00
     * @see T
     */
    String queryYbfByHt(String json, User user, HashMap map) throws Exception;

    /**
     * 查询已拔付明细.<br/>
     * 用于征收资金管理选择与些关联.
     * 
     * @param msg
     * @param user
     * @param map
     * @return
     * @since v1.00
     * @see T
     */
    String queryConditionYbfBMMX(String msg, User user, HashMap map) throws Exception ;

    /**
     * 查询提请款部门明细相关计量数据
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     */
    String queryJlsj(String json, User user) throws Exception;
    
    /**
     * 查询提请款部门明细排迁类合同相关审定值
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     */
    String queryPqsdz(String json, User user) throws Exception;
}
