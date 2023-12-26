package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpResponse;

import org.cloudbus.cloudsim.Cloudlet;


public class API {
	
	
	  public static String postData(Cloudlet Task) {
		    try {
	            URL url = new URL("http://127.0.0.1:5000/process_data");
	            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	            httpCon.setDoOutput(true);
	            httpCon.setRequestMethod("POST");
	            httpCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	            httpCon.setRequestProperty("Accept", "application/json");

	            int taskID = Task.getCloudletId();
	            double taskFileSize = Task.getCloudletFileSize();
	            double taskOutputFileSize = Task.getCloudletOutputSize();
	            double taskFileLength = Task.getCloudletLength();

	            // Create a JSON-like string with a nested structure
	            String jsonString = String.format("{\"client_name\": \"essam\", \"data\": {\"TaskID\": %d, \"TaskFileSize\": %f, \"TaskOutputFileSize\": %f, \"TaskFileLength\": %f}}",
	                    taskID, taskFileSize, taskOutputFileSize, taskFileLength);

	            try (OutputStream os = httpCon.getOutputStream()) {
	                byte[] input = jsonString.getBytes("utf-8");
	                os.write(input, 0, input.length);
	            }

	            int responseCode = httpCon.getResponseCode();
	            String responseMsg = httpCon.getResponseMessage();

	            System.out.println("POST Response Code :: " + responseCode);
	            System.out.println("POST Response Message :: " + responseMsg);

	            // Read the response content
	            try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()))) {
	                String line;
	                StringBuilder responseContent = new StringBuilder();
	                while ((line = reader.readLine()) != null) {
	                    responseContent.append(line);
	                }
	                System.out.println("POST Response Content :: " + responseContent.toString());
	            }

	            httpCon.disconnect();
	            return responseMsg;
	        } catch (Exception e) {
	            return "error :: " + e.getMessage();
	        }
	    }
}


