package server;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

public class API {
	
	
	
	public static Map<String, Object> buildDictionary(List<Datacenter> datacenters, List<Cloudlet> tasks) {
        Map<String, Object> dictionary = new HashMap<>();
        Map<String, Object> DCMap = new HashMap<>();
        Map<String, Object> taskMap = new HashMap<>();

        // Iterate over the datacenters to build the DATA_CENTERS dictionary
        for (Datacenter dc : datacenters) {
            Map<String, Object> DCData = new HashMap<>();
            List<Host> hosts = dc.getHostList();
            if (hosts != null && !hosts.isEmpty()) {
                Host host = hosts.get(0);  // Assuming only one host per datacenter for simplicity
                DCData.put("longitude", host.getDatacenter().getId());
                DCData.put("latitude", host.getDatacenter().getId());
                DCData.put("hostMips", host.getTotalMips());
                DCData.put("hostRam", host.getRamProvisioner().getRam());
                DCData.put("hostStorage", host.getStorage());
                DCData.put("hostBw", host.getBwProvisioner().getBw());

                // Extract VMs data for this Datacenter
                Map<String, Object> VMMap = new HashMap<>();
                Log.printLine("host vms here in dict :"+host.getVmList() );
                for (Vm vm : host.getVmList()) {
                    Map<String, Object> VMData = new HashMap<>();
                    VMData.put("vmMips", vm.getMips());
                    VMData.put("vmstorage", vm.getSize());
                    VMData.put("vmRam", vm.getRam());
                    VMData.put("vmBw", vm.getBw());
                    VMMap.put(String.valueOf(vm.getId()), VMData);
                }
                DCData.put("VMS", VMMap);
            }
            DCMap.put("DC_" + dc.getId(), DCData);
        }

        // Iterate over the tasks (Cloudlets) to build the TASKS dictionary
        for (Cloudlet task : tasks) {
            Map<String, Object> taskData = new HashMap<>();
            taskData.put("longitude", task.getVmId());
            taskData.put("latitude", task.getVmId());
            taskMap.put("Task_" + task.getCloudletId(), taskData);
        }

        dictionary.put("DATA_CENTERS", DCMap);
        dictionary.put("TASKS", taskMap);
       
        return dictionary;
    }

	public static void postLatency(int latency) throws Exception {
	    URL url = new URL("http://127.0.0.1:5000/optimize");
	    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	    httpCon.setDoOutput(true);
	    httpCon.setRequestMethod("POST");
	    httpCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	    httpCon.setRequestProperty("Accept", "application/json");

	    String jsonInputString = "{\"latency_value\":\"" + latency + "\"}";

	    try (OutputStream os = httpCon.getOutputStream()) {
	        byte[] input = jsonInputString.getBytes("utf-8");
	        os.write(input, 0, input.length);
	    }

	    int responseCode = httpCon.getResponseCode();
	    String response_msg = httpCon.getResponseMessage();
	    System.out.println("POST Response Code :: " + responseCode);
	    System.out.println("POST Response Message :: " + response_msg);

	    httpCon.disconnect();
	}

}


