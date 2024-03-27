package simulation_1;
import java.awt.print.Printable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

import Costums_elements.CustomBroker;
import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import Costums_elements.CustomVM;
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
			boolean use_vm_schudeling = true;
			boolean use_randome_values = true;
			boolean save_trining = false;
			boolean save_vm_scheduling = false;
			boolean save_cloudlets_specifications = false;
			boolean save_expereiment = false;
			boolean print_model_quality = false;
			boolean display_simulation_timing_spesifications = false;

        	String modelName="NONE";//GA or SNAKE or NONE or FUNCTIONS or New_Model
			int numUsers = 1;
			int numDatacenters = 3;
			int numVMs = 15;
			int numCloudlets = 10;

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
			
			
			tasksList = ElementsCreation.createCloudlets(numCloudlets, broker,modelName,datacentersList,vmsList, use_randome_values,use_vm_schudeling);

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
				String dataset_name="training_" + numCloudlets + ".csv";
				FileManager.SaveTrainingDataSet(dataset_name, tasksList, datacentersList,vmsList);
			}
			if (save_vm_scheduling) {
				String dataset_name="vms_scheduling_dataset_" + numCloudlets + ".csv";
				FileManager.SaveVmsSchedulingDataset(dataset_name, tasksList, datacentersList, vmsList);
			}
			if (save_cloudlets_specifications) {
				String dataset_name="cloudlets_specifications_" + numCloudlets + ".csv";
				FileManager.SaveCloudletsSpecifications(dataset_name, tasksList);
			}
			if (save_expereiment) {
				String dataset_name="experiement_result_" + modelName + "_" + numCloudlets + ".csv";
				FileManager.SaveExperimentDataSet(dataset_name, tasksList);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
