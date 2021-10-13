package com.placideh.employees.repository;

import com.placideh.employees.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager,String> {
}
