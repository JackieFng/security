package per.fxt.order.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.rmi.Remote;

/**
 * @author Fangxiaoting
 * @date 2021/7/14 15:54
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "res1";

    @Autowired
    private TokenStore tokenStore;

    /**
     * 当资源服务和授权服务在同一个工程下可以使用DefaultTokenServices来验证令牌
     * 否则需要通过http远程调用授权服务的形式来验证token，需要指定client_id、client_secret、及url
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID)
                /*.tokenServices(tokenServices())*/
                .tokenStore(tokenStore)//资源服务通过jwt对称加密的形式验证令牌
                .stateless(true);
    }

    @Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices remoteTokenService = new RemoteTokenServices();
        remoteTokenService.setCheckTokenEndpointUrl("http://localhost:8010/oauth/check_token");
        remoteTokenService.setClientId("qt");
        remoteTokenService.setClientSecret("123");
        return remoteTokenService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 访问资源服务的令牌的scope需要拥有all的权限才能访问
                .authorizeRequests().antMatchers("/**").access("#oauth2.hasScope('all')")
                // 关闭跨域保护;
                .and().csrf().disable()
                //不再创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
