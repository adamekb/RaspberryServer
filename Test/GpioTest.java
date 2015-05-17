import static org.junit.Assert.*;

import org.junit.Test;


public class GpioTest {

	@Test
	public void testGetCurrentState() {
		
		Gpio pins = new Gpio();
		String result = pins.getCurrentState();

        assertEquals("LOW LOW LOW LOW null", result);
	}
	
	@Test
	public void testToggle() {
		
		Gpio pins = new Gpio();
		pins.toggle("unit1 ON");

        assertEquals("HIGH", pins.unit1.getState().toString());
	}
	
}
