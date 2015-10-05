package hu.unimiskolc.iit.distsys;

import java.lang.reflect.InvocationTargetException;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
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
			VirtualAppliance va = new VirtualAppliance("VirtualAppliance", 10, 0, false,100000000l);
			ResourceConstraints rc = new ConstantConstraints(0.001, 10.0, 1248);
			//ResourceConstraints rc = new AlterableResourceConstraints(iaas.machines.get(0).getCapacities().getRequiredCPUs()-100, iaas.machines.get(0).getCapacities().getRequiredProcessingPower()-120, iaas.machines.get(0).getCapacities().getRequiredMemory()-11511);
			
			Repository repo = ExercisesBase.getNewRepository(1);
			repo.registerObject(va);
			VirtualMachine[] vms = iaas.requestVM(va, rc, repo, 100);
			
			Timed.simulateUntilLastEvent();
			
			for (int i=0; i<100; i++){
				System.out.println(vms[i].getState());				
			}			
			
			/*
			for (int i=0; i<10; i++){
				if (iaas.machines.get(i).freeCapacities.getRequiredCPUs() != 0)	
					
			}
			*/
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
