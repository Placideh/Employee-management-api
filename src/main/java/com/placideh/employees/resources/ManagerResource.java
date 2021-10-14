package com.placideh.employees.resources;


import com.placideh.employees.exception.ManagerAuthException;
import com.placideh.employees.model.Constants;
import com.placideh.employees.model.Manager;
import com.placideh.employees.model.Position;
import com.placideh.employees.repository.ManagerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiOperation;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/admins")
public class ManagerResource {
    @Autowired
    ManagerRepository managerRepo;



    @PostMapping("/register")
    @ApiOperation(value = "records a new Manager with Position of MANAGER")
    public ResponseEntity<Map<String,String>> registerManager(@RequestBody Manager manager){
        Map<String,String> map=new HashMap<>();
        Pattern pattern=Pattern.compile("^(.+)@(.)$");

        manager.setCode(randomString());

        manager.setCreatedDate(LocalDate.now());

        manager.setPosition(Position.MANAGER);
        String hashedPassword= BCrypt.hashpw(manager.getPassword(),BCrypt.gensalt(10));
        manager.setPassword(hashedPassword);
        String email=manager.getEmail();
        if(!email.trim().isEmpty())email=email.toLowerCase();
//        if (!pattern.matcher(email).matches())
//            throw new ManagerAuthException("Invalid email format");
        Manager manager1=managerRepo.findByEmail(email);
        if (manager1!=null)
            throw new ManagerAuthException("Email is already registered");
        managerRepo.save(manager);
        map.put("message", "manager recorded");
        return new ResponseEntity<Map<String,String>>(map, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> managerLogin(@RequestBody Map<String,Object> manager){
        String email=(String)manager.get("email");
        String password=(String)manager.get("password");
        try{
            if(!email.trim().isEmpty())email=email.toLowerCase();
            Manager manager1=managerRepo.findByEmail(email);
            System.out.println("Manager1:"+manager1);
            if(!BCrypt.checkpw(password,manager1.getPassword())){
                throw new ManagerAuthException("Invalid email/password");
            }

           Manager manager2= managerRepo.findByConfirmEmailAndPassword(email,manager1.getPassword());
            System.out.println("Manager2:"+manager2);
            return new ResponseEntity<>(generateJWTToken(manager2),HttpStatus.OK);
        }catch (EmptyResultDataAccessException e){
            throw new ManagerAuthException("Invalid email/password");
        }

    }
    @GetMapping("")
    public ResponseEntity<List<Manager>> getManagers(){
        List<Manager> managers=managerRepo.findAll();
        return new ResponseEntity<>(managers,HttpStatus.OK);
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

    private Map<String ,String> generateJWTToken(Manager manager){
        long timestamp=System.currentTimeMillis();
        String token= Jwts.builder().signWith(SignatureAlgorithm.HS256,Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp+ Constants.TOKEN_VALIDITY))
                .claim("nationalId",manager.getNationalId())
                .claim("email",manager.getEmail())
                .claim("name",manager.getName())
                .claim("phoneNumber",manager.getPhoneNumber())
                .claim("position",manager.getPosition())
                .claim("password",manager.getPassword())
                .claim("code",manager.getCode())
//                .claim("createdDate",manager.getCreatedDate())
                .claim("confirmEmail",manager.getConfirmEmail())
//                .claim("dob",manager.getDob())
                .claim("status",manager.getStatus())
                .compact();
        Map<String ,String> map=new HashMap<>();
        map.put("token", token);
        return map;

    }
}
