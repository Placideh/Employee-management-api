package com.placideh.employees.repository;

import com.placideh.employees.model.Manager;
import com.placideh.employees.model.Position;
import com.placideh.employees.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface ManagerRepository extends JpaRepository<Manager,String> {

    Manager findByEmail(String email);
    Manager findByConfirmEmailAndPassword(String confirmEmail,String password);


    //update Manager Password
    @Modifying
    @Transactional
    @Query("UPDATE Manager  SET password=?1  WHERE confirmEmail=?2")
    int updateManagerPassword(String password, String confirmEmail);
}
