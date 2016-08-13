package com.ccthanking.framework.util;

import sun.io.ByteToCharConverter;
import sun.io.CharToByteConverter;

public class EncodeUtil {
	public EncodeUtil() {
	}

	/**
	 * 返回给定汉字串的首字母串,即声母串 此方法基于汉字的国标汉字库区位编码的有效性，不符合此编码的系统此函数无效
	 * 若汉字串含有非汉字字符,如图形符号或ASCII码,则这些非汉字字符将保持不变.
	 * 
	 * @param strinput
	 * @return 声母串
	 * @throws Exception
	 */
	public static String spellCodes(String strinput) throws Exception {
		if (strinput == null || "".equals(strinput))
			return null;
		// 存放国标一级汉字不同读音的起始区位码
		int[] li_SecPosValue = { 1601, 1637, 1833, 2078, 2274, 2302, 2433,
				2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
				4086, 4390, 4558, 4684, 4925, 5249 };
		// 存放国标一级汉字不同读音的起始区位码对应读音
		String[] lc_FirstLetter = { "A", "B", "C", "D", "E", "F", "G", "H",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "W",
				"X", "Y", "Z" };
		// 存放所有国标二级汉字读音
		String ls_SecondSecTable = "CJWGNSPGCGNE[Y[BTYYZDXYKYGT[JNNJQMBSGZSCYJSYY[PGKBZGY[YWJKGKLJYWKPJQHY[W[DZLSGMRYPYWWCCKZNKYYGTT"
				+ "NJJNYKKZYTCJNMCYLQLYPYQFQRPZSLWBTGKJFYXJWZLTBNCXJJJJTXDTTSQZYCDXXHGCK[PHFFSS[YBGXLPPBYLL[HLXS[ZM"
				+ "[JHSOJNGHDZQYKLGJHSGQZHXQGKEZZWYSCSCJXYEYXADZPMDSSMZJZQJYZC[J[WQJBYZPXGZNZCPWHKXHQKMWFBPBYDTJZZK"
				+ "QHYLYGXFPTYJYYZPSZLFCHMQSHGMXXSXJ[[DCSBBQBEFSJYHXWGZKPYLQBGLDLCCTNMAYDDKSSNGYCSGXLYZAYBNPTSDKDYLH"
				+ "GYMYLCXPY[JNDQJWXQXFYYFJLEJPZRXCCQWQQSBNKYMGPLBMJRQCFLNYMYQMSQYRBCJTHZTQFRXQHXMJJCJLXQGJMSHZKBSWYE"
				+ "MYLTXFSYDSWLYCJQXSJNQBSCTYHBFTDCYZDJWYGHQFRXWCKQKXEBPTLPXJZSRMEBWHJLBJSLYYSMDXLCLQKXLHXJRZJMFQHXHW"
				+ "YWSBHTRXXGLHQHFNM[YKLDYXZPYLGG[MTCFPAJJZYLJTYANJGBJPLQGDZYQYAXBKYSECJSZNSLYZHSXLZCGHPXZHZNYTDSBCJK"
				+ "DLZAYFMYDLEBBGQYZKXGLDNDNYSKJSHDLYXBCGHXYPKDJMMZNGMMCLGWZSZXZJFZNMLZZTHCSYDBDLLSCDDNLKJYKJSYCJLKWH"
				+ "QASDKNHCSGANHDAASHTCPLCPQYBSDMPJLPZJOQLCDHJJYSPRCHN[NNLHLYYQYHWZPTCZGWWMZFFJQQQQYXACLBHKDJXDGMMYDJ"
				+ "XZLLSYGXGKJRYWZWYCLZMSSJZLDBYD[FCXYHLXCHYZJQ[[QAGMNYXPFRKSSBJLYXYSYGLNSCMHZWWMNZJJLXXHCHSY[[TTXRYC"
				+ "YXBYHCSMXJSZNPWGPXXTAYBGAJCXLY[DCCWZOCWKCCSBNHCPDYZNFCYYTYCKXKYBSQKKYTQQXFCWCHCYKELZQBSQYJQCCLMTHS"
				+ "YWHMKTLKJLYCXWHEQQHTQH[PQ[QSCFYMNDMGBWHWLGSLLYSDLMLXPTHMJHWLJZYHZJXHTXJLHXRSWLWZJCBXMHZQXSDZPMGFCS"
				+ "GLSXYMJSHXPJXWMYQKSMYPLRTHBXFTPMHYXLCHLHLZYLXGSSSSTCLSLDCLRPBHZHXYYFHB[GDMYCNQQWLQHJJ[YWJZYEJJDHPB"
				+ "LQXTQKWHLCHQXAGTLXLJXMSL[HTZKZJECXJCJNMFBY[SFYWYBJZGNYSDZSQYRSLJPCLPWXSDWEJBJCBCNAYTWGMPAPCLYQPCLZ"
				+ "XSBNMSGGFNZJJBZSFZYNDXHPLQKZCZWALSBCCJX[YZGWKYPSGXFZFCDKHJGXDLQFSGDSLQWZKXTMHSBGZMJZRGLYJBPMLMSXLZ"
				+ "JQQHZYJCZYDJWBMYKLDDPMJEGXYHYLXHLQYQHKYCWCJMYYXNATJHYCCXZPCQLBZWWYTWBQCMLPMYRJCCCXFPZNZZLJPLXXYZTZ"
				+ "LGDLDCKLYRZZGQTGJHHGJLJAXFGFJZSLCFDQZLCLGJDJCSNZLLJPJQDCCLCJXMYZFTSXGCGSBRZXJQQCTZHGYQTJQQLZXJYLYL"
				+ "BCYAMCSTYLPDJBYREGKLZYZHLYSZQLZNWCZCLLWJQJJJKDGJZOLBBZPPGLGHTGZXYGHZMYCNQSYCYHBHGXKAMTXYXNBSKYZZGJ"
				+ "ZLQJDFCJXDYGJQJJPMGWGJJJPKQSBGBMMCJSSCLPQPDXCDYYKY[CJDDYYGYWRHJRTGZNYQLDKLJSZZGZQZJGDYKSHPZMTLCPWN"
				+ "JAFYZDJCNMWESCYGLBTZCGMSSLLYXQSXSBSJSBBSGGHFJLYPMZJNLYYWDQSHZXTYYWHMZYHYWDBXBTLMSYYYFSXJC[DXXLHJHF"
				+ "[SXZQHFZMZCZTQCXZXRTTDJHNNYZQQMNQDMMG[YDXMJGDHCDYZBFFALLZTDLTFXMXQZDNGWQDBDCZJDXBZGSQQDDJCMBKZFFXM"
				+ "KDMDSYYSZCMLJDSYNSBRSKMKMPCKLGDBQTFZSWTFGGLYPLLJZHGJ[GYPZLTCSMCNBTJBQFKTHBYZGKPBBYMTDSSXTBNPDKLEYC"
				+ "JNYDDYKZDDHQHSDZSCTARLLTKZLGECLLKJLQJAQNBDKKGHPJTZQKSECSHALQFMMGJNLYJBBTMLYZXDCJPLDLPCQDHZYCBZSCZB"
				+ "ZMSLJFLKRZJSNFRGJHXPDHYJYBZGDLQCSEZGXLBLGYXTWMABCHECMWYJYZLLJJYHLG[DJLSLYGKDZPZXJYYZLWCXSZFGWYYDLY"
				+ "HCLJSCMBJHBLYZLYCBLYDPDQYSXQZBYTDKYXJY[CNRJMPDJGKLCLJBCTBJDDBBLBLCZQRPPXJCJLZCSHLTOLJNMDDDLNGKAQHQ"
				+ "HJGYKHEZNMSHRP[QQJCHGMFPRXHJGDYCHGHLYRZQLCYQJNZSQTKQJYMSZSWLCFQQQXYFGGYPTQWLMCRNFKKFSYYLQBMQAMMMYX"
				+ "CTPSHCPTXXZZSMPHPSHMCLMLDQFYQXSZYYDYJZZHQPDSZGLSTJBCKBXYQZJSGPSXQZQZRQTBDKYXZKHHGFLBCSMDLDGDZDBLZY"
				+ "YCXNNCSYBZBFGLZZXSWMSCCMQNJQSBDQSJTXXMBLTXZCLZSHZCXRQJGJYLXZFJPHYMZQQYDFQJJLZZNZJCDGZYGCTXMZYSCTLK"
				+ "PHTXHTLBJXJLXSCDQXCBBTJFQZFSLTJBTKQBXXJJLJCHCZDBZJDCZJDCPRNPQCJPFCZLCLZXZDMXMPHJSGZGSZZQLYLWTJPFSY"
				+ "ASMCJBTZKYCWMYTCSJJLJCQLWZMALBXYFBPNLSFHTGJWEJJXXGLLJSTGSHJQLZFKCGNNNSZFDEQFHBSAQTGYLBXMMYGSZLDYDQ"
				+ "MJJRGBJTKGDHGKBLQKBDMBYLXWCXYTTYBKMRTJZXQJBHLMHMJJZMQASLDCYXYQDLQCAFYWYXQHZ";
		// 返回串
		String ls_ReturnStr = "";
		try {
			for (int i = 0; i < strinput.length(); i++) { // 依次处理as_InputString中每个字符
				String s = strinput.substring(i, i + 1);
				byte b[] = s.getBytes();
				if (b.length == 1) { // 如果不是汉字,则直接返回
					ls_ReturnStr = ls_ReturnStr
							+ strinput.substring(i, i + 1).toUpperCase();
				} else {
					int li_SectorCode = 256 + (int) b[0] - 160; // 区码
					int li_PositionCode = 256 + (int) b[1] - 160; // 位码
					int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 区位码
					if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
						for (int j = 22; j >= 0; j--) { // 找声母
							if (li_SecPosCode >= li_SecPosValue[j]) {
								ls_ReturnStr = ls_ReturnStr + lc_FirstLetter[j];
								break;
							}
						}
					} else { // 需要计算偏移量
						int li_offset = (li_SectorCode - 56) * 94
								+ li_PositionCode - 1;
						if (li_offset >= 0 && li_offset <= 3007) { // 二区汉字
							ls_ReturnStr = ls_ReturnStr
									+ ls_SecondSecTable.substring(li_offset,
											li_offset + 1); // 取出此字声母
						} else {
							ls_ReturnStr = ls_ReturnStr + strinput.charAt(i); // 如果是特殊字符保留
						}

					}

				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return (ls_ReturnStr);
	}

	/**
	 * 将中文转化成AscII码存入数据库中
	 * 
	 * @param s
	 * @return 16进制字符串
	 */
	public static String ChineseStringToAscii(String s) {
		try {
			CharToByteConverter toByte = CharToByteConverter
					.getConverter("GBK");
			byte[] orig = toByte.convertAll(s.toCharArray());
			char[] dest = new char[orig.length];
			for (int i = 0; i < orig.length; i++)
				dest[i] = (char) (orig[i] & 0xFF);
			return new String(dest);
		} catch (Exception e) {
			System.out.println(e);
			return s;
		}
	}

	/**
	 * 将AscII字符转换成汉字
	 * 
	 * @param s
	 * @return 汉字
	 */
	public static String AsciiToChineseString(String s) {
		char[] orig = s.toCharArray();
		byte[] dest = new byte[orig.length];
		for (int i = 0; i < orig.length; i++)
			dest[i] = (byte) (orig[i] & 0xFF);
		try {
			ByteToCharConverter toChar = ByteToCharConverter
					.getConverter("GBK");
			return new String(toChar.convertAll(dest));
		} catch (Exception e) {
			System.out.println(e);
			return s;
		}
	}

	// 主函数 调试使用
	public static void main2(String args[]) {

		String test = "a胡光华";
		EncodeUtil ss = new EncodeUtil();
		try {
			String dd = "df"; // ss.MnemonicWords(test);
			System.out.println(dd);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static String toGBK(String s)
			throws java.io.UnsupportedEncodingException {
		return s == null ? null : new String(s.getBytes("ISO-8859-1"), "GBK");

		// return s==null?null:new String(s.getBytes("GBK"),"ISO-8859-1") ;
	}

	/**
	 * < &lt ; >&gt ;
	 * 
	 * @param value
	 *            the source value to be encoded
	 * @return encoded value
	 */
	/**
	 * html encode
	 * 
	 * @param value
	 * @return
	 */
	public static String htmlEncode(String value) {
		if (value == null)
			return null;
		StringBuffer result = new StringBuffer(100);
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '<':
				result.append("&lt");
				break;
			case '>':
				result.append("&gt");
				break;
			case ' ':
				result.append("&nbsp;");
				break;
			case '"':
				result.append("&#34;");
				break;
			case '\r':
			case '\n':
				result.append("<br>");
				break;
			default:
				result.append(c);
			}
		}
		return result + "";
	}

	public static void main(String[] argv) throws Exception {

		String a = "<a href='dfd.com' >a </a>";
		String res = htmlEncode(a);
		System.out.println(res);

	}
}