package entity;

import dao.PersonDao;
import ui.viewmodel.SectionVM;

 /*
 * @author Samir
 */

public class Section implements Entity {
    
    private Integer id;
    private String sectionName;
    private String description;
    private Person teacher;
    private static PersonDao pDao = new PersonDao();
    
    public Section() {}
   
    /* Constructor. If any of these values except id is null, i will set it to
     * an empty string so it is more manageable when converting it into json
     * and processing it on the client side. */
    public Section(Integer id, String sectionName, String description,
            Person teacher) {
        
        this.id = id;
        this.sectionName = sectionName == null ? "" : sectionName;
        this.description = description == null ? "" : description;
        this.teacher = teacher;
        
    }
     
    public Section (SectionVM sectionVM){
        this.id = sectionVM.getId();
        this.sectionName = sectionVM.getSectionName();
        this.description = sectionVM.getDescription();
        this.teacher = pDao.load(sectionVM.getId());
    } 
    
    @Override
    public Integer getId() {
        return id;
    }
    
    public String getSectionName() {
        return sectionName;
    }

    public String getDescription() {
        return description;
    }

    public Person getTeacher() {
        return teacher;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    
}
