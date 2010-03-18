package org.usip.osp.baseobjects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
@Proxy(lazy=false)
public class ActorCategoryAssignments {

    /** Unique id of this actor. */
	@Id @GeneratedValue
    private Long id;
	
	private Long sim_id;
	
	private Long ac_id;
	
	private Long actor_id;

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

	public Long getAc_id() {
		return ac_id;
	}

	public void setAc_id(Long ac_id) {
		this.ac_id = ac_id;
	}

	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}
	
	public static List <ActorCategoryAssignments> getAllForActorCategory(String schema, Long ac_id){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from ActorCategoryAssignments where ac_id = :ac_id")
				.setLong("ac_id", ac_id)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * Saves this generic variable to the database.
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * Retrieves object from database.
	 * 
	 * @param schema
	 * @param ac_id
	 * @return
	 */
	public static ActorCategoryAssignments getMe(String schema, Long aca_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		ActorCategoryAssignments this_aca  = 
			(ActorCategoryAssignments) MultiSchemaHibernateUtil.getSession(schema)
			.get(ActorCategoryAssignments.class, aca_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_aca;

	}
}
