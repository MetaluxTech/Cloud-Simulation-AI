package simulation_1;
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
import Security_Manager.Security;
import tools.FileManager;
import tools.Results;

public class Simulator {


	public static boolean generate_new_requests = false;
	public static boolean enable_security_layer = true;

	public static String DC_MODEL="SNAKE";		//GA or SNAKE or NONE or FUNCTIONS or ENSEMBLE
	public static String VM_MODEL="ENSEMBLE"; // NONE or FUNCTIONS or SNAKE or ENSEMBLE

	public static int numUsers = 1;
	public static int numDatacenters = 3;
	public static int numVMs = 15;
	public static int numCloudlets =100;

	public static int numBlockedRequests=0;

	public static List<CustomDataCenter> datacentersList;
	public static List<CustomVM> vmsList;
	public static List<CustomCloudlet> tasksList;
	public static List<Double> loadMontor=new ArrayList<>();


	public static void main(String[] args) {

		try {
//		  create	simulation variables

			boolean save_cloudlets_properties = false;
			boolean save_training = false;
			boolean save_vm_scheduling = false;
			boolean save_expereiment = false;
			boolean print_model_quality = false;
			boolean display_simulation_timing_spesifications = true;
			boolean save_all_wanted_spec=false;



			Security.GenerateAESKey(64);//CHARECTER_LENGTH


//			create simulation arrays
			vmsList = new ArrayList<>();
			tasksList = new ArrayList<>();
			datacentersList = new ArrayList<>();
			CustomBroker broker = null;
//			init simulation
			Calendar clndr = Calendar.getInstance();
			boolean trace_actions = false;
			CloudSim.init(numUsers, clndr, trace_actions);

//			create datacenters brokeres Cloudlets and vms
			broker = ElementsCreation.createBroker("broker1");
			datacentersList = ElementsCreation.createDatacenters(numDatacenters, generate_new_requests);
			vmsList = ElementsCreation.createVms(numVMs, broker, generate_new_requests);
			tasksList = ElementsCreation.createCloudlets(numCloudlets, broker);
			RequestsHandler.handleCloudlets(tasksList);
			//			submit tasks and vms .....
			broker.submitVmList(vmsList);
			broker.submitCloudletList(tasksList);

			CloudSim.startSimulation();
			CloudSim.stopSimulation();

			Displays.printSimulationSubmittingEvents(broker.getCloudletReceivedList(), datacentersList);
//			Evaluation.printEvaluationParameters(tasksList);


			if(display_simulation_timing_spesifications) {
				Map<String, Double> simulationTimingSpecifications = Results.getSimulationTimingSpecifications(tasksList);
				Displays.printSimulationTimingSpecifications(simulationTimingSpecifications, numCloudlets);
			}
			// Save  to Excel file if you needed
			if (save_training) {
				String dataset_name="training_" + numCloudlets + ".csv";
				FileManager.SaveTrainingDataSet(dataset_name, tasksList, datacentersList,vmsList);
			}
			if (save_vm_scheduling) {
				String dataset_name="vms_scheduling_dataset_" + numCloudlets + ".csv";
				FileManager.SaveVmsSchedulingDataset(dataset_name, tasksList, datacentersList, vmsList);
			}
			if (save_cloudlets_properties) {
				String dataset_name="cloudlets_properties_" + numCloudlets + ".csv";
				FileManager.SaveCloudletsSpecifications(dataset_name, tasksList);
			}
			if (save_expereiment) {
				String model_name=DC_MODEL;
				if (VM_MODEL!="NONE") {
					model_name=VM_MODEL;
				}


				String dataset_name="experiement_result/" + model_name + "_" + numCloudlets + ".csv";
				if (numCloudlets==50) {
					dataset_name="experiement_result/" + model_name + "_0" + numCloudlets + ".csv";
				}
				FileManager.SaveExperimentDataSet(dataset_name, tasksList);
			}
			if(save_all_wanted_spec) {

				FileManager.saveWantedDataSet("wanted_"+numCloudlets+".csv", tasksList,datacentersList,vmsList);

			}
			if(enable_security_layer) {
				Log.printLine("num of blocked requests: "+numBlockedRequests);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}




