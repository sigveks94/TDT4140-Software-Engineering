package database;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerializeXML {
	
	//this class writes/read objects and their states to/from a xml-file.
	
	
	public static void writeObject(Patient patient) {
		try {
			FileOutputStream fos = new FileOutputStream("personDB.xml");
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(patient);
			encoder.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Patient readObject() {
		try {
			FileInputStream fis;
			fis = new FileInputStream("test.xml");
			XMLDecoder decoder = new XMLDecoder(fis);
			Patient p = (Patient)decoder.readObject();
			decoder.close();
			fis.close();
			System.out.println("Patient: " + p);
			return p;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
