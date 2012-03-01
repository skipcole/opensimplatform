package org.usip.osp.coursemanagementinterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
 */
@Entity
@Proxy(lazy = false)
public class ClassOfStudents {

	@Id
	@GeneratedValue
	private Long id;
	
	private String className = "";
	
	private java.util.Date creationDate = new java.util.Date();
	
	/**
	 * Saves the object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * Pulls the object out of the database base on its id and schema.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static ClassOfStudents getById(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		ClassOfStudents classOfStudents = (ClassOfStudents) MultiSchemaHibernateUtil
				.getSession(schema).get(ClassOfStudents.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return classOfStudents;
	}
	
	/**
	 * 
	 * @param schema
	 * @param userId
	 * @return
	 */
	public static List getAllForInstructor(String schema, Long userId){

		ArrayList returnList = new ArrayList();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List tempList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from ClassOfStudentsAssignments where userId = :userId and instructor is true")
				.setLong("userId", userId)
				.list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		for (ListIterator li = tempList.listIterator(); li.hasNext();) {
			ClassOfStudentsAssignments cosa = (ClassOfStudentsAssignments) li.next();
			
			ClassOfStudents cos = ClassOfStudents.getById(schema, cosa.getClassId());
			
			returnList.add(cos);
		}


		return returnList;
	}
	
}
