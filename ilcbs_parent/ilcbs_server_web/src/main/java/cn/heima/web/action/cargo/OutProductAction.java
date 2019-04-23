package cn.heima.web.action.cargo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ReadingConverter;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.ContractProduct;
import cn.heima.service.ContractProductService;
import cn.heima.utils.DownloadUtil;
import cn.heima.utils.UtilFuns;
import cn.heima.web.action.BaseAction;

@Namespace("/cargo")
public class OutProductAction extends BaseAction{

	@Autowired
	private ContractProductService contractProductService;
	private String inputDate;
	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	@Action(value="outProductAction_toedit",results= {@Result(name="toedit",location="/WEB-INF/pages/cargo/outproduct/jOutProduct.jsp")})
	public String toedit() {
		
		return "toedit";
	}
	

	@Action(value="outProductAction_print")
	public String print() throws Exception {
		
		String path= "/make/xlsprint/tOUTPRODUCT.xlsx".replace("/",File.separator);
		String filePath = ServletActionContext.getServletContext().getRealPath(path);
		FileInputStream is = new FileInputStream(filePath);
		Workbook book = new XSSFWorkbook(is);
		Sheet sheet = book.getSheetAt(0);
//		//设置列宽
//		sheet.setColumnWidth(1, 10*256);
//		sheet.setColumnWidth(2, 25*256);
//		sheet.setColumnWidth(3, 10*256);
//		sheet.setColumnWidth(4, 10*256);
//		sheet.setColumnWidth(5, 10*256);
//		sheet.setColumnWidth(6, 10*256);
//		sheet.setColumnWidth(7, 10*256);
//		sheet.setColumnWidth(8, 10*256);
		
		int rowindex = 0;
		//大标题
		Row bigTitleRow = sheet.getRow(rowindex++);
		Cell bigCell = bigTitleRow.getCell(1);
	
		//大标题内容
		
		String titleStr = inputDate.replace("-0", "-").replace("-", "年");
		bigCell.setCellValue(titleStr);
		
		
		
		//小标题
		rowindex++;
		//内容
		//保存样式
		CellStyle cs01 = sheet.getRow(rowindex).getCell(1).getCellStyle();
		CellStyle cs02 = sheet.getRow(rowindex).getCell(2).getCellStyle();
		CellStyle cs03 = sheet.getRow(rowindex).getCell(3).getCellStyle();
		CellStyle cs04 = sheet.getRow(rowindex).getCell(4).getCellStyle();
		CellStyle cs05 = sheet.getRow(rowindex).getCell(5).getCellStyle();
		CellStyle cs06 = sheet.getRow(rowindex).getCell(6).getCellStyle();
		CellStyle cs07 = sheet.getRow(rowindex).getCell(7).getCellStyle();
		CellStyle cs08 = sheet.getRow(rowindex).getCell(8).getCellStyle();
		
		List<ContractProduct> shipTimeList = contractProductService.findCpByShipTime(inputDate);
		for (ContractProduct cpProduct : shipTimeList) {
			
				Row littelRow = sheet.createRow(rowindex++);
				littelRow.setHeightInPoints(26);
				
				
				Cell littelCell = littelRow.createCell(1);
				littelCell.setCellValue(cpProduct.getContract().getCustomName());
				littelCell.setCellStyle(cs01);
				
				Cell littelCell2 = littelRow.createCell(2);
				littelCell2.setCellValue(cpProduct.getContract().getContractNo());
				littelCell2.setCellStyle(cs02);
				
				Cell littelCell3 = littelRow.createCell(3);
				littelCell3.setCellValue(cpProduct.getProductNo());
				littelCell3.setCellStyle(cs03);
				
				Cell littelCell4 = littelRow.createCell(4);
				littelCell4.setCellValue(cpProduct.getCnumber());
				littelCell4.setCellStyle(cs04);
				
				Cell littelCell5 = littelRow.createCell(5);
				littelCell5.setCellValue(cpProduct.getFactoryName());
				littelCell5.setCellStyle(cs05);
				
				Cell littelCell6 = littelRow.createCell(6);
				littelCell6.setCellValue(UtilFuns.dateTimeFormat(cpProduct.getContract().getDeliveryPeriod()));
				littelCell6.setCellStyle(cs06);
				
				Cell littelCell7 = littelRow.createCell(7);
				littelCell7.setCellValue(UtilFuns.dateTimeFormat(cpProduct.getContract().getShipTime()));
				littelCell7.setCellStyle(cs07);
				
				Cell littelCell8 = littelRow.createCell(8);
				littelCell8.setCellValue(cpProduct.getContract().getTradeTerms());
				littelCell8.setCellStyle(cs08);
				
		}
		
		
		//写出excel
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		
		DownloadUtil download= new DownloadUtil();
		
		
		book.write(os);
		
		download.download(os, response, titleStr+".xlsx");
		
		return NONE;
	}
	
	
	
	@Action(value="outProductAction_oldPrint")
	public String oldprint() throws Exception {
		
		Workbook book = new HSSFWorkbook();
		Sheet sheet = book.createSheet();
		//设置列宽
		sheet.setColumnWidth(1, 10*256);
		sheet.setColumnWidth(2, 25*256);
		sheet.setColumnWidth(3, 10*256);
		sheet.setColumnWidth(4, 10*256);
		sheet.setColumnWidth(5, 10*256);
		sheet.setColumnWidth(6, 10*256);
		sheet.setColumnWidth(7, 10*256);
		sheet.setColumnWidth(8, 10*256);
		
		int rowindex = 0;
		//大标题
		Row bigTitleRow = sheet.createRow(rowindex++);
		bigTitleRow.setHeightInPoints(36);
		Cell bigCell = bigTitleRow.createCell(1);
		bigCell.setCellStyle(bigTitle(book));
		
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
		//大标题内容
		
		String titleStr = inputDate.replace("-0", "-").replace("-", "年");
		
		bigCell.setCellValue("2012年8月份出货表");
		
		
		
		//小标题
		String[] smartStrs = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
		Row smartRow = sheet.createRow(rowindex++);
		smartRow.setHeightInPoints(26);
		for(int i=0; i<smartStrs.length;i++) {
			Cell smartCell = smartRow.createCell(i+1);
			smartCell.setCellValue(smartStrs[i]);
			smartCell.setCellStyle(title(book));
		}
		//内容
		List<ContractProduct> shipTimeList = contractProductService.findCpByShipTime(inputDate);
		for (ContractProduct cpProduct : shipTimeList) {
			
				Row littelRow = sheet.createRow(rowindex++);
				
				Cell littelCell = littelRow.createCell(1);
				littelCell.setCellValue(cpProduct.getContract().getCustomName());
				littelCell.setCellStyle(text(book));
				
				Cell littelCell2 = littelRow.createCell(2);
				littelCell2.setCellValue(cpProduct.getContract().getContractNo());
				littelCell2.setCellStyle(text(book));
				
				Cell littelCell3 = littelRow.createCell(3);
				littelCell3.setCellValue(cpProduct.getProductNo());
				littelCell3.setCellStyle(text(book));
				
				Cell littelCell4 = littelRow.createCell(4);
				littelCell4.setCellValue(cpProduct.getCnumber());
				littelCell4.setCellStyle(text(book));
				
				Cell littelCell5 = littelRow.createCell(5);
				littelCell5.setCellValue(cpProduct.getFactoryName());
				littelCell5.setCellStyle(text(book));
				
				Cell littelCell6 = littelRow.createCell(6);
				littelCell6.setCellValue(UtilFuns.dateTimeFormat(cpProduct.getContract().getDeliveryPeriod()));
				littelCell6.setCellStyle(text(book));
				
				Cell littelCell7 = littelRow.createCell(7);
				littelCell7.setCellValue(UtilFuns.dateTimeFormat(cpProduct.getContract().getShipTime()));
				littelCell7.setCellStyle(text(book));
				
				Cell littelCell8 = littelRow.createCell(8);
				littelCell8.setCellValue(cpProduct.getContract().getTradeTerms());
				littelCell8.setCellStyle(text(book));
				
		}
		
		
		//写出excel
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		
		DownloadUtil download= new DownloadUtil();
		
		
		book.write(os);
		
		download.download(os, response, titleStr+".xls");
		
		return NONE;
	}
	
	
	//大标题的样式
		public CellStyle bigTitle(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("宋体");
			font.setFontHeightInPoints((short)16);
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);					//字体加粗
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			return style;
		}
		//小标题的样式
		public CellStyle title(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short)12);
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
			style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
			style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
			style.setBorderRight(CellStyle.BORDER_THIN);				//右细线
			
			return style;
		}
		
		//文字样式
		public CellStyle text(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short)10);
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_LEFT);					//横向居左
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
			style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
			style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
			style.setBorderRight(CellStyle.BORDER_THIN);				//右细线
			
			return style;
		}
}
