package cn.richinfo.richadmin.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
public class DateUtil {

    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);


    /**
     * 获取当前的时间
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前没有秒的时间
     */
    public static Date getNowDateNoSecond() {
        return formatStringToDate(formatDate(getNowDate(), "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
    }

    /**
     * 根据format格式化时间为string
     */
    public static String formatDate(Date date) {
        return formatDate(date, null);
    }

    /**
     * 根据format格式化时间为string
     */
    public static String formatDate(Date date, String format) {
        if (format == null || StringUtils.isEmpty(format.trim())) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } else {
            return new SimpleDateFormat(format).format(date);
        }
    }

    /**
     * 根据format格式化时间为Date
     */
    public static Date formatStringToDate(String date) {
        return formatStringToDate(date, "");
    }

    /**
     * 根据format格式化时间为Date
     */
    public static Date formatStringToDate(String date, String format) {
        try {
            if (format == null || StringUtils.isEmpty(format.trim())) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            } else {
                return new SimpleDateFormat(format).parse(date);
            }
        } catch (ParseException e) {
            log.error("date格式错误", e);
            return null;
        }
    }

    /**
     * 得到几天前的时间
     */
    public static Date getDateBefore(Date nowDate, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到当前之前的0或5的分钟时间
     */
    public static Date getDateByZeroORFive(Date nowDate) {
        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.SECOND, 0);
        int minute = now.get(Calendar.MINUTE) % 5;// 计算出最近0或5的分钟差，若值为,则设为5，
        if (minute == 0) {
            minute = 5;
        }
        return getMinuteBefore(now.getTime(), minute);
    }

    /**
     * 得到几分钟前的时间
     */
    public static Date getMinuteBefore(Date nowDate, int minute) {
        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - minute);
        return now.getTime();
    }

    /**
     * 得到几秒钟前的时间
     */
    public static Date getSecondBefore(Date nowDate, int second) {
        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) - second);
        return now.getTime();
    }

    /**
     * 得到几分钟前的字符串
     */
    public static String getMinuteStringBefore(Date nowDate, int minute) {
        return formatDate(getMinuteBefore(nowDate, minute), "yyyy-MM-dd HH:mm");
    }

    /**
     * 得到几秒后的时间
     */
    public static Date getSecondsDateAfter(Date nowDate, int seconds) {
        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);
        now.add(Calendar.SECOND, seconds);
        return now.getTime();
    }

    /**
     * 格式化时间输出
     *
     */
    public static String getFormatDateString(String date, String format) {
        String formatDate = formatDate(formatStringToDate(date, null), format);
        return formatDate;
    }

    /**
     * 输出日历的年份和月份的字符串形式
     *
     * eg：2016-8-1 输出：201608 2016-11-1 输出：201611
     */
    public static String getYearAndMonth(Calendar ca) {
        String yearMonth = "";
        if (ca != null) {
            int year = ca.get(Calendar.YEAR);
            int month = ca.get(Calendar.MONTH) + 1;
            if (month < 10) {
                yearMonth = String.valueOf(year) + String.valueOf(month);
            } else {
                yearMonth = String.valueOf(year) + String.valueOf(month);
            }
        }
        return yearMonth;
    }

    /**
     * 将long类型的数据进行转换成天 时 分 秒(秒数)
     *
     */
    public static String formatDuring(long mss) {
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / (60);
        long seconds = mss % (60);
        return days + " 天 " + hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒 ";
    }

    /**
     * 获取两个时间相差的天数
     *
     * @param  startDate   endDate
     */
    public static int getDiffDateDays(Date startDate, Date endDate) {
        int dayDiff = 0;
        if (startDate != null && endDate != null) {
        } else if (startDate == null) {
            startDate = formatStringToDate("1970-01-01 00:00:00");
        } else if (endDate == null) {
            endDate = getNowDate();
        }
        dayDiff = (int) ((endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000));
        return dayDiff;
    }

    public static void main(String[] args){
        String s = "2017/02/14 00:00:00";
        Date date = formatStringToDate(s,"yyyy/MM/dd HH:mm:ss");
        System.out.println(date);
    }
    /**
     * 将String转为Calendar
     */
    public static Calendar stringToCalendar(String date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.formatStringToDate(date, "yyyy-MM"));
        return cal;
    }

    /**
     * 将2017-07-07 转为 20177
     */
    public static String getYearAndMonth(String year,String month){
        if ("0".equals(month.substring(0,1))) { // 对日期的格式进行处理将2017-08-09格式转化为20178
            year=year+month.charAt(1)+"";
        } else {
            year = year + month+"";
        }
        return year;
    }

    /**
     * 统一格式化年月日期
     * yyyyMM 格式
     */
    public static String getStandardYearAndMonth(int month,int year){
        String monthNowAfterTrans="";
        if (month == 0) {
            monthNowAfterTrans = year - 1 + "12";
        } else if (month < 10) {
            monthNowAfterTrans = year + "0" + month;
        } else {
            monthNowAfterTrans = year + "" + month;
        }
        return monthNowAfterTrans;
    }

}
