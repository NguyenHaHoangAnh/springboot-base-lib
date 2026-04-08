package com.example.core.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class DateUtil {
    private static String PATTERN_ISO8601 = "^\\d{4}-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|Z)?$";

    // Trả về đầu ngày 00:00:00
    public static Date startOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // Trả về cuối ngày 23:59:59.999
    public static Date endOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static Date addDay(Date dateInput, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInput);
        cal.add(5, day);
        Date returnDate = cal.getTime();
        return returnDate;
    }

    public static Date addDay(Timestamp dateInput, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInput);
        cal.add(5, day);
        Date returnDate = cal.getTime();
        return returnDate;
    }

    public static boolean isStandardDateValue(String input) {
        return Pattern.matches(PATTERN_ISO8601, input);
    }

    public static String toStandardDateStringUtc(Object dateOrTimestamp) {
        return toStandardDateString(dateOrTimestamp, "UTC");
    }

    public static String toStandardDateString(Object dateOrTimestamp) {
        return toStandardDateString(dateOrTimestamp, (String)null);
    }

    public static String toStandardDateString(Object dateOrTimestamp, String timezone) {
        if (dateOrTimestamp != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            if (timezone != null) {
                df.setTimeZone(TimeZone.getTimeZone(timezone));
            }

            if (dateOrTimestamp instanceof Date) {
                return df.format((Date)dateOrTimestamp);
            }

            if (dateOrTimestamp instanceof Timestamp) {
                return df.format((Timestamp)dateOrTimestamp);
            }
        }

        return null;
    }

    public static Date getDate(String strDate) throws ParseException {
        if (isStandardDateValue(strDate)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            return df.parse(strDate);
        } else {
            return null;
        }
    }

    public static java.sql.Date getSqlDate(String strDate) throws ParseException {
        return getSqlDate(getDate(strDate));
    }

    public static Timestamp getSqlTimestamp(String strDate) {
        try {
            return getSqlTimestamp(getDate(strDate));
        } catch (Exception var2) {
            return null;
        }
    }

    public static Timestamp getSqlTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(date.getTime()));
            c.set(14, 0);
            return new Timestamp(c.getTimeInMillis());
        }
    }

    public static java.sql.Date getSqlDate(Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }

    public static String convertDateToString(Date date) {
        if (date == null) {
            return "";
        } else {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(date);
        }
    }

    public static String convertDateTimeToString(Date date) {
        if (date == null) {
            return "";
        } else {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return formatter.format(date);
        }
    }

    public static String convertDateToString1(Date date) {
        if (date == null) {
            return "";
        } else {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(date);
        }
    }

    public static Date getFirstDayOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        return calendar.getTime();
    }

    public static Date getLastDayOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.add(5, -1);
        Date lastDayOfMonth = calendar.getTime();
        return lastDayOfMonth;
    }

    public static Timestamp convertTimestamp(Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }

    public static Date removeTime(Date dateInput) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInput);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        Date returnDate = cal.getTime();
        return returnDate;
    }

    public static Date add23Hours(Date dateInput) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInput);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 59);
        Date returnDate = cal.getTime();
        return returnDate;
    }

    public static String convertDateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
    }

    public static String convertDateTimeToString1(Date date) {
        if (date == null) {
            return null;
        } else {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(date);
        }
    }

    public static boolean isValidDateRangeUtc(Timestamp startDate, Timestamp endDate, Timestamp sysdate) throws Exception {
        if (sysdate != null && startDate != null) {
            if (endDate != null && startDate.after(endDate)) {
                return false;
            } else {
                Date dStart = convertWithTimezone(startDate, "Asia/Ho_Chi_Minh");
                Date dEnd = endDate != null ? convertWithTimezone(endDate, "Asia/Ho_Chi_Minh") : null;
                Date var5 = removeTime(new Date(dStart.getTime()));
                Date var6 = endDate != null ? add23Hours(removeTime(new Date(dEnd.getTime()))) : null;
                return !sysdate.before(var5) && (var6 == null || !sysdate.after(var6));
            }
        } else {
            return false;
        }
    }

    public static Timestamp convertWithTimezone(Timestamp timestamp, String timezone) throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone(timezone.trim());
        ZoneRules zr = timeZone.toZoneId().getRules();
        ZoneOffset zo = zr.getOffset(LocalDateTime.now());
        Date newDate = add(new Date(timestamp.getTime()), zo.getTotalSeconds(), 13);
        return new Timestamp(newDate.getTime());
    }

    public static ZoneOffset getOffset(TimeZone timeZone) {
        ZoneId zi = timeZone.toZoneId();
        ZoneRules zr = zi.getRules();
        return zr.getOffset(LocalDateTime.now());
    }

    public static long getOffsetHours(TimeZone timeZone) {
        ZoneOffset zo = getOffset(timeZone);
        return (long)zo.getTotalSeconds();
    }

    public static Date firstDayOfMonth(Date dtDate) throws Exception {
        try {
            SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");
            String strTemp = fmtDate.format(dtDate);
            return fmtDate.parse("01" + strTemp.substring(2, strTemp.length()));
        } catch (Exception var3) {
            throw var3;
        }
    }

    public static Date addMonth(Date dt, int iMonthAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(2, iMonthAdd);
        return cal.getTime();
    }

    public static Date addMonthTronThang(Date dt, int iMonthAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(2, iMonthAdd - 1);
        int end = cal.getActualMaximum(5);
        cal.set(5, end);
        return cal.getTime();
    }

    public static Date addYear(Date dt, int iValue) {
        return add(dt, iValue, 1);
    }

    public static Date add(Date dt, int iValue, int iType) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(dt);
        cld.add(iType, iValue);
        return cld.getTime();
    }

    public static Date convertDateTimeToStringByType(String strDate, String type) throws Exception {
        try {
            if (strDate == null) {
                return null;
            } else {
                DateFormat formatter = new SimpleDateFormat(type);
                return formatter.parse(strDate);
            }
        } catch (Exception var3) {
            throw var3;
        }
    }

    public static Date FirstDay(Date dtDate) throws Exception {
        SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");
        String strTemp = fmtDate.format(dtDate);
        return fmtDate.parse("01" + strTemp.substring(2, strTemp.length()));
    }

    public static Date toDate(String strDate) throws Exception {
        if (strDate != null && !strDate.isEmpty()) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.parse(strDate);
        } else {
            return null;
        }
    }

    public static Date toDateTime(String strDate) throws Exception {
        if (strDate != null && !strDate.isEmpty()) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return df.parse(strDate);
        } else {
            return null;
        }
    }

    public static Date getDateTime(Timestamp timeStamp) throws Exception {
        return timeStamp == null ? null : new Date(timeStamp.getTime());
    }

    public static Date convertStringtoDate(String strDate, String strDatefomat) throws Exception {
        if (strDate != null && !strDate.isEmpty()) {
            DateFormat df = new SimpleDateFormat(strDatefomat);
            return df.parse(strDate);
        } else {
            return null;
        }
    }

    public static Date checkDate(String strDate, String strDatefomat) throws Exception {
        if (strDate != null && !strDate.isEmpty()) {
            DateFormat df = new SimpleDateFormat(strDatefomat);
            df.setLenient(false);
            return df.parse(strDate);
        } else {
            return null;
        }
    }

    public static Timestamp getCurrTimestamp() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    public static java.sql.Date convertStringtoSqlDate(String strDate, String strDatefomat) throws Exception {
        if (strDate != null && !strDate.isEmpty()) {
            DateFormat df = new SimpleDateFormat(strDatefomat);
            return new java.sql.Date(df.parse(strDate).getTime());
        } else {
            return null;
        }
    }
}
