package org.usip.osp.baseobjects;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This class was created to hold specific values regarding a planned play session, for example the number of 
 * players. Currently this information is just entered in free form text and this class is unused.
 *
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "PSP")
public class PlannedSessionParameters {

	/** Zero argument constructor. Useful for hibernate, and useful to see if anything is creating this 
	 * object (by doing a 'open call hierarchy' in eclipse).  */
	public PlannedSessionParameters(){
		
	}
	
	/** Synchronous or Asynchronous */
	private String play_type = ""; //$NON-NLS-1$
	
	private int num_hours = 0;
	
	private int num_players = 0;
	
	private String details = ""; //$NON-NLS-1$

	public String getPlay_type() {
		return this.play_type;
	}

	public void setPlay_type(String play_type) {
		this.play_type = play_type;
	}

	public int getNum_hours() {
		return this.num_hours;
	}

	public void setNum_hours(int num_hours) {
		this.num_hours = num_hours;
	}

	public int getNum_players() {
		return this.num_players;
	}

	public void setNum_players(int num_players) {
		this.num_players = num_players;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
}
