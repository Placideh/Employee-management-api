package com.placideh.employees.resources;


import com.placideh.employees.model.Manager;
import com.placideh.employees.model.Position;
import com.placideh.employees.repository.ManagerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
public class ManagerResource {
    @Autowired
    ManagerRepository managerRepo;



    @PostMapping("/register")
    @ApiOperation(value = "records a new Manager with Position of MANAGER")
    public ResponseEntity<Map<String,String>> registerManager(@RequestBody Manager manager){
        Map<String,String> map=new HashMap<>();
        manager.setCode(randomString());

        manager.setCreatedDate(LocalDate.now());

        manager.setPosition(Position.MANAGER);

        managerRepo.save(manager);
        map.put("message", "manager recorded");
        return new ResponseEntity<Map<String,String>>(map, HttpStatus.CREATED);
    }



    private  String randomString() {

        String characters = "0123456789";
        String str = "";
        String createdEmplCode="EMP";
        char[] mynewCharacters = characters.toCharArray();
        Integer generatedCodeLength =4;
        for (int i = 0; i < generatedCodeLength; i++) {
            int index = (int) (Math.random() *10);
            String newString = characters.substring(index, characters.length() - 1);
            str += mynewCharacters[newString.length()];
        }
        return createdEmplCode.concat(str);
    }
}
