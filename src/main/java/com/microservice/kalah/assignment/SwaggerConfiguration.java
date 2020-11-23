package com.microservice.kalah.assignment;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.microservice.kalah.assignment.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(generateApiInfo());

    }


    private ApiInfo generateApiInfo() {
        return new ApiInfoBuilder().title("Kalah Game")
                .description("This application is the Kalah game in RESTful Interface which demonstrate my problem solving along with technical skills")
                .license("Unlicensed").licenseUrl("http://unlicense.org").version("1.0")
                .contact(
                        new Contact("Mehdi Yeganehparast",
                                "https://www.linkedin.com/in/mehdi-yeganehparast/",
                                "mehdi.yeganehparast@gmail.com"))
                .build();
    }
}

