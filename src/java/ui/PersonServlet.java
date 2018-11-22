package ui;

import dao.PersonDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.gson.Gson;
import entity.Person;
import java.util.ArrayList;
import ui.viewmodel.PersonSearchVM;

/**
 *
 * @author Samir
 */

public class PersonServlet extends HttpServlet {
   
    // Global variable personDao so only one will be used per session.
    private PersonDao pDao;
    
    /* GET and POST requests are handled the same way and redirected to this 
     * method. */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();
        
        if (session.getAttribute("pDao") == null) {
            pDao = new PersonDao();
            session.setAttribute("pDao", pDao);
        } else {
            pDao = (PersonDao) session.getAttribute("pDao");
        }
        
        // Action parameter will tell the server what to do.
        String action = request.getParameter("Action");
        
        // Data received from the client.
        String json = request.getParameter("JSON");
        System.out.println(json);
        
        /* list will contain data the database returned and will be sent to 
         * the client as a json string. */
        ArrayList<Person> list;
        
        // If action parameter is present a switch will handle what to do.
        if(action != null){
            switch (action){
                /* This case will return to the client all the people stored
                 * in the db as json. */
                case "getAll" :
                    // Load all the people in the person table into an ArrayList
                    list = pDao.load();
                    
                    // This list is sent to the client as a json string
                    String jsonList = new Gson().toJson(list);
                    WriteResponse(response, jsonList);
                    
                    break;
                
                /* Insert a new person into the database based on the data sent
                 * by the client. */
                case "doSave" :
                    // Create a person Entity via the json constructor.
                    Person personToAdd = new Person(json);
                    
                    // Insert this person Entity into the db.
                    pDao.save(personToAdd);
                    
                    break;
                
                /* Returna list of a search in the person table based on 
                 * some criteria the client sent. */ 
                case "getSearch" :
                    // Create a person Entity via the json constructor.
                    PersonSearchVM personToSearch = new PersonSearchVM(json);
                    
                    // Load all the people matching those criteria into a List.
                    list = pDao.load(personToSearch);
                    
                    // This list is sent to the client as a json string
                    json = new Gson().toJson(list);
                    WriteResponse(response, json);
                    
                    break;
                
                /* Performs an update on a person.
                 * The server receives a person object in json, this is 
                 * converted into a java Person Entity and then all the values
                 * in the database are matched with those in the entity. This
                 * can be done because the client sent the hidden id of this 
                 * person aswell*/    
                case "doUpdate" :
                    // Create a person Entity via the json constructor.
                    Person personToUpdate = new Person(json);
                    
                    // Performs the update.
                    pDao.update(personToUpdate);
                    
                    break;
                
                // Delete the person matching the id received.    
                case "doDelete" :
                    /* Id is received from the client even if this value 
                     * is hidden from the user. */
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    
                    // Performs the delete.
                    pDao.delete(id);
                    
                    break;
                
                default :
                    System.out.println("Wrong Action parameter : " + action);
            }
        }
    }
    
    // Send the response to the client.
    private void WriteResponse(HttpServletResponse response, String output) 
            throws IOException {
        
        response.setContentType("text/json");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(output);
    }
    
    
    /* Get and Post requests are treated the same way and redirected to the
     * processRequest() method. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /* Get and Post requests are treated the same way and redirected to the
     * processRequest() method. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}

