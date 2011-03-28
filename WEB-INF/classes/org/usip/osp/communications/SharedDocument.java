package org.usip.osp.communications;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment;
import org.usip.osp.baseobjects.SimSectionDependentObject;
import org.usip.osp.baseobjects.SimSectionRSDepOjbectAssignment;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a document that is shared amongst the players.
 */
 /* 
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
@Table(name = "SHARED_DOCUMENTS")
@Proxy(lazy=false)
public class SharedDocument implements SimSectionDependentObject, Comparable {

	/** Database id of this Shared Document. */
	@Id
	@GeneratedValue
	@Column(name = "SD_ID")
	private Long id;

	/** Id of the base from which this copy is made. */
	@Column(name = "BASE_ID")
	private Long base_id;

	/** Simulation id. */
	@Column(name = "SIM_ID")
	private Long sim_id;

	/** Running simulation id. */
	@Column(name = "RS_ID")
	private Long rs_id;

	/** Indicates if this document can still be edited. */
	private boolean editable = true;
	
	private boolean deleted = false;
	
	/** If a document is just for copying from, and a separate version does not need to be copied for each 
	 * running sim, it can be marked as a template.
	 */
	private boolean templateDoc = false;
	
	public boolean isTemplateDoc() {
		return templateDoc;
	}

	public void setTemplateDoc(boolean templateDoc) {
		this.templateDoc = templateDoc;
	}
	
	private Long primaryAuthorId;

	private Long version;
	
	private Long phase;
	
	/** If documents are indexed on a display, this holds the value for this document. */
	private Long docIndex;
	
	private String internalControlNumber = ""; //$NON-NLS-1$

	/** Contents of this document. */
	@Lob
	private String bigString = ""; //$NON-NLS-1$

	/** Unique identifier of this document. */
	private String uniqueDocTitle = ""; //$NON-NLS-1$

	/** Title of this document to be displayed to the players. */
	private String displayTitle = ""; //$NON-NLS-1$

	/** Short description of this document. */
	@Lob
	private String docDesc = ""; //$NON-NLS-1$

	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransitId() {
		return this.transit_id;
	}

	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEditable() {
		return this.editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Long getBase_id() {
		return this.base_id;
	}

	public void setBase_id(Long base_id) {
		this.base_id = base_id;
	}

	public String getBigString() {
		return this.bigString;
	}

	public void setBigString(String bigString) {
		this.bigString = bigString;
	}

	public String getDocDesc() {
		return this.docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public Long getSim_id() {
		return this.sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRs_id() {
		return this.rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public String getUniqueDocTitle() {
		return this.uniqueDocTitle;
	}

	public void setUniqueDocTitle(String uniqueDocTitle) {
		this.uniqueDocTitle = uniqueDocTitle;
	}

	public String getDisplayTitle() {
		return this.displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}

	public SharedDocument() {

	}

	/**
	 * This constructor allows for the immediate passing in of information
	 * generally known the instant a shared document is created. These
	 * parameters are described below.
	 * 
	 * @param uniq_tit
	 *            The Unique title of this document.
	 * @param doc_tit_display
	 *            The document display title.
	 * @param _sim_id
	 *            The id of the simulation that this document is associated
	 *            with.
	 */
	public SharedDocument(String uniq_tit, String doc_tit_display, Long _sim_id) {
		this.uniqueDocTitle = uniq_tit;
		this.displayTitle = doc_tit_display;
		this.sim_id = _sim_id;

	}

	/**
	 * Saves the object to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		Session session = MultiSchemaHibernateUtil.getSession(schema);
		
		// Save it to get a doc id
		session.saveOrUpdate(this);
		
		// Save the history of what it is at this moment in time
		SharedDocumentVersionHistory sdvh = new SharedDocumentVersionHistory(schema, this, session);
		
		// Save again to record the version number 
		this.version = sdvh.getVersionNum();
		
		session.saveOrUpdate(this);
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * This returns the 'base' documents for a simulation. A 'base' document is
	 * the archatypal document that is copied into a version to be edited in a
	 * particular running simulation.
	 * 
	 * @param schema
	 *            Schema in which to search.
	 * @param the_sim_id
	 *            The id of the simulation that we are interested in.
	 * @return Returns a list of all of the base documents found for a
	 *         particular simulation.
	 */
	public static List getAllBaseDocumentsForSim(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		String hql_string = "from SharedDocument where SIM_ID = :sim_id AND RS_ID is null"; //$NON-NLS-1$ //$NON-NLS-2$
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hql_string)
			.setLong("sim_id", sim_id)
			.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}
	
	public static List getAllDocumentsForRunningSim(String schema, Long sim_id, Long rs_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		String hql_string = "from SharedDocument where SIM_ID = :sim_id AND RS_ID = :rs_id"; //$NON-NLS-1$ //$NON-NLS-2$
		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hql_string)
			.setLong("sim_id", sim_id)
			.setLong("rs_id", rs_id)
			.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}

	/**
	 * This will attempt to get the document for this player in this running simulation for this section.
	 * If that is not found, it will create one by copying the template document.
	 * 
	 * @param schema
	 * @param sim_id
	 * @param rs_id
	 * @return Returns the 'schedule' document for this simulation.
	 */
	public static SharedDocument getPlayerDocument(String schema, Long b_id, Long sim_id, Long rs_id, Long a_id) {

		SharedDocument sd = new SharedDocument();

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String hql_string = "from SharedDocument where SIM_ID = :sim_id AND RS_ID = :rs_id " +   //$NON-NLS-1$ //$NON-NLS-2$
				" AND primaryAuthorId = :a_id AND base_id = :b_id"; //$NON-NLS-1$

		List returnList = MultiSchemaHibernateUtil.getSession(schema)
			.createQuery(hql_string)
			.setLong("sim_id", sim_id)
			.setLong("rs_id", rs_id)
			.setLong("a_id", a_id)
			.setLong("b_id", b_id)
			.list();

		if ((returnList == null) || (returnList.size() == 0)) {
			Logger.getRootLogger().warn("No player document found, creating new one."); //$NON-NLS-1$

			SharedDocument baseSD = SharedDocument.getById(schema, b_id);
			sd = baseSD.createCopy(rs_id, sim_id, schema);
			sd.setPrimaryAuthorId(a_id);
			sd.saveMe(schema);

		} else {
			sd = (SharedDocument) returnList.get(0);
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return sd;
	}


	/**
	 * Creates, and saves to the database, a copy of this shared document. This
	 * is used to create a version of a document for a particular running
	 * simulation session of the archetype document. The copied document will
	 * have the base_id field filled in with the id of the original.
	 * 
	 * @param rsid
	 *            Running simulation id.
	 * @param hibernate_session
	 *            Hibernate session created targetting the appropriate schema
	 *            for saving into.
	 * @return The shared document object created.
	 * 
	 */
	public SharedDocument createCopy(Long rsid, Long sid, String schema) {
		
		SharedDocument sd = new SharedDocument();

		sd.setBase_id(this.getId());
		sd.setBigString(this.getBigString());
		sd.setPrimaryAuthorId(this.getPrimaryAuthorId());
		sd.setDisplayTitle(this.getDisplayTitle());
		sd.setDocDesc(this.getDocDesc());
		sd.setEditable(this.isEditable());
		sd.setRs_id(rsid);
		sd.setSim_id(this.getSim_id());
			
		sd.setRs_id(rsid);
		sd.setSim_id(sid);
		
		sd.saveMe(schema);
		
		sd.setUniqueDocTitle(this.getUniqueDocTitle() + "copy: " + sd.getId());
		
		sd.saveMe(schema);
		
		return sd;
	}

	@Override
	public Long createRunningSimVersion(String schema, Long sim_id, Long rs_id, Object templateObject) {

		Logger.getRootLogger().warn("Creating shared document for running sim : " + rs_id); //$NON-NLS-1$

		SharedDocument templateSD = (SharedDocument) templateObject;

		SharedDocument sd = templateSD.createCopy(rs_id, sim_id, schema);
		
		// If this document has notification objects associated with it, create copies of those:
		//Copy the SharedDocumentActorNotificationAssignmentObjects into a new set for this doc.
		List assignmentsForDoc = 
			SharedDocActorNotificAssignObj.getAllAssignmentsForDocument(schema, templateSD.getId());
		
		for (ListIterator<SharedDocActorNotificAssignObj> li = assignmentsForDoc.listIterator(); li.hasNext();) {
			SharedDocActorNotificAssignObj sdanao = li.next();
			
			SharedDocActorNotificAssignObj sdanao_copy = sdanao.createCopy();
			sdanao_copy.setSd_id(sd.getId());
			sdanao_copy.setRunningSimId(rs_id);
			sdanao_copy.saveMe(schema);
			
		}
		
		return sd.getId();
	}
	

	/**
	 * I hate this method. There has to be a better way, and to not hit the database so much.
	 * I will rid the world of this method! SC
	 * @param schema
	 * @param bss_id
	 * @param this_id
	 * @return
	 */
	public static String getSelectedIfFoundBaseDocsForBaseSimSection(String schema, Long bss_id, Long this_id) {

		String getString = "from BaseSimSectionDepObjectAssignment where bss_id = '" + bss_id + "' and " + //$NON-NLS-1$ //$NON-NLS-2$
			" className = '" + SharedDocument.class.getName() + "'"; //$NON-NLS-1$

		Logger.getRootLogger().debug(getString);

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List docList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString).list();

		if (docList == null) {
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			return " "; //$NON-NLS-1$
		}

		// Go over list and get items.
		for (ListIterator<BaseSimSectionDepObjectAssignment> li = docList.listIterator(); li.hasNext();) {

			BaseSimSectionDepObjectAssignment bssdoa = li.next();
			if (this_id.equals(bssdoa.getObjectId())){
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				return " selected "; //$NON-NLS-1$
			}
			
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return " "; //$NON-NLS-1$

	}
	
	/**
	 * 
	 * @param schema
	 * @param section_id
	 * @return
	 */
	public static List <SharedDocument> getSetOfBaseDocsForSection(String schema, Long section_id){
		
		return BaseSimSectionDepObjectAssignment.getSharedDocumentsForSection(schema, section_id);
		
	}

	/**
	 * 
	 * @param schema
	 * @param section_id
	 * @param rs_id
	 * @return
	 */
	public static List<SharedDocument> getSetOfDocsForSection(String schema, Long section_id, Long rs_id) {

		List<SharedDocument> returnList = new ArrayList<SharedDocument>();

		String getString = "from SimSectionRSDepOjbectAssignment where section_id = '" + section_id + "' " //$NON-NLS-1$ //$NON-NLS-2$
				+ " and rs_id = " + rs_id //$NON-NLS-1$
				+ " and className = 'org.usip.osp.communications.SharedDocument' order by ssrsdoa_index"; //$NON-NLS-1$

		Logger.getRootLogger().debug(getString);

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List docList = MultiSchemaHibernateUtil.getSession(schema).createQuery(getString).list();

		if (docList != null) {
			Logger.getRootLogger().debug("got some docs back: " + docList.size()); //$NON-NLS-1$
		}
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		// Go over list and get items.
		for (ListIterator<SimSectionRSDepOjbectAssignment> li = docList.listIterator(); li.hasNext();) {

			SimSectionRSDepOjbectAssignment ssrsdoa = li.next();

			MultiSchemaHibernateUtil.beginTransaction(schema);

			try {
				Class objClass = Class.forName(ssrsdoa.getClassName());

				SharedDocument sd = (SharedDocument) MultiSchemaHibernateUtil.getSession(schema).get(objClass,
						ssrsdoa.getObjectId());

				Logger.getRootLogger().debug("strter title:" + sd.getDisplayTitle()); //$NON-NLS-1$
				returnList.add(sd);

			} catch (Exception e) {
				e.printStackTrace();
			}

			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		}

		return returnList;

	}
	
	/**
	 * Pulls the simulation out of the database base on its id and schema.
	 * @param schema
	 * @param sim_id
	 * @return
	 */
	public static SharedDocument getById(String schema, Long sim_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		SharedDocument sd = (SharedDocument) MultiSchemaHibernateUtil
				.getSession(schema).get(SharedDocument.class, sim_id);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return sd;

	}

	@Override
	public void setSimId(Long theId) {
		setSim_id(theId);
		
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getPhase() {
		return phase;
	}

	public void setPhase(Long phase) {
		this.phase = phase;
	}

	public Long getPrimaryAuthorId() {
		return primaryAuthorId;
	}

	public void setPrimaryAuthorId(Long primaryAuthorId) {
		this.primaryAuthorId = primaryAuthorId;
	}

	public Long getDocIndex() {
		return docIndex;
	}

	public void setDocIndex(Long docIndex) {
		this.docIndex = docIndex;
	}

	public String getInternalControlNumber() {
		return internalControlNumber;
	}

	public void setInternalControlNumber(String internalControlNumber) {
		this.internalControlNumber = internalControlNumber;
	}

	@Override
	public int compareTo(Object arg0) {
		SharedDocument sd = (SharedDocument) arg0;
		
		return this.getDisplayTitle().compareToIgnoreCase(sd.getDisplayTitle());
	}

}
