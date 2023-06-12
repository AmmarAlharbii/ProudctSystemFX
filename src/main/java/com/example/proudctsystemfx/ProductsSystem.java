package com.example.proudctsystemfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsSystem extends Application {

    static Secure secureConn = new Secure(); //secure Obj to accses Database


    private String Information = " ID:441001021\n Group number :1 \n Name : Ammar abdulaziz Alharbi\nEmail: s441001021@st.uqu.edu.sa";//my Info


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Products System");//Stage title


        // Create MenuBar
        MenuBar menuBar = new MenuBar(); //menu bar
        Menu productsMenu = new Menu("Products"); //root Proudcts
        Menu proudctOptions = new Menu("Product");//First branch
        MenuItem addMenuItem = new MenuItem("Add");// Add branch from Product
        MenuItem searchMenuItem = new MenuItem("Search");// Search branch from Product
        MenuItem deleteMenuItem = new MenuItem("Delete");// Delete branch from Product
        MenuItem exitMenuItem = new MenuItem("Exit");// //Seconed branch


        proudctOptions.getItems().addAll(addMenuItem, searchMenuItem, deleteMenuItem);//add three options to Product
        productsMenu.getItems().addAll(proudctOptions, exitMenuItem);//make to branches
        menuBar.getMenus().add(productsMenu);// add to menu bar

        // Set event handlers for menu items
        addMenuItem.setOnAction(event -> addProductScene());
        searchMenuItem.setOnAction(event -> searchProductsScene());
        deleteMenuItem.setOnAction(event -> deleteProductScene());
        exitMenuItem.setOnAction(event -> {
            System.exit(0);
        });


        // Create GridPane and add TableView and Status Label
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.add(new Label(Information), 0, 0); // my Info


        // Create main scene and set MenuBar and GridPane
        Scene scene = new Scene(new VBox(menuBar, gridPane), 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }// end start methoed

    private void addProductScene() {
        Stage addStage = new Stage();//create Stage
        Label statusMessage = new Label();//Message for the state
        statusMessage.setFont(Font.font(statusMessage.getFont().getFamily(), FontWeight.BOLD, statusMessage.getFont().getSize()));//make it Bold
        addStage.setTitle("Add Product");//set Title for Stage

        ChoiceBox<String> typeChoiceBox = new ChoiceBox<>();//Crete choice box
        typeChoiceBox.getItems().addAll("Tv", "Mobile", "Laptop"); // all available choices
        TextField modelField = new TextField();// model field
        TextField priceField = new TextField();// price field
        Slider slider = new Slider(0, 10, 0); //Slider for count
        DatePicker datePicker = new DatePicker();//DatePicker to take date
        Button saveButton = new Button("Save"); //save button
        slider.setShowTickMarks(true);//display tick mark
        slider.setShowTickLabels(true); //show labels
        slider.setMajorTickUnit(1);// per 1
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);

        // Create GridPane and add input fields and save button
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));//padding
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(new Label("Type:"), 0, 0);
        gridPane.add(typeChoiceBox, 1, 0);
        gridPane.add(new Label("Model:"), 0, 1);
        gridPane.add(modelField, 1, 1);
        gridPane.add(new Label("Price:"), 0, 2);
        gridPane.add(priceField, 1, 2);
        gridPane.add(new Label("Count:"), 0, 3);
        gridPane.add(slider, 1, 3);
        gridPane.add(new Label("Delivery Date:"), 0, 4);
        gridPane.add(datePicker, 1, 4);
        gridPane.add(saveButton, 0, 5);
        gridPane.add(statusMessage, 0, 6);
        // Set a fixed size for the status message label
        statusMessage.setMinWidth(300);
        statusMessage.setMaxWidth(300);

        // Set event handler for save button
        saveButton.setOnAction(event -> {
            // Validate input fields

            try {
                // declare all variables for Validateing
                float price = Float.parseFloat(priceField.getText());//price assaign
                int count = (int) slider.getValue();//number of count
                String type = typeChoiceBox.getValue(); //type
                String priceText = priceField.getText();//price text for check if empty
                LocalDate date = datePicker.getValue();// Date
                String model = modelField.getText();//model

                List<String> fields = new ArrayList<>();// list of empty fields


                if (type == null || type.isEmpty()) {//if the field empty will add to the list
                    fields.add("Type");
                }
                if (model.isEmpty()) {
                    fields.add("Model");
                }
                if (priceText.isEmpty()) {
                    fields.add("Price");
                }
                if (date == null) {
                    fields.add("DeliveryDate");
                }

                if (!fields.isEmpty()) { //if the list not empty
                    String errorMessage = "Please fill the following fields: " + String.join(", ", fields);// assign the empty fields in message
                    statusMessage.setText(errorMessage);
                    statusMessage.setTextFill(Color.RED);
                    return;
                }
                // Validate price
                if (price < 1) { // if the price = 0 will display meassage
                    statusMessage.setText("Price should be greater than 0.");
                    statusMessage.setTextFill(Color.RED);
                    return;
                }

                // Validate count
                if (count < 1) {
                    statusMessage.setText("Count should be greater than 0.");
                    statusMessage.setTextFill(Color.RED);
                    return;
                }


                // Insert the new product data into the database
                Connection connection = secureConn.secureConnection();//create Connection
                String insertQuery = "INSERT INTO " + secureConn.getTableName() + " (Type, Model, Price, Count, DeliveryDate) VALUES (?, ?, ?, ?, ?)"; //Query
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);//create PreparedStatement
                preparedStatement.setString(1, typeChoiceBox.getValue());
                preparedStatement.setString(2, modelField.getText());
                preparedStatement.setFloat(3, price);
                preparedStatement.setInt(4, count);
                preparedStatement.setDate(5, Date.valueOf(datePicker.getValue()));
                preparedStatement.executeUpdate();
                preparedStatement.close();
                connection.close();

                statusMessage.setText("Product added successfully.");//success message
                statusMessage.setTextFill(Color.GREEN);

                // Reset the input data in the form to their initial values
                typeChoiceBox.setValue(null);
                modelField.setText("");
                priceField.setText("");
                slider.setValue(0);
                datePicker.setValue(null);
            } catch (NumberFormatException e) { // if the user dont enter any data
                statusMessage.setText("Please fill all fields.");
                statusMessage.setTextFill(Color.RED);
            } catch (SQLException e) { //error while adding
                e.printStackTrace();
                statusMessage.setText("Error occurred while adding the product.");
                statusMessage.setTextFill(Color.RED);
            }
        });//Event end

        // Create and set scene for addStage
        Scene scene = new Scene(gridPane, 600, 400);// scene
        addStage.setScene(scene);
        addStage.show();
    }// end addProudctScene


    private void searchProductsScene() {
        Stage searchStage = new Stage();//stage
        Label statusMessage = new Label();//Message of state
        statusMessage.setFont(Font.font(statusMessage.getFont().getFamily(), FontWeight.BOLD, statusMessage.getFont().getSize()));//make it Bold
        searchStage.setTitle("Search Products");//title of the Stage

        // Create search criteria input fields and search button
        TextField typeField = new TextField();//for type
        TextField modelField = new TextField();//for model
        Button searchButton = new Button("Search");//search button
        Button refreshButton = new Button("Refresh");//RefreshButoon
        TableView<Product> searchTableView = new TableView<>();// the table


        // Set up columns in the TableView
        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID"); //create id coulmn
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));//set value

        TableColumn<Product, String> typeColumn = new TableColumn<>("Type");//create Type coulmn
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));//set value

        TableColumn<Product, String> modelColumn = new TableColumn<>("Model");//create Model coulmn
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));//set value

        TableColumn<Product, Float> priceColumn = new TableColumn<>("Price");//create Price coulmn
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));//set value

        TableColumn<Product, Integer> countColumn = new TableColumn<>("Count");//create Count coulmn
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));//set value

        TableColumn<Product, String> deliveryDateColumn = new TableColumn<>("Delivery Date");//create Date coulmn
        deliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));//set value

        searchTableView.getColumns().addAll(idColumn, typeColumn, modelColumn, priceColumn, countColumn, deliveryDateColumn);//set all coulmes

        // Set column resizing and sorting
        searchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Set event handler for search button
        searchButton.setOnAction(event -> {
            String typeCriteria = typeField.getText();
            String modelCriteria = modelField.getText();

            // Perform the search and display the results in the table
            ObservableList<Product> searchResults = searchProducts(typeCriteria, modelCriteria);
            searchTableView.setItems(searchResults);

            if (searchResults.isEmpty()) {//check if there  any records
                statusMessage.setText("No records available for this search criteria!");// if no this measssage will display
                statusMessage.setTextFill(Color.RED);//color


            } else {
                statusMessage.setText("");// if empty
            }
        });//Event

        // Set event handler for refresh button
        refreshButton.setOnAction(event -> { //Refresh Event
            //will display all recoreds in the data base
            typeField.setText("");
            modelField.setText("");
            searchTableView.setItems(searchProducts("", ""));
            statusMessage.setText("");
        });//End refresh Event

        // Create GridPane and add search input fields, search button, refresh button, and search TableView
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(new Label("Type:"), 0, 0);
        gridPane.add(typeField, 1, 0);
        gridPane.add(new Label("Model:"), 2, 0);
        gridPane.add(modelField, 3, 0);
        gridPane.add(searchButton, 4, 0);
        gridPane.add(refreshButton, 5, 0);
        gridPane.add(statusMessage, 0, 1, 6, 1);
        gridPane.add(searchTableView, 0, 2, 6, 1);

        // Create and set scene for searchStage
        Scene scene = new Scene(gridPane);
        searchStage.setScene(scene);
        searchStage.show();
    }


    private ObservableList<Product> searchProducts(String typeCriteria, String modelCriteria) { //this methoed will search in Data base
        Label statusMessage = new Label();
        statusMessage.setFont(Font.font(statusMessage.getFont().getFamily(), FontWeight.BOLD, statusMessage.getFont().getSize()));
        ObservableList<Product> searchResults = FXCollections.observableArrayList();//list of result will return

        try {
            Connection connection = secureConn.secureConnection();
            String searchQuery; //Query
            PreparedStatement preparedStatement;//intial PreparedStatement

            if (typeCriteria.isEmpty() && modelCriteria.isEmpty()) {
                // Show all table records if both criteria are empty
                searchQuery = "SELECT * FROM " + secureConn.getTableName();
                preparedStatement = connection.prepareStatement(searchQuery);
            } else {
                // Search by type or model criteria
                searchQuery = "SELECT * FROM " + secureConn.getTableName() + " WHERE Type LIKE ? AND Model LIKE ?";
                preparedStatement = connection.prepareStatement(searchQuery);
                preparedStatement.setString(1, "%" + typeCriteria + "%");
                preparedStatement.setString(2, "%" + modelCriteria + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) { //this while loop for assaign the result in List
                int id = resultSet.getInt("ID");
                String type = resultSet.getString("Type");
                String model = resultSet.getString("Model");
                float price = resultSet.getFloat("Price");
                int count = resultSet.getInt("Count");
                String deliveryDate = String.valueOf(resultSet.getDate("DeliveryDate").toLocalDate());

                Product product = new Product(id, type, model, price, count, deliveryDate);
                searchResults.add(product);//assaign in the list
            }//end while

            resultSet.close();//resultSet close
            preparedStatement.close();//preparedStatement close
            connection.close();// connection close
        } catch (SQLException e) { // if there error while searching
            e.printStackTrace();
            statusMessage.setText("Error occurred while searching for products.");
        }

        return searchResults;//return list
    }// end SearchProudct


    private void deleteProductScene() {
        Stage deleteStage = new Stage();//create stage
        Label statusMessage = new Label();//Message of state
        statusMessage.setFont(Font.font(statusMessage.getFont().getFamily(), FontWeight.BOLD, statusMessage.getFont().getSize()));//make it Bold
        deleteStage.setTitle("Delete Product");//set title Stage

        // Create ID input field and delete button
        TextField idField = new TextField();// ID field
        Button deleteButton = new Button("Delete");//delete Button

        // Set event handler for delete button
        deleteButton.setOnAction(event -> {
            String idText = idField.getText();

            if (idText.isEmpty()) {// if the user enter empty field
                statusMessage.setText("You must enter an ID number.");
                statusMessage.setTextFill(Color.RED);
            } else {
                int productId = Integer.parseInt(idText); //assign id

                // Check if the product ID exists in the products table
                boolean productExists = checkIdExists(productId);

                if (productExists) { //if true
                    // Display confirmation dialog
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION); //alert dilog for confrmation
                    confirmationDialog.setTitle("Confirm Deletion");//title
                    confirmationDialog.setHeaderText(null);
                    confirmationDialog.setContentText("Are you sure you want to delete this product?");//content

                    // Customize the button types
                    ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);//confirm button make it more dark
                    ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);//cancel button
                    confirmationDialog.getButtonTypes().setAll(confirmButton, cancelButton);//assaign buttons

                    Optional<ButtonType> result = confirmationDialog.showAndWait();// Display confirmation dialog and wait for user input

                    if (result.isPresent() && result.get() == confirmButton) {// if the user click confirm
                        try {
                            // Delete the product from the database
                            Connection connection = secureConn.secureConnection();
                            String deleteQuery = "DELETE FROM " + secureConn.getTableName() + " WHERE ID = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                            preparedStatement.setInt(1, productId);
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                            connection.close();

                            statusMessage.setText("Product deleted successfully."); //success message
                            statusMessage.setTextFill(Color.GREEN);
                            idField.setText(null);

                        } catch (SQLException e) {
                            //if there any error in deleteing

                            e.printStackTrace();
                            statusMessage.setText("Error occurred while deleting the product.");//error message
                            statusMessage.setTextFill(Color.RED);
                        }

                    } else {
                        statusMessage.setText("Deletion canceled.");//canceled message
                        statusMessage.setTextFill(Color.RED);

                    }
                } else {
                    statusMessage.setText("Product ID does not exist.");// if the id doesn't found in the database
                    statusMessage.setTextFill(Color.RED);

                }

            }
        });//Event end


        // Create GridPane and add ID input field and delete button
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(new Label("Product ID:"), 0, 0);
        gridPane.add(idField, 1, 0);
        gridPane.add(deleteButton, 0, 1);
        gridPane.add(statusMessage, 0, 2);

        // Set a fixed size for the status label
        statusMessage.setMinWidth(200);
        statusMessage.setMaxWidth(200);

        // Create and set scene for deleteStage
        Scene scene = new Scene(gridPane, 400, 200);
        deleteStage.setScene(scene);
        deleteStage.show();
    }//deleteProductScene


    private boolean checkIdExists(int productId) { //check if the ID in database
        try {
            Connection connection = secureConn.secureConnection();//create connection
            String searchQuery = "SELECT * FROM " + secureConn.getTableName() + " WHERE ID = ?";//search
            PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);//create PreparedStatement
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean productExists = resultSet.next();//assign true if there

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return productExists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // if the id not in the database
        }
    }


}
