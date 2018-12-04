/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Samir
 */
public class Ue implements Entity {
    
    private Integer id;
    private String ueName;
    private Section section;
    private String code;
    private Integer nbrOfPeriods;
    private String description;
    private boolean isDecisive;
    
    
    public Ue() {}
    
    public Ue(Integer id, String ueName, Section section, String code,
            Integer nbrOfPeriods, String description, boolean isDecisive) {
        
        this.id = id;
        this.ueName = ueName == null ? "" : ueName;
        this.section = section;
        this.code = code == null ? "" : code;
        this.nbrOfPeriods = nbrOfPeriods;
        this.description = description == null ? "" : description;
        this.isDecisive = isDecisive;
        
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
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
