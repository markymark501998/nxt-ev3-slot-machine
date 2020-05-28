package bricksoftware;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.utility.Delay;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;

public class SlotMachine {

	public static void main(String[] args) {
		BTCommunicator btStack = new BTCommunicator(false, 50, true);
		boolean stackStarted = btStack.StartCommunicationsCycle();
		
		btStack.CreateMessageQueue1("brick1");
		btStack.CreateMessageQueue2("brick2");		
		int messageCounter = 0;
		
		EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S2);
		int chan1, chan2, chan3, chan4;
		
		while (true && stackStarted) {
			//int buttonId = Button.waitForAnyEvent();
			int buttonId = Button.readButtons();
			
			if (buttonId == Button.ID_ESCAPE) {
				btStack.StopCommunicationsCycle();
				break;
			}
			
			if (buttonId == Button.ID_RIGHT) {
				messageCounter++;
				System.out.println("S2 -> Queued");
				btStack.CreateMessageQueue2("!A:360,B:360,C:360");
				btStack.CreateMessageQueue1("!A:360,B:360,C:0");
				
				while (Button.RIGHT.isDown()) {
					
				}
			}
			
			if (buttonId == Button.ID_DOWN) {
				messageCounter++;
				System.out.println("S2 -> Queued");
				btStack.CreateMessageQueue2("For SLOTS2: " + messageCounter);
				System.out.println("S1 -> Queued");
				btStack.CreateMessageQueue1("For SLOTS1: " + messageCounter);
				
				while (Button.DOWN.isDown()) {
					
				}
			}
			
			if (buttonId == Button.ID_LEFT) {
				messageCounter++;
				System.out.println("S1 -> Queued");
				btStack.CreateMessageQueue1("!A:360,B:360,C:0");
				
				while (Button.LEFT.isDown()) {
					
				}
			}
			
			if (buttonId == Button.ID_UP) {
				if (btStack.killComms) {
					System.out.println("Starting Comms");
					btStack.StartCommunicationsCycle();
				}
				
				while (Button.UP.isDown()) {
					
				}
			}
			
			chan1 = irSensor.getRemoteCommand(0);
			chan2 = irSensor.getRemoteCommand(1);
			chan3 = irSensor.getRemoteCommand(2);
			chan4 = irSensor.getRemoteCommand(3);
			
			if (chan1 != 0) {
				while (irSensor.getRemoteCommand(0) != 0) {
					
				}
				
				System.out.println("1:" + chan1);
			}
			
			if (chan2 != 0) {
				while (irSensor.getRemoteCommand(1) != 0) {
					
				}
				
				System.out.println("2:" + chan2);
			}
			
			if (chan3 != 0) {
				while (irSensor.getRemoteCommand(2) != 0) {
					
				}
				
				System.out.println("3:" + chan3);
			}
			
			if (chan4 != 0) {
				while (irSensor.getRemoteCommand(3) != 0) {
					
				}
				
				System.out.println("4:" + chan4);
			}
			
			//Delay.msDelay(50);
		}
		
		irSensor.close();
	}
}
