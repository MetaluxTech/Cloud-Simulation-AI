package simulation_1;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
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
import Security_Manager.Encryption;
import Security_Manager.Security;

import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

import tools.AI;
import tools.DCs_Caculations;
import tools.FileManager;
import tools.Utils;
import tools.VMS_Caculations;

public class ElementsCreation {
	public static List<CustomCloudlet> createCloudlets(int numCloudlets, CustomBroker broker) {
		List<CustomCloudlet> tasksList = new ArrayList<>();
		int task_id, task_size, task_out_size, task_length;
		double taskLatit, taskLong;
		for (int i = 1; i <= numCloudlets; i++) {


			if (Simulator.generate_new_requests) {
				task_id = i;
				task_size = Utils.getNextRandom(5, 50);
				task_out_size = Utils.getNextRandom(5, 10);
				task_length = Utils.getNextRandom(5, 50);
				taskLatit = Utils.generateRandomLatLon()[0];
				taskLong = Utils.generateRandomLatLon()[1];
			} else {
				String dataset_path = Paths.get("").toAbsolutePath().getParent().resolve("AI_code/dataset/global_dataset.csv").toString();	
				String[] rowData = FileManager.LoadCloudletsSpesification(dataset_path, i);

				task_id = Integer.parseInt(rowData[0]); // task id
				task_size = Integer.parseInt(rowData[1]);
				task_out_size = Integer.parseInt(rowData[2]); // TaskOutputFileSize
				task_length = Integer.parseInt(rowData[3]); // TaskFileLength
				taskLatit = Double.parseDouble(rowData[6]); // UserLatitude
				taskLong = Double.parseDouble(rowData[7]); // UserLongitude
				
			}
			CustomCloudlet task = new CustomCloudlet(task_id, task_length, 1, task_size, task_out_size,
					new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull(), taskLatit,
					taskLong);
			try {
				task.setCloudletStatus(9); // Set your custom status
			} catch (Exception e) {
				e.printStackTrace();
			}

			task.setUserId(broker.getId());
			tasksList.add(task); // Add the task to the list
		}

		return tasksList; // Return the list of tasks
	}


	public static List<CustomVM> createVms(int numVMs, CustomBroker broker1, boolean use_randome_values) {
		List<CustomVM> vmsList = new ArrayList<>();
		double vm_load = 0.0, vm_bwcost, vm_memcost, vm_storagecost, vm_processcost;
		int vm_mips, vm_ram, vm_bandwidth;
		long vm_storage;
		for (int i = 1; i <= numVMs; i++) {
			if (use_randome_values) {
				vm_mips = Utils.getNextRandom(100, 250); /// instructions per second
				vm_storage = Utils.getNextRandom(64, 256);
				vm_ram = Utils.getNextRandom(8, 32);
				vm_bandwidth = Utils.getNextRandom(10, 100);
				vm_bwcost = Utils.getNextRandom(5, 10);
				vm_memcost = Utils.getNextRandom(5, 10);
				vm_storagecost = Utils.getNextRandom(5, 10);
				vm_processcost = Utils.getNextRandom(5, 10);
			} else {
				vm_mips = 3 + (i * 2); /// instructions per second
				vm_storage = 100 + (i * 2);
				vm_ram = 16 + (i * 2);
				vm_bandwidth = 60 + (i * 2);

				vm_bwcost = 4 + (i * 2);
				vm_memcost = 2 + (i * 2);
				vm_storagecost = 3 + (i * 2);
				vm_processcost = 5 + (i * 2);
			}

			int vm_pesNum = 1; // num of cpus in the VM
			String vm_monitor = "xen";
			CloudletScheduler space_shared = new CloudletSchedulerSpaceShared();
			new CloudletSchedulerTimeShared();
			CustomVM v = new CustomVM(i, broker1.getId(), vm_mips, vm_pesNum, vm_ram, vm_bandwidth, vm_storage,
					vm_monitor, space_shared, vm_load, vm_memcost, vm_storagecost, vm_bwcost, vm_processcost);
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
			double DcLatit, DcLongt, costPerBw, costPerMem, costPerStorage, costPerCpu;
			int hostMips, hostRam;
			long hostStorage, hostBw;

			if (use_randome_values) {

				DcLatit = Utils.generateRandomLatLon()[0];
				DcLongt = Utils.generateRandomLatLon()[1];
				hostMips = Utils.getNextRandom(5000, 15000); // miiliion instruction per sec
				hostRam = Utils.getNextRandom(128, 256); // host memory (MB)
				hostStorage = Utils.getNextRandom(1024, 64000);
				hostBw = Utils.getNextRandom(250, 1000);
				costPerCpu = Utils.getNextRandom(40, 60);// the cost of using processing in this resource
				costPerMem = Utils.getNextRandom(12, 24);// the cost of using processing in this resource
				costPerStorage = Utils.getNextRandom(15, 30);// the cost of using processing in this resource
				costPerBw = Utils.getNextRandom(16, 30);// the cost of using processing in this resource

			} else {

				DcLatit = 67.22677605837688 + (i * 4);
				DcLongt = 76.5628888295758 + (i * 4);
				hostMips = 8000 + (i * 4);
				hostRam = 164 + (i * 4);
				hostStorage = 50000 + (i * 4);
				hostBw = 800 + (i * 4);
				costPerCpu = 40 + (i * 2); // the cost of using processing in this resource
				costPerMem = 5 + (i * 2); // the cost of using memory in this resource
				costPerStorage = 7 + (i * 2); // the cost of using storage in this// resource
				costPerBw = 10 + (i * 2); // the cost of using bw in this resource

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
			DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList,
					time_zone, costPerCpu, costPerMem, costPerStorage, costPerBw);

			hostList.add(host1);
			VmAllocationPolicy policy = new VmAllocationPolicySimple(hostList);

			try {
				CustomDataCenter d = new CustomDataCenter(name, characteristics, policy, storageList, 0, DcLatit,
						DcLongt, 0.0);
				datacentersList.add(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return datacentersList;
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

}
