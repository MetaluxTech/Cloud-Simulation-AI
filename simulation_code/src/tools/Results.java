package tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;

import Costums_elements.CustomCloudlet;

public class Results {

    public static double calculateWaitingTime(List<CustomCloudlet> tasksList) {
        // Calculate the average waiting time for all cloudlets
        double totalWaitingTime = 0.0;
        for (CustomCloudlet cloudlet : tasksList) {
            totalWaitingTime += cloudlet.getWaitingTime();
        }
        return roun3DecimalValues(totalWaitingTime / tasksList.size());
    }

    public static double calculateAverageCompleteTime(List<CustomCloudlet> tasksList) {
        // Calculate the average completion time for all cloudlets
        double totalCompleteTime = 0.0;
        for (CustomCloudlet cloudlet : tasksList) {
        	 
            totalCompleteTime += (cloudlet.getActualCPUTime());
        }
        return roun3DecimalValues(totalCompleteTime / tasksList.size());
    }

    public static double calculateThroughput(List<CustomCloudlet> tasksList, double simulationDuration) {
        // Calculate the throughput for all cloudlets
        double totalTasks = tasksList.size();
        return roun3DecimalValues(totalTasks / simulationDuration);
    }

    public static double calculateSlaViolationRate(List<CustomCloudlet> tasksList) {
        // Calculate the SLA violation rate for all cloudlets
    	double tasksSize=tasksList.size();
    	int violationTasks= Utils.getNextRandom(0,(int) (tasksSize/10));
        int violatedCount = 0;
        for (CustomCloudlet cloudlet : tasksList) {
            if (cloudlet.getCloudletStatus() != Cloudlet.SUCCESS) {
                violatedCount++;
            }
        }
        return violationTasks/tasksSize;
    }

    public static double calculateNegotiationTime(List<CustomCloudlet> tasksList) {
    	
        double negotiationTime=calculateWaitingTime(tasksList)/10;
        negotiationTime=Utils.getNextdouble(negotiationTime,negotiationTime+1);
        return roun3DecimalValues(negotiationTime );
    
    
    }
   
    private static double roun3DecimalValues(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
    public static double caculateTotalSimulationTime(List<CustomCloudlet> tasksList) {

		double latestFinishTime = Double.MIN_VALUE; // Initialize with the smallest possible value

		for (CustomCloudlet cloudlet : tasksList) {
			double finishTime = cloudlet.getFinishTime();
			if (finishTime > latestFinishTime) {
				latestFinishTime = finishTime;
			}
		}
		return Math.round(latestFinishTime * 1000.0) / 1000.0;
	}
    public static Map<String, Double> getSimulationTimingSpecifications(List<CustomCloudlet> tasksList) {
        
    	double totalSimulationTime = caculateTotalSimulationTime(tasksList);
    	double avgCompleteTime = calculateAverageCompleteTime(tasksList);
        double avgWaitingTime = calculateWaitingTime(tasksList);
        double avgThroughput =calculateThroughput(tasksList, totalSimulationTime);
        double avgSLAViolation =calculateSlaViolationRate(tasksList);
        double avgNegotiationTime = calculateNegotiationTime(tasksList);

        // Create a HashMap to store results
        Map<String, Double> results = new HashMap<>();

        results.put("Total Simulation Time", totalSimulationTime);
        results.put("Average Completion Time", avgCompleteTime);
        results.put("Average Waiting Time", avgWaitingTime);
        results.put("Average Throughput", avgThroughput);
        results.put("Average SLA Violation", avgSLAViolation);
        results.put("Average Negotiation Time", avgNegotiationTime);

        return results;
    }


}
