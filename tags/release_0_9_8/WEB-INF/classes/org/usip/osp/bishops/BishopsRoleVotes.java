package org.usip.osp.bishops;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.PlayerSessionObject;
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

	private String actorName;

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

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
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

	/**
	 * 
	 * @param schema
	 * @param rs_id
	 * @param user_id
	 * @param choiceNum
	 * @return
	 */
	public static BishopsRoleVotes getBPIIDForPosition(String schema, Long rs_id, Long user_id, int choiceNum) {

		String getString = "from BishopsRoleVotes where running_sim_id = :rs_id and userId = :user_id and choice = :choiceNum ";

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BishopsRoleVotes> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString)
			.setLong("rs_id", rs_id)
			.setLong("user_id", user_id)
			.setInteger("choiceNum", choiceNum).list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		BishopsRoleVotes returnBRV = null;

		if ((returnList != null) && (returnList.size() == 1)) {
			returnBRV = (BishopsRoleVotes) returnList.get(0);
		} else {
			Logger.getRootLogger().warn("failed to find BishopsRoleVotes object by running sim, user and choice number"); //$NON-NLS-1$
		}
		return returnBRV;
	}

	/**
	 * Handles the setting of votes.
	 * 
	 * @param request
	 * @param pso
	 */
	public static void handleSetVotes(HttpServletRequest request, PlayerSessionObject pso) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("select_role_page"))) {

			String first_choice = (String) request.getParameter("first_choice");
			String second_choice = (String) request.getParameter("second_choice");
			String third_choice = (String) request.getParameter("third_choice");

			if ((first_choice != null) && (!(first_choice.equalsIgnoreCase("0")))) {

				BishopsRoleVotes firstChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(),
						pso.user_id, 1);

				if (firstChoice == null) {
					firstChoice = new BishopsRoleVotes();
				}
				firstChoice.setRunning_sim_id(pso.getRunningSimId());
				firstChoice.setUserId(pso.user_id);
				firstChoice.setBishopsPartyInfoId(new Long(first_choice));		
				firstChoice.setUsersName(pso.userDisplayName);
				firstChoice.setSim_id(pso.sim_id);
				firstChoice.setActorId(pso.getActorId());
				firstChoice.setActorName(pso.getActorName());
				firstChoice.setChoice(1);
				firstChoice.saveMe(pso.schema);

			}

			if ((second_choice != null) && (!(second_choice.equalsIgnoreCase("0")))) {

				BishopsRoleVotes secondChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(),
						pso.user_id, 2);

				if (secondChoice == null) {
					secondChoice = new BishopsRoleVotes();
				}
				secondChoice.setRunning_sim_id(pso.getRunningSimId());
				secondChoice.setUserId(pso.user_id);
				secondChoice.setBishopsPartyInfoId(new Long(second_choice));
				secondChoice.setUsersName(pso.userDisplayName);
				secondChoice.setSim_id(pso.sim_id);
				secondChoice.setActorId(pso.getActorId());
				secondChoice.setActorName(pso.getActorName());
				secondChoice.setChoice(2);
				secondChoice.saveMe(pso.schema);

			}

			if ((third_choice != null) && (!(third_choice.equalsIgnoreCase("0")))) {

				BishopsRoleVotes thirdChoice = BishopsRoleVotes.getBPIIDForPosition(pso.schema, pso.getRunningSimId(),
						pso.user_id, 3);

				if (thirdChoice == null) {
					thirdChoice = new BishopsRoleVotes();
				}
				thirdChoice.setRunning_sim_id(pso.getRunningSimId());
				thirdChoice.setUserId(pso.user_id);
				thirdChoice.setBishopsPartyInfoId(new Long(third_choice));
				thirdChoice.setUsersName(pso.userDisplayName);
				thirdChoice.setSim_id(pso.sim_id);
				thirdChoice.setActorId(pso.getActorId());
				thirdChoice.setActorName(pso.getActorName());
				thirdChoice.setChoice(3);
				thirdChoice.saveMe(pso.schema);

			}
		}

	}

}
