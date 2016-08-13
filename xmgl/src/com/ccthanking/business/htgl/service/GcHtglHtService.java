/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtService.java
 * 创建日期： 2013-09-02 上午 08:08:02
 * 功能：    接口：合同信息
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-02 上午 08:08:02  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;

import com.ccthanking.business.htgl.vo.GcHtglHtVO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.service.IBaseService;

/**
 * <p>
 * GcHtglHtService.java
 * </p>
 * <p>
 * 功能：合同信息
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-02
 * 
 */
public interface GcHtglHtService extends IBaseService<GcHtglHtVO, String> {

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
     * 部门合同补存招投标查询
     * @param request
     * @param json
     * @return
     * @throws Exception
     */
    String queryConditionForZtb(String json, User user, HttpServletRequest request) throws Exception;

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
     * 修改合同编号
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */

    String updateHtbh(String json, User user, HttpServletRequest request) throws Exception;

    /**
     * 根据主键修改记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String update(String json, User user, String id, String opttype) throws Exception;

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
     * 查询合同.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryHt(String json, User user) throws Exception;

    /**
     * 查询施工合同列表.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     * @see T
     */
    String querySgHt(String json, User user) throws Exception;

    /**
     * 查询其它信息.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     * @see T
     */
    String queryOther(String json, User user, HashMap map) throws Exception;

    /**
     * 查询合同信息（包含履约保证金）
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     * @see T
     */
    String queryBySJBH(String json, User user) throws Exception;

    /**
     * 将合同更新到已结算状态
     * 
     * @param json
     * @param user
     * @param htid
     * @return
     * @throws Exception
     */
    String updateHtztToYJS(String json, User user, String htid) throws Exception;

    /**
     * 更新合同情况 1.更新最新合同价、变更金额
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     */
    String updateHtZHSJS(String json, User use, HashMap map) throws Exception;

    /**
     * 查询合同造价信息
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryHtzjxx(String json, User user) throws Exception;

    /**
     * 更新合同结算信息
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     */
    String updateHtjs(String json, User user) throws Exception;

    /**
     * 查询合同列表
     * 
     * @param json
     * @param user
     * @param map
     * @return
     * @throws Exception
     */
    String queryHtList(String json, User user, HashMap map) throws Exception;

    /**
     * 更新合同结算价. 选择性更新合同签订价(传入值则更新).
     * 
     * @param user
     * @param htid
     * @param htsjId
     * @param map
     * @return
     * @throws Exception
     */
    String updateHtHtqjjs(User user, String htid, String htsjId, HashMap map) throws Exception;

    /**
     * 查询投资控制动态分析_合同列表.
     * 
     * @param json
     * @param user
     * @param map
     * @return
     * @throws Exception
     */
    String queryTzjkHtList(String json, User user, HashMap map) throws Exception;

    /**
     * 自动完成-查询合同编码.<br/>
     * 
     * @param json
     * @param user
     * @param map
     * @return
     * @throws Exception
     * @since v1.00
     * @see T
     */
    List<autocomplete> htbmAutoQuery(autocomplete json, User user, HashMap map) throws Exception;
    
    /**
     * 查询招投标履约保证金信息
     * 
     * @param map
     * @param user
     * @return
     * @throws Exception
     */
    String queryZtbSjrws(User user, HashMap map) throws Exception;
    
    /**
     * 查询履约保证金信息
     * @param json
     * @param user
     * @throws Exception
     */
    String queryLybzjList(String json,User user,HashMap map) throws Exception;
    
    /**
     * 清楚合同数据（合同修改前数据操作）
     * 1.合同数据
     * 2.合同履约保障金关联
     * 3.排迁子任务关联
     * @param id
     * @param user
     * @param 标段是否改变 String:true or false
     * @return
     * @throws Exception
     */
    String updateHtxxClear(String id, User user, String bdChange) throws Exception;
    
    
    public String queryHtOnlyTqkbm(String json, User user) throws Exception;

	String queryConditionForIndex(String json, User user,Map<String, String> map) throws Exception;
	
	String queryIsCf(User user, HttpServletRequest request) throws Exception;
	
	/**
     * 对合同主体补存招投标信息
     * @param request
     * @param json
     * @return
     * @throws Exception
     */
	String bcZtbxx(HttpServletRequest request) throws Exception;
	
	void deleteHTxm(String json, User user, HttpServletRequest request) throws Exception;
}
