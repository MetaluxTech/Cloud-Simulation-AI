package myjava1;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

public class CustomBroker extends DatacenterBroker {

    public CustomBroker(String name) throws Exception {
        super(name);
    }

    public void submitCloudletToVm(Cloudlet cloudlet, Vm vm) {
        // Assign the cloudlet to this VM
        cloudlet.setVmId(vm.getId());

        // Print out a message
        Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet " + cloudlet.getCloudletId() + " to VM #" + vm.getId());

        // Submit the cloudlet
//        super.submitCloudlet(cloudlet);
    }

}