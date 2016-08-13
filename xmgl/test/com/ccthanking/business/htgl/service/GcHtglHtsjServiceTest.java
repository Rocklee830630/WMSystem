/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtsjServiceTest.java
 * 创建日期： 2013-09-02 上午 08:02:33
 * 功能：    测试类：合同数据
 * 所含类:   GcHtglHtsjService.java
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-02 上午 08:02:33  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service;

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

import com.ccthanking.business.htgl.vo.GcHtglHtsjVO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.test.JsonBinder;
import com.ccthanking.framework.test.ServiceTest;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.RandomGUID;

/**
 * <p>
 * GcHtglHtsjAction.java
 * </p>
 * <p>
 * 功能：合同数据, 测试类.
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtsjManagerTest.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-02
 * 
 */

public class GcHtglHtsjServiceTest extends ServiceTest {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtsjServiceTest.class);

    @Autowired
    @Qualifier("gcHtglHtsjServiceImpl")
    private GcHtglHtsjService service;

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

        GcHtglHtsjVO tmp = service.findById("11");
        logger.info("{}", tmp);

    }

    @Test
    public void testCRUD() {

        HashMap response = new HashMap();
        HashMap data = new HashMap();
        response.put("response", data);

        // 主键
        String id = new RandomGUID().toString();

        GcHtglHtsjVO tmp = new GcHtglHtsjVO();
        tmp.setId(id);
        // 合同编号
        tmp.setHtid("fe35fac2-2957-4ad2-83ad-e9d3c6a54464");

        // 项目编号
//        tmp.setXmbh("A0DAD824-9D6F-0CFC-BDDE-72A3C718F405");
//        tmp.setBdid("A0DAD824-9D6F-0CFC-BDDE-72A3C718F405");
        tmp.setJhsjid("acd02509-3a88-45b3-844a-d8cd9df89fc9");
        tmp.setHtqdj("221311.00");

        // tmp.setInternal("ZFRQ", DateTimeUtil.getDate());
        tmp.setBz("test date " + DateTimeUtil.getDateTime());

        List<GcHtglHtsjVO> list = new ArrayList<GcHtglHtsjVO>();
        list.add(tmp);

        data.put("data", list);

        String json = JsonBinder.buildNonDefaultBinder().toJson(response);

        try {
            String inJson = service.insert(json, user);

            // GcHtglHtsjVO inVO = new GcHtglHtsjVO();
            JSONArray jlist = tmp.doInitJson(inJson);
            tmp.setValueFromJson((JSONObject) jlist.get(0));

            // 修改
            tmp.setInternal("LRR", "_test_");

            list.clear();
            list.add(tmp);
            data.put("data", list);
            json = JsonBinder.buildNonDefaultBinder().toJson(response);
            logger.info(json);

            // service.update(json, user);
            //
            // //删除
            // service.delete(json, user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info(JsonBinder.buildNonDefaultBinder().toJson(response));
    }

}
