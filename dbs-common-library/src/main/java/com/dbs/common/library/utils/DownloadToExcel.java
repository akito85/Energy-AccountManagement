package com.dbs.common.library.utils;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ExcelTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class DownloadToExcel {

    private DownloadToExcel() {}
    
    private static final Logger logger = LoggerFactory.getLogger(DownloadToExcel.class);

    public static List<Map<String, Object>> getListOfObjectToListOfHashMap(List<?> objects) throws JsonProcessingException {
        List<Map<String, Object>> list = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("dd-MMM-yyyy hh-mm-ss").create();
        for (Object object : objects) {
            String temp = gson.toJson(object);
            list.add(new ObjectMapper().readValue(temp, Map.class));
        }
        return list;
    }

//    public static ByteArrayInputStream downloadsFiles(List<?> objects, String fileType) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        List<Map<String, Object>> list = getListOfObjectToListOfHashMap(objects);
//        String[] columns = getColumnsNameFromListOfObject(objects);
//        try {
//            if (fileType.equals("Excel")) {
//                generateExcel(list, columns, out);
//            }
//        } catch (Exception ex) {
//            logger.error(Constant.LOG_ERROR, ex.getMessage());
//        }
//        return new ByteArrayInputStream(out.toByteArray());
//    }

    @SuppressWarnings("java:S1172")
    public static ByteArrayInputStream downloadsMultiSheetUsage(
            List<?> listTemplate, List<?> listBillingPeriod,
            List<?> listSource, String fileType) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String[] column1 = null;
        String[] column3 = null;
        String[] column5 = null;
        List<Map<String, Object>> list1 = getListOfObjectToListOfHashMap(listTemplate);
        List<Map<String, Object>> list3 = getListOfObjectToListOfHashMap(listBillingPeriod);
        List<Map<String, Object>> list5 = getListOfObjectToListOfHashMap(listSource);
        column1 = getColumnsNameFromListOfObject(listTemplate);
        if(!listBillingPeriod.isEmpty()){
            column3 = getColumnsNameFromListOfObject(listBillingPeriod);
        }
        if(!listSource.isEmpty()){
            column5 = getColumnsNameFromListOfObject(listSource);
        }
        try {
            writeToExcelInMultiSheets(list1, list3, list5,
                                    column1, column3, column5, out);

        } catch (Exception ex) {
            logger.error(Constant.LOG_ERROR, ex.getMessage(), ex);
        }
        return new ByteArrayInputStream(out.toByteArray());

    }

    @SuppressWarnings({"java:S3776", "java:S6541", "java:S2095"})
    public static void writeToExcelInMultiSheets(
            List<Map<String, Object>> list1,
            List<Map<String, Object>> list3,
            List<Map<String, Object>> list5,
            String[] column1,
            String[] column3,
            String[] column5,
            ByteArrayOutputStream out) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("Usage Input");
        Font headerFont1 = workbook.createFont();
        headerFont1.setBold(true);
        headerFont1.setColor(IndexedColors.BLACK1.getIndex());
        CellStyle headerCellStyle1 = workbook.createCellStyle();
        headerCellStyle1.setFont(headerFont1);
        Row headerRow1 = sheet1.createRow(0);
        for (int col = 0; col < column1.length; col++) {
            Cell cell = headerRow1.createCell(col);
            cell.setCellValue(column1[col]);
            cell.setCellStyle(headerCellStyle1);
        }
        int rowIdx1 = 1;
        for (int k = 0; k < list1.size(); k++) {
            Row row = sheet1.createRow(rowIdx1++);
            for (Map.Entry<String, Object> entry : list1.get(k).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                for (int col = 0; col < column1.length; col++) {
                    if (key.toString().equals(column1[col])) {
                        if (value == null) {
                            row.createCell(col).setCellValue("");
                        } else {
                            row.createCell(col).setCellValue(value.toString());
                        }
                    }
                }
            }
        }

        Sheet sheet3 = workbook.createSheet("Billing Period");
        Font headerFont3 = workbook.createFont();
        headerFont3.setBold(true);
        headerFont3.setColor(IndexedColors.BLACK1.getIndex());
        CellStyle headerCellStyle3 = workbook.createCellStyle();
        headerCellStyle3.setFont(headerFont3);
        Row headerRow3 = sheet3.createRow(0);
        for (int col = 0; col < column3.length; col++) {
            Cell cell = headerRow3.createCell(col);
            cell.setCellValue(column3[col]);
            cell.setCellStyle(headerCellStyle3);
        }
        int rowIdx3 = 1;
        for (int k = 0; k < list3.size(); k++) {
            Row row = sheet3.createRow(rowIdx3++);
            for (Map.Entry<String, Object> entry : list3.get(k).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                for (int col = 0; col < column3.length; col++) {
                    if (key.toString().equals(column3[col])) {
                        if (value == null) {
                            row.createCell(col).setCellValue("");
                        } else {
                            row.createCell(col).setCellValue(value.toString());
                        }
                    }
                }
            }
        }

        if(!ObjectUtils.isEmpty(column5)){
            Sheet sheet5 = workbook.createSheet("LoV Source");
            Font headerFont5 = workbook.createFont();
            headerFont5.setBold(true);
            headerFont5.setColor(IndexedColors.BLACK1.getIndex());
            CellStyle headerCellStyle5 = workbook.createCellStyle();
            headerCellStyle5.setFont(headerFont5);
            Row headerRow5 = sheet5.createRow(0);
            for (int col = 0; col < column5.length; col++) {
                Cell cell = headerRow5.createCell(col);
                cell.setCellValue(column5[col]);
                cell.setCellStyle(headerCellStyle5);
            }
            int rowIdx5 = 1;
            for (int k = 0; k < list5.size(); k++) {
                Row row = sheet5.createRow(rowIdx5++);
                for (Map.Entry<String, Object> entry : list5.get(k).entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    for (int col = 0; col < column5.length; col++) {
                        if (key.toString().equals(column5[col])) {
                            if (value == null) {
                                row.createCell(col).setCellValue("");
                            } else {
                                row.createCell(col).setCellValue(value.toString());
                            }
                        }
                    }
                }
            }
        }
        workbook.write(out);
        logger.info("workbook : {}", workbook);
    }

    public static ByteArrayInputStream downloadsFiles2(List<?> object1, List<?> object2, String fileType) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Map<String, Object>> list1 = getListOfObjectToListOfHashMap(object1);
        List<Map<String, Object>> list2 = getListOfObjectToListOfHashMap(object2);
        String[] column1 = getColumnsNameFromListOfObject(object1);
        String[] column2 = getColumnsNameFromListOfObject(object2);
        try {
            if (fileType.equals("Excel")) {
                generateExcel2(list1, list2, column1, column2, out);
            }
        } catch (Exception ex) {
            logger.error(Constant.LOG_ERROR, ex.getMessage(), ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static String[] getColumnsNameFromListOfObject(List<?> objects) throws JsonProcessingException {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("dd-MMM-yyyy hh:mm:ss").create();
        String strObjects = gson.toJson(objects.get(0));
        StringBuilder header = new StringBuilder();
        Map<String, Object> data = new ObjectMapper().readValue(strObjects, Map.class);

        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                header = new StringBuilder(String.valueOf(data.keySet().toArray()[i]));
            } else {
                header.append(",").append(data.keySet().toArray()[i]);
            }
        }
        return header.toString().split(",");
    }

//    @SuppressWarnings({"java:S3776", "java:S2095"})
//    public static void generateExcel(List<Map<String, Object>> list, String[] columns, ByteArrayOutputStream out) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Sheet1");
//        Font headerFont = workbook.createFont();
//        headerFont.setBold(true);
//        headerFont.setColor(IndexedColors.BLACK.getIndex());
//        CellStyle headerCellStyle = workbook.createCellStyle();
//        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
//        headerCellStyle.setFont(headerFont);
//        Row headerRow = sheet.createRow(0);
//        for (int col = 0; col < columns.length; col++) {
//            Cell cell = headerRow.createCell(col);
//            cell.setCellValue(columns[col]);
//            cell.setCellStyle(headerCellStyle);
//        }
//        CellStyle centeredCellStyle = workbook.createCellStyle();
//        centeredCellStyle.setAlignment(HorizontalAlignment.CENTER);
//        sheet.setColumnWidth(0, 1000);
//        for (int col = 1; col < columns.length; col++) {
//            sheet.setColumnWidth(col, 4000);
//        }
//        int rowIdx = 1;
//        for (int k = 0; k < list.size(); k++) {
//            Row row = sheet.createRow(rowIdx++);
//            for (Map.Entry<String, Object> entry : list.get(k).entrySet()) {
//                Object key = entry.getKey();
//                Object value = entry.getValue();
//                for (int col = 0; col < columns.length; col++) {
//                    if (key.toString().equals(columns[col])) {
//                        Cell cell = row.createCell(col);
//                        if (value == null) {
//                            cell.setCellValue("");
//                        } else {
//                            cell.setCellValue(value.toString());
//                        }
//                        if (col == 0) {
//                            cell.setCellStyle(centeredCellStyle);
//                        }
//                    }
//                }
//            }
//        }
//        workbook.write(out);
//    }
    
    private static Cell createCell(Object e, Row row, Integer col){
        Cell cell;
        if (e instanceof Integer) {
            // Do Integer things
            cell = row.createCell(col,NUMERIC);
            cell.setCellValue((Integer) e);
            return cell;
        } else if (e instanceof String) {
            // Do String things
            cell = row.createCell(col,STRING);
            cell.setCellValue((String) e);
            return cell;
        } else if (e instanceof Long) {
            // Do Long things
            cell = row.createCell(col,NUMERIC);
            cell.setCellValue((Long) e);
            return cell;
        } else if (e instanceof BigDecimal) {
            // Do other thing, probably want error or print statement
            cell = row.createCell(col,NUMERIC);
            cell.setCellValue(Double.parseDouble(e.toString()));
            return cell;
        } else if (e instanceof Double) {
            // Do other thing, probably want error or print statement
            cell = row.createCell(col,NUMERIC);
            cell.setCellValue(Double.parseDouble(e.toString()));
            return cell;
        }
        else if (e instanceof Date){
            // Do other thing, probably want error or print statement
            cell = row.createCell(col,NUMERIC);
            cell.setCellValue((Date) e);
            return cell;
        } else {
            // Do other thing, probably want error or print statement
            cell = row.createCell(col,BLANK);
            cell.setCellValue("");
            return cell;
        }
        
    }
    
    private static Object getValueByType(Cell cell, Object e){
        if (e instanceof Integer) {
            // Do Integer things
            return getCellValue(cell, ExcelTypeEnum.INTEGER, Boolean.FALSE);
        } else if (e instanceof String) {
            // Do String things
            return getCellValue(cell, ExcelTypeEnum.STRING, Boolean.FALSE);
        } else if (e instanceof Long) {
            // Do Long things
            return getCellValue(cell, ExcelTypeEnum.INTEGER, Boolean.FALSE);
        } else if (e instanceof BigDecimal) {
            // Do other thing, probably want error or print statement
            return getCellValue(cell, ExcelTypeEnum.BIGDECIMAL, Boolean.FALSE);
        } else if (e instanceof Double) {
            // Do other thing, probably want error or print statement
            return getCellValue(cell, ExcelTypeEnum.DOUBLE, Boolean.FALSE);
        } else {
            // Do other thing, probably want error or print statement
            return getCellValue(cell, ExcelTypeEnum.DATE, Boolean.TRUE);
        }
    }

    @SuppressWarnings({"java:S3776", "java:S2095"})
    public static void generateExcel2(List<Map<String, Object>> list1, List<Map<String, Object>> list2, String[] column1,
                                      String[] column2, ByteArrayOutputStream out) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("Sheet1");
        Font headerFont1 = workbook.createFont();
        headerFont1.setBold(true);
        headerFont1.setColor(IndexedColors.BLUE.getIndex());
        CellStyle headerCellStyle1 = workbook.createCellStyle();
        headerCellStyle1.setFont(headerFont1);
        Row headerRow1 = sheet1.createRow(0);
        for (int col = 0; col < column1.length; col++) {
            Cell cell = headerRow1.createCell(col);
            cell.setCellValue(column1[col]);
            cell.setCellStyle(headerCellStyle1);
        }
        int rowIdx1 = 1;
        for (int k = 0; k < list1.size(); k++) {
            Row row = sheet1.createRow(rowIdx1++);
            for (Map.Entry<String, Object> entry : list1.get(k).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                for (int col = 0; col < column1.length; col++) {
                    if (key.toString().equals(column1[col])) {
                        if (value == null) {
                            row.createCell(col).setCellValue("-");
                        } else {
                            row.createCell(col).setCellValue(value.toString());
                        }
                    }
                }
            }
        }

        Sheet sheet2 = workbook.createSheet("Sheet2");
        Font headerFont2 = workbook.createFont();
        headerFont2.setBold(true);
        headerFont2.setColor(IndexedColors.BLUE.getIndex());
        CellStyle headerCellStyle2 = workbook.createCellStyle();
        headerCellStyle2.setFont(headerFont2);
        Row headerRow2 = sheet2.createRow(0);
        for (int col = 0; col < column2.length; col++) {
            Cell cell = headerRow2.createCell(col);
            cell.setCellValue(column2[col]);
            cell.setCellStyle(headerCellStyle2);
        }
        int rowIdx = 1;
        for (int k = 0; k < list2.size(); k++) {
            Row row2 = sheet2.createRow(rowIdx++);
            for (Map.Entry<String, Object> entry : list2.get(k).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                for (int col = 0; col < column2.length; col++) {
                    if (key.toString().equals(column2[col])) {
                        if (value == null) {
                            row2.createCell(col).setCellValue("-");
                        } else {
                            row2.createCell(col).setCellValue(value.toString());
                        }
                    }
                }
            }
        }
        workbook.write(out);
    }
    
    
    public static Object getCellValue(Cell cell, ExcelTypeEnum type, Boolean isMeasDate) {

        if (cell == null) {
            return null;
        }

        if(cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue().equals("")){
            return null;
        }

        if(cell.getCellType().equals(CellType.BLANK)){
            return null;
        }

        switch (cell.getCellType()) {
            case BLANK:
                return null;
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (type != null && type == ExcelTypeEnum.INTEGER) {
                    return cell.getNumericCellValue();
                } else if (type != null && type == ExcelTypeEnum.DATE) {
                    try {
                        return cell.getDateCellValue();
//                        if(isMeasDate) {
//                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//                            return sdf.format(cell.getDateCellValue());
//                        }else{
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                            return sdf.format(cell.getDateCellValue());
//                        }
                    } catch (Exception e) {
                        return null;
                    }
                } else if(type != null && type == ExcelTypeEnum.BIGDECIMAL) {
                    Double d = (Double) cell.getNumericCellValue();
                     if (d % 1 == 0) {
                        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
                        return Double.valueOf(decimalFormat.format(d));
                    } else {
                       return cell.getNumericCellValue();
                    }
                    
                } else {
                    Double d = (Double) cell.getNumericCellValue();
                    if (d % 1 == 0) {
                        return d.intValue();
                    } else {
                        DecimalFormat decimalFormat = new DecimalFormat("##0.0");
                        return Double.valueOf(decimalFormat.format(d));
                    }
                }

            case FORMULA:
                return new BigDecimal(cell.getNumericCellValue());
            default:
                return "";
        }
    }
    
    public static ByteArrayInputStream downloadsFiles(List<?> objects, String fileType) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Map<String, Object>> list = getListOfObjectToListOfHashMap(objects);
        String[] columns = getColumnsNameFromListOfObject(objects);
        try {
            if (fileType.equals("Excel")) {
                generateExcel(list, columns, out);
            }
        } catch (Exception ex) {
            logger.error(Constant.LOG_ERROR, ex.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
    
    
    public static void generateExcel(List<Map<String, Object>> list, String[] columns, ByteArrayOutputStream out) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle centeredCellStyle = workbook.createCellStyle();
        centeredCellStyle.setAlignment(HorizontalAlignment.CENTER);
        sheet.setColumnWidth(0, 1000);
        for (int col = 1; col < columns.length; col++) {
            sheet.setColumnWidth(col, 4000);
        }
        int rowIdx = 1;
        for (int k = 0; k < list.size(); k++) {
            Row row = sheet.createRow(rowIdx++);
            for (Map.Entry<String, Object> entry : list.get(k).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                for (int col = 0; col < columns.length; col++) {
                    if (key.toString().equals(columns[col])) {
                        Cell cell = createCell(value, row, col);
                        if (value == null) {
                            cell.setCellValue("");
                        } else {
                            Object obj = getValueByType(cell, value);
                            if (obj instanceof Date) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                cell.setCellValue(sdf.format((Date) obj));
                            }  else if (obj instanceof Number) {
                                if (obj instanceof BigDecimal) {
                                    cell.setCellValue(((BigDecimal) obj).doubleValue());
                                } else if (obj instanceof Double) {
                                    cell.setCellValue((Double) obj);
                                } else {
                                    cell.setCellValue((Integer) obj);
                                }
                            } else {
                                cell.setCellValue((String) obj);
                            }
                        }
                        if (col == 0) {
                            cell.setCellStyle(centeredCellStyle);
                        }
                    }
                }
            }
        }
        workbook.write(out);
    }

}
