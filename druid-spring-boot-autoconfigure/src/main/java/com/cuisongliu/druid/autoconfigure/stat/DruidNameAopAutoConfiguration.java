/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 cuisongliu@qq.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cuisongliu.druid.autoconfigure.stat;

import com.cuisongliu.druid.autoconfigure.condition.NameAopTypesCondition;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * 按照BeanId来拦截配置stat和spring监控关联
 * @author cuisongliu
 * @since  2017年5月20日 11:15:26
 */
@Conditional(NameAopTypesCondition.class)
public class DruidNameAopAutoConfiguration extends DruidStatInitAutoConfiguration{

    @Value("${spring.aop.proxy-target-class:false}")
    private boolean proxyTargetClass;

    @Bean
    @ConfigurationProperties(DruidStatProperties.DRUID_STAT_PREFIX)
    public BeanNameAutoProxyCreator nameProxyCreator(DruidStatProperties properties){
        if (properties.getBeanNames() == null){
            throw new IllegalStateException(DruidStatProperties.DRUID_STAT_PREFIX+".bean-names must  not null.");
        }
        BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
        creator.setProxyTargetClass(proxyTargetClass);
        String[] beanNames = new String[properties.getBeanNames().size()];
        beanNames = properties.getBeanNames().toArray(beanNames);
        creator.setBeanNames(beanNames);
        creator.setInterceptorNames(DruidStatProperties.DRUID_STAT_INTERCEPTOR_NAME);
        return  creator;
    }
}
