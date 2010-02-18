package org.usip.osp.persistence;

import java.util.*;

import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.usip.osp.baseobjects.User;
import org.apache.log4j.*;

/**
 * This class holds all of the personal information (name, email address, etc.) on players.
 */
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
@Table(name = "BASEUSERTABLE")
@Proxy(lazy = false)
public class BaseUser {

    @Id
    @GeneratedValue
    @Column(name = "BU_ID")
    private Long id;

	@Column(name = "USERNAME", unique = true)
    private String username = ""; //$NON-NLS-1$

    @Column(name = "PASSWORD")
    private String password = ""; //$NON-NLS-1$

    @Column(name = "FULLNAME")
    private String full_name = ""; //$NON-NLS-1$
    
    @Column(name = "FIRSTNAME")
    private String first_name = ""; //$NON-NLS-1$

    @Column(name = "LASTNAME")
    private String last_name = ""; //$NON-NLS-1$

    @Column(name = "MIDDLENAME")
    private String middle_name = ""; //$NON-NLS-1$
    
    @Column(name = "REGISTERED")
    private boolean registered = false;    
    
    /**
     * Zero argument constructed needed by hibernate and other programs.
     *
     */
    public BaseUser (){
        
    }

    /** Creates a new base user with the username and password passed in. */
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
            
            Logger.getRootLogger().debug("Base user " + u + " not found."); //$NON-NLS-1$ //$NON-NLS-2$
            
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
            Logger.getRootLogger().debug("Base user " + u + " already exists."); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        this.id = bu.getId();
        this.setUsername(bu.getUsername());
        this.setPassword(bu.getPassword());
        
        Logger.getRootLogger().debug(bu.getUsername());
        
    }
    

    /**
     * Returns the list of authorized schemas.
     * @return
     */
    public ArrayList getAuthorizedSchemas() {
        return getAuthorizedSchemas(this.id);
    }

    
    /**
     * Returns the user if the password entered is correct, otherwise returns null.
     * 
     * @param username
     * @param password
     * @return
     */
    public static BaseUser validateUser(String username, String password) {

        // byte[] hashedP = MysqlDatabase.getPHash(password);
        // String phash = new String(hashedP);
        if ((username == null) || (password == null)) {
            Logger.getRootLogger().debug("username or password was null."); //$NON-NLS-1$
            return null;
        }

        MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        List sList = MultiSchemaHibernateUtil.getSession(
                MultiSchemaHibernateUtil.principalschema, true).createQuery(
                "from BaseUser where USERNAME = :username AND PASSWORD = :password ")
                .setString("username", username)
                .setString("password", password)
                .list(); //$NON-NLS-1$ 

        if ((sList == null) || (sList.size() == 0)) {
            MultiSchemaHibernateUtil.getSession(
                    MultiSchemaHibernateUtil.principalschema, true)
                    .getTransaction().commit();
            MultiSchemaHibernateUtil.getSession(
                    MultiSchemaHibernateUtil.principalschema, true).close();
            return null;
        } else {
            BaseUser bu = (BaseUser) sList.get(0);

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
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
		this.id = id;
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

        ArrayList<SchemaGhost> returnList = new ArrayList<SchemaGhost>();
        
        // Loop over known schemas and see if this user exists in them.
        for (ListIterator<SchemaInformationObject> sio_l = SchemaInformationObject.getAll().listIterator(); sio_l.hasNext();) {
        	SchemaInformationObject sio = sio_l.next();
        	
        	if (User.doesUserExistInSchema(user_id,sio.getSchema_name())){
                SchemaGhost sg = new SchemaGhost();

                sg.setId(sio.getId());
                sg.setSchema_name(sio.getSchema_name());
                sg.setSchema_organization(sio.getSchema_organization());

                Logger.getRootLogger().debug("user authorized for " + sio.getSchema_name()); //$NON-NLS-1$

                returnList.add(sg);
        	}
        	
        }

        return returnList;

    }

    /**
     * 
     * @param the_username
     * @return
     */
    public static BaseUser getByUsername(String the_username) {

    	BaseUser bu = null;
    	
        Session s = MultiSchemaHibernateUtil.beginTransaction(
                MultiSchemaHibernateUtil.principalschema, true);

        try {
            List returnList = s.createQuery(
                    "from BaseUser where username = '" + the_username + "'").list(); //$NON-NLS-1$ //$NON-NLS-2$
        	
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
    
    /**
     * 
     * @param the_username
     * @return
     */
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
        
        Logger.getRootLogger().debug("getting user by name: " + the_username); //$NON-NLS-1$
        BaseUser bu = getByUsername(the_username);
              
        if (bu!= null) {
            return bu;
        } else {
            bu = new BaseUser(the_username, the_password);
            
            MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema);

            MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema).saveOrUpdate(bu);
            
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

        MultiSchemaHibernateUtil
                .commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);

        return bu;
    }
    
    /**
     * @return Returns the real_name.
     */
    public String getFull_name() {
        return this.full_name;
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
        return this.first_name;
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
        return this.last_name;
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
        return this.middle_name;
    }

    /**
     * @param middle_name
     *            the middle_name to set
     */
    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

	public boolean isRegistered() {
		return this.registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	
	/** Id used when objects are exported and imported moving across databases. */
	private Long transit_id;

	public Long getTransit_id() {
		return this.transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}

}
