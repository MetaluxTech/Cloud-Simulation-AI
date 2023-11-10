package Costums;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;

public class perfomance {
	
	

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
}



