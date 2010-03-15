package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.apache.log4j.*;
/**
 * This class represents a simulation.
 *
 * 
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
@Proxy(lazy = false)
public class SimulationRatings {
	
	public static final int ALL_COMMENTS = 0;
	public static final int PLAYER_COMMENT = 1;
	public static final int INSTRUCTOR_COMMENT = 2;
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long sim_id;
	
	private Long actor_id;
	
	private String actor_name;
	
	private Long user_id;
	
	private String users_stated_name = "Anonymous"; //$NON-NLS-1$
	
	private Long rs_id;
	
	private int numberOfStars = 0;
	
	private int comment_type = 0;
	
	@Lob
	private String user_comments = ""; //$NON-NLS-1$

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getActor_id() {
		return this.actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public String getActor_name() {
		return this.actor_name;
	}

	public void setActor_name(String actor_name) {
		this.actor_name = actor_name;
	}

	public Long getUser_id() {
		return this.user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUsers_stated_name() {
		return this.users_stated_name;
	}

	public void setUsers_stated_name(String users_stated_name) {
		this.users_stated_name = users_stated_name;
	}

	public Long getRs_id() {
		return this.rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public int getNumberOfStars() {
		return this.numberOfStars;
	}

	public void setNumberOfStars(int numberOfStars) {
		this.numberOfStars = numberOfStars;
	}

	public int getComment_type() {
		return this.comment_type;
	}

	public void setComment_type(int comment_type) {
		this.comment_type = comment_type;
	}
	
	public String getCommentoryType (){
		if (this.comment_type == INSTRUCTOR_COMMENT){
			return "Instructor"; //$NON-NLS-1$
		} else if (this.comment_type == PLAYER_COMMENT){
			return "Player"; //$NON-NLS-1$
		} else {
			return "unknown"; //$NON-NLS-1$
		}
	}

	public String getUser_comments() {
		return this.user_comments;
	}

	public void setUser_comments(String user_comments) {
		this.user_comments = user_comments;
	}

	/** Saves a simulation. */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	/**
	 * Gets all ratings for a particular sim.
	 * 
	 * @param schema
	 * @param sid
	 * @return
	 */
	public static List<SimulationRatings> getRatingsBySim(
			String schema, Long sid, int getType) {

		if (sid == null) {

			Logger.getRootLogger().debug("sid/aid/pid: " + sid ); //$NON-NLS-1$
			return new ArrayList<SimulationRatings>();
		} else {

			String getHQL = "from SimulationRatings where SIM_ID = " //$NON-NLS-1$
				+ sid.toString() + " "; //$NON-NLS-1$
			
			if (getType == SimulationRatings.ALL_COMMENTS){
				getHQL += ""; //$NON-NLS-1$
			} else if (getType == SimulationRatings.INSTRUCTOR_COMMENT){
				getHQL += " AND COMMENT_TYPE = " + SimulationRatings.INSTRUCTOR_COMMENT + " "; //$NON-NLS-1$ //$NON-NLS-2$
			} else if (getType == SimulationRatings.PLAYER_COMMENT){
				getHQL += " AND COMMENT_TYPE = " + SimulationRatings.PLAYER_COMMENT + " "; //$NON-NLS-1$ //$NON-NLS-2$
			}

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(getHQL).list();

			if (returnList == null) {
				returnList = new ArrayList();
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			return returnList;
		}

	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param user_id
	 * @return
	 */
	public static SimulationRatings getBySimAndActorAndUser(String schema, Long sim_id, Long actor_id, Long user_id){

		SimulationRatings sr = new SimulationRatings();
		
		if ((sim_id == null) || (user_id == null)){

			Logger.getRootLogger().debug("sid/uid: " + sim_id + "/" + user_id); //$NON-NLS-1$ //$NON-NLS-2$
			return sr;
		} else {

			String getHQL = "from SimulationRatings where SIM_ID = " //$NON-NLS-1$
				+ sim_id + " AND ACTOR_ID = " + actor_id + " AND USER_ID = " + user_id; //$NON-NLS-1$ //$NON-NLS-2$

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(getHQL).list();

			
			if ((returnList != null) && (returnList.size() > 0)){
				sr = (SimulationRatings) returnList.get(0);
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			return sr;
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param user_id
	 * @return
	 */
	public static SimulationRatings getInstructorRatingsBySimAndUser(String schema, Long sim_id, Long user_id){
		SimulationRatings sr = new SimulationRatings();
		
		if ((sim_id == null) || (user_id == null)){

			Logger.getRootLogger().debug("sid/uid: " + sim_id + "/" + user_id); //$NON-NLS-1$ //$NON-NLS-2$
			return sr;
		} else {

			String getHQL = "from SimulationRatings where SIM_ID = " //$NON-NLS-1$
				+ sim_id + " AND USER_ID = " + user_id + " AND COMMENT_TYPE = " + SimulationRatings.INSTRUCTOR_COMMENT; //$NON-NLS-1$ //$NON-NLS-2$

			MultiSchemaHibernateUtil.beginTransaction(schema);

			List returnList = MultiSchemaHibernateUtil.getSession(schema)
					.createQuery(getHQL).list();

			
			if ((returnList != null) && (returnList.size() > 0)){
				sr = (SimulationRatings) returnList.get(0);
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

			return sr;
		}
	}
	
}
