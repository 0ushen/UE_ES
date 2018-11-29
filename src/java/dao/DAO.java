package dao;

import entity.Entity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samir
 * @param <T>
 */

public abstract class DAO<T extends Entity> {
    
    /* ArrayList which will be used to store the database info in the form of
     * Person entities. */
    protected ArrayList<T> entityList = new ArrayList();
    
    /* This variable will contain the connection to the database and will be
     * accessible by all child classes.*/
    protected Connection conn;
    
    // 
    private String deleteCommand;
    
    // Only constructor
    public DAO() {
        // Create a connection with the database and store it into a variable.
        conn = new ConnectDB().getConn();
    }
    
    public ArrayList<T> getList() {
        return entityList;
    }
    
    // Return the entity matching the id passed in parameter.
    public T getById(Integer id) {
        for (T entity : entityList) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }
    
    // Set the specifics for a child class delete SQL query.
    public void setDeleteCommand(String deleteCommand) {
        this.deleteCommand = deleteCommand;
    }
    
    /* Delete an entity by getting its id and deleting the row matching it 
     * in the database.
     * This entity will be erased from the entityList aswell. */
    public void delete(T entity) {
        if (entity != null && entity.getId() != null) {
            try {
                PreparedStatement st = conn.prepareStatement(deleteCommand);
                st.setInt(1, entity.getId());
                st.executeUpdate();
                entityList.remove(entity);
            } catch (SQLException ex) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /* Delete the row in the database where the id match this function 
     * parameter. 
     * It will also search entityList for an entity matching this id, and if it
     * finds, one delete it. */
    public void delete(Integer id) {
        if (id != null) {
            try {
                PreparedStatement st = conn.prepareStatement(deleteCommand);
                st.setInt(1, id);
                st.executeUpdate();
                for (T entity : entityList) {
                    if (Objects.equals(entity.getId(), id)) {
                        entityList.remove(entity);
                        break;
                    }
                }
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // Will be used to load all the database info into the entitylist.
    public abstract ArrayList<T> load();
    
    // Will be used to save an entity into the database.
    public abstract void save(T e);
    
    // Will be used to update the entity passed in parameter in the database.
    public abstract void update(T e);
    
}
