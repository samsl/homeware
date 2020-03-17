package ella.sam.shiro;


import ella.sam.shiro.jwt.JwtCredentialsMatcher;
import ella.sam.shiro.jwt.WxJwtCredentialsMatcher;
import ella.sam.shiro.redis.RedisClient;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import java.util.Arrays;
import java.util.Map;


@Configuration
public class ShiroConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private RedisClient redisClient;

    @Bean
    public FilterRegistrationBean<Filter> filterFilterRegistrationBean(SecurityManager securityManager) throws Exception {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter((Filter) shirofilter(securityManager).getObject());
        filterFilterRegistrationBean.addInitParameter("targetFilterLifecycle", "true");
        filterFilterRegistrationBean.setAsyncSupported(true);
        filterFilterRegistrationBean.setEnabled(true);
        filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        return filterFilterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> disableJwtFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(jwtFilter);
        filterFilterRegistrationBean.setEnabled(false);
        return filterFilterRegistrationBean;
    }


    @Bean
    public Authenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(jwtRealm(), passwordRealm(), wxJwtRealm()));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        chain.addPathDefinition("/login", "noSessionCreation,anon");
        chain.addPathDefinition("/wxLogin", "noSessionCreation,anon");
        chain.addPathDefinition("/register", "noSessionCreation,anon");
        chain.addPathDefinition("/users/isExisting", "noSessionCreation,anon");
        chain.addPathDefinition("/movies/**", "noSessionCreation,anon");
        chain.addPathDefinition("/**", "noSessionCreation,jwt");
        return chain;
    }


    @Bean
    public JwtCredentialsMatcher jwtCredentialsMatcher() {
       return new JwtCredentialsMatcher();

    }

    @Bean
    public WxJwtCredentialsMatcher wxJwtCredentialsMatcher() {
        return new WxJwtCredentialsMatcher();

    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    @Bean
    public PasswordRealm passwordRealm() {
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return passwordRealm;
    }

    @Bean
    public JwtRealm jwtRealm() {
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setAuthenticationCachingEnabled(true);
        jwtRealm.setCacheManager(new ShiroCacheManager(redisClient));
        jwtRealm.setCredentialsMatcher(jwtCredentialsMatcher());
        return jwtRealm;
    }

    @Bean WxJwtRealm wxJwtRealm() {
        WxJwtRealm wxJwtRealm = new WxJwtRealm();
        wxJwtRealm.setAuthenticationCachingEnabled(true);
        wxJwtRealm.setCacheManager(new ShiroCacheManager(redisClient));
        wxJwtRealm.setCredentialsMatcher(wxJwtCredentialsMatcher());
        return wxJwtRealm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(authenticator());

        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shirofilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("jwt", jwtFilter);
        factoryBean.setFilters(filterMap);

        factoryBean.setSecurityManager(securityManager);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
         return factoryBean;
    }


}
