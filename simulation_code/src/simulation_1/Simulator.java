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
import tools.AI;
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

    @SuppressWarnings("unused")
	public static void main(String[] args) {	
    	
        try {
        	
        	String modelName="NONE";//GA or SNAKE or NONE or FUNCTIONS or New_Model
        	
            int numUsers =		 	1;
            int numDatacenters=		3;
            int numVMs=				9;
            int numCloudlets=		400;
            
            
            int best_dc_id=-1;
            GeoDatacenter best_dc=null;
            vms_List = new ArrayList<MyVm>();
        	targetVms = new ArrayList<MyVm>();
        	tasks_List = new ArrayList<GeoCloudlet>();
        	geoDataCentersList = new ArrayList<GeoDatacenter>();
    		hosts_list = new ArrayList<Host>();   	
    		dcs_load =new ArrayList<Double>();
    		
            Calendar clndr = Calendar.getInstance();
            boolean trace_actions = false;
            CloudSim.init(numUsers,clndr,trace_actions);
            
            MyBroker   broker1=new MyBroker("broker1");          
			createDataCenters(numDatacenters);
            createVms(numVMs, broker1);
        	for (int i=1;i<=numCloudlets;i++)
    		{ 	  
        		String[] rowData=Excel.LoadTaskData(i);
        		int task_size = Integer.parseInt(rowData[0]); // TaskFileSize
        	    int task_out_size = Integer.parseInt(rowData[1]); // TaskOutputFileSize
        	    int task_length = Integer.parseInt(rowData[2]); // TaskFileLength
        	    double taskLatit = Double.parseDouble(rowData[3]); // UserLatitude
        	    double taskLong = Double.parseDouble(rowData[4]); // UserLongitude
        	    print("task #"+i+" taskLong: "+rowData[4]);
			
    				UtilizationModel full_utl_model=	new UtilizationModelFull();
    				int task_pesNum=					1 ; 
    				
    				GeoCloudlet task= new GeoCloudlet(i, task_length, task_pesNum, task_size, task_out_size, full_utl_model, full_utl_model, full_utl_model, taskLatit, taskLong);
    				task.setUserId(broker1.getId());							
    				tasks_List.add(task);
    				
    				if(modelName=="FUNCTIONS"){
    					best_dc =Statistics.getBestDataCenter(task, geoDataCentersList, vms_List);
}
    					else if (modelName=="NONE"){
    					best_dc=Tools.getDatacenterById(Tools.getNextRandom(3, numDatacenters+2), geoDataCentersList);
    					}
    					else if (modelName=="GA"){
    						best_dc =AI.PredictBestDataCenter(task,geoDataCentersList,"GA");
    					}
    					else if (modelName=="SNAKE"){
    						best_dc =AI.PredictBestDataCenter(task,geoDataCentersList,"SNAKE");
    					}
    					else if (modelName=="New_Model"){
    						best_dc =AI.PredictBestDataCenter(task,geoDataCentersList,"New_Model");
    					}
    					else {
    						best_dc_id= -1;
    					}
    	    			
    					
        				
    				
    				MyVm bestVm=Tools.getVmWithLowestLoad(Tools.extractDataCenterVms(vms_List, best_dc.getId()));
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
             // Save summary results to Excel
            Double simulationTime=Tools.getSimulationTime(tasks_List);
           	double avgCompleteTime = Results.calculateAverageCompleteTime(tasks_List);
            double avgWaitingTime = Results.calculateWaitingTime(tasks_List);
            double avgThroughput = Results.calculateThroughput(tasks_List, simulationTime);
            double avgSLAViolation = Results.calculateSlaViolationRate(tasks_List);
            double avgNegotiationTime = Results.calculateNegotiationTime(tasks_List);

            // Save summary results to Excel
            String Excel_name="results/"+modelName+"_results_"+numCloudlets+".csv";
            if (numCloudlets==(50))
            {
            	Excel_name="results/"+modelName+"_results_050"+".csv";
            }
                
            String ResultsFilePath = Excel.SaveResultsToExcel(Excel_name,
           
            		tasks_List.size(), simulationTime, avgCompleteTime, avgWaitingTime,
                    avgThroughput, avgSLAViolation, avgNegotiationTime);
            print("number of Processed Tasks: "+numCloudlets);
           	print("Total simulation Time: "+simulationTime);
           	print("Average Complete Time: "+avgCompleteTime);
           	print("Average Waiting Time: "+avgWaitingTime);
            print("Average Throughput: "+avgThroughput);
            print("Average SLA Violation: "+avgSLAViolation);
            print("Average Negotiation Time: "+avgNegotiationTime);

            print(ResultsFilePath);
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
			
			  int vm_mips=			231; /// instructions per second 
		      long vm_storage=		100;
		      int vm_ram=			16;
		      int vm_bandwidth=		60;
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




