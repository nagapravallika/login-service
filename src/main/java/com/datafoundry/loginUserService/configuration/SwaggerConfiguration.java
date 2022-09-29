package com.datafoundry.loginUserService.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

	private String title = "AuthService";

	private String description = "AuthService - APIs";

	private String version = "1.0.0";
	
	private boolean enable = true;

	@Bean
	public Docket productApi() {

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.datafoundry"))
				.paths(PathSelectors.any())
				.build().apiInfo(metaData())
				.securitySchemes(apiKey())
				.securityContexts(Lists.newArrayList(securityContext()))
				.useDefaultResponseMessages(false)
				.enable(enable);
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title(title).description(description).version(version).build();
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public List<ApiKey> apiKey() {
		List<ApiKey> apiKeys = new ArrayList<>();
		apiKeys.add(new ApiKey("JWT", "Authorization", "header"));
		return apiKeys;
	}

	@Bean
		SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex("/.*"))
                //.forPaths(PathSelectors.any())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
    }

}
