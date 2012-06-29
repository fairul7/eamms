package com.tms.fms.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DecimalUtil {
	public static BigDecimal getDecimal(String num) {
		if (num != null && !num.equals("")) {
			num = stripCommas(num);
			
			try {
				return new BigDecimal(num);
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}
	
	public static String formatDecimal(BigDecimal dec, int scale) {
		if (dec != null) {
			BigDecimal fDec = dec.setScale(scale, BigDecimal.ROUND_HALF_UP);
			
			// format integer part
			DecimalFormat df = new DecimalFormat("0,000");
			long front = fDec.longValue();
			String result = df.format(front);
			
			// format fractional part
			String fraction = fDec.toPlainString();
			if (fraction.indexOf(".") > 0) {
				fraction = fraction.substring(fraction.indexOf("."));
				result += fraction;
			}
			
			return result;
		}
		return "";
	}
	
	public static String getPlainDecimal(BigDecimal dec, int scale) {
		if (dec != null) {
			BigDecimal fDec = dec.setScale(scale, BigDecimal.ROUND_HALF_UP);
			return fDec.toPlainString();
		}
		return "";
	}
	
	public static String stripCommas(String num) {
		num = num.replaceAll(",", "");
		return num;
	}
}
