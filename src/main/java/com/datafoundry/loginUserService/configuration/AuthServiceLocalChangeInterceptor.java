package com.datafoundry.loginUserService.configuration;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class AuthServiceLocalChangeInterceptor implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	   registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	   LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	   localeChangeInterceptor.setParamName("language");
	   return localeChangeInterceptor;
	}
	
	@Bean
	public LocaleResolver localeResolver() {
	   SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
	   sessionLocaleResolver.setDefaultLocale(Locale.US);
	   return sessionLocaleResolver;
	}

	//configuring ResourceBundle  
	@Bean  
	public ResourceBundleMessageSource messageSource()  
	{  
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();  
		messageSource.setBasename("messages");  
		return messageSource;  
	}  
}
