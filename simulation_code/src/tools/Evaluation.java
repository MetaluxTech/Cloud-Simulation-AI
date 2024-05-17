package tools;
import java.util.List;

import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;

public class Evaluation {
	  public static double calculateAverageWaitingTime(List<CustomCloudlet> cloudlets) {
	        double totalWaitingTime = 0.0;
	        for (CustomCloudlet cloudlet : cloudlets) {
	            double startTime = cloudlet.getExecStartTime();
	            double submissionTime = cloudlet.getSubmissionTime();
	            double waitingTime = startTime - submissionTime;
	            
	            totalWaitingTime += waitingTime;
	        }
	        return totalWaitingTime / cloudlets.size();
	    }

	public static void printEvaluationParameters(List<CustomCloudlet> tasksList) {
		double av_wt=calculateAverageWaitingTime(tasksList);
		Log.printLine("average Waiting Time : "+av_wt);
		
	}
}
