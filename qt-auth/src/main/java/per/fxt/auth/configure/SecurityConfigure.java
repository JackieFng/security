package per.fxt.auth.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Fangxiaoting
 * @date 2021/7/12 17:29
 */
@EnableWebSecurity
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 配置允许访问的链接
                .authorizeRequests().antMatchers("/oauth/**").permitAll()
                // 其余所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                //加上httBasic提交
                .and()
                .httpBasic()
                // 关闭跨域保护;
                .and().csrf().disable();
    }

}
