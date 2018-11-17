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
   
    PersonDao pDao;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();
        
        if (session.getAttribute("pDao") == null) {
            pDao = new PersonDao();
            session.setAttribute("pDao", pDao);
        } else {
            pDao = (PersonDao) session.getAttribute("categoryDAO");
        }
        
        String Action = request.getParameter("Action");
        if(Action != null){
            if(Action.equals("getList")){
                ArrayList<Person> list = pDao.load();
                String json = new Gson().toJson(list);
                WriteResponse(response, json);
            }
            else if(Action.equals("add")){
                ArrayList<Person> list = pDao.load();
                String json = request.getParameter("JSON");
                System.out.println(json);
                
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
        System.out.println("Hello");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}

