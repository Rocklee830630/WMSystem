/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtWctzService.java
 * 创建日期： 2013-09-05 下午 06:12:53
 * 功能：    接口：合同完成投资
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-05 下午 06:12:53  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service;

import java.util.HashMap;

import com.ccthanking.business.htgl.vo.GcHtglHtWctzVO;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.IBaseService;

/**
 * <p>
 * GcHtglHtWctzService.java
 * </p>
 * <p>
 * 功能：合同完成投资
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtWctzService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-05
 * 
 */
public interface GcHtglHtWctzService extends IBaseService<GcHtglHtWctzVO, String> {

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

    /**
     * 查询单合同完成投资情况.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     * @see T
     */
    String queryOne(String json, User user) throws Exception;

    /**
     * 通过计量录入完成投资
     * @param user
     * @param jhsjid
     * @param map
     * map内容：
     * NF:年份
     * YF:月份
     * JE:完成投资金额
     * @return
     * @throws Exception
     */
    String insertFromJL(User user, String jhsjid , String opttype ,HashMap map) throws Exception;
}
