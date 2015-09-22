package hu.unimiskolc.iit.distsys;

import java.util.EnumMap;

import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.PowerState;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine.PowerStateKind;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine.ResourceAllocation;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine.State;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.AlterableResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;

public class VMC implements VMCreationApproaches{
	
	public void directVMCreation() throws Exception{
		
		PhysicalMachine pm1 = ExercisesBase.getNewPhysicalMachine();
		pm1.turnon();
		Timed.simulateUntilLastEvent();
		VirtualAppliance va = new VirtualAppliance("VirtualAppliance1", 34.0, 0);
		ResourceConstraints rc = new ConstantConstraints(0.25, 10.0, 128000);
		pm1.localDisk.registerObject(va);
		pm1.requestVM(va, rc, pm1.localDisk, 2);
		Timed.simulateUntilLastEvent();
	}

	public void twoPhaseVMCreation() throws Exception{
		
		PhysicalMachine pm1 = ExercisesBase.getNewPhysicalMachine();
		pm1.turnon();
		Timed.simulateUntilLastEvent();
		
		VirtualAppliance va1 = new VirtualAppliance("VirtualAppliance1", 34.0, 0);
		VirtualAppliance va2 = new VirtualAppliance("VirtualAppliance2", 34.0, 0);
		
		VirtualMachine vm1 = new VirtualMachine(va1);
		VirtualMachine vm2 = new VirtualMachine(va2);
		
		ResourceConstraints rc1 = new ConstantConstraints(0.25, 10.0, 128000);
		ResourceConstraints rc2 = new ConstantConstraints(0.25, 10.0, 128000);
		
		ResourceAllocation ra1 = pm1.allocateResources(rc1, false, 3);
		ResourceAllocation ra2 = pm1.allocateResources(rc2, false, 3);
		
		pm1.localDisk.registerObject(va1);
		pm1.localDisk.registerObject(va2);
		
		pm1.deployVM(vm1, ra1, pm1.localDisk);
		pm1.deployVM(vm2, ra2, pm1.localDisk);
		
		Timed.simulateUntilLastEvent();
	}

	public void indirectVMCreation() throws Exception{
		
		PhysicalMachine pm1 = ExercisesBase.getNewPhysicalMachine();		
		pm1.turnon();
		
		IaaSService iaas = ExercisesBase.getNewIaaSService();
		iaas.registerHost(pm1);
		iaas.registerRepository(pm1.localDisk);
		
		Timed.simulateUntilLastEvent();
		
		VirtualAppliance va1 = new VirtualAppliance("VirtualAppliance1", 34.0, 0);
		ResourceConstraints rc1 = new ConstantConstraints(0.25, 10.0, 128000);
		//ResourceAllocation ra1 = pm1.allocateResources(rc1, false, 3);
		pm1.localDisk.registerObject(va1);
		
		iaas.requestVM(va1, rc1, pm1.localDisk, 2);
		
		Timed.simulateUntilLastEvent();
	}

	public void migratedVMCreation() throws Exception{
		
		PhysicalMachine pm1 = ExercisesBase.getNewPhysicalMachine();		
		PhysicalMachine pm2 = ExercisesBase.getNewPhysicalMachine();		
		pm1.turnon();
		pm2.turnon();
		
		Timed.simulateUntilLastEvent();
		
		VirtualAppliance va1 = new VirtualAppliance("VirtualAppliance1", 34.0, 0);
		
		pm1.localDisk.registerObject(va1);
		pm2.localDisk.registerObject(va1);
		
		ResourceConstraints rc1 = new ConstantConstraints(0.25, 10.0, 128000);
		VirtualMachine vm1 = pm1.requestVM(va1, rc1, pm1.localDisk, 1)[0];
		
		Timed.simulateUntilLastEvent();
		
		pm1.migrateVM(vm1, pm2);
		
		Timed.simulateUntilLastEvent();
	}
	
}
