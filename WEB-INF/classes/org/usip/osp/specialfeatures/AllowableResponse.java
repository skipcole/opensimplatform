package org.usip.osp.specialfeatures;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.CustomizeableSection;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;


/**
 * This class represents one specific choice in a set of answers that a player may select. For example,
 * the question might be "Do you want steak, or fish?" There would be one 
 * 
 * @author Ronald "Skip" Cole<br />
 *
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "ALLOWABLERESPONSES")
@Proxy(lazy = false)
public class AllowableResponse{
	
	/** Database id of this Simulation. */
	@Id
	@GeneratedValue
	private Long id;
	
	private Long cust_id;
	
	private int ar_index = 0;
    
	/** Words that introduce this choice. */
	@Lob
    private String responseText = "";
	
	/** If this response has a value associated with it, it is store here. */
	private String specificValue = "";

	@Lob
    private String specificWordsForAAR = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public String getSpecificValue() {
		return specificValue;
	}

	public void setSpecificValue(String specificValue) {
		this.specificValue = specificValue;
	}

	public String getSpecificWordsForAAR() {
		return specificWordsForAAR;
	}

	public void setSpecificWordsForAAR(String specificWordsForAAR) {
		this.specificWordsForAAR = specificWordsForAAR;
	}

	public Long getCust_id() {
		return cust_id;
	}

	public void setCust_id(Long cust_id) {
		this.cust_id = cust_id;
	}
	
	public int getIndex() {
		return ar_index;
	}

	public void setIndex(int ar_index) {
		this.ar_index = ar_index;
	}

	/**
	 * Information about this list 
	 * 
	 * @param cust
	 * @return
	 */
	public static List pullOutArs(CustomizeableSection cust, String schema){
		
		List returnList = new ArrayList();
		// Get ids out of the custom sections grab bag, and pull ARs from database
		
		// If no ids found, just return list of 2 new default style ARs
		
		returnList = getStarterAllowableResponses(cust, schema);
		
		// store information in this custom sections grab bag.
		
		return returnList;
		
	}
	
	/** Returns default set of 2 blank responses. 
	 * 
	 * @param cust
	 * @param schema
	 * @return
	 */
	public static List getStarterAllowableResponses(CustomizeableSection cust, String schema){
		AllowableResponse ar1 = new AllowableResponse();
		ar1.setIndex(1);
		ar1.setResponseText("Choice 1");
		ar1.saveMe(schema);
		
		
		AllowableResponse ar2 = new AllowableResponse();
		ar2.setIndex(2);
		ar2.setResponseText("Choice 2");
		ar2.saveMe(schema);
		
		List returnList = new ArrayList<AllowableResponse>();
		returnList.add(ar1);
		returnList.add(ar2);
		
		return returnList;
	}
	
	/**
	 * Saves this Allowable Response to the database.
	 * @param schema
	 */
	public void saveMe(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

	}
	
	public static String convertListOfARsToInfoString(List theARs){
		return "";
	}
 

}
