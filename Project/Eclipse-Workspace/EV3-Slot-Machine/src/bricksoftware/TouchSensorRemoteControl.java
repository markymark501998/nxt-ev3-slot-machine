package bricksoftware;

import lejos.hardware.Button;
import lejos.remote.nxt.*;

import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.SensorMode;

import lejos.hardware.lcd.LCD;

import lejos.hardware.port.*;
import lejos.hardware.Bluetooth;
import lejos.utility.Delay;

public class TouchSensorRemoteControl {
	private static int ERROR_DELAY = 15000;
	
	public static void main(String[] args) {
		try {
			TouchSensorThreadLoop tl = new TouchSensorThreadLoop(true);
			tl.StartTouchSensorLoop();
		} catch (Exception e) {
			System.err.println("TouchSRC/main Error: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
			return;
		}
	}
	
}

class TouchSensorThreadLoop extends Thread {
	final String NXT1 = "SLOTS1";
	final String NXT2 = "SLOTS2";
	
	private boolean verbose = false;
	private int INTERVAL = 50;
	private static int ERROR_DELAY = 15000;
	
	RemoteNXT slots1 = null;
	//RemoteNXT slots2 = null;
	
	int maxAttempts = 4;
	int attemptNum = 1;
	
	public TouchSensorThreadLoop() {
		
	}
	
	public TouchSensorThreadLoop(boolean verbose) {
		this.verbose = verbose;
	}
	
	public void StartTouchSensorLoop () {
		try {
			NXTCommConnector connector1 = Bluetooth.getNXTCommConnector();
			NXTCommConnector connector2 = Bluetooth.getNXTCommConnector();
			
			Port slots1_1 = null;
			Port slots1_2 = null;
			Port slots1_3 = null;
			
			Port slots2_1 = null;
			Port slots2_2 = null;
			Port slots2_3 = null;
			
			while (slots1 == null && attemptNum < maxAttempts) {
				if (verbose)
					System.out.println("Attempt " + attemptNum + ": " + NXT1);
				
				slots1 = new RemoteNXT(NXT1, connector1);
				
				if (slots1 == null) {
					if (verbose)
						System.out.println("FAILED...Retrying");
				} else {
					System.out.println("CONNECTED");
					attemptNum = 1;
					break;
				}
				
				attemptNum++;
			}
			/*
			while (slots2 == null && attemptNum < maxAttempts) {
				if (verbose)
					System.out.println("Attempt " + attemptNum + ": " + NXT2);
				
				slots2 = new RemoteNXT(NXT2, connector2);
				
				if (slots2 == null) {
					if (verbose)
						System.out.println("FAILED...Retrying");
				} else {
					System.out.println("CONNECTED");
					attemptNum = 1;
					break;
				}
				
				attemptNum++;
			}
			*/
			
			/*
			//slots1_1 = slots1.getPort("S1");
			slots1_2 = slots1.getPort("S2");
			slots1_3 = slots1.getPort("S3");
			
			slots2_1 = slots2.getPort("S1");
			slots2_2 = slots2.getPort("S2");
			slots2_3 = slots2.getPort("S3");
			
			//NXTTouchSensor touchSensorPort1_1 = new NXTTouchSensor(slots1_1);
			NXTTouchSensor touchSensorPort1_2 = new NXTTouchSensor(slots1_2);			
			NXTTouchSensor touchSensorPort2_1 = new NXTTouchSensor(slots2_1);
			*/
			
			//NXTCommand slots1Command = slots1.getNXTCommand();
			//NXTCommand slots2Command = slots2.getNXTCommand();
			
			//Port port1_2 = new RemoteNXTPort("S1", RemoteNXTPort.SENSOR_PORT, 0, slots1Command);
			Port port1_2 = slots1.getPort("S2");	
			NXTTouchSensor touchSensorPort1_2 = new NXTTouchSensor(port1_2);
			//System.out.println(port1_2.getName()); // => S2
			
			//Port port1_3 = new RemoteNXTPort("S3", RemoteNXTPort.SENSOR_PORT, 2, slots1Command);
			Port port1_3 = slots1.getPort("S3");
			NXTLightSensor lightSensorPort1_3 = new NXTLightSensor(port1_3);
			
			
			//lightSensorPort1_3.
			
			
			SensorMode touch1_2 = touchSensorPort1_2.getTouchMode();
			SensorMode light1_3 = lightSensorPort1_3.getRedMode();
			//SensorMode touch2_1 = touchSensorPort2_1.getTouchMode();					
			
			float[] sample1_2 = new float[touch1_2.sampleSize()];	
			float[] sample1_3 = new float[light1_3.sampleSize()];	
			//float[] sample2_1 = new float[touch2_1.sampleSize()];	
									
			while (true) {
				if (Button.readButtons() == Button.ID_ESCAPE) {
					System.out.println("Exiting");
					Delay.msDelay(5000);
					break;
				}
				
				//lightSensorPort1_3.
				
				touch1_2.fetchSample(sample1_2, 0);
				lightSensorPort1_3.fetchSample(sample1_3, 0);
				//touch2_1.fetchSample(sample2_1, 0);
				
				//String oneTwo = new Float(sample1_2[0]).toString();
				//String twoOne = new Float(sample2_1[0]).toString();
				
				//LCD.clear();
				
				//LCD.drawString("1_2: " + oneTwo, 0, 2);
				//LCD.drawString("2_1: " + twoOne, 0, 3);
				
				System.out.println(sample1_3[0]);
				System.out.println(sample1_2[0]);
				
				//System.out.println(sample1_2.toString());
				//System.out.println("1_2: " + oneTwo);
				//System.out.println("2_1: " + twoOne);
				
				//System.out.println("1:" + slots1.getPower().getVoltage());
				//System.out.println("2:" + slots2.getPower().getVoltage());
				
				//LCD.refresh();
				
				//Delay.msDelay(500);
			}
		} catch (Exception e) {
			System.err.println("StartTouchSensorLoop Error: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
			ExitRoutine();
			return;
		}
	}
	
	public void ExitRoutine() {
		
	}
}
