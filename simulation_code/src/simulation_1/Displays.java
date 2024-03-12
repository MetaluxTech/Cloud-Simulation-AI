package simulation_1;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import Costums_elements.CustomVM;
import tools.Utils;

public class Displays {

	
	
	  public static void printSimulationSubmittingEvents(List<CustomCloudlet> geoCloudletsList, List<CustomDataCenter> dcs_list) {
			
		  int size = geoCloudletsList.size();
		  String status="SUCCESS";
		  int VmId=0;
		  Log.printLine("\n recived cloudlets  size= "+size );
		 
			Log.printLine("	 ========== OUTPUT ==========");
			//titles column
			Log.printLine("Cloudlet ID\tSTATUS\tData centerID\t VM ID \t Time \t Start Time\tFinish Time  distance(Km)   DcLoad");

			DecimalFormat dft = new DecimalFormat("###.##");
			for (CustomCloudlet cloudlet:geoCloudletsList) {
				status=cloudlet.getCloudletStatusString().toUpperCase();
				int DataCenterId =cloudlet.getResourceId();
				VmId=cloudlet.getVmId();
				int cloudlet_id= cloudlet.getCloudletId();
				CustomDataCenter geodatacenter=Utils.getDatacenterById(DataCenterId, dcs_list);
				double dis=Utils.calculateDistance(cloudlet,geodatacenter);				 
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
			Log.printLine("");
		}
	  
	  public static void PrintSimulationElemntsIDs(List<CustomDataCenter> geoDataCentersList, List<Host> hosts_list, List<CustomVM> vms_List,List<CustomCloudlet> tasks_List)  {
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
	
	  public static void printSimulationTimingSpecifications(Map<String, Double> simulationResults,int numTasks ) {
		    
		    
		    
		    String totalSimulationTimeStr = String.valueOf(simulationResults.get("Total Simulation Time"));
		    String avgCompleteTimeStr = String.valueOf(simulationResults.get("Average Completion Time"));
		    String avgWaitingTimeStr = String.valueOf(simulationResults.get("Average Waiting Time"));
		    String avgThroughputStr = String.valueOf(simulationResults.get("Average Throughput"));
		    String avgSLAViolationStr = String.valueOf(simulationResults.get("Average SLA Violation"));
		    String avgNegotiationTimeStr = String.valueOf(simulationResults.get("Average Negotiation Time"));

		    // Print the results with stored strings
		    Log.printLine("Number of Processed Tasks: " + numTasks);
		    Log.printLine("Total Simulation Time: " + totalSimulationTimeStr);
		    Log.printLine("Average Completion Time: " + avgCompleteTimeStr);
		    Log.printLine("Average Waiting Time: " + avgWaitingTimeStr);
		    Log.printLine("Average Throughput: " + avgThroughputStr);
		    Log.printLine("Average SLA Violation: " + avgSLAViolationStr);
		    Log.printLine("Average Negotiation Time: " + avgNegotiationTimeStr);
		    Log.printLine("");
		}


	public static void printListIds(String text, List<?> list) {
	        List<Integer> ids = new ArrayList<>();
	        try {
	            // Determine the type of the first item in the list to decide which method to call
	            Object firstItem = list.get(0);
	            Class<?> itemClass = firstItem.getClass();
	            Method getIdMethod;
	
	            // Check if the class is CustomCloudlet, which requires a different method
	            if (itemClass.equals(CustomCloudlet.class)) {
	                getIdMethod = itemClass.getMethod("getCloudletId");
	            } else {
	                // For other classes, use getId()
	                getIdMethod = itemClass.getMethod("getId");
	            }
	
	            for (Object item : list) {
	                Integer id = (Integer) getIdMethod.invoke(item);
	                ids.add(id);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Handle the exception appropriately
	        }
	
	        System.out.println(text+"List of IDs: " + ids);
	    }
}
