package tdt4140.gr1814.app.core.tcp;

//TCPServer.java
//A server program implementing TCP socket
import java.net.*;

import java.io.*; 

public class TCPServer { 
	
public static void main (String args[]) 
{ 
	
	try{ 
			int serverPort = 6880; // LISTENING PORT
			ServerSocket listenSocket = new ServerSocket(serverPort); 
	  
			System.out.println("server start listening... ... ...");
		
			while(true) { 
				Socket clientSocket = listenSocket.accept(); 
				Connection c = new Connection(clientSocket); 
			} 
	} 
	catch(IOException e) {
		System.out.println("Listen :"+e.getMessage());} 
}
}
			  
class Connection extends Thread { 
	DataInputStream input;  
	Socket clientSocket; 
	
	public Connection (Socket aClientSocket) { 
		try { 
					clientSocket = aClientSocket; 
					input = new DataInputStream(clientSocket.getInputStream()); 
					this.start(); 
		} 
			catch(IOException e) {
			System.out.println("Connection:"+e.getMessage());
			} 
	  } 

	  public void run() { 
		try { // an echo server 
		  //  String data = input.readUTF();
			
			DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream()); 
			String inputstream = String.valueOf(SimulatedData.Lines);
			
			BufferedReader reader = new BufferedReader(new StringReader(inputstream));
			String line = "";
			while(true) {
		        while (line != null)  {
		        		line = reader.readLine();
					  //Step 1 send length
					  output.writeInt(line.length());
					  //Step 2 send length
					  output.writeBytes(line); // UTF is a string encoding
					  
					  try {
						  Thread.sleep(400);
					  }catch (InterruptedException e) {
							System.out.println("error in: Thread.sleep(1000);");
							e.printStackTrace();
						}
	        		}
		    }} 
			catch(EOFException e) {
			System.out.println("EOF:"+e.getMessage()); } 
			catch(IOException e) {
			System.out.println("IO:"+e.getMessage());}  

		}
}
