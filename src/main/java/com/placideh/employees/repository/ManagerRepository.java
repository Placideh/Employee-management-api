package com.placideh.employees.repository;

import com.placideh.employees.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ManagerRepository extends JpaRepository<Manager,String> {

    Manager findByEmail(String email);
    Manager findByConfirmEmailAndPassword(String confirmEmail,String password);
}
