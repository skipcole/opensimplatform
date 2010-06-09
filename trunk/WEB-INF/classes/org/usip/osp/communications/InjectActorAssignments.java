package org.usip.osp.communications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * Keeps list of actors to whom inject is targeted to by default.
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
public class InjectActorAssignments {

	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long inject_id;
	
	private Long actor_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInject_id() {
		return inject_id;
	}

	public void setInject_id(Long inject_id) {
		this.inject_id = inject_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}
	
	public static List getAllForInject(String schema, Long inject_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = 
			MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from InjectActorAssignments where inject_id = :inject_id ")
				.setLong("inject_id", inject_id)
				.list(); //$NON-NLS-1$
		

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null){
			returnList = new ArrayList();
		}
		return returnList;
	}
	
}
