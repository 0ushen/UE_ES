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
public class Capacity implements Entity{
    
    private Integer id;
    private String capacityName;
    private String description;
    private Ue ue;
    
    
    public Capacity() {}
    
    public Capacity(Integer id, String capacityName, String description, 
            Ue ue) {
        
        this.id = id;
        this.capacityName = capacityName == null ? "" : capacityName;
        this.description = description == null ? "" : description;
        this.ue = ue;
        
    }
    
    
    @Override
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCapacityName() {
        return capacityName;
    }

    public void setCapacityName(String capacityName) {
        this.capacityName = capacityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ue getUe() {
        return ue;
    }

    public void setUe(Ue ue) {
        this.ue = ue;
    }
    
    
    
}
