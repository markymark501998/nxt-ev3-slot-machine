class BTCommunicatorReceiver extends Thread {
    private boolean verbose = false;
	private int INTERVAL = 50;
    private int ERROR_DELAY = 15000;
    private int TIMEOUT_TIME = 120000;
    
    DataInputStream input;
    DataOutputStream output;

    NXTConnection connection;
    
    private boolean communicating = false;
	public boolean killComms = false;
	
	private Thread t;
    
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

        connection = connector.waitForConnection(TIMEOUT_TIME, NXTConnection.PACKET);

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
    
                        System.out.println("Message[" + length + "]: " + message);
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

    public void StopCommunicationsCycle() throws IOException {
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
}