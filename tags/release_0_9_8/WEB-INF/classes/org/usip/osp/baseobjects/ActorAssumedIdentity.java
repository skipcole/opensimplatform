package org.usip.osp.baseobjects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.log4j.Logger;
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
	
	/** Id of the original actor who is taking on this assumed role. */
	private Long actorId;
	
	/** Id of the simulation that this actor is 'associated' with. They may be associated, but not assigned
	 * to a simulation. */
	private Long running_sim_id;
	
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

	public Long getActorId() {
		return actorId;
	}

	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
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

	public static ActorAssumedIdentity getById(String schema, Long aa_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		ActorAssumedIdentity aai = (ActorAssumedIdentity) MultiSchemaHibernateUtil
				.getSession(schema).get(ActorAssumedIdentity.class, aa_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return aai;

	}
	
	public static ActorAssumedIdentity getAssumedIdentity(String schema, Long a_id, Long rs_id){
		String getHQL = "from ActorAssumedIdentity where actorId = " + a_id //$NON-NLS-1$
		+ " AND running_sim_id = " + rs_id; //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getHQL).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if ((returnList == null) || (returnList.size() == 0)) {
			return null;
		} else if (returnList.size() == 1){
			ActorAssumedIdentity aai = (ActorAssumedIdentity) returnList.get(0);
			return aai;
		} else {
			Logger.getRootLogger().warn("Found multiple assumed identities for actor " + a_id + " in running sim " + rs_id);
			return null;
		}
	}
	
	/**
	 * Saves this object back to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
    
}
