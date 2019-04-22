package cn.heima.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

public class PoiTest {
	
	
	@Test
	public void testPoi() throws FileNotFoundException, IOException {
		
		Workbook book = new HSSFWorkbook();
		
		Sheet sheet = book.createSheet();
		
		Row row = sheet.createRow(0);
		
		Cell cell = row.createCell(1);
		
		cell.setCellValue("2012年8月份出货表");
		 
		//居中显示
		CellStyle style = book.createCellStyle();
		//横向居中
		style.setAlignment(CellStyle.ALIGN_CENTER);
		//纵向居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		
		
		book.write(new FileOutputStream("E:/297.xls"));
		book.close();
	}
}
