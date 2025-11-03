package org.ecommerce.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerScheme=new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").description("JWT Bearer Token");
        SecurityRequirement bearerRequirement=new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
                .info(new Info().title("Ecommerce React X Spring Boot REST APIs").description("""
                                This API provides a complete backend solution for an e-commerce platform built using **Spring Boot** and integrated with a **React** frontend.

                                It includes modules for:
                                - User authentication and JWT-based security
                                - Product management (CRUD operations)
                                - Shopping cart and wishlist management
                                - Order placement and tracking
                                - Payment integration (optional)
                                - Role-based access using Spring Security

                                Use the `Authorize` button above to authenticate via JWT before accessing secured endpoints.
                                """) .version("1.0.0")
                        .contact(new Contact()
                                .name("Ecommerce Project API Support")
                                .email("support@ecommerce.com")
                                .url("""
                                        https://github.com/yourusername/ecommerce"""))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",bearerScheme)).addSecurityItem(bearerRequirement);
    }
}
