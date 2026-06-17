package com.c11partner.desktop.utils;

import android.icu.util.ChineseCalendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 农历工具类
 * 提供农历日期转换、生肖计算、干支纪年等功能
 */
public class LunarCalendarUtils {
    private static final List<String> animals = Arrays.asList("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪");
    private static final List<String> ganZhiCycle = Arrays.asList("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸");
    private static final List<String> zhiCycle = Arrays.asList("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥");

    /**
     * 将公历日期转换为农历日期
     *
     * @param timestamp 时间戳
     * @return 农历信息
     */
    @RequiresApi(Build.VERSION_CODES.N)
    public static LunarInfo convertSolarToLunar(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        ChineseCalendar chineseCalendar = new ChineseCalendar(new Date(timestamp));
        chineseCalendar.setTimeInMillis(timestamp);

        int lunarYear = chineseCalendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
        int lunarMonth = chineseCalendar.get(ChineseCalendar.MONTH) + 1;
        int lunarDay = chineseCalendar.get(ChineseCalendar.DAY_OF_MONTH);

        String animal = animals.get((year - 4) % 12);
        int ganIndex = Math.abs((year - 4) % 10);
        int zhiIndex = Math.abs((year - 4) % 12);
        String ganZhiYear = ganZhiCycle.get(ganIndex) + zhiCycle.get(zhiIndex);
        String constellation = getConstellation(month, day);

        LunarInfo lunarInfo = new LunarInfo(lunarYear, lunarMonth, lunarDay, animal, ganZhiYear, constellation);
        if (lunarInfo == null) {
            throw new IllegalArgumentException("Failed to create LunarInfo object");
        }
        return lunarInfo;
    }

    /**
     * 获取指定年份的生肖
     *
     * @param year 年份
     * @return 生肖
     */
    public static String getZodiac(int year) {
        return animals.get((year - 4) % 12);
    }

    /**
     * 获取指定年份的干支纪年
     *
     * @param year 年份
     * @return 干支纪年
     */
    public static String getGanZhiYear(int year) {
        int ganIndex = (year - 4) % 10;
        int zhiIndex = (year - 4) % 12;
        return ganZhiCycle.get(ganIndex) + zhiCycle.get(zhiIndex);
    }

    /**
     * 获取指定日期的星座
     *
     * @param month 月份
     * @param day   日期
     * @return 星座
     */
    public static String getConstellation(int month, int day) {
        if ((month == 1 && day >= 20) || (month == 2 && day <= 17)) {
            return "水瓶座";
        } else if ((month == 2 && day >= 18) || (month == 3 && day <= 19)) {
            return "双鱼座";
        } else if ((month == 3 && day >= 20) || (month == 4 && day <= 19)) {
            return "白羊座";
        } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
            return "金牛座";
        } else if ((month == 5 && day >= 21) || (month == 6 && day <= 20)) {
            return "双子座";
        } else if ((month == 6 && day >= 21) || (month == 7 && day <= 22)) {
            return "巨蟹座";
        } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
            return "狮子座";
        } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
            return "处女座";
        } else if ((month == 9 && day >= 23) || (month == 10 && day <= 22)) {
            return "天秤座";
        } else if ((month == 10 && day >= 23) || (month == 11 && day <= 21)) {
            return "天蝎座";
        } else if ((month == 11 && day >= 22) || (month == 12 && day <= 21)) {
            return "射手座";
        } else if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) {
            return "摩羯座";
        } else {
            return "无法确定的星座日期";
        }
    }

    /**
     * 将农历日期转换为中文表示
     *
     * @param month       农历月份
     * @param dayOfMonth  农历日期
     * @return 中文表示的农历日期
     */
    public static String lunarDayInChinese(int month, int dayOfMonth) {
        List<String> chineseNumberCharacters = Arrays.asList("零", "一", "二", "三", "四", "五", "六", "七", "八", "九");
        List<String> lunarMonths = Arrays.asList("正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊");
        List<String> specialPrefixes = Arrays.asList("初", "", "十", "廿", "卅");

        String lunarMonthStr = lunarMonths.get(month - 1);

        int tenPlace = dayOfMonth / 10;
        int onePlace = dayOfMonth % 10;
        String dayStr;
        if (dayOfMonth <= 10) {
            dayStr = specialPrefixes.get(tenPlace) + (onePlace > 0 ? chineseNumberCharacters.get(onePlace) : "");
        } else {
            dayStr = specialPrefixes.get(tenPlace + 1) + chineseNumberCharacters.get(onePlace);
        }
        return lunarMonthStr + "月 " + dayStr;
    }

    /**
     * 农历信息类
     * 封装农历年份、月份、日期、生肖、干支纪年和星座等信息
     */
    public static class LunarInfo {
        public int year;
        public int month;
        public int day;
        public String animal;
        public String ganZhiYear;
        public String constellation;

        /**
         * 构造函数
         *
         * @param year        农历年份
         * @param month       农历月份
         * @param day         农历日期
         * @param animal      生肖
         * @param ganZhiYear  干支纪年
         * @param constellation 星座
         */
        public LunarInfo(int year, int month, int day, String animal, String ganZhiYear, String constellation) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.animal = animal;
            this.ganZhiYear = ganZhiYear;
            this.constellation = constellation;
        }
    }

    /**
     * 获取当前农历日期
     *
     * @return 格式化的农历日期字符串
     */
    public static String lunarCalendar() {
        long currentTimestamp = System.currentTimeMillis();
        String str = "";
        LunarInfo lunarInfo = LunarCalendarUtils.convertSolarToLunar(currentTimestamp);
        if (lunarInfo != null) {
            //System.out.println("农历年份: " + lunarInfo.ganZhiYear + "年");
            //System.out.println("农历年份: " + lunarInfo.ganZhiYear + "年");

            // 处理农历月份
            String monthStr = lunarDayInChinese(lunarInfo.month, 1);
            String month = "未知月";
            try {
                month = monthStr.split("月 ")[0] + "月";
            } catch (ArrayIndexOutOfBoundsException e) {
                month = monthStr + "月";
            }

            // 处理农历日期
            String dayStr = lunarDayInChinese(lunarInfo.month, lunarInfo.day);
            String day = "未知";
            try {
                day = dayStr.split("月 ")[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                // 处理特殊情况，如"十月"没有日期
                if (dayStr.endsWith("月")) {
                    day = "初一"; // 假设默认日期为初一
                } else {
                    day = dayStr.substring(dayStr.indexOf("月") + 1).trim();
                }
            }


            String year = lunarInfo.ganZhiYear;
            String animal = lunarInfo.animal;
            String constellation = lunarInfo.constellation;
            str = year + animal + "年，" + month + day + "，" + constellation;
        }
        return str;
    }

    /**
     * 主函数，用于测试农历工具类的功能
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 获取当前时间的时间戳
        long currentTimestamp = System.currentTimeMillis();

        // 调用 convertSolarToLunar 方法获取农历信息
        LunarInfo lunarInfo = LunarCalendarUtils.convertSolarToLunar(currentTimestamp);


        // 调用 getZodiac 方法获取生肖
        int yearForZodiac = 2024;
        String zodiac = LunarCalendarUtils.getZodiac(yearForZodiac);
        //System.out.println(yearForZodiac + " 年的生肖是: " + zodiac);

        // 调用 getGanZhiYear 方法获取干支纪年
        int yearForGanZhi = 2024;
        String ganZhi = LunarCalendarUtils.getGanZhiYear(yearForGanZhi);
        //System.out.println(yearForGanZhi + " 年的干支纪年是: " + ganZhi);

        // 调用 lunarDayInChinese 方法将月日转为中文农历表示
        int lunarMonth = 8;
        int lunarDayOfMonth = 15;
        String lunarDayStr = LunarCalendarUtils.lunarDayInChinese(lunarMonth, lunarDayOfMonth);
        //System.out.println("农历表示: " + lunarDayStr);
    }
}