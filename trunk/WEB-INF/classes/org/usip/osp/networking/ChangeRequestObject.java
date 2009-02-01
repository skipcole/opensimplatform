package org.usip.osp.networking;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * This class represents a change request packet sent to the simulation.
 * 
 * @author Ronald "Skip" Cole<br />
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "CHANGE_REQUESTS")
@Proxy(lazy = false)
public class ChangeRequestObject {
	
	private Long id;
	
	private Long sim_id;
	
	private Long running_sim_id;
	
	private int round_sent;
	
	private Long phase_id_sent;
	
	private String variable_name;
	
	private String new_value;
	
	private String authentication_string;
	
	private Long sending_actor;
	
	private Long sending_user;
	
	
	

}
