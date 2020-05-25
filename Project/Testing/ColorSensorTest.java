import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;
import lejos.nxt.ColorSensor;

public class ColorSensorTest {
    final static int INTERVAL = 200; // milliseconds
	
	public static void main(String [] args) throws Exception {
		ColorSensor cmps = new ColorSensor(SensorPort.S1);
		String color = "Color";
		String r = "R";
		String g = "G";
		String b = "B";
		
		String[] colorNames = {"Red", "Green", "Blue", "Yellow", "Magenta", "Orange",
				             "White", "Black", "Pink", "Gray", "Light gray", "Dark Gray", "Cyan"			
		};
		
		while(!Button.ESCAPE.isDown()) {
            LCD.clear();
            
			LCD.drawString(color, 0, 3);
			LCD.drawInt(cmps.getColorID(),7,3);
            LCD.drawString(colorNames[cmps.getColorID()], 0, 4);
            
            /*
            LCD.drawString(r, 0, 5);            
			LCD.drawInt(cmps.getRGBComponent(Color.RED),1,5);
			LCD.drawString(g, 5, 5);
			LCD.drawInt(cmps.getRGBComponent(Color.GREEN),6,5);
			LCD.drawString(b, 10, 5);
            LCD.drawInt(cmps.getRGBComponent(Color.BLUE),11,5);
            */
            
			LCD.refresh();
			Thread.sleep(INTERVAL);
		}
	}
}