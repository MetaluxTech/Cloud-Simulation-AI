package simulation_1;

import java.nio.file.Paths;
import java.util.List;

import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;
import tools.AI;
import tools.FileManager;
import tools.Utils;

public class RequestsHandler {

	private static String new_equests_dataset = Paths.get("").toAbsolutePath().getParent()
			.resolve("AI_code/dataset/new_requests_dataset.csv").toString();

	public static void handleCloudlets(List<CustomCloudlet> requestsList) {
		if (Simulator.enable_security_layer){
			processSecurity(requestsList);
		}

		if (Simulator.generate_new_requests) {

			FileManager.saveNewRequestsDataset(new_equests_dataset, requestsList);
			AI.runScript2();
			handle_new_requests(requestsList);

		} else {
			handle_pretrained_requests(requestsList);
		}
	}

	private static void processSecurity(List<CustomCloudlet> requestsList) {
		for(CustomCloudlet task : Simulator.tasksList) {
			String taskData=FileManager.GetCloudletData("security_dataset.csv");
		    task.setTaskData(taskData);

			String[] parts = taskData.split(",");
			String status = parts[parts.length - 1];
			task.SetSecuityStatus(status);	
			if (!task.getTaskData().contains("normal")) {
						
								Simulator.numBlockedRequests++;
				
			}
		}
	}

	private static void handle_new_requests(List<CustomCloudlet> tasksList) {

		// Implementation for handling new requests
		for (CustomCloudlet task : tasksList) {
			int[] bestDcVm = AI.getBestDC_VM_forNewRequest(Simulator.DC_MODEL, Simulator.VM_MODEL, task);
			int dc_id = bestDcVm[0];
			int vm_id = bestDcVm[1];

			if (dc_id != -1 && vm_id != -1) {

				task.setVmId(vm_id);
				Utils.updateLoads(task,dc_id, vm_id);

			} else {
                    throw new RuntimeException("Failed to assign DC and VM for task. DC ID: " + dc_id + ", VM ID: " + vm_id + ", Task ID: " + task.getCloudletId());
			}
		}}



	private static void handle_pretrained_requests(List<CustomCloudlet> tasksList) {
         for (CustomCloudlet task : tasksList) {
                int[] bestDcVm = AI.getBestDC_VM_forPretrainedRequests(Simulator.DC_MODEL, Simulator.VM_MODEL, task);
                int dc_id = bestDcVm[0];
                int vm_id = bestDcVm[1];

                if (dc_id != -1 && vm_id != -1) {
                    task.setVmId(vm_id);
                    Utils.updateLoads(task,dc_id, vm_id);
                } else {
                    throw new RuntimeException("Failed to assign DC and VM for task. DC ID: " + dc_id + ", VM ID: " + vm_id + ", Task ID: " + task.getCloudletId());
                }
            }
    }


}
