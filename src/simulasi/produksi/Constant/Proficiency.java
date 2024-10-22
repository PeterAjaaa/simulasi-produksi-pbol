/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.Constant;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Peter
 */
public class Proficiency {

    public Map<String, Double> proficiency = new LinkedHashMap<>();
    
    public Proficiency(){
        proficiency.put("Beginner", 0.25);
        proficiency.put("Intermediate", 0.5);
        proficiency.put("Advanced", 1.0);
    }
}
