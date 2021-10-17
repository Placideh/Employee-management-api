package com.placideh.employees.repository;

import com.placideh.employees.model.Employee;
import com.placideh.employees.model.Position;
import com.placideh.employees.model.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDate;
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepositoryTest;

    @BeforeEach
    void setUp(){
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

    }
    @Test
    public void whenFindByNationalId_thenReturnEmployee(){
        Employee employee=employeeRepositoryTest.findByNationalId("1199912343434356");
        org.junit.jupiter.api.Assertions.assertEquals(employee.getName(),"Placideh");
    }



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
