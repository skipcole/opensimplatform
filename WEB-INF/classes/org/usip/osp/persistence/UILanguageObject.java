package org.usip.osp.persistence;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Proxy;

/**
 * This class represents a line of text presented to a player. It can be in any
 * language.
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
@Table(name = "UILANGUAGEOBJECT")
@Proxy(lazy = false)
public class UILanguageObject {

	public static final int ENGLISH_LANGUAGE_CODE = 1;
	public static final int SPANISH_LANGUAGE_CODE = 2;

	/** Database id of this line. */
	@Id
	@GeneratedValue
	@Column(name = "UILO_ID")
	private Long id;

	public UILanguageObject() {

	}

	/**
	 * 
	 * @param languageCode
	 * @param textKey
	 * @param localizedText
	 */
	public UILanguageObject(int languageCode, String textKey, String localizedText) {
		this.languageCode = languageCode;
		this.textKey = textKey;
		this.localizedText = localizedText;

		this.saveMe();

	}

	/** Will correspond to one of the predefined language codes. */
	private int languageCode;

	private String textKey;

	@Lob
	private String localizedText;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(int languageCode) {
		this.languageCode = languageCode;
	}

	public String getTextKey() {
		return textKey;
	}

	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}

	public String getLocalizedText() {
		return localizedText;
	}

	public void setLocalizedText(String localizedText) {
		this.localizedText = localizedText;
	}

	/** Saves a UILanguage object. */
	public void saveMe() {
		MultiSchemaHibernateUtil.beginTransaction(MultiSchemaHibernateUtil.principalschema, true);
		MultiSchemaHibernateUtil.getSession(MultiSchemaHibernateUtil.principalschema, true).saveOrUpdate(this);
		MultiSchemaHibernateUtil.commitAndCloseTransaction(MultiSchemaHibernateUtil.principalschema);
	}

	private static Hashtable engHash = new Hashtable();
	private static Hashtable spanHash = new Hashtable();

	/**
	 * This method loads the language property files and store them in the
	 * cache. Java properties files tend to have problems with languages that
	 * run left to right, so later we may go to storing and pulling them out of
	 * the database, but for now, pulling them out of properties files seems
	 * okay.
	 */
	public static void loadLanguages() {

		Enumeration e = englishLanguageResourceBundle.getKeys();

		while (e.hasMoreElements()) {

			String key = (String) e.nextElement();
			System.out.println(key + " = " + englishLanguageResourceBundle.getString(key));
			engHash.put(key, englishLanguageResourceBundle.getString(key));

		}

		Enumeration ss = spanishLanguageResourceBundle.getKeys();

		while (ss.hasMoreElements()) {
			String key = (String) ss.nextElement();
			System.out.println(key + " = " + spanishLanguageResourceBundle.getString(key));
			spanHash.put(key, spanishLanguageResourceBundle.getString(key));
		}

	}

	public static Hashtable getEngHash() {
		return engHash;
	}

	public static void setEngHash(Hashtable engHash) {
		UILanguageObject.engHash = engHash;
	}

	public static Hashtable getSpanHash() {
		return spanHash;
	}

	public static void setSpanHash(Hashtable spanHash) {
		UILanguageObject.spanHash = spanHash;
	}

	private static ResourceBundle englishLanguageResourceBundle;
	private static ResourceBundle spanishLanguageResourceBundle;

	static {

		try {
			englishLanguageResourceBundle = ResourceBundle.getBundle("UILanguageObject", new Locale("en", "US")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			spanishLanguageResourceBundle = ResourceBundle.getBundle("UILanguageObject", new Locale("es", "US")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		} catch (Exception e) {
			Logger.getRootLogger().warn(
					"Properties file UILanguageObject.properties not found. Need it. Its a Big Deal."); //$NON-NLS-1$
		}
	}

}
