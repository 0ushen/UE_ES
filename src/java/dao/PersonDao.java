/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Person;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        entityList.clear();
        
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT person_id, first_name, last_name, country, city, postal_code,"
                  + " address, date_of_birth, email, is_teacher FROM person");
            
            while (rs.next()){
                //System.out.println("Starting " + rs.getString("first_name") + " " + rs.getString("last_name"));
                String date;
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
                    date = df.format(rs.getDate("date_of_birth")); 
                } catch (NullPointerException e) {
                    System.out.println("Error when getting date from db");
                    date = "";
                }
                
                Person person = new Person(rs.getInt("person_id"),
                    rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("country"), rs.getString("city"),
                    rs.getString("postal_code"), rs.getString("address"),
                    date, rs.getString("email"),
                    rs.getBoolean("is_teacher"));
                
                entityList.add(person);
            };
            
        } catch (SQLException | NullPointerException ex) {
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
               
            String query = "INSERT INTO person (first_name, last_name, ";
            int i = 0;
            if(!e.getCountry().equals("")){
                query += "country, ";
                i++;
            }
            if(!e.getCity().equals("")){
                query += "city, ";
                i++;
            }
            if(!e.getPostalCode().equals("")){
                query += "postal_code, ";
                i++;
            }
            if(!e.getAddress().equals("")){
                query += "address, ";
                i++;
            }      
            if(!e.getDateOfBirth().equals("")){
                query += "date_of_birth, ";
                i++;
            }
            if(!e.getEmail().equals("")){
                query += "email, ";
                i++;
            }
            query += "is_teacher) VALUES (?, ?, ";
            for(int j = 0 ; j <= i ; j++){
                if(j == i)
                    query += "?)";
                else
                    query += "?, ";
            }
            
            System.out.println(query);
            st = conn.prepareStatement(query);
            st.setString(1, e.getFirstName());
            st.setString(2, e.getLastName());
            i = 3;
            if(!e.getCountry().equals("")){
                st.setString(i, e.getCountry());
                i++;
            }
            if(!e.getCity().equals("")){
                st.setString(i, e.getCity());
                i++;
            }
            if(!e.getPostalCode().equals("")){
                st.setString(i, e.getPostalCode());
                i++;
            }
            if(!e.getAddress().equals("")){
                st.setString(i, e.getAddress());
                i++;
            }      
            if(!e.getDateOfBirth().equals("")){
                st.setDate(i, e.getDateOfBirthSQL());
                i++;
            }
            if(!e.getEmail().equals("")){
                st.setString(i, e.getEmail());
                i++;
            }
            st.setBoolean(i, e.isTeacher());
            st.executeUpdate();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void update(Person e) {
        try {
            PreparedStatement st;
            
            st = conn.prepareStatement(""
                    + "UPDATE person "
                    + "SET first_name = ?, last_name = ?, country = ?, city = ?, "
                    + "postal_code = ?, address = ?, date_of_birth = ?, email = ?, "
                    + "is_teacher = ? "
                    + "WHERE person_id = ?");
            st.setString(1, e.getFirstName());
            st.setString(2, e.getLastName());
            st.setString(3, e.getCountry());
            st.setString(4, e.getCity());
            st.setString(5, e.getPostalCode());
            st.setString(6, e.getAddress());
            st.setDate(7, e.getDateOfBirthSQL());
            st.setString(8, e.getEmail());
            st.setBoolean(9, e.isTeacher());
            st.setInt(10, e.getId());
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
