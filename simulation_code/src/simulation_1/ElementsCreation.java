package simulation_1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import Costums_elements.CustomBroker;
import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import Costums_elements.CustomVM;

import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

import tools.AI;
import tools.DCs_Caculations;
import tools.FileManager;
import tools.Utils;
import tools.VMS_Caculations;

public class ElementsCreation {
	public static List<CustomCloudlet> createCloudlets(int numCloudlets, CustomBroker broker, String modelName, List<CustomDataCenter> datacentersList, List<CustomVM> vmsList, boolean use_random_values) {
		// Initialize tasksList with an empty ArrayList
		List<CustomCloudlet> tasksList = new ArrayList<>();
		int task_id,task_size, task_out_size, task_length;
		double taskLatit, taskLong;
		CustomDataCenter best_dc=null;
		CustomVM best_vm=null;
		List <CustomVM> datacenterVms=new ArrayList<CustomVM>();
		for (int i = 1; i <= numCloudlets; i++) {
			
			if (use_random_values) {
				task_id=i;
				task_size = Utils.getNextRandom(10, 100);
				task_out_size = Utils.getNextRandom(10, 20);
				task_length = Utils.getNextRandom(10, 100);
				taskLatit = Utils.generateRandomLatLon()[0];
				taskLong = Utils.generateRandomLatLon()[1];
			} else {
				String[] rowData=FileManager.LoadCloudletsSpesification("cloudlets_specifications_2000.csv", i);
				//shape the	row of data	[TaskID,StartTime,TaskFileSize,TaskOutputFileSize,TaskFileLength,UserLatitude,UserLongitude];
  				task_id=Integer.parseInt(rowData[0]);       //task id
  				task_size = Integer.parseInt(rowData[2]); // TaskFileSize
  				task_out_size = Integer.parseInt(rowData[3]); // TaskOutputFileSize
        	     task_length = Integer.parseInt(rowData[4]); // TaskFileLength
        	     taskLatit = Double.parseDouble(rowData[5]); // UserLatitude
        	     taskLong = Double.parseDouble(rowData[6]); // UserLongitude

			}
			UtilizationModel full_utl_model = new UtilizationModelFull();
			int task_pesNum = 1;

			CustomCloudlet task = new CustomCloudlet(task_id, task_length, task_pesNum, task_size, task_out_size, full_utl_model,
					full_utl_model, full_utl_model, taskLatit, taskLong);
			task.setUserId(broker.getId());
			
			
			
			if(modelName=="FUNCTIONS"){
				best_dc =DCs_Caculations.getBestDataCenterByFunctions(task, datacentersList, vmsList);
}
				else if (modelName=="NONE"){
				best_dc=Utils.getDatacenterById(Utils.getNextRandom(3, datacentersList.size()+2), datacentersList);
				}
				else if (modelName=="GA"){
					best_dc =AI.PredictBestDataCenter(task,datacentersList,"GA");
				}
				else if (modelName=="SNAKE"){
					best_dc =AI.PredictBestDataCenter(task,datacentersList,"SNAKE");
				}
				else if (modelName=="New_Model"){
					best_dc =AI.PredictBestDataCenter(task,datacentersList,"New_Model");
				}
				else {
					best_dc= null;
				}
			
			
			datacenterVms=Utils.extractDataCenterVms(vmsList,best_dc.getId());
//			best_vm=VMS_Caculations.getBestVMByRank(task, datacentersList, datacenterVms);
			best_vm=Utils.getVmWithLowestLoad(datacenterVms);
			task.setVmId(best_vm.getId());
			
			best_vm.setLoad(best_vm.getLoad()+task_length/10);
			best_dc.setLoad(best_dc.getLoad()+ task_length/10);
			
			
			tasksList.add(task);
			
		}
		return tasksList;
	}

	public static CustomBroker createBroker(String broker_name) {
		try {
			CustomBroker b = new CustomBroker(broker_name);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static List<CustomVM> createVms(int numVMs, CustomBroker broker1, boolean use_randome_values) {
		List<CustomVM> vmsList = new ArrayList<>();
		int vm_mips, vm_ram, vm_bandwidth;
		long vm_storage;
		for (int i = 1; i <= numVMs; i++) {
			if (use_randome_values) {
				vm_mips = Utils.getNextRandom(100, 250); /// instructions per second
				vm_storage = Utils.getNextRandom(64, 256);
				vm_ram = Utils.getNextRandom(8, 32);
				vm_bandwidth = Utils.getNextRandom(10, 100);
			} else {
				vm_mips = 231; /// instructions per second
				vm_storage = 100;
				vm_ram = 16;
				vm_bandwidth = 60;
			}

			int vm_pesNum = 1; // num of cpus in the VM
			String vm_monitor = "xen";
			CloudletScheduler space_shared = new CloudletSchedulerSpaceShared();
			CloudletScheduler time_shared = new CloudletSchedulerTimeShared();
			CustomVM v = new CustomVM(i, broker1.getId(), vm_mips, vm_pesNum, vm_ram, vm_bandwidth, vm_storage, vm_monitor,
					space_shared, 0.0);
			vmsList.add(v);

		}
		return vmsList;
	}

	public static List<CustomDataCenter> createDatacenters(int numDataCenters, boolean use_randome_values) {
		int hostId;
		String name;
		List<CustomDataCenter> datacentersList = new ArrayList<>();
		for (int i = 1; i <= numDataCenters; i++)

		{
			name = "DC_" + Integer.toString(i);
			hostId = i;

//			inite host variables
			List<Host> hostList = new ArrayList<Host>();
			List<Pe> peList = new ArrayList<Pe>();
			LinkedList<Storage> storageList = new LinkedList<Storage>();
			double DcLatit, DcLongt;
			int hostMips, hostRam;
			long hostStorage, hostBw;

			if (use_randome_values) {

				DcLatit = Utils.generateRandomLatLon()[0];
				DcLongt = Utils.generateRandomLatLon()[1];
				hostMips = Utils.getNextRandom(5000, 15000); // miiliion instruction per sec
				hostRam = Utils.getNextRandom(128, 256); // host memory (MB)
				hostStorage = Utils.getNextRandom(1024, 64000);
				; // host storage in ( GB )
				hostBw = Utils.getNextRandom(250, 1000);
				; // MBPs

			} else {

				DcLatit = 67.22677605837688;
				DcLongt = 76.5628888295758;
				hostMips = 8000;
				hostRam = 164;
				hostStorage = 50000;
				hostBw = 800;

			}

//	  		create host
			peList.add(new Pe(0, new PeProvisionerSimple(hostMips))); // need to store Pe id and MIPS Rating
			VmScheduler schudler = new VmSchedulerTimeShared(peList);

			Host host1 = new Host(hostId, new RamProvisionerSimple(hostRam), new BwProvisionerSimple(hostBw),
					hostStorage, peList, schudler);

//			inite datacenter variables
			String arch = "x86"; // system architecture
			String os = "Linux"; // operating system
			String vmm = "Xen";
			double time_zone = 10.0; // time zone this resource located
			double cost = 40; // the cost of using processing in this resource
			double costPerMem = 5; // the cost of using memory in this resource
			double costPerStorage = 7; // the cost of using storage in this// resource
			double costPerBw = 10; // the cost of using bw in this resource
			DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList,
					time_zone, cost, costPerMem, costPerStorage, costPerBw);

			hostList.add(host1);
			VmAllocationPolicy policy = new VmAllocationPolicySimple(hostList);

			try {
				CustomDataCenter d = new CustomDataCenter(name, characteristics, policy, storageList, 0, DcLatit, DcLongt,
						0.0);
				datacentersList.add(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return datacentersList;
	}

}
