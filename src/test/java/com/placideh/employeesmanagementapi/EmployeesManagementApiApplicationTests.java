package com.placideh.employeesmanagementapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


class EmployeesManagementApiApplicationTests {
	Calculator calculator=new Calculator();
	@Test
	void itShouldAddNumbers() {
		int a=20;
		int b=30;
		int result=calculator.add(a,b);
//		then
		Assertions.assertThat(result).isEqualTo(50);
	}
	class Calculator{
		int add(int a ,int b){
			return a+b;
		}
	}
}
