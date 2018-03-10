package com.lynden.gmapsfx;

import com.lynden.gmapsfx.javascript.object.LatLong;

public class InsideCheckTemp {
	
	public LatLong[] latLongs;
	
	public InsideCheckTemp(LatLong[] latLongs) {
		this.latLongs = latLongs;
	}
	
	public boolean checkInside(LatLong p) {
		int k;
		int count = 0;
		for (int i = 0 ; i < latLongs.length ; i++) {
			if (i == latLongs.length - 1) {
				k = 0;
			} else {
				k = i + 1;
			}
			if ((latLongs[i].getLongitude() < p.getLongitude())&&(latLongs[k].getLongitude() > p.getLongitude())) {
				double midle = (p.getLongitude()) - latLongs[i].getLongitude()/(latLongs[k].getLongitude() - latLongs[i].getLongitude());
				if (p.getLatitude() >= (midle * (latLongs[k].getLatitude() - latLongs[i].getLatitude())) + latLongs[i].getLatitude()) {
					count += 1;
				}
			}else if ((latLongs[i].getLongitude() > p.getLongitude())&&(latLongs[k].getLongitude() < p.getLongitude())) {
				double midle = (p.getLongitude() - latLongs[i].getLongitude())/(latLongs[k].getLongitude() - latLongs[i].getLongitude());
				if (p.getLatitude() >= (midle * (latLongs[k].getLatitude() - latLongs[i].getLatitude())) + latLongs[i].getLatitude()) {
					count += 1;
				}
			}
		}
		System.out.println(count);
		return ((count % 2) == 1) ;
	}
}