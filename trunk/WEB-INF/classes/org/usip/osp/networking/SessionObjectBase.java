package org.usip.osp.networking;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.RunningSimulation;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.User;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.baseobjects.UserTrailGhost;
import org.usip.osp.communications.Event;
import org.usip.osp.persistence.BaseUser;
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
	
	public SessionObjectBase() {
		
	}
	
	/** Page to forward the user on to. */
	public boolean forward_on = false;
	
	/**
	 * Returns the simulation based on what sim_id is currently stored in this Session Object Base.
	 * 
	 * @return
	 */
	public Simulation giveMeSim() {

		if (sim_id == null) {
			return new Simulation();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Simulation simulation = (Simulation) MultiSchemaHibernateUtil
				.getSession(schema).get(Simulation.class, sim_id);

		MultiSchemaHibernateUtil.getSession(schema).evict(simulation);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return simulation;

	}

	/** Schema of the database that the user is working in. */
	public String schema = ""; //$NON-NLS-1$

	/** Code to indicate what kind of error was returned. */
	public int errorCode = 0;

	/** Error message to be shown to the user. */
	public String errorMsg = ""; //$NON-NLS-1$

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

	public void setLoggedin(boolean loggedin) {
		this.loggedin = loggedin;
	}

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

	/** Id of the actor being developed */
	public Long actor_being_worked_on_id;

	/** ID of Phase being worked on. */
	public Long phase_id;

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
	public String userDisplayName = ""; //$NON-NLS-1$

	/** User trail ghost of this user. */
	public UserTrailGhost myUserTrailGhost = new UserTrailGhost();

	/**
	 * Pulls the running sim whose id is being stored out of the database.
	 * 
	 * @return
	 */
	public RunningSimulation giveMeRunningSim() {

		if (runningSimId == null) {
			Logger.getRootLogger().warn(
					"Warning RunningSimId is null in pso.giveMeRunningSim");

			return null;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		RunningSimulation rs = (RunningSimulation) MultiSchemaHibernateUtil
				.getSession(schema).get(RunningSimulation.class, runningSimId);

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
	public static String getEventsForPhase(String schema, Long sim_id,
			Long phase_id) {

		if (sim_id == null) {
			return "";
		} else if (phase_id == null) {
			Simulation sim = Simulation.getById(schema, sim_id);
			phase_id = sim.getFirstPhaseId(schema);
		}

		return Event.packupArray(Event.getAllForSimAndPhase(sim_id, phase_id,
				schema));

	}

	public static String getBaseSimURL() {
		return USIP_OSP_Properties.getValue("base_sim_url");
	}

	/** Id of User that is logged on. */
	public Long user_id;

	/**
	 * Username/ Email address of user that is logged in and using this
	 * AuthorFacilitatorSessionObject.
	 */
	public String user_name;
	
	/**
	 * Returns the user associated with this session.
	 * 
	 * @return
	 */
	public User giveMeUser() {
		
		if (this.user_id != null){
			return User.getUser(schema, this.user_id);
		} else {
			return null;
		}
	}

	/** Assigns a user to a simulation. */
	public UserAssignment handleAssignUser(HttpServletRequest request) {

		UserAssignment ua = new UserAssignment();
		
		String command = request.getParameter("command"); //$NON-NLS-1$
		
		Long a_id = null;
		Long s_id = null;
		Long r_id = null;
		Long ua_id = null;

		if (command != null) {
			
			String user_assignment_id = request
			.getParameter("user_assignment_id"); //$NON-NLS-1$
			
			if (command.equalsIgnoreCase("remove_ua")){
				UserAssignment.removeMe(schema, new Long(user_assignment_id));
				return ua;
			}

			String actor_id = request
					.getParameter("actor_to_add_to_simulation"); //$NON-NLS-1$
			String sim_id = request.getParameter("simulation_adding_to"); //$NON-NLS-1$
			String running_sim_id = request
					.getParameter("running_simulation_adding_to"); //$NON-NLS-1$
			
			// Email address of user to assign role to
			String user_to_add_to_simulation = request.getParameter("user_to_add_to_simulation"); //$NON-NLS-1$
			

			try {
				a_id = new Long(actor_id);
				s_id = new Long(sim_id);
				r_id = new Long(running_sim_id);
				
				if ((user_assignment_id != null)
						&& (!(user_assignment_id.equalsIgnoreCase("null")))) {
					ua_id = new Long (user_assignment_id);
				}
			} catch (Exception e){
				
				e.printStackTrace();
				return ua;
				
			}
			
			if ((command != null) && (command.equalsIgnoreCase("Assign User"))) { //$NON-NLS-1$

				Long user_to_add_id = null;

				if ((user_to_add_to_simulation != null)
						&& (user_to_add_to_simulation
								.equalsIgnoreCase("remove"))) {
					errorMsg = "Removed User Assignment";
					
					if (ua_id != null) {
						UserAssignment.removeMe(schema, ua_id);
					}
					
				} else {
					user_to_add_id = USIP_OSP_Cache.getUserIdByName(schema,
							request, user_to_add_to_simulation);
					if (user_to_add_id == null) {
						
						ua.setUsername(user_to_add_to_simulation);
						ua.setSim_id(s_id);
						ua.setActor_id(a_id);
						ua.setRunning_sim_id(r_id);
						forward_on = true;
						
						return ua;
						
					}
				}


				////////////////////////////////////////////////////////////
				// Add user to an existing userAssignment object
				if (ua_id != null) {
					ua = UserAssignment.getById(schema, ua_id);
				}
				if (ua != null){
					ua.setSim_id(s_id);
					ua.setRunning_sim_id(r_id);
					ua.setActor_id(a_id);
					ua.setUser_id(user_to_add_id);
					ua.setUsername(user_to_add_to_simulation);

					ua.saveMe(schema);
				}
				/////////////////////////////////////////////////////////
			} else if (command.equalsIgnoreCase("add_assignment")) {
				
				// Creating a new blank user assignment
				ua = new UserAssignment(schema, s_id, r_id, a_id, null);
				
			} // end of adding a ua object
		} // End of if command is not null
		
		return ua;
	}
	
	public static final int ALL_GOOD = 0;
	public static final int INSUFFICIENT_INFORMATION = 1;
	public static final int PASSWORDS_MISMATCH = 2;
	public static final int WRONG_OLD_PASSWORD = 3;
	public static final int PASSWORDS_CHANGED = 4;
	
	
	/** Assigns a user to a simulation. */
	public int changePassword(HttpServletRequest request) {
		
		String sending_page = (String) request.getParameter("sending_page");
		String update = (String) request.getParameter("update");
		
		if ((sending_page != null) && (sending_page.equalsIgnoreCase("change_password")) ){
			
			if (update != null) {
				String old_password = request.getParameter("old_password");
				String new_password = request.getParameter("new_password");
				String new_password2 = request.getParameter("new_password2");
				
				if ((old_password == null) || (new_password == null) || (new_password == null)){
					return INSUFFICIENT_INFORMATION;
				}
				
				if (!(new_password.equals(new_password2))){
					return PASSWORDS_MISMATCH;
				}
				
				BaseUser bu = BaseUser.validateUser(this.user_name, old_password);
				if (bu == null){
					return WRONG_OLD_PASSWORD;
				}

				bu.setPassword(new_password);
				return PASSWORDS_CHANGED;
			}
		}
		
		
		return ALL_GOOD;
	}
	
}
