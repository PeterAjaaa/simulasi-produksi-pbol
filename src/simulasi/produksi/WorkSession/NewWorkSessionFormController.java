package simulasi.produksi.WorkSession;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID; // Import UUID for generating unique IDs
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import simulasi.produksi.Product.Product;
import simulasi.produksi.Worker.Worker;
import simulasi.produksi.utility.DBConnection;

public class NewWorkSessionFormController implements Initializable {

    @FXML
    private Button ResetButton;
    @FXML
    private Button SubmitButton;
    @FXML
    private ComboBox<Product> ProductMadeSelector;
    @FXML
    private TextField StartTime;
    @FXML
    private ComboBox<Worker> WorkerInvolvedSelector;
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

    private List<Worker> selectedWorkers = new ArrayList<>();

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();

        try {
            dbConnection.bukaKoneksi();
            String query = "SELECT id, name FROM product";
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);
            ResultSet rs = dbConnection.preparedStatement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                products.add(new Product(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
        return products;
    }

    public List<Worker> getAllWorkers() {
        List<Worker> workers = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();

        try {
            dbConnection.bukaKoneksi();
            String query = "SELECT id, name FROM worker";
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);
            ResultSet rs = dbConnection.preparedStatement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                workers.add(new Worker(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }

        return workers;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Product> products = getAllProducts();
        ProductMadeSelector.getItems().addAll(products);

        ProductMadeSelector.setCellFactory(comboBox -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        List<Worker> workers = getAllWorkers();
        WorkerInvolvedSelector.getItems().addAll(workers);
        WorkerInvolvedSelector.setCellFactory(comboBox -> new ListCell<Worker>() {
            @Override
            protected void updateItem(Worker item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        AddWorkerButton.setOnAction(event -> {
            Worker selectedWorker = WorkerInvolvedSelector.getSelectionModel().getSelectedItem();
            if (selectedWorker != null) {
                // Check if the worker is already selected
                if (!selectedWorkers.contains(selectedWorker)) {
                    String currentWorkers = WorkerInvolvedDisplay.getText();
                    if (currentWorkers.isEmpty()) {
                        WorkerInvolvedDisplay.setText(selectedWorker.getName());
                    } else {
                        WorkerInvolvedDisplay.setText(currentWorkers + ", " + selectedWorker.getName());
                    }
                    selectedWorkers.add(selectedWorker);
                    updateWorkerDisplay();
                } else {
                    showAlert("This worker is already added.");
                }
            }
        });
    }

    private void updateWorkerDisplay() {
        String workerNames = selectedWorkers.stream()
                .map(Worker::getName)
                .collect(Collectors.joining(", "));
        WorkerInvolvedDisplay.setText(workerNames);
    }

    @FXML
    private void handleSubmitButton() {
        Product selectedProduct = ProductMadeSelector.getValue();

        if (selectedProduct != null && !selectedWorkers.isEmpty()) {
            String workSessionId = UUID.randomUUID().toString();
            String workerIds = selectedWorkers.stream()
                    .map(Worker::getID)
                    .collect(Collectors.joining(", "));

            boolean isSaved = saveWorkSession(
                    workSessionId,
                    selectedProduct.getID(),
                    workerIds,
                    StartTime.getText(),
                    EndTime.getText()
            );

            if (isSaved) {
                showAlert("Work session saved successfully!"); // Show success message
            }
        } else {
            showAlert("Please select a product and at least one worker");
        }
    }

    private boolean saveWorkSession(String workSessionId, String productId, String workerIds, String startTime, String endTime) {
        DBConnection dbConnection = new DBConnection();
        try {
            dbConnection.bukaKoneksi();

            String query = "INSERT INTO work_session (id, products_made, start_time, end_time, worker_involved) VALUES (?, ?, ?, ?, ?)";
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);

            dbConnection.preparedStatement.setString(1, workSessionId);
            dbConnection.preparedStatement.setString(2, productId);
            dbConnection.preparedStatement.setString(3, startTime);
            dbConnection.preparedStatement.setString(4, endTime);
            dbConnection.preparedStatement.setString(5, workerIds);

            int affectedRows = dbConnection.preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error saving work session: " + e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
