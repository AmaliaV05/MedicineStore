package service;

import domain.*;
import repository.IRepository;

import java.io.IOException;
import java.security.KeyException;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionService {
    private final IRepository<Transaction> transactionRepository;
    private final IRepository<Medicine> medicineIRepository;
    private final TransactionValidator transactionValidator;

    public TransactionService(IRepository<Transaction> transactionRepository, IRepository<Medicine> medicineIRepository, TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.medicineIRepository = medicineIRepository;
        this.transactionValidator = transactionValidator;
    }

    public void addTransaction(int clientCardNumber, int quantity, LocalDateTime dateTime, int medicineId) throws TransactionException, KeyException, IOException {
        Transaction transactionToAdd = new Transaction(clientCardNumber, quantity, dateTime, medicineId);
        this.checkMedicineExists(medicineId);
        this.transactionValidator.validateTransaction(transactionToAdd);
        this.transactionRepository.create(transactionToAdd);
    }

    public void updateTransaction(int id, int clientCardNumber, int quantity, LocalDateTime dateTime, int medicineId) throws IdNotFoundException, KeyException, IOException {
        Transaction oldValue = getTransaction(id);
        Transaction transactionToUpdate = new Transaction(id, clientCardNumber, quantity, dateTime,  medicineId);
        this.checkMedicineExists(medicineId);
        this.transactionValidator.validateTransaction(transactionToUpdate);
        this.transactionRepository.update(transactionToUpdate);
    }

    public void deleteTransaction(int transactionId) throws IdNotFoundException, KeyException, IOException {
        Transaction transaction = getTransaction(transactionId);
        this.transactionRepository.delete(transaction.getIdEntity());
    }

    public Transaction getTransaction(int id) {
        Transaction transaction = this.transactionRepository.readOne(id);
        if (transaction == null) {
            throw new IdNotFoundException("Transaction id does not exist!");
        }
        return transaction;
    }

    public List<Transaction> getTransactions() {
        return this.transactionRepository.read();
    }

    private void checkMedicineExists(int id) {
        Medicine medicine = this.medicineIRepository.readOne(id);
        if (medicine == null) {
            throw new IdNotFoundException("Medicine id does not exist!");
        }
    }

    public List<Entity> searchTransactions(String searchText, LocalDateTime dateTime){
        List<Entity> transactions = new ArrayList<>();

        for (Transaction transaction : this.getTransactions()) {
            if (String.valueOf(transaction.getClientCardNumber()).contains(searchText) ||
                    transaction.getDateTime().toLocalDate().isEqual(dateTime.toLocalDate())) {
                transactions.add(transactionRepository.readOne(transaction.getIdEntity()));
            }
        }
        return transactions;
    }

    public List<ClientCardNumberWithNumberOfTransactions> getClientCardNumberOrderedByNumberOfTransactions() {
        Map<Integer, Integer> clientCardNumberWithNumberOfTransactions = new HashMap<>();
        for(Transaction t : this.getTransactions()) {
            int clientNumberCard = t.getClientCardNumber();
            if(!clientCardNumberWithNumberOfTransactions.containsKey(clientNumberCard)) {
                clientCardNumberWithNumberOfTransactions.put(clientNumberCard, 1);
            } else {
                clientCardNumberWithNumberOfTransactions.put(clientNumberCard, clientCardNumberWithNumberOfTransactions.get(clientNumberCard) + 1);
            }
        }

        List<ClientCardNumberWithNumberOfTransactions> allClientNumberCards = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : clientCardNumberWithNumberOfTransactions.entrySet()) {
            int clientCardNumber = entry.getKey();
            int numberOfTransactions = entry.getValue();

            allClientNumberCards.add(new ClientCardNumberWithNumberOfTransactions(clientCardNumber, numberOfTransactions));
        }
        allClientNumberCards.sort(Comparator.comparing(ClientCardNumberWithNumberOfTransactions::getNumberOfTransactions).reversed());
        return allClientNumberCards;
    }
}
