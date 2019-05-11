package br.unifesp.offlineforms.mobile.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.util.Log;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class Utils {
	
	private static final String MESSAGE = "message";
	
	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      );

	private static final String ACTIVITY_SERVICE = "activity";

	/**
	 * This method gets the message from error xml response like this: 
	 * <error code="500" status="Internal Server Error">
	 * 		<message>form doesn't exist!</message>
	 * </error>
	 * @return message
	 */
	public static String getMessageFromErrorResponse(String xmlError) {
		try {
			String message = null;
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();
	        xpp.setInput( new StringReader( xmlError ));
	        int eventType = xpp.getEventType();
	        while (eventType != XmlPullParser.END_DOCUMENT) {
	        	if(eventType == XmlPullParser.START_TAG && MESSAGE.equals(xpp.getName())) {
	        		eventType = xpp.next();
	        		if(eventType == XmlPullParser.TEXT) {
	        			message = xpp.getText();
	        			break;
	        		}
	        	}
	        	eventType = xpp.next();
	        }
			return message;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Date convertStringToDate(String val, String format) {
		if(val == null || "".equals(val.trim()))
			return null;
		try {
			DateFormat formatter = new SimpleDateFormat(format);
			Date date = (Date)formatter.parse(val);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String getDateFormat(String string) {
		if (string == null || "".equals(string))
			return Constants.DATE_ISO8601FORMAT;
		else
			return string;
	}
	
	public static long getAvailableMemory (Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		activityManager.getMemoryInfo(mi);
		return mi.availMem;
	}
}