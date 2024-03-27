package Costums_elements;

import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.lists.VmList;

public class CustomVM extends Vm {
	private double load;
	private double ramCost;
	private double storageCost;
	private double BwCost;
	private double cpuCost;

	public CustomVM(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm,
			CloudletScheduler cloudletScheduler, double load, double memCost, double storageCost, double BwCost,
			double processCost

	) {
		super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
		this.load = load;
		this.ramCost = memCost;
		this.storageCost = storageCost;
		this.BwCost = BwCost;
		this.cpuCost = processCost;
	}

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = Math.round(load * 100.0) / 100.0;
	}
	

    public double getramCost() {
        return ramCost;
    }

    public void setramCost(double memCost) {
        this.ramCost = memCost ;
    }

    public double getStorageCost() {
        return storageCost;
    }

    public void setStorageCost(double storageCost) {
        this.storageCost = storageCost ;
    }

    public double getBwCost() {
        return BwCost;
    }

    public void setBwCost(double BwCost) {
        this.BwCost = BwCost ;
    }

    public double getcpuCost() {
        return cpuCost;
    }

    public void setcpuCost(double processCost) {
        this.cpuCost = processCost;
    }
	
}
