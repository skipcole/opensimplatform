package org.usip.oscw.baseobjects;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Proxy;
import org.usip.oscw.networking.FileIO;
import org.usip.oscw.persistence.*;
import org.usip.oscw.sharing.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Entity
@Table(name = "BASE_SIM_SECTIONS")
@Proxy(lazy = false)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BaseSimSection {

	public static void main(String args[]) {

		// BaseSimSection.readBaseSimSectionsFromXMLFiles();

		// HibernateUtil.recreateDatabase();
		readCustomLibSimSectionsFromXMLFiles("usiposcw");
		// HibernateUtil.commitAndCloseTransaction();
		/*
		 * List<BaseSimSection> bList = BaseSimSection.getAllControl();
		 * 
		 * for (ListIterator la = bList.listIterator(); la.hasNext();) {
		 * BaseSimSection bss = (BaseSimSection) la.next();
		 * 
		 * System.out.println("--------------------");
		 * System.out.println(ObjectPackager.packageObject(bss));
		 * System.out.println("--------------------"); }
		 */
	}

	public static String readCustomLibSimSectionsFromXMLFiles(String schema) {

		String fileLocation = FileIO.getCustom_section_web_dir();

		File locDir = new File(fileLocation);

		File files[] = locDir.listFiles();

		if (files == null) {
			return ("Problem finding files at " + fileLocation);
		}

		for (int ii = 0; ii < files.length; ii++) {

			String customLibraryName = files[ii].getName();

			System.out.println(customLibraryName);

			if (files[ii].isDirectory()) {
				if (startsWithLetter(customLibraryName)) {
					// Step down into this directory and read its contents.
					String lowerLoc = fileLocation + customLibraryName
							+ File.separator;

					System.out.println("reading from: " + lowerLoc);

					File lowerLocDir = new File(lowerLoc);
					File lowerFiles[] = lowerLocDir.listFiles();

					if (lowerFiles != null) {

						for (int jj = 0; jj < lowerFiles.length; jj++) {
							String lowerFileName = lowerFiles[jj].getName();

							if (lowerFileName.endsWith(".xml")) {

								readInXMLFile(schema, lowerFiles[jj],
										customLibraryName);
							}
						}
					}

				} else {
					System.out.println("not valid dir");
				}
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

						readInXMLFile(schema, files[ii], null);
					}

				}
			}

			return "Read in Base Simulation Section Information.";
		}
	}

	public static void readInXMLFile(String schema, File thisFile,
			String customLibName) {
		try {
			FileReader fr = new FileReader(thisFile);
			BufferedReader br = new BufferedReader(fr);

			String fullBSS = "";

			String daLine = br.readLine();

			while (daLine != null) {
				fullBSS += daLine;

				daLine = br.readLine();
				System.out.println(daLine);
			}

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

					MultiSchemaHibernateUtil.beginTransaction(schema);
					MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(
							brc);
					MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

				} else {

					MultiSchemaHibernateUtil.beginTransaction(schema);
					MultiSchemaHibernateUtil.getSession(schema).saveOrUpdate(
							bRead);
					MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BaseSimSection unpackageXML(String xmlString) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("bss", BaseSimSection.class);

		return (BaseSimSection) xstream.fromXML(xmlString);
	}

	public static void writeBaseSimSectionToXMLFile(BaseSimSection bss) {

		System.out.println(ObjectPackager.packageObject(bss));
	}

	/** The primary key of this section. */
	@Id
	@GeneratedValue
	@Column(name = "BASE_SIMSEC_ID")
	protected Long id;

	/** Description of this standard section. */
	@Column(name = "BASE_SIMSEC_DESC")
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

	public static List<BaseSimSection> getAll(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery(
				"from BaseSimSection where DTYPE='BaseSimSection'").list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		return returnList;
	}

	public static List<BaseSimSection> getAllBaseAndCustomizable(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where "
				+ "DTYPE='BaseSimSection' OR DTYPE='CustomizeableSection'";
		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery(queryString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		return returnList;
	}

	public static List<BaseSimSection> getAllBase(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where "
				+ "DTYPE='BaseSimSection'";
		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery(queryString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		return returnList;
	}

	public static List<CustomizeableSection> getAllCustomizable(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		String queryString = "from BaseSimSection where "
				+ " DTYPE='CustomizeableSection'";

		List<CustomizeableSection> returnList = MultiSchemaHibernateUtil
				.getSession(schema).createQuery(queryString).list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<CustomizeableSection>();
		}

		return returnList;
	}

	public static List<BaseSimSection> getAllAndChildren(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery("from BaseSimSection").list();

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		return returnList;
	}

	public static List<BaseSimSection> getAllControl(String schema) {

		MultiSchemaHibernateUtil.beginTransaction(schema);

		List<BaseSimSection> returnList = MultiSchemaHibernateUtil.getSession(
				schema).createQuery(
				"from BaseSimSection where control_section = '1' order by BASE_SIMSEC_ID").list();

		if (returnList == null) {
			returnList = new ArrayList<BaseSimSection>();
		}

		MultiSchemaHibernateUtil.commitAndCloseTransaction(schema);

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
}
