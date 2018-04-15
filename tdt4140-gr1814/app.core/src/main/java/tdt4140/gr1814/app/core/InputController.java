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
	

	public void metamorphise(String pointString) throws IOException{ 
		//solution to get while-loop running at the same time as main thread running UI.
		/*Task<Void> task = new Task<Void>() {
	        @Override
	        public Void call() throws IOException {*/
  
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
	         /*
				return null;
	        	}
	    	}; 
	    	
	    //Since the simulation/input retriever is supposed to run continuously side by side with the UI a second thread is needed. 
	    	//The method being run on the second thread is stated above and is a simple simulation which 
	    // changes the location of each patient object in the scope slightly every 1 second.
	    Thread simu_thread = new Thread(task);
	    simu_thread.setDaemon(true);
	    simu_thread.start();    */
		}
		
}
