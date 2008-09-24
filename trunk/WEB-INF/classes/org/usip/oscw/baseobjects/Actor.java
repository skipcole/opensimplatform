package org.usip.oscw.baseobjects;

import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.oscw.persistence.MultiSchemaHibernateUtil;
import org.usip.oscw.persistence.MultiSchemaHibernateUtil;

import java.util.*;

import javax.persistence.*;


/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "ACTORS")
@Proxy(lazy=false)
public class Actor {

    /** Unique id of this actor. */
	@Id @GeneratedValue
    @Column(name = "ACTOR_ID")
    private Long id;

    /** The display name of this actor. */
	@Column(name = "ACTOR_NAME", unique = true)
    private String name = "";

    /** The public description of this actor. */
	@Column(name = "ACTOR_PUB_DESC")
    @Lob
	private String public_description = "";

    /** Details known about this actor to those close to him or her. */
	@Column(name = "ACTOR_SP_DESC")
    @Lob
	private String semi_public_description = "";

    /** Details only known to this actor (and perhaps intimate associates).*/
	@Column(name = "ACTOR_PRIV_DESC")
	@Lob
    private String private_description = "";

	/** */
    private String imageFilename = "";
    
    private String defaultColorChatBubble = "#FFFFFF";

    @Column(name = "CONTROL_ACTOR")
    private boolean control_actor = false;
    
    private boolean npc = false;
    
    /** Determines if the characters can see this character. The 'control' character,
     * for example, is invisible to them.
     */
    private boolean isShown = true;
    
    
    public List getAll(String schema){
        
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from Actor order by actor_name").list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
    }
    
    public static void main(String args[]){
        
        String schema = "usiposcw";
        
        MultiSchemaHibernateUtil.beginTransaction(schema);
        
        Actor c = getControlActor(MultiSchemaHibernateUtil.getSession(schema));
        System.out.println("cs name is " + c.getName());
        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
        
        
    }
    
    /**
     * Gets the control actor for the given schema.
     * 
     * @param schema
     * @return
     */
    public static Actor getControlActor(String schema){

        MultiSchemaHibernateUtil.beginTransaction(schema);
        
        Actor controlActor = getControlActor(MultiSchemaHibernateUtil.getSession(schema));
        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
        
        return controlActor;
    }
    
    /**
     * Gets the unique control actor, or if none is present, creates one.
     * @param hibernate_session
     * @return
     */
    public static Actor getControlActor(Session hibernate_session){
        
        Actor controlActor = new Actor();

        List returnList = hibernate_session.createQuery(
                "from Actor where actor_name = 'control'").list();
        
        if ((returnList != null) && (returnList.size() > 0)){
            controlActor = (Actor) returnList.get(0);
            System.out.println("got control actor: " + controlActor.getName());
            
            hibernate_session.evict(controlActor);
        } else {
            controlActor.setName("control");
            controlActor.setControl_actor(true);
            controlActor.setShown(false);
            hibernate_session.saveOrUpdate(controlActor);
        }
        
        return controlActor;
    }
    

    /**
     * Compares two actors to order them by name.
     * 
     * @param o
     * @return
     */
    public int compareTo(Object o) {

        Actor a = (Actor) o;

        return -(a.name.compareTo(name));

    }
    

	/**
	 * @return Returns the defaultColorChatBubble.
	 */
	public String getDefaultColorChatBubble() {
		return defaultColorChatBubble;
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
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the imageFilename.
	 */
	public String getImageFilename() {
		return imageFilename;
	}

	/**
	 * @param imageFilename The imageFilename to set.
	 */
	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
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
		return npc;
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
		return private_description;
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
		return public_description;
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
		return semi_public_description;
	}

	/**
	 * @param semi_public_description The semi_public_description to set.
	 */
	public void setSemi_public_description(String semi_public_description) {
		this.semi_public_description = semi_public_description;
	}


    public boolean isShown() {
        return isShown;
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
        
        String getHQL = "select count(ss) from SimulationSection ss where SIM_ID = "
			+ s_id + " AND PHASE_ID = " + pid.toString() + " AND ACTOR_ID = " + a_id.toString();
        
        System.out.println(getHQL);
        
        Long count = (Long) session.createQuery(getHQL).uniqueResult(); 
        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
        
		return count.intValue();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isControl_actor() {
		return control_actor;
	}

	public void setControl_actor(boolean control_actor) {
		this.control_actor = control_actor;
	}
    
    
} // End of actor
