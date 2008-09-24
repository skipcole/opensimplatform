package org.usip.oscw.persistence;

import java.sql.Connection;

import org.hibernate.annotations.Proxy;

/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
public class DBInformationObject {

    private Long id;
    
    private String schema_name;
    private String username;
    private String userpass;
    private String location = "jdbc:mysql://localhost:";
    private String port = "3306";
    
    public String testConn(){
        
        Connection conn = MysqlDatabase.getConnection(makeConnString());
        
        if (conn == null) {
            return "problem creating database connection";
        }
        
        try {
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in closing connection in test conn.");
        }
        
        return "Database Connection Verified";
    }
    
    public String makeConnString(){
        
        String conn_string = makeURL() + "&user=" + username + "&password=" + userpass;
        
        System.out.print(conn_string);
        return conn_string;
    }
    
    public String makeURL(){
        String url = location + port + "/" + schema_name + "?autoReconnect=true";
        
        return url;
    }
    
    public String getSchema_name() {
        return schema_name;
    }
    public void setSchema_name(String schema_name) {
        this.schema_name = schema_name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserpass() {
        return userpass;
    }
    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String toString(){
        return "schema: " + this.schema_name + "\n\r" +
            "user: " + this.username + "\n\r" +
            "loc: " + this.location + "\n\r" +
            "port: " + this.port + "\n\r" + 
            "url: " + this.makeURL() + "\n\r" +
            "conn: " + this.makeConnString();
    }
    
}
