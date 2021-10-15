package com.placideh.employees.upload;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UploadFile {
    @Autowired
    private final UploadUtil uploadUtil;
    public UploadFile(UploadUtil uploadUtil) {
        this.uploadUtil=uploadUtil;
    }

    public List<Map<String, String>> upload(MultipartFile file) throws  Exception{
        Path tempDir= Files.createTempDirectory("");
        File tempFile=tempDir.resolve(file.getOriginalFilename()).toFile();
        //transfer multipart file to tempFile
        file.transferTo(tempFile);
        Workbook workbook=WorkbookFactory.create(tempFile);


        Sheet sheet=workbook.getSheetAt(0);
        Supplier<Stream<Row>> rowStreamSupplier= uploadUtil.getRowStreamSupplier(sheet);
        Row headerRow=rowStreamSupplier.get().findFirst().get();
        List<String> headerCells=StreamSupport.stream(headerRow.spliterator(),false)
                .map(Cell::getStringCellValue)
                .collect(Collectors.toList());
        int colCount=headerCells.size();

       return rowStreamSupplier.get().map(row->{
            //getting cellStream from a particular row
            List<String> cellList=StreamSupport.stream(headerRow.spliterator(),false)
                    .map(Cell::getStringCellValue)
                    .collect(Collectors.toList());

          return IntStream.range(0,colCount).boxed()
                    .collect(Collectors.toMap(index->headerCells.get(index),
                                                index->cellList.get(index)));

        }).collect(Collectors.toList());



    }
}
