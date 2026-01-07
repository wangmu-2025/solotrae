package com.example.exambankadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 完全禁用CSRF保护，因为是管理系统，且API使用JSON数据
                .csrf(csrf -> csrf.disable())
                // 配置登录页面
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                // 配置登出
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // 配置授权规则
                .authorizeHttpRequests(auth -> auth
                        // 允许所有用户访问根路径、登录页面和静态资源
                        .requestMatchers("/", "/login", "/webjars/**", "/css/**", "/js/**").permitAll()
                        // 允许所有用户访问API端点，方便调试
                        .requestMatchers("/api/**").permitAll()
                        // 只允许管理员访问管理界面
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 其他请求默认拒绝
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    /**
     * 临时使用内存用户存储，后续可替换为数据库存储
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // 创建管理员用户
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
