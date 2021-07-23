package per.fxt.auth.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author admin
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 令牌端点的安全约束配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 客户端详情配置
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*clients.inMemory()
                .withClient("qt")//客户端id
                .secret(new BCryptPasswordEncoder().encode("123"))//客户端密钥
                .resourceIds("res1")//可以访问资源列表
                .scopes("all")//客户端访问范围，默认拥有全部访问权限
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")//授权模式:授权码，密码，客户端，简易模式
                .autoApprove(false);//跳转到授权页面*/
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 从数据库验证客户端身份
     *
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }


    /**
     * 访问端点和令牌服务配置
     * 主要有3点配置
     * 1、令牌的存储策略
     * 2、令牌管理服务
     * 3、令牌的访问端点：比如密码模式需要配置认证管理器，userDetailService,授权码模式还需要配置authorizationCodeServices
     * <p>
     * 授权码模式操作要点
     * 服务启动后通过访问/oauth/authorize?client_id=qt&response_type=code进入到用户登陆界面，
     * 登陆验证用户身份（UserDetailService）。验证通过后，弹出授权界面点击approve在回调地址后面拼接上授权码。接着可以使用该授权码通过/oauth/token获取token
     * （数据库添加数据时要求该客户端必须支持授权码模式且需要配置回调地址）
     * /oauth/token为post请求，需要的参数有：client_id、client_secret、grant_type=authorization_code、code(即授权码)
     * <p>
     * 简化模式通过访问/oauth/authorize?client_id=qt&response_type=token登陆成功后，通过授权直接将token在回调地址后面以#拼接的形式返回
     * <p>
     * 密码模式则不需要授权码，直接访问/oauth/token生成token，需要的参数有：client_id、client_secret、grant_type=password、username、password。
     * 由于密码模式会将密码暴露给客户端，所以一般用于自己开发的client或者web页面
     * <p>
     * 客户端模式直接访问/oauth/token生成token，需要的参数有：client_id、client_secret、grant_type=client_credentials
     * 客户端模式不需要验证用户身份，所以这需要对client的完全信任。比如合作方系统对接等。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)//认证管理器,密码模式和授权码模式必须要配置
                .authorizationCodeServices(authorizationCodeServices())//授权码服务
                .tokenServices(tokenServices())//令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 授权码服务
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 令牌管理服务
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore);
        //设置令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        service.setAccessTokenValiditySeconds(7200);
        service.setRefreshTokenValiditySeconds(259200);
        return service;
    }

}
