package simulation_1;


import java.util.ArrayList;
import java.util.Calendar;
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
import Costums.simulation_functions;
import Costums.tests;
import Costums.utils;
public class Simulator {
	
	
	private static List<GeoDatacenter> geoDataCentersList;
	private static List<Host> hosts_list;
	private static List<Vm> vms_List;
	private static List<GeoCloudlet> tasks_List;
	
    public static void main(String[] args) {		
        try {
        	vms_List = new ArrayList<Vm>();
        	tasks_List = new ArrayList<GeoCloudlet>();
        	geoDataCentersList = new ArrayList<GeoDatacenter>();
    		hosts_list = new ArrayList<Host>();   	
    		
            int numUsers =		 	1;
            int numDatacenters=		5;
            int numVMs=				5;
            int numCloudlets=		40;
            
        
            Log.printLine("      Starting CloudSimulation   ");

            Calendar clndr = Calendar.getInstance();
            boolean trace_actions = false;
            CloudSim.init(numUsers,clndr,trace_actions);
            
            //create broker
            DatacenterBroker   broker1=simulation_functions.createBroker("broker1");          

            //create datacenters and thier hosts
            
			double DcLatit=tests.generateRandomLatLon()[0];
			double DcLongt=tests.generateRandomLatLon()[1];
			int hostMips = 1000000; //miiliion instruction per sec
			int hostRam = 2048;         //host memory (MB)
			long hostStorage = 1000000; //host storage in ( GB )
			int hostBw = 10000;  //MBPs
			
			for (int i=1;i<=numDatacenters;i++)
			{	
			GeoDatacenter dc=	simulation_functions.createDatacenter( "DC_"+Integer.toString(i),i, hostStorage, hostMips, hostRam, hostBw,DcLatit,DcLongt);
			geoDataCentersList.add(dc);
			hosts_list.add(dc.getHostList().get(0));
			}
			
          
            int vm_mips=1000; /// instructions per second 
            long vm_size=10000;
            int vm_ram=512;
            int vm_bandwidth=1000;
            int vm_pesNum=1 ;  //num of cpus in the VM
            String vm_monitor="xen"; 
            Random rand1 = new Random();
            for (int i=1;i<=numVMs;i++)
			{   
        	Vm vm=new Vm (i, broker1.getId(), vm_mips , vm_pesNum, vm_ram, vm_bandwidth, vm_size , vm_monitor , new CloudletSchedulerSpaceShared());
        	vms_List.add(vm);
            }

            // create Cloudlets 
            int task_size = 300;
            int task_out_size=300;
             
            int task_pesNum=1 ;  //num of cpus in the VM
            UtilizationModel full_utl_model=new UtilizationModelFull();
            
            for (int i=1;i<=numCloudlets;i++)
			{ 
            	 double taskLatit=tests.generateRandomLatLon()[0];
                 double taskLong=tests.generateRandomLatLon()[1];
            	int task_length = rand1.nextInt(5000) + 500;
            	GeoCloudlet task= new GeoCloudlet(i, task_length, task_pesNum, task_size, task_out_size, full_utl_model, full_utl_model, full_utl_model, taskLatit, taskLong);
                task.setUserId(broker1.getId());
            tasks_List.add(task);
			}
            
            
            // submit Vms and cloudlets via broker
            broker1.submitVmList(vms_List);
            broker1.submitCloudletList(tasks_List); 
            
            //start simulation
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            //get all cloudlet events
            List<GeoCloudlet> geoClouletsList = broker1.getCloudletReceivedList();
            
             //print the simulation events
            utils.DisplaySimulationEvents(geoClouletsList,geoDataCentersList);
           
            
            //save the simulation reults
             String txtFilePath="";
             String datasetFilePath="";
             
             txtFilePath=IO.SaveSimulationEvents(geoClouletsList,geoDataCentersList);
             datasetFilePath=IO.SaveEventsToCSV(geoClouletsList);
             
             Log.printLine("\n events saved successfully to text file path : "+txtFilePath);
             Log.printLine("\n events saved successfully to CSV file path : "+datasetFilePath);
             
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      
    }
}




