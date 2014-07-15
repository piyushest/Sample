import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

public class Signing {
	// this was for the testing but since in the code we
	// will be provided with the key so i am not going to call this method
	// but keeping this method
	public static void startingPoint() {
		try {
			KeyPairGenerator pair = KeyPairGenerator.getInstance("DSA");
			SecureRandom random = new SecureRandom();
			pair.initialize(KEYSIZE, random);
			KeyPair keyPair = pair.generateKeyPair();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(
							"C:\\Users\\Piyush\\Desktop\\PS1\\Signatures\\publicKeyForSignature"));
			out.writeObject(keyPair.getPublic());
			out.close();
			out = new ObjectOutputStream(
					new FileOutputStream(
							"C:\\Users\\Piyush\\Desktop\\PS1\\Signatures\\privateKeyForSignature"));
			out.writeObject(keyPair.getPrivate());
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// SIgning the key
	// sender_private_key_filename:"C:\\Users\\Piyush\\Desktop\\PS1\\Signatures\\privateKeyForSignature"
	// outputCIpherText:"C:\\Users\\Piyush\\Desktop\\PS1\\receiver\\encrypted"
	// outputCipherTextafter
	// signing:"C:\\Users\\Piyush\\Desktop\\PS1\\Signatures\\signaturesDone"
	public static void sign(String outputCipherText, String senderPrivateKey) {
		try {
			ObjectInputStream keyIn = new ObjectInputStream(
					new FileInputStream(senderPrivateKey));
			PrivateKey privkey = (PrivateKey) keyIn.readObject();
			keyIn.close();

			Signature signalg = Signature.getInstance("DSA");
			signalg.initSign(privkey);

			File inputfile = new File(outputCipherText);
			InputStream in = new FileInputStream(inputfile);
			int length = (int) inputfile.length();
			byte[] message = new byte[length];
			in.read(message, 0, length);
			in.close();

			signalg.update(message);
			byte[] signature = signalg.sign();

			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					outputCipherText));
			int signlength = signature.length;
			out.writeInt(signlength);
			out.write(signature, 0, signlength);
			out.write(message, 0, length);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// sender_public_key_filename:"C:\\Users\\Piyush\\Desktop\\PS1\\Signatures\\publicKeyForSignature"
	// inputCipherTextFile:
	// "C:\\Users\\Piyush\\Desktop\\PS1\\Signatures\\signaturesDone"
	public static void checkSignatures(String senderPublicKey,
			String inputCipherTextFile) {
		try {
			ObjectInputStream keyIn = new ObjectInputStream(
					new FileInputStream(senderPublicKey));
			PublicKey pubkey = (PublicKey) keyIn.readObject();
			keyIn.close();

			Signature verifyalg = Signature.getInstance("DSA");
			verifyalg.initVerify(pubkey);

			File inputFile = new File(inputCipherTextFile);
			DataInputStream in = new DataInputStream(
					new FileInputStream(inputFile));
			int signlength = in.readInt();
			byte[] signature = new byte[signlength];
			in.read(signature, 0, signlength);

			int length = (int) inputFile.length() - signlength - 4;
			byte[] message = new byte[length];
			in.read(message, 0, length);
			in.close();

			verifyalg.update(message);
			if (!verifyalg.verify(signature))
				System.out.print("not ");
			System.out.println("verified");
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int KEYSIZE = 1024;
}
