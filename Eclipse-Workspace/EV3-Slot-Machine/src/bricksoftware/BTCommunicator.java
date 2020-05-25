package bricksoftware;

import lejos.remote.nxt.*;
import lejos.hardware.Bluetooth;
import lejos.utility.Delay;

import java.util.Queue;
import java.util.LinkedList;
import java.io.*;

public class BTCommunicator extends Thread {
	private boolean verbose = false;
	private int INTERVAL = 50;
	private int ERROR_DELAY = 15000;
	
	public Queue<StackMessage> messageQueue1 = new LinkedList<StackMessage>();
	public Queue<StackMessage> messageQueue2 = new LinkedList<StackMessage>();
	
	DataInputStream input1;
	DataOutputStream output1;
	
	DataInputStream input2;
	DataOutputStream output2;
	
	NXTConnection connection1;
	NXTConnection connection2;
	
	private boolean communicating = false;
	public boolean killComms = false;
	
	private Thread t;
	
	
	
	public BTCommunicator(boolean verbose, int interval) {
		this.verbose = verbose;
		this.INTERVAL = interval;
	}
	
	public BTCommunicator(boolean verbose) {
		this.verbose = verbose;
	}
	
	public boolean StartCommunicationsCycle() {
		killComms = false;
		final String NXT1 = "SLOTS1";
		final String NXT2 = "SLOTS2";
		int maxAttempts = 4;
		int attemptNum = 1;
		
		try {
			NXTCommConnector connector1 = Bluetooth.getNXTCommConnector();
			NXTCommConnector connector2 = Bluetooth.getNXTCommConnector();
			
			if(verbose)
				System.out.println("Attempt 1: " + NXT1);
			
			connection1 = connector1.connect(NXT1, NXTConnection.PACKET);
			
			attemptNum = 0;
			while (connection1 == null && attemptNum < maxAttempts) {
				connector1 = Bluetooth.getNXTCommConnector();
				connection1 = connector1.connect(NXT1, NXTConnection.PACKET);
				
				if(verbose)
					System.out.println("Attempt " + (attemptNum + 1) + ": " + NXT1);
				
				if (connection1 != null) {
					attemptNum = 0;
					break;
				} 
				
				attemptNum++;
			}
			
			if(verbose && connection1 != null)
				System.out.println("Connected to " + NXT1);
			
			if (connection1 == null) {
			    System.err.println("Failed to connect to " + NXT1);
			    Delay.msDelay(ERROR_DELAY);
			    return false;
			}
			
			if(verbose)
				System.out.println("Attempt 1: " + NXT2);
			
			attemptNum = 0;
			while (connection2 == null && attemptNum < maxAttempts) {
				connector2 = Bluetooth.getNXTCommConnector();
				connection2 = connector2.connect(NXT2, NXTConnection.PACKET);
				
				if(verbose)
					System.out.println("Attempt " + (attemptNum + 1) + ": " + NXT2);
				
				if (connection2 != null) {
					attemptNum = 0;
					break;
				} 
				
				attemptNum++;
			}
			
			if(verbose && connection2 != null)
				System.out.println("Connected to " + NXT1);
			
			if (connection2 == null) {
			    System.err.println("Failed to connect to " + NXT2);
			    Delay.msDelay(ERROR_DELAY);
			    return false;
			}
			
			if(verbose) 
				System.out.println("Setting up Streams");
			
			input1 = connection1.openDataInputStream();
			output1 = connection1.openDataOutputStream();
			
			input2 = connection2.openDataInputStream();
			output2 = connection2.openDataOutputStream();
			
			if (t == null) {
				t = new Thread (this);
				t.start();
			}
		} catch (Exception e) {
			System.err.println("StartCC Error: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
			return false;
		}
		
		return true;
	}
	
	public void run() {
		if (verbose)
			System.out.println("Starting Thread...");
		
		try {
			while (!killComms) {
				//Communication Cycle Loop
				communicating = true;
				if (verbose)
					System.out.println("Starting Comms...");
				
				StackMessage sm = new StackMessage();
				
				while (!messageQueue1.isEmpty()) {
					sm = messageQueue1.remove();
					
					output1.writeInt(sm.length);
					
					for (int i = 0; i < sm.length; i++) {
						int charRep = (int)sm.message.charAt(i);
						
						output1.writeChar(charRep);
						output1.flush();						
					}
				}
				
				while (!messageQueue2.isEmpty()) {
					sm = messageQueue2.remove();
					
					output2.writeInt(sm.length);
					
					for (int i = 0; i < sm.length; i++) {
						int charRep = (int)sm.message.charAt(i);
						
						output2.writeChar(charRep);
						output2.flush();						
					}
				}
				
				communicating = false;
				if (verbose)
					System.out.println("Ending Comms...");
				Thread.sleep(INTERVAL);			
			}
		} catch (Exception e) {
			System.err.println("BTCommunicator Thread Threw Exception: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
			System.exit(0);
		}
		
		if (verbose)
			System.out.println("Exiting Thread...");
	}
	
	public void StopCommunicationsCycle() throws IOException {
		try {
			if (communicating) {
				while (communicating) {
					
				}
				
				killComms = true;
			} else {
				killComms = true;
			}
			
			//Send kill messages to other bricks?
			
			input1.close();
			output1.close();
			
			input2.close();
			output2.close();
			
			connection1.close();
			connection2.close();
			
			if (verbose)
				System.out.println("BTComm Class Shutdown Successful!...");
		} catch (Exception e) {
            System.err.println("StopCC Error: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
        }  
	}
	
	public void CreateMessageQueue1(String message) {
		StackMessage sm = new StackMessage(message.length(), message);
		messageQueue1.add(sm);
	}
	
	public void CreateMessageQueue2(String message) {
		StackMessage sm = new StackMessage(message.length(), message);
		messageQueue2.add(sm);
	}
}
