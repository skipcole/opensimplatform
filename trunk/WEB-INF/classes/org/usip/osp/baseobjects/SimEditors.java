package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class records who can edit a simulation.
 */
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
@Entity
@Proxy(lazy = false)
public class SimEditors {
	
	public SimEditors () {
		this.creationDate = new Date();
	}
	
	public SimEditors(String schema, Long simId, Long userId, String userName, String userEmail) {
		this.creationDate = new Date();
		this.simId = simId;
		this.userId = userId;
		this.userEmail = userEmail;
		this.userName = userName;
		this.saveMe(schema);
	}

	/** Database id. */
	@Id
	@GeneratedValue
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	private Date creationDate = new java.util.Date();
	
	private Long simId;
	
	private Long userId;
	
	private String userEmail;
	
	private String userName;

	public Long getSimId() {
		return simId;
	}

	public void setSimId(Long simId) {
		this.simId = simId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/** Saves the object back to the database. */
	public void saveMe(String schema) {
			
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/** Checks to see if the user and sim combination is found, which indicates that this
	 * user can edit this simulation.*/
	public static boolean checkIfAuthorized(String schema, Long simId, Long userId){
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimEditors where simId = :simId and userId = :userId")
				.setLong("simId", simId)
				.setLong("userId", userId)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((returnList == null) || (returnList.size() == 0)){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @param simId
	 * @param userId
	 * @return
	 */
	public static List getListOfAuthorizedEditors(String schema, Long simId, Long userId){
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimEditors where simId = :simId and userId = :userId")
				.setLong("simId", simId)
				.setLong("userId", userId)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}

	/** Removes combination of user id and sim id, thus removing all possible
	 * authorization lines for this user.
	 * 
	 * @param schema
	 * @param simId
	 * @param userId
	 */
	public static void removeAuthorization(String schema, Long simId, Long userId){
		
		List deleteList = getListOfAuthorizedEditors(schema, simId, userId);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator li = deleteList.listIterator(); li.hasNext();) {
			SimEditors se = (SimEditors) li.next();
			
			MultiSchemaHibernateUtil.getSession(schema).delete(se);
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}
	
	/**
	 * 
	 * @param schema
	 * @param simId
	 * @param userId
	 * @return
	 */
	public static List<User> getAuthorizedUsers(String schema, Long simId, Long userId){
		
		ArrayList returnList = new ArrayList();
		
		List baseList = getListOfAuthorizedEditors(schema, simId, userId);

		for (ListIterator li = baseList.listIterator(); li.hasNext();) {
			SimEditors se = (SimEditors) li.next();
			
			User user = User.getById(schema, userId);
			returnList.add(user);
		}
		
		return returnList;
	}
}
