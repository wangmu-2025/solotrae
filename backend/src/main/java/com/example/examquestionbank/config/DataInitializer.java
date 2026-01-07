package com.example.examquestionbank.config;

import com.example.examquestionbank.entity.User;
import com.example.examquestionbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 数据初始化类，用于在应用启动时添加初始用户数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已存在管理员用户
        Optional<User> adminUser = userRepository.findByUsername("admin");
        if (adminUser.isEmpty()) {
            // 创建管理员用户
            User user = new User();
            // 手动分配id
            user.setId("1");
            user.setUsername("admin");
            // 使用BCrypt加密密码
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setEmail("admin@example.com");
            user.setPhone("13800138000");
            user.setRole("ADMIN");
            user.setStatus(1);
            // 设置创建和更新时间
            LocalDateTime now = LocalDateTime.now();
            user.setCreatedAt(now);
            user.setUpdatedAt(now);
            
            // 保存到数据库
            userRepository.save(user);
            System.out.println("已创建管理员用户: admin/admin123");
        } else {
            System.out.println("管理员用户已存在");
        }
    }
}