package customes;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

public class GeoTask extends Cloudlet {
  
    private double latitude;
    private double longitude;
  
    // Constructor
    public GeoTask(int cloudletId, long cloudletLength, int pesNumber, long cloudletFileSize, 
            long cloudletOutputSize, UtilizationModel utilizationModelCpu, 
            UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw, 
            boolean record, List<String> fileList, double latitude, double longitude) {

	super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, 
	      utilizationModelCpu, utilizationModelRam, utilizationModelBw, record, fileList != null ? fileList : new ArrayList<>());
	
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

