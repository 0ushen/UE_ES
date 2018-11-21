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
                    "SELECT person_id, first_name, last_name, country, city, "
                  + "postal_code, address, date_of_birth, email, is_teacher "
                  + "FROM person");
            
            buildPersonListFromDB(rs);
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return entityList;
    }

    @Override
    public ArrayList<Person> load(Person e) {
        
        try {
            String query = "SELECT person_id, first_name, last_name, country, city, postal_code,"
                  + " address, date_of_birth, email, is_teacher FROM person WHERE ";
            int i = 0;
            if(e.getId() != null){
                query += "person_id = ? AND ";
                i++;
            }
            if(!e.getFirstName().equals("")){
                query += "first_name = ? AND ";
                i++;
            }
            if(!e.getLastName().equals("")){
                query += "last_name = ? AND ";
                i++;
            }
            if(!e.getCountry().equals("")){
                query += "country = ? AND ";
                i++;
            }
            if(!e.getCity().equals("")){
                query += "city = ? AND ";
                i++;
            }
            if(!e.getPostalCode().equals("")){
                query += "postal_code = ? AND ";
                i++;
            }
            if(!e.getAddress().equals("")){
                query += "address = ? AND ";
                i++;
            }      
            if(!e.getDateOfBirth().equals("")){
                query += "date_of_birth = ? AND ";
                i++;
            }
            if(!e.getEmail().equals("")){
                query += "email = ? AND ";
                i++;
            }
            query += "is_teacher = ?;";
            
            System.out.println("Query in load(E) : " + query);
            
            PreparedStatement st = conn.prepareStatement(query);
            i = 1;
            if(e.getId() != null){
                st.setInt(i, e.getId());
                i++;
            }
            if(!e.getFirstName().equals("")){
                st.setString(i, e.getFirstName());
                i++;
            }
            if(!e.getLastName().equals("")){
                st.setString(i, e.getLastName());
                i++;
            }
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
            ResultSet rs = st.executeQuery();
            buildPersonListFromDB(rs);
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return entityList;
    }

    @Override
    public void save(Person e) {
        
        try {
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
            
            System.out.println("Query in save(e) : " + query);
            
            PreparedStatement st = conn.prepareStatement(query);
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
            PreparedStatement st = conn.prepareStatement(""
                    + "UPDATE person "
                    + "SET first_name = ?, last_name = ?, country = ?, "
                    + "city = ?, postal_code = ?, address = ?, "
                    + "date_of_birth = ?, email = ?, is_teacher = ? "
                    + "WHERE person_id = ?");
            st.setString(1, e.getFirstName());
            st.setString(2, e.getLastName());
            st.setString(3, e.getCountry());
            st.setString(4, e.getCity());
            st.setString(5, e.getPostalCode());
            st.setString(6, e.getAddress());
            
            if(e.getDateOfBirth().equals(""))
                st.setDate(7, null);
            else
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
    
    private void buildPersonListFromDB(ResultSet rs) {
        
        entityList.clear();
        try {
            while (rs.next()){
                String date;
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    date = df.format(rs.getDate("date_of_birth"));
                } catch (NullPointerException ex) {
                    System.out.println("Date is undefined in database and will "
                            + "be set to an empty string in the person Entity");
                    date = "";
                }
                
                Person person = new Person(rs.getInt("person_id"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("country"), rs.getString("city"),
                        rs.getString("postal_code"), rs.getString("address"),
                        date, rs.getString("email"), rs.getBoolean("is_teacher"));
                
                entityList.add(person);
            }
            
            System.out.println("****PERSONS IN ENTITYLIST****\n");
            entityList.forEach((person) -> {
                System.out.println("first name : " + person.getFirstName() +
                        " | last name : " + person.getLastName() + 
                        " | email : " + person.getEmail() + 
                        " | date of birth : " + person.getDateOfBirth() + 
                        " | teacher : " + person.isTeacher());
            });
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
