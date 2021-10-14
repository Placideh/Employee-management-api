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
        manager.setDob(LocalDate.parse(manager.getDob().toString()));
        manager.setPosition(Position.MANAGER);
        String hashedPassword= BCrypt.hashpw(manager.getPassword(),BCrypt.gensalt(10));
        manager.setPassword(hashedPassword);
        String email=manager.getConfirmEmail();
        if(!email.trim().isEmpty())email=email.toLowerCase();
        if (!pattern.matcher(email).matches())
            throw new ManagerAuthException("Invalid email format");
        Manager manager1=managerRepo.findByEmail(email);
        if (manager1!=null)
            throw new ManagerAuthException("Email is already registered");
        managerRepo.save(manager);
        map.put("message", "manager recorded");
        return new ResponseEntity<Map<String,String>>(map, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    @ApiOperation(value = "a Manager logs in by providing email and password")
    public ResponseEntity<Map<String,String>> managerLogin(@RequestBody Map<String,Object> manager){
        String email=(String)manager.get("email");
        String password=(String)manager.get("password");
        try{
            if(!email.trim().isEmpty())email=email.toLowerCase();
            Manager manager1=managerRepo.findByEmail(email);

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
    @ApiOperation(value = "returns a list of all managers")
    public ResponseEntity<List<Manager>> getManagers(){
        List<Manager> managers=managerRepo.findAll();
        return new ResponseEntity<>(managers,HttpStatus.OK);
    }
    @PostMapping("/reset/{email}")
    @ApiOperation(value = "Manager password reset by providing a valid registered email")
    public ResponseEntity<Map<String,String>> passwordReset(@PathVariable String email){
        Pattern pattern=Pattern.compile("^(.+)@(.)$");
        if(!email.trim().isEmpty())email=email.toLowerCase();
        if (!pattern.matcher(email).matches())
                throw new ManagerAuthException("Invalid email format");
        Manager manager=managerRepo.findByEmail(email);
        if (manager == null)
                throw new ManagerAuthException("Email is is not registered");
        Map<String,String>map=new HashMap<>();
        map.put("message","reset code was sent to your email");
        return new ResponseEntity<>(map,HttpStatus.GONE);

    }
    @PutMapping("/reset")
    @ApiOperation(value = "Reset password by providing new password and confirm that password")
    public ResponseEntity<Map<String,String>> passwordResetConfirmation(@RequestBody Map<String,Object> manager){
        String email=(String)manager.get("email");
        String newPassword=(String)manager.get("newPassword");
        String confirmNewPassword=(String)manager.get("confirmNewPassword");
        if(!newPassword.trim().equals(confirmNewPassword.trim()))
            throw new ManagerAuthException("password must match");
        Manager manager1=managerRepo.findByEmail(email.trim());
        String hashedPassword= BCrypt.hashpw(newPassword,BCrypt.gensalt(10));
        if (manager1!=null)managerRepo.updateManagerPassword(hashedPassword,email);
        Map<String,String>map=new HashMap<>();
        map.put("message","password reseted");
        return new ResponseEntity<>(map,HttpStatus.OK);
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
                .claim("code",manager.getCode())
                .claim("confirmEmail",manager.getConfirmEmail())
                .claim("status",manager.getStatus())
                .compact();
        Map<String ,String> map=new HashMap<>();
        map.put("token", token);
        return map;

    }
}
