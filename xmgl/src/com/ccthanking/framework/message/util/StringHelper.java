package com.ccthanking.framework.message.util;

import java.util.StringTokenizer;

public class StringHelper {
	
	public StringHelper() {
	}

	public static String[] split(String source, String delim) {
		String[] wordLists;
		if (source == null) {
			wordLists = new String[1];
			wordLists[0] = source;
			return wordLists;
		}
		if (delim == null) {
			delim = ";";
		}
		StringTokenizer st = new StringTokenizer(source, delim);

		int total = st.countTokens();
		wordLists = new String[total];
		for (int i = 0; i < total; i++) {
			wordLists[i] = st.nextToken();
		}
		return wordLists;
	}

}