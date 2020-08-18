package bricksoftware;

import java.io.IOException;
import java.io.File;

import lejos.hardware.Button;
import lejos.utility.Delay;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.RCXMotor;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;

import lejos.hardware.Sound;
import lejos.hardware.Sounds;

public class SlotMachine {

	public static void main(String[] args) {
        //Configurables
		//---------------------------------------------------------------------------
		int motorSpeed = 250;  
        
		// 1 - 5		
		int configMotor = 1;
        
        //Local Variables
        //---------------------------------------------------------------------------
		BTCommunicator btStack = new BTCommunicator(false, 50, true);
		boolean stackStarted = btStack.StartCommunicationsCycle();
		
		CardReader cardReader = new CardReader(false, true, 500);
		cardReader.StartReaderProcedure();
		
		btStack.CreateMessageQueue1("brick1");
		btStack.CreateMessageQueue2("brick2");		
		int messageCounter = 0;
		
		EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S2);
		int chan1, chan2, chan3, chan4;
		
		//File coinSoundFile = new File("CoinSound1.wav");
		//int soundReturn = 0;		
		
		RCXMotor playLight1 = new RCXMotor(MotorPort.A);
		RCXMotor playLight2 = new RCXMotor(MotorPort.B);
		EV3LargeRegulatedMotor coinDispenserMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		
		playLight1.setPower(100);
		playLight2.setPower(100);
		
		while (true && stackStarted) {
			int buttonId = Button.readButtons();
			
			if (buttonId == Button.ID_ESCAPE) {
				btStack.StopCommunicationsCycle();
				cardReader.StopReaderProcedure();
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
				/*
				if (btStack.killComms) {
					System.out.println("Starting Comms");
					btStack.StartCommunicationsCycle();
				}
				*/
				
				System.out.println("All -> 45 Deg");
				btStack.CreateMessageQueue2("!A:45,B:45,C:45");
				btStack.CreateMessageQueue1("!A:45,B:45,C:0");
				
				while (Button.UP.isDown()) {
					
				}
			}
			
			if (!btStack.receivingQueue.isEmpty()) {
				OpCodeMessage opcm = btStack.receivingQueue.remove();
				
				if (opcm.opcode == 50) {
					System.out.println("Coin Collected");
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
				
				if (chan1 == 1) {
					DispenseCoinsV2(coinDispenserMotor, 1, motorSpeed);
				}
				
				if (chan1 == 2) {
					DispenseCoinsV2(coinDispenserMotor, 2, motorSpeed);
				}
				
				if (chan1 == 3) {
					DispenseCoinsV2(coinDispenserMotor, 3, motorSpeed);
				}
				
				if (chan1 == 4) {
					DispenseCoinsV2(coinDispenserMotor, 4, motorSpeed);
				}
			}
			
			if (chan2 != 0) {
				while (irSensor.getRemoteCommand(1) != 0) {
					
				}
				
				System.out.println("2:" + chan2);
				
				if (chan2 == 1) {
					btStack.CreateMessageQueue2("!A:45,B:45,C:45");
					btStack.CreateMessageQueue1("!A:45,B:45,C:0");
				}
				
				if (chan2 == 2) {
					btStack.CreateMessageQueue2("!A:90,B:90,C:90");
					btStack.CreateMessageQueue1("!A:90,B:90,C:0");
				}
				
				if (chan2 == 3) {
					btStack.CreateMessageQueue2("!A:45,B:45,C:45");
					btStack.CreateMessageQueue1("!A:90,B:90,C:0");
				}
				
				if (chan2 == 4) {
					btStack.CreateMessageQueue2("!A:360,B:360,C:360");
					btStack.CreateMessageQueue1("!A:360,B:360,C:0");
				}
			}
			
			if (chan3 != 0) {
				while (irSensor.getRemoteCommand(2) != 0) {
					
				}
				
				System.out.println("3:" + chan3);
				
				if (chan3 == 1) {
					ConfigMotor(1, 0, configMotor, btStack);
				}
				
				if (chan3 == 2) {
					ConfigMotor(-1, 0, configMotor, btStack);
				}
				
				if (chan3 == 3) {
					configMotor = ConfigMotor(0, 1, configMotor, btStack);
					System.out.println("Cycle: " + configMotor);
				}
				
				if (chan3 == 4) {
					
				}
			}
			
			if (chan4 != 0) {
				while (irSensor.getRemoteCommand(3) != 0) {
					
				}
				
				if (chan4 == 4) {
					btStack.StopCommunicationsCycle();
					cardReader.StopReaderProcedure();
					break;
				}
				
				System.out.println("4:" + chan4);
			}
			
			//Delay.msDelay(50);
		}
		
		irSensor.close();
		coinDispenserMotor.close();
		playLight1.close();
		playLight2.close();
	}
	
	public static void DispenseCoins(EV3LargeRegulatedMotor dispenser, int coins, int motorSpeed) {
		dispenser.resetTachoCount();
		dispenser.setSpeed(motorSpeed);
		dispenser.rotate(360 * coins);
		dispenser.waitComplete();
		dispenser.flt();
	}
	
	public static void DispenseCoinsV2(EV3LargeRegulatedMotor dispenser, int coins, int motorSpeed) {
		for (int i = 0; i < coins; i++) {
			dispenser.resetTachoCount();
			dispenser.setSpeed(motorSpeed);
			dispenser.rotate(112);
			dispenser.waitComplete();
			dispenser.resetTachoCount();
			dispenser.rotate(-112);
			dispenser.flt();
			Delay.msDelay(25);
		}
	}
	
	public static int ConfigMotor (int direction, int cycle, int configMotor, BTCommunicator tempStack) {
		if (cycle > 0 && configMotor == 5) {
			configMotor = 1;
		} else {
			configMotor++;
		}
		
		if (direction != 0) {
			switch (configMotor) {
				case 1:
					if (direction > 0) {
						tempStack.CreateMessageQueue2("!A:45,B:0,C:0");
					} else {
						tempStack.CreateMessageQueue2("!A:-45,B:0,C:0");
					}					
					break;
					
				case 2: 
					if (direction > 0) {
						tempStack.CreateMessageQueue2("!A:0,B:1,C:0");
					} else {
						tempStack.CreateMessageQueue2("!A:0,B:-1,C:0");
					}			
					break;
									
				case 3: 
					if (direction > 0) {
						tempStack.CreateMessageQueue2("!A:0,B:0,C:1");
					} else {
						tempStack.CreateMessageQueue2("!A:0,B:0,C:-1");
					}
					break;
					
				case 4: 
					if (direction > 0) {
						tempStack.CreateMessageQueue1("!A:1,B:0,C:0");
					} else {
						tempStack.CreateMessageQueue1("!A:-1,B:0,C:0");
					}
					break;
					
				case 5: 
					if (direction > 0) {
						tempStack.CreateMessageQueue1("!A:0,B:1,C:0");
					} else {
						tempStack.CreateMessageQueue1("!A:0,B:-1,C:0");
					}
					break;
			}
		}
		
		return configMotor;
	}
}
