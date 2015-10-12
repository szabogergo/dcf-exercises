package hu.unimiskolc.iit.distsys;

import java.util.ArrayList;
import java.util.Collection;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.energy.MonitorConsumption;
import hu.mta.sztaki.lpds.cloud.simulator.helpers.job.Job;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.AlterableResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;
//import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ConsumptionEventAdapter;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceSpreader;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption.ConsumptionEvent;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.unimiskolc.iit.distsys.interfaces.BasicJobScheduler;
import hu.unimiskolc.iit.distsys.ComplexDCFJob;

public class RRJSched implements BasicJobScheduler{

	private ArrayList<VirtualMachine> vm;
	private int jobCounter = 1;
	 
	class MyMonitor extends MonitorConsumption{
		
		public MyMonitor(ResourceSpreader toMonitor) {
			super(toMonitor);						
		}
		
		public void stopMonitor(ConsumptionEvent event){
			unsubscribe();
		}
	}
	
	@Override
	public void setupVMset(Collection<VirtualMachine> vms) {
	}

	@Override
	public void setupIaaS(IaaSService iaas) {
		try{
			VirtualAppliance va = (VirtualAppliance) iaas.repositories.get(0).lookup("mainVA");			
			
			Repository repo;
			repo = iaas.repositories.get(0);
			
			ResourceConstraints rc;
			double core = iaas.machines.get(0).freeCapacities.getRequiredCPUs();
			double freq = iaas.machines.get(0).freeCapacities.getRequiredProcessingPower();
			long mem = iaas.machines.get(0).freeCapacities.getRequiredMemory();
			rc = new AlterableResourceConstraints(core, freq, mem);
			
			vm.add(iaas.requestVM(va, rc, repo, 1)[0]);
			int lastVM = vm.size()-1;
			new MyMonitor((ResourceSpreader) vm.get(lastVM));
			
			Timed.simulateUntilLastEvent();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
		
	
	
	@Override
	public void handleJobRequestArrival(Job j) {
		try{
			
			final ComplexDCFJob myJob = (ComplexDCFJob) j;
			
			// list jobs
			System.out.println(myJob.toString());
			// run jobs
			myJob.startNowOnVM(vm.get(0), new ConsumptionEvent(){

				@Override
				public void conComplete() {
					System.out.println("Job completed -> ID: "+myJob.getId());					
					//checkUtilization();
					//myJob.nprocs
					for (int i=0; i<vm.size(); i++){
						System.out.println(vm.get(i).getState());	
					}
					
					jobCounter++;
				}

				@Override
				public void conCancelled(ResourceConsumption problematic) {					
					
				}
				
			});
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
			
	}

}
