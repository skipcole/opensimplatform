package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a simulation.
 * 
 * @author Ronald "Skip" Cole<br />
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
	
	public static final int PLAYER_COMMENT = 1;
	public static final int INSTRUCTOR_COMMENT = 2;
	
	/** Database id. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long sim_id;
	
	private Long user_id;
	
	private Long rs_id;
	
	private int numberOfStars = 0;
	
	private int comment_type = 0;
	
	@Lob
	private String user_comments = "";

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

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public int getNumberOfStars() {
		return numberOfStars;
	}

	public void setNumberOfStars(int numberOfStars) {
		this.numberOfStars = numberOfStars;
	}

	public int getComment_type() {
		return comment_type;
	}

	public void setComment_type(int comment_type) {
		this.comment_type = comment_type;
	}
	
	public String getCommentoryType (){
		if (this.comment_type == INSTRUCTOR_COMMENT){
			return "Instructor";
		} else if (this.comment_type == PLAYER_COMMENT){
			return "Player";
		} else {
			return "unknown";
		}
	}

	public String getUser_comments() {
		return user_comments;
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
	
	public static List<SimulationRatings> getRatingsBySim(
			String schema, Long sid) {

		if (sid == null) {

			System.out.println("sid/aid/pid: " + sid );
			return new ArrayList<SimulationRatings>();
		} else {

			String getHQL = "from SimulationRatings where SIM_ID = "
					+ sid.toString() + " ";

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
	
}
