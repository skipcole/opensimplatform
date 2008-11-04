package org.usip.osp.persistence;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

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
@Table(name = "USERSCHEMAASSIGNMENT")
@Proxy(lazy = false)
public class UserSchemaAssignment {
    
    private USA_CompoundKey id;

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

}
