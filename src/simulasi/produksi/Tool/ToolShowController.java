/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Tool;

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
import javafx.scene.control.MenuBar;
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
public class ToolShowController implements Initializable {

    @FXML
    private MenuBar NavigationMenu;
    @FXML
    private TableView<Tool> ToolTableView;

    public static ToolDB toolData = new ToolDB();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showData();
    }

    private void showData() {
        ObservableList<Tool> data = NewToolFormController.toolData.loadData();
        if (data != null) {
            ToolTableView.getColumns().clear();
            ToolTableView.getItems().clear();
            TableColumn col = new TableColumn("ID");
            col.setCellValueFactory(new PropertyValueFactory<Tool, String>("ID"));
            ToolTableView.getColumns().addAll(col);
            col = new TableColumn("Name");
            col.setCellValueFactory(new PropertyValueFactory<Tool, String>("name"));
            ToolTableView.getColumns().addAll(col);
            col = new TableColumn("Type");
            col.setCellValueFactory(new PropertyValueFactory<Tool, String>("type"));
            ToolTableView.getColumns().addAll(col);
            col = new TableColumn("Availability");
            col.setCellValueFactory(new PropertyValueFactory<Tool, String>("availability"));
            col = new TableColumn("Efficiency");
            col.setCellValueFactory(new PropertyValueFactory<Tool, Integer>("efficiency"));
            ToolTableView.getColumns().addAll(col);
            ToolTableView.setItems(data);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Data kosong", ButtonType.OK);
            alert.showAndWait();
            ToolTableView.getScene().getWindow().hide();
        }
    }

    @FXML
    public void addData(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewToolForm.fxml"));
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
        Tool selectedData = ToolTableView.getSelectionModel().getSelectedItem();
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirm Deletion", ButtonType.YES, ButtonType.NO);
        a.showAndWait();
        if (a.getResult() == ButtonType.YES) {
            if (ToolShowController.toolData.deleteData(selectedData.getID())) {
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
        Tool selectedData = ToolTableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewToolForm.fxml"));
            Parent root = (Parent) loader.load();
            NewToolFormController newToolController = (NewToolFormController) loader.getController();
            newToolController.getToolID().setEditable(false);
            newToolController.getToolID().setDisable(true);
            newToolController.execute(selectedData);
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
