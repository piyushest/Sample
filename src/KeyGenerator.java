import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;


public class KeyGenerator {

	public void keyPairGenerator(){
		try{
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
		//SecureRandom random = new SecureRandom();
		kpg.initialize(512);
		KeyPair kp = kpg.genKeyPair();
		Key publicKey = kp.getPublic();
		Key privateKey = kp.getPrivate();
		//savingKeys(publicKey,privateKey);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("C:\\Users\\Piyush\\Desktop\\PS1\\receiver\\DSApublic.key"));
        out.writeObject(kp.getPublic());
        out.close();
        out = new ObjectOutputStream(new FileOutputStream("C:\\Users\\Piyush\\Desktop\\PS1\\sender\\DSAprivate.key"));
        out.writeObject(kp.getPrivate());
        out.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void savingKeys(Key publicKey,Key privateKey){
		try{
		KeyFactory fact = KeyFactory.getInstance("DSA");
		RSAPublicKeySpec pub = fact.getKeySpec(publicKey,
		  RSAPublicKeySpec.class);
		RSAPrivateKeySpec priv = fact.getKeySpec(privateKey,
		  RSAPrivateKeySpec.class);

		saveToFile("C:\\Users\\Piyush\\Desktop\\PS1\\receiver\\DSApublic.key", pub.getModulus(),
		  pub.getPublicExponent());
		saveToFile("C:\\Users\\Piyush\\Desktop\\PS1\\receiver\\DSAprivate.key", priv.getModulus(),
		  priv.getPrivateExponent());
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void saveToFile(String fileName,
			  BigInteger mod, BigInteger exp) throws IOException {
			  ObjectOutputStream oout = new ObjectOutputStream(
			    new BufferedOutputStream(new FileOutputStream(fileName)));
			  try {
			    oout.writeObject(mod);
			    oout.writeObject(exp);
			  } catch (Exception e) {
			    throw new IOException("Unexpected error", e);
			  } finally {
			    oout.close();
			  }
			}
}
