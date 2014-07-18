package rinkimai.pro;
import java.util.Calendar;


public class ManoData {
	private int metai;
	private int menesis;
	private int diena;
	private int valanda;
	private int minute;
	public ManoData(Calendar date){
		this.metai = date.get(Calendar.YEAR);
		this.menesis = date.get(Calendar.MONTH)+1;
		this.diena = date.get(Calendar.DAY_OF_MONTH);
		this.valanda = date.get(Calendar.HOUR);
		this.minute = date.get(Calendar.MINUTE);
	}
	
	public Boolean isBigger(ManoData otherDate){
		if(this.metai == otherDate.metai){
			if(this.menesis == otherDate.menesis){
				if(this.diena == otherDate.diena){
					if(this.valanda == otherDate.valanda){
						if(this.minute == otherDate.minute){
							return true;
						}else if(this.minute > otherDate.minute){
							return true;
						}else return false;
					}else if(this.valanda > otherDate.valanda){
						return true;
					}else return false;
				}else if(this.diena > otherDate.diena){
					return true;
				}else return false;
			}else if(this.menesis > otherDate.menesis){
				return true;
			}else return false;
		}else if(this.metai > otherDate.metai){
			return true;
		}else return false;
		
	}
}
