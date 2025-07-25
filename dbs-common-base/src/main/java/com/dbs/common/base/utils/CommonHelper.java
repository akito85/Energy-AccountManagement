package com.dbs.common.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonHelper {

    private CommonHelper() {}

    private static final Logger logger = LoggerFactory.getLogger(CommonHelper.class);

    public static Map<String, Object> convertJsonMapper(String json) {
        Map<String, Object> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(json, Map.class);
        } catch (IOException e) {
            logger.error("error : {}", e.getMessage());
        }
        return result;
    }
    public static double floorNumber(double number) {
        return Math.floor(number);
    }

    public static String convertToJsonString(Map<String,Object> json){
        Gson gson = new Gson();
        return gson.toJson(json,HashMap.class);
    }

    public static String convertToJsonString(JsonObject json){
        Gson gson = new Gson();
        return gson.toJson(json,JsonObject.class);
    }

    public static String convertDateToString(String format, Date date) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    public static String getMD5ForFile(File datafile) throws NoSuchAlgorithmException, IOException {
        String md5Digest = null;
        MessageDigest digest = MessageDigest.getInstance("MD5");
        try (FileInputStream inputStream = new FileInputStream(datafile)){
            byte[] bytesBuffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead);
            }

            byte[] messageDigest = digest.digest();
            BigInteger number = new BigInteger(1, messageDigest);
            md5Digest = number.toString(16);
            md5Digest = appendField(md5Digest, "0", 1, 32);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } 

        return md5Digest;
    }

    public static String appendField(String field, String appender, int appendPos, int maxFieldLen) {
        if (appendPos == 1) { //add
            return StringUtils.leftPad(field, maxFieldLen, appender);
        } else if (appendPos == 2) {
            return StringUtils.rightPad(field, maxFieldLen, appender);
        } else {
            return field;
        }
    }

    public static Date convertStringToDate(String format, String date) {
        if (StringUtils.isEmpty(date))
            return null;

        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    public static String formatDate(String inputDateString, String inputFormatDate, String outputFormatDate) {
        try {
            // Parse the input string into a Date object
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputFormatDate);
            Date date = inputFormat.parse(inputDateString);

            // Format the Date object into a string with the desired format
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Handle the exception if the input string is not in the expected format
            e.printStackTrace();
            return "";
        }
    }

    public static String convertStringToString(String formatInput, String formatOutout, String date) {
        try {
            Date tmp = new SimpleDateFormat(formatInput).parse(date);
            return convertDateToString(formatOutout, tmp);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format("%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    public static String removeSpecialChar(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String removeNonPrintableChar(String input) {
        return input.replaceAll("[^\\x00-\\x7F]", " ").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ").replaceAll("\\p{C}", " ");
    }

    @SuppressWarnings("java:S6397")
    public static String[] getStep(String string){
        return string.split("[,]");
    }

    public static String getFilenameProcess(String filenameInput) {
        return filenameInput.replace(".", "")
                .replace("-", "")
                .replace("_", "")
                .replace(":", "")
                .replace(";", "");
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
    
    public static <T> Predicate<T> distinctByName(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static boolean writeBuffered(List<String> records, int bufSize, String fullname) throws IOException {
        boolean result = false;
        File file = new File(fullname);
        BufferedWriter bufferedWriter = null;
        try (FileWriter writer = new FileWriter(file)){
            bufferedWriter = new BufferedWriter(writer, bufSize);
            write(records, bufferedWriter);
            result = true;
        } catch (Exception e) {
            logger.error("error writeBuffered {}",e.getMessage());
        } finally {
            // comment this out if you want to inspect the files afterward
            Path path = file.toPath();
            cleanUp(path);
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
        return result;
    }
    
    public static void cleanUp(Path path) throws IOException {
        Files.delete(path);
      }

    public static void write(List<String> records, Writer writer) throws IOException {
        try {
            for (String record_ : records) {
                writer.write(record_ + "\n");
            }
        } catch (Exception e) {
            logger.error("error write {}", e.getMessage());
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    public static void deleteDirectoryIfExists(File file) throws IOException {
        
        try {
            File[] list = file.listFiles();
            if (list != null) {
                for (File temp : list) {
                    //recursive delete
                    deleteDirectoryIfExists(temp);
                }
            }
        } catch (Exception e) {
           logger.error("error deleteDirectoryIfExists {}",e.getMessage());
        } finally {
            Path path = file.toPath();
            cleanUp(path);
        }
    }

    public static String convertFilenameFormat(String filename) {
        String regex = "[0-9\\(\\)]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(filename);
        String result = filename;
        while (matcher.find()) {
            if (matcher.group().length() == 14) {
                result = filename;
            } else if (matcher.group().length() == 17) {
                result = filename.replace(matcher.group(), "yyyyMMddHHmmssSSS");
            } else {
                result = filename;
            }
        }
        return result;
    }


    public static Object strToIntegerConvert(Object input) {
        try {
            return Integer.valueOf(String.valueOf(input));
        } catch (NumberFormatException e) {
            return input;
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.of("Asia/Jakarta"))
                        .toInstant());
    }

    public static String getTableNameFromClass(Class<?> obj){
        try{
            String tableName = obj.getName();
            tableName = tableName.substring(tableName.lastIndexOf ('.') + 1);
            return tableName;
        }
        catch(Exception e) {
            return "ERR";
        }

    }

    public static String dateToString(String format, Date date) {
        if (date == null)
            return "";

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String convertNullValueToString(String input) {
        return ObjectUtils.isEmpty(input) ? "" : input;
    }

    public static <T> T convertJsonStringToObject(Class<T> clazz, String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal convertStringAmountToBigDecimal(String amount) {
        // Create a DecimalFormat instance with the desired format
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.###", new DecimalFormatSymbols(Locale.US));
        try {
            // Remove both dot and comma and convert to a BigDecimal
            String cleanedAmount = amount.replace(".", "").replace(",", ".");
            return new BigDecimal(decimalFormat.parse(cleanedAmount).toString());
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isValidExtensionByMultipartFile(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile.getOriginalFilename()))
            return false;
        // utk sementara, return true utk ext file dgn type number
        if (StringUtils.isNumeric(FilenameUtils.getExtension(multipartFile.getOriginalFilename())))
            return true;

        return FilenameUtils.getExtension(multipartFile.getOriginalFilename()).equalsIgnoreCase(Constant.TXT)
                || FilenameUtils.getExtension(multipartFile.getOriginalFilename()).equalsIgnoreCase(Constant.FTR)
                || FilenameUtils.getExtension(multipartFile.getOriginalFilename()).equalsIgnoreCase(Constant.CSV)
                || Objects.equals(FilenameUtils.getExtension(multipartFile.getOriginalFilename()), "");
    }

    public static String convertObjectToJsonString(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

    public static boolean isNotValidEmail(String email) {
        return !EmailValidator.getInstance().isValid(email);
    }

    public static String getDetailAccountNumberFromMt940(String input, String regex) {
        if (StringUtils.isEmpty(input))
            return "";
        // Define a regular expression to match "FFFFFF" followed by digits
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find())
            // Extract the matched number value
            return matcher.group(1);
        return "";
    }

    public static List<String> getListOfMonths(){
        DateFormatSymbols dfs = new DateFormatSymbols();
        return Arrays.asList(dfs.getShortMonths());
    }

    public static String getConvertedCurrency(String currency) {
        if (currency.equalsIgnoreCase(CurrencyEnum.IDR.name()))
            return CurrencyEnum.USD.name();
        return CurrencyEnum.IDR.name();
    }

    public static BigDecimal getCalculateLateCharge(BigDecimal constant, Integer totalLate, String operation) {
        if (ObjectUtils.isEmpty(constant) || ObjectUtils.isEmpty(totalLate) || ObjectUtils.isEmpty(operation)) {
            throw new IllegalArgumentException("Parameter must not be null or empty.");
        }

        BigDecimal totalResult = constant;

        switch (operation) {
            case "MULTIPLY":
                totalResult = totalResult.multiply(new BigDecimal(totalLate));
                break;
            case "DIVIDE":
                if (totalLate == 0)
                    return totalResult;

                totalResult = totalResult.divide(new BigDecimal(totalLate), RoundingMode.HALF_UP);
                break;
            case "ADDITION":
                totalResult = totalResult.add(new BigDecimal(totalLate));
                break;
            case "SUBTRACT":
                totalResult = totalResult.subtract(new BigDecimal(totalLate));
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }

        return totalResult;
    }

    public static BigDecimal calculateBillAmount(String operation, BigDecimal totalAmount, BigDecimal totalBill) {
        if (ObjectUtils.isEmpty(totalAmount) || ObjectUtils.isEmpty(totalBill) || ObjectUtils.isEmpty(operation))
            throw new IllegalArgumentException("Parameter calculate bill amount must not be null or empty!");

        switch (operation) {
            case "MULTIPLY":
                totalAmount = totalAmount.multiply(totalBill);
                break;
            case "DIVIDE":
                if (totalBill.compareTo(BigDecimal.ZERO) == 0)
                    return totalAmount;

                totalAmount = totalAmount.divide(totalBill, RoundingMode.HALF_UP);
                break;
            case "ADDITION":
                totalAmount = totalAmount.add(totalBill);
                break;
            case "SUBTRACT":
                totalAmount = totalAmount.subtract(totalBill);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }

        return totalAmount;
    }

    public static String getMessageNotFound(Long id) {
        return " with id " + id + " not found";
    }

    public static String getAmountFormat(BigDecimal amount, String currency) {
        String format;
        if (Objects.isNull(amount) || amount.equals(BigDecimal.ZERO))
            return "0.00";

        if (currency.equalsIgnoreCase(CurrencyEnum.IDR.name())) {
            NumberStyleFormatter formatter = new NumberStyleFormatter();
            formatter.setPattern("#,##0.00");
            format = formatter.print(amount, Locale.forLanguageTag("ID"));
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            format = decimalFormat.format(amount);
        }

        // Format the BigDecimal to String
        return format;
    }

    public static String getAllocationType(String input) {
        String allocationType = "";
        if (StringUtils.isBlank(input))
            return allocationType;

        if (input.equalsIgnoreCase("C"))
            allocationType = "Credit";
        else
            allocationType = "Debit";

        return allocationType;
    }

    public static String getAllocationStatus(BigDecimal allocationAmount, BigDecimal billingItemAmount) {
        return allocationAmount.compareTo(billingItemAmount) >= 0 ? Constant.PAID : Constant.PARTIALLY_PAID;
    }

    public static String getPaymentStatusBilling(Double paidAmount, BigDecimal receiptAmount) {
        String status = "";
        BigDecimal paidAmountDecimal = BigDecimal.valueOf(paidAmount);
        if (receiptAmount.compareTo(paidAmountDecimal) < 0) {
            status = Constant.PARTIALLY_PAID;
        } else if (receiptAmount.compareTo(paidAmountDecimal) == 0 || receiptAmount.compareTo(paidAmountDecimal) > 0) {
            status = Constant.PAID;
        }

        return status;
    }

    public static String getNpwpFormat(String npwp) {
        return npwp.substring(0, 2) + '.' +
                npwp.substring(2, 5) + '.' +
                npwp.substring(5, 8) + '.' +
                npwp.charAt(8) + '-' +
                npwp.substring(9, 12) + '.' +
                npwp.substring(12);
    }

    @SuppressWarnings("java:S2447")
    public static Boolean convertYNtoBoolean(Character s) {
        if (s == null) {
            return null;
        }
        if (s.equals('Y') || s.equals('y')) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static Date convertMonthYearToDate(String monthYear) {
        try {
            // Parse the input string using a SimpleDateFormat
            SimpleDateFormat inputFormatter = new SimpleDateFormat("MMM yyyy", java.util.Locale.ENGLISH);
            return inputFormatter.parse(monthYear);
        } catch (ParseException e) {
            logger.error("Error parsing date: {}", e.getMessage());
            return null; // Handle the error appropriately, e.g., return an error message or throw an exception
        }
    }
    public static String convertToTitleCase(String input) {
        StringBuilder result = new StringBuilder();
        String[] words = input.toLowerCase().split("\\s");

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return result.toString().trim();
    }
    public static Map<String, Object> jsonStringToMap(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = null;
        try {
            // convert JSON string to Map
            map = (HashMap<String, Object>) mapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String capitalizeFully(String input) {
        return Arrays.stream(input.toLowerCase().split("\\s+"))
                .map(org.apache.commons.lang3.StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    public static boolean isNotValidInactive(String status, String statusApproval) {
        return (Constant.INACTIVE.equalsIgnoreCase(status)
                && Constant.WAITING_APPROVAL.equalsIgnoreCase(statusApproval))
                || (Constant.DRAFT.equalsIgnoreCase(status)
                && Constant.WAITING_APPROVAL.equalsIgnoreCase(statusApproval))
                || (Constant.DRAFT.equalsIgnoreCase(status)
                && Constant.REJECTED.equalsIgnoreCase(statusApproval))
                || (Constant.ACTIVE.equalsIgnoreCase(status)
                && Constant.WAITING_APPROVAL.equalsIgnoreCase(statusApproval))
                || (Constant.INACTIVE.equalsIgnoreCase(status)
                && Constant.APPROVED.equalsIgnoreCase(statusApproval))
                || (Constant.DRAFT.equalsIgnoreCase(status)
                && Constant.DRAFT.equalsIgnoreCase(statusApproval));
    }

    public static boolean isNotValidUpdate(String status, String statusApproval) {
        return (Constant.DRAFT.equalsIgnoreCase(status)
                && Constant.WAITING_APPROVAL.equalsIgnoreCase(statusApproval))
                || (Constant.ACTIVE.equalsIgnoreCase(status)
                && Constant.WAITING_APPROVAL.equalsIgnoreCase(statusApproval))
                || (Constant.INACTIVE.equalsIgnoreCase(status)
                && Constant.APPROVED.equalsIgnoreCase(statusApproval));
    }

    public static String getMessageNotValidInactive(String category, String status, String statusApproval) {
        return "Cannot inactive " + category + " because the status is" + status.toLowerCase() +
                " and status approval is " + statusApproval.toLowerCase();
    }

    public static BigDecimal roundHalfUp(BigDecimal input) {
        if (Objects.isNull(input))
            input = BigDecimal.ZERO;
        return input.setScale(2, RoundingMode.HALF_UP);
    }

    public static boolean isDueDatePlusOne(Date dueDate) {
        Date currentDate = new Date();
        // Add one day to dueDate
        Calendar cal = Calendar.getInstance();
        cal.setTime(dueDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date dueDatePlusOneDay = cal.getTime();

        return dueDatePlusOneDay.before(currentDate) || dueDatePlusOneDay.equals(currentDate);
    }

    public static double getConvertedAmount(String receiptCurrency, String invoiceCurrency, Double receiptAmount, Double rateAmount) {
        if (receiptCurrency.equalsIgnoreCase(CurrencyEnum.IDR.name()) && invoiceCurrency.equalsIgnoreCase(CurrencyEnum.USD.name())) {
            receiptAmount = receiptAmount / rateAmount;
        } else if (receiptCurrency.equalsIgnoreCase(CurrencyEnum.USD.name()) && invoiceCurrency.equalsIgnoreCase(CurrencyEnum.IDR.name())) {
            receiptAmount = receiptAmount * rateAmount;
        }

        return receiptAmount;
    }

    public static String getFileNameAttachment(String fileName) {
        if (StringUtils.isBlank(fileName)) return StringUtils.EMPTY;
        return fileName.substring(15);
    }

    public static String removeSpace(String input) {
        if(input == null) {
            return null;
        }
        return input.strip();
    }
    
    public static String checkOverLapping(String startDateHeader, String endDateHeader, String reqStartDateCriteria, String reqEndDateCriteria){
        var startDate = CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, startDateHeader);
        var endDate = CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, endDateHeader);
        String message = null;
        message = checkOverLappingHeader(startDate, endDate, reqStartDateCriteria, reqEndDateCriteria);
        if (!StringUtils.isEmpty(message)) {
            return message;
        }
        message = checkOverLappingDetail(startDate, endDate, reqStartDateCriteria, reqEndDateCriteria);
        if (!StringUtils.isEmpty(message)) {
            return message;
        }
        return message;
    }
    
    private static String checkOverLappingHeader(Date startDate, Date endDate, String reqStartDateCriteria, String reqEndDateCriteria){
        String message = null;
        var startDateCriteria = CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, reqStartDateCriteria);
        if (Objects.nonNull(startDateCriteria)) {
            if (startDateCriteria.before(startDate)) {
                message = Constant.START_DATE_CRITERIA.concat(reqStartDateCriteria).concat(Constant.CANNOT_BEFORE).concat(CommonHelper.convertDateToString(Constant.FORMAT_START_END_DATE, startDate));
                return message;
            }

            if (Objects.nonNull(endDate) && startDateCriteria.after(endDate) && !StringUtils.isEmpty(reqEndDateCriteria)) {
                message = Constant.START_DATE_CRITERIA.concat(reqEndDateCriteria).concat(Constant.CANNOT_AFTER).concat(CommonHelper.convertDateToString(Constant.FORMAT_START_END_DATE, endDate));
                return message;
            }
        }
        return message;
    }
    
    private static String checkOverLappingDetail(Date startDate, Date endDate, String reqStartDateCriteria, String reqEndDateCriteria){
        String message = null;
        var startDateCriteria = CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, reqStartDateCriteria);
        if (Objects.nonNull(endDate)) {
            var endDateCriteria = CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, reqEndDateCriteria);
            if (Objects.nonNull(endDateCriteria)) {
                if (endDateCriteria.before(startDateCriteria)) {
                    message = Constant.END_DATE_CRITERIA.concat(reqStartDateCriteria).concat(Constant.CANNOT_BEFORE).concat(reqStartDateCriteria);
                    return message;
                }

                if (endDateCriteria.before(startDate)) {
                    message = Constant.END_DATE_CRITERIA.concat(reqStartDateCriteria).concat(Constant.CANNOT_BEFORE).concat(reqStartDateCriteria);
                    return message;
                }

                if (endDateCriteria.after(endDate)) {
                    message = "End date transaction calendar cannot be earlier than end date criteria";
                    return message;
                }
            }
        }
        message = checkEndDate(startDate, endDate, reqStartDateCriteria, reqEndDateCriteria);
        return message;
    }
    
    private static String checkEndDate(Date startDate, Date endDate, String reqStartDateCriteria, String reqEndDateCriteria){
        String message = null;
        if (Objects.isNull(reqEndDateCriteria)) {
            var startDateCriteria = CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, reqStartDateCriteria);
            if (!Objects.isNull(endDate) && Objects.nonNull(startDateCriteria) && startDateCriteria.after(endDate)) {
                message = Constant.START_DATE_CRITERIA
                    .concat(reqStartDateCriteria)
                    .concat(Constant.CANNOT_AFTER)
                    .concat(CommonHelper.dateToString(Constant.FORMAT_START_END_DATE, endDate));
            }
            if (Objects.nonNull(startDateCriteria) && startDateCriteria.before(startDate)) {
                message = Constant.START_DATE_CRITERIA
                        .concat(reqStartDateCriteria)
                        .concat(Constant.CANNOT_BEFORE)
                        .concat(CommonHelper.dateToString(Constant.FORMAT_START_END_DATE, startDate));
            }
        }
        return message;
    }

    public static boolean isDateInRange(Date startDate, Date endDate) {
        Date currentDate = new Date();
        if(startDate==null) {
            return true;
        } else if(endDate==null) {
            return currentDate.after(startDate);
        } else {
            return currentDate.after(startDate) && currentDate.before(endDate);
        }
    }
}
