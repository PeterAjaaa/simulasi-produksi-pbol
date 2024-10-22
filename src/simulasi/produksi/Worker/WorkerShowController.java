/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Worker;

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

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class WorkerShowController implements Initializable {
    @FXML
    private TableView<Worker> WorkerTableView;
    
    public static WorkerDB workerData = new WorkerDB();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showData();
    }

    private void showData() {
        ObservableList<Worker> data = NewWorkerFormController.workerData.loadData();
        if (data != null) {
            WorkerTableView.getColumns().clear();
            WorkerTableView.getItems().clear();
            TableColumn col = new TableColumn("ID");
            col.setCellValueFactory(new PropertyValueFactory<Worker, String>("ID"));
            WorkerTableView.getColumns().addAll(col);
            col = new TableColumn("Name");
            col.setCellValueFactory(new PropertyValueFactory<Worker, String>("name"));
            WorkerTableView.getColumns().addAll(col);
            col = new TableColumn("Proficiency");
            col.setCellValueFactory(new PropertyValueFactory<Worker, String>("proficiency"));
            WorkerTableView.getColumns().addAll(col);
            col = new TableColumn("Work Hours");
            col.setCellValueFactory(new PropertyValueFactory<Worker, String>("work_hours"));
            WorkerTableView.getColumns().addAll(col);
            col = new TableColumn("Availability");
            col.setCellValueFactory(new PropertyValueFactory<Worker, Integer>("availability"));
            WorkerTableView.getColumns().addAll(col);
            WorkerTableView.setItems(data);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Data kosong", ButtonType.OK);
            alert.showAndWait();
            WorkerTableView.getScene().getWindow().hide();
        }
    }

    @FXML
    private void addData(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewWorkerForm.fxml"));
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
        Worker selectedData = WorkerTableView.getSelectionModel().getSelectedItem();
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirm Deletion", ButtonType.YES, ButtonType.NO);
        a.showAndWait();
        if (a.getResult() == ButtonType.YES) {
            if (WorkerShowController.workerData.deleteData(selectedData.getID())) {
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
        Worker selectedData = WorkerTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewWorkerForm.fxml"));
            Parent root = (Parent) loader.load();
            NewWorkerFormController newWorkerFormController = (NewWorkerFormController) loader.getController();
            newWorkerFormController.getWorkerID().setEditable(false);
            newWorkerFormController.getWorkerID().setDisable(true);
            newWorkerFormController.execute(selectedData);
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
