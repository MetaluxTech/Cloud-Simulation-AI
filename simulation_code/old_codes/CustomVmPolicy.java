package myjava1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;

public class CustomVmPolicy extends VmAllocationPolicySimple {


    public CustomVmPolicy(List<? extends Host> list) {
        super(list);
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        Host host = getHostList().get(0); // get host with id in current dc
        for (Host hst : getHostList()) {
        
			Log.printLine("hostid: "+hst.getId());
		}
        if (host.getDatacenter().getId()==4)
        {
        return	super.allocateHostForVm(vm);
        	        }
    	return false;
    	
    }

    @Override
    public void deallocateHostForVm(Vm vm) {
        super.deallocateHostForVm(vm);
    }
}
