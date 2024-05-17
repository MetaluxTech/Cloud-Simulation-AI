package tools;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import Costums_elements.CustomVM;
import simulation_1.Simulator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class VMS_Caculations {
	
	 public static double calculateTaskExcutionTime(CustomVM vm,CustomCloudlet cloudlet,CustomDataCenter dataCenter)
	 {
		   	double task_lenth = cloudlet.getCloudletLength();
		    double cpu_speed=vm.getMips();
		    double task_executioin_time = task_lenth / cpu_speed;
		    return Math.round(task_executioin_time * 100.0) / 100.0; 
	 }
	 public static double calculateTaskExceutionCost(CustomVM vm,CustomCloudlet cloudlet,CustomDataCenter dataCenter) // cost execution task
  	{
			double cpuCost = vm.getcpuCost();
	        double ramCost = vm.getramCost();
	        double storageCost = vm.getStorageCost();
	        double bandwidthCost = vm.getBwCost();

		    double exe = cloudlet.getActualCPUTime();
		    double r = cloudlet.getCloudletFileSize();
		    double st = cloudlet.getCloudletOutputSize();
		    double f = cloudlet.getCloudletFileSize();
		    double cet = (exe  * cpuCost) + (r  * ramCost) + (st * storageCost ) + (f *bandwidthCost );
		    return Math.round(cet/1000 * 100.0) / 100.0;
		    
		}
 	 
	 public static double calculateDataTransfareTime(CustomCloudlet cloudlet, CustomVM vm)
	 {
		 double task_lenth = cloudlet.getCloudletLength();
		 double task_out_size = cloudlet.getCloudletOutputSize();
		    double bw=vm.getBw();
		    double delayN = (task_lenth+task_out_size) /bw;
		    return Math.round(delayN/10 * 100.0) / 100.0;
	 }
	 public static double calculateDataTransfareCost(CustomCloudlet cloudlet,CustomDataCenter dataCenter, CustomVM vm)
	 {
		 double dtt=calculateDataTransfareTime(cloudlet,vm);
		 double band_cost = vm.getBwCost();
		 double datatrnsfarecost=dtt*band_cost;
		 return Math.round(datatrnsfarecost * 100.0) / 100.0;
	       
	 }
	
	 public static double FindRank(CustomVM vm,CustomCloudlet cloudlet, double tet, double tec, double dtt, double dtc)
	 {
		  // Create an array of the values
		  double[] values = {tet, tec, dtt, dtc};
		  Arrays.sort(values);
		  double f1 = values[0];
		  double f2 =Math.round( ( tet + tec + dtt + dtc) * 100.0) / 100.0 ;
		  double rank = Math.round((f1 * 0.5 + f2 * 0.5)* 100.0) / 100.0;
		  
		  return Math.round(rank * 100.0) / 100.0;

		}


	public static int getBestVMIDByRank(CustomCloudlet cloudlet, List<CustomDataCenter> data_centers_list,List<CustomVM> vms_list) 
	{
		List<CustomVM>bestVmsList=new ArrayList<CustomVM>();
		List<Double>Ranks_Array=new ArrayList<Double>();
	    
	    CustomVM best_vm=null;
	    for (CustomVM vm : vms_list) {
	    	int dc_id=Utils.findDatacenterIdForVm(vm.getId());
	    	CustomDataCenter dc=Utils.getDatacenterById(dc_id, data_centers_list);
	    	
	    	double tet = calculateTaskExcutionTime(vm,cloudlet, dc);
	    	double tec = calculateTaskExceutionCost(vm,cloudlet, dc);
	    	double dtt = calculateDataTransfareTime(cloudlet, vm);
	    	double dtc = calculateDataTransfareCost(cloudlet,dc, vm);
	        double RANK = FindRank(vm,cloudlet,tet, tec, dtt,dtc);
	        bestVmsList.add(vm);
	        Ranks_Array.add(RANK);
	        
	            }
	    int indexOfSmallestRank = IntStream.range(0, Ranks_Array.size())
	    	    .boxed()
	    	    .min(Comparator.comparing(i -> Ranks_Array.get(i)))
	    	    .orElse(-1);

	  
		return bestVmsList.get(indexOfSmallestRank).getId();
	        }
	    
	    
		
	
	
}


