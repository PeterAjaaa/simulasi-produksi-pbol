package simulasi.produksi.WorkSession;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID; // Import UUID for generating unique IDs
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulasi.produksi.Product.Product;
import simulasi.produksi.Worker.Worker;
import simulasi.produksi.utility.DBConnection;
import simulasi.produksi.Constant.*;
import simulasi.produksi.Tool.Tool;
import simulasi.produksi.Tool.ToolDB;

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
    @FXML
    private TextField RequiredToolField;

    private List<Worker> selectedWorkers = new ArrayList<>();
    @FXML
    private TextField EstimatedProductionTimeField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load and set products in the ProductMadeSelector
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

        // Set the action for when a product is selected
        ProductMadeSelector.setOnAction(event -> {
            updateRequiredTool(); // Update the required tool when a product is selected
            updateEstimatedProductionTime(); // Update estimated time when product is selected
        });

        // Load and set workers in the WorkerInvolvedSelector
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

        // Set action for the AddWorkerButton
        AddWorkerButton.setOnAction(event -> {
            Worker selectedWorker = WorkerInvolvedSelector.getSelectionModel().getSelectedItem();
            if (selectedWorker != null) {
                // Create a temporary list of workers to simulate adding the new worker
                List<Worker> tempWorkers = new ArrayList<>(selectedWorkers);
                tempWorkers.add(selectedWorker);

                // Simulate production with the temporary list of workers
                String[] outputAndTime = simulateProduction(ProductMadeSelector.getValue(), tempWorkers, Long.MAX_VALUE); // Use a large number to simulate without time constraints

                // Check if the estimated production time would go under 0 hours
                if (Integer.parseInt(outputAndTime[1]) <= 0) {
                    showAlert("Cannot add this worker. Estimated production time would go under 0 hours.");
                    return; // Exit the method if the condition is met
                }

                // Proceed to add the worker if the estimated time is valid
                if (!selectedWorkers.contains(selectedWorker)) {
                    selectedWorkers.add(selectedWorker);
                    updateWorkerDisplay();
                    updateEstimatedProductionTime(); // Update estimated time when a worker is added
                } else {
                    showAlert("This worker is already added.");
                }
            }
        });

        Worker selectedWorker = WorkerInvolvedSelector.getSelectionModel().getSelectedItem();
        if (selectedWorker != null) {
            // Calculate the estimated production time with the new worker added
            List<Worker> tempWorkers = new ArrayList<>(selectedWorkers);
            tempWorkers.add(selectedWorker);
            String[] outputAndTime = simulateProduction(ProductMadeSelector.getValue(), tempWorkers, Long.MAX_VALUE); // Use a large number to simulate without time constraints

            // Check if the estimated production time would go under 0 hours
            if (Integer.parseInt(outputAndTime[1]) <= 0) {
                showAlert("Cannot add this worker. Estimated production time would go under 0 hours.");
                return;
            }

            if (!selectedWorkers.contains(selectedWorker)) {
                selectedWorkers.add(selectedWorker);
                updateWorkerDisplay();
                updateEstimatedProductionTime(); // Update estimated time when a worker is added
            } else {
                showAlert("This worker is already added.");
            }
        }

        refreshProductList();
        refreshWorkerList();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setHeaderText(null); // Optional: Set header text to null if you don't want a header
        alert.showAndWait();
    }

    private void updateRequiredTool() {
        Product selectedProduct = ProductMadeSelector.getValue();

        // Always disable the RequiredToolField
        RequiredToolField.setDisable(true);

        if (selectedProduct != null) {
            String requiredTool = selectedProduct.getRequiredTool();
            if (requiredTool != null && !requiredTool.isEmpty()) {
                RequiredToolField.setText(requiredTool);
                RequiredToolField.setEditable(false);
                RequiredToolField.setDisable(false);
            } else {
                RequiredToolField.setText(""); // Clear the field if no required tool
                RequiredToolField.setDisable(true);
            }
        } else {
            RequiredToolField.setText(""); // Clear the field if no product is selected
            RequiredToolField.setDisable(true);
        }
    }

    private void updateEstimatedProductionTime() {
        Product selectedProduct = ProductMadeSelector.getValue();

        // Check if all necessary fields are filled
        if (selectedProduct == null || selectedWorkers.isEmpty()) {
            EstimatedProductionTimeField.setText("Data isn't complete, can't calculate");
            return;
        }

        // Calculate production
        String[] outputAndTime = simulateProduction(selectedProduct, selectedWorkers, Long.MAX_VALUE); // Use a large number to simulate without time constraints
        EstimatedProductionTimeField.setText(outputAndTime[1] + " hours");
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();

        try {
            dbConnection.bukaKoneksi();
            // Update the query to include required_tool and production_time
            String query = "SELECT id, name, req_tool, prod_time FROM product"; // Include production_time
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);
            ResultSet rs = dbConnection.preparedStatement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String requiredTool = rs.getString("req_tool"); // Retrieve the required tool
                int productionTime = rs.getInt("prod_time"); // Retrieve the production time

                // Create a new Product object with all required parameters
                products.add(new Product(id, name, requiredTool, productionTime)); // Ensure Product constructor accepts requiredTool and productionTime
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
            String query = "SELECT id, name, proficiency FROM worker";
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);
            ResultSet rs = dbConnection.preparedStatement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String proficiency = rs.getString("proficiency");
                workers.add(new Worker(id, name, proficiency));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }

        return workers;
    }

    private void updateWorkerDisplay() {
        String workerNames = selectedWorkers.stream()
                .map(Worker::getName)
                .collect(Collectors.joining(", "));
        WorkerInvolvedDisplay.setText(workerNames);
    }

    private boolean saveWorkSession(String workSessionId, String productId, List<Worker> workers, String startTime, String endTime) {
        DBConnection dbConnection = new DBConnection();
        try {
            dbConnection.bukaKoneksi();

            // Check if the product exists
            String productCheckQuery = "SELECT COUNT(*) FROM product WHERE id = ?";
            dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(productCheckQuery);
            dbConnection.preparedStatement.setString(1, productId);
            ResultSet rs = dbConnection.preparedStatement.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                showAlert("Product does not exist.");
                return false;
            }

            // If multiple workers, create a separate entry for each worker
            if (workers.size() > 1) {
                for (Worker worker : workers) {
                    String query = "INSERT INTO work_session (id, products_made, start_time, end_time, worker_involved) VALUES (?, ?, ?, ?, ?)";
                    dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);
                    dbConnection.preparedStatement.setString(1, workSessionId + "_" + worker.getID());
                    dbConnection.preparedStatement.setString(2, productId);
                    dbConnection.preparedStatement.setString(3, startTime);
                    dbConnection.preparedStatement.setString(4, endTime);
                    dbConnection.preparedStatement.setString(5, worker.getID());
                    dbConnection.preparedStatement.executeUpdate();
                }
            } else if (workers.size() == 1) {
                // For single worker, use the original approach
                String query = "INSERT INTO work_session (id, products_made, start_time, end_time, worker_involved) VALUES (?, ?, ?, ?, ?)";
                dbConnection.preparedStatement = dbConnection.dbConnection.prepareStatement(query);
                dbConnection.preparedStatement.setString(1, workSessionId);
                dbConnection.preparedStatement.setString(2, productId);
                dbConnection.preparedStatement.setString(3, startTime);
                dbConnection.preparedStatement.setString(4, endTime);
                dbConnection.preparedStatement.setString(5, workers.get(0).getID());
                dbConnection.preparedStatement.executeUpdate();
            } else {
                showAlert("No workers selected.");
                return false;
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error saving work session: " + e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection();
        }
    }

    @FXML
    private void handleOpenProductDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulasi/produksi/Product/ProductShow.fxml"));
            Parent root = loader.load();

            Stage productDetailsStage = new Stage();
            productDetailsStage.setTitle("Product Details");
            productDetailsStage.initModality(Modality.APPLICATION_MODAL);
            productDetailsStage.setScene(new Scene(root));
            productDetailsStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening Product Details window: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenWorkerDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulasi/produksi/Worker/WorkerShow.fxml"));
            Parent root = loader.load();

            Stage workerDetailsStage = new Stage();
            workerDetailsStage.setTitle("Worker Details");
            workerDetailsStage.initModality(Modality.APPLICATION_MODAL);
            workerDetailsStage.setScene(new Scene(root));
            workerDetailsStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening Worker Details window: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenToolDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulasi/produksi/Tool/ToolShow.fxml"));
            Parent root = loader.load();

            Stage workSessionDetailsStage = new Stage();
            workSessionDetailsStage.setTitle("Work Session Details");
            workSessionDetailsStage.initModality(Modality.APPLICATION_MODAL);
            workSessionDetailsStage.setScene(new Scene(root));
            workSessionDetailsStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening Work Session Details window: " + e.getMessage());
        }
    }

    @FXML
    private void handleSubmitButton() {
        Product selectedProduct = ProductMadeSelector.getValue();

        // Check if the selected product is valid
        if (selectedProduct == null) {
            showAlert("Please select a product.");
            return;
        }

        // Check if the product has a valid required tool and production time
        String requiredTool = selectedProduct.getRequiredTool();
        if (requiredTool == null || requiredTool.isEmpty()) {
            showAlert("The selected product does not have a required tool.");
            return;
        }

        int productionTime = selectedProduct.getProductionTime();
        if (productionTime <= 0) {
            showAlert("The selected product must have a valid production time greater than zero.");
            return;
        }

        if (selectedWorkers.isEmpty()) {
            showAlert("Please select at least one worker.");
            return;
        }

        String startTimeStr = StartTime.getText();
        String endTimeStr = EndTime.getText();

        // Validate time format
        if (!isValidTimeFormat(startTimeStr) || !isValidTimeFormat(endTimeStr)) {
            showAlert("Please enter valid time in HH:MM format.");
            return;
        }

        // Calculate total working hours from start and end times
        LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
        long totalWorkingHours = ChronoUnit.HOURS.between(startTime, endTime);

        // Ensure totalWorkingHours is non-negative
        if (totalWorkingHours < 0) {
            showAlert("End time must be after start time.");
            return;
        }

        // Calculate production
        String[] outputAndTime = simulateProduction(selectedProduct, selectedWorkers, totalWorkingHours);
        WorkSessionOutput.setText(outputAndTime[0]);

        // Update the Estimated Production Time field
        EstimatedProductionTimeField.setText(outputAndTime[1] + " hours");

        // Check if production is successful before saving
        if (outputAndTime[0].startsWith("Product finished")) {
            String workSessionId = UUID.randomUUID().toString();

            boolean isSaved = saveWorkSession(
                    workSessionId,
                    selectedProduct.getID(),
                    selectedWorkers, // Pass the entire list of workers
                    startTimeStr,
                    endTimeStr
            );

            if (isSaved) {
                showAlert("Work session saved successfully!");
            } else {
                showAlert("Failed to save work session.");
            }
        } else {
            showAlert("Production was not successful: " + outputAndTime[0]);
        }
    }

    private String[] simulateProduction(Product product, List<Worker> workers, long totalWorkingHours) {
        int productionTime = product.getProductionTime(); // in minutes
        String requiredTool = product.getRequiredTool();

        // Create instances of EquipmentData and Proficiency
        EquipmentData equipmentDataInstance = new EquipmentData();
        Proficiency proficiencyInstance = new Proficiency();

        // Initialize tool efficiency
        double toolEfficiency = 1.0; // Start with 100% efficiency

        // Check if the required tool exists in the equipment data
        if (requiredTool != null && equipmentDataInstance.equipmentData.containsKey(requiredTool)) {
            // Get the predefined efficiency rating for the required tool
            int toolEfficiencyPercentage = equipmentDataInstance.equipmentData.get(requiredTool);
            String toolAvailability = getToolAvailability(requiredTool); // Check tool availability

            // Adjust tool efficiency based on availability
            if ("Maintenance".equalsIgnoreCase(toolAvailability)) {
                toolEfficiencyPercentage /= 2; // Cut efficiency in half if under maintenance
            }

            toolEfficiency = toolEfficiencyPercentage / 100.0; // Convert to a decimal
        } else {
            return new String[]{"Production cannot proceed: Required tool not found in equipment data.", "0"};
        }

        // Calculate total worker efficiency (additive)
        double totalWorkerEfficiency = 0.0;
        for (Worker worker : workers) {
            String proficiencyLevel = worker.getProficiency();
            if (proficiencyLevel != null && proficiencyInstance.proficiency.containsKey(proficiencyLevel)) {
                double workerEfficiency = proficiencyInstance.proficiency.get(proficiencyLevel);
                totalWorkerEfficiency += workerEfficiency; // Sum efficiencies
            }
        }

        // Calculate combined efficiency
        double combinedEfficiency = toolEfficiency * totalWorkerEfficiency; // Use total worker efficiency directly

        // Calculate effective production time
        double effectiveProductionTime = productionTime * (1 - combinedEfficiency);
        int effectiveProductionTimeInHours = (int) Math.ceil(effectiveProductionTime / 60.0);

        // Check if production can be completed within available working hours
        if (effectiveProductionTimeInHours > totalWorkingHours) {
            return new String[]{"No worker available anymore. Workers worked for " + totalWorkingHours + " hours. Product not finished.", "0"};
        } else {
            return new String[]{"Product finished. Finished in " + effectiveProductionTimeInHours + " hours.", String.valueOf(effectiveProductionTimeInHours)};
        }
    }

    private String getToolAvailability(String toolName) {
        ToolDB toolDB = new ToolDB();
        ObservableList<Tool> tools = toolDB.loadData(); // Load all tools from the database

        if (tools != null) {
            for (Tool tool : tools) {
                if (tool.getName().equalsIgnoreCase(toolName)) {
                    return tool.getAvailability(); // Return the availability status of the found tool
                }
            }
        }
        return "Unknown"; // Return "Unknown" if the tool is not found
    }

    private boolean isValidTimeFormat(String time) {
        return time.matches("([01]\\d|2[0-3]):([0-5]\\d)");
    }

    @FXML
    private void handleResetButton() {
        // Clear input fields
        StartTime.clear();
        EndTime.clear();
        WorkerInvolvedDisplay.clear();
        WorkSessionOutput.clear();

        // Reset ComboBox selections
        ProductMadeSelector.getSelectionModel().clearSelection();
        WorkerInvolvedSelector.getSelectionModel().clearSelection();

        // Clear the list of selected workers
        selectedWorkers.clear();

        // Disable the RequiredToolField and clear its text
        RequiredToolField.setText("");
        RequiredToolField.setDisable(true);
    }

    @FXML
    private void handleRandomizeButton() {
        Random random = new Random();

        // Randomly select a product
        Product randomProduct = ProductMadeSelector.getItems().get(random.nextInt(ProductMadeSelector.getItems().size()));
        ProductMadeSelector.getSelectionModel().select(randomProduct);
        updateRequiredTool(); // Update the required tool based on the selected product

        boolean validSelection = false;

        while (!validSelection) {
            // Clear previously selected workers
            selectedWorkers.clear();

            // Randomly select multiple workers
            int numberOfWorkersToSelect = random.nextInt(WorkerInvolvedSelector.getItems().size()) + 1; // Select 1 to max available workers
            List<Worker> allWorkers = new ArrayList<>(WorkerInvolvedSelector.getItems());
            Collections.shuffle(allWorkers); // Shuffle the list to randomize selection

            for (int i = 0; i < numberOfWorkersToSelect && i < allWorkers.size(); i++) {
                Worker randomWorker = allWorkers.get(i);
                selectedWorkers.add(randomWorker);
            }

            // Update the worker display
            updateWorkerDisplay();

            // Simulate production to check estimated production time
            String[] outputAndTime = simulateProduction(randomProduct, selectedWorkers, Long.MAX_VALUE); // Use a large number to simulate without time constraints

            // Check if the estimated production time would go under 0 hours
            if (Integer.parseInt(outputAndTime[1]) > 0) {
                validSelection = true; // Valid selection found
            } else {
                showAlert("Randomly selected workers would result in negative estimated production time. Randomizing again...");
            }
        }

        // Randomly generate start and end times
        int startHour = random.nextInt(24);
        int startMinute = random.nextInt(60);
        int endHour = startHour + random.nextInt(3) + 1; // Ensure end time is after start time
        int endMinute = random.nextInt(60);

        // Format times to HH:mm
        String startTime = String.format("%02d:%02d", startHour, startMinute);
        String endTime = String.format("%02d:%02d", endHour % 24, endMinute); // Wrap around if endHour exceeds 23

        StartTime.setText(startTime);
        EndTime.setText(endTime);

        // Update estimated production time based on randomized inputs
        updateEstimatedProductionTime();
    }

    private void refreshProductList() {
        List<Product> products = getAllProducts(); // Fetch the latest products from the database
        ProductMadeSelector.getItems().clear(); // Clear the current items
        ProductMadeSelector.getItems().addAll(products); // Add the updated products
    }

    private void refreshWorkerList() {
        List<Worker> workers = getAllWorkers(); // Fetch the latest workers from the database
        WorkerInvolvedSelector.getItems().clear(); // Clear the current items
        WorkerInvolvedSelector.getItems().addAll(workers); // Add the updated workers
    }
}
