package com.appsdeveloperblog.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.appsdeveloperblog.app.ws.SpringApplicationContext;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//#2
// this class is used to filter username amd password

// this authentication filter cannot autowire or inject other beans, hence need to get the spring context here, see below successfulAuthentication
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter  {
private final AuthenticationManager authenticationManager;
    
    private String contentType;
 
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
    											HttpServletResponse res) throws AuthenticationException {
    	/*
    	 * This method will be triggered when singup method is called, and the payload is converted to a request UserLoginRequestModel
    	 * and passed to authenticate
    	 * */
        try {
        	
        	contentType = req.getHeader("Accept");
        	
        	// our custom class under ui/model/request
            UserLoginRequestModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserLoginRequestModel.class);
            // authenticationManager is used to authenticate the credentials
            // this method will use loadUserByUsername from UserServiceimpl to find user
            // once successful successfulAuthentication method will be triggered, below
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	/*
    	 * This is called after attemptAuthentication is successfully authenticating
    	 * */
        
    	// user name will be read from authentication object
        String userName = ((User) auth.getPrincipal()).getUsername();  
        
        String token = Jwts.builder()
			               .setSubject(userName)
			               .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
			               .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret() )
			               .compact();
     // this authentication filter cannot auto wire or inject other beans, hence need to get the spring context here, see below successfulAuthentication
        UserService userService = (UserService)SpringApplicationContext.getBean("userServiceImpl");
        UserDto userDto = userService.getUser(userName);
       
       // this is the token returned to the client angular, mobile. It needs to store this token and send it back for authorization 
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        res.addHeader("UserID", userDto.getUserId());
        
    }  
}
