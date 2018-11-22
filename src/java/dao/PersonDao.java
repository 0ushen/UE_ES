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
        PreparedStatement st;
        
        try {
            String query = "SELECT "
                    + "person_id, "
                    + "first_name, "
                    + "last_name, "
                    + "country, city, "
                    + "postal_code, "
                    + "address, "
                    + "date_of_birth, "
                    + "email, "
                    + "is_teacher "
                    + "FROM person "
                    + "WHERE ";
            
            if(e.getId() != null)
                query += "person_id = ? AND ";
                
            if(!e.getFirstName().equals(""))
                query += "first_name = ? AND ";
                
            if(!e.getLastName().equals(""))
                query += "last_name = ? AND ";
                
            if(!e.getCountry().equals(""))
                query += "country = ? AND ";
                
            if(!e.getCity().equals(""))
                query += "city = ? AND ";
                
            if(!e.getPostalCode().equals(""))
                query += "postal_code = ? AND ";
                
            if(!e.getAddress().equals(""))
                query += "address = ? AND ";
                  
            if(!e.getDateOfBirth().equals(""))
                query += "date_of_birth = ? AND ";
                
            if(!e.getEmail().equals(""))
                query += "email = ? AND ";
                
            query += "is_teacher = ?;";
            
            System.out.println("Query in load(E) : " + query);
            
            st = conn.prepareStatement(query);
            
            int i = 1;
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
            String query = "INSERT INTO person ("
                    + "first_name, "
                    + "last_name, "
                    + "country, "
                    + "city, "
                    + "postal_code, "
                    + "address, "
                    + "date_of_birth, "
                    + "email, "
                    + "is_teacher)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            
            PreparedStatement st = conn.prepareStatement(query);
            
            int i = 1;
            String firstName = e.getFirstName().equals("") ? null : e.getFirstName();
            st.setString(i++, firstName);
            String lastName = e.getLastName().equals("") ? null : e.getLastName();
            st.setString(i++, lastName);
            String country = e.getCountry().equals("") ? null : e.getCountry();
            st.setString(i++, country);
            String city = e.getCity().equals("") ? null : e.getCity();
            st.setString(i++, city);
            String postalCode = e.getPostalCode().equals("") ? null : e.getPostalCode();
            st.setString(i++, postalCode);
            String address = e.getAddress().equals("") ? null : e.getAddress();
            st.setString(i++, address);
            Date dateOfBirthSQL = e.getDateOfBirth().equals("") ? null : e.getDateOfBirthSQL();
            st.setDate(i++, dateOfBirthSQL);
            String email = e.getEmail().equals("") ? null : e.getEmail();
            st.setString(i++, email);
            st.setBoolean(i++, e.isTeacher());
            
            st.executeUpdate();
            
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void update(Person e) {
        
        try {
            String query =""
                    + "UPDATE person "
                    + "SET "
                    + "first_name = ?, "
                    + "last_name = ?, "
                    + "country = ?, "
                    + "city = ?, "
                    + "postal_code = ?, "
                    + "address = ?, "
                    + "date_of_birth = ?, "
                    + "email = ?, "
                    + "is_teacher = ? "
                    + "WHERE person_id = ?";
            
            PreparedStatement st = conn.prepareStatement(query);
            
            int i = 1;
            String firstName = e.getFirstName().equals("") ? null : e.getFirstName();
            st.setString(i++, firstName);
            String lastName = e.getLastName().equals("") ? null : e.getLastName();
            st.setString(i++, lastName);
            String country = e.getCountry().equals("") ? null : e.getCountry();
            st.setString(i++, country);
            String city = e.getCity().equals("") ? null : e.getCity();
            st.setString(i++, city);
            String postalCode = e.getPostalCode().equals("") ? null : e.getPostalCode();
            st.setString(i++, postalCode);
            String address = e.getAddress().equals("") ? null : e.getAddress();
            st.setString(i++, address);
            Date dateOfBirthSQL = e.getDateOfBirth().equals("") ? null : e.getDateOfBirthSQL();
            st.setDate(i++, dateOfBirthSQL);
            String email = e.getEmail().equals("") ? null : e.getEmail();
            st.setString(i++, email);
            st.setBoolean(i++, e.isTeacher());
            st.setInt(i++, e.getId());
            
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
