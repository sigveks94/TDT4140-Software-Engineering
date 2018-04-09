package tdt4140.gr1814.app.core.tcp;

//TCPClient.java
//A client program implementing TCP socket
import java.net.*;

import tdt4140.gr1814.app.core.InputController;

import java.io.*; 

public class TCPClient { 
	InputController inputController;
	public TCPClient() {
		inputController = new InputController();
		
	}
	
	public void initiate() 
	{// arguments supply message and hostname of destination  
		InputController con = new InputController();
		Socket s = null; 
		try{ 
			int serverPort = 6880;
			  String ip = "localhost"; 
			  
		  s = new Socket(ip, serverPort); 
		  DataInputStream input = new DataInputStream(s.getInputStream()); 
		  
		  while (true) {
		  
		  //Step 1 read length
		  int nb = input.readInt();
		  byte[] digit = new byte[nb];
		  //Step 2 read byte
		  String packetString = "";
		  for(int i = 0; i < nb; i++) {
			digit[i] = input.readByte();
		  }
		   String st = new String(digit);
		   inputController.metamorphise(st);
		  }
		}
		   
		catch (UnknownHostException e){ 
			System.out.println("Sock:"+e.getMessage());}
		catch (EOFException e){
			System.out.println("EOF:"+e.getMessage()); }
		catch (IOException e){
			System.out.println("IO:"+e.getMessage());} 
		
		  
		}
	}

	



