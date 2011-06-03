package org.usip.osp.baseobjects;

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
 * 
 */
public class SimEditAuthorization {

	/** Returned if the user trying to modify a simulation is not an author. */
	public static int USER_NOT_AUTHOR = 0;
	
	/** Returned if author is not authorized to modify the simulation. */
	public static int NOT_AUTHORIZED = 1;
	
	/** Returned if the simulation is locked from editing. */
	public static int SIM_LOCKED = 2;
	
	/** Returned if the author can edit the simulation. */
	public static int SIM_CAN_BE_EDITED = 3;

	
	/**
	 * Looks at a user and a simulation and determines if the user can edit this simulation.
	 * 
	 * @param schema
	 * @param sim_id
	 * @param user_id
	 * @return
	 */
	public static int checkAuthorizedToEdit(String schema, Long sim_id, Long user_id){
		
		User user = User.getById(schema, user_id);
		
		if ((user == null) || (!(user.isSim_author()))){
			return USER_NOT_AUTHOR;
		}
		
		Simulation sim = Simulation.getById(schema, sim_id);
		
		if (sim.getSimEditingRestrictions() == Simulation.SPECIFIC_USERS){
			// need to check to see if this user is autorized
			if (!(SimEditors.checkIfAuthorized(schema, sim_id, user_id))){
				return NOT_AUTHORIZED;
			}
		}
		
		if (sim.getPublishedState() != Simulation.NOT_PUBLISHED){
			return SIM_LOCKED;
		}
		
		return SIM_CAN_BE_EDITED;
	}
}
