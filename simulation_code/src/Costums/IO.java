package Costums;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;

public class IO {

	
	public static String SaveSimulationEvents(List<GeoCloudlet> geoCloudletsList, List<GeoDatacenter> dcs_list) {
		File file = new File("events.txt");
	    try {
	    	
	        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	        int size = geoCloudletsList.size();
	        String status="SUCCESS";
	        int VmId=0;
	       
	        writer.newLine();
	        //titles column
	        writer.write("Cloudlet ID\tSTATUS\tData centerID\tVM ID \t Tim \t Start Time\tFinish Time\tdistance(Km)");
	        writer.newLine();

	        DecimalFormat dft = new DecimalFormat("###.##");
	        for (GeoCloudlet cloudlet:geoCloudletsList) {
	            status=cloudlet.getCloudletStatusString().toUpperCase();
	            int DataCenterId =cloudlet.getResourceId();
	            VmId=cloudlet.getVmId();
	            int cloudlet_id= cloudlet.getCloudletId();
	            GeoDatacenter geodatacenter=tests.getDatacenterById(DataCenterId, dcs_list);
	            double dis=tests.calculateDistance(cloudlet,geodatacenter);

	            writer.write("\t" +cloudlet_id +"\t"
	                    +status+"\t"
	                    +DataCenterId+"\t "
	                    +VmId+"\t"+
	                    dft.format(cloudlet.getActualCPUTime()) +"\t\t"+
	                    dft.format(cloudlet.getExecStartTime())+"\t  "+
	                    dft.format(cloudlet.getFinishTime())+"\t              "+
	                    dis
	            );
	            writer.newLine();
	        }

	        writer.close();
	      	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return file.getAbsolutePath();
	}

	  public static String SaveEventsToCSV(List<GeoCloudlet> tasks_list) {
		  File file = new File("events.csv"); // Path to the CSV file
		    String status = "SUCCESS";
		    int DataCenterId = 0;
		    int VmId = 0;

		    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		        writer.write("Cloudlet ID,STATUS,Data centerID,VM ID,Tim,Start Time,Finish Time");
		        writer.newLine();

		        DecimalFormat dft = new DecimalFormat("###.##");
		        for (Cloudlet cloudlet : tasks_list) {
		            status = cloudlet.getCloudletStatusString().toUpperCase();
		            DataCenterId = cloudlet.getResourceId();
		            VmId = cloudlet.getVmId();
		            int cloudlet_id = cloudlet.getCloudletId();

		            writer.write(cloudlet_id + "," + status + "," + DataCenterId + "," + VmId + ","
		                + dft.format(cloudlet.getActualCPUTime()) + "," + dft.format(cloudlet.getExecStartTime()) + ","
		                + dft.format(cloudlet.getFinishTime()));
		            writer.newLine();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		    return file.getAbsolutePath();
		}  

	  public static String write_resources_to_csv(Map<String, Object> data) {
		    String csvFilePath = "src/simulation_v2/sim_data.csv";

		    try (FileWriter writer = new FileWriter(csvFilePath)) {
		        // Iterate over the data centers and write their information to the CSV file
				Map<String, Object> dataCenters = (Map<String, Object>) data.get("DATA_CENTERS");
		        for (String dataCenterId : dataCenters.keySet()) {
		            Map<String, Object> dcData = (Map<String, Object>) dataCenters.get(dataCenterId);
		            writer.write("DATA_CENTER," + dataCenterId + "\n");
		            writer.write("longitude," + dcData.get("longitude") + "\n");
		            writer.write("latitude," + dcData.get("latitude") + "\n");
		            writer.write("hostMips," + dcData.get("hostMips") + "\n");
		            writer.write("hostRam," + dcData.get("hostRam") + "\n");
		            writer.write("hostStorage," + dcData.get("hostStorage") + "\n");
		            writer.write("hostBw," + dcData.get("hostBw") + "\n");

		            // Write VMs data
		            Map<String, Object> vms = (Map<String, Object>) dcData.get("VMS");
		            for (String vmId : vms.keySet()) {
		                Map<String, Object> vmData = (Map<String, Object>) vms.get(vmId);
		                writer.write("VM," + vmId + "\n");
		                writer.write("vmMips," + vmData.get("vmMips") + "\n");
		                writer.write("vmstorage," + vmData.get("vmstorage") + "\n");
		                writer.write("vmRam," + vmData.get("vmRam") + "\n");
		                writer.write("vmBw," + vmData.get("vmBw") + "\n");
		            }
		        }

		        // Iterate over the tasks and write their information to the CSV file
		        Map<String, Object> tasks = (Map<String, Object>) data.get("TASKS");
		        for (String taskId : tasks.keySet()) {
		            Map<String, Object> taskData = (Map<String, Object>) tasks.get(taskId);
		            writer.write("TASK," + taskId + "\n");
		            writer.write("longitude," + taskData.get("longitude") + "\n");
		            writer.write("latitude," + taskData.get("latitude") + "\n");
		        
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return csvFilePath;
		}
}
