package org.usip.osp.sharing;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * This object/table is used to basically keep a linked list between events.
 * 
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
@Proxy(lazy=false)
public class ResponseObjectTransactions {

	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Date dateOfResponse;

	/** Running Simulation id. */
	private Long rsId;
	
	/** Simulation id. */
	private Long simId;
	
	/** If the dependent objects associated with this section are associated with a particular actor, this is its id. */
	private Long actorId;
	
	private Long phaseId;

	/** The id of this object used to pull it out of the database if necessary. */
	private Long objectId;
	
	/**
	 * The class name of this object used to create it or pull it out of the
	 * database if necessary.
	 */
	private String className;
	
	/** Indicates if this is the object that got the reponse in this related interaction or not. */
	private boolean recipientOfResponse = false;
	

	
}
