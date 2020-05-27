import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;

import lejos.nxt.TouchSensor;

import lejos.nxt.Button;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.NXTConnection;
import lejos.util.Delay;

public class SlotMachine {
    public static void main(String[] args) throws Exception
    {
        BTCommunicatorReceiver btStack = new BTCommunicatorReceiver(false, 50, true);
        boolean receiverStarted = btStack.StartCommunicationsCycle();

        while(true && receiverStarted) {
            if (Button.readButtons() == Button.ID_RIGHT) {
				if (btStack.killComms) {
                    System.out.println("Restarting Cycle");
					btStack.StartCommunicationsCycle();
                }

                while (Button.RIGHT.isDown()) {

                }
            }

            if (Button.readButtons() == Button.ID_LEFT) {
                System.out.println("Queuing Message...");
                btStack.CreateMessageQueue(1);

                while (Button.LEFT.isDown()) {

                }
            }
            
            if (Button.readButtons() == Button.ID_ESCAPE) {
                System.out.println("Exiting");
                btStack.StopCommunicationsCycle();
                
                while (Button.ESCAPE.isDown()) {

                }

                break;
            }
        }
    }
}

class BTCommunicatorReceiver extends Thread {
    private boolean verbose = false;
	private int INTERVAL = 50;
    private int ERROR_DELAY = 15000;
    private int TIMEOUT_TIME = 120000;
	private boolean printResponseCodes = false;
    
    DataInputStream input;
    DataOutputStream output;

    NXTConnection connection;
    public Queue<OpCodeMessage> messageQueue = new Queue<OpCodeMessage>();
    public Queue<StackMessage> receivingQueue = new Queue<StackMessage>();
    
    private boolean communicating = false;
	public boolean killComms = false;
	
    private Thread t;
    
    public BTCommunicatorReceiver(boolean verbose, int interval, boolean printResponseCodes) {
		this.verbose = verbose;
		this.INTERVAL = interval;
		this.printResponseCodes = printResponseCodes;
	}
    
    public BTCommunicatorReceiver(boolean verbose, int interval) {
		this.verbose = verbose;
		this.INTERVAL = interval;
	}
	
	public BTCommunicatorReceiver(boolean verbose) {
		this.verbose = verbose;
    }
    
    public boolean StartCommunicationsCycle() {
        killComms = false;
        
        NXTCommConnector connector = Bluetooth.getConnector();

        if (verbose) 
            System.out.println("Waiting for Connection...");

        connection = connector.waitForConnection(TIMEOUT_TIME, NXTConnection.RAW);

        if (connection != null) {
            System.out.println("Connected!");
        } else {
            System.out.println("Connection Timed Out...");
            return false;
        }

        if(verbose) 
			System.out.println("Setting up Streams");

        input = connection.openDataInputStream();
        output = connection.openDataOutputStream();

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
                                    
                while(true) {
                    try{
                        if(input.available() < 1) {
                            break;
                        }
                        
                        int length = input.readInt();
                        String message = "";
    
                        for (int i = 0; i < length; i++) {
                            char newChar = input.readChar();
                            message = message + newChar;
                        }

                        int responseCode = 0;
                        if(!messageQueue.isEmpty()) {
                            OpCodeMessage om = (OpCodeMessage)messageQueue.pop();
                            responseCode = om.opcode;
                        }
                        
                        if (printResponseCodes && responseCode != 0) 
                            System.out.println("Response Code: " + responseCode);

                        output.writeInt(responseCode);
                        output.flush();
                        
                        if(!message.equals("NoCommand") && printResponseCodes)
                            System.out.println("M[" + length + "]: " + message);

                        receivingQueue.push(new StackMessage(length, message));
                    } catch (EOFException e) {
                        
                    }
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

    public void StopCommunicationsCycle() {
        try {
            if (communicating) {
                while (communicating) {
                    
                }
                
                killComms = true;
            } else {
                killComms = true;
            }
            
            input.close();
            output.close();
    
            connection.close();
            System.out.println("Successfully Stopped Cycle.");
        } catch (Exception e) {
            System.err.println("StopCC Error: " + e.getMessage());
			Delay.msDelay(ERROR_DELAY);
			return;
        }        
    }

    public void CreateMessageQueue(int opcode) {
		messageQueue.push(new OpCodeMessage(opcode));
	}
}

class StackMessage {
	public int length;
	public String message;
	
	public StackMessage() {
		
	}
	
	public StackMessage(int messageLength, String messageText) {
		length = messageLength;
		message = messageText;
	}
}

class OpCodeMessage {
	public int opcode;
	
	public OpCodeMessage() {
		
	}
	
	public OpCodeMessage(int opcode) {
		this.opcode = opcode;
	}
}