package org.usip.osp.persistence;

import java.security.MessageDigest;
import java.sql.*;
import org.usip.osp.baseobjects.USIP_OSP_Properties;
import org.apache.log4j.*;

/*
 * 
 *         This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
public class MysqlDatabase {

    private static String conn_string = ""; //$NON-NLS-1$

    static {
            conn_string = USIP_OSP_Properties.getValue("conn_string"); //$NON-NLS-1$
    }
    
    public static String makeConnString(String base_url, String user, String pass){
        
        String conn_string = base_url + "&user=" + user + "&password=" + pass; //$NON-NLS-1$ //$NON-NLS-2$
        
        System.out.print(conn_string);
        return conn_string;
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
     * Returns a database connection.
     * 
     * @return
     */
    public static Connection getConnection(String connString) {
        
        Logger.getRootLogger().debug(" "); //$NON-NLS-1$
        Logger.getRootLogger().debug("conn string is: "); //$NON-NLS-1$
        Logger.getRootLogger().debug(connString);
        Logger.getRootLogger().debug(" "); //$NON-NLS-1$
        
        Connection connection = null;

        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance(); //$NON-NLS-1$
            connection = DriverManager.getConnection(connString);

            return connection;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    

    public static byte[] getPHash(String password) {

        byte[] pbytes = password.getBytes();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1"); //$NON-NLS-1$

            md.update(pbytes);

            return md.digest();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
