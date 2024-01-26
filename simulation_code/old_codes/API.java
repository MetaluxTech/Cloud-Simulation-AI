package myjava1;



import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;

public class API {

	public static void postData( Cloudlet Task) {
		
		  URL url = new URL("http://127.0.0.1:5000/process_data");
	        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	        httpCon.setDoOutput(true);
	        httpCon.setRequestMethod("POST");
	        httpCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        httpCon.setRequestProperty("Accept", "application/json");

	        // Create a JSON-like string directly
	        String jsonString = String.format("{\"TaskID\": %d, \"TaskFileSize\": %d, \"TaskOutputFileSize\": %d, \"TaskFileLength\": %d}",
	                taskID, taskFileSize, taskOutputFileSize, taskFileLength);

	        try (OutputStream os = httpCon.getOutputStream()) {
	            byte[] input = jsonString.getBytes("utf-8");
	            os.write(input, 0, input.length);
	        }

	        int responseCode = httpCon.getResponseCode();
	        String responseMsg = httpCon.getResponseMessage();
	        System.out.println("POST Response Code :: " + responseCode);
	        System.out.println("POST Response Message :: " + responseMsg);

	        httpCon.disconnect();	
	}
	
	
}



public class REQUESTS
{  
	
public static void GET()
	{}

public static void POST()
	{}

}



