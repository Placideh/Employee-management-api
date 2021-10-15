package com.placideh.employees.repository;

import java.time.LocalDate;
import java.util.List;


import com.placideh.employees.model.Position;
import com.placideh.employees.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.placideh.employees.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,String>{
	
	Employee findByNationalId(String nationalId);
	Employee findByEmail(String email);
	Employee findByCode(String code);
	//search by Employee name
	@Query("FROM Employee WHERE name=?1")
		List<Employee> searchByName(String name);
	//search By Employee Position
	@Query("FROM Employee WHERE position=?1")
	List<Employee> searchByPosition(Position position);
	//Search By PhoneNumber
	@Query("FROM Employee WHERE phoneNumber=?1")
	Employee searchByPhoneNumber(String phoneNumber);
	//search By code
	@Query("FROM Employee WHERE code=?1")
	Employee searchByCode(String code);
	//update employee
	@Modifying
	@Transactional
	@Query("UPDATE Employee  SET name=?1,phoneNumber=?2,position=?3,status=?4 ,dob=?5  WHERE nationalId=?6")
	int updateEmployee(String name, String phoneNumber, Position position, Status status, LocalDate dob, String nationalId);

}
