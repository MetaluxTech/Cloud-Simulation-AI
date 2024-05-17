package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import Costums_elements.CustomVM;
import simulation_1.Simulator;

public class FileManager {
	
	private static String dataset_path=Simulator.dataset_path;

	
	public static String[] LoadTaskData(String prepredicted_dataset_path,int rowID) {
        
        
		
	    try (BufferedReader br = new BufferedReader(new FileReader(prepredicted_dataset_path))) {
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
	
	
	public static String loadSecurityHeader(String security_dataset) {
	   
	    try (BufferedReader br = new BufferedReader(new FileReader(security_dataset))) {
	        List<String> lines = br.lines().collect(Collectors.toList());
	        int randomIndex = Utils.getNextRandom(0, lines.size() - 1);  // Ensure random index is within bounds
	        return lines.get(randomIndex);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;  // Return null if an error occurs
	    }
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
	        writer.write("TaskID,StartTime,TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude,DataCenterLatitude,DataCenterLongitude,DistanceFromDataCenter,DataCenterCpuCost,DataCenterRamCost,DataCenterStorageCost,DataCenterBwCost,DataCenterTotalLoad,NetworkDelay,CET,ObjectiveFunction,DataCenterID");
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
	            double dataCenterLatitude=geodatacenter.getLatitude();
	            double dataCenterLongitude=geodatacenter.getLongitude();
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
	                          dataCenterLatitude + "," + 
	                          dataCenterLongitude + "," + 
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
	    Double totalSimulationTime = simulationResults.get("Total Simulation Time");
	    Double avgCompleteTime = simulationResults.get("Average Completion Time");
	    Double avgWaitingTime = simulationResults.get("Average Waiting Time");
	    Double avgThroughput = simulationResults.get("Average Throughput");
	    Double avgSLAViolation = simulationResults.get("Average SLA Violation");
	    Double avgNegotiationTime = simulationResults.get("Average Negotiation Time");
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

	public static String SaveCloudletsSpecificationsWithTimeExecution(String dataset_name,List<CustomCloudlet> tasksList) {
	    File file = new File(dataset_name); // Path to the CSV file
	    
	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	        // Write the header
	        writer.write("TaskID,StartTime,taskExecutionTime,TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude,taskStatus");
	        writer.newLine();
	        for (CustomCloudlet cloudlet : tasksList) {
	        	String data =
	            			  
	            		      cloudlet.getCloudletId() + "," +
        		    		  cloudlet.getExecStartTime() + "," +
        		    		  cloudlet.getActualCPUTime() + "," +
	                          cloudlet.getCloudletFileSize() + "," +
	                          cloudlet.getCloudletOutputSize() + "," +
	                          cloudlet.getCloudletLength() + "," +
	                          cloudlet.getLatitude() + "," +
	                          cloudlet.getLongitude() + "," +
	                          cloudlet.getSecurityStatus()  ; 
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
	public static String SaveCloudletsSpecifications(String dataset_name,List<CustomCloudlet> tasksList) {
	    File file = new File(dataset_name); // Path to the CSV file
	    
	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	        // Write the header
	        writer.write("TaskID,TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude,taskStatus");
	        writer.newLine();
	        for (CustomCloudlet cloudlet : tasksList) {
	        	String data =
	            			  
	            		      cloudlet.getCloudletId() + "," +
        		    		  cloudlet.getCloudletFileSize() + "," +
	                          cloudlet.getCloudletOutputSize() + "," +
	                          cloudlet.getCloudletLength() + "," +
	                          cloudlet.getLatitude() + "," +
	                          cloudlet.getLongitude() + "," +
	                          cloudlet.getSecurityStatus()  ; 
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


	
	public static String saveWantedDataSet(String dataset_name, List<CustomCloudlet> tasksList, List<CustomDataCenter> dcs_list, List<CustomVM> vmsList) {
        File file = new File(dataset_name); // Path to the CSV file
//        getExecStartTime	getCloudletFileSize	getCloudletOutputSize	getStatus	getUserId	getCloudletLength	getWaitingTime	getFinishTime	DistanceFromDataCenter	DataCenterID	VmID

        String dataset_headers=	"TaskID,TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude,"
        						+"CpuTime,StartExecTime,getFinishTime,"
        						+"DataCenterID,VmID";
        
      
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write the header
        	writer.write(dataset_headers);
        	  writer.newLine();

  	        // Write the data
  	        for (CustomCloudlet cloudlet : tasksList) {
  	        	String dataset_data =

  	        			cloudlet.getCloudletId() + "," +
  	        			cloudlet.getCloudletFileSize() + "," +
  	        			cloudlet.getCloudletOutputSize() + "," +
  	        			cloudlet.getCloudletLength() + "," +
  	        			cloudlet.getLatitude() + "," +
  	        			cloudlet.getLongitude() + "," +
  	        			
  	        			cloudlet.getActualCPUTime()+ ","+  
  	        			cloudlet.getExecStartTime()+ ","+  
  	        			cloudlet.getFinishTime()+ ","+  
  	        			
  	        			cloudlet.getResourceId()+ ","+
						cloudlet.getVmId() ;
  	            
//  	                      
  	            writer.write(dataset_data);
  	            writer.newLine();
  	        }

  	        writer.close();
        } catch (IOException e) {
            Log.printLine("Error in saving to: " + file.getAbsolutePath() + " ..........");
            e.printStackTrace();
        }

        Log.printLine("Saved to: " + file.getAbsolutePath());
        return null;
    }
	
	
	public static String saveWantedTaskDetails(String dataset_name, List<CustomCloudlet> tasksList, List<CustomDataCenter> dcs_list, List<CustomVM> vmsList) {
        File file = new File(dataset_name); // Path to the CSV file
        String task_headers="getCloudletId,getActualCPUTime,getResourceId,getExecStartTime,getCloudletFileSize,getCloudletOutputSize,getUtilizationModelCpu,getCloudletStatusString,getCloudletTotalLength,getCloudletFinishedSoFar,getUtilizationModelBw,getUtilizationModelRam,getRequiredFiles,getVmId,getStatus,getUserId,getCloudletStatus,getNumberOfPes,getCloudletLength,getReservationId,getClassType,getCloudletHistory,getWaitingTime,getNetServiceLevel,getSubmissionTime,getFinishTime,getAllResourceName,getAllResourceId,getCostPerSec,getWallClockTime,getProcessingCost,getClass";
        String dc_headers="DistanceFromDataCenter,DataCenterCpuCost,DataCenterRamCost,DataCenterStorageCost,DataCenterBwCost,DataCenterTotalLoad,NetworkDelay,CET,ObjectiveFunction,DataCenterID,datacenterMemory,datacenterCpu,datacenterBw,datacenterStorage";
        String vm_headers="VmID,VmProcessingSpeed,VmRam,VmBandwidth,VmStorage,VmMemoryCost,VmStorageCost,VmBandwidthCost,VmProcessingCost";
        
        ArrayList<String> task_functions = new ArrayList<>(Arrays.asList(
        		"getCloudletId","getActualCPUTime","getResourceId","getExecStartTime","getCloudletFileSize","getCloudletOutputSize","getUtilizationModelCpu","getCloudletStatusString","getCloudletTotalLength","getCloudletFinishedSoFar","getUtilizationModelBw","getUtilizationModelRam","getRequiredFiles","getVmId","getStatus","getUserId","getCloudletStatus","getNumberOfPes","getCloudletLength","getReservationId","getClassType","getCloudletHistory","getWaitingTime","getNetServiceLevel","getSubmissionTime","getFinishTime","getAllResourceName","getAllResourceId","getCostPerSec","getWallClockTime","getProcessingCost","getClass"
         ));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write the header
        	writer.write(task_headers+","+dc_headers+","+vm_headers);
        	 writer.newLine();

            // Write the data
            for (CustomCloudlet cloudlet : tasksList) {
            	int DataCenterId = cloudlet.getResourceId();
	        	int VmId = cloudlet.getVmId();
	            CustomDataCenter dc = Utils.getDatacenterById(DataCenterId, dcs_list);
	            CustomVM vm = Utils.getVMById(VmId, vmsList);
	            Double dis=Utils.calculateDistance(cloudlet, dc);
	            DatacenterCharacteristics characteristics = dc.getPublicCharacteristics();
	            double dataCenterLoad=dc.getLoad();
	            dc.getLatitude();
	            dc.getLongitude();
	            double networkDelay=DCs_Caculations.calculateNetworkDelay(cloudlet,vm);
	            double networkLatency=DCs_Caculations.calculateLatency(cloudlet,dc);
	            double CET=DCs_Caculations.calculateCET(cloudlet, dc);
	            double ObjectiveFunction=DCs_Caculations.calculateObjectiveFunction(CET, networkDelay, dataCenterLoad,networkLatency);
	            
                StringBuilder taskData = new StringBuilder();
                for (String param : task_functions) {
                    try {
                        // Use reflection to dynamically call the getter method
                        Method method = CustomCloudlet.class.getMethod(param);
                        Object value = method.invoke(cloudlet);
                        taskData.append(value).append(",");
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                // Remove the last comma and add a newline
                taskData.setLength(taskData.length() - 1);
                
                String server_Data=","+
                		dis+","+
                		characteristics.getCostPerSecond() + "," +
                        characteristics.getCostPerMem() + "," +
                        characteristics.getCostPerStorage() + "," +
                        characteristics.getCostPerBw() + "," +
                        dataCenterLoad + "," + 
                        networkDelay + "," + 
                        CET+"," +
                        ObjectiveFunction + "," +
                        DataCenterId +"," +
                        dc.getHostList().get(0).getRam()+"," +
                        dc.getHostList().get(0).getTotalMips()+"," +
                        dc.getHostList().get(0).getBw()+"," +
                        dc.getHostList().get(0).getStorage() ;
                    	
               String vm_Data=vm.getId() + "," +
                       vm.getMips() + "," +
                       vm.getRam() + "," +
                       vm.getBw() + "," +
                       vm.getSize() + "," +
                       vm.getramCost() + "," +
                       vm.getStorageCost() + "," +
                       vm.getBwCost() + "," +
                       vm.getcpuCost()  ;
               writer.write(taskData.toString()+server_Data+vm_Data);

                writer.newLine();
            }
        } catch (IOException e) {
            Log.printLine("Error in saving to: " + file.getAbsolutePath() + " ..........");
            e.printStackTrace();
        }

        Log.printLine("Saved to: " + file.getAbsolutePath());
        return null;
    }
	public static String SaveVmsSchedulingDataset(String dataset_name,List<CustomCloudlet> geoCloudletsList, List<CustomDataCenter> dcs_list, List<CustomVM> vms) {
	    File file = new File(dataset_name); // Path to the CSV file

	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	        writer.write(
	        		"TaskID"
	        		+ ",TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude,"
	        		+ "VmProcessingSpeed,VmRam,VmBandwidth,VmStorage,"
	        		+ "VmMemoryCost,VmStorageCost,VmBandwidthCost,VmProcessingCost,"
	        		+"CpuTime,TotalLength,CostPerSec,StartExecTime,SerivesLevel,"
	        		+ "VmID"
	        		);
  
	        writer.newLine();

	        // Write the data
	        for (CustomCloudlet cloudlet : geoCloudletsList) {
	        	int VmId = cloudlet.getVmId();
	        	CustomVM vm = Utils.getVMById(VmId, vms);
	        	CustomDataCenter dc = Utils.getDatacenterById(cloudlet.getResourceId(), dcs_list);
	           
	        	String data =

	        			cloudlet.getCloudletId() + "," +
	        			cloudlet.getCloudletFileSize() + "," +
	        			cloudlet.getCloudletOutputSize() + "," +
	        			cloudlet.getCloudletLength() + "," +
	        			cloudlet.getLatitude() + "," +
	        			cloudlet.getLongitude() + "," +
	        			vm.getMips() + "," +
	        			vm.getRam() + "," +
	        			vm.getBw() + "," +
	        			vm.getSize() + "," +
	        			vm.getramCost() + "," +
	        			vm.getStorageCost() + "," +
	        			vm.getBwCost() + "," +
	        			vm.getcpuCost() + ","+  
	        			cloudlet.getActualCPUTime()+ ","+  
	        			cloudlet.getCloudletTotalLength()+ ","+  
	        			cloudlet.getCostPerSec()+ ","+  
	        			cloudlet.getExecStartTime()+ ","+  
	        			cloudlet.getNetServiceLevel()+ ","+  
	        			 
	        			vm.getId()  ;
	            
//	                      
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
