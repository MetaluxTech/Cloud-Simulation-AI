package simulation_1;
import java.nio.file.Paths;
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
import tools.AI;
import tools.FileManager;
import tools.Results;

public class Simulator {

	public static String dataset_path = Paths.get("").toAbsolutePath().getParent().resolve("AI_code/dataset/global_dataset.csv").toString();	

	private static List<CustomDataCenter> datacentersList;
	private static List<CustomVM> vmsList;
	private static List<CustomCloudlet> tasksList;
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		try {

//		  create	simulation variables
			boolean use_scheduling=true;
			boolean use_randome_values = false;
			boolean save_cloudlets_properties = false;
			boolean save_training = false;
			boolean save_vm_scheduling = false;
			boolean save_expereiment = false;
			boolean print_model_quality = false;
			boolean display_simulation_timing_spesifications = true;
			boolean save_all_wanted_spec=false;
        	String modelName="GA";//GA or SNAKE or NONE or FUNCTIONS or New_Model or ENSEMBLE
        	String scheduling_model="ENSEMBLE"; // NONE or FUNCTIONS or SNAKE or ENSEMBLE
			int numUsers = 1;
			int numDatacenters = 3;
			int numVMs = 15;
			int numCloudlets = 50;
			Security.GenerateAESKey(16);
			
			
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
			tasksList = ElementsCreation.createCloudlets(numCloudlets, broker,modelName,scheduling_model,datacentersList,vmsList, use_randome_values);
			
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
				String model_name=modelName;
				if (use_scheduling)model_name=scheduling_model;
				
				
				String dataset_name="experiement_result/" + model_name + "_" + numCloudlets + ".csv";
				if (numCloudlets==50) {
					dataset_name="experiement_result/" + model_name + "_0" + numCloudlets + ".csv";
				}
				FileManager.SaveExperimentDataSet(dataset_name, tasksList);
			}
			if(save_all_wanted_spec) {
		        
				FileManager.saveWantedDataSet("wanted_"+numCloudlets+".csv", tasksList,datacentersList,vmsList);
				
			}
			String pretrained_model_name="snake_model_95.keras";
		int predicted_dc=AI.PredictDataCenterIDFromPython(tasksList.get(0),pretrained_model_name );
		
		Log.printLine("predicted dataceter for model (( "+pretrained_model_name+" )) :-> "+predicted_dc);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}




