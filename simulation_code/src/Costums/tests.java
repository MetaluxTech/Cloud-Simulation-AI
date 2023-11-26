package Costums;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

	   public static int getNextRandom(int min, int max) {
	       Random random = new Random();
	       return random.nextInt(max - min + 1) + min;
	   }

	

public static GeoDatacenter getDatacenterById(int  dataCenterId, List<GeoDatacenter> datacentersList) {
		
	    for (GeoDatacenter dc : datacentersList) {
	        if (dc.getId() == dataCenterId) {
	        	return dc;
	        }
	    }
	    return null;
	}
public static MyVm getVmWithLowestLoad(List<MyVm> vms) {
	
	MyVm LowestLoadVm=  vms.stream()
	             .min(Comparator.comparing(MyVm::getLoad))
	             .orElse(null);
		
	
		return LowestLoadVm;
	 
	}

public static GeoDatacenter getDataCenterWithLowestLoad(List<GeoDatacenter> dcs) {
	
	GeoDatacenter LowestLoadDc=  dcs.stream()
	             .min(Comparator.comparing(GeoDatacenter::getLoad))
	             .orElse(null);
		
	
		return LowestLoadDc;
	 
	}

public static MyVm getVMById(int  VmId, List<MyVm> vms) {
	
    for (MyVm v : vms) {
        if (v.getId() == VmId) {
        	return v;
        }
    }
    return null;
}


    public static double calculateDistance(GeoCloudlet geotask,GeoDatacenter geoDC) {
    	try {
    	double lat1=geotask.getLatitude();
    	double lon1=geotask.getLongitude();
    	double lat2=geoDC.getLatitude();
    	double lon2=geoDC.getLongitude();
    	
        // Convert degrees to radians
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // Earth's radius in kilometers
        double earthRadius = 6371.01;

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c1 = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
       
        return Math.floor( earthRadius * c1);
    	}
    	catch (Exception e) {
			return -1;
		}
    }
    
  
    public static List<MyVm> extractDataCenterVms(List<MyVm> allVMs, Integer DataCenterId) {
        List<MyVm> selectedVMs = new ArrayList<>();
        List<Integer> dc_vms_ids=perfomance.DCsVmsMap.get(DataCenterId);
        for (MyVm vm : allVMs) {
            if (dc_vms_ids.contains(vm.getId())) {
                selectedVMs.add(vm);
            }
        }

        return selectedVMs;
    }
    
    public static int findDatacenterIdForVm(int vmId) {
        for (Map.Entry<Integer, List<Integer>> entry : perfomance.DCsVmsMap.entrySet()) {
            if (entry.getValue().contains(vmId)) {
                return entry.getKey();
            }
        }
        return -1; // Default value if VM ID is not found
    }
}
