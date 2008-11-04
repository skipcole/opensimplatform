package org.usip.osp.communications;

import java.util.List;

import javax.persistence.*;

import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "SHARED_DOCUMENTS")
public class SharedDocument {

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

	public void save(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}
	
	public static List getAllForSim(String schema, Long the_sim_id) {
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		List returnList = getAllForSim(MultiSchemaHibernateUtil.getSession(schema), the_sim_id);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;

	}
	
	public static List getAllForSim(  org.hibernate.Session hibernate_session, Long the_sim_id) {
		
		String hql_string = "from SharedDocument where SIM_ID = " + the_sim_id.toString();
		List returnList = hibernate_session.createQuery(hql_string).list();
		

		return returnList;

	}
	
	/**
	 * 
	 * @param schema
	 * @param doc_id
	 * @param cs_id
	 * @param rs_id
	 * @return
	 */
	public static SharedDocument getDocument(String schema, Long cs_id, Long rs_id){
		
		SharedDocument sd = new SharedDocument();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from SharedDocument where CS_ID = " + cs_id + " AND RS_ID = " + rs_id;
		
		System.out.println("hql_string is " + hql_string);
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		if ((returnList == null) || (returnList.size() == 0)){
			// original should have been copied in the 'enable' sim phase.
			// We do it there to keep from two people doing it at the same time.
			System.out.println("Warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("No shared document found.");
			System.out.println("Warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
		} else {
			sd = (SharedDocument) returnList.get(0);
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		
		return sd;
	}
	
	public static SharedDocument getDocumentByBaseId(String schema, Long base_id, Long rs_id){
		
		SharedDocument sd = new SharedDocument();
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		
		String hql_string = "from SharedDocument where BASE_ID = " + base_id + " AND RS_ID = " + rs_id;
		
		System.out.println("hql_string is " + hql_string);
		
		List returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(hql_string).list();
		
		if ((returnList == null) || (returnList.size() == 0)){
			// original should have been copied in the 'enable' sim phase.
			// We do it there to keep from two people doing it at the same time.
			System.out.println("Warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("No shared document found.");
			System.out.println("Warning!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
		} else {
			sd = (SharedDocument) returnList.get(0);
		}
		
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		
		return sd;
	}

	
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