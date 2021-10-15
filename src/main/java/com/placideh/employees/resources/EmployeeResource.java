package com.placideh.employees.resources;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.placideh.employees.exception.*;
import com.placideh.employees.mails.EmailSenderService;
import com.placideh.employees.model.Position;
import com.placideh.employees.model.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.placideh.employees.model.Employee;
import com.placideh.employees.repository.EmployeeRepository;
/*
* https://gitlab.com/task-force-challenge/backend-v2/-/blob/main/README.md
* */
@RestController
@RequestMapping("/api/employees")
@Api(value = "Employee Endpoints",description = "Employee Rest Endpoints")
public class EmployeeResource {
	private static Map<String,String> errors;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private EmailSenderService service;
	private final Logger LOGGER= LoggerFactory.getLogger(EmployeeResource.class);

	@ApiOperation(value="Returns List of Employees Details")
	@GetMapping(" ")
	public ResponseEntity<List<Employee>> getAllEmployees() throws EmployeeNotFoundException{
		List<Employee>employees=employeeRepo.findAll();
		if(employees.isEmpty())throw new EmployeeNotFoundException("List is Empty");
		LOGGER.info("Getting All Employees");
		return new ResponseEntity<>(employees,HttpStatus.OK);
	}
    @ApiOperation(value = "register a single Employee and returns a confirmation Message to that registered user")
	@PostMapping("/register")
	public ResponseEntity<Map<String,String>> registerEmployee(@RequestBody Employee employee )
			throws ConstraintViolationException,
			EmployeeInputException,EmployeeExistException,
			EmailSendException,DbContraintsException, UnknownHostException, IOException {
		Map<String,String> map=new HashMap<>();
		employee.setCode(randomString());
		employee.setStatus(Status.ACTIVE);
		employee.setCreatedDate(LocalDate.now());
		if(employee==null)throw new EmployeeInputException("Fields must be filled");
		if(checkEmployeeInfo(employee));
		Employee employee1=employeeRepo.findByNationalId(employee.getNationalId());
		if(employee1!=null)throw new EmployeeExistException("Employee is Already Registered");
		if(!InetAddress.getByName("www.google.com").isReachable(2000))throw new UnknownHostException("Please Connect on Network");
		if(ConstraintViolationException.class==null) throw new DbContraintsException("Constraint Violation Exception");
		employeeRepo.save(employee);
		triggerTheEmailMsg(employee.getEmail());
		map.put("message", "employee created");
		LOGGER.info("Employee with this Email:"+employee.getEmail()+"Is Registered");
		return new ResponseEntity<Map<String,String>>(map,HttpStatus.CREATED);
		
	}
	@ApiOperation(value = "updates a user by only five fields:name,phoneNumber,position,status and date of birth")
	@PutMapping("/update")
	public ResponseEntity<Map<String,String>> updateEmployee(@RequestBody Employee employee)
	throws  EmployeeNotFoundException{
		
		Map<String,String> map=new HashMap<>();
		Employee emp=employeeRepo.findByNationalId(employee.getNationalId());
		if(emp==null) throw new EmployeeNotFoundException("Employee not Found");

			String name=employee.getName();
			String phoneNumber=employee.getPhoneNumber();
			Position position=employee.getPosition();
			Status status=employee.getStatus();
			LocalDate dob=employee.getDob();
			String nationalId=emp.getNationalId();

			int result=employeeRepo.updateEmployee(name,phoneNumber,position,status,dob,nationalId);
			map.put("message", "employee updated");
			if(result>0) ;
			LOGGER.info("Manager with this Email:"+emp.getEmail()+"Updates Some Information");
			return new ResponseEntity<Map<String,String>>(map,HttpStatus.OK);

	}
    @ApiOperation(value = "removes an Employee by using His/Her generated Code")
	@DeleteMapping("/{code}")
	public ResponseEntity<Map<String,String>> removeEmployee(@PathVariable String code)
	throws EmployeeNotFoundException{
		Employee employee=employeeRepo.findByCode(code);
		Map<String ,String>map=new HashMap<>();
		if(employee==null)throw new EmployeeNotFoundException("Employee Not Exist");

			employeeRepo.delete(employee);
			map.put("message","Employee deleted");
			LOGGER.info("Employee with this Email:"+employee.getEmail()+"Deleted Success");
			return  new ResponseEntity<>(map,HttpStatus.OK);

	}

	@ApiOperation(value = "retrieve a list of employees whith the same position")
	@GetMapping("/position/{position}")
	public ResponseEntity<List<Employee>> getEmployeesByPosition(@PathVariable Position position)
			throws EmployeeNotFoundException{
		List<Employee> employees=employeeRepo.searchByPosition(position);
		if(employees.isEmpty())throw new EmployeeNotFoundException("List is Emppty");
		LOGGER.info(" retrieve a list of employees whith the same position");
		return new ResponseEntity<>(employees,HttpStatus.OK);
	}
	@ApiOperation(value = "returns a single Employee by search using His/Her Code")
	@GetMapping("/code/{code}")
    public ResponseEntity<Employee> getEmployeesByCode(@PathVariable String code) throws EmployeeNotFoundException{
	    Employee employee=employeeRepo.searchByCode(code);
	    if(employee==null)throw new EmployeeNotFoundException("Employee Not Found");
		LOGGER.info(" retrieve employee info by Employee Code");
	    return new ResponseEntity<>(employee,HttpStatus.OK);
    }
    @ApiOperation(value = "returns a single Employee by search using His/Her phone Number")
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<Employee> getEmployeeByPhone(@PathVariable String phoneNumber) throws EmployeeNotFoundException{
        Employee employee=employeeRepo.searchByPhoneNumber(phoneNumber);
		if(employee==null)throw new EmployeeNotFoundException("Employee Not Found");
		LOGGER.info(" retrieve employee info by Phone Number");
        return new ResponseEntity<>(employee,HttpStatus.OK);
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
	
	private boolean checkEmployeeInfo(Employee employee) throws EmployeeInputException {
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
		if(employee.getPosition()==null) {
			errors.put("position","Position must specified");
			throw new EmployeeInputException(errors.get("position"));
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
	private void triggerTheEmailMsg(String email){
        service.sendCommunicationEmail(
                email,
                "Welcome to Our Company you are respected ",
                "Welcome in Placideh Employees Company"
        );
    }
	
	
}
