package com.apprefrig.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.springframework.stereotype.Service;

@Service
public class CalendarService {
	
	public static Calendar getMonthStart() {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH,1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);//set the calendar to fist day of month at 00:00:00
		
		return c;
	}

	public static Calendar getMonthBeforeStart() {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH,1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.MONTH, -1);//set the calendar to fist day of month at 00:00:00

		return c;
	}
	
	public static Calendar getOneMonthAgo() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		return c;
	}


}
