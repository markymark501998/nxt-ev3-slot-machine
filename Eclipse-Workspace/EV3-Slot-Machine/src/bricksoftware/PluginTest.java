package bricksoftware;

import lejos.hardware.lcd.LCD;
import lejos.utility.*;

public class PluginTest {

	public static void main(String[] args) {
		LCD.drawString("Plugin Test", 0, 4);
		Delay.msDelay(5000);
	}
}
