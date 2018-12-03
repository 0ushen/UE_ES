
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
 * 
 * 
 * TODO : 
 * - 
 */


public class Main {
    
    public static void main(String[] args){
            
        PersonDao pDao = new PersonDao();
        ArrayList<Person> persons = pDao.load();
        ArrayList<Person> toDelete = new ArrayList();

        for(Person person : persons) {
            System.out.println("Prenom : " + person.getFirstName() + " | Nom : " 
                    + person.getLastName() + " | Email : " + person.getEmail() + 
                    " | Date de naissance : " + person.getDateOfBirth());
            
            if(person.getFirstName().equals("Samir")){
                toDelete.add(person);
            }
        }
        
        for(Person person : toDelete){
            pDao.delete(person);
        }
        
        
        /*Person test = new Person(null, "Samir", "Benlafya", "Belgium", "Liege",
                "4000", "1b, rue des Rivageois", LocalDate.of(1991, 06, 04),
                "samir.benlafya@gmail.com", false);
        
        pDao.save(test);*/
        
        //pDao.delete(2652);
        
    }
}
