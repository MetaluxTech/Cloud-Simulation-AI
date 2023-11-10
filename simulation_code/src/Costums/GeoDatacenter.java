package Costums;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;

public class GeoDatacenter extends Datacenter {
    private double latitude;
    private double longitude;

    public GeoDatacenter(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList, double schedulingInterval, double latitude, double longitude) throws Exception {
        super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public DatacenterCharacteristics getPublicCharacteristics() {
		return getCharacteristics();
	}
  public double getLongitude() {
        return longitude;
    }
}
