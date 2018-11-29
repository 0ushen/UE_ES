package dao;


import entity.Person;
import entity.Section;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.viewmodel.SectionVM;

/*
 * @author Samir Benlafya
 */

public class SectionDao extends DAO<Section> {
    
    private static final PersonDao pDao = new PersonDao();
    
    /* Load each section from the section table into an ArrayList, this entityList
     * is a protected variable in the parent class DAO. */
    @Override
    public ArrayList<Section> load() {
        
        try {
            // Execute an SQL query on the db and catch his result.
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT section_id, name, description, person_id FROM section;");
            
            /* Results coming raw from the database will be processed into one or
             * more Section entities which will be added into entityList. */
            buildSectionListFromDB(rs);
            
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(SectionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // entityList now contains all the data we asked for as Section objects.
        return entityList;
    }
    
    /* Load each section from the section table that match the info coming from 
     * the viewmodel.*/
    public ArrayList<Section> load(SectionVM vm) {
        
        try {
            /* Need to build the query first. Some fields may be empty and should
             * not be put into the query. */
            String query = "SELECT "
                    + "section_id, "
                    + "name, "
                    + "description, "
                    + "last_name, "
                    + "section.person_id AS person_id "
                    + "FROM section "
                    + "INNER JOIN person ON section.person_id = person.person_id "
                    + "WHERE ";
            
            if(!vm.getSectionName().equals(""))
                query += "name = ? AND ";
                
            if(!vm.getDescription().equals(""))
                query += "description = ? AND ";
                
            if(!vm.getTeacherLastName().equals(""))
                query += "last_name = ? AND ";
            
            // Make sure the query does not end with AND.
            query = query.substring(0, query.length() - 5) + ";";
            
            System.out.println("Query in load(E) : " + query);
            
            // Using the query to build a Prepared Statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            // An index is used so i can keep count on parameters.
            int i = 1;
            
            /* Each field is checked and if it's not empty, it's value is set as
               a parameter in the request. */
            if(!vm.getSectionName().equals("")){
                st.setString(i, vm.getSectionName());
                i++;
            }
            if(!vm.getDescription().equals("")){
                st.setString(i, vm.getDescription());
                i++;
            }
            if(!vm.getTeacherLastName().equals("")){
                st.setString(i, vm.getTeacherLastName());
                i++;
            }
            
            /* Execute the query and it's raw results are processed into one or
             * more Section entities. Eeach one of them is put into the 
             * entityList. */
            ResultSet rs = st.executeQuery();
            buildSectionListFromDB(rs);
            
            st.close();
              
        } catch (SQLException ex) {
            Logger.getLogger(SectionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // entityList now contains all the data we asked for as Section objects.
        return entityList;
    }
    
    // Save this Section in the database.
    @Override
    public void save(Section e) {
        
        try {
            // Building the query
            String query = "INSERT INTO section ("
                    + "name, "
                    + "description, "
                    + "person_id)"
                    + "VALUES (?, ?, ?);";
            
            // Query is used to initialise a prepared statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            //I use an index so i can add or delete a parameter more freely.
            int i = 1;
            
            /* Each input is checked so if it contains an empty string , 
             * it's value will be put as null in the db instead. */
            String sectionName = e.getSectionName().equals("") ? null : e.getSectionName();
            st.setString(i++, sectionName);
            String description = e.getDescription().equals("") ? null : e.getDescription();
            st.setString(i++, description);
            Integer teacherId = e.getTeacher().getId();
            st.setInt(i++, teacherId);
            
            // Prepared statement is executed.
            st.executeUpdate();
            
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(SectionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Update a specific section using its id. 
    @Override
    public void update(Section e) {
        
        try {
            // Building the query.
            String query =""
                    + "UPDATE section "
                    + "SET "
                    + "name = ?, "
                    + "description = ?, "
                    + "person_id = ? "
                    + "WHERE section_id = ?";
            
            // Query is used to initialise a prepared statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            //I use an index so i can add or delete a parameter more freely.
            int i = 1;
            
            /* Each input is checked so if it contains an empty string , 
             * it's value will be put as null in the db instead. */
            String sectionName = e.getSectionName().equals("") ? null : e.getSectionName();
            st.setString(i++, sectionName);
            String description = e.getDescription().equals("") ? null : e.getDescription();
            st.setString(i++, description);
            Integer teacherId = e.getTeacher().getId();
            st.setInt(i++, teacherId);
            
            st.executeUpdate();
            
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(SectionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Delete a a specific section from entityList and the db.
    @Override
    public void delete(Section e) {
        setDeleteCommand("DELETE FROM section WHERE section_id = ?;");
        super.delete(e);
    }
    
    // Delete a specific section from entityList and the db using an id directly.
    @Override
    public void delete(Integer id) {
        setDeleteCommand("DELETE FROM section WHERE section_id = ?;");
        super.delete(id);
    }
    
    /* This function will build a list of Sections 
     * (protected variable from the parent class entityList) using the raw data 
     * coming from the db. */   
    private void buildSectionListFromDB(ResultSet rs) {
        
        // Reset the list.
        entityList.clear();
        
        try {
            // Each row is processed into a Section entity
            while (rs.next()){
                // Create a Person entity with this teacher id.
                Person teacher = pDao.load(rs.getInt("person_id"));
                
                // Create a Section using the row info.
                Section section = new Section(
                        rs.getInt("section_id"),
                        rs.getString("name"), 
                        rs.getString("description"),
                        teacher);
                
                // Adding that Section into the entityList.
                entityList.add(section);
            }
            
            System.out.println("****SECTIONS IN ENTITYLIST****\n");
            entityList.forEach((section) -> {
                System.out.println("section name : " + section.getSectionName() +
                        " | description : " + section.getDescription() + 
                        " | teacher : " + section.getTeacher().getLastName());
            });
        } catch (SQLException ex) {
            Logger.getLogger(SectionDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
