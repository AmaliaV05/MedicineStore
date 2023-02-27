package AmaliaV05;

import domain.ClientCardNumberWithNumberOfTransactions;
import domain.Medicine;
import domain.MedicinesWithNumberOfTransactions;
import domain.Transaction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import service.MedicineService;
import service.TransactionService;
import service.UndoRedoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyException;
import java.time.LocalDateTime;

public class Controller {
    public TextField txtSearchMedicine;
    public TextField txtMedicine;
    public TextField txtProducer;
    public TextField txtPrice;
    public TextField txtStock;
    public CheckBox chkIsOTC;
    public Button btnAddMedicine;
    public Button btnUpdateMedicine;
    public Button btnDeleteMedicine;
    public TableView<Medicine> tblMedicine;
    public TableColumn<Medicine, Integer> colMedicineId;
    public TableColumn<Medicine, String> colMedicineName;
    public TableColumn<Medicine, String> colMedicineProducer;
    public TableColumn<Medicine, BigDecimal> colMedicinePrice;
    public TableColumn<Medicine, Boolean> colMedicineIsOTC;
    public TableColumn<Medicine, Integer> colMedicineStock;
    public Label lblOrderMedicinesByTransactions;
    public Button btnOrderMedicinesByTransactions;
    public TableView<MedicinesWithNumberOfTransactions> tblOrderMedicinesByTransactions;
    public TableColumn<MedicinesWithNumberOfTransactions, Integer> colMedicineIdOrderByTransactions;
    public TableColumn<MedicinesWithNumberOfTransactions, String> colMedicineNameOrderByTransactions;
    public TableColumn<MedicinesWithNumberOfTransactions, Integer> colTransactionsNumber;
    public Label lblRaiseMedicinePrice;
    public Button btnRaiseMedicinePrice;
    public TextField txtRaiseMedicinePrice;
    public TextField txtRaiseMedicinePricePercentage;
    public TextField txtSearchTransaction;
    public TableView<Transaction> tblTransaction;
    public TableColumn<Transaction, Integer> colTransactionId;
    public TableColumn<Transaction, Integer> colClientCard;
    public TableColumn<Transaction, Integer> colMedicineTransaction;
    public TableColumn<Transaction, Integer> colMedicineQuantityTransaction;
    public TableColumn<Transaction, LocalDateTime> colTransactionDateTime;
    public TextField txtClientCard;
    public TextField txtMedicineId;
    public TextField txtQuantity;
    public DatePicker dpTransaction;
    public Button btnAddTransaction;
    public Button btnUpdateTransaction;
    public Button btnDeleteTransaction;
    public Button btnOrderClientCardsByTransactions;
    public TableView<ClientCardNumberWithNumberOfTransactions> tblOrderClientCardsByTransactions;
    public TableColumn<ClientCardNumberWithNumberOfTransactions, Integer> colClientCardOrderByTransactions;
    public TableColumn<ClientCardNumberWithNumberOfTransactions, Integer> colNumberTransactionsOrderByTransactions;
    public Button btnUndoMedicineOperation;
    public Button btnRedoMedicineOperation;
    public Button btnUndoTransactionOperation;
    public Button btnRedoTransactionOperation;

    private UndoRedoService undoRedoService;
    private MedicineService medicineService;
    private TransactionService transactionService;

    private final ObservableList<Medicine> medicinesObservableList = FXCollections.observableArrayList();
    private final ObservableList<Transaction> transactionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<MedicinesWithNumberOfTransactions> medicinesByNumberOfTransactionsObservableList = FXCollections.observableArrayList();
    private final ObservableList<ClientCardNumberWithNumberOfTransactions> clientCardsByNumberOfTransactionsObservableList = FXCollections.observableArrayList();

    public void setServices(UndoRedoService undoRedoService, MedicineService medicineService, TransactionService transactionService) {
        this.undoRedoService = undoRedoService;
        this.medicineService = medicineService;
        this.transactionService = transactionService;
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            editableMedicineTable();
            searchMedicine();
            initializeTableOrderDrugsByTransactionsCount();

            editableTransactionTable();
            searchTransaction();
            initializeTableOrderClientCardsByTransactionsCount();

            addToolTips();
        });
    }

    public void searchMedicine() {
        medicinesObservableList.clear();
        medicinesObservableList.addAll(medicineService.getMedicines());
        FilteredList<Medicine> filteredData = new FilteredList<>(medicinesObservableList, p -> true);
        txtSearchMedicine.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(medicine -> {
                    if(newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (medicine.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return (medicine.getProducer().toLowerCase().contains(lowerCaseFilter));
                }));
        SortedList<Medicine> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblMedicine.comparatorProperty());
        tblMedicine.setItems(sortedData);
    }

    public void onAddMedicine(ActionEvent actionEvent) {
        try {
            String name = txtMedicine.getText();
            String producer = txtProducer.getText();
            BigDecimal price = new BigDecimal(txtPrice.getText());
            boolean isOTC = chkIsOTC.isSelected();
            int stock = Integer.parseInt(txtStock.getText());

            medicineService.addMedicine(name, producer, price, isOTC, stock);
            medicinesObservableList.clear();
            medicinesObservableList.addAll(medicineService.getMedicines());

            colMedicineId.setCellValueFactory(new PropertyValueFactory<>("idEntity"));
            colMedicineName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colMedicineProducer.setCellValueFactory(new PropertyValueFactory<>("producer"));
            colMedicinePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colMedicineIsOTC.setCellValueFactory(new PropertyValueFactory<>("oTC"));
            colMedicineStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

            tblMedicine.refresh();
        } catch(RuntimeException | KeyException | IOException rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void onUpdateMedicine(ActionEvent actionEvent) {
        try {
            Medicine medicine = tblMedicine.getSelectionModel().getSelectedItem();

            int id = medicine.getIdEntity();
            String name = medicine.getName();
            String producer = medicine.getProducer();
            BigDecimal price = medicine.getPrice();
            boolean isOTC = medicine.isOTC();
            int stock = medicine.getStock();

            medicineService.updateMedicine(id, name, producer, price, isOTC, stock);
            medicinesObservableList.clear();
            medicinesObservableList.addAll(medicineService.getMedicines());
            tblMedicine.refresh();
        } catch(Exception rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void onDeleteMedicine(ActionEvent actionEvent) {
        try {
            Medicine medicine = tblMedicine.getSelectionModel().getSelectedItem();
            int id = medicine.getIdEntity();
            medicineService.deleteMedicine(id);

            medicinesObservableList.clear();
            medicinesObservableList.addAll(medicineService.getMedicines());
            tblMedicine.refresh();
        } catch (Exception rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void editableMedicineTable() {
        medicinesObservableList.addAll(medicineService.getMedicines());

        tblMedicine.setEditable(true);

        colMedicineId.setCellValueFactory(new PropertyValueFactory<>("idEntity")
        );
        colMedicineName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMedicineName.setCellFactory(TextFieldTableCell.forTableColumn());
        colMedicineName.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setName(t.getNewValue())
        );
        colMedicineProducer.setCellValueFactory(new PropertyValueFactory<>("producer"));
        colMedicineProducer.setCellFactory(TextFieldTableCell.forTableColumn());
        colMedicineProducer.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setProducer(t.getNewValue())
        );
        colMedicinePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colMedicinePrice.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        colMedicinePrice.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setPrice(t.getNewValue())
        );
        colMedicineIsOTC.setCellValueFactory(new PropertyValueFactory<>("oTC"));
        colMedicineIsOTC.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        colMedicineIsOTC.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setOTC(t.getNewValue())
        );
        colMedicineStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colMedicineStock.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colMedicineStock.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setStock(t.getNewValue())
        );
        tblMedicine.setItems(medicinesObservableList);
    }

    public void onUndoMedicineOperation(ActionEvent actionEvent) {
        try {
            medicineService.undoMedicineOperation();

            medicinesObservableList.clear();
            medicinesObservableList.addAll(medicineService.getMedicines());

            tblMedicine.refresh();
        } catch (RuntimeException rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void onRedoMedicineOperation(ActionEvent actionEvent) {
        try {
            medicineService.redoMedicineOperation();

            medicinesObservableList.clear();
            medicinesObservableList.addAll(medicineService.getMedicines());

            tblMedicine.refresh();
        } catch (RuntimeException rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void initializeTableOrderDrugsByTransactionsCount() {
        colMedicineIdOrderByTransactions.setCellValueFactory(new PropertyValueFactory<>("idMedicine"));
        colMedicineNameOrderByTransactions.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colTransactionsNumber.setCellValueFactory(new PropertyValueFactory<>("numberOfTransactions"));
    }

    public void onGetOrderedMedicinesByTransactions(ActionEvent actionEvent) {
        medicinesByNumberOfTransactionsObservableList.clear();
        medicinesByNumberOfTransactionsObservableList.addAll(medicineService.getMedicinesOrderedByNumberOfTransactions());
        tblOrderMedicinesByTransactions.setItems(medicinesByNumberOfTransactionsObservableList);
        tblOrderMedicinesByTransactions.refresh();
    }

    public void onRaiseMedicinePrice(ActionEvent actionEvent) {
        try {
            BigDecimal price = new BigDecimal(txtRaiseMedicinePrice.getText());
            BigDecimal percentage = new BigDecimal(txtRaiseMedicinePricePercentage.getText());
            medicineService.increasePrice(percentage, price);

            medicinesObservableList.clear();
            medicinesObservableList.addAll(medicineService.getMedicines());

            tblMedicine.refresh();
        } catch (RuntimeException rex) {
            Common.showValidationError(rex.getMessage());
        } catch (KeyException | IOException keyException) {
            keyException.printStackTrace();
        }
    }

    public void addToolTips() {
        lblOrderMedicinesByTransactions.setTooltip(
                new Tooltip("Order medicines by number of transactions in descending order ."));
        lblRaiseMedicinePrice.setTooltip(
                new Tooltip("Find all medicines with prices under " +
                        "given value and raise prices by given percentage.")
        );
    }

    public void searchTransaction() {
        transactionsObservableList.clear();
        transactionsObservableList.addAll(transactionService.getTransactions());
        FilteredList<Transaction> filteredData = new FilteredList<>(transactionsObservableList, p -> true);
        txtSearchTransaction.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(transaction -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String searchText = newValue.toLowerCase();
                    return String.valueOf(transaction.getClientCardNumber()).toLowerCase().contains(searchText);
                }));
        dpTransaction.valueProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(transaction -> {
                    if (newValue == null) {
                        return true;
                    }
                    return newValue.isEqual(transaction.getDateTime().toLocalDate());
                }));
        SortedList<Transaction> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblTransaction.comparatorProperty());
        tblTransaction.setItems(sortedData);
    }

    public void onAddTransaction(ActionEvent actionEvent) {
        try {
            int medicineId = Integer.parseInt(txtMedicineId.getText());
            int clientCardNumber = Integer.parseInt(txtClientCard.getText());
            int quantity = Integer.parseInt(txtQuantity.getText());

            transactionService.addTransaction(clientCardNumber, quantity, medicineId);
            transactionsObservableList.clear();
            transactionsObservableList.addAll(transactionService.getTransactions());

            colTransactionId.setCellValueFactory(new PropertyValueFactory<>("idEntity"));
            colMedicineId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
            colClientCard.setCellValueFactory(new PropertyValueFactory<>("clientCardNumber"));
            colMedicineQuantityTransaction.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colTransactionDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        } catch(RuntimeException | KeyException | IOException rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void onUpdateTransaction(ActionEvent actionEvent) {
        try {
            Transaction transaction = tblTransaction.getSelectionModel().getSelectedItem();

            int idTransaction = transaction.getIdEntity();
            int medicineId = transaction.getMedicineId();
            int clientCardNumber = transaction.getClientCardNumber();
            int quantity = transaction.getQuantity();
            LocalDateTime dateTime = transaction.getDateTime();

            transactionService.updateTransaction(idTransaction, clientCardNumber, quantity, dateTime,  medicineId);
            transactionsObservableList.clear();
            transactionsObservableList.addAll(transactionService.getTransactions());
            tblTransaction.refresh();
        } catch(RuntimeException | KeyException | IOException rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void onDeleteTransaction(ActionEvent actionEvent) {
        try {
            Transaction transaction = tblTransaction.getSelectionModel().getSelectedItem();
            int id = transaction.getIdEntity();
            transactionService.deleteTransaction(id);

            transactionsObservableList.clear();
            transactionsObservableList.addAll(transactionService.getTransactions());
            tblTransaction.refresh();
        } catch (RuntimeException | KeyException | IOException rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void editableTransactionTable() {
        transactionsObservableList.addAll(transactionService.getTransactions());
        tblTransaction.setEditable(true);

        colTransactionId.setCellValueFactory(new PropertyValueFactory<>("idEntity")
        );
        colMedicineTransaction.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colMedicineTransaction.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colMedicineTransaction.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setMedicineId(t.getNewValue())
        );
        colClientCard.setCellValueFactory(new PropertyValueFactory<>("clientCardNumber"));
        colClientCard.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colClientCard.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setClientCardNumber(t.getNewValue())
        );
        colMedicineQuantityTransaction.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colMedicineQuantityTransaction.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colMedicineQuantityTransaction.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setQuantity(t.getNewValue())
        );
        colTransactionDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colTransactionDateTime.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateTimeStringConverter()));
        colTransactionDateTime.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setDateTime(t.getNewValue())
        );
        tblTransaction.setItems(transactionsObservableList);
    }

    public void onUndoTransactionOperation() {
        try {
            transactionService.undoTransactionOperation();

            transactionsObservableList.clear();
            transactionsObservableList.addAll(transactionService.getTransactions());

            tblTransaction.refresh();
        } catch (RuntimeException rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void onRedoTransactionOperation() {
        try {
            transactionService.redoTransactionOperation();

            transactionsObservableList.clear();
            transactionsObservableList.addAll(transactionService.getTransactions());

            tblTransaction.refresh();
        } catch (RuntimeException rex) {
            Common.showValidationError(rex.getMessage());
        }
    }

    public void initializeTableOrderClientCardsByTransactionsCount() {
        colClientCardOrderByTransactions.setCellValueFactory(new PropertyValueFactory<>("clientCardNumber"));
        colNumberTransactionsOrderByTransactions.setCellValueFactory(new PropertyValueFactory<>("numberOfTransactions"));
    }

    public void onOrderClientCardsByTransactions(ActionEvent actionEvent) {
        clientCardsByNumberOfTransactionsObservableList.clear();
        clientCardsByNumberOfTransactionsObservableList.addAll(transactionService.getClientCardNumberOrderedByNumberOfTransactions());
        tblOrderClientCardsByTransactions.setItems(clientCardsByNumberOfTransactionsObservableList);
        tblOrderClientCardsByTransactions.refresh();
    }
}
