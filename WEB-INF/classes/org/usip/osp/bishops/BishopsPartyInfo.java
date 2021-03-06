package org.usip.osp.bishops;

import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.Actor;
import org.usip.osp.baseobjects.CopiedObject;
import org.usip.osp.baseobjects.SimulationSectionAssignment;
import org.usip.osp.communications.Conversation;
import org.usip.osp.networking.PlayerSessionObject;
import org.usip.osp.networking.USIP_OSP_ContextListener;
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
public class BishopsPartyInfo implements CopiedObject{

	/** Database id of this Party Info. */
	@Id
	@GeneratedValue
	private Long id;

	private Long sim_id;

	private Long running_sim_id;
	
	private Long phaseId;
	
	private Long childId;
	
	private Long parentId;
	
	private int version;

	private String name = "";

	private int partyIndex = 0;

	private boolean inActive = false;

	@Lob
	private String needsDoc = "";

	@Lob
	private String fearsDoc = "";

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

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public Long getChildId() {
		return childId;
	}

	public void setChildId(Long childId) {
		this.childId = childId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getBPIName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPartyIndex() {
		return partyIndex;
	}

	public void setPartyIndex(int index) {
		this.partyIndex = index;
	}

	public String getNeedsDoc() {
		return needsDoc;
	}

	public void setNeedsDoc(String needsDoc) {
		this.needsDoc = needsDoc;
	}

	public String getFearsDoc() {
		return fearsDoc;
	}

	public void setFearsDoc(String fearsDoc) {
		this.fearsDoc = fearsDoc;
	}

	public boolean isInActive() {
		return inActive;
	}

	public void setInActive(boolean inActive) {
		this.inActive = inActive;
	}

	/**
	 * Returns the number of parties involved.
	 * 
	 * @param schema
	 * @param rs_id
	 * @return
	 */
	public static int numberOfParties(String schema, Long rs_id) {

		List partyList = getAllForRunningSim(schema, rs_id, false);

		if (partyList == null) {
			return 0;
		} else {
			return partyList.size();
		}
	}

	/**
	 * Returns a list of all party info associated with a particular running
	 * simulation.
	 */
	public static List getAllForRunningSim(String schema, Long rs_id, boolean inAct) {

		String getString = "from BishopsPartyInfo where running_sim_id = :rs_id and inActive = '0' and childId is null order by partyIndex";

		if (inAct) {
			getString = "from BishopsPartyInfo where running_sim_id = :rs_id and inActive = '1' and childId is null";
		}

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<Conversation> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString).setLong(
				"rs_id", rs_id).list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	/**
	 * When an index is changed, the indexes are swapped.
	 * 
	 * @param schema
	 * @param rs_id
	 * @param bpi_id
	 * @param newIndex
	 * @return
	 */
	public static void insertIndex(String schema, Long rs_id, Long bpi_id, int newIndex) {

		// Make sure list is in good shape to begin with.
		reorder(schema, rs_id);

		// Get the current index
		BishopsPartyInfo bpi_bumper = BishopsPartyInfo.getById(schema, bpi_id);

		int oldIndex = bpi_bumper.getPartyIndex();

		List survivors = getAllForRunningSim(schema, rs_id, false);

		if ((newIndex > survivors.size()) || (newIndex <= 0)) {
			// Do Nothing
			Logger.getRootLogger().warn("attempt to move bpi index out of bounds"); //$NON-NLS-1$
			return;
		} else {

			// Get the id of the bpi being bumped
			BishopsPartyInfo bpi_getting_bumped = getByPosition(schema, rs_id, newIndex);

			if (bpi_getting_bumped != null) {
				bpi_getting_bumped.setPartyIndex(oldIndex);
				bpi_getting_bumped.saveMe(schema);

				bpi_bumper.setPartyIndex(newIndex);
				bpi_bumper.saveMe(schema);
			}
		}

	}

	/**
	 * Gets a party information object by its index.
	 * 
	 * @param schema
	 * @param rs_id
	 * @param pIndex
	 * @return
	 */
	public static BishopsPartyInfo getByPosition(String schema, Long rs_id, int pIndex) {

		String getString = "from BishopsPartyInfo where running_sim_id = :rs_id and partyIndex = :partyIndex "
				+ "and inActive = '0' and childId is null";

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BishopsPartyInfo> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString).setLong(
				"rs_id", rs_id).setInteger("partyIndex", pIndex).list(); //$NON-NLS-1$ //$NON-NLS-2$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		BishopsPartyInfo returnBPI = null;

		if ((returnList != null) && (returnList.size() == 1)) {
			returnBPI = (BishopsPartyInfo) returnList.get(0);
		} else {
			Logger.getRootLogger().warn("failed to find Bishops Party Info object by running sim and index"); //$NON-NLS-1$
		}
		return returnBPI;
	}

	/**
	 * 
	 * @param schema
	 * @param rs_id
	 */
	public static void reorder(String schema, Long rs_id) {

		List survivors = getAllForRunningSim(schema, rs_id, false);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		int ii = 1;

		for (ListIterator li = survivors.listIterator(); li.hasNext();) {
			BishopsPartyInfo bpi = (BishopsPartyInfo) li.next();

			bpi.setPartyIndex(ii);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(bpi);

			++ii;

		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

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
	 * Returns a Party Information Object.
	 * 
	 * @param schema
	 * @param actor_id
	 * @return
	 */
	public static BishopsPartyInfo getById(String schema, Long bpi_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		BishopsPartyInfo bpi = (BishopsPartyInfo) MultiSchemaHibernateUtil.getSession(schema).get(
				BishopsPartyInfo.class, bpi_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return bpi;

	}

	/**
	 * 
	 * @param request
	 * @param pso
	 * @return
	 */
	public static BishopsPartyInfo handelAdd(HttpServletRequest request, PlayerSessionObject pso) {

		BishopsPartyInfo bpi = new BishopsPartyInfo();

		String sending_page = (String) request.getParameter("sending_page");
		String queueu_up = (String) request.getParameter("queueu_up");

		if ((sending_page != null) && (sending_page.equalsIgnoreCase("add_party"))) {

			String command = (String) request.getParameter("command");
			
			// Not sure why we need this, but we are getting an error SC. 3/22/12
			if (command == null) {
				command = "";
			}

			if (command.equalsIgnoreCase("Clear")) {

			}

			if (command.equalsIgnoreCase("Update")) {
				String bpi_id = (String) request.getParameter("bpi_id");
				bpi = BishopsPartyInfo.getById(pso.schema, new Long(bpi_id));
			}

			if ((command.equalsIgnoreCase("Create")) || (command.equalsIgnoreCase("Update"))) {
				String party_name = (String) request.getParameter("party_name");
				String party_needs = (String) request.getParameter("party_needs");
				String party_fears = (String) request.getParameter("party_fears");

				String party_index = (String) request.getParameter("party_index");

				String marked_inactive = (String) request.getParameter("marked_inactive");

				if ((marked_inactive != null) && (marked_inactive.equalsIgnoreCase("on"))) {
					bpi.setInActive(true);
				} else {
					bpi.setInActive(false);
				}

				int newPI = new Long(party_index).intValue();

				bpi.setName(party_name);
				bpi.setNeedsDoc(party_needs);
				bpi.setFearsDoc(party_fears);
				bpi.setRunning_sim_id(pso.getRunningSimId());
				bpi.setSim_id(pso.sim_id);
				bpi.setPhaseId(pso.phase_id);
				bpi.saveMe(pso.schema);

				storeNameInCache(pso.schema, request, bpi.getId(), bpi.getBPIName());

				if (bpi.getPartyIndex() != newPI) {
					BishopsPartyInfo.insertIndex(pso.schema, pso.getRunningSimId(), bpi.getId(), newPI);
				}
			}

		} else if ((queueu_up != null) && (queueu_up.equalsIgnoreCase("true"))) {

			String bpi_id = (String) request.getParameter("bpi_id");

			bpi = BishopsPartyInfo.getById(pso.schema, new Long(bpi_id));

		}

		return bpi;
	}

	@Override
	public void copyToNewVersion(String schema) {
		
		BishopsPartyInfo newCopy = new BishopsPartyInfo();
		
		newCopy.setParentId(this.getId());
		
		newCopy.setFearsDoc(this.getFearsDoc());
		newCopy.setInActive(this.isInActive());
		newCopy.setName(name);
		newCopy.setNeedsDoc(needsDoc);
		newCopy.setPartyIndex(partyIndex);

		newCopy.setRunning_sim_id(running_sim_id);
		newCopy.setSim_id(sim_id);
		newCopy.setVersion(version + 1);
		
		newCopy.saveMe(schema);
		
		this.setChildId(newCopy.getId());
		this.saveMe(schema);
		
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
		
	}
	
	/**
	 * Takes the name of the party, and stores it in cache.
	 * 
	 * @param schema
	 * @param request
	 * @param bpi_id
	 * @param bpi_name
	 */
	public static void storeNameInCache(String schema,  HttpServletRequest request, Long bpi_id, String bpi_name){
		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> bpi_names_hash = (Hashtable<String, String>) context.getAttribute(USIP_OSP_ContextListener.getCacheonBpiNames(schema));

		if (bpi_names_hash == null){
			bpi_names_hash = new Hashtable<String, String>();
			context.setAttribute(USIP_OSP_ContextListener.getCacheonBpiNames(schema), bpi_names_hash);
		}
		
		bpi_names_hash.put(schema + "_" +  bpi_id, bpi_name);
		
		context.setAttribute(USIP_OSP_ContextListener.getCacheonBpiNames(schema), bpi_names_hash);
	}
	
	/**
	 * 
	 * @param request
	 * @param a_id
	 * @return
	 */
	public static String getBPIName(String schema,  HttpServletRequest request, Long bpi_id) {

		if (bpi_id == null){
			return "";
		}
		
		ServletContext context = request.getSession().getServletContext();

		Hashtable<String, String> bpi_names_hash = (Hashtable<String, String>) context.getAttribute(USIP_OSP_ContextListener.getCacheonBpiNames(schema));

		if (bpi_names_hash == null) {
			bpi_names_hash = new Hashtable<String, String>();
			context.setAttribute(USIP_OSP_ContextListener.getCacheonBpiNames(schema), bpi_names_hash);
		}

		String bpi_name = bpi_names_hash.get(schema + "_" +  bpi_id);
		
		if (bpi_name == null) {
			BishopsPartyInfo bpi = BishopsPartyInfo.getById(schema, bpi_id);
			
			bpi_name = bpi.getBPIName();
			bpi_names_hash.put(schema + "_" +  bpi_id, bpi.getBPIName());
			
			context.setAttribute(USIP_OSP_ContextListener.getCacheonBpiNames(schema), bpi_names_hash);
		}

		return bpi_name;
	}

}
