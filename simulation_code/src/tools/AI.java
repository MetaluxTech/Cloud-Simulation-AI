package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;

public class AI {

	
	public static CustomDataCenter PredictBestDataCenter(CustomCloudlet task, List<CustomDataCenter> DCList, String modelName) {
	    String datasetpath = "C:/Users/mohsal/Desktop/app/metalux/cloudsim/ga_lstm/AI_code/dataset/predictedDataBase.csv";

	    int Predicted_DC_ID = -1;

	    try (BufferedReader br = new BufferedReader(new FileReader(datasetpath))) {
	        String line;
	        int currentRow = 1;

	        while ((line = br.readLine()) != null) {
	            // Skip the first row (headers)
	            if (currentRow == 1) {
	                currentRow++;
	                continue;
	            }

	            String[] rowData = line.split(",");

	            // Assuming Latitude and Longitude are doubles
	            if (
	                    Integer.parseInt(rowData[0]) == task.getCloudletFileSize() &&
	                    Integer.parseInt(rowData[1]) == task.getCloudletOutputSize() &&
	                    Integer.parseInt(rowData[2]) == task.getCloudletLength() &&
	                    Double.parseDouble(rowData[3]) == task.getLatitude() &&
	                    Double.parseDouble(rowData[4]) == task.getLongitude()) {
	                // Return the predicted data center ID (assuming it's in the 6th column, adjust if needed)
	                if (modelName.equals("GA")) {
	                    Predicted_DC_ID = Integer.parseInt(rowData[6]);  //GA predicted DataCenter
	                } else if (modelName.equals("SNAKE")) {
	                    Predicted_DC_ID = Integer.parseInt(rowData[7]);  //SNAKE predicted DataCenter
	                } else if (modelName.equals("New_Model")) {
	                    Predicted_DC_ID = Integer.parseInt(rowData[8]);  //New Model predicted DataCenter
	                }
	                CustomDataCenter dc = Utils.getDatacenterById(Predicted_DC_ID, DCList);
	                return dc;
	            }
	            currentRow++;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return null; // Return null if task info is not found in the CSV file
	}

	   
	
	
}
