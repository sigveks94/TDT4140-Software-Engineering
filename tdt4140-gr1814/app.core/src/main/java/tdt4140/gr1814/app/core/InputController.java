package tdt4140.gr1814.app.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


public final class InputController {

private static ArrayList<Patient> monitoredPatients = new ArrayList<Patient>();

public static void addPatientInList(Patient...patients) {
	for (Patient pat: patients) {
		monitoredPatients.add(pat);
	}
}

public static void metamorphise(String string) throws IOException {
	BufferedReader reader = new BufferedReader(new StringReader(string));
	String line;
	while ((line = reader.readLine()) != null) {
		String first = line.substring(0, line.indexOf(":"));
		String second = line.substring(line.indexOf(":")+1, line.indexOf(" "));
		String third = line.substring(line.indexOf(" "));
		Double number1 = Double.parseDouble(second);
		Double number2 = Double.parseDouble(third);
		for (Patient p: monitoredPatients) {
			if (p.getID().equals(first)) {
				Point point = new Point(first, number1,number2);
				p.updateCurrentLocation(point);
			}
		}
		
		
}
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
