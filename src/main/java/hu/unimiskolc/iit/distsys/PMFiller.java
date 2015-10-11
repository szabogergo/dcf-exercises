package hu.unimiskolc.iit.distsys;

import java.lang.reflect.InvocationTargetException;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine.ResourceAllocation;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.AlterableResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.UnalterableConstraintsPropagator;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.Scheduler;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.unimiskolc.iit.distsys.interfaces.FillInAllPMs;

public class PMFiller implements FillInAllPMs{

	@Override
	public void filler(IaaSService iaas, int vmCount) {
		
		try{					
			VirtualAppliance va = (VirtualAppliance) iaas.repositories.get(0).lookup("mainVA");			
			
			Repository repo;
			ResourceConstraints rc;
			
			if (!iaas.repositories.isEmpty())
				repo = iaas.repositories.get(0);
			else
				repo = ExercisesBase.getNewRepository(1);
			
			repo.registerObject(va);
			
			// -------------------------------------------------------------------------------------
			// utilizator v1
			// -------------------------------------------------------------------------------------
			/*
			VirtualMachine[] vms = new VirtualMachine[100];	
			
			double cpuCoreResourceSum;
			double cpuFreqResourceSum;
			 
			for (int i=0, iterator=0; i<10; i++){	
			
				cpuCoreResourceSum = iaas.machines.get(i).freeCapacities.getRequiredCPUs() / 10.0;
				cpuFreqResourceSum = iaas.machines.get(i).freeCapacities.getRequiredProcessingPower() / 10.0;
								
				System.out.println(i+". PM -> Core: "+cpuCoreResourceSum+", Freq:"+cpuFreqResourceSum);
				
				for (int j=0; j<10; j++){					
					rc = new ConstantConstraints(cpuCoreResourceSum/10.0, cpuFreqResourceSum/10.0, 1);					
					vms[iterator] = iaas.requestVM(va, rc, repo, 1)[0];					
					iterator++;	
				}
			}	
			*/
			
			// -------------------------------------------------------------------------------------
			// utilizator v2
			// -------------------------------------------------------------------------------------
			/*
			double cpuCoreResourceSum = 0.0;
			double cpuFreqResourceSum = 0.0;
			double maxCpuCore = iaas.machines.get(0).freeCapacities.getRequiredCPUs();
			
			for (int i=0; i<10; i++){
				
				cpuCoreResourceSum += iaas.machines.get(i).freeCapacities.getRequiredCPUs();
				cpuFreqResourceSum += iaas.machines.get(i).freeCapacities.getRequiredProcessingPower();
				
				if (iaas.machines.get(i).freeCapacities.getRequiredCPUs() > maxCpuCore)
					maxCpuCore = iaas.machines.get(i).freeCapacities.getRequiredCPUs();
				
				System.out.println(i+". PM -> Core: "+iaas.machines.get(i).freeCapacities.getRequiredCPUs()+", Freq:"+iaas.machines.get(i).freeCapacities.getRequiredProcessingPower());
			}
			
			System.out.println();
			System.out.println("Core: "+cpuCoreResourceSum +", Freq: "+cpuFreqResourceSum+", maxCpuCore: "+maxCpuCore);
						
			VirtualMachine[] vms = new VirtualMachine[vmCount];
			
			for (int i=100-vmCount; i<100 ;i++){
				
				if ((cpuCoreResourceSum/i) > maxCpuCore){
					
					double actualPart = cpuCoreResourceSum/(100-i);
					System.out.println();
					System.out.println(i+". iteration, value: "+actualPart+" remaining: "+cpuCoreResourceSum);
					
					rc = new AlterableResourceConstraints(maxCpuCore, cpuFreqResourceSum/(100-i), 1);
					cpuCoreResourceSum -= maxCpuCore;
					cpuFreqResourceSum -= (cpuFreqResourceSum/(100-i));
				}
				else{
					double actualPart = cpuCoreResourceSum/(100-i);
					System.out.println();
					System.out.println(i+". iteration, value: "+actualPart+" remaining: "+cpuCoreResourceSum);
					
					rc = new AlterableResourceConstraints(cpuCoreResourceSum/(100-i), cpuFreqResourceSum/(100-i), 1);
					cpuCoreResourceSum -= (cpuCoreResourceSum/(100-i));
					cpuFreqResourceSum -= (cpuFreqResourceSum/(100-i));
				}				
				
				vms[i] = iaas.requestVM(va, rc, repo, 1)[0];
			}
			*/
			
			// -------------------------------------------------------------------------------------
			// utilizator v3
			// -------------------------------------------------------------------------------------
			
			double cpuCoreResourceSum = 0.0;
			double cpuFreqResourceSum = 0.0;			
			
			for (int i=0; i<10; i++){
				
				cpuCoreResourceSum += iaas.machines.get(i).freeCapacities.getRequiredCPUs();
				cpuFreqResourceSum += iaas.machines.get(i).freeCapacities.getRequiredProcessingPower();
				
				System.out.println(i+". PM -> Core: "+iaas.machines.get(i).freeCapacities.getRequiredCPUs()+", Freq:"+iaas.machines.get(i).freeCapacities.getRequiredProcessingPower());
			}
			
			System.out.println();
			System.out.println("Summary -> Core: "+cpuCoreResourceSum +", Freq: "+cpuFreqResourceSum);
			System.out.println();
						
			VirtualMachine[] vms = new VirtualMachine[vmCount];
			
			for (int i=0; i<10; i++){
					
				double reqCpu = iaas.machines.get(i).freeCapacities.getRequiredCPUs();
				double reqFreq = iaas.machines.get(i).freeCapacities.getRequiredProcessingPower();
				
				if (i<9){										
					rc = new AlterableResourceConstraints(reqCpu, reqFreq, 1);
					vms[i] = iaas.requestVM(va, rc, repo, 1)[0];
					
					cpuCoreResourceSum -= reqCpu;
					cpuFreqResourceSum -= reqFreq;
					
					System.out.println(i +". iteration -> Core: "+cpuCoreResourceSum+", freq: "+cpuFreqResourceSum);
					
				}
				else{
					/*
					double coreSubtrPart = cpuCoreResourceSum/91;
					double freqSubtrPart = cpuFreqResourceSum/91;
					
					System.out.println();
					System.out.println("corepart: "+coreSubtrPart+", freqpart: "+freqSubtrPart);
					System.out.println();
					*/
					for (int j=9; j<100; j++){
						
						double coreSubtrPart = cpuCoreResourceSum/(100-j);
						double freqSubtrPart = cpuFreqResourceSum/(100-j);						
						
						System.out.println("coreSubtrPart: "+coreSubtrPart+", freqSubtrPart: "+freqSubtrPart);
						
						rc = new AlterableResourceConstraints(coreSubtrPart, freqSubtrPart, 1);						
						vms[j] = iaas.requestVM(va, rc, repo, 1)[0];
						
						cpuCoreResourceSum -= coreSubtrPart;
						cpuFreqResourceSum -= freqSubtrPart;
						
						System.out.println(j +". iteration -> Core: "+cpuCoreResourceSum+", freq: "+cpuFreqResourceSum);
						
					}
					
				}				
								
			}
			
			// -------------------------------------------------------------------------------------
			
			Timed.simulateUntilLastEvent();
						
			System.out.println();
			System.out.println("Physical machines' state: ");
			System.out.println("*******************************************");
			
			for (int i=0; i<10; i++){
				System.out.println(i+".machine - State: "+ iaas.machines.get(i).getState() + ", Available capacity: " + iaas.machines.get(i).availableCapacities + ", Free capacity: " + iaas.machines.get(i).freeCapacities);				
			}						
			
			System.out.println();
			System.out.println("   ---");
			System.out.println();
			
			System.out.println("Virtual machines' state: ");
			System.out.println("*******************************************");
			
			for (int i=0; i<100; i++){
				System.out.println(i+".machine - State: "+vms[i].getState());				
			}			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
