/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.Worker;

/**
 *
 * @author Peter
 */
public class Worker {
    String id, name, proficiency, availability;
    int work_hours;
    
    public Worker(){};
    
    public Worker(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getWorkHours() {
        return work_hours;
    }

    public void setWorkHours(int work_hours) {
        this.work_hours = work_hours;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
