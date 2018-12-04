/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.viewmodel;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.PersonDao;
import entity.Person;
import entity.Ue;
import java.util.ArrayList;

/**
 *
 * @author Samir
 */
public class UeVM {
    
    private Integer id;
    private String ueName;
    private String sectionName;
    private Integer sectionId ;
    private String code;
    private Integer nbrOfPeriods;
    private String description;
    private boolean isDecisive;
    
    public UeVM() {};
    
    public UeVM(Integer id, String ueName, Integer sectionId, String sectionName,
            String code, Integer nbrOfPeriods, String description,
            boolean isDecisive) {
        
        this.id = id;
        this.ueName = ueName == null ? "" : ueName;
        this.sectionId = sectionId;
        this.sectionName = sectionName == null ? "" : sectionName;
        this.code = code == null ? "" : code;
        this.nbrOfPeriods = nbrOfPeriods;
        this.description = description == null ? "" : description;
        this.isDecisive = isDecisive;
        
    }
    
    /* Constructor via a Ue entity. Only things pertinent to the client
     * view model will be sent. This will reduce the amount of data sent between
     * server and client.*/
    public UeVM(Ue ue) {
        
        this.id = ue.getId();
        this.ueName = ue.getUeName();
        this.sectionId = ue.getSection().getId();
        this.sectionName = ue.getSection().getSectionName();
        this.code = ue.getCode();
        this.nbrOfPeriods = ue.getNbrOfPeriods();
        this.description = ue.getDescription();
        this.isDecisive = ue.isDecisive();
    }
    
    /* Constructor with json. Speed up the process of receiving the json string
     * from the client and turning it into a Ue entity. */
    public UeVM(String json){
        
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(json);
        if(jsonTree.isJsonObject()) {
            JsonObject ue = jsonTree.getAsJsonObject();
            
            this.id = ue.get("id").getAsString().equals("") ? null : ue.get("id").getAsInt();
            this.ueName = ue.get("ueName").getAsString();
            this.sectionId = ue.get("sectionId").getAsString().equals("") ? null : ue.get("sectionId").getAsInt();
            this.sectionName = ue.get("sectionName").getAsString();
            this.code = ue.get("code").getAsString();
            this.nbrOfPeriods = ue.get("nbrOfPeriods").getAsString().equals("") ? null : ue.get("nbrOfPeriods").getAsInt();
            this.description = ue.get("description").getAsString();
            this.isDecisive = ue.get("isDecisive").getAsBoolean();
        }
    }
    
    /* This will turn an array of Ue entities into a list of ue view 
     * models and will return it in a json format. */
    public static String ListOfUeToVMJson (ArrayList<Ue> list) {
        
        ArrayList<UeVM> listOfVM = new ArrayList<>();
        list.forEach((ue) -> {
            UeVM ueToAdd = new UeVM(ue);
            listOfVM.add(ueToAdd);
        });
        
        return new Gson().toJson(listOfVM);
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUeName() {
        return ueName;
    }

    public void setUeName(String ueName) {
        this.ueName = ueName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNbrOfPeriods() {
        return nbrOfPeriods;
    }

    public void setNbrOfPeriods(Integer nbrOfPeriods) {
        this.nbrOfPeriods = nbrOfPeriods;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDecisive() {
        return isDecisive;
    }

    public void setIsDecisive(boolean isDecisive) {
        this.isDecisive = isDecisive;
    }

 
}
