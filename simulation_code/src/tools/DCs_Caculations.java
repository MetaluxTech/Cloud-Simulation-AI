package tools;

import java.util.List;
import java.util.Map;


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
import java.util.Comparator;
import java.util.HashMap;

public class DCs_Caculations {
	 public static Map<CustomCloudlet,String > DCsOFunctions = new HashMap<>();
	 public static Map<Integer, List<Integer>> DCsVmsMap = Map.of(
              3, List.of(1, 2, 3,4,5 ),
              4, List.of(6,7,8,9,10),
              5, List.of(11,12,13,14,15 )
              
               );
       	
	public static double calculateCET(CustomCloudlet cloudlet,CustomDataCenter dataCenter) // cost execution task
	{
		double cpuCost = dataCenter.getPublicCharacteristics().getCostPerMi();
        double ramCost = dataCenter.getPublicCharacteristics().getCostPerMem();
        double storageCost = dataCenter.getPublicCharacteristics().getCostPerStorage();
        double bandwidthCost = dataCenter.getPublicCharacteristics().getCostPerBw();
       
	    double exe = cloudlet.getActualCPUTime();
	    double r = cloudlet.getCloudletFileSize();
	    double st = cloudlet.getCloudletOutputSize();
	    double f = cloudlet.getCloudletFileSize();
	    double cet = (exe * cpuCost) + (r * ramCost) + (st * storageCost) + (f * bandwidthCost);
	    	cet=cet/100;
	    return Math.round(cet * 100.0) / 100.0;
	    
	    //8000
	}

	public static double calculateNetworkDelay(CustomCloudlet cloudlet, CustomVM vm) {  // task delay
	    // Task length in MI
		
	    double lt = cloudlet.getCloudletLength();
	    double bw=vm.getBw();
	    double delayN = lt /bw;
	    delayN=delayN*1.5;
	    return Math.round(delayN * 100.0) / 100.0;
	    //2
	}
	
	public static double calculateLatency(CustomCloudlet cloudlet, CustomDataCenter dataCenter) {  // task latency
		
		double n=5;// propogation time km per s
		double distance=Utils.calculateDistance(cloudlet, dataCenter);
		double latency=distance*n*2+cloudlet.getActualCPUTime();// Propagation time +processing time
		latency= Math.round(latency * 100.0) / 100.0;
		return latency/10000;
		
		//15
	}

	public static double calculateObjectiveFunction(double cet, double networkDelay, double dcLoad,double latency) {
		//50      of=0.4*8000/1000+0.3*5+0.7*15+distance delay
	        double objectiveFunction = 0.4 * cet + 0.3 * networkDelay + 0.7 * dcLoad+latency;
	        objectiveFunction= Math.round(objectiveFunction * 100.0) / 100.0;
		 return objectiveFunction;
	}
	
	public static CustomDataCenter getBestDataCenterByFunctions(CustomCloudlet cloudlet, List<CustomDataCenter> data_centers_list,List<CustomVM> vms_list) {
	    double MaxObjFunction = 99999999999999.0;
	    List<CustomVM> dc_vms=null;
	    List<Double> OFunctions_array=new ArrayList<Double>();
	    CustomDataCenter best_dc=null;
	    String Ofunction_string="";
	    for (CustomDataCenter dc : data_centers_list) {
	    	double cet = calculateCET(cloudlet, dc);
	        dc_vms =Utils.extractDataCenterVms(vms_list, dc.getId());
	        CustomVM vm=Utils.getVmWithLowestLoad(dc_vms);
	        double networkDelay = calculateNetworkDelay(cloudlet, vm);
	        double networkLatency=calculateLatency(cloudlet, dc);
	        double dcLoad = dc.getLoad();
	        double OF = calculateObjectiveFunction(cet, networkDelay, dcLoad,networkLatency);
	        Ofunction_string=Ofunction_string+cet+"+ "+networkDelay+"+ "+dcLoad+"+ "+networkLatency+"= "+OF+"        ";	            if (OF < MaxObjFunction) {
					best_dc=dc;
					MaxObjFunction = OF;

	            }
	        }
	    OFunctions_array.add((double) best_dc.getId());
	    DCsOFunctions.put(cloudlet, Ofunction_string);
	    OFunctions_array=null;
	    
	    

	  //the objective function is the biggest    
	    
//	    return Tools.getDataCenterWithLowestLoad(data_centers_list);
	    return best_dc;//with lowest objective function
	}
	

	}
	





