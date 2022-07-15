package com.apprefrig.services;

import java.util.Calendar;

public class CalendarService {
	
	public static Calendar getMonthStart() {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH,1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);//set the calendar to fist day of month at 00:00:00
		
		return c;
	}

}
