
import dao.PersonDao;
import entity.Person;
import java.time.LocalDate;
import java.util.ArrayList;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Samir
 */
public class Main {
    
    public static void main(String[] args){
            
        PersonDao pDao = new PersonDao();
        ArrayList<Person> persons = pDao.load();

        for(Person person : persons) {
        System.out.println("Prenom : " + person.getFirstName() + " | Nom : " 
                + person.getLastName() + " | Email : " + person.getEmail() + 
                " | Date de naissance : " + person.getDateOfBirth());
        }
        
        
        Person test = new Person(null, "Samir", "Benlafya", "Belgium", "Liege",
                "4000", "1b, rue des Rivageois", LocalDate.of(1991, 06, 04),
                "samir.benlafya@gmail.com", false);
        
        pDao.save(test);
        
        //pDao.delete(2652);
        
    }
}
