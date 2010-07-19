package org.usip.osp.networking;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.User;
import org.usip.osp.baseobjects.UserTrailGhost;
import org.usip.osp.communications.Event;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.UILanguageObject;

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
	
	/**
	 * We use a language code to indicate what language to show the interface
	 * in. It can be set by the simulation, or over ridden by the player.
	 */
	public int languageCode = UILanguageObject.ENGLISH_LANGUAGE_CODE;


	public int getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(int languageCode) {
		this.languageCode = languageCode;
	}

	/** Determines if actor is logged in. */
	private boolean loggedin = false;

	public boolean isLoggedin() {
		return loggedin;
	}
	
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
	protected Long runningSimId;

	public Long getRunningSimId() {
		return runningSimId;
	}

	public void setRunningSimId(Long runningSimId) {
		this.runningSimId = runningSimId;
	}

	/** Name of the running simulation session. */
	public String run_sim_name = ""; //$NON-NLS-1$
	
	/** Records the display name of this user. */
	public String user_Display_Name = ""; //$NON-NLS-1$
	
	/** User trail ghost of this user. */
	public UserTrailGhost myUserTrailGhost = new UserTrailGhost();
	
	/**
	 * Pulls the running sim whose id is being stored out of the database.
	 * 
	 * @return
	 */
	public RunningSimulation giveMeRunningSim() {

		if (runningSimId == null) {
			Logger.getRootLogger().warn("Warning RunningSimId is null in pso.giveMeRunningSim");

			return null;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil.getSession(schema).get(
				RunningSimulation.class, runningSimId);

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
			Simulation sim = Simulation.getById(schema, sim_id);
			phase_id = sim.getFirstPhaseId(schema);
		}
		
		return Event.packupArray(Event.getAllForSim(sim_id, phase_id, schema));
		
	}
	
	public String getBaseSimURL() {
		return USIP_OSP_Properties.getValue("base_sim_url");
	}
	
	/** Id of User that is logged on. */
	public Long user_id;
	
	/**
	 * Returns the user associated with this session.
	 * 
	 * @return
	 */
	public User giveMeUser() {

		return User.getUser(schema, this.user_id);

	}
}
