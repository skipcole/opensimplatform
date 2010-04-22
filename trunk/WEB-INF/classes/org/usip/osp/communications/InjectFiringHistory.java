package org.usip.osp.communications;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;

/**
 * History of which injects have been fired, to whom and to when.
 *
 */
/* 
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
@Proxy(lazy = false)
public class InjectFiringHistory {

	public static final int FIRED_TO_ALL = 1;
	
	public static final int FIRED_TO_SOME = 2;
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	/** Running Sim in which this inject was fired. */
	private Long running_sim_id;
	
	/** Id of the actor that fired this inject. */
	private Long actor_id;
	
	/** comma separated list of actors ids this was fired to. */
	private String actorIdsFiredTo;
	
	/** comma separated list of actor Names this was fired to. */
	private String actorNamessFiredTo;
	
	@Column(name = "FIRED_DATE", columnDefinition = "datetime")
	@GeneratedValue
	private Date firedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public String getActorIdsFiredTo() {
		return actorIdsFiredTo;
	}

	public void setActorIdsFiredTo(String actorIdsFiredTo) {
		this.actorIdsFiredTo = actorIdsFiredTo;
	}

	public String getActorNamessFiredTo() {
		return actorNamessFiredTo;
	}

	public void setActorNamessFiredTo(String actorNamessFiredTo) {
		this.actorNamessFiredTo = actorNamessFiredTo;
	}

	public Date getFiredDate() {
		return firedDate;
	}

	public void setFiredDate(Date firedDate) {
		this.firedDate = firedDate;
	}
	
	
	
}
