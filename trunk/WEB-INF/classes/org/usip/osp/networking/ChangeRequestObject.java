package org.usip.osp.networking;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * This class represents a change request packet sent to the simulation.
 *
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public int getRound_sent() {
		return round_sent;
	}

	public void setRound_sent(int round_sent) {
		this.round_sent = round_sent;
	}

	public Long getPhase_id_sent() {
		return phase_id_sent;
	}

	public void setPhase_id_sent(Long phase_id_sent) {
		this.phase_id_sent = phase_id_sent;
	}

	public String getVariable_name() {
		return variable_name;
	}

	public void setVariable_name(String variable_name) {
		this.variable_name = variable_name;
	}

	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}

	public String getAuthentication_string() {
		return authentication_string;
	}

	public void setAuthentication_string(String authentication_string) {
		this.authentication_string = authentication_string;
	}

	public Long getSending_actor() {
		return sending_actor;
	}

	public void setSending_actor(Long sending_actor) {
		this.sending_actor = sending_actor;
	}

	public Long getSending_user() {
		return sending_user;
	}

	public void setSending_user(Long sending_user) {
		this.sending_user = sending_user;
	}
	
	
	

}
