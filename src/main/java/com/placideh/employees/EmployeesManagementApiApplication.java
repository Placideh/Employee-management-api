package com.placideh.employees;

import com.placideh.employees.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeesManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesManagementApiApplication.class, args);
	}
	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean(){
		FilterRegistrationBean<AuthFilter> registrationBean=new FilterRegistrationBean<>();
		AuthFilter authFilter=new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/employees/*");
		registrationBean.addUrlPatterns("/api/admins");
		return registrationBean;

	}

}
