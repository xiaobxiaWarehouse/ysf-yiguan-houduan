package com.chufang.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	private static final SimpleDateFormat dt14 = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static final SimpleDateFormat dt142 = new SimpleDateFormat(
			"yyyyMMdd HHmmss");
	private static final SimpleDateFormat dt12 = new SimpleDateFormat(
			"yyyyMMddHHmm");
	private static final SimpleDateFormat dtlong14 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dt8 = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat dt10 = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat dt10Ch = new SimpleDateFormat(
			"yyyy年MM月dd日");
	private static final SimpleDateFormat dt8Ch = new SimpleDateFormat(
			"yyyy年MM月");
	private static final SimpleDateFormat dt6 = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat dt7 = new SimpleDateFormat("yyyy-MM");
	private static final SimpleDateFormat time8 = new SimpleDateFormat("HHmmss");
	private static final SimpleDateFormat dtDay = new SimpleDateFormat("MM-dd");
	private static final SimpleDateFormat shortDay = new SimpleDateFormat(
			"MMdd");
	private static final SimpleDateFormat dtfen14 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat dot10 = new SimpleDateFormat(
			"yyyy.MM.dd");
	private static final SimpleDateFormat dot14 = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm");
	private static final SimpleDateFormat dt12Slash = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");
	// private static final SimpleDateFormat day = new SimpleDateFormat("dd");

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	public static String dot12SlashFormat(Date date) {
		return dt12Slash.format(date);
	}

	public static String dot10Format(Date date) {
		return dot10.format(date);
	}

	public static String dot14Format(Date date) {
		return dot14.format(date);
	}

	/**
	 * 自宝义格式化日期
	 * 
	 * @param formatData
	 * @param date
	 * @return
	 */
	public static String customFormat(String formatData, Date date) {
		SimpleDateFormat cus = new SimpleDateFormat(formatData);
		return cus.format(date);
	}

	/**
	 * 日期 类型的字串自定义成另一格式的字符
	 * 
	 * @param formatData
	 * @param dateStr
	 * @return
	 */
	public static String dateStrFormat(String formatData, String dateStr) {

		if (dateStr == null) {
			return "";
		}

		Pattern pattern = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$");
		Matcher matcher = pattern.matcher(dateStr);
		if (matcher.matches()) {
			dateStr += " 00:00:00";
		}

		Date date = dtlong14FromStr(dateStr);
		if (date != null) {
			return customFormat(formatData, date);
		} else {
			return dateStr;
		}
	}

	public static String dt14LongFormat(Date date) {
		return dtlong14.format(date);
	}
	public static String dt142FromDate(Date date) {
		return dt142.format(date);
	}
	public static String dtDayFormat(Date date) {
		return dtDay.format(date);
	}

	public static String dt8ChFromDate(Date date) {
		return dt8Ch.format(date);
	}
	
	public static Date dt8Parse(String date) throws ParseException {
		return dt8Ch.parse(date);
	}

	public static String time8Format(Date date) {
		return time8.format(date);
	}

	public static String dt10ChFromDate(Date date) {
		return dt10Ch.format(date);
	}

	public static String dt6FromDate(Date date) {
		return dt6.format(date);
	}

	public static String dt10FromDate(Date date) {
		try {
			return dt10.format(date);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public static Date dt10FromStr(String date) {
		try {
			return dt10.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static String dtfen14FromDate(Date date) {
		return dtfen14.format(date);
	}

	public static Date dtfen14FromStr(String date) {
		try {
			return dtfen14.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static String dt14FromDate(Date date) {
		return dt14.format(date);
	}

	public static String dt12FromDate(Date date) {
		return dt12.format(date);
	}

	public static Date dtlong14FromStr(String date) {
		try {
			return dtlong14.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date dt14ShortFromStrS(String time) {
		try {
			return dt14.parse(time);
		} catch (ParseException e) {
			LOGGER.error(e.toString(), e);
		}
		return null;
	}

	public static Date dt14FromStr(String time) {
		try {
			return dtlong14.parse(time);
		} catch (ParseException e) {
			LOGGER.error(e.toString(), e);
		}
		return null;
	}

	public static Date dt8StrToDate(String time) {
		try {
			return dt8.parse(time);
		} catch (ParseException e) {
			LOGGER.error(e.toString(), e);
		}
		return null;
	}

	public static String dt8FormDate(Date date) {
		return dt8.format(date);
	}

	public static String dtlong14FromDate(Date date) {
		if (date == null) {
			return "";
		}
		return dtlong14.format(date);
	}

	public static Date getBeginDateTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date getEndDateTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	public static Date getThisMonthFirstDay(Date date) throws ParseException {
		try {
			if (date == null) {
				date = new Date();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			return c.getTime();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public static Date getThisMonthEndDay(Date date) throws ParseException {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.DAY_OF_YEAR, -1);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	public static long getNextDay(int hour) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	public static long getMonthDayLong(int hour, int day) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	public static Date getMonthDay(int hour, int day) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getWeekStart(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return c.getTime();
	}

	public static Date getWeekEnd(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return c.getTime();
	}

	public static Date getMonthStart(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTime();
	}

	public static Date getMonthEnd(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		c.add(Calendar.DAY_OF_YEAR, -1);
		return c.getTime();
	}

	public static int[] getDateLength(String fromDate, String toDate) {
		Calendar c1 = getCal(fromDate);
		Calendar c2 = getCal(toDate);
		int[] p1 = { c1.get(Calendar.YEAR), c1.get(Calendar.MONTH),
				c1.get(Calendar.DAY_OF_MONTH) };
		int[] p2 = { c2.get(Calendar.YEAR), c2.get(Calendar.MONTH),
				c2.get(Calendar.DAY_OF_MONTH) };
		return new int[] {
				p2[0] - p1[0],
				p2[0] * 12 + p2[1] - p1[0] * 12 - p1[1],
				(int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / (24 * 3600 * 1000)) };
	}

	static Calendar getCal(String date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Integer.parseInt(date.substring(0, 4)),
				Integer.parseInt(date.substring(5, 7)) - 1,
				Integer.parseInt(date.substring(8, 10)));
		return cal;
	}

	public static int calculateMonthIn(Date date1, Date date2) {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);
		int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
				+ cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
		return c;
	}
	
	public static long calculateDayGap(Date startDate, Date endDate) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(startDate);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(endDate);
		return (cal2.getTimeInMillis() - cal1.getTimeInMillis())
				/ (1000 * 3600 * 24) + 1;
	}

	public static String shortDayFormat(Date date) {
		return shortDay.format(date);
	}

	public static int calculateDateYearGap(String startDate, String endDate)
			throws ParseException {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dt10FromStr(startDate));
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dt10FromStr(endDate));
		int extYear = 0;
		// 如果开始日期的日月<=结束日期的日月则extyear=1
		if (shortDayFormat(cal2.getTime()).compareTo(
				shortDayFormat(cal1.getTime())) >= 0) {
			extYear = 1;
		}
		return cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR) + extYear;
	}

	public static Date getNextYearDateFromStr(String date)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(dt10FromStr(date));
		c.add(Calendar.YEAR, 1);
		return c.getTime();
	}

	public static Date getOneHourLater(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, 1);
		return c.getTime();
	}

	public static Date extractTimeFromStr(String date) {
		Pattern pattern = Pattern
				.compile("[0-9]{4}[-][0-9]{1,2}[-][0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
		Matcher matcher = pattern.matcher(date);
		String dateStr = null;
		if (matcher.find()) {
			dateStr = matcher.group(0);
		}
		if (dateStr != null) {
			return dt14FromStr(dateStr);
		} else {
			pattern = Pattern.compile("[0-9]{4}[-][0-9]{1,2}[-][0-9]{2}");
			matcher = pattern.matcher(date);
			if (matcher.find()) {
				dateStr = matcher.group(0);
			}
			if (dateStr != null) {
				return dt10FromStr(dateStr);
			} else {
				return null;
			}
		}
	}

	public static String dt7FromDate(Date date) {
		return dt7.format(date);
	}

	public static int[] getNeturalAge(Calendar calendarBirth,
			Calendar calendarNow) {
		int diffYears = 0, diffMonths, diffDays;
		int dayOfBirth = calendarBirth.get(Calendar.DAY_OF_MONTH);
		int dayOfNow = calendarNow.get(Calendar.DAY_OF_MONTH);
		if (dayOfBirth <= dayOfNow) {
			diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
			diffDays = dayOfNow - dayOfBirth;
			if (diffMonths == 0)
				diffDays++;
		} else {
			if (isEndOfMonth(calendarBirth)) {
				if (isEndOfMonth(calendarNow)) {
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
					diffDays = 0;
				} else {
					calendarNow.add(Calendar.MONTH, -1);
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
					diffDays = dayOfNow + 1;
				}
			} else {
				if (isEndOfMonth(calendarNow)) {
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
					diffDays = 0;
				} else {
					calendarNow.add(Calendar.MONTH, -1);// 上个月
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
					// 获取上个月最大的一天
					int maxDayOfLastMonth = calendarNow
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					if (maxDayOfLastMonth > dayOfBirth) {
						diffDays = maxDayOfLastMonth - dayOfBirth + dayOfNow;
					} else {
						diffDays = dayOfNow;
					}
				}
			}
		}
		// 计算月份时，没有考虑年
		diffYears = diffMonths / 12;
		diffMonths = diffMonths % 12;
		return new int[] { diffYears, diffMonths, diffDays };
	}

	/**
	 * 获取两个日历的月份之差
	 * 
	 * @param calendarBirth
	 * @param calendarNow
	 * @return
	 */
	public static int getMonthsOfAge(Calendar calendarBirth,
			Calendar calendarNow) {
		return (calendarNow.get(Calendar.YEAR) - calendarBirth
				.get(Calendar.YEAR))
				* 12
				+ calendarNow.get(Calendar.MONTH)
				- calendarBirth.get(Calendar.MONTH);
	}

	/**
	 * 判断这一天是否是月底
	 * 
	 * @param calendar
	 * @return
	 */
	public static boolean isEndOfMonth(Calendar calendar) {
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth == calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
			return true;
		return false;
	}
	
	public static Date getLstDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	public static Date getFirstDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}
	
	public static Date getFirstDayofWeek(Date date) throws Exception {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		int weekOfDay = (c.get(Calendar.DAY_OF_WEEK));
		if (weekOfDay == 1) { //周日。
			weekOfDay = 7;
		} else {
			weekOfDay--;
		}
		c.add(Calendar.DAY_OF_WEEK, -weekOfDay+1);
		return c.getTime();
	}
	
	public static Date getLstDayofWeek(Date date) throws Exception {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		int weekOfDay = (c.get(Calendar.DAY_OF_WEEK));
		if (weekOfDay != 1) { //非周日。
			weekOfDay = 7 - (weekOfDay-1);
		} else { //周日就是当天
			weekOfDay = 0;
		}
		c.add(Calendar.DAY_OF_WEEK, weekOfDay);
		return c.getTime();
	}
	
	public static void main(String[] arg) {
		System.out.println(DateUtil.extractTimeFromStr("2017-08-09"));
	}
}

