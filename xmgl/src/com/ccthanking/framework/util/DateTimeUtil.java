package com.ccthanking.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类<br>
 * 提供一些常用的日期时间操作方法，所有方法都为静态，不用实例化该类即可使用。<br>
 * <br>
 * 下为日期格式的简单描述详情请参看java API中java.text.SimpleDateFormat<br>
 * The following pattern letters are defined (all other characters from
 * <code>'A'</code> to <code>'Z'</code> and from <code>'a'</code> to
 * <code>'z'</code> are reserved): <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0>
 * <tr bgcolor="#ccccff">
 * <th align=left>Letter
 * <th align=left>Date or Time Component
 * <th align=left>Presentation
 * <th align=left>Examples
 * <tr>
 * <td><code>G</code>
 * <td>Era designator
 * <td><a href="#text">Text</a>
 * <td><code>AD</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>y</code>
 * <td>Year
 * <td><a href="#year">Year</a>
 * <td><code>1996</code>; <code>96</code>
 * <tr>
 * <td><code>M</code>
 * <td>Month in year
 * <td><a href="#month">Month</a>
 * <td><code>July</code>; <code>Jul</code>; <code>07</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>w</code>
 * <td>Week in year
 * <td><a href="#number">Number</a>
 * <td><code>27</code>
 * <tr>
 * <td><code>W</code>
 * <td>Week in month
 * <td><a href="#number">Number</a>
 * <td><code>2</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>D</code>
 * <td>Day in year
 * <td><a href="#number">Number</a>
 * <td><code>189</code>
 * <tr>
 * <td><code>d</code>
 * <td>Day in month
 * <td><a href="#number">Number</a>
 * <td><code>10</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>F</code>
 * <td>Day of week in month
 * <td><a href="#number">Number</a>
 * <td><code>2</code>
 * <tr>
 * <td><code>E</code>
 * <td>Day in week
 * <td><a href="#text">Text</a>
 * <td><code>Tuesday</code>; <code>Tue</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>a</code>
 * <td>Am/pm marker
 * <td><a href="#text">Text</a>
 * <td><code>PM</code>
 * <tr>
 * <td><code>H</code>
 * <td>Hour in day (0-23)
 * <td><a href="#number">Number</a>
 * <td><code>0</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>k</code>
 * <td>Hour in day (1-24)
 * <td><a href="#number">Number</a>
 * <td><code>24</code>
 * <tr>
 * <td><code>K</code>
 * <td>Hour in am/pm (0-11)
 * <td><a href="#number">Number</a>
 * <td><code>0</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>h</code>
 * <td>Hour in am/pm (1-12)
 * <td><a href="#number">Number</a>
 * <td><code>12</code>
 * <tr>
 * <td><code>m</code>
 * <td>Minute in hour
 * <td><a href="#number">Number</a>
 * <td><code>30</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>s</code>
 * <td>Second in minute
 * <td><a href="#number">Number</a>
 * <td><code>55</code>
 * <tr>
 * <td><code>S</code>
 * <td>Millisecond
 * <td><a href="#number">Number</a>
 * <td><code>978</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>z</code>
 * <td>Time zone
 * <td><a href="#timezone">General time zone</a>
 * <td><code>Pacific Standard Time</code>; <code>PST</code>;
 * <code>GMT-08:00</code>
 * <tr>
 * <td><code>Z</code>
 * <td>Time zone
 * <td><a href="#rfc822timezone">RFC 822 time zone</a>
 * <td><code>-0800</code>
 * </table>
 * </blockquote> <h4>Examples</h4>
 * 
 * The following examples show how date and time patterns are interpreted in the
 * U.S. locale. The given date and time are 2001-07-04 12:08:56 local time in
 * the U.S. Pacific Time time zone. <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0>
 * <tr bgcolor="#ccccff">
 * <th align=left>Date and Time Pattern
 * <th align=left>Result
 * <tr>
 * <td><code>"yyyy.MM.dd G 'at' HH:mm:ss z"</code>
 * <td><code>2001.07.04 AD at 12:08:56 PDT</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"EEE, MMM d, ''yy"</code>
 * <td><code>Wed, Jul 4, '01</code>
 * <tr>
 * <td><code>"h:mm a"</code>
 * <td><code>12:08 PM</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"hh 'o''clock' a, zzzz"</code>
 * <td><code>12 o'clock PM, Pacific Daylight Time</code>
 * <tr>
 * <td><code>"K:mm a, z"</code>
 * <td><code>0:08 PM, PDT</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"yyyyy.MMMMM.dd GGG hh:mm aaa"</code>
 * <td><code>02001.July.04 AD 12:08 PM</code>
 * <tr>
 * <td><code>"EEE, d MMM yyyy HH:mm:ss Z"</code>
 * <td><code>Wed, 4 Jul 2001 12:08:56 -0700</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>"yyMMddHHmmssZ"</code>
 * <td><code>010704120856-0700</code>
 * </table>
 * </blockquote>
 */

public class DateTimeUtil {

	/**
	 * 缺省的日期显示格式： yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 私有构造方法，禁止对该类进行实例化
	 */
	private DateTimeUtil() {
	}

	/**
	 * 得到系统当前日期时间
	 * 
	 * @return 当前日期时间
	 */
	public static Date getNow() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 得到用缺省方式格式化的当前日期
	 * 
	 * @return 当前日期
	 */
	public static String getDate() {
		return getDateTime(DEFAULT_DATE_FORMAT);
	}

	/**
	 * 得到用缺省方式格式化的当前日期及时间
	 * 
	 * @return 当前日期及时间
	 */
	public static String getDateTime() {
		return getDateTime(DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * 得到系统当前日期及时间，并用指定的方式格式化
	 * 
	 * @param pattern
	 *            显示格式
	 * @return 当前日期及时间
	 */
	public static String getDateTime(String pattern) {
		Date datetime = Calendar.getInstance().getTime();
		return getDateTime(datetime, pattern);
	}

	/**
	 * 得到用指定方式格式化的日期
	 * 
	 * @param date
	 *            需要进行格式化的日期
	 * @param pattern
	 *            显示格式
	 * @return 日期时间字符串
	 */
	public static String getDateTime(Date date, String pattern) {
		if (null == pattern || "".equals(pattern)) {
			pattern = DEFAULT_DATETIME_FORMAT;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/**
	 * 得到当前年份
	 * 
	 * @return 当前年份
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 得到当前月份
	 * 
	 * @return 当前月份
	 */
	public static int getCurrentMonth() {
		// 用get得到的月份数比实际的小1，需要加上
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到当前日
	 * 
	 * @return 当前日
	 */
	public static int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	/**
	 * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
	 * 
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期
	 */
	public static Date addDays(int days) {
		return add(getNow(), days, Calendar.DATE);
	}

	/**
	 * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
	 * 
	 * @param date
	 *            基准日期
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期
	 */
	public static Date addDays(Date date, int days) {
		return add(date, days, Calendar.DATE);
	}

	/**
	 * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
	 * 
	 * @param months
	 *            增加的月份数
	 * @return 增加以后的日期
	 */
	public static Date addMonths(int months) {
		return add(getNow(), months, Calendar.MONTH);
	}

	/**
	 * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
	 * 
	 * @param date
	 *            基准日期
	 * @param months
	 *            增加的月份数
	 * @return 增加以后的日期
	 */
	public static Date addMonths(Date date, int months) {
		return add(date, months, Calendar.MONTH);
	}

	/**
	 * 内部方法。为指定日期增加相应的天数或月数
	 * 
	 * @param date
	 *            基准日期
	 * @param amount
	 *            增加的数量
	 * @param field
	 *            增加的单位，年，月或者日
	 * @return 增加以后的日期
	 */
	private static Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.add(field, amount);

		return calendar.getTime();
	}

	/**
	 * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差天数
	 */
	public static long diffDays(Date one, Date two) {
		return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差月份数
	 */
	public static int diffMonths(Date one, Date two) {

		Calendar calendar = Calendar.getInstance();

		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		int yearOne = calendar.get(Calendar.YEAR);
		int monthOne = calendar.get(Calendar.MONDAY);

		// 得到第二个日期的年份和月份
		calendar.setTime(two);
		int yearTwo = calendar.get(Calendar.YEAR);
		int monthTwo = calendar.get(Calendar.MONDAY);

		return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	}

	/**
	 * 将一个字符串用给定的格式转换为日期类型。<br>
	 * 注意：如果返回null，则表示解析失败
	 * 
	 * @param datestr
	 *            需要解析的日期字符串
	 * @param pattern
	 *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
	 * @return 解析后的日期
	 */
	public static Date parse(String datestr, String pattern) {
		Date date = null;

		if (null == pattern || "".equals(pattern)) {
			pattern = DEFAULT_DATE_FORMAT;
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			date = dateFormat.parse(datestr);
		} catch (ParseException e) {
			//
		} catch (Exception e) {
			//
		}

		return date;
	}

	/**
	 * 返回本月的最后一天
	 * 
	 * @return 本月最后一天的日期
	 */
	public static Date getMonthLastDay() {
		return getMonthLastDay(getNow());
	}

	/**
	 * 返回给定日期中的月份中的最后一天
	 * 
	 * @param date
	 *            基准日期
	 * @return 该月最后一天的日期
	 */
	public static Date getMonthLastDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1, 1);

		// 减去1天，得到的即本月的最后一天
		calendar.add(Calendar.DATE, -1);

		return calendar.getTime();
	}
	
	public static Date getMonthFirstDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), 1);

		// 减去1天，得到的即本月的最后一天
		//calendar.add(Calendar.DATE, -1);

		return calendar.getTime();
	}

	public static void main(String[] args) {

		String sss = getDateTime("yyyy年MM月dd日");
		String test = "2003-1-31";
		Date date;
		try {
			date = parse(test, "");

			System.out.println("得到当前日期 － getDate():" + DateTimeUtil.getDate());
			System.out.println("得到当前日期时间 － getDateTime():"
					+ DateTimeUtil.getDateTime());

			System.out.println("得到当前年份 － getCurrentYear():"
					+ DateTimeUtil.getCurrentYear());
			System.out.println("得到当前月份 － getCurrentMonth():"
					+ DateTimeUtil.getCurrentMonth());
			System.out.println("得到当前日子 － getCurrentDay():"
					+ DateTimeUtil.getCurrentDay());

			System.out.println("解析 － parse(" + test + "):"
					+ getDateTime(date, "yyyy-MM-dd"));

			System.out.println("自增月份 － addMonths(3):"
					+ getDateTime(addMonths(3), "yyyy-MM-dd"));
			System.out.println("增加月份 － addMonths(" + test + ",3):"
					+ getDateTime(addMonths(date, 3), "yyyy-MM-dd"));
			System.out.println("增加日期 － addDays(" + test + ",3):"
					+ getDateTime(addDays(date, 3), "yyyy-MM-dd"));
			System.out.println("自增日期 － addDays(3):"
					+ getDateTime(addDays(3), "yyyy-MM-dd"));

			System.out.println("比较日期 － diffDays():"
					+ DateTimeUtil.diffDays(DateTimeUtil.getNow(), date));
			System.out.println("比较月份 － diffMonths():"
					+ DateTimeUtil.diffMonths(DateTimeUtil.getNow(), date));

			System.out.println("得到" + test + "所在月份的最后一天:"
					+ getDateTime(getMonthLastDay(date), "yyyy-MM-dd"));

			System.out.println("得到" + test + "所在月份的第一天:"
					+ getDateTime(getMonthFirstDay(date), "yyyy-MM-dd"));

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

	}

}
