package org.usip.osp.specialfeatures;

import java.util.*;

import javax.persistence.*;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;

import org.usip.osp.baseobjects.*;
import org.usip.osp.networking.AuthorFacilitatorSessionObject;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class allows one to set a trigger point such that when the value of one
 * variable switches beyond a limit, a separate action is taken.
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
@Table(name = "TRIGGERS")
@Proxy(lazy = false)
public class Trigger {

	/** Key to pull id out if stored in Hashtable */
	public static final String TRIGGER_KEY = "trigger_key"; //$NON-NLS-1$

	public static final int VAR_TYPE_GENERIC = 0;

	public static final int ACT_TYPE_FINAL_VALUE_TO_AAR = 0;

	public static final int ACT_TYPE_FINAL_VALUE_TEXT_TO_AAR = 0;

	public static final int FIRE_ON_END = 0;
	public static final int FIRE_ON_WHEN_CALLED = 1;
	public static final int FIRE_ON_PHASE_CHANGE = 2;
	public static final int FIRE_ON_ROUND_CHANGE = 3;

	/** Database id of this Trigger. */
	@Id
	@GeneratedValue
	private Long id;

	/** Simulation id. */
	@Column(name = "SIM_ID")
	private Long sim_id;

	private Long var_id;

	private int var_type;

	private int action_type;

	private int fire_on;

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

	public Long getVar_id() {
		return this.var_id;
	}

	public void setVar_id(Long var_id) {
		this.var_id = var_id;
	}

	public int getVar_type() {
		return this.var_type;
	}

	public void setVar_type(int var_type) {
		this.var_type = var_type;
	}

	public int getAction_type() {
		return this.action_type;
	}

	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}

	public int getFire_on() {
		return this.fire_on;
	}

	public void setFire_on(int fire_on) {
		this.fire_on = fire_on;
	}

	/**
	 * Saves this Trigger to the database.
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
	 * @param sim_id
	 * @return
	 */
	public static Trigger getById(String schema, Long t_id) {

		if (t_id == null) {
			return null;
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);
		Trigger this_t = (Trigger) MultiSchemaHibernateUtil.getSession(schema).get(Trigger.class, t_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return this_t;

	}

	/** Gets the GenericVariable referred to in a custom sections hashtable. */
	public static Trigger pullMeOut(String schema, CustomizeableSection cust) {

		Long t_id = (Long) cust.getContents().get(TRIGGER_KEY);

		return getById(schema, t_id);

	}

	public void execute(AuthorFacilitatorSessionObject pso) {

		Logger.getRootLogger().warn("Trigger.execute"); //$NON-NLS-1$
		switch (this.action_type) {

		case ACT_TYPE_FINAL_VALUE_TEXT_TO_AAR: {
			Logger.getRootLogger().warn("ACT_TYPE_FINAL_VALUE_TEXT_TO_AAR"); //$NON-NLS-1$

			GenericVariable gv = GenericVariable.getGVForRunningSim(pso.schema, this.var_id, pso.getRunningSimId());
			Logger.getRootLogger().warn("gv id: " + gv.getId()); //$NON-NLS-1$

			RunningSimulation rs = RunningSimulation.getById(pso.schema, pso.getRunningSimId());
			Logger.getRootLogger().warn("rs id: " + rs.getId()); //$NON-NLS-1$

			AllowableResponse ar = AllowableResponse.getById(pso.schema, gv.getCurrentlySelectedResponse());
			Logger.getRootLogger().warn("ar id: " + ar.getId()); //$NON-NLS-1$

			rs.setAar_text(rs.getAar_text() + ar.getSpecificWordsForAAR());
			rs.saveMe(pso.schema);

		}
		default: {

		}
		}
	}

	/**
	 * Pulls the triggers out for a particular variable.
	 * 
	 * @param schema
	 * @param baseVarId
	 *            The id of the base variable this one was copied from.
	 * @return
	 */
	public static List getTriggersForVariable(String schema, int var_type, Long baseVarId) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = new ArrayList();

		if (var_type == VAR_TYPE_GENERIC) {

			Logger.getRootLogger().debug("from Trigger where var_id = '" + baseVarId + "'"); //$NON-NLS-1$ //$NON-NLS-2$

			returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
					"from Trigger where var_id = '" + baseVarId + "'").list(); //$NON-NLS-1$ //$NON-NLS-2$

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}

		return returnList;
	}

}
