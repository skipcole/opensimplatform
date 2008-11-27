package org.usip.osp.persistence;

import java.sql.*;
import java.util.*;

import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.User;
import org.usip.osp.baseobjects.UserAssignment;
import org.usip.osp.networking.LoggedInTicket;

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
@Table(name = "BASEUSERTABLE")
@Proxy(lazy = false)
public class BaseUser {

    @Id
    @GeneratedValue
    @Column(name = "BU_ID")
    private Long id;

    @Column(name = "USERNAME", unique = true)
    private String username = "";

    @Column(name = "PASSWORD")
    private String password = "";

    @Column(name = "FULLNAME")
    private String full_name = "";
    
    @Column(name = "FIRSTNAME")
    private String first_name = "";

    @Column(name = "LASTNAME")
    private String last_name = "";

    @Column(name = "MIDDLENAME")
    private String middle_name = "";
    
    @Column(name = "REGISTERED")
    private boolean registered = false;    
    
    /**
     * Zero argument constructed needed by hibernate and other programs.
     *
     */
    public BaseUser (){
        
    }

    public BaseUser (String the_username, String the_password){
        
        this.setUsername(the_username);
        this.setPassword(the_password);

        saveMe();
        
    }
    
    public void updateMe(String fi, String fu, String la, String mi){
    	this.first_name = fi;
    	this.full_name = fu;
    	this.last_name = la;
    	this.middle_name = mi;
    	
    	saveMe();
    	
    }

    public void saveMe(){
        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(this);                        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
    }
    
    /**
     * 
     * @param u
     * @param p
     * @param f
     */
    public BaseUser (String u, String p, String f, String s){
        
        // Looks to see if username exists.
        BaseUser bu = getByUsername(u);
        
        if (bu == null){
            
            System.out.println("Base user " + u + " not found.");
            
            bu = new BaseUser();
            bu.setUsername(u);
            bu.setPassword(p);
            bu.setFull_name(f);
            
            MultiSchemaHibernateUtil.beginTransaction(
                    MultiSchemaHibernateUtil.principalschema, true);
            
            MultiSchemaHibernateUtil.getSession(
                    MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(bu);
            MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
        } else {
            System.out.println("Base user " + u + " already exists.");
        }
        
        this.id = bu.getId();
        this.setUsername(bu.getUsername());
        this.setPassword(bu.getPassword());
        
        System.out.println(bu.getUsername());
        
        if (s != null){
            assignUserToSchema(this.getId(), s);
            
        }
    }
    
    public void assignUserToSchema(Long _bu_id, String _schema){
        
        Long schema_id = SchemaInformationObject.lookUpId(_schema);
        
        // Add the user schema assignement
        UserSchemaAssignment usa = new UserSchemaAssignment(_bu_id, schema_id);
        
        MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
        MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(usa);                        
        MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

    }
    

    @Transient
    private ArrayList authorizedSchemas = new ArrayList();

    public ArrayList getAuthorizedSchemas() {
        return authorizedSchemas;
    }

    public void setAuthorizedSchemas(ArrayList authorizedSchemas) {
        this.authorizedSchemas = authorizedSchemas;
    }

    public static BaseUser validateUser(String username, String password) {

        // byte[] hashedP = MysqlDatabase.getPHash(password);
        // String phash = new String(hashedP);
        if ((username == null) || (password == null)) {
            System.out.println("username or password was null.");
            return null;
        }

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        List sList = MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true).createQuery(
                "from BaseUser where USERNAME = '" + username
                        + "' AND PASSWORD = '" + password + "'").list();

        if ((sList == null) || (sList.size() == 0)) {
            MultiSchemaHibernateUtil.getSession(
                    MultiSchemaHibernateUtil.principalschema, true)
                    .getTransaction().commit();
            MultiSchemaHibernateUtil.getSession(
                    MultiSchemaHibernateUtil.principalschema, true).close();
            return null;
        } else {
            BaseUser bu = (BaseUser) sList.get(0);

            bu.setAuthorizedSchemas(bu.getAuthorizedSchemas(
                    MultiSchemaHibernateUtil.getSession(
                            MultiSchemaHibernateUtil.principalschema, true), bu
                            .getId()));

            MultiSchemaHibernateUtil.getSession(
                    MultiSchemaHibernateUtil.principalschema, true).evict(bu);
            MultiSchemaHibernateUtil.getSession(
                    MultiSchemaHibernateUtil.principalschema, true)
                    .getTransaction().commit();
            MultiSchemaHibernateUtil.getSession(
                    MultiSchemaHibernateUtil.principalschema, true).close();
            return bu;
        }

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    private static Session startMeUp() {

        String ps = MultiSchemaHibernateUtil.principalschema;

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        return MultiSchemaHibernateUtil.getSession(ps);
    }

    private static void shutMeDown(Session root_session) {

        String ps = MultiSchemaHibernateUtil.principalschema;

        MultiSchemaHibernateUtil.commitAndCloseTransaction(root_session);
    }

    /**
     * Given a user_id return all of the schemas to which this user has been
     * authorized to access.
     * 
     * assigned
     * 
     * @param user_id
     * @return
     */
    public static ArrayList getAuthorizedSchemas(Long user_id) {

        ArrayList returnList = new ArrayList();

        Session root_session = startMeUp();

        List sList = root_session.createQuery(
                "from UserSchemaAssignment where BU_ID = " + user_id).list();

        for (ListIterator<UserSchemaAssignment> li = sList.listIterator(); li
                .hasNext();) {
            UserSchemaAssignment this_usa = (UserSchemaAssignment) li.next();

            SchemaInformationObject sio = (SchemaInformationObject) root_session
                    .get(SchemaInformationObject.class, this_usa.getId().getSchema_id());

            SchemaGhost sg = new SchemaGhost();

            sg.setId(sio.getId());
            sg.setSchema_name(sio.getSchema_name());
            sg.setSchema_organization(sio.getSchema_organization());

            System.out.println("user authorized for " + sio.getSchema_name());

            returnList.add(sg);
        }

        MultiSchemaHibernateUtil.commitAndCloseTransaction(root_session);

        return returnList;

    }

    /**
     * Given a user_id return all of the schemas to which this user has been
     * authorized to access.
     * 
     * assigned
     * 
     * @param user_id
     * @return
     */
    public static ArrayList getAuthorizedSchemas(Session root_session, Long user_id) {

        System.out.println("getting schemas");

        ArrayList returnList = new ArrayList();

        List sList = root_session.createQuery(
                "from UserSchemaAssignment where BU_ID = " + user_id).list();

        for (ListIterator<UserSchemaAssignment> li = sList.listIterator(); li
                .hasNext();) {
            UserSchemaAssignment this_usa = (UserSchemaAssignment) li.next();

            SchemaInformationObject sio = (SchemaInformationObject) root_session
                    .get(SchemaInformationObject.class, this_usa.getId().getSchema_id());

            SchemaGhost sg = new SchemaGhost();

            sg.setId(sio.getId());
            sg.setSchema_name(sio.getSchema_name());
            sg.setSchema_organization(sio.getSchema_organization());

            System.out.println("user authorized for " + sio.getSchema_name());

            returnList.add(sg);
        }

        return returnList;

    }

    /**
     * 
     * @param the_username
     * @return
     */
    public static BaseUser getByUsername(String the_username) {

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        List returnList = MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true).createQuery(
                "from BaseUser where username = '" + the_username + "'").list();

        BaseUser bu = null;

        try {
        	if ((returnList != null) && (returnList.size() > 0)){
        		bu = (BaseUser) returnList.get(0);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }

        MultiSchemaHibernateUtil
                .commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

        return bu;
    }
    
    public static boolean checkIfUserExists(String the_username){
    	
    	BaseUser bu = getByUsername(the_username);
    	
    	if (bu == null){
    		return false;
    	} else {
    		return true;
    	}
    }
    
    /**
     * Ensures that that base user created is not a new version of an existing user.
     * 
     * @param the_username
     * @param the_password
     * @param schema
     * @return
     */
    public static BaseUser getUniqueUser(String the_username, 
            String the_password, String schema){
        
        System.out.println("getting user by name: " + the_username);
        BaseUser bu = getByUsername(the_username);
        
        if (bu!= null) {
            return bu;
        } else {
            bu = new BaseUser(the_username, the_password);
            
            UserSchemaAssignment usa = null;
            
            // Assign user to schema indicated
            if (schema != null){
                SchemaInformationObject.lookUpId(schema);
                usa = 
                    new UserSchemaAssignment(bu.getId(), SchemaInformationObject.lookUpId(schema));
            }
            
            MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema);

            MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema).saveOrUpdate(bu);
            
            if (usa != null){
            	MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema).saveOrUpdate(usa);
            }
            MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
            
            return bu;
        }
    }

    /**
     * Returns the base user with this id. This is handled specifically here
     * (and not just done as a typical hibernate 'get' operation) since we are
     * coming from the root schema and not just any old schema.
     * 
     * @param user_id
     * @return
     */
    public static BaseUser getByUserId(Long user_id) {

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        BaseUser bu = (BaseUser) MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true).get(
                BaseUser.class, user_id);

        //MultiSchemaHibernateUtil.getSession(
        //        MultiSchemaHibernateUtil.principalschema, true).evict(bu);

        MultiSchemaHibernateUtil
                .commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

        return bu;
    }
    
    /**
     * @return Returns the real_name.
     */
    public String getFull_name() {
        return full_name;
    }

    /**
     * @param real_name
     *            The real_name to set.
     */
    public void setFull_name(String real_name) {
        this.full_name = real_name;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name
     *            the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name
     *            the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the middle_name
     */
    public String getMiddle_name() {
        return middle_name;
    }

    /**
     * @param middle_name
     *            the middle_name to set
     */
    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

}
