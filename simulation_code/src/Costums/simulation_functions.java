package Costums;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class simulation_functions {

	
	  public static GeoDatacenter createDatacenter( String name, int hostId, Long hostStorage, int hostMips, int hostRam, int hostBw,double  DcLatit,double DcLongt) {
		  
			List<Host> hostList = new ArrayList<Host>();
			List<Pe> peList = new ArrayList<Pe>();
			LinkedList<Storage> storageList = new LinkedList<Storage>(); 
			
			
			peList.add(new Pe(0, new PeProvisionerSimple(hostMips))); // need to store Pe id and MIPS Rating
			
			Host host1=new Host(hostId,new RamProvisionerSimple(hostRam),new BwProvisionerSimple(hostBw),hostStorage,peList,new VmSchedulerSpaceShared (peList));
			hostList.add(host1);
//			cost of bandwidth , the cost of storage, the cost of memory, and the processing per second
			String arch = "x86"; // system architecture
			String os = "Linux"; // operating system
			String vmm = "Xen";
			double time_zone = 10.0; // time zone this resource located
			double cost = 1; // the cost of using processing in this resource
			double costPerMem = 0.1; // the cost of using memory in this resource
			double costPerStorage = 0.1; // the cost of using storage in this // resource
			double costPerBw =0.1; // the cost of using bw in this resource
			// we are not adding SAN
														// devices by now

			DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
					arch, os, vmm, hostList, time_zone, cost, costPerMem,
					costPerStorage, costPerBw);

			// 6. Finally, we need to create a PowerDatacenter object.
			GeoDatacenter datacenter = null;
//			CustomVmPolicy vmpolicy1 = new CustomVmPolicy(hostList);
		      	
			try {
				datacenter = new GeoDatacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0,DcLatit,DcLongt);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return datacenter;
		}
public static DatacenterBroker createBroker(String broker_name) {
	
	
	DatacenterBroker broker = null;
	try {
		broker = new DatacenterBroker(broker_name);
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	return broker;
	
	
}
public static Datacenter createVM(String vm_name) {
	
	
	return null;
	
	
}
public static Datacenter createColudLet(String cloudlet_name) {
	
	
	return null;
	
	
}
}
