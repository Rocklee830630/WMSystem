/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkbmmxServiceTest.java
 * 创建日期： 2013-09-29 上午 07:27:48
 * 功能：    测试类：提请款部门明细
 * 所含类:   GcZjglTqkbmmxService.java
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-29 上午 07:27:48  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ccthanking.business.zjgl.vo.GcZjglTqkbmmxVO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.test.JsonBinder;
import com.ccthanking.framework.test.ServiceTest;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.RandomGUID;
/**
 * <p> GcZjglTqkbmmxAction.java </p>
 * <p> 功能：提请款部门明细, 测试类. </p>
 *
 * <p><a href="GcZjglTqkbmmxManagerTest.java.html"><i>查看源代码</i></a></p>  
 *
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-29
 * 
 */


public class GcZjglTqkbmmxServiceTest extends ServiceTest{

	private static Logger logger = LoggerFactory.getLogger(GcZjglTqkbmmxServiceTest.class);
	
	@Autowired
    @Qualifier("gcZjglTqkbmmxServiceImpl")
    private GcZjglTqkbmmxService service;
    
    @Before
    public void before() {
        try {
            if (user == null) {
                user = (User) UserManager.getInstance().getUserByLoginName("superman");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testFindbyPk() {

        GcZjglTqkbmmxVO tmp = service.findById("11");
        logger.info("{}",tmp);

    }
    
    @Test
    public void testCRUD() {

        HashMap response = new HashMap();
        HashMap data = new HashMap();
        response.put("response", data);

        // 主键
        String id = new RandomGUID().toString();

        GcZjglTqkbmmxVO tmp = new GcZjglTqkbmmxVO();
        tmp.setId(id);
        //tmp.setInternal("ZFRQ", DateTimeUtil.getDate());
        tmp.setBz("test date "+DateTimeUtil.getDateTime());

        List<GcZjglTqkbmmxVO> list = new ArrayList<GcZjglTqkbmmxVO>();
        list.add(tmp);

        data.put("data", list);

        String json = JsonBinder.buildNonDefaultBinder().toJson(response);

        try {
            String inJson = service.insert(json, user);

//            GcZjglTqkbmmxVO inVO = new GcZjglTqkbmmxVO();
            JSONArray jlist = tmp.doInitJson(inJson);
            tmp.setValueFromJson((JSONObject) jlist.get(0));

            // 修改
            tmp.setInternal("LRR", "_test_");

            list.clear();
            list.add(tmp);
            data.put("data", list);
            json = JsonBinder.buildNonDefaultBinder().toJson(response);
            logger.info(json);

            service.update(json, user);

            //删除
            service.delete(json, user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info(JsonBinder.buildNonDefaultBinder().toJson(response));
    }

}
