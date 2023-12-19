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

import simulation_1.Simulator;

public class utils {

	  public static void DisplaySimulationEvents(List<GeoCloudlet> geoCloudletsList, List<GeoDatacenter> dcs_list) {
		
		  int size = geoCloudletsList.size();
		  String status="SUCCESS";
		  int VmId=0;
		  Log.printLine("\n recived cloudlets  size= "+size );
		 
			Log.printLine("	 ========== OUTPUT ==========");
			//titles column
			Log.printLine("Cloudlet ID\tSTATUS\tData centerID\t VM ID \t Tim \t Start Time\tFinish Time  distance(Km)   DcLoad");

			DecimalFormat dft = new DecimalFormat("###.##");
			for (GeoCloudlet cloudlet:geoCloudletsList) {
				status=cloudlet.getCloudletStatusString().toUpperCase();
				int DataCenterId =cloudlet.getResourceId();
				VmId=cloudlet.getVmId();
				int cloudlet_id= cloudlet.getCloudletId();
				GeoDatacenter geodatacenter=tests.getDatacenterById(DataCenterId, dcs_list);
				double dis=tests.calculateDistance(cloudlet,geodatacenter);				 
				double load=geodatacenter.getLoad();				 
							 
				Log.printLine("\t" +cloudlet_id +"\t"
								+status+"\t\t"						
								+DataCenterId+"\t "
								+VmId+"\t"+
							
								dft.format(cloudlet.getActualCPUTime()) +"\t\t"+
								dft.format(cloudlet.getExecStartTime())+"\t"+
								dft.format(cloudlet.getFinishTime())+"\t        "+
								dis+"     "+
								load
								
							
									);
				}			
		}
	  public static void printResourcesList(List<GeoDatacenter> geoDataCentersList, List<Host> hosts_list, List<MyVm> vms_List,List<GeoCloudlet> tasks_List)  {
		    List<Integer> datacenterIds = new ArrayList<>();
		    List<Integer> hostIds = new ArrayList<>();
		    List<Integer> vmIds = new ArrayList<>();
		    List<Integer> cloudletIds = new ArrayList<>();
		   
		    for (Datacenter dc : geoDataCentersList) {
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
	  
		 public static void printDatasetObject(Map<GeoCloudlet, List<Double>> datasetObject) {
		        System.out.printf("%-15s%-15s%-15s%-15s%-15s%n", "Cloudlet_ID", "DC1_OF", "DC2_OF", "DC3_OF","DataCenterId");

		        for (Map.Entry<GeoCloudlet, List<Double>> entry : datasetObject.entrySet()) {
		            GeoCloudlet cloudlet = entry.getKey();
		            List<Double> dcOfList = entry.getValue();

		            System.out.printf("%-15s", cloudlet.getCloudletId());

		            for (Double dcOf : dcOfList) {
		                System.out.printf("%-15s", dcOf);
		            }

		            System.out.println();
		        }
		    }

		 
}