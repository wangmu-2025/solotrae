package com.example.examquestionbank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF，因为我们使用的是表单登录和会话
                .csrf(csrf -> csrf.disable())
                // 配置CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 配置授权规则 - 基于角色的访问控制
                .authorizeHttpRequests(auth -> auth
                        // 允许所有用户访问的公开接口 - 放在最前面，最具体的匹配
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/refresh").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/webjars/**").permitAll()
                        // 允许公开访问的计数接口
                        .requestMatchers("/api/users/count").permitAll()
                        .requestMatchers("/api/questions/count").permitAll()
                        .requestMatchers("/api/question-banks/count").permitAll()
                        // 允许所有登录用户访问的个人信息接口
                        .requestMatchers("/api/auth/me").authenticated()
                        // 允许所有登录用户访问的题目和题库相关接口
                        .requestMatchers("/api/questions/**").authenticated()
                        .requestMatchers("/api/question-banks/**").authenticated()
                        // 只允许管理员访问的用户管理接口（不包括计数接口）
                        .requestMatchers("/api/users").hasRole("ADMIN")
                        .requestMatchers("/api/users/{id}").hasRole("ADMIN")
                        // 只允许管理员访问的系统设置接口
                        .requestMatchers("/api/system-settings/**").hasRole("ADMIN")
                        // 只允许管理员访问的其他管理接口
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 允许所有登录用户访问的管理页面
                        .requestMatchers("/admin/**").authenticated()
                        // 其他请求默认拒绝
                        .anyRequest().denyAll()
                )
                // 配置表单登录
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard")
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                // 配置注销
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                // 配置会话管理
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                // 配置异常处理，对API请求返回401错误，对其他请求重定向到登录页面
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 如果是API请求，返回401错误
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
                            } else {
                                // 否则重定向到登录页面
                                response.sendRedirect("/login");
                            }
                        })
                )
                // 配置用户详情服务
                .userDetailsService(userDetailsService);

        return http.build();
    }

    // 配置CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许特定来源
        configuration.addAllowedOrigin("http://localhost:8083");
        // 允许所有方法
        configuration.addAllowedMethod("*");
        // 允许所有头
        configuration.addAllowedHeader("*");
        // 允许凭证
        configuration.setAllowCredentials(true);
        // 允许暴露这些头给客户端
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Set-Cookie");
        configuration.addExposedHeader("Content-Type");
        configuration.addExposedHeader("Accept");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 注册CORS配置到所有API端点
        source.registerCorsConfiguration("/api/**", configuration);
        source.registerCorsConfiguration("/swagger-ui/**", configuration);
        source.registerCorsConfiguration("/v3/api-docs/**", configuration);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}