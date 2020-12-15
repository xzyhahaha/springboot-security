package com.xzy.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //授权
    //链式编程
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        首页所有人可以访问，功能页则需要相应的功能权限
//        请求授权的规则
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

//        没有权限会默认到登录页
//        super.configure(http);

        //定制登录页
        http.formLogin().loginPage("/toLogin");
//        http.formLogin().loginPage("/toLogin").usernameParameter("username").passwordParameter("password").loginProcessingUrl("/login");

        //防止网站工具：  get，post
        http.csrf().disable();   //关闭csrf功能,登录失败可能存在的原因

        //开启注销功能,注销后跳到首页
        http.logout().logoutSuccessUrl("/toLogin");

        //开启记住我功能  cookie,默认存14天,自定义接收前端的参数
        http.rememberMe().rememberMeParameter("isRemember");
    }

//    认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //在内存中定义，也可以在数据可以中去拿....
        //Spring security 5.0中新增了多种加密方式，也改变了密码的格式。
        //要想我们的项目还能够正常登陆，需要修改一下configure中的代码。我们要将前端传过来的密码进行某种方式加密
        //spring security 官方推荐的是使用bcrypt加密方式。
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("xzy").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3")
                .and()
                .withUser("haha").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1");
    }
}
