package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.management.loading.PrivateClassLoader;

import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import Costums_elements.CustomVM;
import simulation_1.Simulator;

public class AI {
		private static String dataset_path=Simulator.dataset_path;

	   public static int PredictDataCenterIDFromPython(CustomCloudlet task,String modelName) {
	        String pythonScriptPath = "C:\\Users\\mohsal\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\simulation_predict.py"; // Assuming the script is in the same folder
	        String pythonPath = "c:\\Users\\mohsal\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\.venv\\Scripts\\python.exe"; // Adjust for your Python interpreter path (if different)

	        Path scriptPath = Paths.get(pythonScriptPath);

	        try {
//	        	TaskFileSize	TaskOutputFileSize	TaskFileLength	UserLatitude	UserLongitude	DataCenterID

//	   features  0   TaskFileSize        2000 non-null   int64  
//	        	 1   TaskOutputFileSize  2000 non-null   int64  
//	        	 2   TaskFileLength      2000 non-null   int64  
//	        	 3   CpuTime             2000 non-null   float64
//	        	 4   TotalLength         2000 non-null   int64  
//	        	 5   UserLatitude        2000 non-null   float64
//	        	 6   UserLongitude       2000 non-null   float64    features to sent to Python to predict dc via model
	            List<String> commandList = new ArrayList<>();
	            commandList.add(pythonPath);
	            commandList.add(scriptPath.toString());
	            commandList.add(modelName); // Add any additional arguments
	            commandList.add(String.valueOf(task.getCloudletFileSize()));
	            commandList.add(String.valueOf(task.getCloudletOutputSize()));
	            commandList.add(String.valueOf(task.getCloudletLength()));
	            commandList.add(String.valueOf(task.getActualCPUTime()));
	            commandList.add(String.valueOf(task.getCloudletTotalLength()));
	            commandList.add(String.valueOf(task.getLatitude()));
	            commandList.add(String.valueOf(task.getLongitude()));

	            ProcessBuilder builder = new ProcessBuilder(commandList);
	            Process process = builder.start();
	            process.waitFor(10, TimeUnit.SECONDS);

	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line;
	            StringBuilder output = new StringBuilder();
	            while ((line   = reader.readLine()) != null) {
	                output.append(line).append("\n");
	            }

	            int datacenterID = Integer.parseInt(output.toString().trim());
	            reader.close();
	            return datacenterID;

	        } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println("Error running Python script:");
	            System.err.println(e.getMessage());
	            return -1; // Or throw an exception if preferred
	        }
	    }

 
	   public static int PredictBestDataCenter(CustomCloudlet task, List<CustomDataCenter> DCList, String modelName) {
		  
	    int Predicted_DC_ID = -1;

	    try (BufferedReader br = new BufferedReader(new FileReader(dataset_path))) {
	        String line;
	        int currentRow = 1;
	       

	        while ((line = br.readLine()) != null) {
	            // Skip the first row (headers)
	            if (currentRow == 1) {
	                currentRow++;
	                continue;
	            }

	            String[] rowData = line.split(",");
	            Boolean findit= (
	                    Double.parseDouble(rowData[7]) == task.getLongitude()) ;
	           
	            // Assuming Latitude and Longitude are doubles
	            if (
//	 dataset cols=  0-TaskID 1-TaskFileSize	2-TaskOutputFileSize	3-TaskFileLength	4-CpuTime	5-TotalLength	6-UserLatitude 7-UserLongitude
//	            	8-DataCenterID 9-VmID 10-ENSEMBLE_predicted_DC 11-GA_predicted_DC 12-SNAKE_predicted_DC 13-SNAKE_predicted_VM	14-ENSEMBLE_predicted_VM

	                    Integer.parseInt(rowData[1]) == task.getCloudletFileSize() &&
	                    Integer.parseInt(rowData[2]) == task.getCloudletOutputSize() &&
                		Integer.parseInt(rowData[3]) == task.getCloudletLength() &&
        				Double.parseDouble(rowData[6]) == task.getLatitude() &&
	                    Double.parseDouble(rowData[7]) == task.getLongitude()) {
	             	                // Return the predicted data center ID (assuming it's in the 6th column, adjust if needed)
	            	
	                if (modelName.equals("GA")) {
	                    Predicted_DC_ID = Integer.parseInt(rowData[11]);  //GA predicted DataCenter
	                } else if (modelName.equals("SNAKE")) {
	                    Predicted_DC_ID = Integer.parseInt(rowData[12]);  //SNAKE predicted DataCenter
	                } else if (modelName.equals("ENSEMBLE")) {
	                    Predicted_DC_ID = Integer.parseInt(rowData[10]);  //New Model predicted DataCenter
	                }
	                return Predicted_DC_ID;
	            }
	            currentRow++;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return -1; // Return null if task info is not found in the CSV file
	}

	   
	   public static int PredictBestVM(CustomCloudlet task, List<CustomVM> VMsList, String modelName) {
		  
	    int Predicted_VM_ID = -1;

	    try (BufferedReader br = new BufferedReader(new FileReader(dataset_path))) {
	        String line;
	        int currentRow = 1;
	       

	        while ((line = br.readLine()) != null) {
	            // Skip the first row (headers)
	            if (currentRow <= 1) {
	                currentRow++;
	                continue;
	            }

	            String[] rowData = line.split(",");
	            if (
//	            		 dataset cols=  0-TaskID 1-TaskFileSize	2-TaskOutputFileSize	3-TaskFileLength	4-CpuTime	5-TotalLength	6-UserLatitude 7-UserLongitude
//	            		            	8-DataCenterID 9-VmID 10-ENSEMBLE_predicted_DC 11-GA_predicted_DC 12-SNAKE_predicted_DC 13-SNAKE_predicted_VM	14-ENSEMBLE_predicted_VM

	            		Integer.parseInt(rowData[1]) == task.getCloudletFileSize() &&
	                    Integer.parseInt(rowData[2]) == task.getCloudletOutputSize() &&
                		Integer.parseInt(rowData[3]) == task.getCloudletLength() &&
        				Double.parseDouble(rowData[6]) == task.getLatitude() &&
	                    Double.parseDouble(rowData[7]) == task.getLongitude()) {
	            	

	                if (modelName.equals("SNAKE")) {
	                    Predicted_VM_ID = Integer.parseInt(rowData[13]);  //SNAKE-LSTM predicted VM

		                
		                return Predicted_VM_ID;
	                } else if (modelName.equals("ENSEMBLE")) {
	                    Predicted_VM_ID = Integer.parseInt(rowData[14]);   //ENSEMBLE predicted VM

		                return Predicted_VM_ID;

	            }
	            currentRow++;
	        }
	    } 
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }

	    return -1; // Return null if task info is not found in the CSV file
	}

	   
}
