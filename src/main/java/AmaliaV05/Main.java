package AmaliaV05;

import domain.Medicine;
import domain.MedicineValidator;
import domain.Transaction;
import domain.TransactionValidator;
import repository.IRepository;
import repository.MedicineFileRepository;
import repository.TransactionFileRepository;
import service.MedicineService;
import service.TransactionService;

public class Main {
    public static void main(String[] args) {
        MedicineValidator medicineValidator = new MedicineValidator();
        TransactionValidator transactionValidator = new TransactionValidator();

        IRepository<Medicine> medicineIRepository = new MedicineFileRepository("data/medicine.txt", Medicine.class);
        IRepository<Transaction> transactionIRepository = new TransactionFileRepository("data/transactions.txt", Transaction.class);

        MedicineService medicineService = new MedicineService(medicineIRepository, transactionIRepository, medicineValidator);
        TransactionService transactionService = new TransactionService(transactionIRepository, medicineIRepository, transactionValidator);
    }
}