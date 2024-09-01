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
		private static String dataset_path=Simulator.global_dataset_path;
		private static 	 String AI_Dcs_script_path = Paths.get("").toAbsolutePath().getParent().resolve("AI_code\\external_interrupt_code_dcs.py").toString();	
		private static 	 String AI_vms_script_path = Paths.get("").toAbsolutePath().getParent().resolve("AI_code\\external_interrupt_code_vms.py").toString();	
		private static String venv_python_exe_path=Paths.get("").toAbsolutePath().getParent().resolve("AI_code\\.venv\\Scripts\\python.exe").toString();	
		
	
	   public static int UseDataSetToGetBestDataCenter(CustomCloudlet task, List<CustomDataCenter> DCList, String modelName) {
		  
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

	   public static int UseDataSetToGetBestVm(CustomCloudlet task, List<CustomVM> VMsList, String modelName) {
		  
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

	 
	   public static int UseAiModelToPredictDataCenter(CustomCloudlet task,String modelName) {

	        Path scriptPath = Paths.get(AI_Dcs_script_path);
			
			modelName=getModelNameOnDisk(modelName, true);

	        try {

	            List<String> commandList = new ArrayList<>();
	            commandList.add(venv_python_exe_path);
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

	   
	   
	   public static int UseAiToPredictVmID(CustomCloudlet task,String modelName) {

	        Path scriptPath = Paths.get(AI_vms_script_path);
	        modelName=getModelNameOnDisk(modelName, false);
	        try {
	            List<String> commandList = new ArrayList<>();
	            commandList.add(venv_python_exe_path);
	            commandList.add(scriptPath.toString());
	            commandList.add(modelName); // Add any additional arguments
	            commandList.add(String.valueOf(task.getCloudletFileSize()));
	            commandList.add(String.valueOf(task.getCloudletOutputSize()));
	            commandList.add(String.valueOf(task.getCloudletLength()));
	            commandList.add(String.valueOf(task.getActualCPUTime()));
	            commandList.add(String.valueOf(task.getCloudletTotalLength()));
	            
	            ProcessBuilder builder = new ProcessBuilder(commandList);
	            Process process = builder.start();
	            process.waitFor(10, TimeUnit.SECONDS);

	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line;
	            StringBuilder output = new StringBuilder();
	            while ((line   = reader.readLine()) != null) {
	                output.append(line).append("\n");
	            }
	            Log.printLine("commands list"+commandList.toString());

	            int vm_id = Integer.parseInt(output.toString().trim());
	            reader.close();
	            return vm_id;

	        } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println("Error running Python script:");
	            System.err.println(e.getMessage());
	            return -1; // Or throw an exception if preferred
	        }
	    }

	   
		private static String  getModelNameOnDisk(String modelname,boolean for_Dcs) {
			if (for_Dcs) {
			if (modelname=="SNAKE") return "snake_model_95.keras";
			if (modelname=="GA") return "ga_model_82.keras";
			if (modelname=="ENSEMBLE") return "ensemble_model_84.joblib";
			}
	
			
			if (modelname=="SNAKE") return "snake_vms_scheduling_99.keras";
			if (modelname=="ENSEMBLE") return "ensemble_vms_scheduling_84.joblib";
			
			
			return "no model selected";
		}
}
