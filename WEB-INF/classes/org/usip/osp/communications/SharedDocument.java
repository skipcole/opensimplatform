package org.usip.osp.communications;

import java.util.List;

import javax.persistence.*;

import org.apache.log4j.Logger;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a document that is shared amongst the players.
 * 
 * @author Ronald "Skip" Cole<br />
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "SHARED_DOCUMENTS")
public class SharedDocument {
	
	/** When store document id(s) in the hashtable of a custom section, this is the key to retrieve them. */
	public static final String DOCS_IN_HASHTABLE_KEY = "doc_ids";
	
	/** Database id of this Running Simulation. */
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
    
    /** Custom Simulation section id. */
    @Column(name = "CS_ID")
    private Long cs_id;

	/** Indicates if this document can still be edited. */
	private boolean editable = true;

	/** Contents of this document. */
	@Lob
	private String bigString = "";

	/** Title of this document. */
	private String uniqueDocTitle = "";

	/** Title of this document to be displayed to the players. */
	private String displayTitle = "";
	
	/** Short description of this document. */
	private String docDesc = "";

	/** Any text that will start in here for the actor to work with. */
	private String docStarterText = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getBigString() {
		return bigString;
	}

	public void setBigString(String bigString) {
		this.bigString = bigString;
	}

	public String getDocDesc() {
		return docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public String getDocStarterText() {
		return docStarterText;
	}

	public void setDocStarterText(String docStarterText) {
		this.docStarterText = docStarterText;
	}

	public Long getSim_id() {
		return sim_id;
	}

	public void setSim_id(Long sim_id) {
		this.sim_id = sim_id;
	}

	public Long getRs_id() {
		return rs_id;
	}

	public void setRs_id(Long rs_id) {
		this.rs_id = rs_id;
	}

	public String getUniqueDocTitle() {
		return uniqueDocTitle;
	}

	public void setUniqueDocTitle(String uniqueDocTitle) {
		this.uniqueDocTitle = uniqueDocTitle;
	}

	public String getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}
	
	public SharedDocument(){
		
	}
	
	/**
	 * This constructor allows for the immediate passing in of information generally known the instant a shared
	 * document is created. These parameters are described below.
	 * @param uniq_tit	The Unique title of this document.
	 * @param doc_tit_display	The document display title.
	 * @param _sim_id	The id of the simulation that this document is associated with.
	 */
	public SharedDocument(String uniq_tit, String doc_tit_display, Long _sim_id){
		this.uniqueDocTitle = uniq_tit;
		this.displayTitle = doc_tit_display;
		this.sim_id = _sim_id;
		
	}

	/**
	 * Saves the object to the database.
	 * 
	 * @param schema
	 */
	public void save(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	/**
	 * 
	 * @param schema Schema in which to search.
	 * @param the_sim_id The id of the simulation that we are interested in.
	 * @return Returns a list of all of the base documents found for a particular simulation.
	 */
	public static List getAllBaseDocumentsForSim(String schema, Long the_sim_id) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		List returnList = getAllBaseDocumentsForSim(MultiSchemaHibernateUtil.getSession(schema), the_sim_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}
	
	/**
	 * This returns the 'base' documents for a simulation. A 'base' document is the archtypal document
	 * that is copied into a version to be edited in a particular running simulation. When the base
	 * document is copied into a copy document, its id is copied into the base_id of the copy. So 
	 * the original 'base' documents have their BASE_ID null, and copies have in values in that field.
	 * @param hibernate_session
	 * @param the_sim_id
	 * @return
	 */
	public static List getAllBaseDocumentsForSim(  org.hibernate.Session hibernate_session, Long the_sim_id) {
		
		String hql_string = "from SharedDocument where SIM_ID = " + the_sim_id.toString() 
			+ " AND BASE_ID is null";
		List returnList = hibernate_session.createQuery(hql_string).list();
		
		return returnList;
	}
	
	
	/**
	 * This method looks up a document in a particular schema for a particular running simulation. If it does not find
	 * one, it attempts to create one by making a copy of the archetype document. This copy should have already been made
	 * during the 'enable sim' step, since allowing for the document to be created the first time anyone goes to access
	 * it, runs the risk of two people doing that at the same time and running into a clash.
	 * 
	 * @param schema Schema in which to search for the document.
	 * @param base_id Base id of the document being looked for.
	 * @param rs_id Running Simulation id of the document being looked for.
	 * @return Returns the document located in this schema with the base id and running sim id passed in.
	 */
	public static SharedDocument getDocumentByBaseId(String schema, Long base_id, Long rs_id){
		
		SharedDocument sd = new SharedDocument();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from SharedDocument where BASE_ID = " + base_id + " AND RS_ID = " + rs_id;
				
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		if ((returnList == null) || (returnList.size() == 0)){
			// original should have been copied in the 'enable' sim phase.
			// We do it there to keep from two people doing it at the same time.
			Logger.getRootLogger().warn("Warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Logger.getRootLogger().warn("No shared document found. It should have be created at the enable step.");
			Logger.getRootLogger().warn("Warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			SharedDocument baseDoc = (SharedDocument) MultiSchemaHibernateUtil.getSession(schema).get(SharedDocument.class,base_id);
				
			sd = baseDoc.createCopy(rs_id, MultiSchemaHibernateUtil.getSession(schema));
			
		} else {
			sd = (SharedDocument) returnList.get(0);
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		
		return sd;
	}
	
	/**
	 * 
	 * @param schema
	 * @param sim_id
	 * @param rs_id
	 * @return Returns the 'schedule' document for this simulation.
	 */
	public static SharedDocument getScheduleDocument(String schema, Long sim_id, Long rs_id){

		SharedDocument sd = new SharedDocument();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from SharedDocument where SIM_ID = " + sim_id + " AND RS_ID = " + rs_id +
			" AND uniqueDocTitle = 'schedule'";
				
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		if ((returnList == null) || (returnList.size() == 0)){
			// original should have been copied in the 'enable' sim phase.
			// We do it there to keep from two people doing it at the same time.
			Logger.getRootLogger().warn("Warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Logger.getRootLogger().warn("No shared document found. It should have be created at the enable step.");
			Logger.getRootLogger().warn("Warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		} else {
			sd = (SharedDocument) returnList.get(0);
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		
		return sd;
	}	

	/**
	 * Creates, and saves to the database, a copy of this shared document. This is used to create a version of a 
	 * document for a particular running simulation session of the archetype document. The copied document
	 * will have the base_id field filled in with the id of the original.
	 * 
	 * @param rsid Running simulation id.
	 * @param hibernate_session Hibernate session created targetting the appropriate schema for saving into.
	 * @return The shared document object created.
	 * 
	 */
	public SharedDocument createCopy(Long rsid,  org.hibernate.Session hibernate_session){
		SharedDocument sd = new SharedDocument();
		
		sd.setBase_id(this.getId());
		
		sd.setBigString(this.getDocStarterText());
		sd.setCs_id(this.getCs_id());
		sd.setDocDesc(this.getDocDesc());
		sd.setEditable(this.isEditable());
		sd.setRs_id(rsid);
		sd.setSim_id(this.getSim_id());
		sd.setUniqueDocTitle(this.getUniqueDocTitle());
		
		hibernate_session.saveOrUpdate(sd);
		
		return sd;
	}
	
	

	public Long getCs_id() {
		return cs_id;
	}

	public void setCs_id(Long cs_id) {
		this.cs_id = cs_id;
	}

	public Long getBase_id() {
		return base_id;
	}

	public void setBase_id(Long base_id) {
		this.base_id = base_id;
	}
	
	
	
}
