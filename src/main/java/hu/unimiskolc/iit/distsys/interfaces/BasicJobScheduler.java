/*
 *  ========================================================================
 *  dcf-exercises
 *  ========================================================================
 *  
 *  This file is part of dcf-exercises.
 *  
 *  dcf-exercises is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or (at
 *  your option) any later version.
 *  
 *  dcf-exercises is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with dcf-exercises.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  (C) Copyright 2015, Gabor Kecskemeti (kecskemeti@iit.uni-miskolc.hu)
 */
package hu.unimiskolc.iit.distsys.interfaces;

import java.util.Collection;

import hu.mta.sztaki.lpds.cloud.simulator.helpers.job.Job;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VirtualMachine;

public interface BasicJobScheduler {
	/**
	 * Use this function to set up a scheduler that should operate on a
	 * predefined VM set.
	 * 
	 * @param vms
	 */
	void setupVMset(Collection<VirtualMachine> vms);

	/**
	 * use this function to set up a scheduler that should operate on a dynamic
	 * VM set requested from an IaaS service.
	 * 
	 * @param iaas
	 */
	void setupIaaS(IaaSService iaas);

	/**
	 * This function is called once a job arrives to the system.
	 * 
	 * @param j
	 */
	void handleJobRequestArrival(Job j);
}
