package bricksoftware;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.utility.Delay;

public class SlotMachine {

	public static void main(String[] args) {
		BTCommunicator btStack = new BTCommunicator(false, 75, true);
		boolean stackStarted = btStack.StartCommunicationsCycle();
		
		btStack.CreateMessageQueue1("brick1");
		btStack.CreateMessageQueue2("brick2");
		
		int messageCounter = 0;
		
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
				btStack.CreateMessageQueue1("!A:360,B:0,C:0");
				
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
			
			//Delay.msDelay(50);
		}
	}
}
