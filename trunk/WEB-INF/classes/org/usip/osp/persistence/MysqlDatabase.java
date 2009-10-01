package org.usip.osp.persistence;

import java.security.MessageDigest;
import java.sql.*;
import org.apache.log4j.*;

import com.mysql.jdbc.SQLError;

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

        } catch (SQLException se) {
            System.out.println(se.toString());
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
