package utilities;

import static org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import commons.GlobalConstants;

public class ExcelHelper {
    private static XSSFSheet sheet;
    private static XSSFWorkbook workbook;
    private static org.apache.poi.ss.usermodel.Cell Cell;
    private static XSSFRow Row;

    public static ExcelHelper getData() {
        return new ExcelHelper();
    }

    public void setExcelFile(String path) throws Exception {
        try {
            FileInputStream ExcelFile = new FileInputStream(path);
            workbook = new XSSFWorkbook(ExcelFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getCellData(int rowNumber, int columnNumber, String sheeetName) throws Exception {
        try {
            sheet = workbook.getSheet(sheeetName);
            Cell = sheet.getRow(rowNumber).getCell(columnNumber);
            String cellData = Cell.getStringCellValue();
            return cellData;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public void setCellData(String result, int rowNumber, int columnNumber, String sheetName) throws Exception {

        try {
            sheet = workbook.getSheet(sheetName);
            Row = sheet.getRow(rowNumber);
            Cell = Row.getCell(columnNumber, RETURN_BLANK_AS_NULL);
            if (Cell == null) {
                Cell = Row.createCell(columnNumber);
                Cell.setCellValue(result);
            } else {
                Cell.setCellValue(result);
            }
            FileOutputStream fileOut = new FileOutputStream(GlobalConstants.PATH_TEST_DATA);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
            workbook = new XSSFWorkbook(new FileInputStream(GlobalConstants.PATH_TEST_DATA));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
