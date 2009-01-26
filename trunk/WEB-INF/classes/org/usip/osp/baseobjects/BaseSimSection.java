package org.usip.osp.baseobjects;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.osp.networking.FileIO;
import org.usip.osp.networking.ObjectPackager;
import org.usip.osp.persistence.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This class represents sections that can be given to an actor at any given
 * phase of the game.
 * 
 * @author Ronald "Skip" Cole<br />
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
@Entity
@Table(name = "BASE_SIM_SECTIONS")
@Proxy(lazy = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BaseSimSection implements Comparable {

	/**
	 * Just used for occasional debugging.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		BaseSimSection.readNewBaseSimSectionsFromXMLFiles("test");
		// BaseSimSection.readBaseSimSectionsFromXMLFiles();

		/*
		 * List<BaseSimSection> bList = BaseSimSection.getAllControl();
		 * 
		 * for (ListIterator la = bList.listIterator(); la.hasNext();) {
		 * BaseSimSection bss = (BaseSimSection) la.next();
		 */
		// BaseSimSection bss = BaseSimSection.getMe("test", "16");
		// bss.setConfers_read_ability(true);
		// System.out.println("can read " + bss.isConfers_read_ability());
		// bss.saveMe("test");
		
		
		
		/*
		System.out.println("--------------------");
		// System.out.println(ObjectPackager.packageObject(bss));
		System.out.println("--------------------");
		StringBuffer sss = new StringBuffer("abcd");

		int aliquot = 2;
		int numPrinted = 0;
		if (sss.length() > aliquot) {
			while (numPrinted < sss.length()) {
				char[] c = new char[aliquot];
				int numToGet = aliquot;
				if ((sss.length() - numPrinted) < aliquot) {
					numToGet = sss.length() - numPrinted;
				}
				System.out.println(numPrinted + " " + numToGet);
				sss.getChars(numPrinted, numPrinted + numToGet, c, 0);
				System.out.println(c);
				try {
					sss.wait(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}

				numPrinted += aliquot;
			}
		}
		*/

	}

	/**
	 * 
	 * @param schema
	 * @param dirName
	 * @return
	 */
	public static String readASpecificCustomLibSection(String schema, String dirName) {

		String fileLocation = FileIO.getCustom_section_web_dir() + File.separator + dirName;

		return readCustomSectionsFromADir(schema, fileLocation);

	}

	/**
	 * 
	 * @param schema
	 * @return
	 */
	public static String readCustomLibSimSectionsFromXMLFiles(String schema) {

		String fileLocation = FileIO.getCustom_section_web_dir();

		return readCustomSectionsFromADir(schema, fileLocation);

	}

	/**
	 * 
	 * @param schema
	 * @param fileLocation
	 * @return
	 */
	public static String readCustomSectionsFromADir(String schema, String dirName) {

		System.out.println("readCustomSectionsFromADir");

		String fileLocation = FileIO.getCustom_section_web_dir() + File.separator + dirName;

		System.out.println("files located at " + fileLocation);

		File locDir = new File(fileLocation);

		File files[] = locDir.listFiles();

		if (files == null) {
			return ("Problem finding files at " + fileLocation);
		}

		for (int ii = 0; ii < files.length; ii++) {

			String customLibraryFileName = files[ii].getName();

			System.out.println(customLibraryFileName);

			if (customLibraryFileName.endsWith(".xml")) {

				readInXMLFile(schema, files[ii], customLibraryFileName);
			}
		}

		return "done";

	}

	public static boolean startsWithLetter(String s) {
		if ((s == null) || (s.equalsIgnoreCase(""))) {
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
	 * Reads the simulation sections from xml files.
	 * 
	 * @param schema
	 * @return Returns a string indicating success, or not.
	 * 
	 */
	public static String readBaseSimSectionsFromXMLFiles(String schema) {

		// The set of base simulation sections are read out of
		// XML files stored in the simulation_section_information directory.

		String fileLocation = FileIO.getBase_section_web_dir();

		System.out.println("Looking for files at: " + fileLocation);

		File locDir = new File(fileLocation);

		if (locDir == null) {
			return ("Problem finding files at " + fileLocation);
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				return ("Problem finding files at " + fileLocation);
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (fName.endsWith(".xml")) {

						try {
							readInXMLFile(schema, files[ii], null);
						} catch (Exception e) {
							System.out.println("problem reading in file " + fName);
							System.out.println(e.getMessage());
						}
					}

				}
			}

			return "Read in Base Simulation Section Information.";
		}
	}
	
	/**
	 * Reads the simulation sections from xml files.
	 * 
	 * @param schema
	 * @return Returns a string indicating success, or not.
	 * 
	 */
	public static String readNewBaseSimSectionsFromXMLFiles(String schema) {

		// The set of base simulation sections are read out of
		// XML files stored in the simulation_section_information directory.

		String fileLocation = FileIO.getBase_section_web_dir();

		System.out.println("Looking for files at: " + fileLocation);

		File locDir = new File(fileLocation);

		if (locDir == null) {
			return ("Problem finding files at " + fileLocation);
		} else {

			File files[] = locDir.listFiles();

			if (files == null) {
				return ("Problem finding files at " + fileLocation);
			} else {
				for (int ii = 0; ii < files.length; ii++) {

					String fName = files[ii].getName();

					if (fName.endsWith(".xml")) {

						try {
							readInNewXMLFile(schema, files[ii], null);
						} catch (Exception e) {
							System.out.println("problem reading in file " + fName);
							System.out.println(e.getMessage());
						}
					}

				}
			}

			return "Read in Base Simulation Section Information.";
		}
	}


	public void saveMe(String schema) {
		MultiSchemaHibernateUtil.beginTransaction(schema);
		MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
	}

	public static void readInXMLFile(String schema, File thisFile, String customLibName) {

		String fullBSS = FileIO.getFileContents(thisFile);

		// BaseSimSection bRead = unpackageXML(fullBSS);
		Object bRead = unpackageXML(fullBSS);

		if (bRead != null) {
			/*
			 * if (bRead.getClass().equals(BaseSimSection.class)){
			 * System.out.println("rec tab: " + bRead.getRec_tab_heading()); }
			 */

			if (bRead.getClass().equals(CustomLibrarySection.class)) {
				CustomLibrarySection brc = (CustomLibrarySection) bRead;
				if (customLibName != null) {
					brc.setCust_lib_name(customLibName);
				}

				brc.saveMe(schema);

			} else {

				MultiSchemaHibernateUtil.beginTransaction(schema);
				MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(bRead);
				MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
			}
		}
	}

	public static void readInNewXMLFile(String schema, File thisFile, String customLibName) {

		String fullBSS = FileIO.getFileContents(thisFile);

		// BaseSimSection bRead = unpackageXML(fullBSS);
		Object bRead = unpackageXML(fullBSS);

		if (bRead != null) {
			/*
			 * if (bRead.getClass().equals(BaseSimSection.class)){
			 * System.out.println("rec tab: " + bRead.getRec_tab_heading()); }
			 */

			if (bRead.getClass().equals(CustomLibrarySection.class)) {
				CustomLibrarySection brc = (CustomLibrarySection) bRead;
				if (customLibName != null) {
					brc.setCust_lib_name(customLibName);
				}

				if (!(tabHeadingExists(schema, brc.getRec_tab_heading()))) {
					brc.saveMe(schema);
				}
			} else {
				BaseSimSection bss = (BaseSimSection) bRead;
				if (!(tabHeadingExists(schema, bss.getRec_tab_heading()))) {
					MultiSchemaHibernateUtil.beginTransaction(schema);
					MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(bss);
					MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				}
			}
		}
	}

	public static boolean tabHeadingExists(String schema, String tabHeading) {

		List allBase = getAll(schema);

		for (ListIterator<BaseSimSection> li = allBase.listIterator(); li.hasNext();) {
			BaseSimSection this_base = (BaseSimSection) li.next();

			if (this_base.getRec_tab_heading().equalsIgnoreCase(tabHeading)) {
				return true;
			}
		}
		return false;

	}

	public static BaseSimSection unpackageXML(String xmlString) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("bss", BaseSimSection.class);

		return (BaseSimSection) xstream.fromXML(xmlString);
	}

	/** The primary key of this section. */
	@Id
	@GeneratedValue
	@Column(name = "BASE_SIMSEC_ID")
	protected Long id;
	
	/** Id used when objects are exported and imported moving across databases. */
	protected Long transit_id;

	public Long getTransit_id() {
		return transit_id;
	}

	public void setTransit_id(Long transit_id) {
		this.transit_id = transit_id;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	protected String creatingOrganization = "";
	protected String uniqueName = "";
	protected String version = "";
	
	public String getCreatingOrganization() {
		return creatingOrganization;
	}

	public void setCreatingOrganization(String creatingOrganization) {
		this.creatingOrganization = creatingOrganization;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	/////////////////////////////////////////////////////////////
	
	/** Description of this standard section. */
	@Column(name = "BASE_SIMSEC_DESC")
	@Lob
	protected String description = "";

	/** URL of this section */
	@Column(name = "BASE_SIMSEC_URL")
	protected String url = "";

	/** Directory of this section */
	@Column(name = "BASE_SIMSEC_DIR")
	protected String directory = "";

	/** Filename of this section */
	@Column(name = "BASE_SIMSEC_FILENAME")
	protected String page_file_name = "";

	/** Recommended (tab heading) of this section */
	@Column(name = "BASE_TAB_HEADING")
	protected String rec_tab_heading = "";

	private static final String NOSAMPLEIMAGE = "no_sample_image.png";

	/**
	 * Sample image to let the simulation creator see what this section looks
	 * like.
	 */
	@Column(name = "BASE_SAMPLE_IMAGE")
	protected String sample_image = "";

	/**
	 * Sets if this is a control section or not for this simulation - indicating
	 * that it will automatically be added to the control player for all phases.
	 */
	@Column(name = "SIMSEC_CONTROL")
	protected boolean control_section = false;

	@Column(name = "CUSTOM_LIB_NAME")
	protected String cust_lib_name = "";

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
	protected String sendString = "";

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
	public BaseSimSection(String schema, String url, String directory, String page_file_name, String rec_tab_heading,
			String description) {

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

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from BaseSimSection where DTYPE='BaseSimSection'").list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		Collections.sort(returnList);

		return returnList;
	}

	public static BaseSimSection getMe(String schema, String the_id) {

		MultiSchemaHibernateUtil.beginTransaction(schema);
		BaseSimSection bss = (BaseSimSection) MultiSchemaHibernateUtil.getSession(schema).get(BaseSimSection.class,
				new Long(the_id));

		System.out.println("this bss can read: " + bss.isConfers_read_ability());
		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		return bss;

	}

	public static List<BaseSimSection> getAllBaseAndCustomizable(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where " + "DTYPE='BaseSimSection' OR DTYPE='CustomizeableSection'";
		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(queryString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		Collections.sort(returnList);

		return returnList;
	}

	public static List<BaseSimSection> getAllBase(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where " + "DTYPE='BaseSimSection'";
		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(queryString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		Collections.sort(returnList);

		return returnList;
	}

	public static List<CustomizeableSection> getAllCustomizable(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where " + " DTYPE='CustomizeableSection'";

		List<CustomizeableSection> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(queryString)
				.list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<CustomizeableSection>();
		}

		Collections.sort(returnList);

		return returnList;
	}

	public static List<BaseSimSection> getAllAndChildren(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(schema)
				.createQuery("from BaseSimSection").list();

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

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from BaseSimSection where control_section = '1' order by BASE_SIMSEC_ID").list();

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
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPage_file_name() {
		return page_file_name;
	}

	public void setPage_file_name(String page_file_name) {
		this.page_file_name = page_file_name;
	}

	public String getRec_tab_heading() {
		return rec_tab_heading;
	}

	public void setRec_tab_heading(String rec_tab_heading) {
		this.rec_tab_heading = rec_tab_heading;
	}

	public String getSample_image() {

		if ((sample_image != null) && (sample_image.length() > 0)) {
			return sample_image;
		} else {
			return NOSAMPLEIMAGE;
		}
	}

	public void setSample_image(String sample_image) {
		this.sample_image = sample_image;
	}

	public String getSendString() {
		return sendString;
	}

	public void setSendString(String sendString) {
		this.sendString = sendString;
	}

	public boolean isControl_section() {
		return control_section;
	}

	public void setControl_section(boolean control_section) {
		this.control_section = control_section;
	}

	public String getCust_lib_name() {
		return cust_lib_name;
	}

	public void setCust_lib_name(String cust_lib_name) {
		this.cust_lib_name = cust_lib_name;
	}

	public boolean isConfers_read_ability() {
		return confers_read_ability;
	}

	public void setConfers_read_ability(boolean confers_read_ability) {
		this.confers_read_ability = confers_read_ability;
	}

	public boolean isConfers_write_ability() {
		return confers_write_ability;
	}

	public void setConfers_write_ability(boolean confers_write_ability) {
		this.confers_write_ability = confers_write_ability;
	}

	@Override
	public int compareTo(Object bss_compared) {

		BaseSimSection bss = (BaseSimSection) bss_compared;

		return -(bss.getRec_tab_heading().compareTo(this.getRec_tab_heading()));

	}

	public static BaseSimSection getByRecommendedTagHeading(String schema, String rec_tab_name) {

		BaseSimSection bss = null;

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(
				"from BaseSimSection where BASE_TAB_HEADING = '" + rec_tab_name + "'").list();

		if (returnList != null) {
			bss = (BaseSimSection) returnList.get(0);
			return bss;
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return bss;

	}

	public static BaseSimSection getByName(String schema, String creatingOrganization, String uniqueName, String version) {
		BaseSimSection bss = null;

		String queryString = 
			"from BaseSimSection where creatingOrganization = '" + creatingOrganization + "' " +
			"AND uniqueName = '" + uniqueName + "' AND version = '" + version + "'";
		System.out.println(queryString);
		
		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(schema).createQuery(queryString).list();

		if (returnList != null) {
			bss = (BaseSimSection) returnList.get(0);
			System.out.println("found " + bss.getVersionInformation());
			return bss;
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
		return bss;
	}
	
	public String getVersionInformation(){
		return creatingOrganization + uniqueName + " version: " + version;
	}
	
}
