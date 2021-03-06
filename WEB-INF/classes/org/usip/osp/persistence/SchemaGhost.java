package org.usip.osp.persistence;

/**
 * This class contains only the information about a schema that is absolutely 
 * necessary to present to a user to allow them the select one: schema name and 
 * schema org.
 */
 /*
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
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSchema_name() {
        return this.schema_name;
    }
    public void setSchema_name(String schema_name) {
        this.schema_name = schema_name;
    }
    public String getSchema_organization() {
        return this.schema_organization;
    }
    public void setSchema_organization(String schema_organization) {
        this.schema_organization = schema_organization;
    }
    
    /**
     * Generates the html to include the schema id in a url string.
     * 
     * @param firstTerm
     * @return
     */
    public String getSchemaIdHTML(boolean firstTerm){
    	
    	String frontDelimiter = "&";
    	
    	if (firstTerm){
    		frontDelimiter = "?";
    	}
    	
    	return frontDelimiter + "schema_id=" + this.getId();
    	
    }
    
    /** Generates the html to include schema id in a url String. This 
     * method defaults to sending the argument as not the first in the chain.
     * 
     * @return
     */
    public String getSchemaIdHTML(){
    	return this.getSchemaIdHTML(false);
    }
    
}
