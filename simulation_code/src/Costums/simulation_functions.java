package Costums;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class simulation_functions {

	
	  public static GeoDatacenter createDatacenter( String name, int hostId) {
		  
			List<Host> hostList = new ArrayList<Host>();
			List<Pe> peList = new ArrayList<Pe>();
			LinkedList<Storage> storageList = new LinkedList<Storage>(); 
			
			
			double DcLatit=			tests.generateRandomLatLon()[0];
			double DcLongt=			tests.generateRandomLatLon()[1];
			int hostMips =			tests.getNextRandom(5000, 15000); //miiliion instruction per sec
			int hostRam = 			tests.getNextRandom(128, 256);         //host memory (MB)
			long hostStorage = 		tests.getNextRandom(1024, 64000);; //host storage in ( GB )
			int hostBw = 			tests.getNextRandom(250, 1000);;  //MBPs
			
			peList.add(new Pe(0, new PeProvisionerSimple(hostMips))); // need to store Pe id and MIPS Rating
			VmScheduler schudler=			new VmSchedulerTimeShared(peList);
			
			
			Host host1=new Host(hostId,new RamProvisionerSimple(hostRam),new BwProvisionerSimple(hostBw),hostStorage,peList,schudler);            
			
			
			
			String arch =			 	"x86"; // system architecture
			String os = 				"Linux"; // operating system
			String vmm = 				"Xen";
			double time_zone = 			10.0; // time zone this resource located
			double cost = 				tests.getNextRandom(10, 50);; // the cost of using processing in this resource
			double costPerMem = 		tests.getNextRandom(1, 10);; // the cost of using memory in this resource
			double costPerStorage = 	tests.getNextRandom(1, 10);; // the cost of using storage in this// resource
			double costPerBw =			tests.getNextRandom(1, 10);; // the cost of using bw in this resource
			DatacenterCharacteristics characteristics = new DatacenterCharacteristics( arch, os, vmm, hostList, time_zone, cost, costPerMem,costPerStorage, costPerBw);			
			
			
			hostList.add(host1);
			VmAllocationPolicy policy=new MyVmPolicy(hostList);
			
			
			
			GeoDatacenter datacenter = null;      	
			try {
				datacenter = new GeoDatacenter(name, characteristics, policy, storageList, 0,DcLatit,DcLongt,0.0);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return datacenter;
		}

}
