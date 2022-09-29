package com.datafoundry.loginUserService.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.datafoundry.loginUserService.configuration.TenantAspect;

@Service
public class DatabaseConnectionService {
    private final BeanFactory beanFactory;
    
//	@Value("${targetDB}")
    private String targetDB = "SQL";

    @Autowired
    public DatabaseConnectionService(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public TenantAspect getDatabaseInstance() {
        TenantAspect service = beanFactory.getBean(targetDB+"DatabaseService", TenantAspect.class);

        return service;
    }
}