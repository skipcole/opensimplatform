package org.usip.osp.persistence;

import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

import org.usip.osp.baseobjects.USIP_OSCW_Properties;

/**
 * @author Ronald "Skip" Cole
 *
 * This file is part of the USIP Online Simulation Platform.<br>
 * 
 * The USIP Online Simulation Platform is free software; you can redistribute it and/or
 * modify it under the terms of the new BSD Style license associated with this
 * distribution.<br>
 * 
 * The USIP Online Simulation Platform is distributed WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. <BR>
 * 
 */
public class MysqlDatabase {

    private static String conn_string = "";

    static {
            conn_string = USIP_OSCW_Properties.getValue("conn_string");
    }
    
    public static String makeConnString(String base_url, String user, String pass){
        
        String conn_string = base_url + "&user=" + user + "&password=" + pass;
        
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
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
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
        
        System.out.println(" ");
        System.out.println("conn string is: ");
        System.out.println(connString);
        System.out.println(" ");
        
        Connection connection = null;

        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
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
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            md.update(pbytes);

            return md.digest();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
