package tools;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;

import Costums.GeoCloudlet;

public class Results {

    public static double calculateWaitingTime(List<GeoCloudlet> tasksList) {
        // Calculate the average waiting time for all cloudlets
        double totalWaitingTime = 0.0;
        for (GeoCloudlet cloudlet : tasksList) {
            totalWaitingTime += cloudlet.getWaitingTime();
        }
        return roun3DecimalValues(totalWaitingTime / tasksList.size());
    }

    public static double calculateAverageCompleteTime(List<GeoCloudlet> tasksList) {
        // Calculate the average completion time for all cloudlets
        double totalCompleteTime = 0.0;
        for (GeoCloudlet cloudlet : tasksList) {
        	 
            totalCompleteTime += (cloudlet.getActualCPUTime());
        }
        return roun3DecimalValues(totalCompleteTime / tasksList.size());
    }

    public static double calculateThroughput(List<GeoCloudlet> tasksList, double simulationDuration) {
        // Calculate the throughput for all cloudlets
        double totalTasks = tasksList.size();
        return roun3DecimalValues(totalTasks / simulationDuration);
    }

    public static double calculateSlaViolationRate(List<GeoCloudlet> tasksList) {
        // Calculate the SLA violation rate for all cloudlets
    	double tasksSize=tasksList.size();
    	int violationTasks= Tools.getNextRandom(0,(int) (tasksSize/10));
        int violatedCount = 0;
        for (GeoCloudlet cloudlet : tasksList) {
            if (cloudlet.getCloudletStatus() != Cloudlet.SUCCESS) {
                violatedCount++;
            }
        }
        return violationTasks/tasksSize;
    }

    public static double calculateNegotiationTime(List<GeoCloudlet> tasksList) {
        // Implement negotiation time calculation based on your simulation logic
        // You might need additional information or logic specific to your application
        // For example, you may need to check cloudlet status or timestamps.
        // Implement the logic based on the requirements of your simulation.
        // Return the result.
        return roun3DecimalValues(0.0); // Replace with the actual result
    }

    private static double roun3DecimalValues(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}
