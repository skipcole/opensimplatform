package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.core.Customizer;
import org.usip.osp.networking.*;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;

/**
 * This class represents a simulation section that has been customized.
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
@Proxy(lazy = false)
public class CustomizeableSection extends BaseSimSection {

	public CustomizeableSection(){
		
	}
	
	public static CustomizeableSection generateXML(HttpServletRequest request){
		
		CustomizeableSection cs = new CustomizeableSection();
		
		cs.setBigString(cleanNulls(request.getParameter("bigString")));
		cs.setConfers_read_ability(makeTrueIfTrue(request.getParameter("confers_read_ability")));
		cs.setConfers_write_ability(makeTrueIfTrue(request.getParameter("confers_write_ability")));
		cs.setControl_section(makeTrueIfTrue(request.getParameter("control_section")));
		cs.setCreatingOrganization(cleanNulls(request.getParameter("creatingOrganization")));
		cs.setCust_lib_name(cleanNulls(request.getParameter("cust_lib_name")));
		cs.setCustomizerClassName(cleanNulls(request.getParameter("customizerClassName")));
		cs.setDescription(cleanNulls(request.getParameter("description")));
		
		
		return cs;
		
	}
	
	public static String cleanNulls(String input){
		if (input == null){
			return "";
		} else {
			return input;
		}
	}
	
	
	public static boolean makeTrueIfTrue (String inputString){
		
		if (inputString == null){
			return false;
		}
		
		if (inputString.equalsIgnoreCase("true")){
			return true;
		} else {
			return false;
		}
	}
	
    /**
     * Indicates that this section is copy of the original template or not.
     */
    private boolean thisIsACustomizedSection = false;
    
    /**
     * If this is a customized copy of an original customizable section, this is the id of the template. 
     */
    private Long idOfOrigCustSection;
    
    /**
     * If this
     */
    private boolean hasASpecificMakePage = false;
    
    /**
     * Name of a specific page to make this page
     */
    private String specificMakePage = "";

    /**
     * This hashtable holds specific content information for this simulation.
     */
	private Hashtable contents = new Hashtable();
    
	private String pageTitle = "";
	
    /**
     * If this custom section has a big string, like a page of text, store it in here.
     */
    @Lob
    private String bigString = "";
    
    private boolean hasCustomizer = false;
    
    /** If this has a customizer, what class should be instantiated to serve as it. */
    private String customizerClassName;
    
    @Transient
    private Customizer myCustomizer;
    
    /** If this object has dependent objects (conversations, shared documents, allowable responses, etc.) then
     * how many of them are expected to be found.
     */
    private int numDependentObjects = 0;
    

	public int getNumDependentObjects() {
		return numDependentObjects;
	}

	public void setNumDependentObjects(int numDependentObjects) {
		this.numDependentObjects = numDependentObjects;
	}

	public Customizer getMyCustomizer() {
		return myCustomizer;
	}

	public void setMyCustomizer(Customizer myCustomizer) {
		this.myCustomizer = myCustomizer;
	}

	public boolean isHasCustomizer() {
		return hasCustomizer;
	}

	public void setHasCustomizer(boolean hasCustomizer) {
		this.hasCustomizer = hasCustomizer;
	}

	public String getCustomizerClassName() {
		return customizerClassName;
	}

	public void setCustomizerClassName(String customizerClassName) {
		this.customizerClassName = customizerClassName;
	}

	public static void main(String args[]) {

		CustomizeableSection cs = new CustomizeableSection();
		
		cs.setBigString("Set this to a good default value if you inted to use it, else you can leave it blank.");
		cs.hasCustomizer = true;
		cs.setCustomizerClassName("org.yourco.yourproject.ClassName");
		cs.setPageTitle("Page Title Here");
		
		System.out.println(ObjectPackager.getObjectXML(cs));

		
	}
	
    /**
     * Copies the template customized section into a new version, and even gives it a pointer to 
     * itself in the form of a url link (cs_id=id).
     * @param schema
     * @return
     */
	public CustomizeableSection makeCopy(String schema){
		CustomizeableSection cs = new CustomizeableSection();
		
        cs.setBigString(this.getBigString());
		cs.setContents(this.getContents());
		cs.setControl_section(this.isControl_section());
        cs.setCust_lib_name(this.getCust_lib_name());
		cs.setDescription(this.getDescription());
		cs.setDirectory(this.getDirectory());
        cs.setHasASpecificMakePage(this.isHasASpecificMakePage());
		cs.setPage_file_name(this.getPage_file_name());
		cs.setPageTitle(this.getPageTitle());
		cs.setRec_tab_heading(this.getRec_tab_heading());
		cs.setSample_image(this.getSample_image());
        cs.setSpecificMakePage(this.getSpecificMakePage());
		cs.setUrl(this.getUrl());
		
		cs.setCreatingOrganization(this.getCreatingOrganization());
		cs.setUniqueName(this.getUniqueName());
		cs.setVersion(this.getVersion());
		
		cs.setIdOfOrigCustSection(this.getId());
		
		cs.setConfers_read_ability(this.isConfers_read_ability());
		cs.setConfers_write_ability(this.isConfers_write_ability());
		
		cs.setHasCustomizer(this.hasCustomizer);
		cs.setCustomizerClassName(this.getCustomizerClassName());
        
        // Copies are made when a section is customized. 
        cs.thisIsACustomizedSection = true;
		
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(cs);
        
        cs.setPage_file_name(cs.getPage_file_name() + "?cs_id=" + cs.getId());
        MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(cs);
        
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
        
        return cs;
	
	}
	
	/**
	 * 
	 * @param schema
	 * @param the_id
	 * @return
	 */
	public static CustomizeableSection getMe(String schema, String the_id){

		MultiSchemaHibernateUtil.beginTransaction(schema);
		CustomizeableSection cs = (CustomizeableSection) MultiSchemaHibernateUtil.
			getSession(schema).get(CustomizeableSection.class, new Long(the_id));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		
		if (cs == null){
			System.out.println("Warning. Null cs returned using id " + the_id);
		}
		
		return cs;
		
	}
	
	public String simImage(String baseWebDirectory){
		String imageName = (String) getContents().get("image_file_name");
		String fullfilename = baseWebDirectory + "/simulation/images/" + imageName;
	
		return fullfilename;
		
	}
    
    public void save(String schema){
        MultiSchemaHibernateUtil.beginTransaction(schema);
        MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
        MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
    }

    /**
     * Returns all customizeable sections <code>DTYPE='CustomizeableSection</code>
     * @param schema
     * @return
     */
	public static List<BaseSimSection> getAll(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery(
						"from BaseSimSection where DTYPE='CustomizeableSection'")
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public static List<BaseSimSection> getAllUncustomized(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		// TODO there is a better hibernate way to do this, i just don't know it.
		String query = "from BaseSimSection where DTYPE = 'CustomizeableSection'";
		
		Query theQuery = MultiSchemaHibernateUtil.getSession(schema).createQuery(query);
		
		List<CustomizeableSection> firstList = theQuery.list();

		ArrayList returnList = new ArrayList();
		
		for (ListIterator<CustomizeableSection> bi = firstList.listIterator(); bi.hasNext();) {
			CustomizeableSection bid = (CustomizeableSection) bi.next();
			
				returnList.add(bid);

			
		}
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public Hashtable getContents() {
		if (contents == null){
			contents = new Hashtable();
		}
		return contents;
	}

	public void setContents(Hashtable contents) {
		this.contents = contents;
	}

    public boolean isThisIsACustomizedSection() {
        return thisIsACustomizedSection;
    }

    public void setThisIsACustomizedSection(boolean thisIsACustomizedSection) {
        this.thisIsACustomizedSection = thisIsACustomizedSection;
    }

    public Long getIdOfOrigCustSection() {
		return idOfOrigCustSection;
	}

	public void setIdOfOrigCustSection(Long idOfOrigCustSection) {
		this.idOfOrigCustSection = idOfOrigCustSection;
	}

    public boolean isHasASpecificMakePage() {
        return hasASpecificMakePage;
    }

    public void setHasASpecificMakePage(boolean hasASpecificMakePage) {
        this.hasASpecificMakePage = hasASpecificMakePage;
    }

    public String getSpecificMakePage() {
        return specificMakePage;
    }

    public void setSpecificMakePage(String specificMakePage) {
        this.specificMakePage = specificMakePage;
    }

    public String getBigString() {
        return bigString;
    }

    public void setBigString(String bigString) {
        this.bigString = bigString;
    }
    
	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * This method is called by the customized 'make' page that fine tunes this section while the simulation
	 * is being authored.
	 * 
	 * @param request
	 * @param cs
	 * @return
	 */
	public void handleCustomizeSection(HttpServletRequest request, ParticipantSessionObject pso){}
	
	/**
	 * This method is called by the simulation section during play.
	 * 
	 * @param request
	 * @param cs
	 * @return
	 */
	public void loadSimCustomizeSection(HttpServletRequest request, ParticipantSessionObject pso){}

}
