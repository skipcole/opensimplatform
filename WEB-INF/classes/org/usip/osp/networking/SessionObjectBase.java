package org.usip.osp.networking;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.communications.Event;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/*
 * 
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
public class SessionObjectBase {

	/** Schema of the database that the user is working in. */
	public String schema = ""; //$NON-NLS-1$
	
	/** ID of Simulation being conducted or worked on. */
	public Long sim_id;

	/** Name of simulation being conducted or worked on. */
	public String simulation_name = ""; //$NON-NLS-1$

	/** Version of the simulation be conducted or worked on. */
	public String simulation_version = ""; //$NON-NLS-1$

	/** Organization that created the simulation. */
	public String simulation_org = ""; //$NON-NLS-1$

	/**
	 * Copyright string to display at the bottom of every page in the
	 * simulation.
	 */
	public String sim_copyright_info = ""; //$NON-NLS-1$

	/** ID of the Running Simulation being conducted or worked on. */
	public Long running_sim_id;

	/** Name of the running simulation session. */
	public String run_sim_name = ""; //$NON-NLS-1$
	
	/**
	 * Pulls the running sim whose id is being stored out of the database.
	 * 
	 * @return
	 */
	public RunningSimulation giveMeRunningSim() {

		if (running_sim_id == null) {
			Logger.getRootLogger().warn("Warning RunningSimId is null in pso.giveMeRunningSim");

			return null;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimulation.class, running_sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(rs);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return rs;
	}
	
	/**
	 * Returns all of the planned events for a phase.
	 * 
	 * @param schema
	 * @param sim_id
	 * @param phase_id
	 * @return
	 */
	public static String getEventsForPhase(String schema, Long sim_id, Long phase_id){
		
		if (sim_id == null){
			return "";
		} else if (phase_id == null){
			Simulation sim = Simulation.getMe(schema, sim_id);
			phase_id = sim.getFirstPhaseId(schema);
		}
		
		return Event.packupArray(Event.getAllForSim(sim_id, phase_id, schema));
		
	}
}
