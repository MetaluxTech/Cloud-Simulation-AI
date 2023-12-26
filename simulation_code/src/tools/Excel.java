package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Vm;

import Costums.GeoCloudlet;
import Costums.GeoDatacenter;
import Costums.MyVm;

public class Excel {
	
	
	
	public static String SaveResourcesToExcel(String dataset_name,List<GeoCloudlet> geoCloudletsList, List<GeoDatacenter> dcs_list, List<MyVm> vms) {
	    File file = new File(dataset_name); // Path to the CSV file

	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	        // Write the header
	        writer.write("TaskID,StartTime,TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude,DistanceFromDataCenter,DataCenterCpuCost,DataCenterRamCost,DataCenterStorageCost,DataCenterBwCost,DataCenterTotalLoad,NetworkDelay,CET,ObjectiveFunction,DataCenterID");
	        writer.newLine();

	        // Write the data
	        for (GeoCloudlet cloudlet : geoCloudletsList) {
	        	int DataCenterId = cloudlet.getResourceId();
	        	int VmId = cloudlet.getVmId();
	            GeoDatacenter geodatacenter = Tools.getDatacenterById(DataCenterId, dcs_list);
	            MyVm vm = Tools.getVMById(VmId, vms);
	            double dis =Tools.calculateDistance(cloudlet, geodatacenter);
	            DatacenterCharacteristics characteristics = geodatacenter.getPublicCharacteristics();
	            double dataCenterLoad=geodatacenter.getLoad();
	            double networkDelay=Statistics.calculateNetworkDelay(cloudlet,vm);
	            double CET=Statistics.calculateCET(cloudlet, geodatacenter);
	            double ObjectiveFunction=Statistics.calculateObjectiveFunction(CET, networkDelay, dataCenterLoad);
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
	        e.printStackTrace();
	    }

	    // Return the absolute path of the file
	    return file.getAbsolutePath();
	}

	public static String SaveResultsToExcel(String dataset_name, int numProcessedTasks, double simulationTime,
	        double avgCompleteTime, double avgWaitingTime, double avgThroughput, double avgSLAViolation,
	        double avgNegotiationTime) {
	    File file = new File(dataset_name);

	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file)); 
	        writer.write("Number of Processed Tasks,Total Simulation Time,Average Completion Time,Average Waiting Time, Average Throughput,Average SLA Violation,  Average Negotiation Time\n");
            writer.write(String.format("%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f\n",
                                    numProcessedTasks, simulationTime, avgCompleteTime, avgWaitingTime, avgThroughput, avgSLAViolation, avgNegotiationTime));
     
	        writer.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return file.getAbsolutePath();
	}


}
