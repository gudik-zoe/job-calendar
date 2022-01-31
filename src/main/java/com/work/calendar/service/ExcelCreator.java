package com.work.calendar.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;

import com.work.calendar.dto.Base64DTO;
import com.work.calendar.dto.BusinessDetailsDTO;
import com.work.calendar.dto.ClientBusinessSummaryDTO;

import antlr.StringUtils;

public class ExcelCreator {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<ClientBusinessSummaryDTO> result;

	public ExcelCreator(List<ClientBusinessSummaryDTO> result) {
		this.result = result;
		workbook = new XSSFWorkbook();
	}

	public void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		}
		cell.setCellStyle(style);
	}

	public void writeHeaderLine() {
		sheet = workbook.createSheet("summary");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "nome cliente", style);
		createCell(row, 1, "nome commessa", style);
		createCell(row, 2, "data", style);
		createCell(row, 3, "durata", style);
		createCell(row, 4, "totale per il cliente", style);

	}

	public void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (ClientBusinessSummaryDTO clientBusinessSummaryDTO : result) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, clientBusinessSummaryDTO.getClientName(), style);
			createCell(row, columnCount+3, clientBusinessSummaryDTO.getTotalHoursForClient(), style);
			for (BusinessDetailsDTO businessDetailsDTO : clientBusinessSummaryDTO.getBusinessDetails()) {
				Row row2 = sheet.createRow(rowCount++);
				int columnCount2 = 1;
				createCell(row2, columnCount2++, businessDetailsDTO.getJobDescription(), style);
				createCell(row2, columnCount2++, businessDetailsDTO.getDate().toString().substring(0,16), style);
				createCell(row2, columnCount2++, businessDetailsDTO.getJobDuration(), style);

			}
		}
	}

	public ByteArrayOutputStream export() throws IOException {
		writeHeaderLine();
		writeDataLines();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		bos.close();
		workbook.close();
		return bos;
	}

	public Base64DTO exportToBase64(String fileName) throws IOException {
		writeHeaderLine();
		writeDataLines();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		bos.close();
		workbook.close();
		byte[] bytes = bos.toByteArray();
		Base64DTO excelDTO = new Base64DTO(fileName, Base64.encodeBase64String(bytes), ".xls");
		return excelDTO;

	}
}
