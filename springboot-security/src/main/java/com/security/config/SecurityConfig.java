package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author lijichen
 * @date 2021/2/9 - 18:07
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    // remember me 配置
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 第一次启动时创建数据库表，如果已经有表就必须关闭
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    // 设置认证用户名密码
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 方式二使用Service
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        // 内存中保存
        /*BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // 加密
        String user = bCryptPasswordEncoder.encode("user");

        auth.inMemoryAuthentication()
                .withUser("user")
                .password(user)
                .roles("admin");*/
    }

    // 如果使用BCryptPasswordEncoder需要放入
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 跳转到没有权限的页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");

        http.formLogin() //自定义登录页面
                .failureUrl("/login.html") // 登陆失败后跳转的页面
                .loginPage("/login.html") // 登陆页面
                .loginProcessingUrl("/user/login") // 登录访问路径
                .defaultSuccessUrl("/success.html").permitAll() // 登陆成功后跳转路径
                .and().authorizeRequests()
                    .antMatchers("/", "/hello" ,"/user/login").permitAll() // 不需要认证的路径
                    // hasAuthority , 表示admin,manager 这两个权限都必须要有
//                    .antMatchers("/test/index").hasAuthority("admin,manager")
                    // hasAnyAuthority 表示只要有连个权hasAnyAuthority限其中之一就可以登录
//                    .antMatchers("/test/index").("admin,manager")
                    // 根据角色
                    .antMatchers("/test/index").hasRole("sale")
                .anyRequest().authenticated()
                // 记住我
                .and().rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(600) // token 有效时常
                    .userDetailsService(userDetailsService);

         http.csrf().disable(); // 关闭csrf防护

        // 退出登录
        http.logout()
                .logoutUrl("/logout") // 退出登录访问路径
                .logoutSuccessUrl("/login.html"); // 推出成功后页面
    }
}
