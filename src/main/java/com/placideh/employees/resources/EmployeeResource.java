package com.placideh.employees.resources;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.placideh.employees.model.Employee;

@RestController
@RequestMapping("/api/employees")
public class EmployeeResource {
	private static Map<String,String> errors;
	
	
	private boolean checkEmployeeInfo(Employee employee) {
		errors=new HashMap<>();
		if(employee.getName().isEmpty()) {
			errors.put("name", "The name can not be Empty");
			
		}
		if(LocalDate.now().getYear()-employee.getDob().getYear()<18) {
			errors.put("dob", "The age are not eligible");
		}
		
	}

}
