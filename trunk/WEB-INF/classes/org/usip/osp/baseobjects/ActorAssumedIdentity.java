package org.usip.osp.baseobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * In simulations where actors can either change names, or assume the roles of each other, this is a useful
 * repositry for data.
 * 
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
public class ActorAssumedIdentity {

    /** Unique id of this actor. */
	@Id 
	@GeneratedValue
    private Long id;
	
	/** Id of the simulation that this actor is 'associated' with. They may be associated, but not assigned
	 * to a simulation. */
	private Long sim_id;
	
    /** The display name of this actor. */
    private String assumedName = ""; //$NON-NLS-1$

    /** The public description of this actor. */
    @Lob
	private String assumedPublicDescription = ""; //$NON-NLS-1$

    /** Details known about this actor to those close to him or her. */
    @Lob
	private String assumedSemiPublicDescription = ""; //$NON-NLS-1$

    /** Details only known to this actor (and perhaps intimate associates).*/
	@Lob
    private String assumedPrivateDescription = ""; //$NON-NLS-1$

	/** Image of this actor to be shown.  */
    private String assumedImageFilename = ""; //$NON-NLS-1$
    
    /** Image of this actor to be shown.  */
    private String assumedImageThumbFilename = ""; //$NON-NLS-1$
    
    private String assumedDefaultColorChatBubble = "FFFFFF"; //$NON-NLS-1$
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public String getAssumedName() {
		return assumedName;
	}

	public void setAssumedName(String assumedName) {
		this.assumedName = assumedName;
	}

	public String getAssumedPublicDescription() {
		return assumedPublicDescription;
	}

	public void setAssumedPublicDescription(String assumedPublicDescription) {
		this.assumedPublicDescription = assumedPublicDescription;
	}

	public String getAssumedSemiPublicDescription() {
		return assumedSemiPublicDescription;
	}

	public void setAssumedSemiPublicDescription(String assumedSemiPublicDescription) {
		this.assumedSemiPublicDescription = assumedSemiPublicDescription;
	}

	public String getAssumedPrivateDescription() {
		return assumedPrivateDescription;
	}

	public void setAssumedPrivateDescription(String assumedPrivateDescription) {
		this.assumedPrivateDescription = assumedPrivateDescription;
	}

	public String getAssumedImageFilename() {
		return assumedImageFilename;
	}

	public void setAssumedImageFilename(String assumedImageFilename) {
		this.assumedImageFilename = assumedImageFilename;
	}

	public String getAssumedImageThumbFilename() {
		return assumedImageThumbFilename;
	}

	public void setAssumedImageThumbFilename(String assumedImageThumbFilename) {
		this.assumedImageThumbFilename = assumedImageThumbFilename;
	}

	public String getAssumedDefaultColorChatBubble() {
		return assumedDefaultColorChatBubble;
	}

	public void setAssumedDefaultColorChatBubble(String assumedDefaultColorChatBubble) {
		this.assumedDefaultColorChatBubble = assumedDefaultColorChatBubble;
	}

	public static ActorAssumedIdentity getMe(String schema, Long aa_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		ActorAssumedIdentity aai = (ActorAssumedIdentity) MultiSchemaHibernateUtil
				.getSession(schema).get(ActorAssumedIdentity.class, aa_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return aai;

	}
    
}
