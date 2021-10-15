package com.placideh.employees.resources;


import com.placideh.employees.exception.EmployeeExistException;
import com.placideh.employees.exception.EmployeeInputException;
import com.placideh.employees.exception.EmployeeNotFoundException;
import com.placideh.employees.mails.EmailSenderService;
import com.placideh.employees.model.*;
import com.placideh.employees.repository.EmployeeRepository;
import com.placideh.employees.repository.ManagerRepository;
import com.placideh.employees.upload.UploadFile;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiOperation;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
public class ManagerResource {
    @Autowired
    ManagerRepository managerRepo;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private EmailSenderService service;
    private final Logger LOGGER= LoggerFactory.getLogger(ManagerResource.class);
    private static Map<String,String> errors;
    @Autowired
    private UploadFile uploadFile;

    @PostMapping("/register")
    @ApiOperation(value = "records a new Manager with Position of MANAGER")
    public ResponseEntity<Map<String,String>> registerManager(@RequestBody Manager manager)
            throws EmployeeInputException, EmployeeExistException {
        Map<String,String> map=new HashMap<>();

        manager.setCode(randomString());
        manager.setConfirmEmail(manager.getConfirmEmail().toLowerCase());
        manager.setEmail(manager.getEmail().toLowerCase());
        manager.setCreatedDate(LocalDate.now());
        manager.setDob(LocalDate.parse(manager.getDob().toString()));
        manager.setPosition(Position.MANAGER);
        String hashedPassword= BCrypt.hashpw(manager.getPassword(),BCrypt.gensalt(10));
        manager.setPassword(hashedPassword);
        if(manager==null)throw new EmployeeInputException("Fields must be filled");

        if(checkManagerInfo(manager));
        String email=manager.getConfirmEmail();
        Manager manager1=managerRepo.findByEmail(email);
        if (manager1!=null)throw new EmployeeExistException("Email is already registered");
        managerRepo.save(manager);
        map.put("message", "manager recorded");
        LOGGER.info("Saving a Manager Record of ManagerResource");
        return new ResponseEntity<Map<String,String>>(map, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    @ApiOperation(value = "a Manager logs in by providing email and password")
    public ResponseEntity<Map<String,String>> managerLogin(@RequestBody Map<String,Object> manager)
        throws EmployeeNotFoundException{
            String email=(String)manager.get("email");
            String password=(String)manager.get("password");
            if(!email.trim().isEmpty())email=email.toLowerCase();
            Manager manager1=managerRepo.findByEmail(email);
            if(manager1==null)throw new EmployeeNotFoundException("Invalid email/password");
            if(!BCrypt.checkpw(password,manager1.getPassword()))
                throw new EmployeeNotFoundException("Invalid email/password");
           Manager manager2= managerRepo.findByConfirmEmailAndPassword(email,manager1.getPassword());
            if(manager2==null)throw new EmployeeNotFoundException("Invalid email/password");
            LOGGER.info("Manager with this Email:"+email+" Logged In");
            return new ResponseEntity<>(generateJWTToken(manager2),HttpStatus.OK);
    }
    @GetMapping("")
    @ApiOperation(value = "returns a list of all managers")
    public ResponseEntity<List<Manager>> getManagers() throws EmployeeNotFoundException{
        List<Manager> managers=managerRepo.findAll();
        if(managers.isEmpty())throw new EmployeeNotFoundException("No Data Found");
        LOGGER.info("Manager with Listing All Managers");
        return new ResponseEntity<>(managers,HttpStatus.OK);
    }
    @PostMapping("/reset/{email}")
    @ApiOperation(value = "Manager password reset by providing a valid registered email")
    public ResponseEntity<Map<String,String>> passwordReset(@PathVariable String email) throws EmployeeInputException, EmployeeNotFoundException {
        Manager manager=managerRepo.findByEmail(email);
        if (manager == null)throw new EmployeeNotFoundException("Email is is not registered");
        Map<String,String>map=new HashMap<>();
        triggerTheResetLink(email);
        map.put("message","reset Link was sent to your email");
        LOGGER.info("Manager with this Email:"+email+"Request For Reset Password");
        return new ResponseEntity<>(map,HttpStatus.GONE);

    }
    @PutMapping("/reset")
    @ApiOperation(value = "Reset password by providing new password and confirm that password")
    public ResponseEntity<Map<String,String>> passwordResetConfirmation(@RequestBody Map<String,Object> manager)
        throws EmployeeInputException,EmployeeNotFoundException{
        String email=(String)manager.get("email");
        String newPassword=(String)manager.get("newPassword");
        String confirmNewPassword=(String)manager.get("confirmNewPassword");
        if(!newPassword.trim().equals(confirmNewPassword.trim()))
            throw new EmployeeInputException("password must match");

        Manager manager1=managerRepo.findByEmail(email);
        if(manager1==null)throw new EmployeeNotFoundException("Manager with "+email+" Not exist");
        String hashedPassword= BCrypt.hashpw(newPassword,BCrypt.gensalt(10));
        managerRepo.updateManagerPassword(hashedPassword,email);
        Map<String,String>map=new HashMap<>();
        map.put("message","password reseted");
        LOGGER.info("Manager with this Email:"+email+"Password Was reseted ");
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    @PutMapping("/suspend/{email}")
    @ApiOperation(value = "Manager Can suspend The Employee via this api ")
    public ResponseEntity<Map<String,String>>suspendEmployee(@PathVariable String email)
        throws EmployeeInputException,EmployeeNotFoundException{
        Employee employee=employeeRepo.findByEmail(email);
        if(employee==null)throw new EmployeeNotFoundException("Employee with "+email+" Not Found");
        employee.setStatus(Status.INACTIVE);
        employeeRepo.updateEmployee(employee.getName(),
                employee.getPhoneNumber(), employee.getPosition(),
                employee.getStatus(),employee.getDob(), employee.getNationalId());
        Map<String,String>map=new HashMap<>();
        map.put("message","Employee Suspended");
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    @PostMapping("/upload")
    @ApiOperation(value = "Upload a list of Employees and sends back Email to each Employee registered")
    public List<Map<String,String>>uploadEmployees(@RequestParam("file")MultipartFile file) throws Exception {
        return uploadFile.upload(file);
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
    private void triggerTheResetLink(String email){
        service.sendCommunicationEmail(
                email,
                "Password Reset Link : http://localhost:8080/api/admins/reset",
                "Password Reset"
        );
    }

    private boolean checkManagerInfo(Manager employee) throws EmployeeInputException {
        Integer atSign=employee.getEmail().indexOf("@");
        Integer emailLength=employee.getEmail().length();
        String textAfterAtSign=employee.getEmail().substring(atSign+1,emailLength);
        Integer dotSign=textAfterAtSign.indexOf(".");
        Integer total=atSign+dotSign;
        Boolean result=(total-atSign)<2;
        String textAfterDotSign=textAfterAtSign.substring(dotSign+1, textAfterAtSign.length());
        Boolean afterDotSignCheck=textAfterDotSign.length()<=1;

        errors=new HashMap<>();
        if(employee.getName().trim().isEmpty()) {
            errors.put("name", "The name can not be Empty");
            throw new EmployeeInputException(errors.get("name"));

        }
        if((LocalDate.now().getYear())-(employee.getDob().getYear())<18) {
            errors.put("dob", "The age are not eligible");
            throw new EmployeeInputException(errors.get("dob"));
        }

        if(!employee.getNationalId().trim().matches("[0-9]{16}")) {

            errors.put("nationalId","invalid national Id it must contains 16 digits");
            throw new EmployeeInputException(errors.get("nationalId"));
        }

        if(!employee.getPhoneNumber().matches("[+250]{4}[78||79||72||73]{2}[0-9]{7}")) {
            errors.put("phoneNumber", "Phone number must be Rwandan number");
            throw new EmployeeInputException(errors.get("phoneNumber"));
        }
        if(employee.getEmail().isEmpty()) {
            errors.put("email", "Email must be filled");
            throw new EmployeeInputException(errors.get("email"));
        }else {
            if(!employee.getEmail().contains(".")||!employee.getEmail().contains("@")){
                errors.put("email","Email must contains @ and . sign");
                throw new EmployeeInputException(errors.get("email"));
            }
            if(result){
                errors.put("email","Email must contain dot after @ sign ");
                throw new EmployeeInputException(errors.get("email"));

            }

            if(afterDotSignCheck){
                errors.put("email", "Email must contain a top level domain");
                throw new EmployeeInputException(errors.get("email"));

            }
        }
        if(errors.isEmpty()) {
            return true;
        }
        throw new EmployeeInputException("one or more fields contains an error");


    }

}
