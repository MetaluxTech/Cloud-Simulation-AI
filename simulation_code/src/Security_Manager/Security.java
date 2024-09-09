package Security_Manager;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Security {
	  public static  String AES_KEY=null;
	  public static String GenerateAESKey(int numBytes) throws Exception {
		  if (AES_KEY==null) {
			AES_KEY=getRandomBytesAsHexString(numBytes);
		}
		  return AES_KEY;
	  }

	 	  public static String getRandomBytesAsHexString(int numBytes) throws NoSuchAlgorithmException {
		  	byte[] aesKeyBytes = SecureRandom.getInstanceStrong().generateSeed(numBytes);
		    StringBuilder hexString = new StringBuilder();
		    for (byte b : aesKeyBytes) {
		        hexString.append(String.format("%02X", b));
		    }
		    String base64String = Base64.getEncoder().encodeToString(aesKeyBytes);
		    return base64String;
		}
}

