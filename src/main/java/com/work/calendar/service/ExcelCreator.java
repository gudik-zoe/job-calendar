package com.work.calendar.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.calendar.dto.Base64DTO;
import com.work.calendar.dto.BusinessDetailsDTO;
import com.work.calendar.dto.ClientBusinessSummaryDTO;
import com.work.calendar.dto.JobsDetail;

import antlr.StringUtils;

public class ExcelCreator {
	private Logger log = LoggerFactory.getLogger(ExcelCreator.class);
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<ClientBusinessSummaryDTO> result;
	private int daysOfTheMonth;

	private static DecimalFormat df = new DecimalFormat("0.00");

	public ExcelCreator(List<ClientBusinessSummaryDTO> result, int daysOfTheMonth) {
		this.result = result;
		workbook = new XSSFWorkbook();
		this.daysOfTheMonth = daysOfTheMonth;
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

		createCell(row, 0, "CLIENTE", style);
		createCell(row, 1, "COMMESSA", style);
		createCell(row, 2, "DATA", style);
		createCell(row, 3, "FERIE", style);
		int i = 4;
		for (int day = 1; day <= daysOfTheMonth; day++) {
			createCell(row, i, day, style);
			i++;
		}
		createCell(row, i, "TOTALI", style);

	}

	public void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		Calendar calendar = Calendar.getInstance();
		Map<Integer, Double> map = new HashMap<>();
		for (ClientBusinessSummaryDTO clientBusinessSummaryDTO : result) {
			for (Map.Entry<String, List<JobsDetail>> entry : clientBusinessSummaryDTO.getJobs().entrySet()) {
				Row row = sheet.createRow(rowCount++);
				int columnCount = 0;
				createCell(row, columnCount++, clientBusinessSummaryDTO.getClientName(), style);
				createCell(row, columnCount++, entry.getKey(), style);
				createCell(row, columnCount++, "data ", style);
				createCell(row, columnCount++, "ferie ", style);
				map.clear();
				double total = 0;
				for (JobsDetail jobsDetail : entry.getValue()) {
					calendar.setTime(jobsDetail.getDate());
					map.put(calendar.get(Calendar.DAY_OF_MONTH), jobsDetail.getJobDuration());
				}
				for (int day = 1; day <= daysOfTheMonth; day++) {
					if (map.containsKey(day)) {
						total += map.get(day);
						createCell(row, columnCount++, df.format(map.get(day)), style);
					} else {
						createCell(row, columnCount++, " ", style);
					}
				}
				createCell(row, columnCount++, df.format(total), style);
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
