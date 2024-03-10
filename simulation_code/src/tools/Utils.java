package tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.cloudbus.cloudsim.Log;
import Costums.CustomCloudlet;
import Costums.CustomDataCenter;
import Costums.CustomVM;

public class Utils {

	public void print(String message) {
		Log.printLine(message);
	}

	public static double[] generateRandomLatLon() {
		Random random = new Random();
		double latitude = -90.0 + (90.0 - (-90.0)) * random.nextDouble();
		double longitude = -180.0 + (180.0 - (-180.0)) * random.nextDouble();
		return new double[] { latitude, longitude };
	}

	public static int getNextRandom(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	public static double getNextdouble(double min, double max) {
		Random RANDOM = new Random();
		return min + ((max - min) * RANDOM.nextDouble());
	}

	public static CustomDataCenter getDatacenterById(int dataCenterId, List<CustomDataCenter> datacentersList) {
		for (CustomDataCenter dc : datacentersList) {
			if (dc.getId() == dataCenterId) {
				return dc;
			}
		}
		return null;
	}

	public static CustomVM getVmWithLowestLoad(List<CustomVM> vms) {
		CustomVM LowestLoadVm = vms.stream().min(Comparator.comparing(CustomVM::getLoad)).orElse(null);

		return LowestLoadVm;

	}

	public static CustomDataCenter getDataCenterWithLowestLoad(List<CustomDataCenter> dcs) {

		CustomDataCenter LowestLoadDc = dcs.stream().min(Comparator.comparing(CustomDataCenter::getLoad)).orElse(null);

		return LowestLoadDc;

	}

	public static CustomVM getVMById(int VmId, List<CustomVM> vms) {

		for (CustomVM v : vms) {
			if (v.getId() == VmId) {
				return v;
			}
		}
		return null;
	}

	public static double calculateDistance(CustomCloudlet geotask, CustomDataCenter geoDC) {
		try {
			double lat1 = geotask.getLatitude();
			double lon1 = geotask.getLongitude();
			double lat2 = geoDC.getLatitude();
			double lon2 = geoDC.getLongitude();

			// Convert degrees to radians
			lat1 = Math.toRadians(lat1);
			lon1 = Math.toRadians(lon1);
			lat2 = Math.toRadians(lat2);
			lon2 = Math.toRadians(lon2);

			// Earth's radius in kilometers
			double earthRadius = 6371.01;

			// Haversine formula
			double dlon = lon2 - lon1;
			double dlat = lat2 - lat1;
			double a = Math.pow(Math.sin(dlat / 2), 2)
					+ Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

			double c1 = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

			return Math.floor(earthRadius * c1);
		} catch (Exception e) {
			return -1;
		}
	}

	public static List<CustomVM> extractDataCenterVms(List<CustomVM> allVMs, Integer DataCenterId) {
		List<CustomVM> selectedVMs = new ArrayList<>();
		List<Integer> dc_vms_ids = DCs_Caculations.DCsVmsMap.get(DataCenterId);
		for (CustomVM vm : allVMs) {
			if (dc_vms_ids.contains(vm.getId())) {
				selectedVMs.add(vm);
			}
		}

		return selectedVMs;
	}

	public static int findDatacenterIdForVm(int vmId) {
		for (Map.Entry<Integer, List<Integer>> entry : DCs_Caculations.DCsVmsMap.entrySet()) {
			if (entry.getValue().contains(vmId)) {
				return entry.getKey();
			}
		}
		return -1; // Default value if VM ID is not found
	}

	public static double normalize(double value, double minValue, double maxValue) {
		double num = (value - minValue) / (maxValue - minValue);

		return Math.round(num * 100.0) / 100.0;
	}

	
}