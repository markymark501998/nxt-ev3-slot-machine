package bricksoftware;

import lejos.remote.nxt.*;
import lejos.hardware.Bluetooth;
import lejos.utility.Delay;

import java.util.Stack;
import java.io.*;

public class BTCommunicator extends Thread {
	private boolean verbose = false;
	private int INTERVAL = 100;
	
	public Stack<StackMessage> messageQueue1 = new Stack<StackMessage>();
	public Stack<StackMessage> messageQueue2 = new Stack<StackMessage>();
	
	DataInputStream input1;
	DataOutputStream output1;
	
	DataInputStream input2;
	DataOutputStream output2;
	
	NXTConnection connection1;
	NXTConnection connection2;
	
	private boolean communicating = false;
	private boolean killComms = false;
	
	private Thread t;
	
	public BTCommunicator(boolean verbose, int interval) {
		this.verbose = verbose;
		this.INTERVAL = interval;
	}
	
	public BTCommunicator(boolean verbose) {
		this.verbose = verbose;
	}
	
	public boolean StartCommunicationsCycle() {
		final String NXT1 = "SLOTS1";
		final String NXT2 = "SLOTS2";
		
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		
		if(verbose)
			System.out.println("Connecting to " + NXT1);
		
		connection1 = connector.connect(NXT1, NXTConnection.PACKET);
		
		if(verbose)
			System.out.println("Connected to " + NXT1);
		
		if (connection1 == null) {
		    System.err.println("Failed to connect to " + NXT1);
		    return false;
		}
		
		if(verbose)
			System.out.println("Connecting to " + NXT2);
		
		connection2 = connector.connect(NXT2, NXTConnection.PACKET);
		
		if(verbose)
			System.out.println("Connected to " + NXT2);
		
		if (connection2 == null) {
		    System.err.println("Failed to connect to " + NXT2);
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
				
				if (!messageQueue1.empty()) {
					sm = messageQueue1.pop();
				}
				
				if (!messageQueue2.empty()) {
					sm = messageQueue2.pop();
				}
				
				communicating = false;
				if (verbose)
					System.out.println("Ending Comms...");
				Thread.sleep(INTERVAL);			
			}
		} catch (Exception e) {
			System.err.println("BTCommunicator Thread Threw Exception: " + e.getMessage());
			System.exit(0);
		}
		
		if (verbose)
			System.out.println("Exiting Thread...");
	}
	
	public void StopCommunicationsCycle() throws IOException {
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
	}
	
	public void CreateMessageQueue1(String message) {
		StackMessage sm = new StackMessage(message.length(), message);
		messageQueue1.push(sm);
	}
	
	public void CreateMessageQueue2(String message) {
		StackMessage sm = new StackMessage(message.length(), message);
		messageQueue2.push(sm);
	}
}
