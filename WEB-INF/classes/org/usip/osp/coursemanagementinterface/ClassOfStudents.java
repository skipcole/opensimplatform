package org.usip.osp.coursemanagementinterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Simulation;
import org.usip.osp.baseobjects.User;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
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
public class ClassOfStudents implements Comparable {

	@Id
	@GeneratedValue
	private Long id;

	private String className = "";

	private java.util.Date creationDate = new java.util.Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public java.util.Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(java.util.Date creationDate) {
		this.creationDate = creationDate;
	}

	public ClassOfStudents() {

	}

	public ClassOfStudents(String schema, String className) {

		this.className = className;

		this.saveMe(schema);

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

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static int createClass(HttpServletRequest request) {

		String addClass = request.getParameter("add_class");
		String className = request.getParameter("class_name");
		String schema = request.getParameter("schema");

		if ((addClass != null) && (addClass.equalsIgnoreCase("true"))) {

			ClassOfStudents cos = new ClassOfStudents(schema, className);

			return 1;
		}

		return 0;
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
	public static List <ClassOfStudents> getAllForInstructor(String schema, Long userId) {

		ArrayList returnList = new ArrayList();

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List tempList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from ClassOfStudentsAssignments where userId = :userId and instructor is true")
				.setLong("userId", userId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		for (ListIterator li = tempList.listIterator(); li.hasNext();) {
			ClassOfStudentsAssignments cosa = (ClassOfStudentsAssignments) li
					.next();

			ClassOfStudents cos = ClassOfStudents.getById(schema,
					cosa.getClassId());

			returnList.add(cos);
		}

		return returnList;
	}

	/**
	 * 
	 * @param schema
	 * @return
	 */
	public static List getAll(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from ClassOfStudents").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		Collections.sort(returnList);

		return returnList;
	}

	@Override
	public int compareTo(Object arg0) {

		ClassOfStudents cos = (ClassOfStudents) arg0;

		return this.getClassName().compareTo(cos.getClassName());

	}

	/**
	 * Gets the members of a class, either instructors or students.
	 * 
	 * @param schema
	 * @param classId
	 * @param instructor
	 * @return
	 */
	public static List<User> getMembers(String schema, Long classId,
			boolean instructor) {

		if (classId == null) {
			return new ArrayList();
		}

		String hqlQuery = "from ClassOfStudentsAssignments where classId = :classId and instructor is false";

		if (instructor) {
			hqlQuery = "from ClassOfStudentsAssignments where classId = :classId and instructor is true";
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List tempList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(hqlQuery).setLong("classId", classId).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		ArrayList returnList = new ArrayList();

		for (ListIterator li = tempList.listIterator(); li.hasNext();) {
			ClassOfStudentsAssignments cosa = (ClassOfStudentsAssignments) li
					.next();

			User user = User.getById(schema, cosa.getUserId());
			user.setTemporaryTag(cosa.getId().toString());

			returnList.add(user);
		}

		Collections.sort(returnList);

		return returnList;
	}

	public static String addMembers(HttpServletRequest request,
			AuthorFacilitatorSessionObject afso, ClassOfStudents cos) {

		String errorMessage = "";

		String add_instructor = request.getParameter("add_instructor");
		if ((add_instructor != null)
				&& (add_instructor.equalsIgnoreCase("true"))) {
			String instructor_username = request
					.getParameter("instructor_username");

			User user = User.getByUsername(afso.schema, instructor_username);

			if (user == null) {
				errorMessage = "User not found.";
			} else {
				@SuppressWarnings("unused")
				ClassOfStudentsAssignments cosa = new ClassOfStudentsAssignments(
						afso.schema, cos.getId(), user.getId(), true);
				
				errorMessage = "User " + instructor_username + " added as instructor";
			}

		}
		
		String add_student = request.getParameter("add_student");
		if ((add_student != null)
				&& (add_student.equalsIgnoreCase("true"))) {
			String student_username = request
					.getParameter("student_username");

			User user = User.getByUsername(afso.schema, student_username);

			if (user == null) {
				errorMessage = "User not found.";
			} else {
				@SuppressWarnings("unused")
				ClassOfStudentsAssignments cosa = new ClassOfStudentsAssignments(
						afso.schema, cos.getId(), user.getId(), false);
				
				errorMessage = "User " + student_username + " added as student";
			}

		}
		
		String remove_member = request.getParameter("remove_member");
		if ((remove_member != null)
				&& (remove_member.equalsIgnoreCase("true"))) {
			
			String cosa_id = request.getParameter("cosa_id");
			
			ClassOfStudentsAssignments.deleteAssignment(afso.schema, new Long(cosa_id));
			
			errorMessage = "Removed Member";
			
		}
		return errorMessage;
	}

}
