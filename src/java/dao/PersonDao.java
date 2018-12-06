package dao;

import entity.Person;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.viewmodel.PersonSearchVM;

/*
 * @author Samir Benlafya
 */

public class PersonDao extends DAO<Person> {
    
    /* Load each person from the person table into an ArrayList, this entityList
     * is a protected variable in the parent class DAO. */
    @Override
    public ArrayList<Person> load() {
        
        try {
            // Execute an SQL query on the db and catch his result.
            ResultSet rs = conn.createStatement().executeQuery("SELECT "
                    + "person_id, "
                    + "first_name, "
                    + "last_name, "
                    + "country, "
                    + "city, "
                    + "postal_code, "
                    + "address, "
                    + "date_of_birth, "
                    + "email, "
                    + "is_teacher "
                    + "FROM person");
            
            /* Results coming raw from the database will be processed into one or
             * more Person entities which will be added into entityList. */
            buildPersonListFromDB(rs);
            
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // entityList now contains all the data we asked for as Person objects.
        return entityList;
    }
    
    /* Load each person from the person table that match the info coming from 
     * the viewmodel.*/
    public ArrayList<Person> load(PersonSearchVM vm) {
        
        try {
            /* Need to build the query first. Some fields may be empty and should
             * not be put into the query. */
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
            
            if(!vm.getFirstName().equals(""))
                query += "first_name = ? AND ";
                
            if(!vm.getLastName().equals(""))
                query += "last_name = ? AND ";
                
            if(!vm.getCountry().equals(""))
                query += "country = ? AND ";
                
            if(!vm.getCity().equals(""))
                query += "city = ? AND ";
                
            if(!vm.getPostalCode().equals(""))
                query += "postal_code = ? AND ";
                
            if(!vm.getAddress().equals(""))
                query += "address = ? AND ";
                  
            if(!vm.getDateOfBirth().equals(""))
                query += "date_of_birth = ? AND ";
                
            if(!vm.getEmail().equals(""))
                query += "email = ? AND ";
            
            /* From my view , whether a person is a teacher or not is always
             * mentionned. (not ideal) */
            query += "is_teacher = ?;";
            
            System.out.println("Query in Person load(vm) : " + query);
            
            // Using the query to build a Prepared Statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            // An index is used so i can keep count on parameters.
            int i = 1;
            
            /* Each field is checked and if it's not empty, it's value is set as
               a parameter in the request. */
            if(!vm.getFirstName().equals("")){
                st.setString(i, vm.getFirstName());
                i++;
            }
            if(!vm.getLastName().equals("")){
                st.setString(i, vm.getLastName());
                i++;
            }
            if(!vm.getCountry().equals("")){
                st.setString(i, vm.getCountry());
                i++;
            }
            if(!vm.getCity().equals("")){
                st.setString(i, vm.getCity());
                i++;
            }
            if(!vm.getPostalCode().equals("")){
                st.setString(i, vm.getPostalCode());
                i++;
            }
            if(!vm.getAddress().equals("")){
                st.setString(i, vm.getAddress());
                i++;
            }      
            if(!vm.getDateOfBirth().equals("")){
                st.setDate(i, vm.getDateOfBirthSQL());
                i++;
            }
            if(!vm.getEmail().equals("")){
                st.setString(i, vm.getEmail());
                i++;
            }
            
            //As i mentionned isTeacher is a mandatory search parameter.
            st.setBoolean(i, vm.isTeacher());
            
            System.out.println("Prepared statement in Person load(vm) : " + st);
            
            /* Execute the query and it's raw results are processed into one or
             * more Person entities. Eeach one of them is put into the 
             * entityList. */
            ResultSet rs = st.executeQuery();
            buildPersonListFromDB(rs);
            
            st.close();
              
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // entityList now contains all the data we asked for as Person objects.
        return entityList;
    }
    
    public Person load(Integer id) {
        
        try {
            // Execute an SQL query on the db and catch his result.
            String query = "SELECT "
                    + "person_id, "
                    + "first_name, "
                    + "last_name, "
                    + "country, "
                    + "city, "
                    + "postal_code, "
                    + "address, "
                    + "date_of_birth, "
                    + "email, "
                    + "is_teacher "
                    + "FROM person "
                    + "WHERE person_id = ?;";
            
            System.out.println("Query in Person load(id) : " + query);
            
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id);
            
            System.out.println("Prepared statement in Person load(id) : " + st);
            
            ResultSet rs = st.executeQuery();
            
            /* Execute the query and it's raw results are processed into one
             * Person entity. That person is put into the entityList. */
            buildPersonListFromDB(rs);
            
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* entityList now contains the person we searched for, so we return 
         * the first item in the list. */
        return entityList.get(0);
    }
    
    // Save this Person in the database.
    @Override
    public void save(Person e) {
        
        try {
            // Building the query
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
            
            System.out.println("Query in Person save(E) : " + query);
            
            // Query is used to initialise a prepared statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            //I use an index so i can add or delete a parameter more freely.
            int i = 1;
            
            /* Each input is checked so if it contains an empty string , 
             * it's value will be put as null in the db instead. */
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
            // As always isTeacher parameter is mandatory :(.
            st.setBoolean(i++, e.isTeacher());
            
            System.out.println("Prepared statement in Person save(E) : " + st);
            
            // Prepared statement is executed.
            st.executeUpdate();
            
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Update a specific person using his id. 
    @Override
    public void update(Person e) {
        
        try {
            // Building the query.
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
            
            System.out.println("Query in Person update(vm) : " + query);
            
            // Query is used to initialise a prepared statement.
            PreparedStatement st = conn.prepareStatement(query);
            
            //I use an index so i can add or delete a parameter more freely.
            int i = 1;
            
            /* Each input is checked so if it contains an empty string , 
             * it's value will be put as null in the db instead. */
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
            
            System.out.println("Prepared statement in Person update(E) : " + st);
            
            st.executeUpdate();
            
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Delete a a specific person from entityList and the db.
    @Override
    public void delete(Person e) {
        setDeleteCommand("DELETE FROM person WHERE person_id = ?;");
        super.delete(e);
    }
    
    // Delete a specific person from entityList and the db using an id directly.
    @Override
    public void delete(Integer id) {
        setDeleteCommand("DELETE FROM person WHERE person_id = ?;");
        super.delete(id);
    }
    
    /* This function will build a list of Persons 
     * (protected variable from the parent class entityList) using the raw data 
     * coming from the db. */   
    private void buildPersonListFromDB(ResultSet rs) {
        
        // Reset the list.
        entityList.clear();
        
        try {
            // Each row is processed into a Person entity
            String date;
            while (rs.next()){
                // Parsing SQL Date object in a string.
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    date = df.format(rs.getDate("date_of_birth"));
                } catch (NullPointerException ex) {
                    System.out.println("Date is undefined in database and will "
                            + "be set to an empty string in the person Entity");
                    date = "";
                }
                
                // Create a Person using the row  info.
                Person person = new Person(rs.getInt("person_id"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("country"), rs.getString("city"),
                        rs.getString("postal_code"), rs.getString("address"),
                        date, rs.getString("email"), rs.getBoolean("is_teacher"));
                
                // Adding that Person into the entityList.
                entityList.add(person);
            }
            
            /*System.out.println("****PERSONS IN ENTITYLIST****\n");
            entityList.forEach((person) -> {
                System.out.println("first name : " + person.getFirstName() +
                        " | last name : " + person.getLastName() + 
                        " | email : " + person.getEmail() + 
                        " | date of birth : " + person.getDateOfBirth() + 
                        " | teacher : " + person.isTeacher());
            });*/
        } catch (SQLException ex) {
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    } 
}
