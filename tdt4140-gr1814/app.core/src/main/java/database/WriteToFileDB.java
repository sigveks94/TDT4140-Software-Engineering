package database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class WriteToFileDB {
	
	//This class takes a person object as input and simply writes its toString to a file.
	private String fileName = "personDB.txt";
	
	
	public void writeToFile(Patient patient) throws FileNotFoundException, UnsupportedEncodingException {
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("personDB.txt", true)));
		    out.println(patient);
		    out.close();
		} catch (IOException e) {
		}
	}
}
