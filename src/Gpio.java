import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;


public class Gpio {
	private ArrayList<Timer> timers = new ArrayList<Timer>();
	private GpioController gpio = GpioFactory.getInstance();
	private GpioPinDigitalOutput unit1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
	private GpioPinDigitalOutput unit2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
	private GpioPinDigitalOutput unit3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
	private GpioPinDigitalOutput unit4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	private String timersString;

	public Gpio () {
		Thread thread = new Thread(){
			public void run(){
				while(true) {

					try {
						String currentTime = format.format(new Date());
						Date now = format.parse(currentTime);
						for (int i = 0; i < timers.size(); i++) {
							Date time = format.parse(timers.get(i).getTime());
							long difference = now.getTime() - time.getTime();
							if(difference >= 0) {
								toggle(timers.get(i).getString());
								timers.remove(i);
								i--;
							}
						}

					} catch (ParseException e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	public void toggle (String signal) {
		switch (signal) {
		case "unit1 ON":
			unit1.high();
			break;
		case "unit2 ON":
			unit2.high();
			break;
		case "unit3 ON":
			unit3.high();
			break;
		case "unit4 ON":
			unit4.high();
			break;
		case "unit1 OFF":
			unit1.low();
			break;
		case "unit2 OFF":
			unit2.low();
			break;
		case "unit3 OFF":
			unit3.low();
			break;
		case "unit4 OFF":
			unit4.low();
			break;
		}
	}

	public void setTimer(BufferedReader input) {
		try {
			String action = input.readLine();
			String time = input.readLine();
			System.out.println(action);
			System.out.println(time);

			Timer timer = new Timer(time, action);

			timers.add(timer);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeTimer(BufferedReader input) {

		try {
			String action = input.readLine();
			String time = input.readLine();
			Date time2 = format.parse(time);

			for (Timer i : timers) {
				if(i.getString().equals(action) || i.getTime().equals(time2)) {
					timers.remove(i);
					break;
				}
			}

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentState() {
		String one = unit1.getState().toString();
		String two = unit2.getState().toString();
		String three = unit3.getState().toString();
		String four = unit4.getState().toString();
		
		for (Timer i : timers) {
			timersString = timersString + " " + i.getString() + " " + i.getTime();
		}
		
		return one + " " + two + " " + three + " " + four + " " + timersString;
	}
}