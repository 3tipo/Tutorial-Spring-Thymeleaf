package com.invoice.system.util.criptografia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Assinatura {	
public static String Asignature(String file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		
		byte[] file1=file.getBytes();
		String PKeyFile = "src\\main\\java\\com\\invoice\\system\\util\\criptografia\\ChavePrivada.der";
		Path pathPKey = Paths.get(PKeyFile);
		byte[] encodedPrivateKey = Files.readAllBytes(pathPKey);
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
	    RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	    
	    Signature signature = Signature.getInstance("SHA1withRSA");
	    signature.initSign(privateKey);
	    signature.update(file1);
	    
	    byte[] signatureBytes = signature.sign();
	    String encoded = new String(Base64.encodeBase64(signatureBytes));
	    //System.out.println(encoded);
	    return encoded;
	}
}