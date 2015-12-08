package hu.unimiskolc.iit.distsys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import hu.mta.sztaki.lpds.cloud.simulator.DeferredEvent;
import hu.mta.sztaki.lpds.cloud.simulator.helpers.job.Job;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine.State;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ConstantConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ConsumptionEventAdapter;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption;
import hu.mta.sztaki.lpds.cloud.simulator.io.Repository;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;
import hu.unimiskolc.iit.distsys.interfaces.BasicJobScheduler;

public class RRJSched implements BasicJobScheduler, VirtualMachine.StateChange {

	private IaaSService iaas;
	private Repository repo;
	private VirtualAppliance va;
	private boolean runningInCicle = false;
	private HashMap<VirtualMachine, Job> vmsWithPurpose = new HashMap<VirtualMachine, Job>();
	private HashMap<VirtualMachine, DeferredEvent> vmPool = new HashMap<VirtualMachine, DeferredEvent>();
	private int jobCounter = 0;
	private ArrayList<Integer> array75 = new ArrayList<Integer>();
	private ArrayList<Integer> array90 = new ArrayList<Integer>();
	private ArrayList<Integer> array95 = new ArrayList<Integer>();
	private ArrayList<Integer> array99 = new ArrayList<Integer>();

	public double checkAvailability(ArrayList<Integer> pArray) {
		int count0 = 0;
		int count1 = 0;

		for (int i = 0; i < pArray.size(); i++) {
			if (pArray.get(i).intValue() == 0) {
				count0++;
			} else {
				count1++;
			}
		}

		return (double) count1 / (count1 + count0);
	}

	public void setupVMset(Collection<VirtualMachine> vms) {

	}

	public void setupIaaS(IaaSService iaas) {
		this.iaas = iaas;
		repo = iaas.repositories.get(0);
		va = (VirtualAppliance) repo.contents().iterator().next();
	}

	public void handleJobRequestArrival(Job j) {
		try {
			ConstantConstraints cc = new ConstantConstraints(j.nprocs, ExercisesBase.minProcessingCap,
					ExercisesBase.minMem / j.nprocs);

			for (VirtualMachine vm : vmPool.keySet()) {
				if (vm.getState().toString() == "DESTROYED"
						|| vm.getResourceAllocation().allocated.getRequiredCPUs() >= j.nprocs) {
					vmPool.remove(vm).cancel();
					allocateVMforJob(vm, j);
					return;
				}
			}

			VirtualMachine vm = iaas.requestVM(va, cc, repo, 1)[0];
			vm.subscribeStateChange(this);
			vmsWithPurpose.put(vm, j);

			
			if (!runningInCicle) {
				runningInCicle = true;
				ComplexDCFJob newJob = new ComplexDCFJob((ComplexDCFJob) j);

				for (int i = 0; i < 7; i++) {
					handleJobRequestArrival(newJob);
				}

				runningInCicle = false;
			}
			

			/*
			if (!runningInCicle) {
				runningInCicle = true;
				ComplexDCFJob newJob = new ComplexDCFJob((ComplexDCFJob) j);

				if (newJob.getAvailabilityLevel() == 0.75) {
					for (int i = 0; i < 3; i++) {
						ComplexDCFJob newJob2 = new ComplexDCFJob((ComplexDCFJob) newJob);
						handleJobRequestArrival(newJob2);
					}
				}

				if (newJob.getAvailabilityLevel() == 0.9) {
					for (int i = 0; i < 5; i++) {
						ComplexDCFJob newJob2 = new ComplexDCFJob((ComplexDCFJob) newJob);
						handleJobRequestArrival(newJob2);
					}
				}

				if (newJob.getAvailabilityLevel() == 0.95) {
					for (int i = 0; i < 8; i++) {
						ComplexDCFJob newJob2 = new ComplexDCFJob((ComplexDCFJob) newJob);
						handleJobRequestArrival(newJob2);
					}
				}

				if (newJob.getAvailabilityLevel() == 0.99) {
					for (int i = 0; i < 15; i++) {
						ComplexDCFJob newJob2 = new ComplexDCFJob((ComplexDCFJob) newJob);
						handleJobRequestArrival(newJob2);
					}
				}

				runningInCicle = false;
			}
			*/

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	private void allocateVMforJob(final VirtualMachine vm, final Job j) {
		try {

			final ComplexDCFJob myJob = new ComplexDCFJob((ComplexDCFJob) j);

			jobCounter++;

			((ComplexDCFJob) j).startNowOnVM(vm, new ConsumptionEventAdapter() {

				@Override
				public void conComplete() {
					super.conComplete();
					vmPool.put(vm, new DeferredEvent(ComplexDCFJob.noJobVMMaxLife - 1000) {
						protected void eventAction() {
							try {
								vmPool.remove(vm);
								vm.destroy(false);
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						}
					});

					System.out.println(jobCounter + ".job -> Original job has finished! -> group: "
							+ ((ComplexDCFJob) j).getAvailabilityLevel());

					if (myJob.getAvailabilityLevel() == 0.75) {
						array75.add(1);
					}

					if (myJob.getAvailabilityLevel() == 0.9) {
						array90.add(1);
					}

					if (myJob.getAvailabilityLevel() == 0.95) {
						array95.add(1);
					}

					if (myJob.getAvailabilityLevel() == 0.99) {
						array99.add(1);
					}
				}

				@Override
				public void conCancelled(ResourceConsumption problematic) {
					System.out.println("job has crashed!");

					if (jobCounter < 30) {
						if (myJob.getAvailabilityLevel() == 0.75) {
							array75.add(1);
							handleJobRequestArrival(myJob);
							runningInCicle = true;
						}

						if (myJob.getAvailabilityLevel() == 0.9) {
							array90.add(1);
							handleJobRequestArrival(myJob);
							runningInCicle = true;
						}

						if (myJob.getAvailabilityLevel() == 0.95) {
							array95.add(1);
							handleJobRequestArrival(myJob);
							runningInCicle = true;
						}

						if (myJob.getAvailabilityLevel() == 0.99) {
							array95.add(1);
							handleJobRequestArrival(myJob);
							runningInCicle = true;
						}
					} else {
						if (myJob.getAvailabilityLevel() == 0.75) {
							if (checkAvailability(array75) > 0.75) {
								array75.add(0);
								runningInCicle = false;
							} else {
								handleJobRequestArrival(myJob);
								runningInCicle = true;
							}
						}

						if (myJob.getAvailabilityLevel() == 0.9) {
							if (checkAvailability(array90) > 0.9) {
								array90.add(0);
								runningInCicle = false;
							} else {
								handleJobRequestArrival(myJob);
								runningInCicle = true;
							}
						}

						if (myJob.getAvailabilityLevel() == 0.95) {
							if (checkAvailability(array95) > 0.95) {
								array95.add(0);
								runningInCicle = false;
							} else {
								handleJobRequestArrival(myJob);
								runningInCicle = true;
							}
						}

						if (myJob.getAvailabilityLevel() == 0.99) {
							if (checkAvailability(array95) > 0.97) {
								array99.add(0);
								runningInCicle = false;
							} else {
								handleJobRequestArrival(myJob);
								runningInCicle = true;
							}
						}
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void stateChanged(final VirtualMachine vm, State oldState, State newState) {
		if (newState.equals(VirtualMachine.State.RUNNING)) {
			allocateVMforJob(vm, vmsWithPurpose.remove(vm));
			vm.unsubscribeStateChange(this);
		}
	}
}
