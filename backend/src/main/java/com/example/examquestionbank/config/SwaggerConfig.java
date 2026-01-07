package com.example.examquestionbank.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Exam Question Bank API",
                description = "考试题库管理系统后端API",
                version = "1.0",
                contact = @Contact(
                        name = "Exam Bank Team",
                        email = "team@exambank.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
public class SwaggerConfig {
    // Swagger配置类，用于启用Swagger UI
    // SpringDoc会自动扫描Controller类生成API文档
}