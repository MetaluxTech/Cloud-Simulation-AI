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

	  public static void DisplaySimulationEvents(List<GeoCloudlet> geoCloudletsList, List<GeoDatacenter> dcs_list) {
		  
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
				double dis=tests.calculateDistance(cloudlet,geodatacenter);
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
    
}