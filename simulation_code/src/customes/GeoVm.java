package customes;
import org.cloudbus.cloudsim.*;

public class GeoVm extends Vm {
    // New properties for latitude and longitude
    private double latitude;
    private double longitude;

    // Constructor
    public GeoVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler, double latitude, double longitude) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters for the new properties
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
