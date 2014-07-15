import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

/*
 * java fcrypt
 *  -e 0
 *  destination_public_key_filename 1
 *  sender_private_key_filename 2
 *  input_plaintext_file 3
 *  output_ciphertext_file 4
 * 
 * java fcrypt
 *  -d 
 *  destination_private_key_filename
 *  sender_public_key_filename
 *  input_ciphertext_file
 *  output_plaintext_file
 * */
public class fcrypt {

	public static void main(String[] args) {

		try {
			// checking the number of arguments i am getting is correct or not
			if (args.length != 5) {
				System.err.println("The number of arguments are nor correct");
			}
			// For Encryption
			if (args[0] == "-e") {
				String inputText = new String();
				FileInputStream stream = new FileInputStream(args[3]);
				DataInputStream in = new DataInputStream(stream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					inputText = inputText + strLine;
				}
				Encryption encrypt = new Encryption();
				encrypt.rsaEncrypt(args[1], inputText, args[4], args[2]);
				// Signing.startingPoint();

				Decryption.cipherText = Encryption.encryptedText;
				System.out.println("Encryption done and signing done");
			}

			// For Decryption
			if (args[0] == "-d") {
				Decryption dec = new Decryption();
				String st = new String();
				ObjectInputStream objIn = new ObjectInputStream(
						new FileInputStream("args[3]"));
				while (objIn.readObject() != null) {
					st = (String) objIn.readObject();
				}
				dec.rsaDecrypt(st.getBytes(), args[1], args[4]);
				Signing.checkSignatures(args[2], args[3]);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
