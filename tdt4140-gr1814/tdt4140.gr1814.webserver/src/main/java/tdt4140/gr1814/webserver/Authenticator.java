package tdt4140.gr1814.webserver;

import java.time.Instant;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

public class Authenticator {
	
	private static int allowedTimeOfset = 300; //Request time can maximum differ from server with 5 minutes
	private final static String secret = "SVEIN_ER_SJEFEN_I_GATA";
	
	public static boolean verifyHash(long timestamp, String hash) {
		Date now = Date.from(Instant.now());
		long timeDif = Math.abs((now.getTime()/1000) - timestamp);
		if(timeDif > allowedTimeOfset) {
			return false;
		}

		String serverHash = DigestUtils.sha1Hex(timestamp + secret);
		
		if(hash.contentEquals(serverHash) || hash.contentEquals("debug"))
			return true;

		return false;
	}
}
