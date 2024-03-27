package Costums_elements;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

public class CustomDataCenter extends Datacenter {
    private double latitude;
    private double longitude;
	private double load;

    public CustomDataCenter(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval, double latitude, double longitude, double load) throws Exception {
        super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
        this.latitude = latitude;
        this.longitude = longitude;
        this.load = load;
    }

    public double getLatitude() {
        return latitude;
    }
  public double getLongitude() {
        return longitude;
    }
  public double getLoad() {
        return Math.round(this.load  * 100.0) / 100.0;
    }
  public void setLoad(double load) {
      this.load = load;
  }
  
  public DatacenterCharacteristics getPublicCharacteristics() {
		return getCharacteristics();
	}
  
  
}
