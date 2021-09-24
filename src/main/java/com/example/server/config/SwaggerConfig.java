package com.example.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author Jingze Zheng
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.server.controller"))
//                .paths(PathSelectors.ant("/api.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Jingze Zheng", "", "zhengjingze94@gmail.com");
        return new ApiInfo(
                "API server"
                , "API server for Vector technical assessment."
                , "1.0"
                , ""
                , contact
                , "Apache License Version 2.0"
                , "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }
}
