package Costums;
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

public class GeoDatacenter extends Datacenter {
    private double latitude;
    private double longitude;
	private double load;
	private List<MyVm> vmList;

    public GeoDatacenter(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval, double latitude, double longitude, double load) throws Exception {
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
        return this.load ;
    }
  public void setLoad(double load) {
      this.load = Math.round(load * 100.0) / 100.0;
  }
  
  public DatacenterCharacteristics getPublicCharacteristics() {
		return getCharacteristics();
	}
  
  
}
