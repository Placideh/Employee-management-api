package com.placideh.employees.upload;

import com.placideh.employees.model.Employee;
import com.placideh.employees.model.Position;
import com.placideh.employees.model.Status;
import com.placideh.employees.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserExcelImporter {
    public List<Employee> employeeList=new ArrayList<>();
    @Autowired
    private EmployeeRepository employeeRepo;
    public void excelImport(MultipartFile file) throws IOException {
         String name="";
         String nationalId="";
         String code="";
         String phoneNumber="";
         String email="";
         LocalDate dob=LocalDate.now();
         Status status=Status.ACTIVE;
         Position position=Position.DEVELOPER;
         LocalDate createdDate=LocalDate.now();
        Path tempDir= Files.createTempDirectory("");
        File tempFile=tempDir.resolve(file.getOriginalFilename()).toFile();
        //transfer multipart file to tempFile
        file.transferTo(tempFile);
//        Workbook workbook=WorkbookFactory.create(tempFile);
//         String path="";
         long start=System.currentTimeMillis();
        FileInputStream inputStream;
        try{
            inputStream=new FileInputStream(tempFile);
            Workbook workbook=new XSSFWorkbook(inputStream);
            Sheet firstSheet=workbook.getSheetAt(0);
            Iterator<Row> rowIterator=firstSheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()){
                Row nextRow=rowIterator.next();
                Iterator<Cell> cellIterator=nextRow.cellIterator();
                while(cellIterator.hasNext()){
                    Cell nextCell=cellIterator.next();
                    int columnIndex=nextCell.getColumnIndex();
                    switch (columnIndex){
                        case 0:
                            name=nextCell.getStringCellValue();
                            break;
                        case 1:
                            nationalId=nextCell.getStringCellValue();
                            break;
                        case 2:
                            code=nextCell.getStringCellValue();
                            break;
                        case 3:
                            phoneNumber=nextCell.getStringCellValue();
                            break;
                        case 4:
                            email=nextCell.getStringCellValue();
                            break;
                        case 5:
                            dob=LocalDate.parse(nextCell.getStringCellValue()) ;
                            break;
                        case 6:
                            status=Status.valueOf( nextCell.getStringCellValue());
                            break;
                        case 7:
                            position=Position.valueOf(nextCell.getStringCellValue());
                            break;
                        case 8:
                            createdDate=LocalDate.parse(nextCell.getStringCellValue());
                            break;

                    }
                    Employee employee= new Employee(name,nationalId,code,phoneNumber,email,dob,status,position,createdDate);
                    System.out.println(employee);
                    employeeRepo.save(employee);
                }
            }
            workbook.close();
            long end=System.currentTimeMillis();
            System.out.println("time taken for Import: "+(end-start));
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

    }


}
