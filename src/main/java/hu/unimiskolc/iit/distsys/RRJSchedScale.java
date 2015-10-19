/*
package hu.unimiskolc.iit.distsys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.energy.MonitorConsumption;
import hu.mta.sztaki.lpds.cloud.simulator.helpers.job.Job;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VMManager.VMManagementException;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.AlterableResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;
//import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ConsumptionEventAdapter;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceSpreader;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption.ConsumptionEvent;
import hu.mta.sztaki.lpds.cloud.simulator.io.NetworkNode.NetworkException;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.unimiskolc.iit.distsys.interfaces.BasicJobScheduler;
import hu.unimiskolc.iit.distsys.ComplexDCFJob;

public class RRJSchedScale implements BasicJobScheduler{

	private ArrayList<VirtualMachine> vm = new ArrayList<VirtualMachine>();
	private ArrayList<MyMonitor> monitor = new ArrayList<MyMonitor>();
	IaaSService iaas;
	private int jobCounter = 1;

	class MyMonitor extends MonitorConsumption{
		
		public double diff = 0.0;
		
		public MyMonitor(ResourceSpreader toMonitor) {
			super(toMonitor);						
		}
		
		public void stopMonitor(ConsumptionEvent event){
			unsubscribe();
		}
	}
	
	public static VirtualMachine createNewVM(IaaSService iaas) throws VMManagementException, NetworkException{
		
		VirtualAppliance va = (VirtualAppliance) iaas.repositories.get(0).lookup("mainVA");			
		
		Repository repo;
		repo = iaas.repositories.get(0);
		
		ResourceConstraints rc;
		double core = iaas.machines.get(0).freeCapacities.getRequiredCPUs();
		double freq = iaas.machines.get(0).freeCapacities.getRequiredProcessingPower();
		long mem = iaas.machines.get(0).freeCapacities.getRequiredMemory();
		rc = new AlterableResourceConstraints(core, freq, mem);
		
		return iaas.requestVM(va, rc, repo, 1)[0];
	}
	
	@Override
	public void setupVMset(Collection<VirtualMachine> vms) {
	}

	@Override
	public void setupIaaS(IaaSService iaas) {
		try{
			this.iaas = iaas;			
			vm.add(RRJSched.createNewVM(iaas));
			monitor.add(new MyMonitor((ResourceSpreader) vm.get(vm.size()-1)));
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
		
	@Override
	public void handleJobRequestArrival(Job j) {
		try{
			
			final ComplexDCFJob myJob = (ComplexDCFJob) j;
			
			myJob.startNowOnVM(vm.get(0), new ConsumptionEvent(){

				@Override
				public void conComplete() {
					
					System.out.println();
					System.out.println("Job completed -> ID: "+myJob.getId());
					System.out.println("Execution time: "+myJob.getExectimeSecs()+" ms");
					System.out.println("Real execution time: "+(myJob.getRealstopTime() - myJob.getRealqueueTime())+" ms");
					System.out.println("Job counter: "+jobCounter);
					
					for (int i=0; i<vm.size(); i++){
						System.out.println(i+". VM state: "+vm.get(i).getState());							
					}
					
					for (MyMonitor mm : monitor){
						
						System.out.println("SubSecondProcessing: "+mm.getSubSecondProcessing());
						mm.diff = Math.abs(mm.getSubSecondProcessing() - mm.diff);
						
						System.out.println("Processing difference: "+mm.diff);						
						
						
						if (mm.diff < 100000){
							try{
								vm.add(RRJSched.createNewVM(iaas));
								monitor.add(new MyMonitor((ResourceSpreader) vm.get(vm.size()-1)));
							}
							catch (Exception e){
								e.printStackTrace();
							}
						}
						
					}
					
					jobCounter++;
					
					if (jobCounter == 1000){
						for (MyMonitor mm : monitor){
							mm.stopMonitor(new ConsumptionEvent() {
								
								@Override
								public void conComplete() {
									// TODO Auto-generated method stub									
								}
								
								@Override
								public void conCancelled(ResourceConsumption problematic) {
									// TODO Auto-generated method stub
								}
							});
						}
						
						System.out.println();	
						System.out.println("Terminating...");
					}
					
					
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
*/