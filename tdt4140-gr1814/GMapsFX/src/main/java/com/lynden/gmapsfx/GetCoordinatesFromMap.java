package com.lynden.gmapsfx;

import java.util.ArrayList;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;

public class GetCoordinatesFromMap {

	private MVCArray mvc;
	private ArrayList<double[]> latLongSave;
	
	public GetCoordinatesFromMap(MVCArray mvc) {
		this.mvc = mvc;
	}
	public void calculate() { 
		latLongSave = new ArrayList<>();
		for (int i = 0 ; i < mvc.getLength() ; i++ ) {
			LatLong tempLat = new LatLong(mvc.getAt(i));
			double lat = tempLat.getLatitude();
			double longt = tempLat.getLongitude();
			double[] latLongt = new double[] {lat,longt};
			latLongSave.add(latLongt);
		}
	}
	public ArrayList<double[]> getLatLongSave(){
		return latLongSave;
	}
}
