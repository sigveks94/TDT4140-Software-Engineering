package tdt4140.gr1814.app.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class Controller {

private ArrayList<Point> inputPoints = new ArrayList<Point>();

public void metamorphise(String string) throws IOException {
	BufferedReader reader = new BufferedReader(new StringReader(string));
	String line;
	while ((line = reader.readLine()) != null) {
		//line = reader.readLine();
		//System.out.println(line);
		String first = line.substring(0, line.indexOf(" "));
		String second = line.substring(line.indexOf(" "));
		Double number1 = Double.parseDouble(first);
		Double number2 = Double.parseDouble(second);
		Point p = new Point(number1,number2);
		inputPoints.add(p);
}
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
