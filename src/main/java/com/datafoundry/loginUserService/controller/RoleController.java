package com.datafoundry.loginUserService.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datafoundry.loginUserService.model.RoleEntity;
import com.datafoundry.loginUserService.model.request.RoleEntityRequest;
import com.datafoundry.loginUserService.model.response.RoleResponse;
import com.datafoundry.loginUserService.model.response.UserResponse;
import com.datafoundry.loginUserService.repository.RoleCollectionRepository;
import com.datafoundry.loginUserService.repository.RoleEntityRepository;
import com.datafoundry.loginUserService.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/authservice/v1/roles")
@Api(value = "RoleController", tags = { "Role APIs" })
public class RoleController {
	
	@Autowired
	RoleEntityRepository roleEntityRepository;
	
	@Autowired
	RoleCollectionRepository roleCollectionRepository;
		
	@Autowired
	RoleService roleService;
	
	@ApiOperation(value = "Gets all role details.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrived all roles details"),
			@ApiResponse(code = 400, message = "No roles found."),
			@ApiResponse(code = 401, message = "You are not authorized to perform this action."),
			@ApiResponse(code = 403, message = "Insufficient privileges."),
			@ApiResponse(code = 404, message = "Parent route not found."),
			@ApiResponse(code = 500, message = "Server side error.") })
	@GetMapping(path = "/", produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity<UserResponse> getAll(@ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale) {
		return roleService.findAll(locale);
	}

	@ApiOperation(value = "Gets one role details (based on the role name)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrived the role details"),
			@ApiResponse(code = 400, message = "Role with the given name is not found"),
			@ApiResponse(code = 401, message = "You are not authorized to perform this action."),
			@ApiResponse(code = 403, message = "Insufficient privileges."),
			@ApiResponse(code = 404, message = "Parent route not found."),
			@ApiResponse(code = 500, message = "Server side error.") })
	@GetMapping(path = "/{roleName}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam(name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	public ResponseEntity<RoleResponse> getAnEmail(@ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @PathVariable(required = true) String roleName)
	{
		RoleEntity roleEntity = roleEntityRepository.findByRoleName(roleName);
		
		if(roleEntity != null)
		{
			return ResponseEntity.ok(new RoleResponse( "Role "+roleName+" details fetched successfully.", true, 200,roleEntity )); 
		}
		else
		{
			return ResponseEntity.ok(new RoleResponse( "Role "+roleName+" details could not be found.", false, 400, null));
		}
		
	}
	
	@ApiOperation(value = "Adds a new role")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created the role with the given details"),
			@ApiResponse(code = 400, message = "Not found - The data not found"),
			@ApiResponse(code = 401, message = "You are not authorized to perform this action."),
			@ApiResponse(code = 403, message = "Insufficient privileges."),
			@ApiResponse(code = 404, message = "Parent route not found."),
			@ApiResponse(code = 500, message = "Server side error.") })
	@PostMapping(path = "/", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	public ResponseEntity<RoleResponse> createRole(@ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @RequestBody(required = true) RoleEntityRequest roleEntityRequest) {

		return roleService.createRole(locale, roleEntityRequest);
	}
		
	@ApiOperation(value = "Removes an existing role")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully removed the role"),
			@ApiResponse(code = 400, message = "Not found - The data not found"),
			@ApiResponse(code = 401, message = "You are not authorized to perform this action."),
			@ApiResponse(code = 403, message = "Insufficient privileges."),
			@ApiResponse(code = 404, message = "Parent route not found."),
			@ApiResponse(code = 500, message = "Server side error.") })
	@DeleteMapping(path = "/{roleName}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	public ResponseEntity<RoleResponse> deleteRole(@ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @PathVariable(required = true) String roleName) {

		return roleService.deleteRole(locale, roleName);
	}
	
	@ApiOperation(value = "Updates an existing role")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the role"),
			@ApiResponse(code = 400, message = "Not found - The data not found"),
			@ApiResponse(code = 401, message = "You are not authorized to perform this action."),
			@ApiResponse(code = 403, message = "Insufficient privileges."),
			@ApiResponse(code = 404, message = "Parent route not found."),
			@ApiResponse(code = 500, message = "Server side error.") })
	@PatchMapping(path = "/{roleName}", produces = { APPLICATION_JSON_VALUE })
	@ApiImplicitParam (name = "Accept-Language", value = "language", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class, example = "en")
	public ResponseEntity<?> updateRole(@ApiIgnore @ApiParam(value = "locale", required = false,hidden = true) @RequestHeader(name="Accept-Language", required=false) Locale locale, @PathVariable(required = true) String roleName, @RequestBody(required = true) RoleEntityRequest roleRequest) {

		return roleService.updateRole(locale, roleName, roleRequest);
	}

}
