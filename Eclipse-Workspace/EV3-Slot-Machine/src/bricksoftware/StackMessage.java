package bricksoftware;

public class StackMessage {
	public int length;
	public String message;
	
	public StackMessage() {
		
	}
	
	public StackMessage(int messageLength, String messageText) {
		length = messageLength;
		message = messageText;
	}
}
