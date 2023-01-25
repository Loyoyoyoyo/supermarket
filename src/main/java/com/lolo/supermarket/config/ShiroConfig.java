package com.lolo.supermarket.config;

import com.lolo.supermarket.Listener.MySessionListener;
import com.lolo.supermarket.realm.MyRealm;
import com.lolo.supermarket.realm.MyRealm2;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;


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
        //5 新增：shiro整合redis  将session管理交给redis
        defaultWebSecurityManager.setSessionManager(sessionManager());
        //Shiro默认整合了EhCache，来实现缓存
        //6 返回
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
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }

    //这四个bean是shiro整合redis的
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;


    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    public RedisManager redisManager() {
        // 它可对redis的ip、端口等进行配置
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host + ":" + port);// 主机地址和端口号要写在一起
        redisManager.setDatabase(database);// 默认数据库
        redisManager.setTimeout(timeout);// 过期时间
        return redisManager;
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     *
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setKeyPrefix("shiro-session");//配置session前缀
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        redisSessionDAO.setRedisManager(redisManager());
        // session在redis中的保存时间,最好大于session会话超时时间
        redisSessionDAO.setExpire(timeout);
        return redisSessionDAO;
    }

    /**
     * 配置会话ID生成器
     *
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 配置session监听
     *
     * @return
     */
    @Bean
    public MySessionListener sessionListener() {
        MySessionListener sessionListener = new MySessionListener();
        return sessionListener;
    }

    /**
     * 配置保存sessionId的cookie 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理
     * 也需要自己的cookie 默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
     *
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        // 这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("REDIS-SESSION");
        // setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        // setcookie()的第七个参数
        // 设为true后，只能通过http访问，javascript无法访问
        // 防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        // maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        // 配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        /**
         * 修改Cookie中的SessionId的key，默认为JSESSIONID，自定义名称
         */
        sessionManager.setSessionIdCookie(new SimpleCookie("REDIS-SESSIONID"));
        // 全局会话超时时间（单位毫秒），默认30分钟 暂时设置为10秒钟 用来测试
        sessionManager.setGlobalSessionTimeout(1800000);//单位毫秒
        // 是否开启删除无效的session对象 默认为true
        sessionManager.setDeleteInvalidSessions(true);
        // 是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        // 设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler
        // 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(3600000);//单位毫秒
        // 取消url 后面的 JSESSIONID，设置为false为取消
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

}
