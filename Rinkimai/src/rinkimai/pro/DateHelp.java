package rinkimai.pro;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateHelp {
	public static Boolean isFirstDateBigger(String firstDate, String secondDate){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = (Date) formatter.parse(firstDate);
			Date date2 = (Date) formatter.parse(secondDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
						
			ManoData me = new ManoData(cal);
			ManoData me2 = new ManoData(cal2);

			if(me.isBigger(me2)){
				return true;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String formatDate(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat formatter2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		String b = "";
		try {
			Date a1 = (Date) formatter2.parse(date);
			b = formatter.format(a1);
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return b;
	}
}
