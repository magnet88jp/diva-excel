package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import java.io.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.*; 

import models.*;

@With(ExcelControllerHelper.class)
public class Application extends Controller {

    public static void index() {
        List<DivaCalendar> calendars = 
            DivaCalendar.find("order by category").fetch();
        render(calendars);
    }

    public static void excel() {
        List<DivaCalendar> calendars = 
            DivaCalendar.find("order by category").fetch();
        request.format = "xls";
        String __FILE_NAME__ = "calendar.xls";
        render(__FILE_NAME__, calendars);
    }

  public static void loadFromFile() {
    int row_num = 50;
    int col_num = 50;
    String [][] table = new String[row_num][col_num];

    File fname = new File("app/views/Application/excel.xls");
    FileInputStream input = null;
    BufferedInputStream binput = null;
    POIFSFileSystem poifs = null;
    try {
      input = new FileInputStream(fname);
      binput = new BufferedInputStream(input);
      poifs = new POIFSFileSystem(binput);
      
      HSSFWorkbook workbook = new HSSFWorkbook(poifs);
      HSSFSheet sheet = workbook.getSheetAt(0);
      int rows = sheet.getLastRowNum();
      rows = rows > (row_num - 1) ? row_num - 1 : rows;
      for(int i = 0;i <= rows;i++){
        HSSFRow row = sheet.getRow(i);
        if (row == null) continue;
        int cols = row.getLastCellNum();
        cols = cols >= (col_num - 1) ? col_num - 1 : cols;
        for(int j = 0;j <= cols;j++){
          HSSFCell cell = row.getCell((short)j);
          if (cell == null) continue;
          if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
            HSSFRichTextString val = cell.getRichStringCellValue();
            table[i][j] = val.toString();
          }
          if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
            double val = cell.getNumericCellValue();
            table[i][j] = Double.toString(val);
          }
        }
      }
     Logger.info("1-1=" + table[1][1]);
     Logger.info("1-3=" + table[1][3]);
     Logger.info("2-3=" + table[2][3]);
     Logger.info("3-1=" + table[3][1]);
     Logger.info("3-2=" + table[3][2]);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        binput.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
