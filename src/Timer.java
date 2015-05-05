import java.util.Date;


public class Timer {
	
	private String string, time;
	
	public Timer (String time, String string) {
		this.time = time;
		this.string = string;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getString() {
		return string;
	}
}
