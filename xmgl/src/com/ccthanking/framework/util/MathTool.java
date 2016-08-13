package com.ccthanking.framework.util;

import java.math.BigDecimal;

/**
 * 数学运算辅助类
 */

public class MathTool {

	/**
	 * 默认除法运算精度
	 */
	private static final int DEF_DIV_SCALE = 10;

	private MathTool() {
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	
	public static BigDecimal sub(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2);
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 将带逗号的字符串转换为double类型
	 * 
	 * @param value
	 *            将被转换的字符串
	 * @return 转换后得到的double型数字
	 */
	public static double parse(String value) {
		value = StringUtil.replace(value, ",", "");
		;
		return Double.valueOf(value).doubleValue();
	}

	/**
	 * 主要功能：为了防止浮点数以科学计数法形式显示出来,转化为普通计数法,最多精确到小数点后4位
	 * 
	 * @param value
	 *            将被转换的字符串
	 * @param length
	 *            精确到小数点后的位数
	 * @return 转换后得到的double型数字
	 */
	public static String convert(double value, int length) {
		java.text.DecimalFormat df = (java.text.DecimalFormat) java.text.NumberFormat
				.getInstance();
		df.setMinimumFractionDigits(length);
		df.setMaximumFractionDigits(length);
		return df.format(value);
	}

	/**
	 * 主要功能：为了防止浮点数以科学计数法形式显示出来,转化为普通计数法,最多精确到小数点后4位
	 * 
	 * @param value
	 *            将被转换的字符串
	 * @param length
	 *            精确到小数点后的位数
	 * @param bl
	 *            true 返回带","逗号的字符串; false 返回不带","逗号的字符串
	 * @return 转换后得到的double型数字
	 */
	public static String convert(double value, int length, boolean bl) {
		java.text.DecimalFormat df = (java.text.DecimalFormat) java.text.NumberFormat
				.getInstance();
		df.setMinimumFractionDigits(length);
		df.setMaximumFractionDigits(length);
		String str;
		if (bl != true) {
			return StringUtil.replace(df.format(value), ",", "");
		} else {
			return df.format(value);
		}
	}

	/**
	 * 主要功能：将金额转换成大写
	 * 
	 * @param money
	 *            将要被转换的金额数字
	 * @return 大写金额
	 */
	public static String convertMoneyToChinese(double money) {
		String b = "壹贰叁肆伍陆柒捌玖拾";
		String chinese = "";
		if (money < 0) {
			chinese = "负";
			money = 0 - money;
		}
		String moneySum = convert(money, 2, false);

		if (moneySum.substring(moneySum.indexOf("."), moneySum.length())
				.length() < 3) {
			moneySum = moneySum + "0";
			// 先将钱数补齐为15位
		}
		if (moneySum.length() > 15) {
			return "金额超大！";
		}
		while (moneySum.length() < 15) {
			moneySum = " " + moneySum;
		}
		if (moneySum.length() > 15) {
			moneySum = moneySum.substring(moneySum.length() - 15,
					moneySum.length());
		}

		// 获得各位的数值
		String x1 = moneySum.substring(0, 1);
		String x2 = moneySum.substring(1, 2);
		String x3 = moneySum.substring(2, 3);
		String x4 = moneySum.substring(3, 4);
		String x5 = moneySum.substring(4, 5);
		String x6 = moneySum.substring(5, 6);
		String x7 = moneySum.substring(6, 7);
		String x8 = moneySum.substring(7, 8);
		String x9 = moneySum.substring(8, 9);
		String x10 = moneySum.substring(9, 10);
		String x11 = moneySum.substring(10, 11);

		String x12 = moneySum.substring(11, 12);

		String x13 = moneySum.substring(13, 14);

		String x14 = moneySum.substring(14, 15);

		int temp = 0;

		if (!x1.equals(" ")) {
			temp = Integer.parseInt(x1);
			chinese = b.substring(temp - 1, temp) + "仟";
		}
		if (!x2.equals(" ")) {
			temp = Integer.parseInt(x2);
			if (x2.equals("0") && !x3.equals("0")) {
				chinese = chinese + "零";
			}
			if (!x2.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "佰";
			}
		}
		if (!x3.equals(" ")) {
			temp = Integer.parseInt(x3);
			if (x3.equals("0") && !x4.equals("0")) {
				chinese = chinese + "零";
			}
			if (!x3.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "拾";
			}
		}
		if (!x4.equals(" ")) {
			temp = Integer.parseInt(x4);
			if (x4.equals("0")) {
				chinese = chinese + "亿";
			} else {
				chinese = chinese + b.substring(temp - 1, temp) + "亿";
			}
		}

		if (!x5.equals(" ")) {
			temp = Integer.parseInt(x5);
			if (x5.equals("0") && !x6.equals("0")) {
				chinese = chinese + "零";
			}
			if (!x5.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "仟";

			}
		}
		if (!x6.equals(" ")) {
			temp = Integer.parseInt(x6);
			if (x6.equals("0") && !x7.equals("0")) {
				chinese = chinese + "零";
			}
			if (!x6.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "佰";
			}
		}
		if (!x7.equals(" ")) {
			temp = Integer.parseInt(x7);
			if (x7.equals("0") && !x8.equals("0")) {
				chinese = chinese + "零";
			}
			if (!x7.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "拾";
			}
		}
		if (!x8.equals(" ")) {
			temp = Integer.parseInt(x8);
			if (x8.equals("0")) {
				chinese = chinese + "万";
			} else {
				chinese = chinese + b.substring(temp - 1, temp) + "万";
			}
		}
		if (!x9.equals(" ")) {
			temp = Integer.parseInt(x9);
			if (x9.equals("0") && !x10.equals("0")) {
				chinese = chinese + "零";
			}
			if (!x9.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "仟";
			}
		}
		if (!x10.equals(" ")) {
			temp = Integer.parseInt(x10);
			if (x10.equals("0") && !x11.equals("0")) {
				chinese = chinese + "零";
			}
			if (!x10.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "佰";
			}
		}
		if (!x11.equals(" ")) {
			temp = Integer.parseInt(x11);
			if (x11.equals("0") && !x12.equals("0")) {
				chinese = chinese + "零";
			}
			if (!x11.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "拾";
			}
		}
		if (!x12.equals(" ")) {
			temp = Integer.parseInt(x12);
			if (x12.equals("0")) {
				if (x11.equals(" ")) {
					if (x13.equals("0") && x14.equals("0")) {
						chinese = "零圆";
					}
				} else {
					chinese += "圆";
				}
			} else {
				chinese = chinese + b.substring(temp - 1, temp) + "圆";
			}
			// if(x12.equals("0") && x11.equals(" ") && x13.equals("0") &&
			// x14.equals("0"))
			// chinese = "零圆";
			// else if(x12.equals("0") && (!x13.equals("0") ||
			// !x14.equals("0"))){
			// chinese = chinese + "圆";
			// }
			// else if (x12.equals("0"))
			// chinese = chinese + "圆";
			// else
			// chinese = chinese + b.substring(temp - 1, temp) + "圆";
		}
		if (!x13.equals(" ")) {
			temp = Integer.parseInt(x13);
			if ((chinese.length() > 0) && !chinese.equals("负")
					&& x13.equals("0") && !x14.equals("0")) {
				chinese = chinese + "零";
			} else if (!x13.equals("0")) {
				chinese = chinese + b.substring(temp - 1, temp) + "角";
			}
		}
		if (!x14.equals(" ")) {
			temp = Integer.parseInt(x14);
			if (x14.equals("0")) {
				if (x13.equals("0")) {
					chinese = chinese + "整";
				}
			} else {
				chinese = chinese + b.substring(temp - 1, temp) + "分";
			}
		}
		return chinese;
	}
	
	/**
	 * 使用BigDecimal达到精确计算数字相加
	 * @param amountA
	 * @param amountB
	 * @return
	 */
	public static String add(String amountA,String amountB){
		if(Pub.empty(amountA) || Pub.empty(amountB)){
			return null;
		}
		try{
			BigDecimal a = new BigDecimal(amountA);
			BigDecimal b = new BigDecimal(amountB);
			return a.add(b).toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 使用BigDecimal达到精确计算数字相乘
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static String mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).toString();
	}

	public static void main(String[] args) {

		double a = 100033.098394993;
		double b = 20.3993;
		String c = "100,124,000.00";

		System.out.println("加法：" + String.valueOf(a) + "+" + String.valueOf(b)
				+ "=" + String.valueOf(add(a, b)));
		System.out.println("减法：" + String.valueOf(a) + "-" + String.valueOf(b)
				+ "=" + String.valueOf(sub(a, b)));
		System.out.println("乘法：" + String.valueOf(a) + "*" + String.valueOf(b)
				+ "=" + String.valueOf(mul(a, b)));
		System.out.println("除法：" + String.valueOf(a) + "/" + String.valueOf(b)
				+ "=" + String.valueOf(div(a, b)));
		System.out.println("格式化[" + a + "]保留5位小数：" + convert(a, 5, true));
		System.out.println("四舍五入[" + a + "]保留1位小数："
				+ String.valueOf(round(a, 1)));
		System.out.println("解析字符串[" + c + "]：" + String.valueOf(parse(c)));
		System.out.println("大写转换[" + c + "]："
				+ String.valueOf(convertMoneyToChinese(parse(c))));

	}

}