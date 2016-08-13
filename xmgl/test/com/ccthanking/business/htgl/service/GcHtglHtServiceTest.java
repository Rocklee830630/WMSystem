/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtServiceTest.java
 * 创建日期： 2013-09-02 上午 08:08:02
 * 功能：    测试类：合同信息
 * 所含类:   GcHtglHtService.java
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-02 上午 08:08:02  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ccthanking.business.htgl.vo.GcHtglHtVO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.test.JsonBinder;
import com.ccthanking.framework.test.ServiceTest;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.RandomGUID;

/**
 * <p>
 * GcHtglHtAction.java
 * </p>
 * <p>
 * 功能：合同信息, 测试类.
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtManagerTest.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-02
 * 
 */

public class GcHtglHtServiceTest extends ServiceTest {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtServiceTest.class);

    @Autowired
    @Qualifier("gcHtglHtServiceImpl")
    private GcHtglHtService service;

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

        GcHtglHtVO tmp = service.findById("11");
        logger.info("{}", tmp);

    }

    @Test
    public void testCRUD() {

        HashMap response = new HashMap();
        HashMap data = new HashMap();
        response.put("response", data);

        // 主键
        String id = new RandomGUID().toString();

        GcHtglHtVO tmp = new GcHtglHtVO();
        tmp.setId(id);
        tmp.setHtbm("合同(" + DateTimeUtil.getCurrentYear() + ")" + new Random().nextInt());
        tmp.setHtmc("测试合同" + DateTimeUtil.getDateTime("HHmmss"));
        tmp.setJfdw("甲方单位test");
        tmp.setYfdw("合同乙方单位" + RandomStringUtils.randomAlphanumeric(8));

        tmp.setInternal("HTJQDRQ", DateTimeUtil.getDate());

        tmp.setBz("test date " + DateTimeUtil.getDateTime());

        List<GcHtglHtVO> list = new ArrayList<GcHtglHtVO>();
        list.add(tmp);

        data.put("data", list);

        String json = JsonBinder.buildNonDefaultBinder().toJson(response);

        try {
            String inJson = service.insert(json, user, null);

            // GcHtglHtVO inVO = new GcHtglHtVO();
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
            // 删除
            // service.delete(json, user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info(JsonBinder.buildNonDefaultBinder().toJson(response));
    }

}
