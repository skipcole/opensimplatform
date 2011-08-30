package org.usip.osp.baseobjects;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.FileIO;
import org.usip.osp.persistence.*;
import org.usip.osp.sharing.ExportableObject;
import org.usip.osp.sharing.ObjectPackager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.log4j.*;

/**
 * This class represents sections that can be given to an actor at any given
 * phase of the game.
 * 
 */
/*
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it
 * and/or modify it under the terms of the new BSD Style license associated with
 * this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. <BR>
 */
@Entity
@Table(name = "BASE_SIM_SECTIONS")
@Proxy(lazy = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BaseSimSection implements Comparable, ExportableObject {

	/**
	 * Just used for occasional debugging.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		// BaseSimSection.readNewBaseSimSectionsFromXMLFiles("test");

		// BaseSimSection.readBaseSimSectionsFromXMLFiles();

		/*
		 * List<BaseSimSection> bList = BaseSimSection.getAllControl();
		 * 
		 * for (ListIterator la = bList.listIterator(); la.hasNext();) {
		 * BaseSimSection bss = (BaseSimSection) la.next();
		 */
		CustomizeableSection bss = new CustomizeableSection();
		bss.setConfers_read_ability(true);
		bss.setBigString("<H1>Broadcast stuff</H1><p>words</p>"); //$NON-NLS-1$

		Logger.getRootLogger()
				.debug("can read " + bss.isConfers_read_ability()); //$NON-NLS-1$

		Logger.getRootLogger().debug("--------------------"); // //$NON-NLS-1$
		Logger.getRootLogger().debug(ObjectPackager.getObjectXML(bss));

		/*
		 * int aliquot = 2; int numPrinted = 0; if (sss.length() > aliquot) {
		 * while (numPrinted < sss.length()) { char[] c = new char[aliquot]; int
		 * numToGet = aliquot; if ((sss.length() - numPrinted) < aliquot) {
		 * numToGet = sss.length() - numPrinted; }
		 * Logger.getRootLogger().debug(numPrinted + " " + numToGet);
		 * sss.getChars(numPrinted, numPrinted + numToGet, c, 0);
		 * Logger.getRootLogger().debug(c); try { sss.wait(1000); } catch
		 * (Exception e) { e.printStackTrace(); }
		 * 
		 * numPrinted += aliquot; } }
		 */

	}

	/**
	 * Checks to see if a simulation section with the same creating
	 * organization, name and version has been loaded. If so, it returns the id
	 * of the section, else it returns null.
	 * 
	 * @param schema
	 * @param bss
	 * @return
	 */
	public static Long checkInstalled(String schema, BaseSimSection bss) {

		BaseSimSection correspondingBss = null;

		try {
			correspondingBss = getByName(schema, bss.creatingOrganization,
					bss.uniqueName, bss.version);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (correspondingBss == null) {
			return null;
		} else {
			return correspondingBss.getId();
		}
	}

	/**
	 * Returns true if string passed in starts with a letter.
	 * 
	 * @param s
	 * @return
	 */
	public static boolean startsWithLetter(String s) {
		if ((s == null) || (s.equalsIgnoreCase(""))) { //$NON-NLS-1$
			return false;
		}

		s = s.toLowerCase();

		char c = s.charAt(0);

		if ((c < 'a') || (c > 'z')) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Reads the simulation sections from xml files, but does not save them to
	 * the database.
	 * 
	 * @param schema
	 * @return Returns a string indicating success, or not.
	 * 
	 */
	public static List screenBaseSimSectionsFromXMLFiles(String schema) {
		
		// The set of base simulation sections are read out of
		// XML files stored in the simulation_section_information directory.
		String fileLocation = FileIO.getBase_section_web_dir();
		
		return screenBaseSimSectionsFromXMLFiles(schema, fileLocation);
	}
	
	public static List screenPluginSectionsFromXMLFiles(String schema) {
		
		// The set of base simulation sections are read out of
		// XML files stored in the simulation_section_information directory.
		String fileLocation = FileIO.getPlugin_dir();
		
		return screenBaseSimSectionsFromXMLFiles(schema, fileLocation);
	}

	/**
	 * Reads the simulation sections from xml files, but does not save them to
	 * the database.
	 * 
	 * @param schema
	 * @return Returns a string indicating success, or not.
	 * 
	 */
	public static List screenBaseSimSectionsFromXMLFiles(String schema, String fileLocation) {

		ArrayList returnList = new ArrayList();

		File locDir = new File(fileLocation);

		if (locDir == null) {
			Logger.getRootLogger().debug(
					"Problem finding files at " + fileLocation); //$NON-NLS-1$
			return returnList;
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				Logger.getRootLogger().debug(
						"Problem finding files at " + fileLocation); //$NON-NLS-1$
				return returnList;
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (fName.endsWith(".xml")) { //$NON-NLS-1$

						try {
							String fullFileLoc = fileLocation + fName;
							returnList.add(BaseSimSection.readAheadXML(schema,
									files[ii], fullFileLoc));

						} catch (Exception e) {
							Logger.getRootLogger().debug(
									"problem reading in file " + fName); //$NON-NLS-1$
							Logger.getRootLogger().debug(e.getMessage());
						}
					}

				}
			}

			return returnList;
		} // end of if found files.
	} // end of method

	/**
	 * Reads the simulation sections from xml files.
	 * 
	 * @param schema
	 * @return Returns a string indicating success, or not.
	 * 
	 */
	public static String readBaseSimSectionsFromXMLFiles(String schema,
			String fileLocation) {

		// The set of base simulation sections are read out of
		// XML files stored in the simulation_section_information directory.

		Logger.getRootLogger().debug("Looking for files at: " + fileLocation); //$NON-NLS-1$

		File locDir = new File(fileLocation);

		File files[] = getFilesFromDirectory(fileLocation, locDir, ".xml");

		for (int ii = 0; ii < files.length; ii++) {

			String fName = files[ii].getName();

			try {
				readInXMLFile(schema, files[ii]);
			} catch (Exception e) {
				Logger.getRootLogger()
						.debug("problem reading in file " + fName); //$NON-NLS-1$
				Logger.getRootLogger().debug(e.getMessage());
			}
		}

		return "Read in Base Simulation Section Information."; //$NON-NLS-1$
	}

	public static File[] getFilesFromDirectory(String fileLocation,
			File locDir, String fileTypes) {

		ArrayList tempList = new ArrayList();

		if (locDir == null) {
			return null;
			// TODO toss error
			// return null
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				return null;
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();
					if (fName.endsWith(fileTypes)) {
						tempList.add(files[ii]);
					}
				}
			}

			File returnFiles[] = new File[tempList.size()];

			int ii = 0;
			for (ListIterator<File> bi = tempList.listIterator(); bi.hasNext();) {
				File bid = bi.next();
				returnFiles[ii] = bid;
				++ii;
			}

			return returnFiles;
		}

	}

	/**
	 * Saves this object to the database.
	 * 
	 * @param schema
	 */
	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Removes a base simulation section. TODO - should check to see if things
	 * are using it ?
	 * 
	 * @param schema
	 * @param the_id
	 */
	public static void removeBSS(String schema, String the_id) {

		BaseSimSection bss = BaseSimSection.getById(schema, the_id);

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).delete(bss);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Returns an object from an xml file without saving it.
	 * 
	 * @param schema
	 * @param thisFile
	 * @param customLibName
	 * @return
	 */
	public static Object readAheadXML(String schema, File thisFile,
			String fullFileLoc) {

		String fullBSS = FileIO.getFileContents(thisFile);

		Object bRead = unpackageXML(fullBSS);

		BaseSimSection bss = (BaseSimSection) bRead;

		// Using the directory field temporarily just to pass back location on
		// where the file read is.
		bss.setDirectory(fullFileLoc);

		return bss;
	}

	/**
	 * Reads an object from an XML file and saves it to the database.
	 * 
	 * @param schema
	 * @param thisFile
	 */
	public static void readInXMLFile(String schema, File thisFile) {

		String fullBSS = FileIO.getFileContents(thisFile);

		// BaseSimSection bRead = unpackageXML(fullBSS);
		Object bRead = unpackageXML(fullBSS);

		if (bRead != null) {

			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(bRead);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		}
	}

	/**
	 * Reads an object from an XML file and saves it to the database.
	 * 
	 * @param schema
	 * @param thisFile
	 */
	public static void reloadXMLFile(String schema, File thisFile, Long the_id) {

		String fullBSS = FileIO.getFileContents(thisFile);

		BaseSimSection bRead = unpackageXML(fullBSS);
		bRead.setId(the_id);

		if (bRead != null) {

			MultiSchemaHibernateUtil.beginTransaction(schema);
			MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(bRead);
			MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		}
	}

	/**
	 * 
	 * @param xmlString
	 * @return
	 */
	public static BaseSimSection unpackageXML(String xmlString) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("bss", BaseSimSection.class); //$NON-NLS-1$

		return (BaseSimSection) xstream.fromXML(xmlString);
	}

	/** The primary key of this section. */
	@Id
	@GeneratedValue
	@Column(name = "BASE_SIMSEC_ID")
	protected Long id;

	/** Id used when objects are exported and imported moving across databases. */
	protected Long transit_id;

	public Long getTransitId() {
		return this.transit_id;
	}

	public void setTransitId(Long transit_id) {
		this.transit_id = transit_id;
	}

	// ////////////////////////////////////////////////////////////////////////////
	protected String creatingOrganization = ""; //$NON-NLS-1$
	protected String uniqueName = ""; //$NON-NLS-1$
	protected String version = ""; //$NON-NLS-1$

	public String getCreatingOrganization() {
		return this.creatingOrganization;
	}

	public void setCreatingOrganization(String creatingOrganization) {
		this.creatingOrganization = creatingOrganization;
	}

	public String getUniqueName() {
		return this.uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	// ///////////////////////////////////////////////////////////

	/** Description of this standard section. */
	@Column(name = "BASE_SIMSEC_DESC")
	@Lob
	protected String description = ""; //$NON-NLS-1$

	/** URL of this section */
	@Column(name = "BASE_SIMSEC_URL")
	protected String url = ""; //$NON-NLS-1$

	/** Directory of this section */
	@Column(name = "BASE_SIMSEC_DIR")
	protected String directory = ""; //$NON-NLS-1$

	/** Filename of this section */
	@Column(name = "BASE_SIMSEC_FILENAME")
	protected String page_file_name = ""; //$NON-NLS-1$

	/** Recommended (tab heading) of this section */
	@Column(name = "BASE_TAB_HEADING")
	protected String rec_tab_heading = ""; //$NON-NLS-1$

	private static final String NOSAMPLEIMAGE = "no_sample_image.png"; //$NON-NLS-1$

	/**
	 * Sample image to let the simulation creator see what this section looks
	 * like.
	 */
	@Column(name = "BASE_SAMPLE_IMAGE")
	protected String sample_image = ""; //$NON-NLS-1$

	/**
	 * Sets if this is a control section or not for this simulation - indicating
	 * that it will automatically be added to the control player for all phases.
	 */
	@Column(name = "SIMSEC_CONTROL")
	protected boolean control_section = false;

	@Column(name = "CUSTOM_LIB_NAME")
	protected String cust_lib_name = ""; //$NON-NLS-1$

	/**
	 * Flag to indicate if this is not an installed section, but one an author
	 * has created.
	 */
	protected boolean authorGeneratedSimulationSection = false;

	public boolean isAuthorGeneratedSimulationSection() {
		return authorGeneratedSimulationSection;
	}

	public void setAuthorGeneratedSimulationSection(
			boolean authorGeneratedSimulationSection) {
		this.authorGeneratedSimulationSection = authorGeneratedSimulationSection;
	}

	private boolean isPluginSection = false;

	public boolean isPluginSection() {
		return isPluginSection;
	}

	public void setPluginSection(boolean isPluginSection) {
		this.isPluginSection = isPluginSection;
	}

	private String pluginDirectory = "";

	public String getPluginDirectory() {
		return pluginDirectory;
	}

	public void setPluginDirectory(String pluginDirectory) {
		this.pluginDirectory = pluginDirectory;
	}

	private String pluginVersion = "";

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	/**
	 * Returns all control base sim sections.
	 * 
	 * @param schema
	 * @return
	 */
	public static List<BaseSimSection> getAllAuthorGenerated(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from BaseSimSection where authorGeneratedSimulationSection = '1' ").list(); //$NON-NLS-1$

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		Collections.sort(returnList);

		return returnList;
	}

	/**
	 * Indicates if having this section allows a player to read a document
	 * associated with this section.
	 */
	protected boolean confers_read_ability = false;

	/**
	 * Indicates if having this section allows a player to write to a document
	 * associated with this section.
	 */
	protected boolean confers_write_ability = false;

	/**
	 * A string indicating which fields should be sent as part of the URL to a
	 * remote web site section. Right now this is planned as a binary string.
	 * The string, for example, of '110' indicates: send the running sim id,
	 * send the actor id, don't send the user id.
	 */
	protected String sendString = ""; //$NON-NLS-1$

	/**
	 * Indicates if we should specifically send the Running Simulation Id to
	 * this external section.
	 */
	protected boolean sendRsId = false;

	/**
	 * Indicates if we should specifically send the Actor Id to this external
	 * section.
	 */
	protected boolean sendActorId = false;

	/**
	 * Indicates if we should specifically send the User Id to this external
	 * section.
	 */
	protected boolean sendUserId = false;

	public boolean isSendRsId() {
		return sendRsId;
	}

	public void setSendRsId(boolean sendRsId) {
		this.sendRsId = sendRsId;
	}

	public boolean isSendActorId() {
		return sendActorId;
	}

	public void setSendActorId(boolean sendActorId) {
		this.sendActorId = sendActorId;
	}

	public boolean isSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(boolean sendUserId) {
		this.sendUserId = sendUserId;
	}

	/**
	 * Zero argument constructor needed by Hibernate.
	 */
	public BaseSimSection() {
	}

	/**
	 * 
	 * @param url
	 * @param directory
	 * @param page_file_name
	 * @param rec_tab_heading
	 * @param description
	 */
	public BaseSimSection(String schema, String url, String directory,
			String page_file_name, String rec_tab_heading, String description) {

		this.url = url;
		this.directory = directory;
		this.page_file_name = page_file_name;
		this.rec_tab_heading = rec_tab_heading;
		this.description = description;

		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	/**
	 * Returns all base sim sections.
	 * 
	 * @param schema
	 * @return
	 */
	public static List<BaseSimSection> getAll(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery(
				"from BaseSimSection where DTYPE='BaseSimSection'").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		Collections.sort(returnList);

		return returnList;
	}

	/**
	 * Pulls a sim section out of the database schema by its id.
	 * 
	 * @param schema
	 * @param the_id
	 * @return
	 */
	public static BaseSimSection getById(String schema, String the_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		BaseSimSection bss = (BaseSimSection) MultiSchemaHibernateUtil
				.getSession(schema).get(BaseSimSection.class, new Long(the_id));
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return bss;

	}

	/**
	 * Returns a sorted list of all base and customized sections
	 * 
	 * @param schema
	 * @return
	 */
	public static List<BaseSimSection> getAllBaseAndCustomizable(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where " + "DTYPE='BaseSimSection' OR DTYPE='CustomizeableSection'"; //$NON-NLS-1$ //$NON-NLS-2$
		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery(queryString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		Collections.sort(returnList);

		return returnList;
	}

	public static List<BaseSimSection> getAllBase(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where " + "DTYPE='BaseSimSection'"; //$NON-NLS-1$ //$NON-NLS-2$
		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery(queryString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		Collections.sort(returnList);

		return returnList;
	}

	public static List<CustomizeableSection> getAllCustomizable(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where " + " DTYPE='CustomizeableSection'"; //$NON-NLS-1$ //$NON-NLS-2$

		List<CustomizeableSection> returnList = MultiSchemaHibernateUtil
				.getSession(schema).createQuery(queryString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<CustomizeableSection>();
		}

		Collections.sort(returnList);

		return returnList;
	}

	public static List<BaseSimSection> getAllAndChildren(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery("from BaseSimSection").list(); //$NON-NLS-1$

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		return returnList;
	}

	/**
	 * Returns all control base sim sections.
	 * 
	 * @param schema
	 * @return
	 */
	public static List<BaseSimSection> getAllControl(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from BaseSimSection where control_section = '1' order by BASE_SIMSEC_ID").list(); //$NON-NLS-1$

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		Collections.sort(returnList);

		return returnList;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDirectory() {
		return this.directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPage_file_name() {
		return this.page_file_name;
	}

	public void setPage_file_name(String page_file_name) {
		this.page_file_name = page_file_name;
	}

	public String getRec_tab_heading() {
		return this.rec_tab_heading;
	}

	public void setRec_tab_heading(String rec_tab_heading) {
		this.rec_tab_heading = rec_tab_heading;
	}

	public String getSample_image() {

		if ((this.sample_image != null) && (this.sample_image.length() > 0)) {
			return this.sample_image;
		} else {
			return NOSAMPLEIMAGE;
		}
	}

	public void setSample_image(String sample_image) {
		this.sample_image = sample_image;
	}

	public String getSendString() {
		return this.sendString;
	}

	public void setSendString(String sendString) {
		this.sendString = sendString;
	}

	public boolean isControl_section() {
		return this.control_section;
	}

	public void setControl_section(boolean control_section) {
		this.control_section = control_section;
	}

	public String getCust_lib_name() {
		return this.cust_lib_name;
	}

	public void setCust_lib_name(String cust_lib_name) {
		this.cust_lib_name = cust_lib_name;
	}

	public boolean isConfers_read_ability() {
		return this.confers_read_ability;
	}

	public void setConfers_read_ability(boolean confers_read_ability) {
		this.confers_read_ability = confers_read_ability;
	}

	public boolean isConfers_write_ability() {
		return this.confers_write_ability;
	}

	public void setConfers_write_ability(boolean confers_write_ability) {
		this.confers_write_ability = confers_write_ability;
	}

	@Override
	public int compareTo(Object bss_compared) {

		BaseSimSection bss = (BaseSimSection) bss_compared;

		return -(bss.getRec_tab_heading().compareTo(this.getRec_tab_heading()));

	}

	/**
	 * 
	 * @param schema
	 * @param rec_tab_name
	 * @return
	 */
	public static BaseSimSection getByRecommendedTagHeading(String schema,
			String rec_tab_name) {

		BaseSimSection bss = null;

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil
				.getSession(schema)
				.createQuery(
						"from BaseSimSection where BASE_TAB_HEADING = '" + rec_tab_name + "'").list(); //$NON-NLS-1$ //$NON-NLS-2$

		if (returnList != null) {
			bss = returnList.get(0);
			return bss;
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return bss;

	}

	/**
	 * 
	 * @param schema
	 * @param creatingOrganization
	 * @param uniqueName
	 * @param version
	 * @return
	 */
	public static BaseSimSection getByName(String schema,
			String creatingOrganization, String uniqueName, String version) {

		BaseSimSection bss = null;

		String queryString = "from BaseSimSection where creatingOrganization = :creatingOrganization "
				+ " AND uniqueName = :uniqueName AND version = :version "; //$NON-NLS-1$

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery(queryString).setString(
				"creatingOrganization", creatingOrganization).setString(
				"uniqueName", uniqueName).setString("version", version).list();

		if ((returnList != null) && (returnList.size() > 0)) {
			bss = returnList.get(0);
			return bss;
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return bss;
	}

	public String getVersionInformation() {
		return this.creatingOrganization + this.uniqueName
				+ " version: " + this.version; //$NON-NLS-1$
	}

	public void setSendFields(String send_rsid_info, String send_actor_info,
			String send_user_info) {

		if ((send_rsid_info != null)
				&& (send_rsid_info.equalsIgnoreCase("true"))) {
			this.sendRsId = true;
		} else {
			this.sendRsId = false;
		}

		if ((send_actor_info != null)
				&& (send_actor_info.equalsIgnoreCase("true"))) {
			this.sendActorId = true;
		} else {
			this.sendActorId = false;
		}

		if ((send_user_info != null)
				&& (send_user_info.equalsIgnoreCase("true"))) {
			this.sendUserId = true;
		} else {
			this.sendUserId = false;
		}

		setSendString();

	}

	public void setSendString() {
		String sendStringWork = "";

		if (this.sendRsId) {
			sendStringWork = "1";
		} else {
			sendStringWork = "0";
		}

		if (this.sendActorId) {
			sendStringWork += "1";
		} else {
			sendStringWork += "0";
		}

		if (this.sendUserId) {
			sendStringWork += "1";
		} else {
			sendStringWork += "0";
		}

		this.sendString = sendStringWork;
	}

	public String getFullPath() {
		String returnString = this.getUrl() + this.getDirectory()
				+ this.getPage_file_name();

		return returnString;
	}

	/**
	 * If this section requires its own set of tables to hold the data, they are
	 * found here in a comma separated list.
	 */
	private String databaseClassNames;

	public String getDatabaseClassNames() {
		return databaseClassNames;
	}

	public void setDatabaseClassNames(String databaseClassNames) {
		this.databaseClassNames = databaseClassNames;
	}

	/**
	 * Gets a list of classes that are 
	 * 
	 * @param schema
	 * @return
	 */
	public static List<String> getUniqSetOfDatabaseClassNames(String schema,
			boolean justPlugins) {

		ArrayList<String> returnList = new ArrayList<String>();

		Hashtable<String, String> uniqList = new Hashtable<String, String>();

		for (ListIterator<BaseSimSection> bi = BaseSimSection
				.getAllBaseAndCustomizable(schema).listIterator(); bi.hasNext();) {
			BaseSimSection bid = bi.next();

			if ((!(justPlugins)) || (bid.isPluginSection)) {
				if ((bid.getDatabaseClassNames() != null)
						&& (bid.getDatabaseClassNames().trim().length() > 0)) {

					StringTokenizer str = new StringTokenizer(bid
							.getDatabaseClassNames(), ",");

					while (str.hasMoreTokens()) {
						uniqList.put(str.nextToken().trim(), "set");
					}
				}
			}
		}

		// Take uniq list out of hashtable and put it in list.
		for (Enumeration e = uniqList.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			returnList.add(key);
		}

		return returnList;

	}

}
