/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Tool;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import simulasi.produksi.Constant.Availability;
import simulasi.produksi.Constant.EquipmentData;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class NewToolFormController implements Initializable {

    @FXML
    private TextField ToolName;
    @FXML
    private ComboBox<String> ToolTypeSelector;
    @FXML
    private ComboBox<String> ToolAvailabilitySelector;
    @FXML
    private TextField ToolID;

    public static ToolDB toolData = new ToolDB();

    boolean editdata;

    Map<String, Integer> equipmentData = new EquipmentData().equipmentData;
    List<String> availability = new Availability().availability;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (String item : equipmentData.keySet()) {
            ToolTypeSelector.getItems().add(item);
        }

        for (String status : availability) {
            ToolAvailabilitySelector.getItems().add(status);
        }
    }

    public TextField getToolID() {
        return ToolID;
    }

    public void execute(Tool data) {
        if (!data.getID().isEmpty()) {
            editdata = true;
            ToolID.setText(data.getID());
            ToolName.setText(data.getName());
            ToolTypeSelector.setValue(data.getType());
            ToolAvailabilitySelector.setValue(data.getAvailability());
            ToolName.requestFocus();
        }
    }

    @FXML
    private void saveData(ActionEvent event) {
        Tool newData = new Tool();
        newData.setID(ToolID.getText());
        newData.setName(ToolName.getText());
        newData.setType(ToolTypeSelector.getSelectionModel().getSelectedItem());
        newData.setAvailability(ToolAvailabilitySelector.getSelectionModel().getSelectedItem());
        newData.setEfficiency(equipmentData.get(ToolTypeSelector.getSelectionModel().getSelectedItem()));
        ToolShowController.toolData.setToolmodel(newData);

        if (editdata) {
            if (ToolShowController.toolData.updateData()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil diubah", ButtonType.OK);
                alert.showAndWait();
                closeWindow();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Data gagal diubah", ButtonType.OK);
                a.showAndWait();
            }
        } else if (ToolShowController.toolData.validateData(newData.getID()) <= 0) {
            if (ToolShowController.toolData.insertData()) {
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
        ToolName.setText(null);
        ToolTypeSelector.setValue(null);
        ToolAvailabilitySelector.setValue(null);
        ToolName.requestFocus();
    }

    private void closeWindow() {
        Stage stage = (Stage) ToolID.getScene().getWindow();
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
