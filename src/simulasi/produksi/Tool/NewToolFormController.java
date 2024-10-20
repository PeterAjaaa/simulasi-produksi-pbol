/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Tool;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class NewToolFormController implements Initializable {

    @FXML
    private Button SubmitButton;
    @FXML
    private TextField ToolName;
    @FXML
    private ComboBox<String> ToolTypeSelector;
    @FXML
    private Button ResetButton;
    @FXML
    private ComboBox<String> ToolAvailabilitySelector;
    @FXML
    private MenuBar NavigationMenu;
    @FXML
    private TextField ToolID;

    public static ToolDB toolData = new ToolDB();

    boolean editdata;

    public Map<String, Integer> equipmentData = new LinkedHashMap<>();

    List<String> availability = Arrays.asList(
            "Ready",
            "Maintenance",
            "Disabled"
    );

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) ToolID.getScene().getWindow();
        stage.close();
    }

}
