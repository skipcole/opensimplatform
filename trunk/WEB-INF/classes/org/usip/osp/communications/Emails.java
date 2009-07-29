package org.usip.osp.communications;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * This class represents an in game email.
 *
 *
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
@Table(name = "EMAILS")
@Proxy(lazy=false)
public class Emails {

    /** Unique id of this chat line. Also used for indexing (thus assuming ids only go up). */
	@Id 
	@GeneratedValue
    private Long id;
	
	private Long sim_id;
	
    /** Identifier of the game this chat line is associated with*/
	@Column(name = "RUNNING_SIM_ID")
    private Long running_sim_id;
	
	@Column(name = "CONV_ID")
    private Long conversation_id;
    
    /** Id of the actor making this chat line. */
    protected Long fromActor;
    
    /** Id of the user making this chat line. */
    private Long fromUser;
    
    private String subjectLine = "";
    
    /** Body of the message text. */
    @Lob
    protected String msgtext = "";
    
	@Column(name="MSG_DATE", columnDefinition="datetime") 	
	private java.util.Date msgDate;
}
