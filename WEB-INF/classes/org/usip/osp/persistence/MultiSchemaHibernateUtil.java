package org.usip.osp.persistence;

import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.usip.osp.baseobjects.UserAssignment;

/**
 * @author original HibernateUtil from Cameron McKenzie
 * @author Multi-schema material by Skip Cole
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
public class MultiSchemaHibernateUtil {
    
    /**
     * One schema will hold the connection information for the other schema
     */
    public static String principalschema = "";
    
    /** Multiple schema each with connection information stored
     * in hashtables keyed off of the schema name.
     */
    private static Hashtable setOfAnnotationConfigs = new Hashtable();
    private static Hashtable setOfSessionFactories = new Hashtable();
    
    /**
     * Hashtable of usernames keyed by the schema name.
     */
    private static Hashtable setOfUsernames = new Hashtable();
    private static Hashtable setOfPasswords = new Hashtable();
    private static Hashtable setOfLocs = new Hashtable();
    private static Hashtable setOfPorts = new Hashtable();
    private static Hashtable setOfUrls = new Hashtable();
   
    /** Our dialect for now is constant. */
    protected static String dialect = "";
    /** Our driver class for now is constant. */
    protected static String driver_class = "";

	static {
        
        principalschema = USIP_OSP_Properties.getValue("principalschema");
        
		dialect = USIP_OSP_Properties.getValue("dialect");
		driver_class = USIP_OSP_Properties.getValue("driver_class");
        
        String username = USIP_OSP_Properties.getValue("username");
        String password = USIP_OSP_Properties.getValue("password");
        String loc = USIP_OSP_Properties.getValue("loc");
        String port = USIP_OSP_Properties.getValue("port");
        String url = loc + port + "/" + principalschema + "?autoReconnect=true";
        
        setOfUsernames.put(principalschema, username);
        setOfPasswords.put(principalschema, password);
        setOfLocs.put(principalschema, loc);
        setOfPorts.put(principalschema, port);
        setOfUrls.put(principalschema, url);
        
        
        //load all of the SIOs, and put em in the session.
        try {
            
            MultiSchemaHibernateUtil.beginTransaction(principalschema, true);
            
            List sioList = MultiSchemaHibernateUtil.getSession(principalschema, true).createQuery(
            "from SchemaInformationObject").list();
            
            System.out.println("loading the sios");
            for (ListIterator<SchemaInformationObject> li = sioList.listIterator(); li.hasNext();) {
                SchemaInformationObject this_sio = (SchemaInformationObject) li.next();
                
                System.out.println("this_sio: " + this_sio.getSchema_name());
                
                storeAnSIOInHashtables(this_sio);
                
            }
            
            MultiSchemaHibernateUtil.commitAndCloseTransaction(principalschema);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}
    
    public static void storeAnSIOInHashtables(SchemaInformationObject sio){
        
        setOfUsernames.put(sio.getSchema_name(), sio.getUsername());
        setOfPasswords.put(sio.getSchema_name(), sio.getUserpass());
        setOfLocs.put(sio.getSchema_name(), sio.getLocation());
        setOfPorts.put(sio.getSchema_name(), sio.getPort());
        setOfUrls.put(sio.getSchema_name(), sio.makeURL());
        
    }
    
    public static void main(String args []){
        String schema = principalschema;
        
        String url = (String) setOfUrls.get(schema);
        String username = (String) setOfUsernames.get(schema);
        String password = (String) setOfPasswords.get(schema);
        
        System.out.println("    in initializeConnection     ");
        System.out.println("schema is " + schema);
        System.out.println("url is " + url);
        System.out.println("user is " + username);
        System.out.println("    done in initializeConnection     ");
    }

    /**
     * 
     * @param schema
     * @param root
     * @return
     */
    public static Configuration getInitializedConfiguration(String schema, boolean root) {

        System.out.println("getInitializedConfiguration " + schema + " " + root);
        
        AnnotationConfiguration config = (AnnotationConfiguration) setOfAnnotationConfigs.get(schema);
        
        if (config == null){
            config = new AnnotationConfiguration();
            initializeConnection(schema, config);
            
            if (root){
                System.out.println(" root! ");
                addRootSchemaClasses(config);
            } else {
                System.out.println("! roots ");
                addSchemaClasses(config);
            }

            setOfAnnotationConfigs.put(schema, config);
        }

        return config;
    }
    
    public static Session getSession(String schema) {
        return getSession(schema, false);
    }

	public static Session getSession(String schema, boolean root) {
        
        SessionFactory factory = (SessionFactory) setOfSessionFactories.get(schema);
        
		if (factory == null) {
            System.out.println("starting with schema : " + schema);
			Configuration config = MultiSchemaHibernateUtil.getInitializedConfiguration(schema, root);
			factory = config.buildSessionFactory();
            setOfSessionFactories.put(schema, factory);
		}

		Session s = null;
		try {

			s = factory.getCurrentSession();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;

	}
    
    

	public static void closeSession(String schema) {
		MultiSchemaHibernateUtil.getSession(schema).close();
	}
    
    /**
     * Loads the schema information from the set of hashtables holding this information.
     * 
     * @param schema
     * @param sio
     */
    public static void loadSchemaInformation(String schema, SchemaInformationObject sio){
        
        setOfUrls.put(schema, sio.makeURL());
        setOfUsernames.put(schema, sio.getUsername());
        setOfPasswords.put(schema, sio.getUserpass());
        setOfLocs.put(schema, sio.getLocation());
        setOfPorts.put(schema, sio.getPort());
        
    }
    
    public static SchemaInformationObject getSIO(String schema){
        
        SchemaInformationObject sio = new SchemaInformationObject();
        
        sio.setSchema_name(schema);
        sio.setUsername((String) setOfUsernames.get(schema));
        sio.setUserpass((String) setOfPasswords.get(schema));
        sio.setLocation((String) setOfLocs.get(schema));
        sio.setPort((String) setOfPorts.get(schema));
        
        return sio;
        
    }
    
    /**
     * The root schema (which has its own set of tables) is created from values taken
     * from the properities file.
     */
    public static void recreateRootDatabase() {
        
        System.out.println("recreatin root");
        
        Configuration config = MultiSchemaHibernateUtil.getInitializedConfiguration(principalschema, true);
        setOfAnnotationConfigs.put(principalschema, config);
        
        new SchemaExport(config).create(true, true);
    }
    
    /**
     * Recreates a simulation database
     * TODO
     * must also store database information in the root schema
     * @param dbi
     */
    public static void recreateDatabase(SchemaInformationObject dbi) {
        
        loadSchemaInformation(dbi.getSchema_name(), dbi);
        
        Configuration config = MultiSchemaHibernateUtil.getInitializedConfiguration(dbi.getSchema_name(), false);
        setOfAnnotationConfigs.put(dbi.getSchema_name(), config);
        
        new SchemaExport(config).create(true, true);
    }

    /**
     * Begins a transaction in the schema entered.
     * @param schema
     * @return
     */
	public static Session beginTransaction(String schema) {
        
	    return beginTransaction(schema, false);
        
	}
    
    /**
     * Begins a transaction in the schema entered.
     * @param schema
     * @return
     */
    public static Session beginTransaction(String schema, boolean rootflag) {
        
        Session hibernateSession = MultiSchemaHibernateUtil.getSession(schema, rootflag);
        hibernateSession.beginTransaction();
        return hibernateSession;
    }

    /**
     * Commits and closes a transaction in the schema entered.
     * @param schema
     */
	public static void commitAndCloseTransaction(String schema) {
        
		try {
			MultiSchemaHibernateUtil.getSession(schema).getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Problem in commit");
			e.printStackTrace();
		} finally {
			MultiSchemaHibernateUtil.getSession(schema).close();
		}
	}
    
    public static void commitAndCloseTransaction(Session session) {
        
        try {
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            System.out.println("Problem in commit");
            e.printStackTrace();
        }
    }

    /**
     * Rollsback a transaction in the schema entered.
     * @param schema
     */
	public static void rollbackTransaction(String schema) {
		MultiSchemaHibernateUtil.getSession(schema).getTransaction().rollback();
	}


    /**
     * Initializes a connection in the schema entered.
     * @param schema
     * @param ac
     */
	public static void initializeConnection(String schema, AnnotationConfiguration ac) {

        String url = (String) setOfUrls.get(schema);
        String username = (String) setOfUsernames.get(schema);
        String password = (String) setOfPasswords.get(schema);
        
        System.out.println("    in initializeConnection     ");
        System.out.println("schema is " + schema);
        System.out.println("url is " + url);
        System.out.println("user is " + username);
        System.out.println("    done in initializeConnection     ");
        
		ac.setProperty("hibernate.dialect", dialect);
		ac.setProperty("hibernate.current_session_context_class",
				"org.hibernate.context.ThreadLocalSessionContext");
		ac.setProperty("hibernate.connection.driver_class", driver_class);
        
		ac.setProperty("hibernate.connection.url", url);       
		ac.setProperty("hibernate.connection.username", username);
		ac.setProperty("hibernate.connection.password", password);
		ac.setProperty("hibernate.transaction.factory_class",
				"org.hibernate.transaction.JDBCTransactionFactory");
		
		ac.setProperty("hibernate.query.substitutions", "true 1, false 0");
		
		ac.setProperty("hibernate.show_sql", "false");

		ac.setProperty("hibernate.connection.isolation", "1");

		ac.setProperty("hibernate.c3p0.min_size", "0");
		ac.setProperty("hibernate.c3p0.max_size", "45");
		ac.setProperty("hibernate.c3p0.timeout", "300");
		ac.setProperty("hibernate.c3p0.max_statements", "50");
		ac.setProperty("hibernate.c3p0.idle_test_period", "300");

		System.out.println("intialization finished");
	}
    
    /**
     * Adds classes needed in the root schema. This is the schema that holds only
     * basic schema information and user login/password information.
     * @param schema
     * @param ac
     */
    public static void addRootSchemaClasses(AnnotationConfiguration ac) {
        ac.addAnnotatedClass(org.usip.osp.persistence.BaseUser.class);
        ac.addAnnotatedClass(org.usip.osp.persistence.SchemaInformationObject.class);
        ac.addAnnotatedClass(org.usip.osp.persistence.UserRegistrationInvite.class);
    }

    /**
     * Adds the schema classes needed in a database that holds all of the tables
     * to hold and entire simulation.
     * @param ac
     */
	public static void addSchemaClasses(AnnotationConfiguration ac) {
		
		// BaseObjects
		ac.addAnnotatedClass(org.usip.osp.baseobjects.Actor.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.BaseSimSection.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.CustomizeableSection.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.CustomLibrarySection.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.Debug.class);
		ac.addAnnotatedClass(org.usip.osp.communications.Alert.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.RunningSimulation.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimActorAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimPhaseAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.Simulation.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimulationPhase.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimulationRatings.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimulationSection.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.User.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.UserAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.UserTrail.class);
		
		// Communications
		ac.addAnnotatedClass(org.usip.osp.communications.ChatLine.class);
		ac.addAnnotatedClass(org.usip.osp.communications.ConvActorAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.communications.Conversation.class);
		ac.addAnnotatedClass(org.usip.osp.communications.Inject.class);
		ac.addAnnotatedClass(org.usip.osp.communications.InjectGroup.class);
		ac.addAnnotatedClass(org.usip.osp.communications.SharedDocument.class);
		
		// Models
		ac.addAnnotatedClass(org.usip.osp.modelinterface.ModelDefinitionObject.class);
		ac.addAnnotatedClass(org.usip.osp.modelinterface.ModelVariableDependencies.class);
		ac.addAnnotatedClass(org.usip.osp.modelinterface.ModelVariables.class);
		
		// Special Features
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.AllowableResponse.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.GenericVariable.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.PlayerReflection.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.SimVariable.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.IntVariable.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.IntVariableHistory.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.Trigger.class);

		System.out.println("classes added");
	}

}
