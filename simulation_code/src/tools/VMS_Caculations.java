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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class VMS_Caculations {
	
	 public static double calculateTaskExcutionTime(CustomVM vm,CustomCloudlet cloudlet,CustomDataCenter dataCenter)
	 {
		   double task_lenth = cloudlet.getCloudletLength();
		    double process_speed=vm.getMips();
		    double task_executioin_time = task_lenth / process_speed;
		    
		    return Math.round(task_executioin_time * 100.0) / 100.0; 
	 }
	 public static double calculateTaskExceutionCost(CustomCloudlet cloudlet,CustomDataCenter dataCenter) // cost execution task
  	{
//			double cpuCost = dataCenter.getPublicCharacteristics().getCostPerMi();
//	        double ramCost = dataCenter.getPublicCharacteristics().getCostPerMem();
//	        double storageCost = dataCenter.getPublicCharacteristics().getCostPerStorage();
//	        double bandwidthCost = dataCenter.getPublicCharacteristics().getCostPerBw();
//	       
		    double exe = cloudlet.getActualCPUTime();
		    double r = cloudlet.getCloudletFileSize();
		    double st = cloudlet.getCloudletOutputSize();
		    double f = cloudlet.getCloudletFileSize();
		    double cet = (exe  ) + (r  ) + (st ) + (f );
		    	cet=cet/100;
		    return Math.round(cet * 100.0) / 100.0;
		    
		    //8000
		}
 	 
	 public static double calculateDataTransfareTime(CustomCloudlet cloudlet, CustomVM vm)
	 {
		 double task_lenth = cloudlet.getCloudletLength();
		 double task_out_size = cloudlet.getCloudletOutputSize();
		    double bw=vm.getBw();
		    double delayN = (task_lenth+task_out_size) /bw;
		    return Math.round(delayN * 100.0) / 100.0;
	 }
	 public static double calculateDataTransfareCost(CustomCloudlet cloudlet,CustomDataCenter dataCenter, CustomVM vm)
	 {
		 double dtt=calculateDataTransfareTime(cloudlet,vm);
		 double band_cost = dataCenter.getPublicCharacteristics().getCostPerBw();
		 double datatrnsfarecost=dtt*band_cost;
		 return datatrnsfarecost;
	       
	 }
	 public static double calculateHostCapacity(List<CustomCloudlet> host_tasks,CustomDataCenter dataCenter)
	 {
//		 edit to be near to cloudlet capacity
		 	return 5 ;
		 
	 }
	 
	 public static double FindRank(CustomVM vm,CustomCloudlet cloudlet, double tet, double tec, double dtt, double dtc, double hc)
	 {
		  // Create an array of the values
		  double[] values = {tet, tec, dtt, dtc, hc};
		  Arrays.sort(values);
		  double f1 = values[0];
		  double f2 = tet + tec + dtt + dtc + hc;
		  double rank = (f1 * 0.5 + f2 * 0.5);
		  Log.printLine(" task#"+cloudlet.getCloudletId()+" VM#"+vm.getId()+"  f1: "+f1+"    f2: "+f2 +"    RANK: "+rank);

		  return rank;

		}


	public static CustomVM getBestVMByRank(CustomCloudlet cloudlet, List<CustomDataCenter> data_centers_list,List<CustomVM> vms_list) 
	{
		List<CustomVM>bestVmsList=new ArrayList<CustomVM>();
		List<Double>Ranks_Array=new ArrayList<Double>();
	    
		double MAXRANK = 99999999999999.0;
	    CustomVM best_vm=null;
	    for (CustomVM vm : vms_list) {
	    	int dc_id=Utils.findDatacenterIdForVm(vm.getId());
	    	CustomDataCenter dc=Utils.getDatacenterById(dc_id, data_centers_list);
	    	
	    	double tet = calculateTaskExcutionTime(vm,cloudlet, dc);
	    	double tec = calculateTaskExceutionCost(cloudlet, dc);
	    	double dtt = calculateDataTransfareTime(cloudlet, vm);
	    	double dtc = calculateDataTransfareCost(cloudlet,dc, vm);
	    	double hc = calculateHostCapacity(null,dc);
	        double RANK = FindRank(vm,cloudlet,tet, tec, dtt,dtc,hc);
	        bestVmsList.add(vm);
	        Ranks_Array.add(RANK);
	        
	            }
	    int indexOfSmallestRank = IntStream.range(0, Ranks_Array.size())
	    	    .boxed()
	    	    .min(Comparator.comparing(i -> Ranks_Array.get(i)))
	    	    .orElse(-1);

		best_vm=bestVmsList.get(indexOfSmallestRank);
//		Log.printLine("VMS Ranks: "+Ranks_Array);

		
		return best_vm;
	        }
	    
	    
		
	
	
}


