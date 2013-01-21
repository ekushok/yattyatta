package my.ekushok.grief;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyDateControl {
	private Calendar cal = new GregorianCalendar();
	private Date date;
	
	MyDateControl(long timestamp){
		date = new Date(timestamp);
		cal.setTime(date);
	}
	
	public String myGetDate(){
		String str = "";
		str = cal.get(Calendar.YEAR)+"/"
				+cal.get(Calendar.MONTH)+"/"
				+cal.get(Calendar.DATE)+"\n"
				+cal.get(Calendar.HOUR_OF_DAY)+":"
				+cal.get(Calendar.MINUTE)+":"
				+cal.get(Calendar.SECOND);
		return str;
	}
}
