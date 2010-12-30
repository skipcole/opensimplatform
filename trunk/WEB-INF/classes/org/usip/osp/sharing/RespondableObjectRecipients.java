package org.usip.osp.sharing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;

/**
 * Only certain players will be able to respond to some events. For example, only a player who sees
 * an inject can respond directly to it. 
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
@Proxy(lazy=false)
public class RespondableObjectRecipients {

    /** Database id. */
	@Id
	@GeneratedValue
    private Long id;
	
	private Long ro_id;
	
	private Long actor_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRo_id() {
		return ro_id;
	}

	public void setRo_id(Long roId) {
		ro_id = roId;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actorId) {
		actor_id = actorId;
	}
	
	
	
}
