package simulation_1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import Costums.GeoCloudlet;
import Costums.GeoDatacenter;
import Costums.IO;
import Costums.MyBroker;
import Costums.PerVm;
import Costums.perfomance;
import Costums.simulation_functions;
import Costums.tests;
import Costums.utils;
public class Simulator {

	private static List<GeoDatacenter> geoDataCentersList;
	private static List<Host> hosts_list;
	private static List<PerVm> vms_List;
	private static List<GeoCloudlet> tasks_List;
	
	
    public static void main(String[] args) {		
        try {
        	Map<GeoDatacenter, Double> DCsLoad = new HashMap<>();
        	Map<GeoDatacenter, Double> DCsObjectiveFunctions = new HashMap<>();
        	vms_List = new ArrayList<PerVm>();
        	tasks_List = new ArrayList<GeoCloudlet>();
        	geoDataCentersList = new ArrayList<GeoDatacenter>();
    		hosts_list = new ArrayList<Host>();   	
    		
            int numUsers =		 	1;
            int numDatacenters=		4;
            int numVMs=				3;
            int numCloudlets=		8;
            
        
            Log.printLine("      Starting CloudSimulation   ");

            Calendar clndr = Calendar.getInstance();
            boolean trace_actions = false;
            CloudSim.init(numUsers,clndr,trace_actions);
            
            //create broker
            MyBroker   broker1=simulation_functions.createBroker("broker1");          

            //create datacenters and thier hosts
          
			for (int i=1;i<=numDatacenters;i++)
			{	
				  
				double DcLatit=			tests.generateRandomLatLon()[0];
				double DcLongt=			tests.generateRandomLatLon()[1];
				int hostMips =			tests.getNextRandom(5000, 15000); //miiliion instruction per sec
				int hostRam = 			tests.getNextRandom(128, 256);         //host memory (MB)
				long hostStorage = 		tests.getNextRandom(1024, 64000);; //host storage in ( GB )
				int hostBw = 			tests.getNextRandom(250, 1000);;  //MBPs
			
				
				GeoDatacenter dc=	simulation_functions.createDatacenter( "DC_"+Integer.toString(i),i, hostStorage, hostMips, hostRam, hostBw,DcLatit,DcLongt);
				geoDataCentersList.add(dc);
				DCsLoad.put(dc, 0.0);
				DCsObjectiveFunctions.put(dc, 0.0);
				hosts_list.add(dc.getHostList().get(0));
				
			}

            for (int i=1;i<=numVMs;i++)
			{   
	        	  int vm_mips=			tests.getNextRandom(100, 250); /// instructions per second 
	              long vm_storage=		tests.getNextRandom(64, 256);
	              int vm_ram=			tests.getNextRandom(16, 64);
	              int vm_bandwidth=		tests.getNextRandom(10, 100);
	              int vm_pesNum=1 ;  //num of cpus in the VM
	              String vm_monitor="xen"; 
	              
	              PerVm vm=new PerVm (i, broker1.getId(), vm_mips , vm_pesNum, vm_ram, vm_bandwidth, vm_storage , vm_monitor , new CloudletSchedulerSpaceShared(), 0.0);
	              System.out.println("VM MIPS: " + vm_mips);

	              // After creating each host
	              Log.printLine("VM MIPS: " + vm_mips);
	              vms_List.add(vm);
            }
            
            broker1.submitVmList(vms_List);
            
            Log.printLine("Number of VMs created: " + vms_List.size());
            
            // After submitting VMs to the broker
            Log.printLine("Number of VMs submitted to the broker: " + broker1.getVmList().size());
            Log.printLine("first dc vmslit"+geoDataCentersList.get(0).getVmList());
            // create Cloudlets 
            
            for (int i=1;i<=numCloudlets;i++)
			{ 	
            		int task_size = 		tests.getNextRandom(10, 100);
					int task_out_size=		tests.getNextRandom(10, 100);
					int task_length = 		tests.getNextRandom(10, 100);	
					int task_pesNum=1 ;  //num of cpus in the VM
					UtilizationModel full_utl_model=new UtilizationModelFull();
					double taskLatit=tests.generateRandomLatLon()[0];
					double taskLong=tests.generateRandomLatLon()[1];				
					
					GeoCloudlet task= new GeoCloudlet(i, task_length, task_pesNum, task_size, task_out_size, full_utl_model, full_utl_model, full_utl_model, taskLatit, taskLong);
					task.setUserId(broker1.getId());							
					GeoDatacenter bestDataCenter=perfomance.getBestDataCenter(task, geoDataCentersList);
					Log.printLine("best data Center: "+bestDataCenter);
					
					//PerVm bestVm=tests.getVmWithLowestLoad(vms_List);
					//bestVm.setLoad(bestVm.getLoad()+task.getCloudletLength());
					//task.setVmId(bestVm.getId());//here is the best vm is lowest load on the vm
					//Log.printLine("lowest vm load :"+bestVm.getId() + "load :"+bestVm.getLoad());					
					tasks_List.add(task);
			}
            
           

            // submit Vms and cloudlets via broker
            broker1.submitCloudletList(tasks_List); 
            //start simulation
            CloudSim.startSimulation();
            Log.printLine("\n events saved successfully ");
            CloudSim.stopSimulation();

            //get all cloudlet events
            List<GeoCloudlet> geoClouletsList = broker1.getCloudletReceivedList();
            
             //print the simulation events
            utils.DisplaySimulationEvents(geoClouletsList,geoDataCentersList);
            utils.printResourcesList(geoDataCentersList, hosts_list, vms_List, tasks_List);
             //save the simulation reults
             String txtFilePath="null";
             String eventsCSVtFilePath="null";
             String resourcesCSVFilePath="null";
             
             
//             txtFilePath=IO.SaveSimulationEvents(geoClouletsList,geoDataCentersList);
//             eventsCSVtFilePath=IO.SaveEventsToCSV(geoClouletsList);
             	resourcesCSVFilePath=IO.SaveResourcesToCSV(geoClouletsList,geoDataCentersList,vms_List);
//             
//             Log.printLine("\n events saved successfully to text file path : "+txtFilePath);
//             Log.printLine("\n events saved successfully to CSV file path : "+eventsCSVtFilePath);
             Log.printLine("\n events saved successfully to CSV file path : "+resourcesCSVFilePath);
//             
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      
    }
}




