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

	public Gpio () {
		Thread thread = new Thread(){
			public void run(){
				while(true) {

					try {
						String currentTime = format.format(new Date());
						Date now = format.parse(currentTime);
						for (Timer i : timers) {
							long difference = now.getTime() - i.getTime().getTime();
							if(difference >= 0) {
								toggle(i.getString());
								timers.remove(i);
							}
						}

					} catch (ParseException e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(60000);
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
		case "ON unit 1":
			unit1.high();
			break;
		case "ON unit 2":
			unit2.high();
			break;
		case "ON unit 3":
			unit3.high();
			break;
		case "ON unit 4":
			unit4.high();
			break;
		case "OFF unit 1":
			unit1.low();
			break;
		case "OFF unit 2":
			unit2.low();
			break;
		case "OFF unit 3":
			unit3.low();
			break;
		case "OFF unit 4":
			unit4.low();
			break;
		}
	}

	public void setTimer(BufferedReader input) {
		try {
			String time = input.readLine();
			String action = input.readLine();

			Date time2 = format.parse(time);
			Timer timer = new Timer(time2, action);

			timers.add(timer);

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public void removeTimer(BufferedReader input) {

		try {
			String time = input.readLine();
			String action = input.readLine();
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
		String timersString = null;
		String one = unit1.getState().toString();
		String two = unit1.getState().toString();
		String three = unit1.getState().toString();
		String four = unit1.getState().toString();
		
		for (Timer i : timers) {
			timersString = timersString + " " + i.getString() + " " + i.getTime();
		}
		
		return one + " " + two + " " + three + " " + four + " " + timersString;
	}
}