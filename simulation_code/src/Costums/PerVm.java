package Costums;
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

public class PerVm extends Vm {
	  private double load;

	  public PerVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler, double load) {
	      super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
	      this.load = load;
	  }

	  public double getLoad() {
	      return load;
	  }

	  public void setLoad(double load) {
	      this.load = load;
	  }
	}
