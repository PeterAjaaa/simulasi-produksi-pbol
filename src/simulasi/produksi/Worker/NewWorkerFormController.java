/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Worker;

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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulasi.produksi.Constant.Proficiency;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class NewWorkerFormController implements Initializable {

    @FXML
    private TextField WorkerName;
    @FXML
    private ComboBox<String> WorkerProficiencySelector;
    @FXML
    private TextField WorkerHour;
    @FXML
    private Button SubmitButton;
    @FXML
    private Button ResetButton;
    @FXML
    private TextField WorkerID;

    public static WorkerDB workerData = new WorkerDB();
    boolean editdata;
    Map<String, Double> proficiency = new Proficiency().proficiency;

    public TextField getWorkerID() {
        return WorkerID;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (String item : proficiency.keySet()) {
            WorkerProficiencySelector.getItems().add(item);
        }
    }

    public void execute(Worker data) {
        if (!data.getID().isEmpty()) {
            editdata = true;
            WorkerID.setText(data.getID());
            WorkerName.setText(data.getName());
            WorkerProficiencySelector.setValue(data.getProficiency());
            WorkerHour.setText(String.valueOf(data.getWorkHours()));
            WorkerID.requestFocus();
        }
    }

    @FXML
    public void saveData(ActionEvent event) {
        Worker newData = new Worker();
        newData.setID(WorkerID.getText());
        newData.setName(WorkerName.getText());
        newData.setProficiency(WorkerProficiencySelector.getSelectionModel().getSelectedItem());
        newData.setWorkHours(Integer.parseInt(WorkerHour.getText()));
        WorkerShowController.workerData.setWorkerModel(newData);

        if (editdata) {
            if (WorkerShowController.workerData.updateData()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data berhasil diubah", ButtonType.OK);
                alert.showAndWait();
                closeWindow();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Data gagal diubah", ButtonType.OK);
                a.showAndWait();
            }
        } else if (WorkerShowController.workerData.validateData(newData.getID()) <= 0) {
            if (WorkerShowController.workerData.insertData()) {
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
    public void resetData(ActionEvent event) {
        WorkerName.setText(null);
        WorkerProficiencySelector.setValue(null);
        WorkerHour.setText(null);
        WorkerID.requestFocus();
    }

    private void closeWindow() {
        Stage stage = (Stage) WorkerID.getScene().getWindow();
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
