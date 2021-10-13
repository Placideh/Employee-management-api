package com.placideh.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.placideh.employees.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{

}
