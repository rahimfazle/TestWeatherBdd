package com.apiTesting.TestAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("rawtypes")
public class ExcelDataSheetValidationPOI {
	
	public int index = 0;
	
	public interface RowHandler{
		boolean handleRow(Map rowMap);
	}
	
	public Workbook getExcelWorkbook(String fileName) {
		
		Workbook newWorkbook = null;
		try {
			File file = new File(fileName);
			String fileName1 = file.getName();
			FileInputStream inputStream = new FileInputStream(file);
			String fileExtensionName = fileName1.substring(fileName1.indexOf("."));
			if(fileExtensionName.equals(".xlsx")) {
				newWorkbook = new XSSFWorkbook(inputStream);
			}
			else if(fileExtensionName.equals(".xls")) {
				newWorkbook = new XSSFWorkbook(inputStream);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return newWorkbook;
	}
	
	public void readExcelData(String fileName, String sheetName, RowHandler rowHandler) {
		
		List<String> headerValues = new ArrayList<String>();		// <String>
		List<String> rowValues = null;								// <String>
		
		try {
			FileInputStream file = new FileInputStream(new File(fileName));
			
			Workbook workbook = getExcelWorkbook(fileName);
			Sheet sheet = (Sheet) workbook.getSheet(sheetName);
			
			Row rw = (sheet.getRow(0));
			for(org.apache.poi.ss.usermodel.Row temp : sheet) {
				rowValues = new ArrayList<String>();
				
				for(int i=0; i<rw.getLastCellNum(); i++) {
					if(temp.getRowNum() == 0) {
						String headerData;
						if(temp.getCell(i) != null) {
							headerData = temp.getCell(i).getStringCellValue();
						} else {
							headerData = null;
						}
						headerValues.add(headerData);
					} else {
						String rowData;
						if(temp.getCell(i) != null) {
							rowData = (String)temp.getCell(i).getStringCellValue().toString();
						} else {
							rowData =null;
						}
						rowValues.add(rowData);
					}
				}
				if(temp.getRowNum() == 0) {
					continue;
				}
				Map<String, String> rowMap = new HashMap<String, String>();
				for(int ixval = 0; ixval < rw.getLastCellNum(); ixval++) {
					if(ixval >= rowValues.size()) {
						break;
					}
					rowMap.put(headerValues.get(ixval), rowValues.get(ixval));
				}
				if(rowHandler.handleRow(rowMap)) {
					break;
				}
			}file.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public class RowObjectBuilder implements RowHandler {
		
		private Object[][] objs;
		
		public RowObjectBuilder(Object[][] objs) {
			this.objs = objs;
		}
		public boolean handleRow(Map row) {
			objs[index][0] = row;
			index++;
			return false;
		}
	}
	
	public Object[][] readFileSheet(String fileName, String sheetName) throws Exception {
		
		Object[][] objArrays = new HashMap[500][1];
		Object[][] returnableArrays = null;
		int rowsCount = 0;
		readExcelData(fileName, sheetName, new RowObjectBuilder(objArrays));
		for(int rows = 0; rows < 500; rows++) {
			if(objArrays[rows][0] != null) {
				rowsCount++;
			}
		}
		returnableArrays = new HashMap[rowsCount][1];
		for(int rows = 0; rows < rowsCount; rows++) {
			returnableArrays[rows][0] = objArrays[rows][0];
		}
		return returnableArrays;
	}
}
