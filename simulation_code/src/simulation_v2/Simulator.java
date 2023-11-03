package simulation_v2;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

public class Simulator {
	private static List<Datacenter> dcs_list;
	private static List<Host> hosts_list;
	private static List<Vm> vms_List;
	private static List<Cloudlet> tasks_List;
	
	@SuppressWarnings("unused")
    public static void main(String[] args) {
		
		vms_List = new ArrayList<Vm>();
    	tasks_List = new ArrayList<Cloudlet>();
    	dcs_list = new ArrayList<Datacenter>();
		hosts_list = new ArrayList<Host>();
		
        try {
    		Log.printLine("      Starting CloudSimulation   ");

            int num_users = 1;
            int num_dc=1;
            int num_vms=3;
            int num_tasks=50;
            
            Calendar clndr = Calendar.getInstance();
            boolean trace_actions = false;
            CloudSim.init(num_users,clndr,trace_actions);
            
          //DCs hosts specification
			double DcLongt=45.258;
			double DcLatit=45.258;
			int hostMips = 1000000; //miiliion instruction per sec
			int hostRam = 2048;         //host memory (MB)
			long hostStorage = 1000000; //host storage in ( GB )
			int hostBw = 10000;  //MBPs
			
			for (int i=1;i<=num_dc;i++)
			{	
			Datacenter dc=	simulation_functions.createDatacenter( "DC_"+Integer.toString(i),i, hostStorage, hostMips, hostRam, hostBw,DcLongt,DcLatit);
			dcs_list.add(dc);
			hosts_list.add(dc.getHostList().get(0));
			}
			
			
            DatacenterBroker   broker1=simulation_functions.createBroker("broker1");          
            int vm_id=1;
//            int vm_mips=1000; /// instructions per second 
            long vm_size=10000;
            int vm_ram=512;
            int vm_bandwidth=1000;
            int vm_pesNum=1 ;  //num of cpus in the VM
            String vm_monitor="xen"; 
            Random rand1 = new Random();

           for (int i=1;i<=num_vms;i++)
			{   
        	int vm_mips = rand1.nextInt(9000) + 1000;//between 1000 and 10000 
            Vm vm=new Vm (i, broker1.getId(), vm_mips , vm_pesNum, vm_ram, vm_bandwidth, vm_size , vm_monitor , new CloudletSchedulerSpaceShared());
            vms_List.add(vm);
            }
            broker1.submitVmList(vms_List);
            int task_id=0;
//            long task_length=5000;  ///  how many instructions in it    it is with relation with vms mips
            int task_size = 300;
            int task_out_size=300;
            int task_pesNum=1 ;  //num of cpus in the VM
            UtilizationModel full_utl_model=new UtilizationModelFull();
            
            for (int i=1;i<=num_tasks;i++)
			{ 
            	int task_length = rand1.nextInt(5000) + 500;
            Cloudlet task= new Cloudlet(i, task_length, task_pesNum, task_size, task_out_size, full_utl_model, full_utl_model, full_utl_model, trace_actions);
            task.setUserId(broker1.getId());
//  //          task.setVmId(1);
            tasks_List.add(task);
			}
            
           
            broker1.submitCloudletList(tasks_List);            
            CloudSim.startSimulation();
            CloudSim.stopSimulation();
            
            
            /// icon cloud **
            List<Cloudlet> tasksList = broker1.getCloudletReceivedList();
            utils.PrintTasks(tasksList);
            Map<String, Object> resources_dict=API.buildDictionary(dcs_list, tasks_List);

            String result_file_path = utils.write_to_csv(tasksList);
            String resources_file_path = utils.write_resources_to_csv(resources_dict);
			 

 //                  Log.printLine();
            utils.printResourcesList(dcs_list, hosts_list, vms_List, tasks_List);
         
            
            System.out.println("CSV file saved at: " + result_file_path);
            System.out.println("CSV resources saved at: " + resources_file_path);
            System.out.println("Dictionary : \n" + resources_dict);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      
    }
}




