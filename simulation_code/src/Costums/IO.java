package Costums;

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

public class IO {
	
	
	 public static String saveDatasetObjectToCSV(Map<GeoCloudlet, List<Double>> datasetObject) {
	        File file = new File("newDataSet.csv");

	        try {
	            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	            // Write the header
	            writer.write("Cloudlet_ID,DC1_OF,DC2_OF,DC3_OF,DataCenterId");
	            writer.newLine();

	            // Write the data
	            for (Map.Entry<GeoCloudlet, List<Double>> entry : datasetObject.entrySet()) {
	                GeoCloudlet cloudlet = entry.getKey();
	                List<Double> dcOfList = entry.getValue();

	                StringBuilder data = new StringBuilder(cloudlet.getCloudletId());

	                for (Double dcOf : dcOfList) {
	                    data.append(",").append(dcOf);
	                }

	                writer.write(data.toString());
	                writer.newLine();
	            }

	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		    return file.getAbsolutePath();

	    }
	
	public static String SaveResourcesToCSV(List<GeoCloudlet> geoCloudletsList, List<GeoDatacenter> dcs_list, List<MyVm> vms) {
	    File file = new File("resources.csv"); // Path to the CSV file

	    try {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	        // Write the header
	        writer.write("TaskID,StartTime,TaskFileSize,TaskOutputFileSize,TaskFileLength,DistanceFromDataCenter,DataCenterCpuCost,DataCenterRamCost,DataCenterStorageCost,DataCenterBwCost,DataCenterTotalLoad,NetworkDelay,CET,ObjectiveFunction,DataCenterID");
	        writer.newLine();

	        // Write the data
	        for (GeoCloudlet cloudlet : geoCloudletsList) {
	        	int DataCenterId = cloudlet.getResourceId();
	        	int VmId = cloudlet.getVmId();
	            GeoDatacenter geodatacenter = tests.getDatacenterById(DataCenterId, dcs_list);
	            MyVm vm = tests.getVMById(VmId, vms);
	            double dis = tests.calculateDistance(cloudlet, geodatacenter);
	            DatacenterCharacteristics characteristics = geodatacenter.getPublicCharacteristics();
	            double dataCenterLoad=geodatacenter.getLoad();
	            double networkDelay=perfomance.calculateNetworkDelay(cloudlet,vm);
	            double CET=perfomance.calculateCET(cloudlet, geodatacenter);
	            double ObjectiveFunction=perfomance.calculateObjectiveFunction(CET, networkDelay, dataCenterLoad);
	            String data =
	            			  
	            		      cloudlet.getCloudletId() + "," +
                              cloudlet.getExecStartTime() + "," +
	                          cloudlet.getCloudletFileSize() + "," +
	                          cloudlet.getCloudletOutputSize() + "," +
	                          cloudlet.getCloudletLength() + "," +
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


}
