package org.usip.osp.persistence;

import java.sql.*;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.apache.log4j.*;

/**
 * @author original HibernateUtil from Cameron McKenzie
 * @author Multi-schema material by Skip Cole
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 *         The USIP Open Simulation Platform is free software; you can
 *         redistribute it and/or modify it under the terms of the new BSD Style
 *         license associated with this distribution.<br>
 * 
 *         The USIP Open Simulation Platform is distributed WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. <BR>
 * 
 */
public class MultiSchemaHibernateUtil {

	/**
	 * One schema will hold the connection information for the other schema
	 */
	public static String principalschema = ""; //$NON-NLS-1$

	private static String username;
	private static String password;
	private static String loc;
	private static String port;
	private static String url;

	private static String conn_string;

	/** Our dialect for now is constant. */
	protected static String dialect = ""; //$NON-NLS-1$
	/** Our driver class for now is constant. */
	protected static String driver_class = ""; //$NON-NLS-1$

	/**
	 * Session factories are not light objects to create. Create them once and store them 
	 * in the hashtable.
	 */
	private static Hashtable setOfSessionFactories = new Hashtable();

	static {

		principalschema = USIP_OSP_Properties.getValue("principalschema"); //$NON-NLS-1$

		dialect = USIP_OSP_Properties.getValue("dialect"); //$NON-NLS-1$
		driver_class = USIP_OSP_Properties.getValue("driver_class"); //$NON-NLS-1$

		username = USIP_OSP_Properties.getValue("username"); //$NON-NLS-1$
		password = USIP_OSP_Properties.getValue("password"); //$NON-NLS-1$
		loc = USIP_OSP_Properties.getValue("loc"); //$NON-NLS-1$
		port = USIP_OSP_Properties.getValue("port"); //$NON-NLS-1$
		url = loc + port + "/" + principalschema + "?autoReconnect=true"; //$NON-NLS-1$ //$NON-NLS-2$
		conn_string = url + "&user=" + username + "&password=" + password;

	}

	/**
	 * 
	 * @param schema
	 * @return
	 */
	public static String makeSchemaConnString(String schema) {

		String temp_url = loc + port + "/" + schema + "?autoReconnect=true"; //$NON-NLS-1$ 
		String temp_conn_string = temp_url + "&user=" + username + "&password=" + password;

		return temp_conn_string;

	}

	/**
	 * 
	 * @return
	 */
	public boolean checkDatabaseCreated() {

		List users = new ArrayList();

		Connection conn = null;

		try {

			conn = MysqlDatabase.getConnection(conn_string);
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery("select * from users");

			while (rst.next()) {
				users.add(rst.getString(1));
			}

			conn.close();

		} catch (Exception e) {
			Logger.getRootLogger().debug("Problem getting users");
			return false;
		} finally {
			try {
				conn.close();
			} catch (Exception e1) {
				Logger.getRootLogger().debug("Could not close connection in pso.");
			}
		}

		boolean returnValue = false;

		if (users == null) {
			returnValue = false;
		} else if (users.size() > 0) {
			returnValue = true;
		}

		return returnValue;
	}

	/**
	 * Returns a database connection.
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection connection = null;

		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); //$NON-NLS-1$

			connection = DriverManager.getConnection(conn_string);

			return connection;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Debugging method just used to check what is found in properties file.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		String schema = principalschema;

		Logger.getRootLogger().warn("    in initializeConnection     "); //$NON-NLS-1$
		Logger.getRootLogger().warn("schema is " + schema); //$NON-NLS-1$
		Logger.getRootLogger().warn("url is " + url); //$NON-NLS-1$
		Logger.getRootLogger().warn("user is " + username); //$NON-NLS-1$
		Logger.getRootLogger().warn("    done in initializeConnection     "); //$NON-NLS-1$

		System.out.println("test conn was: " + testConn());

	}

	/**
	 * Tests to see if a connection can be made to the database in question. The
	 * string "Database Connection Verified" is returned upon successful
	 * connection.
	 * 
	 * @return
	 */
	public static boolean testConn() {

		try {

			Connection conn = MysqlDatabase.getConnection(conn_string);

			if (conn == null) {
				return false;
			}

			conn.close();
		} catch (Exception e) {
			Logger.getRootLogger().warn("Error in closing connection in test conn."); //$NON-NLS-1$
		}

		return true;
	}

	/**
	 * 
	 * @param schema
	 * @param root
	 * @return
	 */
	public static Configuration getInitializedConfiguration(String schema, boolean root) {

		Logger.getRootLogger().warn("getInitializedConfiguration " + schema + " " + root); //$NON-NLS-1$ //$NON-NLS-2$

		AnnotationConfiguration config = new AnnotationConfiguration();

		if (root) {

			initializeConnection(conn_string, config);

			Logger.getRootLogger().warn(" root database! "); //$NON-NLS-1$
			addRootSchemaClasses(config);
		} else {
			String tempURL = makeSchemaConnString(schema);

			initializeConnection(tempURL, config);

			Logger.getRootLogger().warn("! root database "); //$NON-NLS-1$
			addSchemaClasses(config);
		}

		return config;
	}

	public static Session getSession(String schema) {
		return getSession(schema, false);
	}

	/**
	 * Returns the session for the schema. 
	 * @param schema
	 * @param root
	 * @return
	 */
	public static Session getSession(String schema, boolean root) {

		SessionFactory factory = (SessionFactory) setOfSessionFactories.get(schema);

		if (factory == null) {
			Logger.getRootLogger().debug("starting with schema : " + schema); //$NON-NLS-1$
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
	 * The root schema (which has its own set of tables) is created from values
	 * taken from the properities file.
	 */
	public static void recreateRootDatabase() {

		Logger.getRootLogger().debug("recreatin root"); //$NON-NLS-1$

		Configuration config = MultiSchemaHibernateUtil.getInitializedConfiguration(principalschema, true);

		new SchemaExport(config).create(true, true);
	}

	/**
	 * Recreates a simulation database TODO must also store database information
	 * in the root schema
	 * 
	 * @param dbi
	 */
	public static void recreateDatabase(SchemaInformationObject dbi) {

		Configuration config = MultiSchemaHibernateUtil.getInitializedConfiguration(dbi.getSchema_name(), false);

		new SchemaExport(config).create(true, true);
	}

	/**
	 * Begins a transaction in the schema entered.
	 * 
	 * @param schema
	 * @return
	 */
	public static Session beginTransaction(String schema) {

		return beginTransaction(schema, false);

	}

	/**
	 * Begins a transaction in the schema entered.
	 * 
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
	 * 
	 * @param schema
	 */
	public static void commitAndCloseTransaction(String schema) {

		try {
			MultiSchemaHibernateUtil.getSession(schema).getTransaction().commit();
		} catch (Exception e) {
			Logger.getRootLogger().debug("Problem in commit"); //$NON-NLS-1$
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
			Logger.getRootLogger().debug("Problem in commit"); //$NON-NLS-1$
			e.printStackTrace();
		}
	}

	/**
	 * Rollsback a transaction in the schema entered.
	 * 
	 * @param schema
	 */
	public static void rollbackTransaction(String schema) {
		MultiSchemaHibernateUtil.getSession(schema).getTransaction().rollback();
	}

	/**
	 * Initializes a connection in the schema entered.
	 * 
	 * @param schema
	 * @param ac
	 */
	public static void initializeConnection(String tempURL, AnnotationConfiguration ac) {

		Logger.getRootLogger().debug("    in initializeConnection     "); //$NON-NLS-1$
		Logger.getRootLogger().debug("url is " + url); //$NON-NLS-1$
		Logger.getRootLogger().debug("user is " + username); //$NON-NLS-1$
		Logger.getRootLogger().debug("    done in initializeConnection     "); //$NON-NLS-1$

		ac.setProperty("hibernate.dialect", dialect); //$NON-NLS-1$
		ac.setProperty("hibernate.current_session_context_class", //$NON-NLS-1$
				"org.hibernate.context.ThreadLocalSessionContext"); //$NON-NLS-1$
		ac.setProperty("hibernate.connection.driver_class", driver_class); //$NON-NLS-1$

		//ac.setProperty("hibernate.connection.url", url); //$NON-NLS-1$
		ac.setProperty("hibernate.connection.url", tempURL); //$NON-NLS-1$

		ac.setProperty("hibernate.connection.username", username); //$NON-NLS-1$
		ac.setProperty("hibernate.connection.password", password); //$NON-NLS-1$
		ac.setProperty("hibernate.transaction.factory_class", //$NON-NLS-1$
				"org.hibernate.transaction.JDBCTransactionFactory"); //$NON-NLS-1$

		ac.setProperty("hibernate.query.substitutions", "true 1, false 0"); //$NON-NLS-1$ //$NON-NLS-2$

		ac.setProperty("hibernate.show_sql", "false"); //$NON-NLS-1$ //$NON-NLS-2$

		ac.setProperty("hibernate.connection.isolation", "1"); //$NON-NLS-1$ //$NON-NLS-2$

		ac.setProperty("hibernate.c3p0.min_size", "0"); //$NON-NLS-1$ //$NON-NLS-2$
		ac.setProperty("hibernate.c3p0.max_size", "45"); //$NON-NLS-1$ //$NON-NLS-2$
		ac.setProperty("hibernate.c3p0.timeout", "300"); //$NON-NLS-1$ //$NON-NLS-2$
		ac.setProperty("hibernate.c3p0.max_statements", "50"); //$NON-NLS-1$ //$NON-NLS-2$
		ac.setProperty("hibernate.c3p0.idle_test_period", "300"); //$NON-NLS-1$ //$NON-NLS-2$

		Logger.getRootLogger().debug("intialization finished"); //$NON-NLS-1$
	}

	/**
	 * Adds classes needed in the root schema. This is the schema that holds
	 * only basic schema information and user login/password information.
	 * 
	 * @param schema
	 * @param ac
	 */
	public static void addRootSchemaClasses(AnnotationConfiguration ac) {
		ac.addAnnotatedClass(org.usip.osp.persistence.BaseUser.class);
		ac.addAnnotatedClass(org.usip.osp.persistence.SchemaInformationObject.class);
	}

	/**
	 * Adds the schema classes needed in a database that holds all of the tables
	 * to hold and entire simulation.
	 * 
	 * @param ac
	 */
	public static void addSchemaClasses(AnnotationConfiguration ac) {

		// BaseObjects
		ac.addAnnotatedClass(org.usip.osp.baseobjects.Actor.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.ActorAssumedIdentity.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.BaseSimSection.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.BaseSimSectionDepObjectAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.CustomizeableSection.class);
		ac.addAnnotatedClass(org.usip.osp.communications.Alert.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.RunningSimulation.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimActorAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimConversationAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimPhaseAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimSectionRSDepOjbectAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.Simulation.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimulationMetaPhase.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimulationPhase.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimulationRatings.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.SimulationSectionAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.User.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.UserAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.baseobjects.UserTrail.class);

		// Communications
		ac.addAnnotatedClass(org.usip.osp.communications.ChatLine.class);
		ac.addAnnotatedClass(org.usip.osp.communications.CommunicationsHub.class);
		ac.addAnnotatedClass(org.usip.osp.communications.CommunicationsReceived.class);
		ac.addAnnotatedClass(org.usip.osp.communications.ConvActorAssignment.class);
		ac.addAnnotatedClass(org.usip.osp.communications.Conversation.class);
		ac.addAnnotatedClass(org.usip.osp.communications.Email.class);
		ac.addAnnotatedClass(org.usip.osp.communications.EmailRecipients.class);
		ac.addAnnotatedClass(org.usip.osp.communications.Event.class);
		ac.addAnnotatedClass(org.usip.osp.communications.Inject.class);
		ac.addAnnotatedClass(org.usip.osp.communications.InjectGroup.class);
		ac.addAnnotatedClass(org.usip.osp.communications.SharedDocument.class);
		ac.addAnnotatedClass(org.usip.osp.communications.SharedDocActorNotificAssignObj.class);

		// Models
		ac.addAnnotatedClass(org.usip.osp.modelinterface.ModelDefinitionObject.class);
		ac.addAnnotatedClass(org.usip.osp.modelinterface.ModelVariableDependencies.class);
		ac.addAnnotatedClass(org.usip.osp.modelinterface.ModelVariables.class);

		// Special Features
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.AllowableResponse.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.GenericVariable.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.OSPObjectHistory.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.PlayerReflection.class);
		ac.addAnnotatedClass(org.usip.osp.specialfeatures.Trigger.class);

		Logger.getRootLogger().debug("classes added"); //$NON-NLS-1$
	}

}
