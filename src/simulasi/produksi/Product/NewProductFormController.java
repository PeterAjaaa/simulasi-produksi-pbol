/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simulasi.produksi.Product;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class NewProductFormController implements Initializable {

    @FXML
    private Button ResetButton;
    @FXML
    private Button SubmitButton;
    @FXML
    private TextField ProductName;
    @FXML
    private TextField ProductionTime;
    @FXML
    private ComboBox<?> RequiredToolsSelector;
    @FXML
    private TextField ProductionRate;
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
