package bricksoftware;

import lejos.hardware.Key;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.EV3ColorSensor;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.utility.Delay;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.port.*;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.RFIDSensor;

import java.util.Arrays;

public class ColorTest {
	final static int INTERVAL = 25; // milliseconds
	
	public static void main(String[] args) {		
		Brick brick = BrickFinder.getDefault();
		
		Port s1 = LocalEV3.get().getPort("S1");
		Port s2 = LocalEV3.get().getPort("S2");
		Port s3 = LocalEV3.get().getPort("S3");
		
		//NXTColorSensor cmps = new NXTColorSensor(s1);
		//NXTTouchSensor touchSensor = new NXTTouchSensor(s1);
		NXTLightSensor lightSensor = new NXTLightSensor(s2);
		RFIDSensor rfid = new RFIDSensor(s3);
		//rfid.startFirmware();
		//rfid.wakeUp();
		//rfid.getStatus();
		
		//byte[] byteArray = rfid.readTransponder(false);
		
		
		//rfid.startSingleRead();
		//Delay.msDelay(5000);
		
		//rfid.stop();	
		
		//System.out.println(Arrays.toString(byteArray));
		
		System.out.println("Finished Sensor Work");
		
		//EV3ColorSensor cmps = new EV3ColorSensor(s2);
		
		String color = "Color";
		
		String[] colorNames = {"Red", "Green", "Blue", "Yellow", "Magenta", "Orange",
				             "White", "Black", "Pink", "Gray", "Light gray", "Dark Gray", "Cyan"			
		};
		
		Key escape = brick.getKey("Escape");

		//System.out.println("Test Debug2");
		//Delay.msDelay(INTERVAL);
		//GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		
		Integer counter = 0;
		while(escape.isUp()) {
			//counter++;
			//System.out.println("Interation: " + counter.toString());
			
			//LCD.clear();
			
			//SensorMode touch = touchSensor.getTouchMode();
			//float[] sample = new float[touch.sampleSize()];			
			//touch.fetchSample(sample, 0);
			
			SensorMode light = lightSensor.getRedMode();
			float[] sample2 = new float[light.sampleSize()];			
			light.fetchSample(sample2, 0);
			
			//Float temp = sample[0];
			Float temp2 = sample2[0];
			//String tempStr = temp.toString();
			String tempStr2 = temp2.toString();
			//LCD.drawString(tempStr, 0, 3);
			//LCD.drawString(tempStr2, 0, 4);
			
			if (temp2 > .33f) { 
				System.out.println("Val: " + tempStr2);
			}
			
			Delay.msDelay(75);
			
			
			//Integer colId = cmps.getColorID();
			//String colString = colId.toString();
			
			//LCD.drawString(color, 0, 3);
			//LCD.drawString(colString, 7, 3);
			//g.drawString(colorNames[cmps.getColorID()], 0, 4, 0);
			//LCD.refresh();
			
			Delay.msDelay(INTERVAL);
		}
		
		//cmps.close();
		//touchSensor.close();
		rfid.close();
		
		
		/*
		//System.out.println("Running...");
        GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
        final int SW = g.getWidth();
        final int SH = g.getHeight();
        Button.LEDPattern(4);
        Sound.beepSequenceUp();
        
        g.setFont(Font.getLargeFont());
        g.drawString("leJOS/EV3", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        Button.LEDPattern(3);
        Delay.msDelay(4000);
        Button.LEDPattern(5);
        g.clear();
        g.refresh();
        Sound.beepSequence();
        Delay.msDelay(500);
        Button.LEDPattern(0);
        */
	}
}
