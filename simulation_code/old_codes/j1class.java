package myjava1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

public class j1class {

	private static List<Datacenter> datacentersList = new ArrayList<>();
	private static List<Host> hostsList = new ArrayList<>();  
	private static List<Cloudlet> cloudletsList;
	private static List<Vm> vmsList;
	
	public static void main(String[] args) {
		vmsList = new ArrayList<Vm>();
		cloudletsList = new ArrayList<Cloudlet>();
     	int numUsers = 3; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = true; // mean trace events

			CloudSim.init(numUsers, calendar, trace_flag);
			
			
			//DCs hosts specification
			double DcLongt=45.258;
			double DcLatit=45.258;
			int hostMips = 1000; //miiliion instruction per sec
			int hostRam = 2048;         //host memory (MB)
			long hostStorage = 1000000; //host storage in ( GB )
			int hostBw = 10000;  //MBPs
			
			//Creat DCs and Hosts
			for (int i=1;i<=numUsers;i++)
			{	
			Datacenter dc=	functions.createDatacenter( "DC_"+Integer.toString(i),i, hostStorage, hostMips, hostRam, hostBw,DcLongt,DcLatit);
			datacentersList.add(dc);
			hostsList.add(dc.getHostList().get(0));
			}

				//create broker
 			DatacenterBroker broker = functions.createBroker("Broker_1");
 			int  brokerId=broker.getId();

			
			//vms specification 
 			
			int vmMips = 250;
			long vmstorage = 10000; //vm storage (MB)
			int vmRam = 512; // vm memory (MB)
			long vmBw = 1000;
			int vmPesNumber = 1; // number of cpus
			String vmm = "Xen" ; // VMM name
			// create VM

				for (int i=1;i<=numUsers;i++)
				{
					 Vm vm = new Vm(i, brokerId, vmMips, vmPesNumber, vmRam, vmBw,vmstorage, vmm, new CloudletSchedulerTimeShared());
					 vmsList.add(vm);
				}
					
						
			//cloudlets spesification
			double cletLongt=45.258;
			double cletLatit=45.258;
		 	long cletLength = 40000;
		    long cletFileSize = 300;
		    long cletOutputSize = 300;
		    int clletPesNumber = 1;
		    
			
			//create Cloudletes
		    for (int i = 1; i <= numUsers*3; i++) {
		    Cloudlet cloudlet=functions.createCloudlets(i, cletLength, cletFileSize, cletOutputSize, clletPesNumber, brokerId,cletLongt,cletLatit);
		    cloudletsList.add(cloudlet);
		    }

		    broker.submitVmList(vmsList);
		    broker.submitCloudletList(cloudletsList);
			
			
			CloudSim.startSimulation();

			CloudSim.stopSimulation();
			
			functions.printCloudletList(cloudletsList);
			functions.printResourceIds(datacentersList, hostsList, vmsList, cloudletsList);
			Log.printLine("\n distance= "+functions.calculateDistance(40.730610, -73.935242, 34.052235, -118.243683)+" km\n");
			try {
				API.postBudget("25") ;
				Log.printLine("post username done to api");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.printLine("error in post user name");
			}
		    
	}
}

