package com.placideh.employees.resources;

import com.placideh.employees.mails.EmailSenderService;
import com.placideh.employees.model.Employee;
import com.placideh.employees.model.Position;
import com.placideh.employees.model.Status;
import com.placideh.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(EmployeeResource.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class EmployeeResourceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @MockBean
    private EmailSenderService service;

    private Employee employee;
    @Test
    void testGetAllEmployees() throws Exception {
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

        Employee employee2=Employee.builder()
                .name("Elen")
                .phoneNumber("+250789894104")
                .code("EMP1934")
                .createdDate(LocalDate.now())
                .dob(LocalDate.now())
                .email("elen@gmail.com")
                .position(Position.DEVELOPER)
                .status(Status.ACTIVE)
                .nationalId("1199412343434356")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/employees")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

 }
    @Test
    void testEmployeeSearchByCode() throws  Exception{
        Employee employee=Employee.builder()
                .name("Elena")
                .phoneNumber("+250789874104")
                .code("EMP2323")
                .createdDate(LocalDate.now())
                .dob(LocalDate.now())
                .email("elena@gmail.com")
                .position(Position.DEVELOPER)
                .status(Status.ACTIVE)
                .nationalId("1199712343434356")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/employees/code/{code}","EMP2323")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void testEmployeeSearchByPosition() throws  Exception{
        Employee employee=Employee.builder()
                .name("Elena")
                .phoneNumber("+250789874104")
                .code("EMP2323")
                .createdDate(LocalDate.now())
                .dob(LocalDate.now())
                .email("elena@gmail.com")
                .position(Position.DEVELOPER)
                .status(Status.ACTIVE)
                .nationalId("1199712343434356")
                .build();
        Employee employee2=Employee.builder()
                .name("Elen")
                .phoneNumber("+250789894104")
                .code("EMP1934")
                .createdDate(LocalDate.now())
                .dob(LocalDate.now())
                .email("elen@gmail.com")
                .position(Position.DEVELOPER)
                .status(Status.ACTIVE)
                .nationalId("1199412343434356")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/employees/position/{position}","DEVELOPER")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void testEmployeeSearchByPhoneNumber() throws  Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/employees/phone/{phoneNumber}","+250789874104")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testRegisterANewEmployee() throws Exception {
        Employee employee1=Employee.builder()
                .name("torman2")
                .phoneNumber("+250789874104")
                .dob(LocalDate.parse("1999-01-01"))
                .email("placide2@gmail.com")
                .position(Position.DESIGNER)
                .nationalId("1197897401234567")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/employees/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "    \"name\": \"torman2\",\n" +
                            "    \"nationalId\": \"1197897401234567\",\n" +
                            "    \"phoneNumber\": \"+250739394104\",\n" +
                            "    \"email\": \"placide2@gmail.com\",\n" +
                            "    \"dob\": \"1999-01-01T23:28:56.782Z\",\n" +
                            "    \"position\": \"DESIGNER\"\n" +
                            "    \n" +
                            "  }")
        ).andExpect(MockMvcResultMatchers.status().isCreated());

    }

}
