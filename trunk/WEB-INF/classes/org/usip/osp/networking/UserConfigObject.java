package org.usip.osp.networking;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Actor;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;


/**
 * This class holds configuration data that the user may have customized.
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
@Proxy(lazy=false)
public class UserConfigObject {

	
    /** Unique id of this actor. */
	@Id @GeneratedValue
    private Long id;
	
	@Column(unique = true)
	private Long userId;
	
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
     * Returns a particular actor.
     * 
     * @param schema
     * @param actor_id
     * @return
     */
	public static UserConfigObject getById(String schema, Long uco_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		UserConfigObject uco = (UserConfigObject) MultiSchemaHibernateUtil
				.getSession(schema).get(Actor.class, uco_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return uco;

	}
	
	/**
	 * 
	 * @param schema
	 * @param user_id
	 * @return
	 */
	public static UserConfigObject getUsersUserConfigObject(String schema, Long user_id){

		UserConfigObject returnUCO = new UserConfigObject();
		
    	MultiSchemaHibernateUtil.beginTransaction(schema);
    	
    	List <UserConfigObject> returnList = 
    		MultiSchemaHibernateUtil.getSession(schema).createQuery(
    				"from UserConfigObject where userId = :user_id")
    				.setLong("user_id", user_id)
    				.list(); //$NON-NLS-1$
    	
    	MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
    	
    	if (returnList == null){
    		returnUCO.setUserId(user_id);
    		returnUCO.saveMe(schema);
    	} else {
    		returnUCO = (UserConfigObject) returnList.get(0);
    	}
    	
    	return returnUCO;
	}
	
	/**
	 * Saves this object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
}
