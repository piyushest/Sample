import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;

import javax.crypto.Cipher;

public class Decryption {

	// reading the key from the file provided
	public static byte[] cipherText;

	PrivateKey readKeyFromFile(String keyFileName) throws IOException {
		FileInputStream in = new FileInputStream(keyFileName);
		ObjectInputStream objectIN = new ObjectInputStream(
				new BufferedInputStream(in));
		try {
			BigInteger modulus = (BigInteger) objectIN.readObject();
			BigInteger e = (BigInteger) objectIN.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, e);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PrivateKey publicKey = factory.generatePrivate(keySpec);
			return publicKey;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			objectIN.close();
		}
	}

	// decrypting the message
	// destinationPrivateKey:C:\\Users\\Piyush\\Desktop\\PS1\\receiver\\private.key"
	public void rsaDecrypt(byte[] src, String destinationPrivateKey,
			String outputPlainText) {

		try {

			PrivateKey priKey = readKeyFromFile(destinationPrivateKey);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			byte[] decrytedData = cipher.doFinal(src);
			// System.out.println("hi"+decrytedData[0]);
			// System.out.println(decrytedData[1]);
			System.out.println("the length of decryted data is"
					+ decrytedData.length);
			String srcc = new String(decrytedData);
			FileWriter fw = new FileWriter(outputPlainText);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(srcc);
			bw.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
