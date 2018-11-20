/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.PersonDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Person;
import java.util.ArrayList;
/**
 *
 * @author Samir
 */
public class PersonServlet extends HttpServlet {
   
    private PersonDao pDao;
    
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
        
        String action = request.getParameter("Action");
        String json;
        JsonParser parser;
        JsonElement jsonTree;
        ArrayList<Person> list;
        if(action != null){
            switch (action){
                case "getAll" :
                    list = pDao.load();
                    json = new Gson().toJson(list);
                    WriteResponse(response, json);
                    break;
            
                case "doSave" :
                    json = request.getParameter("JSON");
                    System.out.println(json);
                    parser = new JsonParser();
                    jsonTree = parser.parse(json);
                    if(jsonTree.isJsonObject()) {
                        JsonObject person = jsonTree.getAsJsonObject();

                        boolean isTeacher = person.get("isTeacher").getAsString()
                                .equals("true");

                        Person personToAdd = new Person(null, 
                                person.get("firstName").getAsString(),
                                person.get("lastName").getAsString(), 
                                person.get("country").getAsString(), 
                                person.get("city").getAsString(),
                                person.get("postalCode").getAsString(), 
                                person.get("address").getAsString(),
                                person.get("dateOfBirth").getAsString(),
                                person.get("email").getAsString(), isTeacher);

                        System.out.println("Prenom : " + personToAdd.getFirstName() +
                                " | Nom : " + personToAdd.getLastName() + 
                                " | Email : " + personToAdd.getEmail() + 
                                " | Date de naissance : " + 
                                personToAdd.getDateOfBirth() + " | isTeacher : " +
                                personToAdd.isTeacher());

                        pDao.save(personToAdd);
                    }
                    break;
                
                case "getSearch" :
                    json = request.getParameter("JSON");
                    System.out.println(json);
                    parser = new JsonParser();
                    jsonTree = parser.parse(json);
                    if(jsonTree.isJsonObject()) {
                        JsonObject person = jsonTree.getAsJsonObject();

                        boolean isTeacher = person.get("isTeacher").getAsString()
                                .equals("true");

                        Person personToSearch = new Person(null, 
                                person.get("firstName").getAsString(),
                                person.get("lastName").getAsString(), 
                                person.get("country").getAsString(), 
                                person.get("city").getAsString(),
                                person.get("postalCode").getAsString(), 
                                person.get("address").getAsString(),
                                person.get("dateOfBirth").getAsString(),
                                person.get("email").getAsString(), isTeacher);

                        System.out.println("Prenom : " + personToSearch.getFirstName() +
                                " | Nom : " + personToSearch.getLastName() + 
                                " | Email : " + personToSearch.getEmail() + 
                                " | Date de naissance : " + 
                                personToSearch.getDateOfBirth() + " | isTeacher : " +
                                personToSearch.isTeacher());

                        list = pDao.load(personToSearch);
                        json = new Gson().toJson(list);
                        WriteResponse(response, json);
                    }
                    break;
                    
                case "doUpdate" :
                    json = request.getParameter("JSON");
                    System.out.println(json);
                    parser = new JsonParser();
                    jsonTree = parser.parse(json);
                    if(jsonTree.isJsonObject()) {
                        JsonObject person = jsonTree.getAsJsonObject();

                        boolean isTeacher = person.get("isTeacher").getAsString()
                                .equals("true");
                        Integer id = person.get("id").getAsInt();
                        Person personToUpdate = new Person(id, 
                                person.get("firstName").getAsString(),
                                person.get("lastName").getAsString(), 
                                person.get("country").getAsString(), 
                                person.get("city").getAsString(),
                                person.get("postalCode").getAsString(), 
                                person.get("address").getAsString(),
                                person.get("dateOfBirth").getAsString(),
                                person.get("email").getAsString(), isTeacher);

                        System.out.println("Prenom : " + personToUpdate.getFirstName() +
                                " | Nom : " + personToUpdate.getLastName() + 
                                " | Email : " + personToUpdate.getEmail() + 
                                " | Date de naissance : " + 
                                personToUpdate.getDateOfBirth() + " | isTeacher : " +
                                personToUpdate.isTeacher());

                        pDao.update(personToUpdate);
                    }
                    break;
                
                default :
                    System.out.println("Wrong Action parameter : " + action);
            }
        }
    }

    private void WriteResponse(HttpServletResponse response, String output) 
            throws IOException {
        
        response.setContentType("text/json");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(output);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}

