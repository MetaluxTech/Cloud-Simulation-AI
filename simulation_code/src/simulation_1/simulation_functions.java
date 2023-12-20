package simulation_1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import Costums.GeoCloudlet;
import Costums.GeoDatacenter;
import Costums.MyVm;
import tools.Tools;

public class simulation_functions {

	
	  public static GeoDatacenter createDatacenter( String name, int hostId) {
		  
			List<Host> hostList = new ArrayList<Host>();
			List<Pe> peList = new ArrayList<Pe>();
			LinkedList<Storage> storageList = new LinkedList<Storage>(); 
			
			
			double DcLatit=			Tools.generateRandomLatLon()[0];
			double DcLongt=			Tools.generateRandomLatLon()[1];
			int hostMips =			Tools.getNextRandom(5000, 15000); //miiliion instruction per sec
			int hostRam = 			Tools.getNextRandom(128, 256);         //host memory (MB)
			long hostStorage = 		Tools.getNextRandom(1024, 64000);; //host storage in ( GB )
			int hostBw = 			Tools.getNextRandom(250, 1000);;  //MBPs
			
			peList.add(new Pe(0, new PeProvisionerSimple(hostMips))); // need to store Pe id and MIPS Rating
			VmScheduler schudler=			new VmSchedulerTimeShared(peList);
			
			
			Host host1=new Host(hostId,new RamProvisionerSimple(hostRam),new BwProvisionerSimple(hostBw),hostStorage,peList,schudler);            
			
			
			
			String arch =			 	"x86"; // system architecture
			String os = 				"Linux"; // operating system
			String vmm = 				"Xen";
			double time_zone = 			10.0; // time zone this resource located
			double cost = 				Tools.getNextRandom(10, 50);; // the cost of using processing in this resource
			double costPerMem = 		Tools.getNextRandom(1, 10);; // the cost of using memory in this resource
			double costPerStorage = 	Tools.getNextRandom(1, 10);; // the cost of using storage in this// resource
			double costPerBw =			Tools.getNextRandom(1, 10);; // the cost of using bw in this resource
			DatacenterCharacteristics characteristics = new DatacenterCharacteristics( arch, os, vmm, hostList, time_zone, cost, costPerMem,costPerStorage, costPerBw);			
			
			
			hostList.add(host1);
			VmAllocationPolicy policy=new VmAllocationPolicySimple(hostList);
			
			
			
			GeoDatacenter datacenter = null;      	
			try {
				datacenter = new GeoDatacenter(name, characteristics, policy, storageList, 0,DcLatit,DcLongt,0.0);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return datacenter;
		}

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
				GeoDatacenter geodatacenter=Tools.getDatacenterById(DataCenterId, dcs_list);
				double dis=Tools.calculateDistance(cloudlet,geodatacenter);				 
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
