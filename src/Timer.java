import java.util.Date;


public class Timer {
	private Date time;
	private String string;
	
	public Timer (Date time, String string) {
		this.time = time;
		this.string = string;
	}
	
	public Date getTime() {
		return time;
	}
	
	public String getString() {
		return string;
	}
}
