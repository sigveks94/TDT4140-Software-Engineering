package tdt4140.gr1814.app.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


public class ImputController {

private ArrayList<Point> inputPoints = new ArrayList<Point>();

public void metamorphise(String string) throws IOException {
	BufferedReader reader = new BufferedReader(new StringReader(string));
	String line;
	while ((line = reader.readLine()) != null) {
		String first = line.substring(0, line.indexOf(":"));
		String second = line.substring(line.indexOf(":")+1, line.indexOf(" "));
		String third = line.substring(line.indexOf(" "));
		Double number1 = Double.parseDouble(second);
		Double number2 = Double.parseDouble(third);
		Point p = new Point(first, number1,number2);
		inputPoints.add(p);
}
}

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
}
}
