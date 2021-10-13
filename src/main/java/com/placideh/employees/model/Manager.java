package com.placideh.employees.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
@Table(name="manager_table",
        uniqueConstraints = { @UniqueConstraint(
                name="email_unique",
                columnNames="email_address"
        ),
                @UniqueConstraint(
                        name="employee_code",
                        columnNames="employee_code"
                )

        }

)
@EqualsAndHashCode
public class Manager extends Employee {
    @Column(name = "email_confirmation")
    private String confirmEmail;
    private String password;

}
