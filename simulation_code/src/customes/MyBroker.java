package customes;

import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;

public class MyBroker extends DatacenterBroker {

	   public MyBroker(String name) throws Exception {
	       super(name);
	   }

//	   @Override
	   protected Datacenter defaultVmMapper(Vm vm) {
	       // Implement your own logic here to determine the datacenter for the VM
	       // For example, you could select a datacenter based on its location, capacity, etc.
	       Datacenter chosenDatacenter = chooseDatacenterForVm(vm);
	       return chosenDatacenter;
	   }

	   private Datacenter chooseDatacenterForVm(Vm vm) {
		   // This is just a placeholder. Replace this with your own logic.
		   List<Integer> datacenterIds = getDatacenterIdsList();
		   Log.printLine("data centers ids : "+datacenterIds);
		   if (!datacenterIds.isEmpty()) {
		       int id = datacenterIds.get(0);
		       SimEntity entity = CloudSim.getEntity(id);
		       if (entity instanceof Datacenter) {
		           return (Datacenter) entity;
		       }
		   }
		   return null;
		  }


	}