/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat extends Object
{
	public static String format(Date theDate, String dateFormat)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String strTimeStamp = formatter.format(theDate);
		return strTimeStamp;
	}

}
