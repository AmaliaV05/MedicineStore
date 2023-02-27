package service;

import domain.*;
import repository.IRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyException;
import java.util.*;

public class MedicineService {
    private final IRepository<Medicine> medicineIRepository;
    private final IRepository<Transaction> transactionRepository;
    private final MedicineValidator medicineValidator;
    private final UndoRedoService undoRedoService;
    private Medicine existingMedicine;

    public MedicineService(IRepository<Medicine> medicineIRepository, IRepository<Transaction> transactionRepository, MedicineValidator medicineValidator, UndoRedoService undoRedoService) {
        this.medicineIRepository = medicineIRepository;
        this.transactionRepository = transactionRepository;
        this.medicineValidator = medicineValidator;
        this.undoRedoService = undoRedoService;
    }

    public void addMedicine(String name, String producer, BigDecimal price, boolean isOTC, int stock) throws KeyException, IOException {
        if (!checkMedicineExists(name, producer)) {
            Medicine medicineToAdd = new Medicine(name, producer, price, isOTC, stock);
            this.medicineValidator.validateMedicine(medicineToAdd);
            this.medicineIRepository.create(medicineToAdd);
            if (undoRedoService != null)
                this.undoRedoService.addToUndo(new AddOperation<>(this.medicineIRepository, medicineToAdd));
        }
        Medicine oldValue = this.medicineIRepository.readOne(existingMedicine.getIdEntity());
        int newStock = this.existingMedicine.getStock() + stock;
        this.existingMedicine.setStock(newStock);
        this.medicineValidator.validateMedicine(existingMedicine);
        this.medicineIRepository.update(existingMedicine);
        if (undoRedoService != null)
            this.undoRedoService.addToUndo(new UpdateOperation<>(this.medicineIRepository, oldValue, this.existingMedicine));
    }

    public void updateMedicine(int id, String name, String producer, BigDecimal price, boolean isOTC, int stock) throws Exception {
        Medicine oldValue = this.getMedicine(id);
        Medicine medicine = new Medicine(id, name, producer, price, isOTC, stock);
        this.medicineValidator.validateMedicine(medicine);
        this.medicineIRepository.update(medicine);
        if (undoRedoService != null)
            this.undoRedoService.addToUndo(new UpdateOperation<>(this.medicineIRepository, oldValue, medicine));
    }

    public void deleteMedicine(int id) throws Exception {
        Medicine medicine = this.getMedicine(id);
        this.medicineIRepository.delete(medicine.getIdEntity());
        if (undoRedoService != null)
            this.undoRedoService.addToUndo(new DeleteOperation<>(this.medicineIRepository, medicine));
    }

    public List<Medicine> getMedicines() { return this.medicineIRepository.read(); }

    public Medicine getMedicine(int id) {
        Medicine medicine = this.medicineIRepository.readOne(id);
        if (medicine == null) {
            throw new IdNotFoundException("Medicine id does not exist!");
        }
        return medicine;
    }

    public void undoMedicineOperation() { this.undoRedoService.undo(); }

    public void redoMedicineOperation() { this.undoRedoService.redo(); }

    public List<MedicinesWithNumberOfTransactions> getMedicinesOrderedByNumberOfTransactions() {
        Map<Integer, Integer> medicinesWithNumberOfTransactions = new HashMap<>();
        for (Transaction t : this.transactionRepository.read()) {
            int medicineId = t.getMedicineId();
            if(!medicinesWithNumberOfTransactions.containsKey(medicineId)) {
                medicinesWithNumberOfTransactions.put(medicineId, 1);
            } else {
                medicinesWithNumberOfTransactions.put(medicineId, medicinesWithNumberOfTransactions.get(medicineId) + 1);
            }
        }
        List<MedicinesWithNumberOfTransactions> allMedicines = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : medicinesWithNumberOfTransactions.entrySet()) {
            int medicineId = entry.getKey();
            int numberOfTransactions = entry.getValue();

            Medicine medicine = this.medicineIRepository.readOne(medicineId);
            allMedicines.add(new MedicinesWithNumberOfTransactions(medicineId, medicine.getName(), numberOfTransactions));
        }
        allMedicines.sort(Comparator.comparing(MedicinesWithNumberOfTransactions::getNumberOfTransactions).reversed());
        return allMedicines;
    }

    public void increasePrice(BigDecimal percentageToIncrease, BigDecimal minimumMedicinePriceValue) throws KeyException, IOException {
        for(Medicine medicine : this.medicineIRepository.read()) {
            if(medicine.getPrice().compareTo(minimumMedicinePriceValue) <= 0) {
                BigDecimal amount = percentageToIncrease.divide(new BigDecimal(100), 2, RoundingMode.UNNECESSARY).multiply(medicine.getPrice());
                BigDecimal newPrice = medicine.getPrice().add(amount).setScale(2, RoundingMode.HALF_UP);
                medicine.setPrice(newPrice);
                this.medicineIRepository.update(medicine);
            }
        }
    }

    private boolean checkMedicineExists(String medicineName, String producer) {
        List<Medicine> medicines = this.getMedicines();
        for (Medicine medicine : medicines) {
            if (medicine.getName().equalsIgnoreCase(medicineName) &&
                    medicine.getProducer().equalsIgnoreCase(producer)) {
                this.existingMedicine = medicine;
                return true;
            }
        }
        return false;
    }
}
