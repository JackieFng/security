package per.fxt.order.configure;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Fangxiaoting
 * @date 2021/7/12 17:29
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfigure extends WebSecurityConfigurerAdapter {


    /**
     *配置安全拦截机制
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 配置允许访问的链接
                .authorizeRequests()
                // 其余所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                //加上httBasic提交
                .and()
                .httpBasic()
                // 关闭跨域保护;
                .and().csrf().disable();
    }

}
