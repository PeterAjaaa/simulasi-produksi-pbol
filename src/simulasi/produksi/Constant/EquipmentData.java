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
public class EquipmentData {

    public Map<String, Integer> equipmentData = new LinkedHashMap<>();

    public EquipmentData() {
        equipmentData.put("3D Printer", 85);
        equipmentData.put("CNC Machine", 80);
        equipmentData.put("Assembly Robots", 75);
        equipmentData.put("VR Design Station", 70);
        equipmentData.put("Laser Cutter", 70);
        equipmentData.put("Testing Station", 65);
        equipmentData.put("Calibration Equipment", 65);
        equipmentData.put("Electronic Workbench", 60);
        equipmentData.put("Inventory Management System", 55);
        equipmentData.put("Paint Booths", 50);
    }
}
