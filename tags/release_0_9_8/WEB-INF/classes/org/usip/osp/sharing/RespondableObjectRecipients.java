package org.usip.osp.sharing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
	
	private Long rs_id;
	
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

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rsId) {
		rs_id = rsId;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actorId) {
		actor_id = actorId;
	}
	
	/**
	 * Gets all of the Respondable objects for a running simulation in the order in which they were created.
	 * @param schema
	 * @param rs_id
	 * @return
	 */
	public static List<RespondableObjectRecipients> getAllForActorInRunningSim(String schema, Long a_id, Long rs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<RespondableObjectRecipients> returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from RespondableObjectRecipients where rs_id = :rs_id AND actor_id = :a_id order by id")
				.setLong("rs_id", rs_id)
				.setLong("a_id", a_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<RespondableObjectRecipients>();
		}

		return returnList;
	}
	
}
