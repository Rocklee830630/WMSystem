/*
 * ServiceTest.java  v1.00  2013-8-27
 * Peoject	xmg
 * Copyright (c) 2013 KcpmIT
 *
 * Filename	:	ServiceTest.java  v1.00 2013-8-27
 * Project	: 	xmg
 * Copyight	:	Copyright (c) 2013 KcpmIT
 */
package com.ccthanking.framework.test;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;

/**
 * base service Test class.
 * 
 * @author <a href="mailto:jianggl88@gmail.com">蒋根亮</a>
 * @version v1.00
 * @since 1.00 2013-8-27
 * 
 */
@ContextConfiguration(locations = { "classpath:application_context.xml", "classpath:application_viewresolver.xml" })
public class ServiceTest extends AbstractJUnit4SpringContextTests {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static User user;

    @BeforeClass
    public static void setUp1() {

        String path = ServiceTest.class.getClassLoader().getResource(".").getPath();
        path = StringUtils.substringBefore(path, "WebContent").substring(1) + "WebContent";

        // System.setProperty("webApp.root",
        // "E:\\WorkDir\\Project\\kcit\\pcci\\xmgl\\WebContent");
        System.setProperty("webApp.root", path);

    }

}
