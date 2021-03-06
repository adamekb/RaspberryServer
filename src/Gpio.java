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
	public GpioPinDigitalOutput unit1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
	public GpioPinDigitalOutput unit2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
	public GpioPinDigitalOutput unit3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
	public GpioPinDigitalOutput unit4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm");

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
			String string = input.readLine();
			String array[] = string.split(" ");
			String action = array[0] + " " + array[1];
			System.out.println("action: " + action);
			String time = array[2];
			System.out.println("time: " + time);

			for (Timer i : timers) {
				if(i.getString().equals(action) && i.getTime().equals(time)) {
					timers.remove(i);
					System.out.println("EN TIMER HAR TAGITS BORT!!");
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentState() {
		String timersString = null;
		
		String one = unit1.getState().toString();
		String two = unit2.getState().toString();
		String three = unit3.getState().toString();
		String four = unit4.getState().toString();
		
		for (Timer i : timers) {
			if (timersString == null) {
				timersString = i.getString() + " " + i.getTime();
			} else {
				timersString = timersString + " " + i.getString() + " " + i.getTime();
			}
		}
		
		return one + " " + two + " " + three + " " + four + " " + timersString;
	}
}