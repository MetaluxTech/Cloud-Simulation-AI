package myjava1;



import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class API {

	public static void postData(Map<String, Object> DCs, Map<String, Object> Tasks) {
		
		
	}
	
	public static void postBudget(String budget) throws Exception {
	    URL url = new URL("http://127.0.0.1:5000/optimize");
	    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	    httpCon.setDoOutput(true);
	    httpCon.setRequestMethod("POST");
	    httpCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	    httpCon.setRequestProperty("Accept", "application/json");

	    String jsonInputString = "{\"budget_value\":\"" + budget + "\"}";

	    try (OutputStream os = httpCon.getOutputStream()) {
	        byte[] input = jsonInputString.getBytes("utf-8");
	        os.write(input, 0, input.length);
	    }

	    int responseCode = httpCon.getResponseCode();
	    String response_msg = httpCon.getResponseMessage();
	    System.out.println("POST Response Code :: " + responseCode);
	    System.out.println("POST Response Message :: " + response_msg);

	    httpCon.disconnect();
	}

}



 class REQUESTS
{  
	
public static void GET()
	{}

public static void POST()
	{}

}



