package com.dbs.common.library.utils;

import com.dbs.common.base.utils.CommonHelper;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.EmptyStackException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;
import org.joda.time.Years;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Calendar.*;
import java.util.Objects;

@Component
public class UtilsDate {

    private UtilsDate() {}
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UtilsDate.class);
    
    public static final String TIMEZONE = "GMT+7:00";
    private static final long ONE_DAY_IN_MILL_SEC = 86400000;

    public static boolean validateDateWithCurrMinute(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.FORMAT_DATETIME);
        LocalDateTime time = LocalDateTime.parse(date, formatter);
        LocalDateTime now = LocalDateTime.now();
        return !time.isBefore(now);
    }

    public static boolean validateDate(String date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .appendPattern(Constant.FORMAT_START_END_DATE)
                .toFormatter();
        LocalDate time = LocalDate.parse(date, formatter);
        LocalDate now = LocalDate.now();
        if (time.isEqual(now)) {
            return true;
        } else return !time.isBefore(now);
    }
    
    public static boolean checkChangeDate(Date domain, String dateDto) {
        String dateDomain = UtilsDate.dateToString(domain, Constant.FORMAT_START_END_DATE);
        return !dateDomain.equalsIgnoreCase(dateDto);
    }

    /**
     * Format : "dd-MM-yyyy" or "HH : mm" or others
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * Format : "dd-MM-yyyy" or "HH : mm" or others
     *
     * @param dateString
     * @param format
     * @return
     */
    public static Date stringToDate(String dateString, String format) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        try {
            return (new SimpleDateFormat(format)).parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * DatewithoutTime : "21-Oct-2015 12:34:12" : "21-Oct-2015 00:00:00"
     *
     * @param date
     * @return
     */
    public static Date getDateWithoutTime(Date date) {
        Calendar cal = new GregorianCalendar(new Locale("in", "ID"));
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * EndOfDay : "21-Oct-2015 23:59:59"
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar(new Locale("in", "ID"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.MILLISECOND, -1);

        return cal.getTime();
    }

    /**
     * addDay (1) : "31-Oct-2015 12:34:12" : "01-Nov-2015 12:34:12" minusDay
     * (-1) : "01-Oct-2015 12:34:12" : "30-Sep-2015 12:34:12"
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = new GregorianCalendar(new Locale("in", "ID"));
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int months) {
        Calendar cal = new GregorianCalendar(new Locale("in", "ID"));
        cal.setTime(date);
        cal.add(Calendar.MONTH, months); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * @param date
     * @param years
     * @return
     */
    public static Date addYears(Date date, int years) {
        Calendar cal = new GregorianCalendar(new Locale("in", "ID"));
        cal.setTime(date);
        cal.add(Calendar.YEAR, years); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * @param date : date of birth
     * @return
     */
    public static Integer getAge(Date date) {
        org.joda.time.LocalDate birthdate = new org.joda.time.LocalDate(date);
        return Years.yearsBetween(birthdate, new org.joda.time.LocalDate()).getYears();
    }

    public static Date getDateByAge(Integer age) {
        int day = new org.joda.time.LocalDate().getDayOfMonth();
        int month = new org.joda.time.LocalDate().getMonthOfYear();
        int year = new org.joda.time.LocalDate().getYear() - age;
        /* cabisat mode */
        if (year % 4 != 0 && month == 2 && day > 27) {
            /* in february */
            day = new org.joda.time.LocalDate().getDayOfMonth() - 1;
        }

        String dayOfMonth = Integer.toString(day);
        dayOfMonth = dayOfMonth.length() == 1 ? "0" + dayOfMonth : dayOfMonth;
        String monthOfYear = Integer.toString(month);
        monthOfYear = monthOfYear.length() == 1 ? "0" + monthOfYear : monthOfYear;
        return UtilsDate.stringToDate(dayOfMonth.concat(monthOfYear).concat(Integer.toString(year)), "ddMMyyyy");
    }

    /**
     * different between two Date/Time
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDays(Date startDate, Date endDate) {
        return (int) ((getDateWithoutTime(endDate).getTime() - getDateWithoutTime(startDate).getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Boolean isDateValid(Date startDate, Date endDate) {
        return startDate.before(new Date()) && endDate.after(new Date());
    }

    public static Date getDate(Date dates) {
        Calendar cal = new GregorianCalendar(new Locale("in", "ID"));
        if (dates == null) {
            return new Date();
        } else {
            cal.setTime(dates);
            return cal.getTime();
        }
    }

    public static Date toDate(final String date) {
        return toDate(date, "00:00.00.000");
    }

    public static Date toDate(final String date, final String time) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
        } catch (ParseException e) {
            throw new EmptyStackException(e);
        }
    }

    public static Date add(final Date a, int b) {
        Calendar cal = new GregorianCalendar(new Locale("in", "ID"));
        cal.setTime(a);
        cal.add(Calendar.DAY_OF_MONTH, b);
        String s = dateToString(cal.getTime(), Constant.FORMAT_DATE);
        return toDate(s);
    }

    public static boolean isAfter(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);
        int month1 = cal1.get(Calendar.MONTH);
        int year1 = cal1.get(Calendar.YEAR);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);
        int month2 = cal2.get(Calendar.MONTH);
        int year2 = cal2.get(Calendar.YEAR);

        if (year1 < year2) { // check year
            return true;
        } else if (year1 == year2) { // if year is equal
            // if month is equal
            if (month1 < month2) { // check month
                return true;
            } else return month1 == month2 && day1 < day2;
        }
        return false;
    }

    public static boolean isEqual(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);
        int month1 = cal1.get(Calendar.MONTH);
        int year1 = cal1.get(Calendar.YEAR);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);
        int month2 = cal2.get(Calendar.MONTH);
        int year2 = cal2.get(Calendar.YEAR);

        return ((day1 == day2) && (month1 == month2) && (year1 == year2));
    }

    public static Date getDateMinusDay(Date d, int days) {
        return (new Date(d.getTime() - days * ONE_DAY_IN_MILL_SEC));
    }

    public static Date getNextDate(Date date, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * @param initialTime "18:00:00"
     * @param finalTime   "06:00:00"
     * @param currentTime
     * @return
     */@SuppressWarnings("java:S6353")
    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime, Date currentTime) {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        try {
            if (initialTime.matches(reg) && finalTime.matches(reg)) {
                boolean valid = false;
                //Start Time
                java.util.Date inTime = new SimpleDateFormat("HH:mm:ss").parse(initialTime);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(inTime);

                //Current Time
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(currentTime);

                //End Time
                java.util.Date finTime = new SimpleDateFormat("HH:mm:ss").parse(finalTime);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(finTime);

                if (finalTime.compareTo(initialTime) < 0) {
                    calendar2.add(Calendar.DATE, 1);
                    calendar3.add(Calendar.DATE, 1);
                }

                java.util.Date actualTime = calendar3.getTime();
                if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0)
                        && actualTime.before(calendar2.getTime())) {
                    valid = true;
                }
                return valid;
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
        }
        return false;
    }

    public static Period getDateDiff(Date arg0, Date arg1) {
        return new Period(arg0.getTime(), arg1.getTime());
    }

    public static Integer getDateDiffInDays(Date arg0, Date arg1) {
        return (int) (-(arg0.getTime() - arg1.getTime()) / (1000 * 60 * 60 * 24) + 1);
    }

    public static int getDateDiffInYears(Date arg0, Date arg1) {
        Calendar a = getCalendar(arg0);
        Calendar b = getCalendar(arg1);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH)
                || (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static String getDayOfMonth(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        int dayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        String dayOfMonthStr = null;
        switch (dayOfMonth) {
            case 1:
                dayOfMonthStr = "Minggu";
                break;
            case 2:
                dayOfMonthStr = "Senin";
                break;
            case 3:
                dayOfMonthStr = "Selasa";
                break;
            case 4:
                dayOfMonthStr = "Rabu";
                break;
            case 5:
                dayOfMonthStr = "Kamis";
                break;
            case 6:
                dayOfMonthStr = "Jumat";
                break;
            case 7:
                dayOfMonthStr = "Sabtu";
                break;
            default:
        }
        return dayOfMonthStr;
    }

    /**
     * Convert Microsoft un OLE Automation - OADate to Java Date.
     *
     * @param dateInDouble
     * @return
     */
    public static Date convertFromOADate(double dateInDouble) {
        double mantissa = dateInDouble - (long) dateInDouble;
        double hour = mantissa * 24;
        double min = (hour - (long) hour) * 60;
        double sec = (min - (long) min) * 60;

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Date baseDate;
        try {
            baseDate = myFormat.parse("30 12 1899");
            Calendar c = Calendar.getInstance();
            c.setTime(baseDate);
            c.add(Calendar.DATE, (int) dateInDouble);
            c.add(Calendar.HOUR, (int) hour);
            c.add(Calendar.MINUTE, (int) min);
            c.add(Calendar.SECOND, (int) sec);

            return c.getTime();
        } catch (ParseException ex) {
            Logger.getLogger(UtilsDate.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Convert Date to Microsoft OLE Automation - OADate type
     *
     * @param date
     * @return
     */@SuppressWarnings("java:S1874")
    public static String convertToOADate(Date date) {
        double oaDate;
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Date baseDate;
        try {
            baseDate = myFormat.parse("30 12 1899");
            long days = TimeUnit.DAYS.convert(date.getTime() - baseDate.getTime(), TimeUnit.MILLISECONDS);

            oaDate = days + ((double) date.getHours() / 24) + ((double) date.getMinutes() / (60 * 24)) + ((double) date.getSeconds() / (60 * 24 * 60));
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number number = format.parse(String.valueOf(oaDate));
            double d = number.doubleValue();
            return String.valueOf(d);
        } catch (ParseException ex) {
            Logger.getLogger(UtilsDate.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static boolean validateDateWithCurrMinute2(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.FORMAT_DATE);
        LocalDate time = LocalDate.parse(date, formatter);
        LocalDate now = LocalDate.now();
        return !time.isBefore(now);
    }

    public static boolean validateDateWithCurrMinute3(String date, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate time = LocalDate.parse(date, formatter);
        LocalDate now = LocalDate.now();
        return !time.isBefore(now);
    }

    public static boolean isBefore(Date date1, Date date2) {
        return date2.before(date1);
    }

    public static boolean isAfterV1(Date date1, Date date2) {
        return date2.after(date1);
    }

    public static int getMonthNumberOfDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }

    public static int getLastDayOfMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getYearNumberOfDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return Integer.parseInt(dateFormat.format(date));
    }

    public static int getLastDayByMonthAndYear(int year, int month) {
        return LocalDate.of(year, month, 1).getMonth().length(Year.of(year).isLeap());
    }

    public static Date getStartOrEndDateTimeNow(boolean isStartDate) {
        var dateNow = new Date();
        return getStartOrEndDateTimeByDate(dateNow, isStartDate);
    }

    public static Date getStartOrEndDateTimeByDate(Date date, boolean isStartDate) {
         try {
             var dateFormat = new SimpleDateFormat(Constant.FORMAT_DATE);
             var dateString = isStartDate
                     ? dateFormat.format(date).concat(" 00:00:00")
                     : dateFormat.format(date).concat(" 23:59:59");

             return new SimpleDateFormat(Constant.FORMAT_DATETIME)
                     .parse(dateString);
         } catch (ParseException e) {
             Logger.getLogger(UtilsDate.class.getName()).log(Level.SEVERE, e.getMessage(), e);
             return null;
         }
    }

    public static int getEndOfMonthByDateNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getMonthDiff(Date startDate, Date endDate) {
        var startDateStr = dateToString(startDate, Constant.FORMAT_DATE);
        var endDateStr = dateToString(endDate, Constant.FORMAT_DATE);
        var monthsBetween = ChronoUnit.MONTHS.between(
                LocalDate.parse(startDateStr).withDayOfMonth(1),
                LocalDate.parse(endDateStr).withDayOfMonth(1));
        return (int) monthsBetween;
    }

    public static boolean dateValidationAccepted(String stringStartDate, String stringEndDate){
        Date dateCurrentTime = new Date();
        if(ObjectUtils.isEmpty(stringEndDate)){
            return false;
        }

        String stringCurrentTime = UtilsDate.dateToString(dateCurrentTime, Constant.FORMAT_START_END_DATE);

        if(stringStartDate.equals(stringCurrentTime) || stringEndDate.equals(stringCurrentTime)){
            return true;
        }

        Date dateStartDate = UtilsDate.stringToDate(stringStartDate, Constant.FORMAT_START_END_DATE);
        Date dateEndDate = UtilsDate.stringToDate(stringEndDate, Constant.FORMAT_START_END_DATE);

        return dateCurrentTime.after(dateStartDate) && dateCurrentTime.before(dateEndDate);
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
                message = Constant.START_DATE_DETAIL.concat(reqStartDateCriteria).concat(Constant.CANNOT_BEFORE).concat(CommonHelper.convertDateToString(Constant.FORMAT_START_END_DATE, startDate));
                return message;
            }

            if (Objects.nonNull(endDate) && startDateCriteria.after(endDate) && !StringUtils.isEmpty(reqEndDateCriteria)) {
                message = Constant.START_DATE_DETAIL.concat(reqEndDateCriteria).concat(Constant.CANNOT_AFTER).concat(CommonHelper.convertDateToString(Constant.FORMAT_START_END_DATE, endDate));
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
                    message = Constant.END_DATE_DETAIL.concat(reqStartDateCriteria).concat(Constant.CANNOT_BEFORE).concat(reqStartDateCriteria);
                    return message;
                }

                if (endDateCriteria.before(startDate)) {
                    message = Constant.END_DATE_DETAIL.concat(reqStartDateCriteria).concat(Constant.CANNOT_BEFORE).concat(reqStartDateCriteria);
                    return message;
                }

                if (endDateCriteria.after(endDate)) {
                    message = "End date detail cannot be earlier than end date header";
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
                message = Constant.START_DATE_DETAIL
                    .concat(reqStartDateCriteria)
                    .concat(Constant.CANNOT_AFTER)
                    .concat(CommonHelper.dateToString(Constant.FORMAT_START_END_DATE, endDate));
            }
            if (Objects.nonNull(startDateCriteria) && startDateCriteria.before(startDate)) {
                message = Constant.START_DATE_DETAIL
                        .concat(reqStartDateCriteria)
                        .concat(Constant.CANNOT_BEFORE)
                        .concat(CommonHelper.dateToString(Constant.FORMAT_START_END_DATE, startDate));
            }
        }
        return message;
    }
    
    
}
