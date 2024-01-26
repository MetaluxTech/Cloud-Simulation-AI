package myjava1;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import java.util.List;

public class MyDataCenter extends Datacenter {
    
    private double latitude;
    private double longitude;

    public MyDataCenter(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy,
                        List<Storage> storageList, double schedulingInterval, double longitude, double latitude) throws Exception {
        super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
        
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters for latitude and longitude
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
