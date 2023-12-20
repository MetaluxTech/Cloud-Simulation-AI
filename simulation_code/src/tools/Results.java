package tools;

import java.util.List;
import java.util.Map;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

import Costums.GeoCloudlet;
import Costums.GeoDatacenter;
import Costums.MyVm;
import simulation_1.Simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Results {
	 public static Map<GeoCloudlet,String > DCsOFunctions = new HashMap<>();
	 public static Map<Integer, List<Integer>> DCsVmsMap = Map.of(
              3, List.of(1, 2, 3 ),
              4, List.of(4, 5,6),
              5, List.of(7, 8, 9 )
              
               );
       	
	public static double calculateCET(GeoCloudlet cloudlet,GeoDatacenter dataCenter) // cost execution task
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

	public static double calculateNetworkDelay(GeoCloudlet cloudlet, MyVm vm) {  // task delay
	    // Task length in MI
		
	    double lt = cloudlet.getCloudletLength();
	    double bw=vm.getBw();
	    double delayN = lt /bw;
	    delayN=delayN*1.5;
	    return Math.round(delayN * 100.0) / 100.0;
	    //2
	}
	
	private static double calculateLatency(GeoCloudlet cloudlet, GeoDatacenter dataCenter) {  // task latency
		
		double n=5;// propogation time km per s
		double distance=Tools.calculateDistance(cloudlet, dataCenter);
		double latency=distance*n*2+cloudlet.getActualCPUTime();// Propagation time +processing time
		return Math.round(latency * 100.0) / 100.0;
		
		//15
	}

	public static double calculateObjectiveFunction(double cet, double networkDelay, double dcLoad) {
		//50      of=0.4*8000/1000+0.3*5+0.7*15+distance delay
	        double objectiveFunction = 0.4 * cet + 0.3 * networkDelay + 0.7 * dcLoad;
	        objectiveFunction= Math.round(objectiveFunction * 100.0) / 100.0;
		 return objectiveFunction;
	}
	
	public static GeoDatacenter getBestDataCenter(GeoCloudlet cloudlet, List<GeoDatacenter> data_centers_list,List<MyVm> vms_list) {
	    double MaxObjFunction = 99999999999999.0;
	    List<MyVm> dc_vms=null;
	    List<Double> OFunctions_array=new ArrayList<Double>();
	    GeoDatacenter best_dc=null;
	    String Ofunction_string="";
	    for (GeoDatacenter dc : data_centers_list) {
	    	double cet = calculateCET(cloudlet, dc);
	        dc_vms =Tools.extractDataCenterVms(vms_list, dc.getId());
	        MyVm vm=Tools.getVmWithLowestLoad(dc_vms);
	        double networkDelay = calculateNetworkDelay(cloudlet, vm);
	        double dcLoad = dc.getLoad();
	        double OF = calculateObjectiveFunction(cet, networkDelay, dcLoad);
	        Ofunction_string=Ofunction_string+cet+"+ "+networkDelay+"+ "+dcLoad+"= "+OF+"        ";
	            if (OF < MaxObjFunction) {
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
	





