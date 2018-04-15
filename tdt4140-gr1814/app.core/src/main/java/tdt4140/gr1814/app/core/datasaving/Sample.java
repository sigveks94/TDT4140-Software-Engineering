package tdt4140.gr1814.app.core.datasaving;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class Sample {
	
	public String byToStr(byte[] byteArr) {
		String retStr = "";
        for (byte b : byteArr) {
        	retStr += b + ":";
        } return retStr;
	}
	
	public byte[] strToBy(String str, int len) {
		String[] arr = str.split(":");
        
        byte[] bArr = new byte[len];
        
        for (int i = 0; i < len; i++) {
        	bArr[i] = Byte.parseByte(arr[i]);
        }
        return bArr;
	}
    
    public static void main(String [] args) throws Exception {
    	
    	KeyPair keyPair = buildKeyPair();
    	PublicKey puKey = keyPair.getPublic();
    	String[] puKeyStrArray = puKey.toString().split(" ");
    	String key = puKeyStrArray[8];
    	String expo = puKeyStrArray[12];
    	
        byte[] publicKeyBytes = puKey.getEncoded();
        
        String k = new Sample().byToStr(publicKeyBytes);
        System.out.println(k);
        byte[] bArr = new Sample().strToBy(k, publicKeyBytes.length);
 
        
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bArr);
        PublicKey publicKey2 = keyFactory.generatePublic(publicKeySpec);

        System.out.println(puKey.toString().split(" ")[8].equals(publicKey2.toString().split(" ")[8]));
    	
    	/*
    	KeyPair keyPair = buildKeyPair();
    	PublicKey puKey = keyPair.getPublic();
    	String[] puKeyStrArray = puKey.toString().split(" ");
    	String key = puKeyStrArray[8];
    	String expo = puKeyStrArray[12];
    	
    	byte[] byteKey = key.getBytes();
    	X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
    	
    	KeyFactory kf = KeyFactory.getInstance("RSA");
    	
    	
    	PublicKey newKey = kf.generatePublic(keySpecX509);
    	
    	System.out.println(puKey.toString().split(" ")[8]);
    	System.out.println(newKey.toString().split(" ")[8]);
    	*/
    	/*
        // generate public and private keys
        KeyPair keyPair = buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String[] strArr = keyPair.getPublic().toString().split(" ");
        System.out.println(pubKey);
        
        
        // encrypt the message
        byte [] encrypted = encrypt(privateKey, "This is a secret message");     
        System.out.println(new String(encrypted));  // <<encrypted message>>
        
        // decrypt the message
        byte[] secret = decrypt(pubKey, encrypted);                                 
        System.out.println(new String(secret));     // This is a secret message
        */
    }
    
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 512;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

        return cipher.doFinal(message.getBytes());  
    }
    
    public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        
        return cipher.doFinal(encrypted);
    }
}