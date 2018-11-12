package com.appsdeveloperblog.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
//#3
public class SpringApplicationContext implements ApplicationContextAware {
	private static ApplicationContext CONTEXT;

	// getting the spring context here
    @Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		CONTEXT = context;
	}
   
    // to get context, from spring context, example any thing used @component, @service etc ... stereotypes
	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}
}