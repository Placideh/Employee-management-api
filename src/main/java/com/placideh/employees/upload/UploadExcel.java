package com.placideh.employees.upload;

import com.placideh.employees.mails.EmailSenderService;
import com.placideh.employees.model.Employee;
import com.placideh.employees.model.Position;
import com.placideh.employees.model.Status;
import com.placideh.employees.repository.EmployeeRepository;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UploadExcel {
    public List<Employee> employeeList=new ArrayList<>();

    @Autowired
    private EmailSenderService service;
    public List<Employee> excel(MultipartFile file) throws  Exception{
        Path tempDir= Files.createTempDirectory("");
        File tempFile=tempDir.resolve(file.getOriginalFilename()).toFile();
        //transfer multipart file to tempFile
        file.transferTo(tempFile);
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelImportToJTable = null;

        if (true) {
            try {
                excelFIS = new FileInputStream(tempFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelImportToJTable = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelImportToJTable.getSheetAt(0);
                if(!InetAddress.getByName("www.google.com").isReachable(2000))throw new UnknownHostException("Please Connect on Network");
                for (int row = 1; row < excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);

                    XSSFCell name = excelRow.getCell(0);
                    XSSFCell nationalId = excelRow.getCell(1);
                    XSSFCell code = excelRow.getCell(2);
                    XSSFCell phoneNumber = excelRow.getCell(3);
                    XSSFCell email = excelRow.getCell(4);
                    XSSFCell dob = excelRow.getCell(5);
                    XSSFCell status = excelRow.getCell(6);
                    XSSFCell position = excelRow.getCell(7);
                    Employee employee=new Employee(name.toString(),
                            nationalId.toString(),code.toString(),
                            phoneNumber.toString(),email.toString(),
                            LocalDate.parse(dob.toString()), Status.valueOf(status.toString()),
                            Position.valueOf(position.toString()), LocalDate.now());
                     employeeList.add(employee);


                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (excelFIS != null) {
                        excelFIS.close();
                    }
                    if (excelBIS != null) {
                        excelBIS.close();
                    }
                    if (excelImportToJTable != null) {
                        excelImportToJTable.close();
                    }
                } catch (IOException iOException) {

                }
            }
        }
        return employeeList;
    }



}
