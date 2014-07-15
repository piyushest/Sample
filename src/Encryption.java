import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class Encryption {
	static byte[] encryptedText;
	static byte[] sigbytes;

	public PublicKey readKeyFromFile(String keyFileName) throws IOException {
		FileInputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
				in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

	PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException {
		FileInputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
				in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey pubKey = fact.generatePrivate(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

	// destination public key file name:
	// "C:\\Users\\Piyush\\Desktop\\PS1\\receiver\\public.key"
	// output Cipher Text:"C:\\Users\\Piyush\\Desktop\\PS1\\receiver\\encrypted"
	public void rsaEncrypt(String destinationPublicKeyFileName, String sr,
			String outputCipherText, String sender_private_key_filename) {

		try {
			System.out.println("The length of String is " + sr.length());
			byte[] src = sr.getBytes();
			PublicKey pubKey = readKeyFromFile(destinationPublicKeyFileName);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherData = cipher.doFinal(src);
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(outputCipherText));
			out.writeObject(cipherData);
			out.close();
			encryptedText = cipherData;
			Signing.sign(outputCipherText, sender_private_key_filename);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
