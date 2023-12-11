package Costums;

import java.util.List;
import java.util.Map;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

import simulation_1.Simulator;

import java.util.Comparator;

public class perfomance {
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
	    
	    return Math.round(cet * 100.0) / 100.0;
	    
	    //8000
	}

	public static double calculateNetworkDelay(GeoCloudlet cloudlet, MyVm vm) {  // task delay
	    // Task length in MI
		
	    double lt = cloudlet.getCloudletLength();
	    double bw=vm.getBw();
	    double delayN = lt /bw;
	    return Math.round(delayN * 100.0) / 100.0;
	    
	    //2
	}
	public static double calculateLatency(GeoCloudlet cloudlet, GeoDatacenter dataCenter) {  // task latency
		
		double n=5;// propogation time km per s
		double distance=tests.calculateDistance(cloudlet, dataCenter);
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
	public static GeoDatacenter getBestDataCenter(GeoCloudlet cloudlet, List<GeoDatacenter> dataCenters,List<MyVm> vms_list) {
	    GeoDatacenter bestDataCenter = null;
	    double MaxObjFunction = Double.MIN_VALUE;
	    List<MyVm> dc_vms=null;
	    for (GeoDatacenter dataCenter : dataCenters) {
	            double cet = calculateCET(cloudlet, dataCenter);
	            dc_vms =tests.extractDataCenterVms(vms_list, dataCenter.getId());
	            MyVm vm=tests.getVmWithLowestLoad(dc_vms);
	            double networkDelay = calculateNetworkDelay(cloudlet, vm);
	            double dcLoad = dataCenter.getLoad();
	            double OF = calculateObjectiveFunction(cet, networkDelay, dcLoad);
	           
	            if (OF > MaxObjFunction) {
	            	MaxObjFunction = OF;
	                bestDataCenter = dataCenter;

	            }
	        }

	  //the oblective function is the biggest    
	    
	    return tests.getDataCenterWithLowestLoad(dataCenters);
//	    return bestDataCenter;//with lowest objective function
	}
	
	  public static double caculateOFToPrint(GeoDatacenter dataCenter, GeoCloudlet cloudlet, List<MyVm> vms_list) {
	        // Retrieve the costs from the data center
	        double cet = calculateCET(cloudlet, dataCenter);
	        List<MyVm> dc_vms = tests.extractDataCenterVms(vms_list, dataCenter.getId());
	        MyVm vm = tests.getVmWithLowestLoad(dc_vms);
	        double networkDelay = calculateNetworkDelay(cloudlet, vm);
	        double dcLoad = dataCenter.getLoad();

	        // Customize this formula based on your specific requirements
	        
	        double objectiveFunction = 0.4 * cet + 0.3 * networkDelay + 0.3 * dcLoad;
	        return Math.round(objectiveFunction * 100.0) / 100.0;
	    }
	    private static double normalize(double value, double minValue, double maxValue) {
	        double num= (value - minValue) / (maxValue - minValue);
	        
	        return Math.round(num * 100.0) / 100.0;
	    }


	}
	





