package net.ys.utils;

import net.ys.constant.X;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: NMY
 * Date: 16-9-8
 */
public class TimeUtil {

    private static SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat yMdHm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static SimpleDateFormat mdHm = new SimpleDateFormat("MM-dd HH:mm");

    private static SimpleDateFormat yMdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat yMd_Hms = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 获取今日开始的毫秒值
     *
     * @return
     */
    public static long todayStartMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取昨日开始的毫秒值
     *
     * @return
     */
    public static long yesterdayStartMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取今日最后的毫秒值
     *
     * @return
     */
    public static long todayEndMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取本周开始的毫秒值
     *
     * @return
     */
    public static long weekStartMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取上周开始的毫秒值
     *
     * @return
     */
    public static long lastWeekStartMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取本周最后的毫秒值
     *
     * @return
     */
    public static long weekEndMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取本月开始的毫秒值
     *
     * @return
     */
    public static long monthStartMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取上月开始的毫秒值
     *
     * @return
     */
    public static long lastMonthStartMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取本月最后的毫秒值
     *
     * @return
     */
    public static long monthEndMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取世界末日毫秒值
     *
     * @return
     */
    public static long doomsdayMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 99);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取下一年毫秒值
     *
     * @return
     */
    public static long nextYearMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }


    /**
     * 获取下一个小时毫秒值
     *
     * @return
     */
    public static long nextHourMillisecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取今日剩余毫秒值
     *
     * @return
     */
    public static int todayRemainingMillisecond() {
        return (int) (todayEndMillisecond() - System.currentTimeMillis());
    }

    /**
     * 距下一个小时剩余毫秒值
     *
     * @return
     */
    public static long nextHourRemainingMillisecond() {
        return todayRemainingMillisecond() - System.currentTimeMillis();
    }

    /**
     * 距下一个小时剩余秒值
     *
     * @return
     */
    public static int nextHourRemainingSecond() {
        return (int) ((todayRemainingMillisecond() - System.currentTimeMillis()) / 1000);
    }

    /**
     * 获取今日剩余秒值
     *
     * @return
     */
    public static int todayRemainingSecond() {
        return (int) ((todayEndMillisecond() - System.currentTimeMillis()) / 1000);
    }

    /**
     * 获取已过时间（eg:3分钟前）
     *
     * @return
     */
    public static String alreadyPass(long time) {
        long pass = System.currentTimeMillis() - time;
        if (pass <= X.TIME.SECOND_MILLISECOND) {//不足一秒
            pass += 2000;
        }
        if (pass < X.TIME.MINUTE_MILLISECOND) {//几秒前
            return (pass / X.TIME.SECOND_MILLISECOND) + "秒前";
        } else if (pass < X.TIME.HOUR_MILLISECOND) {//分钟前
            return (pass / X.TIME.MINUTE_MILLISECOND) + "分钟前";
        } else if (pass < X.TIME.DAY_MILLISECOND) {
            return (pass / X.TIME.HOUR_MILLISECOND) + "小时前";
        } else if (pass < 6 * X.TIME.DAY_MILLISECOND) {//5天以内
            return (pass / X.TIME.DAY_MILLISECOND) + "天前";
        } else {
            return yMdHm.format(new Date(time));
        }
    }

    /**
     * 数字转换成HH:mm:ss格式-毫秒
     *
     * @param millisecond
     * @return
     */
    public static String millisecondToTime(long millisecond) {
        String timeStr;
        int time = (int) (millisecond / 1000);
        int hour;
        int minute;
        int second;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        return i >= 10 ? "" + i : "0" + i;
    }

    /**
     * 转化时间
     *
     * @param time
     * @return
     */
    public static String toYmdHms(long time) {
        if (time == 0) {
            return "";
        }
        Date date = new Date();
        date.setTime(time);
        return yMdHms.format(date);
    }

    /**
     * 转化时间
     *
     * @param time
     * @return
     */
    public static String toMdHm(long time) {
        if (time == 0) {
            return "";
        }
        Date date = new Date();
        date.setTime(time);
        return mdHm.format(date);
    }

    /**
     * 转化时间
     *
     * @param time
     * @return
     */
    public static long toLong(String time) {
        try {
            return yMdHms.parse(time).getTime();
        } catch (ParseException e) {
        }
        return 0L;
    }

    /**
     * 转化时间-开始
     *
     * @param time
     * @return
     */
    public static long toLongStart(String time) {
        try {
            Date date = yMd.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 1);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
        }
        return 0L;
    }

    /**
     * 转化时间-结束
     *
     * @param time
     * @return
     */
    public static long toLongEnd(String time) {
        try {
            Date date = yMd.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
        }
        return 0L;
    }

    /**
     * 格式化当前时间
     *
     * @return
     */
    public static String genYmdHms() {
        Date date = new Date();
        return yMdHms.format(date);
    }

    /**
     * 格式化当前时间
     *
     * @return
     */
    public static String genCurYmdHms() {
        Date date = new Date();
        return yMd_Hms.format(date);
    }

    /**
     * 转化年月日
     *
     * @param time
     * @return
     */
    public static String toYmd(long time) {
        if (time == 0) {
            return "";
        }
        Date date = new Date();
        date.setTime(time);
        return yMd.format(date);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(nextYearMillisecond());
    }
}
