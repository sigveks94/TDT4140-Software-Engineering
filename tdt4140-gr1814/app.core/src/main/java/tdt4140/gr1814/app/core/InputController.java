package tdt4140.gr1814.app.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.security.auth.x500.X500Principal;

import javafx.concurrent.Task;
import tdt4140.gr1814.app.core.participants.Patient;
import tdt4140.gr1814.app.core.tcp.TCPClient;
import tdt4140.gr1814.app.core.zones.Point;

public class InputController { //inputcontroller now handles single strings with points from tcp package

	// Recieving data and updating Patients currentLocation. 
			
	public void metamorphise(String pointString) throws IOException{ 
		
		        		String first = pointString.substring(0, pointString.indexOf(":"));
		        		String second = pointString.substring(pointString.indexOf(":")+1, pointString.indexOf(" "));
		        		String third = pointString.substring(pointString.indexOf(" "));
		        		Double number1 = Double.parseDouble(second);
		        		Double number2 = Double.parseDouble(third);
		        		
		        		for (Patient p: Patient.getAllPatients()) {
		        			if (p.getID().equals(first)) {
		        				Point point = new Point(first, number1,number2);
		        				p.changeLocation(point);
		        				}
	        		}            	
	        
		}
		
}
