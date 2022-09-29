package com.datafoundry.loginUserService.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        final Map<String, Object> mapBodyException = new HashMap<>() ;
        mapBodyException.put("success", "false");
        mapBodyException.put("Status code", HttpStatus.FORBIDDEN.value());
        mapBodyException.put("message"  , "Insufficient privileges") ;
        response.setContentType("application/json") ;
        response.setStatus(HttpServletResponse.SC_FORBIDDEN) ;
        final ObjectMapper mapper = new ObjectMapper() ;
        mapper.writeValue(response.getOutputStream(), mapBodyException) ;
    }

}
