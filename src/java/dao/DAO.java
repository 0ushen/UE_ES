/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Entity;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.viewmodel.ViewModel;

/**
 *
 * @author Samir
 */
public abstract class DAO<T extends Entity> {
    
    protected ArrayList<T> entityList = new ArrayList();
    protected Connection conn;
    private String deleteCommand;

    public DAO() {
        conn = new ConnectDB().getConn();
    }

    public ArrayList<T> getList() {
        return entityList;
    }

    public T getById(Integer id) {
        for (T entity : entityList) {
            if (Objects.equals(entity.getId(), id)) {
                return entity;
            }
        }
        return null;
    }
    
    public void setDeleteCommand(String deleteCommand) {
        this.deleteCommand = deleteCommand;
    }

    public void delete(T entity) {
        if (entity != null && entity.getId() != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(deleteCommand + entity.getId());
                entityList.remove(entity);
            } catch (SQLException ex) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void delete(Integer id) {
        if (id != null) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(deleteCommand + id);
                for (T entity : entityList) {
                    if (Objects.equals(entity.getId(), id)) {
                        entityList.remove(entity);
                        break;
                    }
                }
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public abstract ArrayList<T> load();
    
    public abstract ArrayList<T> load(ViewModel vm);

    public abstract void save(T e);
    
    public abstract void update(T e);
    
}
