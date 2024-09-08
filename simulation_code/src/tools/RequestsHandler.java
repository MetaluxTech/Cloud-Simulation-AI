package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;
import Costums_elements.CustomDataCenter;
import Costums_elements.CustomVM;
import simulation_1.Simulator;

public class RequestsHandler {

	private static String new_equests_dataset = Paths.get("").toAbsolutePath().getParent()
			.resolve("AI_code/dataset/new_requests_dataset.csv").toString();

	public static void handleCloudlets(List<CustomCloudlet> requestsList) {

		
		if (Simulator.generate_new_requests) {
			
			FileManager.saveNewRequestsDataset(new_equests_dataset, requestsList);
			AI.runScript2();
			handle_new_requests(requestsList);

		} else {
			handle_pretrained_requests(requestsList);
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
