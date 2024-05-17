package Costums_elements;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

public class CustomCloudlet extends Cloudlet {
    private double latitude;
    private double longitude;
    private String task_data;
    private String task_status;

    public CustomCloudlet(int cloudletId, long cloudletLength, 
    		int pesNumber, long cloudletFileSize, long cloudletOutputSize, 
    		UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam, 
    		UtilizationModel utilizationModelBw,
    		double latitude, double longitude) {
        super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw);
        this.latitude = latitude;
        this.longitude = longitude;
        this.task_data=task_data;
        this.task_status=task_status;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    
    public String getTaskData() {
        return task_data;
    }

    public String getSecurityStatus() {
        return task_status;
    }
    public void setTaskData(String data) {
        this.task_data=data;
    }

    public void SetSecuityStatus(String stats) {
        this.task_status=stats;
    }
}
