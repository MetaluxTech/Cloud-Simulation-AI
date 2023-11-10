package Costums;

import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
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
	public static double calculateHostLoad(List<GeoCloudlet> tasks, Vm vm) {
	    double mipsUsed = 0;
	    double mipsTotal = vm.getMips();

	    for (GeoCloudlet task : tasks) {
	        mipsUsed += task.getCloudletLength();
	    }

	    double hostLoad = (mipsUsed / mipsTotal) / 100;
	    return hostLoad;
	}

	public static double calculateDataCenterLoad(List<Host> hosts, Map<Host, List<GeoCloudlet>> hostTasks, Vm vm) {
	    double dcLoad = 0;

	    for (Host host : hosts) {
	        List<GeoCloudlet> tasks = hostTasks.get(host);
	        dcLoad += calculateHostLoad(tasks, vm);
	    }

	    return dcLoad;
	}

	public static double calculateObjectiveFunction(double cet, double networkDelay, double dcLoad) {
	    double objectiveFunction = 0.4 * cet + 0.3 * networkDelay + 0.3 * dcLoad;
	    return objectiveFunction;
	}

}



