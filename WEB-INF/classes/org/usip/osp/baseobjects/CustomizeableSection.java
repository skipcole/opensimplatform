package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.core.Customizer;
import org.usip.osp.networking.*;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.apache.log4j.*;
/**
 * This class represents a simulation section that has been customized.
 *
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
	
	/**
	 * Creates the 
	 * @param request
	 * @return
	 */
	public static CustomizeableSection generateCSforXML(HttpServletRequest request){
		
		CustomizeableSection cs = new CustomizeableSection();
		
		cs.setBigString(cleanNulls(request.getParameter("bigString"))); //$NON-NLS-1$
		cs.setConfers_read_ability(makeTrueIfTrue(request.getParameter("confers_read_ability"))); //$NON-NLS-1$
		cs.setConfers_write_ability(makeTrueIfTrue(request.getParameter("confers_write_ability"))); //$NON-NLS-1$
		cs.setControl_section(makeTrueIfTrue(request.getParameter("control_section"))); //$NON-NLS-1$
		cs.setCreatingOrganization(cleanNulls(request.getParameter("creatingOrganization"))); //$NON-NLS-1$
		cs.setCust_lib_name(cleanNulls(request.getParameter("cust_lib_name"))); //$NON-NLS-1$
		cs.setCustomizerClassName(cleanNulls(request.getParameter("customizerClassName"))); //$NON-NLS-1$
		cs.setDescription(cleanNulls(request.getParameter("description"))); //$NON-NLS-1$
		
		
		return cs;
		
	}
	
	/**
	 * Turns nulls into empty strings.
	 * @param input
	 * @return
	 */
	public static String cleanNulls(String input){
		if (input == null){
			return ""; //$NON-NLS-1$
		} else {
			return input;
		}
	}
	
	
	public static boolean makeTrueIfTrue (String inputString){
		
		if (inputString == null){
			return false;
		}
		
		if (inputString.equalsIgnoreCase("true")){ //$NON-NLS-1$
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
    private String specificMakePage = ""; //$NON-NLS-1$

    /**
     * This hashtable holds specific content information for this simulation.
     */
	private Hashtable contents = new Hashtable();
    
	private String pageTitle = ""; //$NON-NLS-1$
	
    /**
     * If this custom section has a big string, like a page of text, store it in here.
     */
    @Lob
    private String bigString = ""; //$NON-NLS-1$
    
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
		return this.numDependentObjects;
	}

	public void setNumDependentObjects(int numDependentObjects) {
		this.numDependentObjects = numDependentObjects;
	}

	public Customizer getMyCustomizer() {
		return this.myCustomizer;
	}

	public void setMyCustomizer(Customizer myCustomizer) {
		this.myCustomizer = myCustomizer;
	}

	public boolean isHasCustomizer() {
		return this.hasCustomizer;
	}

	public void setHasCustomizer(boolean hasCustomizer) {
		this.hasCustomizer = hasCustomizer;
	}

	public String getCustomizerClassName() {
		return this.customizerClassName;
	}

	public void setCustomizerClassName(String customizerClassName) {
		this.customizerClassName = customizerClassName;
	}

	public static void main(String args[]) {

		CustomizeableSection cs = new CustomizeableSection();
		
		cs.setBigString("Set this to a good default value if you inted to use it, else you can leave it blank."); //$NON-NLS-1$
		cs.hasCustomizer = true;
		cs.setCustomizerClassName("org.yourco.yourproject.ClassName"); //$NON-NLS-1$
		cs.setPageTitle("Page Title Here"); //$NON-NLS-1$
		
		Logger.getRootLogger().debug(ObjectPackager.getObjectXML(cs));

		
	}
	
	// TODO and even gives it a pointer to itself in the form of a url link (cs_id=id).
    /**
     * Copies the template customized section into a new version and saves it in the database.
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
        cs.setNumDependentObjects(this.getNumDependentObjects());
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
        
        //cs.setPage_file_name(cs.getPage_file_name() + "?cs_id=" + cs.getId());
        //MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(cs);
        
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
			Logger.getRootLogger().debug("Warning. Null cs returned using id " + the_id); //$NON-NLS-1$
		}
		
		return cs;
		
	}
	
	public String simImage(String baseWebDirectory){
		String imageName = (String) getContents().get("image_file_name"); //$NON-NLS-1$
		String fullfilename = baseWebDirectory + "/simulation/images/" + imageName; //$NON-NLS-1$
	
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
						"from BaseSimSection where DTYPE='CustomizeableSection'") //$NON-NLS-1$
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
		String query = "from BaseSimSection where DTYPE = 'CustomizeableSection'"; //$NON-NLS-1$
		
		Query theQuery = MultiSchemaHibernateUtil.getSession(schema).createQuery(query);
		
		List<CustomizeableSection> firstList = theQuery.list();

		ArrayList returnList = new ArrayList();
		
		for (ListIterator<CustomizeableSection> bi = firstList.listIterator(); bi.hasNext();) {
			CustomizeableSection bid = bi.next();
			
				returnList.add(bid);

			
		}
		
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return returnList;
	}

	public Hashtable getContents() {
		if (this.contents == null){
			this.contents = new Hashtable();
		}
		return this.contents;
	}

	public void setContents(Hashtable contents) {
		this.contents = contents;
	}

    public boolean isThisIsACustomizedSection() {
        return this.thisIsACustomizedSection;
    }

    public void setThisIsACustomizedSection(boolean thisIsACustomizedSection) {
        this.thisIsACustomizedSection = thisIsACustomizedSection;
    }

    public Long getIdOfOrigCustSection() {
		return this.idOfOrigCustSection;
	}

	public void setIdOfOrigCustSection(Long idOfOrigCustSection) {
		this.idOfOrigCustSection = idOfOrigCustSection;
	}

    public boolean isHasASpecificMakePage() {
        return this.hasASpecificMakePage;
    }

    public void setHasASpecificMakePage(boolean hasASpecificMakePage) {
        this.hasASpecificMakePage = hasASpecificMakePage;
    }

    public String getSpecificMakePage() {
        return this.specificMakePage;
    }

    public void setSpecificMakePage(String specificMakePage) {
        this.specificMakePage = specificMakePage;
    }

    public String getBigString() {
        return this.bigString;
    }

    public void setBigString(String bigString) {
        this.bigString = bigString;
    }
    
	public String getPageTitle() {
		return this.pageTitle;
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
	public void handleCustomizeSection(HttpServletRequest request, AuthorFacilitatorSessionObject pso){}
	
	/**
	 * This method is called by the simulation section during play.
	 * 
	 * @param request
	 * @param cs
	 * @return
	 */
	public void loadSimCustomizeSection(HttpServletRequest request, AuthorFacilitatorSessionObject pso){}

}
