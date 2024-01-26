package myjava1;

import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;

public class CustomTasksPolicy {

	public class CustomCloudletPolicy extends VmAllocationPolicySimple {

	    public CustomCloudletPolicy(List<? extends Host> list) {
	        super(list);
	    }

	    @Override
	    public boolean allocateHostForVm(Vm vm) {
	        Host host = getHostList().get(0); // get host with ID in the current datacenter

	        // Check if the host belongs to the datacenter with ID 4
	        if (host.getDatacenter().getId() == 4) {
	            // Check if the host has sufficient resources for the VM
	            if (host.getVmScheduler().allocatePesForVm(vm, vm.getCurrentRequestedMips())) {
	                // Allocate the VM to the host
	                host.vmCreate(vm);
	                return true;
	            }
	        }

	        return false; // VM will not be allocated on this host
	    }

	    @Override
	    public void deallocateHostForVm(Vm vm) {
	        Host host = vm.getHost();
	        if (host != null) {
	            host.vmDestroy(vm);
	        }
	        super.deallocateHostForVm(vm);
	    }
	}
	
	
}
