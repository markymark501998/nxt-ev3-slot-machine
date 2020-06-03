package bricksoftware;

import java.util.Queue;
import java.util.LinkedList;
import java.io.*;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.*;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import lejos.hardware.sensor.RFIDSensor;
import lejos.hardware.sensor.EV3ColorSensor;

public class CardReader extends Thread {
	private int ERROR_DELAY = 5000;
	private int INTERVAL = 150;
	private boolean verbose = false;
	public boolean printCardCodes = true;
	private int motorSpeed = 100;
	
	public boolean killReader = false;
	private boolean working = false;	

	public boolean hasCardInserted = false;
	public RFIDSensor rfidSensor = null;
	EV3LargeRegulatedMotor cardMotor = null;
	EV3ColorSensor cardColorSensor = null;
	SensorMode colorSM;
	float[] redSample;
	
	int lastTachoCount = 0;
	
	private Thread t;
	
	public CardReader() {
		
	}
	
	public CardReader(boolean verbose) {
		this.verbose = verbose;
	}
	
	public CardReader(boolean verbose, boolean printCardCodes) {
		this.verbose = verbose;
		this.printCardCodes = printCardCodes;
	}
	
	public CardReader(boolean verbose, boolean printCardCodes, int interval) {
		this.verbose = verbose;
		this.printCardCodes = printCardCodes;
		this.INTERVAL = interval;
	}
	
	public boolean StartReaderProcedure() {
		killReader = false;
		
		try {
			cardMotor = new EV3LargeRegulatedMotor(MotorPort.D);
			cardColorSensor = new EV3ColorSensor(SensorPort.S3);
			
			colorSM = cardColorSensor.getRedMode();
			redSample = new float[colorSM.sampleSize()];	
			
			if (verbose) 
				System.out.println("Starting RFID Sensor...");
			
			rfidSensor = new RFIDSensor(SensorPort.S4);
			
			if (verbose) 
				System.out.println("Started RFID Sensor...");
			
			cardMotor.resetTachoCount();
			cardMotor.flt();
			
			if (t == null) {
				t = new Thread (this);
				t.start();
			}			
		} catch (Exception e) {
			if (rfidSensor != null) {
				rfidSensor.close();
			}
			
			System.err.println("StartCR Error: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
			return false;
		}
		
		return true;
	}
	
	public void run() {
		if (verbose)
			System.out.println("Starting Thread...");
		
		try {
			while (!killReader) {
				working = true;
				if (verbose)
					System.out.println("Starting RFID Work...");
				
				if (cardMotor.getTachoCount() == lastTachoCount) {
					
				} else {
					if (verbose)
						System.out.println("Detected Movement!");
										
					cardMotor.setSpeed(motorSpeed);
					cardMotor.forward();
					
					while (true) {
						colorSM.fetchSample(redSample, 0);
						System.out.println("RedSample:" + redSample[0]);
						
						if (redSample[0] > 0.35f) {
							break;
						}
					}
					
					cardMotor.stop();
					cardMotor.flt();					
					cardMotor.resetTachoCount();
					
					if (verbose)
						System.out.println("Reading Card!");					
				}
				
				if (verbose)
					System.out.println("Ending RFID Work...");
				working = false;
				Thread.sleep(INTERVAL);
			}
		} catch (Exception e) {
			System.err.println("CardReader Thread Threw Exception: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
			System.exit(0);
		}
		
		if (verbose)
			System.out.println("Exiting Thread...");
	}
	
	public boolean EjectCard() {
		
		
		return true;
	}
	
	public boolean StopReaderProcedure() {
		try {
			if (working) {
				while (working) {
					
				}
				
				killReader = true;
			} else {
				killReader = true;
			}
			
			rfidSensor.close();
			cardMotor.close();
			cardColorSensor.close();
			
			if (verbose)
				System.out.println("CardReader Shutdown Successful!...");
		} catch (Exception e) {
            System.err.println("StopCR Error: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
			return false;
        } 
		
		return true;
	}
}
