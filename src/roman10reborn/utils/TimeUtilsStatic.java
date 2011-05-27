package roman10reborn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtilsStatic {
	public static String dateToStringFormat(Date d) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy MMMM dd EEE HH:mm");
		return formater.format(d);
	}
	
	public static String dateToStringFormat2(Date d) {
		SimpleDateFormat formater = new SimpleDateFormat("yy MMM dd EEE hh:mm a");
		return formater.format(d);
	}
}
