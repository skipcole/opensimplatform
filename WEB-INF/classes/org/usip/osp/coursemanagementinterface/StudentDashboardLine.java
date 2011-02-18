package org.usip.osp.coursemanagementinterface;

/**
 * This class encapsulates the information on one student dashboard line and allows
 * the user to sort by various attributes.
 *
 */
/* This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
public class StudentDashboardLine implements Comparable<StudentDashboardLine> {

	public static final int SORT_BY_NAME = 0;
	public static final int SORT_BY_LASTNAME = 1;
	public static final int SORT_BY_FIRSTNAME = 2;
	public static final int SORT_BY_MIDDLENAME = 3;
	public static final int SORT_BY_ROLE = 4;
	public static final int SORT_BY_ROLEALIAS = 5;
	public static final int SORT_BY_STATUS = 6;
	public static final int SORT_BY_EMAIL = 7;
	
	/** Name of Student */
	private String studentName = "";
	
	/** Last Name of Student */
	private String studentLastName = "";
	
	/** First Name of Student */
	private String studentFirstName = "";
	
	/** Middle Name of Student */
	private String studentMiddleName = "";
	
	/** Role assigned to this student. */
	private String studentRole = "";
	
	/** If the role has an alias, store it here. */
	private String studentRoleAlias = "";
	
	/** Student Status (confirmed, logged on, etc.)*/
	private String studentStatus = "";
	
	/** Color of status (green, yellow, red, etc.) */
	private String studentStatusColor = "";
	
	/** Student's email address/username */
	private String studentEmail = "";
	

	private int sortType = 0;

	@Override
	public int compareTo(StudentDashboardLine sdl_other) {
		
		int returnInt = 0;
		
		switch (sortType){
		case SORT_BY_NAME: 
			returnInt = this.studentName.compareTo(sdl_other.studentName);
			break;
		case SORT_BY_LASTNAME: 
			returnInt = this.studentLastName.compareTo(sdl_other.studentLastName);
			break;
		case SORT_BY_FIRSTNAME: 
			returnInt = this.studentFirstName.compareTo(sdl_other.studentFirstName);
			break;
		case SORT_BY_MIDDLENAME: 
			returnInt = this.studentMiddleName.compareTo(sdl_other.studentMiddleName);
			break;
		case SORT_BY_ROLE: 
			returnInt = this.studentRole.compareTo(sdl_other.studentRole);
			break;
		case SORT_BY_ROLEALIAS: 
			returnInt = this.studentRoleAlias.compareTo(sdl_other.studentRoleAlias);
			break;
		case SORT_BY_STATUS: 
			returnInt = this.studentStatus.compareTo(sdl_other.studentStatus);
			break;
		case SORT_BY_EMAIL: 
			returnInt = this.studentEmail.compareTo(sdl_other.studentEmail);
			break;			
		}
		
		return returnInt;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	public String getStudentMiddleName() {
		return studentMiddleName;
	}

	public void setStudentMiddleName(String studentMiddleName) {
		this.studentMiddleName = studentMiddleName;
	}

	public String getStudentRole() {
		return studentRole;
	}

	public void setStudentRole(String studentRole) {
		this.studentRole = studentRole;
	}

	public String getStudentRoleAlias() {
		return studentRoleAlias;
	}

	public void setStudentRoleAlias(String studentRoleAlias) {
		this.studentRoleAlias = studentRoleAlias;
	}

	public String getStudentStatus() {
		return studentStatus;
	}

	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}

	public String getStudentStatusColor() {
		return studentStatusColor;
	}

	public void setStudentStatusColor(String studentStatusColor) {
		this.studentStatusColor = studentStatusColor;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

	public int getSortType() {
		return sortType;
	}

	public void setSortType(int sortType) {
		this.sortType = sortType;
	}

}
