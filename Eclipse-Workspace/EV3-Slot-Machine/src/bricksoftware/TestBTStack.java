package bricksoftware;

import lejos.hardware.Button;

public class TestBTStack {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BTCommunicator btStack = new BTCommunicator(true, 2000);
		btStack.StartCommunicationsCycle();
		
		int messageCounter = 0;
		
		while (true) {
			int buttonId = Button.waitForAnyEvent();
			
			if (buttonId == Button.ID_ESCAPE) {
				break;
			}
			
			if (buttonId == Button.ID_RIGHT) {
				messageCounter++;
				btStack.CreateMessageQueue2("For SLOTS2: " + messageCounter);
			}
			
			if (buttonId == Button.ID_LEFT) {
				messageCounter++;
				btStack.CreateMessageQueue1("For SLOTS2: " + messageCounter);
			}
		}
	}
}
