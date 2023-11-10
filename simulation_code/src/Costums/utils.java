package Costums;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

public class utils {

	  public static void PrintTasks(List<GeoCloudlet> geoCloudletsList, List<GeoDatacenter> dcs_list) {
		  
		  int size = geoCloudletsList.size();
		  String status="SUCCESS";
		  int VmId=0;
		  Log.printLine("\n recived cloudlets  size= "+size );
		 
			Log.printLine("	 ========== OUTPUT ==========");
			//titles column
			Log.printLine("Cloudlet ID\tSTATUS\tData centerID\tVM ID \t Tim \t Start Time\tFinish Time  distance(Km)");

			DecimalFormat dft = new DecimalFormat("###.##");
			for (GeoCloudlet cloudlet:geoCloudletsList) {
				status=cloudlet.getCloudletStatusString().toUpperCase();
				int DataCenterId =cloudlet.getResourceId();
				VmId=cloudlet.getVmId();
				int cloudlet_id= cloudlet.getCloudletId();
				GeoDatacenter geodatacenter=tests.getDatacenterById(DataCenterId, dcs_list);
				double dis=perfomance.calculateDistance(cloudlet,geodatacenter);
//						  
				 
				
				Log.printLine("\t" +cloudlet_id +"\t"
								+status+"\t\t"						
								+DataCenterId+"\t "
								+VmId+"\t"+
							
								dft.format(cloudlet.getActualCPUTime()) +"\t\t"+
								dft.format(cloudlet.getExecStartTime())+"\t"+
								dft.format(cloudlet.getFinishTime())+"\t        "+
								dis
									);
				}
			
			
		}
	  public static void printResourcesList(List<Datacenter> dcs_list, List<Host> hosts_list, List<Vm> vms_List,List<Cloudlet> tasks_List)  {
		    List<Integer> datacenterIds = new ArrayList<>();
		    List<Integer> hostIds = new ArrayList<>();
		    List<Integer> vmIds = new ArrayList<>();
		    List<Integer> cloudletIds = new ArrayList<>();

		    for (Datacenter dc : dcs_list) {
		        datacenterIds.add(dc.getId());
		        
		    }
		    for (Host host : hosts_list) {
		        hostIds.add(host.getId());
		    }
		    for (Vm vm : vms_List) {
		        vmIds.add(vm.getId());
		    }

		    for (Cloudlet cloudlet : tasks_List) {
		        cloudletIds.add(cloudlet.getCloudletId());
		    }

		   
		    System.out.println("List of IDs for all resources:");
		    System.out.println("Datacenter IDs: " + datacenterIds);
		    System.out.println("Host IDs: " + hostIds);
		    System.out.println("VM IDs: " + vmIds);
		    System.out.println("Cloudlet IDs: " + cloudletIds +"\n\n");
		    
		}
      public static String write_to_csv(List<Cloudlet> tasks_list) {
		    String csvFilePath = "src/simulation_v2/results.csv"; // Path to the CSV file
		    String status = "SUCCESS";
		    int DataCenterId = 0;
		    int VmId = 0;

		    try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
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

		    return csvFilePath;
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