import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;
import java.lang.String;
import java.io.File;

import lejos.nxt.TouchSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.NXTConnection;
import lejos.util.Delay;

public class SlotMachine {
    private static int ERROR_DELAY = 15000;

    public static void main(String[] args) throws Exception
    {
        //================================================================================================================
        //                                                  Variables
        //================================================================================================================        
        
        //Configurables
        //--------------------------------------------------
        int coinSensorThreshold = 30;
        int coinDelay = 400;

        int holdButtonDelay = 225;
        boolean printMotorCommands = false;  
        int motorSpeed = 100;   

        //Local Variables
        //--------------------------------------------------
        StackMessage sm;
        int buttonId;        

        String brickMode = "na"; 
        String motorCommand = "";
        String[] commands;

        int aDegrees = 0;
        int bDegrees = 0;
        int cDegrees = 0;

        NXTRegulatedMotor MotorA = null;
        NXTRegulatedMotor MotorB = null;
        NXTRegulatedMotor MotorC = null;

        TouchSensor touchSensor1 = null;
        TouchSensor touchSensor2 = null;
        TouchSensor touchSensor3 = null;

        LightSensor lightSensor = null;
        int lightVal = 0;

        File coinSoundFile = new File("CoinSound1.wav");
        int soundReturn = 0;

        //================================================================================================================
        //                                                 Start Main Thread
        //================================================================================================================
        BTCommunicatorReceiver btStack = new BTCommunicatorReceiver(false, 50, false);
        boolean receiverStarted = btStack.StartCommunicationsCycle();

        if (!receiverStarted) {
            System.out.println("FAILED TO START BTSTACK");
            Delay.msDelay(ERROR_DELAY);
        }

        while(true && receiverStarted) {
            buttonId = Button.readButtons();

            //System.out.println("Mem:" + System.getRuntime().freeMemory());
            
            if (buttonId == Button.ID_RIGHT) {
				

                while (Button.RIGHT.isDown()) {

                }
            }

            if (buttonId == Button.ID_LEFT) {
                System.out.println("Queuing Message...");
                btStack.CreateMessageQueue(0);

                while (Button.LEFT.isDown()) {

                }
            }
            
            if (buttonId == Button.ID_ESCAPE) {
                System.out.println("Exiting");
                btStack.StopCommunicationsCycle();

                while (Button.ESCAPE.isDown()) {

                }

                break;
            }

            if (!btStack.receivingQueue.isEmpty()) {
                sm = (StackMessage)btStack.receivingQueue.pop();

                if (sm.message.equals("shutdown")) {
                    System.out.println("Exiting");
                    btStack.StopCommunicationsCycle();
                    break;
                }

                if (sm.message.equals("brick1")) {
                    brickMode = sm.message;

                    MotorA = new NXTRegulatedMotor(MotorPort.A);
                    MotorB = new NXTRegulatedMotor(MotorPort.B);

                    MotorA.resetTachoCount();
                    MotorB.resetTachoCount();

                    MotorA.setSpeed(motorSpeed);
                    MotorB.setSpeed(motorSpeed);

                    touchSensor1 = new TouchSensor(SensorPort.S1);
                    touchSensor2 = new TouchSensor(SensorPort.S2);

                    lightSensor = new LightSensor(SensorPort.S3, true);
                }

                if (sm.message.equals("brick2")) {
                    brickMode = sm.message;

                    MotorA = new NXTRegulatedMotor(MotorPort.A);
                    MotorB = new NXTRegulatedMotor(MotorPort.B);
                    MotorC = new NXTRegulatedMotor(MotorPort.C);

                    MotorA.resetTachoCount();
                    MotorB.resetTachoCount();
                    MotorC.resetTachoCount();

                    MotorA.setSpeed(motorSpeed);
                    MotorB.setSpeed(motorSpeed);
                    MotorC.setSpeed(motorSpeed);

                    touchSensor1 = new TouchSensor(SensorPort.S1);
                    touchSensor2 = new TouchSensor(SensorPort.S2);
                    touchSensor3 = new TouchSensor(SensorPort.S3);
                }

                //Motor Controls
                if (sm.message.charAt(0) == '!') {
                    switch(brickMode) {
                        case "brick1":
        
                            motorCommand = sm.message.substring(1);
                            //commands = motorCommand.split("[,]+");
                            commands = StringSplitter.splitMotorCommandString(3, motorCommand, ',');

                            if (printMotorCommands) {
                                System.out.println(commands[0]);
                                System.out.println(commands[1]);
                                System.out.println(commands[2]);

                                System.out.println(commands[0].substring(2));
                                System.out.println(commands[1].substring(2));
                                System.out.println(commands[2].substring(2));
                            }

                            aDegrees += Integer.parseInt(commands[0].substring(2));
                            bDegrees += Integer.parseInt(commands[1].substring(2));

                            MotorA.rotateTo(aDegrees, true);
                            MotorB.rotateTo(bDegrees, true);

                            MotorA.waitComplete();
                            MotorB.waitComplete();
                            
                            /*
                            MotorA.setSpeed(motorSpeed);
                            MotorA.resetTachoCount();
                            MotorB.setSpeed(motorSpeed);
                            MotorB.resetTachoCount();

                            MotorA.rotateTo(aDegrees, true);
                            MotorB.rotateTo(bDegrees, true);

                            MotorA.waitComplete();
                            MotorB.waitComplete();

                            MotorA.flt(true);
                            MotorB.flt(true);
                            */
        
                            break;
        
                        case "brick2":
        
                            motorCommand = sm.message.substring(1);
                            //commands = motorCommand.split("[,]+");
                            commands = StringSplitter.splitMotorCommandString(3, motorCommand, ',');

                            if (printMotorCommands) {
                                System.out.println(commands[0]);
                                System.out.println(commands[1]);
                                System.out.println(commands[2]);

                                System.out.println(commands[0].substring(2));
                                System.out.println(commands[1].substring(2));
                                System.out.println(commands[2].substring(2));
                            }

                            aDegrees += Integer.parseInt(commands[0].substring(2));
                            bDegrees += Integer.parseInt(commands[1].substring(2));
                            cDegrees += Integer.parseInt(commands[2].substring(2));

                            MotorA.rotateTo(aDegrees, true);
                            MotorB.rotateTo(bDegrees, true);
                            MotorC.rotateTo(cDegrees, true);

                            MotorA.waitComplete();
                            MotorB.waitComplete();
                            MotorC.waitComplete();

                            /*
                            MotorA.setSpeed(motorSpeed);
                            MotorA.resetTachoCount();
                            MotorB.setSpeed(motorSpeed);
                            MotorB.resetTachoCount();
                            MotorC.setSpeed(motorSpeed);
                            MotorC.resetTachoCount();

                            MotorA.rotateTo(aDegrees, true);
                            MotorB.rotateTo(bDegrees, true);
                            MotorC.rotateTo(cDegrees, true);

                            MotorA.waitComplete();
                            MotorB.waitComplete();
                            MotorC.waitComplete();

                            MotorA.flt(true);
                            MotorB.flt(true);
                            MotorC.flt(true);
                            */
        
                            break;
        
                        default:
        
        
        
                            break;
                    }
                }
            }

            //Sensor Readings
            switch(brickMode) {
                case "brick1":
                    if (touchSensor1.isPressed()) {
                        btStack.CreateMessageQueue(4);
                        while (touchSensor1.isPressed()) { if (Button.ESCAPE.isDown()) break; }
                        Delay.msDelay(holdButtonDelay);
                    }
                    
                    if (touchSensor2.isPressed()) {
                        btStack.CreateMessageQueue(5);
                        while (touchSensor2.isPressed()) { if (Button.ESCAPE.isDown()) break; }
                        Delay.msDelay(holdButtonDelay);
                    }

                    lightVal = lightSensor.readValue();

                    if(lightVal > coinSensorThreshold) {
                        System.out.println("LightVal:" + lightVal);
                        btStack.CreateMessageQueue(50);

                        soundReturn = Sound.playSample(coinSoundFile, 90);
					    System.out.println("SR:" + soundReturn);
                        Delay.msDelay(coinDelay);
                    }
                        
                    
                    break;

                case "brick2":

                    if (touchSensor1.isPressed()) {
                        btStack.CreateMessageQueue(1);
                        while (touchSensor1.isPressed()) { if (Button.ESCAPE.isDown()) break; }
                        Delay.msDelay(holdButtonDelay);
                    }

                    if (touchSensor2.isPressed()) {
                        btStack.CreateMessageQueue(2);
                        while (touchSensor2.isPressed()) { if (Button.ESCAPE.isDown()) break; }
                        Delay.msDelay(holdButtonDelay);
                    }

                    if (touchSensor3.isPressed()) {
                        btStack.CreateMessageQueue(3);
                        while (touchSensor3.isPressed()) { if (Button.ESCAPE.isDown()) break; }
                        Delay.msDelay(holdButtonDelay);
                    }

                    break;

                default:



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
    NXTCommConnector connector;

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
        
        connector = Bluetooth.getConnector();

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

        int responseCode = 0;
        int length;
        char newChar;
        String message = "";
        OpCodeMessage om;
            
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
                        
                        length = input.readInt();
                        message = "";
    
                        for (int i = 0; i < length; i++) {
                            newChar = input.readChar();
                            message = message + newChar;
                        }

                        responseCode = 115;
                        if(!messageQueue.isEmpty()) {
                            om = (OpCodeMessage)messageQueue.pop();
                            responseCode = om.opcode;
                        }
                        
                        if (printResponseCodes && responseCode != 115) 
                            System.out.println("Response Code: " + responseCode);

                        output.writeInt(responseCode);
                        output.flush();
                        
                        if(!message.equals("NoCommand") && printResponseCodes)
                            System.out.println("M:" + message);

                        if(!message.equals("NoCommand")) {
                            receivingQueue.push(new StackMessage(length, message));
                        }                            
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

class StringSplitter {
    public static String[] splitMotorCommandString(int size, String input, char splitChar) {
        String[] result = new String[size];
        String tempString = "";
        int counter = 0;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == splitChar) {
                result[counter] = tempString;
                tempString = "";
                counter++;
            } else if ((i + 1) == input.length()) {
                tempString = tempString + input.charAt(i);
                result[counter] = tempString;
            } else {
                tempString = tempString + input.charAt(i);
            }
        }

        return result;
    }
}