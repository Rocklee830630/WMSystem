package com.ccthanking.framework.util;

public class Cryption {

	// for encode and decode
	String ENCODE_KEY1 = "zxcvbnm,./asdfg";
	String ENCODE_KEY_A = "cjk;";
	String ENCODE_KEY2 = "hjkl;'qwertyuiop";
	String ENCODE_KEY_B = "cai2";
	String ENCODE_KEY3 = "[]\\1234567890-";
	String ENCODE_KEY_C = "%^@#";
	String ENCODE_KEY4 = "=` ZXCVBNM<>?:LKJ";
	String ENCODE_KEY_D = "*(N";
	String ENCODE_KEY5 = "HGFDSAQWERTYUI";
	String ENCODE_KEY_E = "%^HJ";
	String ENCODE_KEY6 = "OP{}|+_)(*&^%$#@!~";

	public Cryption() {
	}

	/**
	 * 字符串解密
	 * 
	 * @param varCode
	 *            加密字符串
	 * @return 源码
	 * @exception java.lang.Exception
	 */

	// DES解密；
	public String decryption(String varCode) throws Exception {
		int n;
		int num1, num2, num3;
		Integer helpint;
		String des;
		String strKey;
		String DecodeStr; // 返回值
		char xorstr;

		try {

			if (varCode.length() == 0) {
				DecodeStr = "";
				return DecodeStr;
			}
			strKey = ENCODE_KEY1 + ENCODE_KEY2 + ENCODE_KEY3 + ENCODE_KEY4
					+ ENCODE_KEY5 + ENCODE_KEY6;

			if (varCode.length() % 2 == 1)
				varCode = varCode + "?";
			des = "";
			int midNum = Math.round(varCode.length() / 2 - 1);

			for (n = 0; n <= midNum; n++) {
				num1 = varCode.charAt(n * 2);
				num2 = strKey.indexOf(varCode.charAt(n * 2 + 1));
				num3 = num1 ^ num2;
				xorstr = (char) num3;

				des = des + xorstr;
			}
			int onenum = 1;
			char onestr = (char) onenum;
			n = des.indexOf(onestr);

			if (n >= 0)
				DecodeStr = des.substring(0, n);
			else
				DecodeStr = des;
		} catch (Exception ex) {
			DecodeStr = "Exception/Message:" + ex.getMessage();

		}
		return DecodeStr;
	}

	/**
	 * 字符串加密
	 * 
	 * @param sSource
	 *            源码
	 * @return 结果码
	 * @exception java.lang.Exception
	 */
	// DES加密
	public String encryption(String sSource) throws Exception {
		int n;
		int code; // byte
		String des;
		String strKey;
		int num1, num2;
		char charcode;
		String EncodeStr; // 返回值

		try {
			if (sSource.length() == 0) {
				EncodeStr = "";
				return EncodeStr;
			}
			strKey = ENCODE_KEY1 + ENCODE_KEY2 + ENCODE_KEY3 + ENCODE_KEY4
					+ ENCODE_KEY5 + ENCODE_KEY6;
			int onenum = 1;
			char onestr = (char) onenum;
			while (sSource.length() < 8)
				sSource = sSource + onestr;

			des = "";

			for (n = 1; n <= sSource.length(); n++) {

				while (true) {
					double myrandom = Math.random();
					code = (int) Math.round(myrandom * 100);
					num1 = sSource.charAt(n - 1);
					while ((code > 0)
							&& (((code ^ num1) < 0) || ((code ^ num1) > 90)))
						code = code - 1;
					charcode = (char) code;
					if ((code > 35) && (code < 122) && (charcode != '|')
							&& (charcode != '\'') && (charcode != ',')
							&& (strKey.charAt(code ^ num1) != '|')
							&& (strKey.charAt(code ^ num1) != '\'')
							&& (strKey.charAt(code ^ num1) != ','))
						break;
				}
				charcode = (char) code;
				// 确保生成的字符是可见字符并且在SQL语句中有效
				num2 = sSource.charAt(n - 1);
				des = des + charcode + strKey.charAt(code ^ num2);
			}
			EncodeStr = des;
		} catch (Exception ex) {
			EncodeStr = "Exception/Message:" + ex.getMessage();

		}
		return EncodeStr;
	}

}
