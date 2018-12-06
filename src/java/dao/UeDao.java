/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Section;
import entity.Ue;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.viewmodel.UeVM;

/**
 *
 * @author Samir
 */
public class UeDao extends DAO<Ue> {

    private static final SectionDao SDAO = new SectionDao();
    
    /* Load each ue from the ue table into an ArrayList, this entityList
     * is a protected variable in the parent class DAO. */
    @Override
    public ArrayList<Ue> load() {
        
        try {
            // Execute an SQL query on the db and catch his result.
            ResultSet rs = conn.createStatement().executeQuery("SELECT "
                    + "ue_id, "
                    + "name, "
                    + "section_id, "
                    + "code, "
                    + "num_of_periods, "
                    + "description, "
                    + "is_decisive "
                    + "FROM ue;");
            
            /* Results coming raw from the database will be processed into one or
             * more Ue entities which will be added into entityList. */
            buildUeListFromDB(rs);
            
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(UeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // entityList now contains all the data we asked for as Ue objects.
        return entityList;
    }
    
    /* Load each ue from the ue table that match the info coming from 
     * the viewmodel.*/
    public ArrayList<Ue> load(UeVM vm) {
        
        try {
            /* Need to build the query first. Some fields may be empty and should
             * not be put into the query. */
            String query = "SELECT "
                    + "ue.ue_id, "
                    + "ue.name, "
                    + "ue.section_id, "
                    + "section.name, "
                    + "ue.code, "
                    + "ue.num_of_periods, "
                    + "ue.description, "
                    + "ue.is_decisive "
                    + "FROM ue "
                    + "LEFT JOIN section ON ue.section_id = section.section_id "
                    + "WHERE ";
            
            if(!vm.getUeName().equals(""))
                query += "LOWER(ue.name) LIKE LOWER(?) AND ";
            
            if(!vm.getSectionName().equals(""))
                query += "LOWER(section.name) LIKE LOWER(?) AND ";
            
            if(!vm.getCode().equals(""))
                query += "ue.code = ? AND ";
                
            if(vm.getNbrOfPeriods() != null)
                query += "ue.num_of_periods = ? AND ";
                
            if(!vm.getDescription().equals(""))
                query += "LOWER(ue.description) LIKE LOWER(?) AND ";
            
            query += "ue.is_decisive = ?;";
            
            /*// Make sure the query does not end with AND.
            query = query.substring(0, query.length() - 5) + ";";*/
            
            System.out.println("Query in Ue load(vm) : " + query);
            
            // Using the query to build a Prepared Statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            // An index is used so i can keep count on parameters.
            int i = 1;
            
            /* Each field is checked and if it's not empty, it's value is set as
               a parameter in the request. */
            if(!vm.getUeName().equals(""))
                st.setString(i++, '%' + vm.getUeName() + '%');

            if(!vm.getSectionName().equals(""))
                st.setString(i++, '%' + vm.getSectionName() + '%');

            if(!vm.getCode().equals(""))
                st.setString(i++, vm.getCode());
;
            if(vm.getNbrOfPeriods() != null)
                st.setInt(i++, vm.getNbrOfPeriods());

            if(!vm.getDescription().equals(""))
                st.setString(i++, '%' + vm.getDescription() + '%');

            st.setBoolean(i++, vm.isDecisive());
            
            System.out.println("Prepared Statement in Ue load(vm) : " + st);
            
            /* Execute the query and it's raw results are processed into one or
             * more Ue entities. Eeach one of them is put into the 
             * entityList. */
            ResultSet rs = st.executeQuery();
            buildUeListFromDB(rs);
            
            st.close();
              
        } catch (SQLException ex) {
            Logger.getLogger(UeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // entityList now contains all the data we asked for as Ue objects.
        return entityList;
    }
    
    // Load a single ue via its id.
    public Ue load(Integer id) {
        
        try {
            // Execute an SQL query on the db and catch his result.
            String query = "SELECT "
                    + "ue_id, "
                    + "name, "
                    + "section_id, "
                    + "code, "
                    + "num_of_periods, "
                    + "description, "
                    + "is_decisive "
                    + "FROM ue "
                    + "WHERE ue_id = ?;";
            
            System.out.println("Query in Ue load(id) : " + query);
            
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id);
            
            System.out.println("Prepared statement in Ue load(id) : " + st);
            
            ResultSet rs = st.executeQuery();
            
            /* Execute the query and it's raw results are processed into one
             * Ue entity. That Ue is put into the entityList. */
            buildUeListFromDB(rs);
            
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(UeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* entityList now contains the ue we searched for, so we return 
         * the first item in the list. */
        return entityList.get(0);
    }
    
    // Save this Ue in the database.
    @Override
    public void save(Ue e) {
        
        try {
            // Building the query
            String query = "INSERT INTO ue ("
                    + "name, "
                    + "section_id, "
                    + "code, "
                    + "num_of_periods, "
                    + "description, "
                    + "is_decisive) "
                    + "VALUES (?, ?, ?, ?, ?, ?);";
            
            System.out.println("Query in Ue save(e) : " + query);
            
            // Query is used to initialise a prepared statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            //I use an index so i can add or delete a parameter more freely.
            int i = 1;
            
            /* Each input is checked so if it contains an empty string , 
             * it's value will be put as null in the db instead. */
            String ueName = e.getUeName().equals("") ? null : e.getUeName();
            st.setString(i++, ueName);
            st.setInt(i++, e.getSection().getId());
            String code =  e.getCode().equals("") ? null : e.getCode();
            st.setString(i++, code);
            st.setInt(i++, e.getNbrOfPeriods());
            String description = e.getDescription().equals("") ? null : e.getDescription();
            st.setString(i++, description);
            st.setBoolean(i++, e.isDecisive());
            
            System.out.println("Prepared Statement in Ue save(e) : " + st);
            
            st.executeUpdate();
            
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(UeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Update a specific ue using its id. 
    @Override
    public void update(Ue e) {
        
        try {
            // Building the query.
            String query = "UPDATE ue "
                    + "SET "
                    + "name = ?, "
                    + "section_id = ?, "
                    + "code = ?, "
                    + "num_of_periods = ?, "
                    + "description = ?, "
                    + "is_decisive = ? "
                    + "WHERE ue_id = ?;";
            
            System.out.println("Query in Ue update(e) : " + query);
            
            // Query is used to initialise a prepared statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            //I use an index so i can add or delete a parameter more freely.
            int i = 1;
            
            /* Each input is checked so if it contains an empty string , 
             * it's value will be put as null in the db instead. */
            String ueName = e.getUeName().equals("") ? null : e.getUeName();
            st.setString(i++, ueName);
            st.setInt(i++, e.getSection().getId());
            String code =  e.getCode().equals("") ? null : e.getCode();
            st.setString(i++, code);
            st.setInt(i++, e.getNbrOfPeriods());
            String description = e.getDescription().equals("") ? null : e.getDescription();
            st.setString(i++, description);
            st.setBoolean(i++, e.isDecisive());
            System.out.println(e.getId());
            st.setInt(i++, e.getId());
            
            System.out.println("Prepared Statement in Ue update(e) : " + st);
            
            st.executeUpdate();
            
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(UeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Delete a a specific ue from entityList and the db.
    @Override
    public void delete(Ue e) {
        setDeleteCommand("DELETE FROM ue WHERE ue_id = ?;");
        super.delete(e);
    }
    
    // Delete a specific ue from entityList and the db using an id directly.
    @Override
    public void delete(Integer id) {
        setDeleteCommand("DELETE FROM ue WHERE ue_id = ?;");
        super.delete(id);
    }
    
    /* This function will build a list of Ue 
     * (protected variable entityList from the parent class DAO) using the raw 
     * data coming from the db. */   
    private void buildUeListFromDB(ResultSet rs) {
        
        // Reset the list.
        entityList.clear();
        
        try {
            // Each row is processed into a Ue entity
            while (rs.next()){
                // Create a Section entity with this section id.
                Section section = SDAO.load(rs.getInt("section_id"));
                
                // Create a Ue using the row info.
                Ue ue = new Ue(
                        rs.getInt("ue_id"),
                        rs.getString("name"),
                        section,
                        rs.getString("code"),
                        rs.getInt("num_of_periods"),
                        rs.getString("description"),
                        rs.getBoolean("is_decisive"));
                
                // Adding that Ue into the entityList.
                entityList.add(ue);
            }
            
            System.out.println("****UE IN ENTITYLIST****\n");
            entityList.forEach((ue) -> {
                System.out.println("ue name : " + ue.getUeName() +
                        " | description : " + ue.getDescription() + 
                        " | section : " + ue.getSection().getSectionName());
            });
        } catch (SQLException ex) {
            Logger.getLogger(UeDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
