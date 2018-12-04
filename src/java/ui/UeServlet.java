package ui;

import dao.SectionDao;
import dao.UeDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import entity.Section;
import entity.Ue;
import java.util.ArrayList;
import ui.viewmodel.UeVM;

/**
 *
 * @author Samir
 */

public class UeServlet extends HttpServlet {
   
    /* Global variables SectionDao and UeDao so only one will be used 
     * per session. */
    private static SectionDao sDao;
    private static UeDao ueDao;
    
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
        
        if (session.getAttribute("ueDao") == null) {
            ueDao = new UeDao();
            session.setAttribute("ueDao", ueDao);
        } else {
            ueDao = (UeDao) session.getAttribute("ueDao");
        }
        
        // Action parameter will tell the server what to do.
        String action = request.getParameter("Action");
        
        // Data received from the client.
        String json = request.getParameter("JSON");
        
        /* list will contain data the database returned and will be sent to 
         * the client as a json string. */
        ArrayList<Ue> list;
        String jsonList;
        UeVM ueVM;
        Section section;
        
        // If action parameter is present a switch will handle what to do.
        if(action != null){
            switch (action){
                /* This case will return to the client all the ue stored
                 * in the db as json. */
                case "getAll" :
                    // Load all the ue in the ue table into an ArrayList
                    list = ueDao.load();
                    
                    /* Transform this list of java entities into useful data for
                    /* the view model and return it as a json string. */
                    jsonList = UeVM.ListOfUeToVMJson(list);
                    
                    // This list is sent to the client as a json string
                    WriteResponse(response, jsonList);
                    
                    break;
                
                /* Insert a new ue into the database based on the data sent
                 * by the client. */
                case "doSave" :
                    // Create a ue view model via the json constructor.
                    ueVM = new UeVM(json);
                    
                    // Create a ue entity from the view model.
                    section = sDao.load(ueVM.getSectionId());
                    Ue ueToAdd = new Ue(null,
                            ueVM.getUeName(), 
                            section,
                            ueVM.getCode(),
                            ueVM.getNbrOfPeriods(),
                            ueVM.getDescription(), 
                            ueVM.isDecisive());
                    
                    // Insert this ue Entity into the db.
                    ueDao.save(ueToAdd);
                    
                    break;
                
                /* Returna a list of a search in the ue table based on 
                 * some criteria the client sent. */ 
                case "getSearch" :
                    // Create a ue view model via the json constructor.
                    UeVM ueToSearch = new UeVM(json);
                    
                    // Load all the ue matching those criteria into a List.
                    list = ueDao.load(ueToSearch);
                    
                    /* Transform this list of java entities into useful data for
                    /* the view model and return it as a json string. */
                    jsonList = UeVM.ListOfUeToVMJson(list);
                    
                    // This list is sent to the client as a json string.
                    WriteResponse(response, jsonList);
                    
                    break;
                
                /* Performs an update on an ue.
                 * The server receives a ue object in json, this is 
                 * converted into a java Ue Entity and then all the values
                 * in the database are matched with those in the entity. This
                 * can be done because the client sent the id of this 
                 * ue aswell. */    
                case "doUpdate" :
                    // Create a ue view model via the json constructor.
                    ueVM = new UeVM(json);
                    
                    // Create a Ue entity from the view model.
                    section = sDao.load(ueVM.getSectionId());
                    Ue ueToUpdate = new Ue(ueVM.getId(),
                            ueVM.getUeName(), 
                            section,
                            ueVM.getCode(),
                            ueVM.getNbrOfPeriods(),
                            ueVM.getDescription(), 
                            ueVM.isDecisive());
                    
                    // Performs the update.
                    ueDao.update(ueToUpdate);
                    
                    break;
                
                // Delete the ue matching the id received.    
                case "doDelete" :
                    
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    
                    // Performs the delete.
                    ueDao.delete(id);
                    
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

