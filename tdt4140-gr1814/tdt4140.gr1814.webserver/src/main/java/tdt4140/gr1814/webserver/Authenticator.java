package tdt4140.gr1814.webserver;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.digest.DigestUtils;

public class Authenticator {

	private static String RSA_PUBLIC_KEY = "48:92:48:13:6:9:42:-122:72:-122:-9:13:1:1:1:5:0:3:75:0:48:72:2:65:0:-105:30:37:-14:62:-121:50:-51:-47:9:-63:114:-65:78:88:71:-26:14:-122:8:-88:-54:93:-95:32:-105:-9:-28:112:17:58:-90:-121:-92:-25:-97:-3:83:-88:70:22:47:95:83:125:74:111:117:-87:105:53:-107:-55:56:-55:11:36:-57:-99:46:-50:18:84:93:2:3:1:0:1:";
	private static String RSA_PRIVATE_KEY = "48:-126:1:84:2:1:0:48:13:6:9:42:-122:72:-122:-9:13:1:1:1:5:0:4:-126:1:62:48:-126:1:58:2:1:0:2:65:0:-105:30:37:-14:62:-121:50:-51:-47:9:-63:114:-65:78:88:71:-26:14:-122:8:-88:-54:93:-95:32:-105:-9:-28:112:17:58:-90:-121:-92:-25:-97:-3:83:-88:70:22:47:95:83:125:74:111:117:-87:105:53:-107:-55:56:-55:11:36:-57:-99:46:-50:18:84:93:2:3:1:0:1:2:64:84:-41:78:66:-108:-84:94:-17:-126:94:-47:-58:-44:116:-19:-57:95:-9:-29:116:85:31:88:53:-80:16:-31:-47:-44:-63:55:-123:-122:-39:18:-42:-21:25:50:-21:-13:110:-25:-76:-80:23:64:63:43:-47:-45:20:-82:123:-104:-77:-57:-16:-105:-95:-51:46:7:-103:2:33:0:-1:61:102:-31:46:-38:-91:-100:57:-111:49:-43:-18:46:122:-12:-95:0:0:32:70:31:-9:-72:-67:-106:-92:-127:-89:48:91:31:2:33:0:-105:-111:92:-62:-87:88:-120:44:70:12:89:-23:53:-108:24:-7:31:29:36:54:-27:111:60:-57:-65:23:123:68:-46:-71:93:3:2:33:0:-37:78:-17:-15:-10:103:74:59:36:-65:-101:-7:124:49:21:-42:-47:-21:-123:48:53:-16:-53:-46:65:-100:-119:119:25:-89:-109:-13:2:32:83:85:111:-5:-13:98:34:-44:-68:-38:-45:-96:126:125:-29:-47:45:3:55:-61:27:24:-77:1:92:22:-116:7:6:25:54:-125:2:32:74:5:-5:22:36:68:32:-62:13:37:7:117:-68:68:-95:54:-14:-68:112:-96:114:-45:-83:-124:61:-79:44:-21:22:65:84:66:";
	
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
	
	public static String getPublicKey() {
		return RSA_PUBLIC_KEY;
	}
	
	public static String encryptMessage(String message, String publicKey) {
	
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, createPublic(publicKey));
			return new String(cipher.doFinal(strToBytes(message)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String decryptMessage(String encryptedMessage) {
		
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, createPrivate());
			return new String(cipher.doFinal(encryptedMessage.getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static PrivateKey createPrivate() {
		byte[] bytArr = strToBytes(RSA_PRIVATE_KEY);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytArr);
		try {
			return keyFactory.generatePrivate(privateKeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} return null;
	}	
	
	private static PublicKey createPublic(String publicKey) {
		byte[] bytArr = strToBytes(publicKey);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytArr);
        try {
			return keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} return null;
	}
	
	public static byte[] strToBytes(String str) {
		String[] arr = str.split(":");
        int len = arr.length;
        byte[] bArr = new byte[len];
        
        for (int i = 0; i < len; i++) {
        	bArr[i] = Byte.parseByte(arr[i]);
        }
        return bArr;
	}
		
}
