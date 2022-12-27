package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspose.cells.Cells;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

public class ExcelHelper {

    public static ExcelHelper getData() {
        return new ExcelHelper();
    }

    public Map<String, List<String>> getExcelDataAsMap(String excelFilePath, String sheetName) {
        Map<String, List<String>> data = new HashMap<>();
        try {
            Workbook workbook = new Workbook(excelFilePath);
            Worksheet sheet = workbook.getWorksheets().get(sheetName);
            int maxCell = sheet.getCells().getMaxDataColumn();
            int maxRow = sheet.getCells().getMaxDataRow();
            Cells cells = sheet.getCells();
            for (int i = 0; i <= maxCell; i++) {
                List<String> value = new ArrayList<>();
                String key = cells.get(0, i).getDisplayStringValue();
                for (int j = 1; j <= maxRow; j++) {
                    value.add(cells.get(j, i).getDisplayStringValue());
                }
                if (!key.equals("")) {
                    data.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
