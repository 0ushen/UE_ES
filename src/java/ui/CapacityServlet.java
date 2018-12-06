package ui;


import entity.Capacity;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.ArrayList;
import com.google.gson.Gson;
import dao.CapacityDao;

/**
 *
 * @author Samir
 */

public class CapacityServlet extends HttpServlet {
   
    private CapacityDao cDao;
    
    /* GET and POST requests are handled the same way and redirected to this 
     * method. */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();
        
        if (session.getAttribute("cDao") == null) {
            cDao = new CapacityDao();
            session.setAttribute("cDao", cDao);
        } else {
            cDao = (CapacityDao) session.getAttribute("cDao");
        }
        
        // Action parameter will tell the server what to do.
        String action = request.getParameter("Action");
        
        // Data received from the client.
        String json = request.getParameter("JSON");
        
        /* list will contain data the database returned and will be sent to 
         * the client as a json string. */
        String jsonList;
        Integer ueId, capacityId;
        ArrayList<Capacity> list;
        
        // If action parameter is present a switch will handle what to do.
        if(action != null){
            switch (action){
                
                case "getAll" :
                    // Load all the capacities in the capacity table into an ArrayList
                    list = cDao.load();
                    
                    /* Transform this list of java entities into a jsonlist. */
                    jsonList = new Gson().toJson(list);
                    
                    // This list is sent to the client as a json string
                    WriteResponse(response, jsonList);
                    
                    break;
                    
                /* Send to the client the capacities contained in the ue the 
                 * client sent (id). */
                case "getC" : 
                    
                    ueId = Integer.parseInt(request.getParameter("ueId"));
                    
                    // the capacities are stored into a list.
                    list = cDao.load(ueId);
                    
                    // this list is converted to a json string.
                    jsonList = new Gson().toJson(list);
                    
                    WriteResponse(response, jsonList);
                    
                    break;
                    
                case "deleteCapacity" : 
                    
                    capacityId = Integer.parseInt(request.getParameter("capacityId"));
                    
                    cDao.delete(capacityId);
                    
                    break;
                    
                case "addCapacityToUe" :
                    
                    ueId = Integer.parseInt(request.getParameter("ueId"));
                    capacityId = Integer.parseInt(request.getParameter("capacityId"));
                    
                    cDao.addCapacityToUe(capacityId, ueId);
                    
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

