package bricksoftware;

import lejos.remote.nxt.*;
import lejos.hardware.Bluetooth;

import java.io.*;

public class NXTBTTest {

	public static void main(String[] args) throws IOException {
		final String NXT = "SLOTS1";
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();   
		System.out.println("Connecting to " + NXT);
		NXTConnection connection = connector.connect(NXT, NXTConnection.PACKET);
		if (connection == null) {
		    System.err.println("Failed to connect");
		    return;
		}
		
		System.out.println("Connected");

		DataInputStream input = connection.openDataInputStream();
		DataOutputStream output = connection.openDataOutputStream();

		System.out.println("Sending data");

		for(int i=0;i<100;i++) {
		    output.writeInt(i);
		    output.flush();
		    System.out.println("Read: " + input.readInt());
		}

		System.out.println("All data sent");

		output.close();
		input.close();
		connection.close(); 

		System.out.println("Connection closed");
	}

}
