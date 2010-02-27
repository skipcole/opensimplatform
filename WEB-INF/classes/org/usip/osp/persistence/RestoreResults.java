package org.usip.osp.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Proxy;

/* This file is part of the USIP Open Simulation Platform.<br>
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
public class RestoreResults {

    @Id
    @GeneratedValue
    private Long id;
    
	@GeneratedValue
    private Date restoreTime = new Date();
    
    private Long restoreId;
    
    private String objectClass;
    
    private String objectId;
    
    private String objectName;
    
    @Lob
    private String notes;
    
    public RestoreResults() {
    	
    }
    
    public RestoreResults(Long reId){
    	this.restoreId = reId;
    }
    
    public static void createAndSaveNotes(Long reId, String notes){
    	RestoreResults rr = new RestoreResults(reId);
    	rr.setNotes(notes);
    	rr.saveMe();
    }
    
    
    public void saveMe(){
        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(this);                        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
    }
    
    
    
    public static RestoreResults getById(Long rr_id) {

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        RestoreResults rr = (RestoreResults) MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true).get(
                		RestoreResults.class, rr_id);

        MultiSchemaHibernateUtil
                .commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

        return rr;
    }



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Date getRestoreTime() {
		return restoreTime;
	}



	public void setRestoreTime(Date restoreTime) {
		this.restoreTime = restoreTime;
	}



	public Long getRestoreId() {
		return restoreId;
	}



	public void setRestoreId(Long restoreId) {
		this.restoreId = restoreId;
	}



	public String getObjectClass() {
		return objectClass;
	}



	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}



	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectName() {
		return objectName;
	}



	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}



	public String getNotes() {
		return notes;
	}



	public void setNotes(String notes) {
		this.notes = notes;
	}
    
    

}
