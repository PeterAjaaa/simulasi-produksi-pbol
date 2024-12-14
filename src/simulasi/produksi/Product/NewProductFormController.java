/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulasi.produksi.Constant.EquipmentData;
import simulasi.produksi.Constant.ProductType;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class NewProductFormController implements Initializable {
    @FXML
    private TextField ProductName;
    @FXML
    private ComboBox<String> RequiredToolsSelector;
    @FXML
    private ComboBox<String> ProductType;
    @FXML
    private TextField ProductID;
    @FXML
    private TextField ProductionTime;

    public static ProductDB productData = new ProductDB();
    boolean editdata;
    Map<String, Integer> productType = new ProductType().productType;
    Map<String, Integer> equipmentData = new EquipmentData().equipmentData;
    
    public TextField getProductID() {
        return ProductID;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (String item : productType.keySet()) {
            ProductType.getItems().add(item);
        }
        
        for (String item: equipmentData.keySet()) {
            RequiredToolsSelector.getItems().add(item);
        }
    }

    public void execute(Product data) {
        if (!data.getID().isEmpty()) {
            editdata = true;
            ProductID.setText(data.getID());
            ProductName.setText(data.getName());
            RequiredToolsSelector.setValue(data.getRequiredTool());
            ProductType.setValue(data.getProductType());
            ProductionTime.setText(String.valueOf(data.getProductionTime()));
            ProductID.requestFocus();
        }
    }

    @FXML
    private void saveData(ActionEvent event) {
        Product newData = new Product();
        newData.setID(ProductID.getText());
        newData.setName(ProductName.getText());
        newData.setRequiredTool(RequiredToolsSelector.getSelectionModel().getSelectedItem());
        newData.setProductType(ProductType.getSelectionModel().getSelectedItem());
        newData.setProductionTime(Integer.parseInt(ProductionTime.getText()));
        System.out.println(newData.getID());
        ProductShowController.productData.setProductModel(newData);

        if (editdata) {
            if (ProductShowController.productData.updateData()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil diubah", ButtonType.OK);
                alert.showAndWait();
                closeWindow();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Data gagal diubah", ButtonType.OK);
                a.showAndWait();
            }
        } else if (ProductShowController.productData.validateData(newData.getID()) <= 0) {
            if (ProductShowController.productData.insertData()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Data berhasil disimpan", ButtonType.OK);
                a.showAndWait();
                closeWindow();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Data gagal disimpan", ButtonType.OK);
                a.showAndWait();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR, "Data sudah ada", ButtonType.OK);
            a.showAndWait();
        }
    }

    @FXML
    private void resetData(ActionEvent event) {
        ProductName.setText(null);
        RequiredToolsSelector.setValue(null);
        ProductType.setValue(null);
        ProductionTime.setText(null);
        ProductID.requestFocus();
    }

    private void closeWindow() {
        Stage stage = (Stage) ProductID.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void openToolShowWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulasi/produksi/Tool/ToolShow.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stg = new Stage();
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setResizable(false);
            stg.setIconified(false);
            stg.setScene(scene);
            stg.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void openProductShowWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulasi/produksi/Product/ProductShow.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stg = new Stage();
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setResizable(false);
            stg.setIconified(false);
            stg.setScene(scene);
            stg.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void openWorkerShowWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulasi/produksi/Worker/WorkerShow.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stg = new Stage();
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setResizable(false);
            stg.setIconified(false);
            stg.setScene(scene);
            stg.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
