package Security_Manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Costums_elements.CustomCloudlet;

public class Decryption {
	public static String decryptData(String data, String method, String aes_key) {
		
		
		
	    String pythonScriptPath = "C:\\Users\\mohsal\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\security\\simulation_connection.py"; // Assuming the script is in the same folder
	    String pythonPath = "c:\\Users\\mohsal\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\.venv\\Scripts\\python.exe"; // Adjust for your Python interpreter path (if different)

	    Path scriptPath = Paths.get(pythonScriptPath);

	    try {
	        List<String> commandList = new ArrayList<>();
	        commandList.add(pythonPath);
	        commandList.add(scriptPath.toString());
	        commandList.add(method); // The string to encrypt
	        commandList.add(data); // The string to encrypt
	        if (aes_key != null)commandList.add(aes_key);
	        ProcessBuilder builder = new ProcessBuilder(commandList);
	        Process process = builder.start();
	        process.waitFor(10, TimeUnit.SECONDS);

	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        String line;
	        StringBuilder output = new StringBuilder();
	        while ((line = reader.readLine()) != null) {
	            output.append(line).append("\n");
	        }

	        String encryptedString = output.toString().trim();
	        reader.close();
	        return encryptedString;

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println("Error running Python script:");
	        System.err.println(e.getMessage());
	        return "errors there"; // Or throw an exception if preferred
	    }
	}

}
