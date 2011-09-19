package org.usip.osp.coursemanagementinterface;

import java.util.*;

import org.usip.osp.baseobjects.*;
import org.usip.osp.networking.*;

/**
 * This class encapsulates the information on a line describing a running
 * simulation.
 * 
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 */
public class RunningSimulationInformationLine {
	
	private Long simId;
	
	private Long rsId;
	
	private Long aId;
	
	private String simName = "";
	
	private String rsName = "";
	
	private String phaseName = "";
	
	private boolean enabled = false;
	
	private String timeZone = "";

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	

	public Long getSimId() {
		return simId;
	}

	public void setSimId(Long simId) {
		this.simId = simId;
	}

	public Long getRsId() {
		return rsId;
	}

	public void setRsId(Long rsId) {
		this.rsId = rsId;
	}

	public Long getaId() {
		return aId;
	}

	public void setaId(Long aId) {
		this.aId = aId;
	}

	public String getSimName() {
		return simName;
	}

	public void setSimName(String simName) {
		this.simName = simName;
	}

	public String getRsName() {
		return rsName;
	}

	public void setRsName(String rsName) {
		this.rsName = rsName;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * This gets all of the Running Simulation entries for a user where they are the 
	 * instructor.
	 * 
	 * @param afso
	 * @return
	 */
	public static List<RunningSimulationInformationLine> getRunningSimLines(
			AuthorFacilitatorSessionObject afso, Long sim_id) {

		ArrayList<RunningSimulationInformationLine> returnList = new ArrayList<RunningSimulationInformationLine>();

		List simsList = new ArrayList();
		
		if (sim_id == null){
			simsList = Simulation.getAll(afso.schema);
		} else {
			Simulation sim = Simulation.getById(afso.schema, sim_id);
			simsList.add(sim);
		}
		
		for (ListIterator lis = simsList.listIterator(); lis
				.hasNext();) {
			Simulation sim = (Simulation) lis.next();
			afso.sim_id = sim.getId();

			List rsList = RunningSimulation.getAllForSim(
					afso.sim_id, afso.schema);

			for (ListIterator li = rsList.listIterator(); li.hasNext();) {
				RunningSimulation rs = (RunningSimulation) li.next();

				SimulationPhase sp = new SimulationPhase();
				if (rs.getPhase_id() != null) {
					sp = SimulationPhase.getById(afso.schema, rs.getPhase_id()
							.toString());
				}

				if (InstructorRunningSimAssignments.checkIsInstructor(
						afso.user_id, afso.schema, rs.getId())) {
					
					RunningSimulationInformationLine rsil = new RunningSimulationInformationLine();
					rsil.setSimName(sim.getDisplayName());
					rsil.setTimeZone(rs.getTimeZone());
					rsil.setRsName(rs.getRunningSimulationName());
					rsil.setRsId(rs.getId());
					rsil.setEnabled(rs.isReady_to_begin());
					rsil.setPhaseName(sp.getPhaseName());
					
					returnList.add(rsil);

				}
			}
		}
		return returnList;
	}
}
