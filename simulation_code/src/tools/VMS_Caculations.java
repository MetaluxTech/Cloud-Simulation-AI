package tools;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;

import Costums.GeoCloudlet;
import Costums.GeoDatacenter;
import Costums.MyVm;
import simulation_1.Simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class VMS_Caculations {
	
	 public static double calculateTaskExcutionTime(MyVm vm,GeoCloudlet cloudlet,GeoDatacenter dataCenter)
	 {
		  double task_lenth = cloudlet.getCloudletLength();
		    double process_speed=vm.getMips();
		    double task_executioin_time = task_lenth /process_speed;
		    
		    return Math.round(task_executioin_time * 100.0) / 100.0; 
	 }
	 public static double calculateTaskExceutionCost(GeoCloudlet cloudlet,GeoDatacenter dataCenter) // cost execution task
		{
			double cpuCost = dataCenter.getPublicCharacteristics().getCostPerMi();
	        double ramCost = dataCenter.getPublicCharacteristics().getCostPerMem();
	        double storageCost = dataCenter.getPublicCharacteristics().getCostPerStorage();
	        double bandwidthCost = dataCenter.getPublicCharacteristics().getCostPerBw();
	       
		    double exe = cloudlet.getActualCPUTime();
		    double r = cloudlet.getCloudletFileSize();
		    double st = cloudlet.getCloudletOutputSize();
		    double f = cloudlet.getCloudletFileSize();
		    double cet = (exe * cpuCost) + (r * ramCost) + (st * storageCost) + (f * bandwidthCost);
		    	cet=cet/100;
		    return Math.round(cet * 100.0) / 100.0;
		    
		    //8000
		}
 	 public static double calculateDataTransfareTime(GeoCloudlet cloudlet, MyVm vm)
	 {
		  double task_lenth = cloudlet.getCloudletLength();
		    double bw=vm.getBw();
		    double delayN = task_lenth /bw;
		    delayN=delayN*1.5;
		    return Math.round(delayN * 100.0) / 100.0;
	 }
	 public static double calculateDataTransfareCost(GeoCloudlet cloudlet,GeoDatacenter dataCenter, MyVm vm)
	 {
		 double dtt=calculateDataTransfareTime(cloudlet,vm);
		 double band_cost = dataCenter.getPublicCharacteristics().getCostPerBw();
		 double datatrnsfarecost=dtt*band_cost;
		 return datatrnsfarecost;
	       
	 }
	 public static double calculateHostCapacity(List<GeoCloudlet> host_tasks,GeoDatacenter dataCenter)
	 {
		 	return 1.0;
		 
	 }
	 
	 public static double FindRank(double tet, double tec, double dtt,double dtc,double hc) 
	 {
//		 f1 (v) = Min (p, TET, TEC, DTT, DTC, hostC)
//		 f2 (v) = Sum (p, TET, TEC, DTT, DTC, hostC) 
//		 rank (v) =f1 (v)*0.5 + f2 (v) *0.5 
		 return 1.5;
	 }

	public static MyVm getBestVM(GeoCloudlet cloudlet, List<GeoDatacenter> data_centers_list,List<MyVm> vms_list) 
	{
		List<MyVm>bestVmsList=null;
		List<Double>Ranks_Array=null;
	    double MaxObjFunction = 99999999999999.0;
	    MyVm best_vm=null;
	    for (MyVm vm : vms_list) {
	    	int dc_id=Tools.findDatacenterIdForVm(vm.getId());
	    	GeoDatacenter dc=Tools.getDatacenterById(dc_id, data_centers_list);
	    	double tet = calculateTaskExcutionTime(vm,cloudlet, dc);
	    	double tec = calculateTaskExceutionCost(cloudlet, dc);
	    	double dtt = calculateDataTransfareTime(cloudlet, vm);
	    	double dtc = calculateDataTransfareCost(cloudlet,dc, vm);
	    	double hc = calculateHostCapacity(null,dc);
	        double RANK = FindRank(tet, tec, dtt,dtc,hc);
	        bestVmsList.add(vm);
	        Ranks_Array.add(RANK);
	        
	            }
	    int indexOfSmallestRank = IntStream.range(0, Ranks_Array.size())
	    	    .boxed()
	    	    .min(Comparator.comparing(i -> Ranks_Array.get(i)))
	    	    .orElse(-1);

		best_vm=bestVmsList.get(indexOfSmallestRank);
		
		return best_vm;
	        }
	    
	    
		
	
	
}


