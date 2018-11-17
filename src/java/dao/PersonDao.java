/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Person;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.viewmodel.ViewModel;

/**
 *
 * @author Samir
 */
public class PersonDao extends DAO<Person> {
    
    @Override
    public ArrayList<Person> load() {
        
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT person_id, first_name, last_name, country, city, postal_code,"
                  + " address, date_of_birth, email, is_teacher FROM person");
            
            while (rs.next()){
                
                LocalDate date;
                try {
                    date = rs.getDate("date_of_birth").toLocalDate();
                } catch (NullPointerException e) {
                    date = LocalDate.of(1, 1, 1);
                }
                
                
                Person person = new Person(rs.getInt("person_id"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("country"), rs.getString("city"),
                        rs.getString("postal_code"), rs.getString("address"),
                        date, rs.getString("email"),
                        rs.getBoolean("is_teacher"));
                
                entityList.add(person);
            };
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return entityList;
    }

    @Override
    public ArrayList<Person> load(ViewModel vm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(Person e) {
        try {
            PreparedStatement st;

            if(e.getId() == null) {
                st = conn.prepareStatement(""
                        + "INSERT INTO person "
                        + "(first_name, last_name, country, city, postal_code,"
                        + " address, date_of_birth, email, is_teacher) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }
            else{
                st = conn.prepareStatement(""
                        + "UPDATE person "
                        + "SET first_name = ?, last_name = ?, country = ?, city = ?, "
                        + "postal_code = ?, address = ?, date_of_birth = ?, email = ?, "
                        + "is_teacher = ? "
                        + "WHERE person_id = ?");
                st.setInt(10, e.getId());
            }
            st.setString(1, e.getFirstName());
            st.setString(2, e.getLastName());
            st.setString(3, e.getCountry());
            st.setString(4, e.getCity());
            st.setString(5, e.getPostalCode());
            st.setString(6, e.getAddress());
            st.setDate(7, e.getDateOfBirthSQL());
            st.setString(8, e.getEmail());
            st.setBoolean(9, e.isTeacher());
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Person e) {
        setDeleteCommand("DELETE FROM person WHERE person_id = ");
        super.delete(e);
    }
    
    @Override
    public void delete(Integer id) {
        setDeleteCommand("DELETE FROM person WHERE person_id = ");
        super.delete(id);
    }
    
    
}
