package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sf.json.JSONObject;
import play.Play;

public class ExcelUtils {

	/**
	 * @param sheetName  表名
	 * @param list		  填充内容list集合
	 * @param titles	  表头
	 * @param fieldNames 类的属性（便于单元格内容的填充）
	 * @return
	 */
	public static <T> File export(String sheetName, List<T> list, String[] titles, String[] fieldNames) {
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFSheet sheet = null;
		
		// 对每个表生成一个新的sheet,并以表名命名
		if(sheetName == null){
			sheetName = "sheet1";
		}
		
		sheet = wb.createSheet(sheetName);
		
		// 设置表头的说明
		HSSFRow topRow = sheet.createRow(0);

		for(int i = 0; i < titles.length; i++){
			setCellGBKValue(topRow.createCell(i), titles[i]);
			sheet.autoSizeColumn(i);
		}
		
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i+1);
			JSONObject obj = JSONObject.fromObject(list.get(i));
			
			for(int j = 0; j < fieldNames.length; j++){
				setCellGBKValue(row.createCell(j), obj.get(fieldNames[j])+"");
			}
		}
		
		String path = Play.getFile("/tmp/").getAbsolutePath();
    	String filename = UUID.randomUUID().toString() + ".xls";
    	File file = new File(path + "/" + filename);

    	try {
    		OutputStream os = new FileOutputStream(file);
    		wb.write(os);
    		os.flush();
    		os.close();
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
		
		return file;
	}
	
	/**
	 * 合并单元格
	 * @param startChar 开始字符，比如A2，就是A
	 * @param endChar 结束字符，比如B3，就是B
	 * @param startRow 开始行，比如A2，就是2
	 * @param startCol 开始列，比如A2，就是1，A是第一列
	 * @param rows 合并的行数
	 * @param cols 合并的列数
	 * @param value 合并后的值
	 * @param style 合并后单元格的样式
	 * @param sheet 需要合并的sheet
	 */
	private static void mergeCells(String startChar, String endChar, int startRow, int startCol, int rows, int cols, String value, CellStyle style, Sheet sheet){
		String regionArea = startChar + startRow + ":" + endChar + (startRow + rows -1 );
		if(rows > 1){
			CellRangeAddress region = CellRangeAddress.valueOf(regionArea);
			sheet.addMergedRegion(region);
		}
		Cell firstCell = sheet.getRow(startRow - 1).getCell(startCol-1);
		if(firstCell == null){
			firstCell = sheet.getRow(startRow - 1).createCell(startCol - 1);
		}
		firstCell.setCellValue(value);
		firstCell.setCellStyle(style);
	}

	@SuppressWarnings("deprecation")
	private static void setCellGBKValue(Cell cell, String value) {
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		// 设置CELL的编码信息
//		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(value);
	}

}
