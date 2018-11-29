package ui;

import dao.PersonDao;
import dao.SectionDao;
import entity.Person;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import entity.Section;
import java.util.ArrayList;
import ui.viewmodel.SectionVM;

/**
 *
 * @author Samir
 */

public class SectionServlet extends HttpServlet {
   
    // Global variable SectionDao so only one will be used per session.
    private SectionDao sDao;
    private PersonDao pDao;
    
    /* GET and POST requests are handled the same way and redirected to this 
     * method. */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();
        
        if (session.getAttribute("sDao") == null) {
            sDao = new SectionDao();
            session.setAttribute("sDao", sDao);
        } else {
            sDao = (SectionDao) session.getAttribute("sDao");
        }
        
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
        ArrayList<Section> list;
        String jsonList;
        SectionVM sectionVM;
        Person teacher;
        
        // If action parameter is present a switch will handle what to do.
        if(action != null){
            switch (action){
                /* This case will return to the client all the sections stored
                 * in the db as json. */
                case "getAll" :
                    // Load all the sections in the section table into an ArrayList
                    list = sDao.load();
                    
                    /* Transform this list of java entities into useful data for
                    /* the view model and return it as a json string. */
                    jsonList = SectionVM.ListOfSectionsToVMJson(list);
                    
                    // This list is sent to the client as a json string
                    WriteResponse(response, jsonList);
                    
                    break;
                
                /* Insert a new section into the database based on the data sent
                 * by the client. */
                case "doSave" :
                   // Create a section view model via the json constructor.
                    sectionVM = new SectionVM(json);
                    
                    teacher = pDao.load(sectionVM.getId());
                    
                    // Create a person entity from the view model.
                    Section sectionToAdd = new Section(null,
                            sectionVM.getSectionName(), 
                            sectionVM.getDescription(), 
                            teacher);
                    
                    // Insert this section Entity into the db.
                    sDao.save(sectionToAdd);
                    
                    break;
                
                /* Returna a list of a search in the section table based on 
                 * some criteria the client sent. */ 
                case "getSearch" :
                    // Create a section view model via the json constructor.
                    SectionVM sectionToSearch = new SectionVM(json);
                    
                    // Load all the sections matching those criteria into a List.
                    list = sDao.load(sectionToSearch);
                    
                    /* Transform this list of java entities into useful data for
                    /* the view model and return it as a json string. */
                    jsonList = SectionVM.ListOfSectionsToVMJson(list);
                    
                    // This list is sent to the client as a json string.
                    WriteResponse(response, jsonList);
                    
                    break;
                
                /* Performs an update on a section.
                 * The server receives a section object in json, this is 
                 * converted into a java Section Entity and then all the values
                 * in the database are matched with those in the entity. This
                 * can be done because the client sent the hidden id of this 
                 * section aswell*/    
                case "doUpdate" :
                    // Create a section viewmodel via the json constructor.
                    sectionVM = new SectionVM(json);
                    
                    teacher = pDao.load(sectionVM.getTeacherId());
                    
                    // Convert the view model into a java entity.
                    Section sectionToUpdate = new Section(sectionVM.getId(), 
                            sectionVM.getSectionName(), sectionVM.getDescription(),
                            teacher);
                    
                    // Performs the update.
                    sDao.update(sectionToUpdate);
                    
                    break;
                
                // Delete the section matching the id received.    
                case "doDelete" :
                    /* Id is received from the client even if this value 
                     * is hidden from the user. */
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    
                    // Performs the delete.
                    sDao.delete(id);
                    
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

