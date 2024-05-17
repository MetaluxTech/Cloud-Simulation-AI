package Security_Manager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import tools.FileManager;

public class Security {
	  public static  String AES_KEY=null;
	  public static String GenerateAESKey(int numBytes) throws Exception {
		  if (AES_KEY==null) 
			  AES_KEY=getRandomBytesAsHexString(numBytes);
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

