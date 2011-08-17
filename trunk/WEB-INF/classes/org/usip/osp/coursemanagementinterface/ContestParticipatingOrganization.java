package org.usip.osp.coursemanagementinterface;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.usip.osp.persistence.MultiSchemaHibernateUtil;
import org.usip.osp.persistence.PhysicalAddress;

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
 */
@Entity
@Proxy(lazy = false)
public class ContestParticipatingOrganization {

	@Id
	@GeneratedValue
	private Long id;
	
	private Long contestId;
	
	private String organizationName;
	
	@Lob
	private String organizationNotes;
	
	/** Participating organization information will be added first by the registering agent,
	 * later the database will be created. */
	private String organizationSchema;
	

	
	public static ContestParticipatingOrganization processInitialRegistration(HttpServletRequest request){
		return null;
	}
	
	/**
	 * Pulls the ContestParticipatingOrganization out of the root database base on its id.
	 * 
	 * @param schema
	 * @param sim_id
	 * @return
	 */
    public static ContestParticipatingOrganization getById(Long cpoId) {

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        ContestParticipatingOrganization cpo = (ContestParticipatingOrganization) MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true).get(
                		ContestParticipatingOrganization.class, cpoId);

        MultiSchemaHibernateUtil
                .commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

        return cpo;
    }
	
    /**
     * Saves this object back to the main database.
     * 
     */
    public void saveMe(){
        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(this);                        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
    }
	
	
}
