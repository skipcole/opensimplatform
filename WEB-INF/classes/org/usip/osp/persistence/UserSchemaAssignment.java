package org.usip.osp.persistence;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
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
@Table(name = "USERSCHEMAASSIGNMENT")
@Proxy(lazy = false)
public class UserSchemaAssignment {
    
    private USA_CompoundKey id;

    /**
     * Zero argument constructor necessary for hibernate.
     */
    public UserSchemaAssignment() {

    }

    @Id
    public USA_CompoundKey getId() { return id; }
    public void setId(USA_CompoundKey id) { this.id = id; }

    
    /**
     * 
     * @param _bu_id
     * @param _sio_id
     */
    public UserSchemaAssignment(Long _bu_id, Long _sio_id) {

        System.out.println("creating new usa: " + _bu_id + " " + _sio_id);
        
        this.id = new USA_CompoundKey(_bu_id, _sio_id);
        
        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);
        
        MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true)
                .saveOrUpdate(this);
        
        MultiSchemaHibernateUtil
                .commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

    }
    
    /**
     * 
     * @param _bu_id
     * @param _sio_id
     * @param schema
     * @return
     */
    public static boolean isAlreadyAssigned(Long _bu_id, Long _sio_id) {
    	
    	boolean returnValue = false;
    	
		MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema);

		List returnList = MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema)
				.createQuery("from UserSchemaAssignment where bu_id = '" 
				+ _bu_id + "' and schema_id = '" + _sio_id + "'").list();
		
		if (returnList.size() > 0){
			returnValue = true;
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
		
		return returnValue;
		
		
    }
    
    

}
