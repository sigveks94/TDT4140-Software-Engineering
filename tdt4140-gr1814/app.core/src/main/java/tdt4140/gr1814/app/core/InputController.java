package tdt4140.gr1814.app.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javafx.concurrent.Task;


public final class InputController {
	//temporary simulation of recieving data and updating Patients currentLocation. 
			static String inputstream = 
					"id1:63.418140 10.402736 \r\n" +//1gamle kjemi
					"id2:63.418000 10.402000 \r\n" + 
					"id1:63.418389 10.402392 \r\n" +//1
					"id2:63.418010 10.402000 \r\n" + 
					"id1:63.418663 10.402103 \r\n" +//1
					"id2:63.418020 10.402000 \r\n" +
					"id1:63.418843 10.402293 \r\n" +//1
					"id2:63.418030 10.402000 \r\n" + 
					"id1:63.418949 10.402819 \r\n" +//1
					"id2:63.418040 10.402000 \r\n" + 
					"id1:63.419237 10.403259 \r\n" +//1
					"id2:63.418050 10.402000 \r\n" + 
					"id1:63.419597 10.403055 \r\n" +//1
					"id2:63.418060 10.402000 \r\n" +
					"id1:63.419765 10.402487 \r\n" +//1hovedbygget
					"id2:63.418170 10.402000 \r\n" + 
					"id1:63.419946 10.401575 \r\n" +//1
					"id2:63.418080 10.402000 \r\n" + 
					"id1:63.420243 10.401264 \r\n" +//1
					"id2:63.418150 10.402000 \r\n" + 
					"id1:63.420546 10.400931 \r\n" +//1
					"id2:63.418090 10.402000 \r\n" +
					"id1:63.420853 10.400555 \r\n" +//1
					"id2:63.418080 10.402000 \r\n" + 
					"id1:63.421161 10.400062 \r\n" +//1
					"id2:63.418300 10.402000 \r\n" + 
					"id1:63.421478 10.399794 \r\n" +//1
					"id2:63.418220 10.402000 \r\n" +
					"id1:63.421792 10.399501 \r\n" +//1
					"id2:63.418200 10.402000 \r\n" + 
					"id1:63.422013 10.399308 \r\n" +//1lyskryss
					"id2:63.418320 10.402000 \r\n" + 
					"id1:63.422015 10.399309 \r\n" +//1lyskryss
					"id2:63.418425 10.402000 \r\n" + 
					"id1:63.422234 10.399233 \r\n" +//1bunnpris
					"id2:63.418220 10.402000 \r\n" +
					"id1:63.422234 10.399233 \r\n" +//1bunnpris
					"id2:63.418370 10.402000 \r\n" + 
					"id1:63.422234 10.399233 \r\n" +//1bunnpris
					"id2:63.418300 10.402000 \r\n" + 
					"id1:63.422234 10.399233 \r\n" +//1bunnpris
					"id2:63.418260 10.402000 \r\n" + 
					"id1:63.422114 10.399404 \r\n" +//1
					"id2:63.418120 10.402000 \r\n" +
					"id1:63.421989 10.399383 \r\n" +//1
					"id2:63.418250 10.402000 \r\n" + 
					"id1:63.421806 10.399522";//1

			private static ArrayList<Patient> monitoredPatients = new ArrayList<Patient>();

	//add the patient-objects this instance of the program should monitor. only location of these patients will be updated.
	public static void addPatientInList(Patient...patients) {
		for (Patient pat: patients) {
			monitoredPatients.add(pat);
		}
	}

	public static void metamorphise() throws IOException{ 
		BufferedReader reader = new BufferedReader(new StringReader(inputstream));//uses the temporary static String
		//solution to get while-loop running at the same time as main thread running UI.
		Task task = new Task<Void>() {
	        @Override
	        public Void call() throws IOException {
	        		String line = "";
	            while (line != null)  {
		        		line = reader.readLine();
		        		String first = line.substring(0, line.indexOf(":"));
		        		String second = line.substring(line.indexOf(":")+1, line.indexOf(" "));
		        		String third = line.substring(line.indexOf(" "));
		        		Double number1 = Double.parseDouble(second);
		        		Double number2 = Double.parseDouble(third);
		        		
		        		for (Patient p: monitoredPatients) {
		        			if (p.getID().equals(first)) {
		        				Point point = new Point(first, number1,number2);
		        				p.changeLocation(point);
		        				}
	        		}            	
	             try {
						Thread.sleep(1500);
				}catch (InterruptedException e) {
						System.out.println("error in: Thread.sleep(1000);");
						e.printStackTrace();
					}
	            	}
				return null;
	        	}
	    	}; 
	    	
	    //Since the simulation/input retriever is supposed to run continuously side by side with the UI a second thread is needed. The method being run on the second thread is stated above and is a simple simulatin which 
	    // changes the location of each patient object in the scope slightly every 1.5 second.
	    Thread simu_thread = new Thread(task);
	    simu_thread.setDaemon(true);
	    simu_thread.start();    
		}
		

		

		


/*
public ArrayList<Point> getPoints(){
	return this.inputPoints;
}

@Override 
public String toString() {
	String s = "";
	for (int i=0; i < inputPoints.size(); i++) {
		s +=  inputPoints.get(i) + "\n" ;
	}
	return s;
}*/
}