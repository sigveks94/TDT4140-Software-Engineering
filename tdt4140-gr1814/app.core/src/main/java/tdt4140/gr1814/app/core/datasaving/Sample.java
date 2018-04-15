package tdt4140.gr1814.app.core.datasaving;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class Sample {
	
	public static String byToStr(byte[] byteArr) {
		String retStr = "";
        for (byte b : byteArr) {
        	retStr += b + ":";
        } return retStr;
	}
	
	public static byte[] strToBy(String str) {
		String[] arr = str.split(":");
        int len = arr.length;
        byte[] bArr = new byte[len];
        
        for (int i = 0; i < len; i++) {
        	bArr[i] = Byte.parseByte(arr[i]);
        }
        return bArr;
	}
	
	public static PublicKey makePuKey(String str) {
		byte[] bytArr = strToBy(str);
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
	
	public static PrivateKey makePrKey(String str) {
		byte[] bytArr = strToBy(str);
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
	
	public static String sendPublic(PublicKey key) {
		return byToStr(key.getEncoded());
	}
    
    public static void main(String [] args) throws Exception {
    	
    	String str = "hshjhdjkaskjdhkjasdhkaskjsdhajKAShjkdshskdjllfhudfkldskfhsdkjfghdghdfgh";
    	DataFetchController c = new DataFetchController();
    	//System.out.println(c.doEncrypt(str));
    	
    	/*
    	KeyPair keyPair = buildKeyPair();
    	PublicKey puKey = keyPair.getPublic();
    	String[] puKeyStrArray = puKey.toString().split(" ");
    	String key = puKeyStrArray[8];
    	
        byte[] publicKeyBytes = puKey.getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        
        String k = Sample.byToStr(publicKeyBytes);
        String r = Sample.byToStr(privateKeyBytes);
        System.out.println(k);
        System.out.println(r);
 
        PublicKey publicKey2 = Sample.makePuKey(k);

        System.out.println(puKey.toString().split(" ")[8].equals(publicKey2.toString().split(" ")[8]));
        */
    	/*
        // generate public and private keys
        KeyPair keyPair = buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String[] strArr = keyPair.getPublic().toString().split(" ");
        System.out.println(pubKey);
        
        
        // encrypt the message
        String encrypted = encrypt(pubKey, "This is a secret message and hashasjkdhjkasfhsdghjkhsdjkfghsdfkjhjsdjkfhsdkjfhjkdsfkjhsdfjkhsdjkfhdskjfhkujdsfhkjsdfhkjdsf sdjkfhkjsdfhkdshsdfkjhsdkjfhkjdshfsdhkjsdfh");     
        System.out.println(new String(encrypted));  // <<encrypted message>>
        
        // decrypt the message
        String secret = decrypt(privateKey, encrypted);                                 
        System.out.println(new String(secret));     // This is a secret message
        */
    }
    
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 512;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }

    public static String encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  

        return Sample.byToStr(cipher.doFinal(message.getBytes()));  
    }
    
    public static String decrypt(PrivateKey privateKey, String str) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        return new String(cipher.doFinal(Sample.strToBy(str)));
    }
}