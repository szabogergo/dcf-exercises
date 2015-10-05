package hu.unimiskolc.iit.distsys;

import java.lang.reflect.InvocationTargetException;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.unimiskolc.iit.distsys.interfaces.FillInAllPMs;

public class PMFiller implements FillInAllPMs{

	@Override
	public void filler(IaaSService iaas, int vmCount) {
		
		VirtualAppliance va = new VirtualAppliance("VirtualAppliance", 10, 0, false,100000000l);
		ResourceConstraints rc = new ConstantConstraints(0.25, 10.0, 128000);
		
		try{					
			Repository repo = ExercisesBase.getNewRepository(1);
			repo.registerObject(va);
			iaas.requestVM(va, rc, repo, vmCount);
			
			
			
			Timed.simulateUntilLastEvent();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
