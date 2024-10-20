/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.WorkSession;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class NewWorkSessionFormController implements Initializable {

    @FXML
    private Button ResetButton;
    @FXML
    private Button SubmitButton;
    @FXML
    private ComboBox<?> ProductMadeSelector;
    @FXML
    private TextField StartTime;
    @FXML
    private ComboBox<?> WorkerInvolvedSelector;
    @FXML
    private TextField EndTime;
    @FXML
    private Button AddWorkerButton;
    @FXML
    private TextField WorkerInvolvedDisplay;
    @FXML
    private TextArea WorkSessionOutput;
    @FXML
    private MenuBar NavigationMenu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
