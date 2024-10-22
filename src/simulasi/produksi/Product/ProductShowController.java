/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulasi.produksi.Tool.Tool;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class ProductShowController implements Initializable {

    @FXML
    private TableView<Product> ProductTableView;

    public static ProductDB productData = new ProductDB();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showData();
    }

    private void showData() {
        ObservableList<Product> data = NewProductFormController.productData.loadData();
        if (data != null) {
            ProductTableView.getColumns().clear();
            ProductTableView.getItems().clear();
            TableColumn col = new TableColumn("ID");
            col.setCellValueFactory(new PropertyValueFactory<Tool, String>("ID"));
            ProductTableView.getColumns().addAll(col);
            col = new TableColumn("Name");
            col.setCellValueFactory(new PropertyValueFactory<Tool, String>("name"));
            ProductTableView.getColumns().addAll(col);
            col = new TableColumn("Required Tool");
            col.setCellValueFactory(new PropertyValueFactory<Tool, String>("requiredTool"));
            ProductTableView.getColumns().addAll(col);
            col = new TableColumn("Product Type");
            col.setCellValueFactory(new PropertyValueFactory<Tool, String>("productType"));
            ProductTableView.getColumns().addAll(col);
            col = new TableColumn("Production Time");
            col.setCellValueFactory(new PropertyValueFactory<Tool, Integer>("productionTime"));
            ProductTableView.getColumns().addAll(col);
            col = new TableColumn("Production Rate");
            col.setCellValueFactory(new PropertyValueFactory<Tool, Integer>("productionRate"));
            ProductTableView.getColumns().addAll(col);
            ProductTableView.setItems(data);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Data kosong", ButtonType.OK);
            alert.showAndWait();
            ProductTableView.getScene().getWindow().hide();
        }
    }

    @FXML
    private void addData(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewProductForm.fxml"));
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
        showData();
    }

    @FXML
    private void deleteData(ActionEvent event) {
        Product selectedData = ProductTableView.getSelectionModel().getSelectedItem();
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirm Deletion", ButtonType.YES, ButtonType.NO);
        a.showAndWait();
        if (a.getResult() == ButtonType.YES) {
            if (ProductShowController.productData.deleteData(selectedData.getID())) {
                Alert b = new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus", ButtonType.OK);
                b.showAndWait();
            } else {
                Alert b = new Alert(Alert.AlertType.ERROR, "Data gagal dihapus", ButtonType.OK);
                b.showAndWait();
            }
            showData();
        }
    }

    @FXML
    private void updateData(ActionEvent event) {
        Product selectedData = ProductTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewProductForm.fxml"));
            Parent root = (Parent) loader.load();
            NewProductFormController newProductController = (NewProductFormController) loader.getController();
            newProductController.getProductID().setEditable(false);
            newProductController.getProductID().setDisable(true);
            newProductController.execute(selectedData);
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
        showData();
    }
}
