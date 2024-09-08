package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.Log;

import Costums_elements.CustomCloudlet;
import simulation_1.Simulator;

public class AI {

	private static String pretraineddataset_path = Paths.get("").toAbsolutePath().getParent()
			.resolve("AI_code/dataset/global_dataset.csv").toString();
	private static String newreqests_datset_path = Paths.get("").toAbsolutePath().getParent()
			.resolve("AI_code/dataset/new_requests_dataset.csv").toString();
	private static Map<Integer, List<Integer>> DCsVmsMap = DCs_Caculations.DCsVmsMap;

	public static int[] getBestDC_VM_forNewRequest(String DC_MODEL, String VM_MODEL, CustomCloudlet task) {
		int dc_id = -1;
		int vm_id = -1;

		try (BufferedReader br = new BufferedReader(new FileReader(newreqests_datset_path))) {
			String line;
			int currentRow = 1;

			while ((line = br.readLine()) != null) {
				if (currentRow == 1) {
					currentRow++;
					continue;
				}
				// dataset columns = 0-TaskID 1-TaskFileSize 2-TaskOutputFileSize
				// 3-TaskFileLength 4-CpuTime 5-TotalLength
				// 6-UserLatitude 7-UserLongitude
				// 8-GA_predicted_DC 9-SNAKE_predicted_DC 10-ENSEMBLE_predicted_DC
				// 11-ENSEMBLE_predicted_VM 12-SNAKE_predicted_VM

				String[] rowData = line.split(",");
				if (Math.round(Double.parseDouble(rowData[6]) * 10000000)
						/ 10000000.0 == Math.round(task.getLatitude() * 10000000) / 10000000.0
						&& Math.round(Double.parseDouble(rowData[7]) * 10000000)
								/ 10000000.0 == Math.round(task.getLongitude() * 10000000) / 10000000.0) {

					if (DC_MODEL.equals("FUNCTIONS")) {
						dc_id = DCs_Caculations.getBestDataCenterByFunctions(task, Simulator.datacentersList,
								Simulator.vmsList);
					} else if (DC_MODEL.equals("NONE")) {
						dc_id = Utils.getNextRandom(3, Simulator.numDatacenters + 2);
					} else if (DC_MODEL.equals("GA")) {
						dc_id = Integer.parseInt(rowData[8]);
					} else if (DC_MODEL.equals("SNAKE")) {
						dc_id = Integer.parseInt(rowData[9]);
					} else if (DC_MODEL.equals("ENSEMBLE")) {
						dc_id = Integer.parseInt(rowData[10]);
					}

					if (DC_MODEL.equals("FUNCTIONS")) {
						vm_id = VMS_Caculations.getBestVMIDByRank(task, Simulator.datacentersList, Simulator.vmsList);
					} else if (DC_MODEL.equals("NONE")) {
						vm_id = Utils.getLeastVm(Utils.extractDataCenterVms(Simulator.vmsList, dc_id)).getId();
					} else if (VM_MODEL.equals("ENSEMBLE")) {
						vm_id = Integer.parseInt(rowData[11]);
					} else if (VM_MODEL.equals("SNAKE")) {
						vm_id = Integer.parseInt(rowData[12]);
					}
					Log.printLine("VMID "+vm_id+ " dcID "+dc_id);
					if (!DCsVmsMap.get(dc_id).contains(vm_id)) {
						List<Integer> availableVms = DCsVmsMap.get(dc_id);
						if (availableVms != null && !availableVms.isEmpty()) {
							vm_id = availableVms.get(0);
						}
					}
					if (dc_id == -1 || vm_id == -1) {
						throw new IllegalStateException("No available datacenter or VM found for task at latitude: "
								+ task.getLatitude() + ", longitude: " + task.getLongitude());
					}
					return new int[] { dc_id, vm_id };
				}
				currentRow++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new int[] { -1, -1 };
	}

	public static int[] getBestDC_VM_forPretrainedRequests(String DC_MODEL, String VM_MODEL, CustomCloudlet task) {
		int dc_id = -1;
		int vm_id = -1;

		try (BufferedReader br = new BufferedReader(new FileReader(pretraineddataset_path))) {
			String line;
			int currentRow = 1;

			while ((line = br.readLine()) != null) {
				if (currentRow == 1) {
					currentRow++;
					continue;
				}
				// dataset columns = 0-TaskID 1-TaskFileSize 2-TaskOutputFileSize
				// 3-TaskFileLength 4-CpuTime 5-TotalLength
				// 6-UserLatitude 7-UserLongitude
				// 8-GA_predicted_DC 9-SNAKE_predicted_DC 10-ENSEMBLE_predicted_DC
				// 11-ENSEMBLE_predicted_VM 12-SNAKE_predicted_VM

				String[] rowData = line.split(",");

				if (Math.round(Double.parseDouble(rowData[6]) * 10000000)
						/ 10000000.0 == Math.round(task.getLatitude() * 10000000) / 10000000.0
						&& Math.round(Double.parseDouble(rowData[7]) * 10000000)
								/ 10000000.0 == Math.round(task.getLongitude() * 10000000) / 10000000.0) {

					if (DC_MODEL.equals("FUNCTIONS")) {
						dc_id = DCs_Caculations.getBestDataCenterByFunctions(task, Simulator.datacentersList,
								Simulator.vmsList);
					} else if (DC_MODEL.equals("NONE"))

					{
						dc_id = Utils.getNextRandom(3, Simulator.numDatacenters + 2);
					} else if (DC_MODEL.equals("GA")) {

						dc_id = Integer.parseInt(rowData[8]);
					} else if (DC_MODEL.equals("SNAKE")) {
						dc_id = Integer.parseInt(rowData[9]);
					} else if (DC_MODEL.equals("ENSEMBLE")) {
						dc_id = Integer.parseInt(rowData[10]);
					}

					if (DC_MODEL.equals("FUNCTIONS")) {
						vm_id = VMS_Caculations.getBestVMIDByRank(task, Simulator.datacentersList, Simulator.vmsList);
					} else if (DC_MODEL.equals("NONE")) {
						vm_id = Utils.getLeastVm(Utils.extractDataCenterVms(Simulator.vmsList, dc_id)).getId();
					} else if (VM_MODEL.equals("ENSEMBLE")) {
						vm_id = Integer.parseInt(rowData[11]);
					} else if (VM_MODEL.equals("SNAKE")) {
						vm_id = Integer.parseInt(rowData[12]);
					}

					if (!DCsVmsMap.get(dc_id).contains(vm_id)) {
						List<Integer> availableVms = DCsVmsMap.get(dc_id);
						if (availableVms != null && !availableVms.isEmpty()) {
							vm_id = availableVms.get(0);
						}
					}
					return new int[] { dc_id, vm_id };
				}
				currentRow++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new int[] { -1, -1 };
	}

	public static void runScript() {
		Log.printLine("Generating new requests. This process may take a while ...");
		String script1_path = Paths.get("").toAbsolutePath().getParent().resolve("AI_code\\simulator_predict_dcs.py")
				.toString();
		String script2_path = Paths.get("").toAbsolutePath().getParent().resolve("AI_code\\simulator_predict_vms.py")
				.toString();
		String venv_python_exe_path = Paths.get("").toAbsolutePath().getParent()
				.resolve("AI_code\\.venv\\Scripts\\python.exe").toString();

		try {
			Log.printLine("Starting execution of script 1: " + script1_path);
			ProcessBuilder pb1 = new ProcessBuilder(venv_python_exe_path, script1_path);
			pb1.start();
			Log.printLine("Starting execution of script 2: " + script1_path);
			ProcessBuilder pb2 = new ProcessBuilder(venv_python_exe_path, script2_path);
			pb2.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void runScript2() {
		Log.printLine("Generating new requests. This process may take a while ...");
		String script1_path = Paths.get("").toAbsolutePath().getParent().resolve("AI_code\\simulator_predict_dcs.py")
				.toString();
		String script2_path = Paths.get("").toAbsolutePath().getParent().resolve("AI_code\\simulator_predict_vms.py")
				.toString();
		String venv_python_exe_path = Paths.get("").toAbsolutePath().getParent()
				.resolve("AI_code\\.venv\\Scripts\\python.exe").toString();

		try {
			Log.printLine("Starting execution of script 1: " + script1_path);
			ProcessBuilder pb1 = new ProcessBuilder(venv_python_exe_path, script1_path);
			pb1.redirectErrorStream(true);
			Process p1 = pb1.start();

			BufferedReader reader1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			String line1;
			while ((line1 = reader1.readLine()) != null) {
				Log.printLine(" output: " + line1);
			}

			int exitCode1 = p1.waitFor();
			Log.printLine("Script 1/2 completed with exit code: " + exitCode1);

			Log.printLine("Starting execution of script 2: " + script2_path);
			ProcessBuilder pb2 = new ProcessBuilder(venv_python_exe_path, script2_path);
			pb2.redirectErrorStream(true);
			Process p2 = pb2.start();

			BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
			String line2;
			while ((line2 = reader2.readLine()) != null) {
				Log.printLine(" output: " + line2);
			}

			int exitCode2 = p2.waitFor();
			Log.printLine("Script 2/2 completed with exit code: " + exitCode2);
		} catch (IOException | InterruptedException e) {
			Log.printLine("Error occurred while running scripts: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
