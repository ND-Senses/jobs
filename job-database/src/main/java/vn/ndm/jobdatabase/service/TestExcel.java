package vn.ndm.jobdatabase.service;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TestExcel {
    public static void main(String[] args) {
        readExcel("export.xlsx");

    }


    public static List<HashMap<String, String>> readExcel(String pathFile) {
        List<HashMap<String, String>> data = new ArrayList<>();
        Workbook workbook = null;
//        pathFile = pathFile.replace("\\", "/");
        try (FileInputStream excelFileInput = new FileInputStream(pathFile);){
            workbook = WorkbookFactory.create(excelFileInput);
            // get số sheet trong file
            int numSheet = workbook.getNumberOfSheets();
            for (int i = 0; i < numSheet; i++) {
                LinkedHashMap<String, String> rowData = new LinkedHashMap<>();
                Sheet sheet = workbook.getSheetAt(i);
                DataFormatter fmt = new DataFormatter();
                //get discount name
                Row rowDiscountName = sheet.getRow(2);
                Cell cellDiscountName = rowDiscountName.getCell(2);
                String discountName = fmt.formatCellValue(cellDiscountName).trim();
                rowData.put("discountName", discountName);
                //get number chu ky
                Row rowNumberChuKy = sheet.getRow(3);
                Cell cellNumberChuKy = rowNumberChuKy.getCell(2);
                String numberChuKy = fmt.formatCellValue(cellNumberChuKy).trim();
                rowData.put("numberChuKy", numberChuKy);

                for (Row currentRow : sheet) {
                    if (currentRow.getRowNum() < 5) {
                        continue;
                    }
                    Cell cellFromPrice = currentRow.getCell(6);
                    rowData.put("fromPrice", fmt.formatCellValue(cellFromPrice).trim());
                    Cell cellToPrice = currentRow.getCell(7);
                    rowData.put("toPrice", fmt.formatCellValue(cellToPrice).trim());
                    for (int j = 1; j <= Integer.parseInt(numberChuKy); j++) {
                        Cell cellValue = currentRow.getCell(j + 2);
                        if (!fmt.formatCellValue(cellValue).equals("")) {
                            rowData.put("value" + j, fmt.formatCellValue(cellValue).trim());
                        }else{
                            rowData.put("value" + j, "0");
                        }
                    }

                    data.add(rowData);
                }
            }
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

  

}
