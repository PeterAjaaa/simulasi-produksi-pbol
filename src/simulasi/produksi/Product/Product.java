/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.Product;

/**
 *
 * @author Peter
 */
public class Product {

    String ID, name, requiredTool, productType;
    int productionTime, productionRate;

    public Product() {
    }

    ;
    
    public Product(String id, String name) {
        this.ID = id;
        this.name = name;
    }

    public Product(String id, String name, String requiredTool, int productionTime) {
        this.ID = id;
        this.name = name;
        this.requiredTool = requiredTool; // Set the required tool
        this.productionTime = productionTime; // Set the production time
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequiredTool() {
        return requiredTool;
    }

    public void setRequiredTool(String requiredTool) {
        this.requiredTool = requiredTool;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }

    public int getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(int productionRate) {
        this.productionRate = productionRate;
    }

    @Override
    public String toString() {
        return name;
    }

}
