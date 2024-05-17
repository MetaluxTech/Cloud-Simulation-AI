package simulation_1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import Costums_elements.CustomDataCenter;
import Costums_elements.CustomCloudlet;
import Costums_elements.CustomVM;
import Costums_elements.CustomBroker;
import tools.Utils;

public class simulation_functions {

	
	  public static CustomDataCenter createDatacenter( String name, int hostId) {
		  
			List<Host> hostList = new ArrayList<Host>();
			List<Pe> peList = new ArrayList<Pe>();
			LinkedList<Storage> storageList = new LinkedList<Storage>(); 
		
			
			double DcLatit=			67.22677605837688;
			double DcLongt=			76.5628888295758;
			int hostMips =			8000; //miiliion instruction per sec
			int hostRam = 			164;         //host memory (MB)
			long hostStorage = 		50000; //host storage in ( GB )
			int hostBw = 			800;  //MBPs
			peList.add(new Pe(0, new PeProvisionerSimple(hostMips))); // need to store Pe id and MIPS Rating
			VmScheduler schudler=			new VmSchedulerTimeShared(peList);
			Host host1=new Host(hostId,new RamProvisionerSimple(hostRam),new BwProvisionerSimple(hostBw),hostStorage,peList,schudler);            
			
	
			
			String arch =			 	"x86"; // system architecture
			String os = 				"Linux"; // operating system
			String vmm = 				"Xen";
			double time_zone = 			10.0; // time zone this resource located
			double cost = 				40; // the cost of using processing in this resource
			double costPerMem = 		5; // the cost of using memory in this resource
			double costPerStorage = 	7; // the cost of using storage in this// resource
			double costPerBw =			10; // the cost of using bw in this resource
			DatacenterCharacteristics characteristics = new DatacenterCharacteristics( arch, os, vmm, hostList, time_zone, cost, costPerMem,costPerStorage, costPerBw);			
			
			
			hostList.add(host1);
			VmAllocationPolicy policy=new VmAllocationPolicySimple(hostList);
			
			
			
			CustomDataCenter datacenter = null;      	
			try {
				datacenter = new CustomDataCenter(name, characteristics, policy, storageList, 0,DcLatit,DcLongt,0.0);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return datacenter;
		}

	  public static void DisplaySimulationEvents(List<CustomCloudlet> geoCloudletsList, List<CustomDataCenter> dcs_list) {
			
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
				CustomDataCenter CustomDataCenter=Utils.getDatacenterById(DataCenterId, dcs_list);
				double dis=Utils.calculateDistance(cloudlet,CustomDataCenter);				 
				double load=CustomDataCenter.getLoad();				 
							 
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
	  
	  public static void printResourcesList(List<CustomDataCenter> CustomDataCentersList, List<Host> hosts_list, List<CustomVM> vms_List,List<CustomCloudlet> tasks_List)  {
		    List<Integer> datacenterIds = new ArrayList<>();
		    List<Integer> hostIds = new ArrayList<>();
		    List<Integer> vmIds = new ArrayList<>();
		    List<Integer> cloudletIds = new ArrayList<>();
		   
		    for (Datacenter dc : CustomDataCentersList) {
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
  
	  public static void printOFunctions(Map<CustomCloudlet, String> DCsOFunctions) {
		  System.out.printf("\n\n\n%-10s%-1s%-10s%-10s%-1s%n", "Cld_ID", "DC #3", "DC #4", "DC #5","DC #");

		        for (Entry<CustomCloudlet, String> entry : DCsOFunctions.entrySet()) {
		            CustomCloudlet cloudlet = entry.getKey();
		            String Ofunc_string = entry.getValue();

		            System.out.printf("%-1s", cloudlet.getCloudletId());
		            System.out.printf("%-1s", Ofunc_string);
		            System.out.printf("        %-15s", entry.getKey().getResourceId());
		            

		            System.out.println();
		        }
		    }
  
	  
}
