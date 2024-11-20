package com.project.realtime_collaborative_doc_editing.utils;

import com.project.realtime_collaborative_doc_editing.model.CustomerInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    public static boolean checkFileType(MultipartFile file){
        String contentTypeType = file.getContentType();
        String excelContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if(Objects.equals(contentTypeType,excelContentType)){
            return true;
        }
        return false;
    }

    public static List<CustomerInfo> getRecordsFromExcelFile(InputStream inputStream){
        List<CustomerInfo> customerRecords = new ArrayList<>();
        try {
            XSSFWorkbook excelSheet =  new XSSFWorkbook(inputStream);
            XSSFSheet dataSheet = excelSheet.getSheet("customerInfo");
            int rowNumber = 0;
            Iterator<Row> iterator = dataSheet.iterator();
            while(iterator.hasNext()){
                CustomerInfo customerInfo = new CustomerInfo();
                Row row = iterator.next();
                if(rowNumber == 0){
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellData = row.iterator();
                int cellNumber = 0;
                while(cellData.hasNext()){
                    Cell cell = cellData.next();
                    switch (cellNumber){
                        case 0:
                            String customerName = cell.getStringCellValue();
                            customerInfo.setCustomerName(customerName);;
                            //customerInfo.setCustomerName((String)cell.getStringCellValue());
                        case 1:
                            customerInfo.setMobileNo((String)cell.getStringCellValue());
                        case 2:
                            customerInfo.setEmailId((String)cell.getStringCellValue());
                        case 3:
                            customerInfo.setState((String)cell.getStringCellValue());
                        case 4:
                            customerInfo.setCity((String)cell.getStringCellValue());
                        case 5:
                            customerInfo.setNetWorth((Double)Double.parseDouble(cell.getStringCellValue()));
                        default:
                            break;
                    }
                    cellNumber++;
                }
                customerRecords.add(customerInfo);
            }
        }catch (Exception exception){
            log.error("error processing in file : {}",exception.getMessage());
        }
        return customerRecords;
    }

}
