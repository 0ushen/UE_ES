/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;


import java.util.*;
import java.time.LocalDate;
import java.sql.Date;

 /*
 * @author Samir
 */

public class Person implements Entity {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private String postalCode;
    private String address;
    private LocalDate dateOfBirth;
    private String email;
    private boolean isTeacher;
    
    public Person() {}
    
    public Person(Integer id, String firstName, String lastName, String country, String city,
            String postalCode, String address, LocalDate dateOfBirth, String email,
            boolean isTeacher) {
        
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.isTeacher = isTeacher;
        
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }
    
    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public java.sql.Date getDateOfBirthSQL() {
        java.sql.Date dateSQL = Date.valueOf(dateOfBirth);
        return dateSQL;
    }

    public String getEmail() {
        return email;
    }

    public boolean isTeacher() {
        return isTeacher;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsTeacher(boolean isTeacher) {
        this.isTeacher = isTeacher;
    }
    
    
    
    
}
