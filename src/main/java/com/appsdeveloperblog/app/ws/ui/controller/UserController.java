package com.appsdeveloperblog.app.ws.ui.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.serializers.AddressDtoSerializer;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.PasswordResetModel;
import com.appsdeveloperblog.app.ws.ui.model.request.PasswordResetRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.utils.JsonViewSerializeUtils;
import com.appsdeveloperblog.app.ws.views.View;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressesService;
	
	@GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) // adding support to respond as xml & Json
	// @GetMapping({"/{id}"})
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);
		return returnValue;
	}
	
	@PostMapping(
				  consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, // incoming request can also have xml or json for post type hence consumes	
				  produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
				)
	// @Request body converts Json into java objects
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{
		UserRest returnValue = new UserRest();
		
		
		if(userDetails.getFirstName().isEmpty()) { throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage()); }
		
		//UserDto userDto = new UserDto();
		// when nested objects come in like one to many, BeanUtils does not copy properly
		//BeanUtils.copyProperties(userDetails, userDto);
				
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		
		return returnValue;
	}
	
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
								produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updateUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);

		return returnValue;
	}
	
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }) // MediaType.APPLICATION_XML_VALUE,
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
								   @RequestParam(value = "limit", defaultValue = "2") int limit) {
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);
		
		//Type listType = new TypeToken<List<UserRest>>() {}.getType();
		//returnValue = new ModelMapper().map(users, listType);

		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}

		return returnValue;
	}
	
	
	// http://www.makeinjava.com/convert-list-objects-tofrom-json-java-jackson-objectmapper-example/
	@GetMapping(path = "/objectMapper", 
			    produces = { MediaType.APPLICATION_JSON_VALUE }) // MediaType.APPLICATION_XML_VALUE,
	public String getUsersAsMapper(@RequestParam(value = "page", defaultValue = "0") int page,
								   @RequestParam(value = "limit", defaultValue = "2") int limit) throws JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
    	//Set pretty printing of json
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		List<UserDto> users = userService.getUsers(page, limit);
		
		String arrayToJson = objectMapper.writeValueAsString(users);
		
		return arrayToJson;
	}
	
	// https://grokonez.com/json/use-jsonview-serializede-serialize-customize-json-format-java-object
		@GetMapping(path = "/objectsListMapper", 
				    produces = { MediaType.APPLICATION_JSON_VALUE }) // MediaType.APPLICATION_XML_VALUE,
		public String getUsersAsListMapper(@RequestParam(value = "page", defaultValue = "0") int page,
									   @RequestParam(value = "limit", defaultValue = "2") int limit) throws JsonProcessingException {
			
			ObjectMapper objectMapper = new ObjectMapper();
	    	//Set pretty printing of json
	    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

			List<UserDto> users = userService.getUsers(page, limit);
			
		
			
//			String overalViewString = JsonViewSerializeUtils.serializeObjectToString(users, View.OveralView.class);
			String overalViewString = JsonViewSerializeUtils.serializeObjectToString(users, View.DetailView.class);
			
			return overalViewString;
		}
		
//		@JsonView(View.DetailView.class)
		@JsonView(View.OveralView.class)
		@GetMapping(path = "/objectsListMapper2", 
			    produces = { MediaType.APPLICATION_JSON_VALUE }) // MediaType.APPLICATION_XML_VALUE,
	   public List<UserDto> getUsersAsListMapper2(@RequestParam(value = "page", defaultValue = "0") int page,
								   @RequestParam(value = "limit", defaultValue = "2") int limit) throws JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
    	//Set pretty printing of json
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		List<UserDto> users = userService.getUsers(page, limit);
		
	
		
//		String overalViewString = JsonViewSerializeUtils.serializeObjectToString(users, View.OveralView.class);
		//String overalViewString = JsonViewSerializeUtils.serializeObjectToString(users, View.DetailView.class);
		
		return users;
	}
		
		
		
		@GetMapping(path = "/addressCustomSerializer", 
			    produces = { MediaType.APPLICATION_JSON_VALUE }) // MediaType.APPLICATION_XML_VALUE,
	public String getAddressCustomSerializer(@RequestParam(value = "page", defaultValue = "0") int page,
								   @RequestParam(value = "limit", defaultValue = "2") int limit) throws JsonProcessingException {

		List<AddressDTO> addresses = addressesService.getAllAddresses();

		ObjectMapper mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addSerializer(AddressDTO.class, new AddressDtoSerializer());
		mapper.registerModule(module);

		String serialized = mapper.writeValueAsString(addresses);

		return serialized;

}
		
		// https://grokonez.com/json/use-jsonview-serializede-serialize-customize-json-format-java-object
				@GetMapping(path = "/objectsListMapperAddress", 
						    produces = { MediaType.APPLICATION_JSON_VALUE }) // MediaType.APPLICATION_XML_VALUE,
				public String getAddressAsListMapper(@RequestParam(value = "page", defaultValue = "0") int page,
											   @RequestParam(value = "limit", defaultValue = "2") int limit) throws JsonProcessingException {
					
					ObjectMapper objectMapper = new ObjectMapper();
			    	//Set pretty printing of json
			    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

					List<AddressDTO> addresses = addressesService.getAllAddresses();
					
				
					
//					String overalViewString = JsonViewSerializeUtils.serializeObjectToString(users, View.OveralView.class);
					// this maps to DTO view
					String overalViewString = JsonViewSerializeUtils.serializeObjectToString(addresses, View.DetailView.class);
					
					return overalViewString;
				}
				
				// https://grokonez.com/json/use-jsonview-serializede-serialize-customize-json-format-java-object
				@JsonView(View.OveralView.class)
				@GetMapping(path = "/objectsListMapperAddressController", 
						    produces = { MediaType.APPLICATION_JSON_VALUE }) // MediaType.APPLICATION_XML_VALUE,
				public List<AddressDTO> getAddressAsListMapperController(@RequestParam(value = "page", defaultValue = "0") int page,
											   @RequestParam(value = "limit", defaultValue = "2") int limit) throws JsonProcessingException {
					
					ObjectMapper objectMapper = new ObjectMapper();
			    	//Set pretty printing of json
			    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

					List<AddressDTO> addresses = addressesService.getAllAddresses();
					
				
					
//							String overalViewString = JsonViewSerializeUtils.serializeObjectToString(users, View.OveralView.class);
					// this maps to DTO view
//							String overalViewString = JsonViewSerializeUtils.serializeObjectToString(addresses, View.DetailView.class);
//							
//							return overalViewString;
					
					return addresses;		
				}
	
	
	// http://localhost:8080/mobile-app-ws/users/jfhdjeufhdhdj/addressses
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public Resources<AddressesRest> getUserAddresses(@PathVariable String id) {
		List<AddressesRest> addressesListRestModel = new ArrayList<>();

		List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

		if (addressesDTO != null && !addressesDTO.isEmpty()) {
			// see model mapper how to array of type object to other array
			Type listType = new TypeToken<List<AddressesRest>>() { }.getType();
			addressesListRestModel = new ModelMapper().map(addressesDTO, listType);

			for (AddressesRest addressRest : addressesListRestModel) {
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId()))
						.withSelfRel();
				addressRest.add(addressLink);

				Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
				addressRest.add(userLink);
			}
		}

		return new Resources<>(addressesListRestModel);
	}

	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE, "application/hal+json" })
	public Resource<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

		AddressDTO addressesDto = addressesService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

		AddressesRest addressesRestModel = modelMapper.map(addressesDto, AddressesRest.class);

		addressesRestModel.add(addressLink);
		addressesRestModel.add(userLink);
		addressesRestModel.add(addressesLink);

		return new Resource<>(addressesRestModel);
	}
	
	
	/*
     * http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
     * */
    @GetMapping(path = "/email-verification", 
    			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
        
        boolean isVerified = userService.verifyEmailToken(token);
        
        if(isVerified)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }
    
    
    /*
     * http://localhost:8080/mobile-app-ws/users/password-reset-request
     * */
    @PostMapping(path = "/password-reset-request", 
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
    	OperationStatusModel returnValue = new OperationStatusModel();
 
        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
        
        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
 
        if(operationResult)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }
    
    
    
    @PostMapping(path = "/password-reset",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
    	OperationStatusModel returnValue = new OperationStatusModel();
 
        boolean operationResult = userService.resetPassword(
                passwordResetModel.getToken(),
                passwordResetModel.getPassword());
        
        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
 
        if(operationResult)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

	
}
