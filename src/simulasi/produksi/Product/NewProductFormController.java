/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Product;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    private TextField ProductionRate;
    @FXML
    private ComboBox<String> ProductType;
    @FXML
    private TextField ProductID;
    @FXML
    private TextField ProductionTime;

    public static ProductDB productData = new ProductDB();
    boolean editdata;
    Map<String, Integer> productType = new ProductType().productType;
    
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
    }

    public void execute(Product data) {
        if (!data.getID().isEmpty()) {
            editdata = true;
            ProductID.setText(data.getID());
            ProductName.setText(data.getName());
            RequiredToolsSelector.setValue(data.getRequiredTool());
            ProductType.setValue(data.getProductType());
            ProductionTime.setText(String.valueOf(data.getProductionTime()));
            ProductionRate.setText(String.valueOf(data.getProductionRate()));
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
        newData.setProductionRate(Integer.parseInt(ProductionRate.getText()));
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
        ProductionRate.setText(null);
        ProductID.requestFocus();
    }

    private void closeWindow() {
        Stage stage = (Stage) ProductID.getScene().getWindow();
        stage.close();
    }

}
