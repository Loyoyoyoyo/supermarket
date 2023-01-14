package com.lolo.supermarket.config;

import com.lolo.supermarket.realm.MyRealm;
import com.lolo.supermarket.realm.MyRealm2;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Configuration
public class ShiroConfig {
    @Resource
    MyRealm myRealm;

    @Resource
    MyRealm2 myRealm2;

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        //1 创建 defaultWebSecurityManager 对象
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //2 创建加密对象，并设置相关属性
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //2.1 采用 md5 加密
        matcher.setHashAlgorithmName("md5");
        //2.2 迭代加密次数
        matcher.setHashIterations(3);
        //3 将加密对象存储到 myRealm 中
        myRealm.setCredentialsMatcher(matcher);
        myRealm2.setCredentialsMatcher(matcher);
        //4 封装myRealm集合，将集合存入 defaultWebSecurityManager 对象
        List<Realm> list = new ArrayList<>();
        list.add(myRealm);
        list.add(myRealm2);
        defaultWebSecurityManager.setRealms(list);
        //5 返回
        return defaultWebSecurityManager;
    }

    //配置 Shiro 内置过滤器拦截范围
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置SecuritManager
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/sign-in-by-name");
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
//不需要在此处配置权限页面,因为上面的ShiroFilterFactoryBean已经配置过,但是此处必须存在,因shiro-spring-boot-web-starter或查找此Bean,没有会报错
        return new DefaultShiroFilterChainDefinition();
    }
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }
}
