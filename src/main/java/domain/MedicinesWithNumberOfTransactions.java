package domain;

public class MedicinesWithNumberOfTransactions {
    private final int idMedicine;
    private final String medicineName;
    private final int numberOfTransactions;

    public MedicinesWithNumberOfTransactions(int idMedicine, String medicineName, int numberOfTransactions) {
        this.idMedicine = idMedicine;
        this.medicineName = medicineName;
        this.numberOfTransactions = numberOfTransactions;
    }

    public int getIdMedicine() {
        return idMedicine;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }
}
