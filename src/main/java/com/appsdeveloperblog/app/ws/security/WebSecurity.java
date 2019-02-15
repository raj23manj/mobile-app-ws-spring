package com.appsdeveloperblog.app.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.service.UserService;
//#1
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
        .antMatchers(HttpMethod.GET, "/users")
        .permitAll()
        .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
        .permitAll()
        .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL)
        .permitAll()
        .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
        .permitAll()
        .antMatchers(SecurityConstants.H2_CONSOLE)// making h2console also public
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        // adding custom user login
        .addFilter(getAuthenticationFilter())
        .addFilter(new AuthorizationFilter(authenticationManager()))
        // the reason why we do this because AuthenticationFilter does not have any annotation @component etc, hence can't autowire
        //.addFilter(new AuthenticationFilter(authenticationManager()));
        // should make rest api stateless, because it creates session and cashes, and does not require JWT to authorize again, I came across this issue
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // there are other options 
        
        // to disable browser from loading iframe tags, in use of h2 only
        http.headers().frameOptions().disable();
        
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
    
    
    // override user login page uri
    protected AuthenticationFilter getAuthenticationFilter() throws Exception {
	    final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
	    filter.setFilterProcessesUrl("/users/login");
	    return filter;
	}
}

/*
 * spring gives a default /login url to login
 * */
