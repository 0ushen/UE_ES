/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laurence
 */
public class ConnectDB{
    
    private static Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public ConnectDB() {
        try {
            if (conn == null || conn.isClosed()) {
                String sCon = "jdbc:postgresql://titus.isl.be:5432/ue";
                String sUser = "ue";
                String sPwd = "ue2018";
                Driver pilote = (Driver) Class.forName("org.postgresql.Driver").newInstance();

                conn = DriverManager.getConnection(sCon, sUser, sPwd);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void close() {
        try {
            conn.close();
            System.out.println("Closing DB connection");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
