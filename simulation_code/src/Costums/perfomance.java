package Costums;

import java.util.List;
import java.util.Map;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;


public class perfomance {
	
	public static double calculateCET(GeoCloudlet cloudlet, Vm vm, double cpuCost, double ramCost, double storageCost, double bandwidthCost) 
	{
	    double exe = cloudlet.getActualCPUTime();
	    double r = cloudlet.getCloudletFileSize();
	    double st = cloudlet.getCloudletOutputSize();
	    double f = cloudlet.getCloudletFileSize();
	    double cet = (exe * cpuCost) + (r * ramCost) + (st * storageCost) + (f * bandwidthCost);
	    return cet;
	}

	public static double calculateNetworkDelay(GeoCloudlet cloudlet, Vm vm) {
	    // Task length in MI
	    double lt = cloudlet.getCloudletLength();

	    // VM bandwidth in bps
	    double bv = vm.getBw();

	    // Calculate and return the network delay
	    double delayN = lt / bv;
	    return delayN;
	}

	public static double calculateDataCenterLoad(GeoDatacenter dataCenter) {
	
	    return dataCenter.getLoad();
	}
	
	
	public static double calculateObjectiveFunction(double cet, double networkDelay, double dcLoad) {
	    double objectiveFunction = 0.4 * cet + 0.3 * networkDelay + 0.3 * dcLoad;
	    return objectiveFunction;
	}
	public static GeoDatacenter getBestDataCenter(GeoCloudlet cloudlet, List<GeoDatacenter> dataCenters) {
	    GeoDatacenter bestDataCenter = null;
	    double maxObjectiveFunction = Double.MIN_VALUE;

	    for (GeoDatacenter dataCenter : dataCenters) {
	        // Retrieve the costs from the data center
	        double cpuCost = dataCenter.getPublicCharacteristics().getCostPerMi();
	        double ramCost = dataCenter.getPublicCharacteristics().getCostPerMem();
	        double storageCost = dataCenter.getPublicCharacteristics().getCostPerStorage();
	        double bandwidthCost = dataCenter.getPublicCharacteristics().getCostPerBw();
	       
	        // Initialize the maximum objective function for the data center
	        double maxDataCenterObjectiveFunction = Double.MIN_VALUE;
	        Log.printLine("dataCenter vmslist "+dataCenter.getVmList());

	        // Loop over each VM in the data center
	        for (Vm vm : dataCenter.getVmList()) {
	            double cet = calculateCET(cloudlet, vm, cpuCost, ramCost, storageCost, bandwidthCost);
	            double networkDelay = calculateNetworkDelay(cloudlet, vm);
	            double dcLoad = calculateDataCenterLoad(dataCenter);
	            double vmObjectiveFunction = calculateObjectiveFunction(cet, networkDelay, dcLoad);
	            Log.printLine("Objective function for VM:"+vm.getId()+" " + vmObjectiveFunction);
	            
	            // Update the maximum objective function for the data center
	            if (vmObjectiveFunction > maxDataCenterObjectiveFunction) {
	                maxDataCenterObjectiveFunction = vmObjectiveFunction;
	            }
	        }

	        // If the maximum objective function for the data center is greater than the maximum so far, update it
	        if (maxDataCenterObjectiveFunction > maxObjectiveFunction) {
	            maxObjectiveFunction = maxDataCenterObjectiveFunction;
	            bestDataCenter = dataCenter;
	        }
	    }

	    return bestDataCenter;
	}

	}
	





