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

    public MedicineService(IRepository<Medicine> medicineIRepository, IRepository<Transaction> transactionRepository, MedicineValidator medicineValidator) {
        this.medicineIRepository = medicineIRepository;
        this.transactionRepository = transactionRepository;
        this.medicineValidator = medicineValidator;
    }

    public void addMedicine(String name, String producer, BigDecimal price, boolean isOTC, int stock) throws KeyException, IOException {
        Medicine medicineToAdd = new Medicine(name, producer, price, isOTC, stock);
        this.medicineValidator.validateMedicine(medicineToAdd);
        this.medicineIRepository.create(medicineToAdd);
    }

    public void updateMedicine(int id, String name, String producer, BigDecimal price, boolean isOTC, int stock) throws MedicineException, KeyException, IOException {
        Medicine oldValueMedicine = medicineIRepository.readOne(id);
        Medicine medicineToUpdate = new Medicine(id, name, producer, price, isOTC, stock);
        this.medicineIRepository.update(medicineToUpdate);
    }

    public void deleteMedicine(int id) throws KeyException, IOException {
        Medicine oldValue = medicineIRepository.readOne(id);
        this.medicineIRepository.delete(id);
    }

    public List<Medicine> getMedicines() { return this.medicineIRepository.read(); }

    public Medicine getMedicine(int id) throws Exception {
        return this.medicineIRepository.readOne(id);
    }

    public List<Medicine> searchMedicines(String searchText) {
        List<Medicine> medicines = new ArrayList<>();
        for (Medicine medicine : this.getMedicines()) {
            if (medicine.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    medicine.getProducer().toLowerCase().contains(searchText.toLowerCase())) {
                medicines.add(medicineIRepository.readOne(medicine.getIdEntity()));
            }
        }
        return medicines;
    }

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
                BigDecimal newPrice = medicine.getPrice().add(amount);
                medicine.setPrice(newPrice);
                this.medicineIRepository.update(medicine);
            }
        }
    }
}
