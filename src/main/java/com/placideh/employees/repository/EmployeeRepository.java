package com.placideh.employees.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.placideh.employees.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
	
	Employee findByNationalId(String nationalId); 
	//update employee
	@Modifying
	@Transactional
	@Query(
			value="UPDATE employee_table  SET employee_name=?1 "
					+ ",phone_number=?2,position=?3,status=?4,date_of_birth=?5 WHERE national_id=?6",
			nativeQuery=true
			
			)
	int updateEmployee(Employee employee,String national_id);

}
