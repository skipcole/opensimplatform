package com.seachangesimulations.osp.teamscores;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.RunningSimSet;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

import com.seachangesimulations.osp.griddoc.GridData;

@Entity
@Proxy(lazy = false)
public class TeamScoresTimePeriod {

	/** Database id of this Inject. */
	@Id
	@GeneratedValue
	private Long id;

	private Long simId;

	private Long csId;

	private Long rsId;

	private String periodDescription;

	private Date periodDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSimId() {
		return simId;
	}

	public void setSimId(Long simId) {
		this.simId = simId;
	}

	public Long getCsId() {
		return csId;
	}

	public void setCsId(Long csId) {
		this.csId = csId;
	}

	public Long getRsId() {
		return rsId;
	}

	public void setRsId(Long rsId) {
		this.rsId = rsId;
	}

	public String getPeriodDescription() {
		return periodDescription;
	}

	public void setPeriodDescription(String periodDescription) {
		this.periodDescription = periodDescription;
	}

	public Date getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(Date periodDate) {
		this.periodDate = periodDate;
	}

	public TeamScoresTimePeriod() {

	}

	public TeamScoresTimePeriod(String schema, String periodDescription,
			Long simId, Long csId, Long rsId) {

		this.periodDescription = periodDescription;
		this.simId = simId;
		this.csId = csId;
		this.rsId = rsId;

		this.saveMe(schema);
	}

	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	public static TeamScoresTimePeriod getById(String schema, Long tstp_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		TeamScoresTimePeriod tstp = (TeamScoresTimePeriod) MultiSchemaHibernateUtil
				.getSession(schema).get(TeamScoresTimePeriod.class, tstp_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return tstp;
	}

	/** Returns all of the sections for a particular section and sim. */
	public static List<TeamScoresTimePeriod> getAllForSectionAndRunningSim(
			String schema, Long csId, Long rsId) {

		if ((rsId == null) || (csId == null)) {
			return new ArrayList();
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from TeamScoresTimePeriod where rsId = :rsId and csId = :csId") //$NON-NLS-1$
				.setLong("rsId", rsId).setLong("csId", csId).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * Checks to see if someone is creating a time period for running sims all in the same set.
	 * 
	 * @param request
	 * @param schema
	 * @param simId
	 * @param csId
	 * @param rsId
	 */
	public static void checkForPeriodCreation(HttpServletRequest request,
			String schema, Long simId, Long csId, Long rsId) {

		String sending_page = (String) request.getParameter("sending_page");

		if ((sending_page != null)
				&& (sending_page.equalsIgnoreCase("create_tstp"))) {

			String tstp_name = (String) request.getParameter("tstp_name");

			Enumeration simSets = RunningSimSet.getAllRunningSimsInSameSet(
					schema, rsId);

			for (; simSets.hasMoreElements();) {
				Long thisRsId = (Long) simSets.nextElement();
				@SuppressWarnings("unused")
				TeamScoresTimePeriod tstp = new TeamScoresTimePeriod(schema,
						tstp_name, simId, csId, thisRsId);
			}

		}

	}

}
