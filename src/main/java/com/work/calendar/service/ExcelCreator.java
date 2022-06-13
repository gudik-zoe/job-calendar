package com.work.calendar.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

import com.work.calendar.dto.Base64DTO;
import com.work.calendar.dto.ClientBusinessSummaryDTO;
import com.work.calendar.dto.JobsDetail;
import com.work.calendar.utility.ExcelStyle;

public class ExcelCreator {
	private Logger log = LoggerFactory.getLogger(ExcelCreator.class);
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<ClientBusinessSummaryDTO> result;
	private Calendar calendar;

	private static DecimalFormat df = new DecimalFormat("0.00");

	public ExcelCreator(List<ClientBusinessSummaryDTO> result, Calendar calendar) {
		this.result = result;
		workbook = new XSSFWorkbook();
		this.calendar = calendar;
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

	private CellStyle getStyleFromDay(int day) throws ParseException {
		Calendar cal = Calendar.getInstance();
		int month = calendar.MONTH;
		String dayDate = day + "/" + (month + 1) + "/2022";
		Date dayDateUtil = new SimpleDateFormat("dd/MM/yyyy").parse(dayDate);
		cal.setTime(dayDateUtil);
		int theDay = cal.get(Calendar.DAY_OF_WEEK);
		if (theDay == Calendar.SATURDAY || theDay == Calendar.SUNDAY) {
			return ExcelStyle.weekEndStyle(workbook);
		} else {
			return ExcelStyle.valueStyle(workbook);
		}
	}

	public void writeHeaderLine() throws ParseException {
		sheet = workbook.createSheet("summary");
		Row firstRow = sheet.createRow(0);
		

		createCell(firstRow, 18, calendar.getDisplayName(calendar.MONTH, calendar.LONG, Locale.getDefault()),
				ExcelStyle.headerStyle(workbook));
		Row row = sheet.createRow(1);
		createCell(row, 0, "CLIENTE", ExcelStyle.headerStyle(workbook));
		createCell(row, 1, "COMMESSA", ExcelStyle.headerStyle(workbook));
		createCell(row, 2, "DATA", ExcelStyle.headerStyle(workbook));
		createCell(row, 3, "FERIE", ExcelStyle.headerStyle(workbook));
		int i = 4;
		for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
			createCell(row, i, day, getStyleFromDay(day));
			i++;
		}
		createCell(row, i, "TOTALI", ExcelStyle.headerStyle(workbook));

	}

	public void writeDataLines() throws ParseException {
		int rowCount = 2;
		Calendar calendar2 = Calendar.getInstance();
		Map<Integer, Double> map = new HashMap<>();
		Map<Integer, Double> totalDayMap = new HashMap<>();
		Integer lastRow = null; 
		for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
			totalDayMap.put(day, 0.0);
		}
		for (ClientBusinessSummaryDTO clientBusinessSummaryDTO : result) {
			for (Map.Entry<String, List<JobsDetail>> entry : clientBusinessSummaryDTO.getJobs().entrySet()) {
				Row row = sheet.createRow(rowCount++);
//				row.setRowStyle(ExcelStyle.valueStyle(workbook));
				int columnCount = 0;
				createCell(row, columnCount++, clientBusinessSummaryDTO.getClientName(),
						ExcelStyle.valueStyle(workbook));
				createCell(row, columnCount++, entry.getKey(), ExcelStyle.valueStyle(workbook));
				createCell(row, columnCount++, "data ", ExcelStyle.valueStyle(workbook));
				createCell(row, columnCount++, "ferie ", ExcelStyle.valueStyle(workbook));
				map.clear();
				double total = 0;
				for (JobsDetail jobsDetail : entry.getValue()) {
					calendar2.setTime(jobsDetail.getDate());
					map.put(calendar2.get(Calendar.DAY_OF_MONTH), jobsDetail.getJobDuration());
				}
				for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
					if (map.containsKey(day)) {
						totalDayMap.put(day, totalDayMap.get(day) + map.get(day));
						total += map.get(day);
						createCell(row, columnCount++,  df.format(map.get(day)), getStyleFromDay(day));
					} else {
						totalDayMap.put(day, totalDayMap.get(day) + 0.0);
						createCell(row, columnCount++, null, getStyleFromDay(day));
					}
				}
				createCell(row, columnCount++, df.format(total), ExcelStyle.valueStyle(workbook));
				lastRow = rowCount;
			}
		}
		insertTotalDaysHoursColumn(totalDayMap , rowCount);
		for (int i = 0; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 5; i++) {
			sheet.autoSizeColumn(i);
		}

	}

	private void insertTotalDaysHoursColumn(Map<Integer, Double> totalDayMap , Integer rowCount) throws ParseException {

		Row lastRow = sheet.createRow(rowCount);
		int columnCount = 3;
		createCell(lastRow, columnCount++, "TOTALI", ExcelStyle.headerStyle(workbook));
		double totalMonthHours = 0;
		for (Map.Entry<Integer, Double> totalDayMapEntry : totalDayMap.entrySet()) {
			totalMonthHours += totalDayMapEntry.getValue();
			createCell(lastRow, columnCount++, df.format(totalDayMapEntry.getValue()),
					getStyleFromDay(totalDayMapEntry.getKey()));
		}
		createCell(lastRow, columnCount++, df.format(totalMonthHours), ExcelStyle.valueStyle(workbook));

	}

	public InputStreamSource export() throws IOException, ParseException {
		writeHeaderLine();
		writeDataLines();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		bos.close();
		workbook.close();
		 InputStreamSource attachment = new ByteArrayResource(bos.toByteArray());
	       return attachment;
//		return bos;
	}

	public Base64DTO exportToBase64(String fileName) throws IOException, ParseException {
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
