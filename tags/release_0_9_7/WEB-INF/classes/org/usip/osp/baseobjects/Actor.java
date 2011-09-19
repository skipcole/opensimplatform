package org.usip.osp.baseobjects;

import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.USIP_OSP_Cache;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.sharing.ExportableObject;

import java.util.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.*;

/**
 * This class represents an actor playing in a particular simulation, for example 'Red Cross Worker.'
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
@Table(name = "ACTORS")
@Proxy(lazy=false)
public class Actor implements ExportableObject{

	public Actor (){
		
	}
	
    /** Unique id of this actor. */
	@Id @GeneratedValue
    @Column(name = "ACTOR_ID")
    private Long id;
	
	/** Id of the simulation that this actor is 'associated' with. They may be associated, but not assigned
	 * to a simulation. */
	private Long sim_id;
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return this.transit_id;
	}

	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}

    /** The display name of this actor. */
	@Column(name = "ACTOR_NAME")
    private String name = ""; //$NON-NLS-1$

    /** The public description of this actor. */
	@Column(name = "ACTOR_PUB_DESC")
    @Lob
	private String public_description = ""; //$NON-NLS-1$

    /** Details known about this actor to those close to him or her. */
	@Column(name = "ACTOR_SP_DESC")
    @Lob
	private String semi_public_description = ""; //$NON-NLS-1$

    /** Details only known to this actor (and perhaps intimate associates).*/
	@Column(name = "ACTOR_PRIV_DESC")
	@Lob
    private String private_description = ""; //$NON-NLS-1$

	/** Image of this actor to be shown.  */
    private String imageFilename = ""; //$NON-NLS-1$
    
    /** Image of this actor to be shown.  */
    private String imageThumbFilename = ""; //$NON-NLS-1$
    
    private String defaultColorChatBubble = "FFFFFF"; //$NON-NLS-1$

    @Column(name = "CONTROL_ACTOR")
    private boolean control_actor = false;
    
    /** Flag to indicate if this is a 'non-player character' as opposed to a real live player. */
    private boolean npc = false;
    
    /** Determines if the characters can see this character. The 'control' character,
     * for example, may be invisible to them.
     */
    private boolean isShown = true;
	
	public void createAssumedIdentity(String schema, String newName, Long running_sim_id){
		
		// Check to see if identity exists
		if (true) {
			ActorAssumedIdentity aai = ActorAssumedIdentity.getAssumedIdentity(schema, id, running_sim_id);
		}
		// set name TODO
	}
	
    /**
     * Returns all of the actors found in a schema.
     * 
     * @param schema
     * @return
     */
    public static List getAll(String schema){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Actor order by actor_name").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }
   
    
    /**
     * Returns all of the actors found in a schema for a particular simulation
     * 
     * @param schema
     * @return
     */
    public static List getAllForSimulation(String schema, Long sim_id){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Actor where sim_id = :sim_id order by actor_name")
				.setLong("sim_id", sim_id).list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }
    
    /**
     * Returns a particular actor.
     * 
     * @param schema
     * @param actor_id
     * @return
     */
	public static Actor getById(String schema, Long actor_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Actor act = (Actor) MultiSchemaHibernateUtil
				.getSession(schema).get(Actor.class, actor_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return act;

	}
	
	public static Actor cloneMe(String schema, Long actor_id){
		
		Actor clone = new Actor();
		clone.saveMe(schema);
		Long clone_id = clone.getId();
		
		Actor act = Actor.getById(schema, actor_id);
		act.setId(clone_id);
		act.saveMe(schema);
	
		return act;
	}
    
	/**
	 * Returns a list of all actors in this schema. (Useful since actor names have to be unique.)
	 * @param schema
	 * @return
	 */
    public static ArrayList<String> getAllActorNames(String schema){
    	
    	ArrayList returnList = new ArrayList<String>();
    	
    	for (ListIterator<Actor> li = getAll(schema).listIterator(); li.hasNext();) {
    		Actor act = li.next();
    		
    		returnList.add(act.getActorName());		
    	}
    
    	return returnList;
    	
    }
    
    /**
     * 
     * @param schema
     * @param _sim_id
     * @return
     */
    public static List <Actor> getControlActors(String schema, Long _sim_id){
    	
    	MultiSchemaHibernateUtil.beginTransaction(schema);
    	
    	List <Actor> returnList = 
    		MultiSchemaHibernateUtil.getSession(schema).createQuery(
    				"from Actor where sim_id = " + _sim_id + " and control_actor = '1'").list(); //$NON-NLS-1$
    	
    	MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
    	
    	if (returnList == null){
    		Logger.getRootLogger().debug("Found no control actors");
    	} else {
    		Logger.getRootLogger().debug("num control actors: " + returnList.size());
    	}
    	
    	return returnList;
    	
    }
    

    /**
     * Compares two actors to order them by name.
     * 
     * @param o
     * @return
     */
    public int compareTo(Object o) {

        Actor a = (Actor) o;

        return -(a.name.compareTo(this.name));

    }
    

	/**
	 * @return Returns the defaultColorChatBubble.
	 */
	public String getDefaultColorChatBubble() {
		return this.defaultColorChatBubble;
	}

	/**
	 * @param defaultColorChatBubble The defaultColorChatBubble to set.
	 */
	public void setDefaultColorChatBubble(String defaultColorChatBubble) {
		this.defaultColorChatBubble = defaultColorChatBubble;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	/**
	 * @return Returns the imageFilename.
	 */
	public String getImageFilename() {
		return this.imageFilename;
	}

	/**
	 * @param imageFilename The imageFilename to set.
	 */
	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}

	public String getImageThumbFilename() {
		return this.imageThumbFilename;
	}

	public void setImageThumbFilename(String imageThumbFilename) {
		this.imageThumbFilename = imageThumbFilename;
	}

	/**
	 * @return Returns the name.
	 */
	public String getActorName() {
		
			return this.name;
	}
	
	
	public String getActorName(String schema, Long running_sim_id, HttpServletRequest request){
		
		if (running_sim_id == null){
			return this.name;
		}
		
		return USIP_OSP_Cache.getActorName(schema, sim_id, running_sim_id, request, id);
	}
	
	public String getInitialActorName(){
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the npc.
	 */
	public boolean isNpc() {
		return this.npc;
	}

	/**
	 * @param npc The npc to set.
	 */
	public void setNpc(boolean npc) {
		this.npc = npc;
	}

	/**
	 * @return Returns the private_description.
	 */
	public String getPrivate_description() {
		return this.private_description;
	}

	/**
	 * @param private_description The private_description to set.
	 */
	public void setPrivate_description(String private_description) {
		this.private_description = private_description;
	}

	/**
	 * @return Returns the public_description.
	 */
	public String getPublic_description() {
		return this.public_description;
	}

	/**
	 * @param public_description The public_description to set.
	 */
	public void setPublic_description(String public_description) {
		this.public_description = public_description;
	}

	/**
	 * @return Returns the semi_public_description.
	 */
	public String getSemi_public_description() {
		return this.semi_public_description;
	}

	/**
	 * @param semi_public_description The semi_public_description to set.
	 */
	public void setSemi_public_description(String semi_public_description) {
		this.semi_public_description = semi_public_description;
	}


    public boolean isShown() {
        return this.isShown;
    }


    public void setShown(boolean isShown) {
        this.isShown = isShown;
    }

    /**
     * 
     * @param schema
     * @param s_id
     * @param pid
     * @param a_id
     * @return
     */
	public static int getHighestTabPosForPhase(String schema, Long s_id, Long pid, Long a_id) {

        MultiSchemaHibernateUtil.beginTransaction(schema);
        
        Session session = MultiSchemaHibernateUtil.getSession(schema);
        
        String getHQL = "select count(ss) from SimulationSectionAssignment ss where SIM_ID = " //$NON-NLS-1$
			+ s_id + " AND PHASE_ID = " + pid.toString() + " AND ACTOR_ID = " + a_id.toString(); //$NON-NLS-1$ //$NON-NLS-2$
        
        Logger.getRootLogger().debug(getHQL);
        
        Long count = (Long) session.createQuery(getHQL).uniqueResult(); 
        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
        
		return count.intValue();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isControl_actor() {
		return this.control_actor;
	}

	public void setControl_actor(boolean control_actor) {
		this.control_actor = control_actor;
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
    
	public String getRole(String schema, Long sim_id){
		
		SimActorAssignment saa = SimActorAssignment.getBySimIdAndActorId(schema, sim_id, this.id);
		
		if ((sim_id == null) || (saa == null)){
			return "Actor has not been assigned to this simulation."; //$NON-NLS-1$
		}
		return saa.getActors_role();
		
	}
	
    
} // End of actor
