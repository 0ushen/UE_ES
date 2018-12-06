/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Capacity;
import entity.Ue;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samir
 */
public class CapacityDao extends DAO<Capacity> {
    
    private static final UeDao UEDAO = new UeDao();
    
    @Override
    public ArrayList<Capacity> load() {
        try {
            // Execute an SQL query on the db and catch his result.
            ResultSet rs = conn.createStatement().executeQuery(
                      "SELECT "
                    + "capacity_id, "
                    + "name, "
                    + "description, "
                    + "ue_id "
                    + "FROM capacity;");
                    
            
            /* Results coming raw from the database will be processed into one or
             * more Section entities which will be added into entityList. */
            buildCapacityListFromDB(rs);
            
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(SectionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // entityList now contains all the data we asked for as Section objects.
        return entityList;
    }
    
    public ArrayList<Capacity> load(Integer ueId) {
        
        try {
            // Execute an SQL query on the db and catch his result.
            String query = "SELECT "
                    + "capacity_id, "
                    + "name, "
                    + "description, "
                    + "ue_id "
                    + "FROM capacity "
                    + "WHERE ue_id = ?;";
            
            System.out.println("Query in Capacity load(ueId) : " + query);
            
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, ueId);
            
            System.out.println("Prepared statement in Capacity load(ueId) : " + st);
            
            ResultSet rs = st.executeQuery();
            
            /* Execute the query and it's raw results are processed into one
             *  or more Capacity entity. Those capacities are put into the 
             * entityList. */
            buildCapacityListFromDB(rs);
            
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(Capacity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* entityList now contains the capacities we searched for, so we return 
         * the list. */
        return entityList;
    }
    
    @Override
    public void save(Capacity e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Capacity e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /* This function will build a list of Capacities 
     * (protected variable entityList from the parent class DAO) using the raw 
     * data coming from the db. */   
    private void buildCapacityListFromDB(ResultSet rs) {
        
        // Reset the list.
        entityList.clear();
        
        try {
            // Each row is processed into a Capacity entity
            while (rs.next()){
                // Create a Ue entity with this ue id.
                Ue ue;
               if(rs.getInt("ue_id") == 0)
                   ue = null;
               else
                   ue = UEDAO.load(rs.getInt("ue_id"));
                
                
                // Create a capacity using the row info.
                Capacity capacity = new Capacity(
                        rs.getInt("capacity_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        ue);
                
                // Adding that capacity into the entityList.
                entityList.add(capacity);
            }
            
            System.out.println("****Capacities IN ENTITYLIST****\n");
            entityList.forEach((capacity) -> {
                System.out.println("capacity name : " + capacity.getCapacityName() +
                        " | description : " + capacity.getDescription());
            });
        } catch (SQLException ex) {
            Logger.getLogger(CapacityDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
