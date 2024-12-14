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
public class ProductType {

    public Map<String, Integer> productType = new LinkedHashMap<>();

    public ProductType() {
        productType.put("Racing Drone", 30);
        productType.put("Entertainment Drone", 40);
        productType.put("Delivery Drone", 50);
        productType.put("Medical Delivery Drone", 60);
        productType.put("Agricultural Drone", 65);
        productType.put("Construction Drone", 70);
        productType.put("Environmental Monitoring Drone", 80);
        productType.put("SAR Drone", 85);
        productType.put("Combat Drone", 95);
    }
}
