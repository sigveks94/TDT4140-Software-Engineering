package tdt4140.gr1814.app.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class FileHandler {
	
	public void write(String filename, String input) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		writer.print(input);
		writer.close();
	}
	
	public String read(String filename) throws FileNotFoundException {
		String outString;
		Scanner in = new Scanner(new FileReader(filename));
		String result="";
		while(in.hasNextLine()) {
			result+=in.nextLine()+"\n";
		}
		in.close();
		return result;
	}
}