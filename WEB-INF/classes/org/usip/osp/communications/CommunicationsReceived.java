package org.usip.osp.communications;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Proxy;

/**
 * This class represents a marker that a particular communication has been received.
 * 
 * The static methods of this class also provide the things necessary for a communication item 
 * (A chat message, an email, a modified document of interest, and announcement, an inject, etc.) 
 * to be marked read or unread, so this 
 * information can be given to the player on their 'communication central' page. For example, the player
 * gets a new email, and they see that they have directly from their main page.
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
public class CommunicationsReceived {

	/** Database id of this CommunicationsHub item. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long userAssignment;
	
	private Long commHubID;
	
	private boolean hasBeenRead;
	
	public static void markUnreadMessage(int messageType, Long msgId, Class msgClass){};
	public static void markReadMessage(int messageType, Long msgId, Class msgClass){};
	public static void doExistUnreadMessages(int messageType){};
}
