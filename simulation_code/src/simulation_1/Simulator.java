package simulation_1;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.core.CloudSim;

import Costums.GeoCloudlet;
import Costums.GeoDatacenter;
import Costums.MyBroker;
import Costums.MyVm;
import tools.Excel;
import tools.Statistics;
import tools.Results;
import server.API;
import tools.Tools;
public class Simulator {
	private static List<GeoDatacenter> geoDataCentersList;
	private static List<Host> hosts_list;
	public static List<Double> dcs_load;
	public static List<String> string_dcsOF;
	public static List<String> string_dc_Load;
	
	private static List<MyVm> vms_List;
	private static List<MyVm> targetVms;
	private static List<GeoCloudlet> tasks_List;

    public static void main(String[] args) {	
    	
        try {
        	
        	Log.printLine("hi");
            vms_List = new ArrayList<MyVm>();
        	targetVms = new ArrayList<MyVm>();
        	tasks_List = new ArrayList<GeoCloudlet>();
        	geoDataCentersList = new ArrayList<GeoDatacenter>();
    		hosts_list = new ArrayList<Host>();   	
    		dcs_load =new ArrayList<Double>();
    		
        
            int numUsers =		 	1;
            int numDatacenters=		3;
            int numVMs=				9;
            int numCloudlets=		2000;

            Calendar clndr = Calendar.getInstance();
            boolean trace_actions = false;
            CloudSim.init(numUsers,clndr,trace_actions);
            
            MyBroker   broker1=new MyBroker("broker1");          
			createDataCenters(numDatacenters);
            createVms(numVMs, broker1);
        	for (int i=1;i<=numCloudlets;i++)
    		{ 	
    				int task_size = 					Tools.getNextRandom(10, 100);
    				int task_out_size=					Tools.getNextRandom(10, 20);
    				int task_length = 					Tools.getNextRandom(10, 25);	
    				double taskLatit=					Tools.generateRandomLatLon()[0];
    				double taskLong=					Tools.generateRandomLatLon()[1];				
    				UtilizationModel full_utl_model=	new UtilizationModelFull();
    				int task_pesNum=					1 ; 
    				
    				GeoCloudlet task= new GeoCloudlet(i, task_length, task_pesNum, task_size, task_out_size, full_utl_model, full_utl_model, full_utl_model, taskLatit, taskLong);
    				task.setUserId(broker1.getId());							
    				tasks_List.add(task);
    				
    				
    				GeoDatacenter best_dc =Statistics.getBestDataCenter(task, geoDataCentersList, vms_List);
    				targetVms=Tools.extractDataCenterVms(vms_List, best_dc.getId());
    				MyVm bestVm=Tools.getVmWithLowestLoad(targetVms);
    				bestVm.setLoad(bestVm.getLoad()+task_length/10);
    				best_dc.setLoad(best_dc.getLoad()+ task_length/10);
    				dcs_load.add( best_dc.getLoad());
    				
    				task.setVmId(bestVm.getId());//here is the best vm is lowest load on the vm
    				
    		}
            broker1.submitVmList(vms_List);
            broker1.submitCloudletList(tasks_List); 

            CloudSim.startSimulation();
            
             CloudSim.stopSimulation();
           
            simulation_functions.DisplaySimulationEvents(broker1.getCloudletReceivedList(),geoDataCentersList);
             simulation_functions.printOFunctions(Statistics.DCsOFunctions);  
             String path=Excel.SaveResourcesToExcel("a.csv",tasks_List,geoDataCentersList,vms_List);
             print("path: "+path);
             // Save summary results to Excel
           
            } catch (Exception e) {
            e.printStackTrace();
        }
      
    }



	private static void printTaskDetails(int taskid) {
		print("last task: "+ tasks_List.get(taskid).getCloudletId() );
		print("last task getWaitingTime: "+ tasks_List.get(taskid).getWaitingTime() );
		print("last task getSubmissionTime: "+ tasks_List.get(taskid).getSubmissionTime() );
		print("last task getExecStartTime: "+ tasks_List.get(taskid).getExecStartTime() );
		print("last task getFinishTime: "+ tasks_List.get(taskid).getFinishTime() );
		print("last task getActualCPUTime: "+ tasks_List.get(taskid).getActualCPUTime() );
		print("last task getWallClockTime: "+ tasks_List.get(taskid).getWallClockTime() );
		print("last task getCloudletHistory : "+ tasks_List.get(taskid).getCloudletHistory() );
		print("last task getCloudletStatus: "+ tasks_List.get(taskid).getCloudletStatus() );
		print("last task getCloudletStatusString: "+ tasks_List.get(taskid).getCloudletStatusString() );
		}

    
    
    /* 
 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 */
    

	private static void createDataCenters(int numDatacenters) {
		for (int i=1;i<=numDatacenters;i++)
		{	
			GeoDatacenter dc=	simulation_functions.createDatacenter( "DC_"+Integer.toString(i),i);
			geoDataCentersList.add(dc);	
		
			hosts_list.add(dc.getHostList().get(0));
			
		}
	}
	public static  void  print(String message) {
		Log.printLine(message);
	}
	private static void createVms(int numVMs, MyBroker broker1) {
		for (int i=1;i<=numVMs;i++)
		{   
			  int vm_mips=			Tools.getNextRandom(100, 250); /// instructions per second 
		      long vm_storage=		Tools.getNextRandom(64, 256);
		      int vm_ram=			Tools.getNextRandom(8, 32);
		      int vm_bandwidth=		Tools.getNextRandom(10, 100);
		      int vm_pesNum=1 ;  //num of cpus in the VM
		      String vm_monitor="xen";
		      CloudletScheduler space_shared=new CloudletSchedulerSpaceShared();
		      CloudletScheduler time_shared=new CloudletSchedulerTimeShared();
		      
		      MyVm vm=new MyVm (i, broker1.getId(), vm_mips , vm_pesNum, vm_ram, vm_bandwidth, vm_storage , vm_monitor ,space_shared, 0.0);
		      vms_List.add(vm);
		     
//	              
		}
	}
}




