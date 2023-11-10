package Costums;

import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

public class tests {
	

	public static  double[] generateRandomLatLon() {
	    Random random = new Random();

	    // Generate random latitude (-90 to 90)
	    double latitude = -90.0 + (90.0 - (-90.0)) * random.nextDouble();

	    // Generate random longitude (-180 to 180)
	    double longitude = -180.0 + (180.0 - (-180.0)) * random.nextDouble();

	    return new double[]{latitude, longitude};
	}

	public static GeoCloudlet getCloudletById(int cloudletId, List<GeoCloudlet> cloudletsList) {
	    for (GeoCloudlet cloudlet : cloudletsList) {
	        if (cloudlet.getCloudletId() == cloudletId) {
	            return cloudlet;
	        }
	    }
	    return null;
	}

	public static GeoDatacenter getDatacenterById(int  dataCenterId, List<GeoDatacenter> datacentersList) {
		
	    for (GeoDatacenter dc : datacentersList) {
	        if (dc.getId() == dataCenterId) {
	        	return dc;
	        }
	    }
	    return null;
	}

	
}
