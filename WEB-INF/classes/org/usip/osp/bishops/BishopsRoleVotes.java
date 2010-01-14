package org.usip.osp.bishops;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

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
public class BishopsRoleVotes {

	/** Database id of this Party Info. */
	@Id
	@GeneratedValue
	private Long id;

	private Long sim_id;

	private Long running_sim_id;

	/** Id of this choice. */
	private Long bishopsPartyInfoId;

	/** Choice 1, 2 or 3. */
	private int choice = 0;

	private Long actorId;

	/** Id of the user making this selection. */
	private Long userId;

	private String usersName = "";

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

	public Long getRunning_sim_id() {
		return running_sim_id;
	}

	public void setRunning_sim_id(Long running_sim_id) {
		this.running_sim_id = running_sim_id;
	}

	public Long getBishopsPartyInfoId() {
		return bishopsPartyInfoId;
	}

	public void setBishopsPartyInfoId(Long bishopsPartyInfoId) {
		this.bishopsPartyInfoId = bishopsPartyInfoId;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}

	public Long getActorId() {
		return actorId;
	}

	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsersName() {
		return usersName;
	}

	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}

	/**
	 * Saves the object to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	public static BishopsRoleVotes getBPIIDForPosition(String schema, Long rs_id, Long user_id, int choiceNum) {

		String getString = "from BishopsRoleVotes where running_sim_id = :rs_id and userId = :user_id and choice = :choiceNum ";

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BishopsRoleVotes> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString)
			.setLong("rs_id", rs_id)
			.setLong("user_id", user_id)
			.setInteger("choiceNum", choiceNum).list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		BishopsRoleVotes returnBPI = null;

		if ((returnList != null) && (returnList.size() == 1)) {
			returnBPI = (BishopsRoleVotes) returnList.get(0);
		} else {
			Logger.getRootLogger().warn("failed to find Bishops Party Info object by running sim and index"); //$NON-NLS-1$
		}
		return returnBPI;
	}

}
