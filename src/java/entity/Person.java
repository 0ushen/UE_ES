package entity;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private String dateOfBirth;
    private String email;
    private boolean isTeacher;
    
    public Person() {}
    
    /* Constructor. If any of these values except id is null, i will set it to
     * an empty string so it is more manageable when converting it into json
     * and processing it on the client side. */
    public Person(Integer id, String firstName, String lastName, String country, 
            String city, String postalCode, String address, String dateOfBirth, 
            String email, boolean isTeacher) {
        
        this.id = id;
        this.firstName = firstName == null ? "" : firstName;
        this.lastName = lastName == null ? "" : lastName;
        this.country = country == null ? "" : country;
        this.city = city == null ? "" : city;
        this.postalCode = postalCode == null ? "" : postalCode;
        this.address = address == null ? "" : address;
        this.dateOfBirth = dateOfBirth == null ? "" : dateOfBirth;
        this.email = email == null ? "" : email;
        this.isTeacher = isTeacher;
        
    }
    
    /* Constructor with json. Speed up the process of receiving the json string
     * from the client and turning it into a Person entity. */
    public Person(String json){
        
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(json);
        if(jsonTree.isJsonObject()) {
            JsonObject person = jsonTree.getAsJsonObject();
            
            this.id = person.has("id") ? person.get("id").getAsInt() : null; 
            this.firstName = person.get("firstName").getAsString();
            this.lastName = person.get("lastName").getAsString();
            this.country = person.get("country").getAsString();
            this.city = person.get("city").getAsString();
            this.postalCode = person.get("postalCode").getAsString();
            this.address = person.get("address").getAsString();
            this.dateOfBirth = person.get("dateOfBirth").getAsString();
            this.email = person.get("email").getAsString();
            this.isTeacher = person.get("isTeacher").getAsString().equals("true");
        }
    }
            
    
    @Override
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
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    // Transforms a date string (format yyyy-MM-dd) into a LocalDate object.
    public LocalDate getDateOfBirthObject() {
        return LocalDate.parse(dateOfBirth);
    }
    
    // Transforms a LocalDateobject into an SQL Date Object.
    public java.sql.Date getDateOfBirthSQL() {
        java.sql.Date dateSQL = Date.valueOf(getDateOfBirthObject());
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

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsTeacher(boolean isTeacher) {
        this.isTeacher = isTeacher;
    }
    
    
    
    
}
