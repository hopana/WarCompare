package com.wft.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
 
	
	/** yyyy-MM-dd HH:mm:ss
	 * @return
	 * 得到当前日期
	 */
	public static String getCurrentTime() {
		 
		  return fmtDate("yyyyMMddHHmm");

	}
	 
	/**
	 * 
	 * @return
	 *格式化
	 */
	public static String fmtDate(String fmt) {
		  Calendar c = Calendar.getInstance();
		  c.setTimeInMillis(System.currentTimeMillis());
		  java.util.Date d = c.getTime();
		  SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		  return sdf.format(d);

	}

    /**
     * 获取日期
     * @return yyyyMMdd格式日期
     */
    public static String getCurrentDate() {
        return fmtDate("yyyyMMdd");
    }

    /**
     * 获取日期
     * @return yyyyMMdd格式日期
     */
    public static String getCurrentMonthDay() {
        return fmtDate("MMdd");
    }

}
