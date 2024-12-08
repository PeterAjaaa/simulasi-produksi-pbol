package simulasi.produksi.WorkSession;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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

    public String getWorkerNameById(String workerId) {
        String workerName = "";
        DBConnection dbConnection = new DBConnection();

        try {
            dbConnection.bukaKoneksi();
            String query = "SELECT name FROM worker WHERE id = ?";
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);
            dbConnection.preparedStatement.setString(1, workerId);

            ResultSet rs = dbConnection.preparedStatement.executeQuery();
            if (rs.next()) {
                workerName = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }

        return workerName;
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
            List<String> workerIds = selectedWorkers.stream()
                    .map(Worker::getID)
                    .collect(Collectors.toList());
            saveWorkSession(
                    selectedProduct.getID(),
                    workerIds,
                    StartTime.getText(),
                    EndTime.getText()
            );
        } else {
            showAlert("Please select a product and at least one worker");
        }
    }

    private void saveWorkSession(String productId, List<String> workerIds, String startTime, String endTime) {
        DBConnection dbConnection = new DBConnection();
        try {
            dbConnection.bukaKoneksi();

            String query = "INSERT INTO work_session (product_made, start_time, end_time) VALUES (?, ?, ?)";
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            dbConnection.preparedStatement.setString(1, productId);
            dbConnection.preparedStatement.setString(2, startTime);
            dbConnection.preparedStatement.setString(3, endTime);

            int affectedRows = dbConnection.preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = dbConnection.preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int workSessionId = generatedKeys.getInt(1);
                    insertWorkerWorkSession(workSessionId, workerIds);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error saving work session: " + e.getMessage());
        } finally {
            dbConnection.closeConnection();
        }
    }

    private void insertWorkerWorkSession(int workSessionId, List<String> workerIds) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        String query = "INSERT INTO worker_work_sessions (work_session_id, worker_id) VALUES (?, ?)";

        for (String workerId : workerIds) {
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);
            dbConnection.preparedStatement.setInt(1, workSessionId);
            dbConnection.preparedStatement.setString(2, workerId);
            dbConnection.preparedStatement.executeUpdate();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
