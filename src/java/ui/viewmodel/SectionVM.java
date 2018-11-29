package ui.viewmodel;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Person;
import entity.Section;
import java.util.ArrayList;

 /*
 * @author Samir
 */

public class SectionVM {
    
    private Integer id;
    private String sectionName;
    private String description;
    private String teacherLastName;
    private Integer teacherId;
    
    public SectionVM() {}
    
    public SectionVM(Section section) {
        
        this.id = section.getId();
        this.sectionName = section.getSectionName();
        this.description = section.getDescription();
        this.teacherLastName = section.getTeacher().getLastName();
        this.teacherId = section.getTeacher().getId();
    }
    
    /* Constructor. If any of these values except id is null, i will set it to
     * an empty string so it is more manageable when converting it into json
     * and processing it on the client side. */
    public SectionVM(Integer id, String sectionName, String description,
            String teacherLastName, Integer teacherId) {
        
        this.id = id;
        this.sectionName = sectionName == null ? "" : sectionName;
        this.description = description == null ? "" : description;
        this.teacherLastName = teacherLastName == null ? "" : teacherLastName;
        this.teacherId = teacherId;
        
    }
    
    /* Constructor with json. Speed up the process of receiving the json string
     * from the client and turning it into a Section entity. */
    public SectionVM(String json){
        
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(json);
        if(jsonTree.isJsonObject()) {
            JsonObject section = jsonTree.getAsJsonObject();
            
            this.id = section.get("id").getAsString().equals("") ? null : section.get("id").getAsInt();
            this.sectionName = section.get("sectionName").getAsString();
            this.description = section.get("description").getAsString();
            this.teacherLastName = section.get("teacherLastName").getAsString();
            this.teacherId = section.get("teacherId").getAsString().equals("") ? null : section.get("teacherId").getAsInt();
        }
    }
    
    public static String ListOfSectionsToVMJson (ArrayList<Section> list) {
        
        ArrayList<SectionVM> listOfVM = new ArrayList<>();
        list.forEach((section) -> {
            SectionVM sectionToAdd = new SectionVM(section);
            listOfVM.add(sectionToAdd);
        });
        
        return new Gson().toJson(listOfVM);
    }
    
    public Integer getId() {
        return id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getDescription() {
        return description;
    }

    public String getTeacherLastName() {
        return teacherLastName;
    }
    
    public Integer getTeacherId() {
        return teacherId;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeacherLastName(String teacherLastName) {
        this.teacherLastName = teacherLastName;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    
}
