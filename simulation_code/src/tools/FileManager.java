package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import Costums_elements.CustomVM;

public class FileManager {
	
	public static String[] LoadTaskData(String prepredicted_dataset_path,int rowID) {
        Path projectpath = Paths.get(System.getProperty("user.dir")).getParent();
		
        String datasetpath = projectpath + prepredicted_dataset_path;
		
	    try (BufferedReader br = new BufferedReader(new FileReader(datasetpath))) {
	        String line;
	        int currentRow = 0;

	        while ((line = br.readLine()) != null) {
	            if (currentRow == rowID) {
	                return line.split(",");
	            }
	            currentRow++;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return null; // Return null if rowID is not found
	}
	
	

	public static String[] LoadCloudletsSpesification(String cloudlets_spesification_path,int rowID) {
       
        File file = new File(cloudlets_spesification_path);
	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        String line;
	        int currentRow = 0;

	        while ((line = br.readLine()) != null) {
	            if (currentRow == rowID) {
	                return line.split(",");
	            }
	            currentRow++;
	        }
	    } catch (IOException e) {
	        Log.printLine("error in get from: "+file.getAbsolutePath()+" ..........");
	        e.printStackTrace();
	    }

	    return null; // Return null if rowID is not found
	}
	
	
	
		
		
	
	public static String SaveTrainingDataSet(String dataset_name,List<CustomCloudlet> geoCloudletsList, List<CustomDataCenter> dcs_list, List<CustomVM> vms) {
	    File file = new File(dataset_name); // Path to the CSV file

	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	        // Write the header
	        writer.write("TaskID,StartTime,TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude,DistanceFromDataCenter,DataCenterCpuCost,DataCenterRamCost,DataCenterStorageCost,DataCenterBwCost,DataCenterTotalLoad,NetworkDelay,CET,ObjectiveFunction,DataCenterID");
	        writer.newLine();

	        // Write the data
	        for (CustomCloudlet cloudlet : geoCloudletsList) {
	        	int DataCenterId = cloudlet.getResourceId();
	        	int VmId = cloudlet.getVmId();
	            CustomDataCenter geodatacenter = Utils.getDatacenterById(DataCenterId, dcs_list);
	            CustomVM vm = Utils.getVMById(VmId, vms);
	            double dis =Utils.calculateDistance(cloudlet, geodatacenter);
	            DatacenterCharacteristics characteristics = geodatacenter.getPublicCharacteristics();
	            double dataCenterLoad=geodatacenter.getLoad();
	            double networkDelay=DCs_Caculations.calculateNetworkDelay(cloudlet,vm);
	            double networkLatency=DCs_Caculations.calculateLatency(cloudlet,geodatacenter);
	            double CET=DCs_Caculations.calculateCET(cloudlet, geodatacenter);
	            double ObjectiveFunction=DCs_Caculations.calculateObjectiveFunction(CET, networkDelay, dataCenterLoad,networkLatency);
	            String data =
	            			  
	            		      cloudlet.getCloudletId() + "," +
                              cloudlet.getExecStartTime() + "," +
	                          cloudlet.getCloudletFileSize() + "," +
	                          cloudlet.getCloudletOutputSize() + "," +
	                          cloudlet.getCloudletLength() + "," +
	                          cloudlet.getLatitude() + "," +
	                          cloudlet.getLongitude() + "," +
	                          dis + "," +
	                          characteristics.getCostPerSecond() + "," +
	                          characteristics.getCostPerMem() + "," +
	                          characteristics.getCostPerStorage() + "," +
	                          characteristics.getCostPerBw() + "," +
	                          dataCenterLoad + "," + 
	                          networkDelay + "," + 
	                          CET+"," +
	                          ObjectiveFunction + "," +
	                          DataCenterId ; 
	            writer.write(data);
	            writer.newLine();
	        }

	        writer.close();
	    } catch (IOException e) {
	        Log.printLine("error in saving to: "+file.getAbsolutePath()+" ..........");

	        e.printStackTrace();
	    }

	    Log.printLine("saved to: "+file.getAbsolutePath());
	    return null;
	}

	public static String SaveExperimentDataSet(String dataset_name,List<CustomCloudlet> tasksList) {
		
	    File file = new File(dataset_name);
	    int numTasks=tasksList.size();
	    Map<String, Double> simulationResults = Results.getSimulationTimingSpecifications(tasksList);		   
	    String totalSimulationTime = String.valueOf(simulationResults.get("Total Simulation Time"));
	    String avgCompleteTime = String.valueOf(simulationResults.get("Average Completion Time"));
	    String avgWaitingTime = String.valueOf(simulationResults.get("Average Waiting Time"));
	    String avgThroughput = String.valueOf(simulationResults.get("Average Throughput"));
	    String avgSLAViolation = String.valueOf(simulationResults.get("Average SLA Violation"));
	    String avgNegotiationTime = String.valueOf(simulationResults.get("Average Negotiation Time"));

	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file)); 
	        writer.write("Number of Processed Tasks,Total Simulation Time,Average Completion Time,Average Waiting Time, Average Throughput,Average SLA Violation,  Average Negotiation Time\n");
            writer.write(String.format("%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f\n",
            		numTasks, totalSimulationTime, avgCompleteTime, avgWaitingTime, avgThroughput, avgSLAViolation, avgNegotiationTime));
     
	        writer.close();
	    } catch (IOException e) {
	        Log.printLine("error in saving to: "+file.getAbsolutePath()+" ..........");

	        e.printStackTrace();
	    }

	    Log.printLine("saved to "+file.getAbsolutePath());
	    return null;
	}

	public static String SaveCloudletsSpecifications(String dataset_name,List<CustomCloudlet> tasksList) {
	    File file = new File(dataset_name); // Path to the CSV file
	    
	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	        // Write the header
	        writer.write("TaskID,StartTime,TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude");
	        writer.newLine();
	        for (CustomCloudlet cloudlet : tasksList) {
	        	String data =
	            			  
	            		      cloudlet.getCloudletId() + "," +
                              cloudlet.getExecStartTime() + "," +
	                          cloudlet.getCloudletFileSize() + "," +
	                          cloudlet.getCloudletOutputSize() + "," +
	                          cloudlet.getCloudletLength() + "," +
	                          cloudlet.getLatitude() + "," +
	                          cloudlet.getLongitude() + ","  ; 
	            writer.write(data);
	            writer.newLine();
	        }

	        writer.close();
	    } catch (IOException e) {
	        Log.printLine("error in saving to: "+file.getAbsolutePath()+" ..........");

	        e.printStackTrace();
	    }

	    Log.printLine("saved to: "+file.getAbsolutePath());
	    return null;
	}


}
