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
        String json = request.getParameter("JSON");
        ArrayList<Person> list;
        if(action != null){
            switch (action){
                case "getAll" :
                    list = pDao.load();
                    String jsonList = new Gson().toJson(list);
                    WriteResponse(response, jsonList);
                    break;
            
                case "doSave" :
                    Person personToAdd = new Person(json);
                    pDao.save(personToAdd);
                    break;
                
                case "getSearch" :
                    Person personToSearch = new Person(json);
                    list = pDao.load(personToSearch);
                    json = new Gson().toJson(list);
                    WriteResponse(response, json);
                    break;
                    
                case "doUpdate" :
                    Person personToUpdate = new Person(json);
                    pDao.update(personToUpdate);
                    break;
                
                case "doDelete" :
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    pDao.delete(id);
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

