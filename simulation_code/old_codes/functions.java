package myjava1;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;

import org.cloudbus.cloudsim.DatacenterBroker;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.lists.HostList;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;



public class functions {



  public static void printCloudletList(List<Cloudlet> cloudletslist) {
	  
	  int size = cloudletslist.size();
	  String status="SUCCESS";
	  int DataCenterId=0;
	  int VmId=0;
	  Log.printLine("\n recived cloudlets  size= "+size+ "\n" );
		
	
		Log.printLine("	 ========== OUTPUT ==========");
		//titles column
		Log.printLine("Cloudlet ID\tSTATUS\tData centerID\tVM ID \t Tim \t Start Time\tFinish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (Cloudlet cloudlet:cloudletslist) {
			status=cloudlet.getCloudletStatusString().toUpperCase();
			DataCenterId=cloudlet.getResourceId();
			VmId=cloudlet.getVmId();
			int cloudlet_id= cloudlet.getCloudletId();
			 
			
			Log.printLine("\t" +cloudlet_id +"\t"
							+status+"\t\t"						
							+DataCenterId+"\t "
							+VmId+"\t"+
						
							dft.format(cloudlet.getActualCPUTime()) +"\t\t"+
							dft.format(cloudlet.getExecStartTime())+"\t"+
							dft.format(cloudlet.getFinishTime()));
			}
		
		
	}

  
  
  public static Datacenter createDatacenter( String name, int hostId, Long hostStorage, int hostMips, int hostRam, int hostBw,double longitude,double latitude) {

		List<Host> hostList = new ArrayList<Host>();
		List<Pe> peList = new ArrayList<Pe>();
		LinkedList<Storage> storageList = new LinkedList<Storage>(); 
		
		
		peList.add(new Pe(0, new PeProvisionerSimple(hostMips))); // need to store Pe id and MIPS Rating
		
		Host host1=new Host(hostId,new RamProvisionerSimple(hostRam),new BwProvisionerSimple(hostBw),hostStorage,peList,new VmSchedulerSpaceShared (peList));
		hostList.add(host1);

		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		// we are not adding SAN
													// devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
//		CustomVmPolicy vmpolicy1 = new CustomVmPolicy(hostList);
	      	
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

public static DatacenterBroker createBroker(String name) {
	
	DatacenterBroker broker;
	try {
		
		broker = new DatacenterBroker(name);
		return broker;
	
	
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	
	
		
	}

public static Cloudlet createCloudlets(int cloudletId, long cletLength, long cletFileSize, long cletOutputSize, int clletPesNumber, int brokerId,double longitude,double latitude) {


      Cloudlet task = new Cloudlet(cloudletId, cletLength, clletPesNumber, cletFileSize, cletOutputSize,  new UtilizationModelFull(),  new UtilizationModelFull(),  new UtilizationModelFull());
      task.setUserId(brokerId);
     

  return task;
}

public static void printResourceIds(List<Datacenter> datacenters, List<Host> hosts, List<Vm> vms, List<Cloudlet> cloudlets) {
    List<Integer> datacenterIds = new ArrayList<>();
    List<Integer> hostIds = new ArrayList<>();
    List<Integer> vmIds = new ArrayList<>();
    List<Integer> cloudletIds = new ArrayList<>();

    for (Datacenter dc : datacenters) {
        datacenterIds.add(dc.getId());
        
    }
    for (Host host : hosts) {
        hostIds.add(host.getId());
    }
    for (Vm vm : vms) {
        vmIds.add(vm.getId());
    }

    for (Cloudlet cloudlet : cloudlets) {
        cloudletIds.add(cloudlet.getCloudletId());
    }

   
    System.out.println("\n\nList of IDs for all resources:");
    System.out.println("\nDatacenter IDs: " + datacenterIds);
    System.out.println("Host IDs: " + hostIds);
    System.out.println("VM IDs: " + vmIds);
    System.out.println("Cloudlet IDs: " + cloudletIds +"\n\n");
    
}

private static final int EARTH_RADIUS = 6371; // Radius in kilometers

public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS * c;
}
}
