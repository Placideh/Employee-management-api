package com.placideh.employees.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="employee_table",
		uniqueConstraints = { @UniqueConstraint(
					name="email_unique",
					columnNames="eamil_address"
				),
				@UniqueConstraint(
						name="employee_code",
						columnNames="employee_code"
						)

}
			
		)
public class Employee {
	@Column(name="employee_name")
	private String name;
	@Id
	@Column(name="national_id")
	private Integer nationalId;
	@Column(name="employee_code")
	private String code;
	@Column(name="phone_number")
	private String phoneNumber;
	@Column(name="email_address",
			nullable=false
			)
	private String eamil;
	@Column(name="date_of_birth")
	private LocalDate dob;
	private Status status;
	private Position position;
	private LocalDate createdDate;
	

}
