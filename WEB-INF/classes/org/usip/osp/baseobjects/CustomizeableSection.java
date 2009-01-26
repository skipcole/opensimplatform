package org.usip.osp.baseobjects;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.ObjectPackager;
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
    
    /**
     * If this custom section has a big string, like a page of text, store it in here.
     */
    @Lob
    private String bigString = "";

    /**
     * This hashtable holds information about the content - 
     * explainations for the simulation author to read.
     */
	private Hashtable meta_content = new Hashtable();
    
    /** This holds information about the data, such as if it is applicable
     * for a text field or a text area field for input. */
    private Hashtable meta_data_content = new Hashtable();

	public static void main(String args[]) {

		List x = getAllUncustomized("test");
		for (ListIterator<CustomizeableSection> bi = x.listIterator(); bi.hasNext();) {
			CustomizeableSection bid = (CustomizeableSection) bi.next();
			System.out.println(bid.getRec_tab_heading());
			
		}
		/*
		CustomizeableSection cs = new CustomizeableSection();
		cs.thisIsACustomizedSection = false;
		cs.hasASpecificMakePage = true;
		cs.specificMakePage = "make_caucus_page.jsp";
		cs.bigString = "";
		cs.description = "";
		cs.getContents().put("text1", "");
		cs.getMeta_content().put("image1", "Image to be shown on page.");
        cs.getMeta_data_content().put("image1", "textarea");
        cs.setHasASpecificMakePage(true);
        cs.setSpecificMakePage("this_page");
        
        Hashtable ht = new Hashtable();
        
        /*
         * <org.usip.osp.baseobjects.CustomizeableSection>
        <thisIsACustomizedSection>false</thisIsACustomizedSection>
        <hasASpecificMakePage>true</hasASpecificMakePage>
        <specificMakePage>make_caucus_page.jsp</specificMakePage>
        <bigString></bigString>
        <description>A page to allow several actors to communicate privately.</description>
        <url></url>
        <directory>../osp_core/</directory>
        <page__file__name>caucus_page.jsp</page__file__name>
        <rec__tab__heading>Caucus Page</rec__tab__heading>
        <sample__image>caucus_page.png</sample__image>
        <control__section>false</control__section>
      </org.usip.osp.baseobjects.CustomizeableSection>
        */
		
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
		cs.setMeta_content(this.getMeta_content());
		cs.setPage_file_name(this.getPage_file_name());
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
			
			if (!(bid.isThisIsACustomizedSection())){
				returnList.add(bid);
			}
			
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

	public Hashtable getMeta_content() {
		if (meta_content == null){
			meta_content = new Hashtable();
		}
		return meta_content;
	}

	public void setMeta_content(Hashtable meta_content) {
		this.meta_content = meta_content;
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

	public Hashtable getMeta_data_content() {
        return meta_data_content;
    }

    public void setMeta_data_content(Hashtable meta_data_content) {
        this.meta_data_content = meta_data_content;
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

}
