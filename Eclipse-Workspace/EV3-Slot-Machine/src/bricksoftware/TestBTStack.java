package bricksoftware;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.utility.Delay;

public class TestBTStack {
	public static void main(String[] args) {
		BTCommunicator btStack = new BTCommunicator(true, 4000);
		boolean stackStarted = btStack.StartCommunicationsCycle();
		
		int messageCounter = 0;
		
		while (true && stackStarted) {
			int buttonId = Button.waitForAnyEvent();
			
			if (buttonId == Button.ID_ESCAPE) {
				btStack.StopCommunicationsCycle();
				break;
			}
			
			if (buttonId == Button.ID_RIGHT) {
				messageCounter++;
				System.out.println("S2 -> Queued");
				btStack.CreateMessageQueue2("For SLOTS2: " + messageCounter);
			}
			
			if (buttonId == Button.ID_DOWN) {
				messageCounter++;
				System.out.println("S2 -> Queued");
				btStack.CreateMessageQueue2("For SLOTS2: " + messageCounter);
				System.out.println("S1 -> Queued");
				btStack.CreateMessageQueue1("For SLOTS1: " + messageCounter);
			}
			
			if (buttonId == Button.ID_LEFT) {
				messageCounter++;
				System.out.println("S1 -> Queued");
				btStack.CreateMessageQueue1("For SLOTS1: " + messageCounter);
			}
			
			if (buttonId == Button.ID_UP) {
				if (btStack.killComms) {
					System.out.println("Starting Comms");
					btStack.StartCommunicationsCycle();
				}
			}
			
			//Delay.msDelay(50);
		}
	}
}
