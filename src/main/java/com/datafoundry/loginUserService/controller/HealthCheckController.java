package com.datafoundry.loginUserService.controller;

import com.datafoundry.loginUserService.model.response.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@Api(value = "HealthCheckController", tags = { "Health Check config APIs" })
public class HealthCheckController {

    @ApiOperation(value = "Health check API")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrived") })
    @GetMapping(path = "/health", produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponse> healthCheck() {
        UserResponse successResponse = new UserResponse("Running!",true,HttpStatus.OK.value());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

}



