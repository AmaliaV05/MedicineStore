package AmaliaV05;

import domain.Medicine;
import domain.MedicineValidator;
import domain.Transaction;
import domain.TransactionValidator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.IRepository;
import repository.MedicineFileRepository;
import repository.TransactionFileRepository;
import service.MedicineService;
import service.TransactionService;
import javafx.application.Application;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/medicineStore.fxml"));
        Parent root = fxmlLoader.load();

        Controller controller =  fxmlLoader.getController();

        MedicineValidator medicineValidator = new MedicineValidator();
        TransactionValidator transactionValidator = new TransactionValidator();

        IRepository<Medicine> medicineIRepository = new MedicineFileRepository("data/medicine.txt", Medicine.class);
        IRepository<Transaction> transactionIRepository = new TransactionFileRepository("data/transactions.txt", Transaction.class);

        MedicineService medicineService = new MedicineService(medicineIRepository, transactionIRepository, medicineValidator);
        TransactionService transactionService = new TransactionService(transactionIRepository, medicineIRepository, transactionValidator);

        controller.setServices(medicineService, transactionService);

        primaryStage.setTitle("Medicine Store");
        primaryStage.setScene(new Scene(root, 850, 675));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}