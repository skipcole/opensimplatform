package org.usip.osp.persistence;

import java.sql.Connection;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

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
@Entity
@Table(name = "SCHEMAINFORMATION")
@Proxy(lazy = false)
public class SchemaInformationObject {

    @Id
    @GeneratedValue
    @Column(name = "SIO_ID")
    private Long id;
    
    @Column(name = "SCHEMANAME", unique = true)
    private String schema_name;
    
    /** Organization for which this schema has been created. */
    private String schema_organization;
    
    /** username to login to database to access schema. */
    private String username;
    
    /** password to login to database to access schema. */
    private String userpass;
    
    /** locationt to access database, such as 'jdbc:mysql://localhost:' */
    private String location = "jdbc:mysql://localhost:";
    
    /** Port to access schema, on MySQL it is generally 3306. */
    private String port = "3306";
    
    ////////////////////////////////////////////
    /** Email SMTP server */
    private String email_smtp;
    
    /** Email authorized user */
    private String smtp_auth_user;
    
    /** Email password for authorized user. */
    private String smtp_auth_password;
    
    /** Email archive address. */
    private String email_archive_address;
    
    
    public static void main(String args[]){
        
        List x = getAll();
        
        System.out.println("got all");
        
        for (ListIterator li = x.listIterator(); li.hasNext();) {
            SchemaInformationObject sio = (SchemaInformationObject) li.next();
            
            System.out.println(sio.getSchema_name());
        }
        
    }
    /**
     * Returns a list of all of the SchemaInformationObjects (SIOs) found
     * @return
     */
    public static List getAll() {

        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);

        List returnList = MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).createQuery(
                "from SchemaInformationObject").list();

        MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

        return returnList;
    }
    
    /**
     * Tests to see if a connection can be made to the databse in question.
     * The string "Database Connection Verified" is returned upon successful connection.
     * @return
     */
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
    
    /**
     * Generates the connection string from the url, username and password.
     * @return
     */
    public String makeConnString(){
        
        String conn_string = makeURL() + "&user=" + username + "&password=" + userpass;
        
        System.out.print(conn_string);
        return conn_string;
    }
    
    /**
     * Generates the URL based on the location, port and schema_name.
     * @return
     */
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
            "conn: " + this.makeConnString() +
            "email smtp server: " + this.email_smtp + "\n\r" +
            "email user: " + this.smtp_auth_user + "\n\r" +
            "email archive: " + this.email_archive_address + "\n\r";
    }

    public String getSchema_organization() {
        return schema_organization;
    }

    public void setSchema_organization(String schema_organization) {
        this.schema_organization = schema_organization;
    }
    
    public static Long lookUpId(String schemaName){
        
        Long returnId = null;
        
        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
        
        List sList = MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).createQuery(
        "from SchemaInformationObject where SCHEMANAME = '" + schemaName + "'").list();
        
        if ((sList != null) && (sList.size() == 1)){
            SchemaInformationObject sio = (SchemaInformationObject) sList.get(0);
            returnId = sio.getId();
        }
        
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).getTransaction().commit();
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).close();
        
        return returnId;
    }
    public String getEmail_archive_address() {
        return email_archive_address;
    }
    public void setEmail_archive_address(String email_archive_address) {
        this.email_archive_address = email_archive_address;
    }
    public String getEmail_smtp() {
        return email_smtp;
    }
    public void setEmail_smtp(String email_smtp) {
        this.email_smtp = email_smtp;
    }
    public String getSmtp_auth_password() {
        return smtp_auth_password;
    }
    public void setSmtp_auth_password(String smtp_auth_password) {
        this.smtp_auth_password = smtp_auth_password;
    }
    public String getSmtp_auth_user() {
        return smtp_auth_user;
    }
    public void setSmtp_auth_user(String smtp_auth_user) {
        this.smtp_auth_user = smtp_auth_user;
    }
    
}