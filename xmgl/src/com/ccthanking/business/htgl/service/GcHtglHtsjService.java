/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtsjService.java
 * 创建日期： 2013-09-02 上午 08:02:33
 * 功能：    接口：合同数据
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-02 上午 08:02:33  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service;

import java.util.HashMap;
import java.util.Map;

import com.ccthanking.business.htgl.vo.GcHtglHtsjVO;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.IBaseService;

/**
 * <p>
 * GcHtglHtsjService.java
 * </p>
 * <p>
 * 功能：合同数据
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtsjService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-02
 * 
 */
public interface GcHtglHtsjService extends IBaseService<GcHtglHtsjVO, String> {

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
     * 查询单合同标段数据列表.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryOne(String json, User user) throws Exception;
    
    /**
     * 更新合同数据情况
     * 1.更新合同支付、合同完成投资、合同变更
	 * 2.更新合同数据  最新合同价
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     */
    String updateHtsj(String json, User use, HashMap map) throws Exception;
    
    /**
     * 查询投资控制动态分析数据
     * 
     * @param json
     * @param user
     * @throws Exception
     */
    String queryTzkzdtfx(String json, User user,HashMap map) throws Exception;
    
    /**
     * 获取项目的合同信息、拦标价信息、概算信息
     * @param json
     * @param user
     * @return
     * @throws Exception
     */
    String queryXmxgje(String json, User user) throws Exception;
    
    /**
     * 获取项目投资明细分析数据
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     */
    String queryXmtzmxfx(String json, User user) throws Exception;
    
   /**
    * 资金信息卡-执行情况
    * @param json
    * @param user
    * @param map
    * @return
    * @throws Exception
    */
    String queryTzxxk_zxqk(String json, User user, HashMap map) throws Exception;
    
    /**
     * 资金信息卡-支付情况
     * @param json
     * @param user
     * @param map
     * @return
     * @throws Exception
     */
     String queryTzxxk_zfqk(String json, User user, HashMap map) throws Exception;
     
     /**
      * 资金信息卡-总支付情况统计
      * @param json
      * @param user
      * @param map
      * @return
      * @throws Exception
      */
     String queryTzxxk_zjzfqktj(String json, User user, HashMap map) throws Exception;
     
     /**
      * 资金信息卡-总支付情况统计
      * @param json
      * @param user
      * @param map
      * @return
      * @throws Exception
      */
     String queryTzxxk_zjzfqktj_zsn(String json, User user, HashMap map) throws Exception;
     
     /**
      * 资金信息卡-总支付情况统计
      * @param json
      * @param user
      * @param map
      * @return
      * @throws Exception
      */
     String queryTzxxk_zjzfqktj_bnd(String json, User user, HashMap map) throws Exception;
}
