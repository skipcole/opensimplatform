package org.usip.osp.coursemanagementinterface;

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
public class ClassOfStudentsAssignments {

	@Id
	@GeneratedValue
	private Long id;

	private Long classId;

	private Long userId;

	private boolean instructor = false;

	private java.util.Date assignmentDate = new java.util.Date();

	public ClassOfStudentsAssignments() {

	}

	public ClassOfStudentsAssignments(String schema, Long classId, Long userId, boolean instructor) {
		this.classId = classId;
		this.userId = userId;
		this.instructor = instructor;
		this.saveMe(schema);
	}
	
	/**
	 * Deletes the assignment.
	 * 
	 * @param schema
	 * @param cosaId
	 */
	public static void deleteAssignment(String schema, Long cosaId){
		
		ClassOfStudentsAssignments cosa = ClassOfStudentsAssignments.getById(schema, cosaId);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).delete(cosa);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	/**
	 * 
	 * @param schema
	 * @param cosa_id
	 * @return
	 */
	public static ClassOfStudentsAssignments getById(String schema, Long cosa_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		ClassOfStudentsAssignments cosa = (ClassOfStudentsAssignments) MultiSchemaHibernateUtil
				.getSession(schema).get(ClassOfStudentsAssignments.class, cosa_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return cosa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isInstructor() {
		return instructor;
	}

	public void setInstructor(boolean instructor) {
		this.instructor = instructor;
	}

	public java.util.Date getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(java.util.Date assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

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

}
