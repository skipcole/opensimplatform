package org.usip.osp.baseobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
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
			System.out.println("checkIfAuthorized is false");
			return false;
		} else {
			System.out.println("checkIfAuthorized is true");
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
	public static List <SimEditors> getListOfAuthorizedEditors(String schema, Long simId){
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimEditors where simId = :simId")
				.setLong("simId", simId)
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
		
		List deleteList = getUserAuthorizationsForThisSim(schema, simId, userId);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		for (ListIterator li = deleteList.listIterator(); li.hasNext();) {
			SimEditors se = (SimEditors) li.next();
			
			MultiSchemaHibernateUtil.getSession(schema).delete(se);
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
	}
	
	/**
	 * Returns the list of users currently identified to edit this sim.
	 * @param schema
	 * @param simId
	 * @param userId
	 * @return
	 */
	public static List<User> getAuthorizedUsers(String schema, Long simId){
		
		ArrayList returnList = new ArrayList();
		
		if (simId == null){
			return returnList;
		}
		
		List baseList = getListOfAuthorizedEditors(schema, simId);

		for (ListIterator li = baseList.listIterator(); li.hasNext();) {
			SimEditors se = (SimEditors) li.next();
			
			User user = User.getById(schema, se.getUserId());
			returnList.add(user);
		}
		
		return returnList;
	}
	
	
	public static Hashtable<Long, String> getCurrentEditors(String schema, Long simId){
		
		Hashtable <Long, String> returnTable = new Hashtable();
		
		List <User> fullList = SimEditors.getAuthorizedUsers(schema, simId);
		
		for (ListIterator li = fullList.listIterator(); li.hasNext();) {
			User se = (User) li.next();
			
			returnTable.put(se.getId(), "set");
		}
		
		return returnTable;
		
	}
	
	/** 
	 * Returns all records that match this sim/user combination. 
	 * @param schema
	 * @param simId
	 * @param userId
	 * @return
	 */
	public static List getUserAuthorizationsForThisSim(String schema, Long simId, Long userId){
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from SimEditors where simId = :simId and userId = :userId")
				.setLong("simId", simId)
				.setLong("userId", userId)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		return returnList;
	}
}
