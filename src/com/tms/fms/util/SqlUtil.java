package com.tms.fms.util;

public class SqlUtil {
	public static String placeholders(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			if (i == 0) {
				sb.append("?");
			} else {
				sb.append(", ?");
			}
		}
		return sb.toString();
	}
}
