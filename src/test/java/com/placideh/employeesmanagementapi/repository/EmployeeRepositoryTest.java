package com.placideh.employeesmanagementapi.repository;

import com.placideh.employees.model.Employee;
import com.placideh.employees.model.Position;
import com.placideh.employees.model.Status;
import com.placideh.employees.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepositoryTest;
    @Test
    void itShouldFindEmployeeByNationalId(){
        //given
        Employee employee=Employee.builder()
                .name("Placideh")
                .phoneNumber("+250789394104")
                .code("EMP1234")
                .createdDate(LocalDate.now())
                .dob(LocalDate.now())
                .email("h.uwizeyeplacide1@gmail.com")
                .position(Position.DEVELOPER)
                .status(Status.ACTIVE)
                .nationalId("1199912343434356")
                .build();
        employeeRepositoryTest.save(employee);
        //when
        Employee employee1=employeeRepositoryTest.findByNationalId("1199912343434356");
        //then
        Assertions.assertThat(employee).isEqualTo(employee1);


    }
}
