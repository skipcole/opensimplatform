package org.usip.osp.persistence;

/**
 * This class contains only the information about a schema that is absolutely 
 * necessary to present to a user to allow them the select one: schema name and 
 * schema org.
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
public class SchemaGhost {

    private Long id;
    private String schema_name;
    private String schema_organization;
    
    public SchemaGhost(){
    	
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSchema_name() {
        return schema_name;
    }
    public void setSchema_name(String schema_name) {
        this.schema_name = schema_name;
    }
    public String getSchema_organization() {
        return schema_organization;
    }
    public void setSchema_organization(String schema_organization) {
        this.schema_organization = schema_organization;
    }
    
}
