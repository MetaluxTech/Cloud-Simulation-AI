package simulation_1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

import Costums.CustomCloudlet;
import Costums.CustomDataCenter;
import Costums.CustomBroker;
import Costums.CustomVM;
import tools.FileManager;
import tools.Results;

public class Simulator {

	private static List<CustomDataCenter> datacentersList;
	private static List<CustomVM> vmsList;
	private static List<CustomCloudlet> tasksList;

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		try {

//		  create	simulation variables
			boolean use_randome_values = false;
			boolean save_trining = true;
			boolean save_cloudlets_specifications = false;
			boolean save_expereiment = false;
			boolean print_model_quality = false;
			boolean display_simulation_timing_spesifications = true;
			
			String modelName = "NONE";

			int numUsers = 1;
			int numDatacenters = 3;
			int numVMs = 9;
			int numCloudlets = 5;

//			create simulation arrays
			vmsList = new ArrayList<CustomVM>();
			tasksList = new ArrayList<CustomCloudlet>();
			datacentersList = new ArrayList<CustomDataCenter>();
			CustomBroker broker = null;

//			init simulation
			Calendar clndr = Calendar.getInstance();
			boolean trace_actions = false;
			CloudSim.init(numUsers, clndr, trace_actions);

//			create datacenters brokeres Cloudlets and vms 
			broker = ElementsCreation.createBroker("broker1");
			datacentersList = ElementsCreation.createDatacenters(numDatacenters, use_randome_values);
			vmsList = ElementsCreation.createVms(numVMs, broker, use_randome_values);
			tasksList = ElementsCreation.createCloudlets(numCloudlets, broker, use_randome_values);

//			submit tasks and vms .....
			broker.submitVmList(vmsList);
			broker.submitCloudletList(tasksList);
			CloudSim.startSimulation();

			CloudSim.stopSimulation();
			
			// display simulation events and results
			Map<String, Double> simulationTimingSpecifications = Results.getSimulationTimingSpecifications(tasksList);
			Displays.printSimulationSubmittingEvents(broker.getCloudletReceivedList(), datacentersList);
			if(display_simulation_timing_spesifications) {
			Displays.printSimulationTimingSpecifications(simulationTimingSpecifications, numCloudlets);
			}
			// Save  to Excel file if you needed
			if (save_trining) {
				FileManager.SaveTrainingDataSet("training_" + numCloudlets + ".csv", tasksList, datacentersList,vmsList);
			}
			if (save_cloudlets_specifications) {
				FileManager.SaveCloudletsSpecifications("cloudlets_specifications_" + numCloudlets + ".csv", tasksList);
			}
			if (save_expereiment) {
				FileManager.SaveExperimentDataSet("experiement_result/" + modelName + "_results_" + numCloudlets + ".csv", tasksList);
			}
			Log.printLine("datacetertasks: "+ datacentersList.get(0).getVmList());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
