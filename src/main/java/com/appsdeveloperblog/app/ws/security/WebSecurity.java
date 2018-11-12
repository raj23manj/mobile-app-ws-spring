package com.appsdeveloperblog.app.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.service.UserService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	// this is an interface given to us by spring security
	private final UserService userDetailsService; 
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    // configure web service entry points which are to be allowed and not allowed
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// post /users should be public, permitAll() takes care of that to be public
    	// anyRequest() -> means any other request will be needed to be authenticated
    	// we will not be able to access others get /users, delete /users etc ....
        http.csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
        .permitAll()
        .anyRequest().authenticated();
        
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	// need to setup AuthenticationManagerBuilder from spring security, along with the userDetailsService from spring security
    	// mentioning that we are using bCryptPasswordEncoder 
    	// AuthenticationManagerBuilder handles everything under the hood, all we need to say it is
    	// which interface are we using for user details and what is the encryption method we are using(bCryptPasswordEncoder),
    	// which also comes from spring
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
